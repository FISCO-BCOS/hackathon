// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package permission

import (
	"math/big"
	"strings"

	"github.com/FISCO-BCOS/go-sdk/abi"
	"github.com/FISCO-BCOS/go-sdk/abi/bind"
	"github.com/FISCO-BCOS/go-sdk/core/types"
	ethereum "github.com/ethereum/go-ethereum"
	"github.com/ethereum/go-ethereum/common"
)

// Reference imports to suppress errors if they are not otherwise used.
var (
	_ = big.NewInt
	_ = strings.NewReader
	_ = ethereum.NotFound
	_ = abi.U256
	_ = bind.Bind
	_ = common.Big1
	_ = types.BloomLookup
)

// PermissionABI is the input ABI used to generate the binding from.
const PermissionABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"table_name\",\"type\":\"string\"},{\"name\":\"addr\",\"type\":\"string\"}],\"name\":\"insert\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"table_name\",\"type\":\"string\"}],\"name\":\"queryByName\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"table_name\",\"type\":\"string\"},{\"name\":\"addr\",\"type\":\"string\"}],\"name\":\"remove\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"contractAddr\",\"type\":\"address\"}],\"name\":\"queryPermission\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"contractAddr\",\"type\":\"address\"},{\"name\":\"user\",\"type\":\"address\"}],\"name\":\"grantWrite\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"contractAddr\",\"type\":\"address\"},{\"name\":\"user\",\"type\":\"address\"}],\"name\":\"revokeWrite\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"

// Permission is an auto generated Go binding around a Solidity contract.
type Permission struct {
	PermissionCaller     // Read-only binding to the contract
	PermissionTransactor // Write-only binding to the contract
	PermissionFilterer   // Log filterer for contract events
}

// PermissionCaller is an auto generated read-only Go binding around a Solidity contract.
type PermissionCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// PermissionTransactor is an auto generated write-only Go binding around a Solidity contract.
type PermissionTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// PermissionFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type PermissionFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// PermissionSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type PermissionSession struct {
	Contract     *Permission       // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// PermissionCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type PermissionCallerSession struct {
	Contract *PermissionCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts     // Call options to use throughout this session
}

// PermissionTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type PermissionTransactorSession struct {
	Contract     *PermissionTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts     // Transaction auth options to use throughout this session
}

// PermissionRaw is an auto generated low-level Go binding around a Solidity contract.
type PermissionRaw struct {
	Contract *Permission // Generic contract binding to access the raw methods on
}

// PermissionCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type PermissionCallerRaw struct {
	Contract *PermissionCaller // Generic read-only contract binding to access the raw methods on
}

// PermissionTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type PermissionTransactorRaw struct {
	Contract *PermissionTransactor // Generic write-only contract binding to access the raw methods on
}

