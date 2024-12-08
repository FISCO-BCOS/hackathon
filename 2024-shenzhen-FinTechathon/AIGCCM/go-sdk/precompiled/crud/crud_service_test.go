package crud

import (
	"encoding/hex"
	"math/big"
	"os"
	"strconv"
	"testing"
	"time"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/FISCO-BCOS/go-sdk/precompiled"
)

const (
	tableName         = "t_test"
	tableNameForAsync = "t_test_async"
	key               = "name"
	valueFields       = "item_id, item_name"
	timeout           = 1 * time.Second
)

var (
	service *Service
	channel = make(chan int)
)

func getClient(t *testing.T) *client.Client {
	privateKey, _ := hex.DecodeString("145e247e170ba3afd6ae97e88f00dbc976c2345d511b0f6713355d19d8b80b58")
	config := &conf.Config{IsHTTP: true, ChainID: 1, IsSMCrypto: false, GroupID: 1,
		PrivateKey: privateKey, NodeURL: "http://localhost:8545"}
	c, err := client.Dial(config)
	if err != nil {
		t.Fatalf("Dial to %s failed of %v", config.NodeURL, err)
	}
	return c
}

func getService(t *testing.T) {
	c := getClient(t)
	newService, err := NewCRUDService(c)
	if err != nil {
		t.Fatalf("init CrudService failed: %+v", err)
	}
	service = newService
}

func TestMain(m *testing.M) {
	getService(&testing.T{})
	exitCode := m.Run()
	os.Exit(exitCode)
}

func TestCreateTable(t *testing.T) {
	result, err := service.CreateTable(tableName, key, valueFields)
	if err != nil {
		t.Fatalf("create table failed: %v", err)
	}
	if result != 0 {
		t.Fatalf("TestCreateTable failed, the result \"%v\" is inconsistent with \"0\"", result)
	}
	t.Logf("result: %d\n", result)
}

func TestAsyncCreateTable(t *testing.T) {
	handler := func(receipt *types.Receipt, err error) {
		if err != nil {
			t.Fatalf("receive receipt failed, %v\n", err)
		}
		var bigNum *big.Int
		bigNum, err = precompiled.ParseBigIntFromOutput(receipt)
		if err != nil {
			t.Fatalf("parseReturnValue failed, err: %v\n", err)
		}
		result, err := precompiled.BigIntToInt64(bigNum)
		if err != nil {
			t.Fatalf("%v\n", err)
		}
		if result != 0 {
			t.Fatalf("TestAsyncCreateTable failed, the result \"%v\" is inconsistent with \"0\"", result)
		}
		t.Logf("result: %d\n", result)
		channel <- 0
	}

	_, err := service.AsyncCreateTable(handler, tableNameForAsync, key, valueFields)
	if err != nil {
		t.Fatalf("create table failed: %v", err)
	}
	select {
	case <-channel:
		return
	case <-time.After(timeout):
		t.Fatal("timeout")
	}
}

func TestInsert(t *testing.T) {
	var insertResults int64
	insertEntry := NewEntry()
	for i := 1; i <= 5; i++ {
		insertEntry.Put("item_id", "1")
		insertEntry.Put("item_name", "apple"+strconv.Itoa(i))
		insertResult, err := service.Insert(tableName, "fruit", insertEntry)
		if err != nil {
			t.Fatalf("insert table failed: %v", err)
		}
		insertResults += insertResult
	}
	if insertResults != 5 {
		t.Fatalf("TestInsert failed, the insertResults \"%v\" is inconsistent with \"5\"", insertResults)
	}
	t.Logf("insertResults: %d\n", insertResults)
}

func TestAsyncInsert(t *testing.T) {
	handler := func(receipt *types.Receipt, err error) {
		if err != nil {
			t.Fatalf("receive receipt failed, %v\n", err)
		}
		var bigNum *big.Int
		bigNum, err = precompiled.ParseBigIntFromOutput(receipt)
		if err != nil {
			t.Fatalf("parseReturnValue failed, err: %v\n", err)
		}
		result, err := precompiled.BigIntToInt64(bigNum)
		if err != nil {
			t.Fatalf("%v\n", err)
		}
		if result != 1 {
			t.Fatalf("TestAsyncInsert failed, the result \"%v\" is inconsistent with \"1\"", result)
		}
		t.Logf("result: %d\n", result)
		channel <- 0
	}

	insertEntry := NewEntry()
	insertEntry.Put("item_id", "1")
	insertEntry.Put("item_name", "apple")
	_, err := service.AsyncInsert(handler, tableNameForAsync, "fruit", insertEntry)
	if err != nil {
		t.Fatalf("insert table failed: %v", err)
	}
	select {
	case <-channel:
		return
	case <-time.After(timeout):
		t.Fatal("timeout")
	}
}

