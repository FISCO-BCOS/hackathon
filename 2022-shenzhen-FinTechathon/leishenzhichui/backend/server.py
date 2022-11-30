from flask import jsonify, request
from flask_cors import CORS
from models import app, db, User, Admin
from client.bcosclient import BcosClient

from client.contractnote import ContractNote
from client.bcosclient import BcosClient
import os
import requests as httpRequests
import paramiko
import time
from eth_utils import to_checksum_address
from client.datatype_parser import DatatypeParser
from client.common.compiler import Compiler
from client_config import client_config
from client.bcoserror import BcosException, BcosError
from client.contractnote import ContractNote

CORS(app)
client = BcosClient()

# 账户管理
adminAddress = "0x0a5eefce381688a93b861c2fc392486a4abec151"
userAddress = ["0x32b1a0dd5f2bb3f9b8b3bfdf14ce3339d471d8b0", "0x0a5eefce381688a93b861c2fc392486a4abec151",
               "0x5384e833f381e9598ca9dceb1df07c7ed88836d8", "0x30fcd4c2156eaf78ee3647f49cebc63fb17a52fa",
               "0xc0f28b4d3ec925509efdf7bf30ac8b4016026c30"]
userCount = 0
nodeCount = 10

@app.route('/api/sys/nodeExport', methods=['POST'])
def nodeExport():
    data = request.get_json(force=True)
    hostname = data['hostname']
    username = data['username']
    password = data['password']
    res = []
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(
    paramiko.AutoAddPolicy())  # 自动添加策略，保存服务器的主机名和密钥信息，如果不添加，那么不再本地know_hosts文件中记录的主机将无法连接

    try:
        client.connect(hostname=hostname, port=22, username=username, password=password)  # 连接SSH服务端，以用户名和密码进行认证
        # 打开一个Channel并执行命令
        stdin, stdout, stderr = client.exec_command("mkdir -p ~/monitor && cd ~/monitor")
        print(stdout.read().decode('utf-8'))
        # 在请求的节点上安装node_exporter并启动
        stdin, stdout, stderr = client.exec_command("wget https://github.com/prometheus/node_exporter/releases/download/v0.17.0/node_exporter-0.17.0.linux-amd64.tar.gz")
        print(stdout.read().decode('utf-8'))
        time.sleep(60 * 1)
        print(stdout.read().decode('utf-8'))
        stdin, stdout, stderr = client.exec_command("tar -xvfz node_exporter-0.17.0.linux-amd64.tar.gz")
        print(stdout.read().decode('utf-8'))
        stdin, stdout, stderr = client.exec_command("./node_exporter-0.17.0.linux-amd64/node_exporter &")  # stdout 为正确输出，stderr为错误输出，同时是有1个变量有值
        print(stdout.read().decode('utf-8'))
        # 修改主节点上Prometheus的配置文件
        with open("/root/monitor/prometheus-2.7.2.linux-amd64", encoding="utf-8", mode="a") as file:
            file.write("- job_name: " + "'SyncNode{}'".format(nodeCount) + '\n' +
                       "static_config:" + '\n' +
                       '\t' + "targets: ['" + str(hostname) + ":9100']" + '\n')
    except Exception as e:
        res = {
            "code": 500,
            "message": "Failed: command execute Failed"
        }
    res = {
        "code": 200,
        "data": data,
        "message": "SUCCESS: command execute success"
    }
    return jsonify(res)

# 用户注册（需要管理员权限）
@app.route('/api/contract/registerUser', methods=['POST'])
def registerUser():
    data = request.get_json(force=True)
    userType = data['userType']
    global userCount
    global userAddress
    account = userAddress[userCount]
    userCount += 1

    result = False
    if userType == "seller":
        result = client.sendRawTransactionGetReceipt(contract_address, contract_abi, "addSeller", [to_checksum_address(account)])
    elif userType == "player":
        cpu = data["cpu"] if "cpu" in data else 0
        mem = data["mem"] if "mem" in data else 0
        stor = data["stor"] if "stor" in data else 0
        result = client.sendRawTransactionGetReceipt(contract_address, contract_abi, "addPlayer", [to_checksum_address(account), cpu, mem, stor])
    if result:
        data = {
            "account": account,
            "result": result
        }
        res = {
            "code": 200,
            "data": data,
            "message": "SUCCESS: Register user success"
        }
    else:
        res = {
            "code": 500,
            "data": None,
            "message": "FAILED: Register user failed"
        }
    return jsonify(res)

