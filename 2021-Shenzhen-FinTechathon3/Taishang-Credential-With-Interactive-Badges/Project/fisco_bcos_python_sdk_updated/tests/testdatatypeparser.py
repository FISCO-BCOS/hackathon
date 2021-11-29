'''
  @author: kentzhang
  @date: 2019-06
'''
from client.datatype_parser import DatatypeParser

import json

if(True):
    parser = DatatypeParser()
    parser.load_abi_file("sample/SimpleInfo.abi")
    parser.parse_abi()

    with open("sample/receipt.json", "r") as f:
        receipt = json.load(f)
        f.close()
    logs = receipt["logs"]
    parser.parse_event_logs(logs)
    print("parse result")
    for log in logs:
        print(log)
    result = parser.parse_receipt_output("set", receipt['output'])
    print("output :", result)

if True:
    parser = DatatypeParser()
    parser.load_abi_file("sample/SimpleInfo.abi")
    parser.parse_abi()
    with open("sample/tx_simpleinfo_set.json", "r") as f:
        tx = json.load(f)
        f.close()
    result = parser.parse_transaction_input(tx["input"])
    print(result)
