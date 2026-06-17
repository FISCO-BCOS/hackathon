// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package modelregistry

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

// ModelRegistryABI is the input ABI used to generate the binding from.
const ModelRegistryABI = "[{\"constant\":true,\"inputs\":[{\"name\":\"_modelId\",\"type\":\"string\"}],\"name\":\"getProposal\",\"outputs\":[{\"name\":\"modelId\",\"type\":\"string\"},{\"name\":\"modelHash\",\"type\":\"string\"},{\"name\":\"reportHash\",\"type\":\"string\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"votes\",\"type\":\"uint256\"},{\"name\":\"isApproved\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_modelId\",\"type\":\"string\"}],\"name\":\"voteForProposal\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"threshold\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_newThreshold\",\"type\":\"uint256\"}],\"name\":\"updateThreshold\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"}],\"name\":\"evaluators\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_modelId\",\"type\":\"string\"},{\"name\":\"_modelHash\",\"type\":\"string\"},{\"name\":\"_reportHash\",\"type\":\"string\"},{\"name\":\"_owner\",\"type\":\"address\"}],\"name\":\"createProposal\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_evaluator\",\"type\":\"address\"}],\"name\":\"addEvaluator\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"admin\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_modelId\",\"type\":\"string\"}],\"name\":\"getModel\",\"outputs\":[{\"name\":\"modelHash\",\"type\":\"string\"},{\"name\":\"reportHash\",\"type\":\"string\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"timestamp\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_threshold\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"modelId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"proposer\",\"type\":\"address\"}],\"name\":\"ProposalCreated\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"modelId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"voter\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"totalVotes\",\"type\":\"uint256\"}],\"name\":\"VoteCasted\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"modelId\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"timestamp\",\"type\":\"uint256\"}],\"name\":\"ModelApproved\",\"type\":\"event\"}]"

