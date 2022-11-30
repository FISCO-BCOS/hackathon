#读一下bcos3 c sdk 的config文件
import configparser

    

class Bcos3SDKConfig:
    thread_pool_size = 8
    message_timeout_ms = 10000
    cert = dict()
    peers = dict()
    def __init__(self,configfile):
        
        try:
            ini = configparser.ConfigParser()
            ini.read(configfile)
            self.thread_pool_size = ini["common"]["thread_pool_size"]
            self.message_timeout_ms = ini["common"]["message_timeout_ms"]
            for item in ini["cert"].items():
                self.cert[item[0]]=item[1]
            for item in ini["peers"].items():
                self.peers[item[0]]=item[1]
        except Exception as e:
            print(f"load bcos3 sdk config error! file:{configfile}")
            return

        
    def getdetail(self):
        detail=f"thread_pool_size={self.thread_pool_size};message_timeout_ms={self.message_timeout_ms};"
        detail=detail+f"cert:{self.cert};"
        detail=detail+f"peers:{self.peers}"
        return detail
        

#config = Bcos3SDKConfig("bcos3sdklib/bcos3_sdk_config.ini")
#print(config.getdetail())


'''
[common]
    ; if ssl connection is disabled, default: false
    ; disable_ssl = true
    ; thread pool size for network message sending recving handing
    thread_pool_size = 8
    ; send message timeout(ms)
    message_timeout_ms = 10000

; ssl cert config items,
[cert]
    ; ssl_type: ssl or sm_ssl, default: ssl
    ssl_type = ssl
    ; directory the certificates located in, defaul: ./conf
    ca_path=./bcos3sdklib
    ; the ca certificate file
    ca_cert=ca.crt
    ; the node private key file
    sdk_key=sdk.key
    ; the node certificate file
    sdk_cert=sdk.crt

[peers]
# supported ipv4 and ipv6
    node.0=127.0.0.1:20200
    
'''