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
import shutil
import time
import os
import json
import subprocess
import re
from client.datatype_parser import DatatypeParser
from eth_utils.hexadecimal import decode_hex
from client_config import client_config
from eth_utils import to_checksum_address
from utils.contracts import get_function_info
from utils.abi import get_fn_abi_types_single
from client.bcoserror import ArgumentsError, BcosException
from eth_abi import decode_single
from eth_utils.hexadecimal import bytesToHex


def backup_file(file_name):
    """
    backup files
    """
    if os.path.isfile(file_name) is False:
        return
    forcewrite = True
    option = "y"
    if client_config.background is False:
        option = input("INFO >> file [{}] exist , continue (y/n): ".format(file_name))
    if (option.lower() == "y"):
        forcewrite = True
    else:
        forcewrite = False
        print("skip write to file: {}".format(file_name))

    # forcewrite ,so do backup job
    if(forcewrite):
        filestat = os.stat(file_name)
        filetime = time.strftime("%Y%m%d%H%M%S", time.localtime(filestat.st_ctime))
        filepath, shortname = os.path.split(file_name)
        backuppath = "{}/{}".format(filepath, "backup")
        if not os.path.exists(backuppath):
            os.mkdir(backuppath)
        backupfile = "{}/{}.{}".format(backuppath, shortname, filetime)
        print("backup [{}] to [{}]".format(file_name, backupfile))
        shutil.copyfile(file_name, backupfile)
    return forcewrite


def print_info(level, cmd):
    """
    print information
    """
    print("{} : {}".format(level, cmd))


def print_result(ret):
    """
    print result
    """
    if isinstance(ret, dict):
        print_info("    ", "{}".format(json.dumps(ret, indent=4)))
    elif isinstance(ret, list):
        if len(ret) > 0:
            for ret_item in ret:
                print_result(ret_item)
        else:
            print_info("    ", "Empty Set")
    else:
        print_info("    ", "{}".format(ret))


def check_address_startwith_0x(address):
    """
    check the address: must be starts with 0x
    """
    if address.startswith("0x") is False:
        raise ArgumentsError("invalid address {}, must be start with 0x".format(address))


def check_and_format_address(address):
    """
    check address
    """
    try:
        formatted_address = to_checksum_address(address)
        return formatted_address
    except Exception as e:
        raise ArgumentsError("invalid address {}, reason: {}"
                             .format(address, e))


def execute_cmd(cmd):
    """
    execute command
    """
    data = subprocess.check_output(cmd.split(), shell=False, universal_newlines=True)
    status = 0
    return (status, data)


def print_error_msg(cmd, e):
    """
    print error msg
    """
    print("ERROR >> execute {} failed\nERROR >> error information: {}\n".format(cmd, e))


max_block_number = pow(2, 63) - 1


def check_int_range(number_str, limit=max_block_number):
    """
    check integer range
    """
    try:
        if isinstance(number_str, int):
            return number_str
        number = 0
        if isinstance(number_str, str):
            if number_str.startswith("0x"):
                number = int(number_str, 16)
            else:
                number = int(number_str)
        else:
            number = number_str
        if number > limit or number < 0:
            raise ArgumentsError(("invalid input: {},"
                                  " must between 0 and {}").
                                 format(number_str, limit))
        return number
    except Exception as e:
        raise ArgumentsError("invalid input:{}, error info: {}".format(number_str, e))


def check_and_trans_to_bool(param):
    """
    check bool
    """
    if isinstance(param, bool):
        return param
    true_str = "true"
    false_str = "false"
    if isinstance(param, str):
        if param.lower() == true_str:
            return True
        if param.lower() == false_str:
            return False
    raise ArgumentsError(("invalid input: {}, "
                          "must be true/True/false/False").format(param))


