import sys

from client.gm_account import GM_Account
from client.signer_impl import Signer_GM;
from eth_utils import decode_hex,encode_hex
from eth_utils.crypto import set_crypto_type, CRYPTO_TYPE_GM
set_crypto_type("gm")
from gmssl import sm2, func
from gmssl import sm2_helper
def test_gmaccount_spec():
    privkey = "82dcd33c98a23d5d06f9331554e14ab4044a1d71b169b7a38b61c214f0690f80"
    account = GM_Account()
    account.from_key(privkey)
    signer = Signer_GM(account)
    data = b"1234567890"
    (v,r,s) = signer.sign(data)
    #print("hex v:",hex(v))
    print("create account detail: ", account.getdetail())
    print("after sign result is ",(r,s,v))
    sigdata = "49889ee5e24e9fac262e04e82aab58aa0a2d29368e0a105913b20e96d0df6e1d887b8200d1d23052eae96419e21675cc579c759335226a16726f9d480f06b83f";
    print("sigdata len = ",len(decode_hex(sigdata)))
    sm2_crypt = sm2.CryptSM2(
        public_key=account.keypair.public_key, private_key=privkey)
    #sigdata= sm2_crypt.combine_signed_R_S(r,s)
    print("sigdata :",sigdata)
    vres = sm2_crypt.verify(sigdata[0:128],data)
    print("verify result : {}",vres)

    r = int( "49889ee5e24e9fac262e04e82aab58aa0a2d29368e0a105913b20e96d0df6e1d",16)
    s = int( "887b8200d1d23052eae96419e21675cc579c759335226a16726f9d480f06b83f",16)
    sigdata = sm2_crypt.combine_signed_R_S(r, s)
    vres = sm2_crypt.verify(sigdata,data)
    print("verify result 2: {}",vres)






test_gmaccount_spec()
sys.exit(0)
'''
私钥：77afa2e920597092d3374993bcc98e82911b5302337d3e4cc5d457c3dee3f96d
公钥：0d91c4a0712cb8bd826dadc3458458d0ae120d54b4f3d8dc914d3bf18cfc41ec9ee30ca57402f80b6c847cd684d0382d295b4b497e6d11b56d9bb24df7d44e4d
地址：0xb925aa05369c23ab1b46d9f4faca08126536ae85
'''
print("\n\ntest create new account")
account = GM_Account()
account.create()
print("create account detail: ", account.getdetail())


privatekey = "77afa2e920597092d3374993bcc98e82911b5302337d3e4cc5d457c3dee3f96d"
print("\n\ntest from private key and set to  crypto content,key:", privatekey)
filename = "bin/accounts/gm_account1.json"
print("filename :", filename)
account1 = GM_Account()
account1.from_key(privatekey)
print(account1.getdetail())
account1.save_to_file(filename, "123456")
account11 = GM_Account()
account11.load_from_file(filename, "123456")
print("load account detail: ", account11.getdetail())


print("\n\ntest account save to plain file ,from key: ", privatekey)
filename = "bin/accounts/gm_account2.json"
print("filename: ", filename)
account2 = GM_Account()
account2.from_key(privatekey)
account2.save_to_file(filename)
account21 = GM_Account()
account21.load_from_file(filename)
print("load from plain file: ", account21.getdetail())


'''
publicKey ="FA05C51AD1162133DFDF862ECA5E4A481B52FB37FF83E53D45FD18BBD6F32668A92C4692EEB305684E3B9D4ACE767F91D5D108234A9F07936020A92210BA9447"
privatekey = "5EB4DF17021CC719B678D970C620690A11B29C8357D71FA4FF9BF7FB6D89767A"

'''
print("\n\n test other")
privatekey = "5EB4DF17021CC719B678D970C620690A11B29C8357D71FA4FF9BF7FB6D89767A"
account3 = GM_Account()
account3.from_key(privatekey)
print(account3.getdetail())
