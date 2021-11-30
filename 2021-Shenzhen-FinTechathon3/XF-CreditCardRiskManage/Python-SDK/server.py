from flask import jsonify, request
from flask_cors import CORS
from models import app, db, User, Admin
from client.bcosclient import BcosClient
import requests
import main_multi


@app.route('/')
def hello_world():
    return 'Welcome! —— XFTankLab'

# 启动BGFL
@app.route('/api/startBGFL', methods=['GET'])
def startBGFL():
    # 调用BGFL主文件，开始进行训练
    main_multi.main()


# 获取准确率
@app.route('/api/getAccuracy', methods=['GET'])
def getAccuracy():
    accuracy_list, global_accuracy, contract_address = main_multi.get_accuracy()
    count = 0
    print(accuracy_list)
    data = {}
    for k, v in accuracy_list.items():
        data.update({"% d" % count: v})
        count += 1
    data.update({"global_accuracy": global_accuracy})
    print(data)
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)




# 获取区块链节点版本信息
@app.route('/api/getNodeVersion', methods=['POST'])
def getNodeVersion():
    info = client.getNodeVersion()
    chain_id = info["Chain Id"]
    fiscobcos_version = info["Supported Version"]
    build_time = info["Build Time"]
    build_type = info["Build Type"]

    data = {
        "chain id": chain_id,
        "FISCO-BCOS Version": fiscobcos_version,
        "Build Time": build_time,
        "Build Type": build_type
    }

    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }

    return jsonify(res)

# 获取最新块高
@app.route('/api/getBlockNumber', methods=['POST'])
def getBlockNumber():
    info = client.getBlockNumber()
    data = {
        "blocknumber": info
    }

    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)

# 获取共识节点列表
@app.route('/api/getSealerList', methods=['POST'])
def getSealerList():
    info = client.getSealerList()
    data = {
        "SealerList": info
    }

    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)

# 获取区块链节点共识状态
@app.route('/api/getConsensusStatus', methods=['POST'])
def getConsensusStatus():
    info = client.getConsensusStatus()
    connectedNodes = info[0]['connectedNodes']
    accountType = info[0]['accountType']
    consensusedBlockNumber = info[0]['consensusedBlockNumber']
    groupId = info[0]['groupId']
    highestblockHash = info[0]['highestblockHash']
    highestblockNumber = info[0]['highestblockNumber']
    nodeId = info[0]['nodeId']
    nodeNum = info[0]['nodeNum']
    node_index = info[0]['node_index']
    protocolId = info[0]['protocolId']
    sealer0 = info[0]['sealer.0']
    sealer1 = info[0]['sealer.1']
    sealer2 = info[0]['sealer.2']
    sealer3 = info[0]['sealer.3']
    toView = info[0]['toView']
    cfgErr = info[0]['cfgErr']
    currentView = info[0]['currentView']
    omitEmptyBlock = info[0]['omitEmptyBlock']
    max_faulty_leader = info[0]['max_faulty_leader']
    leaderFailed = info[0]['leaderFailed']


    data = {
        "connectedNodes": connectedNodes,
        "accountType": accountType,
        "consensusedBlockNumber": consensusedBlockNumber,
        "groupId": groupId,
        "highestblockHash": highestblockHash,
        "highestblockNumber": highestblockNumber,
        "nodeId": nodeId,
        "nodeNum": nodeNum,
        "node_index": node_index,
        "protocolId": protocolId,
        "sealer0": sealer0,
        "sealer1": sealer1,
        "sealer2": sealer2,
        "sealer3": sealer3,
        "toView": toView,
        "cfgErr": cfgErr,
        "currentView": currentView,
        "omitEmptyBlock": omitEmptyBlock,
        "max_faulty_leader": max_faulty_leader,
        "leaderFailed": leaderFailed
    }

    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 获取区块链节点同步状态