# 删除用户（需要管理员权限）
@app.route('/api/contract/removeUser', methods=['POST'])
def removeUser():
    data = request.get_json(force=True)
    userType = data['userType']
    account = data['account']

    if userType == "seller":
        result = client.sendRawTransactionGetReceipt(contract_address, contract_abi, "removeSeller", [to_checksum_address(account)])
    elif userType == "player":
        result = client.sendRawTransactionGetReceipt(contract_address, contract_abi, "removePlayer", [to_checksum_address(account)])
    if result:
        data = {
            "account": account,
            "result": result
        }
        res = {
            "code": 200,
            "data": data,
            "message": "SUCCESS: Remove user success"
        }
    else:
        res = {
            "code": 500,
            "data": None,
            "message": "FAILED: Remove user failed"
        }
    return jsonify(res)

# 修改账户余额
@app.route('/api/contract/chargeBalance', methods=['POST'])
def chargeBalance():
    data = request.get_json(force=True)
    value = data["value"]
    account = data["account"]

    result = client.sendRawTransactionGetReceipt(contract_address, contract_abi, "chargeBalance", [to_checksum_address(account), value])
    if result:
        data = {
            "account": account,
            "result": result
        }
        res = {
            "code": 200,
            "data": data,
            "message": "SUCCESS"
        }
    else:
        res = {
            "code": 500,
            "data": None,
            "message": "FAILED"
        }
    return jsonify(res)

# 修改信用分
@app.route('/api/contract/changeCredit', methods=['POST'])
def changeCredit():
    data = request.get_json(force=True)
    value = data["value"]
    account = data["account"]

    result = client.sendRawTransactionGetReceipt(contract_address, contract_abi, "changeCredit", [to_checksum_address(account), value])
    if result:
        data = {
            "account": account,
            "result": result
        }
        res = {
            "code": 200,
            "data": data,
            "message": "SUCCESS"
        }
    else:
        res = {
            "code": 500,
            "data": None,
            "message": "FAILED"
        }
    return jsonify(res)

# 添加充电桩
@app.route('/api/contract/synchronization', methods=['POST'])
def synchronization():
    data = request.get_json(force=True)
    account = data["account"]
    cpu = data["cpu"]
    mem = data["mem"]
    stor = data["stor"]
    cred = int(cpu * 1 + mem / 4 + stor / 10)

    result = client.sendRawTransactionGetReceipt(contract_address, contract_abi, "synchronization", [to_checksum_address(account), cpu, mem, stor, cred])
    if result:
        data = {
            "account": account,
            "result": result
        }
        res = {
            "code": 200,
            "data": data,
            "message": "SUCCESS"
        }
    else:
        res = {
            "code": 500,
            "data": None,
            "message": "FAILED"
        }
    return jsonify(res)

# 电力交易
@app.route('/api/contract/transaction', methods=['POST'])
def transaction():
    data = request.get_json(force=True)
    seller = data["seller"]
    player = data["player"]
    cpu = data["cpu"]
    mem = data["mem"]
    stor = data["stor"]
    cpuPrice = data["cpuPrice"]
    memPrice = data["memPrice"]
    storPrice = data["storPrice"]
    time = data["time"]
    cred = int(cpu * 1 + mem / 4 + stor / 10)
    totalPrice = int((cpu * cpuPrice + mem * memPrice + stor * storPrice) * time)

    result = client.sendRawTransactionGetReceipt(contract_address, contract_abi, "transcation", [to_checksum_address(seller), to_checksum_address(player),
                                                                         cpu, mem, stor, cred, totalPrice])
    if result:
        data = {
            "seller": seller,
            "player": player,
            "result": result
        }
        res = {
            "code": 200,
            "data": data,
            "message": "SUCCESS"
        }
    else:
        res = {
            "code": 500,
            "data": None,
            "message": "FAILED"
        }
    return jsonify(res)

# 电力交易到期
@app.route('/api/contract/expire', methods=['POST'])
def expire():
    data = request.get_json(force=True)
    seller = data["seller"]
    player = data["player"]
    cpu = data["cpu"]
    mem = data["mem"]
    stor = data["stor"]
    cred = int(cpu * 1 + mem / 4 + stor / 10)
    result = client.sendRawTransactionGetReceipt(contract_address, contract_abi, "expire", [to_checksum_address(seller), to_checksum_address(player),
                                                                    cpu, mem, stor, cred])
    if result:
        data = {
            "seller": seller,
            "player": player,
            "result": result
        }
        res = {
            "code": 200,
            "data": data,
            "message": "SUCCESS"
        }
    else:
        res = {
            "code": 500,
            "data": None,
            "message": "FAILED"
        }
    return jsonify(res)

