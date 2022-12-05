// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package chaingovernance

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

// ChainGovernanceABI is the input ABI used to generate the binding from.
const ChainGovernanceABI = "[{\"constant\":true,\"inputs\":[],\"name\":\"listOperators\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"user\",\"type\":\"address\"},{\"name\":\"weight\",\"type\":\"int256\"}],\"name\":\"updateCommitteeMemberWeight\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"queryThreshold\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"user\",\"type\":\"address\"}],\"name\":\"queryCommitteeMemberWeight\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"},{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"user\",\"type\":\"address\"}],\"name\":\"grantCommitteeMember\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"unfreezeAccount\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"listCommitteeMembers\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"threshold\",\"type\":\"int256\"}],\"name\":\"updateThreshold\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"user\",\"type\":\"address\"}],\"name\":\"revokeCommitteeMember\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"user\",\"type\":\"address\"}],\"name\":\"grantOperator\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"freezeAccount\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"user\",\"type\":\"address\"}],\"name\":\"revokeOperator\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"getAccountStatus\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]"

// ChainGovernance is an auto generated Go binding around a Solidity contract.
type ChainGovernance struct {
	ChainGovernanceCaller     // Read-only binding to the contract
	ChainGovernanceTransactor // Write-only binding to the contract
	ChainGovernanceFilterer   // Log filterer for contract events
}

// ChainGovernanceCaller is an auto generated read-only Go binding around a Solidity contract.
type ChainGovernanceCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ChainGovernanceTransactor is an auto generated write-only Go binding around a Solidity contract.
type ChainGovernanceTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ChainGovernanceFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type ChainGovernanceFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ChainGovernanceSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type ChainGovernanceSession struct {
	Contract     *ChainGovernance  // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// ChainGovernanceCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type ChainGovernanceCallerSession struct {
	Contract *ChainGovernanceCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts          // Call options to use throughout this session
}

// ChainGovernanceTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type ChainGovernanceTransactorSession struct {
	Contract     *ChainGovernanceTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts          // Transaction auth options to use throughout this session
}

// ChainGovernanceRaw is an auto generated low-level Go binding around a Solidity contract.
type ChainGovernanceRaw struct {
	Contract *ChainGovernance // Generic contract binding to access the raw methods on
}

// ChainGovernanceCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type ChainGovernanceCallerRaw struct {
	Contract *ChainGovernanceCaller // Generic read-only contract binding to access the raw methods on
}

// ChainGovernanceTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type ChainGovernanceTransactorRaw struct {
	Contract *ChainGovernanceTransactor // Generic write-only contract binding to access the raw methods on
}

