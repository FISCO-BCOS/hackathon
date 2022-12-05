from __future__ import print_function, division
import tensorflow.compat.v1 as tf
import pandas as pd
import numpy as np
import random
import json
import os
import time
import traceback
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from multiprocessing import Manager, Process
# client是python-sdk中自带的py文件
from client.bcosclient import BcosClient
from client.datatype_parser import DatatypeParser
from client.common.compiler import Compiler
from client.bcoserror import BcosException, BcosError
from client_config import client_config

tf.disable_v2_behavior()
os.environ["CUDA_VISIBLE_DEVICES"] = ""


# 序列化和反序列化
def serialize(data):
    json_data = json.dumps(data)
    return json_data


def deserialize(json_data):
    data = json.loads(json_data)
    return data


# 切分数据集
def split_data(path, clients_num):
    # 读取数据
    data = pd.read_csv(path)
    # 拆分数据（默认测试集是0.25）
    X_train, X_test, y_train, y_test = train_test_split(
        data[["Temperature", "Humidity", "Light", "CO2", "HumidityRatio"]].values,
        data["Occupancy"].values.reshape(-1, 1),
        random_state=42)

    # one-hot 编码
    y_train = np.concatenate([1 - y_train, y_train], 1)
    y_test = np.concatenate([1 - y_test, y_test], 1)

    # 训练集划分给多个client
    X_train = np.array_split(X_train, clients_num)
    y_train = np.array_split(y_train, clients_num)
    return X_train, X_test, y_train, y_test


# 所有训练客户端数量
CLIENT_NUM = 20
# 划分的训练组数
GROUP_NUM = 4
# 领导客户端数量
GROUP_CLIENT_NUM = CLIENT_NUM/GROUP_NUM-1
# 节点角色常量   训练节点和公式节点是分开的
ROLE_TRAINER = "trainer"  # 训练节点
ROLE_COMM = "comm"  # 委员会
# 轮询的时间间隔，单位秒
QUERY_INTERVAL = 10
# 最大的执行轮次，每个客户端50轮
MAX_EPOCH = 1 * CLIENT_NUM
# 根据数据集特征和标签类设置模型
n_features = 5
n_class = 2
# 设置学习参数
batch_size = 100
learning_rate = 0.001
# 数据集划分，不仅要划分训练集和测试集，还要划分给多个客户端（用字典的方式）
X_train, X_test, y_train, y_test = split_data("./data/datatraining.txt", CLIENT_NUM)

# 编译合约并从文件加载合约abi
if os.path.isfile(client_config.solc_path) or os.path.isfile(client_config.solcjs_path):
    Compiler.compile_file("contracts/CommitteePrecompiled.sol")
abi_file = "contracts/CommitteePrecompiled.abi"
data_parser = DatatypeParser()
data_parser.load_abi_file(abi_file)
contract_abi = data_parser.contract_abi
# 分配智能合约地址
to_address = "0x0000000000000000000000000000000000005007"