# 查询用户身份信息
@app.route('/api/contract/getUserInfo', methods=['POST'])
def getUserInfo():
    data = request.get_json(force=True)
    userType = data["userType"]
    account = data["account"]

    result = None
    if userType == "seller":
        result = client.call(contract_address, contract_abi, "getSellerInfo", [to_checksum_address(account)])
        data = {
            "account": account,
            "result": {
                "cpuTotal": result[0],
                "memTotal": result[1],
                "storTotal": result[2],
                "cpuAvail": result[3],
                "memAvail": result[4],
                "storAvail": result[5],
            }
        }
    elif userType == "player":
        result = client.call(contract_address, contract_abi, "getPlayerInfo", [to_checksum_address(account)])
        data = {
            "account": account,
            "result": {
                "cpuSelf": result[0],
                "memSelf": result[1],
                "storSelf": result[2],
                "cpuBuy": result[3],
                "memBuy": result[4],
                "storBuy": result[5],
            }
        }
    if result:
        res = {
            "code": 200,
            "data": data,
            "message": "SUCCESS"
        }
    else:
        res = {
            "code": 500,
            "data": None,
            "message": "FAILED"
        }
    return jsonify(res)

# 查询用户账户信息
@app.route('/api/contract/getAccountInfo', methods=['POST'])
def getAccountInfo():
    data = request.get_json(force=True)
    userType = data["userType"]
    account = data["account"]

    result = None
    if userType == "seller":
        result = client.call(contract_address, contract_abi, "getAccountInfo", [to_checksum_address(account)])
    elif userType == "player":
        result = client.call(contract_address, contract_abi, "getAccountInfo", [to_checksum_address(account)])
    if result:
        data = {
            "account": account,
            "result": {
                "creditScore": result[0],
                "balance": result[1]
            }
        }
        res = {
            "code": 200,
            "data": data,
            "message": "SUCCESS"
        }
    else:
        res = {
            "code": 500,
            "data": None,
            "message": "FAILED"
        }
    return jsonify(res)


@app.route('/')
def hello_world():
    return 'Welcome! —— XFTankLab'

