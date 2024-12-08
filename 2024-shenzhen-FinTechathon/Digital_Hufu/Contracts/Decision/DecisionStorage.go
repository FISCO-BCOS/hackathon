// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package Decision

import (
	"fmt"
	"math/big"
	"strings"

	"github.com/FISCO-BCOS/go-sdk/v3/abi"
	"github.com/FISCO-BCOS/go-sdk/v3/abi/bind"
	"github.com/FISCO-BCOS/go-sdk/v3/types"
	"github.com/ethereum/go-ethereum/common"
)

// Reference imports to suppress errors if they are not otherwise used.
var (
	_ = big.NewInt
	_ = strings.NewReader
	_ = abi.U256
	_ = bind.Bind
	_ = common.Big1
)

// DecisionStorageABI is the input ABI used to generate the binding from.
const DecisionStorageABI = "[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"evidence\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"nodeID\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"timestamp\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"bool\",\"name\":\"approved\",\"type\":\"bool\"}],\"name\":\"DecisionRecorded\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"decisions\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"evidence\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"nodeID\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"timestamp\",\"type\":\"uint256\"},{\"internalType\":\"bool\",\"name\":\"approved\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"index\",\"type\":\"uint256\"}],\"name\":\"getDecision\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"evidence\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"nodeID\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"timestamp\",\"type\":\"uint256\"},{\"internalType\":\"bool\",\"name\":\"approved\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getDecisionCount\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"evidence\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"nodeID\",\"type\":\"string\"},{\"internalType\":\"bool\",\"name\":\"approved\",\"type\":\"bool\"}],\"name\":\"recordDecision\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"evidence\",\"type\":\"string\"}],\"name\":\"verifyEvidence\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"}]"

