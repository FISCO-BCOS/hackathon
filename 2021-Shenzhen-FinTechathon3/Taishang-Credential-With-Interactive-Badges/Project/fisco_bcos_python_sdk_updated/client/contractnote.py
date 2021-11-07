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
from configobj import ConfigObj
import time
import os


class ContractNote:

    @staticmethod
    def get_last_contracts():
        config = ConfigObj(client_config.contract_info_file, encoding='UTF8')
        if "address" not in config:
            return None
        return config["address"]

    @staticmethod
    def get_history_list():
        config = ConfigObj(client_config.contract_info_file, encoding='UTF8')
        if "history" not in config:
            return None
        return config["history"]

    @staticmethod
    def get_last(name):
        config = ConfigObj(client_config.contract_info_file, encoding='UTF8')
        if name in config["address"]:
            address = config["address"][name]
        else:
            address = None
        return address

    @staticmethod
    def get_address_history(address):
        config = ConfigObj(client_config.contract_info_file,
                           encoding='UTF8')
        try:
            if address in config["history"]:
                historystr = config["history"][address]
                res = historystr.split("|")
                detail = {}
                detail["name"] = res[0].strip()
                detail["timestr"] = res[1].strip()
                detail["blocknum"] = res[2].strip()
                detail["txhash"] = res[3].strip()
                return detail
        except Exception as e:
            print(e)
            import traceback
            traceback.print_exc()
            return None
        return None

    @staticmethod
    def save_address_to_contract_note(contractname, newaddress):
        # write to file
        config = ConfigObj(client_config.contract_info_file,
                           encoding='UTF8')
        if 'address' not in config:
            # print("address not in config",config)
            config['address'] = {}
        print("save new address {} -> {}".format(contractname, newaddress))
        config['address'][contractname] = newaddress
        config.write()

    @staticmethod
    def save_history(contractname, newaddress, blocknum=None, txhash=None):
        # print (config)
        config = ConfigObj(client_config.contract_info_file,
                           encoding='UTF8')
        if blocknum is not None:
            if "history" not in config:
                config["history"] = {}
            timestr = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
            if blocknum is None:
                blocknum = "-1"  # -1 means unknown
            if txhash is None:
                txhash = ""
            detail = "{} | {} | {} | {}".format(contractname, timestr, blocknum, txhash)
            config["history"][newaddress] = detail
        config.write()

    @staticmethod
    def save_contract_address(contract_name, newadddress):
        """
        record the deployed contract address to the file
        """
        cache_dir = ".cache/"
        if os.path.exists(cache_dir) is False:
            os.makedirs(cache_dir)
        cache_file = cache_dir + contract_name
        fp = open(cache_file, 'a')
        fp.write(newadddress + "\n")
        fp.close()

    @staticmethod
    def get_contract_addresses(contract_name):
        """
        get contract address according to the file
        """
        cache_dir = ".cache/"
        cache_file = cache_dir + contract_name
        if os.path.exists(cache_file) is False:
            return None
        # get addresses
        fp = open(cache_file, 'r')
        lines = fp.readlines()
        contract_addresses = []
        for line in lines:
            line = line.strip('\n')
            contract_addresses.append(line)
        fp.close()
        return contract_addresses
