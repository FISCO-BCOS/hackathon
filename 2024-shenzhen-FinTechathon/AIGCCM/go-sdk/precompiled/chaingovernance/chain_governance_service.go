package chaingovernance

import (
	"encoding/json"
	"fmt"
	"math/big"

	"github.com/FISCO-BCOS/go-sdk/abi/bind"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/FISCO-BCOS/go-sdk/precompiled"
	"github.com/ethereum/go-ethereum/common"
)

// ChainGovernance precompiled contract error code
const (
	currentValueIsExpectedValue     int64 = -52012
	accountFrozen                   int64 = -52011
	accountAlreadyAvailable         int64 = -52010
	invalidAccountAddress           int64 = -52009
	accountNotExist                 int64 = -52008
	operatorNotExist                int64 = -52007
	operatorExist                   int64 = -52006
	committeeMemberCannotBeOperator int64 = -52005
	operatorCannotBeCommitteeMember int64 = -52004
	invalidThreshold                int64 = -52003
	invalidRequestPermissionDenied  int64 = -52002
	committeeMemberNotExist         int64 = -52001
	committeeMemberExist            int64 = -52000
)

type AccountInfo struct {
	Address   string `json:"address"`
	EnableNum string `json:"enable_num"`
}

// getErrorMessage returns the message of error code
func getErrorMessage(errorCode int64) string {
	var message string
	switch errorCode {
	case currentValueIsExpectedValue:
		message = "current value is expected value"
	case accountFrozen:
		message = "account has been frozen"
	case accountAlreadyAvailable:
		message = "account already available"
	case invalidAccountAddress:
		message = "invalid account address"
	case accountNotExist:
		message = "account not exist"
	case operatorNotExist:
		message = "operator not exist"
	case operatorExist:
		message = "operator exist"
	case committeeMemberCannotBeOperator:
		message = "committee member cannot be operator"
	case operatorCannotBeCommitteeMember:
		message = "operator cannot be committee member"
	case invalidThreshold:
		message = "invalid threshold"
	case invalidRequestPermissionDenied:
		message = "invalid request permission denied"
	case committeeMemberNotExist:
		message = "committee member not exist"
	case committeeMemberExist:
		message = "committee member exist"
	default:
		message = ""
	}
	return message
}

// errorCodeToError judges whether the error code represents an error
func errorCodeToError(errorCode int64) error {
	var errorCodeMessage string
	errorCodeMessage = precompiled.GetCommonErrorCodeMessage(errorCode)
	if errorCodeMessage != "" {
		return fmt.Errorf("error code: %v, error code message: %v", errorCode, errorCodeMessage)
	}
	errorCodeMessage = getErrorMessage(errorCode)
	if errorCodeMessage != "" {
		return fmt.Errorf("error code: %v, error code message: %v", errorCode, errorCodeMessage)
	}
	return nil
}

// Service is a precompile contract service.
type Service struct {
	chainGovernance     *ChainGovernance
	chainGovernanceAuth *bind.TransactOpts
	client              *client.Client
}

// chainGovernancePrecompileAddress is the contract address of ChainGovernance
var chainGovernancePrecompileAddress = common.HexToAddress("0x0000000000000000000000000000000000001008")

// NewService returns ptr of Service
func NewService(client *client.Client) (*Service, error) {
	instance, err := NewChainGovernance(chainGovernancePrecompileAddress, client)
	if err != nil {
		return nil, fmt.Errorf("construct Service failed, err: %+v", err)
	}
	auth := client.GetTransactOpts()
	return &Service{instance, auth, client}, nil
}

// GrantCommitteeMember grants committee member
func (service *Service) GrantCommitteeMember(accountAddress common.Address) (int64, error) {
	_, receipt, err := service.chainGovernance.GrantCommitteeMember(service.chainGovernanceAuth, accountAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService GrantCommitteeMember failed, err: %v", err)
	}
	return parseReturnValue(receipt, "grantCommitteeMember")
}

// RevokeCommitteeMember revokes committee member
func (service *Service) RevokeCommitteeMember(accountAddress common.Address) (int64, error) {
	_, receipt, err := service.chainGovernance.RevokeCommitteeMember(service.chainGovernanceAuth, accountAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService RevokeCommitteeMember failed, err: %v", err)
	}
	return parseReturnValue(receipt, "revokeCommitteeMember")
}

// ListCommitteeMembers lists committee members
func (service *Service) ListCommitteeMembers() ([]AccountInfo, error) {
	opts := &bind.CallOpts{From: service.chainGovernanceAuth.From}
	committeeMembersInfo, err := service.chainGovernance.ListCommitteeMembers(opts)
	if err != nil {
		return nil, fmt.Errorf("ChainGovernanceService ListCommitteeMembers failed, err: %v", err)
	}
	// unmarshal result
	var committeeInfos []AccountInfo
	if err := json.Unmarshal([]byte(committeeMembersInfo), &committeeInfos); err != nil {
		return nil, fmt.Errorf("ChainGovernanceService ListCommitteeMembers: Unmarshal the List result failed, err: %v", err)
	}
	return committeeInfos, nil
}

// QueryCommitteeMemberWeight queries committee member
func (service *Service) QueryCommitteeMemberWeight(accountAddress common.Address) (uint64, error) {
	opts := &bind.CallOpts{From: service.chainGovernanceAuth.From}
	boolean, weight, err := service.chainGovernance.QueryCommitteeMemberWeight(opts, accountAddress)
	if err != nil {
		return 0, fmt.Errorf("ChainGovernanceService QueryCommitteeMemberWeight failed, err: %v", err)
	}
	if !boolean {
		return 0, fmt.Errorf("ChainGovernanceService QueryCommitteeMemberWeight, Address %s does not exist", common.Bytes2Hex(accountAddress[:]))
	}
	num, err := precompiled.BigIntToUint64(weight)
	if err != nil {
		return 0, fmt.Errorf("ChainGovernanceService QueryCommitteeMemberWeight failed, err: %v", err)
	}
	return num, nil
}