// DecisionStorageBin is the compiled bytecode used for deploying new contracts.
var DecisionStorageBin = "0x608060405234801561001057600080fd5b50610b7e806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80636f13f95a1461005c578063af7548481461008c578063b829df82146100bf578063b8c0dbbd146100f2578063e4ff19da14610122575b600080fd5b610076600480360381019061007191906107ae565b610140565b6040516100839190610812565b60405180910390f35b6100a660048036038101906100a19190610863565b61014b565b6040516100b69493929190610927565b60405180910390f35b6100d960048036038101906100d49190610863565b610335565b6040516100e99493929190610927565b60405180910390f35b61010c600480360381019061010791906109a6565b610492565b6040516101199190610812565b60405180910390f35b61012a6105a5565b6040516101379190610a31565b60405180910390f35b600060019050919050565b6060806000806000805490508510610198576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161018f90610a98565b60405180910390fd5b60008086815481106101ad576101ac610ab8565b5b90600052602060002090600402016040518060800160405290816000820180546101d690610b16565b80601f016020809104026020016040519081016040528092919081815260200182805461020290610b16565b801561024f5780601f106102245761010080835404028352916020019161024f565b820191906000526020600020905b81548152906001019060200180831161023257829003601f168201915b5050505050815260200160018201805461026890610b16565b80601f016020809104026020016040519081016040528092919081815260200182805461029490610b16565b80156102e15780601f106102b6576101008083540402835291602001916102e1565b820191906000526020600020905b8154815290600101906020018083116102c457829003601f168201915b50505050508152602001600282015481526020016003820160009054906101000a900460ff161515151581525050905080600001518160200151826040015183606001519450945094509450509193509193565b6000818154811061034557600080fd5b906000526020600020906004020160009150905080600001805461036890610b16565b80601f016020809104026020016040519081016040528092919081815260200182805461039490610b16565b80156103e15780601f106103b6576101008083540402835291602001916103e1565b820191906000526020600020905b8154815290600101906020018083116103c457829003601f168201915b5050505050908060010180546103f690610b16565b80601f016020809104026020016040519081016040528092919081815260200182805461042290610b16565b801561046f5780601f106104445761010080835404028352916020019161046f565b820191906000526020600020905b81548152906001019060200180831161045257829003601f168201915b5050505050908060020154908060030160009054906101000a900460ff16905084565b60008060405180608001604052808681526020018581526020014281526020018415158152509050600081908060018154018082558091505060019003906000526020600020906004020160009091909190915060008201518160000190805190602001906105029291906105b1565b50602082015181600101908051906020019061051f9291906105b1565b506040820151816002015560608201518160030160006101000a81548160ff02191690831515021790555050507f641014cc6abca52c1d84346d29137b54af42068d1b3b5b3c52e98b16eb08542881600001518260200151836040015184606001516040516105919493929190610927565b60405180910390a160019150509392505050565b60008080549050905090565b8280546105bd90610b16565b90600052602060002090601f0160209004810192826105df5760008555610626565b82601f106105f857805160ff1916838001178555610626565b82800160010185558215610626579182015b8281111561062557825182559160200191906001019061060a565b5b5090506106339190610637565b5090565b5b80821115610650576000816000905550600101610638565b5090565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6106bb82610672565b810181811067ffffffffffffffff821117156106da576106d9610683565b5b80604052505050565b60006106ed610654565b90506106f982826106b2565b919050565b600067ffffffffffffffff82111561071957610718610683565b5b61072282610672565b9050602081019050919050565b82818337600083830152505050565b600061075161074c846106fe565b6106e3565b90508281526020810184848401111561076d5761076c61066d565b5b61077884828561072f565b509392505050565b600082601f83011261079557610794610668565b5b81356107a584826020860161073e565b91505092915050565b6000602082840312156107c4576107c361065e565b5b600082013567ffffffffffffffff8111156107e2576107e1610663565b5b6107ee84828501610780565b91505092915050565b60008115159050919050565b61080c816107f7565b82525050565b60006020820190506108276000830184610803565b92915050565b6000819050919050565b6108408161082d565b811461084b57600080fd5b50565b60008135905061085d81610837565b92915050565b6000602082840312156108795761087861065e565b5b60006108878482850161084e565b91505092915050565b600081519050919050565b600082825260208201905092915050565b60005b838110156108ca5780820151818401526020810190506108af565b838111156108d9576000848401525b50505050565b60006108ea82610890565b6108f4818561089b565b93506109048185602086016108ac565b61090d81610672565b840191505092915050565b6109218161082d565b82525050565b6000608082019050818103600083015261094181876108df565b9050818103602083015261095581866108df565b90506109646040830185610918565b6109716060830184610803565b95945050505050565b610983816107f7565b811461098e57600080fd5b50565b6000813590506109a08161097a565b92915050565b6000806000606084860312156109bf576109be61065e565b5b600084013567ffffffffffffffff8111156109dd576109dc610663565b5b6109e986828701610780565b935050602084013567ffffffffffffffff811115610a0a57610a09610663565b5b610a1686828701610780565b9250506040610a2786828701610991565b9150509250925092565b6000602082019050610a466000830184610918565b92915050565b7f496e646578206f7574206f6620626f756e647300000000000000000000000000600082015250565b6000610a8260138361089b565b9150610a8d82610a4c565b602082019050919050565b60006020820190508181036000830152610ab181610a75565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b60006002820490506001821680610b2e57607f821691505b60208210811415610b4257610b41610ae7565b5b5091905056fea2646970667358221220977b96c748e0423ec306543f50d16911b1ba1f1f2928bff91954f64010dea1d264736f6c634300080b0033"
var DecisionStorageSMBin = "0x"