// ModelRegistryBin is the compiled bytecode used for deploying new contracts.
var ModelRegistryBin = "0x608060405234801561001057600080fd5b50604051602080611d358339810180604052810190808051906020019092919050505033600460006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508060038190555050611caa8061008b6000396000f300608060405260043610610099576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806325d3a09d1461009e5780632ba78f991461029d57806342cde4e814610306578063d7d7442f14610331578063e50d582c1461035e578063e84f10f2146103b9578063eb26e5f0146104ce578063f851a44014610511578063fab5740414610568575b600080fd5b3480156100aa57600080fd5b50610105600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506106f0565b604051808060200180602001806020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018681526020018515151515815260200184810384528a818151815260200191508051906020019080838360005b8381101561018f578082015181840152602081019050610174565b50505050905090810190601f1680156101bc5780820380516001836020036101000a031916815260200191505b50848103835289818151815260200191508051906020019080838360005b838110156101f55780820151818401526020810190506101da565b50505050905090810190601f1680156102225780820380516001836020036101000a031916815260200191505b50848103825288818151815260200191508051906020019080838360005b8381101561025b578082015181840152602081019050610240565b50505050905090810190601f1680156102885780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b3480156102a957600080fd5b50610304600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610a63565b005b34801561031257600080fd5b5061031b611253565b6040518082815260200191505060405180910390f35b34801561033d57600080fd5b5061035c60048036038101908080359060200190929190505050611259565b005b34801561036a57600080fd5b5061039f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061134e565b604051808215151515815260200191505060405180910390f35b3480156103c557600080fd5b506104cc600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061136e565b005b3480156104da57600080fd5b5061050f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506117db565b005b34801561051d57600080fd5b506105266118fb565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561057457600080fd5b506105cf600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050611921565b6040518080602001806020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001848152602001838103835287818151815260200191508051906020019080838360005b8381101561064b578082015181840152602081019050610630565b50505050905090810190601f1680156106785780820380516001836020036101000a031916815260200191505b50838103825286818151815260200191508051906020019080838360005b838110156106b1578082015181840152602081019050610696565b50505050905090810190601f1680156106de5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b60608060606000806000806000886040518082805190602001908083835b602083101515610733578051825260208201915060208101905060208303925061070e565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390209050600073ffffffffffffffffffffffffffffffffffffffff168160030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151515610831576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f50726f706f73616c20646f6573206e6f742065786973742e000000000000000081525060200191505060405180910390fd5b8060000181600101826002018360030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1684600401548560050160009054906101000a900460ff16858054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561090e5780601f106108e35761010080835404028352916020019161090e565b820191906000526020600020905b8154815290600101906020018083116108f157829003601f168201915b50505050509550848054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109aa5780601f1061097f576101008083540402835291602001916109aa565b820191906000526020600020905b81548152906001019060200180831161098d57829003601f168201915b50505050509450838054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a465780601f10610a1b57610100808354040283529160200191610a46565b820191906000526020600020905b815481529060010190602001808311610a2957829003601f168201915b505050505093509650965096509650965096505091939550919395565b6000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515610b26576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260198152602001807f4f6e6c79206576616c7561746f72732063616e20766f74652e0000000000000081525060200191505060405180910390fd5b6000826040518082805190602001908083835b602083101515610b5e5780518252602082019150602081019050602083039250610b39565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390209050600073ffffffffffffffffffffffffffffffffffffffff168160030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151515610c5c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f50726f706f73616c20646f6573206e6f742065786973742e000000000000000081525060200191505060405180910390fd5b8060060160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16151515610d20576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f596f75206861766520616c726561647920766f7465642e00000000000000000081525060200191505060405180910390fd5b8060050160009054906101000a900460ff16151515610da7576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f50726f706f73616c20697320616c726561647920617070726f7665642e00000081525060200191505060405180910390fd5b60018160060160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555080600401600081548092919060010191905055507fffb039b229183b0cb867e5598d1788cc4130c39b74d1da506a000d910759d0c88233836004015460405180806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828103825285818151815260200191508051906020019080838360005b83811015610eb5578082015181840152602081019050610e9a565b50505050905090810190601f168015610ee25780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a1600354816004015410151561124f5760018160050160006101000a81548160ff021916908315150217905550608060405190810160405280826001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610fc15780601f10610f9657610100808354040283529160200191610fc1565b820191906000526020600020905b815481529060010190602001808311610fa457829003601f168201915b50505050508152602001826002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156110635780601f1061103857610100808354040283529160200191611063565b820191906000526020600020905b81548152906001019060200180831161104657829003601f168201915b505050505081526020018260030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001428152506001836040518082805190602001908083835b6020831015156110e957805182526020820191506020810190506020830392506110c4565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206000820151816000019080519060200190611138929190611bd9565b506020820151816001019080519060200190611155929190611bd9565b5060408201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550606082015181600301559050507f4a91d635bdf5fe8e3e48c1011d1abe1eedbd70c2915b81f2dee0d5b10a9368d182426040518080602001838152602001828103825284818151815260200191508051906020019080838360005b838110156112135780820151818401526020810190506111f8565b50505050905090810190601f1680156112405780820380516001836020036101000a031916815260200191505b50935050505060405180910390a15b5050565b60035481565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611344576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001807f4f6e6c792061646d696e2063616e20757064617465207468652074687265736881526020017f6f6c642e0000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b8060038190555050565b60026020528060005260406000206000915054906101000a900460ff1681565b600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515611455576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001807f4f6e6c79206576616c7561746f72732063616e206372656174652070726f706f81526020017f73616c732e00000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff166000856040518082805190602001908083835b6020831015156114a55780518252602082019150602081019050602083039250611480565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515611587576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f4d6f64656c20494420616c7265616479206578697374732e000000000000000081525060200191505060405180910390fd5b60c0604051908101604052808581526020018481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff16815260200160008152602001600015158152506000856040518082805190602001908083835b60208310151561160757805182526020820191506020810190506020830392506115e2565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206000820151816000019080519060200190611656929190611bd9565b506020820151816001019080519060200190611673929190611bd9565b506040820151816002019080519060200190611690929190611bd9565b5060608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506080820151816004015560a08201518160050160006101000a81548160ff0219169083151502179055509050507f6b34733d94be17886338d1436b2c705b81d42edc9ae3b26f9e6634e4f33d8e2a843360405180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b8381101561179a57808201518184015260208101905061177f565b50505050905090810190601f1680156117c75780820380516001836020036101000a031916815260200191505b50935050505060405180910390a150505050565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156118a0576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f4f6e6c792061646d696e2063616e20616464206576616c7561746f72732e000081525060200191505060405180910390fd5b6001600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60608060008060006001866040518082805190602001908083835b602083101515611961578051825260208201915060208101905060208303925061193c565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390209050600073ffffffffffffffffffffffffffffffffffffffff168160020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151515611a5f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f4d6f64656c20646f6573206e6f742065786973742e000000000000000000000081525060200191505060405180910390fd5b80600001816001018260020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168360030154838054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611b265780601f10611afb57610100808354040283529160200191611b26565b820191906000526020600020905b815481529060010190602001808311611b0957829003601f168201915b50505050509350828054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611bc25780601f10611b9757610100808354040283529160200191611bc2565b820191906000526020600020905b815481529060010190602001808311611ba557829003601f168201915b505050505092509450945094509450509193509193565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611c1a57805160ff1916838001178555611c48565b82800160010185558215611c48579182015b82811115611c47578251825591602001919060010190611c2c565b5b509050611c559190611c59565b5090565b611c7b91905b80821115611c77576000816000905550600101611c5f565b5090565b905600a165627a7a72305820edd9bcb74ab57d79e74e8babb2cd10a5700a32814fdb7b688fa6726d7d0340ff0029"

