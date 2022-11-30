import ctypes
import datetime
import sys
import os
# sys.path.append("../")

print("CWD: ",os.getcwd())
sys.path.append("./")
import json
from ctypes import *
import time
from client.datatype_parser import DatatypeParser
from bcos3sdk.bcos3sdk_wrap import NativeBcos3sdk, BCOS_CALLBACK_FUNC, BcosReqContext, BcosCallbackFuture

##---------------------------------------------------------
##以下为测试代码
# 应用层回调


bcossdk = NativeBcos3sdk()
res = bcossdk.init_sdk("./bcos3sdklib/bcos3_sdk_config.ini")
if res != 0:
    print("START SDK error")
    sys.exit(0)

print("sdkobj:", bcossdk)
cbfuture = BcosCallbackFuture();
cb_func = BCOS_CALLBACK_FUNC(cbfuture.bcos_callback)
n = 100

key_pair = bcossdk.bcos_sdk_create_keypair(0)  # 0:ecdsa  1:sm
key = b"7a94d9793bcc38f533c6e15d8ef9c557e8ead2d3f86e9ac1178ce56b2815f86b"
key_pair = bcossdk.bcos_sdk_create_keypair_by_hex_private_key(0, key)

address = bcossdk.bcos_sdk_get_keypair_address(key_pair)
print(" ==> key pair, address: {}".format(address))

pub = bcossdk.bcos_sdk_get_keypair_public_key(key_pair)
print(" ==> pub: {}".format(pub))

priv = bcossdk.bcos_sdk_get_keypair_private_key(key_pair)
print(" ==> pri: {}".format(priv))

group_id = "group0".encode("utf-8")
chain_id = bcossdk.bcos_sdk_get_group_chain_id(bcossdk.sdk, group_id)
print(" ==> chain_id: {}".format(chain_id))

wasm = ctypes.c_int()
sm_crypto = ctypes.c_int()

bcossdk.bcos_sdk_get_group_wasm_and_crypto(bcossdk.sdk, group_id, byref(wasm), byref(sm_crypto))
print(" ==> wasm: {}, sm_crypto: {}".format(wasm, sm_crypto))

# cb_context = BcosReqContext(n, "bcos_rpc_get_block_by_number", "this is test 1332")
# bcossdk.bcos_rpc_get_block_by_number(bcossdk.sdk, group_id, None, 0, 1, 1, cb_func, byref(cb_context))
# cbfuture.wait()
# cbfuture.display()

blocklimit = bcossdk.bcos_rpc_get_block_limit(bcossdk.sdk, group_id)
print("blocklimit ", blocklimit)

contractFile = r"contracts\HelloWorld.abi"
abi_parser = DatatypeParser()
abi_parser.load_abi_file(contractFile)
contract_abi = abi_parser.contract_abi

# 将要调用的函数和参数编码
bindata = ""
with open(r"contracts\HelloWorld.bin", 'r') as load_f:
    bindata = load_f.read()
    load_f.close()
print("bindata len", len(bindata))
print("bindata:", bindata)

cbfuture = BcosCallbackFuture();
cb_func = BCOS_CALLBACK_FUNC(cbfuture.bcos_callback)
cb_context = BcosReqContext(n, "deploy contract", "this is deploy test")
# 发起部署调用
contract_address = ""
do_deploy = False
if do_deploy:
    # 对交易数据进行签名
    # signed_tx = bcossdk.bcos_sdk_create_signed_tx(key_pair, b"", bindata.encode("UTF-8"), chain_id, group_id,
    #                                              blocklimit)
    p_txhash = c_char_p(0)
    p_signed_tx = c_char_p(0)
    bcossdk.bcos_sdk_create_signed_transaction(key_pair, group_id, chain_id,
                                               b"", bytes(bindata, "UTF-8"), b"",
                                               blocklimit, 0,
                                               pointer(p_txhash), pointer(p_signed_tx))
    print("sign result", bcossdk.bcos_sdk_get_last_error())
    print("signed tx len", len(p_signed_tx.value))
    print("signed tx:", p_signed_tx.value)
    
    bcossdk.bcos_rpc_send_transaction(bcossdk.sdk, group_id, b"", p_signed_tx.value, 0, cb_func, byref(cb_context))
    cbfuture.wait()
    cbfuture.display()
    resp = json.loads(cbfuture.data)
    if "result" in resp:
        contract_address = resp["result"]["contractAddress"]
        print("Deploy new addr : ", contract_address)
