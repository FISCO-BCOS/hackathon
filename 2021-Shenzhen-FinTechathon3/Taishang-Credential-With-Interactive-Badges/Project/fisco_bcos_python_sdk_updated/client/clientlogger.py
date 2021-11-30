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

from client_config import client_config
import logging
from logging import handlers
import os
logger = logging.getLogger(__name__)
logger.setLevel(level=logging.DEBUG)
logfile = client_config.logdir + "/client.log"
if os.path.exists(client_config.logdir) is False:
    os.mkdir(client_config.logdir)
# handler = logging.FileHandler(logfile)
handler = logging.handlers.TimedRotatingFileHandler(logfile, 'D', 1, 0)  # 切割日志
handler.suffix = '%Y%m%d'  # 切割后的日志设置后缀
# handler.setLevel(logging.DEBUG)
# formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
formatter = logging.Formatter('%(asctime)s %(levelname)s - %(message)s')
handler.setFormatter(formatter)
logger.addHandler(handler)


statlogger = logging.getLogger("STAT")
statlogger.setLevel(level=logging.DEBUG)
logfile = client_config.logdir + "/stat.log"
# handler = logging.FileHandler(logfile)
handler = logging.handlers.TimedRotatingFileHandler(logfile, 'D', 1, 0)  # 切割日志
handler.suffix = '%Y%m%d'  # 切割后的日志设置后缀
# handler.setLevel(logging.DEBUG)
# formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
formatter = logging.Formatter('%(asctime)s %(levelname)s - %(message)s')
handler.setFormatter(formatter)
statlogger.addHandler(handler)
