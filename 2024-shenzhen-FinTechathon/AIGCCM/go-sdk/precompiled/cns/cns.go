// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package cns

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

// CnsABI is the input ABI used to generate the binding from.
const CnsABI = "[{\"constant\":true,\"inputs\":[{\"name\":\"name\",\"type\":\"string\"}],\"name\":\"selectByName\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"version\",\"type\":\"string\"}],\"name\":\"selectByNameAndVersion\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"version\",\"type\":\"string\"},{\"name\":\"addr\",\"type\":\"string\"},{\"name\":\"abi\",\"type\":\"string\"}],\"name\":\"insert\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"version\",\"type\":\"string\"}],\"name\":\"getContractAddress\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]"

// Cns is an auto generated Go binding around a Solidity contract.
type Cns struct {
	CnsCaller     // Read-only binding to the contract
	CnsTransactor // Write-only binding to the contract
	CnsFilterer   // Log filterer for contract events
}

// CnsCaller is an auto generated read-only Go binding around a Solidity contract.
type CnsCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// CnsTransactor is an auto generated write-only Go binding around a Solidity contract.
type CnsTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// CnsFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type CnsFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// CnsSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type CnsSession struct {
	Contract     *Cns              // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// CnsCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type CnsCallerSession struct {
	Contract *CnsCaller    // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts // Call options to use throughout this session
}

// CnsTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type CnsTransactorSession struct {
	Contract     *CnsTransactor    // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// CnsRaw is an auto generated low-level Go binding around a Solidity contract.
type CnsRaw struct {
	Contract *Cns // Generic contract binding to access the raw methods on
}

// CnsCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type CnsCallerRaw struct {
	Contract *CnsCaller // Generic read-only contract binding to access the raw methods on
}

// CnsTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type CnsTransactorRaw struct {
	Contract *CnsTransactor // Generic write-only contract binding to access the raw methods on
}

