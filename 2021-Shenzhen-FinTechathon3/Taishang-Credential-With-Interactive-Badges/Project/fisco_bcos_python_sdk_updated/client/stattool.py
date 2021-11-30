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
from client import clientlogger
import time


class StatTool:
    time_begin = time.time()
    time_end = 0
    time_used = 0
    unit = "ms"
    @staticmethod
    def begin(unit="ms"):
        stat = StatTool()
        stat.time_begin = time.time()
        stat.unit = unit
        return stat

    def done(self):
        self.time_end = time.time()
        self.time_used = self.time_end - self.time_begin

    def make_statmsg(self, msg):
        if self.time_end == 0:
            self.done()
        timeused_toshow = self.time_used

        if self.unit == "ms":
            timeused_toshow = timeused_toshow * 1000

        statmsg = "%.3f%s,%s" % (timeused_toshow, self.unit, msg)
        return statmsg

    def debug(self, msg):
        clientlogger.statlogger.debug(self.make_statmsg(msg))

    def info(self, msg):
        clientlogger.statlogger.info(self.make_statmsg(msg))

    def error(self, msg):
        clientlogger.statlogger.info(self.make_statmsg(msg))