// DeployDecisionStorage deploys a new contract, binding an instance of DecisionStorage to it.
func DeployDecisionStorage(auth *bind.TransactOpts, backend bind.ContractBackend) (common.Address, *types.Receipt, *DecisionStorage, error) {
	parsed, err := abi.JSON(strings.NewReader(DecisionStorageABI))
	if err != nil {
		return common.Address{}, nil, nil, err
	}

	var bytecode []byte
	if backend.SMCrypto() {
		bytecode = common.FromHex(DecisionStorageSMBin)
	} else {
		bytecode = common.FromHex(DecisionStorageBin)
	}
	if len(bytecode) == 0 {
		return common.Address{}, nil, nil, fmt.Errorf("cannot deploy empty bytecode")
	}
	address, receipt, contract, err := bind.DeployContract(auth, parsed, bytecode, DecisionStorageABI, backend)
	if err != nil {
		return common.Address{}, nil, nil, err
	}
	return address, receipt, &DecisionStorage{DecisionStorageCaller: DecisionStorageCaller{contract: contract}, DecisionStorageTransactor: DecisionStorageTransactor{contract: contract}, DecisionStorageFilterer: DecisionStorageFilterer{contract: contract}}, nil
}

func AsyncDeployDecisionStorage(auth *bind.TransactOpts, handler func(*types.Receipt, error), backend bind.ContractBackend) (*types.Transaction, error) {
	parsed, err := abi.JSON(strings.NewReader(DecisionStorageABI))
	if err != nil {
		return nil, err
	}

	var bytecode []byte
	if backend.SMCrypto() {
		bytecode = common.FromHex(DecisionStorageSMBin)
	} else {
		bytecode = common.FromHex(DecisionStorageBin)
	}
	if len(bytecode) == 0 {
		return nil, fmt.Errorf("cannot deploy empty bytecode")
	}
	tx, err := bind.AsyncDeployContract(auth, handler, parsed, bytecode, DecisionStorageABI, backend)
	if err != nil {
		return nil, err
	}
	return tx, nil
}

// DecisionStorage is an auto generated Go binding around a Solidity contract.
type DecisionStorage struct {
	DecisionStorageCaller     // Read-only binding to the contract
	DecisionStorageTransactor // Write-only binding to the contract
	DecisionStorageFilterer   // Log filterer for contract events
}

// DecisionStorageCaller is an auto generated read-only Go binding around a Solidity contract.
type DecisionStorageCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// DecisionStorageTransactor is an auto generated write-only Go binding around a Solidity contract.
type DecisionStorageTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// DecisionStorageFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type DecisionStorageFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// DecisionStorageSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type DecisionStorageSession struct {
	Contract     *DecisionStorage  // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// DecisionStorageCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type DecisionStorageCallerSession struct {
	Contract *DecisionStorageCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts          // Call options to use throughout this session
}

// DecisionStorageTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type DecisionStorageTransactorSession struct {
	Contract     *DecisionStorageTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts          // Transaction auth options to use throughout this session
}

// DecisionStorageRaw is an auto generated low-level Go binding around a Solidity contract.
type DecisionStorageRaw struct {
	Contract *DecisionStorage // Generic contract binding to access the raw methods on
}

// DecisionStorageCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type DecisionStorageCallerRaw struct {
	Contract *DecisionStorageCaller // Generic read-only contract binding to access the raw methods on
}

// DecisionStorageTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type DecisionStorageTransactorRaw struct {
	Contract *DecisionStorageTransactor // Generic write-only contract binding to access the raw methods on
}

