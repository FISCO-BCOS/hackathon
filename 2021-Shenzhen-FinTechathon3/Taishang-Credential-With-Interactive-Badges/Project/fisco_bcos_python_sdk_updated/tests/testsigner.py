from client.signer_impl import Signer_ECDSA,Signer_GM

data  =  b'1234567890'

account = Signer_ECDSA.load_from_keyfile("bin/accounts/pyaccount.keystore","123456")
signer =  Signer_ECDSA(account)

res = signer.sign(data)
print(res)
res = signer.sign(data)
print(res)
res = signer.sign(data,2)
print(res)
print("----GM----")
accountgm = Signer_GM.from_key_file("bin/accounts/gm_account.json","123456")
signergm= Signer_GM(accountgm)
res = signergm.sign(data)
print(res)
res = signergm.sign(data)
print(res)