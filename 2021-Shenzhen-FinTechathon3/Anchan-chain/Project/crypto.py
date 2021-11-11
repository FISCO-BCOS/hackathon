from typing import List, Tuple
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Protocol.SecretSharing import Shamir
from Crypto.PublicKey import RSA

def shamir_encode(t=3, n=5) -> List[Tuple[int, bytes]]:
    key = get_random_bytes(16)
    shares = Shamir.split(t, n, key)
    return key, shares

def shamir_recover(shares: List[Tuple[int, bytes]]) -> bytes:
    return Shamir.combine(shares)

def aes_encode(key: bytes, filename: str, output: str):
    with open(filename, "rb") as fi:
        cipher = AES.new(key, AES.MODE_EAX)
        nonce = cipher.nonce
        ct, tag = cipher.encrypt(fi.read()), cipher.digest()

    with open(output, "wb") as fo:
        fo.write(nonce+tag+ct)

def aes_decode(key: bytes, cipherfile: str, output: str):
    with open(cipherfile, "rb") as fi:
        ct = fi.read()
    nonce = ct[:16]
    tag = ct[16:32]
    ctt = ct[32:]

    cipher = AES.new(key, AES.MODE_EAX, nonce = nonce)
    
    result = cipher.decrypt(ctt)
    cipher.verify(tag)
    with open(output, "wb") as fo:
        fo.write(result)

def gen_rsakey():
    key_pair = RSA.generate(2048)
    return key_pair.exportKey(), key_pair.public_key().export_key()
