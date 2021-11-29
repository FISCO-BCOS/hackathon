import ssl
import utils.rpc
import sys
print(sys.path)

url = "http://127.0.0.1:8545"
rpc = utils.rpc.HTTPProvider(url)
rpc.isConnected()
param = [1]


bn = rpc.make_request("getBlockNumber", param)
if "error" in bn:
    print("error %d, [%s]" % (bn["error"]["code"], bn["error"]["message"]))
else:
    print(int(bn['result'], 16))


txhash = "0x27a7e7c14470a534cbf491310d85ad8e5620a893637cc47cfac964413bb08bf9"
tx = rpc.make_request("getTransactionByHash", [1, txhash])
input = tx["result"]["input"]
print(input)
