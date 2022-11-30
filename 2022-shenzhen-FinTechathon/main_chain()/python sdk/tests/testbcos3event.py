import json
import os
import sys
import time
from ctypes import byref
sys.path.append("./")
from bcos3sdk.bcos3sdk_wrap import BcosCallbackFuture, s2b
from client.common.common import print_receipt_logs



from bcos3sdk.bcos3client import Bcos3Client
from client.datatype_parser import DatatypeParser

bcos3client = Bcos3Client()
print(bcos3client.getinfo())
num = bcos3client.getBlockNumber()
print(f"Current block number {num}")

from client.contractnote import ContractNote

current_address = ContractNote.get_last(bcos3client.get_full_name(),"HelloWorld")
print("Current address:", current_address)

(currpath, currfile) = os.path.split(os.path.realpath(__file__))
cbfuture = BcosCallbackFuture(currfile, "")

print("-----START: ", cbfuture.context.detail())
# https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/java_sdk/event_sub.html
event_param = dict()
event_param["fromBlock"] = 0  # change this for new event
event_param["toBlock"] = 1000000  # change this for new event
event_param["addresses"] = [current_address]  # sample helloWorld address
event_param["topics"] = []
parser = DatatypeParser("contracts/HelloWorld.abi")
event_name = "onset"
eventtopic = parser.topic_from_event_name(event_name)
event_param["topics"].append(eventtopic)
eventparam_str = json.dumps(event_param)
print("Subcribe event ,input param in str: ", eventparam_str)
eventid = bcos3client.bcossdk.bcos_event_sub_subscribe_event(bcos3client.bcossdk.sdk,
                                                             s2b(bcos3client.group),
                                                             s2b(eventparam_str),
                                                             cbfuture.callback,
                                                             byref(cbfuture.context))
print("->EventSub result: ", eventid)
print("callback:")
cbfuture.wait().display()

waittick = 0
lasttick = time.time()
while True:
    if cbfuture.wait().is_timeout is False: #default timeout 5 sec
        print(">>> CBFuture Get Message")
        print(f"{cbfuture.data.strip()}")
        result = bcos3client.get_result(cbfuture.data)
        logs = parser.parse_event_logs(result)
        print_receipt_logs(logs)
    else:
        print(">>> CBFuture timeout ,try again")
    
    nowtick = time.time()
    if nowtick - lasttick > 3:
        waittick = waittick + 1
        print(f"WaitTick : {waittick}")
        lasttick = time.time()
    
    if waittick >= 5:
        break

bcos3client.bcossdk.bcos_event_sub_unsubscribe_event(bcos3client.bcossdk.sdk, eventid)
bcos3client.finish()
print("Event listen test done")
