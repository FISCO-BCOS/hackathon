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
  @author: kentzhang
  @date: 2019-06
'''


class BcosError(Exception):
    code = None
    data = None
    message = None

    def __init__(self, code, data, msg):
        self.code = code
        self.data = data
        self.message = msg

    def info(self):
        return "code :{},data :{},message : {}".format(self.code, self.data, self.message)


class BcosException(Exception):
    """
    the exception should be catched
    """

    def __init__(self, msg=None):
        super().__init__(msg)


class PrecompileError(BcosException):
    """
    PrecompileError
    """

    def __init__(self, msg=None):
        super().__init__(msg)


class ArgumentsError(BcosException):
    """
    ArgumentsError
    """

    def __init__(self, msg=None):
        super().__init__(msg)


class CompileError(BcosException):
    """
    CompileError
    """

    def __init__(self, msg=None):
        super().__init__(msg)


class CompilerNotFound(BcosException):
    """
    CompileError
    """

    def __init__(self, msg=None):
        super().__init__(msg)


class ChannelException(BcosException):
    """
    exception when init channel
    """

    def __init__(self, msg=None):
        super().__init__(msg)
