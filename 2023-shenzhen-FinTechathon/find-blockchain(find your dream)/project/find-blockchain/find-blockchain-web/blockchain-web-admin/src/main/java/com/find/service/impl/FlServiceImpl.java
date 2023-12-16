package com.find.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.find.NodeSecurService;
import com.find.component.JavaWebSocketClient;
import com.find.component.RabbitMqReceiver;
import com.find.component.WebSocketServer;
import com.find.dao.AccountDao;
import com.find.dao.Result;
import com.find.pojo.*;
import com.find.component.RabbitMqSender;
import com.find.service.FlService;
import com.find.service.WebSocketService;
import com.find.util.Constant;
import com.find.util.RedisData;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.tomcat.jni.Time;
import org.java_websocket.enums.ReadyState;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.find.util.Constant.*;
import static java.lang.Thread.sleep;


@Service
public class FlServiceImpl implements FlService {

    private static Integer workerNum = 0;

    @Autowired
    RabbitMqSender rabbitMqSender; // rabbitMQ 发送服务

    @Autowired
    FlNodeContext flNodeContext; // 节点上下文

    @Autowired
    OnlineNodeContext onlineNodeContext; // 节点上下文

    @Autowired
    FlOnlineNodeContext CarFlNodeContext; // 节点上下文

    @Autowired
    WebSocketService webSocketService;

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Reference(mock = "com.find.service.impl.MockNodeSecurServiceImpl",loadbalance = "roundrobin",cluster = "failfast")
    NodeSecurService nodeSecurService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AccountDao accountDao;

    //聚类后的用于保存车辆数据的双层列表
    private ArrayList<ArrayList<SharedNode>> cluster;

    //维护一个候选车辆共享节点列表
    public ArrayList<SharedNode> candidateList;

    //维护一个参与训练的共享节点列表
    public ArrayList<String> AllCarNodeIdList = new ArrayList<>();

    private LinkedList<String> receiveDocIdList = new LinkedList<>(); // 接收文档id缓存列表

    //从[minVal, topVal]范围中选出cnt个不重复的数据，sendModel2Cli函数会用到
    public static List<Integer> genUniqueRandomVal(int minVal, int topVal, int cnt){
        List<Integer> mBaseList = new ArrayList<Integer>();
        int index;
        int size = topVal-minVal+1;
        if (minVal >= topVal){
            return null;
        }
        if (cnt>size){
            return null;
        }

        for (int i = minVal; i <= topVal; i++){
            mBaseList.add(i);
        }

        List<Integer> uniqueValList = new ArrayList<Integer>();//无重复的数据集合
        Random random = new Random();
        for (; cnt > 0;){
            index = random.nextInt(size);//范围[0, size)
            uniqueValList.add(mBaseList.get(index));//添加到数据集合
            mBaseList.remove(index);//基本数据集合移除已经加到uniqueValList的数据，这样子就不会重复
            cnt--;
            size--;
        }
        return uniqueValList;
    }

