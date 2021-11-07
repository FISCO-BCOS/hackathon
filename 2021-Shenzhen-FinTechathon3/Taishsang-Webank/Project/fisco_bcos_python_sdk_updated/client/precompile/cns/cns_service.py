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
  @file: cns_service.py
  @function:
  @author: yujiechen
  @date: 2019-07
'''
import json
import client.clientlogger as clientlogger
from client.common import transaction_common
from client.common import common
from client.bcoserror import BcosException
import re


class CnsService:
    """
    implementation for CNS Service
    """

    def __init__(self, contract_path):
        """
        init the address for CNS contract
        """
        self.logger = clientlogger.logger
        self._cns_address = "0x0000000000000000000000000000000000001004"
        self._max_version_len = 40
        self.define_error_code()
        # define bcosclient
        self.contract_name = "CNS"
        self.gasPrice = 300000000
        self.client = transaction_common.TransactionCommon(
            self._cns_address, contract_path, self.contract_name)

    def define_error_code(self):
        """
        common error code for CNS Service
        """
        self._version_exceeds = -51201

    def get_error_msg(self, error_code):
        """
        get error information according to the given error code
        """
        if error_code == self._version_exceeds:
            return "version string length exceeds the maximum limit"

    def register_cns(self, name, version, address, abi):
        """
        register cns contract: (name, version)->address
        precompile api: insert(string,string,string,string)
        """
        common.check_and_format_address(address)
        version = re.sub(r"\s+", "", version)
        common.print_info("INFO", "CNS version (strip space): {}".format(version))
        # invalid version
        if len(version) > self._max_version_len:
            error_info = self.get_error_msg(self._version_exceeds)
            self.logger.error("register cns failed, error info: {}".format(error_info))
            raise BcosException(error_info)

        # call insert function of CNS
        # function definition: insert(string,string,string,string)
        fn_name = "insert"
        fn_args = [name, version, address, json.dumps(abi)]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)

    def query_cns_by_name(self, name):
        """
        query cns contract information by name
        precompile api: selectByName(string)
        """
        fn_name = "selectByName"
        fn_args = [name]
        return self.client.call_and_decode(fn_name, fn_args)

    def query_cns_by_nameAndVersion(self, name, version):
        """
        query contract address according to the contract name and version
        precompile api: selectByNameAndVersion(string, string)
        """
        version = re.sub(r"\s+", "", version)
        common.print_info("INFO", "CNS version (strip space): {}".format(version))
        fn_name = "selectByNameAndVersion"
        fn_args = [name, version]
        return self.client.call_and_decode(fn_name, fn_args)
