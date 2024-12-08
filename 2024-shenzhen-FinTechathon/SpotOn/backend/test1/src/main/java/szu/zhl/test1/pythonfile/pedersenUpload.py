
from web3 import Web3
import time
import csv
import json




def read_file():
	# 用于存储十六进制数据的列表
	hex_data = []

	# 假设 filename 是你的 JSON 文件名
	filename = 'C:\example\identity.json'

	# 打开文件并读取数据
	with open(filename, mode='r', encoding='utf-8') as file:
		data = json.load(file)  # 读取 JSON 文件

		# 假设你需要读取 JSON 中的多个键
		keys_of_interest = ['id', 'oldcmt']  # 替换为你实际需要的键名

		for key in keys_of_interest:
			if key in data:
				# 获取该键对应的值
				# 假设你需要读取 JSON 中的多个键
				values = data[key]

				# 检查值是否是列表
				if isinstance(values, list):
					for value in values:
						# 如果值是以 '0x' 开头的字符串，保持不变
						if isinstance(value, str) and value.startswith('0x'):
							hex_data.append(value)
						else:
							try:
								# 尝试将其转换为十六进制字符串
								hex_value = '0x' + format(int(value, 16), 'x')
								hex_data.append(hex_value)
							except ValueError:
								# 如果转换失败，直接添加原始值
								hex_data.append(value)
				else:
					# 如果值不是列表，直接处理单个值
					if isinstance(values, str) and values.startswith('0x'):
						hex_data.append(values)
					else:
						try:
							hex_value = '0x' + format(int(values, 16), 'x')
							hex_data.append(hex_value)
						except ValueError:
							hex_data.append(values)

			# 打印十六进制数据
	return hex_data

# 用于存储数据的列表
data = []
hex=[]
# 打开文件并读取数据
headers = read_file()
print(headers)
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
web3 = Web3(Web3.HTTPProvider('http://127.0.0.1:7545'))
# # 设置账户和私钥
# private_key = '0xca4d44e3fa4942e41555095e11d65585567b2af7c5ec32d6f29324d0f034a5e7'  # 请确保私钥的安全
# account=Account.from_key(private_key)
contract_address = '0x91a1243998B66F21E32622Eb7DDC1576A6521aeE'
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
	print("true")
except:
    print("false")
end=time.time()*1000
execution_time = end - t