// NewPermission creates a new instance of Permission, bound to a specific deployed contract.
func NewPermission(address common.Address, backend bind.ContractBackend) (*Permission, error) {
	contract, err := bindPermission(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &Permission{PermissionCaller: PermissionCaller{contract: contract}, PermissionTransactor: PermissionTransactor{contract: contract}, PermissionFilterer: PermissionFilterer{contract: contract}}, nil
}

// NewPermissionCaller creates a new read-only instance of Permission, bound to a specific deployed contract.
func NewPermissionCaller(address common.Address, caller bind.ContractCaller) (*PermissionCaller, error) {
	contract, err := bindPermission(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &PermissionCaller{contract: contract}, nil
}

// NewPermissionTransactor creates a new write-only instance of Permission, bound to a specific deployed contract.
func NewPermissionTransactor(address common.Address, transactor bind.ContractTransactor) (*PermissionTransactor, error) {
	contract, err := bindPermission(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &PermissionTransactor{contract: contract}, nil
}

// NewPermissionFilterer creates a new log filterer instance of Permission, bound to a specific deployed contract.
func NewPermissionFilterer(address common.Address, filterer bind.ContractFilterer) (*PermissionFilterer, error) {
	contract, err := bindPermission(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &PermissionFilterer{contract: contract}, nil
}

// bindPermission binds a generic wrapper to an already deployed contract.
func bindPermission(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(PermissionABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Permission *PermissionRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Permission.Contract.PermissionCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Permission *PermissionRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.PermissionTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Permission *PermissionRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.PermissionTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Permission *PermissionCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Permission.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Permission *PermissionTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Permission *PermissionTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.contract.Transact(opts, method, params...)
}

// QueryByName is a free data retrieval call binding the contract method 0x20586031.
//
// Solidity: function queryByName(string table_name) constant returns(string)
func (_Permission *PermissionCaller) QueryByName(opts *bind.CallOpts, table_name string) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Permission.contract.Call(opts, out, "queryByName", table_name)
	return *ret0, err
}

// QueryByName is a free data retrieval call binding the contract method 0x20586031.
//
// Solidity: function queryByName(string table_name) constant returns(string)
func (_Permission *PermissionSession) QueryByName(table_name string) (string, error) {
	return _Permission.Contract.QueryByName(&_Permission.CallOpts, table_name)
}

// QueryByName is a free data retrieval call binding the contract method 0x20586031.
//
// Solidity: function queryByName(string table_name) constant returns(string)
func (_Permission *PermissionCallerSession) QueryByName(table_name string) (string, error) {
	return _Permission.Contract.QueryByName(&_Permission.CallOpts, table_name)
}

// QueryPermission is a free data retrieval call binding the contract method 0x54ad6352.
//
// Solidity: function queryPermission(address contractAddr) constant returns(string)
func (_Permission *PermissionCaller) QueryPermission(opts *bind.CallOpts, contractAddr common.Address) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Permission.contract.Call(opts, out, "queryPermission", contractAddr)
	return *ret0, err
}

// QueryPermission is a free data retrieval call binding the contract method 0x54ad6352.
//
// Solidity: function queryPermission(address contractAddr) constant returns(string)
func (_Permission *PermissionSession) QueryPermission(contractAddr common.Address) (string, error) {
	return _Permission.Contract.QueryPermission(&_Permission.CallOpts, contractAddr)
}

// QueryPermission is a free data retrieval call binding the contract method 0x54ad6352.
//
// Solidity: function queryPermission(address contractAddr) constant returns(string)
func (_Permission *PermissionCallerSession) QueryPermission(contractAddr common.Address) (string, error) {
	return _Permission.Contract.QueryPermission(&_Permission.CallOpts, contractAddr)
}

// GrantWrite is a paid mutator transaction binding the contract method 0x96ec37c4.
//
// Solidity: function grantWrite(address contractAddr, address user) returns(int256)
func (_Permission *PermissionTransactor) GrantWrite(opts *bind.TransactOpts, contractAddr common.Address, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _Permission.contract.Transact(opts, "grantWrite", contractAddr, user)
}

// GrantWrite is a paid mutator transaction binding the contract method 0x96ec37c4.
//
// Solidity: function grantWrite(address contractAddr, address user) returns(int256)
func (_Permission *PermissionSession) GrantWrite(contractAddr common.Address, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.GrantWrite(&_Permission.TransactOpts, contractAddr, user)
}

// GrantWrite is a paid mutator transaction binding the contract method 0x96ec37c4.
//
// Solidity: function grantWrite(address contractAddr, address user) returns(int256)
func (_Permission *PermissionTransactorSession) GrantWrite(contractAddr common.Address, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.GrantWrite(&_Permission.TransactOpts, contractAddr, user)
}

// Insert is a paid mutator transaction binding the contract method 0x06e63ff8.
//
// Solidity: function insert(string table_name, string addr) returns(int256)
func (_Permission *PermissionTransactor) Insert(opts *bind.TransactOpts, table_name string, addr string) (*types.Transaction, *types.Receipt, error) {
	return _Permission.contract.Transact(opts, "insert", table_name, addr)
}

// Insert is a paid mutator transaction binding the contract method 0x06e63ff8.
//
// Solidity: function insert(string table_name, string addr) returns(int256)
func (_Permission *PermissionSession) Insert(table_name string, addr string) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.Insert(&_Permission.TransactOpts, table_name, addr)
}

// Insert is a paid mutator transaction binding the contract method 0x06e63ff8.
//
// Solidity: function insert(string table_name, string addr) returns(int256)
func (_Permission *PermissionTransactorSession) Insert(table_name string, addr string) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.Insert(&_Permission.TransactOpts, table_name, addr)
}

// Remove is a paid mutator transaction binding the contract method 0x44590a7e.
//
// Solidity: function remove(string table_name, string addr) returns(int256)
func (_Permission *PermissionTransactor) Remove(opts *bind.TransactOpts, table_name string, addr string) (*types.Transaction, *types.Receipt, error) {
	return _Permission.contract.Transact(opts, "remove", table_name, addr)
}

// Remove is a paid mutator transaction binding the contract method 0x44590a7e.
//
// Solidity: function remove(string table_name, string addr) returns(int256)
func (_Permission *PermissionSession) Remove(table_name string, addr string) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.Remove(&_Permission.TransactOpts, table_name, addr)
}

// Remove is a paid mutator transaction binding the contract method 0x44590a7e.
//
// Solidity: function remove(string table_name, string addr) returns(int256)
func (_Permission *PermissionTransactorSession) Remove(table_name string, addr string) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.Remove(&_Permission.TransactOpts, table_name, addr)
}

// RevokeWrite is a paid mutator transaction binding the contract method 0x99c26010.
//
// Solidity: function revokeWrite(address contractAddr, address user) returns(int256)
func (_Permission *PermissionTransactor) RevokeWrite(opts *bind.TransactOpts, contractAddr common.Address, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _Permission.contract.Transact(opts, "revokeWrite", contractAddr, user)
}

// RevokeWrite is a paid mutator transaction binding the contract method 0x99c26010.
//
// Solidity: function revokeWrite(address contractAddr, address user) returns(int256)
func (_Permission *PermissionSession) RevokeWrite(contractAddr common.Address, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.RevokeWrite(&_Permission.TransactOpts, contractAddr, user)
}

// RevokeWrite is a paid mutator transaction binding the contract method 0x99c26010.
//
// Solidity: function revokeWrite(address contractAddr, address user) returns(int256)
func (_Permission *PermissionTransactorSession) RevokeWrite(contractAddr common.Address, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _Permission.Contract.RevokeWrite(&_Permission.TransactOpts, contractAddr, user)
}
