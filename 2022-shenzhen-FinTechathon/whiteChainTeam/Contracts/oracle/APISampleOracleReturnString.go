// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package oracle

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

// OracleABI is the input ABI used to generate the binding from.
const OracleABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"_url\",\"type\":\"string\"}],\"name\":\"setUrl\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"returnType\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getById\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"info\",\"outputs\":[{\"name\":\"province\",\"type\":\"string\"},{\"name\":\"city\",\"type\":\"string\"},{\"name\":\"weather\",\"type\":\"string\"},{\"name\":\"temperature\",\"type\":\"string\"},{\"name\":\"winddirection\",\"type\":\"string\"},{\"name\":\"windpower\",\"type\":\"string\"},{\"name\":\"humidity\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"request\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"EXPIRY_TIME\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"result\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"requestId\",\"type\":\"bytes32\"},{\"name\":\"result\",\"type\":\"bytes\"},{\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"callback\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"province\",\"type\":\"string\"},{\"name\":\"city\",\"type\":\"string\"},{\"name\":\"weather\",\"type\":\"string\"},{\"name\":\"temperature\",\"type\":\"string\"},{\"name\":\"winddirection\",\"type\":\"string\"},{\"name\":\"windpower\",\"type\":\"string\"},{\"name\":\"humidity\",\"type\":\"string\"}],\"name\":\"push\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getUrl\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"oracleAddress\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Requested\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Fulfilled\",\"type\":\"event\"}]"

