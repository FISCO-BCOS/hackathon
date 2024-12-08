package main

import (
    "fmt"
    "log"

    "github.com/FISCO-BCOS/go-sdk/client"
    "github.com/FISCO-BCOS/go-sdk/conf"
    "github.com/FISCO-BCOS/go-sdk/helloworld"
    "github.com/ethereum/go-ethereum/common"
)

func main() {
    configs, err := conf.ParseConfigFile("config.toml")
    if err != nil {
        log.Fatal(err)
    }
    config := &configs[0]
    client, err := client.Dial(config)
    if err != nil {
        log.Fatal(err)
    }

    // load the contract
    contractAddress := common.HexToAddress("0xAAC410d4093Ad00dc6995f853864701b3b71845E") // 0x481D3A1dcD72cD618Ea768b3FbF69D78B46995b0
    instance, err := helloworld.NewHelloWorld(contractAddress, client)
    if err != nil {
        log.Fatal(err)
    }

    helloworldSession := &helloworld.HelloWorldSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}

    value, err := helloworldSession.Get()    // call Get API
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println("value :", value)

    value = "Hello, FISCO BCOS"
    tx, receipt, err := helloworldSession.Set(value)  // call set API
    if err != nil {
        log.Fatal(err)
    }

    fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
    fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
}
