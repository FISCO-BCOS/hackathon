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
  @date: 2020-01
'''

import threading
from client import clientlogger
import queue
import time
from client.channelpack import ChannelPack


class ChannelPushHandler:  # interface
    def on_push(self, packmsg):
        pass


class ChannelPushDispatcher (threading.Thread):
    dispatch_register = dict()
    keepWorking = False
    logger = clientlogger.logger
    pushQueue = queue.Queue(1024 * 10)
    lock = threading.RLock()

    def __init__(self):
        threading.Thread.__init__(self)

    def add_handler(self, pack_type, handler):
        try:
            self.lock.acquire()
            if pack_type in self.dispatch_register:
                self.dispatch_register[pack_type][handler] = True
            else:
                handlerMap = dict()
                handlerMap[handler] = True
                self.dispatch_register[pack_type] = handlerMap
        except Exception as e:
            self.logger.error("channel push dispatcher add handler error", e)
        finally:
            self.lock.release()

    def remove_handler(self, pack_type, handler):
        try:
            self.lock.acquire()
            if pack_type in self.dispatch_register:
                if handler in self.dispatch_register[pack_type]:
                    self.dispatch_register[pack_type].pop(handler)
            else:
                return
        except Exception as e:
            self.logger.error("channel push dispatcher remove_handler error", e)
        finally:
            self.lock.release()

    def getHandler(self, pack_type):
        try:
            self.lock.acquire()
            if pack_type in self.dispatch_register:
                return self.dispatch_register[pack_type]
            else:
                return dict()
        except Exception as e:
            self.logger.error("channel push dispatcher remove_handler error", e)
        finally:
            self.lock.release()

    def finish(self):
        if self.keepWorking is True:
            self.keepWorking = False
            self.join(timeout=1)

    def push(self, packmsg: ChannelPack):
        if self.pushQueue.full():
            pack = self.pushQueue.get_nowait()
            self.logger.error("Push queue FULL pop and LOST: {}".format(pack.detail()))
        self.pushQueue.put_nowait(packmsg)

    def dealmsg(self, packmsg):
        try:
            self.lock.acquire()
            handlers = self.getHandler(packmsg.type)
            if handlers is None:
                # no handler register
                return
            for handler in handlers:
                if isinstance(handler, ChannelPushHandler):
                    handler.on_push(packmsg)
        except Exception as e:
            print("{} push handler error {},{},{}".format(self.name, e, packmsg.type, packmsg.data))
            self.logger.error(
                "{} push handler error {},{},{}".format(
                    self.name, e, packmsg.type, packmsg.data))
            #import traceback
            #traceback.print_exc()
        finally:
            self.lock.release()

    def run(self):
        try:
            self.keepWorking = True
            self.logger.debug(self.name + ":start thread-->")
            #print(self.name + ":start thread-->")
            while self.keepWorking:
                #print(self.name + ":start-->",self.keepWorking)
                packmsg = None
                if not self.pushQueue.empty():
                    packmsg = self.pushQueue.get_nowait()
                if packmsg is None and self.keepWorking:
                    time.sleep(0.01)
                    #print("push running")
                    continue
                self.dealmsg(packmsg)
        except Exception as e:
            print("push Thread exception:", e)
            self.logger.error("{} push dispatcher error {}".format(self.name, e))
        finally:
            self.logger.debug("{}:thread finished ,keepWorking = {}".format(
                self.name, self.keepWorking))


if False:
    cpp = ChannelPushDispacher()
    handler = "test"
    cpp.add_handler(1, handler)
    cpp.add_handler(1, handler)
    handler = "test123"
    cpp.add_handler(1, handler)
    print(cpp.getHandler(1))
    cpp.remove_handler(1, handler)
    print(cpp.getHandler(1))

    cpp.add_handler(2, "2handler")
    cpp.add_handler(2, "3handler")
    cpp.add_handler(3, "2handler")

    print(cpp.dispatch_register)
    for (item, v) in cpp.getHandler(1).items():
        print("{}:{}".format(item, v))