// OracleBin is the compiled bytecode used for deploying new contracts.
var OracleBin = "0x6080604052600180556000600460006101000a81548160ff021916908360028111156200002857fe5b02179055506000600655606060405190810160405280603e81526020017f6a736f6e2868747470733a2f2f6170692e65786368616e6765726174652d617081526020017f692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a50590000815250600b9080519060200190620000a692919062000120565b50348015620000b457600080fd5b5060405160208062001fb18339810180604052810190808051906020019092919050505080600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050620001cf565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200016357805160ff191683800117855562000194565b8280016001018555821562000194579182015b828111156200019357825182559160200191906001019062000176565b5b509050620001a39190620001a7565b5090565b620001cc91905b80821115620001c8576000816000905550600101620001ae565b5090565b90565b611dd280620001df6000396000f3006080604052600436106100ba576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063252498a2146100bf5780632d1a193d146101285780632dff0d0d146101615780632e3405991461020b578063338cdca1146105395780634b6022821461056c57806365372147146105975780636d4ce63c146106275780638ab4be9e146106b7578063ac04e92614610774578063c75f695a14610981578063d6bd8727146109ca575b600080fd5b3480156100cb57600080fd5b50610126600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610a5a565b005b34801561013457600080fd5b5061013d610a74565b6040518082600281111561014d57fe5b60ff16815260200191505060405180910390f35b34801561016d57600080fd5b506101906004803603810190808035600019169060200190929190505050610a87565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101d05780820151818401526020810190506101b5565b50505050905090810190601f1680156101fd5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561021757600080fd5b5061023660048036038101908080359060200190929190505050610b44565b604051808060200180602001806020018060200180602001806020018060200188810388528f818151815260200191508051906020019080838360005b8381101561028e578082015181840152602081019050610273565b50505050905090810190601f1680156102bb5780820380516001836020036101000a031916815260200191505b5088810387528e818151815260200191508051906020019080838360005b838110156102f45780820151818401526020810190506102d9565b50505050905090810190601f1680156103215780820380516001836020036101000a031916815260200191505b5088810386528d818151815260200191508051906020019080838360005b8381101561035a57808201518184015260208101905061033f565b50505050905090810190601f1680156103875780820380516001836020036101000a031916815260200191505b5088810385528c818151815260200191508051906020019080838360005b838110156103c05780820151818401526020810190506103a5565b50505050905090810190601f1680156103ed5780820380516001836020036101000a031916815260200191505b5088810384528b818151815260200191508051906020019080838360005b8381101561042657808201518184015260208101905061040b565b50505050905090810190601f1680156104535780820380516001836020036101000a031916815260200191505b5088810383528a818151815260200191508051906020019080838360005b8381101561048c578082015181840152602081019050610471565b50505050905090810190601f1680156104b95780820380516001836020036101000a031916815260200191505b50888103825289818151815260200191508051906020019080838360005b838110156104f25780820151818401526020810190506104d7565b50505050905090810190601f16801561051f5780820380516001836020036101000a031916815260200191505b509e50505050505050505050505050505060405180910390f35b34801561054557600080fd5b5061054e610fbd565b60405180826000191660001916815260200191505060405180910390f35b34801561057857600080fd5b506105816110f9565b6040518082815260200191505060405180910390f35b3480156105a357600080fd5b506105ac611100565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105ec5780820151818401526020810190506105d1565b50505050905090810190601f1680156106195780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561063357600080fd5b5061063c61119e565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561067c578082015181840152602081019050610661565b50505050905090810190601f1680156106a95780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156106c357600080fd5b506107726004803603810190808035600019169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050611240565b005b34801561078057600080fd5b5061097f600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506113c4565b005b34801561098d57600080fd5b506109b0600480360381019080803560001916906020019092919050505061150c565b604051808215151515815260200191505060405180910390f35b3480156109d657600080fd5b506109df61153e565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610a1f578082015181840152602081019050610a04565b50505050905090810190601f168015610a4c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b80600b9080519060200190610a70929190611c43565b5050565b600460009054906101000a900460ff1681565b60606008600083600019166000191681526020019081526020016000208054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b385780601f10610b0d57610100808354040283529160200191610b38565b820191906000526020600020905b815481529060010190602001808311610b1b57829003601f168201915b50505050509050919050565b600581815481101515610b5357fe5b9060005260206000209060070201600091509050806000018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610bff5780601f10610bd457610100808354040283529160200191610bff565b820191906000526020600020905b815481529060010190602001808311610be257829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c9d5780601f10610c7257610100808354040283529160200191610c9d565b820191906000526020600020905b815481529060010190602001808311610c8057829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610d3b5780601f10610d1057610100808354040283529160200191610d3b565b820191906000526020600020905b815481529060010190602001808311610d1e57829003601f168201915b505050505090806003018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610dd95780601f10610dae57610100808354040283529160200191610dd9565b820191906000526020600020905b815481529060010190602001808311610dbc57829003601f168201915b505050505090806004018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610e775780601f10610e4c57610100808354040283529160200191610e77565b820191906000526020600020905b815481529060010190602001808311610e5a57829003601f168201915b505050505090806005018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610f155780601f10610eea57610100808354040283529160200191610f15565b820191906000526020600020905b815481529060010190602001808311610ef857829003601f168201915b505050505090806006018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610fb35780601f10610f8857610100808354040283529160200191610fb3565b820191906000526020600020905b815481529060010190602001808311610f9657829003601f168201915b5050505050905087565b6000806001600460006101000a81548160ff02191690836002811115610fdf57fe5b02179055506110bc600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600b8054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156110a05780601f10611075576101008083540402835291602001916110a0565b820191906000526020600020905b81548152906001019060200180831161108357829003601f168201915b50505050506000600460009054906101000a900460ff166115e0565b9050600160096000836000191660001916815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b620927c081565b600a8054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156111965780601f1061116b57610100808354040283529160200191611196565b820191906000526020600020905b81548152906001019060200180831161117957829003601f168201915b505050505081565b6060600a8054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156112365780601f1061120b57610100808354040283529160200191611236565b820191906000526020600020905b81548152906001019060200180831161121957829003601f168201915b5050505050905090565b8260026000826000191660001916815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611345576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001807f536f75726365206d75737420626520746865206f7261636c65206f662074686581526020017f207265717565737400000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b60026000826000191660001916815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905580600019167f6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e2352698460405160405180910390a26113be8484611634565b50505050565b6113cc611cc3565b60e06040519081016040528089815260200188815260200187815260200186815260200185815260200184815260200183815250905080600560065481548110151561141457fe5b9060005260206000209060070201600082015181600001908051906020019061143e929190611d01565b50602082015181600101908051906020019061145b929190611d01565b506040820151816002019080519060200190611478929190611d01565b506060820151816003019080519060200190611495929190611d01565b5060808201518160040190805190602001906114b2929190611d01565b5060a08201518160050190805190602001906114cf929190611d01565b5060c08201518160060190805190602001906114ec929190611d01565b509050506006600081548092919060010191905055505050505050505050565b600060096000836000191660001916815260200190815260200160002060009054906101000a900460ff169050919050565b6060600b8054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156115d65780601f106115ab576101008083540402835291602001916115d6565b820191906000526020600020905b8154815290600101906020018083116115b957829003601f168201915b5050505050905090565b600061162a620927c06040805190810160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250878787600088611747565b9050949350505050565b60096000836000191660001916815260200190815260200160002060009054906101000a900460ff1615156116d1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b806008600084600019166000191681526020019081526020016000209080519060200190611700929190611c43565b5060096000836000191660001916815260200190815260200160002060006101000a81549060ff021916905580600a9080519060200190611742929190611c43565b505050565b6000806000876000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016040805180830381600087803b15801561181057600080fd5b505af1158015611824573d6000803e3d6000fd5b505050506040513d604081101561183a57600080fd5b8101908080519060200190929190805190602001909291905050508092508193505050818130600154604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c010000000000000000000000000281526014018281526020019450505050506040516020818303038152906040526040518082805190602001908083835b60208310151561190757805182526020820191506020810190506020830392506118e2565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902092508760026000856000191660001916815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600019167f9dc80e952f32e8aef7828795a4cc11bbe605c2ad3cc626df8115be4f78338f6960405160405180910390a26000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637b5a2033306001548a8a8f8b8b6002811115611a1257fe5b6040518863ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015611ad1578082015181840152602081019050611ab6565b50505050905090810190601f168015611afe5780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015611b2357600080fd5b505af1158015611b37573d6000803e3d6000fd5b505050506040513d6020811015611b4d57600080fd5b81019080805190602001909291905050501515611bd2576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600081548092919060010191905055508292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611c8457805160ff1916838001178555611cb2565b82800160010185558215611cb2579182015b82811115611cb1578251825591602001919060010190611c96565b5b509050611cbf9190611d81565b5090565b60e060405190810160405280606081526020016060815260200160608152602001606081526020016060815260200160608152602001606081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611d4257805160ff1916838001178555611d70565b82800160010185558215611d70579182015b82811115611d6f578251825591602001919060010190611d54565b5b509050611d7d9190611d81565b5090565b611da391905b80821115611d9f576000816000905550600101611d87565b5090565b905600a165627a7a7230582055a199435561cccb0fc36edb51ea6733837c5911448682c89a89bf97ccae49c00029"

