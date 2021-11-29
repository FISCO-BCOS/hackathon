from random import choice


def xor(a, b): return list(map(lambda x, y: x ^ y, a, b))


def rotl(x, n): return ((x << n) & 0xffffffff) | ((x >> (32 - n)) & 0xffffffff)


def get_uint32_be(key_data): return (
    (key_data[0] << 24) | (
        key_data[1] << 16) | (
            key_data[2] << 8) | (
                key_data[3]))


def put_uint32_be(n): return [
    ((n >> 24) & 0xff),
    ((n >> 16) & 0xff),
    ((n >> 8) & 0xff),
    ((n) & 0xff)]


def padding(data, block=16): return data + \
    [(16 - len(data) % block)for _ in range(16 - len(data) % block)]


def unpadding(data): return data[:-data[-1]]


def list_to_bytes(data): return b''.join([bytes((i,)) for i in data])


def bytes_to_list(data): return [i for i in data]


def random_hex(x): return ''.join([choice('0123456789abcdef') for _ in range(x)])
