// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package trade

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

// ModelTransactionABI is the input ABI used to generate the binding from.
const ModelTransactionABI = "[{\"constant\":true,\"inputs\":[{\"name\":\"_modelId\",\"type\":\"string\"},{\"name\":\"_transactionIndex\",\"type\":\"uint256\"}],\"name\":\"getTransaction\",\"outputs\":[{\"name\":\"modelId\",\"type\":\"string\"},{\"name\":\"seller\",\"type\":\"address\"},{\"name\":\"buyer\",\"type\":\"address\"},{\"name\":\"amount\",\"type\":\"uint256\"},{\"name\":\"timestamp\",\"type\":\"uint256\"},{\"name\":\"isCompleted\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_modelId\",\"type\":\"string\"}],\"name\":\"getTransactionsByModel\",\"outputs\":[{\"name\":\"sellers\",\"type\":\"address[]\"},{\"name\":\"buyers\",\"type\":\"address[]\"},{\"name\":\"amounts\",\"type\":\"uint256[]\"},{\"name\":\"timestamps\",\"type\":\"uint256[]\"},{\"name\":\"completions\",\"type\":\"bool[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_modelId\",\"type\":\"string\"},{\"name\":\"_seller\",\"type\":\"address\"},{\"name\":\"_buyer\",\"type\":\"address\"},{\"name\":\"_amount\",\"type\":\"uint256\"}],\"name\":\"createTransaction\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_modelId\",\"type\":\"string\"},{\"name\":\"_transactionIndex\",\"type\":\"uint256\"}],\"name\":\"voteForTransaction\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"modelId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"seller\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"buyer\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"timestamp\",\"type\":\"uint256\"}],\"name\":\"TransactionCreated\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"modelId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"transactionIndex\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"voter\",\"type\":\"address\"}],\"name\":\"VoteCompleted\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"modelId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"transactionIndex\",\"type\":\"uint256\"}],\"name\":\"TransactionCompleted\",\"type\":\"event\"}]"