@app.route('/api/getSyncStatus', methods=['POST'])
def getSyncStatus():
    info = client.getSyncStatus()
    blockNumber = info['blockNumber']
    genesisHashgenesisHash = info['genesisHash']
    isSyncing = info['isSyncing']
    knownHighestNumber = info['knownHighestNumber']
    knownLatestHash = info['knownLatestHash']
    latestHash = info['latestHash']
    nodeId = info['nodeId']
    peers = info['peers']
    protocolId = info['protocolId']
    txPoolSize = info['txPoolSize']



    data = {
        "blockNumber": blockNumber,
        "genesisHashgenesisHash": genesisHashgenesisHash,
        "isSyncing": isSyncing,
        "knownHighestNumber": knownHighestNumber,
        "knownLatestHash": knownLatestHash,
        "latestHash": latestHash,
        "nodeId": nodeId,
        "peers": peers,
        "protocolId": protocolId,
        "txPoolSize": txPoolSize,
    }

    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 根据区块链高度获取区块哈希值
@app.route('/api/getBlockHashByNumber', methods=['POST'])
def getBlockHashByNumber():
    data = request.get_json(silent=True)
    # 前端传入区块高度
    height = data['']

    block_hash_by_number = client.getBlockHashByNumber(height)
    data = {
        "blockhashbynumber": block_hash_by_number
    }

    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)

# 指定群组的上链交易数目
@app.route('/api/getTotalTransactionCount', methods=['POST'])
def getTotalTransactionCount():
    info = client.getTotalTransactionCount()
    blockNumber = info['blockNumber']
    failedTxSum = info['failedTxSum']
    txSum = info['txSum']

    data = {
        "blockNumber": blockNumber,
        "failedTxSum": failedTxSum,
        "txSum": txSum
    }

    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 登录
@app.route('/api/user/login', methods=['POST'])
def login():
    global res
    data = request.get_json(silent=True)
    account = data['account']
    admin = Admin.query.filter_by(name=account).first()
    if admin:
        if admin.password == data['password']:
            code = 20000
            data = {
                       "account": admin.name,
                       "id": admin.id,
                       "status": "true",
                       "token": "1"
                   },
            res = {
                "code": code,
                "data": data,
                "message": "login success"
            }
    else:
        code = 20001
        res = {
            "code": code,
            "data": None,
            "message": "no"
        }
    return jsonify(res)


