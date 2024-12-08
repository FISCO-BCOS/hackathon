package main

import (
    "fmt"
    "log"

    "github.com/FISCO-BCOS/go-sdk/client"
    "github.com/FISCO-BCOS/go-sdk/conf"
    "github.com/FISCO-BCOS/go-sdk/helloworld" // import helloworld
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
    address, tx, instance, err := helloworld.DeployHelloWorld(client.GetTransactOpts(), client) // deploy contract
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println("contract address: ", address.Hex())  // the address should be saved
    fmt.Println("transaction hash: ", tx.Hash().Hex())
    _ = instance
}
