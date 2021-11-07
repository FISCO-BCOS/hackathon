'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @file: permission_service.py
  @function:
  @author: yujiechen
  @date: 2019-07
'''
import json
from client.common import transaction_common
from client.common import common
from client.precompile.crud.crud_service import CRUDService
from client.precompile.common import PrecompileCommon


class PermissionService:
    """
    implementation of PermissionService
    """

    def __init__(self, contract_path):
        """
        init the address and contract path for PermissionService
        """
        self.permission_address = "0x0000000000000000000000000000000000001005"
        self.contract_path = contract_path
        self.gasPrice = 300000000
        self.client = transaction_common.TransactionCommon(
            self.permission_address, contract_path, "Permission")

    def grant(self, table_name, account_address):
        """
        grant write permission of table_name to account_address
        related api:
        function insert(string table_name, string addr) public returns(int256);
        """
        common.check_and_format_address(account_address)
        common.check_address_startwith_0x(account_address)
        fn_name = "insert"
        fn_args = [table_name, account_address]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)

    def revoke(self, table_name, account_address):
        """
        revoke write permission to table_name from account_address
        related api:
        function remove(string table_name, string addr) public returns(int256);
        """
        common.check_and_format_address(account_address)
        common.check_address_startwith_0x(account_address)
        fn_name = "remove"
        fn_args = [table_name, account_address]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)

    @staticmethod
    def print_permission_info(result):
        """
        print permission info
        """
        i = 0
        if result is None:
            return
        if common.check_result(result) is False:
            return
        result_list = list(result)
        if result_list is None or len(result_list) < 1:
            return
        for permission_info in result_list:
            permission_item = json.loads(permission_info)
            for permission_obj in permission_item:
                print("----->> ITEM {}".format(i))
                i = i + 1
                if "address" in permission_obj.keys():
                    print("     = address: {}".format(permission_obj["address"]))
                    print("     = enable_num: {}".format(permission_obj["enable_num"]))
        if i == 0:
            common.print_info("    ", "Empty Set, permission info: {}".format(result))

    def list_permission(self, table_name):
        """
        list write-permitted accounts to table_name
        related api:
        function queryByName(string table_name) public constant returns(string);
        """
        fn_name = "queryByName"
        fn_args = [table_name]
        return self.client.call_and_decode(fn_name, fn_args)

    def grantUserTableManager(self, table_name, account_address):
        """
        grant user table permission to the given account
        """
        crud_service = CRUDService(self.contract_path)
        table = crud_service.desc(table_name)
        if table is None:
            print(" WARNING >> non-exist table {}, create table firstly".format(table_name))
            # ./return
        return self.grant(table_name, account_address)

    def revokeUserTableManager(self, table_name, account_address):
        """
        """
        return self.revoke(table_name, account_address)

    def listUserTableManager(self, table_name):
        return self.list_permission(table_name)

    def grantDeployAndCreateManager(self, account_address):
        return self.grant(PrecompileCommon.SYS_TABLE, account_address)

    def revokeDeployAndCreateManager(self, account_addr):
        return self.revoke(PrecompileCommon.SYS_TABLE, account_addr)

    def listDeployAndCreateManager(self):
        return self.list_permission(PrecompileCommon.SYS_TABLE)

    def grantPermissionManager(self, account_addr):
        return self.grant(PrecompileCommon.SYS_TABLE_ACCESS, account_addr)

    def revokePermissionManager(self, account_addr):
        return self.revoke(PrecompileCommon.SYS_TABLE_ACCESS, account_addr)

    def listPermissionManager(self):
        return self.list_permission(PrecompileCommon.SYS_TABLE_ACCESS)

    def grantNodeManager(self, account_addr):
        return self.grant(PrecompileCommon.SYS_CONSENSUS, account_addr)

    def revokeNodeManager(self, account_addr):
        return self.revoke(PrecompileCommon.SYS_CONSENSUS, account_addr)

    def listNodeManager(self):
        return self.list_permission(PrecompileCommon.SYS_CONSENSUS)

    def grantCNSManager(self, account_addr):
        return self.grant(PrecompileCommon.SYS_CNS, account_addr)

    def revokeCNSManager(self, account_addr):
        return self.revoke(PrecompileCommon.SYS_CNS, account_addr)

    def listCNSManager(self):
        return self.list_permission(PrecompileCommon.SYS_CNS)

    def grantSysConfigManager(self, account_addr):
        return self.grant(PrecompileCommon.SYS_CONFIG, account_addr)

    def revokeSysConfigManager(self, account_addr):
        return self.revoke(PrecompileCommon.SYS_CONFIG, account_addr)

    def listSysConfigManager(self):
        return self.list_permission(PrecompileCommon.SYS_CONFIG)
