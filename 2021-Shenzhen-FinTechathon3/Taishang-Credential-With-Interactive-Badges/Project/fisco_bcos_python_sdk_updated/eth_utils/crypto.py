from typing import Union

from eth_hash.auto import keccak as keccak_256

from .conversions import to_bytes

from gmssl.sm3 import sm3_hash

from eth_utils.hexadecimal import decode_hex
CRYPTO_TYPE = 'keccak'
CRYPTO_TYPE_GM = "GM"
CRYPTO_TYPE_ECDSA = "ECDSA"


def set_crypto_type(crypto_type):
    global CRYPTO_TYPE
    CRYPTO_TYPE = crypto_type.upper()

# 国密实现，注意sm_hash接受的时候bytearray数组，返回的是已经hex化的结果如 'a75feb...'，调用者期望的是binary的结果，需要decode为二进制


def gmhash(primitive: Union[bytes, int, bool] = None, hexstr: str = None, text: str = None
           ) -> bytes:
    forhash = bytearray(to_bytes(primitive, hexstr, text))
    gmhash = sm3_hash(forhash)
    gmhash_bytes = decode_hex(gmhash)
    return gmhash_bytes

# 原来的是所有的hash都用keccak算法，是个总入口，后续重构


def keccak(
    primitive: Union[bytes, int, bool] = None, hexstr: str = None, text: str = None
) -> bytes:
    global CRYPTO_TYPE
    global CRYPTO_TYPE_GM
   # print("hashtype",CRYPTO_TYPE)
    if CRYPTO_TYPE == CRYPTO_TYPE_GM:
        return gmhash(primitive, hexstr, text)
    return keccak_256(to_bytes(primitive, hexstr, text))
