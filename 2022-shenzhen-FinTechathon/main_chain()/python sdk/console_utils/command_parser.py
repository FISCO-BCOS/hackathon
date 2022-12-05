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
  @file: precompile.py
  @function:
  @author: yujiechen
  @date: 2021-03-03
'''
import os
import argparse
import argcomplete
import glob
from client_config import client_config
from client.datatype_parser import DatatypeParser
from client.contractnote import ContractNote
from console_utils.console_common import contracts_dir


class CommandParser:
    """
    command parser
    """
    parser_config = client_config
    

    def __init__(self, supported_cmds,config_instance=client_config):
        """
        init supported cmds
        """
        self.supported_cmds = supported_cmds
        self.parser_config = config_instance

    def parse_commands(self, argv):
        # 首先创建一个ArgumentParser对象
        parser = argparse.ArgumentParser(description="FISCO BCOS 2.0 lite client @python")
        cmd = parser.add_argument("cmd", nargs="+", help="Please enter ./console.py usage for help information ")  # 添加参数
        cmd.completer = self.completion

        argcomplete.autocomplete(parser)
        args = parser.parse_args()

        print("\nINFO >> user input : {}\n".format(args.cmd))
        cmd = args.cmd[0]
        inputparams = args.cmd[1:]
        return cmd, inputparams

    @staticmethod
    def default_abi_file(contractname):
        abi_file = contractname
        if not abi_file.endswith(".abi"):
            abi_file = contracts_dir + "/" + contractname + ".abi"
        return abi_file

    @staticmethod
    def get_functions_by_contract(contract_name):
        """
        get functions according to contract_name
        """
        data_parser = DatatypeParser(CommandParser.default_abi_file(contract_name))
        return [*data_parser.func_abi_map_by_name.keys()]

    @staticmethod
    def filter_files_by_file_pattern(file_pattern):
        """
        return list according to file_pattern
        """
        file_list = [f for f in glob.glob(file_pattern)]
        targets = []
        for file in file_list:
            targets.append(os.path.basename(file).split(".")[0])
        return targets

    @staticmethod
    def get_contracts():
        """
        list all contractname for call
        """
        return CommandParser.filter_files_by_file_pattern(contracts_dir + "/*.sol")

    @staticmethod
    def get_accounts():
        """
        list all accounts
        """
        account_keyfile_path = CommandParser.parser_config.account_keyfile_path
        return CommandParser.filter_files_by_file_pattern(account_keyfile_path + "/*.keystore")

    def completion(self, prefix, parsed_args, **kwargs):
        """
        complete the shell
        """
        if parsed_args.cmd is None:
            return self.supported_cmds
        # deploy contract
        if parsed_args.cmd[0] == "deploy":
            return CommandParser.get_contracts()

        # call and sendtx
        # warn(parsed_args)
        if parsed_args.cmd[0] == "call" or parsed_args.cmd[0] == "sendtx":
            # only list the contract name
            if len(parsed_args.cmd) == 1:
                return CommandParser.get_contracts()
            # list the contract address
            if len(parsed_args.cmd) == 2:
                return ContractNote.get_contract_addresses(parsed_args.cmd[1])
            # list functions
            if len(parsed_args.cmd) == 3:
                return CommandParser.get_functions_by_contract(parsed_args.cmd[1])

        # call showaccount
        if parsed_args.cmd[0] == "showaccount":
            return CommandParser.get_accounts()
        # registerCNS [contract_name] [contract_address] [contract_version]
        if parsed_args.cmd[0] == "registerCNS":
            # list contract name
            if len(parsed_args.cmd) == 1:
                return CommandParser.get_contracts()
            # list contract address
            if len(parsed_args.cmd) == 2:
                return ContractNote.get_contract_addresses(parsed_args.cmd[1])
        # queryCNSByName [contract_name]
        if parsed_args.cmd[0] == "queryCNSByName":
            # list contract name
            if len(parsed_args.cmd) == 1:
                return CommandParser.get_contracts()
        # queryCNSByNameAndVersion [contract_name] [contract_version]
        if parsed_args.cmd[0] == "queryCNSByNameAndVersion":
            if len(parsed_args.cmd) == 1:
                return CommandParser.get_contracts()
        # sysconfig
        if parsed_args.cmd[0] == "setSystemConfigByKey" or parsed_args.cmd[0] == "getSystemConfigByKey":
            return ["tx_count_limit", "tx_gas_limit"]
        return []
