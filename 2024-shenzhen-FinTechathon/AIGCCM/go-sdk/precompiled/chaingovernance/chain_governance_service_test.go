package chaingovernance

import (
	"encoding/hex"
	"os"
	"testing"

	helloworld "github.com/FISCO-BCOS/go-sdk/.ci/hello"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/ethereum/go-ethereum/common"
)

const (
	committeeMemberUserAccount = "0x83309d045a19c44dc3722d15a6abd472f95866ac"
	operatorUserAccount        = "0x112fb844934c794a9e425dd6b4e57eff1b519f17"
	standardOutput             = 1
)

var (
	service *Service
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
	newService, err := NewService(c)
	if err != nil {
		t.Fatalf("init ChainGovernanceService failed: %+v", err)
	}
	service = newService
}

func TestMain(m *testing.M) {
	getService(&testing.T{})
	if service.client.GetCompatibleVersion() < client.V2_5_0 {
		os.Exit(0)
	}
	// only freezing the accounts that deploy contracts
	deployHelloWorldContract(&testing.T{})
	exitCode := m.Run()
	os.Exit(exitCode)
}

func deployHelloWorldContract(t *testing.T) {
	privateKey, _ := hex.DecodeString("8c47f550380591adab955cf050c439c0ffabb236bf05a64849ee0ba8aed42a41")
	config := &conf.Config{IsHTTP: true, ChainID: 1, IsSMCrypto: false, GroupID: 1,
		PrivateKey: privateKey, NodeURL: "http://localhost:8545"}
	c, err := client.Dial(config)
	if err != nil {
		t.Fatalf("Dial to %s failed of %v", config.NodeURL, err)
	}
	address, tx, instance, err := helloworld.DeployHelloWorld(c.GetTransactOpts(), c) // deploy contract
	if err != nil {
		t.Fatalf("deploy HelloWorld contract failed：%v", err)
	}
	t.Logf("the address of contract: %v", address.Hex())
	t.Logf("the hash of transaction: %v", tx.Hash().Hex())
	_ = instance
}

