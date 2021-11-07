from pyweidentity.weidentityClient import weidentityClient
from pyweidentity import localweid
import random,binascii

URL = "http://124.251.110.210:6001"
weid = weidentityClient(URL)
privKey = "c4a116fb87ae9b8b87842b0f46e1bbf71c37fdae1104fd6d3fd2041e23c8c68e"
nonce = str(random.randint(1, 999999999999999999999999999999))
# create_weid = weid.create_weidentity_did(privKey, nonce)
_weid = "did:weid:3:0xb9275bbe19d79aff758621bed1df0c332da87552"


cptJsonSchema = {
          "title": "信用等级证书",
          "description": "信用等级证书",
          "properties": {
              "con_addr": {
                  "type": "string",
                  "description": "Contract address"
              },
              "token_id": {
                  "type": "string",
                  "description": "tokenID"
              },
          },
          "required": [
              "con_addr",
              "token_id"
          ]
      }
# privKey, weId, cptJsonSchema, cptSignature, nonce, signType="1")

cptSignatureHex = localweid.ecdsa_sign(str(cptJsonSchema), privKey)
cptSignatureStr = binascii.b2a_hex(cptSignatureHex)
cptSignature = localweid.base64_encode(cptSignatureStr)
print(cptSignature)

create_cpt_data = weid.create_cpt(_weid,"0x"+privKey,cptJsonSchema,cptSignature, nonce)

print(create_cpt_data)
