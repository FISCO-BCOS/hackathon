#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @author: kentzhang
  @date: 2019-06
'''
import sys
from client.bcosclient import BcosClient
import os
from client_config import client_config
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


def usage():
    usagetext = 'params: [contractname] [address(可以为last)] [event_name] [indexed value(根据event定义，可以为多个)]\n\n'
    usagetext = usagetext + "\teg: for contract sample [contracts/HelloEvent.sol], use cmdline:\n\n"
    usagetext = usagetext + "\tpython event_callback.py HelloEvent last on_set \n"
    usagetext = usagetext + \
        "\tpython event_callback.py HelloEvent last on_number 5\n\n...(and other events)"
    print(usagetext)


class EventPushHandler01(ChannelPushHandler):
    parser = DatatypeParser()

    def on_push(self, packmsg: ChannelPack):
        print("--------------------EventPushHandler01", packmsg.detail())

        strmsg = packmsg.data.decode("utf-8")
        response = json.loads(strmsg)
        loglist = parser.parse_event_logs(response["logs"])
        print("FilterID ", response["filterID"])
        print("--------------------EventPushHandler01", json.dumps(loglist, indent=4))


class EventPushHandler02(ChannelPushHandler):
    parser = DatatypeParser()

    def on_push(self, packmsg: ChannelPack):
        print(">>>>>>>>>>>>>>>>>>EventPushHandler02", packmsg.detail())
        strmsg = packmsg.data.decode("utf-8")
        response = json.loads(strmsg)
        loglist = parser.parse_event_logs(response["logs"])
        print("FilterID ", response["filterID"])
        print(">>>>>>>>>>>>>>>>>>EventPushHandler02", json.dumps(loglist, indent=4))


parser: DatatypeParser = None
client: BcosClient = None
eventHandler01 = EventPushHandler01()
eventHandler02 = EventPushHandler02()


def format_event_register_request(
        from_block,
        to_block,
        addresses,
        topics,
        groupid="1",
        filterid=None):
    '''
    {
  "fromBlock": "latest",
  "toBlock": "latest",
  "addresses": [
    0xca5ed56862869c25da0bdf186e634aac6c6361ee
  ],
  "topics": [
    "0x91c95f04198617c60eaf2180fbca88fc192db379657df0e412a9f7dd4ebbe95d"
  ],
  "groupID": "1",
  "filterID": "bb31e4ec086c48e18f21cb994e2e5967"
}'''
    request = dict()
    request["fromBlock"] = from_block
    request["toBlock"] = to_block
    request["addresses"] = addresses
    request["topics"] = topics
    request["groupID"] = groupid
    if filterid is None:
        seq = uuid.uuid1()
        filterid = seq.hex
    request["filterID"] = filterid
    requestJson = FriendlyJsonSerde().json_encode(request)
    return requestJson


def register_event_callback(addresses, event_name, indexed_value):
    topics = []
    topic0 = parser.topic_from_event_name(event_name)
    topics.append(topic0)
    event_abi = parser.event_name_map[event_name]
    print("event abi:", event_abi)
    if len(indexed_value) > 0:
        indexedinput = []
        for input in event_abi["inputs"]:
            if input["indexed"] is True:
                indexedinput.append((input['name'], input['type']))
        print(indexedinput)
        i = 0
        for v in indexed_value:
            itype = indexedinput[i][1]
            topic = DatatypeParser.topic_from_type(itype, v)
            if not (topic is None):
                topics.append(topic)
            i = i + 1
    requestJson = format_event_register_request("latest", "latest", addresses, topics)
    requestbytes = ChannelPack.pack_amop_topic_message("", requestJson)
    client.channel_handler.pushDispacher.add_handler(ChannelPack.EVENT_LOG_PUSH, eventHandler01)
    client.channel_handler.pushDispacher.add_handler(ChannelPack.EVENT_LOG_PUSH, eventHandler02)
    response = client.channel_handler.make_channel_request(requestbytes,
                                                           ChannelPack.CLIENT_REGISTER_EVENT_LOG,
                                                           ChannelPack.CLIENT_REGISTER_EVENT_LOG)
    (topic, result) = ChannelPack.unpack_amop_topic_message(response)
    dataobj = json.loads(result)
    print(
        "after register ,event_name:{},topic:{},result:{}".format(
            event_name,
            topic,
            dataobj['result']))

# abi address event_name  indexed_value


def main(argv):
    global parser
    global client
    if len(argv) < 3:
        usage()
        exit(0)

    contractname = argv[0]
    address = argv[1]
    event_name = argv[2]
    indexed_value = argv[3:]

    try:
        print("usage input {},{},{},{}".format(contractname, address, event_name, indexed_value))
        if address == "last":
            cn = ContractNote()
            bcosclient = BcosClient()
            address = cn.get_last(bcosclient.get_full_name(),contractname)
            print("hex address :", address)
            bcosclient.finish()
        abifile = "contracts/" + contractname + ".abi"
        parser = DatatypeParser(abifile)
        client = BcosClient()
        print(client.getinfo())
        register_event_callback([address], event_name, indexed_value)
    except Exception as e:
        import traceback
        traceback.print_exc()
        client.finish()
        import time
        time.sleep(0.5)
        sys.exit(-1)

if __name__ == "__main__":
    main(sys.argv[1:])
