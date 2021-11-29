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
'''


class TransactionStatusCode:
    """
    the common transaction retcode
    """
    status_code_to_message = {}
    status_code_to_message[1] = "Unknown"
    status_code_to_message[2] = "Bad RLP"
    status_code_to_message[3] = "Invalid format"
    status_code_to_message[4] = "The contract to deploy is too long(or input data is too long)"
    status_code_to_message[5] = "Invalid signature"
    status_code_to_message[6] = "Invalid nonce"
    status_code_to_message[7] = "Not enough cash"
    status_code_to_message[8] = "Input data is too long"
    status_code_to_message[9] = "Block gas limit reached"
    status_code_to_message[10] = "Bad instruction"
    status_code_to_message[11] = "Bad jump destination"
    status_code_to_message[12] = "Out-of-gas during EVM execution"
    status_code_to_message[13] = "Out of stack"
    status_code_to_message[14] = "Stack underflow"
    status_code_to_message[15] = "Nonce check fail"
    status_code_to_message[16] = "Block limit check fail"
    status_code_to_message[17] = "Filter check fail"
    status_code_to_message[18] = "No deploy permission"
    status_code_to_message[19] = "No call permission"
    status_code_to_message[20] = "No tx permission"
    status_code_to_message[21] = "Precompiled error"
    status_code_to_message[22] = "Revert instruction"
    status_code_to_message[23] = "Invalid zero signature format"
    status_code_to_message[24] = "Address already used"
    status_code_to_message[25] = "Permission denied"
    status_code_to_message[26] = "Call address error"
    status_code_to_message[27] = "Gas overflow"
    status_code_to_message[28] = "Transaction pool is full"
    status_code_to_message[29] = "Transaction refused"
    status_code_to_message[30] = "The contract has been frozen"
    status_code_to_message[31] = "The account has been frozen"
    status_code_to_message[10000] = "Transaction already known"
    status_code_to_message[10001] = "Transaction already in chain"
    status_code_to_message[10002] = "Invalid chain id"
    status_code_to_message[10003] = "Invalid group id"
    status_code_to_message[10004] = "The request doesn't belong to the grou"
    status_code_to_message[10005] = "Malformed transaction"
    status_code_to_message[10006] = "Exceeded the group transaction pool capacity limit"

    @staticmethod
    def get_error_message(status):
        """
        get message according to status
        """
        if status != 0 and status in TransactionStatusCode.status_code_to_message.keys():
            return TransactionStatusCode.status_code_to_message[status]
        return None