def check_word(word):
    """
    check world
    """
    result = re.findall(r'([0x]*[a-fA-F0-9]*)', word)
    if result[0] != word:
        raise ArgumentsError(("invalid input {},"
                              " must be in 'a-f' or '0-9' or 'A-F'")
                             .format(word))


def check_hash(hash_str):
    """
    check hash
    """
    min_size = 64
    max_size = 66
    if len(hash_str) < min_size or \
        hash_str.startswith("0x") and len(hash_str) < max_size \
            or len(hash_str) > max_size:
        raise BcosException(("invalid hash: {},"
                             "expected len: {} or {}, real len: {}").
                            format(min_size, max_size,
                                   hash_str, len(hash_str)))
    check_word(hash_str)


def check_nodeId(nodeId):
    """
    check nodeId
    """
    nodeId_len = 128
    if len(nodeId) != nodeId_len:
        raise ArgumentsError("invalid nodeId, must be {} bytes".format(nodeId_len))
    check_word(nodeId)


def check_param_num(args, expected, needEqual=False):
    """
    check param num
    """
    if needEqual is False:
        if len(args) < expected:
            raise ArgumentsError(("invalid arguments, expected num >= {},"
                                  "real num: {}").format(expected, len(args)))
    else:
        if len(args) != expected:
            raise ArgumentsError(("invalid arguments, expected num {},"
                                  "real num: {}").format(expected, len(args)))


def parse_output(output, fn_name, contract_abi, args):
    fn_abi, fn_selector, fn_arguments = fn_abi, fn_selector, fn_arguments = get_function_info(
        fn_name, contract_abi, None, args, None)
    fn_output_types = get_fn_abi_types_single(fn_abi, "outputs")
    decoderesult = decode_single(fn_output_types, decode_hex(output))
    return decoderesult


def print_receipt_logs(logs):
    print("\nlogs : >> ")
    i = 0
    for log in logs:
        if 'eventname' in log:
            i = i + 1
            print("{}): log name: {} , data: {}".format(i, log['eventname'], log['eventdata']))


def print_output_and_input(logs, output, txinput, contract_name, contract_path):
    """
    parse_output_from_abi
    """
    abi_path = os.path.join(contract_path, contract_name + ".abi")
    if os.path.isfile(abi_path) is False:
        raise BcosException("parse outpt failed for {} doesn't exist"
                            .format(abi_path))
    try:
        dataParser = DatatypeParser(abi_path)
        # parse txinput
        input_result = dataParser.parse_transaction_input(txinput)
        if input_result is not None:
            print_info("txinput result", input_result)
            # get function name
            fn_name = input_result["name"]
            output_result = dataParser.parse_receipt_output(fn_name, output)
            if output_result is None:
                print_info("INFO", "empty return, output: {}".format(output))
                return
            print_info("output result", output_result)
        log_result = dataParser.parse_event_logs(logs)
        print_receipt_logs(log_result)
        # print_info("log result", log_result)

    except Exception as e:
        raise BcosException("parse output failed for reason: {}".format(e))


def parse_input(txinput, contract_name, contract_path):
    """
    parse txinput
    """
    abi_path = os.path.join(contract_path, contract_name + ".abi")
    if os.path.isfile(abi_path) is False:
        raise BcosException("parse txinput failed for {} doesn't exist"
                            .format(abi_path))
    try:
        dataParser = DatatypeParser(abi_path)
        result = dataParser.parse_transaction_input(txinput)
        return result
    except Exception as e:
        raise BcosException("parse txinput failed for reason: {}"
                            .format(e))


def print_tx_result(outputresults):
    """
    print result of call or sendtx
    """
    for result in outputresults:
        if isinstance(result, bytes):
            print("{}, ".format(bytesToHex(result)))
            continue
        print("reuslt: {}, ".format(result))


def check_result(result):
    if isinstance(result, dict) and "status" in result.keys():
        status = result["status"]
        if int(status, 16) != 0:
            print("{}".format(result))
            return False
    return True