// NewCns creates a new instance of Cns, bound to a specific deployed contract.
func NewCns(address common.Address, backend bind.ContractBackend) (*Cns, error) {
	contract, err := bindCns(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &Cns{CnsCaller: CnsCaller{contract: contract}, CnsTransactor: CnsTransactor{contract: contract}, CnsFilterer: CnsFilterer{contract: contract}}, nil
}

// NewCnsCaller creates a new read-only instance of Cns, bound to a specific deployed contract.
func NewCnsCaller(address common.Address, caller bind.ContractCaller) (*CnsCaller, error) {
	contract, err := bindCns(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &CnsCaller{contract: contract}, nil
}

// NewCnsTransactor creates a new write-only instance of Cns, bound to a specific deployed contract.
func NewCnsTransactor(address common.Address, transactor bind.ContractTransactor) (*CnsTransactor, error) {
	contract, err := bindCns(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &CnsTransactor{contract: contract}, nil
}

// NewCnsFilterer creates a new log filterer instance of Cns, bound to a specific deployed contract.
func NewCnsFilterer(address common.Address, filterer bind.ContractFilterer) (*CnsFilterer, error) {
	contract, err := bindCns(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &CnsFilterer{contract: contract}, nil
}

// bindCns binds a generic wrapper to an already deployed contract.
func bindCns(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(CnsABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Cns *CnsRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Cns.Contract.CnsCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Cns *CnsRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Cns.Contract.CnsTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Cns *CnsRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Cns.Contract.CnsTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Cns *CnsCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Cns.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Cns *CnsTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Cns.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Cns *CnsTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Cns.Contract.contract.Transact(opts, method, params...)
}

// GetContractAddress is a free data retrieval call binding the contract method 0xf85f8126.
//
// Solidity: function getContractAddress(string name, string version) constant returns(address)
func (_Cns *CnsCaller) GetContractAddress(opts *bind.CallOpts, name string, version string) (common.Address, error) {
	var (
		ret0 = new(common.Address)
	)
	out := ret0
	err := _Cns.contract.Call(opts, out, "getContractAddress", name, version)
	return *ret0, err
}

// GetContractAddress is a free data retrieval call binding the contract method 0xf85f8126.
//
// Solidity: function getContractAddress(string name, string version) constant returns(address)
func (_Cns *CnsSession) GetContractAddress(name string, version string) (common.Address, error) {
	return _Cns.Contract.GetContractAddress(&_Cns.CallOpts, name, version)
}

// GetContractAddress is a free data retrieval call binding the contract method 0xf85f8126.
//
// Solidity: function getContractAddress(string name, string version) constant returns(address)
func (_Cns *CnsCallerSession) GetContractAddress(name string, version string) (common.Address, error) {
	return _Cns.Contract.GetContractAddress(&_Cns.CallOpts, name, version)
}

// SelectByName is a free data retrieval call binding the contract method 0x819a3d62.
//
// Solidity: function selectByName(string name) constant returns(string)
func (_Cns *CnsCaller) SelectByName(opts *bind.CallOpts, name string) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Cns.contract.Call(opts, out, "selectByName", name)
	return *ret0, err
}

// SelectByName is a free data retrieval call binding the contract method 0x819a3d62.
//
// Solidity: function selectByName(string name) constant returns(string)
func (_Cns *CnsSession) SelectByName(name string) (string, error) {
	return _Cns.Contract.SelectByName(&_Cns.CallOpts, name)
}

// SelectByName is a free data retrieval call binding the contract method 0x819a3d62.
//
// Solidity: function selectByName(string name) constant returns(string)
func (_Cns *CnsCallerSession) SelectByName(name string) (string, error) {
	return _Cns.Contract.SelectByName(&_Cns.CallOpts, name)
}

// SelectByNameAndVersion is a free data retrieval call binding the contract method 0x897f0251.
//
// Solidity: function selectByNameAndVersion(string name, string version) constant returns(string)
func (_Cns *CnsCaller) SelectByNameAndVersion(opts *bind.CallOpts, name string, version string) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Cns.contract.Call(opts, out, "selectByNameAndVersion", name, version)
	return *ret0, err
}

// SelectByNameAndVersion is a free data retrieval call binding the contract method 0x897f0251.
//
// Solidity: function selectByNameAndVersion(string name, string version) constant returns(string)
func (_Cns *CnsSession) SelectByNameAndVersion(name string, version string) (string, error) {
	return _Cns.Contract.SelectByNameAndVersion(&_Cns.CallOpts, name, version)
}

// SelectByNameAndVersion is a free data retrieval call binding the contract method 0x897f0251.
//
// Solidity: function selectByNameAndVersion(string name, string version) constant returns(string)
func (_Cns *CnsCallerSession) SelectByNameAndVersion(name string, version string) (string, error) {
	return _Cns.Contract.SelectByNameAndVersion(&_Cns.CallOpts, name, version)
}

// Insert is a paid mutator transaction binding the contract method 0xa216464b.
//
// Solidity: function insert(string name, string version, string addr, string abi) returns(uint256)
func (_Cns *CnsTransactor) Insert(opts *bind.TransactOpts, name string, version string, addr string, abi string) (*types.Transaction, *types.Receipt, error) {
	return _Cns.contract.Transact(opts, "insert", name, version, addr, abi)
}

// Insert is a paid mutator transaction binding the contract method 0xa216464b.
//
// Solidity: function insert(string name, string version, string addr, string abi) returns(uint256)
func (_Cns *CnsSession) Insert(name string, version string, addr string, abi string) (*types.Transaction, *types.Receipt, error) {
	return _Cns.Contract.Insert(&_Cns.TransactOpts, name, version, addr, abi)
}

// Insert is a paid mutator transaction binding the contract method 0xa216464b.
//
// Solidity: function insert(string name, string version, string addr, string abi) returns(uint256)
func (_Cns *CnsTransactorSession) Insert(name string, version string, addr string, abi string) (*types.Transaction, *types.Receipt, error) {
	return _Cns.Contract.Insert(&_Cns.TransactOpts, name, version, addr, abi)
}