// NewChainGovernance creates a new instance of ChainGovernance, bound to a specific deployed contract.
func NewChainGovernance(address common.Address, backend bind.ContractBackend) (*ChainGovernance, error) {
	contract, err := bindChainGovernance(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &ChainGovernance{ChainGovernanceCaller: ChainGovernanceCaller{contract: contract}, ChainGovernanceTransactor: ChainGovernanceTransactor{contract: contract}, ChainGovernanceFilterer: ChainGovernanceFilterer{contract: contract}}, nil
}

// NewChainGovernanceCaller creates a new read-only instance of ChainGovernance, bound to a specific deployed contract.
func NewChainGovernanceCaller(address common.Address, caller bind.ContractCaller) (*ChainGovernanceCaller, error) {
	contract, err := bindChainGovernance(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &ChainGovernanceCaller{contract: contract}, nil
}

// NewChainGovernanceTransactor creates a new write-only instance of ChainGovernance, bound to a specific deployed contract.
func NewChainGovernanceTransactor(address common.Address, transactor bind.ContractTransactor) (*ChainGovernanceTransactor, error) {
	contract, err := bindChainGovernance(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &ChainGovernanceTransactor{contract: contract}, nil
}

// NewChainGovernanceFilterer creates a new log filterer instance of ChainGovernance, bound to a specific deployed contract.
func NewChainGovernanceFilterer(address common.Address, filterer bind.ContractFilterer) (*ChainGovernanceFilterer, error) {
	contract, err := bindChainGovernance(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &ChainGovernanceFilterer{contract: contract}, nil
}

// bindChainGovernance binds a generic wrapper to an already deployed contract.
func bindChainGovernance(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(ChainGovernanceABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_ChainGovernance *ChainGovernanceRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _ChainGovernance.Contract.ChainGovernanceCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_ChainGovernance *ChainGovernanceRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.ChainGovernanceTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_ChainGovernance *ChainGovernanceRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.ChainGovernanceTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_ChainGovernance *ChainGovernanceCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _ChainGovernance.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_ChainGovernance *ChainGovernanceTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_ChainGovernance *ChainGovernanceTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.contract.Transact(opts, method, params...)
}

// GetAccountStatus is a free data retrieval call binding the contract method 0xfd4fa05a.
//
// Solidity: function getAccountStatus(address account) constant returns(string)
func (_ChainGovernance *ChainGovernanceCaller) GetAccountStatus(opts *bind.CallOpts, account common.Address) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _ChainGovernance.contract.Call(opts, out, "getAccountStatus", account)
	return *ret0, err
}

// GetAccountStatus is a free data retrieval call binding the contract method 0xfd4fa05a.
//
// Solidity: function getAccountStatus(address account) constant returns(string)
func (_ChainGovernance *ChainGovernanceSession) GetAccountStatus(account common.Address) (string, error) {
	return _ChainGovernance.Contract.GetAccountStatus(&_ChainGovernance.CallOpts, account)
}

// GetAccountStatus is a free data retrieval call binding the contract method 0xfd4fa05a.
//
// Solidity: function getAccountStatus(address account) constant returns(string)
func (_ChainGovernance *ChainGovernanceCallerSession) GetAccountStatus(account common.Address) (string, error) {
	return _ChainGovernance.Contract.GetAccountStatus(&_ChainGovernance.CallOpts, account)
}

// ListCommitteeMembers is a free data retrieval call binding the contract method 0x885a3a72.
//
// Solidity: function listCommitteeMembers() constant returns(string)
func (_ChainGovernance *ChainGovernanceCaller) ListCommitteeMembers(opts *bind.CallOpts) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _ChainGovernance.contract.Call(opts, out, "listCommitteeMembers")
	return *ret0, err
}

// ListCommitteeMembers is a free data retrieval call binding the contract method 0x885a3a72.
//
// Solidity: function listCommitteeMembers() constant returns(string)
func (_ChainGovernance *ChainGovernanceSession) ListCommitteeMembers() (string, error) {
	return _ChainGovernance.Contract.ListCommitteeMembers(&_ChainGovernance.CallOpts)
}

// ListCommitteeMembers is a free data retrieval call binding the contract method 0x885a3a72.
//
// Solidity: function listCommitteeMembers() constant returns(string)
func (_ChainGovernance *ChainGovernanceCallerSession) ListCommitteeMembers() (string, error) {
	return _ChainGovernance.Contract.ListCommitteeMembers(&_ChainGovernance.CallOpts)
}

// ListOperators is a free data retrieval call binding the contract method 0x039a93ca.
//
// Solidity: function listOperators() constant returns(string)
func (_ChainGovernance *ChainGovernanceCaller) ListOperators(opts *bind.CallOpts) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _ChainGovernance.contract.Call(opts, out, "listOperators")
	return *ret0, err
}

// ListOperators is a free data retrieval call binding the contract method 0x039a93ca.
//
// Solidity: function listOperators() constant returns(string)
func (_ChainGovernance *ChainGovernanceSession) ListOperators() (string, error) {
	return _ChainGovernance.Contract.ListOperators(&_ChainGovernance.CallOpts)
}

// ListOperators is a free data retrieval call binding the contract method 0x039a93ca.
//
// Solidity: function listOperators() constant returns(string)
func (_ChainGovernance *ChainGovernanceCallerSession) ListOperators() (string, error) {
	return _ChainGovernance.Contract.ListOperators(&_ChainGovernance.CallOpts)
}

// QueryCommitteeMemberWeight is a free data retrieval call binding the contract method 0x6c147119.
//
// Solidity: function queryCommitteeMemberWeight(address user) constant returns(bool, int256)
func (_ChainGovernance *ChainGovernanceCaller) QueryCommitteeMemberWeight(opts *bind.CallOpts, user common.Address) (bool, *big.Int, error) {
	var (
		ret0 = new(bool)
		ret1 = new(*big.Int)
	)
	out := &[]interface{}{
		ret0,
		ret1,
	}
	err := _ChainGovernance.contract.Call(opts, out, "queryCommitteeMemberWeight", user)
	return *ret0, *ret1, err
}

// QueryCommitteeMemberWeight is a free data retrieval call binding the contract method 0x6c147119.
//
// Solidity: function queryCommitteeMemberWeight(address user) constant returns(bool, int256)
func (_ChainGovernance *ChainGovernanceSession) QueryCommitteeMemberWeight(user common.Address) (bool, *big.Int, error) {
	return _ChainGovernance.Contract.QueryCommitteeMemberWeight(&_ChainGovernance.CallOpts, user)
}

// QueryCommitteeMemberWeight is a free data retrieval call binding the contract method 0x6c147119.
//
// Solidity: function queryCommitteeMemberWeight(address user) constant returns(bool, int256)
func (_ChainGovernance *ChainGovernanceCallerSession) QueryCommitteeMemberWeight(user common.Address) (bool, *big.Int, error) {
	return _ChainGovernance.Contract.QueryCommitteeMemberWeight(&_ChainGovernance.CallOpts, user)
}

// QueryThreshold is a free data retrieval call binding the contract method 0x281af27d.
//
// Solidity: function queryThreshold() constant returns(int256)
func (_ChainGovernance *ChainGovernanceCaller) QueryThreshold(opts *bind.CallOpts) (*big.Int, error) {
	var (
		ret0 = new(*big.Int)
	)
	out := ret0
	err := _ChainGovernance.contract.Call(opts, out, "queryThreshold")
	return *ret0, err
}

// QueryThreshold is a free data retrieval call binding the contract method 0x281af27d.
//
// Solidity: function queryThreshold() constant returns(int256)
func (_ChainGovernance *ChainGovernanceSession) QueryThreshold() (*big.Int, error) {
	return _ChainGovernance.Contract.QueryThreshold(&_ChainGovernance.CallOpts)
}

// QueryThreshold is a free data retrieval call binding the contract method 0x281af27d.
//
// Solidity: function queryThreshold() constant returns(int256)
func (_ChainGovernance *ChainGovernanceCallerSession) QueryThreshold() (*big.Int, error) {
	return _ChainGovernance.Contract.QueryThreshold(&_ChainGovernance.CallOpts)
}

// FreezeAccount is a paid mutator transaction binding the contract method 0xf26c159f.
//
// Solidity: function freezeAccount(address account) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactor) FreezeAccount(opts *bind.TransactOpts, account common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.contract.Transact(opts, "freezeAccount", account)
}

// FreezeAccount is a paid mutator transaction binding the contract method 0xf26c159f.
//
// Solidity: function freezeAccount(address account) returns(int256)
func (_ChainGovernance *ChainGovernanceSession) FreezeAccount(account common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.FreezeAccount(&_ChainGovernance.TransactOpts, account)
}

// FreezeAccount is a paid mutator transaction binding the contract method 0xf26c159f.
//
// Solidity: function freezeAccount(address account) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactorSession) FreezeAccount(account common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.FreezeAccount(&_ChainGovernance.TransactOpts, account)
}

