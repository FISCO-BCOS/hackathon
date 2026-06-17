// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package KeyShare

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

// KeyShareABI is the input ABI used to generate the binding from.
const KeyShareABI = "[{\"inputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"int256\",\"name\":\"count\",\"type\":\"int256\"}],\"name\":\"CreateResult\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"int256\",\"name\":\"count\",\"type\":\"int256\"}],\"name\":\"InsertResult\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"int256\",\"name\":\"count\",\"type\":\"int256\"}],\"name\":\"RemoveResult\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"int256\",\"name\":\"count\",\"type\":\"int256\"}],\"name\":\"UpdateResult\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"tableName\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"internalType\":\"string[]\",\"name\":\"fields\",\"type\":\"string[]\"}],\"name\":\"createTable\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"desc\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string[]\",\"name\":\"\",\"type\":\"string[]\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"id\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"secret\",\"type\":\"string\"}],\"name\":\"insert\",\"outputs\":[{\"internalType\":\"int32\",\"name\":\"\",\"type\":\"int32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"id\",\"type\":\"string\"}],\"name\":\"remove\",\"outputs\":[{\"internalType\":\"int32\",\"name\":\"\",\"type\":\"int32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"id\",\"type\":\"string\"}],\"name\":\"select\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"id\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"secret\",\"type\":\"string\"}],\"name\":\"update\",\"outputs\":[{\"internalType\":\"int32\",\"name\":\"\",\"type\":\"int32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"