else:
    contract_address = "31ed5233b81c79d5adddeef991f531a9bbc2ad01"
# sys.exit()


cb_context = BcosReqContext(n, "send tx", "this is sendtx test")
inputparams = [f"abcefg:{datetime.datetime.now()}"]
inputparams = [f"abcdefg"]
# 第三个参数是方法的abi，可以传入None，encode_transaction_data做了修改，支持通过方法+参数在整个abi里找到对应的方法abi来编码
functiondata = abi_parser.encode_function_data("set", inputparams)
# 对交易数据进行签名

functiondata = functiondata[2:]
print("function data len : ", len(functiondata))
print("function data : ", functiondata)
p_txhash = c_char_p(0)
p_signed_tx = c_char_p(0)
print("before sign: ", p_signed_tx)
print("before sign pointer: ", pointer(p_signed_tx))

bcossdk.bcos_sdk_create_signed_transaction(key_pair, group_id, chain_id,
                                           contract_address.encode("UTF-8"), functiondata.encode("UTF-8"), b"",
                                           blocklimit, 0,
                                           p_txhash, p_signed_tx)
print("after sign", p_signed_tx)
print("after sign pointer: ", pointer(p_signed_tx))
print("signed tx type", type(p_signed_tx))
print("signed tx len", len(p_signed_tx.value))

print("signed tx:", p_signed_tx.value)
print("tx hash :", p_txhash.value)
print("tx hash len :", len(p_txhash.value))

bcossdk.bcos_rpc_send_transaction(bcossdk.sdk, group_id, b"", p_signed_tx.value, 0, cb_func, byref(cb_context))
cbfuture.wait()
cbfuture.display()

bcossdk.bcos_sdk_c_free(p_signed_tx)
bcossdk.bcos_sdk_c_free(p_txhash)

resp = json.loads(cbfuture.data)
if "result" in resp:
    logs = abi_parser.parse_event_logs(resp["result"]["logEntries"])
    print("logs", logs)

cb_context = BcosReqContext(n, "call ", "this is call test")
reqdata = abi_parser.encode_function_data("get", [])
bcossdk.bcos_rpc_call(bcossdk.sdk, group_id, b"", contract_address.encode("UTF-8"), reqdata.encode("UTF-8"), cb_func,
                      byref(cb_context))
cbfuture.wait()
cbfuture.display()

resp = json.loads(cbfuture.data)
output = abi_parser.parse_output("get", resp["result"]["output"])
print(">>> call output", output)

n = n + 1
cb_context = BcosReqContext(n, "bcos_rpc_get_block_number", "this is test " + str(n))
bcossdk.bcos_rpc_get_block_number(bcossdk.sdk, group_id, None, cb_func, byref(cb_context))
cbfuture.wait().display()

n = n + 1
cb_context = BcosReqContext(n, "bcos_rpc_get_total_transaction_count", "this is test " + str(n))
bcossdk.bcos_rpc_get_total_transaction_count(bcossdk.sdk, group_id, None, cb_func, byref(cb_context))
cbfuture.wait().display()

ver = bcossdk.bcos_sdk_version()
print("VERSION: ", ver.decode("UTF-8"))

time.sleep(1)
bcossdk.bcos_sdk_destroy_keypair(key_pair)
bcossdk.bcos_sdk_stop(bcossdk.sdk)
bcossdk.bcos_sdk_destroy(bcossdk.sdk)