// ModelTransactionBin is the compiled bytecode used for deploying new contracts.
var ModelTransactionBin = "0x608060405234801561001057600080fd5b50611b19806100206000396000f300608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806312b35c1c1461006757806319d94cc5146101d257806339f7e1bc146103b05780636286932a14610463575b600080fd5b34801561007357600080fd5b506100d8600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001909291905050506104d6565b60405180806020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200185815260200184815260200183151515158152602001828103825288818151815260200191508051906020019080838360005b83811015610192578082015181840152602081019050610177565b50505050905090810190601f1680156101bf5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b3480156101de57600080fd5b50610239600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061076b565b60405180806020018060200180602001806020018060200186810386528b818151815260200191508051906020019060200280838360005b8381101561028c578082015181840152602081019050610271565b5050505090500186810385528a818151815260200191508051906020019060200280838360005b838110156102ce5780820151818401526020810190506102b3565b50505050905001868103845289818151815260200191508051906020019060200280838360005b838110156103105780820151818401526020810190506102f5565b50505050905001868103835288818151815260200191508051906020019060200280838360005b83811015610352578082015181840152602081019050610337565b50505050905001868103825287818151815260200191508051906020019060200280838360005b83811015610394578082015181840152602081019050610379565b505050509050019a505050505050505050505060405180910390f35b3480156103bc57600080fd5b50610461600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610ae4565b005b34801561046f57600080fd5b506104d4600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192908035906020019092919050505061102d565b005b60606000806000806000806000896040518082805190602001908083835b60208310151561051957805182526020820191506020810190506020830392506104f4565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902080549050881015156105c6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f5472616e73616374696f6e20646f6573206e6f742065786973742e000000000081525060200191505060405180910390fd5b6000896040518082805190602001908083835b6020831015156105fe57805182526020820191506020810190506020830392506105d9565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208881548110151561063e57fe5b90600052602060002090600702019050806000018160010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168260020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16836003015484600401548560060160009054906101000a900460ff16858054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561074d5780601f106107225761010080835404028352916020019161074d565b820191906000526020600020905b81548152906001019060200180831161073057829003601f168201915b50505050509550965096509650965096509650509295509295509295565b6060806060806060600080600080896040518082805190602001908083835b6020831015156107af578051825260208201915060208101905060208303925061078a565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020805490509250826040519080825280602002602001820160405280156108175781602001602082028038833980820191505090505b509750826040519080825280602002602001820160405280156108495781602001602082028038833980820191505090505b5096508260405190808252806020026020018201604052801561087b5781602001602082028038833980820191505090505b509550826040519080825280602002602001820160405280156108ad5781602001602082028038833980820191505090505b509450826040519080825280602002602001820160405280156108df5781602001602082028038833980820191505090505b509350600091505b82821015610ad8576000896040518082805190602001908083835b6020831015156109275780518252602082019150602081019050602083039250610902565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208281548110151561096757fe5b906000526020600020906007020190508060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1688838151811015156109aa57fe5b9060200190602002019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250508060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168783815181101515610a1957fe5b9060200190602002019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff168152505080600301548683815181101515610a6857fe5b906020019060200201818152505080600401548583815181101515610a8957fe5b90602001906020020181815250508060060160009054906101000a900460ff168483815181101515610ab757fe5b906020019060200201901515908115158152505081806001019250506108e7565b50505091939590929450565b60008073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff1614151515610b8a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f53656c6c6572206164647265737320697320696e76616c69642e00000000000081525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1614151515610c2f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260198152602001807f4275796572206164647265737320697320696e76616c69642e0000000000000081525060200191505060405180910390fd5b600082111515610ccd576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602d8152602001807f5472616e73616374696f6e20616d6f756e74206d75737420626520677265617481526020017f6572207468616e207a65726f2e0000000000000000000000000000000000000081525060400191505060405180910390fd5b6000856040518082805190602001908083835b602083101515610d055780518252602082019150602081019050602083039250610ce0565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054809190600101610d4a919061192d565b506000856040518082805190602001908083835b602083101515610d835780518252602082019150602081019050602083039250610d5e565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060016000876040518082805190602001908083835b602083101515610df05780518252602082019150602081019050602083039250610dcb565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054905003815481101515610e3457fe5b9060005260206000209060070201905084816000019080519060200190610e5c92919061195f565b50838160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550828160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081816003018190555042816004018190555060008160060160006101000a81548160ff0219169083151502179055507fc77dc98ddea19ac999a0d4bbac9370bac6f7adac39536b04a6cc8dbf36d2c6ba858585854260405180806020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001848152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610fe8578082015181840152602081019050610fcd565b50505050905090810190601f1680156110155780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a15050505050565b600080836040518082805190602001908083835b6020831015156110665780518252602082019150602081019050602083039250611041565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054905082101515611113576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f5472616e73616374696f6e20646f6573206e6f742065786973742e000000000081525060200191505060405180910390fd5b6000836040518082805190602001908083835b60208310151561114b5780518252602082019150602081019050602083039250611126565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208281548110151561118b57fe5b906000526020600020906007020160060160009054906101000a900460ff16151515611245576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260218152602001807f5472616e73616374696f6e20697320616c726561647920636f6d706c6574656481526020017f2e0000000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6000836040518082805190602001908083835b60208310151561127d5780518252602082019150602081019050602083039250611258565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020828154811015156112bd57fe5b906000526020600020906007020160010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806113fc57506000836040518082805190602001908083835b60208310151561135b5780518252602082019150602081019050602083039250611336565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208281548110151561139b57fe5b906000526020600020906007020160020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b1515611470576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f4f6e6c792073656c6c6572206f722062757965722063616e20766f74652e000081525060200191505060405180910390fd5b6000836040518082805190602001908083835b6020831015156114a85780518252602082019150602081019050602083039250611483565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020828154811015156114e857fe5b906000526020600020906007020160050160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515156115b9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f596f75206861766520616c726561647920766f7465642e00000000000000000081525060200191505060405180910390fd5b6000836040518082805190602001908083835b6020831015156115f157805182526020820191506020810190506020830392506115cc565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208281548110151561163157fe5b9060005260206000209060070201905060018160050160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055507f50fe0359d11b15588781bd182af399dfd83f69442dc1584553204a9637f0697d83833360405180806020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825285818151815260200191508051906020019080838360005b8381101561173757808201518184015260208101905061171c565b50505050905090810190601f1680156117645780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a18060050160008260010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16801561186157508060050160008260020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff165b156119285760018160060160006101000a81548160ff0219169083151502179055507f751228e6d7f5ddefd2b36f753979e05485845bd412fbd0effbd3bb021a2ed04b83836040518080602001838152602001828103825284818151815260200191508051906020019080838360005b838110156118ec5780820151818401526020810190506118d1565b50505050905090810190601f1680156119195780820380516001836020036101000a031916815260200191505b50935050505060405180910390a15b505050565b81548183558181111561195a5760070281600702836000526020600020918201910161195991906119df565b5b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106119a057805160ff19168380011785556119ce565b828001600101855582156119ce579182015b828111156119cd5782518255916020019190600101906119b2565b5b5090506119db9190611a80565b5090565b611a7d91905b80821115611a7957600080820160006119fe9190611aa5565b6001820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556002820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055600382016000905560048201600090556006820160006101000a81549060ff0219169055506007016119e5565b5090565b90565b611aa291905b80821115611a9e576000816000905550600101611a86565b5090565b90565b50805460018160011615610100020316600290046000825580601f10611acb5750611aea565b601f016020900490600052602060002090810190611ae99190611a80565b5b505600a165627a7a7230582012bcb688e9de342a047c0feb10fafe916cacd474172645f67c6de870c29686840029"

