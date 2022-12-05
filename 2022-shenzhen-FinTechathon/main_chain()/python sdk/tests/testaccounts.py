'''
  @author: kentzhang
  @date: 2019-06
'''
import codecs
from eth_keys.backends.native.ecdsa import ecdsa_raw_sign, ecdsa_raw_verify, ecdsa_raw_recover, \
    private_key_to_public_key
from eth_utils.crypto import keccak
import json

from ecdsa import SigningKey
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.serialization import pkcs12

from eth_account.account import (
    Account
)
from eth_utils.hexadecimal import decode_hex, encode_hex
from OpenSSL import crypto


def test_keystore():
    ac1 = Account.create('123456')
    print(ac1.address)
    print(encode_hex(ac1.key))
    print(ac1.publickey)
    print()

    kf = Account.encrypt(ac1.privateKey, "123456")
    print(kf)
    keyfile = "d:/blockchain/accounts/pyaccount.keystore"
    with open(keyfile, "w") as dump_f:
        json.dump(kf, dump_f)

    with open(keyfile, "r") as dump_f:
        keytext = json.load(dump_f)
        privkey = Account.decrypt(keytext, "123456")
        ac2 = Account.from_key(privkey)
        print("read from file:", ac2.address)
        print(encode_hex(ac2.key))
        print(ac2.publickey)


def test_pem():
    privkeyfile = "bin/0x2de5c210370daef452eb610af76c3a293ae1661f.pem.save"
    with open(privkeyfile) as f:
        p = f.read()
        print("pem file:", p)
        key = SigningKey.from_pem(p)
        print("privkey : ", encode_hex(key.to_string()))

        ac2 = Account.from_key(encode_hex(key.to_string()))
        print("pubkey: ", ac2.publickey)
        print("address: ", ac2.address)
        toPem = SigningKey.to_pem(key)
        print("pem from key", toPem)
        # with open(privkeyfile+".save","w") as fw:
        #    fw.write(str(toPem,encoding = "utf8") )
        #    fw.close()
        # f.close()


def test_p12():

    p12filename = "bin/0xea5d262806c5771ae57e8fe4051c91d62b1d67bf.p12"
    with open(p12filename, "rb") as f:

        p12buff = f.read()
        pwd = b"123456"
        (key, cert, additional_certificates) = pkcs12.load_key_and_certificates(
            bytes(p12buff), password=pwd, backend=default_backend())
        print("p12 privkey :", key)
        print("p12 privkey size:", key.key_size)
        print("p12 public  bytes:", key.public_key)

        # 用crypto加载p12文件，会有warning "PKCS#12 support in pyOpenSSL is deprecated.
        # You should use the APIs in cryptography."
        crypto_p12 = crypto.load_pkcs12(p12buff, pwd)
        print("crypto_p12: ", crypto_p12)
        print("crypto_p12 privatekey  : ", crypto_p12.get_privatekey())

        # 用cryto可以导出私钥到pem，但目前不能导出到p12
        privatekey = crypto.dump_privatekey(crypto.FILETYPE_PEM, crypto_p12.get_privatekey())
        print("private pem :", privatekey)
        key = SigningKey.from_pem(privatekey)
        print("privkey : ", encode_hex(key.to_string()))
        ac2 = Account.from_key(encode_hex(key.to_string()))
        print("pubkey: ", ac2.publickey)
        print("address: ", ac2.address)
        f.close()


def test_sign():
    print("sign test")
    # ac = Account.create("123456")
    # print("account priv key: ", ac.privateKey)
    privkey = "52413c714e418cc6fb06afb1bc3f6c54449c89a82a17c9a117a5aa0bad56a9cd"
    binprivkey = codecs.decode(privkey, "hex")
    pubkey = private_key_to_public_key(binprivkey)

    print("binary priv key: ", decode_hex(privkey))
    print("binary pub key: ", pubkey)
    acforverify = Account.from_key(binprivkey)
    msg = b"this is a test"
    msghash = keccak(msg)
    sig = acforverify._key_obj.sign_msg_hash(msghash)
    print("pulic key :", acforverify.publickey)
    vresult = sig.verify_msg_hash(msghash, acforverify.publickey)
    print("verify result ", vresult)
    (v, r, s) = ecdsa_raw_sign(msghash, decode_hex(privkey))

    vres = ecdsa_raw_verify(msghash, (r, s), pubkey)
    print("ecdsa raw verify: ", vres)

    recoverres = ecdsa_raw_recover(msghash, (v, r, s))
    print("raw recover result", recoverres)
    print("hex recover result", encode_hex(recoverres))


#test_p12()
# test_pem()
#test_sign()
key = "7a94d9793bcc38f533c6e15d8ef9c557e8ead2d3f86e9ac1178ce56b2815f86b"
#key = "6eb7d91a86ee378defd30ef8abb9b72499845c05846a2d408583f0e1a4903f5d"
ac1 = Account.from_key(key)
print(ac1.address)
print(encode_hex(ac1.key))
print(ac1.publickey)