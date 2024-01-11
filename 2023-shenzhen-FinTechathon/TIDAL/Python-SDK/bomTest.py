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
  @author: kentzhang
  @date: 2019-06
"""

from client.bcosclient import BcosClient
import os
from client.stattool import StatTool
from client.datatype_parser import DatatypeParser
from client.common.compiler import Compiler
from client_config import client_config
from client.bcoserror import BcosException, BcosError
import traceback
import json
# 从文件加载abi定义
# if os.path.isfile(client_config.solc_path) or os.path.isfile(client_config.solcjs_path):
#     Compiler.compile_file("contracts/Table.sol")
#     Compiler.compile_file("contracts/TableTest.sol")

# 以下是查询类的接口，大部分是返回json，可以根据对fisco bcos rpc接口json格式的理解，进行字段获取和转码
"""
useful helper:
int(num,16)  hex -> int
hex(num)  : int -> hex
"""


def init_contract():
    abi_file = "abi.json"
    data_parser = DatatypeParser()
    data_parser.load_abi_file(abi_file)
    contract_abi = data_parser.contract_abi
    client = BcosClient()
    contract_address = 'Your Address'

    return client, contract_address, contract_abi


def end_contract(client):
    client.finish()


def selectByKey(aimKey):
    # Initialize the contract.
    client, contract_address, contract_abi = init_contract()
    
    # Call select function.
    indices = client.call(contract_address, contract_abi, "select", [aimKey])
    for item in indices:
        if len(item) == 0:
            return None

    # End the contract.
    end_contract(client)

    return {"Key": indices[0][0], "databaseIndices": indices[1][0].split(','), "hashValue": indices[2][0]}


def insertValues(aimKey, databaseIndices, hashValue):
    # Initialize the contract.
    client, contract_address, contract_abi = init_contract()

    if len(databaseIndices) > 1:
        # It contains more than 1 databases.
        insertVal = ",".join(databaseIndices)
    else:
        insertVal = databaseIndices[0]
    args_insert = [aimKey, insertVal, hashValue]

    client.call(contract_address, contract_abi, "insert", args_insert)
    # If you want to make some change on the blockchain, you have to send the transaction.
    client.sendRawTransactionGetReceipt(
        contract_address, contract_abi, "insert", args_insert)
    # End the contract.
    end_contract(client)


def updateHashOnKey(aimKey, newHash):
    # Initialize the contract.
    client, contract_address, contract_abi = init_contract()

    args_update = [aimKey, newHash]
    client.call(contract_address, contract_abi, "update", args_update)
    client.sendRawTransactionGetReceipt(
        contract_address, contract_abi, "update", args_update)

    # End the contract.
    end_contract(client)


def deleteByKey(aimKey, databaseIndices):
    # Initialize the contract.
    client, contract_address, contract_abi = init_contract()

    if len(databaseIndices) > 1:
        # It contains more than 1 databases.
        insertVal = ",".join(databaseIndices)
    else:
        insertVal = databaseIndices[0]

    args_remove = [aimKey, insertVal]
    client.call(contract_address, contract_abi, "remove", args_remove)
    client.sendRawTransactionGetReceipt(
        contract_address, contract_abi, "remove", args_remove)
    # End the contract.
    end_contract(client)


def main():
    # Call insert function.
    args_insert = ["D", ['1', '3', '5'], "2029"]
    insertValues(*args_insert)

if __name__ == '__main__':
    main()