// DeployOracle deploys a new contract, binding an instance of Oracle to it.
func DeployOracle(auth *bind.TransactOpts, backend bind.ContractBackend, oracleAddress common.Address) (common.Address, *types.Transaction, *Oracle, error) {
	parsed, err := abi.JSON(strings.NewReader(OracleABI))
	if err != nil {
		return common.Address{}, nil, nil, err
	}

	address, tx, contract, err := bind.DeployContract(auth, parsed, common.FromHex(OracleBin), backend, oracleAddress)
	if err != nil {
		return common.Address{}, nil, nil, err
	}
	return address, tx, &Oracle{OracleCaller: OracleCaller{contract: contract}, OracleTransactor: OracleTransactor{contract: contract}, OracleFilterer: OracleFilterer{contract: contract}}, nil
}

func AsyncDeployOracle(auth *bind.TransactOpts, handler func(*types.Receipt, error), backend bind.ContractBackend, oracleAddress common.Address) (*types.Transaction, error) {
	parsed, err := abi.JSON(strings.NewReader(OracleABI))
	if err != nil {
		return nil, err
	}

	tx, err := bind.AsyncDeployContract(auth, handler, parsed, common.FromHex(OracleBin), backend, oracleAddress)
	if err != nil {
		return nil, err
	}
	return tx, nil
}

// Oracle is an auto generated Go binding around a Solidity contract.
type Oracle struct {
	OracleCaller     // Read-only binding to the contract
	OracleTransactor // Write-only binding to the contract
	OracleFilterer   // Log filterer for contract events
}

// OracleCaller is an auto generated read-only Go binding around a Solidity contract.
type OracleCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// OracleTransactor is an auto generated write-only Go binding around a Solidity contract.
type OracleTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// OracleFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type OracleFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// OracleSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type OracleSession struct {
	Contract     *Oracle           // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// OracleCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type OracleCallerSession struct {
	Contract *OracleCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts // Call options to use throughout this session
}

// OracleTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type OracleTransactorSession struct {
	Contract     *OracleTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// OracleRaw is an auto generated low-level Go binding around a Solidity contract.
type OracleRaw struct {
	Contract *Oracle // Generic contract binding to access the raw methods on
}

// OracleCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type OracleCallerRaw struct {
	Contract *OracleCaller // Generic read-only contract binding to access the raw methods on
}

// OracleTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type OracleTransactorRaw struct {
	Contract *OracleTransactor // Generic write-only contract binding to access the raw methods on
}

