package contractlifecycle

import (
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/abi/bind"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/FISCO-BCOS/go-sdk/precompiled"
	"github.com/ethereum/go-ethereum/common"
)

// ChainGovernance precompiled contract error code
const (
	invalidNoAuthorized                = -51905
	invalidContractNotExist            = -51904
	invalidContractAddress             = -51903
	invalidContractRepeatAuthorization = -51902
	invalidContractAvailable           = -51901
	invalidContractFrozen              = -51900
)

// getErrorMessage returns the message of error code
func getErrorMessage(errorCode int64) string {
	var message string
	switch errorCode {
	case invalidNoAuthorized:
		message = "invalid no authorized"
	case invalidContractNotExist:
		message = "invalid contract not exist"
	case invalidContractAddress:
		message = "invalid contract address"
	case invalidContractRepeatAuthorization:
		message = "invalid contract repeat authorization"
	case invalidContractAvailable:
		message = "invalid contract available"
	case invalidContractFrozen:
		message = "invalid contract frozen"
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
	contractLifeCycle     *ContractLifeCycle
	contractLifeCycleAuth *bind.TransactOpts
	client                *client.Client
}

// contractLifeCyclePrecompileAddress is the contract address of ContractLifeCycle
var contractLifeCyclePrecompileAddress = common.HexToAddress("0x0000000000000000000000000000000000001007")

// NewService returns ptr of Service
func NewService(client *client.Client) (*Service, error) {
	instance, err := NewContractLifeCycle(contractLifeCyclePrecompileAddress, client)
	if err != nil {
		return nil, fmt.Errorf("construct Service failed, err: %+v", err)
	}
	auth := client.GetTransactOpts()
	return &Service{instance, auth, client}, nil
}

// Freeze freezes contract address
func (service *Service) Freeze(contractAddress common.Address) (int64, error) {
	_, receipt, err := service.contractLifeCycle.Freeze(service.contractLifeCycleAuth, contractAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ContractLifeCycleService Freeze failed, err: %v", err)
	}
	return parseReturnValue(receipt, "freeze")
}

// Unfreeze unfreezes contract address
func (service *Service) Unfreeze(contractAddress common.Address) (int64, error) {
	_, receipt, err := service.contractLifeCycle.Unfreeze(service.contractLifeCycleAuth, contractAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ContractLifeCycleService Unfreeze failed, err: %v", err)
	}
	return parseReturnValue(receipt, "unfreeze")
}

// GrantManager grants manager
func (service *Service) GrantManager(contractAddress, accountAddress common.Address) (int64, error) {
	_, receipt, err := service.contractLifeCycle.GrantManager(service.contractLifeCycleAuth, contractAddress, accountAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ContractLifeCycleService GrantManager failed, err: %v", err)
	}
	return parseReturnValue(receipt, "grantManager")
}

// GetStatus gets the status of contract account
func (service *Service) GetStatus(contractAddress common.Address) (uint64, string, error) {
	opts := &bind.CallOpts{From: service.contractLifeCycleAuth.From}
	bigNum, message, err := service.contractLifeCycle.GetStatus(opts, contractAddress)
	if err != nil {
		return types.PrecompiledError, "", fmt.Errorf("ContractLifeCycleService GetStatus failed, err: %v", err)
	}
	num, err := precompiled.BigIntToUint64(bigNum)
	if err != nil {
		return types.PrecompiledError, "", fmt.Errorf("ContractLifeCycleService GetStatus failed, err: %v", err)
	}
	return num, message, nil
}

// ListManager lists managers of contract
func (service *Service) ListManager(contractAddress common.Address) (uint64, []common.Address, error) {
	opts := &bind.CallOpts{From: service.contractLifeCycleAuth.From}
	bigNum, managerAddressList, err := service.contractLifeCycle.ListManager(opts, contractAddress)
	if err != nil {
		return types.PrecompiledError, nil, fmt.Errorf("ContractLifeCycleService ListManager failed, err: %v", err)
	}
	num, err := precompiled.BigIntToUint64(bigNum)
	if err != nil {
		return types.PrecompiledError, nil, fmt.Errorf("ContractLifeCycleService GetStatus failed, err: %v", err)
	}
	return num, managerAddressList, nil
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
