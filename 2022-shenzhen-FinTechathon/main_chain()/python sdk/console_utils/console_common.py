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
  @function:
  @author: kentzhang
  @date: 2020-10
'''
import importlib
import glob
from inspect import signature

from client.datatype_parser import DatatypeParser
from client.common import common
import os


def list_contracts():
    """
    list all contractname for call
    """
    return list_files(contracts_dir + "/*.sol")


'''
这些动态调用的方法仅供参考，用于通过模块名找方法，
'''


def getfunction(obj, funcname):
    func = None
    try:
        func = getattr(obj, funcname)
    except BaseException:
        pass
    return func


def getClassbyName(modulename, cname):
    try:
        m = importlib.import_module("." + modulename, "console_utils")
        clazz = getattr(m, cname)
        c = clazz()
        return c
    except BaseException:
        return None


def list_files(file_pattern):
    """
    return list according to file_pattern
    """
    file_list = [f for f in glob.glob(file_pattern)]
    targets = []
    for file in file_list:
        targets.append(os.path.basename(file))
    return targets


def console_run_byname(modulename, classname, cmd, inputparams):
    obj = getClassbyName(modulename, classname)
    if obj is None:
        
        return -1
    return console_run(obj, cmd, inputparams)


def try_usage(obj):
    if getfunction(obj, "usage") is not None:
        return (-1, obj.usage())
    else:
        return (-1, ["cmd is not support yet."])


def console_run(obj, cmd, inputparams):
    # 到这里inputparams应该传给实际调用的方法,就是纯纯的参数了，没有类名和方法名
    func_name = cmd  # 取方法名

    func = getfunction(obj, func_name)
    if func is not None and callable(func):
        sig = signature(func)
        if len(sig.parameters) ==0:
            func()
        else:
            func(inputparams)
        return 0, ''
    else:
        return try_usage(obj)


contracts_dir = "contracts"


def default_abi_file(contractname):
    abi_file = contractname
    if not abi_file.endswith(
        ".abi"
    ):  # default from contracts/xxxx.abi,if only input a name
        abi_file = contracts_dir + "/" + contractname + ".abi"
    return abi_file


def print_receipt_logs_and_txoutput(client, receipt, contractname, parser=None):
    print("INFO >>  transaction from account :", receipt["from"])
    print("INFO >>  receipt logs : ")
    # 解析receipt里的log
    if parser is None and len(contractname) > 0:
        parser = DatatypeParser(default_abi_file(contractname))
    logresult = parser.parse_event_logs(receipt["logs"])
    i = 0
    # print(json.dumps(logresult,indent=4))
    for log in logresult:
        if "eventname" in log:
            i = i + 1
            print(
                "{}): log name: {} , data: {} , topic: {}".format(
                    i, log["eventname"], log["eventdata"], log["topic"]
                )
            )
    inputdetail = print_parse_transaction(receipt, "", parser)
    # 解析该交易在receipt里输出的output,即交易调用的方法的return值
    if inputdetail is not None and "output" in receipt:
        outputresults = parser.parse_receipt_output(inputdetail["name"], receipt["output"])
        common.print_tx_result(outputresults)


def print_parse_transaction(txReceipt, contractname, parser=None):
    if "input" not in txReceipt:
        return None
    if parser is None:
        parser = DatatypeParser(default_abi_file(contractname))
    inputdata = txReceipt["input"]
    inputdetail = parser.parse_transaction_input(inputdata)
    print("INFO >> transaction hash : ", txReceipt["transactionHash"])
    if inputdetail is not None:
        print("tx input data detail:\n {}".format(inputdetail))
    return inputdetail


def check_result(result):
    """
    check result
    """
    if isinstance(result, dict) and "error" in result.keys():
        return True
    return False


def fill_params(params, paramsname):
    index = 0
    result = dict()
    for name in paramsname:
        result[name] = params[index]
        index += 1
    return result


'''
如要求的參數是 func(name,number,colors),其中color是數組
命令行輸入的是个字符串数组,如inputparams = ["alice", "3", "red", "blue", "gree"]
定义 paramtypes = [("name",str,None),("number",int,3),("colors",list,[])]
即  (名字,类型,默认值),即可从命令行输入的字符串数组中match出所需的参数
类型目前仅支持int,str,list，list一定要在最后
如果默认值为None，则表示一定要有输入，否则会异常

用法：
(name,number,colors)=match_input_params(inputparams,paramtypes)
* 虽然很简单而且有通用的库，但写一个作为helper也非常顺手
'''
def match_input_params(inputparams, valtypes):
    n = 0
    result = []
    for (param_name,paramtype, defaultvalue) in valtypes:
        if n < len(inputparams):
            if paramtype is list:
                param = inputparams[n:]
            else:
                param = inputparams[n]
        else:
            param = defaultvalue
            if param is None:
                raise Exception(f"param [{param_name}] must input but None")
        if paramtype is int:
            param = int(param)
        result.append(param)
        n = n + 1
    return tuple(result)
