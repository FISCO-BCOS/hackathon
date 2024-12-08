# go-sdk

Golang SDK For FISCO BCOS 2.2.0+

[![CodeFactor](https://www.codefactor.io/repository/github/fisco-bcos/go-sdk/badge)](https://www.codefactor.io/repository/github/fisco-bcos/go-sdk)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/afbb696df3a8436a9e446d39251b2158)](https://www.codacy.com/gh/FISCO-BCOS/go-sdk?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=FISCO-BCOS/go-sdk&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/FISCO-BCOS/go-sdk/branch/master/graph/badge.svg)](https://codecov.io/gh/FISCO-BCOS/go-sdk)
[![Code Lines](https://tokei.rs/b1/github/FISCO-BCOS/go-sdk?category=code)](https://github.com/FISCO-BCOS/go-sdk)
[![version](https://img.shields.io/github/tag/FISCO-BCOS/go-sdk.svg)](https://github.com/FISCO-BCOS/go-sdk/releases/latest)


![FISCO-BCOS Go-SDK GitHub Actions](https://github.com/FISCO-BCOS/go-sdk/workflows/FISCO-BCOS%20Go-SDK%20GitHub%20Actions/badge.svg)
[![Build Status](https://travis-ci.org/FISCO-BCOS/go-sdk.svg?branch=master)](https://travis-ci.org/FISCO-BCOS/go-sdk)

____

FISCO BCOS Go语言版本的SDK，主要实现的功能有：

- [FISCO BCOS 2.0 JSON-RPC服务](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html)
- `Solidity`合约编译为Go文件
- 部署、查询、写入智能合约
- 控制台

`go-sdk`的使用可以当做是一个`package`进行使用，亦可对项目代码进行编译，直接使用**控制台**通过配置文件来进行访问FISCO BCOS。

# 环境准备

- [Golang](https://golang.org/), [版本需不低于`1.17`](https://endoflife.date/go)，本项目采用`go module`进行包管理。具体可查阅[Using Go Modules](https://blog.golang.org/using-go-modules)，[环境配置](doc/README.md#环境配置)
- [FISCO BCOS 2.2.0+](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/), **需要提前运行** FISCO BCOS 区块链平台，可参考[安装搭建](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html#fisco-bcos)

- Solidity编译器，默认[0.4.25版本](https://github.com/ethereum/solidity/releases/tag/v0.4.25)

# 配置文件说明(config.toml)

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

## Network

**注意**：go-sdk暂不支持国密SSL，请注意在使用国密模式时，将节点的config.ini中`chain.sm_crypto_channel`设置为`false`，详情[请参考这里](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/configuration.html#id10)

- Type：支持channel和rpc两种模式，其中`channel`使用ssl链接，需要提供证书。`rpc`使用http访问节点。
- CAFile：链根证书
- Cert：SDK建立SSL链接时使用的证书
- Key：SDK建立SSL链接时使用的证书对应的私钥
- Network.Connection数组，配置节点信息，可配置多个。

## Account

- KeyFile：节点签发交易时所使用的私钥，PEM格式，支持国密和非国密。

请使用[get_account.sh](https://github.com/FISCO-BCOS/console/blob/master/tools/get_account.sh)和[get_gm_account.sh](https://github.com/FISCO-BCOS/console/blob/master/tools/get_gm_account.sh)脚本生成。使用方式[参考这里](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/account.html)。

如果想使用Go-SDK代码生成，请[参考这里](doc/README.md#环境配置#外部账户)。

## Chain

- ChainID：链ID，与节点config.ini中`chain.id`保持一致。
- SMCrypto：链使用的签名算法，`ture`表示使用国密SM2，`false`表示使用普通ECDSA。

# 控制台使用

在使用控制台需要先拉取代码或下载代码，然后对配置文件`config.toml`进行更改:

1. 拉取代码并编译

```bash
git clone https://github.com/FISCO-BCOS/go-sdk.git
cd go-sdk
go mod tidy
go build cmd/console.go
```

2. 搭建FISCO BCOS 2.2以上版本节点，请[参考这里](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html)。

3. config.toml默认使用channel模式，请拷贝对应的SDK证书。

4. 最后，运行控制台查看可用指令:

```bash
./console help
```

# Package功能使用

以下的示例是通过`import`的方式来使用`go-sdk`，如引入RPC控制台库:

```go
import "github.com/FISCO-BCOS/go-sdk/client"
```

## Solidity合约编译为Go文件

在利用SDK进行项目开发时，对智能合约进行操作时需要将Solidity智能合约利用go-sdk的`abigen`工具转换为`Go`文件代码。整体上主要包含了五个流程：

- 准备需要编译的智能合约
- 配置好相应版本的`solc`编译器
- 构建go-sdk的合约编译工具`abigen`
- 编译生成go文件
- 使用生成的go文件进行合约调用

下面的内容作为一个示例进行使用介绍。

1.提供一份简单的样例智能合约`Store.sol`如下:

```solidity
pragma solidity ^0.4.25;

contract Store {
  event ItemSet(bytes32 key, bytes32 value);

  string public version;
  mapping (bytes32 => bytes32) public items;

  constructor(string _version) public {
    version = _version;
  }

  function setItem(bytes32 key, bytes32 value) external {
    items[key] = value;
    emit ItemSet(key, value);
  }
}
```

2.安装对应版本的[`solc`编译器](https://github.com/ethereum/solidity/releases/tag/v0.4.25)，目前FISCO BCOS默认的`solc`编译器版本为0.4.25。

```bash
# 如果是国密则添加-g选项
bash tools/download_solc.sh -v 0.4.25
```

3.构建`go-sdk`的代码生成工具`abigen`

```bash
# 下面指令在go-sdk目录下操作，编译生成abigen工具
go build ./cmd/abigen
```

执行命令后，检查根目录下是否存在`abigen`，并将准备的智能合约`Store.sol`放置在一个新的目录下：

```
mkdir ./store
mv Store.sol ./store
```

4.编译生成go文件，先利用`solc`将合约文件生成`abi`和`bin`文件，以前面所提供的`Store.sol`为例：

```bash
# 国密请使用 ./solc-0.4.25-gm --bin --abi -o ./store ./store/Store.sol
./solc-0.4.25 --bin --abi -o ./store ./store/Store.sol
```

`Store.sol`目录下会生成`Store.bin`和`Store.abi`。此时利用`abigen`工具将`Store.bin`和`Store.abi`转换成`Store.go`：

```bash
# 国密请使用 ./abigen --bin ./store/Store.bin --abi ./store/Store.abi --pkg store --type Store --out ./store/Store.go --smcrypto=true
./abigen --bin ./store/Store.bin --abi ./store/Store.abi --pkg store --type Store --out ./store/Store.go
```

最后store目录下面存在以下文件：

```bash
Store.abi  Store.bin  Store.go  Store.sol
```

5.调用生成的`Store.go`文件进行合约调用

至此，合约已成功转换为go文件，用户可根据此文件在项目中利用SDK进行合约操作。具体的使用可参阅下一节。

## 部署智能合约

创建main函数，调用Store合约，
```bash
touch store_main.go
```

下面的例子先部署合约，在部署过程中设置的`Store.sol`合约中有一个公开的名为`version`的全局变量，这种公开的成员将自动创建`getter`函数，然后调用`Version()`来获取version的值。

写入智能合约需要我们用私钥来对交易事务进行签名，我们创建的智能合约有一个名为`SetItem`的外部方法，它接受solidity`bytes32`类型的两个参数（key，value）。 这意味着在Go文件中需要传递一个长度为32个字节的字节数组。

```go
package main

import (
    "fmt"
    "log"

    "github.com/FISCO-BCOS/go-sdk/client"
    "github.com/FISCO-BCOS/go-sdk/conf"
    "github.com/FISCO-BCOS/go-sdk/store" // import store
)

func main(){
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		log.Fatalf("ParseConfigFile failed, err: %v", err)
	}
	client, err := client.Dial(&configs[0])
	if err != nil {
		log.Fatal(err)
	}
	input := "Store deployment 1.0"
	address, tx, instance, err := store.DeployStore(client.GetTransactOpts(), client, input)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("contract address: ", address.Hex()) // the address should be saved, will use in next example
	fmt.Println("transaction hash: ", tx.Hash().Hex())

	// load the contract
	// contractAddress := common.HexToAddress("contract address in hex String")
	// instance, err := store.NewStore(contractAddress, client)
	// if err != nil {
	// 	log.Fatal(err)
	// }

	fmt.Println("================================")
	storeSession := &store.StoreSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}

	version, err := storeSession.Version()
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("version :", version) // "Store deployment 1.0"

	// contract write interface demo
	fmt.Println("================================")
	key := [32]byte{}
	value := [32]byte{}
	copy(key[:], []byte("foo"))
	copy(value[:], []byte("bar"))

	tx, receipt, err := storeSession.SetItem(key, value)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())

	// read the result
	result, err := storeSession.Items(key)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("get item: " + string(result[:])) // "bar"
}
```
