package controller

import (
	"fmt"
	"log"
	"math/big"
	"strings"

	"github.com/gin-gonic/gin"

	"github.com/FISCO-BCOS/go-sdk/abi"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	kvtable "github.com/FISCO-BCOS/go-sdk/contract/kvtabletest"
	"github.com/ethereum/go-ethereum/common"
)

func Ping(c *gin.Context) {
	c.JSON(200, gin.H{
		"message": "pong",
	})
}
func Kvset(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0xE46EE87f19049d5705989f49305b136d0732a0Cf") // deploy contract to get address
	instance, err := kvtable.NewKVTableTest(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	kvtabletestSession := &kvtable.KVTableTestSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	id := "100010001001"
	item_name := "Laptop"
	item_price := big.NewInt(6000)
	_, tx, receipt, err := kvtabletestSession.Set(id, item_price, item_name) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	kvtableTestABI, err := abi.JSON(strings.NewReader(kvtable.KVTableTestABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// kvtableTestABI 解析返回值
	ret := big.NewInt(0)
	err = kvtableTestABI.Unpack(&ret, "set", common.FromHex(receipt.Output))
	if err != nil {
		fmt.Printf("parse return value failed, err: %v\n", err)
		return
	}
	record := ret.String()
	c.JSON(200, gin.H{
		"tx":     tx.Hash().Hex(),
		"record": record,
	})
}

func Kvget(c *gin.Context) {
	id := "100010001001"
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
	contractAddress := common.HexToAddress("0xE46EE87f19049d5705989f49305b136d0732a0Cf") // deploy contract to get address
	instance, err := kvtable.NewKVTableTest(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	kvtabletestSession := &kvtable.KVTableTestSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	success, item_price, item_name, err := kvtabletestSession.Get(id) // call get API
	if err != nil {
		log.Fatal(err)
	}
	if !success {
		log.Fatalf("id: %v is not found \n", id)
	}
	fmt.Printf("id: %v, item_price: %v, item_name: %v \n", id, item_price, item_name)
	c.JSON(200, gin.H{
		"id":    id,
		"name":  item_name,
		"price": item_price,
	})

}