// NewOracle creates a new instance of Oracle, bound to a specific deployed contract.
func NewOracle(address common.Address, backend bind.ContractBackend) (*Oracle, error) {
	contract, err := bindOracle(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &Oracle{OracleCaller: OracleCaller{contract: contract}, OracleTransactor: OracleTransactor{contract: contract}, OracleFilterer: OracleFilterer{contract: contract}}, nil
}

// NewOracleCaller creates a new read-only instance of Oracle, bound to a specific deployed contract.
func NewOracleCaller(address common.Address, caller bind.ContractCaller) (*OracleCaller, error) {
	contract, err := bindOracle(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &OracleCaller{contract: contract}, nil
}

// NewOracleTransactor creates a new write-only instance of Oracle, bound to a specific deployed contract.
func NewOracleTransactor(address common.Address, transactor bind.ContractTransactor) (*OracleTransactor, error) {
	contract, err := bindOracle(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &OracleTransactor{contract: contract}, nil
}

// NewOracleFilterer creates a new log filterer instance of Oracle, bound to a specific deployed contract.
func NewOracleFilterer(address common.Address, filterer bind.ContractFilterer) (*OracleFilterer, error) {
	contract, err := bindOracle(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &OracleFilterer{contract: contract}, nil
}

// bindOracle binds a generic wrapper to an already deployed contract.
func bindOracle(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(OracleABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Oracle *OracleRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Oracle.Contract.OracleCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Oracle *OracleRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.OracleTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Oracle *OracleRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.OracleTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Oracle *OracleCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Oracle.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Oracle *OracleTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Oracle *OracleTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.contract.Transact(opts, method, params...)
}

// EXPIRYTIME is a free data retrieval call binding the contract method 0x4b602282.
//
// Solidity: function EXPIRY_TIME() constant returns(uint256)
func (_Oracle *OracleCaller) EXPIRYTIME(opts *bind.CallOpts) (*big.Int, error) {
	var (
		ret0 = new(*big.Int)
	)
	out := ret0
	err := _Oracle.contract.Call(opts, out, "EXPIRY_TIME")
	return *ret0, err
}

// EXPIRYTIME is a free data retrieval call binding the contract method 0x4b602282.
//
// Solidity: function EXPIRY_TIME() constant returns(uint256)
func (_Oracle *OracleSession) EXPIRYTIME() (*big.Int, error) {
	return _Oracle.Contract.EXPIRYTIME(&_Oracle.CallOpts)
}

// EXPIRYTIME is a free data retrieval call binding the contract method 0x4b602282.
//
// Solidity: function EXPIRY_TIME() constant returns(uint256)
func (_Oracle *OracleCallerSession) EXPIRYTIME() (*big.Int, error) {
	return _Oracle.Contract.EXPIRYTIME(&_Oracle.CallOpts)
}

// CheckIdFulfilled is a free data retrieval call binding the contract method 0xc75f695a.
//
// Solidity: function checkIdFulfilled(bytes32 id) constant returns(bool)
func (_Oracle *OracleCaller) CheckIdFulfilled(opts *bind.CallOpts, id [32]byte) (bool, error) {
	var (
		ret0 = new(bool)
	)
	out := ret0
	err := _Oracle.contract.Call(opts, out, "checkIdFulfilled", id)
	return *ret0, err
}

// CheckIdFulfilled is a free data retrieval call binding the contract method 0xc75f695a.
//
// Solidity: function checkIdFulfilled(bytes32 id) constant returns(bool)
func (_Oracle *OracleSession) CheckIdFulfilled(id [32]byte) (bool, error) {
	return _Oracle.Contract.CheckIdFulfilled(&_Oracle.CallOpts, id)
}

// CheckIdFulfilled is a free data retrieval call binding the contract method 0xc75f695a.
//
// Solidity: function checkIdFulfilled(bytes32 id) constant returns(bool)
func (_Oracle *OracleCallerSession) CheckIdFulfilled(id [32]byte) (bool, error) {
	return _Oracle.Contract.CheckIdFulfilled(&_Oracle.CallOpts, id)
}

// Get is a free data retrieval call binding the contract method 0x6d4ce63c.
//
// Solidity: function get() constant returns(string)
func (_Oracle *OracleCaller) Get(opts *bind.CallOpts) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Oracle.contract.Call(opts, out, "get")
	return *ret0, err
}

// Get is a free data retrieval call binding the contract method 0x6d4ce63c.
//
// Solidity: function get() constant returns(string)
func (_Oracle *OracleSession) Get() (string, error) {
	return _Oracle.Contract.Get(&_Oracle.CallOpts)
}

// Get is a free data retrieval call binding the contract method 0x6d4ce63c.
//
// Solidity: function get() constant returns(string)
func (_Oracle *OracleCallerSession) Get() (string, error) {
	return _Oracle.Contract.Get(&_Oracle.CallOpts)
}

// GetById is a free data retrieval call binding the contract method 0x2dff0d0d.
//
// Solidity: function getById(bytes32 id) constant returns(string)
func (_Oracle *OracleCaller) GetById(opts *bind.CallOpts, id [32]byte) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Oracle.contract.Call(opts, out, "getById", id)
	return *ret0, err
}

// GetById is a free data retrieval call binding the contract method 0x2dff0d0d.
//
// Solidity: function getById(bytes32 id) constant returns(string)
func (_Oracle *OracleSession) GetById(id [32]byte) (string, error) {
	return _Oracle.Contract.GetById(&_Oracle.CallOpts, id)
}

// GetById is a free data retrieval call binding the contract method 0x2dff0d0d.
//
// Solidity: function getById(bytes32 id) constant returns(string)
func (_Oracle *OracleCallerSession) GetById(id [32]byte) (string, error) {
	return _Oracle.Contract.GetById(&_Oracle.CallOpts, id)
}

// GetUrl is a free data retrieval call binding the contract method 0xd6bd8727.
//
// Solidity: function getUrl() constant returns(string)
func (_Oracle *OracleCaller) GetUrl(opts *bind.CallOpts) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Oracle.contract.Call(opts, out, "getUrl")
	return *ret0, err
}

// GetUrl is a free data retrieval call binding the contract method 0xd6bd8727.
//
// Solidity: function getUrl() constant returns(string)
func (_Oracle *OracleSession) GetUrl() (string, error) {
	return _Oracle.Contract.GetUrl(&_Oracle.CallOpts)
}

// GetUrl is a free data retrieval call binding the contract method 0xd6bd8727.
//
// Solidity: function getUrl() constant returns(string)
func (_Oracle *OracleCallerSession) GetUrl() (string, error) {
	return _Oracle.Contract.GetUrl(&_Oracle.CallOpts)
}

// Info is a free data retrieval call binding the contract method 0x2e340599.
//
// Solidity: function info(uint256 ) constant returns(string province, string city, string weather, string temperature, string winddirection, string windpower, string humidity)
func (_Oracle *OracleCaller) Info(opts *bind.CallOpts, arg0 *big.Int) (struct {
	Province      string
	City          string
	Weather       string
	Temperature   string
	Winddirection string
	Windpower     string
	Humidity      string
}, error) {
	ret := new(struct {
		Province      string
		City          string
		Weather       string
		Temperature   string
		Winddirection string
		Windpower     string
		Humidity      string
	})
	out := ret
	err := _Oracle.contract.Call(opts, out, "info", arg0)
	return *ret, err
}

// Info is a free data retrieval call binding the contract method 0x2e340599.
//
// Solidity: function info(uint256 ) constant returns(string province, string city, string weather, string temperature, string winddirection, string windpower, string humidity)
func (_Oracle *OracleSession) Info(arg0 *big.Int) (struct {
	Province      string
	City          string
	Weather       string
	Temperature   string
	Winddirection string
	Windpower     string
	Humidity      string
}, error) {
	return _Oracle.Contract.Info(&_Oracle.CallOpts, arg0)
}

// Info is a free data retrieval call binding the contract method 0x2e340599.
//
// Solidity: function info(uint256 ) constant returns(string province, string city, string weather, string temperature, string winddirection, string windpower, string humidity)
func (_Oracle *OracleCallerSession) Info(arg0 *big.Int) (struct {
	Province      string
	City          string
	Weather       string
	Temperature   string
	Winddirection string
	Windpower     string
	Humidity      string
}, error) {
	return _Oracle.Contract.Info(&_Oracle.CallOpts, arg0)
}

// Result is a free data retrieval call binding the contract method 0x65372147.
//
// Solidity: function result() constant returns(string)
func (_Oracle *OracleCaller) Result(opts *bind.CallOpts) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Oracle.contract.Call(opts, out, "result")
	return *ret0, err
}

// Result is a free data retrieval call binding the contract method 0x65372147.
//
// Solidity: function result() constant returns(string)
func (_Oracle *OracleSession) Result() (string, error) {
	return _Oracle.Contract.Result(&_Oracle.CallOpts)
}

// Result is a free data retrieval call binding the contract method 0x65372147.
//
// Solidity: function result() constant returns(string)
func (_Oracle *OracleCallerSession) Result() (string, error) {
	return _Oracle.Contract.Result(&_Oracle.CallOpts)
}

// ReturnType is a free data retrieval call binding the contract method 0x2d1a193d.
//
// Solidity: function returnType() constant returns(uint8)
func (_Oracle *OracleCaller) ReturnType(opts *bind.CallOpts) (uint8, error) {
	var (
		ret0 = new(uint8)
	)
	out := ret0
	err := _Oracle.contract.Call(opts, out, "returnType")
	return *ret0, err
}

// ReturnType is a free data retrieval call binding the contract method 0x2d1a193d.
//
// Solidity: function returnType() constant returns(uint8)
func (_Oracle *OracleSession) ReturnType() (uint8, error) {
	return _Oracle.Contract.ReturnType(&_Oracle.CallOpts)
}

// ReturnType is a free data retrieval call binding the contract method 0x2d1a193d.
//
// Solidity: function returnType() constant returns(uint8)
func (_Oracle *OracleCallerSession) ReturnType() (uint8, error) {
	return _Oracle.Contract.ReturnType(&_Oracle.CallOpts)
}

// Callback is a paid mutator transaction binding the contract method 0x8ab4be9e.
//
// Solidity: function callback(bytes32 requestId, bytes result, bytes proof) returns()
func (_Oracle *OracleTransactor) Callback(opts *bind.TransactOpts, requestId [32]byte, result []byte, proof []byte) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.contract.Transact(opts, "callback", requestId, result, proof)
}

