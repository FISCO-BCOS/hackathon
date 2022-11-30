// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package config

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

// ConfigABI is the input ABI used to generate the binding from.
const ConfigABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"string\"}],\"name\":\"setValueByKey\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"

// Config is an auto generated Go binding around a Solidity contract.
type Config struct {
	ConfigCaller     // Read-only binding to the contract
	ConfigTransactor // Write-only binding to the contract
	ConfigFilterer   // Log filterer for contract events
}

// ConfigCaller is an auto generated read-only Go binding around a Solidity contract.
type ConfigCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ConfigTransactor is an auto generated write-only Go binding around a Solidity contract.
type ConfigTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ConfigFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type ConfigFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ConfigSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type ConfigSession struct {
	Contract     *Config           // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// ConfigCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type ConfigCallerSession struct {
	Contract *ConfigCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts // Call options to use throughout this session
}

// ConfigTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type ConfigTransactorSession struct {
	Contract     *ConfigTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// ConfigRaw is an auto generated low-level Go binding around a Solidity contract.
type ConfigRaw struct {
	Contract *Config // Generic contract binding to access the raw methods on
}

// ConfigCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type ConfigCallerRaw struct {
	Contract *ConfigCaller // Generic read-only contract binding to access the raw methods on
}

// ConfigTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type ConfigTransactorRaw struct {
	Contract *ConfigTransactor // Generic write-only contract binding to access the raw methods on
}

// NewConfig creates a new instance of Config, bound to a specific deployed contract.
func NewConfig(address common.Address, backend bind.ContractBackend) (*Config, error) {
	contract, err := bindConfig(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &Config{ConfigCaller: ConfigCaller{contract: contract}, ConfigTransactor: ConfigTransactor{contract: contract}, ConfigFilterer: ConfigFilterer{contract: contract}}, nil
}

// NewConfigCaller creates a new read-only instance of Config, bound to a specific deployed contract.
func NewConfigCaller(address common.Address, caller bind.ContractCaller) (*ConfigCaller, error) {
	contract, err := bindConfig(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &ConfigCaller{contract: contract}, nil
}

// NewConfigTransactor creates a new write-only instance of Config, bound to a specific deployed contract.
func NewConfigTransactor(address common.Address, transactor bind.ContractTransactor) (*ConfigTransactor, error) {
	contract, err := bindConfig(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &ConfigTransactor{contract: contract}, nil
}

// NewConfigFilterer creates a new log filterer instance of Config, bound to a specific deployed contract.
func NewConfigFilterer(address common.Address, filterer bind.ContractFilterer) (*ConfigFilterer, error) {
	contract, err := bindConfig(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &ConfigFilterer{contract: contract}, nil
}

// bindConfig binds a generic wrapper to an already deployed contract.
func bindConfig(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(ConfigABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Config *ConfigRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Config.Contract.ConfigCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Config *ConfigRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Config.Contract.ConfigTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Config *ConfigRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Config.Contract.ConfigTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Config *ConfigCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Config.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Config *ConfigTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Config.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Config *ConfigTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Config.Contract.contract.Transact(opts, method, params...)
}

// SetValueByKey is a paid mutator transaction binding the contract method 0xbd291aef.
//
// Solidity: function setValueByKey(string key, string value) returns(int256)
func (_Config *ConfigTransactor) SetValueByKey(opts *bind.TransactOpts, key string, value string) (*types.Transaction, *types.Receipt, error) {
	return _Config.contract.Transact(opts, "setValueByKey", key, value)
}

// SetValueByKey is a paid mutator transaction binding the contract method 0xbd291aef.
//
// Solidity: function setValueByKey(string key, string value) returns(int256)
func (_Config *ConfigSession) SetValueByKey(key string, value string) (*types.Transaction, *types.Receipt, error) {
	return _Config.Contract.SetValueByKey(&_Config.TransactOpts, key, value)
}

// SetValueByKey is a paid mutator transaction binding the contract method 0xbd291aef.
//
// Solidity: function setValueByKey(string key, string value) returns(int256)
func (_Config *ConfigTransactorSession) SetValueByKey(key string, value string) (*types.Transaction, *types.Receipt, error) {
	return _Config.Contract.SetValueByKey(&_Config.TransactOpts, key, value)
}
