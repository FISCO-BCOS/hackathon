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

// TableFactoryABI is the input ABI used to generate the binding from.
const TableFactoryABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"tableName\",\"type\":\"string\"},{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"valueField\",\"type\":\"string\"}],\"name\":\"createTable\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"

// TableFactory is an auto generated Go binding around a Solidity contract.
type TableFactory struct {
	TableFactoryCaller     // Read-only binding to the contract
	TableFactoryTransactor // Write-only binding to the contract
	TableFactoryFilterer   // Log filterer for contract events
}

// TableFactoryCaller is an auto generated read-only Go binding around a Solidity contract.
type TableFactoryCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// TableFactoryTransactor is an auto generated write-only Go binding around a Solidity contract.
type TableFactoryTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// TableFactoryFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type TableFactoryFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// TableFactorySession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type TableFactorySession struct {
	Contract     *TableFactory     // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// TableFactoryCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type TableFactoryCallerSession struct {
	Contract *TableFactoryCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts       // Call options to use throughout this session
}

// TableFactoryTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type TableFactoryTransactorSession struct {
	Contract     *TableFactoryTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts       // Transaction auth options to use throughout this session
}

// TableFactoryRaw is an auto generated low-level Go binding around a Solidity contract.
type TableFactoryRaw struct {
	Contract *TableFactory // Generic contract binding to access the raw methods on
}

// TableFactoryCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type TableFactoryCallerRaw struct {
	Contract *TableFactoryCaller // Generic read-only contract binding to access the raw methods on
}

// TableFactoryTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type TableFactoryTransactorRaw struct {
	Contract *TableFactoryTransactor // Generic write-only contract binding to access the raw methods on
}

// NewTableFactory creates a new instance of TableFactory, bound to a specific deployed contract.
func NewTableFactory(address common.Address, backend bind.ContractBackend) (*TableFactory, error) {
	contract, err := bindTableFactory(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &TableFactory{TableFactoryCaller: TableFactoryCaller{contract: contract}, TableFactoryTransactor: TableFactoryTransactor{contract: contract}, TableFactoryFilterer: TableFactoryFilterer{contract: contract}}, nil
}

// NewTableFactoryCaller creates a new read-only instance of TableFactory, bound to a specific deployed contract.
func NewTableFactoryCaller(address common.Address, caller bind.ContractCaller) (*TableFactoryCaller, error) {
	contract, err := bindTableFactory(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &TableFactoryCaller{contract: contract}, nil
}

// NewTableFactoryTransactor creates a new write-only instance of TableFactory, bound to a specific deployed contract.
func NewTableFactoryTransactor(address common.Address, transactor bind.ContractTransactor) (*TableFactoryTransactor, error) {
	contract, err := bindTableFactory(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &TableFactoryTransactor{contract: contract}, nil
}

// NewTableFactoryFilterer creates a new log filterer instance of TableFactory, bound to a specific deployed contract.
func NewTableFactoryFilterer(address common.Address, filterer bind.ContractFilterer) (*TableFactoryFilterer, error) {
	contract, err := bindTableFactory(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &TableFactoryFilterer{contract: contract}, nil
}

// bindTableFactory binds a generic wrapper to an already deployed contract.
func bindTableFactory(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(TableFactoryABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_TableFactory *TableFactoryRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _TableFactory.Contract.TableFactoryCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_TableFactory *TableFactoryRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _TableFactory.Contract.TableFactoryTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_TableFactory *TableFactoryRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _TableFactory.Contract.TableFactoryTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_TableFactory *TableFactoryCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _TableFactory.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_TableFactory *TableFactoryTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _TableFactory.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_TableFactory *TableFactoryTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _TableFactory.Contract.contract.Transact(opts, method, params...)
}

// CreateTable is a paid mutator transaction binding the contract method 0x56004b6a.
//
// Solidity: function createTable(string tableName, string key, string valueField) returns(int256)
func (_TableFactory *TableFactoryTransactor) CreateTable(opts *bind.TransactOpts, tableName string, key string, valueField string) (*types.Transaction, *types.Receipt, error) {
	return _TableFactory.contract.Transact(opts, "createTable", tableName, key, valueField)
}

func (_TableFactory *TableFactoryTransactor) AsyncCreateTable(handler func(*types.Receipt, error), opts *bind.TransactOpts, tableName string, key string, valueField string) (*types.Transaction, error) {
	return _TableFactory.contract.AsyncTransact(opts, handler, "createTable", tableName, key, valueField)
}

// CreateTable is a paid mutator transaction binding the contract method 0x56004b6a.
//
// Solidity: function createTable(string tableName, string key, string valueField) returns(int256)
func (_TableFactory *TableFactorySession) CreateTable(tableName string, key string, valueField string) (*types.Transaction, *types.Receipt, error) {
	return _TableFactory.Contract.CreateTable(&_TableFactory.TransactOpts, tableName, key, valueField)
}

func (_TableFactory *TableFactorySession) AsyncCreateTable(handler func(*types.Receipt, error), tableName string, key string, valueField string) (*types.Transaction, error) {
	return _TableFactory.Contract.AsyncCreateTable(handler, &_TableFactory.TransactOpts, tableName, key, valueField)
}

// CreateTable is a paid mutator transaction binding the contract method 0x56004b6a.
//
// Solidity: function createTable(string tableName, string key, string valueField) returns(int256)
func (_TableFactory *TableFactoryTransactorSession) CreateTable(tableName string, key string, valueField string) (*types.Transaction, *types.Receipt, error) {
	return _TableFactory.Contract.CreateTable(&_TableFactory.TransactOpts, tableName, key, valueField)
}

func (_TableFactory *TableFactoryTransactorSession) AsyncCreateTable(handler func(*types.Receipt, error), tableName string, key string, valueField string) (*types.Transaction, error) {
	return _TableFactory.Contract.AsyncCreateTable(handler, &_TableFactory.TransactOpts, tableName, key, valueField)
}
