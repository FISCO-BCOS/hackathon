from pyweidentity.weidentityService import weidentityService
from pyweidentity.weidentityClient import weidentityClient
import random

URL = "http://124.251.110.210:6001"

# weidClient = weidentityClient(URL)

# privKey = "0x223f0c657846634dc7c04fbbdfd7763fc4cd370fff94eedcd0a6d6f6f54c622b"
# nonce = str(random.randint(1, 999999999999999999999999999999))
# create_weid = weidClient.create_weidentity_did(privKey, nonce)
# print(create_weid)


# WeIdentity RestService URL
weid = weidentityService(URL)

_weid = "did:weid:3:0xbf29d133d0b45b2db1f89394813e5a384088ce40"
issuer = "did:weid:3:0x520b68c1a9f74cbe0b0fac372b779ded458b739f"
claim = {
    "weid": "did:weid:3:0xbf29d133d0b45b2db1f89394813e5a384088ce40",
    "addr": "0xbf29d133d0b45b2db1f89394813e5a384088ce40",
    "date": "2021-06-05",
    "lesson_name": "FISCO BCOS 金融联盟",
    "credit_level": 3,
    "name": "蔡聰明",
}

    # def create_credentialpojo(self, cptId, issuerWeId, expirationDate, claim,):

credential = weid.create_credentialpojo(333333, issuer, "2022-04-18T21:12:33Z", claim, issuer)


print(credential)

# cptId = 333333
# issuanceDate = 1636196972

# context = "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1"
# claim = {"addr": "0xbf29d133d0b45b2db1f89394813e5a384088ce40", "credit_level": 3, "date": "2021-06-05", "lesson_name": "FISCO BCOS 金融联盟", "name": "蔡聰明", "weid": "did:weid:3:0xbf29d133d0b45b2db1f89394813e5a384088ce40"}
# id = "bc60d10a-372e-4605-808c-5f8a7ae6aa62"
# proof = {
#     "created": 1636196972, 
#     "creator": "did:weid:3:0xbf29d133d0b45b2db1f89394813e5a384088ce40#keys-0", 
#     "salt": {"addr": "l04FZ", "credit_level": "qL420", "date": "ULksb", "lesson_name": "5z0Q2", "name": "Icp1s", "weid": "O7JpV"}, 
#     "signatureValue": "YFb8CMIRFZnspywKhN9sag8bG/2vjYjgVpw/i5h9+woYI91UPFQkbPSQ2ZssVqBRkv32yh8ByYHYydRqBLxYrwA=", 
#     "type": "Secp256k1"
#     }


# _type = ["VerifiableCredential", "original"]


# issuer = "did:weid:3:0xbf29d133d0b45b2db1f89394813e5a384088ce40"

# expirationDate = 1650287553
# data = {
#     "cptId": cptId,
#     "issuanceDate": issuanceDate,
#     "context": context,
#     "claim": claim,
#     "id": id,
#     "proof": proof,
#     "type": _type,
#     "issuer": issuer,
#     "expirationDate": expirationDate
# }

# from pprint import pprint
# pprint(data)
# # # self, cptId, issuanceDate, context, claim, credential_pojo_id, proof, issuerWeId, expirationDate

# verify_credential_data = weid.verify_credentialpojo(cptId,issuanceDate,context,claim,id,proof,_type,issuer,expirationDate)
# print(verify_credential_data)



# # {"claim": {"addr": "0xbf29d133d0b45b2db1f89394813e5a384088ce40",
# #            "credit_level": 3,
# #            "date": "2021-06-05",
# #            "lesson_name": "FISCO BCOS 金融联盟",
# #            "name": "蔡聰明",
# #            "weid": "did:weid:3:0xbf29d133d0b45b2db1f89394813e5a384088ce40"},
# #  "context": "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1",
# #  "cptId": 333333,
# #  "expirationDate": 1650287553,
# #  "id": "bc60d10a-372e-4605-808c-5f8a7ae6aa62",
# #  "issuanceDate": 1636196972,
# #  "issuer": "did:weid:3:0xbf29d133d0b45b2db1f89394813e5a384088ce40",
# #  "proof": {"created": 1636196972,
# #            "creator": "did:weid:3:0xbf29d133d0b45b2db1f89394813e5a384088ce40#keys-0",
# #            "salt": {"addr": "l04FZ",
# #                     "credit_level": "qL420",
# #                     "date": "ULksb",
# #                     "lesson_name": "5z0Q2",
# #                     "name": "Icp1s",
# #                     "weid": "O7JpV"},
# #            "signatureValue": "YFb8CMIRFZnspywKhN9sag8bG/2vjYjgVpw/i5h9+woYI91UPFQkbPSQ2ZssVqBRkv32yh8ByYHYydRqBLxYrwA=",
# #            "type": "Secp256k1"},
# #  "type": ["VerifiableCredential", "original"]}