# 重定向WeBASE
@app.route('/api/goWebase', methods=['GET'])
def goWebase():
    data = {
        "address": "36.134.126.116:5000",
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 获取区块链群组基本信息
@app.route('/api/getGeneralInfo', methods=['GET'])
def getGeneralInfo():
    chainID = request.args.get("chainID")
    groupID = request.args.get("groupID")

    url = "http://36.133.48.250:5010/WeBASE-Data-Fetcher/group/general/" + str(chainID) + "/" + str(groupID)
    info = httpRequests.get(url)
    if info["code"] == 0:
        data = eval(info.text)["data"]
        response = {
            "code": 200,
            "data": data,
            "message": "success"
        }
    else:
        response = {
            "code": 400,
            "data": null,
            "message": "failed"
        }
    return jsonify(response)

# 获取区块链节点版本信息
@app.route('/api/getNodeVersion', methods=['GET'])
def getNodeVersion():
    info = client.getNodeVersion()
    data = {
        "chainID": info["Chain Id"],
        "version": info["Supported Version"],
        "buildTime": info["Build Time"],
        "buildType": info["Build Type"]
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 获取最新块高
@app.route('/api/getBlockNumber', methods=['GET'])
def getBlockNumber():
    info = client.getBlockNumber()
    data = {
        "blockNumber": info
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 根据区块链高度以及要获取的区块个数，获取多个区块哈希值
@app.route('/api/getBlockHashByNumber', methods=['POST'])
def getBlockHashByNumber():
    input = request.get_json(force=True)
    # 前端传入区块高度和区块个数
    height = int(input['height'])
    number = int(input['number'])
    blocksHash = []
    for i in range(height - number, height):
        blocksHash.append(client.getBlockHashByNumber(i))
    data = {
        "blockHashByNumber": blocksHash
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 根据区块高度获取区块信息
@app.route('/api/getBlockByNumber', methods=['POST'])
def getBlockByNumber():
    input = request.get_json(force=True)
    blockHight = input['blockHight']
    blockInfo = client.getBlockByNumber(blockHight)
    data = {
        "blockInfo": blockInfo
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 根据交易所属的区块高度、交易索引，获取前n条交易信息中的hash    TODO 需要postman测试一下交易信息里面是否包含hash
@app.route('/api/getTransactionByBlockNumberAndIndex', methods=['POST'])
def getTransactionByBlockNumberAndIndex():
    input = request.get_json(force=True)
    # 前端传入区块高度和区块个数
    height = input['height']
    number = input['number']
    transIndex = 1  # 都是获取区块第0条交易记录
    transHashes = []
    for blockNum in range(height - number, height):
        transHashes.append(client.getTransactionByBlockNumberAndIndex(blockNum, transIndex))
    data = {
        "transactionHash": transHashes
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 根据交易哈希获取交易信息
@app.route('/api/getTransactionByHash', methods=['POST'])
def getTransactionByHash():
    input = request.get_json(force=True)
    # 前端传入区块高度和区块个数
    transHash = input['transHash']
    transInfo = client.getTransactionByHash(transHash)
    data = {
        "transactionInfo": transInfo
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 获取共识节点列表
@app.route('/api/getSealerList', methods=['GET'])
def getSealerList():
    info = client.getSealerList()
    data = {
        "sealerList": info
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 登录
@app.route('/api/user/login', methods=['POST'])
def login():
    global response
    data = request.get_json(force=True)
    account = data['account']
    admin = Admin.query.filter_by(name=account).first()
    if admin:
        if admin.password == data['password']:
            code = 200
            data = {
                       "account": admin.name,
                       "id": admin.id,
                       "status": "true",
                       "token": "1"
                   },
            response = {
                "code": code,
                "data": data,
                "message": "login success"
            }
    else:
        code = 20001
        response = {
            "code": code,
            "data": None,
            "message": "no"
        }
    return jsonify(response)

# 登录验证
@app.route('/api/user/info', methods=['GET'])
def userInfo():
    roles = [{
        "id": 1,
        "name": "administrator"
    }]
    data = {
        "roles": roles
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 查看用户列表
@app.route('/api/loan/list', methods=['GET'])
def creditList():
    users_list = User.query.all()
    user_data = []
    for user in users_list:
        user_data.append(user.to_json())
    data = {
        "data": user_data,
        "rows": len(users_list),
        "pages": int(len(users_list) / 10)
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 更新用户
@app.route('/api/loan/update', methods=['PUT'])
def updateRow():
    # 查询用户
    data = request.get_json(force=True)
    user = User.query.filter_by(id=data['id']).first()
    user.name = data['name']
    user.phone = data['phone']
    user.addresponses1 = data['addresponses1']
    user.birthday = data['birthday']
    user.sex = data['sex']
    user.education = data['education']
    db.session.commit()
    data = {
        "status": "true"
    }
    response = {
        "code": 200,
        "data": data,
        "message": "success"
    }
    return jsonify(response)

# 删除用户
@app.route('/api/loan/delete/<int:id>', methods=['DELETE'])
def deleteRow(id):
    # 查询用户
    user = User.query.filter_by(id=id).first()
    # 删除用户
    db.session.delete(user)
    # 提交数据库会话
    db.session.commit()
    response = {
        "code": 200,
        "message": "success"
    }
    return jsonify(response)


# 启动运行
if __name__ == '__main__':
    # 编译合约
    if os.path.isfile(client_config.solc_path) or os.path.isfile(client_config.solcjs_path):
        Compiler.compile_file("contracts/CPNTrading.sol")
    # 从文件加载abi定义
    abi_file = "contracts/CPNTrading.abi"
    data_parser = DatatypeParser()
    data_parser.load_abi_file(abi_file)
    contract_abi = data_parser.contract_abi
    contract_address = "0x4fb700f036dbd8a80940a88df8d9e55705a8ac97"
    # 部署合约
    # print("\n>>Deploy:---------------------------------------------------------------------")
    # with open("contracts/CPNTrading.bin", 'r') as load_f:
    #     contract_bin = load_f.read()
    #     load_f.close()
    # result = client.deploy(contract_bin)
    # contract_address = result['contractAddress']
    # print("deploy", result)
    # print("new contract address : ", result["contractAddress"])
    # contract_name = os.path.splitext(os.path.basename(abi_file))[0]
    # memo = "tx:" + result["transactionHash"]

    app.run(host='0.0.0.0', port=8000, debug=True)  # 这样子会直t接运行在本地服务器，也即是 localhost:5000
    # app.run(host='0.0.0.0', port=5000)  # 这里可通过 host 指定在公网IP上运行

