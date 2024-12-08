package main

import (
	"encoding/hex"
	"fmt"
	"math/big"
	"strings"

	"github.com/ethereum/go-ethereum/common"
	"github.com/sirupsen/logrus"

	"github.com/FISCO-BCOS/go-sdk/abi"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	kvtable "github.com/FISCO-BCOS/go-sdk/examples" // import kvtabletest
)

var (
	channel         = make(chan int, 0)
	contractAddress common.Address
)

func deployContractHandler(receipt *types.Receipt, err error) {
	if err != nil {
		fmt.Printf("%v\n", err)
		return
	}
	fmt.Println("contract address: ", receipt.ContractAddress.Hex()) // the address should be saved
	contractAddress = receipt.ContractAddress
	channel <- 0
}

func invokeSetHandler(receipt *types.Receipt, err error) {
	if err != nil {
		fmt.Printf("%v\n", err)
		return
	}
	setedLines, err := parseOutput(kvtable.KVTableTestABI, "set", receipt)
	if err != nil {
		logrus.Fatalf("error when transfer string to int: %v\n", err)
	}
	fmt.Printf("seted lines: %v\n", setedLines.Int64())
	channel <- 0
}

func main() {
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		logrus.Fatal(err)
	}
	config := &configs[0]

	// deploy Asynccontract
	fmt.Println("-------------------starting deploy contract-----------------------")
	client, err := client.Dial(config)
	if err != nil {
		logrus.Fatal(err)
	}
	tx, err := kvtable.AsyncDeployKVTableTest(client.GetTransactOpts(), deployContractHandler, client)
	if err != nil {
		logrus.Fatal(err)
	}
	fmt.Println("transaction hash: ", tx.Hash().Hex())
	<-channel

	// invoke AsyncSet to insert info
	fmt.Println("\n-------------------starting invoke Set to insert info-----------------------")
	instance, err := kvtable.NewKVTableTest(contractAddress, client)
	if err != nil {
		logrus.Fatal(err)
	}
	kvtabletestSession := &kvtable.KVTableTestSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	id := "100010001001"
	item_name := "Laptop"
	item_price := big.NewInt(6000)
	tx, err = kvtabletestSession.AsyncSet(invokeSetHandler, id, item_price, item_name) // call set API
	if err != nil {
		logrus.Fatal(err)
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	<-channel

	// invoke Get to query info
	fmt.Println("\n-------------------starting invoke Get to query info-----------------------")
	bool, item_price, item_name, err := kvtabletestSession.Get(id) // call get API
	if err != nil {
		logrus.Fatal(err)
	}
	if !bool {
		logrus.Fatalf("id：%v is not found \n", id)
	}
	fmt.Printf("id: %v, item_price: %v, item_name: %v \n", id, item_price, item_name)
}

func parseOutput(abiStr, name string, receipt *types.Receipt) (*big.Int, error) {
	parsed, err := abi.JSON(strings.NewReader(abiStr))
	if err != nil {
		fmt.Printf("parse ABI failed, err: %v", err)
	}
	var ret *big.Int
	b, err := hex.DecodeString(receipt.Output[2:])
	if err != nil {
		return nil, fmt.Errorf("decode receipt.Output[2:] failed, err: %v", err)
	}
	err = parsed.Unpack(&ret, name, b)
	if err != nil {
		return nil, fmt.Errorf("unpack %v failed, err: %v", name, err)
	}
	return ret, nil
}