// GrantCommitteeMember is a paid mutator transaction binding the contract method 0x6f8f521f.
//
// Solidity: function grantCommitteeMember(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactor) GrantCommitteeMember(opts *bind.TransactOpts, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.contract.Transact(opts, "grantCommitteeMember", user)
}

// GrantCommitteeMember is a paid mutator transaction binding the contract method 0x6f8f521f.
//
// Solidity: function grantCommitteeMember(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceSession) GrantCommitteeMember(user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.GrantCommitteeMember(&_ChainGovernance.TransactOpts, user)
}

// GrantCommitteeMember is a paid mutator transaction binding the contract method 0x6f8f521f.
//
// Solidity: function grantCommitteeMember(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactorSession) GrantCommitteeMember(user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.GrantCommitteeMember(&_ChainGovernance.TransactOpts, user)
}

// GrantOperator is a paid mutator transaction binding the contract method 0xe348da13.
//
// Solidity: function grantOperator(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactor) GrantOperator(opts *bind.TransactOpts, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.contract.Transact(opts, "grantOperator", user)
}

// GrantOperator is a paid mutator transaction binding the contract method 0xe348da13.
//
// Solidity: function grantOperator(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceSession) GrantOperator(user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.GrantOperator(&_ChainGovernance.TransactOpts, user)
}

// GrantOperator is a paid mutator transaction binding the contract method 0xe348da13.
//
// Solidity: function grantOperator(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactorSession) GrantOperator(user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.GrantOperator(&_ChainGovernance.TransactOpts, user)
}

// RevokeCommitteeMember is a paid mutator transaction binding the contract method 0xcafb4d1b.
//
// Solidity: function revokeCommitteeMember(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactor) RevokeCommitteeMember(opts *bind.TransactOpts, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.contract.Transact(opts, "revokeCommitteeMember", user)
}

