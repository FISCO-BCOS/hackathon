
from web3 import Web3
import time
import csv

#将proof.txt格式的文件保存在本地，这样就能直接调用该py
filename = r'E:\java\本地服务器以及本地前端\test1\src\main\java\szu\zhl\test1\pythonfile\proof.txt'

# 用于存储数据的列表
data = []
hex=[]
# 打开文件并读取数据
with open(filename, mode='r', encoding='utf-8') as file:
    csv_reader = csv.reader(file, delimiter=',')
    headers = next(csv_reader)  # 读取标题行

    # 打印标题行
    print("Headers:", headers)

    # 读取并打印每一行数据
    for row in csv_reader:
        data.append(row)
        print(row)
for head in headers:
    integer_value = int(head, 16)
    hex.append(integer_value)
# 打印整个数据列表
for h in hex:
    print(h)
param1=((hex[0],hex[1]),((hex[2],hex[3]),(hex[4],hex[5])),(hex[6],hex[7]))
param2=(hex[8],hex[9],hex[10],hex[11],hex[12])
# 连接到Infura节点（替换为你的Infura项目ID和节点URL）
web3 = Web3(Web3.HTTPProvider('http://172.31.100.87:7545'))
# # 设置账户和私钥
# private_key = '0xca4d44e3fa4942e41555095e11d65585567b2af7c5ec32d6f29324d0f034a5e7'  # 请确保私钥的安全
# account=Account.from_key(private_key)
contract_address = '0x2335Efa031Ffa51bd618E5097C8e11B1090f1Cb9'
contract_abi = [
		{
			"inputs": [
				{
					"components": [
						{
							"components": [
								{
									"internalType": "uint256",
									"name": "X",
									"type": "uint256"
								},
								{
									"internalType": "uint256",
									"name": "Y",
									"type": "uint256"
								}
							],
							"internalType": "struct Pairing.G1Point",
							"name": "a",
							"type": "tuple"
						},
						{
							"components": [
								{
									"internalType": "uint256[2]",
									"name": "X",
									"type": "uint256[2]"
								},
								{
									"internalType": "uint256[2]",
									"name": "Y",
									"type": "uint256[2]"
								}
							],
							"internalType": "struct Pairing.G2Point",
							"name": "b",
							"type": "tuple"
						},
						{
							"components": [
								{
									"internalType": "uint256",
									"name": "X",
									"type": "uint256"
								},
								{
									"internalType": "uint256",
									"name": "Y",
									"type": "uint256"
								}
							],
							"internalType": "struct Pairing.G1Point",
							"name": "c",
							"type": "tuple"
						}
					],
					"internalType": "struct Verifier.Proof",
					"name": "proof",
					"type": "tuple"
				},
				{
					"internalType": "uint256[5]",
					"name": "input",
					"type": "uint256[5]"
				}
			],
			"name": "verifyTx",
			"outputs": [
				{
					"internalType": "bool",
					"name": "r",
					"type": "bool"
				}
			],
			"stateMutability": "view",
			"type": "function"
		}
	]
contract = web3.eth.contract(address=contract_address, abi=contract_abi)

print("start")
t=time.time()*1000
try:
    tx = contract.functions.verifyTx(param1,param2).call()
    print("true")
except:
    print("false")
end=time.time()*1000
execution_time = end - t