func TestGrantCommitteeMember(t *testing.T) {
	result, err := service.GrantCommitteeMember(common.HexToAddress(committeeMemberUserAccount))
	if err != nil {
		t.Fatalf("TestGrantCommitteeMember failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestGrantCommitteeMember failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("TestGrantCommitteeMember: %v", result)
}

func TestRevokeCommitteeMember(t *testing.T) {
	result, err := service.RevokeCommitteeMember(common.HexToAddress(committeeMemberUserAccount))
	if err != nil {
		t.Fatalf("TestRevokeCommitteeMember failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRevokeCommitteeMember failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("TestRevokeCommitteeMember: %v", result)

	//committee members are required to continue to execute other test cases
	_, err = service.GrantCommitteeMember(common.HexToAddress(committeeMemberUserAccount))
	if err != nil {
		t.Fatalf("GrantCommitteeMember failed: %v", err)
	}
}

func TestListCommitteeMembers(t *testing.T) {
	result, err := service.ListCommitteeMembers()
	if err != nil {
		t.Fatalf("TestListCommitteeMembers failed: %v", err)
	}
	if len(result) == 0 {
		t.Fatalf("TestListCommitteeMembers failed, the result %s is inconsistent with \"0x83309d045a19c44dc3722d15a6abd472f95866ac\"", result)
	}
	if result[0].Address != "0x83309d045a19c44dc3722d15a6abd472f95866ac" {
		t.Fatalf("TestListCommitteeMembers failed, the result %s is inconsistent with \"0x83309d045a19c44dc3722d15a6abd472f95866ac\"", result[0].Address)
	}
	t.Logf("TestListCommitteeMembers: %v", result)
}

func TestQueryCommitteeMemberWeight(t *testing.T) {
	result, err := service.QueryCommitteeMemberWeight(common.HexToAddress(committeeMemberUserAccount))
	if err != nil {
		t.Fatalf("TestQueryCommitteeMemberWeight failed, err: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestQueryCommitteeMemberWeight failed, the result %v is inconsistent with 1", result)
	}
	t.Logf("TestQueryCommitteeMemberWeight: %v", result)
}

func TestUpdateCommitteeMemberWeight(t *testing.T) {
	var weight uint64 = 2
	result, err := service.UpdateCommitteeMemberWeight(common.HexToAddress(committeeMemberUserAccount), weight)
	if err != nil {
		t.Fatalf("TestUpdateCommitteeMemberWeight failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestUpdateCommitteeMemberWeight failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("TestUpdateCommitteeMemberWeight: %v", result)

	// test unusual weight value
	result, err = service.UpdateCommitteeMemberWeight(common.HexToAddress(committeeMemberUserAccount), 0)
	if result == types.PrecompiledError {
		t.Logf("TestUpdateCommitteeMemberWeight failed: %v", err)
	}
}

func TestUpdateUpdateThreshold(t *testing.T) {
	var threshold uint64 = 3
	result, err := service.UpdateThreshold(threshold)
	if err != nil {
		t.Fatalf("TestUpdateUpdateThreshold failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestUpdateUpdateThreshold failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("TestUpdateUpdateThreshold: %v", result)

	// test unusual weight value
	result, err = service.UpdateThreshold(100)
	if result == types.PrecompiledError {
		t.Logf("TestUpdateUpdateThreshold failed: %v", err)
	}
}

func TestQueryThreshold(t *testing.T) {
	result, err := service.QueryThreshold()
	if err != nil {
		t.Fatalf("TestQueryThreshold failed: %v", err)
	}
	if result != 3 {
		t.Fatalf("TestQueryThreshold failed, the result %v is inconsistent with 3", result)
	}
	t.Logf("TestQueryThreshold: %v", result)
}

func TestGrantOperator(t *testing.T) {
	result, err := service.GrantOperator(common.HexToAddress(operatorUserAccount))
	if err != nil {
		t.Fatalf("TestGrantOperator failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestGrantOperator failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("TestGrantOperator: %v", result)
}

func TestRevokeOperator(t *testing.T) {
	result, err := service.RevokeOperator(common.HexToAddress(operatorUserAccount))
	if err != nil {
		t.Fatalf("TestRevokeOperator failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestRevokeOperator failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("TestRevokeOperator: %v", result)

	// Operators are required to continue to execute other test cases
	_, err = service.GrantOperator(common.HexToAddress(operatorUserAccount))
	if err != nil {
		t.Fatalf("GrantOperator failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("GrantOperator failed, the result %v is inconsistent with \"1\"", result)
	}
}

func TestListOperators(t *testing.T) {
	result, err := service.ListOperators()
	if err != nil {
		t.Fatalf("TestListOperators failed: %v", err)
	}
	if len(result) == 0 {
		t.Fatalf("TestListOperators failed, the result %s is inconsistent with \"0x112fb844934c794a9e425dd6b4e57eff1b519f17\"", result)
	}
	if result[0].Address != "0x112fb844934c794a9e425dd6b4e57eff1b519f17" {
		t.Fatalf("TestListOperators failed, the result %s is inconsistent with \"0x112fb844934c794a9e425dd6b4e57eff1b519f17\"", result[0].Address)
	}
	t.Logf("TestListOperators: %v", result)
}

func TestFreezeAccount(t *testing.T) {
	result, err := service.FreezeAccount(common.HexToAddress(operatorUserAccount))
	if err != nil {
		t.Fatalf("TestFreezeAccount failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestFreezeAccount failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("TestFreezeAccount: %v", result)
}

func TestUnfreezeAccount(t *testing.T) {
	result, err := service.UnfreezeAccount(common.HexToAddress(operatorUserAccount))
	if err != nil {
		t.Fatalf("TestUnfreezeAccount failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("TestUnfreezeAccount failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("TestUnfreezeAccount: %v", result)
}

func TestGetAccountStatus(t *testing.T) {
	result, err := service.GetAccountStatus(common.HexToAddress(operatorUserAccount))
	if err != nil {
		t.Fatalf("TestGetAccountStatus failed: %v", err)
	}
	if result != "The account is available." {
		t.Fatalf("TestGetAccountStatus failed, the result \"%s\" is inconsistent with \"The account is available.\"", result)
	}
	t.Logf("TestGetAccountStatus: %v", result)
}

// TestRecoverRoleState restores role permissions, otherwise it will affect other test cases
func TestRecoverRoleState(t *testing.T) {
	// revoke operator
	result, err := service.RevokeOperator(common.HexToAddress(operatorUserAccount))
	if err != nil {
		t.Fatalf("RevokeOperator failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("RevokeOperator failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("RevokeOperator: %v", result)

	// revoke committee member
	result, err = service.RevokeCommitteeMember(common.HexToAddress(committeeMemberUserAccount))
	if err != nil {
		t.Fatalf("RevokeCommitteeMember failed: %v", err)
	}
	if result != standardOutput {
		t.Fatalf("RevokeCommitteeMember failed, the result %v is inconsistent with \"1\"", result)
	}
	t.Logf("RevokeCommitteeMember: %v", result)
}