// UpdateCommitteeMemberWeight updates the weight of committee member
func (service *Service) UpdateCommitteeMemberWeight(accountAddress common.Address, weight uint64) (int64, error) {
	if weight < 1 {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService UpdateCommitteeMemberWeight failed, the weight %v is less than 1", weight)
	}
	weightInt64, err := precompiled.Uint64ToInt64(weight)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService UpdateCommitteeMemberWeight failed, err: %v", err)
	}
	bigNum := big.NewInt(weightInt64)
	_, receipt, err := service.chainGovernance.UpdateCommitteeMemberWeight(service.chainGovernanceAuth, accountAddress, bigNum)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService UpdateCommitteeMemberWeight failed, err: %v", err)
	}
	return parseReturnValue(receipt, "updateCommitteeMemberWeight")
}

// UpdateThreshold updates the threshold that the committee vote needs to reach
func (service *Service) UpdateThreshold(threshold uint64) (int64, error) {
	if threshold > 99 {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService UpdateThreshold failed, the threshold %v is not in the range of [0, 100)", threshold)
	}
	thresholdInt64, err := precompiled.Uint64ToInt64(threshold)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService UpdateThreshold failed, err: %v", err)
	}
	bigNum := big.NewInt(thresholdInt64)
	_, receipt, err := service.chainGovernance.UpdateThreshold(service.chainGovernanceAuth, bigNum)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService UpdateThreshold failed: %v", err)
	}
	return parseReturnValue(receipt, "updateThreshold")
}

// QueryThreshold queries the threshold of committee member
func (service *Service) QueryThreshold() (uint64, error) {
	opts := &bind.CallOpts{From: service.chainGovernanceAuth.From}
	result, err := service.chainGovernance.QueryThreshold(opts)
	if err != nil {
		return 0, fmt.Errorf("ChainGovernanceService QueryThreshold failed, err: %v", err)
	}
	num, err := precompiled.BigIntToUint64(result)
	if err != nil {
		return 0, fmt.Errorf("ChainGovernanceService QueryThreshold failed, err: %v", err)
	}
	return num, nil
}

// GrantOperator grants operator
func (service *Service) GrantOperator(accountAddress common.Address) (int64, error) {
	_, receipt, err := service.chainGovernance.GrantOperator(service.chainGovernanceAuth, accountAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService GrantOperator failed, err: %v", err)
	}
	return parseReturnValue(receipt, "grantOperator")
}

// RevokeOperator revokes operator
func (service *Service) RevokeOperator(accountAddress common.Address) (int64, error) {
	_, receipt, err := service.chainGovernance.RevokeOperator(service.chainGovernanceAuth, accountAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService RevokeOperator failed, err: %v", err)
	}
	return parseReturnValue(receipt, "revokeOperator")
}

// ListOperators lists operators
func (service *Service) ListOperators() ([]AccountInfo, error) {
	opts := &bind.CallOpts{From: service.chainGovernanceAuth.From}
	committeeMembersInfo, err := service.chainGovernance.ListOperators(opts)
	if err != nil {
		return nil, fmt.Errorf("ChainGovernanceService ListOperators failed, err: %v", err)
	}
	// unmarshal result
	var operatorInfos []AccountInfo
	if err := json.Unmarshal([]byte(committeeMembersInfo), &operatorInfos); err != nil {
		return nil, fmt.Errorf("ChainGovernanceService ListOperators: Unmarshal the List result failed, err: %v", err)
	}
	return operatorInfos, nil
}

// FreezeAccount freezes user account
func (service *Service) FreezeAccount(accountAddress common.Address) (int64, error) {
	_, receipt, err := service.chainGovernance.FreezeAccount(service.chainGovernanceAuth, accountAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService FreezeAccount failed, err: %v", err)
	}
	return parseReturnValue(receipt, "freezeAccount")
}

// UnfreezeAccount unfreezes operator
func (service *Service) UnfreezeAccount(accountAddress common.Address) (int64, error) {
	_, receipt, err := service.chainGovernance.UnfreezeAccount(service.chainGovernanceAuth, accountAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ChainGovernanceService UnfreezeAccount failed, err: %v", err)
	}
	return parseReturnValue(receipt, "unfreezeAccount")
}

// GetAccountStatus gets the status of account
func (service *Service) GetAccountStatus(accountAddress common.Address) (string, error) {
	opts := &bind.CallOpts{From: service.chainGovernanceAuth.From}
	result, err := service.chainGovernance.GetAccountStatus(opts, accountAddress)
	if err != nil {
		return "", fmt.Errorf("ChainGovernanceService GetAccountStatus failed, err: %v", err)
	}
	return result, nil
}

func parseReturnValue(receipt *types.Receipt, name string) (int64, error) {
	errorMessage := receipt.GetErrorMessage()
	if errorMessage != "" {
		return int64(receipt.GetStatus()), fmt.Errorf("receipt.Status err: %v", errorMessage)
	}
	bigNum, err := precompiled.ParseBigIntFromOutput(receipt)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("parseReturnValue failed, err: %v", err)
	}
	errorCode, err := precompiled.BigIntToInt64(bigNum)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("parseReturnValue failed, err: %v", err)
	}
	return errorCode, errorCodeToError(errorCode)
}
