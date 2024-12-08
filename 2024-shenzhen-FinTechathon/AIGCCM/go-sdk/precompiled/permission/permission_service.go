package permission

import (
	"encoding/json"
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/abi/bind"
	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	"github.com/FISCO-BCOS/go-sdk/precompiled"
	"github.com/ethereum/go-ethereum/common"
)

const (
	// Permission precompiled contract error code
	committeePermission     int64 = -51004
	contractNotExist        int64 = -51003
	tableNameOverflow       int64 = -51002
	tableAndAddressNotExist int64 = -51001
	tableAndAddressExist    int64 = -51000

	// system table name
	SysConsensus   = "_sys_consensus_"
	SysCNS         = "_sys_cns_"
	SysTableAccess = "_sys_table_access_"
	SysConfig      = "_sys_config_"
	SysTable       = "_sys_tables_"
)

type Info struct {
	Address   string `json:"address"`
	EnableNum string `json:"enable_num"`
	TableName string `json:"table_name"`
}

// getErrorMessage returns the message of error code
func getErrorMessage(errorCode int64) string {
	var message string
	switch errorCode {
	case committeePermission:
		message = "committee member permission managed by ChainGovernance"
	case contractNotExist:
		message = "contract not exist"
	case tableNameOverflow:
		message = "table name overflow"
	case tableAndAddressNotExist:
		message = "table and address not exist"
	case tableAndAddressExist:
		message = "table and address exist"
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
	permission     *Permission
	permissionAuth *bind.TransactOpts
	client         *client.Client
}

// PermissionPrecompileAddress is the contract address of Permission
var permissionPrecompileAddress common.Address = common.HexToAddress("0x0000000000000000000000000000000000001005")

// NewPermissionService returns ptr of Service
func NewPermissionService(client *client.Client) (*Service, error) {
	instance, err := NewPermission(permissionPrecompileAddress, client)
	if err != nil {
		return nil, fmt.Errorf("construct PermissionService failed: %+v", err)
	}
	auth := client.GetTransactOpts()
	return &Service{permission: instance, permissionAuth: auth, client: client}, nil
}

// GrantUserTableManager grants the info by the table name and user address
func (service *Service) GrantUserTableManager(tableName string, accountAddress common.Address) (int64, error) {
	return service.grant(tableName, accountAddress)
}

// RevokeUserTableManager revokes a accountAddress' right of the table name
func (service *Service) RevokeUserTableManager(tableName string, accountAddress common.Address) (int64, error) {
	return service.revoke(tableName, accountAddress)
}

// ListUserTableManager returns the list of permission info
func (service *Service) ListUserTableManager(tableName string) ([]Info, error) {
	return service.list(tableName)
}

// GrantDeployAndCreateManager grants the deploy and create option to an address
func (service *Service) GrantDeployAndCreateManager(accountAddress common.Address) (int64, error) {
	return service.grant(SysTable, accountAddress)
}

// RevokeDeployAndCreateManager revokes a accountAddress's right of the deploy and create option
func (service *Service) RevokeDeployAndCreateManager(accountAddress common.Address) (int64, error) {
	return service.revoke(SysTable, accountAddress)
}

// ListDeployAndCreateManager returns the list of permission info
func (service *Service) ListDeployAndCreateManager() ([]Info, error) {
	return service.list(SysTable)
}

// GrantPermissionManager grants the permission
func (service *Service) GrantPermissionManager(accountAddress common.Address) (int64, error) {
	return service.grant(SysTableAccess, accountAddress)
}

// RevokePermissionManager revokes the permission
func (service *Service) RevokePermissionManager(accountAddress common.Address) (int64, error) {
	return service.revoke(SysTableAccess, accountAddress)
}

// ListPermissionManager returns the list of permission
func (service *Service) ListPermissionManager() ([]Info, error) {
	return service.list(SysTableAccess)
}

// GrantNodeManager grants the Node
func (service *Service) GrantNodeManager(accountAddress common.Address) (int64, error) {
	return service.grant(SysConsensus, accountAddress)
}

// RevokeNodeManager revokes the Node
func (service *Service) RevokeNodeManager(accountAddress common.Address) (int64, error) {
	return service.revoke(SysConsensus, accountAddress)
}

// ListNodeManager returns the list of Node manager
func (service *Service) ListNodeManager() ([]Info, error) {
	return service.list(SysConsensus)
}

// GrantCNSManager grants the CNS
func (service *Service) GrantCNSManager(accountAddress common.Address) (int64, error) {
	return service.grant(SysCNS, accountAddress)
}

// RevokeCNSManager revokes the CNS
func (service *Service) RevokeCNSManager(accountAddress common.Address) (int64, error) {
	return service.revoke(SysCNS, accountAddress)
}

// ListCNSManager returns the list of CNS manager
func (service *Service) ListCNSManager() ([]Info, error) {
	return service.list(SysCNS)
}

// GrantSysConfigManager grants the System configuration manager
func (service *Service) GrantSysConfigManager(accountAddress common.Address) (int64, error) {
	return service.grant(SysConfig, accountAddress)
}

// RevokeSysConfigManager revokes the System configuration manager
func (service *Service) RevokeSysConfigManager(accountAddress common.Address) (int64, error) {
	return service.revoke(SysConfig, accountAddress)
}

// ListSysConfigManager returns the list of System configuration manager
func (service *Service) ListSysConfigManager() ([]Info, error) {
	return service.list(SysConfig)
}

// GrantContractWritePermission grants the permission of writing contract
func (service *Service) GrantContractWritePermission(contractAddress common.Address, accountAddress common.Address) (int64, error) {
	_, receipt, err := service.permission.GrantWrite(service.permissionAuth, contractAddress, accountAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("GrantWrite grant failed: %v", err)
	}
	return parseReturnValue(receipt, "grantWrite")
}

// RevokeContractWritePermission revokes the permission of writing contract
func (service *Service) RevokeContractWritePermission(contractAddress common.Address, accountAddress common.Address) (int64, error) {
	_, receipt, err := service.permission.RevokeWrite(service.permissionAuth, contractAddress, accountAddress)
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("RevokeWrite revoke failed: %v", err)
	}
	return parseReturnValue(receipt, "revokeWrite")
}

