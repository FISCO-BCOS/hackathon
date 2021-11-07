'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @file: crud_service.py
  @function:
  @author: yujiechen
  @date: 2019-07
'''
import json
from client.common import transaction_common
from client.precompile.crud.condition import Condition
from client.precompile.common import PrecompileCommon
from client.bcoserror import PrecompileError
from client.common import common


class Entry:
    """
    define Entry
    """

    def __init__(self):
        self._fields = {}

    def get(self, key):
        """
        get value according to key
        """
        return self._fields[key]

    def put(self, key, value):
        """
        set <key, value> into the field
        """
        self._fields[key] = value

    def get_fields(self):
        return self._fields


class Table:
    """
    define Table
    """

    def __init__(self, table_name, table_key, table_fields, optional=""):
        """
        init table name and table key
        """
        self._table_name = table_name
        self._table_key = table_key
        self._table_fields = table_fields
        self._optional = optional

    def get_table_name(self):
        return self._table_name

    def get_table_key(self):
        return self._table_key

    def get_table_fields(self):
        return self._table_fields

    def get_optional(self):
        return self._optional

    def getEntry(self):
        """
        new entry
        """
        return Entry()

    def get_condition(self):
        """
        new Condition
        """
        return Condition()


class CRUDService:
    """
    implementation of CRUDService
    """

    def __init__(self, contract_path):
        """
        init precompile address for TableFactory precompile and CRUDPrecompile
        """
        self.tableFactory_address = "0x0000000000000000000000000000000000001001"
        self.crud_address = "0x0000000000000000000000000000000000001002"
        self.contract_path = contract_path
        self.gasPrice = 300000000
        self.client = transaction_common.TransactionCommon(
            self.crud_address, contract_path, "CRUD")
        self.tableFactory_client = transaction_common.TransactionCommon(
            self.tableFactory_address, contract_path, "TableFactory")
        self.define_const()

    def define_const(self):
        """
        define const value for CURD
        """
        self._max_table_key_len = 255

    # interface releated to TableFactory
    def create_table(self, table):
        """
        function createTable(string, string, string) public returns (int)
        """
        fn_name = "createTable"
        fn_args = [table.get_table_name(), table.get_table_key(), table.get_table_fields()]
        return self.tableFactory_client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)

    def check_key_length(self, key):
        """
        check key length
        """
        if len(key) >= self._max_table_key_len:
            raise Exception('''The value of the table key exceeds
                            the maximum limit {}'''.
                            format(self._max_table_key_len))

    def insert(self, table, entry):
        """
        insert(string tableName, string key, string entry,
               string optional)
        """
        key_value = self.get_value_for_key(table, entry)
        self.check_key_length(table.get_table_key())
        fn_name = "insert"
        fn_args = [table.get_table_name(), key_value, json.dumps(
            entry.get_fields()), table.get_optional()]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)

    def get_key_value_from_condition(self, table, condition):
        """
        get key value from the condition
        """
        condition_map = condition.get_conditions().get(table.get_table_key())
        if condition_map is None:
            raise Exception('''Must set condition for the primary key {}'''.format(
                table.get_table_key()))
        key_value = ""
        if len(condition_map.keys()) == 0:
            raise Exception('''Must set one condition for the primary key {}'''.format(
                table.get_table_key()))
        if len(condition_map.keys()) > 1:
            raise Exception('''Only support set one condition for the primary key {}'''.format(
                table.get_table_key()))
        for key, value in condition_map.items():
            key_value = value
            break
        return key_value

    def get_value_for_key(self, table, entry):
        """
        get value for the key
        """
        if table.get_table_key() is None:
            raise Exception('''Must provide the table key''')
        key = entry.get(table.get_table_key())
        if key is None:
            raise Exception('''Must provide value for the table key {}'''.format(
                table.get_table_key()))
        return key

    def update(self, table, entry, condition):
        """
        function update(string tableName, string key, string entry,
                string condition, string optional) public returns(int);
        """
        key_value = self.get_key_value_from_condition(table, condition)
        self.check_key_length(key_value)
        fn_name = "update"
        fn_args = [table.get_table_name(), key_value,
                   json.dumps(entry.get_fields()), json.dumps(condition.get_conditions()),
                   table.get_optional()]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)

    def remove(self, table, condition):
        """
        function remove(string tableName, string key,
                    string condition, string optional) public returns(int);
        """
        key_value = self.get_key_value_from_condition(table, condition)
        self.check_key_length(key_value)
        fn_name = "remove"
        fn_args = [table.get_table_name(), key_value,
                   json.dumps(condition.get_conditions()), table.get_optional()]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)

    def select(self, table, condition):
        """
        function select(string tableName, string key, string condition,
                 string optional) public constant returns(string)
        """
        key_value = self.get_key_value_from_condition(table, condition)

        self.check_key_length(key_value)
        fn_name = "select"
        fn_args = [table.get_table_name(), key_value, json.dumps(
            condition.get_conditions()), table.get_optional()]
        return self.client.call_and_decode(fn_name, fn_args)

    def desc(self, table_name):
        self.check_key_length(table_name)
        selected_user_table = PrecompileCommon.USER_TABLE_PREFIX + table_name
        table = Table(PrecompileCommon.SYS_TABLE,
                      PrecompileCommon.SYS_TABLE_KEY, "")
        condition = table.get_condition()
        condition.eq(PrecompileCommon.SYS_TABLE_KEY, selected_user_table)
        user_table = self.select(table, condition)
        if common.check_result(user_table) is False:
            return
        if user_table is not None:
            user_table_list = list(user_table)
            if user_table_list is None:
                return None
            if len(user_table_list) < 1:
                return None
            user_table_obj = json.loads(user_table_list[0])
            if user_table_obj is None:
                return None
            if len(user_table_obj) < 1:
                return None
            if "key_field" not in user_table_obj[0].keys():
                return None
            key_field = user_table_obj[0]["key_field"]
            value_field = user_table_obj[0]["value_field"]
            print("INFO >> table {}".format(table_name))
            print("     >> key_field: {}".format(key_field))
            print("     >> value_field: {}".format(value_field))
            return Table(table_name, key_field, value_field)
        else:
            raise PrecompileError("The table {} doesn't exits!".format(table_name))
