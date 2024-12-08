package main

import (
    "fmt"
    "log"

    "github.com/FISCO-BCOS/go-sdk/client"
    "github.com/FISCO-BCOS/go-sdk/conf"
    kvtable "github.com/FISCO-BCOS/go-sdk/contract/kvtabletest" // import kvtabletest
)

func main(){
    configs, err := conf.ParseConfigFile("config.toml")
    if err != nil {
        log.Fatal(err)
    }
    config := &configs[0]

    client, err := client.Dial(config)
    if err != nil {
        log.Fatal(err)
    }
    address, tx, instance, err := kvtable.DeployKVTableTest(client.GetTransactOpts(), client)
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println("contract address: ", address.Hex())  // the address should be saved
    fmt.Println("transaction hash: ", tx.Hash().Hex())
    _ = instance
}