// KeyShareBin is the compiled bytecode used for deploying new contracts.
var KeyShareBin = "0x60806040523480156200001157600080fd5b506000600267ffffffffffffffff81111562000032576200003162000399565b5b6040519080825280602002602001820160405280156200006757816020015b6060815260200190600190039081620000515790505b5090506040518060400160405280600481526020017f6e616d650000000000000000000000000000000000000000000000000000000081525081600081518110620000b757620000b6620003c8565b5b60200260200101819052506040518060400160405280600681526020017f7365637265740000000000000000000000000000000000000000000000000000815250816001815181106200010f576200010e620003c8565b5b6020026020010181905250600060405180604001604052806040518060400160405280600281526020017f6964000000000000000000000000000000000000000000000000000000000000815250815260200183815250905061100273ffffffffffffffffffffffffffffffffffffffff166331a5a51e6040518060400160405280600b81526020017f745f6b65795f7368617265000000000000000000000000000000000000000000815250836040518363ffffffff1660e01b8152600401620001dc92919062000605565b6020604051808303816000875af1158015620001fc573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019062000222919062000683565b50600061100273ffffffffffffffffffffffffffffffffffffffff1663f23f63c96040518060400160405280600b81526020017f745f6b65795f73686172650000000000000000000000000000000000000000008152506040518263ffffffff1660e01b8152600401620002979190620006b5565b602060405180830381865afa158015620002b5573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190620002db91906200073e565b9050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141562000350576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040162000347906200079a565b60405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050620007bc565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b600081519050919050565b600082825260208201905092915050565b60005b838110156200043357808201518184015260208101905062000416565b8381111562000443576000848401525b50505050565b6000601f19601f8301169050919050565b60006200046782620003f7565b62000473818562000402565b93506200048581856020860162000413565b620004908162000449565b840191505092915050565b600082825260208201905092915050565b6000620004b982620003f7565b620004c581856200049b565b9350620004d781856020860162000413565b620004e28162000449565b840191505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6000620005278383620004ac565b905092915050565b6000602082019050919050565b60006200054982620004ed565b620005558185620004f8565b935083602082028501620005698562000509565b8060005b85811015620005ab578484038952815162000589858262000519565b945062000596836200052f565b925060208a019950506001810190506200056d565b50829750879550505050505092915050565b60006040830160008301518482036000860152620005dc8282620004ac565b91505060208301518482036020860152620005f882826200053c565b9150508091505092915050565b600060408201905081810360008301526200062181856200045a565b90508181036020830152620006378184620005bd565b90509392505050565b600080fd5b60008160030b9050919050565b6200065d8162000645565b81146200066957600080fd5b50565b6000815190506200067d8162000652565b92915050565b6000602082840312156200069c576200069b62000640565b5b6000620006ac848285016200066c565b91505092915050565b60006020820190508181036000830152620006d181846200045a565b905092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006200070682620006d9565b9050919050565b6200071881620006f9565b81146200072457600080fd5b50565b60008151905062000738816200070d565b92915050565b60006020828403121562000757576200075662000640565b5b6000620007678482850162000727565b91505092915050565b50565b60006200078260008362000402565b91506200078f8262000770565b600082019050919050565b60006020820190508181036000830152620007b58162000773565b9050919050565b6115c480620007cc6000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80632fe99bdc1461006757806331c3e4561461009757806355f150f1146100c75780636a5bae4e146100e657806380599e4b14610116578063fcd7e3c114610146575b600080fd5b610081600480360381019061007c9190610a2f565b610177565b60405161008e9190610af2565b60405180910390f35b6100b160048036038101906100ac9190610a2f565b610309565b6040516100be9190610af2565b60405180910390f35b6100cf61051b565b6040516100dd929190610ca1565b60405180910390f35b61010060048036038101906100fb9190610dbe565b6105eb565b60405161010d9190610e7e565b60405180910390f35b610130600480360381019061012b9190610e99565b6106cf565b60405161013d9190610af2565b60405180910390f35b610160600480360381019061015b9190610e99565b6107b0565b60405161016e929190610ee2565b60405180910390f35b600080600267ffffffffffffffff81111561019557610194610904565b5b6040519080825280602002602001820160405280156101c857816020015b60608152602001906001900390816101b35790505b50905083816000815181106101e0576101df610f19565b5b60200260200101819052508281600181518110610200576101ff610f19565b5b60200260200101819052506000604051806040016040528087815260200183815250905060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16635c6e105f836040518263ffffffff1660e01b81526004016102809190611012565b6020604051808303816000875af115801561029f573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906102c39190611060565b90507fc57b01fa77f41df77eaab79a0e2623fab2e7ae3e9530d9b1cab225ad65f2b7ce816040516102f491906110c8565b60405180910390a18093505050509392505050565b600080600267ffffffffffffffff81111561032757610326610904565b5b60405190808252806020026020018201604052801561036057816020015b61034d6108bb565b8152602001906001900390816103455790505b50905060405180604001604052806040518060400160405280600481526020017f6e616d6500000000000000000000000000000000000000000000000000000000815250815260200185815250816000815181106103c1576103c0610f19565b5b602002602001018190525060405180604001604052806040518060400160405280600681526020017f73656372657400000000000000000000000000000000000000000000000000008152508152602001848152508160018151811061042a57610429610f19565b5b602002602001018190525060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166341ffd75f87846040518363ffffffff1660e01b81526004016104939291906111e9565b6020604051808303816000875af11580156104b2573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906104d69190611060565b90507f8e5890af40fc24a059396aca2f83d6ce41fcef086876548fa4fb8ec27e9d292a8160405161050791906110c8565b60405180910390a180925050509392505050565b606080600061100273ffffffffffffffffffffffffffffffffffffffff16635d0d6d546040518060400160405280600b81526020017f745f6b65795f73686172650000000000000000000000000000000000000000008152506040518263ffffffff1660e01b81526004016105909190611220565b600060405180830381865afa1580156105ad573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052508101906105d691906113f9565b90508060000151816020015192509250509091565b6000806040518060400160405280858152602001848152509050600061100273ffffffffffffffffffffffffffffffffffffffff166331a5a51e87846040518363ffffffff1660e01b8152600401610644929190611486565b6020604051808303816000875af1158015610663573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906106879190611060565b90507fb5636cd912a73dcdb5b570dbe331dfa3e6435c93e029e642def2c8e40dacf210816040516106b891906110c8565b60405180910390a18060030b925050509392505050565b60008060008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166380599e4b846040518263ffffffff1660e01b815260040161072b9190611220565b6020604051808303816000875af115801561074a573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061076e9190611060565b90507f4b930e280fe29620bdff00c88155d46d6d82a39f45dd5c3ea114dc31573581128160405161079f91906110c8565b60405180910390a180915050919050565b60608060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663fcd7e3c1856040518263ffffffff1660e01b815260040161080f9190611220565b600060405180830381865afa15801561082c573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052508101906108559190611545565b9050606080600283602001515114156108ad5782602001516000815181106108805761087f610f19565b5b6020026020010151915082602001516001815181106108a2576108a1610f19565b5b602002602001015190505b818194509450505050915091565b604051806040016040528060608152602001606081525090565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b61093c826108f3565b810181811067ffffffffffffffff8211171561095b5761095a610904565b5b80604052505050565b600061096e6108d5565b905061097a8282610933565b919050565b600067ffffffffffffffff82111561099a57610999610904565b5b6109a3826108f3565b9050602081019050919050565b82818337600083830152505050565b60006109d26109cd8461097f565b610964565b9050828152602081018484840111156109ee576109ed6108ee565b5b6109f98482856109b0565b509392505050565b600082601f830112610a1657610a156108e9565b5b8135610a268482602086016109bf565b91505092915050565b600080600060608486031215610a4857610a476108df565b5b600084013567ffffffffffffffff811115610a6657610a656108e4565b5b610a7286828701610a01565b935050602084013567ffffffffffffffff811115610a9357610a926108e4565b5b610a9f86828701610a01565b925050604084013567ffffffffffffffff811115610ac057610abf6108e4565b5b610acc86828701610a01565b9150509250925092565b60008160030b9050919050565b610aec81610ad6565b82525050565b6000602082019050610b076000830184610ae3565b92915050565b600081519050919050565b600082825260208201905092915050565b60005b83811015610b47578082015181840152602081019050610b2c565b83811115610b56576000848401525b50505050565b6000610b6782610b0d565b610b718185610b18565b9350610b81818560208601610b29565b610b8a816108f3565b840191505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b600082825260208201905092915050565b6000610bdd82610b0d565b610be78185610bc1565b9350610bf7818560208601610b29565b610c00816108f3565b840191505092915050565b6000610c178383610bd2565b905092915050565b6000602082019050919050565b6000610c3782610b95565b610c418185610ba0565b935083602082028501610c5385610bb1565b8060005b85811015610c8f5784840389528151610c708582610c0b565b9450610c7b83610c1f565b925060208a01995050600181019050610c57565b50829750879550505050505092915050565b60006040820190508181036000830152610cbb8185610b5c565b90508181036020830152610ccf8184610c2c565b90509392505050565b600067ffffffffffffffff821115610cf357610cf2610904565b5b602082029050602081019050919050565b600080fd5b6000610d1c610d1784610cd8565b610964565b90508083825260208201905060208402830185811115610d3f57610d3e610d04565b5b835b81811015610d8657803567ffffffffffffffff811115610d6457610d636108e9565b5b808601610d718982610a01565b85526020850194505050602081019050610d41565b5050509392505050565b600082601f830112610da557610da46108e9565b5b8135610db5848260208601610d09565b91505092915050565b600080600060608486031215610dd757610dd66108df565b5b600084013567ffffffffffffffff811115610df557610df46108e4565b5b610e0186828701610a01565b935050602084013567ffffffffffffffff811115610e2257610e216108e4565b5b610e2e86828701610a01565b925050604084013567ffffffffffffffff811115610e4f57610e4e6108e4565b5b610e5b86828701610d90565b9150509250925092565b6000819050919050565b610e7881610e65565b82525050565b6000602082019050610e936000830184610e6f565b92915050565b600060208284031215610eaf57610eae6108df565b5b600082013567ffffffffffffffff811115610ecd57610ecc6108e4565b5b610ed984828501610a01565b91505092915050565b60006040820190508181036000830152610efc8185610b5c565b90508181036020830152610f108184610b5c565b90509392505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b600082825260208201905092915050565b6000610f6482610b95565b610f6e8185610f48565b935083602082028501610f8085610bb1565b8060005b85811015610fbc5784840389528151610f9d8582610c0b565b9450610fa883610c1f565b925060208a01995050600181019050610f84565b50829750879550505050505092915050565b60006040830160008301518482036000860152610feb8282610bd2565b915050602083015184820360208601526110058282610f59565b9150508091505092915050565b6000602082019050818103600083015261102c8184610fce565b905092915050565b61103d81610ad6565b811461104857600080fd5b50565b60008151905061105a81611034565b92915050565b600060208284031215611076576110756108df565b5b60006110848482850161104b565b91505092915050565b6000819050919050565b60006110b26110ad6110a884610ad6565b61108d565b610e65565b9050919050565b6110c281611097565b82525050565b60006020820190506110dd60008301846110b9565b92915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6000604083016000830151848203600086015261112c8282610bd2565b915050602083015184820360208601526111468282610bd2565b9150508091505092915050565b600061115f838361110f565b905092915050565b6000602082019050919050565b600061117f826110e3565b61118981856110ee565b93508360208202850161119b856110ff565b8060005b858110156111d757848403895281516111b88582611153565b94506111c383611167565b925060208a0199505060018101905061119f565b50829750879550505050505092915050565b600060408201905081810360008301526112038185610b5c565b905081810360208301526112178184611174565b90509392505050565b6000602082019050818103600083015261123a8184610b5c565b905092915050565b600080fd5b600080fd5b600061125f61125a8461097f565b610964565b90508281526020810184848401111561127b5761127a6108ee565b5b611286848285610b29565b509392505050565b600082601f8301126112a3576112a26108e9565b5b81516112b384826020860161124c565b91505092915050565b60006112cf6112ca84610cd8565b610964565b905080838252602082019050602084028301858111156112f2576112f1610d04565b5b835b8181101561133957805167ffffffffffffffff811115611317576113166108e9565b5b808601611324898261128e565b855260208501945050506020810190506112f4565b5050509392505050565b600082601f830112611358576113576108e9565b5b81516113688482602086016112bc565b91505092915050565b60006040828403121561138757611386611242565b5b6113916040610964565b9050600082015167ffffffffffffffff8111156113b1576113b0611247565b5b6113bd8482850161128e565b600083015250602082015167ffffffffffffffff8111156113e1576113e0611247565b5b6113ed84828501611343565b60208301525092915050565b60006020828403121561140f5761140e6108df565b5b600082015167ffffffffffffffff81111561142d5761142c6108e4565b5b61143984828501611371565b91505092915050565b6000604083016000830151848203600086015261145f8282610bd2565b915050602083015184820360208601526114798282610f59565b9150508091505092915050565b600060408201905081810360008301526114a08185610b5c565b905081810360208301526114b48184611442565b90509392505050565b6000604082840312156114d3576114d2611242565b5b6114dd6040610964565b9050600082015167ffffffffffffffff8111156114fd576114fc611247565b5b6115098482850161128e565b600083015250602082015167ffffffffffffffff81111561152d5761152c611247565b5b61153984828501611343565b60208301525092915050565b60006020828403121561155b5761155a6108df565b5b600082015167ffffffffffffffff811115611579576115786108e4565b5b611585848285016114bd565b9150509291505056fea2646970667358221220c4aba62bac51140354dd55b475ed3bde2226a18fc413bccf0b2fbfb1fec7c8dc64736f6c634300080b0033"
var KeyShareSMBin = "0x"

