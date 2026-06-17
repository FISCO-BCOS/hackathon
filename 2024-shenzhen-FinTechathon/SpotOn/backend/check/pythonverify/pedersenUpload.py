
from web3 import Web3
import time
import csv

#将proof.txt格式的文件保存在本地，这样就能直接调用该py
filename = r'D:\java\check\pythonverify\pedsen'

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
headers=headers[:143]
for head in headers:
    integer_value = int(head, 16)
    hex.append(integer_value)
tuple = []

# 使用循环来添加元素到列表中
for h in hex:  # range的结束索引是不包含的，所以要写13而不是12的下一个数字
	tuple.append(h)
tuple=tuple[:129]
# 连接到Infura节点（替换为你的Infura项目ID和节点URL）
web3 = Web3(Web3.HTTPProvider('http://172.31.100.87:7545'))
# # 设置账户和私钥
# private_key = '0xca4d44e3fa4942e41555095e11d65585567b2af7c5ec32d6f29324d0f034a5e7'  # 请确保私钥的安全
# account=Account.from_key(private_key)
contract_address = '0xE803b2180a4c0825F8C9500C7CE3a30d5b182377'
contract_abi = [
		{
			"inputs": [],
			"name": "retrieve",
			"outputs": [
				{
					"internalType": "uint256[129]",
					"name": "",
					"type": "uint256[129]"
				}
			],
			"stateMutability": "view",
			"type": "function"
		},
		{
			"inputs": [
				{
					"internalType": "uint256[129]",
					"name": "num",
					"type": "uint256[129]"
				}
			],
			"name": "store",
			"outputs": [],
			"stateMutability": "nonpayable",
			"type": "function"
		}
	]
contract = web3.eth.contract(address=contract_address, abi=contract_abi)

print("start")
t=time.time()*1000
try:
	tx = contract.functions.store(tuple).call()
	print("succeed")
except:
    print("false")
end=time.time()*1000
execution_time = end - t

