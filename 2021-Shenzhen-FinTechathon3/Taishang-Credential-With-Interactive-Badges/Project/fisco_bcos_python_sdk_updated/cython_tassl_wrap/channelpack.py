#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @author: kentzhang
  @date: 2019-06
"""
import uuid
import struct

'''
channel protocol ref:
https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-2.0/docs/design/protocol_description.html#channelmessage
'''


class ChannelPack:
    TYPE_RPC = 0x12
    TYPE_HEATBEAT = 0x13
    CLIENT_HANDSHAKE = 0x14
    CLIENT_REGISTER_EVENT_LOG = 0x15
    TYPE_AMOP_REQ = 0x30
    TYPE_AMOP_RESP = 0x31
    TYPE_TOPIC_REPORT = 0x32
    TYPE_TOPIC_MULTICAST = 0x35
    TYPE_TX_COMMITTED = 0x1000
    TYPE_TX_BLOCKNUM = 0x1001
    EVENT_LOG_PUSH = 0x1002

    headerfmt = "!IH32sI"
    headerlen = 0
    type = None
    result = None
    data = None
    seq = None
    totallen = None

    def __init__(self, ctype, seq, result, data):
        self.type = ctype
        self.seq = seq
        self.result = result
        self.data = data

    def detail(self):
        if self.totallen is None:
            datalen = 0
            if self.data is not None:
                datalen = len(self.data)
            self.totallen = ChannelPack.getheaderlen() + datalen
        msg = "len:{},type:{},result:{},seq:{},data:{}" \
            .format(self.totallen, hex(self.type), hex(self.result), self.seq, self.data)
        return msg

    @staticmethod
    def make_seq32():
        seq = uuid.uuid1()
        seq32 = seq.hex
        # seq32 = "".join(str(seq).split("-")).upper()
        seq32bytes = bytes(seq32, encoding='utf-8')
        return seq32bytes

    @staticmethod
    def get_seq_zero():
        seq_zero = "00000000000000000000000000000000"
        seqbytes = bytes(seq_zero, encoding='utf-8')
        return seqbytes

    @staticmethod
    def getheaderlen():
        if ChannelPack.headerfmt == 0:
            ChannelPack.headerfmt = struct.calcsize(ChannelPack.headerfmt)
        return ChannelPack.headerlen

    def pack(self):
        return ChannelPack.pack_all(self.type, self.seq, self.result, self.data)

    @staticmethod
    def pack_all(ctype, seq, result, data):
        headerlen = struct.calcsize(ChannelPack.headerfmt)
        databytes = data
        if not isinstance(databytes, bytes):
            databytes = bytes(data, "utf-8")
        fmt = "!IH32sI%ds" % (len(data))
        totallen = headerlen + len(data)
        buffer = struct.pack(fmt, totallen, ctype, seq, result, databytes)
        return buffer

    @staticmethod
    # return（code, 消耗的字节数，解析好的cp或None）
    def unpack(buffer):
        headerlen = struct.calcsize(ChannelPack.headerfmt)
        if len(buffer) < headerlen:
            return -1, 0, None
        totallen = struct.unpack_from("!I", buffer, 0)[0]
        if len(buffer) < totallen:
            # no enough bytes to decode
            return -1, 0, None
        datalen = len(buffer) - headerlen
        (totallen, ctype, seq, result) = struct.unpack_from(ChannelPack.headerfmt, buffer, 0)
        data = struct.unpack_from("%ds" % datalen, buffer, headerlen)[0]
        cp = ChannelPack(ctype, seq, result, data)
        cp.totallen = totallen
        return 0, totallen, cp


'''
x	pad byte	no value
c	char	string of length 1	1
b	signed char	integer	1	(3)
B	unsigned char	integer	1	(3)
?	_Bool	bool	1	(1)
h	short	integer	2	(3)
H	unsigned short	integer	2	(3)
i	int	integer	4	(3)
I	unsigned int	integer	4	(3)
l	long	integer	4	(3)
L	unsigned long	integer	4	(3)
q	long long	integer	8	(2), (3)
Q	unsigned long long	integer	8	(2), (3)
f	float	float	4	(4)
d	double	float	8	(4)
s	char[]	string	1
p	char[]	string
P	void *	integer	 	(5), (3)
'''
