'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @file: config_precompile.py
  @function:
  @author: yujiechen
  @date: 2019-07
'''

from client.common import transaction_common


class ConfigPrecompile:
    """
    implementation of ConfigPrecompile
    """

    def __init__(self, contract_path):
        """
        init the address for SystemConfig contract
        """
        self._config_address = "0x0000000000000000000000000000000000001000"
        self.gasPrice = 300000000
        self.client = transaction_common.TransactionCommon(
            self._config_address, contract_path, "SystemConfig")

    def setValueByKey(self, key, value):
        """
        set value for the givn key
        """
        fn_name = "setValueByKey"
        fn_args = [key, value]
        return self.client.send_transaction_getReceipt(fn_name, fn_args, self.gasPrice)
