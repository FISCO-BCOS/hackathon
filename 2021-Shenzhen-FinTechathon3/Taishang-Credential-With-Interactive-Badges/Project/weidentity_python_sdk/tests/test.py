from pyweidentity.weidentityService import weidentityService

URL = "http://124.251.110.210:6001"
# WeIdentity RestService URL

weid = weidentityService(URL)
# create_weid = weid.create_weidentity_did()
# print(create_weid)
_weid = "did:weid:3:0xbce3653371dd7d77aebd17a6832dca8eb8c8a212"



# 创建cpt
# cptJsonSchema = {
#           "title": "信用等级证书",
#           "description": "信用等级证书",
#           "properties": {
#               "con_addr": {
#                   "type": "string",
#                   "description": "Contract address"
#               },
#               "token_id": {
#                   "type": "string",
#                   "description": "tokenID"
#               },
#           },
#           "required": [
#               "con_addr",
#               "token_id"
#           ]
#       }

# create_cpt_data = weid.create_cpt(_weid,_weid,cptJsonSchema)


# 创建credential
# claim = {
#             "con_addr": "test",
#             "token_id": "test",
#         }

# create_credential_data = weid.create_credentialpojo(2000003,_weid,"2022-04-18T21:12:33Z",claim,_weid)
# print(create_credential_data)




# ——————————————————————————————————验证credential
# {
#     "functionArg": {
#       "@context": "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1",
#       "claim": {
#           "content": "b1016358-cf72-42be-9f4b-a18fca610fca",
#           "receiver": "did:weid:101:0x7ed16eca3b0737227bc986dd0f2851f644cf4754",
#           "weid": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa"
#       },
#       "cptId": 2000156,
#       "expirationDate": "2100-04-18T21:12:33Z",
#       "id": "da6fbdbb-b5fa-4fbe-8b0c-8659da2d181b",
#       "issuanceDate": "2020-02-06T22:24:00Z",
#       "issuer": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa",
#       "proof": {
#           "created": "1580999040000",
#           "creator": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa",
#           "signature": "G0XzzLY+MqUAo3xXkS3lxVsgFLnTtvdXM24p+G5hSNNMSIa5vAXYXXKl+Y79CO2ho5DIGPPvSs2hvAixmfIJGbw=",
#           "type": "Secp256k1"
#       }
#     },
#     "transactionArg": {
#     },
#     "functionName": "verifyCredential",
#     "v": "1.0.0"
# }
{'respBody': {'cptId': 2000003, 'issuanceDate': 1636165120, 'context': 'https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1', 'claim': {'con_addr': 'test', 'token_id': 'test'}, 'id': 'c5cb3828-5d94-4e5b-afdd-f965125fd3e8', 'proof': {'created': 1636165120, 'creator': 'did:weid:3:0xbce3653371dd7d77aebd17a6832dca8eb8c8a212#keys-0', 'salt': {'con_addr': 'kWtjQ', 'token_id': 'grtBq'}, 'signatureValue': '8+zTMGEYOjqgiwBjF/lULyuhYDpJSwHZKdOdc6xtN+lJWoboKRow8/DK9AQAMCsI0mqxZno1j4w09DM2mNuWUgA=', 'type': 'Secp256k1'}, 'type': ['VerifiableCredential', 'original'], 'issuer': 'did:weid:3:0xbce3653371dd7d77aebd17a6832dca8eb8c8a212', 'expirationDate': 1650287553}, 'loopback': None, 'errorCode': 0, 'errorMessage': 'success'}

cptId = 2000003

issuanceDate = 1636165120

context = "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1"

claim = {'con_addr': 'test', 'token_id': 'test'}

id = 'c5cb3828-5d94-4e5b-afdd-f965125fd3e8'

proof =  {'created': 1636165120, 'creator': 'did:weid:3:0xbce3653371dd7d77aebd17a6832dca8eb8c8a212#keys-0', 'salt': {'con_addr': 'kWtjQ', 'token_id': 'grtBq'}, 'signatureValue': '8+zTMGEYOjqgiwBjF/lULyuhYDpJSwHZKdOdc6xtN+lJWoboKRow8/DK9AQAMCsI0mqxZno1j4w09DM2mNuWUgA=', 'type': 'Secp256k1'}

issuer = 'did:weid:3:0xbce3653371dd7d77aebd17a6832dca8eb8c8a212'

expirationDate = 1650287553

# self, cptId, issuanceDate, context, claim, credential_pojo_id, proof, issuerWeId, expirationDate

verify_credential_data = weid.verify_credentialpojo(cptId,issuanceDate,context,claim,id,proof,issuer,expirationDate)
print(verify_credential_data)

# {'functionArg': 
#     {'claim': {'con_addr': 'test', 'token_id': 'test'},
#                  'context': 'https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1',
#                  'cptId': 2000003,
#                  'expirationDate': 1650287553,
#                  'id': 'c5cb3828-5d94-4e5b-afdd-f965125fd3e8',
#                  'issuanceDate': 1636165120,
#                  'issuer': 'did:weid:3:0xbce3653371dd7d77aebd17a6832dca8eb8c8a212',
#                  'proof': {'created': 1636165120,
#                            'creator': 'did:weid:3:0xbce3653371dd7d77aebd17a6832dca8eb8c8a212#keys-0',
#                            'salt': {'con_addr': 'kWtjQ', 'token_id': 'grtBq'},
#                            'signatureValue': '8+zTMGEYOjqgiwBjF/lULyuhYDpJSwHZKdOdc6xtN+lJWoboKRow8/DK9AQAMCsI0mqxZno1j4w09DM2mNuWUgA=',
#                            'type': 'Secp256k1'},
#                  'type': ['VerifiableCredential', 'hashTree']},
#  'functionName': 'verifyCredentialPojo',
#  'transactionArg': {},
#  'v': '1.0.0'}