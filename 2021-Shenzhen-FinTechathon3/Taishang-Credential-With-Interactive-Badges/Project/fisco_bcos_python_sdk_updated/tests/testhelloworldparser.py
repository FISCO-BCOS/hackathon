'''
  @author: kentzhang
  @date: 2019-06
'''
import rlp
from client.bcostransactions import serializable_unsigned_transaction_from_dict
from client.datatype_parser import DatatypeParser

import json

from eth_abi import decode_single
from eth_account.account import Account
from eth_utils import decode_hex, encode_hex, to_checksum_address
from utils.abi import get_fn_abi_types_single
from utils.contracts import encode_transaction_data, get_function_info

if(True):
    parser = DatatypeParser()
    parser.load_abi_file("contracts/HelloWorld.abi")
    parser.parse_abi()
    fn_name = "set"
    contract_abi = parser.contract_abi
    args=["1234"]
    set_tx_data = encode_transaction_data(fn_name, contract_abi, None, args)
    print("set_tx_data:",set_tx_data)
    contractAddress = to_checksum_address("0x882be29b2d5ac85d6c476fa3fd5f0cae4b4585cc")
    txmap = dict()
    txmap["randomid"] = 10003  # 测试用 todo:改为随机数
    txmap["gasPrice"] = 30000000
    txmap["gasLimit"] = 30000000
    txmap["blockLimit"] = 501  # 测试用，todo：从链上查一下
    txmap["to"] = contractAddress
    txmap["value"] = 0
    txmap["data"] = set_tx_data
    txmap["fiscoChainId"] = 1
    txmap["groupId"] = 1
    txmap["extraData"] = ""
    # 将mapping构建一个transaction对象,非必要，用来对照的
    transaction = serializable_unsigned_transaction_from_dict(txmap)
    # 感受下transaction encode的原始数据
    print(encode_hex(rlp.encode(transaction)))
    ac3 = Account.from_key("255f01b066a90853a0aa18565653d7b944cd2048c03613a9ff31cb9df9e693e5")
    print("test from key")
    print("read from file: address", ac3.address)
    print("pubkey: ", ac3.publickey)
    print("privkey: ", encode_hex(ac3.key))
    # 实际上只需要用sign_transaction就可以获得rawTransaction的编码数据了,input :txmap,私钥
    signedTxResult = Account.sign_transaction(txmap, ac3.privateKey)
    print(signedTxResult)
    print(encode_hex(signedTxResult["rawTransaction"]))
    if False:
        #call encode
        fn_name = "get"
        contract_abi = parser.contract_abi
        args=[]
        res = encode_transaction_data(fn_name, contract_abi, None, args)
        print("tx data:",res)
        outputdata = "08c379a0000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000364572726f7220616464726573733a6635656338656232623962666333386161656335616430303438643936663463343134306437333600000000000000000000";
        fn_abi, fn_selector, fn_arguments = get_function_info(
            fn_name, contract_abi, None, args, None,
        )
        fn_output_types = get_fn_abi_types_single(fn_abi, "outputs")
        try:
            decoderesult = decode_single(fn_output_types, decode_hex(outputdata))
            print(decoderesult)
        except BaseException as e:
            print("passerror : ",e)
