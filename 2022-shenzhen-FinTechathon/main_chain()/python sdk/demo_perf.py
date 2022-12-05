'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @file: transaction_common.py
  @function:
  @author: yujiechen
  @date: 2019-07
'''

from client.common.transaction_common import TransactionCommon
from concurrent.futures import ThreadPoolExecutor
from client.bcoserror import BcosException, BcosError
import multiprocessing
import random
import sys

contract_addr = ""
contract_path = "contracts"
contract_name = "Ok"
process_num = 8
total_count = int(sys.argv[1])
step = int(total_count / process_num)


def sendRequest(client):
    try:
        # generate random number
        trans_num = random.randint(1, 1000)
        fn_name = "trans"
        fn_args = [trans_num]
        client.send_transaction_getReceipt(fn_name, fn_args)
    except BcosException as e:
        print("call trans contract failed, reason: {}".format(e))
    except BcosError as e:
        print("call trans contract failed, reason: {}".format(e))


def sendMultiRequests(process_id):
    """
    send trans request
    """
    client = TransactionCommon("",
                               contract_path,
                               contract_name)
    executor = ThreadPoolExecutor(max_workers=10)
    for i in range(0, step):
        executor.submit(sendRequest, (client))
    print("process {}, send {}%, tx_num: {}".format(process_id, step * 100 / total_count, step))
    executor.shutdown(True)
    print("\t\t process {}, receive {} %, tx_num:{}".format(
        process_id, step * 100 / total_count, step))


def main(argv):
    """
    1. deploy Ok.sol
    2. send transactions: set
    """
    # deploy Ok contract
    gasPrice = 30000000
    client = TransactionCommon("",
                               contract_path,
                               contract_name)
    result = client.send_transaction_getReceipt(None, None, gasPrice, True)[0]
    contract_addr = result['contractAddress']
    client.set_contract_addr(contract_addr)
    pool = multiprocessing.Pool(process_num)
    args_list = []
    for i in range(0, process_num):
        args_list.append(i)
    pool.map(sendMultiRequests, args_list)
    pool.close()
    pool.join()
    client.finish()


if __name__ == "__main__":
    main(sys.argv[1:])
