import json


def read_file():
	# 用于存储十六进制数据的列表
	hex_data = []

	# 假设 filename 是你的 JSON 文件名
	filename = 'C:\example\proof.json'

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

hexdata = read_file()
print(hexdata)
