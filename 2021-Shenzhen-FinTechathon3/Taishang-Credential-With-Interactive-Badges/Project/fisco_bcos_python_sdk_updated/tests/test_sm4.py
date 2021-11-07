from gmssl.sm4 import CryptSM4, SM4_ENCRYPT, SM4_DECRYPT
from eth_utils import decode_hex,encode_hex
# key = b'3l5butlj26hvv313'
key = "0x142d340c0f4df64bf56bbc0a3931e5228c7836add09cf8ff3cefeb3d7e610deb458ec871a9da86bae1ffc029f5aba41e725786ecb7f93ad2670303bf2db27b8a"
key= decode_hex(key)
value = b'12345678901234561234567890123456'
iv = b'\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00'
crypt_sm4 = CryptSM4()

crypt_sm4.set_key(key, SM4_ENCRYPT)
encrypt_value = crypt_sm4.crypt_ecb(value)
crypt_sm4.set_key(key, SM4_DECRYPT)
decrypt_value = crypt_sm4.crypt_ecb(encrypt_value)
assert value == decrypt_value

crypt_sm4.set_key(key, SM4_ENCRYPT)
encrypt_value = crypt_sm4.crypt_cbc(iv, value)
crypt_sm4.set_key(key, SM4_DECRYPT)
decrypt_value = crypt_sm4.crypt_cbc(iv, encrypt_value)
assert value == decrypt_value
hexen = encode_hex(encrypt_value)
print(hexen)
print("len= ",len(hexen))
print("plain : ", crypt_sm4.crypt_cbc(iv,encrypt_value))