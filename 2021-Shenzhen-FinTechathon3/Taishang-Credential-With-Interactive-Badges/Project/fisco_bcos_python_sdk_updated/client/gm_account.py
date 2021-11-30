#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @author: kentzhang
  @date: 2020-01
'''

# 建立一个简单的国密账户类，只管理私钥，和转换到公钥，地址
# 可以保存为pem文件，或者从明文文件加载私钥，然后转换
# 不做签名，Account应该是签名的一个输入参数
# **原来已经实现的，使用ecdsa的account，暂时不做重构，保持其稳定性
import base64
import binascii
from gmssl import sm2, func
from gmssl import sm2_helper
from gmssl import sm3, func
from eth_utils.hexadecimal import decode_hex
from eth_utils import to_checksum_address
from eth_utils.crypto import *
from gmssl.sm4 import CryptSM4, SM4_DECRYPT, SM4_ENCRYPT
from client.bcoskeypair import BcosKeyPair
import json
import hmac


class GM_Account(object):
    keypair = BcosKeyPair()
    cbc_iv = b'\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00'

    def getdetail(self, sep="\n"):
        return self.keypair.getdetail(sep)

    def publickey_to_address(self, publickey):
        publickeybytes = func.bytes_to_list(bytes(decode_hex(publickey)))
        hashres = sm3.sm3_hash(publickeybytes)  # sm3_hash 对应sm3_implement.py里的sm3_hash,返回的是hex形态的结果
        hash_raw = decode_hex(hashres)  # 转为bytes
        addr_raw = hash_raw[-20:]  # 截取20个字节
        addr = to_checksum_address(addr_raw)  # 转为格式化的地址
        return addr

    def create(self):
        kp = sm2_helper.sm2_key_pair_gen()
        self.keypair.private_key = kp[0]
        self.keypair.public_key = kp[1]
        self.keypair.address = self.publickey_to_address(self.keypair.public_key)

    def from_key(self, privkey):
        self.keypair.public_key = sm2_helper.sm2_privkey_to_pub(privkey)
        self.keypair.private_key = privkey
        self.keypair.address = self.publickey_to_address(self.keypair.public_key)

    def pwd_ljust(self, password):
        return password.ljust(16, '0')

    def save_to_file(self, filename, password=None):
        content = {}
        key = self.keypair.private_key
        content["address"] = self.keypair.address
        content["encrypt"] = False
        if password is not None:
            crypt_sm4 = CryptSM4()
            password = self.pwd_ljust(password)
            crypt_sm4.set_key(bytes(password, "utf-8"), SM4_ENCRYPT)
            encrypt_value = crypt_sm4.crypt_cbc(
                self.cbc_iv, bytes(self.keypair.private_key, "utf-8"))
            key = str(base64.b64encode(encrypt_value), "utf-8")
            content["encrypt"] = True
        content["private_key"] = key
        content["type"] = "gm"
        # set mac of the password
        passwdBytes = bytes(password, "utf-8")
        content["mac"] = sm3.sm3_hash(passwdBytes)
        with open(filename, "w") as dump_f:
            json.dump(content, dump_f, indent=4)
            dump_f.close()

    # 从文件加载，格式是json
    def load_from_file(self, filename, password=None):
        if password is None:
            return
        with open(filename, "r") as dump_f:
            content = json.load(dump_f)
            dump_f.close()

        if content["type"] != "gm":
            return
        # get and compare mac
        expected_mac = content["mac"]
        password = self.pwd_ljust(password)
        passwdBytes = bytes(password, "utf-8")
        mac = sm3.sm3_hash(passwdBytes)
        if not hmac.compare_digest(mac, expected_mac):
            raise ValueError("MAC mismatch")
        key = content["private_key"]
        crypt_sm4 = CryptSM4()
        crypt_sm4.set_key(bytes(password, "utf-8"), SM4_DECRYPT)
        key = base64.b64decode(bytes(key, "utf-8"))
        key = str(crypt_sm4.crypt_cbc(self.cbc_iv, key), "utf-8")
        self.from_key(key)
