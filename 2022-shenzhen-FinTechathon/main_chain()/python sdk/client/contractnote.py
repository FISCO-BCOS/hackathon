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

import os
import time

from configobj import ConfigObj


class ContractNote:
    
    @staticmethod
    def get_last_contracts(segment,contract_info_file="bin/contract.ini"):
        config = ConfigObj(contract_info_file, encoding='UTF8')
        key = f"address.{segment}"
        if key not in config:
            return None
        return config[key]
    
    @staticmethod
    def get_history_list(segment,contract_info_file="bin/contract.ini"):
        config = ConfigObj(contract_info_file, encoding='UTF8')
        key = f"history.{segment}"
        if  key not in config:
            return None
        return config[key]
    
    @staticmethod
    def get_last(segment,name, contract_info_file="bin/contract.ini"):
        config = ConfigObj(contract_info_file, encoding='UTF8')
        key = f"address.{segment}"
        if key not in config:
            return None
        if name in config[key]:
            address = config[key][name]
        else:
            address = None
        return address
    
    @staticmethod
    def get_address_history(segment,address, contract_info_file="bin/contract.ini"):
        config = ConfigObj(contract_info_file,
                           encoding='UTF8')
        try:
            key = f"history.{segment}"
            if key not in config:
                return None
            if address in config[key]:
                historystr = config[key][address]
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
    def save_address_to_contract_note(segment,contractname, newaddress, contract_info_file="bin/contract.ini"):
        # write to file
        config = ConfigObj(contract_info_file,
                           encoding='UTF8')
        key = f"address.{segment}"
        if key not in config:
            # print("address not in config",config)
            config[key] = {}
        print("save new address {} -> {}".format(contractname, newaddress))
        config[key][contractname] = newaddress
        config.write()
    
    @staticmethod
    def save_history(segment,contractname, newaddress, blocknum=None, txhash=None, contract_info_file="bin/contract.ini"):
        # print (config)
        config = ConfigObj(contract_info_file,
                           encoding='UTF8')
        key = f"history.{segment}"
        if blocknum is not None:
            if key not in config:
                config[key] = {}
            timestr = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
            if blocknum is None:
                blocknum = "-1"  # -1 means unknown
            if txhash is None:
                txhash = ""
            detail = "{} | {} | {} | {}".format(contractname, timestr, blocknum, txhash)
            config[key][newaddress] = detail
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
