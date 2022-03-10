import json
import os

from ecdsa import SigningKey

from client.bcoserror import BcosException
from client.bcoskeypair import BcosKeyPair
from client.gm_account import GM_Account
from eth_account.account import Account
from eth_account.signers.local import LocalAccount
from eth_utils import encode_hex
from gmssl import sm2
from gmssl.sm2 import CryptSM2


class Signer_Impl:
    def sign(self, data_in_byte, chain_id=None):
        pass

    def get_keypair(self) -> BcosKeyPair:
        pass


# 国密的签名实现
class Signer_GM(Signer_Impl):
    gm_account: GM_Account
    keypair: BcosKeyPair
    sm2_crypt: CryptSM2 = None

    def __init_(self):
        pass

    def __init__(self, account: GM_Account):
        self.set_account(account)

    def set_account(self, account: GM_Account):
        self.gm_account = account
        self.keypair = account.keypair

    def get_keypair(self) -> BcosKeyPair:
        return self.gm_account.keypair

    def sign(self, data_in_byte, chain_id=None):
        if self.sm2_crypt is None:
            self.sm2_crypt = sm2.CryptSM2(
                public_key=self.gm_account.keypair.public_key,
                private_key=self.gm_account.keypair.private_key)
        (r, s) = self.sm2_crypt.sign(data_in_byte)
        v_raw = self.keypair.public_key
        v = int(v_raw, 16)
        return (v, r, s)

    @staticmethod
    def from_privkey(privkey):
        account = GM_Account()
        account.from_key(privkey)
        return account

    @staticmethod
    def from_key_file(the_key_file, password):
        try:

            gm_account = GM_Account()
            if os.path.exists(the_key_file) is False:
                raise BcosException(("gm account keyfile file {} doesn't exist, "
                                     "please check client_config.py again "
                                     "and make sure this account exist")
                                    .format(the_key_file))
            gm_account.load_from_file(
                the_key_file, password)

            signer = Signer_GM(gm_account)

            return signer
        except Exception as e:
            raise BcosException("load gm account from {} failed, reason: {}"
                                .format(the_key_file, e))


# ------------------------------------------------------------
# ecdsa的签名实现
class Signer_ECDSA(Signer_Impl):
    CHAIN_ID_OFFSET = 35
    V_OFFSET = 27
    # signature versions
    PERSONAL_SIGN_VERSION = b'E'  # Hex value 0x45
    INTENDED_VALIDATOR_SIGN_VERSION = b'\x00'  # Hex value 0x00
    STRUCTURED_DATA_SIGN_VERSION = b'\x01'  # Hex value 0x01

    ecdsa_account: LocalAccount
    keypair: BcosKeyPair

    def __init__(self, account: LocalAccount):
        self.set_account(account)

    def get_keypair(self) -> BcosKeyPair:
        return self.keypair

    def set_account(self, account: LocalAccount):
        self.ecdsa_account = account
        self.keypair = BcosKeyPair()
        self.keypair.private_key = account.privateKey
        self.keypair.public_key = account.publickey
        self.keypair.address = account.address

    def to_eth_v(self, v_raw, chain_id=None):
        if chain_id is None:
            v = v_raw + Signer_ECDSA.V_OFFSET
        else:
            v = v_raw + Signer_ECDSA.CHAIN_ID_OFFSET + 2 * chain_id
        return v

    def sign(self, data_in_byte, chain_id=None):
        # print("ecdsa_account:",self.ecdsa_account)
        signature = self.ecdsa_account._key_obj.sign_msg_hash(data_in_byte)
        (v_raw, r, s) = signature.vrs
        v = self.to_eth_v(v_raw, chain_id)
        return (v, r, s)

    @staticmethod
    def from_privkey(privkey):
        account = Account.from_key(privkey)
        signer = Signer_ECDSA(account)
        return signer

    @staticmethod
    def from_key_file(the_key_file, password):
        # print("load from ",the_key_file)
        if os.path.exists(the_key_file) is False:
            raise BcosException(("key file {} doesn't exist, "
                                 "please check client_config.py again "
                                 "and make sure this account exist")
                                .format(the_key_file))

        account = Signer_ECDSA.load_from_keyfile(the_key_file, password)
        signer = Signer_ECDSA(account)
        return signer

    # 一些工具方法：
    def load_from_pem(filename):
        with open(filename) as f:
            p = f.read()
            # print("pem file:", p)
            key = SigningKey.from_pem(p)
            # print("privkey : ", encode_hex(key.to_string()))
            ac = Account.from_key(encode_hex(key.to_string()))
            f.close()
            return ac

    def load_from_keystore(filename, password):
        with open(filename, "r") as f:
            keytext = json.load(f)
            privkey = Account.decrypt(keytext, password)
            ac = Account.from_key(privkey)
            f.close()
            return ac

    def load_from_keyfile(filename, keyfilepwd=None):
        extname = os.path.splitext(filename)[1]
        if extname.endswith(".pem"):
            return Signer_ECDSA.load_from_pem(filename)
        if extname.endswith(".keystore"):
            return Signer_ECDSA.load_from_keystore(filename, keyfilepwd)
