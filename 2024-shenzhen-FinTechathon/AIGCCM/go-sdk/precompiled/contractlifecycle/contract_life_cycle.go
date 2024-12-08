// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package contractlifecycle

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

// ContractLifeCycleABI is the input ABI used to generate the binding from.
const ContractLifeCycleABI = "[{\"constant\":true,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"getStatus\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"unfreeze\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"freeze\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"contractAddr\",\"type\":\"address\"},{\"name\":\"userAddr\",\"type\":\"address\"}],\"name\":\"grantManager\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"listManager\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"},{\"name\":\"\",\"type\":\"address[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]"

// ContractLifeCycle is an auto generated Go binding around a Solidity contract.
type ContractLifeCycle struct {
	ContractLifeCycleCaller     // Read-only binding to the contract
	ContractLifeCycleTransactor // Write-only binding to the contract
	ContractLifeCycleFilterer   // Log filterer for contract events
}

// ContractLifeCycleCaller is an auto generated read-only Go binding around a Solidity contract.
type ContractLifeCycleCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ContractLifeCycleTransactor is an auto generated write-only Go binding around a Solidity contract.
type ContractLifeCycleTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ContractLifeCycleFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type ContractLifeCycleFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ContractLifeCycleSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type ContractLifeCycleSession struct {
	Contract     *ContractLifeCycle // Generic contract binding to set the session for
	CallOpts     bind.CallOpts      // Call options to use throughout this session
	TransactOpts bind.TransactOpts  // Transaction auth options to use throughout this session
}

// ContractLifeCycleCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type ContractLifeCycleCallerSession struct {
	Contract *ContractLifeCycleCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts            // Call options to use throughout this session
}

// ContractLifeCycleTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type ContractLifeCycleTransactorSession struct {
	Contract     *ContractLifeCycleTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts            // Transaction auth options to use throughout this session
}

// ContractLifeCycleRaw is an auto generated low-level Go binding around a Solidity contract.
type ContractLifeCycleRaw struct {
	Contract *ContractLifeCycle // Generic contract binding to access the raw methods on
}

// ContractLifeCycleCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type ContractLifeCycleCallerRaw struct {
	Contract *ContractLifeCycleCaller // Generic read-only contract binding to access the raw methods on
}

// ContractLifeCycleTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type ContractLifeCycleTransactorRaw struct {
	Contract *ContractLifeCycleTransactor // Generic write-only contract binding to access the raw methods on
}

