// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package examples

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

// KVTableTestABI is the input ABI used to generate the binding from.
const KVTableTestABI = "[{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"string\"}],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"},{\"name\":\"\",\"type\":\"int256\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"item_price\",\"type\":\"int256\"},{\"name\":\"item_name\",\"type\":\"string\"}],\"name\":\"set\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"count\",\"type\":\"int256\"}],\"name\":\"SetResult\",\"type\":\"event\"}]"

// KVTableTestBin is the compiled bytecode used for deploying new contracts.
var KVTableTestBin = "0x608060405234801561001057600080fd5b506110106000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166356004b6a6040805190810160405280600881526020017f745f6b76746573740000000000000000000000000000000000000000000000008152506040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018060200180602001848103845285818151815260200191508051906020019080838360005b8381101561013957808201518184015260208101905061011e565b50505050905090810190601f1680156101665780820380516001836020036101000a031916815260200191505b50848103835260028152602001807f6964000000000000000000000000000000000000000000000000000000000000815250602001848103825260148152602001807f6974656d5f70726963652c6974656d5f6e616d65000000000000000000000000815250602001945050505050602060405180830381600087803b1580156101ef57600080fd5b505af1158015610203573d6000803e3d6000fd5b505050506040513d602081101561021957600080fd5b810190808051906020019092919050505050610da38061023a6000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063693ec85e14610051578063ed0c8b1714610145575b600080fd5b34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610212565b604051808415151515815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101085780820151818401526020810190506100ed565b50505050905090810190601f1680156101355780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b34801561015157600080fd5b506101fc600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929080359060200190929190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506106d6565b6040518082815260200191505060405180910390f35b600080606060008060008060606000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f23f63c96040805190810160405280600881526020017f745f6b76746573740000000000000000000000000000000000000000000000008152506040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156102fd5780820151818401526020810190506102e2565b50505050905090810190601f16801561032a5780820380516001836020036101000a031916815260200191505b5092505050602060405180830381600087803b15801561034957600080fd5b505af115801561035d573d6000803e3d6000fd5b505050506040513d602081101561037357600080fd5b81019080805190602001909291905050509450600093508473ffffffffffffffffffffffffffffffffffffffff1663693ec85e8a6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156104125780820151818401526020810190506103f7565b50505050905090810190601f16801561043f5780820380516001836020036101000a031916815260200191505b50925050506040805180830381600087803b15801561045d57600080fd5b505af1158015610471573d6000803e3d6000fd5b505050506040513d604081101561048757600080fd5b810190808051906020019092919080519060200190929190505050809450819550505083156106c1578273ffffffffffffffffffffffffffffffffffffffff1663fda69fae6040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018281038252600a8152602001807f6974656d5f707269636500000000000000000000000000000000000000000000815250602001915050602060405180830381600087803b15801561055057600080fd5b505af1158015610564573d6000803e3d6000fd5b505050506040513d602081101561057a57600080fd5b810190808051906020019092919050505091508273ffffffffffffffffffffffffffffffffffffffff16639c981fcb6040518163ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825260098152602001807f6974656d5f6e616d650000000000000000000000000000000000000000000000815250602001915050600060405180830381600087803b15801561062d57600080fd5b505af1158015610641573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561066b57600080fd5b81019080805164010000000081111561068357600080fd5b8281019050602081018481111561069957600080fd5b81518560018202830111640100000000821117156106b657600080fd5b505092919050505090505b83828297509750975050505050509193909250565b6000806000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f23f63c96040805190810160405280600881526020017f745f6b76746573740000000000000000000000000000000000000000000000008152506040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156107ba57808201518184015260208101905061079f565b50505050905090810190601f1680156107e75780820380516001836020036101000a031916815260200191505b5092505050602060405180830381600087803b15801561080657600080fd5b505af115801561081a573d6000803e3d6000fd5b505050506040513d602081101561083057600080fd5b810190808051906020019092919050505092508273ffffffffffffffffffffffffffffffffffffffff166313db93466040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156108a757600080fd5b505af11580156108bb573d6000803e3d6000fd5b505050506040513d60208110156108d157600080fd5b810190808051906020019092919050505091508173ffffffffffffffffffffffffffffffffffffffff1663e942b516886040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260028152602001807f6964000000000000000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156109a4578082015181840152602081019050610989565b50505050905090810190601f1680156109d15780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b1580156109f157600080fd5b505af1158015610a05573d6000803e3d6000fd5b505050508173ffffffffffffffffffffffffffffffffffffffff16632ef8ba74876040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018381526020018281038252600a8152602001807f6974656d5f70726963650000000000000000000000000000000000000000000081525060200192505050600060405180830381600087803b158015610ab157600080fd5b505af1158015610ac5573d6000803e3d6000fd5b505050508173ffffffffffffffffffffffffffffffffffffffff1663e942b516866040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260098152602001807f6974656d5f6e616d650000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610b89578082015181840152602081019050610b6e565b50505050905090810190601f168015610bb65780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610bd657600080fd5b505af1158015610bea573d6000803e3d6000fd5b505050508273ffffffffffffffffffffffffffffffffffffffff1663a815ff1588846040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015610ca9578082015181840152602081019050610c8e565b50505050905090810190601f168015610cd65780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b158015610cf657600080fd5b505af1158015610d0a573d6000803e3d6000fd5b505050506040513d6020811015610d2057600080fd5b810190808051906020019092919050505090507fb103249d88cd818b10c5cd6889874103a7699c5834cb078d8f35925dca8a62d6816040518082815260200191505060405180910390a180935050505093925050505600a165627a7a723058206a0d6addde8d8ea9d23a06db3c3c9d0dd817f8552c3ea736c60a5c174e866f660029"

// DeployKVTableTest deploys a new contract, binding an instance of KVTableTest to it.
func DeployKVTableTest(auth *bind.TransactOpts, backend bind.ContractBackend) (common.Address, *types.Transaction, *KVTableTest, error) {
	parsed, err := abi.JSON(strings.NewReader(KVTableTestABI))
	if err != nil {
		return common.Address{}, nil, nil, err
	}

	address, tx, contract, err := bind.DeployContract(auth, parsed, common.FromHex(KVTableTestBin), backend)
	if err != nil {
		return common.Address{}, nil, nil, err
	}
	return address, tx, &KVTableTest{KVTableTestCaller: KVTableTestCaller{contract: contract}, KVTableTestTransactor: KVTableTestTransactor{contract: contract}, KVTableTestFilterer: KVTableTestFilterer{contract: contract}}, nil
}

func AsyncDeployKVTableTest(auth *bind.TransactOpts, handler func(*types.Receipt, error), backend bind.ContractBackend) (*types.Transaction, error) {
	parsed, err := abi.JSON(strings.NewReader(KVTableTestABI))
	if err != nil {
		return nil, err
	}

	tx, err := bind.AsyncDeployContract(auth, handler, parsed, common.FromHex(KVTableTestBin), backend)
	if err != nil {
		return nil, err
	}
	return tx, nil
}

// KVTableTest is an auto generated Go binding around a Solidity contract.
type KVTableTest struct {
	KVTableTestCaller     // Read-only binding to the contract
	KVTableTestTransactor // Write-only binding to the contract
	KVTableTestFilterer   // Log filterer for contract events
}

// KVTableTestCaller is an auto generated read-only Go binding around a Solidity contract.
type KVTableTestCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// KVTableTestTransactor is an auto generated write-only Go binding around a Solidity contract.
type KVTableTestTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// KVTableTestFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type KVTableTestFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// KVTableTestSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type KVTableTestSession struct {
	Contract     *KVTableTest      // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// KVTableTestCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type KVTableTestCallerSession struct {
	Contract *KVTableTestCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts      // Call options to use throughout this session
}

// KVTableTestTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type KVTableTestTransactorSession struct {
	Contract     *KVTableTestTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts      // Transaction auth options to use throughout this session
}

// KVTableTestRaw is an auto generated low-level Go binding around a Solidity contract.
type KVTableTestRaw struct {
	Contract *KVTableTest // Generic contract binding to access the raw methods on
}

// KVTableTestCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type KVTableTestCallerRaw struct {
	Contract *KVTableTestCaller // Generic read-only contract binding to access the raw methods on
}

// KVTableTestTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type KVTableTestTransactorRaw struct {
	Contract *KVTableTestTransactor // Generic write-only contract binding to access the raw methods on
}

// NewKVTableTest creates a new instance of KVTableTest, bound to a specific deployed contract.
func NewKVTableTest(address common.Address, backend bind.ContractBackend) (*KVTableTest, error) {
	contract, err := bindKVTableTest(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &KVTableTest{KVTableTestCaller: KVTableTestCaller{contract: contract}, KVTableTestTransactor: KVTableTestTransactor{contract: contract}, KVTableTestFilterer: KVTableTestFilterer{contract: contract}}, nil
}

// NewKVTableTestCaller creates a new read-only instance of KVTableTest, bound to a specific deployed contract.
func NewKVTableTestCaller(address common.Address, caller bind.ContractCaller) (*KVTableTestCaller, error) {
	contract, err := bindKVTableTest(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &KVTableTestCaller{contract: contract}, nil
}

// NewKVTableTestTransactor creates a new write-only instance of KVTableTest, bound to a specific deployed contract.
func NewKVTableTestTransactor(address common.Address, transactor bind.ContractTransactor) (*KVTableTestTransactor, error) {
	contract, err := bindKVTableTest(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &KVTableTestTransactor{contract: contract}, nil
}

// NewKVTableTestFilterer creates a new log filterer instance of KVTableTest, bound to a specific deployed contract.
func NewKVTableTestFilterer(address common.Address, filterer bind.ContractFilterer) (*KVTableTestFilterer, error) {
	contract, err := bindKVTableTest(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &KVTableTestFilterer{contract: contract}, nil
}

// bindKVTableTest binds a generic wrapper to an already deployed contract.
func bindKVTableTest(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(KVTableTestABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_KVTableTest *KVTableTestRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _KVTableTest.Contract.KVTableTestCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_KVTableTest *KVTableTestRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.KVTableTestTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_KVTableTest *KVTableTestRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.KVTableTestTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_KVTableTest *KVTableTestCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _KVTableTest.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_KVTableTest *KVTableTestTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_KVTableTest *KVTableTestTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.contract.Transact(opts, method, params...)
}

// Get is a free data retrieval call binding the contract method 0x693ec85e.
//
// Solidity: function get(string id) constant returns(bool, int256, string)
func (_KVTableTest *KVTableTestCaller) Get(opts *bind.CallOpts, id string) (bool, *big.Int, string, error) {
	var (
		ret0 = new(bool)
		ret1 = new(*big.Int)
		ret2 = new(string)
	)
	out := &[]interface{}{
		ret0,
		ret1,
		ret2,
	}
	err := _KVTableTest.contract.Call(opts, out, "get", id)
	return *ret0, *ret1, *ret2, err
}

// Get is a free data retrieval call binding the contract method 0x693ec85e.
//
// Solidity: function get(string id) constant returns(bool, int256, string)
func (_KVTableTest *KVTableTestSession) Get(id string) (bool, *big.Int, string, error) {
	return _KVTableTest.Contract.Get(&_KVTableTest.CallOpts, id)
}

// Get is a free data retrieval call binding the contract method 0x693ec85e.
//
// Solidity: function get(string id) constant returns(bool, int256, string)
func (_KVTableTest *KVTableTestCallerSession) Get(id string) (bool, *big.Int, string, error) {
	return _KVTableTest.Contract.Get(&_KVTableTest.CallOpts, id)
}

// Set is a paid mutator transaction binding the contract method 0xed0c8b17.
//
// Solidity: function set(string id, int256 item_price, string item_name) returns(int256)
func (_KVTableTest *KVTableTestTransactor) Set(opts *bind.TransactOpts, id string, item_price *big.Int, item_name string) (*types.Transaction, *types.Receipt, error) {
	return _KVTableTest.contract.Transact(opts, "set", id, item_price, item_name)
}

func (_KVTableTest *KVTableTestTransactor) AsyncSet(handler func(*types.Receipt, error), opts *bind.TransactOpts, id string, item_price *big.Int, item_name string) (*types.Transaction, error) {
	return _KVTableTest.contract.AsyncTransact(opts, handler, "set", id, item_price, item_name)
}

// Set is a paid mutator transaction binding the contract method 0xed0c8b17.
//
// Solidity: function set(string id, int256 item_price, string item_name) returns(int256)
func (_KVTableTest *KVTableTestSession) Set(id string, item_price *big.Int, item_name string) (*types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.Set(&_KVTableTest.TransactOpts, id, item_price, item_name)
}

func (_KVTableTest *KVTableTestSession) AsyncSet(handler func(*types.Receipt, error), id string, item_price *big.Int, item_name string) (*types.Transaction, error) {
	return _KVTableTest.Contract.AsyncSet(handler, &_KVTableTest.TransactOpts, id, item_price, item_name)
}

// Set is a paid mutator transaction binding the contract method 0xed0c8b17.
//
// Solidity: function set(string id, int256 item_price, string item_name) returns(int256)
func (_KVTableTest *KVTableTestTransactorSession) Set(id string, item_price *big.Int, item_name string) (*types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.Set(&_KVTableTest.TransactOpts, id, item_price, item_name)
}

func (_KVTableTest *KVTableTestTransactorSession) AsyncSet(handler func(*types.Receipt, error), id string, item_price *big.Int, item_name string) (*types.Transaction, error) {
	return _KVTableTest.Contract.AsyncSet(handler, &_KVTableTest.TransactOpts, id, item_price, item_name)
}

// KVTableTestSetResultIterator is returned from FilterSetResult and is used to iterate over the raw logs and unpacked data for SetResult events raised by the KVTableTest contract.
type KVTableTestSetResultIterator struct {
	Event *KVTableTestSetResult // Event containing the contract specifics and raw log

	contract *bind.BoundContract // Generic contract to use for unpacking event data
	event    string              // Event name to use for unpacking event data

	logs chan types.Log        // Log channel receiving the found contract events
	sub  ethereum.Subscription // Subscription for errors, completion and termination
	done bool                  // Whether the subscription completed delivering logs
	fail error                 // Occurred error to stop iteration
}

// KVTableTestSetResult represents a SetResult event raised by the KVTableTest contract.
type KVTableTestSetResult struct {
	Count *big.Int
	Raw   types.Log // Blockchain specific contextual infos
}

// ParseSetResult is a log parse operation binding the contract event 0xb103249d88cd818b10c5cd6889874103a7699c5834cb078d8f35925dca8a62d6.
//
// Solidity: event SetResult(int256 count)
func (_KVTableTest *KVTableTestFilterer) ParseSetResult(log types.Log) (*KVTableTestSetResult, error) {
	event := new(KVTableTestSetResult)
	if err := _KVTableTest.contract.UnpackLog(event, "SetResult", log); err != nil {
		return nil, err
	}
	return event, nil
}
