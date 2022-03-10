import logging
from .Base import Base
import time

LOG = logging.getLogger(__name__)

class weidentityClient(Base):
    def __init__(self, host, port=None, version="1.0.0"):
        super(weidentityClient, self).__init__(host, port, version)

    def create_weidentity_did_first(self, publicKey, nonce):
        # 创建WeIdentity DID
        data_dict = {
            "functionArg": {
                "publicKey": publicKey
            },
            "transactionArg": {
                "nonce": nonce
            },
            "functionName": "createWeId",
            "v": self.version
        }
        return self.post("/weid/api/encode", data=data_dict)

    def create_weidentity_did_second(self, nonce, data, signedMessage, blockLimit, signType="1"):
        # 创建WeIdentity DID
        data_dict = {
            "functionArg": {},
            "transactionArg": {
                "nonce": nonce,
                "data": data,
                "blockLimit": blockLimit,
                "signType": signType,
                "signedMessage": signedMessage
            },
            "functionName": "createWeId",
            "v": self.version
        }
        return self.post("/weid/api/transact", data=data_dict)

    def create_weidentity_did(self, privKey, nonce, signType="1"):
        publicKey = self.priv_to_public(privKey)
        respBody = self.create_weidentity_did_first(publicKey, nonce)
        encode_transaction = respBody['respBody']['encodedTransaction']

        blockLimit = respBody['respBody']['blockLimit']

        raw_result = self.weid_ecdsa_sign(privKey, encode_transaction)

        weid_second = self.create_weidentity_did_second(nonce, data=respBody['respBody']['data'], blockLimit=blockLimit,
                                                        signType=signType, signedMessage=raw_result)

        return weid_second

    def register_authority_issuer_first(self, name, weId, nonce):
        # 注册Authority Issuer
        data_dict = {
            "functionArg": {
                "name": name,
                "weId": weId
            },
            "transactionArg": {
                "nonce": nonce
            },
            "functionName": "registerAuthorityIssuer",
            "v": self.version
        }
        return self.post("/weid/api/encode", data=data_dict)

    def register_authority_issuer_second(self, nonce, data, signedMessage, blockLimit, signType="1"):
        # 注册Authority Issuer
        data_dict = {
            "functionArg": {},
            "transactionArg": {
                "nonce": nonce,
                "data": data,
                "blockLimit": blockLimit,
                "signType": signType,
                "signedMessage": signedMessage
            },
            "functionName": "registerAuthorityIssuer",
            "v": self.version
        }
        return self.post("/weid/api/transact", data=data_dict)

    def register_authority_issuer(self, privKey, name, weId, nonce, signType="1"):

        respBody = self.register_authority_issuer_first(name, weId, nonce)

        encode_transaction = respBody['respBody']['encodedTransaction']

        blockLimit = respBody['respBody']['blockLimit']

        raw_result = self.weid_ecdsa_sign(privKey, encode_transaction)

        authority_issuer_second = self.register_authority_issuer_second(nonce, data=respBody['respBody']['data'], blockLimit=blockLimit,
                                                        signType=signType, signedMessage=raw_result)

        return authority_issuer_second


    def create_cpt_first(self, weId, cptJsonSchema, cptSignature, nonce):
        # 创建CPT
        data_dict = {
            "functionArg": {
                "weId": weId,
                "cptJsonSchema": cptJsonSchema,
                "cptSignature": cptSignature
            },
            "transactionArg": {
                "nonce": nonce
            },
            "functionName": "registerCpt",
            "v": self.version
        }
        return self.post("/weid/api/encode", data=data_dict)

    def create_cpt_second(self, nonce, data, signedMessage, blockLimit, signType="1"):
        # 创建CPT
        data_dict = {
            "functionArg": {},
            "transactionArg": {
                "nonce": nonce,
                "data": data,
                "blockLimit": blockLimit,
                "signType": signType,
                "signedMessage": signedMessage
            },
            "functionName": "registerCpt",
            "v": self.version
        }
        return self.post("/weid/api/transact", data=data_dict)

    def create_cpt(self, privKey, weId, cptJsonSchema, cptSignature, nonce, signType="1"):

        respBody = self.create_cpt_first(weId, cptJsonSchema, cptSignature, nonce)

        print(respBody)

        encode_transaction = respBody['respBody']['encodedTransaction']


        blockLimit = respBody['respBody']['blockLimit']

        raw_result = self.weid_ecdsa_sign(privKey, encode_transaction)

        cpt_second = self.create_cpt_second(nonce, data=respBody['respBody']['data'], blockLimit=blockLimit,
                                                        signType=signType, signedMessage=raw_result)

        return cpt_second


    def create_credential_pojo(self, cptId, issuer_weid, expirationDate, claim):
        '''
        dfshfsdflaksj
        '''
        # 创建CredentialPojo
        if isinstance(expirationDate, int):
            expirationDate = time.strftime('%Y-%m-%dT%H:%M:%SZ', time.localtime(expirationDate))
        data_dict = {
            "functionArg": {
                "cptId": cptId,
                "issuer": issuer_weid,
                "expirationDate": expirationDate,
                "claim": claim,
            },
            "transactionArg": {
            },
            "functionName": "createCredentialPojo",
            "v": self.version
        }
        return self.post("/weid/api/encode", data=data_dict)