### 链端部署文档

#### 1. 配置生产环境

生产环境为单机部署一条4节点联盟链。操作系统ubuntu18.04LTS

##### 1.1 安装依赖

`开发部署工具 build_chain.sh`脚本依赖于`openssl, curl`，根据您使用的操作系统，使用以下命令安装依赖。

```bash
sudo apt install -y openssl curl
```

##### 1.2 创建目录并下载脚本

```bash
## 创建操作目录
cd ~ && mkdir -p fisco && cd fisco

## 下载脚本
curl -#LO https://github.com/FISCO-BCOS/FISCO-BCOS/releases/download/v2.11.0/build_chain.sh && chmod u+x build_chain.sh
```

如果因为网络问题导致长时间无法下载build_chain.sh脚本，请尝试 `curl -#LO https://github.com/FISCO-BCOS/FISCO-BCOS/releases/download/v2.11.0//build_chain.sh && chmod u+x build_chain.sh`

##### 1.3 搭建单群组4节点联盟链

在fisco目录下执行下面的指令，生成一条单群组4节点的FISCO链。 请确保机器的`30300~30303，20200~20203，8545~8548`端口没有被占用。

```bash
bash build_chain.sh -l 127.0.0.1:4 -p 30300,20200,8545
##    - 其中-p选项指定起始端口，分别是p2p_port,channel_port,jsonrpc_port
##    - 出于安全性和易用性考虑，v2.3.0版本最新配置将listen_ip拆分成jsonrpc_listen_ip和channel_listen_ip，但仍保留对listen_ip的解析功能，详细请参考Fisco-Bcos官方文档
##    - 为便于开发和体验，channel_listen_ip参考配置是 `0.0.0.0` ，出于安全考虑，请根据实际业务网络情况，修改为安全的监听地址，如：内网IP或特定的外网IP
```

命令执行成功会输出`All completed`。如果执行出错，请检查`nodes/build.log`文件中的错误信息。

```cpp
Checking fisco-bcos binary...
Binary check passed.
==============================================================
Generating CA key...
==============================================================
Generating keys ...
Processing IP:127.0.0.1 Total:4 Agency:agency Groups:1
==============================================================
Generating configurations...
Processing IP:127.0.0.1 Total:4 Agency:agency Groups:1
==============================================================
[INFO] Execute the download_console.sh script in directory named by IP to get FISCO-BCOS console.
e.g.  bash /home/ubuntu/fisco/nodes/127.0.0.1/download_console.sh
==============================================================
[INFO] FISCO-BCOS Path   : bin/fisco-bcos
[INFO] Start Port        : 30300 20200 8545
[INFO] Server IP         : 127.0.0.1:4
[INFO] Output Dir        : /home/ubuntu/fisco/nodes
[INFO] CA Key Path       : /home/ubuntu/fisco/nodes/cert/ca.key
==============================================================
[INFO] All completed. Files in /home/ubuntu/fisco/nodes
```

##### 1.4 启动Fisco-Bcos联盟链

```bash
bash nodes/127.0.0.1/start_all.sh
```

启动成功会输出类似下面内容的响应。否则请使用`netstat -an | grep tcp`检查机器的`30300~30303，20200~20203，8545~8548`端口是否被占用。

```bash
try to start node0
try to start node1
try to start node2
try to start node3
 node1 start successfully
 node2 start successfully
 node0 start successfully
 node3 start successfully
```

##### 1.5 检查进程

```cpp
ps -ef | grep -v grep | grep fisco-bcos
```

正常情况会有类似下面的输出； 如果进程数不为4，则进程没有启动（一般是端口被占用导致的）

```bash
fisco       5453     1  1 17:11 pts/0    00:00:02 /home/ubuntu/fisco/nodes/127.0.0.1/node0/../fisco-bcos -c config.ini
fisco       5459     1  1 17:11 pts/0    00:00:02 /home/ubuntu/fisco/nodes/127.0.0.1/node1/../fisco-bcos -c config.ini
fisco       5464     1  1 17:11 pts/0    00:00:02 /home/ubuntu/fisco/nodes/127.0.0.1/node2/../fisco-bcos -c config.ini
fisco       5476     1  1 17:11 pts/0    00:00:02 /home/ubuntu/fisco/nodes/127.0.0.1/node3/../fisco-bcos -c config.ini
```



