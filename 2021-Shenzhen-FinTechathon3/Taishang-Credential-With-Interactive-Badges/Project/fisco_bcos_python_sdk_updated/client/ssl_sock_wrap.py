import abc
import socket
import ssl
from ssl import SSLContext

from client import clientlogger
from client.bcoserror import ChannelException


# 抽象接口类，用于对接国密和非国密的ssl实现
class CommonSSLSockWrap:
    @abc.abstractmethod
    def init(self, ca_file, node_crt_file, node_key_file,
             en_crt_file=None,
             en_key_file=None,
             protocol=ssl.PROTOCOL_TLSv1_2,
             verify_mode=ssl.CERT_REQUIRED):
        pass

    @abc.abstractmethod
    def try_connect(self, host=None, port=0):
        pass

    @abc.abstractmethod
    def recv(self, recvsize) -> bytes:
        pass

    @abc.abstractmethod
    def send(self, buffer):
        pass

    @abc.abstractmethod
    def finish(self):
        pass


# 非国密的SSL实现，直接使用python的ssl模块
class SSLSockWrap(CommonSSLSockWrap):
    context: SSLContext = None
    ssock = None  # 被SSL包装过的socket
    host = ""
    port = 0
    logger: clientlogger
    ECDH_curve = "secp256k1"

    def __init__(self):
        pass

    def init(self, ca_file, node_crt_file, node_key_file,
             en_crt_file=None,
             en_key_file=None,
             protocol=ssl.PROTOCOL_TLSv1_2,
             verify_mode=ssl.CERT_REQUIRED):

        try:
            # print("init tls ", ca_file, node_key_file, node_key_file,protocol)
            context = ssl.SSLContext(protocol)

            context.check_hostname = False
            context.load_verify_locations(ca_file)
            if en_crt_file is None or en_key_file is None:
                pass
            context.load_cert_chain(node_crt_file, node_key_file)

            # print(context.get_ca_certs())
            context.set_ecdh_curve(self.ECDH_curve)
            context.verify_mode = verify_mode
            self.context = context
        except Exception as e:
            import traceback
            traceback.print_stack()
            raise ChannelException(("init ssl context failed,"
                                    " please check the certificates ,reason : {}").
                                   format(e))

    def try_connect(self, host=None, port=0):
        if host is not None:
            self.host = host
        if port != 0:
            self.port = port
        # print("connect ",self.host,self.port)
        sock = socket.create_connection((self.host, self.port))
        self.logger.debug("connect {}:{},as socket {}".format(self.host, self.port, sock))
        # 将socket打包成SSL socket
        self.ssock = self.context.wrap_socket(sock)

    def recv(self, recvsize) -> bytes:
        r = self.ssock.recv(recvsize)
        # print("recv", r,recvsize)
        return r

    def send(self, buffer):
        # print("send buffer: ",buffer)
        return self.ssock.send(buffer)

    def finish(self):
        if self.ssock is None:
            return
        self.ssock.shutdown(socket.SHUT_RDWR)
        self.ssock.close()
        self.ssock = None