// DeployKeyShare deploys a new contract, binding an instance of KeyShare to it.
func DeployKeyShare(auth *bind.TransactOpts, backend bind.ContractBackend) (common.Address, *types.Receipt, *KeyShare, error) {
	parsed, err := abi.JSON(strings.NewReader(KeyShareABI))
	if err != nil {
		return common.Address{}, nil, nil, err
	}

	var bytecode []byte
	if backend.SMCrypto() {
		bytecode = common.FromHex(KeyShareSMBin)
	} else {
		bytecode = common.FromHex(KeyShareBin)
	}
	if len(bytecode) == 0 {
		return common.Address{}, nil, nil, fmt.Errorf("cannot deploy empty bytecode")
	}
	address, receipt, contract, err := bind.DeployContract(auth, parsed, bytecode, KeyShareABI, backend)
	if err != nil {
		return common.Address{}, nil, nil, err
	}
	return address, receipt, &KeyShare{KeyShareCaller: KeyShareCaller{contract: contract}, KeyShareTransactor: KeyShareTransactor{contract: contract}, KeyShareFilterer: KeyShareFilterer{contract: contract}}, nil
}

func AsyncDeployKeyShare(auth *bind.TransactOpts, handler func(*types.Receipt, error), backend bind.ContractBackend) (*types.Transaction, error) {
	parsed, err := abi.JSON(strings.NewReader(KeyShareABI))
	if err != nil {
		return nil, err
	}

	var bytecode []byte
	if backend.SMCrypto() {
		bytecode = common.FromHex(KeyShareSMBin)
	} else {
		bytecode = common.FromHex(KeyShareBin)
	}
	if len(bytecode) == 0 {
		return nil, fmt.Errorf("cannot deploy empty bytecode")
	}
	tx, err := bind.AsyncDeployContract(auth, handler, parsed, bytecode, KeyShareABI, backend)
	if err != nil {
		return nil, err
	}
	return tx, nil
}

