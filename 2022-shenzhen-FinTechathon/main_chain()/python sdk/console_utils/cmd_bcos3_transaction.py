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
  @function:
  @author: kentzhang
  @date: 2020-10
'''
import compileall
import json
import os
import sys
import traceback

from bcos3sdk.bcos3client import Bcos3Client
from client.bcoserror import BcosException
from client.common import common
from client.common.compiler import Compiler
from client.contractnote import ContractNote
from client.datatype_parser import DatatypeParser
from client_config import client_config
from console_utils.console_common import fill_params, match_input_params
from console_utils.console_common import list_files
from utils.abi import abi_to_signature

contracts_dir = "contracts"


class CmdBcos3Transaction:
    cmd_config=client_config
    def __init__(self,config_instance=client_config):
       self.cmd_config = config_instance
    @staticmethod
    def make_usage():
        usagemsg = []
        usagemsg.append(
            """
>> deploy [contract_name]:
部署合约, 新地址会写入本地记录文件,

>> call [contractname] [address] [func]  [args...]
call合约的一个只读接口,解析返回值,address可以是last或latest,表示调用最近部署的该合约实例

>> sendtx [contractname]  [address] [func] [args...]
发送交易调用指定合约的接口，交易如成功，结果会写入区块和状态，address可以是last或latest,表示调用最近部署的该合约实例
""")
        return usagemsg
    
    @staticmethod
    def usage():
        usagemsg = CmdBcos3Transaction.make_usage()
        for m in usagemsg:
            print(m)
    
    def deploy(self, inputparams):
        print("BCOS3 Deploy start------------->")
        print("user input:",inputparams)
        tx_client = Bcos3Client()
        print(tx_client.getinfo())
        if len(inputparams) == 0:
            print(">> Without contractname , try these:")
            sols = list_files(contracts_dir + "/*.sol")
            for sol in sols:
                print(sol + ".sol")
            return
        """deploy abi bin file"""
        paramtypes = [("contractname",str,None),("args",list,[])]
        (contractname,fn_args) = match_input_params(inputparams,paramtypes)

        (fname, extname) = os.path.splitext(contractname)
        if extname.endswith("wasm"):
            contractfile = ""
            contract_abi_file = f"{tx_client.config.contract_dir}/{fname}.abi"
            contract_bin_file = f"{tx_client.config.contract_dir}/{fname}.wasm"
        else:
            contract_abi_file = f"{tx_client.config.contract_dir}/{fname}.abi"
            contractfile = f"{tx_client.config.contract_dir}/{fname}.sol"
            contract_bin_file = f"{tx_client.config.contract_dir}/{fname}.bin"
            
        common.backup_file(contract_abi_file)
        common.backup_file(contract_bin_file)
        if contractfile.endswith(".sol"):
            Compiler.compile_file(contractfile)
        try:
            
            print(f"Deploy bin file: {contract_bin_file}")
            abiparser = DatatypeParser(contract_abi_file)
            (contract_abi,args) = abiparser.format_abi_args(None,fn_args)
            receipt = tx_client.deployFromFile(contractbinfile=contract_bin_file, fn_args=args)
            print("INFO >> client info: {}".format(tx_client.getinfo()))
            print(
                "deploy result  for [{}] is:\n {}".format(
                    contractname, json.dumps(receipt, indent=4)
                )
            )
            if receipt["status"] !=0  :
                if 'errorMessage' in receipt:
                    msg = receipt['errorMessage']
                print(f"Deploy Error, status: {receipt['status']},msg: {msg}")
                return
            address = receipt["contractAddress"]
            if address is None or len(address) == 0 :
                if 'errorMessage' in receipt:
                    msg = receipt['errorMessage']
                print(f"Deploy Error,address is empty， status: {receipt['status']},msg: {msg}")
                return
            
            blocknum = receipt["blockNumber"]
            txhash = receipt["transactionHash"]

            print("on block : {},address: {} ".format(blocknum, address))
            ContractNote.save_address_to_contract_note(tx_client.get_full_name(),contractname, address)
            print("address save to file: ", tx_client.config.contract_info_file)
            ContractNote.save_history(tx_client.get_full_name(),contractname,  address, blocknum, txhash,
                                      contract_info_file=tx_client.config.contract_info_file)
            # contractabi = f"{tx_client.config.contract_dir}/{contractname}.abi"
            # data_parser = DatatypeParser(contractabi)
            # 解析receipt里的log 和 相关的tx ,output
            # print_receipt_logs_and_txoutput(tx_client, receipt, "", data_parser)
        except Exception as e:
            print("deploy exception! ", e)
            traceback.print_exc()
            tx_client.finish()



    
    def call(self, inputparams):
        if len(inputparams) == 0:
            sols = list_files(contracts_dir + "/*.sol")
            for sol in sols:
                print(sol + ".sol")
            return
        paramtypes = [("contractname",str,None),("address",str,None),("fn_name",str,None),("fn_args",list,[])]
        (contractname,address,fn_name,fn_args) = match_input_params(inputparams,paramtypes)

        tx_client = Bcos3Client()
        print(tx_client.getinfo())
        if address == "last" or address == "latest":
            address = ContractNote.get_last(tx_client.get_full_name(),contractname)
            if address is None:
                sys.exit(
                    "can not get last address for [{}],break;".format(contractname)
                )

        abiparser = DatatypeParser(f"{tx_client.config.contract_dir}/{contractname}.abi")
        
        print("INFO>> client info: {}".format(tx_client.getinfo()))
        print(
            "INFO >> call {} , address: {}, func: {}, args:{}".format(
                contractname, address, fn_name, fn_args
            )
        )
        try:
            (contract_abi,args) = abiparser.format_abi_args(fn_name,fn_args)
            result = tx_client.call(address, abiparser.contract_abi, fn_name, args)
            print("Call Result:", result)
        
        except Exception as e:
            common.print_error_msg("call", e)
    
    # 2021.02版本已经支持创建不同的账户来发送交易，考虑到python命令行控制台的输入繁琐（也不像java控制台这样是预加载账户
    # 所以暂时未支持在控制台命令行传入账户名，如需用不同账户发送交易，可以切换到不同的目录或配置文件
    # 如果自己写代码调用，则可以指定不同的账户了
    def sendtx(self, inputparams):
        if len(inputparams) == 0:
            sols = list_files(contracts_dir + "/*.sol")
            for sol in sols:
                print(sol + ".sol")
            return
        paramtypes = [("contractname",str,None),("address",str,None),("fn_name",str,None),("fn_args",list,[])]
        (contractname,address,fn_name,fn_args) = match_input_params(inputparams,paramtypes)
        tx_client = Bcos3Client()
        print(tx_client.getinfo())
        if address == "last" or address == "latest":
            address = ContractNote.get_last(tx_client.get_full_name(),contractname)
            if address is None:
                sys.exit(
                    "can not get last address for [{}],break;".format(contractname)
                )
        print("INFO>> client info: {}".format(tx_client.getinfo()))
        print(
            "INFO >> sendtx {} , address: {}, func: {}, args:{}".format(
                contractname, address, fn_name, fn_args
            )
        )
        try:
            abiparser = DatatypeParser(f"{tx_client.config.contract_dir}/{contractname}.abi")
            (contract_abi,args) = abiparser.format_abi_args(fn_name,fn_args)
            print("sendtx:",args)
            result = tx_client.sendRawTransaction(address, abiparser.contract_abi, fn_name, args)
            # 解析receipt里的log 和 相关的tx ,output
            print(f"Transaction result >> \n{result}")
            print(f"Transaction Status >> {result['status']}")
            output = result['output']
            output = abiparser.parse_output(fn_name, output)
            print(f"Transaction Output >> {output}")
            if "logEntries" in result:
                logs = abiparser.parse_event_logs(result["logEntries"])
                print("transaction receipt events >>")
                n = 1
                for log in logs:
                    print(f"{n} ):{log['eventname']} -> {log['eventdata']}")
                    n = n + 1
        except Exception as e:
            common.print_error_msg("sendtx", e)
    
    def deploylast(self):
        contracts = ContractNote.get_last_contracts(contract_info_file=self.cmd_config.contract_info_file)
        for name in contracts:
            print("{} -> {}".format(name, contracts[name]))
    
    def deploylog(self):
        historys = ContractNote.get_history_list(contract_info_file=self.cmd_config.contract_info_file)
        for address in historys:
            print("{} -> {} ".format(address, historys[address]))

    def abi(self,inputparams):
        paramtypes = [("contractname",str,None),("detail",int,0)]
        (contractname,detail) = match_input_params(inputparams,paramtypes)
        abifile = f"{self.cmd_config.contract_dir}/{contractname}.abi"
        parser = DatatypeParser(abifile)
        print(f"functions in con"f"tract [{contractname}] --> ")
        n = 0
        for (name,func) in parser.func_abi_map_by_name.items():
            n = n+1
            sig = abi_to_signature(func)
            print(f"{n} ) : {sig}")
            if detail == 1:
                print(f"abi:{func}")
        n = 0
        print(f"\n\nEvents in con"f"tract [{contractname}] --> ")
        for (name,event) in parser.event_name_map.items():
            n = n+1
            sig = abi_to_signature(event)
            print(f"{n} ) : {sig}")
            if detail == 1:
                print(f"abi:{event}")