package main

import (
	"encoding/hex"
	"fmt"
	"math/big"
	"strings"

	"github.com/FISCO-BCOS/go-sdk/abi"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	kvtable "github.com/FISCO-BCOS/go-sdk/examples" // import kvtabletest
	"github.com/sirupsen/logrus"
)

func main() {
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		logrus.Fatal(err)
	}
	config := &configs[0]
	client, err := client.Dial(config)
	if err != nil {
		logrus.Fatal(err)
	}

	// deploy contract
	fmt.Println("-------------------starting deploy contract-----------------------")
	address, tx, instance, err := kvtable.DeployKVTableTest(client.GetTransactOpts(), client)
	if err != nil {
		logrus.Fatal(err)
	}
	fmt.Println("contract address: ", address.Hex()) // the address should be saved
	fmt.Println("transaction hash: ", tx.Hash().Hex())
	_ = instance

	// invoke Set to insert info
	fmt.Println("\n-------------------starting invoke Set to insert info-----------------------")
	kvtabletestSession := &kvtable.KVTableTestSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	id := "100010001001"
	item_name := "Laptop"
	item_price := big.NewInt(6000)
	tx, receipt, err := kvtabletestSession.Set(id, item_price, item_name) // call set API
	if err != nil {
		logrus.Fatal(err)
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	setedLines, err := parseOutput(kvtable.KVTableTestABI, "set", receipt)
	if err != nil {
		logrus.Fatalf("error when transfer string to int: %v\n", err)
	}
	fmt.Printf("seted lines: %v\n", setedLines.Int64())

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
