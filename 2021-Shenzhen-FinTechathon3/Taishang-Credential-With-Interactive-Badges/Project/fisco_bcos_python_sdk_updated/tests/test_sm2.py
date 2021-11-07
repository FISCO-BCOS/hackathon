import base64
import binascii
from gmssl import sm2, func
from gmssl import sm2_helper
from eth_utils import decode_hex,encode_hex
import os



if __name__ == '__main__':
    private_key = '00B9AB0B828FF68872F21A837FC303668428DEA11DCD1B24429D0C99E24EED83D5'
    private_key = encode_hex( os.urandom(4)  )
    print("key seed is ",private_key)
    #public_key = 'B9C9A6E04E9C91F7BA880429273747D7EF5DDEB0BB2FF6317EB00BEF331A83081A6994B8993F3F5D6EADDDB81872266C87C018FB4162F5AF347B483E24620207'

    kp = sm2_helper.sm2_key_pair_gen()
    print("key_pair_gen:", kp)
    public_key = sm2_helper.sm2_privkey_to_pub(private_key, True)
    print("key gen by privatekey:")
    print(public_key)
    print(public_key)
    sm2_crypt = sm2.CryptSM2(
        public_key=public_key, private_key=private_key)
    data = b"12345678"
    enc_data = sm2_crypt.encrypt(data)

    #print("enc_data:%s" % enc_data)
    #print("enc_data_base64:%s" % base64.b64encode(bytes.fromhex(enc_data)))
    dec_data = sm2_crypt.decrypt(enc_data)
    print(b"dec_data:%s" % dec_data)
    assert data == dec_data
    print(len(encode_hex(enc_data)))

    print("-----------------test sign and verify---------------")
    #random_hex_str = func.random_hex(sm2_crypt.para_len)
    (R, S) = sm2_crypt.sign(data)

    print("signed r s = ", R, S)
    sign = sm2_crypt.combine_signed_R_S(R, S)
    print('sign:%s' % sign)
    verify = sm2_crypt.verify(sign, data)
    print('verify:%s' % verify)
    assert verify