// KeyShare is an auto generated Go binding around a Solidity contract.
type KeyShare struct {
	KeyShareCaller     // Read-only binding to the contract
	KeyShareTransactor // Write-only binding to the contract
	KeyShareFilterer   // Log filterer for contract events
}

// KeyShareCaller is an auto generated read-only Go binding around a Solidity contract.
type KeyShareCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// KeyShareTransactor is an auto generated write-only Go binding around a Solidity contract.
type KeyShareTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// KeyShareFilterer is an auto generated log filtering Go binding around a Solidity contract events.
type KeyShareFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// KeyShareSession is an auto generated Go binding around a Solidity contract,
// with pre-set call and transact options.
type KeyShareSession struct {
	Contract     *KeyShare         // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// KeyShareCallerSession is an auto generated read-only Go binding around a Solidity contract,
// with pre-set call options.
type KeyShareCallerSession struct {
	Contract *KeyShareCaller // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts   // Call options to use throughout this session
}

// KeyShareTransactorSession is an auto generated write-only Go binding around a Solidity contract,
// with pre-set transact options.
type KeyShareTransactorSession struct {
	Contract     *KeyShareTransactor // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts   // Transaction auth options to use throughout this session
}

// KeyShareRaw is an auto generated low-level Go binding around a Solidity contract.
type KeyShareRaw struct {
	Contract *KeyShare // Generic contract binding to access the raw methods on
}

// KeyShareCallerRaw is an auto generated low-level read-only Go binding around a Solidity contract.
type KeyShareCallerRaw struct {
	Contract *KeyShareCaller // Generic read-only contract binding to access the raw methods on
}

// KeyShareTransactorRaw is an auto generated low-level write-only Go binding around a Solidity contract.
type KeyShareTransactorRaw struct {
	Contract *KeyShareTransactor // Generic write-only contract binding to access the raw methods on
}

// NewKeyShare creates a new instance of KeyShare, bound to a specific deployed contract.
func NewKeyShare(address common.Address, backend bind.ContractBackend) (*KeyShare, error) {
	contract, err := bindKeyShare(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &KeyShare{KeyShareCaller: KeyShareCaller{contract: contract}, KeyShareTransactor: KeyShareTransactor{contract: contract}, KeyShareFilterer: KeyShareFilterer{contract: contract}}, nil
}

// NewKeyShareCaller creates a new read-only instance of KeyShare, bound to a specific deployed contract.
func NewKeyShareCaller(address common.Address, caller bind.ContractCaller) (*KeyShareCaller, error) {
	contract, err := bindKeyShare(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &KeyShareCaller{contract: contract}, nil
}

// NewKeyShareTransactor creates a new write-only instance of KeyShare, bound to a specific deployed contract.
func NewKeyShareTransactor(address common.Address, transactor bind.ContractTransactor) (*KeyShareTransactor, error) {
	contract, err := bindKeyShare(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &KeyShareTransactor{contract: contract}, nil
}

// NewKeyShareFilterer creates a new log filterer instance of KeyShare, bound to a specific deployed contract.
func NewKeyShareFilterer(address common.Address, filterer bind.ContractFilterer) (*KeyShareFilterer, error) {
	contract, err := bindKeyShare(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &KeyShareFilterer{contract: contract}, nil
}

// bindKeyShare binds a generic wrapper to an already deployed contract.
func bindKeyShare(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(KeyShareABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_KeyShare *KeyShareRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _KeyShare.Contract.KeyShareCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_KeyShare *KeyShareRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.KeyShareTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_KeyShare *KeyShareRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.KeyShareTransactor.contract.TransactWithResult(opts, result, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_KeyShare *KeyShareCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _KeyShare.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_KeyShare *KeyShareTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_KeyShare *KeyShareTransactorRaw) TransactWithResult(opts *bind.TransactOpts, result interface{}, method string, params ...interface{}) (*types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.contract.TransactWithResult(opts, result, method, params...)
}

// Desc is a free data retrieval call binding the contract method 0x55f150f1.
//
// Solidity: function desc() constant returns(string, string[])
func (_KeyShare *KeyShareCaller) Desc(opts *bind.CallOpts) (string, []string, error) {
	var (
		ret0 = new(string)
		ret1 = new([]string)
	)
	out := &[]interface{}{
		ret0,
		ret1,
	}
	err := _KeyShare.contract.Call(opts, out, "desc")
	return *ret0, *ret1, err
}

// Desc is a free data retrieval call binding the contract method 0x55f150f1.
//
// Solidity: function desc() constant returns(string, string[])
func (_KeyShare *KeyShareSession) Desc() (string, []string, error) {
	return _KeyShare.Contract.Desc(&_KeyShare.CallOpts)
}

// Desc is a free data retrieval call binding the contract method 0x55f150f1.
//
// Solidity: function desc() constant returns(string, string[])
func (_KeyShare *KeyShareCallerSession) Desc() (string, []string, error) {
	return _KeyShare.Contract.Desc(&_KeyShare.CallOpts)
}

// Select is a free data retrieval call binding the contract method 0xfcd7e3c1.
//
// Solidity: function select(string id) constant returns(string, string)
func (_KeyShare *KeyShareCaller) Select(opts *bind.CallOpts, id string) (string, string, error) {
	var (
		ret0 = new(string)
		ret1 = new(string)
	)
	out := &[]interface{}{
		ret0,
		ret1,
	}
	err := _KeyShare.contract.Call(opts, out, "select", id)
	return *ret0, *ret1, err
}

// Select is a free data retrieval call binding the contract method 0xfcd7e3c1.
//
// Solidity: function select(string id) constant returns(string, string)
func (_KeyShare *KeyShareSession) Select(id string) (string, string, error) {
	return _KeyShare.Contract.Select(&_KeyShare.CallOpts, id)
}

// Select is a free data retrieval call binding the contract method 0xfcd7e3c1.
//
// Solidity: function select(string id) constant returns(string, string)
func (_KeyShare *KeyShareCallerSession) Select(id string) (string, string, error) {
	return _KeyShare.Contract.Select(&_KeyShare.CallOpts, id)
}

// CreateTable is a paid mutator transaction binding the contract method 0x6a5bae4e.
//
// Solidity: function createTable(string tableName, string key, string[] fields) returns(int256)
func (_KeyShare *KeyShareTransactor) CreateTable(opts *bind.TransactOpts, tableName string, key string, fields []string) (*big.Int, *types.Transaction, *types.Receipt, error) {
	var (
		ret0 = new(*big.Int)
	)
	out := ret0
	transaction, receipt, err := _KeyShare.contract.TransactWithResult(opts, out, "createTable", tableName, key, fields)
	return *ret0, transaction, receipt, err
}

func (_KeyShare *KeyShareTransactor) AsyncCreateTable(handler func(*types.Receipt, error), opts *bind.TransactOpts, tableName string, key string, fields []string) (*types.Transaction, error) {
	return _KeyShare.contract.AsyncTransact(opts, handler, "createTable", tableName, key, fields)
}

// CreateTable is a paid mutator transaction binding the contract method 0x6a5bae4e.
//
// Solidity: function createTable(string tableName, string key, string[] fields) returns(int256)
func (_KeyShare *KeyShareSession) CreateTable(tableName string, key string, fields []string) (*big.Int, *types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.CreateTable(&_KeyShare.TransactOpts, tableName, key, fields)
}

func (_KeyShare *KeyShareSession) AsyncCreateTable(handler func(*types.Receipt, error), tableName string, key string, fields []string) (*types.Transaction, error) {
	return _KeyShare.Contract.AsyncCreateTable(handler, &_KeyShare.TransactOpts, tableName, key, fields)
}

// CreateTable is a paid mutator transaction binding the contract method 0x6a5bae4e.
//
// Solidity: function createTable(string tableName, string key, string[] fields) returns(int256)
func (_KeyShare *KeyShareTransactorSession) CreateTable(tableName string, key string, fields []string) (*big.Int, *types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.CreateTable(&_KeyShare.TransactOpts, tableName, key, fields)
}

func (_KeyShare *KeyShareTransactorSession) AsyncCreateTable(handler func(*types.Receipt, error), tableName string, key string, fields []string) (*types.Transaction, error) {
	return _KeyShare.Contract.AsyncCreateTable(handler, &_KeyShare.TransactOpts, tableName, key, fields)
}

// Insert is a paid mutator transaction binding the contract method 0x2fe99bdc.
//
// Solidity: function insert(string id, string name, string secret) returns(int32)
func (_KeyShare *KeyShareTransactor) Insert(opts *bind.TransactOpts, id string, name string, secret string) (int32, *types.Transaction, *types.Receipt, error) {
	var (
		ret0 = new(int32)
	)
	out := ret0
	transaction, receipt, err := _KeyShare.contract.TransactWithResult(opts, out, "insert", id, name, secret)
	return *ret0, transaction, receipt, err
}

func (_KeyShare *KeyShareTransactor) AsyncInsert(handler func(*types.Receipt, error), opts *bind.TransactOpts, id string, name string, secret string) (*types.Transaction, error) {
	return _KeyShare.contract.AsyncTransact(opts, handler, "insert", id, name, secret)
}

// Insert is a paid mutator transaction binding the contract method 0x2fe99bdc.
//
// Solidity: function insert(string id, string name, string secret) returns(int32)
func (_KeyShare *KeyShareSession) Insert(id string, name string, secret string) (int32, *types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.Insert(&_KeyShare.TransactOpts, id, name, secret)
}

func (_KeyShare *KeyShareSession) AsyncInsert(handler func(*types.Receipt, error), id string, name string, secret string) (*types.Transaction, error) {
	return _KeyShare.Contract.AsyncInsert(handler, &_KeyShare.TransactOpts, id, name, secret)
}

// Insert is a paid mutator transaction binding the contract method 0x2fe99bdc.
//
// Solidity: function insert(string id, string name, string secret) returns(int32)
func (_KeyShare *KeyShareTransactorSession) Insert(id string, name string, secret string) (int32, *types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.Insert(&_KeyShare.TransactOpts, id, name, secret)
}

func (_KeyShare *KeyShareTransactorSession) AsyncInsert(handler func(*types.Receipt, error), id string, name string, secret string) (*types.Transaction, error) {
	return _KeyShare.Contract.AsyncInsert(handler, &_KeyShare.TransactOpts, id, name, secret)
}

// Remove is a paid mutator transaction binding the contract method 0x80599e4b.
//
// Solidity: function remove(string id) returns(int32)
func (_KeyShare *KeyShareTransactor) Remove(opts *bind.TransactOpts, id string) (int32, *types.Transaction, *types.Receipt, error) {
	var (
		ret0 = new(int32)
	)
	out := ret0
	transaction, receipt, err := _KeyShare.contract.TransactWithResult(opts, out, "remove", id)
	return *ret0, transaction, receipt, err
}

func (_KeyShare *KeyShareTransactor) AsyncRemove(handler func(*types.Receipt, error), opts *bind.TransactOpts, id string) (*types.Transaction, error) {
	return _KeyShare.contract.AsyncTransact(opts, handler, "remove", id)
}

// Remove is a paid mutator transaction binding the contract method 0x80599e4b.
//
// Solidity: function remove(string id) returns(int32)
func (_KeyShare *KeyShareSession) Remove(id string) (int32, *types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.Remove(&_KeyShare.TransactOpts, id)
}

func (_KeyShare *KeyShareSession) AsyncRemove(handler func(*types.Receipt, error), id string) (*types.Transaction, error) {
	return _KeyShare.Contract.AsyncRemove(handler, &_KeyShare.TransactOpts, id)
}

// Remove is a paid mutator transaction binding the contract method 0x80599e4b.
//
// Solidity: function remove(string id) returns(int32)
func (_KeyShare *KeyShareTransactorSession) Remove(id string) (int32, *types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.Remove(&_KeyShare.TransactOpts, id)
}

func (_KeyShare *KeyShareTransactorSession) AsyncRemove(handler func(*types.Receipt, error), id string) (*types.Transaction, error) {
	return _KeyShare.Contract.AsyncRemove(handler, &_KeyShare.TransactOpts, id)
}

// Update is a paid mutator transaction binding the contract method 0x31c3e456.
//
// Solidity: function update(string id, string name, string secret) returns(int32)
func (_KeyShare *KeyShareTransactor) Update(opts *bind.TransactOpts, id string, name string, secret string) (int32, *types.Transaction, *types.Receipt, error) {
	var (
		ret0 = new(int32)
	)
	out := ret0
	transaction, receipt, err := _KeyShare.contract.TransactWithResult(opts, out, "update", id, name, secret)
	return *ret0, transaction, receipt, err
}

func (_KeyShare *KeyShareTransactor) AsyncUpdate(handler func(*types.Receipt, error), opts *bind.TransactOpts, id string, name string, secret string) (*types.Transaction, error) {
	return _KeyShare.contract.AsyncTransact(opts, handler, "update", id, name, secret)
}

// Update is a paid mutator transaction binding the contract method 0x31c3e456.
//
// Solidity: function update(string id, string name, string secret) returns(int32)
func (_KeyShare *KeyShareSession) Update(id string, name string, secret string) (int32, *types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.Update(&_KeyShare.TransactOpts, id, name, secret)
}

func (_KeyShare *KeyShareSession) AsyncUpdate(handler func(*types.Receipt, error), id string, name string, secret string) (*types.Transaction, error) {
	return _KeyShare.Contract.AsyncUpdate(handler, &_KeyShare.TransactOpts, id, name, secret)
}

// Update is a paid mutator transaction binding the contract method 0x31c3e456.
//
// Solidity: function update(string id, string name, string secret) returns(int32)
func (_KeyShare *KeyShareTransactorSession) Update(id string, name string, secret string) (int32, *types.Transaction, *types.Receipt, error) {
	return _KeyShare.Contract.Update(&_KeyShare.TransactOpts, id, name, secret)
}

func (_KeyShare *KeyShareTransactorSession) AsyncUpdate(handler func(*types.Receipt, error), id string, name string, secret string) (*types.Transaction, error) {
	return _KeyShare.Contract.AsyncUpdate(handler, &_KeyShare.TransactOpts, id, name, secret)
}

// KeyShareCreateResult represents a CreateResult event raised by the KeyShare contract.
type KeyShareCreateResult struct {
	Count *big.Int
	Raw   types.Log // Blockchain specific contextual infos
}

// WatchCreateResult is a free log subscription operation binding the contract event 0xb5636cd912a73dcdb5b570dbe331dfa3e6435c93e029e642def2c8e40dacf210.
//
// Solidity: event CreateResult(int256 count)
func (_KeyShare *KeyShareFilterer) WatchCreateResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.contract.WatchLogs(fromBlock, handler, "CreateResult")
}

func (_KeyShare *KeyShareFilterer) WatchAllCreateResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.contract.WatchLogs(fromBlock, handler, "CreateResult")
}

// ParseCreateResult is a log parse operation binding the contract event 0xb5636cd912a73dcdb5b570dbe331dfa3e6435c93e029e642def2c8e40dacf210.
//
// Solidity: event CreateResult(int256 count)
func (_KeyShare *KeyShareFilterer) ParseCreateResult(log types.Log) (*KeyShareCreateResult, error) {
	event := new(KeyShareCreateResult)
	if err := _KeyShare.contract.UnpackLog(event, "CreateResult", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchCreateResult is a free log subscription operation binding the contract event 0xb5636cd912a73dcdb5b570dbe331dfa3e6435c93e029e642def2c8e40dacf210.
//
// Solidity: event CreateResult(int256 count)
func (_KeyShare *KeyShareSession) WatchCreateResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.Contract.WatchCreateResult(fromBlock, handler)
}

func (_KeyShare *KeyShareSession) WatchAllCreateResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.Contract.WatchAllCreateResult(fromBlock, handler)
}

// ParseCreateResult is a log parse operation binding the contract event 0xb5636cd912a73dcdb5b570dbe331dfa3e6435c93e029e642def2c8e40dacf210.
//
// Solidity: event CreateResult(int256 count)
func (_KeyShare *KeyShareSession) ParseCreateResult(log types.Log) (*KeyShareCreateResult, error) {
	return _KeyShare.Contract.ParseCreateResult(log)
}

// KeyShareInsertResult represents a InsertResult event raised by the KeyShare contract.
type KeyShareInsertResult struct {
	Count *big.Int
	Raw   types.Log // Blockchain specific contextual infos
}

// WatchInsertResult is a free log subscription operation binding the contract event 0xc57b01fa77f41df77eaab79a0e2623fab2e7ae3e9530d9b1cab225ad65f2b7ce.
//
// Solidity: event InsertResult(int256 count)
func (_KeyShare *KeyShareFilterer) WatchInsertResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.contract.WatchLogs(fromBlock, handler, "InsertResult")
}

func (_KeyShare *KeyShareFilterer) WatchAllInsertResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.contract.WatchLogs(fromBlock, handler, "InsertResult")
}

// ParseInsertResult is a log parse operation binding the contract event 0xc57b01fa77f41df77eaab79a0e2623fab2e7ae3e9530d9b1cab225ad65f2b7ce.
//
// Solidity: event InsertResult(int256 count)
func (_KeyShare *KeyShareFilterer) ParseInsertResult(log types.Log) (*KeyShareInsertResult, error) {
	event := new(KeyShareInsertResult)
	if err := _KeyShare.contract.UnpackLog(event, "InsertResult", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchInsertResult is a free log subscription operation binding the contract event 0xc57b01fa77f41df77eaab79a0e2623fab2e7ae3e9530d9b1cab225ad65f2b7ce.
//
// Solidity: event InsertResult(int256 count)
func (_KeyShare *KeyShareSession) WatchInsertResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.Contract.WatchInsertResult(fromBlock, handler)
}

func (_KeyShare *KeyShareSession) WatchAllInsertResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.Contract.WatchAllInsertResult(fromBlock, handler)
}

// ParseInsertResult is a log parse operation binding the contract event 0xc57b01fa77f41df77eaab79a0e2623fab2e7ae3e9530d9b1cab225ad65f2b7ce.
//
// Solidity: event InsertResult(int256 count)
func (_KeyShare *KeyShareSession) ParseInsertResult(log types.Log) (*KeyShareInsertResult, error) {
	return _KeyShare.Contract.ParseInsertResult(log)
}

// KeyShareRemoveResult represents a RemoveResult event raised by the KeyShare contract.
type KeyShareRemoveResult struct {
	Count *big.Int
	Raw   types.Log // Blockchain specific contextual infos
}

// WatchRemoveResult is a free log subscription operation binding the contract event 0x4b930e280fe29620bdff00c88155d46d6d82a39f45dd5c3ea114dc3157358112.
//
// Solidity: event RemoveResult(int256 count)
func (_KeyShare *KeyShareFilterer) WatchRemoveResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.contract.WatchLogs(fromBlock, handler, "RemoveResult")
}

func (_KeyShare *KeyShareFilterer) WatchAllRemoveResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.contract.WatchLogs(fromBlock, handler, "RemoveResult")
}

// ParseRemoveResult is a log parse operation binding the contract event 0x4b930e280fe29620bdff00c88155d46d6d82a39f45dd5c3ea114dc3157358112.
//
// Solidity: event RemoveResult(int256 count)
func (_KeyShare *KeyShareFilterer) ParseRemoveResult(log types.Log) (*KeyShareRemoveResult, error) {
	event := new(KeyShareRemoveResult)
	if err := _KeyShare.contract.UnpackLog(event, "RemoveResult", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchRemoveResult is a free log subscription operation binding the contract event 0x4b930e280fe29620bdff00c88155d46d6d82a39f45dd5c3ea114dc3157358112.
//
// Solidity: event RemoveResult(int256 count)
func (_KeyShare *KeyShareSession) WatchRemoveResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.Contract.WatchRemoveResult(fromBlock, handler)
}

func (_KeyShare *KeyShareSession) WatchAllRemoveResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.Contract.WatchAllRemoveResult(fromBlock, handler)
}

// ParseRemoveResult is a log parse operation binding the contract event 0x4b930e280fe29620bdff00c88155d46d6d82a39f45dd5c3ea114dc3157358112.
//
// Solidity: event RemoveResult(int256 count)
func (_KeyShare *KeyShareSession) ParseRemoveResult(log types.Log) (*KeyShareRemoveResult, error) {
	return _KeyShare.Contract.ParseRemoveResult(log)
}

// KeyShareUpdateResult represents a UpdateResult event raised by the KeyShare contract.
type KeyShareUpdateResult struct {
	Count *big.Int
	Raw   types.Log // Blockchain specific contextual infos
}

// WatchUpdateResult is a free log subscription operation binding the contract event 0x8e5890af40fc24a059396aca2f83d6ce41fcef086876548fa4fb8ec27e9d292a.
//
// Solidity: event UpdateResult(int256 count)
func (_KeyShare *KeyShareFilterer) WatchUpdateResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.contract.WatchLogs(fromBlock, handler, "UpdateResult")
}

