package permission

import (
	"encoding/hex"
	"os"
	"testing"

	helloworld "github.com/FISCO-BCOS/go-sdk/.ci/hello"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/precompiled/crud"
	"github.com/ethereum/go-ethereum/common"
)

const (
	tableName      = "t_test1"
	key            = "name"
	valueFields    = "item_id, item_name"
	permissionAdd  = "0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F"
	standardOutput = 1
)

var (
	service         *Service
	contractAddress string
)

func getClient(t *testing.T) *client.Client {
	privateKey, _ := hex.DecodeString("b89d42f12290070f235fb8fb61dcf96e3b11516c5d4f6333f26e49bb955f8b62")
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
	newService, err := NewPermissionService(c)
	if err != nil {
		t.Fatalf("init PermissionService failed: %+v", err)
	}
	service = newService
}

func deployHelloWorldContract(t *testing.T) {
	c := getClient(t)
	address, tx, instance, err := helloworld.DeployHelloWorld(c.GetTransactOpts(), c) // deploy contract
	if err != nil {
		t.Fatalf("deploy HelloWorld contract failed：%v", err)
	}
	t.Logf("the address of contract: %v", address.Hex())
	t.Logf("the hash of transaction: %v", tx.Hash().Hex())
	_ = instance
	contractAddress = address.Hex()
}

func createTable(t *testing.T) {
	c := getClient(t)
	crudService, err := crud.NewCRUDService(c)
	if err != nil {
		t.Fatalf("createTable failed, init curdService error: %v", err)
	}
	result, err := crudService.CreateTable(tableName, key, valueFields)
	if err != nil {
		t.Fatalf("create table failed: %v", err)
	}
	if result != 0 {
		t.Fatalf("createTable failed, the result \"%v\" is inconsistent with \"0\"", result)
	}
	t.Logf("result: %d\n", result)
}

func TestMain(m *testing.M) {
	getService(&testing.T{})
	deployHelloWorldContract(&testing.T{})
	createTable(&testing.T{})

	exitCode := m.Run()

	os.Exit(exitCode)
}

