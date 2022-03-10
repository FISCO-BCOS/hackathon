#!/usr/bin/env python
# PYTHON_ARGCOMPLETE_OK
# -*- coding: utf-8 -*-
"""
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @author: kentzhang
  @date: 2019-06

"""

import sys

from console_utils.cmd_account import CmdAccount
from console_utils.cmd_encode import CmdEncode
from console_utils.cmd_transaction import CmdTransaction
from console_utils.console_common import console_run_byname, contracts_dir
from console_utils.precompile import Precompile
from console_utils.command_parser import CommandParser
from console_utils.rpc_console import RPCConsole
from client.common import common
import traceback
from client.bcoserror import (
    BcosError,
    CompileError,
    PrecompileError,
    ArgumentsError,
    BcosException,
)
from client.common.transaction_exception import TransactionException
from py_vesion_checker import check_py_version_with_exception
check_py_version_with_exception()
"""
2020.10 重构，原来的实现很多塞在这文件里，有点乱，
把命令分组为“账户account，交易transaction，内置合约precompile，读接口rpc，转码encode,
每一组命令一个class，放到console_utils目录里
把一些公共工具函数抽到console_common.py里
在此文件主要是是解析输入，试图按配置和自动加载类结合的方式调取指定的命令实现.
优化了下usage，可以指定分组的usage打印，如usage account,usage rpc,输出的信息比较少了，比较简洁易读
"""
cmd_mapping = dict()
cmd_mapping["showaccount"] = ["cmd_account", "CmdAccount"]
cmd_mapping["listaccount"] = ["cmd_account", "CmdAccount"]
cmd_mapping["newaccount"] = ["cmd_account", "CmdAccount"]
cmd_mapping["hex"] = ["cmd_encode", "CmdEncode"]
cmd_mapping["decodehex"] = ["cmd_encode", "CmdEncode"]
cmd_mapping["checkaddr"] = ["cmd_encode", "CmdEncode"]
cmd_mapping["txinput"] = ["cmd_encode", "CmdEncode"]
cmd_mapping["deploy"] = ["cmd_transaction", "CmdTransaction"]
cmd_mapping["call"] = ["cmd_transaction", "CmdTransaction"]
cmd_mapping["sendtx"] = ["cmd_transaction", "CmdTransaction"]
cmd_mapping["deploylast"] = ["cmd_transaction", "CmdTransaction"]
cmd_mapping["deploylog"] = ["cmd_transaction", "CmdTransaction"]


def usage(inputparams=[]):
    """
    print usage
    """
    print("FISCO BCOS 2.0 @python-SDK Usage:")

    if len(inputparams) == 0:
        module = "all"
    else:
        module = inputparams[0]
    if module == 'all' or module == 'account':
        CmdAccount.usage()
    if module == 'all' or module == 'transaction':
        CmdTransaction.usage()
    if module == 'all' or module == 'encode':
        CmdEncode.usage()
    if module == 'all' or module == 'precompile':
        Precompile.usage()
    if module == 'all' or module == 'rpc':
        RPCConsole.usage()

    print('''------------------------
输入 : 'usage [module]' 查看指定控制台模块的指令(更加简洁):
[ account , rpc , transaction ,  precompile , encode ] ''')


def check_cmd(cmd, validcmds):
    if cmd not in validcmds:
        common.print_error_msg(
            ("console cmd  [{}]  not implement yet," " see the usage\n").format(cmd), ""
        )
        return False
    return True


def get_validcmds():
    """
    get valid cmds
    """
    cmds = []
    for key in cmd_mapping:
        cmds.append(key)
    return cmds


Precompile.define_functions()
RPCConsole.define_commands()
validcmds = get_validcmds() + RPCConsole.get_all_cmd() + Precompile.get_all_cmd() + ["usage"]


def main(argv):
    try:
        command_parser = CommandParser(validcmds)
        cmd, inputparams = command_parser.parse_commands(argv)
        if cmd == "list":
            RPCConsole.print_rpc_usage()
            print(
                "--------------------------------------------------------------------"
            )
            return
        # check cmd
        valid = check_cmd(cmd, validcmds)
        if valid is False:
            usage()
            return
        if cmd == "usage":
            usage(inputparams)
            return
        if cmd in cmd_mapping:
            (modulename, classname) = cmd_mapping[cmd]
            console_run_byname(modulename, classname, cmd, inputparams)
            return
        precompile = Precompile(cmd, inputparams, contracts_dir + "/precompile")
        # try to callback cns precompile
        precompile.call_cns()
        # try to callback consensus precompile
        precompile.call_consensus()
        # try to callback config precompile
        precompile.call_sysconfig_precompile()
        # try to callback permission precompile
        precompile.call_permission_precompile()
        # try to callback crud precompile
        precompile.call_crud_precompile()
        # try to callback rpc functions
        rpcconsole = RPCConsole(cmd, inputparams, contracts_dir)
        rpcconsole.executeRpcCommand()
    except TransactionException as e:
        common.print_error_msg(cmd, e)
    except PrecompileError as e:
        common.print_error_msg(cmd, e)
    except BcosError as e:
        common.print_error_msg(cmd, e)
    except CompileError as e:
        common.print_error_msg(cmd, e)
    except ArgumentsError as e:
        common.print_error_msg(cmd, e)
    except BcosException as e:
        common.print_error_msg(cmd, e)
    except ValueError as e:
        common.print_error_msg(cmd, e)
    except Exception as e:
        print("exception happened!")
        print(traceback.format_exc())
        exit(-1)


if __name__ == "__main__":
    main(sys.argv[1:])