# 登录验证
@app.route('/api/user/info', methods=['GET'])
def getUserInfo():
    roles = [{
        "id": 1,
        "name": "administrator"
    }]
    data = {
        "roles": roles
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 申请信用卡
@app.route('/api/loan/create', methods=['POST'])
def creditCreate():
    data = request.get_json(silent=True)
    user = User()
    user.address1 = data['address1']
    user.address2 = data['address2']
    user.address3 = data['address3']
    user.birthday = data['birthday']
    user.company = data['company']
    user.company_email = data['company_email']
    user.company_type = data['company_type']
    user.company_phone = data['company_phone']
    user.contact = data['contact']
    user.contact2 = data['contact2']
    user.contact2_dep = data['contact2_dep']
    user.contact2_name = data['contact2_name']
    user.contact2_phone = data['contact2_phone']
    user.contact2_pos = data['contact2_pos']
    user.contact_name = data['contact_name']
    user.contact_phone = data['contact_phone']
    user.education = data['education']
    user.identity_card = data['identity_card']
    user.income = data['income']
    user.marriage = data['marriage']
    user.mobile_phone = data['mobile_phone']
    user.name = data['name']
    user.phone = data['phone']
    user.position = data['position']
    user.remark = data['remark']
    user.sex = data['sex']
    user.trade = data['trade']
    user.total_assets = data['total_assets']
    user.status = 0
    # 将新创建的用户添加到数据库会话中
    db.session.add(user)
    # 将数据库会话中的变动提交到数据库中, 记住, 如果不 commit, 数据库中是没有变化的.
    db.session.commit()
    res = {
        "code": 20000,
        "data": None,
        "message": "yes"
    }
    return jsonify(res)


# 查看列表
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
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 更新
@app.route('/api/loan/update', methods=['PUT'])
def updateRow():
    # 查询用户
    data = request.get_json(silent=True)
    user = User.query.filter_by(id=data['id']).first()
    user.name = data['name']
    user.phone = data['phone']
    user.address1 = data['address1']
    user.birthday = data['birthday']
    user.sex = data['sex']
    user.education = data['education']
    db.session.commit()
    data = {
        "status": "true"
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 删除
@app.route('/api/loan/delete/<int:id>', methods=['DELETE'])
def deleteRow(id):
    # 查询用户
    user = User.query.filter_by(id=id).first()
    # 删除用户
    db.session.delete(user)
    # 提交数据库会话
    db.session.commit()
    res = {
        "code": 20000,
        "message": "success"
    }
    return jsonify(res)


# 申请管理 -- 提交审核
@app.route('/api/loan/submitToApprove', methods=['POST'])
def submitToApprove():
    # 查询用户
    data = request.get_json(silent=True)
    user = User.query.filter_by(id=data['id']).first()
    user.status = 1
    db.session.commit()
    data = {
        "status": "true"
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 查看准备阶段列表
@app.route('/api/approve/ready/list', methods=['GET'])
def readyList():
    # 查询所有用户
    users_list = User.query.filter_by(status=1).all()
    user_data = []
    for user in users_list:
        user_data.append(user.to_json())
    data = {
        "data": user_data,
        "rows": len(users_list),
        "pages": int(len(users_list) / 10)
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 查看准备阶段——拒绝
@app.route('/api/approve/ready/reject', methods=['POST'])
def readyReject():
    # 查询用户
    data = request.get_json(silent=True)
    user = User.query.filter_by(id=data['id']).first()
    user.status = 0
    db.session.commit()
    data = {
        "status": "true"
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 查看准备阶段——通过
@app.route('/api/approve/ready/pass', methods=['POST'])
def readyPass():
    # 查询用户
    data = request.get_json(silent=True)
    user = User.query.filter_by(id=data['id']).first()
    user.status = 2
    db.session.commit()
    data = {
        "status": "true"
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 查看初审阶段列表
@app.route('/api/approve/first/list', methods=['GET'])
def firstList():
    # 查询所有用户
    users_list = User.query.filter_by(status=2).all()
    user_data = []
    for user in users_list:
        user_data.append(user.to_json())
    data = {
        "data": user_data,
        "rows": len(users_list),
        "pages": int(len(users_list) / 10)
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 查看初审阶段——通过
@app.route('/api/approve/first/pass', methods=['POST'])
def firstPass():
    # 查询用户
    data = request.get_json(silent=True)
    user = User.query.filter_by(id=data['id']).first()
    user.status = 3
    db.session.commit()
    data = {
        "status": "true"
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 查看初审阶段——拒绝
@app.route('/api/approve/first/reject', methods=['POST'])
def firstReject():
    # 查询用户
    data = request.get_json(silent=True)
    user = User.query.filter_by(id=data['id']).first()
    user.status = 1
    db.session.commit()
    data = {
        "status": "true"
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 查看终审阶段列表
@app.route('/api/approve/end/list', methods=['GET'])
def endList():
    # 查询所有用户
    users_list = User.query.filter_by(status=2).all()
    user_data = []
    for user in users_list:
        user_data.append(user.to_json())
    data = {
        "data": user_data,
        "rows": len(users_list),
        "pages": int(len(users_list) / 10)
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 查看终审阶段——通过
@app.route('/api/approve/end/pass', methods=['POST'])
def endPass():
    # 查询用户
    data = request.get_json(silent=True)
    user = User.query.filter_by(id=data['id']).first()
    user.status = 3
    db.session.commit()
    data = {
        "status": "true"
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 查看终审 阶段——拒绝
@app.route('/api/approve/end/reject', methods=['POST'])
def endReject():
    # 查询用户
    data = request.get_json(silent=True)
    user = User.query.filter_by(id=data['id']).first()
    user.status = 1
    db.session.commit()
    data = {
        "status": "true"
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# 金融监管列表
@app.route('/api/financial/regulation/list', methods=['GET'])
def financialRegulationList():
    # 查询所有用户
    users_list = User.query.filter_by(status=3).all()
    user_data = []
    for user in users_list:
        user_data.append(user.to_json())
    data = {
        "data": user_data,
        "rows": len(users_list),
        "pages": int(len(users_list) / 10)
    }
    res = {
        "code": 20000,
        "data": data,
        "message": "success"
    }
    return jsonify(res)


# @app.route('/getJingli', methods=['GET', 'POST'])  # /getJingli 是传递给前端的路径名
# def getJingli():
#     jinglis = Jingli.query.order_by(Jingli.id.desc()).all()
#     res = {
#         "msg": Jingli.repr(None, jinglis=jinglis)
#     }
#     return jsonify(res)


# delJingli 删除经理
# @app.route("/delJingli", methods=['GET', 'POST'])
# def delJingli():
#     data = request.get_json(silent=True)  # {'id': 1}
#     id = data['id']
#     jingli = Jingli.query.get_or_404(int(id))
#     db.session.delete(jingli)
#     db.session.commit()
#     res = {
#         "msg": 200
#     }
#     return jsonify(res)


# @app.route('/updateJingli', methods=['GET', 'POST'])
# def updateJingli():
#     data = request.get_json(silent=True)
#     # print(data)
#     id = data['id']
#     jingli = Jingli.query.get_or_404(int(id))
#     jingli.name = data['name']
#     jingli.serviceArea = data["serviceArea"]
#
#     # update
#     db.session.add(jingli)
#     db.session.commit()
#     res = {
#         "msg": 200
#     }
#     return jsonify(res)


# @app.route('/addJingli', methods=['GET', 'POST'])
# def addJingli():
#     data = request.get_json(silent=True)
#     # print(data)
#     jingli = Jingli(
#         name=data['name'],
#         serviceArea=data["serviceArea"],
#     )
#     db.session.add(jingli)
#     db.session.commit()
#
#     res = {
#         "msg": 200
#     }
#     return jsonify(res)


# @app.route('/analyze', methods=['GET', 'POST'])
# def analyze():
#     data = request.get_json(silent=True)
#     jingli = Jingli.query.get_or_404(data['id'])
#
#     # 统计各工单每月数量
#     count_guz = countGongdans(jingli.guzhangs)
#     count_fuw = countGongdans(jingli.fuwus)
#     print(count_guz, count_fuw)
#
#     # 统计故障工单每个分类比例
#     pie = {}
#     for g in jingli.guzhangs:
#         yiji = g.fenlei
#         pie[yiji] = pie.get(yiji, 0) + 1
#     pie2 = [{'name': k, 'value': round(v / len(jingli.guzhangs), 2)} for k, v in pie.items()]
#
#     # 发送信息
#     msg = {
#         'bar': {
#             'x': [str(i + 1) + '月' for i in range(12)],
#             'legend': ['故障工单', '非故障工单'],
#             'y': [count_guz, count_fuw],
#         }, 'pie': pie2
#     }
#     res = {
#         "msg": msg
#     }
#     return jsonify(res)



localurl  = "192.168.153.101:6001"
# 创建政府credential
@app.route('/weid/api/postGovern', methods=['POST'])
def postGovern():
    data = request.get_json(silent=True)
    print(data)
    credit = data['credit']
    input = {
    "functionArg": {
        "cptId": 200,
        "issuer": "did:weid:120:0xaeba745a51013438ae3b47a9b6a37296b3d933ab",
        "expirationDate": "2019-04-18T21:12:33Z",
        "claim": {
            "credit": credit
        },
    },
    "transactionArg": {
        "invokerWeId": "did:weid:120:0xaeba745a51013438ae3b47a9b6a37296b3d933ab"
    },
    "functionName": "createCredential",
    "v": "1.0.0"
}
    tmp = requests.post(localurl, json=input)
    res = tmp['respBody']
    return jsonify(res)

# # 创建公司credential
# @app.route('weid/api/invoke', methods=['POST'])
# def postCompany():
#     data = request.get_json(silent=True)
#     employment = data['employment']
#     income = data['income']
#     workage = data['work age']
#     input = {
#     "functionArg": {
#         "cptId": 203,
#         "issuer": "did:weid:120:0x8a6d3565bcf38597501f5831330ea57895de06f4",
#         "expirationDate": "2019-04-18T21:12:33Z",
#         "claim": {
#             "employment": employment,
#             "income": income,
#             "work age": workage
#         },
#     },
#     "transactionArg": {
#         "invokerWeId": "did:weid:120:0x8a6d3565bcf38597501f5831330ea57895de06f4"
#     },
#     "functionName": "createCredential",
#     "v": "1.0.0"
# }
#     tmp = requests.post("192.168.50.120:6001", json=input)
#     res = tmp['respBody']
#     return jsonify(res)
#
# # 创建其他credential
# @app.route('weid/api/invoke', methods=['POST'])
# def postOther():
#     data = request.get_json(silent=True)
#     car = data['car']
#     creditcardnumber = data['credit card number']
#     habitation = data['habitation']
#     liveage = data['live age']
#     numberofbankaccount = data['number of bank account']
#     personalphone = data['personal phone']
#     input = {
#     "functionArg": {
#         "cptId": 204,
#         "issuer": "did:weid:120:0x1dd442c918b30067e398a58370e4a136af6feaed",
#         "expirationDate": "2019-04-18T21:12:33Z",
#         "claim": {
#             "car": car,
#             "credit card number": creditcardnumber,
#             "habitation": habitation,
#             "live age": liveage,
#             "number of bank account": numberofbankaccount,
#             "personal phone": personalphone
#         },
#     },
#     "transactionArg": {
#         "invokerWeId": "did:weid:120:0x1dd442c918b30067e398a58370e4a136af6feaed"
#     },
#     "functionName": "createCredential",
#     "v": "1.0.0"
# }
#     tmp = requests.post("192.168.50.120:6001",json=input)
#     res = tmp['respBody']
#     return jsonify(res)
#
# # 政府验证credential
# @app.route('weid/api/invoke', methods=['POST'])
# def verifyGovern():
#     data = request.get_json(silent=True)
#     input = {
#     "functionArg": data,
#     "transactionArg": {
#     },
#     "functionName": "verifyCredential",
#     "v": "1.0.0"
# }
#     output = requests.post("192.168.50.120:6001", json=input)
#     data = output['respBody']
#     return jsonify(data)
#
# # 警局验证credential
# @app.route('weid/api/invoke', methods=['POST'])
# def verifyPolice():
#     data = request.get_json(silent=True)
#     input = {
#     "functionArg": data,
#     "transactionArg": {
#     },
#     "functionName": "verifyCredential",
#     "v": "1.0.0"
# }
#     output = requests.post("192.168.50.120:6001", json=input)
#     data = output['respBody']
#     return jsonify(data)
#
# # 公司验证credential
# @app.route('weid/api/invoke', methods=['POST'])
# def verifyCompany():
#     data = request.get_json(silent=True)
#     input = {
#     "functionArg": data,
#     "transactionArg": {
#     },
#     "functionName": "verifyCredential",
#     "v": "1.0.0"
# }
#     output = requests.post("192.168.50.120:6001", json=input)
#     data = output['respBody']
#     return jsonify(data)
#
# # 其他验证credential
# @app.route('weid/api/invoke', methods=['POST'])
# def verifyOther():
#     data = request.get_json(silent=True)
#     input = {
#     "functionArg": data,
#     "transactionArg": {
#     },
#     "functionName": "verifyCredential",
#     "v": "1.0.0"
# }
#     output = requests.post("192.168.50.120:6001", json=input)
#     data = output['respBody']
#     return jsonify(data)


# 启动运行
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=9000, debug=True)  # 这样子会直t接运行在本地服务器，也即是 localhost:5000
    # app.run(host='0.0.0.0', port=5000)  # 这里可通过 host 指定在公网IP上运行