func (_Oracle *OracleTransactor) AsyncCallback(handler func(*types.Receipt, error), opts *bind.TransactOpts, requestId [32]byte, result []byte, proof []byte) (*types.Transaction, error) {
	return _Oracle.contract.AsyncTransact(opts, handler, "callback", requestId, result, proof)
}

// Callback is a paid mutator transaction binding the contract method 0x8ab4be9e.
//
// Solidity: function callback(bytes32 requestId, bytes result, bytes proof) returns()
func (_Oracle *OracleSession) Callback(requestId [32]byte, result []byte, proof []byte) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.Callback(&_Oracle.TransactOpts, requestId, result, proof)
}

func (_Oracle *OracleSession) AsyncCallback(handler func(*types.Receipt, error), requestId [32]byte, result []byte, proof []byte) (*types.Transaction, error) {
	return _Oracle.Contract.AsyncCallback(handler, &_Oracle.TransactOpts, requestId, result, proof)
}

// Callback is a paid mutator transaction binding the contract method 0x8ab4be9e.
//
// Solidity: function callback(bytes32 requestId, bytes result, bytes proof) returns()
func (_Oracle *OracleTransactorSession) Callback(requestId [32]byte, result []byte, proof []byte) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.Callback(&_Oracle.TransactOpts, requestId, result, proof)
}

