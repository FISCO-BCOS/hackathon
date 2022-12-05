# 国密的实现，引入对TASSL的cython包装

import sys

from client.ssl_sock_wrap import CommonSSLSockWrap

sys.path.append("./client")

ECHO_NONE = 0x0000
ECHO_PRINTF = 0x0001
ECHO_LOG = 0x0010


class TasslSockWrap(CommonSSLSockWrap):
    ssock: CommonSSLSockWrap = None

    def __init__(self):
        from cython_tassl_wrap.tassl_ssock_wrap_factory import tassl_ssock_wrap_factory
        self.ssock = tassl_ssock_wrap_factory()
        print("INFO >> SSL using tassl ssock  : ", type(self.ssock))

    def try_connect(self, host=None, port=0):
        # print("try_connect")
        return self.ssock.try_connect(host, port)

    def recv(self, recvsize) -> bytes:
        # print("---DO RECV----",recvsize)
        # traceback.print_stack()
        r = self.ssock.recv(recvsize)
        # print("IMPL-RECV----->",r)
        return r

    def send(self, buffer):
        # print("---DO SEND----",buffer)
        return self.ssock.send(buffer)

    def finish(self):
        # traceback.print_stack()
        self.ssock.finish()

    def init(self, ca_file, node_crt_file, node_key_file, en_crt_file=None, en_key_file=None,
             protocol=None, verify_mode=None):
        self.ssock.set_echo_mode(ECHO_NONE)
        r = self.ssock.init(ca_file, node_crt_file, node_key_file, en_crt_file, en_key_file)

        return r
