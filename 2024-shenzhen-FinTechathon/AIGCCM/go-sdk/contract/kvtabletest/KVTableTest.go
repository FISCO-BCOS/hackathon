// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package kvtabletest

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
var KVTableTestBin = "0x608060405234801561001057600080fd5b506110106000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166356004b6a6040805190810160405280600881526020017f745f6b76746573740000000000000000000000000000000000000000000000008152506040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018060200180602001848103845285818151815260200191508051906020019080838360005b8381101561013957808201518184015260208101905061011e565b50505050905090810190601f1680156101665780820380516001836020036101000a031916815260200191505b50848103835260028152602001807f6964000000000000000000000000000000000000000000000000000000000000815250602001848103825260148152602001807f6974656d5f70726963652c6974656d5f6e616d65000000000000000000000000815250602001945050505050602060405180830381600087803b1580156101ef57600080fd5b505af1158015610203573d6000803e3d6000fd5b505050506040513d602081101561021957600080fd5b810190808051906020019092919050505050610ea28061023a6000396000f3fe608060405260043610610046576000357c010000000000000000000000000000000000000000000000000000000090048063693ec85e1461004b578063ed0c8b171461019e575b600080fd5b34801561005757600080fd5b506101116004803603602081101561006e57600080fd5b810190808035906020019064010000000081111561008b57600080fd5b82018360208201111561009d57600080fd5b803590602001918460018302840111640100000000831117156100bf57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061031b565b604051808415151515815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610161578082015181840152602081019050610146565b50505050905090810190601f16801561018e5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b3480156101aa57600080fd5b50610305600480360360608110156101c157600080fd5b81019080803590602001906401000000008111156101de57600080fd5b8201836020820111156101f057600080fd5b8035906020019184600183028401116401000000008311171561021257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803590602001909291908035906020019064010000000081111561027f57600080fd5b82018360208201111561029157600080fd5b803590602001918460018302840111640100000000831117156102b357600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506107d8565b6040518082815260200191505060405180910390f35b600080606060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f23f63c96040805190810160405280600881526020017f745f6b76746573740000000000000000000000000000000000000000000000008152506040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156104005780820151818401526020810190506103e5565b50505050905090810190601f16801561042d5780820380516001836020036101000a031916815260200191505b509250505060206040518083038186803b15801561044a57600080fd5b505afa15801561045e573d6000803e3d6000fd5b505050506040513d602081101561047457600080fd5b81019080805190602001909291905050509050600080905060008273ffffffffffffffffffffffffffffffffffffffff1663693ec85e886040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156105165780820151818401526020810190506104fb565b50505050905090810190601f1680156105435780820380516001836020036101000a031916815260200191505b5092505050604080518083038186803b15801561055f57600080fd5b505afa158015610573573d6000803e3d6000fd5b505050506040513d604081101561058957600080fd5b81019080805190602001909291908051906020019092919050505080925081935050506000606083156107c3578273ffffffffffffffffffffffffffffffffffffffff1663fda69fae6040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018281038252600a8152602001807f6974656d5f70726963650000000000000000000000000000000000000000000081525060200191505060206040518083038186803b15801561065457600080fd5b505afa158015610668573d6000803e3d6000fd5b505050506040513d602081101561067e57600080fd5b810190808051906020019092919050505091508273ffffffffffffffffffffffffffffffffffffffff16639c981fcb6040518163ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825260098152602001807f6974656d5f6e616d65000000000000000000000000000000000000000000000081525060200191505060006040518083038186803b15801561072f57600080fd5b505afa158015610743573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561076d57600080fd5b81019080805164010000000081111561078557600080fd5b8281019050602081018481111561079b57600080fd5b81518560018202830111640100000000821117156107b857600080fd5b505092919050505090505b83828297509750975050505050509193909250565b6000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f23f63c96040805190810160405280600881526020017f745f6b76746573740000000000000000000000000000000000000000000000008152506040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156108b957808201518184015260208101905061089e565b50505050905090810190601f1680156108e65780820380516001836020036101000a031916815260200191505b509250505060206040518083038186803b15801561090357600080fd5b505afa158015610917573d6000803e3d6000fd5b505050506040513d602081101561092d57600080fd5b8101908080519060200190929190505050905060008173ffffffffffffffffffffffffffffffffffffffff166313db93466040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040160206040518083038186803b1580156109a457600080fd5b505afa1580156109b8573d6000803e3d6000fd5b505050506040513d60208110156109ce57600080fd5b810190808051906020019092919050505090508073ffffffffffffffffffffffffffffffffffffffff1663e942b516876040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260028152602001807f6964000000000000000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610aa1578082015181840152602081019050610a86565b50505050905090810190601f168015610ace5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610aee57600080fd5b505af1158015610b02573d6000803e3d6000fd5b505050508073ffffffffffffffffffffffffffffffffffffffff16632ef8ba74866040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018381526020018281038252600a8152602001807f6974656d5f70726963650000000000000000000000000000000000000000000081525060200192505050600060405180830381600087803b158015610bae57600080fd5b505af1158015610bc2573d6000803e3d6000fd5b505050508073ffffffffffffffffffffffffffffffffffffffff1663e942b516856040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260098152602001807f6974656d5f6e616d650000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610c86578082015181840152602081019050610c6b565b50505050905090810190601f168015610cb35780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610cd357600080fd5b505af1158015610ce7573d6000803e3d6000fd5b5050505060008273ffffffffffffffffffffffffffffffffffffffff1663a815ff1588846040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015610da8578082015181840152602081019050610d8d565b50505050905090810190601f168015610dd55780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b158015610df557600080fd5b505af1158015610e09573d6000803e3d6000fd5b505050506040513d6020811015610e1f57600080fd5b810190808051906020019092919050505090507fb103249d88cd818b10c5cd6889874103a7699c5834cb078d8f35925dca8a62d6816040518082815260200191505060405180910390a1809350505050939250505056fea165627a7a723058207b85dc2c442cf3dabeded6201e2f9b19ee21090377bef82eb9066fda1f8dddfb0029"

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
func (_KVTableTest *KVTableTestRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.KVTableTestTransactor.contract.TransactWithResult(opts, result, method, params...)
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
func (_KVTableTest *KVTableTestTransactorRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.contract.TransactWithResult(opts, result, method, params...)
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
func (_KVTableTest *KVTableTestTransactor) Set(opts *bind.TransactOpts, id string, item_price *big.Int, item_name string) (*big.Int, *types.Transaction, *types.Receipt, error) {
	var (
		ret0 = new(*big.Int)
	)
	out := ret0
	transaction, receipt, err := _KVTableTest.contract.TransactWithResult(opts, out, "set", id, item_price, item_name)
	return *ret0, transaction, receipt, err
}

func (_KVTableTest *KVTableTestTransactor) AsyncSet(handler func(*types.Receipt, error), opts *bind.TransactOpts, id string, item_price *big.Int, item_name string) (*types.Transaction, error) {
	return _KVTableTest.contract.AsyncTransact(opts, handler, "set", id, item_price, item_name)
}

// Set is a paid mutator transaction binding the contract method 0xed0c8b17.
//
// Solidity: function set(string id, int256 item_price, string item_name) returns(int256)
func (_KVTableTest *KVTableTestSession) Set(id string, item_price *big.Int, item_name string) (*big.Int, *types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.Set(&_KVTableTest.TransactOpts, id, item_price, item_name)
}

func (_KVTableTest *KVTableTestSession) AsyncSet(handler func(*types.Receipt, error), id string, item_price *big.Int, item_name string) (*types.Transaction, error) {
	return _KVTableTest.Contract.AsyncSet(handler, &_KVTableTest.TransactOpts, id, item_price, item_name)
}

// Set is a paid mutator transaction binding the contract method 0xed0c8b17.
//
// Solidity: function set(string id, int256 item_price, string item_name) returns(int256)
func (_KVTableTest *KVTableTestTransactorSession) Set(id string, item_price *big.Int, item_name string) (*big.Int, *types.Transaction, *types.Receipt, error) {
	return _KVTableTest.Contract.Set(&_KVTableTest.TransactOpts, id, item_price, item_name)
}

func (_KVTableTest *KVTableTestTransactorSession) AsyncSet(handler func(*types.Receipt, error), id string, item_price *big.Int, item_name string) (*types.Transaction, error) {
	return _KVTableTest.Contract.AsyncSet(handler, &_KVTableTest.TransactOpts, id, item_price, item_name)
}

// KVTableTestSetResult represents a SetResult event raised by the KVTableTest contract.
type KVTableTestSetResult struct {
	Count *big.Int
	Raw   types.Log // Blockchain specific contextual infos
}

// WatchSetResult is a free log subscription operation binding the contract event 0xb103249d88cd818b10c5cd6889874103a7699c5834cb078d8f35925dca8a62d6.
//
// Solidity: event SetResult(int256 count)
func (_KVTableTest *KVTableTestFilterer) WatchSetResult(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _KVTableTest.contract.WatchLogs(fromBlock, handler, "SetResult")
}

func (_KVTableTest *KVTableTestFilterer) WatchAllSetResult(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _KVTableTest.contract.WatchLogs(fromBlock, handler, "SetResult")
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

// WatchSetResult is a free log subscription operation binding the contract event 0xb103249d88cd818b10c5cd6889874103a7699c5834cb078d8f35925dca8a62d6.
//
// Solidity: event SetResult(int256 count)
func (_KVTableTest *KVTableTestSession) WatchSetResult(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _KVTableTest.Contract.WatchSetResult(fromBlock, handler)
}

func (_KVTableTest *KVTableTestSession) WatchAllSetResult(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _KVTableTest.Contract.WatchAllSetResult(fromBlock, handler)
}

// ParseSetResult is a log parse operation binding the contract event 0xb103249d88cd818b10c5cd6889874103a7699c5834cb078d8f35925dca8a62d6.
//
// Solidity: event SetResult(int256 count)
func (_KVTableTest *KVTableTestSession) ParseSetResult(log types.Log) (*KVTableTestSetResult, error) {
	return _KVTableTest.Contract.ParseSetResult(log)
}
