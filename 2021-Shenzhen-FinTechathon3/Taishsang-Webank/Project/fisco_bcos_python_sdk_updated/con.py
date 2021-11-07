from client.bcosclient import BcosClient
from client.datatype_parser import DatatypeParser
from web3 import Web3

class Car_Contract:
    def __init__(self, address: str):
        abi_file = "contracts/Con.abi"
        data_parser = DatatypeParser()
        data_parser.load_abi_file(abi_file)
        self.contract_abi = data_parser.contract_abi

        self.client = BcosClient()
        self.to_address = address

    def balanceOf(self, addr:str):
        addr = Web3.toChecksumAddress(addr)
        new_carowner = self.client.call(self.to_address, self.contract_abi, "balanceOf", [addr])
        return new_carowner

    def tokenURI(self, tokenid: int):
        # amount = Web3.toChecksumAddress(amount)
        new_user = self.client.call(self.to_address, self.contract_abi, "tokenURI",[tokenid])
        return new_user

    def transferTo(self, addr:str,tokenid: int):
        addr = Web3.toChecksumAddress(addr)
        car_message = self.client.sendRawTransactionGetReceipt(self.to_address, self.contract_abi, "transferTo", [addr,tokenid])
        return car_message

    def tokenOfOwnerByIndex(self,addr:str,index:int):
        addr = Web3.toChecksumAddress(addr)
        car_list = self.client.call(self.to_address, self.contract_abi, "tokenOfOwnerByIndex", [addr,index])
        return car_list

    def ownerOf(self, tokenid: int):
        # amount = Web3.toChecksumAddress(amount)
        is_carowner = self.client.call(self.to_address, self.contract_abi, "ownerOf",
                                                                [tokenid])
        return is_carowner

    def is_user(self, amount: str):
        amount = Web3.toChecksumAddress(amount)
        is_user = self.client.call(self.to_address, self.contract_abi, "isUser", [amount])
        return is_user

    def new_vehicle(self, chainNumber: int, number: str, brand: str, color: str, quality: str, price: int, day: int):
        new_vehicle = self.client.sendRawTransactionGetReceipt(self.to_address, self.contract_abi, "newVehicle", [chainNumber, number, brand, color, quality, price, day])
        return new_vehicle

    def reback_vehicle(self, chainNumber: int):
        reback_vehicle = self.client.sendRawTransactionGetReceipt(self.to_address, self.contract_abi, "rebackVehicle", [chainNumber])
        return reback_vehicle

    def sign_vehicle(self, chainNumber: int):
        sign_vehicle = self.client.sendRawTransactionGetReceipt(self.to_address, self.contract_abi, "signVehicle", [chainNumber])
        return sign_vehicle


if __name__ == "__main__":
    test = Car_Contract("0xa7844eb44fd581ff9b9ffc678037fd506f111fb7")
    # a = test.transferTo("")
    print(a)