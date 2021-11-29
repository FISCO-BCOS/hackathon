'''
  @author: kentzhang
  @date: 2019-12
'''
import utils.rpc
from client.bcostransactions import BcosTransaction
import random
from eth_utils import to_checksum_address
from eth_utils import (keccak)
from cytoolz import (
    curry,
    dissoc,
    identity,
    merge,
    partial,
    pipe,
)
import json
from client.gm_account import GM_Account
from client.signtransaction import SignTx
import rlp
from client.bcostransactions import (
    serializable_unsigned_transaction_from_dict,
)
from client.datatype_parser import DatatypeParser
from eth_account.account import (
    Account
)
from eth_utils.hexadecimal import encode_hex
from utils.contracts import (
    encode_transaction_data,
)

from eth_utils.crypto import set_crypto_type, CRYPTO_TYPE_GM, CRYPTO_TYPE_ECDSA

set_crypto_type("Gm")


data = b'\x87-\x8dP\xab\x0f"N\xf9\x111Dp17\xd0Q\t\xeeDNa\x04Da\xee\x8e\xe9]\x96\xa1<'
print("hexdata", data.hex())
data = b'7551ab5643a1690f7b128d0d1790ca6b02a2b5a34653d4311673588d0fa42789'

keyfile = "bin/account1.json"
account = GM_Account()
account.load_from_file(keyfile)
print(account.getdetail())
signer = SignTx()
signer.KeyPair = account
# 从abi文件获得abi的文本定义
parser = DatatypeParser()
abi_file = "contracts/HelloWorld.abi"
parser.load_abi_file(abi_file)
inputparam = ['test123789111']
# print(parser.contract_abi)
functiondata = encode_transaction_data("set", parser.contract_abi, None, inputparam)
result = parser.parse_transaction_input(functiondata)
print("parse tx input :", result)
print("functiondata ", functiondata)
sighash = keccak(b"set(string)")
print(sighash)

# 填写一个bcos transaction 的 mapping
contractAddress = to_checksum_address("0x565081461f6f0e1c5bf738013f11f1ca8a5b1537")
txmap = dict()
txmap["randomid"] = random.randint(0, 1000000000)  # 测试用 todo:改为随机数
txmap["gasPrice"] = 30000000
txmap["gasLimit"] = 30000000
txmap["blockLimit"] = 300  # 测试用，todo：从链上查一下
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
print("[rlpencode transaction]", encode_hex(rlp.encode(transaction)))

# 实际上只需要用sign_transaction就可以获得rawTransaction的编码数据了,input :txmap,私钥

signedTxResult = signer.sign_transaction(txmap)

#print("signedTxResult",signedTxResult )
# signedTxResult.rawTransaction是二进制的，要放到rpc接口里要encode下
print("rawTransaction encode:", encode_hex(signedTxResult.rawTransaction))


def encode_transaction(unsigned_transaction, vrs):
    (v, r, s) = vrs
    chain_naive_transaction = dissoc(unsigned_transaction.as_dict(), 'v', 'r', 's')
    signed_transaction = BcosTransaction(v=v, r=r, s=s, **chain_naive_transaction)
    return rlp.encode(signed_transaction)


data = b"Hello,World!"
b = keccak(data)
print("hello world hash : ", encode_hex(b))
url = "http://127.0.0.1:8555"
rpc = utils.rpc.HTTPProvider(url)
if True:
    param = [1, encode_hex(signedTxResult.rawTransaction)]
    # 发送
    response = rpc.make_request("sendRawTransaction", param)
    print(response)
