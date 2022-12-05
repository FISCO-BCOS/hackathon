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
import os
from client.bcoserror import (
    BcosException,
)
from client.datatype_parser import DatatypeParser
from console_utils.console_common import default_abi_file
from eth_utils import to_checksum_address


class CmdEncode:
    @staticmethod
    def make_usage():
        usagemsg = []
        usagemsg.append(
            """
>> txinput [contractname] [inputdata(in hex string)]
复制一段来自transaction的inputdata(十六进制字符串)，指定合约名，则可以自动解析（合约的abi文件应存在指定目录下）

>> checkaddr [address] 将普通地址转为自校验地址,自校验地址使用时不容易出错

>> hex [value]: 将10进制转为16进制

>> decodehex [hexvalue] 将16进制转为10进制，可以有0x开头
""")

        return usagemsg

    @staticmethod
    def usage():
        usagemsg = CmdEncode.make_usage()
        for m in usagemsg:
            print(m)
        return usagemsg

    def txinput(self, inputparams):
        # [contractname] [inputdata(in hex string)
        contractname = inputparams[0]
        inputdata = inputparams[1]
        abi_path = default_abi_file(contractname)
        if os.path.isfile(abi_path) is False:
            raise BcosException(
                "execute {} failed for {} doesn't exist".format(contractname, abi_path)
            )
        try:
            dataparser = DatatypeParser(abi_path)
            # print(dataParser.func_abi_map_by_selector)
            result = dataparser.parse_transaction_input(inputdata)
            print("\nabifile : ", default_abi_file(contractname))
            print("parse result: {}".format(result))
        except Exception as e:
            raise BcosException("execute {} failed for reason: {}".format(contractname, e))

    def checkaddr(self, inputparams):
        # [address]
        address = inputparams[0]
        result = to_checksum_address(address)
        print("{} -->\n{}".format(address, result))

    def hex(self, inputparams):
        print(hex(int(inputparams[0])))

    def decodehex(self, inputparams):
        print(int(inputparams[0], 16))