func TestGrantPermissionManager(t *testing.T) {
	// FISCO BCOS version compatible test
	if service.client.GetCompatibleVersion() >= client.V2_5_0 {
		result, err := service.GrantPermissionManager(common.HexToAddress(permissionAdd))
		if result != -51004 {
			t.Fatalf("TestGrantPermissionManager failed: %v", err)
		}
		t.Logf("TestGrantPermissionManager: %v", err)
	} else {
		result, err := service.GrantPermissionManager(common.HexToAddress(permissionAdd))
		if err != nil {
			t.Fatalf("TestGrantPermissionManager failed: %v", err)
		}
		if result != standardOutput {
			t.Fatalf("TestGrantPermissionManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
		}
		t.Logf("TestGrantPermissionManager: %v", result)
	}
}

func TestListPermissionManager(t *testing.T) {
	// FISCO BCOS version compatible test
	if service.client.GetCompatibleVersion() >= client.V2_5_0 {
		listResult, err := service.ListPermissionManager()
		if err != nil {
			t.Fatalf("TestListPermissionManager failed: %v", err)
		}
		if len(listResult) != 0 && len(listResult) != 1 {
			t.Fatalf("TestListPermissionManager failed, the length of listResult is %v, err: %v", len(listResult), err)
		}
		t.Logf("TestListPermissionManager: %+v", listResult)
	} else {
		listResult, err := service.ListPermissionManager()
		if err != nil {
			t.Fatalf("TestListPermissionManager failed: %v", err)
		}
		t.Logf("TestListPermissionManager: %+v", listResult)
	}
}

func TestRevokePermissionManager(t *testing.T) {
	// FISCO BCOS version compatible test
	if service.client.GetCompatibleVersion() >= client.V2_5_0 {
		result, err := service.RevokePermissionManager(common.HexToAddress(permissionAdd))
		if result != -51004 {
			t.Fatalf("TestRevokePermissionManager failed: %v", err)
		}
		t.Logf("TestRevokePermissionManager: %v", result)
	} else {
		result, err := service.RevokePermissionManager(common.HexToAddress(permissionAdd))
		if err != nil {
			t.Fatalf("TestRevokePermissionManager failed: %v", err)
		}
		if result != standardOutput {
			t.Fatalf("TestRevokePermissionManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
		}
		t.Logf("TestRevokePermissionManager: %v", result)
	}
}

func TestGrantUserTableManager(t *testing.T) {
	result, err := service.GrantUserTableManager(tableName, common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestGrantUserTableManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestGrantUserTableManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestGrantUserTableManager: %v", result)
}

func TestListUserTableManager(t *testing.T) {
	listResult, err := service.ListUserTableManager(tableName)
	if err != nil {
		t.Fatalf("TestListUserTableManager failed: %v", err)
	}
	if len(listResult) != 1 {
		t.Fatalf("TestListUserTableManager failed, the length of listResult %v is inconsistent with \"1\"", len(listResult))
	}
	if listResult[0].Address != "0xfbb18d54e9ee57529cda8c7c52242efe879f064f" {
		t.Fatalf("TestListUserTableManager failed, the address %v is inconsistent with \"0xfbb18d54e9ee57529cda8c7c52242efe879f064f\"", listResult[0].Address)
	}
	for i := 0; i < len(listResult); i++ {
		t.Logf("TestListUserTableManager: %v", listResult[i])
	}
}

func TestRevokeUserTableManager(t *testing.T) {
	result, err := service.RevokeUserTableManager(tableName, common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestRevokeUserTableManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRevokeUserTableManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestRevokeUserTableManager revoke result: %v", result)
}

func TestGrantDeployAndCreateManager(t *testing.T) {
	result, err := service.GrantDeployAndCreateManager(common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestGrantDeployAndCreateManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestGrantDeployAndCreateManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestGrantDeployAndCreateManager: %v", result)
}

func TestListDeployAndCreateManager(t *testing.T) {
	listResult, err := service.ListDeployAndCreateManager()
	if err != nil {
		t.Fatalf("TestListDeployAndCreateManager failed: %v", err)
	}
	if len(listResult) != 1 && len(listResult) != 2 {
		t.Fatalf("TestListDeployAndCreateManager failed, the length of listResult %v is inconsistent with \"1 or 2\"", len(listResult))
	}
	for i := 0; i < len(listResult); i++ {
		t.Logf("TestListDeployAndCreateManager: %v", listResult[i])
	}
}

func TestRevokeDeployAndCreateManager(t *testing.T) {
	result, err := service.RevokeDeployAndCreateManager(common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestRevokeDeployAndCreateManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRevokeDeployAndCreateManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestRevokeDeployAndCreateManager revoke result: %v", result)
}

func TestGrantNodeManager(t *testing.T) {
	result, err := service.GrantNodeManager(common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestGrantNodeManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestGrantNodeManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestGrantNodeManager: %v", result)
}

func TestListNodeManager(t *testing.T) {
	listResult, err := service.ListNodeManager()
	if err != nil {
		t.Fatalf("TestListNodeManager failed: %v", err)
	}
	if len(listResult) != 1 && len(listResult) != 2 {
		t.Fatalf("TestListNodeManager failed, the length of listResult %v is inconsistent with \"1 or 2\"", len(listResult))
	}
	for i := 0; i < len(listResult); i++ {
		t.Logf("TestListNodeManager: %v", listResult[i])
	}
}

func TestRevokeNodeManager(t *testing.T) {
	result, err := service.RevokeNodeManager(common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestRevokeNodeManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRevokeNodeManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestRevokeNodeManager revoke result: %v", result)
}

func TestGrantCNSManager(t *testing.T) {
	result, err := service.GrantCNSManager(common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestGrantCNSManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestGrantCNSManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestGrantCNSManager: %v", result)
}

func TestListCNSManager(t *testing.T) {
	listResult, err := service.ListCNSManager()
	if err != nil {
		t.Fatalf("TestListCNSManager failed: %v", err)
	}
	if len(listResult) != 1 {
		t.Fatalf("TestListCNSManager failed, the length of listResult %v is inconsistent with \"1\"", len(listResult))
	}
	if listResult[0].Address != "0xfbb18d54e9ee57529cda8c7c52242efe879f064f" {
		t.Fatalf("TestListCNSManager failed, the address %v is inconsistent with \"0xfbb18d54e9ee57529cda8c7c52242efe879f064f\"", listResult[0].Address)
	}
	for i := 0; i < len(listResult); i++ {
		t.Logf("TestListCNSManager: %v", listResult[i])
	}
}

func TestRevokeCNSManager(t *testing.T) {
	result, err := service.RevokeCNSManager(common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestRevokeCNSManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRevokeCNSManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestRevokeCNSManager revoke result: %v", result)
}

func TestGrantSysConfigManager(t *testing.T) {
	result, err := service.GrantSysConfigManager(common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestGrantSysConfigManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestGrantSysConfigManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestGrantSysConfigManager: %v", result)
}

func TestListSysConfigManager(t *testing.T) {
	listResult, err := service.ListSysConfigManager()
	if err != nil {
		t.Fatalf("TestListSysConfigManager failed: %v", err)
	}
	if len(listResult) != 1 {
		t.Fatalf("TestListSysConfigManager failed, the length of listResult %v is inconsistent with \"1\"", len(listResult))
	}
	if listResult[0].Address != "0xfbb18d54e9ee57529cda8c7c52242efe879f064f" {
		t.Fatalf("TestListSysConfigManager failed, the address %v is inconsistent with \"0xfbb18d54e9ee57529cda8c7c52242efe879f064f\"", listResult[0].Address)
	}
	for i := 0; i < len(listResult); i++ {
		t.Logf("TestListSysConfigManager: %v", listResult[i])
	}
}

func TestRevokeSysConfigManager(t *testing.T) {
	result, err := service.RevokeSysConfigManager(common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestRevokeSysConfigManager failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRevokeSysConfigManager failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestRevokeSysConfigManager revoke result: %v", result)
}

func TestGrantContractWritePermission(t *testing.T) {
	result, err := service.GrantContractWritePermission(common.HexToAddress(contractAddress), common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestGrantContractWritePermission failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestGrantContractWritePermission failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestGrantContractWritePermission: %v", result)
}

func TestQueryPermission(t *testing.T) {
	listResult, err := service.ListContractWritePermission(common.HexToAddress(contractAddress))
	if err != nil {
		t.Fatalf("TestListContractWritePermission failed: %v", err)
	}
	if len(listResult) != 1 {
		t.Fatalf("TestListContractWritePermission failed, the length of listResult %v is inconsistent with \"1\"", len(listResult))
	}
	if listResult[0].Address != "0xfbb18d54e9ee57529cda8c7c52242efe879f064f" {
		t.Fatalf("TestListContractWritePermission failed, the address %v is inconsistent with \"0xfbb18d54e9ee57529cda8c7c52242efe879f064f\"", listResult[0].Address)
	}
	for i := 0; i < len(listResult); i++ {
		t.Logf("TestListContractWritePermission: %v", listResult[i])
	}
}

func TestRevokeContractWritePermission(t *testing.T) {
	result, err := service.RevokeContractWritePermission(common.HexToAddress(contractAddress), common.HexToAddress(permissionAdd))
	if err != nil {
		t.Fatalf("TestRevokeContractWritePermission failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRevokeContractWritePermission failed, the result %v is inconsistent with \"%v\"", result, standardOutput)
	}
	t.Logf("TestRevokeContractWritePermission revoke result: %v", result)
}
