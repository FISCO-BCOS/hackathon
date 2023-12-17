package cns

import (
	"encoding/json"
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/abi/bind"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/FISCO-BCOS/go-sdk/precompiled"
	"github.com/ethereum/go-ethereum/common"
)

// cns precompiled contract error code
const (
	versionLengthOverflow  int64 = -51201
	addressAndVersionExist int64 = -51200
)

// getErrorMessage returns the message of error code
func getErrorMessage(errorCode int64) string {
	var message string
	switch errorCode {
	case versionLengthOverflow:
		message = "version length overflow"
	case addressAndVersionExist:
		message = "address and version exist"
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

// Info is used for the CNSService
type Info struct {
	Name    string `json:"name"`
	Version string `json:"version"`
	Address string `json:"address"`
	Abi     string `json:"abi"`
}

// Service is a precompile contract service.
type Service struct {
	cns     *Cns
	cnsAuth *bind.TransactOpts
	client  *client.Client
}

const maxVersionLength = 40

// contract address
var cnsPrecompileAddress = common.HexToAddress("0x0000000000000000000000000000000000001004")

// NewCnsService returns ptr of Service
func NewCnsService(client *client.Client) (*Service, error) {
	instance, err := NewCns(cnsPrecompileAddress, client)
	if err != nil {
		return nil, fmt.Errorf("construct Service failed: %+v", err)
	}
	auth := client.GetTransactOpts()
	return &Service{cns: instance, cnsAuth: auth, client: client}, nil
}

// SelectByName returns the cns information according to the name string.
func (service *Service) SelectByName(name string) (string, error) {
	opts := &bind.CallOpts{From: service.cnsAuth.From}
	cnsName, err := service.cns.SelectByName(opts, name)
	if err != nil {
		return "", fmt.Errorf("service SelectByName failed: %+v", err)
	}
	return cnsName, nil
}

// SelectByNameAndVersion returns the cns information according to the name string and version string.
func (service *Service) SelectByNameAndVersion(name string, version string) (string, error) {
	opts := &bind.CallOpts{From: service.cnsAuth.From}
	cnsName, err := service.cns.SelectByNameAndVersion(opts, name, version)
	if err != nil {
		return "", fmt.Errorf("service SelectByNameAndVersion failed: %+v", err)
	}
	return cnsName, nil
}

// GetAddressByContractNameAndVersion returns the contract address.
func (service *Service) GetAddressByContractNameAndVersion(contractName, version string) (common.Address, error) {
	opts := &bind.CallOpts{From: service.cnsAuth.From}
	address, err := service.cns.GetContractAddress(opts, contractName, version)
	if err != nil {
		return common.Address{}, fmt.Errorf("service GetAddressByContractNameAndVersion failed: %+v", err)
	}
	return address, nil
}

// RegisterCns registers a contract for its CNS.
func (service *Service) RegisterCns(name string, version string, address common.Address, abi string) (int64, error) {
	if len(version) > maxVersionLength {
		return precompiled.DefaultErrorCode, fmt.Errorf("version string length exceeds the maximum limit")
	}
	_, receipt, err := service.cns.Insert(service.cnsAuth, name, version, address.String(), abi)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("service RegisterCns failed: %+v", err)
	}
	return parseReturnValue(receipt, "insert")
}

// QueryCnsByName returns the CNS info according to the CNS name
func (service *Service) QueryCnsByName(name string) ([]Info, error) {
	cnsInfo, err := service.SelectByName(name)
	if err != nil {
		return nil, err
	}
	// json unmarshal
	var infos []Info
	if err := json.Unmarshal([]byte(cnsInfo), &infos); err != nil {
		return nil, fmt.Errorf("unmarshal the Info failed")
	}
	return infos, nil
}

// QueryCnsByNameAndVersion returns the CNS info according to the name and version
func (service *Service) QueryCnsByNameAndVersion(name string, version string) ([]Info, error) {
	cnsInfo, err := service.SelectByNameAndVersion(name, version)
	if err != nil {
		return nil, err
	}
	// json unmarshal
	var infos []Info
	if err := json.Unmarshal([]byte(cnsInfo), &infos); err != nil {
		return nil, fmt.Errorf("unmarshal the Info failed")
	}
	return infos, nil
}

func parseReturnValue(receipt *types.Receipt, name string) (int64, error) {
	errorMessage := receipt.GetErrorMessage()
	if errorMessage != "" {
		return int64(receipt.GetStatus()), fmt.Errorf("receipt.Status err: %v", errorMessage)
	}
	bigNum, err := precompiled.ParseBigIntFromOutput(receipt)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("ParseBigIntFromOutput failed, err: %v, txHah: %v", err, receipt.TransactionHash)
	}
	errorCode, err := precompiled.BigIntToInt64(bigNum)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("parseReturnValue failed, err: %v, txHah: %v", err, receipt.TransactionHash)
	}
	return errorCode, errorCodeToError(errorCode)
}
