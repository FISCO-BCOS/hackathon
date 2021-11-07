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
  @date: 2021-02
'''



from collections import Iterable

from client.bcoserror import ArgumentsError
from eth_utils import to_checksum_address
import re

'''
此文件里的实现的主要目的是把控制台输入的纯字符串，按abi进行解析，转换成python的内部类型，以便传递给python abi编码器
sdk里的abi编解码器接受python类型的int,bool,string,array,tuple等等
如控制台输入 ‘abc' 99 ，实际上99也是字符串 ’99‘，根据abi:(string,uint256)定义转换后，就成了['abc',99]这样的数组，其中99是int了
对于tuple如('alice',99), 会先切分为['alice','99']这样的字符串数组，然后根据abi，解析成python里的('alice',99)这样的tuple类型
'''
def remove_pre_su_fix(item: str):
    item = item.strip(" ")
    item = item.rstrip(" ")
    item = item.strip("'")
    item = item.rstrip("'")
    item = item.strip("\"")
    item = item.rstrip("\"")
    return item

'''
对较为复杂的输入参数，用split分割不可行， split_param支持以下格式
1,2,3
['a','b','c']
['a',('alice',23)] ->tuple，对应合约结构体,内部包含,
[('alice',23),('bob',28)] -> tuple结构体数组
’ab,cd' -> 字符串本身包含逗号,
'ab\\'cd' -> 字符串包含转义符
'''
def split_param(inputparam:str):
    splitter = ','
    item = ""
    stopchar = ''
    arrayres=[]
    status = 0
    stopchardict = dict()
    #设置几个常见的成对出现的范围符号，如'',"",(),[],{}
    stopchardict['\'']='\''
    stopchardict['"'] = '"'
    stopchardict['('] = ')'
    stopchardict['['] = ']'
    stopchardict['{'] = '}'
    i=0
    oldstatus =0
    for i in range(0,len(inputparam)):
        c = inputparam[i]
        #print("c:[{}],itemis [{}],status {}".format(c,item,status) )
        if c == '\\': #转义字符\ ，跳过它，下一个字符默认纳入
            #print("skip [{}]".format(c))
            oldstatus =  status
            status = 2 #默认纳入下一个字符
            continue
        #print(c)
        if status ==0:
            if c == splitter: # 遇到分隔符,
                arrayres.append(item)
                item = ""
                continue
            if c in stopchardict:
                stopchar = stopchardict[c]
                status = 1 #status =1意思是这一段字符串必须到配对的stopchar才结束
            item = item + c
            continue
        if status == 1: #遇到配对的停止符才允许分割，在中间的,不做为分隔符
            item = item + c
            if c==stopchar:
                stopchar=''
                status = 0
            continue
        if status == 2: #转义符后面的一个字符直接加入
            status = oldstatus
            item = item + c
            continue

    arrayres.append(item)
    return arrayres


def parse_input_array_str(inputstr: str):
    # 用json解析有点问题，命令行输入json时，会转换掉"双引号，所以自行实现
    inputstr = remove_pre_su_fix(inputstr)
    inputstr = inputstr.strip("[")
    inputstr = inputstr.rstrip("]")
    #tempitems = inputstr.split(",")
    tempitems = split_param(inputstr)
    #print("tempitems",tempitems)
    stritems = []
    for item in tempitems:
        item = remove_pre_su_fix(item)
        #print("item in params:",item)
        #(isarray,lenof) = is_array_param(item) #数组嵌套数组,不要支持了算了
        #if isarray:
        #    item = parse_input_array_str(item)
        stritems.append(item)
    #print("stritems",stritems)
    return stritems


def parse_input_tuple_str(inputstr: str):
    # 用json解析有点问题，命令行输入json时，会转换掉"双引号，所以自行实现
    inputstr = remove_pre_su_fix(inputstr)
    inputstr = inputstr.strip("(")
    inputstr = inputstr.rstrip(")")
    tempitems = split_param(inputstr)
    stritems = []
    for item in tempitems:
        item = remove_pre_su_fix(item)
        if is_tuple_param(item):
            item = parse_input_tuple_str(item)
        stritems.append(item)
    tupleitems = tuple(stritems)
    #print("tupleitems:",tupleitems)
    return tupleitems


def format_single_param(param, abitype):
    # 默认是string
    #print("format_single_param",abitype)
    fmt_res = param
    # print(type(param))
    abitype = abitype.lower()
    if is_tuple_param(abitype):
        fmt_res = format_tuple_args_by_abi(param,abitype)
        return fmt_res

    if "int" in abitype or "int256" in abitype:
        if not isinstance(param, int):
            fmt_res = int(param, 10)
        return fmt_res

    if "address" in abitype:
        try:
            fmt_res = to_checksum_address(param)
        except ArgumentsError as e:
            raise ArgumentsError(("ERROR >> covert {} to to_checksum_address failed,"
                                  " exception: {}").format(param, e))
        return fmt_res
    if "bytes" in abitype:
        try:
            fmt_res = bytes(param, "utf-8")
        except Exception as e:
            raise ArgumentsError(
                "ERROR >> parse {} to bytes failed, error info: {}".format(param, e))
        return fmt_res
    if "bool" in abitype:
        # 有可能是string或已经是bool类型了，判断下
        if isinstance(param, str):
            if "TRUE" == param.upper():
                fmt_res = True
            elif "FALSE" == param.upper():
                fmt_res = False
            else:
                raise ArgumentsError(
                    "ERROR >> format bool type failed, WRONG param: {}".format(param))
        return fmt_res
    #default is string,just return it
    #print("fmt_res {},abi {}".format(fmt_res,abitype))
    return fmt_res

def format_tuple_args_by_abi(input_param,abitypestr):
    #print("format_tuple_args_by_abi param",input_param)
    abitype = parse_input_tuple_str(abitypestr)
    #print("format_tuple_args_by_abi abitype",abitype)

    paramtuple= parse_input_tuple_str(input_param)
    #print("format_tuple_args_by_abi tuple",paramtuple)
    arraylen = len(abitype)
    #print(arraylen)
    #print(len(paramtuple))
    if arraylen > 0 and len(paramtuple) != arraylen:
        raise ArgumentsError("ERROR >> not match abi array size {}, params: {}"
                             .format(abitype, paramtuple))
    resarray = []
    #print(paramtuple)
    i = 0
    for param in paramtuple:
        fmt_res = format_single_param(param, abitype[i])
        i+=1
        resarray.append(fmt_res)
    #print("resarray",resarray)
    restuple = tuple(resarray)
    return restuple

def format_array_args_by_abi(input_param, abitype, arraylen):
    # abi类型类似address[],string[],int256[]
    # 参数类似['0x111','0x2222'],[1,2,3],['aaa','bbb','ccc']
    #print("format_array_args_by_abi",abitype)
    paramarray = parse_input_array_str(input_param)
    if arraylen > 0 and len(paramarray) != arraylen:
        raise ArgumentsError("ERROR >> not match abi array size {}, params: {}"
                             .format(abitype, paramarray))
    resarray = []
    # print(paramarray)
    for param in paramarray:
        #print("params {}, abitype {}".format(param,abitype))
        fmt_res = format_single_param(param, abitype)
        resarray.append(fmt_res)
    return resarray


def is_array_param(abitype):
    matchpattern = r"\[(.*?)\]"
    findres = re.findall(matchpattern, abitype)
    if len(findres) == 0:
        return (False, 0)
    lendef = findres[0].strip()

    if len(lendef) == 0:
        # means address[]
        arraylen = 0
    else:
        # means address[3]
        arraylen = int(lendef, 10)
    return (True, arraylen)

def is_tuple_param(input):
    #print("is_tuple_param",input)

    if input.startswith("("):
        return True
    return False


def format_args_by_function_abi(inputparams, inputabi):
    #print("inputparams",inputparams)
    #print("inputabi",inputabi)
    paramformatted = []
    index = -1
    if len(inputparams) != len(inputabi):
        raise ArgumentsError(("Invalid Arguments {}, expected params size: {},"
                              " inputted params size: {}".format(inputparams,
                                                                 len(inputabi),
                                                                 len(inputparams))))

    for abi_item in inputabi:
        #print("process ",abi_item)
        index += 1
        param = inputparams[index]
        #print("param:",param)
        if param is None:
            continue
        if isinstance(param, Iterable) is False:
            #print("is Iterable false")
            paramformatted.append(param)
            continue
        #   abitype = abi_item["type"]
        (isarray, arraylen) = is_array_param(abi_item)
        #print("is array", isarray)
        istuple = is_tuple_param(abi_item)
        #print("is tuple",istuple)
        if isarray:
            # print("is Array")
            param = format_array_args_by_abi(param, abi_item, arraylen)
            paramformatted.append(param)
            continue
        if istuple:
            param = format_tuple_args_by_abi(param, abi_item)
            paramformatted.append(param)
            continue
        if '\'' in param:
            param = param.replace('\'', "")
        param = format_single_param(param, abi_item)
        paramformatted.append(param)
    return paramformatted


doTestregex = False
if doTestregex:
    abi = "address[3]"
    res = is_array_param(abi)
    print("abi : {}, res : {} ".format(abi, res))
    abi = "address[]"
    res = is_array_param(abi)
    print("abi : {}, res : {} ".format(abi, res))
    abi = "address[ ]"
    res = is_array_param(abi)
    print("abi : {}, res : {} ".format(abi, res))
    abi = "address[ 10 ]"
    res = is_array_param(abi)
    print("abi : {}, res : {} ".format(abi, res))
    abi = "address[ x ]"
    res = is_array_param(abi)
    print("abi : {}, res : {} ".format(abi, res))

doTest = False
if doTest:
    matchpattern = r"\[(.*?)\]"
    res = re.findall(matchpattern, "address[]")
    print(res)
    res = re.findall(matchpattern, "address[3]")
    print(res)
    # 数组参数需要加上中括号，比如[1, 2, 3]，数组中是字符串或字节类型，加双引号或单引号，例如[“alice”, ”bob”]，注意数组参数中不要有空格；布尔类型为true或者false。
    strarrayparam = "[\"aaa\",\"bbb\",\"ccc\"]"
    intarrayparam = "[1,2,3]"
    boolarrayparam = "[true,true,false]"
    boolstrarrayparam = "[\"True\",\"false\",\"true\"]"
    addrarrayparam = "[\"0x7029c502b4f824d19bd7921e9cb74ef92392fb1b\"," \
                     "\"0x9029c502b4f824d19bd7921e9cb74ef92392fb1b\"," \
                     "\"0xa029c502b4f824d19bd7921e9cb74ef92392fb1b\"]"
    res = format_array_args_by_abi(addrarrayparam, "address[]")
    print(res)
    res = format_array_args_by_abi(intarrayparam, "int256[]")
    print(res)
    res = format_array_args_by_abi(strarrayparam, "string[]")
    print(res)
    res = format_array_args_by_abi(boolarrayparam, "bool[]")
    print(res)
    res = format_array_args_by_abi(boolstrarrayparam, "bool[]")
    print(res)

    from client.datatype_parser import DatatypeParser

    parser = DatatypeParser("contracts/NoteGroup.abi")
    fn_abi = parser.get_function_inputs_abi("set_addr")
    fmt_args = format_args_by_function_abi([addrarrayparam, "testing"], fn_abi)
    print(fmt_args)
