import requests
import logging
from pprint import pprint
from .Base import Base

LOG = logging.getLogger(__name__)

class weidentityService(Base):
    def __init__(self, host, port=None, version="1.0.0"):
        super(weidentityService, self).__init__(host, port, version)

    def create_weidentity_did(self, function_arg={}, transaction_arg={}):
        # 创建WeIdentity DID
        data_dict = {
            "functionArg": function_arg,
            "transactionArg": transaction_arg,
            "functionName": "createWeId",
            "v": self.version
        }
        return self.post("/weid/api/invoke", data=data_dict)

    def get_weidentity_did(self, weId):
        # 获取WeIdentity DID Document
        data_dict = {
            "functionArg": {
                "weId": weId
            },
            "transactionArg": {},
            "functionName": "getWeIdDocument",
            "v": self.version
        }
        return self.post("/weid/api/invoke", data=data_dict)

    def create_authority_issuer(self, weId, orgName, invokerWeId):
        # 创建AuthorityIssuer
        data_dict = {
            "functionArg": {
                "weid": weId,
                "name": orgName
            },
            "transactionArg": {
                "invokerWeId": invokerWeId
            },
            "functionName": "registerAuthorityIssuer",
            "v": self.version
        }
        return self.post("/weid/api/invoke", data=data_dict)

    def get_authority_issuer(self, weId, orgName, invokerWeId):
        # 查询AuthorityIssuer
        data_dict = {
            "functionArg": {
               "weId": weId
            },
            "transactionArg": {
            },
            "functionName": "queryAuthorityIssuer",
            "v": self.version
        }
        return self.post("/weid/api/invoke", data=data_dict)

    def create_cpt(self, weId, invokerWeId, cptJsonSchema):
        # 创建CPT
        data_dict = {
          "functionArg": {
              "weId": weId,
              "cptJsonSchema": cptJsonSchema
          },
          "transactionArg": {
              "invokerWeId": invokerWeId
          },
          "functionName": "registerCpt",
          "v": self.version
        }

        return self.post("/weid/api/invoke", data=data_dict)

    def get_cpt(self, cptId):
        # 查询CPT
        data_dict = {
            "functionArg": {
                "cptId": cptId,
            },
            "transactionArg": {
            },
            "functionName": "queryCpt",
            "v": self.version
        }

        return self.post("/weid/api/invoke", data=data_dict)

    def create_credential(self, cptId, issuerWeId, expirationDate, claim, invokerWeId):
        # 创建Credential
        data_dict = {
            "functionArg": {
                "cptId": cptId,
                "issuer": issuerWeId,
                "expirationDate": expirationDate,
                "claim": claim,
            },
            "transactionArg": {
                "invokerWeId": invokerWeId
            },
            "functionName": "createCredential",
            "v": self.version
        }

        return self.post("/weid/api/invoke", data=data_dict)

    def verify_credential(self, cptId, uuid, issuerWeId, expirationDate, issuranceDate, claim, context, proof):
        # 验证Credential
        data_dict = {
            "functionArg": {
              "@context": context,
              "claim": claim,
              "cptId": cptId,
              "expirationDate": expirationDate,
              "id": uuid,
              "issuanceDate": issuranceDate,
              "issuer": issuerWeId,
              "proof": proof
            },
            "transactionArg": {
            },
            "functionName": "verifyCredential",
            "v": self.version
        }

        return self.post("/weid/api/invoke", data=data_dict)


    def create_credentialpojo(self, cptId, issuerWeId, expirationDate, claim,invokerWeId):
        # 创建CredentialPojo
        data_dict = {
            "functionArg": {
                "cptId": cptId,
                "issuer": issuerWeId,
                "expirationDate": expirationDate,
                "claim": claim,
            },
            "transactionArg": {
                "invokerWeId": invokerWeId
            },
            "functionName": "createCredentialPojo",
            "v": self.version
        }

        return self.post("/weid/api/invoke", data=data_dict)

    def verify_credentialpojo(self, cptId, issuanceDate, context, claim, credential_pojo_id, proof, issuerWeId, expirationDate):
        # 验证CredentialPojo
        data_dict = {
            "functionArg": {
              "cptId": cptId,
              "issuanceDate": issuanceDate,
              "context": context,
              "claim": claim,
              "id": credential_pojo_id,
              "proof": proof,
              "type": [
                  "VerifiableCredential",
                  "hashTree"
              ],
              "issuer": issuerWeId,
              "expirationDate": expirationDate
            },
            "transactionArg": {
            },
            "functionName": "verifyCredentialPojo",
            "v": self.version
        }
        pprint(data_dict)

        return self.post("/weid/api/invoke", data=data_dict)

    def get_registered_endpoint(self):
        # 获取所有已注册的Endpoint信息
        return self.get("/weid/api/endpoint")

    def get_endpoint(self, body):
        # 进行Endpoint调用
        data_dict = {
            "body": body
        }

        return self.post("/weid/api/endpoint", data=data_dict)

    def get_authorize_fetch_data(self, authToken, signedNonce):
        data_dict = {
          "authToken": authToken,
          "signedNonce": signedNonce
        }
        return self.post("/weid/api/authorize/fetch-data", data=data_dict)
