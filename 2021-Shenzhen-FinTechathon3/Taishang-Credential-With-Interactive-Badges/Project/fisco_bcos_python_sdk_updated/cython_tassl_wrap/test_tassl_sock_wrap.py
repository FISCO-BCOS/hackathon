#!/usr/bin/env python

'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  @author: kentzhang
  @date: 2021-03
'''
import os
currpath = os.path.dirname(os.path.realpath(__file__) )
print(currpath)
import time
import threading
import json
import sys
from channelpack import ChannelPack

ECHO_NONE   =  0x0000
ECHO_PRINTF =  0x0001
ECHO_LOG    =  0x0010

print("starting....")
wrap = None
from tassl_ssock_wrap_factory import tassl_ssock_wrap_factory
wrap = tassl_ssock_wrap_factory(True)
    
print("init module : ",wrap)
wrap.set_echo_mode(ECHO_PRINTF)
r = wrap.init(
    "sdk/gmca.crt",
    "sdk/gmsdk.crt",
    "sdk/gmsdk.key",
    "sdk/gmensdk.crt",
    "sdk/gmensdk.key")

print("init ", r)
if r < 0:
    sys.exit(0)
ip="127.0.0.1"
port=20200
try:
    with open("server.ini") as f:
        content = f.read()
        result = content.split(":")
        print(result)
        ip=result[0]
        port = int(result[1].strip(),10)
        f.close()
except Exception as e:
    import traceback
    traceback.print_exc()

wrap.try_connect(ip,port)
seq = ChannelPack.make_seq32()
data = "{\"jsonrpc\":\"2.0\",\"method\":\"getClientVersion\",\"params\":[],\"id\":1}"
pack = ChannelPack(ChannelPack.TYPE_RPC, seq, 0, data)
json.loads(data)

class recvThread(threading.Thread):
    def run(self):
        i = 100
        # time.sleep(2)
        while True:
            i = i + 1
            print("-----RECV thread running ", i)
            rsp = wrap.recv()
            if len(rsp) > 42:
                try:
                    (code, decodelen, responsePack) = ChannelPack.unpack(rsp)
                    print("\n->unpack channelpack from server :")
                    print(responsePack.detail())
                    j = json.loads(responsePack.data)
                    print(json.dumps(j, indent=4))
                except Exception as e:
                    print("decode response error: ", e)
                    print("-----RSP: ", rsp)
            else:
                print("-----RSP: ", rsp)
            time.sleep(1)


class sendThread(threading.Thread):
    def run(self):
        i = 0
        print(">>>>start send thread")
        time.sleep(1)
        while True:
            i = i + 1
            print(">>>>>SEND thread running ", i)
            r = wrap.send(pack.pack())
            print(">>>>>Write bytes: ", r)
            time.sleep(1)


rt = recvThread()
rt.setDaemon(True)
rt.start()

st = sendThread()
st.setDaemon(True)
st.start()

if False:
    r = wrap.send(pack.pack())
    print("wrap write return ", r)
    resultbuffer = wrap.recv()
    print("read from lib : ", len(resultbuffer), resultbuffer)
    (code, decodelen, responsePack) = ChannelPack.unpack(resultbuffer)
    print("\n->unpack channelpack from server :")
    print(responsePack.detail())

    j = json.loads(responsePack.data)
    print(json.dumps(j, indent=4))
time.sleep(3)
wrap.finish()