// DeployModelRegistry deploys a new contract, binding an instance of ModelRegistry to it.
func DeployModelRegistry(auth *bind.TransactOpts, backend bind.ContractBackend, _threshold *big.Int) (common.Address, *types.Transaction, *ModelRegistry, error) {
	parsed, err := abi.JSON(strings.NewReader(ModelRegistryABI))
	if err != nil {
		return common.Address{}, nil, nil, err
	}

	address, tx, contract, err := bind.DeployContract(auth, parsed, common.FromHex(ModelRegistryBin), backend, _threshold)
	if err != nil {
		return common.Address{}, nil, nil, err
	}
	return address, tx, &ModelRegistry{ModelRegistryCaller: ModelRegistryCaller{contract: contract}, ModelRegistryTransactor: ModelRegistryTransactor{contract: contract}, ModelRegistryFilterer: ModelRegistryFilterer{contract: contract}}, nil
}

func AsyncDeployModelRegistry(auth *bind.TransactOpts, handler func(*types.Receipt, error), backend bind.ContractBackend, _threshold *big.Int) (*types.Transaction, error) {
	parsed, err := abi.JSON(strings.NewReader(ModelRegistryABI))
	if err != nil {
		return nil, err
	}

	tx, err := bind.AsyncDeployContract(auth, handler, parsed, common.FromHex(ModelRegistryBin), backend, _threshold)
	if err != nil {
		return nil, err
	}
	return tx, nil
}

// ModelRegistry is an auto generated Go binding around a Solidity contract.
type ModelRegistry struct {
	ModelRegistryCaller     // Read-only binding to the contract
	ModelRegistryTransactor // Write-only binding to the contract
	ModelRegistryFilterer   // Log filterer for contract events
}

// ModelRegistryCaller is an auto generated read-only Go binding around a Solidity contract.
type ModelRegistryCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ModelRegistryTransactor is an auto generated write-only Go binding around a Solidity contract.
type ModelRegistryTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ModelRegistryFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type ModelRegistryFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// ModelRegistrySession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type ModelRegistrySession struct {
	Contract     *ModelRegistry    // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// ModelRegistryCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type ModelRegistryCallerSession struct {
	Contract *ModelRegistryCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts        // Call options to use throughout this session
}

// ModelRegistryTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type ModelRegistryTransactorSession struct {
	Contract     *ModelRegistryTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts        // Transaction auth options to use throughout this session
}

// ModelRegistryRaw is an auto generated low-level Go binding around a Solidity contract.
type ModelRegistryRaw struct {
	Contract *ModelRegistry // Generic contract binding to access the raw methods on
}

// ModelRegistryCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type ModelRegistryCallerRaw struct {
	Contract *ModelRegistryCaller // Generic read-only contract binding to access the raw methods on
}

// ModelRegistryTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type ModelRegistryTransactorRaw struct {
	Contract *ModelRegistryTransactor // Generic write-only contract binding to access the raw methods on
}

