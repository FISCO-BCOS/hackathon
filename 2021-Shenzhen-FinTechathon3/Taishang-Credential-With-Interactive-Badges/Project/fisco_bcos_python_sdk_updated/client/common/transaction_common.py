#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
 is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
 is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @file: transaction_common.py
  @function:
  @author: yujiechen
  @date: 2019-07
'''
import os
from client.common import common
from client.common import transaction_status_code
from client.datatype_parser import DatatypeParser
from client.common.compiler import Compiler
import client.bcosclient as bcosclient
from client.bcoserror import BcosError, CompileError, BcosException
from client.common.transaction_exception import TransactionException
from utils.abi import get_constructor_abi, get_abi_input_types
from client.format_param_by_abi import format_args_by_function_abi
from eth_utils.hexadecimal import encode_hex
from utils.contracts import get_function_info


class TransactionCommon(bcosclient.BcosClient):
    """
    define common functions
    """

    def __init__(self, contract_addr, contract_path, contract_name):
        """
        init client to send transactions
        """
        bcosclient.BcosClient.__init__(self)
        self.contract_addr = contract_addr
        self.contract_path = contract_path
        (fname, extname) = os.path.splitext(contract_name)
        if extname.endswith("wasm"):
            # deal with wasm , not compile in this version, todo list
            self.contract_abi_path = contract_path + "/" + fname + ".abi"
            self.contract_bin_path = contract_path + "/" + contract_name
            self.sol_path = contract_path + "/" + contract_name
        else:
            # deal with sol files ,may be force re compile sol file ,so set the sol filename
            self.contract_abi_path = contract_path + "/" + contract_name + ".abi"
            self.contract_bin_path = contract_path + "/" + contract_name + ".bin"
            self.sol_path = contract_path + "/" + contract_name + ".sol"
            if os.path.exists(self.sol_path) is False:
                raise BcosException(("contract {} not exists,"
                                     " please put {}.sol into {}").
                                    format(contract_name,
                                           contract_name, contract_path))
        print("contract_abi_path {}, contract_bin_path {}".format(self.contract_abi_path,self.contract_bin_path))
        self.dataparser = None
        if os.path.exists(self.contract_bin_path):
            self.dataparser = DatatypeParser(self.contract_abi_path)

    def __del__(self):
        super().finish()

    def set_contract_addr(self, contractAddress):
        self.contract_addr = contractAddress

    def gen_contract_abi(self, needCover=False):
        """
        get contract abi according to contract_abi_path
        """
        if needCover is False and os.path.exists(self.contract_abi_path) is True:
            return
        # backup the abi and bin
        else:
            force_write = common.backup_file(self.contract_abi_path)
            if force_write is False:
                return
            force_write = common.backup_file(self.contract_bin_path)
            if force_write is False:
                return
        Compiler.compile_file(self.sol_path, self.contract_path)

    def send_transaction_getReceipt(
            self,
            fn_name,
            fn_args,
            gasPrice=30000000,
            isdeploy=False,
            from_account_signer=None):
        """
        send transactions to CNS contract with the givn function name and args
        """
        try:
            contract_abi, args = self.format_abi_args(fn_name, fn_args, isdeploy)
            contract_bin = None
            if isdeploy is True and os.path.exists(self.contract_bin_path) is True:
                with open(self.contract_bin_path,"rb") as f:
                    contract_bin = f.read()
                    f.close()
                    # print(contract_bin)
                    if self.contract_bin_path.endswith("wasm"):
                        contract_bin = encode_hex(contract_bin)
                    else:
                        contract_bin = bytes.decode(contract_bin,"utf-8")

                if contract_bin is not None and len(contract_bin) > 0x40000:
                    raise BcosException(("contract bin size overflow,"
                                         " limit: 0x40000(256K), size: {})")
                                        .format(len(contract_bin), 16))

            receipt = super().sendRawTransactionGetReceipt(self.contract_addr,
                                                           contract_abi, fn_name,
                                                           args, contract_bin, gasPrice,
                                                           from_account_signer=from_account_signer
                                                           )
            # check status
            if "status" not in receipt.keys() or \
                    "output" not in receipt.keys():
                raise BcosError(-1, None,
                                ("send transaction failed"
                                 "for empty status and output,"
                                 "transaction receipt:{}").format(receipt))
            status = receipt["status"]
            status_code = int(status, 16)
            error_message = transaction_status_code.TransactionStatusCode.get_error_message(
                status_code)
            if error_message is not None:
                raise BcosException("call error, error message: {}".format(error_message))

            if receipt["output"] is None:
                raise TransactionException(receipt, ("send transaction failed,"
                                                     "status: {}, gasUsed: {}").
                                           format(status,
                                                  receipt["gasUsed"]))
            if fn_name is not None and fn_args is not None and self.dataparser is not None:
                output = self.dataparser.parse_receipt_output(fn_name, receipt["output"])
            else:
                output = None
            return (receipt, output)
        except BcosError as e:
            self.logger.error("send transaction failed, fn_name: {}, fn_args:{}, error_info:{}".
                              format(fn_name, fn_args, e))
            raise e
        except CompileError as e:
            self.logger.error(("send transaction failed for compile soldity failed,"
                               "contract_path {}, error_info:{}").
                              format(self.sol_path, e))
            raise e

    def format_abi_args(self, fn_name :str, fn_args, needCover=False):
        """
        format args
        """
        if not self.contract_bin_path.endswith(".wasm"):
            self.gen_contract_abi(needCover)
        data_parser = DatatypeParser(self.contract_abi_path)
        contract_abi = data_parser.contract_abi
        self.dataparser = data_parser
        args = None
        if fn_args is None:
            return (contract_abi, fn_args)
        if fn_name in data_parser.func_abi_map_by_name.keys() is None:
            raise BcosException("invalid function: {}, the right function list:"
                                .format(fn_name,
                                        ''.join(data_parser.func_abi_map_by_name.keys())))
        if fn_name is not None:
            fn_abi = data_parser.func_abi_map_by_name[fn_name]
            inputabi = data_parser.get_function_inputs_abi(fn_name)
            #inputabi = data_parser.get_function_abi(fn_name)

            args = format_args_by_function_abi(fn_args, inputabi)
            #print("args after format:",args)
        # the constructor with params
        elif fn_args is not None and contract_abi is not None:
            abidata = get_constructor_abi(contract_abi)
            if abidata is not None:
                inputabi = abidata["inputs"]
                args = format_args_by_function_abi(fn_args, inputabi)
        return (contract_abi, args)

    def call_and_decode(self, fn_name, fn_args=None):
        """
        call and get the output
        """
        contract_abi, args = self.format_abi_args(fn_name, fn_args, False)
        result = super().call(self.contract_addr, contract_abi, fn_name, args)
        return result