// RevokeCommitteeMember is a paid mutator transaction binding the contract method 0xcafb4d1b.
//
// Solidity: function revokeCommitteeMember(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceSession) RevokeCommitteeMember(user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.RevokeCommitteeMember(&_ChainGovernance.TransactOpts, user)
}

// RevokeCommitteeMember is a paid mutator transaction binding the contract method 0xcafb4d1b.
//
// Solidity: function revokeCommitteeMember(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactorSession) RevokeCommitteeMember(user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.RevokeCommitteeMember(&_ChainGovernance.TransactOpts, user)
}

// RevokeOperator is a paid mutator transaction binding the contract method 0xfad8b32a.
//
// Solidity: function revokeOperator(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactor) RevokeOperator(opts *bind.TransactOpts, user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.contract.Transact(opts, "revokeOperator", user)
}

// RevokeOperator is a paid mutator transaction binding the contract method 0xfad8b32a.
//
// Solidity: function revokeOperator(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceSession) RevokeOperator(user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.RevokeOperator(&_ChainGovernance.TransactOpts, user)
}

// RevokeOperator is a paid mutator transaction binding the contract method 0xfad8b32a.
//
// Solidity: function revokeOperator(address user) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactorSession) RevokeOperator(user common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.RevokeOperator(&_ChainGovernance.TransactOpts, user)
}

// UnfreezeAccount is a paid mutator transaction binding the contract method 0x788649ea.
//
// Solidity: function unfreezeAccount(address account) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactor) UnfreezeAccount(opts *bind.TransactOpts, account common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.contract.Transact(opts, "unfreezeAccount", account)
}

// UnfreezeAccount is a paid mutator transaction binding the contract method 0x788649ea.
//
// Solidity: function unfreezeAccount(address account) returns(int256)
func (_ChainGovernance *ChainGovernanceSession) UnfreezeAccount(account common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.UnfreezeAccount(&_ChainGovernance.TransactOpts, account)
}

// UnfreezeAccount is a paid mutator transaction binding the contract method 0x788649ea.
//
// Solidity: function unfreezeAccount(address account) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactorSession) UnfreezeAccount(account common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.UnfreezeAccount(&_ChainGovernance.TransactOpts, account)
}

// UpdateCommitteeMemberWeight is a paid mutator transaction binding the contract method 0x246c3376.
//
// Solidity: function updateCommitteeMemberWeight(address user, int256 weight) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactor) UpdateCommitteeMemberWeight(opts *bind.TransactOpts, user common.Address, weight *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.contract.Transact(opts, "updateCommitteeMemberWeight", user, weight)
}

// UpdateCommitteeMemberWeight is a paid mutator transaction binding the contract method 0x246c3376.
//
// Solidity: function updateCommitteeMemberWeight(address user, int256 weight) returns(int256)
func (_ChainGovernance *ChainGovernanceSession) UpdateCommitteeMemberWeight(user common.Address, weight *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.UpdateCommitteeMemberWeight(&_ChainGovernance.TransactOpts, user, weight)
}

// UpdateCommitteeMemberWeight is a paid mutator transaction binding the contract method 0x246c3376.
//
// Solidity: function updateCommitteeMemberWeight(address user, int256 weight) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactorSession) UpdateCommitteeMemberWeight(user common.Address, weight *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.UpdateCommitteeMemberWeight(&_ChainGovernance.TransactOpts, user, weight)
}

// UpdateThreshold is a paid mutator transaction binding the contract method 0x97b00861.
//
// Solidity: function updateThreshold(int256 threshold) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactor) UpdateThreshold(opts *bind.TransactOpts, threshold *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.contract.Transact(opts, "updateThreshold", threshold)
}

// UpdateThreshold is a paid mutator transaction binding the contract method 0x97b00861.
//
// Solidity: function updateThreshold(int256 threshold) returns(int256)
func (_ChainGovernance *ChainGovernanceSession) UpdateThreshold(threshold *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.UpdateThreshold(&_ChainGovernance.TransactOpts, threshold)
}

// UpdateThreshold is a paid mutator transaction binding the contract method 0x97b00861.
//
// Solidity: function updateThreshold(int256 threshold) returns(int256)
func (_ChainGovernance *ChainGovernanceTransactorSession) UpdateThreshold(threshold *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ChainGovernance.Contract.UpdateThreshold(&_ChainGovernance.TransactOpts, threshold)
}