// NewContractLifeCycle creates a new instance of ContractLifeCycle, bound to a specific deployed contract.
func NewContractLifeCycle(address common.Address, backend bind.ContractBackend) (*ContractLifeCycle, error) {
	contract, err := bindContractLifeCycle(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &ContractLifeCycle{ContractLifeCycleCaller: ContractLifeCycleCaller{contract: contract}, ContractLifeCycleTransactor: ContractLifeCycleTransactor{contract: contract}, ContractLifeCycleFilterer: ContractLifeCycleFilterer{contract: contract}}, nil
}

// NewContractLifeCycleCaller creates a new read-only instance of ContractLifeCycle, bound to a specific deployed contract.
func NewContractLifeCycleCaller(address common.Address, caller bind.ContractCaller) (*ContractLifeCycleCaller, error) {
	contract, err := bindContractLifeCycle(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &ContractLifeCycleCaller{contract: contract}, nil
}

// NewContractLifeCycleTransactor creates a new write-only instance of ContractLifeCycle, bound to a specific deployed contract.
func NewContractLifeCycleTransactor(address common.Address, transactor bind.ContractTransactor) (*ContractLifeCycleTransactor, error) {
	contract, err := bindContractLifeCycle(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &ContractLifeCycleTransactor{contract: contract}, nil
}

// NewContractLifeCycleFilterer creates a new log filterer instance of ContractLifeCycle, bound to a specific deployed contract.
func NewContractLifeCycleFilterer(address common.Address, filterer bind.ContractFilterer) (*ContractLifeCycleFilterer, error) {
	contract, err := bindContractLifeCycle(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &ContractLifeCycleFilterer{contract: contract}, nil
}

// bindContractLifeCycle binds a generic wrapper to an already deployed contract.
func bindContractLifeCycle(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(ContractLifeCycleABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_ContractLifeCycle *ContractLifeCycleRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _ContractLifeCycle.Contract.ContractLifeCycleCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_ContractLifeCycle *ContractLifeCycleRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.ContractLifeCycleTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_ContractLifeCycle *ContractLifeCycleRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.ContractLifeCycleTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_ContractLifeCycle *ContractLifeCycleCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _ContractLifeCycle.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_ContractLifeCycle *ContractLifeCycleTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_ContractLifeCycle *ContractLifeCycleTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.contract.Transact(opts, method, params...)
}

// GetStatus is a free data retrieval call binding the contract method 0x30ccebb5.
//
// Solidity: function getStatus(address addr) constant returns(int256, string)
func (_ContractLifeCycle *ContractLifeCycleCaller) GetStatus(opts *bind.CallOpts, addr common.Address) (*big.Int, string, error) {
	var (
		ret0 = new(*big.Int)
		ret1 = new(string)
	)
	out := &[]interface{}{
		ret0,
		ret1,
	}
	err := _ContractLifeCycle.contract.Call(opts, out, "getStatus", addr)
	return *ret0, *ret1, err
}

// GetStatus is a free data retrieval call binding the contract method 0x30ccebb5.
//
// Solidity: function getStatus(address addr) constant returns(int256, string)
func (_ContractLifeCycle *ContractLifeCycleSession) GetStatus(addr common.Address) (*big.Int, string, error) {
	return _ContractLifeCycle.Contract.GetStatus(&_ContractLifeCycle.CallOpts, addr)
}

// GetStatus is a free data retrieval call binding the contract method 0x30ccebb5.
//
// Solidity: function getStatus(address addr) constant returns(int256, string)
func (_ContractLifeCycle *ContractLifeCycleCallerSession) GetStatus(addr common.Address) (*big.Int, string, error) {
	return _ContractLifeCycle.Contract.GetStatus(&_ContractLifeCycle.CallOpts, addr)
}

// ListManager is a free data retrieval call binding the contract method 0xc5252d0e.
//
// Solidity: function listManager(address addr) constant returns(int256, address[])
func (_ContractLifeCycle *ContractLifeCycleCaller) ListManager(opts *bind.CallOpts, addr common.Address) (*big.Int, []common.Address, error) {
	var (
		ret0 = new(*big.Int)
		ret1 = new([]common.Address)
	)
	out := &[]interface{}{
		ret0,
		ret1,
	}
	err := _ContractLifeCycle.contract.Call(opts, out, "listManager", addr)
	return *ret0, *ret1, err
}

// ListManager is a free data retrieval call binding the contract method 0xc5252d0e.
//
// Solidity: function listManager(address addr) constant returns(int256, address[])
func (_ContractLifeCycle *ContractLifeCycleSession) ListManager(addr common.Address) (*big.Int, []common.Address, error) {
	return _ContractLifeCycle.Contract.ListManager(&_ContractLifeCycle.CallOpts, addr)
}

// ListManager is a free data retrieval call binding the contract method 0xc5252d0e.
//
// Solidity: function listManager(address addr) constant returns(int256, address[])
func (_ContractLifeCycle *ContractLifeCycleCallerSession) ListManager(addr common.Address) (*big.Int, []common.Address, error) {
	return _ContractLifeCycle.Contract.ListManager(&_ContractLifeCycle.CallOpts, addr)
}

// Freeze is a paid mutator transaction binding the contract method 0x8d1fdf2f.
//
// Solidity: function freeze(address addr) returns(int256)
func (_ContractLifeCycle *ContractLifeCycleTransactor) Freeze(opts *bind.TransactOpts, addr common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.contract.Transact(opts, "freeze", addr)
}

// Freeze is a paid mutator transaction binding the contract method 0x8d1fdf2f.
//
// Solidity: function freeze(address addr) returns(int256)
func (_ContractLifeCycle *ContractLifeCycleSession) Freeze(addr common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.Freeze(&_ContractLifeCycle.TransactOpts, addr)
}

// Freeze is a paid mutator transaction binding the contract method 0x8d1fdf2f.
//
// Solidity: function freeze(address addr) returns(int256)
func (_ContractLifeCycle *ContractLifeCycleTransactorSession) Freeze(addr common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.Freeze(&_ContractLifeCycle.TransactOpts, addr)
}

// GrantManager is a paid mutator transaction binding the contract method 0xa721fb43.
//
// Solidity: function grantManager(address contractAddr, address userAddr) returns(int256)
func (_ContractLifeCycle *ContractLifeCycleTransactor) GrantManager(opts *bind.TransactOpts, contractAddr common.Address, userAddr common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.contract.Transact(opts, "grantManager", contractAddr, userAddr)
}

// GrantManager is a paid mutator transaction binding the contract method 0xa721fb43.
//
// Solidity: function grantManager(address contractAddr, address userAddr) returns(int256)
func (_ContractLifeCycle *ContractLifeCycleSession) GrantManager(contractAddr common.Address, userAddr common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.GrantManager(&_ContractLifeCycle.TransactOpts, contractAddr, userAddr)
}

// GrantManager is a paid mutator transaction binding the contract method 0xa721fb43.
//
// Solidity: function grantManager(address contractAddr, address userAddr) returns(int256)
func (_ContractLifeCycle *ContractLifeCycleTransactorSession) GrantManager(contractAddr common.Address, userAddr common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.GrantManager(&_ContractLifeCycle.TransactOpts, contractAddr, userAddr)
}

// Unfreeze is a paid mutator transaction binding the contract method 0x45c8b1a6.
//
// Solidity: function unfreeze(address addr) returns(int256)
func (_ContractLifeCycle *ContractLifeCycleTransactor) Unfreeze(opts *bind.TransactOpts, addr common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.contract.Transact(opts, "unfreeze", addr)
}

// Unfreeze is a paid mutator transaction binding the contract method 0x45c8b1a6.
//
// Solidity: function unfreeze(address addr) returns(int256)
func (_ContractLifeCycle *ContractLifeCycleSession) Unfreeze(addr common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.Unfreeze(&_ContractLifeCycle.TransactOpts, addr)
}

// Unfreeze is a paid mutator transaction binding the contract method 0x45c8b1a6.
//
// Solidity: function unfreeze(address addr) returns(int256)
func (_ContractLifeCycle *ContractLifeCycleTransactorSession) Unfreeze(addr common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ContractLifeCycle.Contract.Unfreeze(&_ContractLifeCycle.TransactOpts, addr)
}
