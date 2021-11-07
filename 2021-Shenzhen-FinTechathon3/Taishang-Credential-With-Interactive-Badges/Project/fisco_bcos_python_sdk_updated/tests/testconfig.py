'''
  @author: kentzhang
  @date: 2019-06
'''
from configobj import ConfigObj

config = ConfigObj("sample/contract.ini", encoding='UTF8')

config['address'] = {}
config['address']['SimpleInfo'] = "0x0b8ae6ce3850cedf8ebf6b6a7b949bb085d3ad17"
config['address']['testnode'] = "this is a test"


# 写入
config.write()
# 读配置文件
print(config['address'])
print(config['address']['SimpleInfo'])
print(config['address']['testnode'])


del config['address']['testnode']
config.write()
