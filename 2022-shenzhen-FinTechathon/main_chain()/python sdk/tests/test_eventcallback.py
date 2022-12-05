import time
from event_callback import (format_event_register_request, register_event_callback)
from client.bcosclient import BcosClient
import os
from client.stattool import StatTool
from client.datatype_parser import DatatypeParser
from client.common.compiler import Compiler
from client_config import client_config
from client.bcoserror import BcosException, BcosError
from client.contractnote import ContractNote
from eth_utils import encode_hex, decode_hex
import uuid
from eth_utils.crypto import keccak
import json
import struct
from utils.encoding import FriendlyJsonSerde
from client.bcoserror import BcosError, ChannelException
from eth_utils import (to_text, to_bytes)
from client.channel_push_dispatcher import ChannelPushHandler
from client.channelpack import ChannelPack


class TestEventPushHandler(ChannelPushHandler):
    parser = DatatypeParser()

    def on_push(self, packmsg: ChannelPack):
        print("EventPushHandler", packmsg.detail())
        strmsg = packmsg.data.decode("utf-8")
        response = json.loads(strmsg)
        print("response filterID:", response['filterID'])
        #print("response:", json.dumps(response,indent=4))
        loglist = parser.parse_event_logs(response["logs"])
        print(json.dumps(loglist, indent=4))

client = None
client = BcosClient()
info = client.getinfo()
print("client info:", info)
# 从文件加载abi定义
abi_file = "contracts/HelloEvent.abi"
parser = data_parser = DatatypeParser(abi_file)
contract_abi = data_parser.contract_abi
contractnode = ContractNote()
address = contractnode.get_last(client.get_full_name(),"HelloEvent")
print("event contract address is ", address)

params_set = ["aaabbb"]
params_setnum_5 = ["setnum_ab", 5]
params_setnum_10 = ["setnum_ab", 10]
params_settwo_1 = ["settwo_aabb", 10, 'key1']
params_settwo_2 = ["settwo_aabb", 10, 'key2']


'''
    CHANNEL_RPC_REQUEST = 0x12,        // type for rpc request
    CLIENT_HEARTBEAT = 0x13,           // type for heart beat for sdk
    CLIENT_HANDSHAKE = 0x14,           // type for hand shake
    CLIENT_REGISTER_EVENT_LOG = 0x15,  // type for event log filter register request and response
    AMOP_REQUEST = 0x30,               // type for request from sdk
    AMOP_RESPONSE = 0x31,              // type for response to sdk
    AMOP_CLIENT_TOPICS = 0x32,         // type for topic request
    AMOP_MULBROADCAST = 0x35,          // type for mult broadcast
    REQUEST_TOPICCERT = 0x37,          // type request verify
    UPDATE_TOPIICSTATUS = 0x38,        // type for update status
    TRANSACTION_NOTIFY = 0x1000,       // type for  transaction notify
    BLOCK_NOTIFY = 0x1001,             // type for  block notify
    EVENT_LOG_PUSH = 0x1002            // type for event log push
'''


def test_register_event():

    register_event_callback("HelloEvent", "on_address", None)


def test_event():
    receipt = client.rpc_sendRawTransactionGetReceipt(address, contract_abi, "set", params_set)
    print(json.dumps(receipt, indent=4))
    logresult = parser.parse_event_logs(receipt["logs"])
    i = 0
    # print(json.dumps(logresult,indent=4))
    for log in logresult:
        if 'eventname' in log:
            i = i + 1
            print("{}): log name: {} , data: {} , topic: {}".format(
                i, log['eventname'], log['eventdata'], log['topic']))
    txhash = receipt["transactionHash"]
    assert 1 == 1


topic = parser.topic_from_event_name("onset")
format_event_register_request("lastest", "lastest", address, [topic])
#
test_register_event()
# test_event()
time.sleep(5)
print("test done")
client.finish()
