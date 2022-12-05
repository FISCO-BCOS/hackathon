import json
from typing import Iterable, Callable

from bcos3sdk.bcos3client import Bcos3Client
from console_utils.console_common import match_input_params


def friendly_print_result(result):
    if isinstance(result,Iterable) and type(result) is not str:
        result = json.dumps(result, indent=4)
    print(result)

class CmdBcos3RPC:
    bcos3sdk:None
    param_types= dict()

    @staticmethod
    def get_cmds():
        skipname = ["make_usage", "usage","get_cmds"]
        cmdlist = []
        for item in dir(CmdBcos3RPC):
            if item in skipname:
                continue
            if item.startswith("_"):
                continue
            attr = getattr(CmdBcos3RPC,item)
            if not isinstance(attr,Callable):
                continue
            cmdlist.append(item)

        return cmdlist

    @staticmethod
    def make_usage():
        usagemsg = []
        cmdlist=CmdBcos3RPC.get_cmds()
        cmdtext = ""
        n = 0
        for item in cmdlist:
            result = item
            if item in CmdBcos3RPC.param_types:
                paramtype = CmdBcos3RPC.param_types[item]
                #print(paramtype)
                strnames = ""
                for param in paramtype:
                    strnames =f"{strnames} {param[0]} ,"
                result = f"{item}({strnames.strip(',')})"
            cmdtext = f"{cmdtext} {result} | "
            n = n+1
            if n % 4 ==0:
                cmdtext = f"{cmdtext}  \n"

        usagemsg.append("\n----RPC Query Cmds--->\n")
        usagemsg.append(
            cmdtext
        )
        usagemsg.append(f"\nTotal rpc query cmds: {len(cmdlist)}")
        return usagemsg

    def __init__(self):
        try:
            self.bcos3sdk = Bcos3Client()
            print(self.bcos3sdk.getinfo())
        except Exception as e:
            import traceback
            traceback.print_exc()
            print("bcos3 rpc init error",e)
            
            

    @staticmethod
    def usage():
        usagemsg = CmdBcos3RPC.make_usage()
        for m in usagemsg:
            print(m)


    def getBlockNumber(self):
        result = self.bcos3sdk.getBlockNumber()
        friendly_print_result(result)


    def getPbftView(self):
        result = self.bcos3sdk.getPbftView()
        friendly_print_result(result)

    def getSealerList(self):
        result = self.bcos3sdk.getSealerList()
        friendly_print_result(result)

    def getObserverList(self,inputparams):
        result = self.bcos3sdk.getObserverList()
        friendly_print_result(result)

    def getConsensusStatus(self):
        result = self.bcos3sdk.getConsensusStatus()
        friendly_print_result(result)

    def getSyncStatus(self):
        result = self.bcos3sdk.getSyncStatus()
        friendly_print_result(result)

    def getPeers(self):
        result = self.bcos3sdk.getPeers()
        friendly_print_result(result)

    def getGroupPeers(self):
        result = self.bcos3sdk.getGroupPeers()
        friendly_print_result(result)

    def getGroupList(self):
        result = self.bcos3sdk.getGroupList()
        friendly_print_result(result)

    param_types["getBlockByHash"] = [("block_hash", str, None), ("only_header", int, 0), ("only_tx_hash", int, 0)]
    def getBlockByHash(self, inputparams):
        #block_hash, only_header=0,only_tx_hash=0
        (hash, only_header, only_tx_hash) = match_input_params(inputparams, CmdBcos3RPC.param_types["getBlockByHash"])
        result = self.bcos3sdk.getBlockByHash(hash, only_header, only_tx_hash)
        friendly_print_result(result)


    param_types["getBlockByNumber"] =  [("num", int, None), ("only_header", int, 0), ("only_tx_hash", int, 0)]
    def getBlockByNumber(self, inputparams):
        #num, only_header=0,only_tx_hash=0
        (num,only_header,only_tx_hash) = match_input_params(inputparams,CmdBcos3RPC.param_types["getBlockByNumber"])

        result = self.bcos3sdk.getBlockByNumber(num,only_header,only_tx_hash)
        friendly_print_result(result)

    param_types["getBlockHashByNumber"] = [("number", int, None)]
    def getBlockHashByNumber(self, inputparams):
        (num,) = match_input_params(inputparams, CmdBcos3RPC.param_types["getBlockHashByNumber"])
        result = self.bcos3sdk.getBlockHashByNumber(num)
        friendly_print_result(result)

    param_types["getTransactionByHash"] = [("hash", str, None), ("proof", int, 0)]
    def getTransactionByHash(self,inputparams):
        (hash, proof) = match_input_params(inputparams, CmdBcos3RPC.param_types["getTransactionByHash"])
        result = self.bcos3sdk.getTransactionByHash(hash, proof )
        friendly_print_result(result)

    param_types["getTransaction"] = [("hash", str, None), ("proof", int, 0)]
    def getTransaction(self,inputparams):
        self.getTransactionByHash(inputparams)

    param_types["getTransactionReceipt"] = [("hash", str, None), ("proof", int, 0)]
    def getTransactionReceipt(self,inputparams):
        (hash, proof) = match_input_params(inputparams, CmdBcos3RPC.param_types["getTransactionReceipt"])
        result = self.bcos3sdk.getTransactionReceipt(hash,proof)
        friendly_print_result(result)

    def getPendingTxSize(self):
        result = self.bcos3sdk.getPendingTxSize()
        friendly_print_result(result)

    param_types["getCode"] = [("address", str, None)]
    def getCode(self, inputparams):
        (address,) = match_input_params(inputparams, CmdBcos3RPC.param_types["getCode"])

        result = self.bcos3sdk.getCode(address)
        friendly_print_result(result)

    def getTotalTransactionCount(self):
        result = self.bcos3sdk.getTotalTransactionCount()
        friendly_print_result(result)

    param_types["getSystemConfigByKey"] = [("key", str, None)]
    def getSystemConfigByKey(self, inputparams):
        (key,) = match_input_params(inputparams, CmdBcos3RPC.param_types["getSystemConfigByKey"])
        result = self.bcos3sdk.getSystemConfigByKey(key)
        friendly_print_result(result)

    def getBlocklimit(self):
        result = self.bcos3sdk.getBlocklimit()
        friendly_print_result(result)


if False:
    valuetyps = [("number",int,None),("height",int,None),("length",str,"abc"),("values",list,(1,2,3))]
    inputparams=[9,2,"test",7,8,9,0]
    (number,h,l,v) = match_input_params(inputparams,valuetyps)
    print(f"match result {number},{h},{l},{v}")
    rpc = CmdBcos3RPC()
    rpc.getBlockNumber()
    #print(CmdBcos3RPC.param_types)
    #inputparams = [1,1,1]
    #rpc = CmdBcos3RPC()
    #rpc.getBlockByNumber(inputparams)
    #rpc.getBlockNumber()
    CmdBcos3RPC.usage()


