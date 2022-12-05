import os
from eth_utils.crypto import set_crypto_type, CRYPTO_TYPE_GM, CRYPTO_TYPE_ECDSA


class client_config_controller:
    """
    类成员变量，便于用.调用和区分命名空间
    """
    # 整个客户端的全局配置，影响console相关的账户目录、日志目录、合约目录等
    # crypto_type : 大小写不敏感："GM" for 国密, "ECDSA" 或其他是椭圆曲线默认实现。
    crypto_type = "ECDSA"
    # crypto_type = "GM"
    ssl_type = crypto_type  # 和节点tls通信方式，如设为gm，则使用国密证书认证和加密
    # ssl_type = "GM"
    set_crypto_type(crypto_type)  # 使其全局生效
    # 默认日志输出目录，该目录不会自动建，必须先建立
    logdir = "bin/logs_controller"
    # 合约相关路径
    contract_dir = "./contracts"
    contract_info_file = "bin/contract.ini"  # 保存已部署合约信息的文件
    # 账号文件相关路径
    account_keyfile_path = "bin/accounts"  # 保存keystore文件的路径，在此路径下,keystore文件以 [name].keystore命名
    # account_keyfile = "pyaccount.keystore"

    account_keyfile = "Permission.pem"
    account_password = "123456"  # 实际使用时建议改为复杂密码
    gm_account_keyfile = "gm_account.json"  # 国密账号的存储文件，可以加密存储,如果留空则不加载
    gm_account_password = ""  # 如果不设密码，置为None或""则不加密

    # ---------编译器 compiler related--------------
    # path of solc compiler
    solc_path = "bin/solc/solc.exe"
    # solc_path = "bin/solc/solc6.exe"
    solcjs_path = "./solcjs"
    gm_solc_path = "./bin/solc/solc-gm.exe"
    # ---------console mode, support user input--------------
    background = True

    # ------------------FISCO BCOS3.0 Begin----------------------------------------
    # FISCO BCOS3.0的配置段，如连接FISCO BCOS2.0版本，无需关心此段
    # FISCO BCOS3.0 c底层sdk的配置，都在bcos3_config_file里，无需配置在此文件
    bcos3_lib_path = "./bcos3sdklib"
    bcos3_config_file = "./bcos3sdklib/bcos3_sdk_config.ini"
    bcos3_group = "group0"
    # -------------------FISCO BCOS3.0 End-----------------------------------------

    # --------------------------------------
    # FISCO BCOS2.0的配置段，如连接FISCO BCOS3.0版本，无需关心此段
    # keyword used to represent the RPC Protocol
    PROTOCOL_RPC = "rpc"
    # keyword used to represent the Channel Protocol
    PROTOCOL_CHANNEL = "channel"
    fiscoChainId = 1  # 链ID，和要通信的节点*必须*一致
    groupid = 1  # 群组ID，和要通信的节点*必须*一致，如和其他群组通信，修改这一项，或者设置bcosclient.py里对应的成员变量
    client_protocol = "channel"  # or PROTOCOL_CHANNEL to use channel prototol
    # client_protocol = PROTOCOL_CHANNEL
    remote_rpcurl = "http://127.0.0.1:8545"  # 采用rpc通信时，节点的rpc端口,和要通信的节点*必须*一致,如采用channel协议通信，这里可以留空
    channel_host = "127.0.0.1"  # 采用channel通信时，节点的channel ip地址,如采用rpc协议通信，这里可以留空
    channel_port = 20200  # 节点的channel 端口,如采用rpc协议通信，这里可以留空
    channel_ca = "bin/ca.crt"  # 采用channel协议时，需要设置链证书,如采用rpc协议通信，这里可以留空
    channel_node_cert = "bin/sdk.crt"  # 采用channel协议时，需要设置sdk证书,如采用rpc协议通信，这里可以留空
    channel_node_key = "bin/sdk.key"  # 采用channel协议时，需要设置sdk私钥,如采用rpc协议通信，这里可以留空
    channel_en_crt = "bin/gmensdk.crt"  # 仅国密双证书使用，加密证书
    channel_en_key = "bin/gmensdk.key"  # 仅国密双证书使用，加密key