func (_KeyShare *KeyShareFilterer) WatchAllUpdateResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.contract.WatchLogs(fromBlock, handler, "UpdateResult")
}

// ParseUpdateResult is a log parse operation binding the contract event 0x8e5890af40fc24a059396aca2f83d6ce41fcef086876548fa4fb8ec27e9d292a.
//
// Solidity: event UpdateResult(int256 count)
func (_KeyShare *KeyShareFilterer) ParseUpdateResult(log types.Log) (*KeyShareUpdateResult, error) {
	event := new(KeyShareUpdateResult)
	if err := _KeyShare.contract.UnpackLog(event, "UpdateResult", log); err != nil {
		return nil, err
	}
	return event, nil
}

// WatchUpdateResult is a free log subscription operation binding the contract event 0x8e5890af40fc24a059396aca2f83d6ce41fcef086876548fa4fb8ec27e9d292a.
//
// Solidity: event UpdateResult(int256 count)
func (_KeyShare *KeyShareSession) WatchUpdateResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.Contract.WatchUpdateResult(fromBlock, handler)
}

func (_KeyShare *KeyShareSession) WatchAllUpdateResult(fromBlock *int64, handler func(int, []types.Log)) (string, error) {
	return _KeyShare.Contract.WatchAllUpdateResult(fromBlock, handler)
}

// ParseUpdateResult is a log parse operation binding the contract event 0x8e5890af40fc24a059396aca2f83d6ce41fcef086876548fa4fb8ec27e9d292a.
//
// Solidity: event UpdateResult(int256 count)
func (_KeyShare *KeyShareSession) ParseUpdateResult(log types.Log) (*KeyShareUpdateResult, error) {
	return _KeyShare.Contract.ParseUpdateResult(log)
}
