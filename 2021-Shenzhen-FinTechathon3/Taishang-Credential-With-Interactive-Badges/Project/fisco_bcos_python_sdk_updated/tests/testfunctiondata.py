'''
  @author: kentzhang
  @date: 2019-06
'''
from eth_utils import *
from utils.abi import filter_by_name
import json
from utils.abi import *
from eth_utils.hexadecimal import decode_hex
from eth_abi import encode_single, encode_abi, decode_single
from utils.contracts import (
    prepare_transaction,
    encode_transaction_data,
)
'''--------------------------------------------------------------'''
inputdata = "0x36a55494000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000003e80000000000000000000000007029c502b4f824d19bd7921e9cb74ef92392fb1b00000000000000000000000000000000000000000000000000000000000000036162630000000000000000000000000000000000000000000000000000000000"
with open(r"sample\AddrTableWorker.abi", 'r') as load_f:
    contract_abi = json.load(load_f)
print(contract_abi)
# 获取指定名字的function的abi的json
func_abi_byname = filter_by_name("create", contract_abi)
# 第一个匹配的function的inputs
print(func_abi_byname[0]['inputs'])

# 获得4字节的方法标识，
b4 = function_abi_to_4byte_selector(func_abi_byname[0])
print("b4:4byes selector", encode_hex(b4))
# 获得方法“签名”，也就是 func(参数列表)的字符串create(string,uint256,address)
funcsig = abi_to_signature(func_abi_byname[0])
print("abi_to_signature:", funcsig)
# 获得类似['string', 'uint256', 'address']的输入列表
print(get_abi_input_types(func_abi_byname[0]))

# 解析inputdata
# 获得方法selector，4个字节，8个hex字符
functionhash = inputdata[0:10]  # 10是因为带了0x
# 携带的数据
params = inputdata[10:]
print("functionhash :%s,inputs %s" % (functionhash, params))
# 转为二进制
bindata = decode_hex(params)
print("binary params ", bindata)
# decode_single要指定一个字符串的格式描述,解析输入参数
abitext = "(string,uint256,address)"
result = decode_single(abitext, bindata)
print("!!param decode result ", result)

# 将('abc', 1000, '0x7029c502b4f824d19bd7921e9cb74ef92392fb1b') 这样的数据encode一次，进行对比
# 大写：('abc', 1000, '0x7029c502b4F824d19Bd7921E9cb74Ef92392FB1b')
inputparams = ('abc', 1000, '0x7029c502b4F824d19Bd7921E9cb74Ef92392FB1b')
encoderesult = encode_single(abitext, inputparams)
print(encode_hex(encoderesult))

func_abi = func_abi_byname[0]
# encode_transaction_data 里对address的编码要大写
with open(r"sample\AddrTableWorker.abi", 'r') as load_f:
    contract_abi = json.load(load_f)
inputparams = ('abc', 1000, '0x7029c502b4F824d19Bd7921E9cb74Ef92392FB1b')
result = encode_transaction_data("create", contract_abi, None, inputparams)
print("encode_transaction_data:", result)
if result == inputdata:
    print("Success")


'''
def prepare_transaction(
        address,
        fn_identifier,
        contract_abi=None,
        fn_abi=None,
        transaction=None,
        fn_args=None,
        fn_kwargs=None):'''
'''
txpre = prepare_transaction(address = "0x7029c502b4f824d19bd7921e9cb74ef92392fb1b",
                    fn_identifier = "create",
                    contract_abi = contract_abi,
                    fn_args=('abc',1000,'0x7029c502b4F824d19Bd7921E9cb74Ef92392FB1b'),
                    transaction={'from': "0x8f62b0a6d7d928df18b1e86b8d76f7160519a9da",
                                 'gas':300000000,
                                 'gasPrice':300000000,
                                 'bocklimit':100
                                 }
                    )

print(txpre)'''
