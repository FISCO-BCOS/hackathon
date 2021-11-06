# 安产链-运行指引

## 基本依赖

|  依赖   | 版本  |
|  ----  | ----  |
| Python  | 3.8.10 |
| pip    | 21.3.1 |
| Java  | openjdk-11 |
| Node  | 8.17.0 |
|npm    | 6.13.4 |
|docker | 20.10.7|
|docker-compose | 1.25.0 |

本项目包括：

**fisco bcos 区块链**
**python sdk**
**ipfs私有集群**
**安产链项目**
**WeBASE服务**
**Truora预言机**
**Hyperledger Caliper**、

## Fisco BCOS 区块链

利用fisco bcos v2.8.0 build_chain.sh搭建节点
```sh
cd ~ 
mkdir anchan & cd anchan
curl -#LO https://github.com/FISCO-BCOS/FISCO-BCOS/releases/download/v2.8.0/build_chain.sh && chmod u+x build_chain.sh #下载脚本
bash build_chain.sh -l 127.0.0.1:4 -p 30300,20200,8545 #部署4个节点
./nodes/127.0.0.1/start_all.sh #启动所有节点
```

## Python SDK


**拉取源代码**
```sh
git clone https://github.com/FISCO-BCOS/python-sdk #克隆仓库
cd python-sdk
pip install -r requirements.txt 
bash init_env.sh -i
npm install solc@v0.4.25
cp ~/anchan/nodes/127.0.0.1/sdk/* ./bin #复制证书
```

**测试部署**

```sh
./console.py getNodeVersion #检查SDK能否连接节点。
```
## IPFS 私有集群

```
wget https://dist.ipfs.io/ipfs-cluster-ctl/v0.14.0/ipfs-cluster-ctl_v0.14.0_linux-amd64.tar.gz

tar xvzf ipfs-cluster-ctl_v0.14.0_linux-amd64.tar.gz
cd ipfs-cluster-ctl & wget https://raw.githubusercontent.com/ipfs/ipfs-cluster/master/docker-compose.yml

```
由于ipfsclientclient的版本限制，需要修改源码：
```sh
cd $HOME/.local/lib/python3.8/site-packages/ipfshttpclient/client
# 将__init__.py中的VERSION_MAXIMUM改为：
VERSION_MAXIMUM   = "0.11.0"
```
回到ipfs-cluster-clt文件夹，利用docker-compose启动容器
```
docker-compose up 
```

## 安产链项目

```sh
git clone https://git.lug.ustc.edu.cn/anchan-chain/anchan-chain.git #拉取源代码

cd anchan-chain
pip install -r requirement.txt

mkdir -p ./bin/logs
cp -r ${PATH_TO_PYTHON_SDK}/bin/* ./bin #将python-sdk的bin文件夹内容复制bin中

```

**修改client_config.py**

`${PATH_TO_PYTHON_SDK}`为Python SDK路径
`${PATH_TO_ANCAHNCHAIN_PROJECT}` 为安产链项目代码路径

