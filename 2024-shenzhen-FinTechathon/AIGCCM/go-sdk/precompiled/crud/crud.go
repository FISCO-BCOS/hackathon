// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package crud

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

// CrudABI is the input ABI used to generate the binding from.
const CrudABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"tableName\",\"type\":\"string\"},{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"entry\",\"type\":\"string\"},{\"name\":\"condition\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"}],\"name\":\"update\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"tableName\",\"type\":\"string\"}],\"name\":\"desc\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"tableName\",\"type\":\"string\"},{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"condition\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"}],\"name\":\"select\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"tableName\",\"type\":\"string\"},{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"entry\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"}],\"name\":\"insert\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"tableName\",\"type\":\"string\"},{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"condition\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"}],\"name\":\"remove\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"

// Crud is an auto generated Go binding around a Solidity contract.
type Crud struct {
	CrudCaller     // Read-only binding to the contract
	CrudTransactor // Write-only binding to the contract
	CrudFilterer   // Log filterer for contract events
}

// CrudCaller is an auto generated read-only Go binding around a Solidity contract.
type CrudCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// CrudTransactor is an auto generated write-only Go binding around a Solidity contract.
type CrudTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// CrudFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type CrudFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// CrudSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type CrudSession struct {
	Contract     *Crud             // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// CrudCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type CrudCallerSession struct {
	Contract *CrudCaller   // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts // Call options to use throughout this session
}

// CrudTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type CrudTransactorSession struct {
	Contract     *CrudTransactor   // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// CrudRaw is an auto generated low-level Go binding around a Solidity contract.
type CrudRaw struct {
	Contract *Crud // Generic contract binding to access the raw methods on
}

// CrudCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type CrudCallerRaw struct {
	Contract *CrudCaller // Generic read-only contract binding to access the raw methods on
}

// CrudTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type CrudTransactorRaw struct {
	Contract *CrudTransactor // Generic write-only contract binding to access the raw methods on
}