// NewDecisionStorage creates a new instance of DecisionStorage, bound to a specific deployed contract.
func NewDecisionStorage(address common.Address, backend bind.ContractBackend) (*DecisionStorage, error) {
	contract, err := bindDecisionStorage(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &DecisionStorage{DecisionStorageCaller: DecisionStorageCaller{contract: contract}, DecisionStorageTransactor: DecisionStorageTransactor{contract: contract}, DecisionStorageFilterer: DecisionStorageFilterer{contract: contract}}, nil
}

// NewDecisionStorageCaller creates a new read-only instance of DecisionStorage, bound to a specific deployed contract.
func NewDecisionStorageCaller(address common.Address, caller bind.ContractCaller) (*DecisionStorageCaller, error) {
	contract, err := bindDecisionStorage(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &DecisionStorageCaller{contract: contract}, nil
}

// NewDecisionStorageTransactor creates a new write-only instance of DecisionStorage, bound to a specific deployed contract.
func NewDecisionStorageTransactor(address common.Address, transactor bind.ContractTransactor) (*DecisionStorageTransactor, error) {
	contract, err := bindDecisionStorage(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &DecisionStorageTransactor{contract: contract}, nil
}

// NewDecisionStorageFilterer creates a new log filterer instance of DecisionStorage, bound to a specific deployed contract.
func NewDecisionStorageFilterer(address common.Address, filterer bind.ContractFilterer) (*DecisionStorageFilterer, error) {
	contract, err := bindDecisionStorage(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &DecisionStorageFilterer{contract: contract}, nil
}

// bindDecisionStorage binds a generic wrapper to an already deployed contract.
func bindDecisionStorage(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(DecisionStorageABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_DecisionStorage *DecisionStorageRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _DecisionStorage.Contract.DecisionStorageCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_DecisionStorage *DecisionStorageRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _DecisionStorage.Contract.DecisionStorageTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_DecisionStorage *DecisionStorageRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _DecisionStorage.Contract.DecisionStorageTransactor.contract.TransactWithResult(opts, result, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_DecisionStorage *DecisionStorageCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _DecisionStorage.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_DecisionStorage *DecisionStorageTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _DecisionStorage.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_DecisionStorage *DecisionStorageTransactorRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _DecisionStorage.Contract.contract.TransactWithResult(opts, result, method, params...)
}

// Decisions is a free data retrieval call binding the contract method 0xb829df82.
//
// Solidity: function decisions(uint256 ) constant returns(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageCaller) Decisions(opts *bind.CallOpts, arg0 *big.Int) (struct {
	Evidence  string
	NodeID    string
	Timestamp *big.Int
	Approved  bool
}, error) {
	ret := new(struct {
		Evidence  string
		NodeID    string
		Timestamp *big.Int
		Approved  bool
	})
	out := ret
	err := _DecisionStorage.contract.Call(opts, out, "decisions", arg0)
	return *ret, err
}

// Decisions is a free data retrieval call binding the contract method 0xb829df82.
//
// Solidity: function decisions(uint256 ) constant returns(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageSession) Decisions(arg0 *big.Int) (struct {
	Evidence  string
	NodeID    string
	Timestamp *big.Int
	Approved  bool
}, error) {
	return _DecisionStorage.Contract.Decisions(&_DecisionStorage.CallOpts, arg0)
}

// Decisions is a free data retrieval call binding the contract method 0xb829df82.
//
// Solidity: function decisions(uint256 ) constant returns(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageCallerSession) Decisions(arg0 *big.Int) (struct {
	Evidence  string
	NodeID    string
	Timestamp *big.Int
	Approved  bool
}, error) {
	return _DecisionStorage.Contract.Decisions(&_DecisionStorage.CallOpts, arg0)
}

// GetDecision is a free data retrieval call binding the contract method 0xaf754848.
//
// Solidity: function getDecision(uint256 index) constant returns(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageCaller) GetDecision(opts *bind.CallOpts, index *big.Int) (struct {
	Evidence  string
	NodeID    string
	Timestamp *big.Int
	Approved  bool
}, error) {
	ret := new(struct {
		Evidence  string
		NodeID    string
		Timestamp *big.Int
		Approved  bool
	})
	out := ret
	err := _DecisionStorage.contract.Call(opts, out, "getDecision", index)
	return *ret, err
}

// GetDecision is a free data retrieval call binding the contract method 0xaf754848.
//
// Solidity: function getDecision(uint256 index) constant returns(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageSession) GetDecision(index *big.Int) (struct {
	Evidence  string
	NodeID    string
	Timestamp *big.Int
	Approved  bool
}, error) {
	return _DecisionStorage.Contract.GetDecision(&_DecisionStorage.CallOpts, index)
}

