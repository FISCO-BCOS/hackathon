import os

print("CWD:", os.getcwd())
import sys

import ctypes

sys.path.append("./")

from client.datatype_parser import DatatypeParser

import json
from ctypes import *
import time
from bcos3sdk.bcos3sdk_wrap import NativeBcos3sdk, BCOS_CALLBACK_FUNC, BcosReqContext, BcosCallbackFuture, \
    BCOS_AMOP_SUB_CALLBACK_FUNC, s2b

##---------------------------------------------------------
##以下为测试代码
# 应用层回调


group_id = "group0".encode("utf-8")

bcossdk = NativeBcos3sdk()
res = bcossdk.init_sdk("./bcos3sdklib/bcos3_sdk_config.ini")
if res != 0:
    print("ERROR init sdk")
    sys.exit()
print("sdkobj:", bcossdk)
cbfuture = BcosCallbackFuture();
cb_func = BCOS_CALLBACK_FUNC(cbfuture.bcos_callback)
n = 100

key_pair = bcossdk.bcos_sdk_create_keypair(1)  # 1:ecdsa  2:sm

address = bcossdk.bcos_sdk_get_keypair_address(key_pair)
print(" ==> new key pair, address: {}".format(address))

pub = bcossdk.bcos_sdk_get_keypair_public_key(key_pair)
print(" ==> pub: {}".format(pub))

priv = bcossdk.bcos_sdk_get_keypair_private_key(key_pair)
print(" ==> pri: {}".format(priv))

chain_id = bcossdk.bcos_sdk_get_group_chain_id(bcossdk.sdk, group_id)
print(" ==> chain_id: {}".format(chain_id))

wasm = ctypes.c_int()
sm_crypto = ctypes.c_int()
bcossdk.bcos_sdk_get_group_wasm_and_crypto(bcossdk.sdk, group_id, byref(wasm), byref(sm_crypto))
print(" ==> wasm: {}, sm_crypto: {}".format(wasm, sm_crypto))

TEST_GROUP_INFO = True
if TEST_GROUP_INFO:
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_group_node_info", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    nodename = b"84804b2eda60a3b08add6f05dcfd459ee37f6212fe1163b4cdde85977136e71efe2847174a48895476c2f4ea9ae6af5fd31d2ab3a1453b2e9a4e27059d82d901"
    bcossdk.bcos_rpc_get_group_node_info(bcossdk.sdk, group_id, nodename, cb_func, byref(cb_context))
    cbfuture.wait().display()
    # sys.exit(0)
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_group_info_list", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_group_info_list(bcossdk.sdk, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_group_info", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_group_info(bcossdk.sdk, group_id, cb_func, byref(cb_context))
    resp = cbfuture.wait(2).display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_group_list", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_group_list(bcossdk.sdk, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_group_peers", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_group_peers(bcossdk.sdk, group_id, cb_func, byref(cb_context))
    cbfuture.wait().display()

