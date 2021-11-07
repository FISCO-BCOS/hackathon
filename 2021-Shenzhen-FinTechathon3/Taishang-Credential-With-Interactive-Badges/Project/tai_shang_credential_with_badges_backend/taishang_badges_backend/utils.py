# -*- coding: utf-8 -*-
"""Helper utilities and decorators."""
try:
    from urlparse import urlparse, urljoin
except ImportError:
    from urllib.parse import urlparse, urljoin

from flask import request, redirect, url_for, current_app

from flask import flash
import hashlib
import base64
# import sha3
from ecdsa import SigningKey, SECP256k1
import os
from eth_account import Account
# import ipfshttpclient


from socket import *
from time import ctime, sleep

import random
import threading
import time
from multiprocessing import Process, Pipe

def flash_errors(form, category="warning"):
    """Flash all errors for a form."""
    for field, errors in form.errors.items():
        for error in errors:
            flash(f"{getattr(form, field).label.text} - {error}", category)

def is_safe_url(target):
    ref_url = urlparse(request.host_url)
    test_url = urlparse(urljoin(request.host_url, target))
    return test_url.scheme in ('http', 'https') and ref_url.netloc == test_url.netloc


def redirect_back(default='public.home', **kwargs):
    for target in request.args.get('next'), request.referrer:
        if not target:
            continue
        if is_safe_url(target):
            return redirect(target)
    return redirect(url_for(default, **kwargs))


def ecdsa_sign(self, encode_transaction, privkey, hashfunc=hashlib.sha256):
    signning_key = SigningKey.from_string(bytes.fromhex(privkey), curve=SECP256k1)
    # encode_transaction = respBody['respBody']['encodedTransaction']
    # base64解密
    transaction = self.base64_decode(encode_transaction)
    # 获取hash
    hashedMsg = self.Hash(transaction)
    bytes_hashed = bytes(bytearray.fromhex(hashedMsg))
    # 签名
    signature = signning_key.sign(bytes_hashed, hashfunc=hashfunc)
    # base64加密
    transaction_encode = self.base64_encode(signature)
    return transaction_encode

def base64_decode(self, base_data):
    """
    base64解密
    :param base_data:
    :return:
    """
    bytes_data = base64.b64decode(base_data)
    return bytes_data

def base64_encode(self, bytes_data):
    """
    base64加密
    :param bytes_data:
    :return:
    """
    base_data = base64.b64encode(bytes_data)
    return bytes.decode(base_data)

def Hash(self, msg):
    """
    hash加密
    :return:
    """
    k = sha3.keccak_256()
    k.update(msg)
    return k.hexdigest()




def generate_addr(priv=None):
    if priv == None:
        account = Account.create()
    else:
        try:
            account = Account.privateKeyToAccount(priv)
        except Exception as e:
            return {"result": "error", "error":e}
    return {"result": "success",
            "payload":
                {"addr": account.address,
                 "priv": account.privateKey.hex(),
                 "pubv": str(account._key_obj.public_key).lower()
                 }}



def create_privkey():
    return os.urandom(32)


def create_address_by_privkey(privkey):
    if privkey[:2] == "0x":
        account = generate_addr(priv=privkey[2:])
    else:
        account = generate_addr(priv=hex(int(privkey))[2:])

    addr = account["payload"]["addr"]
    data = {
        "privateKeyHex": account["payload"]["priv"],
        "publicKeyHex": account["payload"]["pubv"],
        "privateKeyInt": str(int(account["payload"]["priv"], 16)),
        "publicKeyInt": str(int(account["payload"]["pubv"], 16)),
        "address": addr,
    }
    return data






def run_async(func):
    def wrapper(*args, **kwargs):
        thr = threading.Thread(target = func, args = args, kwargs = kwargs)
        thr.start()
        thr.setName("func-{}".format(func.__name__))
        # thr.join()
        print("线程id={},\n线程名称={},\n正在执行的线程列表:{},\n正在执行的线程数量={},\n当前激活线程={}".format(
            thr.ident,thr.getName(),threading.enumerate(),threading.active_count(),thr.isAlive)
        )
    return wrapper

def run_process(func):
    def wrapper(*args, **kwargs):
        parent_conn, child_conn = Pipe()
        p = Process(target=func, args=(child_conn,))
        p.start()
        print(parent_conn.recv())  # prints "[42, None, 'hello']"
        p.join()
    return wrapper


