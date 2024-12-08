package controller

import (
	"fmt"
	"log"
	"math/big"
	"strings"

	"encoding/json"
	"io/ioutil"
	"net/http"

	"github.com/FISCO-BCOS/go-sdk/agent/model"
	"github.com/gin-gonic/gin"
	
	"github.com/FISCO-BCOS/go-sdk/abi"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	accounttable "github.com/FISCO-BCOS/go-sdk/contract/accounttable"
	"github.com/ethereum/go-ethereum/common"
)

var wl=NewWhiteList()

func AccountInsert(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0xAAC410d4093Ad00dc6995f853864701b3b71845E") // deploy contract to get address
	instance, err := accounttable.NewAccountTable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	accounttableSession := &accounttable.AccountTableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var account model.Account
	err = c.ShouldBindJSON(&account)//从json中提取账户信息

	user_id := account.UserId
	user_money := account.UserMoney
	user_name := account.UserName
	user_icon := account.UserIcon

	_, tx, receipt, err := accounttableSession.Insert(user_id, user_money, user_name, user_icon) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	accounttableABI, err := abi.JSON(strings.NewReader(accounttable.AccountTableABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// ABI 解析返回值
	ret := big.NewInt(0)
	err = accounttableABI.Unpack(&ret, "insert", common.FromHex(receipt.Output))
	if err != nil {
		fmt.Printf("parse return value failed, err: %v\n", err)
		return
	}
	record := ret.String()
	// stringSlice = append(stringSlice, "Hello")
	// size := len(stringSlice)
	// fmt.Println("The size of the slice is:", size)
	var user Identity
	user.UserID=account.UserId
	user.PublicKey=tx.Hash().Hex()
	user.Authorized=true
	wl.AddItemToWhiteList(user)
	c.JSON(200, gin.H{
		"tx":     tx.Hash().Hex(),
		"record": record,
	})
	
}
func AccountSelect(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0xAAC410d4093Ad00dc6995f853864701b3b71845E") // deploy contract to get address
	instance, err := accounttable.NewAccountTable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	accounttableSession := &accounttable.AccountTableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var account model.Account
	err = c.ShouldBindJSON(&account)

	user_id := account.UserId
	user_ids, user_moneys, user_names, user_icons, err := accounttableSession.Select(user_id) // call set API
	if err != nil {
		log.Fatal(err)
	}

	c.JSON(200, gin.H{
		"user_ids":    user_ids,
		"user_moneys": user_moneys,
		"user_names":  user_names,
		"user_icons":  user_icons,
	})
}

func AccountUpdate(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0xAAC410d4093Ad00dc6995f853864701b3b71845E") // deploy contract to get address
	instance, err := accounttable.NewAccountTable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	accounttableSession := &accounttable.AccountTableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var account model.Account
	err = c.ShouldBindJSON(&account)

	user_id := account.UserId
	user_money := account.UserMoney
	user_name := account.UserName
	user_icon := account.UserIcon

	fmt.Println(user_name)
	_, tx, receipt, err := accounttableSession.Update(user_id, user_money, user_name, user_icon) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	accounttableABI, err := abi.JSON(strings.NewReader(accounttable.AccountTableABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// ABI 解析返回值
	ret := big.NewInt(0)
	err = accounttableABI.Unpack(&ret, "update", common.FromHex(receipt.Output))
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

func AccountDelete(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0xAAC410d4093Ad00dc6995f853864701b3b71845E") // deploy contract to get address
	instance, err := accounttable.NewAccountTable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	accounttableSession := &accounttable.AccountTableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var account model.Account
	err = c.ShouldBindJSON(&account)

	user_id := account.UserId

	_, tx, receipt, err := accounttableSession.Remove(user_id) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	accounttableABI, err := abi.JSON(strings.NewReader(accounttable.AccountTableABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// ABI 解析返回值
	ret := big.NewInt(0)
	err = accounttableABI.Unpack(&ret, "remove", common.FromHex(receipt.Output))
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

func AccountTransfer(c *gin.Context) {
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
	contractAddress := common.HexToAddress("0xAAC410d4093Ad00dc6995f853864701b3b71845E") // deploy contract to get address
	instance, err := accounttable.NewAccountTable(contractAddress, client)
	if err != nil {
		log.Fatal(err)
	}
	accounttableSession := &accounttable.AccountTableSession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}
	var transfer model.Transfer
	err = c.ShouldBindJSON(&transfer)

	user_id1 := transfer.UserId1
	user_id2 := transfer.UserId2
	money := transfer.Goods
	_, tx, receipt, err := accounttableSession.Transfer(user_id1, user_id2, money) // call set API
	if err != nil {
		log.Fatal(err)
	}
	// 解析abi
	accounttableABI, err := abi.JSON(strings.NewReader(accounttable.AccountTableABI))
	if err != nil {
		fmt.Printf("parse abi failed, err: %v\n", err)
		return
	}
	// ABI 解析返回值
	ret := big.NewInt(0)
	err = accounttableABI.Unpack(&ret, "update", common.FromHex(receipt.Output))
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

type ReqResponse struct {
	Code int `json:"code"`
	Data struct {
		DeviceID int `json:"device_id"`
	} `json:"data"`
	Msg string `json:"msg"`
}

func req() {
	url := "http://127.0.0.1:10010/collection/transfer"
	contentType := "application/json"
	name := "aaaaas"
	data := `{
		"free_num": 0,
		"name": "` + name + `",
		"status" : "run"
	}`

	client := &http.Client{}
	req, err := http.NewRequest("POST", url, strings.NewReader(data))
	req.Header.Set("Content-Type", contentType)
	resp, err := client.Do(req)
	if err != nil {
		fmt.Println("post failed, err:%v\n", err)
		return
	}
	defer resp.Body.Close()
	b, err := ioutil.ReadAll(resp.Body)

	var res ReqResponse

	err = json.Unmarshal(b, &res)
	if err != nil {
		fmt.Println("get resp failed,err:%v\n", err)
		return
	}
	fmt.Println(res.Data.DeviceID)

}
