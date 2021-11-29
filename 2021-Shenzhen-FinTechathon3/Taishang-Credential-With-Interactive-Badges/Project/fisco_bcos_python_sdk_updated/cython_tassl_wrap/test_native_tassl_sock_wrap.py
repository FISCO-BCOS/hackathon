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
import json
# -*- coding: utf-8 -*-
import time

from channelpack import ChannelPack
from native_tassl_sock_wrap import ECHO_PRINTF
from native_tassl_sock_wrap import NativeTasslSockWrap

# 测试代码，python + ctypes 调用 native_tassl_sock_wrap dll, 通过dll里的c接口调用TasllSockWrap

sw = NativeTasslSockWrap()
# sw.load_lib()


sw.init("sdk/gmca.crt", "sdk/gmsdk.crt", "sdk/gmsdk.key", "sdk/gmensdk.crt", "sdk/gmensdk.key")
sw.set_echo_mode(ECHO_PRINTF);
ip = "127.0.0.1"
port = 20800

try:
    with open("server.ini") as f:
        content = f.read()
        result = content.split(":")
        print(result)
        ip = result[0]
        port = int(result[1].strip(), 10)
        f.close()
except Exception as e:
    import traceback

    traceback.print_exc()

sw.try_connect(ip, port)
seq = ChannelPack.make_seq32()
data = "{\"jsonrpc\":\"2.0\",\"method\":\"getClientVersion\",\"params\":[],\"id\":1}"
pack = ChannelPack(ChannelPack.TYPE_RPC, seq, 0, data)
ret = sw.send(pack.pack())
print("in test,send ret ", ret)
for i in range(0, 10):
    response = sw.recv()
    if len(response) > 0:
        print(response)
        if len(response) > 42:
            try:
                (code, decodelen, responsePack) = ChannelPack.unpack(response)
                print("\n->unpack channelpack from server :")
                print(responsePack.detail())
                j = json.loads(responsePack.data)
                print(json.dumps(j, indent=4))
            except Exception as e:
                print("decode response error: ", e)
                print("-----response: ", response)
        break
    time.sleep(1)
sw.release()