// NewCrud creates a new instance of Crud, bound to a specific deployed contract.
func NewCrud(address common.Address, backend bind.ContractBackend) (*Crud, error) {
	contract, err := bindCrud(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &Crud{CrudCaller: CrudCaller{contract: contract}, CrudTransactor: CrudTransactor{contract: contract}, CrudFilterer: CrudFilterer{contract: contract}}, nil
}

// NewCrudCaller creates a new read-only instance of Crud, bound to a specific deployed contract.
func NewCrudCaller(address common.Address, caller bind.ContractCaller) (*CrudCaller, error) {
	contract, err := bindCrud(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &CrudCaller{contract: contract}, nil
}

// NewCrudTransactor creates a new write-only instance of Crud, bound to a specific deployed contract.
func NewCrudTransactor(address common.Address, transactor bind.ContractTransactor) (*CrudTransactor, error) {
	contract, err := bindCrud(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &CrudTransactor{contract: contract}, nil
}

// NewCrudFilterer creates a new log filterer instance of Crud, bound to a specific deployed contract.
func NewCrudFilterer(address common.Address, filterer bind.ContractFilterer) (*CrudFilterer, error) {
	contract, err := bindCrud(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &CrudFilterer{contract: contract}, nil
}

// bindCrud binds a generic wrapper to an already deployed contract.
func bindCrud(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(CrudABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Crud *CrudRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Crud.Contract.CrudCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Crud *CrudRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.CrudTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Crud *CrudRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.CrudTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Crud *CrudCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Crud.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Crud *CrudTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Crud *CrudTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.contract.Transact(opts, method, params...)
}

// Desc is a free data retrieval call binding the contract method 0x5d0d6d54.
//
// Solidity: function desc(string tableName) constant returns(string, string)
func (_Crud *CrudCaller) Desc(opts *bind.CallOpts, tableName string) (string, string, error) {
	var (
		ret0 = new(string)
		ret1 = new(string)
	)
	out := &[]interface{}{
		ret0,
		ret1,
	}
	err := _Crud.contract.Call(opts, out, "desc", tableName)
	return *ret0, *ret1, err
}

// Desc is a free data retrieval call binding the contract method 0x5d0d6d54.
//
// Solidity: function desc(string tableName) constant returns(string, string)
func (_Crud *CrudSession) Desc(tableName string) (string, string, error) {
	return _Crud.Contract.Desc(&_Crud.CallOpts, tableName)
}

// Desc is a free data retrieval call binding the contract method 0x5d0d6d54.
//
// Solidity: function desc(string tableName) constant returns(string, string)
func (_Crud *CrudCallerSession) Desc(tableName string) (string, string, error) {
	return _Crud.Contract.Desc(&_Crud.CallOpts, tableName)
}

// Select is a free data retrieval call binding the contract method 0x983c6c4f.
//
// Solidity: function select(string tableName, string key, string condition, string ) constant returns(string)
func (_Crud *CrudCaller) Select(opts *bind.CallOpts, tableName string, key string, condition string, arg3 string) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Crud.contract.Call(opts, out, "select", tableName, key, condition, arg3)
	return *ret0, err
}

// Select is a free data retrieval call binding the contract method 0x983c6c4f.
//
// Solidity: function select(string tableName, string key, string condition, string ) constant returns(string)
func (_Crud *CrudSession) Select(tableName string, key string, condition string, arg3 string) (string, error) {
	return _Crud.Contract.Select(&_Crud.CallOpts, tableName, key, condition, arg3)
}

// Select is a free data retrieval call binding the contract method 0x983c6c4f.
//
// Solidity: function select(string tableName, string key, string condition, string ) constant returns(string)
func (_Crud *CrudCallerSession) Select(tableName string, key string, condition string, arg3 string) (string, error) {
	return _Crud.Contract.Select(&_Crud.CallOpts, tableName, key, condition, arg3)
}

// Insert is a paid mutator transaction binding the contract method 0xa216464b.
//
// Solidity: function insert(string tableName, string key, string entry, string ) returns(int256)
func (_Crud *CrudTransactor) Insert(opts *bind.TransactOpts, tableName string, key string, entry string, arg3 string) (*types.Transaction, *types.Receipt, error) {
	return _Crud.contract.Transact(opts, "insert", tableName, key, entry, arg3)
}

func (_Crud *CrudTransactor) AsyncInsert(handler func(*types.Receipt, error), opts *bind.TransactOpts, tableName string, key string, entry string, arg3 string) (*types.Transaction, error) {
	return _Crud.contract.AsyncTransact(opts, handler, "insert", tableName, key, entry, arg3)
}

// Insert is a paid mutator transaction binding the contract method 0xa216464b.
//
// Solidity: function insert(string tableName, string key, string entry, string ) returns(int256)
func (_Crud *CrudSession) Insert(tableName string, key string, entry string, arg3 string) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.Insert(&_Crud.TransactOpts, tableName, key, entry, arg3)
}

func (_Crud *CrudSession) AsyncInsert(handler func(*types.Receipt, error), tableName string, key string, entry string, arg3 string) (*types.Transaction, error) {
	return _Crud.Contract.AsyncInsert(handler, &_Crud.TransactOpts, tableName, key, entry, arg3)
}

// Insert is a paid mutator transaction binding the contract method 0xa216464b.
//
// Solidity: function insert(string tableName, string key, string entry, string ) returns(int256)
func (_Crud *CrudTransactorSession) Insert(tableName string, key string, entry string, arg3 string) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.Insert(&_Crud.TransactOpts, tableName, key, entry, arg3)
}

func (_Crud *CrudTransactorSession) AsyncInsert(handler func(*types.Receipt, error), tableName string, key string, entry string, arg3 string) (*types.Transaction, error) {
	return _Crud.Contract.AsyncInsert(handler, &_Crud.TransactOpts, tableName, key, entry, arg3)
}

// Remove is a paid mutator transaction binding the contract method 0xa72a1e65.
//
// Solidity: function remove(string tableName, string key, string condition, string ) returns(int256)
func (_Crud *CrudTransactor) Remove(opts *bind.TransactOpts, tableName string, key string, condition string, arg3 string) (*types.Transaction, *types.Receipt, error) {
	return _Crud.contract.Transact(opts, "remove", tableName, key, condition, arg3)
}

func (_Crud *CrudTransactor) AsyncRemove(handler func(*types.Receipt, error), opts *bind.TransactOpts, tableName string, key string, condition string, arg3 string) (*types.Transaction, error) {
	return _Crud.contract.AsyncTransact(opts, handler, "remove", tableName, key, condition, arg3)
}

// Remove is a paid mutator transaction binding the contract method 0xa72a1e65.
//
// Solidity: function remove(string tableName, string key, string condition, string ) returns(int256)
func (_Crud *CrudSession) Remove(tableName string, key string, condition string, arg3 string) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.Remove(&_Crud.TransactOpts, tableName, key, condition, arg3)
}

func (_Crud *CrudSession) AsyncRemove(handler func(*types.Receipt, error), tableName string, key string, condition string, arg3 string) (*types.Transaction, error) {
	return _Crud.Contract.AsyncRemove(handler, &_Crud.TransactOpts, tableName, key, condition, arg3)
}

// Remove is a paid mutator transaction binding the contract method 0xa72a1e65.
//
// Solidity: function remove(string tableName, string key, string condition, string ) returns(int256)
func (_Crud *CrudTransactorSession) Remove(tableName string, key string, condition string, arg3 string) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.Remove(&_Crud.TransactOpts, tableName, key, condition, arg3)
}

