#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @file: consensus_precompile.py
  @function:
  @author: yujiechen
  @date: 2019-07
"""
from client.bcosclient import BcosClient
from client.bcoserror import ArgumentsError
from eth_utils import to_checksum_address
from client.common import common
from client.contractnote import ContractNote
import os


class RPCConsole:
    """
    console for RPC
    """

    def __init__(self, cmd, params, contract_path):
        self.func_prefix = "self.client."
        self.cmd = cmd
        self.params = params
        self.contract_path = contract_path
        RPCConsole.define_commands()

    @staticmethod
    def usage():
        RPCConsole.print_rpc_usage()

    @staticmethod
    def print_rpc_usage():
        """
        print rpc usage information
        """
        common.print_info("", "RPC commands")
        for command in RPCConsole.functions["zero"]:
            print("   ", "[{}]".format(command))
        for command in RPCConsole.functions["one_int"]:
            print("   ", "[{}] [number]".format(command))
        for command in RPCConsole.functions["one_hash"]:
            if command == "getCode":
                continue
            print("   ", "[{}] [hash] [contract_name]".format(command))
        print("   ", ("[getCode] [contract_address]"))
        print("   ", ("[getTransactionByBlockHashAndIndex] "
                      "[blockHash] [transactionIndex] [contract_name]"))
        print("   ", ("[getTransactionByBlockNumberAndIndex] "
                      "[blockNumber] [transactionIndex] [contract_name]"))
        print("   ", "[{}] [tx_count_limit/tx_gas_limit]".
              format("getSystemConfigByKey"))
        print("   ", "[{}] [blockNumber] True/False".
              format("getBlockByNumber"))
        print("   ", "[{}] [blockHash] True/False".
              format("getBlockByHash"))

    @staticmethod
    def define_commands():
        """
        define functions
        """
        RPCConsole.functions = {}
        # function with 0 param
        RPCConsole.functions["zero"] = ["getNodeVersion", "getBlockNumber",
                                        "getPbftView", "getSealerList",
                                        "getObserverList",
                                        "getConsensusStatus", "getSyncStatus",
                                        "getPeers", "getGroupPeers", "getNodeIDList",
                                        "getGroupList", "getPendingTxSize",
                                        "getTotalTransactionCount",
                                        "getPendingTransactions"]

        # function with one param, and the param is int
        RPCConsole.functions["one_int"] = ["getBlockHashByNumber"]
        # function with one param, and the param is string
        RPCConsole.functions["one_hash"] = ["getCode",
                                            "getTransactionByHash",
                                            "getTransactionReceipt"]
        RPCConsole.functions["one_str"] = ["getSystemConfigByKey"]
        RPCConsole.functions["two"] = ["getTransactionByBlockHashAndIndex",
                                       "getTransactionByBlockNumberAndIndex"]
        RPCConsole.functions["two_bool"] = ["getBlockByNumber", "getBlockByHash"]
        RPCConsole.functions["parse_in"] = ["getTransactionByHash",
                                            "getTransactionByBlockHashAndIndex",
                                            "getTransactionByBlockNumberAndIndex"]
        RPCConsole.functions["parse_out"] = ["getTransactionReceipt"]
        RPCConsole.functions["human_friendly_output"] = ["getTotalTransactionCount",
                                                         "getPendingTxSize",
                                                         "getPbftView"]

    @staticmethod
    def get_all_cmd():
        """
        get all cmd
        """
        command_list = []
        for key in RPCConsole.functions.keys():
            if key == "parse_in" and key == "parse_output":
                continue
            for command in RPCConsole.functions[key]:
                command_list.append(command)
        return command_list

    def parse_output(self, cmd, contract_name, result):
        """
        parse the result
        """

        if "blockNumber" in result:
            blocknum = result["blockNumber"]
            if blocknum.startswith("0x"):
                blocknum = int(blocknum, 16)
            print("transaction in block number :", blocknum)

        if cmd in self.functions["parse_in"]:
            decode_result = common.parse_input(result["input"],
                                               contract_name,
                                               self.contract_path)
            common.print_info("transaction input", decode_result)
        if cmd in self.functions["parse_out"]:
            common.print_output_and_input(result["logs"], result["output"], result["input"],
                                          contract_name, self.contract_path)

    def get_func_name(self, cmd):
        """
        get function name
        """
        return self.func_prefix + cmd

    def exec_cmd_with_two_bool_param(self, cmd, params):
        """
        execute cmd with two params
        """
        if cmd not in RPCConsole.functions["two_bool"]:
            return
        # check param
        common.check_param_num(params, 1, False)
        if len(params) > 2:
            raise ArgumentsError("params must be no more than 2")
        self.exec_command(cmd, params)

    def exec_cmd_with_zero_param(self, cmd, params):
        """
        execute cmd with zero param
        """
        if cmd not in RPCConsole.functions["zero"]:
            return
        # check param
        common.check_param_num(params, 0, True)
        self.exec_command(cmd, params)

    def exec_cmd_with_str_param(self, cmd, params):
        """
        execute cmd with one star params
        """
        if cmd not in RPCConsole.functions["one_str"]:
            return
        common.check_param_num(params, 1, True)
        self.exec_command(cmd, params)

    def exec_cmd_with_int_param(self, cmd, params):
        """
        execute cmd with one param, and the param is a int
        """
        if cmd not in RPCConsole.functions["one_int"]:
            return
        # check param
        common.check_param_num(params, 1, True)
        # check int range
        number = common.check_int_range(params[0])
        self.exec_command(cmd, [number])

    def parse_tx_and_receipt(self, result, cmd, params):
        if result is None:
            return
        # decode output
        contractname = None
        # print(params)
        to_addr = None
        if "to" in result:
            to_addr = result["to"]
        deploy_addr = None
        is_deploy = False
        if to_addr is None or to_addr == "0x0000000000000000000000000000000000000000":
            is_deploy = True
            if "contractAddress" in result:
                # transaction 结构体并没有contractAddress字段
                deploy_addr = result["contractAddress"]

        if len(params) >= 2:
            contractname = params[-1]
            # 試一下是否存在這個合約
            abi_path = os.path.join(self.contract_path, contractname + ".abi")
            if not os.path.exists(abi_path):
                contractname = None

        # 从参数获取不到，则从地址去检索历史记录，尝试获取

        if contractname is None:
            # 如果是部署合约，则to是空的，要获取address
            to_addr = result["to"]
            if is_deploy and deploy_addr is not None:
                abi_contract_addr = deploy_addr
            else:
                abi_contract_addr = to_addr
            hisdetail = ContractNote.get_address_history(abi_contract_addr)
            if hisdetail is not None:
                contractname = hisdetail["name"]
                print(
                    "histroy transaction to contract : {} [{}] (deploy time: {})".format(
                        contractname,
                        abi_contract_addr,
                        hisdetail["timestr"]))

        if is_deploy and deploy_addr is not None:
            print("\nis [DEPLOY] transaction,result address : {}\n"
                  .format(deploy_addr))
        if contractname is None:
            return
        self.parse_output(cmd, contractname, result)

    def exec_cmd_with_hash_param(self, cmd, params):
        """
        execute cmd with one hash param
        """
        if cmd not in RPCConsole.functions["one_hash"]:
            return
        # check_param
        common.check_param_num(params, 1, False)
        # check contract address
        if cmd == "getCode":
            try:
                if len(params) > 1:
                    raise ArgumentsError("{} must provide one param".format(cmd))
                address = to_checksum_address(params[0])
                self.exec_command(cmd, [address])
            except Exception as e:
                raise ArgumentsError("invalid address: {}, info: {}"
                                     .format(params[0], e))
        else:
            if len(params) > 2:
                raise ArgumentsError("{} must provide no more than one param".format(cmd))
            # check hash
            common.check_hash(params[0])
            result = self.exec_command(cmd, [params[0]])
            self.parse_tx_and_receipt(result, cmd, params)

    def exec_cmd_with_two_param(self, cmd, params):
        """
        execute command with two params:
        """
        if cmd not in RPCConsole.functions["two"]:
            return
        # check param
        common.check_param_num(params, 2, False)
        if len(params) > 3:
            raise ArgumentsError("{} : params must be no more than 3")
        index = common.check_int_range(params[1])
        result = None
        # check param type
        if cmd == "getTransactionByBlockHashAndIndex":
            common.check_hash(params[0])
            result = self.exec_command(cmd, [params[0], index])
        if cmd == "getTransactionByBlockNumberAndIndex":
            number = common.check_int_range(params[0])
            result = self.exec_command(cmd, [number, index])
        self.parse_tx_and_receipt(result, cmd, params)

    def convertHexToDec(self, cmd, json_str):
        if cmd == "getTotalTransactionCount":
            json_str["blockNumber"] = int(json_str["blockNumber"], 16)
            json_str["failedTxSum"] = int(json_str["failedTxSum"], 16)
            json_str["txSum"] = int(json_str["txSum"], 16)
        elif cmd == "getPendingTxSize" or cmd == "getPbftView":
            if isinstance(json_str, int):
                return json_str
            json_str = int(json_str, 16)
        return json_str

    def convertHexToDec(self, cmd, json_str):
        if cmd == "getTotalTransactionCount":
            json_str["blockNumber"] = int(json_str["blockNumber"], 16)
            json_str["failedTxSum"] = int(json_str["failedTxSum"], 16)
            json_str["txSum"] = int(json_str["txSum"], 16)
        elif cmd == "getPendingTxSize" or cmd == "getPbftView":
            if isinstance(json_str, int):
                return json_str
            json_str = int(json_str, 16)
        return json_str

    def exec_command(self, cmd, params):
        """
        exec_command
        """
        self.client: BcosClient = BcosClient()
        print("INFO >> BcosClient: ", self.client.getinfo())
        function_name = self.get_func_name(cmd)
        # execute function
        ret_json = eval(function_name)(*params)
        common.print_info("INFO", self.cmd)
        if cmd in RPCConsole.functions["human_friendly_output"]:
            ret_json = self.convertHexToDec(cmd, ret_json)
        common.print_result(ret_json)
        return ret_json

    def executeRpcCommand(self):
        """
        execute RPC commands
        """
        self.exec_cmd_with_zero_param(self.cmd, self.params)
        self.exec_cmd_with_int_param(self.cmd, self.params)
        self.exec_cmd_with_hash_param(self.cmd, self.params)
        self.exec_cmd_with_two_param(self.cmd, self.params)
        self.exec_cmd_with_str_param(self.cmd, self.params)
        self.exec_cmd_with_two_bool_param(self.cmd, self.params)