// DeployModelTransaction deploys a new contract, binding an instance of ModelTransaction to it.
func DeployModelTransaction(auth *bind.TransactOpts, backend bind.ContractBackend) (common.Address, *types.Transaction, *ModelTransaction, error) {
	parsed, err := abi.JSON(strings.NewReader(ModelTransactionABI))
	if err != nil {
		return common.Address{}, nil, nil, err
	}

	address, tx, contract, err := bind.DeployContract(auth, parsed, common.FromHex(ModelTransactionBin), backend)
	if err != nil {
		return common.Address{}, nil, nil, err
	}
	return address, tx, &ModelTransaction{ModelTransactionCaller: ModelTransactionCaller{contract: contract}, ModelTransactionTransactor: ModelTransactionTransactor{contract: contract}, ModelTransactionFilterer: ModelTransactionFilterer{contract: contract}}, nil
}

func AsyncDeployModelTransaction(auth *bind.TransactOpts, handler func(*types.Receipt, error), backend bind.ContractBackend) (*types.Transaction, error) {
	parsed, err := abi.JSON(strings.NewReader(ModelTransactionABI))
	if err != nil {
		return nil, err
	}

	tx, err := bind.AsyncDeployContract(auth, handler, parsed, common.FromHex(ModelTransactionBin), backend)
	if err != nil {
		return nil, err
	}
	return tx, nil
}

// ModelTransaction is an auto generated Go binding around a Solidity contract.
type ModelTransaction struct {
	ModelTransactionCaller     // Read-only binding to the contract
	ModelTransactionTransactor // Write-only binding to the contract
	ModelTransactionFilterer   // Log filterer for contract events
}

// ModelTransactionCaller is an auto generated read-only Go binding around a Solidity contract.
type ModelTransactionCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ModelTransactionTransactor is an auto generated write-only Go binding around a Solidity contract.
type ModelTransactionTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ModelTransactionFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type ModelTransactionFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ModelTransactionSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type ModelTransactionSession struct {
	Contract     *ModelTransaction // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// ModelTransactionCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type ModelTransactionCallerSession struct {
	Contract *ModelTransactionCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts           // Call options to use throughout this session
}

// ModelTransactionTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type ModelTransactionTransactorSession struct {
	Contract     *ModelTransactionTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts           // Transaction auth options to use throughout this session
}

// ModelTransactionRaw is an auto generated low-level Go binding around a Solidity contract.
type ModelTransactionRaw struct {
	Contract *ModelTransaction // Generic contract binding to access the raw methods on
}

// ModelTransactionCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type ModelTransactionCallerRaw struct {
	Contract *ModelTransactionCaller // Generic read-only contract binding to access the raw methods on
}

// ModelTransactionTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type ModelTransactionTransactorRaw struct {
	Contract *ModelTransactionTransactor // Generic write-only contract binding to access the raw methods on
}

// NewModelTransaction creates a new instance of ModelTransaction, bound to a specific deployed contract.
func NewModelTransaction(address common.Address, backend bind.ContractBackend) (*ModelTransaction, error) {
	contract, err := bindModelTransaction(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &ModelTransaction{ModelTransactionCaller: ModelTransactionCaller{contract: contract}, ModelTransactionTransactor: ModelTransactionTransactor{contract: contract}, ModelTransactionFilterer: ModelTransactionFilterer{contract: contract}}, nil
}

// NewModelTransactionCaller creates a new read-only instance of ModelTransaction, bound to a specific deployed contract.
func NewModelTransactionCaller(address common.Address, caller bind.ContractCaller) (*ModelTransactionCaller, error) {
	contract, err := bindModelTransaction(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &ModelTransactionCaller{contract: contract}, nil
}

// NewModelTransactionTransactor creates a new write-only instance of ModelTransaction, bound to a specific deployed contract.
func NewModelTransactionTransactor(address common.Address, transactor bind.ContractTransactor) (*ModelTransactionTransactor, error) {
	contract, err := bindModelTransaction(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &ModelTransactionTransactor{contract: contract}, nil
}

// NewModelTransactionFilterer creates a new log filterer instance of ModelTransaction, bound to a specific deployed contract.
func NewModelTransactionFilterer(address common.Address, filterer bind.ContractFilterer) (*ModelTransactionFilterer, error) {
	contract, err := bindModelTransaction(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &ModelTransactionFilterer{contract: contract}, nil
}

// bindModelTransaction binds a generic wrapper to an already deployed contract.
func bindModelTransaction(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(ModelTransactionABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_ModelTransaction *ModelTransactionRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _ModelTransaction.Contract.ModelTransactionCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_ModelTransaction *ModelTransactionRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _ModelTransaction.Contract.ModelTransactionTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_ModelTransaction *ModelTransactionRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _ModelTransaction.Contract.ModelTransactionTransactor.contract.TransactWithResult(opts, result, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_ModelTransaction *ModelTransactionCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _ModelTransaction.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_ModelTransaction *ModelTransactionTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _ModelTransaction.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_ModelTransaction *ModelTransactionTransactorRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _ModelTransaction.Contract.contract.TransactWithResult(opts, result, method, params...)
}

// GetTransaction is a free data retrieval call binding the contract method 0x12b35c1c.
//
// Solidity: function getTransaction(string _modelId, uint256 _transactionIndex) constant returns(string modelId, address seller, address buyer, uint256 amount, uint256 timestamp, bool isCompleted)
func (_ModelTransaction *ModelTransactionCaller) GetTransaction(opts *bind.CallOpts, _modelId string, _transactionIndex *big.Int) (struct {
	ModelId     string
	Seller      common.Address
	Buyer       common.Address
	Amount      *big.Int
	Timestamp   *big.Int
	IsCompleted bool
}, error) {
	ret := new(struct {
		ModelId     string
		Seller      common.Address
		Buyer       common.Address
		Amount      *big.Int
		Timestamp   *big.Int
		IsCompleted bool
	})
	out := ret
	err := _ModelTransaction.contract.Call(opts, out, "getTransaction", _modelId, _transactionIndex)
	return *ret, err
}

// GetTransaction is a free data retrieval call binding the contract method 0x12b35c1c.
//
// Solidity: function getTransaction(string _modelId, uint256 _transactionIndex) constant returns(string modelId, address seller, address buyer, uint256 amount, uint256 timestamp, bool isCompleted)
func (_ModelTransaction *ModelTransactionSession) GetTransaction(_modelId string, _transactionIndex *big.Int) (struct {
	ModelId     string
	Seller      common.Address
	Buyer       common.Address
	Amount      *big.Int
	Timestamp   *big.Int
	IsCompleted bool
}, error) {
	return _ModelTransaction.Contract.GetTransaction(&_ModelTransaction.CallOpts, _modelId, _transactionIndex)
}

// GetTransaction is a free data retrieval call binding the contract method 0x12b35c1c.
//
// Solidity: function getTransaction(string _modelId, uint256 _transactionIndex) constant returns(string modelId, address seller, address buyer, uint256 amount, uint256 timestamp, bool isCompleted)
func (_ModelTransaction *ModelTransactionCallerSession) GetTransaction(_modelId string, _transactionIndex *big.Int) (struct {
	ModelId     string
	Seller      common.Address
	Buyer       common.Address
	Amount      *big.Int
	Timestamp   *big.Int
	IsCompleted bool
}, error) {
	return _ModelTransaction.Contract.GetTransaction(&_ModelTransaction.CallOpts, _modelId, _transactionIndex)
}

// GetTransactionsByModel is a free data retrieval call binding the contract method 0x19d94cc5.
//
// Solidity: function getTransactionsByModel(string _modelId) constant returns(address[] sellers, address[] buyers, uint256[] amounts, uint256[] timestamps, bool[] completions)
func (_ModelTransaction *ModelTransactionCaller) GetTransactionsByModel(opts *bind.CallOpts, _modelId string) (struct {
	Sellers     []common.Address
	Buyers      []common.Address
	Amounts     []*big.Int
	Timestamps  []*big.Int
	Completions []bool
}, error) {
	ret := new(struct {
		Sellers     []common.Address
		Buyers      []common.Address
		Amounts     []*big.Int
		Timestamps  []*big.Int
		Completions []bool
	})
	out := ret
	err := _ModelTransaction.contract.Call(opts, out, "getTransactionsByModel", _modelId)
	return *ret, err
}

// GetTransactionsByModel is a free data retrieval call binding the contract method 0x19d94cc5.
//
// Solidity: function getTransactionsByModel(string _modelId) constant returns(address[] sellers, address[] buyers, uint256[] amounts, uint256[] timestamps, bool[] completions)
func (_ModelTransaction *ModelTransactionSession) GetTransactionsByModel(_modelId string) (struct {
	Sellers     []common.Address
	Buyers      []common.Address
	Amounts     []*big.Int
	Timestamps  []*big.Int
	Completions []bool
}, error) {
	return _ModelTransaction.Contract.GetTransactionsByModel(&_ModelTransaction.CallOpts, _modelId)
}

// GetTransactionsByModel is a free data retrieval call binding the contract method 0x19d94cc5.
//
// Solidity: function getTransactionsByModel(string _modelId) constant returns(address[] sellers, address[] buyers, uint256[] amounts, uint256[] timestamps, bool[] completions)
func (_ModelTransaction *ModelTransactionCallerSession) GetTransactionsByModel(_modelId string) (struct {
	Sellers     []common.Address
	Buyers      []common.Address
	Amounts     []*big.Int
	Timestamps  []*big.Int
	Completions []bool
}, error) {
	return _ModelTransaction.Contract.GetTransactionsByModel(&_ModelTransaction.CallOpts, _modelId)
}

// CreateTransaction is a paid mutator transaction binding the contract method 0x39f7e1bc.
//
// Solidity: function createTransaction(string _modelId, address _seller, address _buyer, uint256 _amount) returns()
func (_ModelTransaction *ModelTransactionTransactor) CreateTransaction(opts *bind.TransactOpts, _modelId string, _seller common.Address, _buyer common.Address, _amount *big.Int) (*types.Transaction, *types.Receipt, error) {
	var ()
	out := &[]interface{}{}
	transaction, receipt, err := _ModelTransaction.contract.TransactWithResult(opts, out, "createTransaction", _modelId, _seller, _buyer, _amount)
	return transaction, receipt, err
}

func (_ModelTransaction *ModelTransactionTransactor) AsyncCreateTransaction(handler func(*types.Receipt, error), opts *bind.TransactOpts, _modelId string, _seller common.Address, _buyer common.Address, _amount *big.Int) (*types.Transaction, error) {
	return _ModelTransaction.contract.AsyncTransact(opts, handler, "createTransaction", _modelId, _seller, _buyer, _amount)
}

// CreateTransaction is a paid mutator transaction binding the contract method 0x39f7e1bc.
//
// Solidity: function createTransaction(string _modelId, address _seller, address _buyer, uint256 _amount) returns()
func (_ModelTransaction *ModelTransactionSession) CreateTransaction(_modelId string, _seller common.Address, _buyer common.Address, _amount *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ModelTransaction.Contract.CreateTransaction(&_ModelTransaction.TransactOpts, _modelId, _seller, _buyer, _amount)
}

func (_ModelTransaction *ModelTransactionSession) AsyncCreateTransaction(handler func(*types.Receipt, error), _modelId string, _seller common.Address, _buyer common.Address, _amount *big.Int) (*types.Transaction, error) {
	return _ModelTransaction.Contract.AsyncCreateTransaction(handler, &_ModelTransaction.TransactOpts, _modelId, _seller, _buyer, _amount)
}

// CreateTransaction is a paid mutator transaction binding the contract method 0x39f7e1bc.
//
// Solidity: function createTransaction(string _modelId, address _seller, address _buyer, uint256 _amount) returns()
func (_ModelTransaction *ModelTransactionTransactorSession) CreateTransaction(_modelId string, _seller common.Address, _buyer common.Address, _amount *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ModelTransaction.Contract.CreateTransaction(&_ModelTransaction.TransactOpts, _modelId, _seller, _buyer, _amount)
}

func (_ModelTransaction *ModelTransactionTransactorSession) AsyncCreateTransaction(handler func(*types.Receipt, error), _modelId string, _seller common.Address, _buyer common.Address, _amount *big.Int) (*types.Transaction, error) {
	return _ModelTransaction.Contract.AsyncCreateTransaction(handler, &_ModelTransaction.TransactOpts, _modelId, _seller, _buyer, _amount)
}

// VoteForTransaction is a paid mutator transaction binding the contract method 0x6286932a.
//
// Solidity: function voteForTransaction(string _modelId, uint256 _transactionIndex) returns()
func (_ModelTransaction *ModelTransactionTransactor) VoteForTransaction(opts *bind.TransactOpts, _modelId string, _transactionIndex *big.Int) (*types.Transaction, *types.Receipt, error) {
	var ()
	out := &[]interface{}{}
	transaction, receipt, err := _ModelTransaction.contract.TransactWithResult(opts, out, "voteForTransaction", _modelId, _transactionIndex)
	return transaction, receipt, err
}

func (_ModelTransaction *ModelTransactionTransactor) AsyncVoteForTransaction(handler func(*types.Receipt, error), opts *bind.TransactOpts, _modelId string, _transactionIndex *big.Int) (*types.Transaction, error) {
	return _ModelTransaction.contract.AsyncTransact(opts, handler, "voteForTransaction", _modelId, _transactionIndex)
}

// VoteForTransaction is a paid mutator transaction binding the contract method 0x6286932a.
//
// Solidity: function voteForTransaction(string _modelId, uint256 _transactionIndex) returns()
func (_ModelTransaction *ModelTransactionSession) VoteForTransaction(_modelId string, _transactionIndex *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ModelTransaction.Contract.VoteForTransaction(&_ModelTransaction.TransactOpts, _modelId, _transactionIndex)
}

func (_ModelTransaction *ModelTransactionSession) AsyncVoteForTransaction(handler func(*types.Receipt, error), _modelId string, _transactionIndex *big.Int) (*types.Transaction, error) {
	return _ModelTransaction.Contract.AsyncVoteForTransaction(handler, &_ModelTransaction.TransactOpts, _modelId, _transactionIndex)
}

// VoteForTransaction is a paid mutator transaction binding the contract method 0x6286932a.
//
// Solidity: function voteForTransaction(string _modelId, uint256 _transactionIndex) returns()
func (_ModelTransaction *ModelTransactionTransactorSession) VoteForTransaction(_modelId string, _transactionIndex *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ModelTransaction.Contract.VoteForTransaction(&_ModelTransaction.TransactOpts, _modelId, _transactionIndex)
}

func (_ModelTransaction *ModelTransactionTransactorSession) AsyncVoteForTransaction(handler func(*types.Receipt, error), _modelId string, _transactionIndex *big.Int) (*types.Transaction, error) {
	return _ModelTransaction.Contract.AsyncVoteForTransaction(handler, &_ModelTransaction.TransactOpts, _modelId, _transactionIndex)
}

// ModelTransactionTransactionCompleted represents a TransactionCompleted event raised by the ModelTransaction contract.
type ModelTransactionTransactionCompleted struct {
	ModelId          string
	TransactionIndex *big.Int
	Raw              types.Log // Blockchain specific contextual infos
}

// WatchTransactionCompleted is a free log subscription operation binding the contract event 0x751228e6d7f5ddefd2b36f753979e05485845bd412fbd0effbd3bb021a2ed04b.
//
// Solidity: event TransactionCompleted(string modelId, uint256 transactionIndex)
func (_ModelTransaction *ModelTransactionFilterer) WatchTransactionCompleted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.contract.WatchLogs(fromBlock, handler, "TransactionCompleted")
}

func (_ModelTransaction *ModelTransactionFilterer) WatchAllTransactionCompleted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.contract.WatchLogs(fromBlock, handler, "TransactionCompleted")
}

// ParseTransactionCompleted is a log parse operation binding the contract event 0x751228e6d7f5ddefd2b36f753979e05485845bd412fbd0effbd3bb021a2ed04b.
//
// Solidity: event TransactionCompleted(string modelId, uint256 transactionIndex)
func (_ModelTransaction *ModelTransactionFilterer) ParseTransactionCompleted(log types.Log) (*ModelTransactionTransactionCompleted, error) {
	event := new(ModelTransactionTransactionCompleted)
	if err := _ModelTransaction.contract.UnpackLog(event, "TransactionCompleted", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchTransactionCompleted is a free log subscription operation binding the contract event 0x751228e6d7f5ddefd2b36f753979e05485845bd412fbd0effbd3bb021a2ed04b.
//
// Solidity: event TransactionCompleted(string modelId, uint256 transactionIndex)
func (_ModelTransaction *ModelTransactionSession) WatchTransactionCompleted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.Contract.WatchTransactionCompleted(fromBlock, handler)
}

func (_ModelTransaction *ModelTransactionSession) WatchAllTransactionCompleted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.Contract.WatchAllTransactionCompleted(fromBlock, handler)
}

// ParseTransactionCompleted is a log parse operation binding the contract event 0x751228e6d7f5ddefd2b36f753979e05485845bd412fbd0effbd3bb021a2ed04b.
//
// Solidity: event TransactionCompleted(string modelId, uint256 transactionIndex)
func (_ModelTransaction *ModelTransactionSession) ParseTransactionCompleted(log types.Log) (*ModelTransactionTransactionCompleted, error) {
	return _ModelTransaction.Contract.ParseTransactionCompleted(log)
}

// ModelTransactionTransactionCreated represents a TransactionCreated event raised by the ModelTransaction contract.
type ModelTransactionTransactionCreated struct {
	ModelId   string
	Seller    common.Address
	Buyer     common.Address
	Amount    *big.Int
	Timestamp *big.Int
	Raw       types.Log // Blockchain specific contextual infos
}

// WatchTransactionCreated is a free log subscription operation binding the contract event 0xc77dc98ddea19ac999a0d4bbac9370bac6f7adac39536b04a6cc8dbf36d2c6ba.
//
// Solidity: event TransactionCreated(string modelId, address seller, address buyer, uint256 amount, uint256 timestamp)
func (_ModelTransaction *ModelTransactionFilterer) WatchTransactionCreated(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.contract.WatchLogs(fromBlock, handler, "TransactionCreated")
}

func (_ModelTransaction *ModelTransactionFilterer) WatchAllTransactionCreated(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.contract.WatchLogs(fromBlock, handler, "TransactionCreated")
}

// ParseTransactionCreated is a log parse operation binding the contract event 0xc77dc98ddea19ac999a0d4bbac9370bac6f7adac39536b04a6cc8dbf36d2c6ba.
//
// Solidity: event TransactionCreated(string modelId, address seller, address buyer, uint256 amount, uint256 timestamp)
func (_ModelTransaction *ModelTransactionFilterer) ParseTransactionCreated(log types.Log) (*ModelTransactionTransactionCreated, error) {
	event := new(ModelTransactionTransactionCreated)
	if err := _ModelTransaction.contract.UnpackLog(event, "TransactionCreated", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchTransactionCreated is a free log subscription operation binding the contract event 0xc77dc98ddea19ac999a0d4bbac9370bac6f7adac39536b04a6cc8dbf36d2c6ba.
//
// Solidity: event TransactionCreated(string modelId, address seller, address buyer, uint256 amount, uint256 timestamp)
func (_ModelTransaction *ModelTransactionSession) WatchTransactionCreated(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.Contract.WatchTransactionCreated(fromBlock, handler)
}

func (_ModelTransaction *ModelTransactionSession) WatchAllTransactionCreated(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.Contract.WatchAllTransactionCreated(fromBlock, handler)
}

// ParseTransactionCreated is a log parse operation binding the contract event 0xc77dc98ddea19ac999a0d4bbac9370bac6f7adac39536b04a6cc8dbf36d2c6ba.
//
// Solidity: event TransactionCreated(string modelId, address seller, address buyer, uint256 amount, uint256 timestamp)
func (_ModelTransaction *ModelTransactionSession) ParseTransactionCreated(log types.Log) (*ModelTransactionTransactionCreated, error) {
	return _ModelTransaction.Contract.ParseTransactionCreated(log)
}

// ModelTransactionVoteCompleted represents a VoteCompleted event raised by the ModelTransaction contract.
type ModelTransactionVoteCompleted struct {
	ModelId          string
	TransactionIndex *big.Int
	Voter            common.Address
	Raw              types.Log // Blockchain specific contextual infos
}

// WatchVoteCompleted is a free log subscription operation binding the contract event 0x50fe0359d11b15588781bd182af399dfd83f69442dc1584553204a9637f0697d.
//
// Solidity: event VoteCompleted(string modelId, uint256 transactionIndex, address voter)
func (_ModelTransaction *ModelTransactionFilterer) WatchVoteCompleted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.contract.WatchLogs(fromBlock, handler, "VoteCompleted")
}

func (_ModelTransaction *ModelTransactionFilterer) WatchAllVoteCompleted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.contract.WatchLogs(fromBlock, handler, "VoteCompleted")
}

// ParseVoteCompleted is a log parse operation binding the contract event 0x50fe0359d11b15588781bd182af399dfd83f69442dc1584553204a9637f0697d.
//
// Solidity: event VoteCompleted(string modelId, uint256 transactionIndex, address voter)
func (_ModelTransaction *ModelTransactionFilterer) ParseVoteCompleted(log types.Log) (*ModelTransactionVoteCompleted, error) {
	event := new(ModelTransactionVoteCompleted)
	if err := _ModelTransaction.contract.UnpackLog(event, "VoteCompleted", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchVoteCompleted is a free log subscription operation binding the contract event 0x50fe0359d11b15588781bd182af399dfd83f69442dc1584553204a9637f0697d.
//
// Solidity: event VoteCompleted(string modelId, uint256 transactionIndex, address voter)
func (_ModelTransaction *ModelTransactionSession) WatchVoteCompleted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.Contract.WatchVoteCompleted(fromBlock, handler)
}

func (_ModelTransaction *ModelTransactionSession) WatchAllVoteCompleted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelTransaction.Contract.WatchAllVoteCompleted(fromBlock, handler)
}

// ParseVoteCompleted is a log parse operation binding the contract event 0x50fe0359d11b15588781bd182af399dfd83f69442dc1584553204a9637f0697d.
//
// Solidity: event VoteCompleted(string modelId, uint256 transactionIndex, address voter)
func (_ModelTransaction *ModelTransactionSession) ParseVoteCompleted(log types.Log) (*ModelTransactionVoteCompleted, error) {
	return _ModelTransaction.Contract.ParseVoteCompleted(log)
}