func (_Oracle *OracleTransactorSession) AsyncCallback(handler func(*types.Receipt, error), requestId [32]byte, result []byte, proof []byte) (*types.Transaction, error) {
	return _Oracle.Contract.AsyncCallback(handler, &_Oracle.TransactOpts, requestId, result, proof)
}

// Push is a paid mutator transaction binding the contract method 0xac04e926.
//
// Solidity: function push(string province, string city, string weather, string temperature, string winddirection, string windpower, string humidity) returns()
func (_Oracle *OracleTransactor) Push(opts *bind.TransactOpts, province string, city string, weather string, temperature string, winddirection string, windpower string, humidity string) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.contract.Transact(opts, "push", province, city, weather, temperature, winddirection, windpower, humidity)
}

func (_Oracle *OracleTransactor) AsyncPush(handler func(*types.Receipt, error), opts *bind.TransactOpts, province string, city string, weather string, temperature string, winddirection string, windpower string, humidity string) (*types.Transaction, error) {
	return _Oracle.contract.AsyncTransact(opts, handler, "push", province, city, weather, temperature, winddirection, windpower, humidity)
}

// Push is a paid mutator transaction binding the contract method 0xac04e926.
//
// Solidity: function push(string province, string city, string weather, string temperature, string winddirection, string windpower, string humidity) returns()
func (_Oracle *OracleSession) Push(province string, city string, weather string, temperature string, winddirection string, windpower string, humidity string) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.Push(&_Oracle.TransactOpts, province, city, weather, temperature, winddirection, windpower, humidity)
}