TEST_SYS_INFO = True
if TEST_SYS_INFO:
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_peers", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_peers(bcossdk.sdk, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_system_config_by_key", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_system_config_by_key(bcossdk.sdk, group_id, None, b"key", cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_consensus_status", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_consensus_status(bcossdk.sdk, group_id, None, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_sync_status", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_sync_status(bcossdk.sdk, group_id, None, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_pbft_view", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_pbft_view(bcossdk.sdk, group_id, None, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_observer_list", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_observer_list(bcossdk.sdk, group_id, None, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_sealer_list", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_sealer_list(bcossdk.sdk, group_id, None, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_code", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_code(bcossdk.sdk, group_id, None, b"0x1001", cb_func, byref(cb_context))
    cbfuture.wait().display()

TEST_TX_COUNT = False
if TEST_TX_COUNT:
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_total_transaction_count", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_total_transaction_count(bcossdk.sdk, group_id, None, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_pending_tx_size", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_pending_tx_size(bcossdk.sdk, group_id, None, cb_func, byref(cb_context))
    cbfuture.wait().display()

TEST_GET_BLOCK_TX = True
if TEST_GET_BLOCK_TX:
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_block_number", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_block_number(bcossdk.sdk, group_id, None, cbfuture.callback, byref(cb_context))
    cbfuture.wait().display()
    
    cb_context = BcosReqContext(n, "bcos_rpc_get_block_by_number", "this is test" + str(n))
    print("------START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_block_by_number(bcossdk.sdk, group_id, None, 1, 1, 1, cb_func, byref(cb_context))
    cbfuture.wait().display()
    cbfuture.data
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_block_limit", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    l = bcossdk.bcos_rpc_get_block_limit(bcossdk.sdk, group_id)
    print("blocklimit ", l)
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_block_hash_by_number", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bcossdk.bcos_rpc_get_block_hash_by_number(bcossdk.sdk, group_id, None, 1, cb_func, byref(cb_context))
    cbfuture.wait().display()
    response = json.loads(cbfuture.data)
    print(response)
    blockhash = response["result"]
    print("blockhash", blockhash)
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_block_by_hash", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    bh = bytes(blockhash, "UTF-8")
    bcossdk.bcos_rpc_get_block_by_hash(bcossdk.sdk, group_id, None, bh, 0, 0, cb_func, byref(cb_context))
    cbfuture.wait().display()
    response = json.loads(cbfuture.data)
    block = response["result"]
    print("BLOCK: ", block)
    txhash = block["transactions"][0]["hash"]
    print("TXHASH: ", txhash)
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_transaction", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    th = bytes(txhash, "UTF-8")  # b"0xc0a367cb5d11f21fd51196e9683cbfc2b8cd33c2e86c559d67142152f5fa7ee5"
    bcossdk.bcos_rpc_get_transaction(bcossdk.sdk, group_id, None, th, 0, cb_func, byref(cb_context))
    cbfuture.wait().display()
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_rpc_get_transaction_receipt", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    th = th = bytes(txhash, "UTF-8")  # b"0xc0a367cb5d11f21fd51196e9683cbfc2b8cd33c2e86c559d67142152f5fa7ee5"
    bcossdk.bcos_rpc_get_transaction_receipt(bcossdk.sdk, group_id, None, th, 0, cb_func, byref(cb_context))
    cbfuture.wait().display()

TEST_EVENT_SUB = True
if TEST_EVENT_SUB:
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_event_sub_subscribe_event", "this is test " + str(n))
    print("-----START: ", cb_context.detail())
    '''
    public class EventSubParams {
    private BigInteger fromBlock;
    private BigInteger toBlock;
    private List<String> addresses;
    private List<List<String>> topics;
}
    '''
    # https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/java_sdk/event_sub.html
    event_param = dict()
    event_param["fromBlock"] = 0  # change this for new event
    event_param["toBlock"] = 1000000  # change this for new event
    event_param["address"] = ["44a8a3cf7e5f2ba1555152968ec536d2a15817fa"]  # sample helloWorld address
    event_param["topics"] = []
    parser = DatatypeParser("contracts/HelloWorld.abi")
    event_name = "onset"
    eventtopic = parser.topic_from_event_name(event_name)
    event_param["topics"].append(eventtopic)
    eventparam_str = json.dumps(event_param)
    print("Subcribe event ,input param in str: ", eventparam_str)
    eventid = bcossdk.bcos_event_sub_subscribe_event(bcossdk.sdk, group_id, s2b(eventparam_str), cb_func,
                                                     byref(cb_context))
    print("->EventSub result: ", eventid)
    print("callback:")
    cbfuture.wait().display()
    
    bcossdk.bcos_event_sub_unsubscribe_event(bcossdk.sdk, eventid)

TEST_AMOP = False
if TEST_AMOP:
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_amop_subscribe_topic", "this is test " + str(n))
    
    TOPIC_ARRAY_3 = c_char_p * 3
    topics = TOPIC_ARRAY_3()
    topics[0] = b"chongqing001"
    topics[1] = b"chengdu002"
    topics[2] = b"guiyang003"
    bcossdk.bcos_amop_subscribe_topic(bcossdk.sdk, topics, 3)
    print(">>>>bcos_amop_subscribe_topic: done");
    
    
    def bcos_sdk_c_amop_subscribe_cb(endpoint, seq, resp):
        print("bcos_sdk_c_amop_subscribe_cb callback")
        epstr = str(endpoint, "utf-8")
        seqstr = str(seq, "utf-8")
        print("bcos_sdk_c_amop_subscribe_cb ", epstr, seqstr)
        if resp is None:
            print("resp is None")
        else:
            print("resp is ", resp)
    
    
    n = n + 1
    cb_context = BcosReqContext(n, "bcos_amop_subscribe_topic_with_cb", "this is test " + str(n))
    
    topic = b'abcdefg'
    print("try bcos_amop_subscribe_topic_with_cb")
    bcossdk.bcos_amop_subscribe_topic_with_cb(bcossdk.sdk, topic,
                                              BCOS_AMOP_SUB_CALLBACK_FUNC(bcos_sdk_c_amop_subscribe_cb),
                                              byref(cb_context))

# ----------------------------------------

time.sleep(2)
bcossdk.bcos_sdk_stop(bcossdk.sdk)
bcossdk.bcos_sdk_destroy(bcossdk.sdk)
