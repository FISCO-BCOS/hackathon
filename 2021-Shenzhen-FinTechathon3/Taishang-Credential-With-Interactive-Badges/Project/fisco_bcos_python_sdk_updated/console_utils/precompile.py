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
  @date: 2019-07
'''
import os
import json
from client.common.compiler import Compiler
from client.common import common
from client.common.transaction_exception import TransactionException
from client.precompile.cns.cns_service import CnsService
from client.precompile.consensus.consensus_precompile import ConsensusPrecompile
from client.precompile.config.config_precompile import ConfigPrecompile
from client.precompile.permission.permission_service import PermissionService
from client.precompile.crud.crud_service import CRUDService, Table
from client.precompile.common import PrecompileCommon
from console_utils.console_common import contracts_dir
from client.bcoserror import BcosError, CompileError, PrecompileError, ArgumentsError, BcosException


class Precompile:
    """
    """
    functions = {}

    def __init__(self, cmd, args, contract_path):
        self._cmd = cmd
        self._args = args
        self._contract_path = contract_path
        Precompile.define_functions()

    @staticmethod
    def define_functions():
        """
        define all cmds
        """
        # cns
        Precompile.functions["cns"] = ["registerCNS", "queryCNSByName", "queryCNSByNameAndVersion"]
        # consensus
        Precompile.functions["consensus"] = ["addSealer", "addObserver", "removeNode"]
        # configuration system contract
        Precompile.functions["sysconfig"] = ["setSystemConfigByKey"]
        # permission precompile
        Precompile.functions["permission"] = ["grantUserTableManager", "grantPermissionManager",
                                              "grantNodeManager", "grantCNSManager",
                                              "grantSysConfigManager",
                                              "grantDeployAndCreateManager",
                                              "revokeUserTableManager",
                                              "revokeDeployAndCreateManager",
                                              "revokePermissionManager",
                                              "revokeNodeManager", "revokeCNSManager",
                                              "revokeSysConfigManager", "listUserTableManager",
                                              "listDeployAndCreateManager",
                                              "listPermissionManager", "listNodeManager",
                                              "listSysConfigManager", "listCNSManager"]
        Precompile.functions["crud"] = ["createTable", "desc"]

    def print_crud_usage(self, print_all=False):
        """
        print crud usage
        """
        prefix = "CRUD USAGE NOTE:"
        if print_all is True or self._cmd == self.functions["crud"][0]:
            print("{} {} [tableName] [tableKey] [tableFields]".format(
                prefix, self.functions["crud"][0]))
        if print_all is True or self._cmd == self.functions["crud"][1]:
            print("{} {} [tableName]".format(
                prefix, self.functions["crud"][0]))

    def print_cns_usage(self, print_all=False):
        """
        print cns usage
        """
        prefix = "CNS USAGE NOTE:"
        if print_all:
            print("INFO >> CNS Usage:")
            prefix = "\t"
        if print_all is True or self._cmd == self.functions["cns"][0]:
            print(("{} {} [contract_name] [contract_address]"
                   " [contract_version]").format(prefix, self.functions["cns"][0]))
        if print_all is True or self._cmd == self.functions["cns"][1]:
            print("{} {} [contract_name]".
                  format(prefix, self.functions["cns"][1]))
        if print_all is True or self._cmd == self.functions["cns"][2]:
            print('''{} {} [contract_name] [contract_version]'''.
                  format(prefix, self.functions["cns"][2]))

    def print_consensus_usage(self, print_all=False):
        """
        print usage information for consensus precompile
        """
        prefix = "CONSENSUS USAGE NOTE:"
        if print_all:
            print("INFO >> CONSENSUS Usage:")
            prefix = "\t"
        if print_all is True or self._cmd == self.functions["consensus"][0]:
            print('''{} {} [nodeId]'''.format(prefix, self.functions["consensus"][0]))
        if print_all is True or self._cmd == self.functions["consensus"][1]:
            print('''{} {} [nodeId]'''.format(prefix, self.functions["consensus"][1]))
        if print_all is True or self._cmd == self.functions["consensus"][2]:
            print('''{} {} [nodeId]'''.format(prefix, self.functions["consensus"][2]))

    def print_sysconfig_usage(self, print_all=False):
        """
        print usage for sysconfig precompile
        """
        prefix = "SYSCONFIG USAGE NOTE: "
        if print_all:
            print("INFO >> SYSCONFIG Usage:")
            prefix = "\t"
        if print_all is True or self._cmd == self.functions["sysconfig"][0]:
            print('''{} {} [key(tx_count_limit/tx_gas_limit)] [value]'''.
                  format(prefix, self.functions["sysconfig"][0]))

    def print_permission_usage(self):
        """
        print usage information for permission
        """
        if self._cmd.startswith("grantUserTable") or self._cmd.startswith("revokeUserTable"):
            print('''USAGE NOTE:  {} [tableName] [account_adddress]'''.
                  format(self._cmd))
        elif self._cmd == "listUserTableManager":
            print('''USAGE NOTE:  {} [table_name]'''.format(self._cmd))
        else:
            print('''USAGE NOTE:  {}'''.format(self._cmd))

    @staticmethod
    def print_all_permission_usage():
        """
        print all permission usage
        """
        print("INFO >> Permission Usage:")
        for cmd in Precompile.functions["permission"]:
            if cmd.startswith("grantUserTable") or cmd.startswith("revokeUserTable"):
                print('''\t{} [tableName] [account_adddress]'''.
                      format(cmd))
            elif "listUserTableManager" in cmd:
                print('''\t{} [tableName]'''.format(cmd))
            elif "list" in cmd:
                print('''\t{}'''.format(cmd))
            else:
                print('''\t{} [account_adddress]'''.format(cmd))

    @staticmethod
    def get_all_cmd():
        """
        get all cmd
        """
        cmds = []
        for cmd_array in Precompile.functions:
            for cmd in Precompile.functions[cmd_array]:
                cmds.append(cmd)
        return cmds

    def print_error_msg(self, err_msg):
        """
        print error msg
        """
        print("ERROR >> call {} failed for {}".format(self._cmd, err_msg))

    def print_transaction_exception(self, transaction_exception):
        error_msg = '''send transaction failed\n, >> INFO\n {},
                    {}'''.format(transaction_exception.get_status_error_info(),
                                 transaction_exception.get_output_error_info())
        self.print_error_msg(error_msg)

    def print_succ_msg(self, result):
        """
        print succ msg
        """
        if isinstance(result, tuple):
            receipt = result[0]
            output = result[1][0]
            if isinstance(receipt, dict) and "status" in receipt.keys():
                common.print_info("INFO", self._cmd)
                print("     >> status: {}".format(receipt["status"]))
                print("     >> transactionHash: {}".format(receipt["transactionHash"]))
                print("     >> gasUsed: {}".format(receipt["gasUsed"]))
                if str(output) not in PrecompileCommon.error_code.keys():
                    print("     >> {} succ, output: {}".format(self._cmd, output))
                else:
                    print("     >> {}: {}"
                          .format(output, PrecompileCommon.error_code[str(output)]))
            elif result is None:
                print("INFO >> {}: \n\tEmpty Set".format(self._cmd))
            else:
                print("INFO >> {}: \n{}".format(self._cmd, result))

    def check_abi(self, abi_path):
        """
        check abi path
        """
        if os.path.exists(abi_path) is False:
            self.print_error_msg("abi file {} not exists".format(abi_path))
            return False
        return True

    def check_param_num(self, expected, needEqual=False):
        """
        check param num
        """
        common.check_param_num(self._args, expected, needEqual)

    @staticmethod
    def print_cns_info(cns_info):
        """
        print cns information
        """
        if common.check_result(cns_info) is False:
            return
        for cns_item in cns_info:
            cns_obj = json.loads(cns_item)
            i = 0
            for cns in cns_obj:
                print("CNS ITEM {} >>".format(i))
                print("\tContractName: {}".format(cns["name"]))
                print("\tContractVersion: {}".format(cns["version"]))
                print("\tContractAddress: {}".format(cns["address"]))
                i = i + 1
        if i == 0:
            common.print_info("    ", "Empty Set, result: {}".format(cns_info))

    @staticmethod
    def load_abi(contract_name, contracts_dir, contract_abi_path):
        """
        """
        contract_abi = ""
        contract_file_path = contracts_dir + "/" + contract_name + ".sol"
        if not os.path.exists(contract_abi_path):
            Compiler.compile_file(contract_file_path, contracts_dir)
        with open(contract_abi_path, 'r') as load_f:
            contract_abi = json.load(load_f)
            load_f.close()
        return contract_abi

    def call_cns(self):
        """
        call cns service
        register name, version, address, abi
        queryCNSByName name
        queryCnsByNameAndVersion name version
        """
        if self._cmd not in self.functions["cns"]:
            return
        self.cns_service = CnsService(self._contract_path)
        try:
            # register cns contract
            # registerCNS
            if self._cmd == self.functions["cns"][0]:
                self.check_param_num(3, True)
                contract_name = self._args[0]
                contract_address = self._args[1]
                contract_version = self._args[2]
                contract_abi_path = contracts_dir + "/" + contract_name + ".abi"
                contract_abi = Precompile.load_abi(contract_name, contracts_dir, contract_abi_path)
                try:
                    result = self.cns_service.register_cns(
                        contract_name, contract_version, contract_address, contract_abi)
                    self.print_succ_msg(result)
                except TransactionException as e:
                    self.print_transaction_exception(e)
                except PrecompileError as e:
                    self.print_error_msg(e)
                except BcosError as e:
                    self.print_error_msg(e)
                except CompileError as e:
                    self.print_error_msg(e)
                return
            # query cns information by name
            # queryCNSByName
            if self._cmd == self.functions["cns"][1]:
                self.check_param_num(1, True)
                result = self.cns_service.query_cns_by_name(self._args[0])
                Precompile.print_cns_info(result)
                return
            # query cns information by name and version
            # queryCNSByNameAndVersions
            if self._cmd == self.functions["cns"][2]:
                self.check_param_num(2, True)
                result = self.cns_service.query_cns_by_nameAndVersion(self._args[0], self._args[1])
                Precompile.print_cns_info(result)
                return
        except ArgumentsError as e:
            common.print_error_msg(self._cmd, e)
            self.print_cns_usage()

    @staticmethod
    def check_nodeList(client, nodeId):
        """
        check node list
        """
        nodeList = list(client.getNodeIDList())
        if nodeId not in nodeList:
            raise BcosException(("node {} is not in nodeList: {}, "
                                 "please check the existence of "
                                 "the node").format(nodeId, nodeList))

    @staticmethod
    def check_nodeExist(client, nodeId):
        """
        check node num
        """
        nodeList = list(client.getNodeIDList())
        if nodeId not in nodeList:
            raise BcosException(("remove non-exist node, "
                                 "currentNodeList: {}").format(nodeList))

    @staticmethod
    def check_sealer(client, nodeId):
        """
        check sealer
        """
        sealerList = list(client.getSealerList())
        nodeNum = len(sealerList)
        if nodeNum == 1 and nodeId in sealerList:
            raise BcosException("forbid remove the last node {}".format(nodeId))

    def call_consensus(self):
        """
        call consensusPrecompile
        addSealer(string nodeID) public returns(int256)
        addObserver(string nodeID) public returns(int256)
        remove(string nodeID) public returns(int256)
        """
        if self._cmd not in self.functions["consensus"]:
            return
        self.consensus_precompile = ConsensusPrecompile(self._contract_path)
        try:
            self.check_param_num(1, True)
            result = None
            # addSealer
            if self._cmd == self.functions["consensus"][0]:
                # check nodeList
                Precompile.check_nodeList(self.consensus_precompile.client,
                                          self._args[0])
                result = self.consensus_precompile.addSealer(self._args[0])
            # addObserver
            elif self._cmd == self.functions["consensus"][1]:
                result = self.consensus_precompile.addObserver(self._args[0])
            # removeNode
            elif self._cmd == self.functions["consensus"][2]:
                # check node existence
                Precompile.check_sealer(self.consensus_precompile.client,
                                        self._args[0])
                result = self.consensus_precompile.removeNode(self._args[0])
            self.print_succ_msg(result)
        except TransactionException as e:
            self.print_transaction_exception(e)
        except PrecompileError as e:
            self.print_error_msg(e)
        except ArgumentsError as e:
            common.print_error_msg(self._cmd, e)
            self.print_consensus_usage()
        except BcosError as e:
            self.print_error_msg(e)
        except CompileError as e:
            self.print_error_msg(e)

    def call_sysconfig_precompile(self):
        """
        call sysconfig precompile
        function setSystemConfigByKey(string key, string value) public returns(int256)
        """
        if self._cmd not in self.functions["sysconfig"]:
            return
        self.config_precompile = ConfigPrecompile(self._contract_path)
        try:
            result = None
            # setSystemConfigByKey
            if self._cmd == self.functions["sysconfig"][0]:
                self.check_param_num(2, True)
                result = self.config_precompile.setValueByKey(self._args[0], self._args[1])
                self.print_succ_msg(result)
        except TransactionException as e:
            self.print_transaction_exception(e)
        except PrecompileError as e:
            self.print_error_msg(e)
        except BcosError as e:
            self.print_error_msg(e)
        except CompileError as e:
            self.print_error_msg(e)
        except ArgumentsError as e:
            common.print_error_msg(self._cmd, e)
            self.print_sysconfig_usage()

    def exec_permission_cmd(self):
        """
        execute permission cmd
        """
        func_name = "self.premisson_service." + self._cmd
        if self._cmd.startswith("grantUserTable") or self._cmd.startswith("revokeUserTable"):
            self.check_param_num(2, True)
            return eval(func_name)(self._args[0], self._args[1])
        if self._cmd.startswith("listUser"):
            self.check_param_num(1, True)
            result = eval(func_name)(self._args[0])
            PermissionService.print_permission_info(result)
            return None
        # list functions
        if self._cmd.startswith("list"):
            result = eval(func_name)()
            PermissionService.print_permission_info(result)
            return None
        # other functions
        self.check_param_num(1, True)
        return eval(func_name)(self._args[0])

    def call_permission_precompile(self):
        """
        call permission precompile
        """
        if self._cmd not in self.functions["permission"]:
            return
        self.premisson_service = PermissionService(self._contract_path)
        try:
            result = self.exec_permission_cmd()
            self.print_succ_msg(result)
        except TransactionException as e:
            self.print_transaction_exception(e)
        except PrecompileError as e:
            self.print_error_msg(e)
        except BcosError as e:
            self.print_error_msg(e)
        except CompileError as e:
            self.print_error_msg(e)
        except ArgumentsError as e:
            common.print_error_msg(self._cmd, e)
            self.print_permission_usage()

    def call_crud_precompile(self):
        """
        createTable
        """
        try:
            if self._cmd not in self.functions["crud"]:
                return
            self.crud_serivce = CRUDService(self._contract_path)
            # create table
            if self._cmd == self.functions["crud"][0]:
                self.check_param_num(3)
                table = Table(self._args[0], self._args[1], ','.join(self._args[2:]))
                result = self.crud_serivce.create_table(table)
                self.print_succ_msg(result)
            # desc table
            if self._cmd == self.functions["crud"][1]:
                self.check_param_num(1)
                result = self.crud_serivce.desc(self._args[0])
                if result is None:
                    common.print_info("WARN", "non-exist table {}".format(self._args[0]))
        except ArgumentsError as e:
            common.print_error_msg(self._cmd, e)
            self.print_crud_usage()

    @staticmethod
    def usage():
        precompile = Precompile("", [], "")
        precompile.print_cns_usage(True)
        precompile.print_consensus_usage(True)
        precompile.print_sysconfig_usage(True)
        precompile.print_all_permission_usage()