func (_Crud *CrudTransactorSession) AsyncRemove(handler func(*types.Receipt, error), tableName string, key string, condition string, arg3 string) (*types.Transaction, error) {
	return _Crud.Contract.AsyncRemove(handler, &_Crud.TransactOpts, tableName, key, condition, arg3)
}

// Update is a paid mutator transaction binding the contract method 0x2dca76c1.
//
// Solidity: function update(string tableName, string key, string entry, string condition, string ) returns(int256)
func (_Crud *CrudTransactor) Update(opts *bind.TransactOpts, tableName string, key string, entry string, condition string, arg4 string) (*types.Transaction, *types.Receipt, error) {
	return _Crud.contract.Transact(opts, "update", tableName, key, entry, condition, arg4)
}

func (_Crud *CrudTransactor) AsyncUpdate(handler func(*types.Receipt, error), opts *bind.TransactOpts, tableName string, key string, entry string, condition string, arg4 string) (*types.Transaction, error) {
	return _Crud.contract.AsyncTransact(opts, handler, "update", tableName, key, entry, condition, arg4)
}

// Update is a paid mutator transaction binding the contract method 0x2dca76c1.
//
// Solidity: function update(string tableName, string key, string entry, string condition, string ) returns(int256)
func (_Crud *CrudSession) Update(tableName string, key string, entry string, condition string, arg4 string) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.Update(&_Crud.TransactOpts, tableName, key, entry, condition, arg4)
}

func (_Crud *CrudSession) AsyncUpdate(handler func(*types.Receipt, error), tableName string, key string, entry string, condition string, arg4 string) (*types.Transaction, error) {
	return _Crud.Contract.AsyncUpdate(handler, &_Crud.TransactOpts, tableName, key, entry, condition, arg4)
}

// Update is a paid mutator transaction binding the contract method 0x2dca76c1.
//
// Solidity: function update(string tableName, string key, string entry, string condition, string ) returns(int256)
func (_Crud *CrudTransactorSession) Update(tableName string, key string, entry string, condition string, arg4 string) (*types.Transaction, *types.Receipt, error) {
	return _Crud.Contract.Update(&_Crud.TransactOpts, tableName, key, entry, condition, arg4)
}

func (_Crud *CrudTransactorSession) AsyncUpdate(handler func(*types.Receipt, error), tableName string, key string, entry string, condition string, arg4 string) (*types.Transaction, error) {
	return _Crud.Contract.AsyncUpdate(handler, &_Crud.TransactOpts, tableName, key, entry, condition, arg4)
}
