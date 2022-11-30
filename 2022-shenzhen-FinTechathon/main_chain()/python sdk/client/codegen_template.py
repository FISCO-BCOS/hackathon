# template for codegen
from client.bcosclient import (
    BcosClient
)
from client.datatype_parser import DatatypeParser
import json


class TEMPLATE_CLASSNAME:  # name of abi
    address = None
    contract_abi_string = '''TEMPLATE_CONTRACT_ABI'''
    contract_abi = None
    data_parser = DatatypeParser()
    client = None

    def __init__(self, address):
        self.client = BcosClient()
        self.address = address
        self.contract_abi = json.loads(self.contract_abi_string)
        self.data_parser.set_abi(self.contract_abi)

    def deploy(self, contract_bin_file):
        result = self.client.deployFromFile(contract_bin_file)
        self.address = result["contractAddress"]
        return result
