# Python SDK

![](./images/FISCO_BCOS_Logo.svg)


[![Build Status](https://travis-ci.org/FISCO-BCOS/python-sdk.svg?branch=master)](https://travis-ci.org/FISCO-BCOS/python-sdk)
[![CodeFactor](https://www.codefactor.io/repository/github/fisco-bcos/python-sdk/badge)](https://www.codefactor.io/repository/github/fisco-bcos/python-sdk)
[![GitHub license](https://img.shields.io/github/license/FISCO-BCOS/python-sdk.svg)](https://github.com/FISCO-BCOS/python-sdk/blob/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/FISCO-BCOS/python-sdk.svg)](https://github.com/FISCO-BCOS/python-sdk/issues)
--- 

Python SDK为[FISCO BCOS](https://github.com/FISCO-BCOS/FISCO-BCOS/tree/master)提供Python API，使用FISCO BCOS Python SDK可以简单快捷的基于FISCO-BCOS进行区块链应用开发。

2022.09版本同时支持FISCO BCOS 2.x / 3.x。与3.x相关的技术说明参见 [FISCO BCOS 3.x 开发介绍](./README_bcos3.md)

**Python SDK定位为开发版本，持续迭代，供使用Python语言开发FISCO BCOS应用的开发者参考。企业级应用推荐使用 Java SDK**



如需将Python SDK用于正式环境，请认真阅读和理解代码，掌握相关知识点，按自己的需求进行二次开发并**严谨测试**后上线。

如有任何问题，欢迎到社区提问交流，或修改扩展提交pr，共建项目

## 关键特性

- 支持FISCO BCOS 3.x接口: [JSON-RPC](https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/api.html)。对于FISCO BCOS3.x，客户端采用Python包装C语言开发的底层SDK库，由SDK库负责网络协议封装和安全通信细节。
- 支持FISCO BCOS 2.x接口: [JSON-RPC](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html)。对于FISCO BCOS2.x，客户端基于[Channel协议](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/design/protocol_description.html#channelmessage)与FISCO BCOS进行通信，保证节点与SDK安全加密通信的同时，可接收节点推送的消息。
- 支持交易解析功能：包括交易输入、交易输出、Event Log等ABI数据的拼装和解析。
- 支持合约编译，将`sol`合约编译成`abi`和`bin`文件。
- 支持基于keystore的账户管理。支持从pem文件加载ECDSA算法的私钥，以便和其他私钥管理模块互通。
- 支持本地的合约部署历史查询 (不支持链上所有部署的合约查询)。
- 支持国密(SM2,SM3,SM4算法)。对于FISCO BCOS2.x如需支持国密SSL通信需要独立编译组件，参见 [cython_tassl_wrap的README](./cython_tassl_wrap)。对于FISCO BCOS 3.x,国密特性已经由底层库封装，参见FISCO BCOS 3.x 配置和库文件下载。
- 支持event回调监听
- 支持liquid智能合约。需要采用[liquid开发环境](https://liquid-doc.readthedocs.io/)编译wasm合约。使用python-sdk部署和调用liquid合约时，合约名带上.wasm后缀，以和sol合约区别。
- 控制台支持Struct参数，数组等复杂数据结构，SDK调用方法参见[tests/teststructclient.py](tests/teststructclient.py)

## 部署Python SDK

### 环境要求
- Python环境：python 3.6.3, 3.7.x
- FISCO BCOS节点：请参考

[FISCO BCOS 2.x节点安装](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html#fisco-bcos)搭建
[FISCO BCOS 3.x节点安装](https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/quick_start/air_installation.html)搭建


### 依赖软件

- **Ubuntu**: `sudo apt install -y zlib1g-dev libffi6 libffi-dev wget git`
- **CentOS**：`sudo yum install -y zlib-devel libffi-devel wget git`
- **MacOs**: `brew install wget npm git`

### 初始化环境(若python环境符合要求，可跳过)

#### **Linux环境初始化**

> 拉取源代码
```bash
git clone https://github.com/FISCO-BCOS/python-sdk
```

> 配置环境(安装pyenv和python，若python版本>=3.6.3可跳过本步)
```bash
# 获取python版本
python --version

## --------若python版本小于3.6.3，执行下面流程--------------------------------------
# 判断python版本，并为不符合条件的python环境安装python 3.7.3的虚拟环境，命名为python-sdk
# 若python环境符合要求，可以跳过此步
# 若脚本执行出错，请检查是否参考[依赖软件]说明安装了依赖
# 提示：安装python-3.7.3可能耗时比较久
cd python-sdk && bash init_env.sh -p

## --------若通过bash init_env.sh -p安装了python-sdk虚拟环境，执行下面流程-------------
# 激活python-sdk虚拟环境
source ~/.bashrc && pyenv activate python-sdk && pip install --upgrade pip
```

#### **Windows环境初始化**

在Windows运行Python SDK，需要按照以下步骤安装依赖软件并配置合约编译器：

**安装依赖软件**

- 直接安装[Python-3.7.x](https://www.python.org/downloads/release/python-373/)和[git](https://git-scm.com/download/win)软件  
> python环境变量配置可参考[这里](https://jingyan.baidu.com/article/b0b63dbff271e24a4830708d.html)

- [Visual C++ 14.0库](https://visualstudio.microsoft.com/downloads)
> (注：Microsoft Visual C++ 14.0 is required. Get it with "Microsoft Visual C++ Build Tools"解决方法: https://visualstudio.microsoft.com/downloads （注意选择vs 2005即14.0版）或 https://pan.baidu.com/s/1ZmDUGZjZNgFJ8D14zBu9og 提取码: zrby)

- 下载Windows版本solc, 点击[这里](https://github.com/ethereum/solidity/releases/download/v0.4.25/solidity-windows.zip)下载
> solc编译器下载成功后，解压，将其中的 solc.exe 文件复制 ${python-sdk}\bin 目录下。若 python-sdk 路为 D:\\open-source\\python-sdk, 则 solc.exe 文件复制路径为D:\\open-source\\python-sdk\\bin\\solc.exe 

**拉取源代码**

打开 git，在任意目录执行如下命令
```bash
git clone https://github.com/FISCO-BCOS/python-sdk
```

**配置solc编译器路径**
```bash
# 修改client_config.py.template: 
# 配置solc编译器路径，若solc存放路径为D:\\open-source\\python-sdk\\bin\\solc.exe，则solc_path配置如下：
solc_path = "D:\\open-source\\python-sdk\\bin\\solc.exe"

# 将client_config.py.template拷贝到client_config.py
```

### **安装Python SDK依赖**

```bash
cd python-sdk 
pip install -r requirements.txt
```

> **若因网络原因，安装依赖失败，可使用清华的pip源下载，安装命令如下：**

```bash
pip install -i https://pypi.tuna.tsinghua.edu.cn/simple -r requirements.txt
```

### 初始化配置(Windows环境可跳过)

```bash
# 该脚本执行操作如下：
# 1. 拷贝client_config.py.template -> client_config.py
# 2. 安装solc编译器
bash init_env.sh -i
```

> **若MacOS环境solc安装较慢，可在python-sdk目录下执行如下命令安装solcjs**，python-sdk自动加载nodejs编译器：

```bash
# 安装编译器
npm install solc@v0.4.25
```

> 若没有执行以上初始化步骤，需要将`contracts/`目录下的`sol`代码手动编译成`bin`和`abi`文件并放置于`contracts`目录，才可以部署和调用相应合约。合约编译可以使用[remix](https://remix.ethereum.org)


## 国密支持
 -  支持国密版本的非对称加密、签名验签(SM2), HASH算法(SM3),对称加解密(SM4)
 -  国密版本在使用上和非国密版本基本一致，主要是配置差异。
 -  国密版本sdk同一套代码可以连接国密和非国密的节点，需要根据不同的节点配置相应的IP端口和证书
 -  因为当前版本的实现里，账户文件格式有差异，所以国密的账户文件和ECDSA的账户文件采用不同的配置
 -  FISCO BCOS 2.x国密SSL目前需要手动编译，配置，方法参见 [cython_tassl_wrap的README](./cython_tassl_wrap)
 -  FISCO BCOS 3.x的国密同样由底层库支持，lib库下载已经在CDN下载提供，参见[相关连接](https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/c_sdk/dylibs.html)

连接国密节点时，有以下相关的配置项需要修改和确认，IP端口也需要确认是指向国密版本节点
```bash
crypto_type = "GM" 	#密码算法选择: 大小写不敏感："GM" 标识国密, "ECDSA" 或其他是椭圆曲线默认实现。
ssl_type = "GM" # 和节点tls通信方式，如设为gm，则使用国密证书认证和加密
gm_account_keyfile = "gm_account.json"  #国密账号的存储文件，可以加密存储,如果留空则不加载
gm_account_password = "123456" 		#如果不设密码，置为None或""则不加密
gm_solc_path = "./bin/solc/v0.4.25/solc-gm" #合约编译器配置，通过执行bash init_env.sh -i命令下载
以及5个证书配置
```

## FISCO BCOS 3.x 配置和库文件下载 (连接FISCO BCOS 2.x无需关注这一段)

FISCO BCOS 3.x相关配置也在client_config.py文件里，大部分和2.x的一致。

只需要关注以下几个字段：
```bash
    # FISCO BCOS3.0的配置段，如连接FISCO BCOS2.0版本，无需关心此段
    # FISCO BCOS3.0 c底层sdk的配置，都在bcos3_config_file里，无需配置在此文件
    bcos3_lib_path ="./bcos3sdklib"
    bcos3_config_file ="./bcos3sdklib/bcos3_sdk_config.ini"
    group = "group0"
```

由于FISCO BCOS 3.x提供了SDK的C语言库，诸多细节封装在库里，并引入了独立的配置文件，即上面配置里的bcos3_config_file字段。

**即对于FISCO BCOS 3.x版本，要修改两个配置文件:** 

1) client_config.py,可参考client_config.py.template创建后缀为ini的配置文件,字段说明参见文件里的注释

2) bcos3_sdk_config.ini,可参考bcos3sdklib/bcos3_sdk_config.ini.template创建后缀为ini的配置文件，并将其路径配置到client_config.py** 

建议详细查看bcos3_sdk_config.ini,[字段的说明参见此连接](https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/c_sdk/config.html)

bcos3_sdk_config.ini配置文件可以和库文件等一起放在 bcos3sdklib目录(可参照上方的配置项修改) 。节点证书、SDK证书等文件路径、节点IP端口等信息在该文件里配置。

节点证书，SDK证书等文件，到FISCO BCOS3.x的安装目录下的sdk子目录里获取，本地存放的路径与bcos3_sdk_config.ini里配置的保持一致

**clog.ini** 默认放在当前目录下 ，sdk的C语言库打日志使用此配置


**重要:**

最新版本的C语言的SDK库文件可到[文件下载连接](https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/c_sdk/dylibs.html),下载相应操作系统的库文件。

如windows平台上的bcos-c-sdk.dll,linux平台上的libbcos-c-sdk.so等。

建议下载后放到当前目录的 ./bcos3sdklib 目录下（参照client_config.py里的bcos3_lib_path字段，可根据自己的部署环境实际路径修改），供python代码调用。

如库名发生修改，或者带上了版本号等，有可能导致文件指向差异，可以到bcos3sdk/bcos3sdk_wrap.py里，修改libname
```bash
class NativeBcos3sdk:
    libname = "bcos-c-sdk"
```
总之，一定要能加载C语言的SDK库，才能连接FISCO BCOS3.0的节点

## FISCO BCOS 2.x 配置Channel通信协议 （连接FISCO BCOS 3.x 无需关注这一段）

Python SDK支持使用[Channel协议](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/design/protocol_description.html#channelmessage-v1)与FISCO BCOS节点通信，通过SSL加密通信保障SDK与节点通信的机密性。

设SDK连接的节点部署在目录`~/fisco/nodes/127.0.0.1`目录下，则通过如下步骤使用Channel协议：

**配置Channel信息**

在节点目录下的 config.ini 文件中获取 channel_listen_port, 这里为20200  
```bash
[rpc]
    listen_ip=0.0.0.0
    channel_listen_port=20200
    jsonrpc_listen_port=8545
```
    
切换到python-sdk目录，修改 client_config.py 文件中`channel_host`为实际的IP，`channel_port`为上步获取的`channel_listen_port`：

```bash
channel_host = "127.0.0.1"
channel_port = 20200
```

**配置证书**

```bash
# 若节点与python-sdk位于不同机器，请将节点sdk目录下所有相关文件拷贝到bin目录
# 若节点与sdk位于相同机器，直接拷贝节点证书到SDK配置目录
cp ~/fisco/nodes/127.0.0.1/sdk/* bin/
```

**配置证书路径**

  - `client_config.py`的`channel_node_cert`和`channel_node_key`选项分别用于配置SDK证书和私钥
  - `release-2.1.0`版本开始，SDK证书和私钥更新为`sdk.crt`和`sdk.key`，配置证书路径前，请先检查上步拷贝的证书名和私钥名，并将`channel_node_cert`配置为SDK证书路径，将`channel_node_key`配置为SDK私钥路径

检查从节点拷贝的sdk证书路径，若sdk证书和私钥路径分别为`bin/sdk.crt`和`bin/sdk.key`，则`client_config.py`中相关配置项如下：

```bash
channel_node_cert = "bin/sdk.crt"  # 采用channel协议时，需要设置sdk证书,如采用rpc协议通信，这里可以留空
channel_node_key = "bin/sdk.key"   # 采用channel协议时，需要设置sdk私钥,如采用rpc协议通信，这里可以留空
```

若sdk证书和私钥路径分别为`bin/node.crt`和`bin/node.key`，则`client_config.py`中相关配置项如下:
```bash
channel_node_cert = "bin/node.crt"  # 采用channel协议时，需要设置sdk证书,如采用rpc协议通信，这里可以留空
channel_node_key = "bin/node.key"   # 采用channel协议时，需要设置sdk私钥,如采用rpc协议通信，这里可以留空
```


**使用Channel协议访问节点**

```bash
# 获取FISCO BCOS节点版本号
./console.py getNodeVersion
```

**Event事件回调**
 -  针对已经部署在链上某个地址的合约，先注册要监听的事件，当合约被交易调用，且生成事件时，节点可以向客户端推送相应的事件
 -  事件定义如有indexed类型的输入，可以指定监听某个特定值作为过滤，如事件定义为 on_set(string name,int indexed value),可以增加一个针对value的topic监听，只监听value=5的事件
 -  具体实现参考demo_event_callback.py,使用的命令行为：
```bash
params: contractname address event_name indexed
        1. contractname :       合约的文件名,不需要带sol后缀,默认在当前目录的contracts目录下
        2. address :    十六进制的合约地址,或者可以为:last,表示采用bin/contract.ini里的记录
        3. event_name : 可选,如不设置监听所有事件
        4. indexed :    可选,根据event定义里的indexed字段,作为过滤条件)

        eg: for contract sample [contracts/HelloEvent.sol], use cmdline:

        python demo_event_callback.py HelloEvent last
        --listen all event at all indexed ：

        python demo_event_callback.py HelloEvent last on_set
        --listen event on_set(string newname) （no indexed）：

        python demo_event_callback.py HelloEvent last on_number 5
        --listen event on_number(string name,int indexed age), age ONLY  5 ：

```


## SDK使用示例

注:FISCO BCOS 3.0的控制台文件是** console3.py**,使用方法基本同FISCO BCOS 2.0，主要是查询类接口有数量上的区别

具体使用信息，可输入 **python console3.py usage** 查看

**查看SDK使用方法**

* 注：以下示例不区分2.x和3.x的控制台，如要使用3.x的控制台，把console.py换成console3.py即可。
* 屏幕上的少量输入输出差异和版本有关，不做详细分解

> **windows环境下执行console.py请使用`.\console.py`或者`python console.py`**

```bash
# 查看SDK使用方法
./console.py usage

# 获取区块高度
./console.py getBlockNumber
```

**部署HelloWorld合约**
```bash
$ ./console.py deploy HelloWorld  

INFO >> user input : ['deploy', 'HelloWorld']

backup [contracts/HelloWorld.abi] to [contracts/HelloWorld.abi.20190807102912]
backup [contracts/HelloWorld.bin] to [contracts/HelloWorld.bin.20190807102912]
INFO >> compile with solc compiler
deploy result  for [HelloWorld] is:
 {
    "blockHash": "0x3912605dde5f7358fee40a85a8b97ba6493848eae7766a8c317beecafb2e279d",
    "blockNumber": "0x1",
    "contractAddress": "0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce",
    "from": "0x95198b93705e394a916579e048c8a32ddfb900f7",
    "gasUsed": "0x44ab3",
    "input": "0x6080604052...省略若干行...c6f2c20576f726c642100000000000000000000000000",
    "logs": [],
    "logsBloom": "0x000...省略若干行...0000",
    "output": "0x",
    "status": "0x0",
    "to": "0x0000000000000000000000000000000000000000",
    "transactionHash": "0xb291e9ca38b53c897340256b851764fa68a86f2a53cb14b2ecdcc332e850bb91",
    "transactionIndex": "0x0"
}
on block : 1,address: 0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce 
address save to file:  bin/contract.ini
```

**调用HelloWorld合约**

```bash
# 合约地址：0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce
# 调用接口：get
$./console.py  call HelloWorld 0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce get 

INFO >> user input : ['call', 'HelloWorld', '0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce', 'get']

INFO >> call HelloWorld , address: 0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce, func: get, args:[]
INFO >> call result: ('Hello, World!',)

# 合约名：HelloWorld
# 合约地址：0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce
# 调用接口：set
# 参数："Hello, FISCO"
$ ./console.py sendtx HelloWorld 0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce set "Hello, FISCO"

INFO >> user input : ['sendtx', 'HelloWorld', '0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce', 'set', 'Hello, FISCO']

INFO >> sendtx HelloWorld , address: 0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce, func: set, args:['Hello, FISCO']

INFO >>  receipt logs : 
INFO >> transaction hash :  0xc20cbc6b0f28ad8fe1c560c8ce28c0e7eb7719a4a618a81604ac87ac46cc60f0
tx input data detail:
 {'name': 'set', 'args': ('Hello, FISCO',), 'signature': 'set(string)'}
receipt output : ()

# 调用get接口获取更新后字符串
$./console.py call HelloWorld 0x2d1c577e41809453c50e7e5c3f57d06f3cdd90ce get

INFO >> user input : ['call', 'HelloWorld', '0x42883e01ac97a3a5ef8a70c290abe0f67913964e', 'get']

INFO >> call HelloWorld , address: 0x42883e01ac97a3a5ef8a70c290abe0f67913964e, func: get, args:[]
INFO >> call result:  'Hello, FISCO!'
```

## 控制台输入复杂数据类型概要说明

**数组**

合约数组如uint256[3] nums，那么在python层面，其参数构造可以是 [1,2,3],同理，字符串数组对应['a','b','c']

在控制台输入时，数组参数需要加上中括号，比如[1, 2, 3]，数组中是字符串或字节类型，加双引号或单引号，例如[“alice”, ”bob”]，注意数组参数中不要有空格；布尔类型为true或者false。

**结构体**

合约结构体如
```
    struct User {
        string name;
        uint256 age;
     }
```
对应python的tuple类型，如 ('alice',23)

如果是结构体数组 User[] _users, 则对应tuple数组如[('alice',23),('bob',28)]

在控制台输入时，按以上格式输入即可。举例
```
单个结构体参数
python console.py sendtx TestStruct latest addUser ('alice',23)

两个参数，第二个参数是结构体
python console.py sendtx TestStruct latest addbyname alice ('alice',23)

结构体数组参数
python console.py sendtx TestStruct latest addUsers [('alice',23),('bob',28)]

查询，返回的是结构体
python console.py call TestStruct latest getUser alice
```

在控制台输入时，不支持复杂的嵌套(数组套结构体套数组等)，也不完整支持复杂转义字符，仅供一般体验使用。

开发时，可参见[tests/teststructclient.py](tests/teststructclient.py)里的实现,复杂的数据结构，按abi接口定义结合python的数据结构（array,tuple等）进行组合构造。

## 开启命令行自动补全

Python SDK引入[argcomplete](https://argcomplete.readthedocs.io/en/latest/)支持命令行补全，运行如下命令开启此功能(**bashrc仅需设置一次，设置之后每次登陆自动生效**)，**目前仅支持bash，不支持zsh**：

```bash
echo "eval \"\$(register-python-argcomplete ./console.py)\"" >> ~/.bashrc
source ~/.bashrc
```

## 文档

[**中文**](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/python_sdk/index.html)

## 贡献代码

- 我们欢迎并非常感谢您的贡献，请参阅[代码贡献流程](https://mp.weixin.qq.com/s/hEn2rxqnqp0dF6OKH6Ua-A
)。
- 如项目对您有帮助，欢迎star支持！


## 加入社区

**FISCO BCOS开源社区**是国内活跃的开源社区，社区长期为机构和个人开发者提供各类支持与帮助。已有来自各行业的数千名技术爱好者在研究和使用FISCO BCOS。如您对FISCO BCOS开源技术及应用感兴趣，欢迎加入社区获得更多支持与帮助。

![](https://media.githubusercontent.com/media/FISCO-BCOS/LargeFiles/master/images/QR_image.png)

## License
![license](https://img.shields.io/github/license/FISCO-BCOS/python-sdk.svg)

Python SDK的开源协议为[MIT License](https://opensource.org/licenses/MIT). 详情参考[LICENSE](./LICENSE)。