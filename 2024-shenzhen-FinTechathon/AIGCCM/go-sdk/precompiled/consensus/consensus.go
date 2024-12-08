// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package consensus

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

// ConsensusABI is the input ABI used to generate the binding from.
const ConsensusABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"nodeID\",\"type\":\"string\"}],\"name\":\"addObserver\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"nodeID\",\"type\":\"string\"}],\"name\":\"remove\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"nodeID\",\"type\":\"string\"}],\"name\":\"addSealer\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"

// Consensus is an auto generated Go binding around a Solidity contract.
type Consensus struct {
	ConsensusCaller     // Read-only binding to the contract
	ConsensusTransactor // Write-only binding to the contract
	ConsensusFilterer   // Log filterer for contract events
}

// ConsensusCaller is an auto generated read-only Go binding around a Solidity contract.
type ConsensusCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ConsensusTransactor is an auto generated write-only Go binding around a Solidity contract.
type ConsensusTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ConsensusFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type ConsensusFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ConsensusSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type ConsensusSession struct {
	Contract     *Consensus        // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// ConsensusCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type ConsensusCallerSession struct {
	Contract *ConsensusCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts    // Call options to use throughout this session
}

// ConsensusTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type ConsensusTransactorSession struct {
	Contract     *ConsensusTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts    // Transaction auth options to use throughout this session
}

// ConsensusRaw is an auto generated low-level Go binding around a Solidity contract.
type ConsensusRaw struct {
	Contract *Consensus // Generic contract binding to access the raw methods on
}

// ConsensusCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type ConsensusCallerRaw struct {
	Contract *ConsensusCaller // Generic read-only contract binding to access the raw methods on
}

// ConsensusTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type ConsensusTransactorRaw struct {
	Contract *ConsensusTransactor // Generic write-only contract binding to access the raw methods on
}

// NewConsensus creates a new instance of Consensus, bound to a specific deployed contract.
func NewConsensus(address common.Address, backend bind.ContractBackend) (*Consensus, error) {
	contract, err := bindConsensus(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &Consensus{ConsensusCaller: ConsensusCaller{contract: contract}, ConsensusTransactor: ConsensusTransactor{contract: contract}, ConsensusFilterer: ConsensusFilterer{contract: contract}}, nil
}

// NewConsensusCaller creates a new read-only instance of Consensus, bound to a specific deployed contract.
func NewConsensusCaller(address common.Address, caller bind.ContractCaller) (*ConsensusCaller, error) {
	contract, err := bindConsensus(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &ConsensusCaller{contract: contract}, nil
}

// NewConsensusTransactor creates a new write-only instance of Consensus, bound to a specific deployed contract.
func NewConsensusTransactor(address common.Address, transactor bind.ContractTransactor) (*ConsensusTransactor, error) {
	contract, err := bindConsensus(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &ConsensusTransactor{contract: contract}, nil
}

// NewConsensusFilterer creates a new log filterer instance of Consensus, bound to a specific deployed contract.
func NewConsensusFilterer(address common.Address, filterer bind.ContractFilterer) (*ConsensusFilterer, error) {
	contract, err := bindConsensus(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &ConsensusFilterer{contract: contract}, nil
}

// bindConsensus binds a generic wrapper to an already deployed contract.
func bindConsensus(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(ConsensusABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Consensus *ConsensusRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Consensus.Contract.ConsensusCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Consensus *ConsensusRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.ConsensusTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Consensus *ConsensusRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.ConsensusTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Consensus *ConsensusCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Consensus.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Consensus *ConsensusTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Consensus *ConsensusTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.contract.Transact(opts, method, params...)
}

// AddObserver is a paid mutator transaction binding the contract method 0x2800efc0.
//
// Solidity: function addObserver(string nodeID) returns(int256)
func (_Consensus *ConsensusTransactor) AddObserver(opts *bind.TransactOpts, nodeID string) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.contract.Transact(opts, "addObserver", nodeID)
}

// AddObserver is a paid mutator transaction binding the contract method 0x2800efc0.
//
// Solidity: function addObserver(string nodeID) returns(int256)
func (_Consensus *ConsensusSession) AddObserver(nodeID string) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.AddObserver(&_Consensus.TransactOpts, nodeID)
}

// AddObserver is a paid mutator transaction binding the contract method 0x2800efc0.
//
// Solidity: function addObserver(string nodeID) returns(int256)
func (_Consensus *ConsensusTransactorSession) AddObserver(nodeID string) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.AddObserver(&_Consensus.TransactOpts, nodeID)
}

// AddSealer is a paid mutator transaction binding the contract method 0x89152d1f.
//
// Solidity: function addSealer(string nodeID) returns(int256)
func (_Consensus *ConsensusTransactor) AddSealer(opts *bind.TransactOpts, nodeID string) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.contract.Transact(opts, "addSealer", nodeID)
}

// AddSealer is a paid mutator transaction binding the contract method 0x89152d1f.
//
// Solidity: function addSealer(string nodeID) returns(int256)
func (_Consensus *ConsensusSession) AddSealer(nodeID string) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.AddSealer(&_Consensus.TransactOpts, nodeID)
}

// AddSealer is a paid mutator transaction binding the contract method 0x89152d1f.
//
// Solidity: function addSealer(string nodeID) returns(int256)
func (_Consensus *ConsensusTransactorSession) AddSealer(nodeID string) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.AddSealer(&_Consensus.TransactOpts, nodeID)
}

// Remove is a paid mutator transaction binding the contract method 0x80599e4b.
//
// Solidity: function remove(string nodeID) returns(int256)
func (_Consensus *ConsensusTransactor) Remove(opts *bind.TransactOpts, nodeID string) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.contract.Transact(opts, "remove", nodeID)
}

// Remove is a paid mutator transaction binding the contract method 0x80599e4b.
//
// Solidity: function remove(string nodeID) returns(int256)
func (_Consensus *ConsensusSession) Remove(nodeID string) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.Remove(&_Consensus.TransactOpts, nodeID)
}

// Remove is a paid mutator transaction binding the contract method 0x80599e4b.
//
// Solidity: function remove(string nodeID) returns(int256)
func (_Consensus *ConsensusTransactorSession) Remove(nodeID string) (*types.Transaction, *types.Receipt, error) {
	return _Consensus.Contract.Remove(&_Consensus.TransactOpts, nodeID)
}