# # coding=utf-8
# import socket
# # 获取本机的ip地址
# def get_addr():
#     # 获取本机计算机名称
#     hostname = socket.gethostname()
#     # 获取本机ip并返回
#     return socket.gethostbyname(hostname)
# # 创建udp套接字,
# # AF_INET表示ip地址的类型是ipv4，
# # SOCK_DGRAM表示传输的协议类型是udp
# udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
# # 绑定本地信息，若不绑定，系统会自动分配
# host = get_addr()
# bind_addr = (host, 8080)
# print('bind_addr = ', bind_addr)
# udp_socket.bind(bind_addr) # ip和port，ip一般不用写，表示本机的任何一个ip
# # 等待接收数据
# revc_data = udp_socket.recvfrom(1024) # 1024表示本次接收的最大字节数
# # 打印接收到的数据
# print('revc_data = ', revc_data)
# print('data = ', revc_data[0])
# print('ip_port = ', revc_data[1])
# # 关闭套接字
# udp_socket.close()
#
# def recv(self):
#     """接收广播"""
#
#     print("UDP接收器启动成功...")
#     while True:
#         # 接收数据格式：(data, (ip, port))
#         recvData = self.recvSocket.recvfrom(1024)
#
#         print("【%s】[%s : %s] : %s" % (ctime(), recvData[1][0], recvData[1][1], recvData[0].decode(self.encoding)))
#
#         sleep(1)



class UDPPlus:
    def __init__(self, port):
        # 全局参数配置
        self.encoding = "utf-8"  # 使用的编码方式
        self.broadcastPort = port   # 广播端口
        self.broadcastHost = self.get_addr() # 广播IP
        # 创建广播接收器
        self.recvSocket = socket(AF_INET, SOCK_DGRAM)
        self.recvSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        self.recvSocket.setsockopt(SOL_SOCKET, SO_BROADCAST, 1)
        self.recvSocket.bind((self.broadcastHost, self.broadcastPort))

        # 创建广播发送器
        self.sendSocket = socket(AF_INET, SOCK_DGRAM)
        self.sendSocket.setsockopt(SOL_SOCKET, SO_BROADCAST, 1)

        # 其他
        self.threads = []

    # 获取本机的ip地址
    def get_addr(self):
        # 获取本机计算机名称
        hostname = gethostname()
        # 获取本机ip并返回
        return gethostbyname(hostname)

    def send(self):
        """发送广播"""

        print("调度节点通信广播器启动成功...")
        # self.sendSocket.sendto("".encode(self.encoding), (self.broadcastHost, self.broadcastPort))
        while True:
            sendData = input("请输入需要发送的消息:")

            self.sendSocket.sendto(sendData.encode(self.encoding), (self.broadcastHost, self.broadcastPort))
            # self.sendSocket.sendto(sendData.encode(self.encoding), ('255.255.255.255', self.broadcastPort))
            # print("【%s】%s:%s" % (ctime(), "我", sendData))

            sleep(1)

    def recv(self):
        """接收广播"""

        print("调度节点通信接收器启动成功...")
        while True:
            # 接收数据格式：(data, (ip, port))
            recvData = self.recvSocket.recvfrom(1024)

            print("【%s】[%s : %s] : %s" % (ctime(), recvData[1][0], recvData[1][1], recvData[0].decode(self.encoding)))

            sleep(1)

    def start(self):
        """启动线程"""

        t1 = threading.Thread(target=self.recv)
        t2 = threading.Thread(target=self.send)
        self.threads.append(t1)
        self.threads.append(t2)

        for t in self.threads:
            t.setDaemon(True)
            t.start()

        while True:
            pass

    def startRecv(self):
        t1 = threading.Thread(target=self.recv)
        self.threads.append(t1)

        for t in self.threads:
            t.setDaemon(True)
            t.start()

        while True:
            pass

    def startSend(self):
        t2 = threading.Thread(target=self.send)
        self.threads.append(t2)

        for t in self.threads:
            t.setDaemon(True)
            t.start()

        while True:
            pass

# if __name__ == "__main__":
#     demo = ChatRoomPlus()
#     demo.start()

# if __name__ == '__main__':
#     priv_key = "18e14a7b6a307f426a94f8114701e7c8e774e7f9a47e2c2035db29a206321725"
#     a = generate_addr()
#     print(a)
#     b = create_privkey()
#     print(b.hex())
#
#     print(priv_key)
    # signning_key = SigningKey.from_string(bytes.fromhex(priv_key), curve=SECP256k1)
    # # signning_key = SigningKey.generate(curve=SECP256k1)
    # privkey = signning_key.to_string()
    # print(privkey)
    # verifing_key = signning_key.get_verifying_key()
    # print(verifing_key)
    # data = "hello, world"
    # print(data)
    # bytes_hashed = str.encode(data)
    # print(bytes_hashed)
    # # 签名
    # signature = signning_key.sign(bytes_hashed, hashfunc=hashlib.sha256)
    # print(signature)
    # print(type(signature))
    # print(signature.hex())
    #
    # result = verifing_key.verify(signature=signature, data=bytes_hashed, hashfunc=hashlib.sha256)
    # print(result)
