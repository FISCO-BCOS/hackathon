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
  @date: 2019-06
'''

import itertools

from cytoolz import (
    curry,
    dissoc,
    identity,
    merge,
    partial,
    pipe,
)
from eth_rlp.HashableRLP import (
    HashableRLP
)
from eth_utils.curried import (
    apply_formatters_to_dict,
    apply_one_of_formatters,
    hexstr_if_str,
    is_0x_prefixed,
    is_bytes,
    is_integer,
    is_string,
    to_bytes,
    to_int,
)
import rlp
from rlp.sedes import (
    Binary,
    big_endian_int,
    binary,
)

from eth_account._utils.validation import (
    is_valid_address,
)

VALID_EMPTY_ADDRESSES = {None, b'', ''}


def serializable_unsigned_transaction_from_dict(transaction_dict):
    assert_valid_fields(transaction_dict)
    filled_transaction = pipe(
        transaction_dict,
        dict,
        partial(merge, TRANSACTION_DEFAULTS),
        chain_id_to_v,
        apply_formatters_to_dict(TRANSACTION_FORMATTERS),
    )
    if 'v' in filled_transaction:
        serializer = BcosTransaction
    else:
        serializer = BcosUnsignedTransaction
    return serializer.from_dict(filled_transaction)


def encode_transaction(unsigned_transaction, vrs):
    (v, r, s) = vrs
    chain_naive_transaction = dissoc(unsigned_transaction.as_dict(), 'v', 'r', 's')
    signed_transaction = BcosTransaction(v=v, r=r, s=s, **chain_naive_transaction)
    return rlp.encode(signed_transaction)


def is_int_or_prefixed_hexstr(val):
    if is_integer(val):
        return True
    elif isinstance(val, str) and is_0x_prefixed(val):
        return True
    else:
        return False


def is_empty_or_checksum_address(val):
    if val in VALID_EMPTY_ADDRESSES:
        return True
    else:
        return is_valid_address(val)


def is_none(val):
    return val is None


TRANSACTION_DEFAULTS = {
    'to': b'',
    'value': 0,
    'data': b'',
    'extraData': None,
}

TRANSACTION_FORMATTERS = {
    'randomid': hexstr_if_str(to_int),
    'gasPrice': hexstr_if_str(to_int),
    'gasLimit': hexstr_if_str(to_int),
    'to': apply_one_of_formatters((
        (is_string, hexstr_if_str(to_bytes)),
        (is_bytes, identity),
        (is_none, lambda val: b''),
    )),
    'value': hexstr_if_str(to_int),
    'data': hexstr_if_str(to_bytes),
    'fiscoChainId': hexstr_if_str(to_int),
    'groupId': hexstr_if_str(to_int),
    'extraData': hexstr_if_str(to_bytes),
    'v': hexstr_if_str(to_int),
    'r': hexstr_if_str(to_int),
    's': hexstr_if_str(to_int),
}

TRANSACTION_VALID_VALUES = {
    'randomid': is_int_or_prefixed_hexstr,
    'gasPrice': is_int_or_prefixed_hexstr,
    'gasLimit': is_int_or_prefixed_hexstr,
    'blockLimit': is_int_or_prefixed_hexstr,
    'to': is_empty_or_checksum_address,
    'value': is_int_or_prefixed_hexstr,
    'data': lambda val: isinstance(val, (int, str, bytes, bytearray)),
    'fiscoChainId': lambda val: val is None or is_int_or_prefixed_hexstr(val),
    'groupId': is_int_or_prefixed_hexstr,
    'extraData': lambda val: isinstance(val, (int, str, bytes, bytearray)),
}

ALLOWED_TRANSACTION_KEYS = {
    'randomid',
    'gasPrice',
    'gasLimit',
    'blockLimit',
    'to',
    'value',
    'data',
    'fiscoChainId',
    'groupId',
    'extraData',

}

REQUIRED_TRANSACITON_KEYS = ALLOWED_TRANSACTION_KEYS.difference(TRANSACTION_DEFAULTS.keys())


def assert_valid_fields(transaction_dict):
    # check if any keys are missing
    missing_keys = REQUIRED_TRANSACITON_KEYS.difference(transaction_dict.keys())
    if missing_keys:
        raise TypeError("Transaction must include these fields: %r" % missing_keys)

    # check if any extra keys were specified
    superfluous_keys = set(transaction_dict.keys()).difference(ALLOWED_TRANSACTION_KEYS)
    if superfluous_keys:
        raise TypeError("Transaction must not include unrecognized fields: %r" % superfluous_keys)

    # check for valid types in each field
    valid_fields = apply_formatters_to_dict(TRANSACTION_VALID_VALUES, transaction_dict)
    if not all(valid_fields.values()):
        invalid = {key: transaction_dict[key] for key, valid in valid_fields.items() if not valid}
        raise TypeError("Transaction had invalid fields: %r" % invalid)


def chain_id_to_v(transaction_dict):
    # See EIP 155
    return transaction_dict
    chain_id = transaction_dict.pop('chainId')
    if chain_id is None:
        return transaction_dict
    else:
        return dict(transaction_dict, v=chain_id, r=0, s=0)


@curry
def fill_transaction_defaults(transaction):
    return merge(TRANSACTION_DEFAULTS, transaction)


UNSIGNED_TRANSACTION_FIELDS = (
    ('randomid', big_endian_int),
    ('gasPrice', big_endian_int),
    ('gasLimit', big_endian_int),
    ('blockLimit', big_endian_int),
    ('to', Binary.fixed_length(20, allow_empty=True)),
    ('value', big_endian_int),
    ('data', binary),
    ('fiscoChainId', big_endian_int),
    ('groupId', big_endian_int),
    ('extraData', binary),
)


class BcosTransaction(HashableRLP):
    fields = UNSIGNED_TRANSACTION_FIELDS + (
        ('v', big_endian_int),
        ('r', big_endian_int),
        ('s', big_endian_int),
    )


class BcosUnsignedTransaction(HashableRLP):
    fields = UNSIGNED_TRANSACTION_FIELDS


ChainAwareUnsignedTransaction = BcosTransaction


def strip_signature(txn):
    unsigned_parts = itertools.islice(txn, len(UNSIGNED_TRANSACTION_FIELDS))
    return list(unsigned_parts)


def vrs_from(transaction):
    return (getattr(transaction, part) for part in 'vrs')