func (_Oracle *OracleSession) AsyncPush(handler func(*types.Receipt, error), province string, city string, weather string, temperature string, winddirection string, windpower string, humidity string) (*types.Transaction, error) {
	return _Oracle.Contract.AsyncPush(handler, &_Oracle.TransactOpts, province, city, weather, temperature, winddirection, windpower, humidity)
}

// Push is a paid mutator transaction binding the contract method 0xac04e926.
//
// Solidity: function push(string province, string city, string weather, string temperature, string winddirection, string windpower, string humidity) returns()
func (_Oracle *OracleTransactorSession) Push(province string, city string, weather string, temperature string, winddirection string, windpower string, humidity string) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.Push(&_Oracle.TransactOpts, province, city, weather, temperature, winddirection, windpower, humidity)
}

func (_Oracle *OracleTransactorSession) AsyncPush(handler func(*types.Receipt, error), province string, city string, weather string, temperature string, winddirection string, windpower string, humidity string) (*types.Transaction, error) {
	return _Oracle.Contract.AsyncPush(handler, &_Oracle.TransactOpts, province, city, weather, temperature, winddirection, windpower, humidity)
}

// Request is a paid mutator transaction binding the contract method 0x338cdca1.
//
// Solidity: function request() returns(bytes32)
func (_Oracle *OracleTransactor) Request(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.contract.Transact(opts, "request")
}

func (_Oracle *OracleTransactor) AsyncRequest(handler func(*types.Receipt, error), opts *bind.TransactOpts) (*types.Transaction, error) {
	return _Oracle.contract.AsyncTransact(opts, handler, "request")
}

// Request is a paid mutator transaction binding the contract method 0x338cdca1.
//
// Solidity: function request() returns(bytes32)
func (_Oracle *OracleSession) Request() (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.Request(&_Oracle.TransactOpts)
}

func (_Oracle *OracleSession) AsyncRequest(handler func(*types.Receipt, error)) (*types.Transaction, error) {
	return _Oracle.Contract.AsyncRequest(handler, &_Oracle.TransactOpts)
}

// Request is a paid mutator transaction binding the contract method 0x338cdca1.
//
// Solidity: function request() returns(bytes32)
func (_Oracle *OracleTransactorSession) Request() (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.Request(&_Oracle.TransactOpts)
}

func (_Oracle *OracleTransactorSession) AsyncRequest(handler func(*types.Receipt, error)) (*types.Transaction, error) {
	return _Oracle.Contract.AsyncRequest(handler, &_Oracle.TransactOpts)
}

// SetUrl is a paid mutator transaction binding the contract method 0x252498a2.
//
// Solidity: function setUrl(string _url) returns()
func (_Oracle *OracleTransactor) SetUrl(opts *bind.TransactOpts, _url string) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.contract.Transact(opts, "setUrl", _url)
}

func (_Oracle *OracleTransactor) AsyncSetUrl(handler func(*types.Receipt, error), opts *bind.TransactOpts, _url string) (*types.Transaction, error) {
	return _Oracle.contract.AsyncTransact(opts, handler, "setUrl", _url)
}

// SetUrl is a paid mutator transaction binding the contract method 0x252498a2.
//
// Solidity: function setUrl(string _url) returns()
func (_Oracle *OracleSession) SetUrl(_url string) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.SetUrl(&_Oracle.TransactOpts, _url)
}

func (_Oracle *OracleSession) AsyncSetUrl(handler func(*types.Receipt, error), _url string) (*types.Transaction, error) {
	return _Oracle.Contract.AsyncSetUrl(handler, &_Oracle.TransactOpts, _url)
}

// SetUrl is a paid mutator transaction binding the contract method 0x252498a2.
//
// Solidity: function setUrl(string _url) returns()
func (_Oracle *OracleTransactorSession) SetUrl(_url string) (*types.Transaction, *types.Receipt, error) {
	return _Oracle.Contract.SetUrl(&_Oracle.TransactOpts, _url)
}

func (_Oracle *OracleTransactorSession) AsyncSetUrl(handler func(*types.Receipt, error), _url string) (*types.Transaction, error) {
	return _Oracle.Contract.AsyncSetUrl(handler, &_Oracle.TransactOpts, _url)
}

