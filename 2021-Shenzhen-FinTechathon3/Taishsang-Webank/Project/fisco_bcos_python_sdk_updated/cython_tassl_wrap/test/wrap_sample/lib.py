#!/usr/bin/env python
import ctypes
import os
from ctypes import *
print("testing")
dllname='kernel32.dll'
dll = windll.LoadLibrary(dllname)
print("load dll",dll)
currpath = os.path.dirname(os.path.realpath(__file__) )
print(currpath)
os.add_dll_directory(currpath)
dllname="./native_lib.dll"
dll = windll.LoadLibrary(dllname)
print("load dll",dll)

a=dll.testprint(b"abcdefg\n")
print(type(a))
print(a)
ret=c_int(0)
param=c_int(199);

pt = ctypes.c_void_p

dll.create.restype  = ctypes.c_void_p
pt = dll.create(param,pointer(ret))
print("create ret {},pt {}:type{}".format(ret.value,pt,type(pt)))
dll.call.argtypes =[ctypes.c_void_p,ctypes.c_char_p]
ret = dll.call(pt,b"this is a test ");
print("call: ",ret)
dll.release.argtypes=[ctypes.c_void_p]
dll.release(pt);


