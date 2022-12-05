from client.signer_impl import Signer_ECDSA
from eth_account.account import Account
from eth_utils.crypto import keccak
from eth_utils import decode_hex,encode_hex
data = b"WeDPR TEST"
hashres = keccak(data)
print(encode_hex(hashres))
privkey = decode_hex("82dcd33c98a23d5d06f9331554e14ab4044a1d71b169b7a38b61c214f0690f80")
ac = Account().from_key(privkey)
print("privkey:",privkey)
print("public key:",ac.publickey)
print("address:",ac.address)

signer = Signer_ECDSA(ac)
sign_res =signer.sign(hashres)
print("sign:",sign_res)