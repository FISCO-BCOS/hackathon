#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
  @author: kentzhang
  @date: 2021-03
'''

# python + ctypes 调用 native_tassl_sock_wrap 库, 通过库里的c接口调用TasllSockWrap


import ctypes
import os
import platform
from ctypes import *
import time

ECHO_NONE = 0x0000
ECHO_PRINTF = 0x0001
ECHO_LOG = 0x0010

# 一系列的接口参数和返回值定义
# 对void类型的返回值，可以直接忽略
FN_ssock_create = {}
FN_ssock_create["name"] = "ssock_create"
FN_ssock_create["argtypes"] = []
FN_ssock_create["restype"] = ctypes.c_void_p

FN_ssock_release = {}
FN_ssock_release["name"] = "ssock_release"
FN_ssock_release["argtypes"] = [ctypes.c_void_p]
FN_ssock_release["restype"] = ctypes

FN_ssock_init = {}
FN_ssock_init["name"] = "ssock_init"
FN_ssock_init["argtypes"] = [ctypes.c_void_p, ctypes.c_char_p, ctypes.c_char_p, ctypes.c_char_p, ctypes.c_char_p,
                             ctypes.c_char_p]
FN_ssock_init["restype"] = ctypes.c_int

FN_ssock_finish = {}
FN_ssock_finish["name"] = "ssock_finish"
FN_ssock_finish["argtypes"] = [ctypes.c_void_p]
FN_ssock_finish["restype"] = []

FN_ssock_set_echo_mode = {}
FN_ssock_set_echo_mode["name"] = "ssock_set_echo_mode"
FN_ssock_set_echo_mode["argtypes"] = [ctypes.c_void_p, ctypes.c_int]
FN_ssock_set_echo_mode["restype"] = []

FN_ssock_try_connect = {}
FN_ssock_try_connect["name"] = "ssock_try_connect"
FN_ssock_try_connect["argtypes"] = [ctypes.c_void_p, ctypes.c_char_p, ctypes.c_int]
FN_ssock_try_connect["restype"] = ctypes.c_int

FN_sscok_recv = {}
FN_sscok_recv["name"] = "ssock_recv"
FN_sscok_recv["argtypes"] = [ctypes.c_void_p, ctypes.c_char_p, ctypes.c_int]
FN_sscok_recv["restype"] = ctypes.c_int

FN_sscok_send = {}
FN_sscok_send["name"] = "ssock_send"
FN_sscok_send["argtypes"] = [ctypes.c_void_p, ctypes.c_char_p, ctypes.c_int]
FN_sscok_send["restype"] = ctypes.c_int


# 用dl load的方式封装接口，在windows、linux平台实测通过
class NativeTasslSockWrap:
    libname_win = 'native_tassl_sock_wrap.dll'
    libpath_win = "cpp_win"
    libname_linux = "libnative_tassl_sock_wrap.so"
    libpath_linux = "cpp_linux"

    target_libname = ""
    target_libpath = ""

    target_platform = "unknown"

    nativelib = None
    ssock: ctypes.c_void_p = None

    def __init__(self):
        # 先处理平台兼容性
        target_platform = "unknown"
        platsys = platform.platform()
        if platsys.lower().startswith("win"):
            self.target_platform = "win64"
            self.target_libname = self.libname_win
            self.target_libpath = self.libpath_win
        if "linux" in platsys.lower():
            self.target_platform = "linux"
            self.target_libname = self.libname_linux
            self.target_libpath = self.libpath_linux

            # print(self.target_libpath)
        self.load_lib()

    def load_lib(self):
        # os.add_dll_directory(module_path)
        # print("load_lib ",self.nativelib)
        if self.nativelib is not None:
            return self.nativelib
        currpath = os.getcwd()
        # print( "load_lib currpath ",currpath)
        read_lib_name = os.path.join(currpath, self.target_libname)
        module_path = os.path.dirname(os.path.realpath(__file__))
        if not os.path.exists(read_lib_name):
            # 当前运行路径未找到，继续找
            # print("lib not found in cwd",read_lib_name)
            read_lib_name = os.path.join(module_path, self.target_libname)
        if not os.path.exists(read_lib_name):
            # 当前模块位置未找到，试图加上相对路径再来一次
            # print("lib not found in module_path:",read_lib_name)
            read_lib_name = os.path.join(module_path, self.target_libpath, self.target_libname)

        print("at last native_tassl_sock_wrap load the nativelib:", read_lib_name)
        # print(self.target_platform)
        if self.target_platform.lower().startswith("win"):
            # print(os.path.dirname(read_lib_name))
            os.add_dll_directory(os.path.dirname(read_lib_name))
        # self.nativelib = cdll.LoadLibrary(read_lib_name) 
        # print("test as RTLD_LOCAL",ctypes.RTLD_LOCAL)
        # print("test as RTLD_GLOBAL",ctypes.RTLD_GLOBAL)
        self.nativelib = PyDLL(read_lib_name,ctypes.RTLD_GLOBAL)
        if self.nativelib is None:
            return -1
        # python里，对c语言的库 ,需要显式定义入参和出参，否则ctypes可能会出错
        self.nativelib.ssock_create.argtypes = FN_ssock_create["argtypes"]
        self.nativelib.ssock_create.restype = FN_ssock_create["restype"]

        self.nativelib.ssock_release.argtypes = FN_ssock_release["argtypes"]
        # self.nativelib.ssock_release.restype  = FN_ssock_release["restype"]

        self.nativelib.ssock_init.argtypes = FN_ssock_init["argtypes"]
        self.nativelib.ssock_init.restype = FN_ssock_init["restype"]

        self.nativelib.ssock_finish.argtypes = FN_ssock_finish["argtypes"]
        # self.nativelib.ssock_finish.restype  = FN_ssock_finish["restype"]

        self.nativelib.ssock_set_echo_mode.argtypes = FN_ssock_set_echo_mode["argtypes"]
        # self.nativelib.ssock_set_echo_mode.restype  = FN_ssock_set_echo_mode["restype"]

        self.nativelib.ssock_try_connect.argtypes = FN_ssock_try_connect["argtypes"]
        self.nativelib.ssock_try_connect.restype = FN_ssock_try_connect["restype"]

        self.nativelib.ssock_recv.argtypes = FN_sscok_recv["argtypes"]
        self.nativelib.ssock_recv.restype = FN_sscok_recv["restype"]

        self.nativelib.ssock_send.argtypes = FN_sscok_send["argtypes"]
        self.nativelib.ssock_send.restype = FN_sscok_send["restype"]

        self.ssock = self.nativelib.ssock_create()
        # print("ssock_create result {},type:{}\n".format(self.ssock,type(self.ssock) ) )
        if self.ssock is None:
            return -2

        return 0

    def set_echo_mode(self, mode):
        self.nativelib.ssock_set_echo_mode(self.ssock, mode)

    def init(self, ca_file, node_crt_file, node_key_file,
             en_crt_file=None,
             en_key_file=None):
        return self.nativelib.ssock_init(self.ssock,
                                         ca_file.encode("UTF-8"),
                                         node_crt_file.encode("UTF-8"),
                                         node_key_file.encode("UTF-8"),
                                         en_crt_file.encode("UTF-8"),
                                         en_key_file.encode("UTF-8"))

    def try_connect(self, host=None, port=0):
        retval = self.nativelib.ssock_try_connect(self.ssock, host.encode("UTF-8"), port)
        return retval

    def recv(self, recvsize=None) :
        recvsize = 10 * 10 * 1024
        buffer = ctypes.create_string_buffer(recvsize)
        retval = self.nativelib.ssock_recv(self.ssock, buffer, recvsize)
        # print("recv,retval={},len = {}".format(retval,len(buffer.raw)) )
        retbuffer = b''
        if retval > 0:
            retbuffer = buffer[:retval]
        return retbuffer

    def send(self, buffer, bufferlen=None):
        if type(buffer) == str:
            buffer = buffer.encode("utf-8")
        bufflen = len(buffer)
        retval = -1
        for i in range(0,3):
            retval = self.nativelib.ssock_send(self.ssock, buffer, bufflen)
            if retval > 0:
                break
            time.sleep(0.1)
        return retval

    def release(self):
        if self.ssock is None:
            return
        self.nativelib.ssock_release(self.ssock)
        self.ssock = None

    def finish(self):
        self.nativelib.ssock_finish(self.ssock)

