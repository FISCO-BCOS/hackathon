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
  @file: consensus_precompile.py
  @function:
  @author: yujiechen
  @date: 2019-07
'''
from client.common import transaction_common
from client.common import common


class ConsensusPrecompile:
    """
    implementation of ConsensusPrecompile
    """

    def __init__(self, contract_path):
        """
        init the address for Consensus contract
        """
        self._consensus_address = "0x0000000000000000000000000000000000001003"
        self.contract_name = "Consensus"
        self.gasPrice = 300000000
        self.client = transaction_common.TransactionCommon(
            self._consensus_address, contract_path, self.contract_name)

    def addSealer(self, nodeId):
        """
        addSealer
        """
        common.check_nodeId(nodeId)
        fn_name = "addSealer"
        fn_args = [nodeId]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)

    def addObserver(self, nodeId):
        """
        addObserver
        """
        common.check_nodeId(nodeId)
        fn_name = "addObserver"
        fn_args = [nodeId]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)

    def removeNode(self, nodeId):
        """
        remove Node
        """
        common.check_nodeId(nodeId)
        fn_name = "remove"
        fn_args = [nodeId]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)