# 写一个节点的工作流程
def run_one_node(node_id):
    """指定一个node id，并启动一个进程"""
    trained_epoch = -1
    node_index = int(node_id.split('_')[-1])

    # 初始化bcos客户端
    try:
        # 每个节点都是一个客户端
        client = BcosClient()
        # 为了更好模拟真实多个客户端场景，给不同的客户端分配不同的地址
        client.set_from_account_signer(node_id)
        print("{} initializing....".format(node_id))
    except Exception as e:
        client.finish()
        traceback.print_exc()

    # 训练节点训练
    def local_training():
        print("{} begin training..".format(node_id))
        try:
            # 调用智能合约获取最新模型，模型应该是个字典
            model, epoch = client.call(to_address, contract_abi, "QueryGlobalModel")
            # 什么是反序列化
            model = deserialize(model)
            # 清除默认图形堆栈并重置全局默认图形，只适用于当前线程（否则模型参数命名会不一样，在多进程中一般会使用到）
            tf.reset_default_graph()
            # 训练集样本数量
            n_samples = X_train[node_index].shape[0]
            # placeholder()函数是在神经网络构建graph的时候在模型中的占位符，此时并没有把要输入的数据传入模型，它只会分配必要的内存
            # 在每一次传minibatch时会减小开销
            x = tf.placeholder(tf.float32, [None, n_features])
            y = tf.placeholder(tf.float32, [None, n_class])
            # 获得全局模型参数W，b
            ser_W, ser_b = model['ser_W'], model['ser_b']
            # 全局模型参数初始化
            W = tf.Variable(ser_W)
            b = tf.Variable(ser_b)
            # 预测结果，用的是一个线性回归模型？？？
            pred = tf.matmul(x, W) + b
            # 定义损失函数
            loss = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=pred, labels=y))
            # 梯度下降
            # optimizer = tf.train.AdamOptimizer(learning_rate)
            optimizer = tf.train.GradientDescentOptimizer(learning_rate)
            gradient = optimizer.compute_gradients(loss)
            train_op = optimizer.apply_gradients(gradient)
            # 初始化所有变量
            init = tf.global_variables_initializer()
            # 训练模型
            with tf.Session(config=tf.ConfigProto(device_count={'cpu': 0})) as sess:
                sess.run(init)
                avg_loss = 0
                # 总轮数
                total_batch = int(n_samples / batch_size)
                for i in range(total_batch):
                    _, c = sess.run(
                        [train_op, loss],
                        feed_dict={
                            x: X_train[node_index][i * batch_size:(i + 1) * batch_size],  # 只发送当前节点当前轮次的数据
                            y: y_train[node_index][i * batch_size:(i + 1) * batch_size, :]
                        }
                    )
                    avg_loss += c / total_batch
                # 获取新的本地模型
                val_W, val_b = sess.run([W, b])
            # 评估本地模型与全局模型的差异，将差异部分作为更新模型
            delta_W = (ser_W - val_W) / learning_rate
            delta_b = (ser_b - val_b) / learning_rate
            delta_model = {'ser_W': delta_W.tolist(), 'ser_b': delta_b.tolist()}  # 模型是以字典形式组织起来的W和b列表
            meta = {'n_samples': n_samples, 'avg_cost': avg_loss}  # meta里存放样本数量和平均损失值
            update_model = {'delta_model': delta_model, 'meta': meta}  # 模型是以字典形式组织起来的W、b列表，样本数量和损失值
            update_model = serialize(update_model)
            # 上传本地模型
            receipt = client.sendRawTransactionGetReceipt(to_address, contract_abi, "UploadLocalUpdate",
                                                          [update_model, epoch])
            nonlocal trained_epoch  # nonlocal声明的变量不是局部变量,也不是全局变量,而是外部嵌套函数内的变量。（为了修改这个变量所以要声明）
            trained_epoch = epoch
        except Exception as e:
            client.finish()
            traceback.print_exc()
        return

    # 训练节点测试
    def local_testing(ser_W, ser_b):
        tf.reset_default_graph()

        x = tf.placeholder(tf.float32, [None, n_features])
        y = tf.placeholder(tf.float32, [None, n_class])

        W = tf.Variable(ser_W)
        b = tf.Variable(ser_b)
        pred = tf.matmul(x, W) + b

        correct_prediction = tf.equal(tf.argmax(pred, 1), tf.argmax(y, 1))
        accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

        # 初始化所有变量
        init = tf.global_variables_initializer()

        # 测试模型
        with tf.Session(config=tf.ConfigProto(device_count={'cpu': 0})) as sess:
            sess.run(init)
            acc = accuracy.eval({x: X_train[node_index], y: y_train[node_index]})

        return acc

    # 训练结果打分，由leader节点完成
    def local_scoring():
        try:
            res = client.call(to_address, contract_abi, "QueryAllUpdates")
            updates = res[0]
            # 如果更新列表里，则还不能开始评分
            if len(updates) == 0:
                return
            updates = deserialize(updates)
            # print("查询得得到的模型更新为：", res)
            # print("获取的模型更新个数为：% 所有的模型更新为：%".format(len(updates), updates))

            # 获取全局模型
            model, epoch = client.call(to_address, contract_abi, "QueryGlobalModel")
            model = deserialize(model)
            # print("反序列化后的模型model为：%,epoch为：%".format(model, epoch))

            print("{} begin scoring..".format(node_id))
            # print("leader node{}查询到的模型更新为：{}".format(node_index, updates))
            scores = {}
            score_num = 0
            loop_num = 0
            for trainer_id, update in updxates.items():
                loop_num += 1
                if score_num >= GROUP_CLIENT_NUM:
                    break
                # print("被打分的训练节点id：", trainer_id)
                # print("对应的模型更新为：", update)
                if (1 <= loop_num <= GROUP_CLIENT_NUM) and (group[0] == node_index):
                    update = deserialize(update)
                    delta_model, meta = update['delta_model'], update['meta']
                    ser_W = [a - learning_rate * b for a, b in
                             zip(np.array(model['ser_W']), np.array(delta_model['ser_W']))]
                    ser_b = [a - learning_rate * b for a, b in
                             zip(np.array(model['ser_b']), np.array(delta_model['ser_b']))]
                elif (GROUP_CLIENT_NUM+1 <= loop_num <= GROUP_CLIENT_NUM*2) and (
                        group[1] == node_index):
                    update = deserialize(update)
                    delta_model, meta = update['delta_model'], update['meta']
                    ser_W = [a - learning_rate * b for a, b in
                             zip(np.array(model['ser_W']), np.array(delta_model['ser_W']))]
                    ser_b = [a - learning_rate * b for a, b in
                             zip(np.array(model['ser_b']), np.array(delta_model['ser_b']))]
                elif (GROUP_CLIENT_NUM*2+1 <= loop_num <= GROUP_CLIENT_NUM*3) and (
                        group[2] == node_index):
                    update = deserialize(update)
                    delta_model, meta = update['delta_model'], update['meta']
                    ser_W = [a - learning_rate * b for a, b in
                             zip(np.array(model['ser_W']), np.array(delta_model['ser_W']))]
                    ser_b = [a - learning_rate * b for a, b in
                             zip(np.array(model['ser_b']), np.array(delta_model['ser_b']))]
                elif (GROUP_CLIENT_NUM*3+1 <= loop_num <= GROUP_CLIENT_NUM*4) and (
                        group[3] == node_index):
                    update = deserialize(update)
                    delta_model, meta = update['delta_model'], update['meta']
                    ser_W = [a - learning_rate * b for a, b in
                             zip(np.array(model['ser_W']), np.array(delta_model['ser_W']))]
                    ser_b = [a - learning_rate * b for a, b in
                             zip(np.array(model['ser_b']), np.array(delta_model['ser_b']))]
                else:
                    # print("不属于任何分组，continue")
                    continue
                # 给测试集评分
                score_num += 1
                scores[trainer_id] = np.asscalar(
                    local_testing(np.array(ser_W).astype(np.float32), np.array(ser_b).astype(np.float32)))
                # # np.asscalar将向量X转换成标量，且向量X只能为一维含单个元素的向量
                # scores[trainer_id] = np.asscalar(
                #     local_testing(np.array(ser_W).astype(np.float32), np.array(ser_b).astype(np.float32)))

            print("leader node{}打分的个数为：{}".format(node_index, score_num))
            print("具体的打分分数为：{}".format(scores))
            # 将评分上链并且得到凭据receipt
            receipt = client.sendRawTransactionGetReceipt(to_address, contract_abi, "UploadScores",
                                                          [epoch, serialize(scores)])
            # 进行本地模型聚合并上传至智能合约
            la_model = local_aggregation(updates, model, epoch)

            nonlocal trained_epoch  # nonlocal声明的变量不是局部变量,也不是全局变量,而是外部嵌套函数内的变量。（为了修改这个变量所以要声明）
            trained_epoch = epoch
        except Exception as e:
            client.finish()
            traceback.print_exc()
        return

    # TODO：如果效果不好，可以尝试像智能合约里一样按样本数量进行加权
    def local_aggregation(updates, model, epoch):
        print(node_index, "leader本地模型聚合开始...")
        agg_model = model
        agg_meta = {'avg_cost': 0, 'n_samples': 0}
        ratio_dict = {}      # 如果要采用多权重加权，则使用到比例字典
        aggregation_num = 0
        loop_num = 0
        for trainer_id, update in updates.items():
            update = deserialize(update)
            loop_num += 1
            if aggregation_num >= GROUP_CLIENT_NUM:
                break
            if (1 <= loop_num <= GROUP_CLIENT_NUM) and (group[0] == node_index):
                delta_model, meta = update['delta_model'], update['meta']
                agg_model['ser_W'] += (np.array((delta_model['ser_W']))/GROUP_CLIENT_NUM).tolist()
                agg_model['ser_b'] += (np.array((delta_model['ser_b']))/GROUP_CLIENT_NUM).tolist()
                agg_meta['avg_cost'] += meta['avg_cost']/GROUP_CLIENT_NUM
                agg_meta['n_samples'] += meta['n_samples']
                aggregation_num += 1
            elif (GROUP_CLIENT_NUM + 1 <= loop_num <= GROUP_CLIENT_NUM * 2) and (
                    group[1] == node_index):
                delta_model, meta = update['delta_model'], update['meta']
                agg_model['ser_W'] += (np.array((delta_model['ser_W']))/GROUP_CLIENT_NUM).tolist()
                agg_model['ser_b'] += (np.array((delta_model['ser_b']))/GROUP_CLIENT_NUM).tolist()
                agg_meta['avg_cost'] += meta['avg_cost']/GROUP_CLIENT_NUM
                agg_meta['n_samples'] += meta['n_samples']
                aggregation_num += 1
            elif (GROUP_CLIENT_NUM * 2 + 1 <= loop_num <= GROUP_CLIENT_NUM * 3) and (
                    group[2] == node_index):
                delta_model, meta = update['delta_model'], update['meta']
                agg_model['ser_W'] += (np.array((delta_model['ser_W'])) / GROUP_CLIENT_NUM).tolist()
                agg_model['ser_b'] += (np.array((delta_model['ser_b'])) / GROUP_CLIENT_NUM).tolist()
                agg_meta['avg_cost'] += meta['avg_cost'] / GROUP_CLIENT_NUM
                agg_meta['n_samples'] += meta['n_samples']
                aggregation_num += 1
            elif (GROUP_CLIENT_NUM * 3 + 1 <= loop_num <= GROUP_CLIENT_NUM * 4) and (
                    group[3] == node_index):
                delta_model, meta = update['delta_model'], update['meta']
                agg_model['ser_W'] += (np.array((delta_model['ser_W']))/GROUP_CLIENT_NUM).tolist()
                agg_model['ser_b'] += (np.array((delta_model['ser_b']))/GROUP_CLIENT_NUM).tolist()
                agg_meta['avg_cost'] += meta['avg_cost']/GROUP_CLIENT_NUM
                agg_meta['n_samples'] += meta['n_samples']
                aggregation_num += 1
            else:
                continue
        print("全局模型为{}".format(model['ser_W']))
        print("本地聚合模型为{}".format(agg_model['ser_W']))
        delta_W = ((np.array((model['ser_W'])) - np.array((agg_model['ser_W']))) / learning_rate).tolist()
        delta_b = ((np.array((model['ser_W'])) - np.array((agg_model['ser_W']))) / learning_rate).tolist()
        delta_model = {'ser_W': delta_W, 'ser_b': delta_b}  # 模型是以字典形式组织起来的W和b列表
        delta_meta = {'n_samples': agg_meta['n_samples'], 'avg_cost': agg_meta['avg_cost']}  # meta里存放样本数量和平均损失值
        update_model = {'delta_model': delta_model, 'meta': delta_meta}  # 模型是以字典形式组织起来的W、b列表，样本数量和损失值
        update_model = serialize(update_model)
        # 将本地聚合模型上链
        print("leader node{}聚合的个数为：{}".format(node_index, aggregation_num))
        print("node{} model:{}".format(node_index, update_model))
        receipt = client.sendRawTransactionGetReceipt(to_address, contract_abi, "UploadLocalAggregation",
                                                      [update_model, epoch])
        return agg_model

    # 这个是等待什么？？？
    def wait():
        time.sleep(random.uniform(QUERY_INTERVAL, QUERY_INTERVAL * 3))
        return

    # 主循环
    def main_loop():
        try:
            # 注册节点
            receipt = client.sendRawTransactionGetReceipt(to_address, contract_abi, "RegisterNode", [])
            print("{} registered successfully".format(node_id))

            while True:
                # 查询状态，获取节点的角色和当前全局的轮数。 怎么决定一个节点是训练节点还是公式节点的？？--文章里介绍了三种选举方式
                (role, global_epoch) = client.call(to_address, contract_abi, "QueryState")
                # print("{} role: {}, local e: {}, up_c: {}, sc_c: {}".format(node_id, role, trained_epoch,
                #                                                             update_count, score_count))
                # 终止条件，感觉可以改写成for循环
                if global_epoch > MAX_EPOCH:
                    print("主循环停止时，global_epoch, MAX_EPOCH:", global_epoch, MAX_EPOCH)
                    break
                # 这一句是干什么？？
                if global_epoch < trained_epoch:
                    print("主循环continue时，global_epoch, trained_epoch:", global_epoch, trained_epoch)
                    # print("{} skip.".format(node_id))
                    wait()  # 为什么需要wait()函数？
                    continue
                if global_epoch < trained_epoch:
                    group.clear()
                # 分角色进行训练/打分任务
                if role == ROLE_TRAINER:
                    local_training()
                if role == ROLE_COMM:
                    group.append(node_index)
                    local_scoring()
                wait()

        except Exception as e:
            client.finish()
            traceback.print_exc()

        return

    # 每个客户端从此处开始真正执行代码
    main_loop()
    # 关闭客户端
    client.finish()


