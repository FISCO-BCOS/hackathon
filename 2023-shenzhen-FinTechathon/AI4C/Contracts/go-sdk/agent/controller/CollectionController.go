package controller

import (
	"fmt"
	"log"
	"math/big"
	"strings"

	"github.com/FISCO-BCOS/go-sdk/agent/model"
	"github.com/gin-gonic/gin"

	"github.com/FISCO-BCOS/go-sdk/abi"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	collectiontable "github.com/FISCO-BCOS/go-sdk/contract/collectiontable"
	"github.com/ethereum/go-ethereum/common"
)

func CollectionInsert(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0x32CA0eEe658Acd8d92FF68EdAf991B818b6870b2") // deploy contract to get address
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var collection model.Collection
	err = c.ShouldBindJSON(&collection)

	collection_id := collection.CollectionId
	collection_name := collection.CollectionName
	owner_id := collection.OwnerId
	certificate_time := collection.CertificateTime
	certificate_organization := collection.CertificateOrganization
	collection_semantic := collection.CollectionSemantic

	_, tx, receipt, err := collectiontableSession.Insert(collection_id, collection_name, owner_id, certificate_time, certificate_organization, collection_semantic) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	collectiontableABI, err := abi.JSON(strings.NewReader(collectiontable.CollectiontableABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// ABI 解析返回值
	ret := big.NewInt(0)
	err = collectiontableABI.Unpack(&ret, "insert", common.FromHex(receipt.Output))
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
func CollectionSelect(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0x32CA0eEe658Acd8d92FF68EdAf991B818b6870b2") // deploy contract to get address
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var collection model.Collection
	err = c.ShouldBindJSON(&collection)

	collection_id := collection.CollectionId
	collection_id, collection_name, owner_id, certificate_time, certificate_organization, collection_semantic, err := collectiontableSession.Select(collection_id) // call set API
	fmt.Println(collection_semantic)
	if err != nil {
		log.Fatal(err)
	}

	c.JSON(200, gin.H{
		"collection_id":            collection_id,
		"collection_name":          collection_name,
		"owner_id":                 owner_id,
		"certificate_time":         certificate_time,
		"certificate_organization": certificate_organization,
		"collection_semantic":      collection_semantic,
	})
}

func CollectionUpdate(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0x32CA0eEe658Acd8d92FF68EdAf991B818b6870b2") // deploy contract to get address
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var collection model.Collection
	err = c.ShouldBindJSON(&collection)

	collection_id := collection.CollectionId
	collection_name := collection.CollectionName
	owner_id := collection.OwnerId
	certificate_time := collection.CertificateTime
	certificate_organization := collection.CertificateOrganization

	collection_semantic := collection.CollectionSemantic
	fmt.Println(collection_semantic)
	_, tx, receipt, err := collectiontableSession.Update(collection_id, collection_name, owner_id, certificate_time, certificate_organization, collection_semantic) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	collectiontableABI, err := abi.JSON(strings.NewReader(collectiontable.CollectiontableABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// ABI 解析返回值
	ret := big.NewInt(0)
	err = collectiontableABI.Unpack(&ret, "update", common.FromHex(receipt.Output))
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

func CollectionDelete(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0x32CA0eEe658Acd8d92FF68EdAf991B818b6870b2") // deploy contract to get address
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var collection model.Collection
	err = c.ShouldBindJSON(&collection)

	collection_id := collection.CollectionId

	_, tx, receipt, err := collectiontableSession.Remove(collection_id) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	collectiontableABI, err := abi.JSON(strings.NewReader(collectiontable.CollectiontableABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// ABI 解析返回值
	ret := big.NewInt(0)
	err = collectiontableABI.Unpack(&ret, "remove", common.FromHex(receipt.Output))
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

func CollectionTransfer(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0x32CA0eEe658Acd8d92FF68EdAf991B818b6870b2") // deploy contract to get address
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var transfer model.Transfer
	err = c.ShouldBindJSON(&transfer)

	user_id1 := transfer.UserId1
	user_id2 := transfer.UserId2
	collection_id := transfer.CollectionId
	money := transfer.Goods
	_, tx, receipt, err := collectiontableSession.Transfer(user_id1, user_id2, money, collection_id) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	collectiontableABI, err := abi.JSON(strings.NewReader(collectiontable.CollectiontableABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// ABI 解析返回值
	ret := big.NewInt(0)
	err = collectiontableABI.Unpack(&ret, "update", common.FromHex(receipt.Output))
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

func CollectionGIncome(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0x32CA0eEe658Acd8d92FF68EdAf991B818b6870b2") // deploy contract to get address
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var transfer model.Transfer
	err = c.ShouldBindJSON(&transfer)

	user_id1 := transfer.UserId1
	user_id2 := transfer.UserId2
	money := transfer.Goods
	_, tx, receipt, err := collectiontableSession.Gincome(user_id1, user_id2, money) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	collectiontableABI, err := abi.JSON(strings.NewReader(collectiontable.CollectiontableABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// ABI 解析返回值
	ret := big.NewInt(0)
	err = collectiontableABI.Unpack(&ret, "update", common.FromHex(receipt.Output))
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