// GetDecision is a free data retrieval call binding the contract method 0xaf754848.
//
// Solidity: function getDecision(uint256 index) constant returns(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageCallerSession) GetDecision(index *big.Int) (struct {
	Evidence  string
	NodeID    string
	Timestamp *big.Int
	Approved  bool
}, error) {
	return _DecisionStorage.Contract.GetDecision(&_DecisionStorage.CallOpts, index)
}

// GetDecisionCount is a free data retrieval call binding the contract method 0xe4ff19da.
//
// Solidity: function getDecisionCount() constant returns(uint256)
func (_DecisionStorage *DecisionStorageCaller) GetDecisionCount(opts *bind.CallOpts) (*big.Int, error) {
	var (
		ret0 = new(*big.Int)
	)
	out := ret0
	err := _DecisionStorage.contract.Call(opts, out, "getDecisionCount")
	return *ret0, err
}

// GetDecisionCount is a free data retrieval call binding the contract method 0xe4ff19da.
//
// Solidity: function getDecisionCount() constant returns(uint256)
func (_DecisionStorage *DecisionStorageSession) GetDecisionCount() (*big.Int, error) {
	return _DecisionStorage.Contract.GetDecisionCount(&_DecisionStorage.CallOpts)
}

// GetDecisionCount is a free data retrieval call binding the contract method 0xe4ff19da.
//
// Solidity: function getDecisionCount() constant returns(uint256)
func (_DecisionStorage *DecisionStorageCallerSession) GetDecisionCount() (*big.Int, error) {
	return _DecisionStorage.Contract.GetDecisionCount(&_DecisionStorage.CallOpts)
}

// VerifyEvidence is a free data retrieval call binding the contract method 0x6f13f95a.
//
// Solidity: function verifyEvidence(string evidence) constant returns(bool)
func (_DecisionStorage *DecisionStorageCaller) VerifyEvidence(opts *bind.CallOpts, evidence string) (bool, error) {
	var (
		ret0 = new(bool)
	)
	out := ret0
	err := _DecisionStorage.contract.Call(opts, out, "verifyEvidence", evidence)
	return *ret0, err
}

// VerifyEvidence is a free data retrieval call binding the contract method 0x6f13f95a.
//
// Solidity: function verifyEvidence(string evidence) constant returns(bool)
func (_DecisionStorage *DecisionStorageSession) VerifyEvidence(evidence string) (bool, error) {
	return _DecisionStorage.Contract.VerifyEvidence(&_DecisionStorage.CallOpts, evidence)
}

// VerifyEvidence is a free data retrieval call binding the contract method 0x6f13f95a.
//
// Solidity: function verifyEvidence(string evidence) constant returns(bool)
func (_DecisionStorage *DecisionStorageCallerSession) VerifyEvidence(evidence string) (bool, error) {
	return _DecisionStorage.Contract.VerifyEvidence(&_DecisionStorage.CallOpts, evidence)
}

// RecordDecision is a paid mutator transaction binding the contract method 0xb8c0dbbd.
//
// Solidity: function recordDecision(string evidence, string nodeID, bool approved) returns(bool)
func (_DecisionStorage *DecisionStorageTransactor) RecordDecision(opts *bind.TransactOpts, evidence string, nodeID string, approved bool) (bool, *types.Transaction, *types.Receipt, error) {
	var (
		ret0 = new(bool)
	)
	out := ret0
	transaction, receipt, err := _DecisionStorage.contract.TransactWithResult(opts, out, "recordDecision", evidence, nodeID, approved)
	return *ret0, transaction, receipt, err
}

func (_DecisionStorage *DecisionStorageTransactor) AsyncRecordDecision(handler func(*types.Receipt, error), opts *bind.TransactOpts, evidence string, nodeID string, approved bool) (*types.Transaction, error) {
	return _DecisionStorage.contract.AsyncTransact(opts, handler, "recordDecision", evidence, nodeID, approved)
}

