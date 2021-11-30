import ssl
import socket
from client.channelpack import ChannelPack

context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
context.check_hostname = False
CA_FILE = "bin/ca.crt"
context.load_verify_locations(CA_FILE)
context.load_cert_chain("bin/node.crt", "bin/node.key")
print(context.get_ca_certs())
context.set_ecdh_curve("secp256k1")
context.verify_mode = ssl.CERT_REQUIRED


#ciphers = 'ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-ECDSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:ECDHE-ECDSA-AES256-SHA:DH-DSS-AES256-GCM-SHA384:DHE-DSS-AES256-GCM-SHA384:DH-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-SHA256:DHE-DSS-AES256-SHA256:DH-RSA-AES256-SHA256:DH-DSS-AES256-SHA256:DHE-RSA-AES256-SHA:DHE-DSS-AES256-SHA:DH-RSA-AES256-SHA:DH-DSS-AES256-SHA:DHE-RSA-CAMELLIA256-SHA:DHE-DSS-CAMELLIA256-SHA:DH-RSA-CAMELLIA256-SHA:DH-DSS-CAMELLIA256-SHA:ECDH-RSA-AES256-GCM-SHA384:ECDH-ECDSA-AES256-GCM-SHA384:ECDH-RSA-AES256-SHA384:ECDH-ECDSA-AES256-SHA384:ECDH-RSA-AES256-SHA:ECDH-ECDSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:CAMELLIA256-SHA:PSK-AES256-CBC-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-ECDSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:ECDHE-ECDSA-AES128-SHA:DH-DSS-AES128-GCM-SHA256:DHE-DSS-AES128-GCM-SHA256:DH-RSA-AES128-GCM-SHA256:DHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES128-SHA256:DHE-DSS-AES128-SHA256:DH-RSA-AES128-SHA256:DH-DSS-AES128-SHA256:DHE-RSA-AES128-SHA:DHE-DSS-AES128-SHA:DH-RSA-AES128-SHA:DH-DSS-AES128-SHA:DHE-RSA-SEED-SHA:DHE-DSS-SEED-SHA:DH-RSA-SEED-SHA:DH-DSS-SEED-SHA:DHE-RSA-CAMELLIA128-SHA:DHE-DSS-CAMELLIA128-SHA:DH-RSA-CAMELLIA128-SHA:DH-DSS-CAMELLIA128-SHA:ECDH-RSA-AES128-GCM-SHA256:ECDH-ECDSA-AES128-GCM-SHA256:ECDH-RSA-AES128-SHA256:ECDH-ECDSA-AES128-SHA256:ECDH-RSA-AES128-SHA:ECDH-ECDSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA:SEED-SHA:CAMELLIA128-SHA:PSK-AES128-CBC-SHA:ECDHE-RSA-DES-CBC3-SHA:ECDHE-ECDSA-DES-CBC3-SHA:EDH-RSA-DES-CBC3-SHA:EDH-DSS-DES-CBC3-SHA:DH-RSA-DES-CBC3-SHA:DH-DSS-DES-CBC3-SHA:ECDH-RSA-DES-CBC3-SHA:ECDH-ECDSA-DES-CBC3-SHA:DES-CBC3-SHA:IDEA-CBC-SHA:PSK-3DES-EDE-CBC-SHA:KRB5-IDEA-CBC-SHA:KRB5-DES-CBC3-SHA:KRB5-IDEA-CBC-MD5:KRB5-DES-CBC3-MD5:ECDHE-RSA-RC4-SHA:ECDHE-ECDSA-RC4-SHA:ECDH-RSA-RC4-SHA:ECDH-ECDSA-RC4-SHA:RC4-SHA:RC4-MD5:PSK-RC4-SHA:KRB5-RC4-SHA:KRB5-RC4-MD5'
# context.set_ciphers(ciphers)

host = '127.0.0.1'
port = 20200
testreq = '{"jsonrpc":"2.0","method":"getClientVersion","params":[],"id":1}'


class client_ssl:
    def send_hello(self,):
        # 与服务端建立socket连接
        with socket.create_connection((host, port)) as sock:
            #sock.send(bytes(testreq,"utf-8") )
            # print("sent",testreq)
            #reply = sock.recv(4096)
            # print(reply)
            # 将socket打包成SSL socket
            print("connect {}:{},as socket {}".format(host, port, sock))
            with context.wrap_socket(sock) as ssock:
                print(ssock)
                # 向服务端发送信息
                buffer = ChannelPack.pack_all(ChannelPack.TYPE_RPC, testreq)
                ssock.send(buffer)
                # 接收服务端返回的信息
                responsePack = None
                respbuffer = bytearray()
                for i in [0, 3]:
                    msg = ssock.recv(1024 * 10)
                    respbuffer += msg
                    (code, len, responsePack) = ChannelPack.unpack(bytes(respbuffer))
                    i += 1
                    if code != 0:
                        continue
                    if len > 0:
                        respbuffer = respbuffer[len:]
                        if code == 0:
                            break
                print(responsePack.result, responsePack.data)

                ssock.close()


if __name__ == "__main__":
    client = client_ssl()
    client.send_hello()
