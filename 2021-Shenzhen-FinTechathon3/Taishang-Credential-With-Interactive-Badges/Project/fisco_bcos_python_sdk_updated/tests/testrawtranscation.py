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
from utils.contracts import (
    encode_transaction_data,

)

from eth_abi import decode_single, decode_abi
from eth_utils.hexadecimal import decode_hex, encode_hex
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

'''
    #也可以从私钥文本得到一个可用来签名的帐户对象
    ac3 = Account.from_key("255f01b066a90853a0aa18565653d7b944cd2048c03613a9ff31cb9df9e693e5")
    print("test from key")
    print("read from file: address", ac3.address)
    print("pubkey: ", ac3.publickey)
    print("privkey: ", encode_hex(ac3.key))
'''
# 从abi文件获得abi的文本定义
with open(r"sample\AddrTableWorker.abi", 'r') as load_f:
    contract_abi = json.load(load_f)
# 将要调用的函数和参数编码
inputparams = ['abcefggg', 189, '0x7029c502b4F824d19Bd7921E9cb74Ef92392FB1b']
# 第三个参数是方法的abi，可以传入None，encode_transaction_data做了修改，支持通过方法+参数在整个abi里找到对应的方法abi来编码
functiondata = encode_transaction_data("create", contract_abi, None, inputparams)
print("encode_transaction_data:", functiondata)
# 填写一个bcos transaction 的 mapping
contractAddress = "0x7029c502b4F824d19Bd7921E9cb74Ef92392FB1b"
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
if False:
    param = [1, encode_hex(signedTxResult.rawTransaction)]
    # 发送
    response = rpc.make_channel_rpc_request("sendRawTransaction", param)
    print(response)

if True:
    # testing call
    functiondata = encode_transaction_data("select", contract_abi, None, ['abcefggg'])
    print("functiondata for call:", functiondata)
    decoderesult = decode_single("(string)", decode_hex(functiondata[10:]))
    print("testing decode: ", decoderesult)
    callmap = dict()
    callmap["data"] = functiondata
    callmap["from"] = ac2.address
    callmap["to"] = contractAddress
    callmap["value"] = 0
    print("calldata", callmap)
    param = [1, callmap]
    # 发送
    response = rpc.make_request("call", param)
    print(response)
    outputdata = response["result"]["output"]
    retabi = "(int256,uint256,address)"

    print("data:", outputdata)
    decoderesult = decode_single(retabi, decode_hex(outputdata))
    print(decoderesult)
    from utils.contracts import get_function_info
    from utils.abi import *

    fn_abi, fn_selector, fn_arguments = get_function_info(
        "select", contract_abi, None, ['string'], None,
    )
    print("fn_abi: ", fn_abi)
    print("fn_selector: ", fn_selector)
    print("fn_arguments: ", fn_arguments)
    outputs = fn_abi["outputs"]
    print("outputs:", outputs)
    fn_output_types = get_fn_abi_types_single(fn_abi, "outputs")
    print("output types str:", fn_output_types)
    decoderesult = decode_single(fn_output_types, decode_hex(outputdata))
    print(decoderesult)

    fn_output_types = get_fn_abi_types(fn_abi, "outputs")
    decoderesult = decode_abi(fn_output_types, decode_hex(outputdata))
    print("decode  by abi:", decoderesult)
    fn_input_types = get_fn_abi_types(fn_abi, "inputs")
    print(fn_input_types)
    fn_input_types = get_fn_abi_types_single(fn_abi, "inputs")
    print(fn_input_types)