// RecordDecision is a paid mutator transaction binding the contract method 0xb8c0dbbd.
//
// Solidity: function recordDecision(string evidence, string nodeID, bool approved) returns(bool)
func (_DecisionStorage *DecisionStorageSession) RecordDecision(evidence string, nodeID string, approved bool) (bool, *types.Transaction, *types.Receipt, error) {
	return _DecisionStorage.Contract.RecordDecision(&_DecisionStorage.TransactOpts, evidence, nodeID, approved)
}

func (_DecisionStorage *DecisionStorageSession) AsyncRecordDecision(handler func(*types.Receipt, error), evidence string, nodeID string, approved bool) (*types.Transaction, error) {
	return _DecisionStorage.Contract.AsyncRecordDecision(handler, &_DecisionStorage.TransactOpts, evidence, nodeID, approved)
}

// RecordDecision is a paid mutator transaction binding the contract method 0xb8c0dbbd.
//
// Solidity: function recordDecision(string evidence, string nodeID, bool approved) returns(bool)
func (_DecisionStorage *DecisionStorageTransactorSession) RecordDecision(evidence string, nodeID string, approved bool) (bool, *types.Transaction, *types.Receipt, error) {
	return _DecisionStorage.Contract.RecordDecision(&_DecisionStorage.TransactOpts, evidence, nodeID, approved)
}

func (_DecisionStorage *DecisionStorageTransactorSession) AsyncRecordDecision(handler func(*types.Receipt, error), evidence string, nodeID string, approved bool) (*types.Transaction, error) {
	return _DecisionStorage.Contract.AsyncRecordDecision(handler, &_DecisionStorage.TransactOpts, evidence, nodeID, approved)
}

// DecisionStorageDecisionRecorded represents a DecisionRecorded event raised by the DecisionStorage contract.
type DecisionStorageDecisionRecorded struct {
	Evidence  string
	NodeID    string
	Timestamp *big.Int
	Approved  bool
	Raw       types.Log // Blockchain specific contextual infos
}

// WatchDecisionRecorded is a free log subscription operation binding the contract event 0x641014cc6abca52c1d84346d29137b54af42068d1b3b5b3c52e98b16eb085428.
//
// Solidity: event DecisionRecorded(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageFilterer) WatchDecisionRecorded(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _DecisionStorage.contract.WatchLogs(fromBlock, handler, "DecisionRecorded")
}

func (_DecisionStorage *DecisionStorageFilterer) WatchAllDecisionRecorded(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _DecisionStorage.contract.WatchLogs(fromBlock, handler, "DecisionRecorded")
}

// ParseDecisionRecorded is a log parse operation binding the contract event 0x641014cc6abca52c1d84346d29137b54af42068d1b3b5b3c52e98b16eb085428.
//
// Solidity: event DecisionRecorded(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageFilterer) ParseDecisionRecorded(log types.Log) (*DecisionStorageDecisionRecorded, error) {
	event := new(DecisionStorageDecisionRecorded)
	if err := _DecisionStorage.contract.UnpackLog(event, "DecisionRecorded", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchDecisionRecorded is a free log subscription operation binding the contract event 0x641014cc6abca52c1d84346d29137b54af42068d1b3b5b3c52e98b16eb085428.
//
// Solidity: event DecisionRecorded(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageSession) WatchDecisionRecorded(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _DecisionStorage.Contract.WatchDecisionRecorded(fromBlock, handler)
}

func (_DecisionStorage *DecisionStorageSession) WatchAllDecisionRecorded(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _DecisionStorage.Contract.WatchAllDecisionRecorded(fromBlock, handler)
}

// ParseDecisionRecorded is a log parse operation binding the contract event 0x641014cc6abca52c1d84346d29137b54af42068d1b3b5b3c52e98b16eb085428.
//
// Solidity: event DecisionRecorded(string evidence, string nodeID, uint256 timestamp, bool approved)
func (_DecisionStorage *DecisionStorageSession) ParseDecisionRecorded(log types.Log) (*DecisionStorageDecisionRecorded, error) {
	return _DecisionStorage.Contract.ParseDecisionRecorded(log)
}
