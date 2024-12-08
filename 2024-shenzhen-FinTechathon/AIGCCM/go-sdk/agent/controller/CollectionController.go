package controller
// load the contract"0x904ed579402eD8BBb80ee7F0eb02e8226d78a70f"
import (
	"fmt"
	"log"
	"math/big"
	"strings"
	"crypto/ecdsa"
	"crypto/elliptic"
	"crypto/rand"
	"strconv"
	"reflect"
	

	"github.com/FISCO-BCOS/go-sdk/agent/model"
	"github.com/gin-gonic/gin"

	"github.com/FISCO-BCOS/go-sdk/abi"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	collectiontable "github.com/FISCO-BCOS/go-sdk/contract/collectiontable"
	"github.com/ethereum/go-ethereum/common"
)

var Matrixmaps = map[string][][]float64{}
var Simhashmaps = map[string]string{}
var ctStruct = NewCopyrightTree(513*513,10,1)

var PrivateKey *ecdsa.PrivateKey
var PublicKey *ecdsa.PublicKey
var cnt = 114513
func getid()string{
	cnt++
	return strconv.Itoa(cnt)
}
func PriKeysinit() {
	// 在init函数中生成密钥对，这样它们在程序启动时就被初始化
	PrivateKey, _ = ecdsa.GenerateKey(elliptic.P256(), rand.Reader)
	PublicKey = &PrivateKey.PublicKey
}
func CollectionInsert(c *gin.Context) {
	fmt.Println("CollectionInsert")
	fmt.Println("Privatekey:",PrivateKey)
	configs, err := conf.ParseConfigFile("certinf1/config1.toml")
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
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var collection model.Collection
	err = c.ShouldBindJSON(&collection)

	collection_id := getid()
	collection_name := collection.CollectionName
	collection_matrix := collection.CollectionMatrix
	collection_make := collection.CollectionMake
	collection_record := collection.CollectionRecord
	owner_id := collection.OwnerId
	certificate_time := collection.CertificateTime
	certificate_organization := collection.CertificateOrganization
	collection_semantic := collection.CollectionSemantic

	node:=&Node{
		Matrix:collection_matrix,
		Make:collection_make,
		Record:collection_record,
		Owner:owner_id,
		Style:"picture",
	}

	node.Hash = GetSimhash(node)
	fmt.Println("node.Hash:",node.Hash)
	// fmt.Println(node)
	//collection_id := BinaryStringtoHex(node.Hash)
	fmt.Println("collection_id:",collection_id)
	//fmt.Println("node.Hash:",HexToBinaryString(collection_id))
	ctStruct.InsertIndexNode(node)
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
	Matrixmaps[collection_id] = collection_matrix
	Simhashmaps[collection_id] = node.Hash
	fmt.Println("owner_id:",owner_id)
	c.JSON(200, gin.H{
		"tx":     tx.Hash().Hex(),
		"record": record,
		"collection_id":  collection_id,
	})
}


func CollectionSelect(c *gin.Context) {
	configs, err := conf.ParseConfigFile("certinf2/config2.toml")
	if err != nil {
		log.Fatal(err)
	}
	config := &configs[0]
	fmt.Println(1)
	client, err := client.Dial(config)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(2)
	// load the contract
	contractAddress := common.HexToAddress("0xE46EE87f19049d5705989f49305b136d0732a0Cf") // deploy contract to get address
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}

	var collection model.Collection
	err = c.ShouldBindJSON(&collection)

	collection_id := collection.CollectionId
	collection_matrix := Matrixmaps[collection_id]

	node := &Node{
		Hash:Simhashmaps[collection_id],
		Matrix: collection_matrix,
	}

	collection_id, collection_name, owner_id, certificate_time, certificate_organization, collection_semantic, err := collectiontableSession.Select(collection_id) // call set API
	Qnode:=ctStruct.FindNodeByHash(node);
	// fmt.Println("cout node")
	// fmt.Println(Qnode)
	// fmt.Println(collection_semantic)
	if err != nil||Qnode==nil||Qnode.Hash!=Simhashmaps[collection_id] {
		c.JSON(200, gin.H{
			"Verdict: ": "Not Found",
		})
		log.Fatal(err)
		return

	}

	fmt.Println(reflect.TypeOf(Matrixmaps[collection_id]).String())
	fmt.Println(owner_id)

	c.JSON(200, gin.H{
		"collection_id":            collection_id,
		"collection_name":          collection_name,
		"owner_id":                 owner_id,
		"certificate_time":         certificate_time,
		"certificate_organization": certificate_organization,
		"collection_semantic":      collection_semantic,
		"collection_matrix":        Matrixmaps[collection_id],
	})
}

