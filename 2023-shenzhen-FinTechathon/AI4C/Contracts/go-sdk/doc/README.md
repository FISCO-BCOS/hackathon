# FISCO BCOS Go-SDK手册

## 外部账户

SDK发送交易需要一个外部账户，导入go-sdk的`crypto`包，该包提供用于生成随机私钥的`GenerateKey`方法：

```go
privateKey, err := crypto.GenerateKey()
if err != nil {
    log.Fatal(err)
}
```

然后我们可以通过导入golang`crypto/ecdsa`包并使用`FromECDSA`方法将其转换为字节：

```go
privateKeyBytes := crypto.FromECDSA(privateKey)
```

我们现在可以使用go-sdk的`common/hexutil`包将它转换为十六进制字符串，该包提供了一个带有字节切片的`Encode`方法。 然后我们在十六进制编码之后删除“0x”。

```go
fmt.Println(hexutil.Encode(privateKeyBytes)[2:])
```

这就是`用于签署交易的私钥，将被视为密码，永远不应该被共享给别人`。

由于公钥是从私钥派生的，加密私钥具有一个返回公钥的`Public`方法：

```go
publicKey := privateKey.Public()
```

将其转换为十六进制的过程与我们使用转化私钥的过程类似。 我们剥离了`0x`和前2个字符`04`，它始终是EC前缀，不是必需的。

```go
publicKeyECDSA, ok := publicKey.(*ecdsa.PublicKey)
if !ok {
    log.Fatal("cannot assert type: publicKey is not of type *ecdsa.PublicKey")
}

publicKeyBytes := crypto.FromECDSAPub(publicKeyECDSA)
fmt.Println(hexutil.Encode(publicKeyBytes)[4:])
```

现在我们拥有公钥，就可以轻松生成你经常看到的公共地址。 加密包里有一个`PubkeyToAddress`方法，它接受一个ECDSA公钥，并返回公共地址。

```go
address := crypto.PubkeyToAddress(*publicKeyECDSA).Hex()
fmt.Println(address) // 0x96216849c49358B10257cb55b28eA603c874b05E
```

公共地址可以查询合约信息。

整体的代码示例为：

```go
package main

import (
    "crypto/ecdsa"
    "fmt"
    "log"
    "os"
    "github.com/ethereum/go-ethereum/crypto"
    "github.com/ethereum/go-ethereum/common/hexutil"
)

func main() {
    privateKey, err := crypto.GenerateKey()
    if err != nil {
        log.Fatal(err)
    }

    privateKeyBytes := crypto.FromECDSA(privateKey)
    fmt.Println("private key: ", hexutil.Encode(privateKeyBytes)[2:]) // privateKey in hex without "0x"

    publicKey := privateKey.Public()
    publicKeyECDSA, ok := publicKey.(*ecdsa.PublicKey)
    if !ok {
        log.Fatal("cannot assert type: publicKey is not of type *ecdsa.PublicKey")
    }

    publicKeyBytes := crypto.FromECDSAPub(publicKeyECDSA)
    fmt.Println("publick key: ", hexutil.Encode(publicKeyBytes)[4:])  // publicKey in hex without "0x"

    address := crypto.PubkeyToAddress(*publicKeyECDSA).Hex()
    fmt.Println("address: ", address)  // account address
}
```

## 环境配置

本项目使用了`go module`的[特性](https://blog.golang.org/using-go-modules)，可以在旧版本的`$GOPATH`路径之外直接运行`go`命令，如果项目仍然在`$GOPATH`路径之下，则需要显示开启`GO111MODULE`以支持该特性:

```
export GO111MODULE=on
```

如果不能访问外网，则可以设置开源代理进行依赖下载(需使用`go module`的特性)：
```bash
export GOPROXY=https://goproxy.io
```