```py 
    channel_ca = "${PATH_TO_PYTHON_SDK}/bin/ca.crt"  # 采用channel协议时，需要设置链证书,如采用rpc协议通信，这里可以留空
    channel_node_cert = "${PATH_TO_PYTHON_SDK}/bin/sdk.crt"  # 采用channel协议时，需要设置sdk证书,如采用rpc协议通信，这里可以留空
    channel_node_key = "${PATH_TO_PYTHON_SDK}/bin/sdk.key"   # 采用channel协议时，需要设置sdk私钥,如采用rpc协议通信，这里可以留空

    # ---------account &keyfile config--------------
    contract_info_file = "${PATH_TO_ANCHANCHAIN_PROJECT}/bin/contract.ini"  # 保存已部署合约信息的文件
    account_keyfile_path = "${PATH_TO_ANCHANCHAIN_PROJECT}/bin/accounts"  # 保存keystore文件的路径，在此路径下,keystore文件以 [name].keystore命名
    account_keyfile = "pyaccount.keystore"
    account_password = "123456"  # 实际使用时建议改为复杂密码
    gm_account_keyfile = "gm_account.json"  # 国密账号的存储文件，可以加密存储,如果留空则不加载
    gm_account_password = "123456"  # 如果不设密码，置为None或""则不加密
    
    # ---------console mode, support user input--------------
    background = True

    # ---------runtime related--------------
    # path of solc compiler
    solc_path = "${PATH_TO_PYTHON_SDK}/bin/solc/v0.4.25/solc"
    gm_solc_path = "${PATH_TO_PYTHON_SDK}/bin/solc/v0.4.25/solc-gm"
    solcjs_path = "${PATH_TO_PYTHON_SDK}/solcjs"

    logdir = "${PATH_TO_ANCHANCHAIN_PROJECT}/bin/logs"  # 默认日志输出目录，该目录必须先建立
```

**启动服务**

```sh
python blockchain.py #启动SDK，编译、部署并初始化所有智能合约，
python app.py #启动flask服务
```

## WeBASE

**MySql环境部署**
```sh
docker pull mysql:5.6
sudo apt install mariadb-client-core-10.1 
pip3 install PyMySQL
```

**WeBase配置**
```sh
wget https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/WeBASE/releases/download/v1.5.3/webase-deploy.zip
unzip webase-deploy.zip && cd webase-deploy
mkdir sqlConf sqlData sqlLogs
docker run -p 3306:3306 --name mysql56 -v $PWD/sqlConf:/etc/mysql/conf.d -v $PWD/sqlLogs:/logs -v $PWD/sqlData:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.6

```
**跳过验证**
修改webase-deploy/comm/check.py, 将47行checkExitedChainInfo() 注释掉。否则无法运行

**修改commom-properties**
```s
# Mysql database configuration of WeBASE-Node-Manager
mysql.ip=127.0.0.1
mysql.port=3306
mysql.user=root
mysql.password=123456
mysql.database=webasenodemanager

# Mysql database configuration of WeBASE-Sign
sign.mysql.ip=127.0.0.1
sign.mysql.port=3306
sign.mysql.user=root
sign.mysql.password=123456
sign.mysql.database=webasesign

# 由于与IPFS端口冲突，所以将5001-5004改为5100-5104
# WeBASE-Web service port 
web.port=5100

# WeBASE-Node-Manager service port
mgr.port=5101

# WeBASE-Front service port
front.port=5102

# WeBASE-Sign service port
sign.port=5104

# 由于使用已有区块链节点，故设置一下参数
if.exist.fisco=yes
fisco.dir=/home/ubuntu/anchan/nodes/127.0.0.1
```
**启动WeBASE**

```sh
python deploy.py installAll # 启动WeBASE服务
```

## Truora可信预言机

**拉取源码**
```sh
wget "https://github.com/WeBankBlockchain/Truora-Service/releases/download/v1.1.0/docker-deploy.zip"
unzip docker-deploy.zip -d docker-deploy # 必须加-d 参数，否则会将所有内容解压到当前文件夹
```
**创建数据库**

```sh 
mysql -h 127.0.0.1 -P 3306 -u root -p
> CREATE DATABASE `truora`;
```
**跳过某些步骤**
由于docker已经启动，当使用普通用户时，运行deploy_single.sh脚本是无法使用`systemctl start docker`命令启动docker的，

将docker-deploy/util/utils.sh `135`行`systemctl start docker`注释。

跳过启动truora-web和truora-service容器启动后检查端口部分：

将docker-deploy/start.sh `59-67`行代码注释。

注：节点sdk文件夹中没有`node.key`和`node.crt`文件，必须要将某个node的conf文件夹中的`node.key`和`node.crt`复制到sdk文件夹中。

**启动docker-deploy**
```sh
bash deploy_single.sh -d
```

## Hyperledger Caliper 