func CollectionUpdate(c *gin.Context) {
	configs, err := conf.ParseConfigFile("certinf1/config1.toml")
	if err != nil {
		log.Fatal(err)
	}
	config := &configs[0]
	client, err := client.Dial(config)
	if err != nil {
		log.Fatal(err)
	}
	
	contractAddress := common.HexToAddress("0xE46EE87f19049d5705989f49305b136d0732a0Cf") // deploy contract to get address
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var collection model.Collection
	err = c.ShouldBindJSON(&collection)

	collection_id := collection.CollectionId
	collection_name := collection.CollectionName
	//collection_matrix := collection.CollectionMatrix
	collection_make := collection.CollectionMake
	collection_record := collection.CollectionRecord
	owner_id := collection.OwnerId
	certificate_time := collection.CertificateTime
	certificate_organization := collection.CertificateOrganization
	collection_semantic := collection.CollectionSemantic

	node:=&Node{
		Hash:Simhashmaps[collection_id],
		Matrix:Matrixmaps[collection_id],
	}
	Qnode:=ctStruct.FindNodeByHash(node);
	if(Qnode==nil||Qnode.Hash!=Simhashmaps[collection_id]){
		c.JSON(200, gin.H{
			"Verdict: ": "Not Found",
		})
		return
	}
	ctStruct.DeleteNode(Qnode)

	cNode:=&Node{
		Matrix:Matrixmaps[collection_id],
		Make:collection_make,
		Record:collection_record,
		Owner:owner_id,
		Style:"picture",
	}
	cNode.Hash=GetSimhash(cNode)
	ctStruct.InsertIndexNode(cNode)
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
	configs, err := conf.ParseConfigFile("certinf1/config1.toml")
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
	instance, err := collectiontable.NewCollectiontable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	collectiontableSession := &collectiontable.CollectiontableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var collection model.Collection
	err = c.ShouldBindJSON(&collection)

	collection_id := collection.CollectionId

	node:=&Node{
		Hash:Simhashmaps[collection_id],
		Matrix:Matrixmaps[collection_id],
	}
	Qnode:=ctStruct.FindNodeByHash(node);
	if(Qnode==nil||Qnode.Hash!=Simhashmaps[collection_id]){
		c.JSON(200, gin.H{
			"Verdict: ": "Not Found",
		})
		return
	}
	ctStruct.DeleteNode(Qnode)
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
	Matrixmaps[collection_id] = nil
	Simhashmaps[collection_id] = ""
	return
}

func CollectionTransfer(c *gin.Context) {
	configs, err := conf.ParseConfigFile("certinf1/config1.toml")
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

	fmt.Println(user_id1)
	fmt.Println(user_id2)
	fmt.Println(collection_id)
	fmt.Println(money)

	node:=&Node{
		Hash:Simhashmaps[collection_id],
		Matrix:Matrixmaps[collection_id],
	}
	Qnode:=ctStruct.FindNodeByHash(node);
	//fmt.Println(Qnode)
	if(Qnode==nil||Qnode.Hash!=Simhashmaps[collection_id]){
		c.JSON(200, gin.H{
			"Verdict: ": "Not Found",
		})
		return
	}
	ctStruct.DeleteNode(Qnode)
	Qnode.Owner=user_id2
	newNodes:=&Node{
		Matrix:Qnode.Matrix,
		Make:Qnode.Make,
		Record:Qnode.Record,
		Owner:Qnode.Owner,
		Style:Qnode.Style,
	}
	newNodes.Hash=GetSimhash(newNodes)
	//fmt.Println(newNodes)
	ctStruct.InsertIndexNode(newNodes)


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
	fmt.Println(record)
	fmt.Println(tx.Hash().Hex())
	c.JSON(200, gin.H{
		"tx":     tx.Hash().Hex(),
		"record": record,
	})
}

