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

from collections import (
    Mapping,
)

from cytoolz import (
    dissoc,
)

from client.bcoserror import BcosException
from client.bcostransactions import (
    BcosUnsignedTransaction,
    encode_transaction,
    serializable_unsigned_transaction_from_dict,
)
from client.signer_impl import Signer_Impl
from eth_account.datastructures import (
    AttributeDict,
)
from eth_utils.curried import (
    keccak,
)
from hexbytes import (
    HexBytes,
)


class SignTx():
    # gm_account = None  # 国密的账号
    # ecdsa_account = None  # ECDSA的公私钥对象，类型为 LocalAccount : eth_account/signers/local.py
    # crypto_type = None  # 加密类型，国密或通用，来自eth_utils/crypto.py的CRYPTO_TYPE_GM，CRYPTO_TYPE_ECDSA

    # 2021.02 采用统一的signer接口去签名交易（需要附带chain_id,可以为None
    # 实现参见 client/singer_impl.py, sign(data,chain_id)即为通用接口
    signer: Signer_Impl

    def sign_transaction_hash(self, transaction_hash, chain_id):

        if not isinstance(self.signer, Signer_Impl):
            raise BcosException("Transaction Signer must by Signer_Imple(GM/ECDSA)type")

        (v, r, s) = self.signer.sign(transaction_hash, chain_id)
        return (v, r, s)
        '''
        原来的老实现，先放一放，稳定后删掉
        hashbytes = bytes(transaction_hash)
        if self.crypto_type == CRYPTO_TYPE_GM:
            # gm sign
            public_key = self.gm_account.keypair.public_key
            private_key = self.gm_account.keypair.private_key
            sm2_crypt = sm2.CryptSM2(
                public_key=public_key, private_key=private_key)
            (r, s) = sm2_crypt.sign(hashbyte)
            v_raw = public_key
            v = int(v_raw, 16)
        elif self.crypto_type == CRYPTO_TYPE_ECDSA:
            # ecdsa sign
            signature = self.ecdsa_account._key_obj.sign_msg_hash(transaction_hash)
            (v_raw, r, s) = signature.vrs
            v = self.to_eth_v(v_raw, chain_id)

        else:
            raise BcosException(
                "when sign transaction, unknown crypto type {}".format(
                    self.crypto_type))

        return (v, r, s)
        '''
    '''
        debug = False
        if debug: #debug gm
            print("transaction_hash in hex", encode_hex(transaction_hash))
            print("transaction_hash in byte:", hashbyte)
            print(len(hashbyte))
            signstr = sm2_crypt.combine_signed_R_S(r, s)
            data = transaction_hash
            verify = sm2_crypt.verify(signstr, data)
            print('verify:%s' % verify)
            print("privatekey: ", private_key)
            print("data to sign:", transaction_hash)
            print("SIGN to V:%s,len:%d" % (v_raw, len(v_raw)))
            print("SIGN to R:", r)  # guomi
            print("SIGN to R(hex): %064x" % (r))  # guomi
            print("SIGN to S(hex): %064x" % (s))
            print('signstr:%s' % signstr)
            print(v)
            print("hex_v:", hex(v))
       '''

    def sign_transaction_dict(self, transaction_dict):
        # generate RLP-serializable transaction, with defaults filled
        unsigned_transaction = serializable_unsigned_transaction_from_dict(transaction_dict)

        transaction_hash = unsigned_transaction.hash()

        # detect chain
        if isinstance(unsigned_transaction, BcosUnsignedTransaction):
            chain_id = None
        else:
            chain_id = unsigned_transaction.v

        # sign with private key
        (v, r, s) = self.sign_transaction_hash(transaction_hash, chain_id)

        # serialize transaction with rlp
        encoded_transaction = encode_transaction(unsigned_transaction, vrs=(v, r, s))

        return (v, r, s, encoded_transaction)

    def sign_transaction(self, transaction_dict):

        if not isinstance(transaction_dict, Mapping):
            raise TypeError("transaction_dict must be dict-like, got %r" % transaction_dict)

        # allow from field, *only* if it matches the private key
        if 'from' in transaction_dict:
            if transaction_dict['from'] == self.signer.get_keypair().address:
                sanitized_transaction = dissoc(transaction_dict, 'from')
            else:
                raise TypeError("from field must match key's %s, but it was %s" % (
                    self.signer.get_keypair().address,
                    transaction_dict['from'],
                ))
        else:
            sanitized_transaction = transaction_dict

        # sign transaction
        (
            v,
            r,
            s,
            rlp_encoded,
        ) = self.sign_transaction_dict(sanitized_transaction)

        transaction_hash = keccak(rlp_encoded)

        return AttributeDict({
            'rawTransaction': HexBytes(rlp_encoded),
            'hash': HexBytes(transaction_hash),
            'r': r,
            's': s,
            'v': v,
        })
