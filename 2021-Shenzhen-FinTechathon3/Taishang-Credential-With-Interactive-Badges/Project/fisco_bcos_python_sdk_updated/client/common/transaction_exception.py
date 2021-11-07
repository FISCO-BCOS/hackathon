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
  @file: transaction_exception.py
  @function:
  @author: yujiechen
  @date: 2019-07
'''


class TransactionException(Exception):
    """
    define common transaction exception
    """

    def __init__(self, receipt_json=None, err='TransactionException'):
        """
        define exception
        """
        Exception.__init__(self, err)
        self.receipt = receipt_json

    def get_status_error_info(self):
        """
        get status information
        """
        if int(self.receipt['status'], 16) != 0:
            return '''transaction failure for non-zero status,
                    status:{}'''.format(self.receipt['status'])
        return "valid transaction receipt, status: {}".format(self.receipt['status'])

    def get_output_error_info(self):
        """
        get output information
        """
        if self.receipt['output'] is None:
            return "transaction failure for empty output"
        return "valid transaction receipt, output: {}".format(self.receipt['output'])
