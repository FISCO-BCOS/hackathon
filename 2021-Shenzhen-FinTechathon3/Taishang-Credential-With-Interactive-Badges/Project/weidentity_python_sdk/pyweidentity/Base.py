import requests
import logging
import hashlib
import base64
import sha3
from ecdsa import SigningKey, SECP256k1
from pyweidentity.localweid import generate_addr
from Crypto.Hash import keccak
from eth_account import Account

LOG = logging.getLogger(__name__)
class Base(object):
    def __init__(self, host, port, version):

        self.HOST = host
        self.PORT = port
        self.version = version
        if "://" in host:
            if host[-1] == "/":
                host = host[:-1]
            entity = host.split("/")[2].split(":")
            host = entity[0]
            self.HOST = host
            port = int(entity[1])
            self.PORT = port
        if not isinstance(port, int):
            raise TypeError("port must be an instance of int")
        self.BASEURL = "http://{host}:{port}".format(host=host, port=port)

    def get(self, url, params=""):

        response = requests.get("{BASEURL}{url}".format(BASEURL=self.BASEURL, url=url), params=params)
        if response.status_code >= 400:
            LOG.warning('create charging_rule error: %s:%s', response.status_code, response.text)
            return None
        return response.json()

    def post(self, url, data):

        response = requests.post("{BASEURL}{url}".format(BASEURL=self.BASEURL, url=url), json=data)
        if response.status_code >= 400:
            LOG.warning('create charging_rule error: %s:%s', response.status_code, response.text)
            return None
        return response.json()

    # def priv_to_public_hex(self, privkey):
    #     if privkey[:2] == "0x":
    #         account = generate_addr(priv=privkey[2:])
    #     else:
    #         account = generate_addr(priv=hex(int(privkey))[2:])
    #
    #     publickey = account["payload"]["pubv"]
    #     return publickey

    def priv_to_public(self, privkey):
        if privkey[:2] == "0x":
            account = generate_addr(priv=privkey[2:])
        else:
            account = generate_addr(priv=hex(int(privkey))[2:])

        publickey = str(int(account["payload"]["pubv"], 16))
        return publickey


    def weid_ecdsa_sign(self, privKey, encode_transaction):
        # 轻客户端模式的二次签名 signType is 1.
        account = Account.privateKeyToAccount(privKey)
        raw_tx_bytes = base64.b64decode(encode_transaction)

        msg = keccak.new(digest_bits=256)
        msg.update(raw_tx_bytes)

        sig = account.signHash(msg.digest())
        if (len(hex(sig.r)[2:]) % 2) == 0:
            hexed_r = hex(sig.r)[2:]
        else:
            hexed_r = "0" + hex(sig.r)[2:]
        if (len(hex(sig.s)[2:]) % 2) == 0:
            hexed_s = hex(sig.s)[2:]
        else:
            hexed_s = "0" + hex(sig.s)[2:]
        b_sig = bytes(bytearray.fromhex(hex(sig.v)[2:] + hexed_r + hexed_s))
        b_64_sig = base64.b64encode(b_sig).decode()
        # print("sig: {sig}".format(sig=b_64_sig))
        return b_64_sig

    def ecdsa_sign(self, encode_transaction, privkey, hashfunc=hashlib.sha256):
        # 生成证书时需要用到的ecdsa签名

        signning_key = SigningKey.from_string(bytes.fromhex(privkey), curve=SECP256k1)
        # encode_transaction = respBody['respBody']['encodedTransaction']
        # base64解密
        transaction = self.base64_decode(encode_transaction)
        # 获取hash
        hashedMsg = self.Hash(transaction)
        bytes_hashed = bytes(bytearray.fromhex(hashedMsg))
        # 签名
        signature = signning_key.sign(bytes_hashed, hashfunc=hashfunc)
        # base64加密
        transaction_encode = self.base64_encode(signature)
        return transaction_encode

    def base64_decode(self, base_data):
        """
        base64解密
        :param base_data:
        :return:
        """
        bytes_data = base64.b64decode(base_data)
        return bytes_data

    def base64_encode(self, bytes_data):
        """
        base64加密
        :param bytes_data:
        :return:
        """
        base_data = base64.b64encode(bytes_data)
        return bytes.decode(base_data)

    def Hash(self, msg):
        """
        hash加密
        :return:
        """
        k = sha3.keccak_256()
        k.update(msg)
        return k.hexdigest()
