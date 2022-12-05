import sys
sys.path.append("./")
from datetime import datetime

from bcos3sdk.bcos3client import Bcos3Client
from client.datatype_parser import DatatypeParser

bcos3 = Bcos3Client()
print(bcos3.getinfo())
num = bcos3.getBlockNumber()
print(f"getBlockNumber {num},{type(num)}")
print(f"getPeers : {bcos3.getPeers()}")
print(f"getSealerList:{bcos3.getSealerList()}")
print(f"getConsensusStatus:{bcos3.getConsensusStatus()}")
print(f"getSyncStatus:{bcos3.getSyncStatus()}")
print(f"getPbftView:{bcos3.getPbftView()}")
print(f"getGroupPeers:{bcos3.getGroupPeers()}")
print(f"getGroupList:{bcos3.getGroupList()}")
print(f"getGroupList:{bcos3.getGroupList()}")
result = bcos3.getBlockByNumber(2)
print(f"getBlockByNumber:{result}")
blockhash = result["hash"]
result = bcos3.getBlockByHash(blockhash)
print(f"getBlockByHash:{result}")
print(f"getBlockHashByNumber:{bcos3.getBlockHashByNumber(2)}")
txhash = result["transactions"][0]["hash"]
result = bcos3.getTransactionByHash(txhash)
print(f"transaction hash : {txhash}")
print(f"getTransactionByHash:{result}")
print(f"getTransactionReceipt:{bcos3.getTransactionReceipt(txhash)}")
print(f"getPendingTxSize:{bcos3.getPendingTxSize()}")
print(f"getBlocklimit:{bcos3.getBlocklimit()}")


contractFile = r"contracts\HelloWorld.abi"
abi_parser = DatatypeParser()
abi_parser.load_abi_file(contractFile)
contract_abi = abi_parser.contract_abi
to = "31ed5233b81c79d5adddeef991f531a9bbc2ad01"
response = bcos3.deployFromFile("contracts/HelloWorld.bin")
print(f"deploy response: {response}")
contract_address = response["contractAddress"]
to = contract_address
print(f"contract address {to}")
response = bcos3.call(to,abi_parser.contract_abi,"get",[])
print(f"call HelloWorld: {response}")

inputparams = [f"abcefg:{datetime.now()}"]
response = bcos3.sendRawTransaction(to,contract_abi,"set",inputparams)
print("sendTransation",response)
logs = abi_parser.parse_event_logs(response["logEntries"])
print("logs",logs)

response = bcos3.call(to,abi_parser.contract_abi,"get",[])
print(f"call HelloWorld: {response}")




bcos3.finish()