#这个py文件对应的合约是contracts/TestStruct.sol
#注意：TestStruct.sol 的编译器版本是solc6以上
from client.contractnote import ContractNote
from client.bcosclient import (
    BcosClient,
    BcosError
)
import os
from eth_utils import to_checksum_address, decode_hex, keccak
from client.datatype_parser import DatatypeParser
from console_utils.console_common import print_receipt_logs_and_txoutput
client = BcosClient()
info = client.getinfo()
print(info)

# 从文件加载abi定义
contractname = "TestStruct"
contractFile = "contracts\\"+contractname+".abi"
abi_parser = DatatypeParser()
abi_parser.load_abi_file(contractFile)
contract_abi = abi_parser.contract_abi
print(client.getNodeVersion())

#如合约未部署，用python console.py deploy TestStruct 部署一次
#或者自行定位链上既有的合约地址
#address = "0x901250d3fcb6cf282134b12acdd0f1d67f265566"
address = ContractNote.get_last(client.get_full_name(),contractname)
print(address)

res = client.call(address,contract_abi,"getUser",["alice"])
print ("call:",res)

# User结构体参数示例。合约接口里的结构体，对应python的tuple数据类型
# 注：调用合约时的入参一定是个python数组，因为合约接口参数可能有一到多个
args=[("zero",78)]
res = client.sendRawTransactionGetReceipt(address,contract_abi,"addUser",args)
#print("addUser",res)
print_receipt_logs_and_txoutput(client,res,contractname)
res = client.call(address,contract_abi,"getUser",["zero"])
print ("call:",res)

#对应第一个参数User[] memory _users，这个参数本身是个数组，所以,_users是参数数组里的’数组‘ [[]]
args=[[("zero",78),("yark",55),("xar",23)]]
res = client.sendRawTransactionGetReceipt(address,contract_abi,"addUsers",args)
print_receipt_logs_and_txoutput(client,res,contractname)

res = client.call(address,contract_abi,"getUser",["yark"])
print ("call:",res)

print("address is",address)
b = keccak(bytes(address, "utf-8"))
print(b)
args = ["name123",b]
res = client.sendRawTransactionGetReceipt(address,contract_abi,"putbytes",args)
print_receipt_logs_and_txoutput(client,res,contractname)

