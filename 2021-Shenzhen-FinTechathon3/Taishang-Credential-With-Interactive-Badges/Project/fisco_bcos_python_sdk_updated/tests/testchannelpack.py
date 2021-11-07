from client.channelpack import ChannelPack
from eth_utils import encode_hex
import json;
seq = b"123456789012345678900123456789012";
data = "{\"jsonrpc\":\"2.0\",\"method\":\"getClientVersion\",\"params\":[],\"id\":1}";
json.loads(data)
print("datalen ",len(data))
pack = ChannelPack(0x12, seq, 0, data)
packres = pack.pack()

print(encode_hex(packres) )

aa = []
b = dict()
aa.append({"test",1})
aa.append({"test",2})
print(b)
print(json.dumps(b))
