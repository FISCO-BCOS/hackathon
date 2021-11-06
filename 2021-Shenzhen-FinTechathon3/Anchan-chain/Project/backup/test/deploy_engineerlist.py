import sys
from time import sleep
sys.path.append("/home/anchan/anchan-chain/python-sdk")

from client.contractnote import ContractNote
from client.bcosclient import BcosClient
import os
from eth_utils import to_checksum_address
from client.datatype_parser import DatatypeParser
from client.common.compiler import Compiler
from client.bcoserror import BcosException, BcosError
from client_config import client_config
import sys
import traceback

from flask import Flask

app = Flask(__name__)

contracts = "EngineerList"
contracts_address = {}
contracts_abi = {}

# 从文件加载abi定义
if os.path.isfile(client_config.solc_path) or os.path.isfile(client_config.solcjs_path):
    print("begin compile")
    Compiler.compile_file(f"Contracts/{contracts}.sol", output_path="Contracts")

client = BcosClient()
print(client.getinfo())

# for c in contracts:
#     with open(f"Contracts/{c}.bin", 'r') as load_f:
#         contract_bin = load_f.read()
#         load_f.close()
#     result = client.deploy(contract_bin)
#     contracts_address[c] = result["contractAddress"]
#     data_parser = DatatypeParser()
#     data_parser.load_abi_file(f"Contracts/{c}.abi")
#     contracts_abi[c] = data_parser.contract_abi
#     print(f"Deployed {c} at {contracts_address[c]}")
names = ["罗*","梁*","宋*","唐*","许*","韩*","冯*","邓*","曹*","彭*",
        "曾*","萧*","田*","董*","袁*","潘*","于*","蒋*","蔡*","余*",
        "杜*","叶*","程*","苏*","魏*","吕*","丁*","任*","沈*","姚*",
        "卢*","姜*","崔*","钟*","谭*","陆*","汪*","范*","金*","石*",
        "廖*","贾*","夏*","韦*","付*","方*","白*","邹*","孟*","熊*",]

ids = [str(543216198008020000+i) for i in range(50)]

with open(f"Contracts/{contracts}.bin", 'r') as load_f:
    contract_bin = load_f.read()
    load_f.close()
result = client.deploy(contract_bin)
contracts_address[contracts] = result["contractAddress"]
data_parser = DatatypeParser()
data_parser.load_abi_file(f"Contracts/{contracts}.abi")
contracts_abi[contracts] = data_parser.contract_abi
print(f"Deployed {contracts} at {contracts_address[contracts]}")

for i in range(50):
    client.sendRawTransaction(contracts_address["EngineerList"], contracts_abi["EngineerList"], "addEngineer",[names[i],ids[i],""])

# client.sendRawTransaction(contracts_address["EngineerList"],contracts_abi["EngineerList"],"add",[123])
sleep(10)
ret1 = client.call(contracts_address["EngineerList"],contracts_abi["EngineerList"],"getInfo",[0])
print(ret1)

client.finish()
sys.exit(0)