// NewModelRegistry creates a new instance of ModelRegistry, bound to a specific deployed contract.
func NewModelRegistry(address common.Address, backend bind.ContractBackend) (*ModelRegistry, error) {
	contract, err := bindModelRegistry(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &ModelRegistry{ModelRegistryCaller: ModelRegistryCaller{contract: contract}, ModelRegistryTransactor: ModelRegistryTransactor{contract: contract}, ModelRegistryFilterer: ModelRegistryFilterer{contract: contract}}, nil
}

// NewModelRegistryCaller creates a new read-only instance of ModelRegistry, bound to a specific deployed contract.
func NewModelRegistryCaller(address common.Address, caller bind.ContractCaller) (*ModelRegistryCaller, error) {
	contract, err := bindModelRegistry(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &ModelRegistryCaller{contract: contract}, nil
}

// NewModelRegistryTransactor creates a new write-only instance of ModelRegistry, bound to a specific deployed contract.
func NewModelRegistryTransactor(address common.Address, transactor bind.ContractTransactor) (*ModelRegistryTransactor, error) {
	contract, err := bindModelRegistry(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &ModelRegistryTransactor{contract: contract}, nil
}

// NewModelRegistryFilterer creates a new log filterer instance of ModelRegistry, bound to a specific deployed contract.
func NewModelRegistryFilterer(address common.Address, filterer bind.ContractFilterer) (*ModelRegistryFilterer, error) {
	contract, err := bindModelRegistry(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &ModelRegistryFilterer{contract: contract}, nil
}

// bindModelRegistry binds a generic wrapper to an already deployed contract.
func bindModelRegistry(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(ModelRegistryABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_ModelRegistry *ModelRegistryRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _ModelRegistry.Contract.ModelRegistryCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_ModelRegistry *ModelRegistryRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.ModelRegistryTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_ModelRegistry *ModelRegistryRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.ModelRegistryTransactor.contract.TransactWithResult(opts, result, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_ModelRegistry *ModelRegistryCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _ModelRegistry.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_ModelRegistry *ModelRegistryTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_ModelRegistry *ModelRegistryTransactorRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.contract.TransactWithResult(opts, result, method, params...)
}

// Admin is a free data retrieval call binding the contract method 0xf851a440.
//
// Solidity: function admin() constant returns(address)
func (_ModelRegistry *ModelRegistryCaller) Admin(opts *bind.CallOpts) (common.Address, error) {
	var (
		ret0 = new(common.Address)
	)
	out := ret0
	err := _ModelRegistry.contract.Call(opts, out, "admin")
	return *ret0, err
}

// Admin is a free data retrieval call binding the contract method 0xf851a440.
//
// Solidity: function admin() constant returns(address)
func (_ModelRegistry *ModelRegistrySession) Admin() (common.Address, error) {
	return _ModelRegistry.Contract.Admin(&_ModelRegistry.CallOpts)
}

// Admin is a free data retrieval call binding the contract method 0xf851a440.
//
// Solidity: function admin() constant returns(address)
func (_ModelRegistry *ModelRegistryCallerSession) Admin() (common.Address, error) {
	return _ModelRegistry.Contract.Admin(&_ModelRegistry.CallOpts)
}

// Evaluators is a free data retrieval call binding the contract method 0xe50d582c.
//
// Solidity: function evaluators(address ) constant returns(bool)
func (_ModelRegistry *ModelRegistryCaller) Evaluators(opts *bind.CallOpts, arg0 common.Address) (bool, error) {
	var (
		ret0 = new(bool)
	)
	out := ret0
	err := _ModelRegistry.contract.Call(opts, out, "evaluators", arg0)
	return *ret0, err
}

// Evaluators is a free data retrieval call binding the contract method 0xe50d582c.
//
// Solidity: function evaluators(address ) constant returns(bool)
func (_ModelRegistry *ModelRegistrySession) Evaluators(arg0 common.Address) (bool, error) {
	return _ModelRegistry.Contract.Evaluators(&_ModelRegistry.CallOpts, arg0)
}

// Evaluators is a free data retrieval call binding the contract method 0xe50d582c.
//
// Solidity: function evaluators(address ) constant returns(bool)
func (_ModelRegistry *ModelRegistryCallerSession) Evaluators(arg0 common.Address) (bool, error) {
	return _ModelRegistry.Contract.Evaluators(&_ModelRegistry.CallOpts, arg0)
}

// GetModel is a free data retrieval call binding the contract method 0xfab57404.
//
// Solidity: function getModel(string _modelId) constant returns(string modelHash, string reportHash, address owner, uint256 timestamp)
func (_ModelRegistry *ModelRegistryCaller) GetModel(opts *bind.CallOpts, _modelId string) (struct {
	ModelHash  string
	ReportHash string
	Owner      common.Address
	Timestamp  *big.Int
}, error) {
	ret := new(struct {
		ModelHash  string
		ReportHash string
		Owner      common.Address
		Timestamp  *big.Int
	})
	out := ret
	err := _ModelRegistry.contract.Call(opts, out, "getModel", _modelId)
	return *ret, err
}

// GetModel is a free data retrieval call binding the contract method 0xfab57404.
//
// Solidity: function getModel(string _modelId) constant returns(string modelHash, string reportHash, address owner, uint256 timestamp)
func (_ModelRegistry *ModelRegistrySession) GetModel(_modelId string) (struct {
	ModelHash  string
	ReportHash string
	Owner      common.Address
	Timestamp  *big.Int
}, error) {
	return _ModelRegistry.Contract.GetModel(&_ModelRegistry.CallOpts, _modelId)
}

// GetModel is a free data retrieval call binding the contract method 0xfab57404.
//
// Solidity: function getModel(string _modelId) constant returns(string modelHash, string reportHash, address owner, uint256 timestamp)
func (_ModelRegistry *ModelRegistryCallerSession) GetModel(_modelId string) (struct {
	ModelHash  string
	ReportHash string
	Owner      common.Address
	Timestamp  *big.Int
}, error) {
	return _ModelRegistry.Contract.GetModel(&_ModelRegistry.CallOpts, _modelId)
}

// GetProposal is a free data retrieval call binding the contract method 0x25d3a09d.
//
// Solidity: function getProposal(string _modelId) constant returns(string modelId, string modelHash, string reportHash, address owner, uint256 votes, bool isApproved)
func (_ModelRegistry *ModelRegistryCaller) GetProposal(opts *bind.CallOpts, _modelId string) (struct {
	ModelId    string
	ModelHash  string
	ReportHash string
	Owner      common.Address
	Votes      *big.Int
	IsApproved bool
}, error) {
	ret := new(struct {
		ModelId    string
		ModelHash  string
		ReportHash string
		Owner      common.Address
		Votes      *big.Int
		IsApproved bool
	})
	out := ret
	err := _ModelRegistry.contract.Call(opts, out, "getProposal", _modelId)
	return *ret, err
}

// GetProposal is a free data retrieval call binding the contract method 0x25d3a09d.
//
// Solidity: function getProposal(string _modelId) constant returns(string modelId, string modelHash, string reportHash, address owner, uint256 votes, bool isApproved)
func (_ModelRegistry *ModelRegistrySession) GetProposal(_modelId string) (struct {
	ModelId    string
	ModelHash  string
	ReportHash string
	Owner      common.Address
	Votes      *big.Int
	IsApproved bool
}, error) {
	return _ModelRegistry.Contract.GetProposal(&_ModelRegistry.CallOpts, _modelId)
}

// GetProposal is a free data retrieval call binding the contract method 0x25d3a09d.
//
// Solidity: function getProposal(string _modelId) constant returns(string modelId, string modelHash, string reportHash, address owner, uint256 votes, bool isApproved)
func (_ModelRegistry *ModelRegistryCallerSession) GetProposal(_modelId string) (struct {
	ModelId    string
	ModelHash  string
	ReportHash string
	Owner      common.Address
	Votes      *big.Int
	IsApproved bool
}, error) {
	return _ModelRegistry.Contract.GetProposal(&_ModelRegistry.CallOpts, _modelId)
}

// Threshold is a free data retrieval call binding the contract method 0x42cde4e8.
//
// Solidity: function threshold() constant returns(uint256)
func (_ModelRegistry *ModelRegistryCaller) Threshold(opts *bind.CallOpts) (*big.Int, error) {
	var (
		ret0 = new(*big.Int)
	)
	out := ret0
	err := _ModelRegistry.contract.Call(opts, out, "threshold")
	return *ret0, err
}

// Threshold is a free data retrieval call binding the contract method 0x42cde4e8.
//
// Solidity: function threshold() constant returns(uint256)
func (_ModelRegistry *ModelRegistrySession) Threshold() (*big.Int, error) {
	return _ModelRegistry.Contract.Threshold(&_ModelRegistry.CallOpts)
}

// Threshold is a free data retrieval call binding the contract method 0x42cde4e8.
//
// Solidity: function threshold() constant returns(uint256)
func (_ModelRegistry *ModelRegistryCallerSession) Threshold() (*big.Int, error) {
	return _ModelRegistry.Contract.Threshold(&_ModelRegistry.CallOpts)
}

// AddEvaluator is a paid mutator transaction binding the contract method 0xeb26e5f0.
//
// Solidity: function addEvaluator(address _evaluator) returns()
func (_ModelRegistry *ModelRegistryTransactor) AddEvaluator(opts *bind.TransactOpts, _evaluator common.Address) (*types.Transaction, *types.Receipt, error) {
	var ()
	out := &[]interface{}{}
	transaction, receipt, err := _ModelRegistry.contract.TransactWithResult(opts, out, "addEvaluator", _evaluator)
	return transaction, receipt, err
}

func (_ModelRegistry *ModelRegistryTransactor) AsyncAddEvaluator(handler func(*types.Receipt, error), opts *bind.TransactOpts, _evaluator common.Address) (*types.Transaction, error) {
	return _ModelRegistry.contract.AsyncTransact(opts, handler, "addEvaluator", _evaluator)
}

// AddEvaluator is a paid mutator transaction binding the contract method 0xeb26e5f0.
//
// Solidity: function addEvaluator(address _evaluator) returns()
func (_ModelRegistry *ModelRegistrySession) AddEvaluator(_evaluator common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.AddEvaluator(&_ModelRegistry.TransactOpts, _evaluator)
}

func (_ModelRegistry *ModelRegistrySession) AsyncAddEvaluator(handler func(*types.Receipt, error), _evaluator common.Address) (*types.Transaction, error) {
	return _ModelRegistry.Contract.AsyncAddEvaluator(handler, &_ModelRegistry.TransactOpts, _evaluator)
}

// AddEvaluator is a paid mutator transaction binding the contract method 0xeb26e5f0.
//
// Solidity: function addEvaluator(address _evaluator) returns()
func (_ModelRegistry *ModelRegistryTransactorSession) AddEvaluator(_evaluator common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.AddEvaluator(&_ModelRegistry.TransactOpts, _evaluator)
}

func (_ModelRegistry *ModelRegistryTransactorSession) AsyncAddEvaluator(handler func(*types.Receipt, error), _evaluator common.Address) (*types.Transaction, error) {
	return _ModelRegistry.Contract.AsyncAddEvaluator(handler, &_ModelRegistry.TransactOpts, _evaluator)
}

// CreateProposal is a paid mutator transaction binding the contract method 0xe84f10f2.
//
// Solidity: function createProposal(string _modelId, string _modelHash, string _reportHash, address _owner) returns()
func (_ModelRegistry *ModelRegistryTransactor) CreateProposal(opts *bind.TransactOpts, _modelId string, _modelHash string, _reportHash string, _owner common.Address) (*types.Transaction, *types.Receipt, error) {
	var ()
	out := &[]interface{}{}
	transaction, receipt, err := _ModelRegistry.contract.TransactWithResult(opts, out, "createProposal", _modelId, _modelHash, _reportHash, _owner)
	return transaction, receipt, err
}

func (_ModelRegistry *ModelRegistryTransactor) AsyncCreateProposal(handler func(*types.Receipt, error), opts *bind.TransactOpts, _modelId string, _modelHash string, _reportHash string, _owner common.Address) (*types.Transaction, error) {
	return _ModelRegistry.contract.AsyncTransact(opts, handler, "createProposal", _modelId, _modelHash, _reportHash, _owner)
}

// CreateProposal is a paid mutator transaction binding the contract method 0xe84f10f2.
//
// Solidity: function createProposal(string _modelId, string _modelHash, string _reportHash, address _owner) returns()
func (_ModelRegistry *ModelRegistrySession) CreateProposal(_modelId string, _modelHash string, _reportHash string, _owner common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.CreateProposal(&_ModelRegistry.TransactOpts, _modelId, _modelHash, _reportHash, _owner)
}

func (_ModelRegistry *ModelRegistrySession) AsyncCreateProposal(handler func(*types.Receipt, error), _modelId string, _modelHash string, _reportHash string, _owner common.Address) (*types.Transaction, error) {
	return _ModelRegistry.Contract.AsyncCreateProposal(handler, &_ModelRegistry.TransactOpts, _modelId, _modelHash, _reportHash, _owner)
}

// CreateProposal is a paid mutator transaction binding the contract method 0xe84f10f2.
//
// Solidity: function createProposal(string _modelId, string _modelHash, string _reportHash, address _owner) returns()
func (_ModelRegistry *ModelRegistryTransactorSession) CreateProposal(_modelId string, _modelHash string, _reportHash string, _owner common.Address) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.CreateProposal(&_ModelRegistry.TransactOpts, _modelId, _modelHash, _reportHash, _owner)
}

func (_ModelRegistry *ModelRegistryTransactorSession) AsyncCreateProposal(handler func(*types.Receipt, error), _modelId string, _modelHash string, _reportHash string, _owner common.Address) (*types.Transaction, error) {
	return _ModelRegistry.Contract.AsyncCreateProposal(handler, &_ModelRegistry.TransactOpts, _modelId, _modelHash, _reportHash, _owner)
}

// UpdateThreshold is a paid mutator transaction binding the contract method 0xd7d7442f.
//
// Solidity: function updateThreshold(uint256 _newThreshold) returns()
func (_ModelRegistry *ModelRegistryTransactor) UpdateThreshold(opts *bind.TransactOpts, _newThreshold *big.Int) (*types.Transaction, *types.Receipt, error) {
	var ()
	out := &[]interface{}{}
	transaction, receipt, err := _ModelRegistry.contract.TransactWithResult(opts, out, "updateThreshold", _newThreshold)
	return transaction, receipt, err
}

func (_ModelRegistry *ModelRegistryTransactor) AsyncUpdateThreshold(handler func(*types.Receipt, error), opts *bind.TransactOpts, _newThreshold *big.Int) (*types.Transaction, error) {
	return _ModelRegistry.contract.AsyncTransact(opts, handler, "updateThreshold", _newThreshold)
}

// UpdateThreshold is a paid mutator transaction binding the contract method 0xd7d7442f.
//
// Solidity: function updateThreshold(uint256 _newThreshold) returns()
func (_ModelRegistry *ModelRegistrySession) UpdateThreshold(_newThreshold *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.UpdateThreshold(&_ModelRegistry.TransactOpts, _newThreshold)
}

func (_ModelRegistry *ModelRegistrySession) AsyncUpdateThreshold(handler func(*types.Receipt, error), _newThreshold *big.Int) (*types.Transaction, error) {
	return _ModelRegistry.Contract.AsyncUpdateThreshold(handler, &_ModelRegistry.TransactOpts, _newThreshold)
}

// UpdateThreshold is a paid mutator transaction binding the contract method 0xd7d7442f.
//
// Solidity: function updateThreshold(uint256 _newThreshold) returns()
func (_ModelRegistry *ModelRegistryTransactorSession) UpdateThreshold(_newThreshold *big.Int) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.UpdateThreshold(&_ModelRegistry.TransactOpts, _newThreshold)
}

func (_ModelRegistry *ModelRegistryTransactorSession) AsyncUpdateThreshold(handler func(*types.Receipt, error), _newThreshold *big.Int) (*types.Transaction, error) {
	return _ModelRegistry.Contract.AsyncUpdateThreshold(handler, &_ModelRegistry.TransactOpts, _newThreshold)
}

// VoteForProposal is a paid mutator transaction binding the contract method 0x2ba78f99.
//
// Solidity: function voteForProposal(string _modelId) returns()
func (_ModelRegistry *ModelRegistryTransactor) VoteForProposal(opts *bind.TransactOpts, _modelId string) (*types.Transaction, *types.Receipt, error) {
	var ()
	out := &[]interface{}{}
	transaction, receipt, err := _ModelRegistry.contract.TransactWithResult(opts, out, "voteForProposal", _modelId)
	return transaction, receipt, err
}

func (_ModelRegistry *ModelRegistryTransactor) AsyncVoteForProposal(handler func(*types.Receipt, error), opts *bind.TransactOpts, _modelId string) (*types.Transaction, error) {
	return _ModelRegistry.contract.AsyncTransact(opts, handler, "voteForProposal", _modelId)
}

// VoteForProposal is a paid mutator transaction binding the contract method 0x2ba78f99.
//
// Solidity: function voteForProposal(string _modelId) returns()
func (_ModelRegistry *ModelRegistrySession) VoteForProposal(_modelId string) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.VoteForProposal(&_ModelRegistry.TransactOpts, _modelId)
}

func (_ModelRegistry *ModelRegistrySession) AsyncVoteForProposal(handler func(*types.Receipt, error), _modelId string) (*types.Transaction, error) {
	return _ModelRegistry.Contract.AsyncVoteForProposal(handler, &_ModelRegistry.TransactOpts, _modelId)
}

// VoteForProposal is a paid mutator transaction binding the contract method 0x2ba78f99.
//
// Solidity: function voteForProposal(string _modelId) returns()
func (_ModelRegistry *ModelRegistryTransactorSession) VoteForProposal(_modelId string) (*types.Transaction, *types.Receipt, error) {
	return _ModelRegistry.Contract.VoteForProposal(&_ModelRegistry.TransactOpts, _modelId)
}

func (_ModelRegistry *ModelRegistryTransactorSession) AsyncVoteForProposal(handler func(*types.Receipt, error), _modelId string) (*types.Transaction, error) {
	return _ModelRegistry.Contract.AsyncVoteForProposal(handler, &_ModelRegistry.TransactOpts, _modelId)
}

// ModelRegistryModelApproved represents a ModelApproved event raised by the ModelRegistry contract.
type ModelRegistryModelApproved struct {
	ModelId   string
	Timestamp *big.Int
	Raw       types.Log // Blockchain specific contextual infos
}

// WatchModelApproved is a free log subscription operation binding the contract event 0x4a91d635bdf5fe8e3e48c1011d1abe1eedbd70c2915b81f2dee0d5b10a9368d1.
//
// Solidity: event ModelApproved(string modelId, uint256 timestamp)
func (_ModelRegistry *ModelRegistryFilterer) WatchModelApproved(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.contract.WatchLogs(fromBlock, handler, "ModelApproved")
}

func (_ModelRegistry *ModelRegistryFilterer) WatchAllModelApproved(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.contract.WatchLogs(fromBlock, handler, "ModelApproved")
}

// ParseModelApproved is a log parse operation binding the contract event 0x4a91d635bdf5fe8e3e48c1011d1abe1eedbd70c2915b81f2dee0d5b10a9368d1.
//
// Solidity: event ModelApproved(string modelId, uint256 timestamp)
func (_ModelRegistry *ModelRegistryFilterer) ParseModelApproved(log types.Log) (*ModelRegistryModelApproved, error) {
	event := new(ModelRegistryModelApproved)
	if err := _ModelRegistry.contract.UnpackLog(event, "ModelApproved", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchModelApproved is a free log subscription operation binding the contract event 0x4a91d635bdf5fe8e3e48c1011d1abe1eedbd70c2915b81f2dee0d5b10a9368d1.
//
// Solidity: event ModelApproved(string modelId, uint256 timestamp)
func (_ModelRegistry *ModelRegistrySession) WatchModelApproved(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.Contract.WatchModelApproved(fromBlock, handler)
}

func (_ModelRegistry *ModelRegistrySession) WatchAllModelApproved(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.Contract.WatchAllModelApproved(fromBlock, handler)
}

// ParseModelApproved is a log parse operation binding the contract event 0x4a91d635bdf5fe8e3e48c1011d1abe1eedbd70c2915b81f2dee0d5b10a9368d1.
//
// Solidity: event ModelApproved(string modelId, uint256 timestamp)
func (_ModelRegistry *ModelRegistrySession) ParseModelApproved(log types.Log) (*ModelRegistryModelApproved, error) {
	return _ModelRegistry.Contract.ParseModelApproved(log)
}

// ModelRegistryProposalCreated represents a ProposalCreated event raised by the ModelRegistry contract.
type ModelRegistryProposalCreated struct {
	ModelId  string
	Proposer common.Address
	Raw      types.Log // Blockchain specific contextual infos
}

// WatchProposalCreated is a free log subscription operation binding the contract event 0x6b34733d94be17886338d1436b2c705b81d42edc9ae3b26f9e6634e4f33d8e2a.
//
// Solidity: event ProposalCreated(string modelId, address proposer)
func (_ModelRegistry *ModelRegistryFilterer) WatchProposalCreated(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.contract.WatchLogs(fromBlock, handler, "ProposalCreated")
}

func (_ModelRegistry *ModelRegistryFilterer) WatchAllProposalCreated(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.contract.WatchLogs(fromBlock, handler, "ProposalCreated")
}

// ParseProposalCreated is a log parse operation binding the contract event 0x6b34733d94be17886338d1436b2c705b81d42edc9ae3b26f9e6634e4f33d8e2a.
//
// Solidity: event ProposalCreated(string modelId, address proposer)
func (_ModelRegistry *ModelRegistryFilterer) ParseProposalCreated(log types.Log) (*ModelRegistryProposalCreated, error) {
	event := new(ModelRegistryProposalCreated)
	if err := _ModelRegistry.contract.UnpackLog(event, "ProposalCreated", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchProposalCreated is a free log subscription operation binding the contract event 0x6b34733d94be17886338d1436b2c705b81d42edc9ae3b26f9e6634e4f33d8e2a.
//
// Solidity: event ProposalCreated(string modelId, address proposer)
func (_ModelRegistry *ModelRegistrySession) WatchProposalCreated(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.Contract.WatchProposalCreated(fromBlock, handler)
}

func (_ModelRegistry *ModelRegistrySession) WatchAllProposalCreated(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.Contract.WatchAllProposalCreated(fromBlock, handler)
}

// ParseProposalCreated is a log parse operation binding the contract event 0x6b34733d94be17886338d1436b2c705b81d42edc9ae3b26f9e6634e4f33d8e2a.
//
// Solidity: event ProposalCreated(string modelId, address proposer)
func (_ModelRegistry *ModelRegistrySession) ParseProposalCreated(log types.Log) (*ModelRegistryProposalCreated, error) {
	return _ModelRegistry.Contract.ParseProposalCreated(log)
}

// ModelRegistryVoteCasted represents a VoteCasted event raised by the ModelRegistry contract.
type ModelRegistryVoteCasted struct {
	ModelId    string
	Voter      common.Address
	TotalVotes *big.Int
	Raw        types.Log // Blockchain specific contextual infos
}

// WatchVoteCasted is a free log subscription operation binding the contract event 0xffb039b229183b0cb867e5598d1788cc4130c39b74d1da506a000d910759d0c8.
//
// Solidity: event VoteCasted(string modelId, address voter, uint256 totalVotes)
func (_ModelRegistry *ModelRegistryFilterer) WatchVoteCasted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.contract.WatchLogs(fromBlock, handler, "VoteCasted")
}

func (_ModelRegistry *ModelRegistryFilterer) WatchAllVoteCasted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.contract.WatchLogs(fromBlock, handler, "VoteCasted")
}

// ParseVoteCasted is a log parse operation binding the contract event 0xffb039b229183b0cb867e5598d1788cc4130c39b74d1da506a000d910759d0c8.
//
// Solidity: event VoteCasted(string modelId, address voter, uint256 totalVotes)
func (_ModelRegistry *ModelRegistryFilterer) ParseVoteCasted(log types.Log) (*ModelRegistryVoteCasted, error) {
	event := new(ModelRegistryVoteCasted)
	if err := _ModelRegistry.contract.UnpackLog(event, "VoteCasted", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchVoteCasted is a free log subscription operation binding the contract event 0xffb039b229183b0cb867e5598d1788cc4130c39b74d1da506a000d910759d0c8.
//
// Solidity: event VoteCasted(string modelId, address voter, uint256 totalVotes)
func (_ModelRegistry *ModelRegistrySession) WatchVoteCasted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.Contract.WatchVoteCasted(fromBlock, handler)
}

func (_ModelRegistry *ModelRegistrySession) WatchAllVoteCasted(fromBlock *uint64, handler func(int, []types.Log)) (string, error) {
	return _ModelRegistry.Contract.WatchAllVoteCasted(fromBlock, handler)
}

// ParseVoteCasted is a log parse operation binding the contract event 0xffb039b229183b0cb867e5598d1788cc4130c39b74d1da506a000d910759d0c8.
//
// Solidity: event VoteCasted(string modelId, address voter, uint256 totalVotes)
func (_ModelRegistry *ModelRegistrySession) ParseVoteCasted(log types.Log) (*ModelRegistryVoteCasted, error) {
	return _ModelRegistry.Contract.ParseVoteCasted(log)
}