    @Override
    public void sendModel2Cli(String message) throws  IOException {

        FlNode flNode = JSONObject.parseObject(message, FlNode.class);
        if (flNode.getTimes().equals("1")){
            System.out.println("清空");
            flNodeContext.removeFlNodeContext();
        }
        //为了让服务端聚合的次数与round一致
        if(RabbitMqReceiver.round + 1 == Integer.parseInt(flNode.getTimes())){
            flNode.setState(FlNode.State.DONE);
            flNode.setTimes(String.valueOf(RabbitMqReceiver.round));
            flNodeContext.putNode(flNode.getId(), flNode);
            try {
                webSocketService.nodeInCall("in");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        flNode.setState(FlNode.State.WAITING);
//        System.out.println("cliName is : "+flNode.getId());
        flNodeContext.putNode(flNode.getId(), flNode);

        List<Integer> idsList = genUniqueRandomVal(1, 5, workerNum);
        System.out.println("idsList:" + idsList);
        for (int i = 0; i < workerNum; i++) {
            ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 6, 3,
                    TimeUnit.SECONDS, new ArrayBlockingQueue<>(3), new ThreadPoolExecutor.DiscardPolicy());
            final int flag = i;
            poolExecutor.execute(() -> {
                System.out.println("进入线程池");
            String uri = "";
            Integer index = idsList.get(flag);
            System.out.println(onlineNodeContext.getAllNode());
            if (index < 10 && onlineNodeContext.getAllNode().contains("client" + index)){
                System.out.println("index" + index);
                uri = "ws://10.23.13.236:800" + index +"/train";
            } else if(onlineNodeContext.getAllNode().contains(String.valueOf(index))){
                System.out.println("index" + index);
                uri = "ws://10.23.13.236:80" + index +"/train";
            }
            try {
                JavaWebSocketClient myClient = new JavaWebSocketClient(new URI(uri));
                System.out.println(uri);
                myClient.connect();
                while (!myClient.getReadyState().equals(ReadyState.OPEN)) {
                    System.out.println("连接中...");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    index++;
                }
                    // 连接成功往websocket服务端发送数据
//                JSONObject object = new JSONObject();
//                object.put("message", "success连接");
//                System.out.println("message:" + message);
                myClient.send(flNode.getHashcode());
            } catch (Exception e) {
                e.printStackTrace();
            }
            });
        }
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //发送模型到联邦学习服务端
        while (Constant.TrainList.size() != workerNum){
            //消息发到mq队列
//            if(!Constant.CLIENT_DATA_LIST.isEmpty()){
//                for (String s : Constant.CLIENT_DATA_LIST) {
//                    rabbitMqSender.sendUploadClientMsg(s);
//                }
//                Constant.CLIENT_DATA_LIST.clear();
//            }
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //消息发到mq队列
//        if(!Constant.CLIENT_DATA_LIST.isEmpty()){
//            for (String s : Constant.CLIENT_DATA_LIST) {
//                rabbitMqSender.sendUploadClientMsg(s);
//            }
//            Constant.CLIENT_DATA_LIST.clear();
//        }
//        poolExecutor.shutdown();
        //todo:为了测试注释掉了
//        sendModel2Server(Constant.TrainList);
        //下面185到190是用mq对应的代码
//            rabbitMqSender.sendDownloadMsg(flNode.getHashcode());
        try {
            webSocketService.nodeInCall("in");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //执行联邦学习任务
    @Override
    public void sendModel2Cli2(String message) throws IOException {

        //将python服务端发来的信息上链
        nodeSecurService.Upload_data(message);
        //聚合服务器的信息
        SharedNode sharedNode = JSONObject.parseObject(message, SharedNode.class);
        System.out.println("message:" + message);
        if (sharedNode.getTimes().equals("0")){
            //挑选训练车辆，将参与到FL的车辆放在一个列表中
            AllCarNodeIdList.clear();
            //报酬低于某个阈值,系统随机选
            if(REWARD <= 5){
                for (int i = 0; i < workerNum; i++) {
                    String nodeId = onlineNodeContext.getAllNode().get(i);
                    AllCarNodeIdList.add(nodeId);
                    accountDao.modifySceneById(nodeId,"FL");
                }
            }
            System.out.println("AllCarNodeIdList:" + AllCarNodeIdList);
        }
        //等到节点数量够了,再发到客户端去训练
        while (AllCarNodeIdList.size() != workerNum){
            Time.sleep(100);
        }
        //最后一轮python服务端聚合后的信息更新
        if(RabbitMqReceiver.round == Integer.parseInt(sharedNode.getTimes())){
            //更新聚合服务端的状态信息
            sharedNode.setState(SharedNode.State.DONE);
            sharedNode.setTimes(String.valueOf(RabbitMqReceiver.round));
            //更新参与训练的节点的任务状态:FL->NONE
            for (String nodeId : AllCarNodeIdList) {
                accountDao.modifySceneById(nodeId,"NONE");
            }
            CarFlNodeContext.putNode(sharedNode.getNodeId(), sharedNode);
            //执行插入用户发起任务信息到数据库
            Date date = new Date();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
            accountDao.insertTrainData(sharedNode.getNodeId(), sharedNode.getTimes(),"user1",dateFormat.format(date),REWARD,sharedNode.getAccuracy());
            accountDao.insertUserTaskInfo("user1",sharedNode.getTimes(), dateFormat.format(date),REWARD,sharedNode.getAccuracy(),workerNum);
            //todo:更新在线安全列表
            //执行SQL——“获取在线且安全的节点返回节点列表”

            try {
                webSocketService.nodeInCall("in");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        //更新聚合服务器的状态
        sharedNode.setState(SharedNode.State.WAITING);
        CarFlNodeContext.putNode(sharedNode.getNodeId(), sharedNode);
        //将聚合服务器的训练情况保存到数据库
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        accountDao.insertTrainData(sharedNode.getNodeId(), sharedNode.getTimes(),"user1",dateFormat.format(date),REWARD,sharedNode.getAccuracy());

        //初始化线程池，与车辆发起通信,发布模型给车辆客户端（取前workerNum个客户端）
        ThreadPoolExecutor poolExecutor = null;
        for (int i = 0; i < workerNum; i++) {
            final int flag = i;
            poolExecutor = new ThreadPoolExecutor(5, 6, 3,
                    TimeUnit.SECONDS, new ArrayBlockingQueue<>(3), new ThreadPoolExecutor.DiscardPolicy());
            poolExecutor.execute(() -> {
                System.out.println("进入线程池");
                //异步方式——发送训练任务给客户端
                webSocketServer.sendInfo(AllCarNodeIdList.get(flag),sharedNode.getDocId());
            });
        }

        System.out.println("将模型发送到了客户端");

        //关闭线程池
        if (poolExecutor!=null){
            poolExecutor.shutdown();
        }

        try {
            webSocketService.nodeInCall("in");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void queryOnlineNode() {
        System.out.println("进入查询");
        //将onlineNodeContext的数据清空
        onlineNodeContext.removeAllNode();
        //调用websocketServer的getUser函数
        List<OnlineNode> nodeList = webSocketServer.getUser();
        //nodeList列表为空,直接返回
        if(nodeList.isEmpty()){
            return;
        }
        for (OnlineNode onlineNode : nodeList) {
            //根据nodeId查onlineNode
            OnlineNode node = accountDao.getOnlineNodeById(onlineNode.getNodeId());
            if(node != null){
                //根据数据库查询的信息设置信誉值和所属的域
                onlineNode.setRepValue(node.getRepValue());
                onlineNode.setIp(node.getIp());
                onlineNode.setLon(node.getLon());
                onlineNode.setLat(node.getLat());
            }
            onlineNodeContext.putNode(onlineNode.getNodeId(), onlineNode);
        }
        //刷新网页
        try {
            webSocketService.nodeInCall("in");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 6, 3,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(3), new ThreadPoolExecutor.DiscardPolicy());
//        //需要发起请求的节点数量
////        int nums = accountDao.getAllOnlineNode().size();
//        //todo:num数量需要修改下
//        int nums = 5;
//        String uri = "";
//        for (int i = 1; i < nums; i++) {
//            if (i < 10){
//                uri = "ws://10.23.13.236:800" + i +"/online";
//            }else {
//                uri = "ws://10.23.13.236:80" + i +"/online";
//            }
//            try {
//                JavaWebSocketClient myClient = new JavaWebSocketClient(new URI(uri));
//                poolExecutor.execute(() -> {
//                    myClient.connect();
////            WebSocket webSocket = new WebSocketImpl();
//                    int index = 0;
//                    while (!myClient.getReadyState().equals(ReadyState.OPEN)) {
//                        System.out.println("连接中...");
//                        try {
//                            sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        index++;
//                        if (index > 3) {
//                            break;
//                        }
//                    }
//                    // 连接成功往websocket服务端发送数据
//                    JSONObject object = new JSONObject();
//                    object.put("message", "success连接");
//                    myClient.send(object.toJSONString());
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        //到时这个try catch看看能不能删除了
//        try {
//            //预留在线的客户端都发回验证其在线的数据的处理时间
//            sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        countOnlineNode(MessageList);
//        poolExecutor.shutdown();
////        //如果确定好终端的数量，就可以采用下面的if
////        if (MessageList.size() == workerNum){
////            countOnlineNode(MessageList);
////            poolExecutor.shutdown();
////        }
    }

    @Override
    public void startTraining(Integer setWorkerNum, Integer rounds,Double reward){
        //初始化CarFlNodeContext
        CarFlNodeContext.removeAllNode();
        workerNum = setWorkerNum;
        REWARD = reward;

        //报酬超过5
        if(REWARD > 5){
            //将任务显示到前端页面,让节点进行抢占,这里就不模拟前端了
            //将需要参与训练的节点数保存到redis,让节点去抢占
            stringRedisTemplate.opsForValue().set(LOCK_WORKER_NUM,workerNum.toString());
        }
        //发到mq对应交换机
        rabbitMqSender.sendStartTraining(rounds);
    }

    //客户端完成训练，处理数据并发给服务端
    @Override
    public void sendModel2Server(List<String> TrainList) {
        //测试是否进入该函数
        System.out.println("进入函数：sendModel2Server");
        System.out.println("TrainList" + TrainList);
        //处理训练完的数据
        //等待来自客户端的消息收齐
        while (TrainList.size() != workerNum){
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (String message : TrainList) {
            try {
                //解析终端上传的数据到实体类
                SharedNode sharedNode = JSONObject.parseObject(message, SharedNode.class);
                //设置共享节点车道的值
                sharedNode.setState(SharedNode.State.WAITING);
                //根据训练精度更改信誉值和收益，todo:收益还没做
                //更新的算法，训练精度高于96就提升信誉值，低于92就调低信誉值
                if (sharedNode.getAccuracy() >= 96){
                    //根据id查询信誉值
                    Double repValue = accountDao.getRepValueById(sharedNode.getNodeId());
                    //更新信誉值
                    if (repValue != null){
                        repValue = (10 - repValue) * 0.1 + repValue;
                        //修改数据库中对应节点的信誉值
                        accountDao.modifyRepValueById(sharedNode.getNodeId(), repValue);
                        //节点信誉值大于等于8，如果没有pem文件，则生成pem文件
                        if (repValue >= 8){
                            File file = new File("G:\\findlab-subq\\个人项目\\find-blockchain\\find-blockchain-web\\pemfile\\node" + sharedNode.getNodeId().substring(6) + ".pem");
                            if (!file.exists()){
                                //账户文件不存在，则创建账户
                                nodeSecurService.createNodeAccount(sharedNode.getNodeId());
                                //修改该节点在在线节点列表和数据库中的安全信息为可信
                                sharedNode.setSecurity("不安全");
                                CarFlNodeContext.putNode(sharedNode.getNodeId(),sharedNode);
                                accountDao.UpdateSecurityById(sharedNode.getNodeId(),"安全");
                            }
                        }
                    }
                }else if(sharedNode.getAccuracy() <= 92){
                    //根据id查询信誉值
                    Double repValue = accountDao.getRepValueById(sharedNode.getNodeId());
                    if (repValue != null){
                        //更新信誉值
                        repValue = Double.valueOf(String.format("%.2f",repValue * 0.9f));
                        //修改数据库中对应节点的信誉值
                        accountDao.modifyRepValueById(sharedNode.getNodeId(), repValue);
                        //节点信誉值小于4，如果有pem文件，则删除pem文件
                        if (repValue < 4){
                            File priFile = new File("G:\\findlab-subq\\个人项目\\find-blockchain\\find-blockchain-web\\pemfile\\node" + sharedNode.getNodeId().substring(6) + ".pem");
                            File pubFile = new File("G:\\findlab-subq\\个人项目\\find-blockchain\\find-blockchain-web\\pemfile\\node" + sharedNode.getNodeId().substring(6) + ".pem.pub");
                            if (priFile.exists()){
                                //账户文件存在，则删除账户公私钥文件
                                boolean deleted = priFile.delete();
                                boolean deleted2 = pubFile.delete();
                                //accountDao.modifyAddressById(sharedNode.getNodeId(), null);
                                if (deleted && deleted2) {
                                    System.out.println("文件删除成功！");
                                    //修改该节点在在线节点列表和数据库中的安全信息为不可信
                                    sharedNode.setSecurity("不安全");
                                    accountDao.UpdateSecurityById(sharedNode.getNodeId(),"不安全");
                                    CarFlNodeContext.putNode(sharedNode.getNodeId(),sharedNode);
                                } else {
                                    System.out.println("文件删除失败！");
                                }
                            }
                        }
                    }
                }
                String cliName = sharedNode.getNodeId();
                receiveDocIdList.add(sharedNode.getDocId());
                CarFlNodeContext.putNode(cliName, sharedNode);

                if (receiveDocIdList.size() == workerNum) { // 节点数量到达阈值
                    StringBuilder sb = new StringBuilder();
                    for (String docId : receiveDocIdList) {
                        sb.append(docId + " ");
                    }
                    rabbitMqSender.sendUploadMsg(sb.substring(0, sb.length() - 1));
                    SharedNode server = CarFlNodeContext.getNode("server0");
                    if (server != null)
                        server.setState(SharedNode.State.GROUPING);
                    JSONArray jsonNodeList = CarFlNodeContext.getJsonNodeList(1, 10);
                    System.out.println(jsonNodeList.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //清空列表的数据
        TrainList.clear();
        receiveDocIdList.clear();
    }

    @Override
    public List<String> querySecurity(List<String> nodeIdList) {

        if (nodeIdList.isEmpty()){
            nodeIdList = onlineNodeContext.getAllNode();
            System.out.println("获取所有在线节点:" + nodeIdList.toString());
        }
//        System.out.println("nodeIdList:" + nodeIdList.toString());
        List<String> securityNodeId = nodeSecurService.validation(nodeIdList);
        return securityNodeId;
    }

    //更新安全节点列表定时任务
    public String updateSecurityNode(){

        //查询数据库中状态在线且可信值为安全的节点列表并更新
        return "1";
    }

    //查询安全节点列表——redis
    @Override
    public String querySecurityNode(){
        List<String> nodeIdList = onlineNodeContext.getAllNode();
        System.out.println("获取所有在线节点:" + nodeIdList.toString());

        //在线列表为空，则返回空字符串
        if(nodeIdList.isEmpty()){
            return "";
        }else{
            //列表不为空，查询redis缓存
            String result = stringRedisTemplate.opsForValue().get(SECURITY_LIST);
            //缓存不为空
            if(result != null){
                //命中，需要先把json反序列化为对象
                RedisData redisData = JSONUtil.toBean(result, RedisData.class);
                if(redisData.getExpireTime().isAfter(LocalDateTime.now())){
                    //未过期
                    return redisData.getData();
                }else{  //缓存数据过期
                    //数据已过期，需要缓存重建——解决缓存击穿问题
                    //缓存重建
                    //拼接sql查询的语句
                    //获取互斥锁
                    String lockKey = LOCK_SECURITY_KEY;
                    boolean isLock = tryLock(lockKey);
                    //判断锁是否获取成功
                    if (isLock){
                        // 6.3.成功，开启独立线程，实现缓存重建
                        CACHE_REBUILD_EXECUTOR.submit(() -> {
                            try {
                                //查询数据库
                                StringBuilder stringBuilder = new StringBuilder();
                                for (String s : nodeIdList) {
                                    stringBuilder.append("'").append(s).append("'").append(",");
                                }
                                String s = stringBuilder.substring(0, stringBuilder.length() - 1);
                                //System.out.println(s);
                                List<String> securityList = accountDao.getSecurityNode(s);
                                String jsonString = "";
                                //列表非null
                                if (!(securityList==null)) {
                                    //非null，解析json字符串并返回结果
                                    try {
                                        jsonString = new ObjectMapper().writeValueAsString(securityList);
                                        //redis缓存重建
                                        RedisData redisData1 = new RedisData();
                                        redisData1.setData(jsonString);
                                        redisData1.setExpireTime(LocalDateTime.now().plusMinutes(CACHE_LOGICAL_TTL));
                                        stringRedisTemplate.opsForValue().set(SECURITY_LIST, JSONUtil.toJsonStr(redisData1));
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }finally {
                                // 释放锁
                                unlock(lockKey);
                            }
                        });
                    }
                    //返回旧数据
                    return redisData.getData();
                }
            }else{  //缓存数据为空，即在更新数据数被删除
                //查询数据库，并更新redis缓存
                //查询数据库
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : nodeIdList) {
                    stringBuilder.append("'").append(s).append("'").append(",");
                }
                String s = stringBuilder.substring(0, stringBuilder.length() - 1);
                //System.out.println(s);
                List<String> securityList = accountDao.getSecurityNode(s);
                String jsonString = "";
                //列表非null
                if (!(securityList==null)) {
                    //非null，解析json字符串并返回结果
                    try {
                        jsonString = new ObjectMapper().writeValueAsString(securityList);
                        boolean isLock = tryLock(LOCK_SECURITY_KEY);
                        if (isLock){
                            //获取锁成功,建立缓存
                            String finalJsonString = jsonString;
                            CACHE_REBUILD_EXECUTOR.submit(() -> {
                                try {
                                    //redis缓存重建
                                    RedisData redisData1 = new RedisData();
                                    redisData1.setData(finalJsonString);
                                    redisData1.setExpireTime(LocalDateTime.now().plusMinutes(CACHE_LOGICAL_TTL));
                                    stringRedisTemplate.opsForValue().set(SECURITY_LIST, JSONUtil.toJsonStr(redisData1));
                                }catch (Exception e) {
                                    throw new RuntimeException(e);
                                }finally {
                                    // 释放锁
                                    unlock(LOCK_SECURITY_KEY);
                                }
                            });
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                return jsonString;
            }
        }
    }

    //获取互斥锁
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    //释放互斥锁
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    //根据dubbo服务方的证书查验后，返回一个节点安全列表，根据列表修改数据库中节点的可信状态
    @Override
    public void UpdateNode(List<String> nodeIdList) {
        if (nodeIdList != null){
            List<OnlineNode> allOnlineNode = accountDao.getAllOnlineNode();
            System.out.println(allOnlineNode.toString());
            for (OnlineNode node : allOnlineNode) {
                if (nodeIdList.contains(node.getNodeId())){
                    //修改可信值
                    System.out.println("进入修改sql");
                    accountDao.UpdateSecurityById(node.getNodeId(),"可信");
                }else{
                    // 使用编码后的字符串执行插入操作
                    accountDao.UpdateSecurityById(node.getNodeId(),"不可信");
                }
            }
            for (String s : onlineNodeContext.getAllNode()) {
                if (nodeIdList.contains(s)){
                    OnlineNode node = onlineNodeContext.getNode(s);
                    node.setSecurity("可信");
                    onlineNodeContext.putNode(s,node);
                }else{
                    OnlineNode node = onlineNodeContext.getNode(s);
                    node.setSecurity("不可信");
                    onlineNodeContext.putNode(s,node);
                }
            }

            //更新redis中的可信列表
            RedisData redisData1 = new RedisData();
            redisData1.setData(nodeIdList.toString());
            redisData1.setExpireTime(LocalDateTime.now().plusMinutes(CACHE_LOGICAL_TTL));
            stringRedisTemplate.opsForValue().set(SECURITY_LIST, JSONUtil.toJsonStr(redisData1));
            System.out.println(stringRedisTemplate.opsForValue().get(SECURITY_LIST));

            try {
                webSocketService.nodeInCall("in");
                System.out.println("进入nodeInCall");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //节点离线后的更新函数
    @Override
    public void offline(String userName) {
        //更新数据库中该离线节点的在线状态和任务状态(onlineState scene)
        accountDao.modifyonlineStateById(userName,"离线"," ", " ", " ");
        accountDao.modifySceneById(userName,"NONE");
        onlineNodeContext.removeNodeById(userName);
        //判断训练的列表中是否有该离线节点
        if(AllCarNodeIdList.contains(userName)){
            //将该离线节点(nodeId)从列表中移除
            AllCarNodeIdList.remove(userName);
            //查询数据库中在线且未参与到FL的节点，按照信誉值降序排序取第一个
            SharedNode newnode = accountDao.getNewNode();
            //将取到的节点添加到AllCarNodeIdList中
            AllCarNodeIdList.add(newnode.getNodeId());
            //让补充上来的节点做联邦学习
            webSocketServer.sendInfo(newnode.getNodeId(),CarFlNodeContext.getNode("server0").getDocId());
            accountDao.modifySceneById(newnode.getNodeId(),"FL");
        }
    }

    //抢占FL训练任务
    @Override
    public Result requestTask(String nodeId) {
        //初始化
        AllCarNodeIdList.clear();
        //使用Redisson分布式锁
        //避免重复下单
        RLock lock = redissonClient.getLock(nodeId);
        boolean islock = lock.tryLock();
        if(!islock){
            // 获取锁失败，直接返回失败或者重试
            return Result.fail("无法重复下单");
        }
        try{
            //查询是否已经在训练节点列表里
            if(AllCarNodeIdList.contains(nodeId)){
                return Result.fail("无法重复下单");
            }
            //获取redis中的任务锁
            RLock fl_task = redissonClient.getLock("FL_Task");
            while (!fl_task.tryLock()){
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String nums = stringRedisTemplate.opsForValue().get(LOCK_WORKER_NUM);
//            System.out.println("当前剩余的工作节点数" + nums);
            if(nums != null && Integer.parseInt(nums) > 0){
                //更新redis中的值
                stringRedisTemplate.opsForValue().set(LOCK_WORKER_NUM,String.valueOf(Integer.parseInt(nums)-1));
                AllCarNodeIdList.add(nodeId);
            }
            fl_task.unlock();
        }finally {
            lock.unlock();
        }
        System.out.println(AllCarNodeIdList);
        return Result.ok("获取训练任务成功");
    }

    /*
     * 聚类算法-函数主体:按照信誉值聚类
     * 针对一维 double 数组。指定聚类数目 k。
     * 将数据聚成 k 类。
     */
    public static ArrayList<ArrayList<SharedNode>> cluster(ArrayList<SharedNode> NodeList, int nums) {
        // 存放聚类旧的聚类中心
        double[] c = new double[nums];
        // 存放新计算的聚类中心
        double[] nc = new double[nums];
        // 存放放回结果
        ArrayList<ArrayList<SharedNode>> g;
        // 初始化聚类中心
        // 经典方法是随机选取 k 个
        // 本例中采用前 k 个作为聚类中心
        // 聚类中心的选取不影响最终结果
        List<Double> list = new ArrayList<Double>();
        int index = 0;
        for (int i = 0; i < nums; i++) {
            if (!list.contains(NodeList.get(i+index).getRepValue())){
                c[i] = NodeList.get(i+index).getRepValue();
                list.add(NodeList.get(i+index).getRepValue());
            }else{
                i--;
                index++;
            }
            System.out.println("一开始信誉值：" + NodeList.get(i).getRepValue());
            // 循环聚类，更新聚类中心
            // 到聚类中心不变为止
        }
        while (true) {
            // 根据聚类中心将元素分类
            System.out.println("一开始的c" + Arrays.toString(c));
            g = group(NodeList, c);
            // 计算分类后的聚类中心
            for (int i = 0; i < g.size(); i++) {
                nc[i] = center(g.get(i));
            }
            System.out.println("新的nc:" + Arrays.toString(nc));
            // 如果聚类中心不同
            if (!equal(nc, c)) {
                // 为下一次聚类准备
                c = nc;
                nc = new double[nums];
            } else // 聚类结束
                break;
        }
        // 返回聚类结果
        return g;
    }
    /*
     * 聚类中心函数
     * 简单的一维聚类返回其算数平均值
     * 可扩展
     */
    public static double center(ArrayList<SharedNode> p) {
        double sum = 0;
        for (int i = 0; i < p.size(); i++) {
            sum += p.get(i).getRepValue();
        }
        System.out.println("进入center，平均数为" + sum / p.size());
        return sum / p.size();
    }
    /*
     * 给定 double 型数组 p 和聚类中心 c。
     * 根据 c 将 p 中元素聚类。返回二维数组。
     * 存放各组元素。
     */
    public static ArrayList<ArrayList<SharedNode>> group(ArrayList<SharedNode> p, double[] c) {
        // 中间变量，用来分组标记
        int[] gi = new int[p.size()];
        // 考察每一个元素 pi 同聚类中心 cj 的距离
        // pi 与 cj 的距离最小则归为 j 类
        for (int i = 0; i < p.size(); i++) {
            // 存放距离
            double[] d = new double[c.length];
            // 计算到每个聚类中心的距离
            for (int j = 0; j < c.length; j++) {
                System.out.println("信誉值：" + p.get(i).getRepValue());
                System.out.println("c[j]:" + Arrays.toString(c));
                d[j] = distance(p.get(i).getRepValue(), c[j]);
            }
            System.out.println("d:" + Arrays.toString(d));
            // 找出最小距离，返回最小值的下标
            int ci = min(d);
            // 标记属于哪一组
            gi[i] = ci;
            System.out.println(Arrays.toString(gi));
        }
        // 存放分组结果
        ArrayList<ArrayList<SharedNode>> g = new ArrayList<ArrayList<SharedNode>>();
        // 遍历每个聚类中心，分组
        for (int i = 0; i < c.length; i++) {
            // 中间变量，记录聚类后每一组的大小
            //int s = 0;
            // 计算每一组的长度
            // 存储每一组的成员
            //g[i] = new double[s];
            //s = 0;
            // 根据分组标记将各元素归位
            ArrayList<SharedNode> temp = new ArrayList<SharedNode>();
            for (int j = 0; j < gi.length; j++) {
                if (gi[j] == i) {
                    //g[i][s] = p[j];
                    temp.add(p.get(j));
                }
            }
            g.add(temp);
            //System.out.println(g.toString());
        }
        // 返回分组结果
        System.out.println("g的值：" + g);
        return g;
    }
    /*
     * 计算两个点之间的距离， 这里采用最简单得一维欧氏距离， 可扩展。
     */
    public static double distance(double x, double y) {
        return Math.abs(x - y);
    }

    /*
     * 返回给定 double 数组各元素之和。
     */
    public static double sum(double[] p) {
        double sum = 0.0;
        for (int i = 0; i < p.length; i++)
            sum += p[i];
        return sum;
    }

    /*
     * 给定 double 类型数组，返回最小值得下标。
     */
    public static int min(double[] p) {
        int i = 0;
        double m = p[0];
        for (int j = 1; j < p.length; j++) {
            if (p[j] < m) {
                i = j;
                m = p[j];
            }
        }
        return i;
    }

    /*
     * 判断两个 double 数组是否相等。 长度一样且对应位置值相同返回真。
     */
    public static boolean equal(double[] a, double[] b) {
        if (a.length != b.length)
            return false;
        else {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i])
                    return false;
            }
        }
        return true;
    }
}