**基本配置**
```sh
mkdir benchmarks && cd benchmarks
nvm use 8
nvm init
npm install --only=prod @hyperledger/caliper-cli@0.2.0
npx caliper bind --caliper-bind-sut fisco-bcos --caliper-bind-sdk latest
git clone https://github.com/vita-dounai/caliper-benchmarks.git
```

**修改源码**
阅读<https://github.com/FISCO-BCOS/FISCO-BCOS/issues/1248>相关描述，

根据<https://github.com/hyperledger/caliper/pull/647/files>
修改以下文件
`benchmarks/node_modules/@hyperledger/caliper-fisco-bcos/lib/channelPromise.js`
`benchmarks/node_modules/@hyperledger/caliper-fisco-bcos/lib/fiscoBcos.js`
`benchmarks/node_modules/@hyperledger/caliper-fisco-bcos/lib/web3lib/web3sync.js`

根据<https://github.com/FISCO-BCOS/FISCO-BCOS/issues/1721#event-3661578575>修改
`benchmarks/node_modules/@hyperledger/caliper-fisco-bcos/package.json`中`dependencies`添加一项`"secp256k1": "^3.8.0"`并执行`npm -i `



**创建账户**
```
cd ${PATH_TO_PYTHON_SDK}
./console.py newaccount ${newAccount} ${accountPrivkey}
```
**修改fisco-bcos.json**
由于我们使用脚本本地建链，需要创建新账户并修改`benchmarks/caliper-benchmarks/networks/fisco-bcos/4nodes1group/fisco-bcos.json`

注1：填写私钥${accountPrivkey} 时要删掉`0x`前缀
注2：节点sdk文件夹中没有`node.key`和`node.crt`文件，必须要将某个node的conf文件夹中的`node.key`和`node.crt`复制到sdk文件夹中。
```json
{
    "caliper": {
        "blockchain": "fisco-bcos"
    },
    "fisco-bcos": {
        "config": {
            "privateKey": "${accountPrivkey}", 
            "account": "${newAccount}"
        },
        "network": {
            "nodes": [
                {
                    "ip": "127.0.0.1",
                    "rpcPort": "8545",
                    "channelPort": "20200"
                },
                {
                    "ip": "127.0.0.1",
                    "rpcPort": "8546",
                    "channelPort": "20201"
                },
                {
                    "ip": "127.0.0.1",
                    "rpcPort": "8547",
                    "channelPort": "20202"
                },
                {
                    "ip": "127.0.0.1",
                    "rpcPort": "8548",
                    "channelPort": "20203"
                }
            ],
            "authentication": {
                "key": "/home/ubuntu/anchan/nodes/127.0.0.1/sdk/node.key",
                "cert": "/home/ubuntu/anchan/nodes/127.0.0.1/sdk/node.crt",
                "ca": "/home/ubuntu/anchan/nodes/127.0.0.1/sdk/ca.crt"
            },
            "groupID": 1,
            "timeout": 100000
        }
    }{以下省略}
}

```
**执行HelloWorld合约测试**
```
npx caliper benchmark run --caliper-workspace caliper-benchmarks --caliper-benchconfig benchmarks/samples/fisco-bcos/helloworld/config.yaml  --caliper-networkconfig networks/fisco-bcos/4nodes1group/fisco-bcos.json
```
**执行Solidity版转账合约测试**
```
npx caliper benchmark run --caliper-workspace caliper-benchmarks --caliper-benchconfig benchmarks/samples/fisco-bcos/transfer/solidity/config.yaml  --caliper-networkconfig networks/fisco-bcos/4nodes1group/fisco-bcos.json
```
**执行预编译版转账合约测试**
```
npx caliper benchmark run --caliper-workspace caliper-benchmarks --caliper-benchconfig benchmarks/samples/fisco-bcos/transfer/precompiled/config.yaml  --caliper-networkconfig networks/fisco-bcos/4nodes1group/fisco-bcos.json
```