// ListContractWritePermission queries the accounts that have the permission of writing contract
func (service *Service) ListContractWritePermission(contractAddress common.Address) ([]Info, error) {
	opts := &bind.CallOpts{From: service.permissionAuth.From}
	permissionInfo, err := service.permission.QueryPermission(opts, contractAddress)
	if err != nil {
		return nil, fmt.Errorf("QueryPermission failed: %v", err)
	}
	// unmarshal result
	var results []Info
	if err := json.Unmarshal([]byte(permissionInfo), &results); err != nil {
		return nil, fmt.Errorf("QueryPermission: Unmarshal the List result failed: %v", err)
	}
	return results, nil
}

func (service *Service) grant(tableName string, accountAddress common.Address) (int64, error) {
	_, receipt, err := service.permission.Insert(service.permissionAuth, tableName, accountAddress.Hex())
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("PermissionService grant failed: %v", err)
	}
	return parseReturnValue(receipt, "insert")
}

func (service *Service) revoke(tableName string, accountAddress common.Address) (int64, error) {
	_, receipt, err := service.permission.Remove(service.permissionAuth, tableName, accountAddress.Hex())
	if err != nil {
		return precompiled.DefaultErrorCode, fmt.Errorf("PermissionService revoke failed: %v", err)
	}
	return parseReturnValue(receipt, "remove")
}

func (service *Service) list(tableName string) ([]Info, error) {
	opts := &bind.CallOpts{From: service.permissionAuth.From}
	permissionInfo, err := service.permission.QueryByName(opts, tableName)
	if err != nil {
		return nil, fmt.Errorf("PermissionService List failed: %v", err)
	}
	// unmarshal result
	var results []Info
	if err := json.Unmarshal([]byte(permissionInfo), &results); err != nil {
		return nil, fmt.Errorf("PermissionService: Unmarshal the List result failed: %v", err)
	}
	return results, nil
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
