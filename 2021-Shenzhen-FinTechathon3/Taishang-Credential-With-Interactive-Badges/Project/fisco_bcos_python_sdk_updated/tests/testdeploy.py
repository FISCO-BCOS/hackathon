'''
  @author: kentzhang
  @date: 2019-06
'''
import utils.rpc
import random
from eth_account.account import (
    Account
)
from client.bcostransactions import(
    serializable_unsigned_transaction_from_dict,
)
import rlp
import os
import time
from configobj import ConfigObj

from eth_utils.hexadecimal import encode_hex
import json
keyfile = "d:/blockchain/accounts/pyaccount.keystore"

# 从keystore打开一个公私钥对
with open(keyfile, "r") as dump_f:
    keytext = json.load(dump_f)
    privkey = Account.decrypt(keytext, "123456")
    ac2 = Account.from_key(privkey)
    print("read from file: address", ac2.address)
    print("pubkey: ", ac2.publickey)
    print("privkey: ", encode_hex(ac2.key))
    dump_f.close()

'''
    #也可以从私钥文本得到一个可用来签名的帐户对象
    ac3 = Account.from_key("255f01b066a90853a0aa18565653d7b944cd2048c03613a9ff31cb9df9e693e5")
    print("test from key")
    print("read from file: address", ac3.address)
    print("pubkey: ", ac3.publickey)
    print("privkey: ", encode_hex(ac3.key))
'''
# 从abi文件获得abi的文本定义
contractFile = r"sample\SimpleInfo.abi"
with open(contractFile, 'r') as load_f:
    contract_abi = json.load(load_f)
    load_f.close()
# 将要调用的函数和参数编码
with open(r"sample\SimpleInfo.bin", 'r') as load_f:
    functiondata = load_f.read()
    load_f.close()

print("encode_transaction_data:", functiondata)
# 填写一个bcos transaction 的 mapping
contractAddress = ""  # empty for deloy
txmap = dict()
txmap["randomid"] = random.randint(0, 1000000000)  # 测试用 todo:改为随机数
txmap["gasPrice"] = 30000000
txmap["gasLimit"] = 30000000
txmap["blockLimit"] = 501  # 测试用，todo：从链上查一下
txmap["to"] = contractAddress
txmap["value"] = 0
txmap["data"] = functiondata
txmap["fiscoChainId"] = 1
txmap["groupId"] = 1
txmap["extraData"] = ""
# txmap["chainId"]=None #chainId没用了，fiscoChainId有用
print(txmap)
# 将mapping构建一个transaction对象,非必要，用来对照的
transaction = serializable_unsigned_transaction_from_dict(txmap)
# 感受下transaction encode的原始数据
print(encode_hex(rlp.encode(transaction)))

# 实际上只需要用sign_transaction就可以获得rawTransaction的编码数据了,input :txmap,私钥
signedTxResult = Account.sign_transaction(txmap, ac2.privateKey)
print(signedTxResult)
# signedTxResult.rawTransaction是二进制的，要放到rpc接口里要encode下
print(encode_hex(signedTxResult.rawTransaction))

url = "http://127.0.0.1:8545"
rpc = utils.rpc.HTTPProvider(url)
if True:
    param = [1, encode_hex(signedTxResult.rawTransaction)]
    # 发送
    response = rpc.make_request("sendRawTransaction", param)
    print("deploy", response)
    txid = response['result']
    param = [1, txid]
    i = 0
    for i in range(0, 15):
        response = rpc.make_request("getTransactionReceipt", param)
        print("getTransactionReceipt : ", response)
        if response["result"] is None:
            time.sleep(1)
            continue
        result = response["result"]
        newaddr = result['contractAddress']
        blocknum = result['blockNumber']
        print("onblock : %d newaddr : %s " % (int(blocknum, 16), newaddr))
        contractname = os.path.splitext(os.path.basename(contractFile))[0]
        # write to file
        config = ConfigObj("sample/contract.ini", encoding='UTF8')
        if "addess" not in config:
            config['address'] = {}
        config['address'][contractname] = newaddr
        config.write()
