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
  # reference :https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html
  useful helper:
  int(num,16)  hex -> int
  hex(num)  : int -> hex
'''
import json
import os
import time

import utils.rpc
from client import clientlogger
from client.bcoserror import BcosError, ArgumentsError, BcosException
from client.channelhandler import ChannelHandler
from client.channelpack import ChannelPack
from client.common import common
from client.common import transaction_status_code
from client.signer_impl import Signer_GM, Signer_ECDSA, Signer_Impl
from client.signtransaction import SignTx
from client.stattool import StatTool
from client_config import client_config
from eth_abi import decode_single
from eth_utils.crypto import CRYPTO_TYPE_GM, CRYPTO_TYPE_ECDSA
from eth_utils.hexadecimal import decode_hex, encode_hex
from utils.abi import itertools, get_fn_abi_types_single, get_abi_output_types
from utils.contracts import encode_transaction_data
from utils.contracts import get_aligned_function_data
from utils.contracts import get_function_info
from eth_abi import decode_abi

class BcosClient:
    name = "bcos2"
    default_from_account_signer: Signer_Impl = None
    rpc = None
    channel_handler = None
    fiscoChainId = None
    groupid = None
    logger = clientlogger.logger  # logging.getLogger("BcosClient")
    request_counter = itertools.count()
    max_group_id = pow(2, 15) - 1
    max_chain_id = pow(2, 63) - 1
    protocol_list = ["rpc", "channel"]
    sysconfig_keys = ["tx_count_limit", "tx_gas_limit"]
    bcosconfig = None

    #修改为可以传入client_config实例，
    # 此版本采用py文件作为客户端的配置，不参与ini
    # 如果每个客户端采用不同的client_client_config,copy一份client_config.py文件，将里面的client_config改个名，修改里面的配置
    # 并将其实例传入初始化入口即可
    # 开发者需要保证配置化的py文件必须有效
    # 默认传入client_config.py里的client_config
    def __init__(self,client_config_instance=client_config):
        #print("------->>>>>>>>>",client_config_instance)
        self.init(client_config_instance)
        self.lastblocknum = 0
        self.lastblocklimittime = 0

    #返回能完整标识此客户端的字符串，先简单点，可以根据场景扩展，比如增加ip地址端口等
    def get_full_name(self):
        return f"{self.name}-{self.groupid}"

    def __del__(self):
        """
        release the resources
        """
        self.finish()

    # load the account from keyfile

    def load_default_account(self):
        if self.default_from_account_signer is not None:
            return  # 不需要重复加载

        if self.bcosconfig.crypto_type == CRYPTO_TYPE_GM:
            # 加载默认国密账号
            self.gm_account_file = "{}/{}".format(self.bcosconfig.account_keyfile_path,
                                                  self.bcosconfig.gm_account_keyfile)
            self.default_from_account_signer = Signer_GM.from_key_file(
                self.gm_account_file, self.bcosconfig.gm_account_password)
            return

        # 默认的 ecdsa 账号
        # check account keyfile
        if self.bcosconfig.crypto_type == CRYPTO_TYPE_ECDSA:
            self.key_file = "{}/{}".format(self.bcosconfig.account_keyfile_path,
                                           self.bcosconfig.account_keyfile)
            self.default_from_account_signer = Signer_ECDSA.from_key_file(
                self.key_file, self.bcosconfig.account_password)

   
    def init(self,client_config_instance = client_config):
        try:
            self.bcosconfig = client_config_instance
            self.blockLimit = 500
            # check chainID
            common.check_int_range(self.bcosconfig.groupid, BcosClient.max_group_id)
            # check group id
            common.check_int_range(self.bcosconfig.fiscoChainId, BcosClient.max_chain_id)
            # check protocol
            if self.bcosconfig.client_protocol.lower() not in BcosClient.protocol_list:
                raise BcosException("invalid configuration, must be: {}".
                                    format(''.join(BcosClient.protocol_list)))

            self.fiscoChainId = self.bcosconfig.fiscoChainId
            self.groupid = self.bcosconfig.groupid

            if self.bcosconfig.client_protocol == self.bcosconfig.PROTOCOL_RPC \
                    and self.bcosconfig.remote_rpcurl is not None:
                self.rpc = utils.rpc.HTTPProvider(self.bcosconfig.remote_rpcurl)
                self.rpc.logger = self.logger

            if self.bcosconfig.client_protocol == self.bcosconfig.PROTOCOL_CHANNEL:
                if os.path.exists(self.bcosconfig.channel_node_cert) is False:
                    raise BcosException("{} not found!".format(self.bcosconfig.channel_node_cert))
                if os.path.exists(self.bcosconfig.channel_node_key) is False:
                    raise BcosException("{} not found!".format(self.bcosconfig.channel_node_key))

                self.channel_handler = ChannelHandler()
                self.channel_handler.setDaemon(True)
                self.channel_handler.logger = self.logger
                self.channel_handler.initTLSContext(
                                                    self.bcosconfig.ssl_type,
                                                    self.bcosconfig.channel_ca,
                                                    self.bcosconfig.channel_node_cert,
                                                    self.bcosconfig.channel_node_key,
                                                    self.bcosconfig.channel_en_crt,
                                                    self.bcosconfig.channel_en_key
                                                    )
                self.channel_handler.start_channel(
                    self.bcosconfig.channel_host, self.bcosconfig.channel_port)
                blockNumber = self.getBlockNumber()
                self.channel_handler.setBlockNumber(blockNumber)
                self.channel_handler.getBlockNumber(self.groupid)

            self.logger.info("using protocol " + self.bcosconfig.client_protocol)
            return self.getinfo()
        except Exception as e:
            raise BcosException("init bcosclient failed, reason: {}".format(e))

    def finish(self):
        if self.bcosconfig.client_protocol == self.bcosconfig.PROTOCOL_CHANNEL \
           and self.channel_handler is not None:
            self.channel_handler.finish()

    def getinfo(self):
        info = ""
        if self.bcosconfig.client_protocol == self.bcosconfig.PROTOCOL_RPC:
            info = "rpc:{}\n".format(self.rpc)
        if self.bcosconfig.client_protocol == self.bcosconfig.PROTOCOL_CHANNEL:
            info = "channel {}:{}".format(self.channel_handler.host, self.channel_handler.port)
        info += ",groupid :{}".format(self.groupid)
        info += ",crypto type:{}".format(self.bcosconfig.crypto_type)
        info += ",ssl type:{}".format(self.bcosconfig.ssl_type)
        return info

    def is_error_response(self, response):
        if response is None:
            raise BcosError(-1, None, "response is None")
        result = response["result"]
        if isinstance(result, dict) and "error" in result.keys():
            msg = result["error"]["message"]
            code = result["error"]["code"]
            data = None
            if("data" in result["error"]):
                data = result["error"]["data"]
            self.logger.error("is_error_response code: {}, msg:{} ,data:{}".format(code, msg, data))
            raise BcosError(code, data, msg)
        return None

    def common_request(self, cmd, params, packet_type=ChannelPack.TYPE_RPC):
        response = None
        try:
            next(self.request_counter)
            stat = StatTool.begin()
            if self.bcosconfig.client_protocol == self.bcosconfig.PROTOCOL_RPC:
                response = self.rpc.make_request(cmd, params)
            if self.bcosconfig.client_protocol == self.bcosconfig.PROTOCOL_CHANNEL:
                response = self.channel_handler.make_channel_rpc_request(
                    cmd, params, ChannelPack.TYPE_RPC, packet_type)
            self.is_error_response(response)
            memo = "DONE"
            stat.done()
            stat.debug("commonrequest:{}:{}".format(cmd, memo))
            return response["result"]
        except Exception as e:
            # timeout exception
            exception_str = str(e).lower()
            if "timeout" in exception_str:
                raise BcosException(("{} timeout for without response after 60s, "
                                     "please check the status of the node").format(cmd))
            else:
                raise BcosError(-1, None, ("{} failed,"
                                           " params: {}, response: {}, error information: {}").
                                format(cmd, params, json.dumps(response), e))

    def getNodeVersion(self):
        """
        get node version
        // Request
        curl -X POST --data '{"jsonrpc":"2.0","method":"getClientVersion",
        "params":[],"id":1}' http://127.0.0.1:8545 |jq
        // Response
        {
        "id": 83,
        "jsonrpc": "2.0",
        "result": {
            "Build Time": "20190106 20:49:10",
            "Build Type": "Linux/g++/RelWithDebInfo",
            "FISCO-BCOS Version": "2.0.0",
            "Git Branch": "master",
            "Git Commit Hash": "693a709ddab39965d9c39da0104836cfb4a72054"
        }
        }
        """
        cmd = "getClientVersion"
        params = []
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getblocknumber
    def getBlockNumber(self):
        cmd = "getBlockNumber"
        params = [self.groupid]
        num_hex = self.common_request(cmd, params)
        return int(num_hex, 16)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getpbftview
    def getPbftView(self):
        cmd = "getPbftView"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getsealerlist

    def getSealerList(self):
        cmd = "getSealerList"
        params = [self.groupid]
        return self.common_request(cmd, params)
    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getobserverlist

    def getObserverList(self):
        cmd = "getObserverList"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getconsensusstatus
    def getConsensusStatus(self):
        cmd = "getConsensusStatus"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getsyncstatus
    def getSyncStatus(self):
        cmd = "getSyncStatus"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getpeers
    def getPeers(self):
        cmd = "getPeers"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getgrouppeers
    def getGroupPeers(self):
        cmd = "getGroupPeers"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getnodeidlist
    def getNodeIDList(self):
        cmd = "getNodeIDList"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getgrouplist
    def getGroupList(self):
        cmd = "getGroupList"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getblockbyhash
    def getBlockByHash(self, block_hash, _includeTransactions=True):
        cmd = "getBlockByHash"
        common.check_hash(block_hash)
        includeTransactions = common.check_and_trans_to_bool(_includeTransactions)
        params = [self.groupid, block_hash, includeTransactions]
        return self.common_request(cmd, params)
    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getblockbynumber

    def getBlockByNumber(self, num, _includeTransactions=True):
        """
        get block according to number
        """
        cmd = "getBlockByNumber"
        number = common.check_int_range(num)
        includeTransactions = common.check_and_trans_to_bool(_includeTransactions)
        params = [self.groupid, hex(number), includeTransactions]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getblockhashbynumber
    def getBlockHashByNumber(self, num):
        cmd = "getBlockHashByNumber"
        common.check_int_range(num)
        params = [self.groupid, hex(num)]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_cn/release-2.0/docs/api.html#gettransactionbyhash
    def getTransactionByHash(self, hash):
        cmd = "getTransactionByHash"
        common.check_hash(hash)
        params = [self.groupid, hash]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#gettransactionbyblockhashandindex
    def getTransactionByBlockHashAndIndex(self, hash, index):
        cmd = "getTransactionByBlockHashAndIndex"
        common.check_hash(hash)
        common.check_int_range(index)
        params = [self.groupid, hash, hex(index)]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#gettransactionbyblocknumberandindex
    def getTransactionByBlockNumberAndIndex(self, num, index):
        cmd = "getTransactionByBlockNumberAndIndex"
        common.check_int_range(num)
        common.check_int_range(index)
        params = [self.groupid, hex(num), hex(index)]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#gettransactionreceipt
    def getTransactionReceipt(self, hash):
        cmd = "getTransactionReceipt"
        common.check_hash(hash)
        params = [self.groupid, hash]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getpendingtransactions
    def getPendingTransactions(self):
        cmd = "getPendingTransactions"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getpendingtxsize
    def getPendingTxSize(self):
        cmd = "getPendingTxSize"
        params = [self.groupid]
        tx_size = self.common_request(cmd, params)
        return int(tx_size, 16)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getcode
    def getCode(self, address):
        cmd = "getCode"
        fmt_addr = common.check_and_format_address(address)
        params = [self.groupid, fmt_addr]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#gettotaltransactioncount
    def getTotalTransactionCount(self):
        cmd = "getTotalTransactionCount"
        params = [self.groupid]
        return self.common_request(cmd, params)

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getsystemconfigbykey
    def getSystemConfigByKey(self, key):
        if key not in BcosClient.sysconfig_keys:
            raise ArgumentsError("invalid system key, must be {}"
                                 .format(BcosClient.sysconfig_keys))
        cmd = "getSystemConfigByKey"
        params = [self.groupid, key]
        return self.common_request(cmd, params)

    def channel_getBlockLimit(self):
        """
        get blockNumber from _block_notify directly when use channelHandler
        """
        return self.channel_handler.blockNumber + self.blockLimit

    def RPC_getBlocklimit(self):
        tick = time.time()
        deltablocklimit = 500
        tickstamp = tick - self.lastblocklimittime
        self.logger.debug("blocklimit tick stamp {}".format(tickstamp))
        if tickstamp < 100:  # get blocklimit every 100sec
            return self.lastblocknum + deltablocklimit
        for i in range(0, 5):  # try n times
            try:

                blocknum = self.getBlockNumber()
                oldblocknum = self.lastblocknum
                # print("last {},now {}".format(self.lastblocknum,blocknum))
                if blocknum >= self.lastblocknum:
                    self.lastblocknum = blocknum
                    self.logger.info("getBlocklimit:{},blocknum:{},old:{}".format(
                        self.lastblocknum, blocknum, oldblocknum))
                    return self.lastblocknum + deltablocklimit
            except BcosError as e:
                self.logger.error("getBlocklimit error {}, {}".format(e.code, e.message))
                time.sleep(0.2)

                continue
        return self.lastblocknum

    def getBlockLimit(self):
        """
        get block limit
        """
        if self.channel_handler is not None:
            return self.channel_getBlockLimit()
        return self.RPC_getBlocklimit()

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getpendingtransactions

    def call(self, to_address, contract_abi, fn_name, args=None):
        cmd = "call"
        if to_address != "":
            common.check_and_format_address(to_address)

        self.load_default_account()
        functiondata = encode_transaction_data(fn_name, contract_abi, None, args)
        callmap = dict()
        callmap["data"] = functiondata
        callmap["from"] = self.default_from_account_signer.get_keypair().address
        callmap["to"] = to_address
        callmap["value"] = 0

        # send transaction to the given group
        params = [self.bcosconfig.groupid, callmap]
        # 发送
        response = self.common_request(cmd, params)
        #print("response : ",response)
        # check status
        if "status" in response.keys():
            status = int(response["status"], 16)
            error_message = transaction_status_code.TransactionStatusCode.get_error_message(status)
            if error_message is not None:
                raise BcosException("call error, error message: {}".format(error_message))
        if "output" in response.keys():
            outputdata = response["output"]
            # 取得方法的abi，签名，参数 和返回类型，进行call返回值的解析
            fn_abi, fn_selector, fn_arguments = get_function_info(
                fn_name, contract_abi, None, args, None,
            )
            # print("fn_selector",fn_selector)
            # print("fn_arguments",fn_arguments)
            #fn_output_types = get_fn_abi_types_single(fn_abi, "outputs")
            fn_output_types = get_abi_output_types(fn_abi);
            try:
                #decoderesult = decode_single(fn_output_types, decode_hex(outputdata))
                #print("fn_output_types",fn_output_types)
                decoderesult = decode_abi(fn_output_types,decode_hex(outputdata))
                return decoderesult
            except BaseException as e:
                return response
        return response

    # https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/api.html#getpendingtransactions
    '''
        可用于所有已知abi的合约，传入abi定义，方法名，正确的参数列表，即可发送交易。交易由BcosClient里加载的账号进行签名。
        如果显式传入from_accont,要注意根据国密( CRYPTO_TYPE_GM)
        或非国密选择类型(CRYPTO_TYPE_ECDSA),构建对应的signer
    '''

    def sendRawTransaction(self, to_address, contract_abi, fn_name, args=None,
                           bin_data=None, gasPrice=30000000,
                           packet_type=ChannelPack.TYPE_RPC,
                           from_account_signer=None):
        cmd = "sendRawTransaction"
        if to_address != "":
            common.check_and_format_address(to_address)
        # 第三个参数是方法的abi，可以传入None，encode_transaction_data做了修改，支持通过方法+参数在整个abi里找到对应的方法abi来编码

        if bin_data is None:
            functiondata = encode_transaction_data(fn_name, contract_abi, None, args)
        # the args is None
        elif args is None:
            functiondata = bin_data
        # deploy with params
        else:
            print(contract_abi)
            fn_data = get_aligned_function_data(contract_abi, None, args)
            functiondata = bin_data + fn_data[2:]

        if to_address is not None and len(to_address) > 0:
            from eth_utils import to_checksum_address
            to_address = to_checksum_address(to_address)

        # 填写一个bcos transaction 的 mapping
        import random
        txmap = dict()
        txmap["randomid"] = random.randint(0, 1000000000)
        txmap["gasPrice"] = gasPrice
        txmap["gasLimit"] = gasPrice
        txmap["blockLimit"] = self.getBlockLimit()

        txmap["to"] = to_address
        txmap["value"] = 0
        txmap["data"] = functiondata
        txmap["fiscoChainId"] = self.fiscoChainId
        txmap["groupId"] = self.groupid
        txmap["extraData"] = ""
        #print("\n>>>>functiondata ",functiondata)
        signtx = SignTx()
        # 关键流程：对交易签名，重构后全部统一到 SignTx 类(client/signtransaction.py)完成
        #  如果本方法未传入 from_account, 则用默认的账户代替，要区分国密或非国密
        # load default account if not set .notice: account only use for
        # sign transaction for sendRawTransa# if self.client_account is None:
        if from_account_signer is None:
            self.load_default_account()
            from_account_signer = self.default_from_account_signer
        # print(signtx.signer)
        signtx.signer = from_account_signer
        signed_result = signtx.sign_transaction(txmap)
        # print("@@@@@rawTransaction : ",encode_hex(signedTxResult.rawTransaction))
        # signedTxResult.rawTransaction是二进制的，要放到rpc接口里要encode下
        params = [self.groupid, encode_hex(signed_result.rawTransaction)]
        result = self.common_request(cmd, params, packet_type)
        return result

    # 发送交易后等待共识完成，检索receipt
    def channel_sendRawTransactionGetReceipt(self, to_address, contract_abi,
                                             fn_name, args=None, bin_data=None, gasPrice=30000000,
                                             from_account_signer=None
                                             ):
        return self.sendRawTransaction(
            to_address,
            contract_abi,
            fn_name,
            args,
            bin_data,
            gasPrice,
            ChannelPack.TYPE_TX_COMMITTED,
            from_account_signer=from_account_signer)

    def rpc_sendRawTransactionGetReceipt(self, to_address, contract_abi,
                                         fn_name, args=None, bin_data=None, gasPrice=30000000,
                                         timeout=15,
                                         from_account_signer=None
                                         ):
        # print("sendRawTransactionGetReceipt",args)
        stat = StatTool.begin()
        txid = self.sendRawTransaction(to_address, contract_abi, fn_name, args, bin_data, gasPrice,
                                       from_account_signer=from_account_signer)
        result = None
        for i in range(0, timeout):
            result = self.getTransactionReceipt(txid)
            # print("getTransactionReceipt : ", result)
            if result is None:
                time.sleep(1)
                self.logger.info(
                    "sendRawTransactionGetReceipt,retrying getTransactionReceipt : {}".format(i))
                continue
            else:
                break  # get the result break
        stat.done()
        memo = "DONE"
        if result is None:
            memo = "ERROR:TIMEOUT"
        stat.debug("sendRawTransactionGetReceipt,{}".format(memo))
        if result is None:
            raise BcosError(-1, None, "sendRawTransactionGetReceipt,{}".format(memo))
        return result

    def sendRawTransactionGetReceipt(self, to_address, contract_abi,
                                     fn_name, args=None, bin_data=None, gasPrice=30000000,
                                     timeout=15,
                                     from_account_signer=None
                                     ):
        if self.channel_handler is not None:
            return self.channel_sendRawTransactionGetReceipt(
                to_address,
                contract_abi,
                fn_name,
                args,
                bin_data,
                gasPrice,
                from_account_signer=from_account_signer)

        return self.rpc_sendRawTransactionGetReceipt(
            to_address,
            contract_abi,
            fn_name,
            args,
            bin_data,
            gasPrice,
            timeout,
            from_account_signer=from_account_signer)

    '''
        newaddr = result['contractAddress']
        blocknum = result['blockNumber']
    '''

    def deploy(self, contract_bin, contract_abi=None, fn_args=None, from_account_signer=None):
        result = self.sendRawTransactionGetReceipt(
            to_address="",
            contract_abi=contract_abi,
            fn_name=None,
            args=fn_args,
            bin_data=contract_bin,
            from_account_signer=from_account_signer
        )
        # newaddr = result['contractAddress']
        # blocknum = result['blockNumber']
        # print("onblock : %d newaddr : %s "%(int(blocknum,16),newaddr))
        return result

    def deployFromFile(
            self,
            contractbinfile,
            contract_abi=None,
            fn_args=None,
            from_account_signer=None):
        with open(contractbinfile, "r") as f:
            contractbin = f.read()
            f.close()
        result = self.deploy(contractbin, contract_abi, fn_args,
                             from_account_signer=from_account_signer)
        return result