func TestSelect(t *testing.T) {
	condition := NewCondition()
	condition.EQ("item_id", "1")
	condition.Limit(5)

	resultSelect, err := service.Select(tableName, "fruit", condition)
	if err != nil {
		t.Fatalf("select table failed: %v", err)
	}
	if len(resultSelect) != 5 {
		t.Fatalf("TestSelect failed, the length of resultSelect \"%v\" is not inconsistent with \"5\"", len(resultSelect))
	}
	t.Logf("resultSelect :\n")
	t.Logf("%d", len(resultSelect))
	for i := 0; i < len(resultSelect); i++ {
		t.Logf("resultSelect[%d]'s name is：%s\n", i, resultSelect[i]["name"])
		t.Logf("resultSelect[%d]'s item_id is：%s\n", i, resultSelect[i]["item_id"])
		t.Logf("resultSelect[%d]'s item_name is：%s\n", i, resultSelect[i]["item_name"])
	}
}

func TestUpdate(t *testing.T) {
	updateEntry := NewEntry()
	updateEntry.Put("item_id", "1")
	updateEntry.Put("item_name", "orange")
	updateCondition := NewCondition()
	updateCondition.EQ("item_id", "1")
	updateResult, err := service.Update(tableName, "fruit", updateEntry, updateCondition)
	if err != nil {
		t.Fatalf("update table failed: %v", err)
	}
	if updateResult != 5 {
		t.Fatalf("TestUpdate failed, the updateResult \"%v\" is not inconsistent with \"5\"", updateResult)
	}
	t.Logf("updateResult: %d", updateResult)
}

func TestAsyncUpdate(t *testing.T) {
	handler := func(receipt *types.Receipt, err error) {
		if err != nil {
			t.Fatalf("receive receipt failed, %v\n", err)
		}
		var bigNum *big.Int
		bigNum, err = precompiled.ParseBigIntFromOutput(receipt)
		if err != nil {
			t.Fatalf("parseReturnValue failed, err: %v\n", err)
		}
		result, err := precompiled.BigIntToInt64(bigNum)
		if err != nil {
			t.Fatalf("%v\n", err)
		}
		if result != 1 {
			t.Fatalf("TestAsyncUpdate failed, the result \"%v\" is inconsistent with \"1\"", result)
		}
		t.Logf("result: %d\n", result)
		channel <- 0
	}

	updateEntry := NewEntry()
	updateEntry.Put("item_id", "1")
	updateEntry.Put("item_name", "orange")
	updateCondition := NewCondition()
	updateCondition.EQ("item_id", "1")
	_, err := service.AsyncUpdate(handler, tableNameForAsync, "fruit", updateEntry, updateCondition)
	if err != nil {
		t.Fatalf("update table failed: %v", err)
	}
	select {
	case <-channel:
		return
	case <-time.After(timeout):
		t.Fatal("timeout")
	}
}

func TestRemove(t *testing.T) {
	removeCondition := NewCondition()
	removeCondition.EQ("item_id", "1")
	removeResult, err := service.Remove(tableName, "fruit", removeCondition)
	if err != nil {
		t.Fatalf("remove table failed: %v", err)
	}
	if removeResult != 5 {
		t.Fatalf("TestRemove failed, the removeResult \"%v\" is not inconsistent with \"5\"", removeResult)
	}
	t.Logf("removeResult: %d\n", removeResult)
}

func TestAsyncRemove(t *testing.T) {
	handler := func(receipt *types.Receipt, err error) {
		if err != nil {
			t.Fatalf("receive receipt failed, %v\n", err)
		}
		var bigNum *big.Int
		bigNum, err = precompiled.ParseBigIntFromOutput(receipt)
		if err != nil {
			t.Fatalf("parseReturnValue failed, err: %v\n", err)
		}
		result, err := precompiled.BigIntToInt64(bigNum)
		if err != nil {
			t.Fatalf("%v\n", err)
		}
		if result != 1 {
			t.Fatalf("TestAsyncRemove failed, the result \"%v\" is inconsistent with \"1\"", result)
		}
		t.Logf("result: %d\n", result)
		channel <- 0
	}

	removeCondition := NewCondition()
	removeCondition.EQ("item_id", "1")
	_, err := service.AsyncRemove(handler, tableNameForAsync, "fruit", removeCondition)
	if err != nil {
		t.Fatalf("remove data failed: %v", err)
	}
	select {
	case <-channel:
		return
	case <-time.After(timeout):
		t.Fatal("timeout")
	}
}

func TestDesc(t *testing.T) {
	keyField, valueField, err := service.Desc(tableName)
	if err != nil {
		t.Fatalf("query table info by tableName failed: %v", err)
	}
	if keyField != "name" {
		t.Fatalf("TestDesc failed, the keyField \"%v\" is not inconsistent with \"name\"", keyField)
	}
	if valueField != "item_id,item_name" {
		t.Fatalf("TestDesc failed, the valueField \"%v\" is not inconsistent with \"item_id,item_name\"", valueField)
	}
	t.Logf("keyFiled is：%s\n", keyField)
	t.Logf("valueField is：%s\n", valueField)
}
