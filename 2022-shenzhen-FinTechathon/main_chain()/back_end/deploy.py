from client.contractnote import ContractNote
from client.bcosclient import BcosClient
import os
from eth_utils import to_checksum_address
from client.datatype_parser import DatatypeParser
from client.common.compiler import Compiler
from client.bcoserror import BcosException, BcosError
from client_config import client_config
from client_config_data import client_config_data
from client_config_qy import client_config_qy
from client_config_gov import client_config_gov
from client_config_controller import client_config_controller

import pymysql
import traceback
import sys


#client是默认账户用来部署合约
def deploy(client,text,abi_file,args_init=None):#text是bin文件
    # 部署合约
    print("\n>>Deploy:----------------------------------------------------------")
    with open(text, 'r') as load_f:
        contract_bin = load_f.read()
        load_f.close()
    # 部署合约返回的结果
    result = client.deploy(contract_bin=contract_bin,fn_args=args_init)
    print("deploy", result)
    # 合约地址
    print("new address : ", result["contractAddress"])
    # 合约名称
    contract_name = os.path.splitext(os.path.basename(abi_file))[0]
    memo = "tx:" + result["transactionHash"]
    # 把部署结果存入文件备查
    ContractNote.save_address_to_contract_note(contract_name,
                                               result["contractAddress"])
    return result

#一个合约对象 通过实例化部署不同合约 并执行不同的方法
class Contract:
    def __init__(self,text_bin,text_abi_file,tex_sol,args_init):
        self.client = BcosClient(client_config_controller)
        print(self.client.getinfo())
        self.demo_config = client_config
        if os.path.isfile(self.demo_config.solc_path) or os.path.isfile(self.demo_config.solcjs_path):
            #Compiler.compile_file("contracts/HelloWorld.sol")
            Compiler.compile_file(tex_sol)

        self.abi_file = "contracts/SimpleInfo.abi"#text_abi_file
        self.data_parser = DatatypeParser()
        self.data_parser.load_abi_file(self.abi_file)
        self.contract_abi = self.data_parser.contract_abi
        #部署合约
        self.result = deploy(client=self.client,text=text_bin,abi_file=text_abi_file,fn_args=args_init)
        self.to_address = self.result['contractAddress']  # use new deploy address

        #拿到权限合约地址
    def getaddr(self):
        #得到部署的合约地址
        return self.to_address
    def sendRawTransaction(self,fun_name,client_use,args=None):
        try:
            # 发送交易，调用一个改写数据的接口
            print("\n>>sendRawTransaction:----------------------------------------------------")
            #合约地址
            #args = ['simplename', 2024, to_checksum_address('0x7029c502b4F824d19Bd7921E9cb74Ef92392FB1c')]
            #发送交易并获取交易执行的结果
            receipt =client_use.sendRawTransactionGetReceipt(self.to_address, self.contract_abi, fun_name, args)
            print("receipt:", receipt)
            # 解析receipt里的log
            print("\n>>parse receipt and transaction:--------------------------------------")
            txhash = receipt['transactionHash']
            print("transaction hash: ", txhash)
            logresult = self.data_parser.parse_event_logs(receipt["logs"])
            i = 0
            for log in logresult:
                if 'eventname' in log:
                    i = i + 1
                    print("{}): log name: {} , data: {}".format(i, log['eventname'], log['eventdata']))

            #获取对应的交易数据，解析出调用方法名和参数
            txresponse = client_use.getTransactionByHash(txhash)
            inputresult =self.data_parser.parse_transaction_input(txresponse['input'])
            print("transaction inself.put parse:", txhash)
            print(inputresult)

            # 解析该交易在receipt里输出的output,即交易调用的方法的return值
            outputresult = self.data_parser.parse_receipt_output(inputresult['name'], receipt['output'])
            print("receipt output :", outputresult)
            traceback.print_exc()

        except BcosError as e:
            print("execute demo_transaction failed ,BcosError for: {}".format(e))
            traceback.print_exc()

        except Exception as e:
            client_use.finish()
            traceback.print_exc()
            client_use.finish()
            sys.exit(0)
        return (str(txhash),
                outputresult)
    def call(self,fun_name,client_use,args=None):
        try:
            # 调用一下call，获取数据
            print("\n>>Call:------------------------------------------------------------------------")
            res = client_use.call(self.to_address, self.contract_abi, fun_name,args)
            print("call getbalance result:", res)

            # res = client_use.call(self.to_address, self.contract_abi, "getbalance1", [100])
            #
            # print("call getbalance1 result:", res)
            #
            # res = client_use.call(self.to_address, self.contract_abi, "getname")
            #
            # print("call getname:", res)
            #
            # res = client_use.call(self.to_address, self.contract_abi, "getall")
            #
            # print("call getall result:", res)
            #
            # print("done,demo_tx,total req {}".format(self.client.request_counter))

        except BcosException as e:

            print("execute demo_transaction failed ,BcosException for: {}".format(e))

            traceback.print_exc()

        except BcosError as e:

            print("execute demo_transaction failed ,BcosError for: {}".format(e))

            traceback.print_exc()

        except Exception as e:

            client_use.finish()

            traceback.print_exc()

            client_use.finish()

            sys.exit(0)
        return res

#部署合约四个
#权限合约
Contract_control = Contract("contracts/Permission.bin","contracts/Permission.abi","contracts/Permission.sol")
addr_control=Contract_control.getaddr()
#政府
Contract_Gov = Contract("contracts/Goverment.bin","contracts/Goverment.abi","contracts/Goverment.sol",args_init=[to_checksum_address(addr_control)])
#企业
Contract_institution = Contract("contracts/Enterprise.bin","contracts/Enterprise.abi","contracts/Enterprise.sol",args_init=[to_checksum_address(addr_control)])
#审计机构
Contract_data = Contract("contracts/Audit.bin","contracts/Audit.abi","contracts/Audit.sol",args_init=[to_checksum_address(addr_control)])

#调用者账户 改写config_client文件
client_use_controller=BcosClient(client_config_controller)
client_use_gov=BcosClient(client_config_gov)
client_use_qy=BcosClient(client_config_qy)
client_use_data=BcosClient(client_config_data)