#### 2.拉取链端服务项目

环境前提：Golang >= 1.13.6，使用Go Module管理

此处环境建议Golang=1.18.10

##### 2.1 克隆项目Go-SDK



##### 2.2 配置链端认证文件

拷贝`fisco/nodes/127.0.0.1/sdk`下的`ca.crt`等一众配置文件到go-sdk文件夹下

```bash
cp -Rf ~/fisco/nodes/127.0.0.1/sdk/* ~/AIGC/go-sdk/
```

编辑`config.toml`

```toml
[Network]
#type rpc or channel
Type="channel"
CAFile="ca.crt"
Cert="sdk.crt"
Key="sdk.key"
[[Network.Connection]]
NodeURL="127.0.0.1:20200"
GroupID=1
# [[Network.Connection]]
# NodeURL="127.0.0.1:20200"
# GroupID=2

[Account]
# only support PEM format for now
KeyFile=".ci/0x83309d045a19c44dc3722d15a6abd472f95866ac.pem"

[Chain]
ChainID=1
SMCrypto=false
```

##### 2.3 编译合约

###### 2.3.1 安装solc编译器

该编译器用于将 sol 合约文件编译成 abi 和 bin 文件，目前FISCO BCOS提供的`solc`编译器有0.4.25/0.5.2/0.6.10

```bash
# 该指令在helloworld文件夹中执行
bash ~/AIGC/go-sdk/tools/download_solc.sh -v 0.4.25
```

###### 2.3.2 构建go-sdk的代码生成工具abigen

该工具用于将 abi 和 bin 文件转换为 go 文件

```bash
# 该指令在合约sol所在文件夹中执行，编译生成abigen工具
go build ~/AIGC/go-sdk/cmd/abigen
```

###### 2.3.3 编译合约(以collectiontable为例)

```bash
#编译合约
~/AIGC/go-sdk/solc-0.6.10 --bin --abi -o ./ ./CollectionTable.sol --overwrite
#编译Go文件
./abigen --bin ./CollectionTable.bin -
-abi ./CollectionTable.abi --pkg collectiontable --type collectiontable --out ./collectiontable.go
```

###### 2.3.4 部署合约并记录合约地址（关键）

部署三次合约,一定记录号返回值都是谁

```bash
#accounttable
root@celestial-1-0003:~/AIGC/go-sdk# cd ~/AIGC/go-sdk
root@celestial-1-0003:~/AIGC/go-sdk# go run /root/AIGC/go-sdk/contract/accounttable/cmd/main.go
contract address:  0xAAC410d4093Ad00dc6995f853864701b3b71845E
transaction hash:  0xf1188ff010c21ebd8fe384c3a6ecfcecc6fafdb63ba02e061dfe45d3f6413cbc

root@celestial-1-0003:~/AIGC/go-sdk# go run /root/AIGC/go-sdk/contract/collectiontable/cmd/main.go 
contract address:  0x0a68F060B46e0d8f969383D260c34105EA13a9dd
transaction hash:  0xc575ed13aa98d5919f032f2c2f078631cc2fcb541c2300f00c5b28b8b6059e7a

```

###### 2.3.5 修改链下控制程序地址

使用Vscode打开`/root/AIGC/go-sdk/agent/controller/AccountController.go`

修改涉及accounttable的`HexToAddress("xxxxxxxxx")`括号中参数为`"0xAAC410d4093Ad00dc6995f853864701b3b71845E"`，为`accounttable`合约地址

打开`/root/AIGC/go-sdk/agent/controller/CollectionController.go`

修改涉及collectiontable的`HexToAddress("xxxxxxxxx")`括号中参数为`"0x0a68F060B46e0d8f969383D260c34105EA13a9dd"`，为`collectiontable`合约地址

###### 2.3.6 启动

```bash
go run ./agent/main.go 
#测试系统
go run test.go
```







