package cns

import (
	"encoding/hex"
	"os"
	"testing"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/ethereum/go-ethereum/common"
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
	newService, err := NewCnsService(c)
	if err != nil {
		t.Fatalf("init CnsService failed: %+v", err)
	}
	service = newService
}

const (
	standardOutput = 1
	name           = "store"
	version        = "5.0"
	address        = "0x0626918C51A1F36c7ad4354BB1197460A533a2B9"
	testABI        = `[
		{
			"constant": true,
			"inputs": [
				{
					"name": "",
					"type": "bytes32"
				}
			],
			"name": "items",
			"outputs": [
				{
					"name": "",
					"type": "bytes32"
				}
			],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [],
			"name": "version",
			"outputs": [
				{
					"name": "",
					"type": "string"
				}
			],
			"payable": false,
			"stateMutability": "view",
			"type": "function"
		},
		{
			"constant": false,
			"inputs": [
				{
					"name": "key",
					"type": "bytes32"
				},
				{
					"name": "value",
					"type": "bytes32"
				}
			],
			"name": "setItem",
			"outputs": [],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "function"
		},
		{
			"inputs": [
				{
					"name": "_version",
					"type": "string"
				}
			],
			"payable": false,
			"stateMutability": "nonpayable",
			"type": "constructor"
		},
		{
			"anonymous": false,
			"inputs": [
				{
					"indexed": false,
					"name": "key",
					"type": "bytes32"
				},
				{
					"indexed": false,
					"name": "value",
					"type": "bytes32"
				}
			],
			"name": "ItemSet",
			"type": "event"
		}
	]`
)

var (
	service *Service
)

func TestMain(m *testing.M) {
	getService(&testing.T{})
	exitCode := m.Run()
	os.Exit(exitCode)
}

func TestRegisterCns(t *testing.T) {
	result, err := service.RegisterCns(name, version, common.HexToAddress(address), testABI)
	if err != nil {
		t.Fatalf("Service RegisterCns failed: %+v\n", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRegisterCns failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestRegisterCns result: %v", result)
}

func TestGetAddressByContractNameAndVersion(t *testing.T) {
	addr, err := service.GetAddressByContractNameAndVersion(name, version)
	if err != nil {
		t.Fatalf("GetAddressByContractNameAndVersion failed: %v", err)
	}
	if addr.Hex() != address {
		t.Fatalf("GetAddressByContractNameAndVersion failed, the result %v is inconsistent with \"0626918c51a1f36c7ad4354bb1197460a533a2b9\"", addr.Hex())
	}
}

func TestQueryCnsByNameAndVersion(t *testing.T) {
	cnsInfo, err := service.QueryCnsByNameAndVersion(name, version)
	if err != nil {
		t.Fatalf("QueryCnsByNameAndVersion failed: %v\n", err)
	}
	if len(cnsInfo) != 1 {
		t.Fatalf("QueryCnsByNameAndVersion failed, the length of cnsInfo \"%v\" is inconsistent with 1", len(cnsInfo))
	}
	// t.Logf("QueryCnsByNameAndVersion: %v", cnsInfo[0])
}

func TestQueryCnsByName(t *testing.T) {
	cnsInfoByName, err := service.QueryCnsByName(name)
	if err != nil {
		t.Fatalf("QueryCnsByName failed: %v\n", err)
	}
	if len(cnsInfoByName) != 1 {
		t.Fatalf("QueryCnsByNameAndVersion failed, the length of cnsInfoByName \"%v\" is inconsistent with 1", len(cnsInfoByName))
	}
	// t.Logf("QueryCnsByName: %v", cnsInfoByName[0])
}
