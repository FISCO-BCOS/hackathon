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
  channel protocol ref:
  https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/design/protocol_description.html#channelmessage
'''

import queue
import threading
from queue import Empty
from pymitter import EventEmitter
from promise import Promise
import time
import itertools
import ssl
import json
from client.channelpack import ChannelPack
from utils.encoding import FriendlyJsonSerde
from client.bcoserror import BcosError, ChannelException
from eth_utils import (to_text, to_bytes)
from client.channel_push_dispatcher import ChannelPushDispatcher
from client.ssl_sock_wrap import CommonSSLSockWrap, SSLSockWrap


class ChannelHandler(threading.Thread):
    context = None

    ssock: CommonSSLSockWrap = None

    host = None
    port = None
    request_counter = itertools.count()
    logger = None
    recvThread = None
    sendThread = None
    keepWorking = False
    socketClosed = False
    pushDispacher = None

    def __init__(self, max_timeout=10, name="channelHandler"):
        self.timeout = max_timeout
        threading.Thread.__init__(self)
        self.callbackEmitter = EventEmitter()
        self.requests = []
        self.name = name
        self.blockNumber = 0
        self.onResponsePrefix = "onResponse"
        self.getResultPrefix = "getResult"
        self.lock = threading.RLock()

    def initTLSContext(self, ssl_type, ca_file, node_crt_file, node_key_file,
                       en_crt_file=None,  # for gmssl
                       en_key_file=None,  # for gmssl
                       protocol=ssl.PROTOCOL_TLSv1_2,
                       verify_mode=ssl.CERT_REQUIRED):
        if ssl_type.upper() != "GM":
            self.ssock = SSLSockWrap()
            self.ssock.logger = self.logger
        else:
            from client.tassl_sock_wrap_impl import TasslSockWrap
            self.ssock = TasslSockWrap()
        self.ssock.init(ca_file, node_crt_file, node_key_file,
                        en_crt_file=en_crt_file,
                        en_key_file=en_key_file,
                        protocol=protocol,
                        verify_mode=verify_mode)

    def __del__(self):
        self.finish()

    def disconnect(self):
        self.lock.acquire()
        if self.ssock is not None and self.socketClosed is False:

            self.socketClosed = True
            self.ssock.finish()
            self.logger.info(
                "disconnect for read/write error, host: {}, port: {}".format(self.host, self.port))
        self.lock.release()

    def reconnect(self):
        if self.socketClosed is True:
            self.lock.acquire()
            self.logger.info("reconnect, host: {}, port: {}".format(self.host, self.port))
            try:
                self.start_connect()
                self.socketClosed = False
                self.logger.info(
                    "reconnect success, host: {}, port: {}".format(self.host, self.port))
            except Exception as e:
                self.logger.error("reconnect failed, error: {}".format(e))
            self.lock.release()

    def finish(self):
        self.disconnect()
        self.ssock = None
        if self.keepWorking is True:
            self.keepWorking = False
            self.join(timeout=1)
        if self.recvThread is not None:
            self.recvThread.finish()
            self.recvThread.join(timeout=2)
        if self.sendThread is not None:
            self.sendThread.finish()
            self.sendThread.join(timeout=2)
        if self.pushDispacher is not None:
            self.pushDispacher.finish()

    def run(self):
        try:
            self.keepWorking = True
            self.logger.debug(self.name + ":start thread-->")
            while self.keepWorking:
                try:
                    # try to reconnect
                    self.reconnect()
                    responsepack = self.recvThread.recvQueue.get_nowait()  # pop msg from queue
                    if responsepack is None and self.keepWorking:
                        time.sleep(0.001)
                        continue
                    emitter_str = ChannelHandler.getEmitterStr(self.onResponsePrefix,
                                                               responsepack.seq, responsepack.type)
                    if emitter_str in self.requests:
                        self.lock.acquire()
                        self.callbackEmitter.emit(emitter_str, responsepack)
                        self.lock.release()
                    else:
                        # 并非客户端指定的等待接受的来自节点的包，可能是push来的消息，包括amop，event push等
                        # 独立接口处理
                       # print("push  type ",hex(responsepack.type) )
                        self.pushDispacher.push(responsepack)
                except Empty:
                    time.sleep(0.001)
        except Exception as e:
            self.logger.error("{} recv error {}".format(self.name, e))
        finally:
            self.logger.debug("{}:thread finished ,keepWorking = {}".format(
                self.name, self.keepWorking))

    def start_connect(self):
        self.ssock.try_connect(self.host, self.port)
        self.socketClosed = False

    def start_channel(self, host, port):
        try:
            self.socketClosed = False
            self.keepWorking = False
            self.host = host
            self.port = port
            self.start_connect()
            # print("start send thread")
            self.sendThread = ChannelSendThread(self)
            self.sendThread.setDaemon(True)
            # print("start read thread")
            self.recvThread = ChannelRecvThread(self)
            self.recvThread.setDaemon(True)
            self.recvThread.start()

            self.sendThread.start()
            self.pushDispacher = ChannelPushDispatcher()
            self.pushDispacher.setDaemon(True)
            self.pushDispacher.start()
            super().start()
        except Exception as e:
            raise ChannelException(("start channelHandler Failed for {},"
                                    " host: {}, port: {}").format(e,
                                                                  self.host, self.port))

    def decode_rpc_response(self, response):
        text_response = to_text(response)
        return FriendlyJsonSerde().json_decode(text_response)

    def encode_rpc_request(self, method, params):
        rpc_dict = {
            "jsonrpc": "2.0",
            "method": method,
            "params": params or [],
            "id": next(self.request_counter),
        }
        encoded = FriendlyJsonSerde().json_encode(rpc_dict)
        return to_bytes(text=encoded)

    '''
    result:
    0	成功
    100	节点不可达
    101	SDK不可达
    102	超时
    '''
    errorMsg = dict()
    errorMsg[0] = "success"
    errorMsg[100] = "node unreachable"
    errorMsg[101] = "sdk unreachable"
    errorMsg[102] = "timeout"

    def make_channel_request(self, data, packet_type,
                             response_type=None):
        seq = ChannelPack.make_seq32()
        request_pack = ChannelPack(packet_type, seq, 0, data)
        self.send_pack(request_pack)
        onresponse_emitter_str = ChannelHandler.getEmitterStr(self.onResponsePrefix,
                                                              seq, response_type)
        # register onResponse emitter
        self.lock.acquire()
        self.callbackEmitter.on(onresponse_emitter_str, self.onResponse)
        self.lock.release()
        self.requests.append(onresponse_emitter_str)

        # register onResponse emitter of RPC
        rpc_onresponse_emitter_str = None
        rpc_result_emitter_str = None
        if response_type is ChannelPack.TYPE_TX_COMMITTED \
                or response_type is ChannelPack.CLIENT_REGISTER_EVENT_LOG:
            rpc_onresponse_emitter_str = ChannelHandler.getEmitterStr(self.onResponsePrefix,
                                                                      seq, packet_type)
            self.requests.append(rpc_onresponse_emitter_str)
            rpc_result_emitter_str = ChannelHandler.getEmitterStr(self.getResultPrefix,
                                                                  seq, packet_type)
            self.lock.acquire()
            self.callbackEmitter.on(rpc_onresponse_emitter_str, self.onResponse)
            self.lock.release()
        emitter_str = ChannelHandler.getEmitterStr(self.getResultPrefix,
                                                   seq, response_type)

        def resolve_promise(resolve, reject):
            """
            resolve promise
            """
            # register getResult emitter
            self.lock.acquire()
            self.callbackEmitter.on(emitter_str, (lambda result, is_error: resolve(result)))

            # 1. if send transaction failed, return the error message directly
            #    and erase the registered 0x1002 emitter
            # 2. if send transaction success, remove the registered 0x12 emitter
            if rpc_result_emitter_str is not None:
                self.callbackEmitter.on(
                    rpc_result_emitter_str,
                    (lambda result, is_error:
                     resolve(result) and self.requests.remove(onresponse_emitter_str)
                     if is_error is True else self.requests.remove(rpc_onresponse_emitter_str)
                     if self.requests.count(rpc_onresponse_emitter_str) else None))
            self.lock.release()
        p = Promise(resolve_promise)
        # default timeout is 60s
        return p.get(60)

    def make_channel_rpc_request(self, method, params, packet_type=ChannelPack.TYPE_RPC,
                                 response_type=ChannelPack.TYPE_RPC):
        rpc_data = self.encode_rpc_request(method, params)
        #self.logger.debug("request rpc_data : {}".format(rpc_data))
        return self.make_channel_request(rpc_data, packet_type, response_type)

    def setBlockNumber(self, blockNumber):
        """
        init block number
        """
        self.blockNumber = blockNumber

    def getBlockNumber(self, groupId):
        """
        get block number notify
        """
        block_notify_emitter = ChannelHandler.getEmitterStr(self.onResponsePrefix,
                                                            ChannelPack.get_seq_zero(),
                                                            ChannelPack.TYPE_TX_BLOCKNUM)
        self.callbackEmitter.on(block_notify_emitter, self.onResponse)
        self.logger.debug("block notify emitter: {}".format(block_notify_emitter))
        self.requests.append(block_notify_emitter)
        seq = ChannelPack.make_seq32()
        topic = json.dumps(["_block_notify_{}".format(groupId)])
        request_pack = ChannelPack(ChannelPack.TYPE_TOPIC_REPORT, seq, 0, topic)
        self.send_pack(request_pack)

    @staticmethod
    def getEmitterStr(prefix, seq, response_type):
        """
        get emitter str
        """
        return "{}_{}_{}".format(prefix, str(seq), str(response_type))

    def onResponse(self, responsepack):
        """
        obtain the response of given type
        """
        result = responsepack.result
        data = responsepack.data.decode("utf-8")
        # get onResponse emitter
        onresponse_emitter = ChannelHandler.getEmitterStr(self.onResponsePrefix,
                                                          responsepack.seq, responsepack.type)
        if onresponse_emitter in self.requests and responsepack.type != ChannelPack.TYPE_TX_BLOCKNUM:
            self.requests.remove(onresponse_emitter)

        emitter_str = ChannelHandler.getEmitterStr(self.getResultPrefix,
                                                   responsepack.seq, responsepack.type)
        self.logger.debug("onResponse, emitter: {}".format(emitter_str))
        if result != 0:
            self.logger.error("response from server failed , seq: {}, type:{}, result: {}".
                              format(responsepack.seq, responsepack.type, result))
            self.callbackEmitter.emit(emitter_str, result, True)
            return
        try:
            # json packet
            if responsepack.type == ChannelPack.TYPE_RPC or \
                    responsepack.type == ChannelPack.TYPE_TX_COMMITTED:
                response = FriendlyJsonSerde().json_decode(data)
                response_item = None
                error_status = False
                if 'error' in response.keys():
                    error_status = True
                    response_item = dict()
                    response_item["result"] = response
                elif "result" not in response.keys():
                    response_item = dict()
                    response_item["result"] = response
                else:
                    response_item = response

                self.callbackEmitter.emit(emitter_str, response_item, error_status)
                self.logger.debug("response from server , seq: {}, type:{}".
                                  format(responsepack.seq, responsepack.type))
            # block notify
            elif responsepack.type == ChannelPack.TYPE_TX_BLOCKNUM:
                number = int(data.split(',')[1], 10)
                self.logger.debug("receive block notify: seq: {} type:{}, data:{}".
                                  format(responsepack.seq, responsepack.type, data))
                if self.blockNumber < number:
                    self.blockNumber = number
                self.logger.debug("currentBlockNumber: {}".format(self.blockNumber))
            elif responsepack.type == ChannelPack.CLIENT_REGISTER_EVENT_LOG:
                self.logger.debug("receive event register result: seq: {} type:{}".
                                  format(responsepack.seq, responsepack.type))
                #print("receive event register result: seq: {} type:{}".format(responsepack.seq, responsepack.type))
                self.callbackEmitter.emit(emitter_str, responsepack.data, 0)
            elif responsepack.type == ChannelPack.EVENT_LOG_PUSH:
                print("event log push:", responsepack.data)
        except Exception as e:
            self.logger.error("decode response failed, seq:{}, type:{}, error info: {}"
                              .format(responsepack.seq, responsepack.type, e))
            error_msg = "decode response failed, seq:{}, type:{}, message: {}".format(
                responsepack.seq, responsepack.type, result)
            self.callbackEmitter.emit(emitter_str, error_msg, True)

    def send_pack(self, pack):
        if self.sendThread.packQueue.full():
            self.logger.error("channel send Queue full!")
            raise BcosError(-1, None, "channel send Queue full!")
        self.sendThread.packQueue.put(pack)

# --------------------------------------------------------------------
# --------------------------------------------------------------------
# thread: channel reading
# --------------------------------------------------------------------
# --------------------------------------------------------------------


class ChannelRecvThread(threading.Thread):
    QUEUE_SIZE = 1024 * 1024 * 10
    channelHandler = None
    keepWorking = True
    # threadLock = threading.RLock()
    logger = None

    def __init__(self, handler, name="ChannelRecvThread"):
        threading.Thread.__init__(self)
        # self.threadID = threadID
        self.name = name
        self.recvQueue = queue.Queue(ChannelRecvThread.QUEUE_SIZE)
        self.channelHandler = handler
        self.logger = handler.logger

    respbuffer = bytearray()  # a buffer append by read, consume by decode

    def read_channel(self):
        # 接收服务端返回的信息
        try:
            self.logger.debug("{} channelHandler.ssock.recv begin.".format(self.name))
            msg = self.channelHandler.ssock.recv(1024 * 1024 * 10)
            self.logger.debug("channelHandler.ssock.recv len:{}".format(len(msg)))
            if msg is None:
                return -1
            if len(msg) == 0:
                return 0
        except Exception as e:
            self.logger.error("{}:ssock read error {}".format(self.name, e))
            # print("{}:ssock read error {}".format(self.name, e))
            return -2
        self.respbuffer += msg
        # if no enough data even for header ,continue to read
        if len(self.respbuffer) < ChannelPack.getheaderlen():
            return len(msg)

        code = 0
        # decode all packs in buffer from node,maybe got N packs on one read
        # -1 means no enough bytes for decode, should break to  continue read and wait
        while code != -1:
            (code, decodelen, responsePack) = ChannelPack.unpack(bytes(self.respbuffer))
            # print("respbuffer:",self.respbuffer)
            if decodelen > 0:
                # cut the buffer from last decode  pos
                self.respbuffer = self.respbuffer[decodelen:]
            if code != -1 and responsePack is not None:  # got a pack
                # print("get a pack from node, put to queue {}".format(responsePack.detail()))
                self.logger.debug("{}:pack from node, put queue(qsize{}),detail {}".format(
                    self.name, self.recvQueue.qsize(), responsePack.detail()))
                if self.recvQueue.full():
                    self.recvQueue.get()  # if queue full ,pop the head item ,!! the item LOST
                    self.logger.error("{}:queue {} FULL pop and LOST: {}".format(
                        self.name, responsePack.type, responsePack.detail()))
                self.recvQueue.put(responsePack)
                # self.print_queue()

        return len(msg)

    def finish(self):
        self.keepWorking = False

    def run(self):
        # lockres = ChannelRecvThread.threadLock.acquire(blocking=False)
        # if lockres is False:  # other thread has got the lock and running
        # print(self.name + ":other thread has got the lock and running ")
        #    self.logger.error(self.name + ":other thread has got the lock and running ")
        #    return
        try:
            self.keepWorking = True
            self.logger.debug(self.name + ":start thread-->")
            while self.keepWorking:
                if self.channelHandler.socketClosed is True or self.channelHandler.ssock is None:
                    time.sleep(0.001)
                bytesread = self.read_channel()
                if self.keepWorking is False:
                    break
                if bytesread == 0:  # if async read, maybe return 0
                    time.sleep(0.01)
                if bytesread < 0 and self.keepWorking is True:  # error accord when read
                    self.channelHandler.disconnect()
        except Exception as e:
            self.logger.error("{} recv error {}".format(self.name, e))
            if self.keepWorking is True:
                self.channelHandler.disconnect()

        finally:
            self.logger.debug("{}:thread finished ,keepWorking = {}".format(
                self.name, self.keepWorking))
            # ChannelRecvThread.threadLock.release()

# -----------------------------------------------------------
# -----------------------------------------------------------
# send thread begin
# -----------------------------------------------------------
# -----------------------------------------------------------


class ChannelSendThread(threading.Thread):
    QUEUE_SIZE = 1024 * 1024 * 10
    channelHandler = None
    packQueue = None
    keepWorking = True
    # threadLock = threading.RLock()
    heatbeatStamp = 3  # heatbeat very N sec
    logger = None

    def sendpack(self, pack):
        if self.packQueue.full():
            raise BcosError(-1, None, "sendThread Queue full")
        self.packQueue.put(pack)

    def __init__(self, handler, name="ChannelSendThread"):
        threading.Thread.__init__(self)
        self.name = "channelSendThread"
        self.channelHandler = handler
        self.packQueue = queue.Queue(ChannelSendThread.QUEUE_SIZE)
        self.logger = handler.logger

    lastheatbeattime = time.time()

    def check_heatbeat(self):
        if time.time() - self.lastheatbeattime < self.heatbeatStamp:
            return
        pack = ChannelPack(ChannelPack.TYPE_HEATBEAT,
                           ChannelPack.make_seq32(), 0, bytes("", "utf-8"))
        self.sendpack(pack)
        self.lastheatbeattime = time.time()

    def finish(self):
        self.keepWorking = False

    def run(self):
        # lockres = ChannelSendThread.threadLock.acquire(blocking=False)
        # if lockres is False:  # other thread has got the lock and running
        #    print(self.name + ":other thread has got the lock and running ")
        #    return
        try:
            self.keepWorking = True
            self.logger.debug(self.name + ":start thread-->")
            while self.keepWorking:
                if self.channelHandler.socketClosed is True or self.channelHandler.ssock is None:
                    time.sleep(0.001)
                try:
                    pack = self.packQueue.get(block=True, timeout=0.2)
                except Empty:
                    self.check_heatbeat()
                    continue
                self.lastheatbeattime = time.time()  # reset heatbeat time
                self.logger.debug("{} send pack {}".format(self.name, pack.detail()))
                # print("{} send pack {}".format(self.name,pack.detail()))
                buffer = pack.pack()
                try:
                    res = self.channelHandler.ssock.send(buffer)
                    if res < 0 and self.keepWorking is True:
                        self.logger.error(
                            "{}:ssock send error {}, disconnect".format(self.name, res))
                        self.channelHandler.disconnect()
                except Exception as e:
                    self.logger.error("{}:ssock send error {}".format(self.name, e))
                    if self.keepWorking is True:
                        self.channelHandler.disconnect()

        except Exception as e:
            self.logger.error("{}:ssock send error {}".format(self.name, e))
            # self.logger.error(traceback.format_exc())
        finally:
            self.logger.debug("{}:thread finished ,keepWorking = {}".format(
                self.name, self.keepWorking))
            # ChannelSendThread.threadLock.release()