// OracleFulfilled represents a Fulfilled event raised by the Oracle contract.
type OracleFulfilled struct {
	Id  [32]byte
	Raw types.Log // Blockchain specific contextual infos
}

// WatchFulfilled is a free log subscription operation binding the contract event 0x6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e23526984.
//
// Solidity: event Fulfilled(bytes32 indexed id)
func (_Oracle *OracleFilterer) WatchFulfilled(fromBlock *uint64, handler func(int, []types.Log), id [32]byte) error {
	return _Oracle.contract.WatchLogs(fromBlock, handler, "Fulfilled", id)
}

func (_Oracle *OracleFilterer) WatchAllFulfilled(fromBlock *uint64, handler func(int, []types.Log)) error {
	return _Oracle.contract.WatchLogs(fromBlock, handler, "Fulfilled")
}

// ParseFulfilled is a log parse operation binding the contract event 0x6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e23526984.
//
// Solidity: event Fulfilled(bytes32 indexed id)
func (_Oracle *OracleFilterer) ParseFulfilled(log types.Log) (*OracleFulfilled, error) {
	event := new(OracleFulfilled)
	if err := _Oracle.contract.UnpackLog(event, "Fulfilled", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchFulfilled is a free log subscription operation binding the contract event 0x6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e23526984.
//
// Solidity: event Fulfilled(bytes32 indexed id)
func (_Oracle *OracleSession) WatchFulfilled(fromBlock *uint64, handler func(int, []types.Log), id [32]byte) error {
	return _Oracle.Contract.WatchFulfilled(fromBlock, handler, id)
}

func (_Oracle *OracleSession) WatchAllFulfilled(fromBlock *uint64, handler func(int, []types.Log)) error {
	return _Oracle.Contract.WatchAllFulfilled(fromBlock, handler)
}

// ParseFulfilled is a log parse operation binding the contract event 0x6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e23526984.
//
// Solidity: event Fulfilled(bytes32 indexed id)
func (_Oracle *OracleSession) ParseFulfilled(log types.Log) (*OracleFulfilled, error) {
	return _Oracle.Contract.ParseFulfilled(log)
}

// OracleRequested represents a Requested event raised by the Oracle contract.
type OracleRequested struct {
	Id  [32]byte
	Raw types.Log // Blockchain specific contextual infos
}

// WatchRequested is a free log subscription operation binding the contract event 0x9dc80e952f32e8aef7828795a4cc11bbe605c2ad3cc626df8115be4f78338f69.
//
// Solidity: event Requested(bytes32 indexed id)
func (_Oracle *OracleFilterer) WatchRequested(fromBlock *uint64, handler func(int, []types.Log), id [32]byte) error {
	return _Oracle.contract.WatchLogs(fromBlock, handler, "Requested", id)
}

func (_Oracle *OracleFilterer) WatchAllRequested(fromBlock *uint64, handler func(int, []types.Log)) error {
	return _Oracle.contract.WatchLogs(fromBlock, handler, "Requested")
}

// ParseRequested is a log parse operation binding the contract event 0x9dc80e952f32e8aef7828795a4cc11bbe605c2ad3cc626df8115be4f78338f69.
//
// Solidity: event Requested(bytes32 indexed id)
func (_Oracle *OracleFilterer) ParseRequested(log types.Log) (*OracleRequested, error) {
	event := new(OracleRequested)
	if err := _Oracle.contract.UnpackLog(event, "Requested", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchRequested is a free log subscription operation binding the contract event 0x9dc80e952f32e8aef7828795a4cc11bbe605c2ad3cc626df8115be4f78338f69.
//
// Solidity: event Requested(bytes32 indexed id)
func (_Oracle *OracleSession) WatchRequested(fromBlock *uint64, handler func(int, []types.Log), id [32]byte) error {
	return _Oracle.Contract.WatchRequested(fromBlock, handler, id)
}

func (_Oracle *OracleSession) WatchAllRequested(fromBlock *uint64, handler func(int, []types.Log)) error {
	return _Oracle.Contract.WatchAllRequested(fromBlock, handler)
}

// ParseRequested is a log parse operation binding the contract event 0x9dc80e952f32e8aef7828795a4cc11bbe605c2ad3cc626df8115be4f78338f69.
//
// Solidity: event Requested(bytes32 indexed id)
func (_Oracle *OracleSession) ParseRequested(log types.Log) (*OracleRequested, error) {
	return _Oracle.Contract.ParseRequested(log)
}