# 发起人的全局测试
def run_sponsor():

    # 跑测试集
    def global_testing(ser_W, ser_b):
        tf.reset_default_graph()
        x = tf.placeholder(tf.float32, [None, n_features])
        y = tf.placeholder(tf.float32, [None, n_class])
        W = tf.Variable(ser_W)
        b = tf.Variable(ser_b)
        # 预测
        pred = tf.matmul(x, W) + b
        correct_prediction = tf.equal(tf.argmax(pred, 1), tf.argmax(y, 1))
        accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
        # 初始化所有变量
        init = tf.global_variables_initializer()
        # 训练模型
        with tf.Session() as sess:
            sess.run(init)
            acc = accuracy.eval({x: X_test, y: y_test})
        return acc

    def wait():
        time.sleep(random.uniform(QUERY_INTERVAL, QUERY_INTERVAL * 3))
        return

    def test():
        # 初始化bcos客户端
        try:
            client = BcosClient()

            while True:
                model, epoch = client.call(to_address, contract_abi, "QueryGlobalModel")
                model = deserialize(model)
                ser_W, ser_b = model['ser_W'], model['ser_b']
                nonlocal test_epoch
                print("========================epoch{}, test_epoch{}".format(epoch, test_epoch))
                if epoch > test_epoch:
                    test_acc = global_testing(ser_W, ser_b)
                    print("Epoch: {:03}, test_acc: {:.4f}" \
                          .format(test_epoch, test_acc))
                    test_epoch = epoch

                wait()

        except Exception as e:
            client.finish()
            traceback.print_exc()

    test_epoch = 0
    test()
    # 关闭客户端
    client.finish()


if __name__ == "__main__":
    # 进程池
    process_pool = []
    # 对节点进行分组[0,1,2,3,4],[5,6,7,8,9],[10,11,12,13,14],[15,16,17,18,19]
    group = []
    # 为多个客户端创建多个进程开始训练
    for i in range(CLIENT_NUM):
        node_id = 'node_{}'.format(len(process_pool))
        # 实例化进程manager
        manager = Manager()
        p = Process(target=run_one_node, args=(node_id,))
        p.start()
        process_pool.append(p)
        time.sleep(3)

    # 加入发起者的进行全局测试，p又是另外一个进程了
    p = Process(target=run_sponsor)
    p.start()
    process_pool.append(p)

    # 阻塞住主进程，让子进程都先跑完，再结束main
    for p in process_pool:
        p.join()