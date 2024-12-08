package client

import (
	"context"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"strings"
	"testing"

	"github.com/FISCO-BCOS/go-sdk/abi"
	"github.com/FISCO-BCOS/go-sdk/abi/bind"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/ethereum/go-ethereum/common"
)

// HelloWorldABI is the input ABI used to generate the binding from.
const HelloWorldABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"v\",\"type\":\"string\"}],\"name\":\"set\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"version\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"\",\"type\":\"string\"}],\"name\":\"setValue\",\"type\":\"event\"}]"

// HelloWorldBin is the compiled bytecode used for deploying new contracts.
var HelloWorldBin = "0x60806040526040805190810160405280600181526020017f31000000000000000000000000000000000000000000000000000000000000008152506001908051906020019061004f9291906100ae565b5034801561005c57600080fd5b506040805190810160405280600d81526020017f48656c6c6f2c20576f726c642100000000000000000000000000000000000000815250600090805190602001906100a89291906100ae565b50610153565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100ef57805160ff191683800117855561011d565b8280016001018555821561011d579182015b8281111561011c578251825591602001919060010190610101565b5b50905061012a919061012e565b5090565b61015091905b8082111561014c576000816000905550600101610134565b5090565b90565b6104ac806101626000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680634ed3885e1461005c57806354fd4d50146100c55780636d4ce63c14610155575b600080fd5b34801561006857600080fd5b506100c3600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506101e5565b005b3480156100d157600080fd5b506100da61029b565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561011a5780820151818401526020810190506100ff565b50505050905090810190601f1680156101475780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561016157600080fd5b5061016a610339565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101aa57808201518184015260208101905061018f565b50505050905090810190601f1680156101d75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b80600090805190602001906101fb9291906103db565b507f93a093529f9c8a0c300db4c55fcd27c068c4f5e0e8410bc288c7e76f3d71083e816040518080602001828103825283818151815260200191508051906020019080838360005b8381101561025e578082015181840152602081019050610243565b50505050905090810190601f16801561028b5780820380516001836020036101000a031916815260200191505b509250505060405180910390a150565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103315780601f1061030657610100808354040283529160200191610331565b820191906000526020600020905b81548152906001019060200180831161031457829003601f168201915b505050505081565b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103d15780601f106103a6576101008083540402835291602001916103d1565b820191906000526020600020905b8154815290600101906020018083116103b457829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061041c57805160ff191683800117855561044a565b8280016001018555821561044a579182015b8281111561044957825182559160200191906001019061042e565b5b509050610457919061045b565b5090565b61047d91905b80821115610479576000816000905550600101610461565b5090565b905600a165627a7a72305820fd433a091cb8e1aba3f49e5efb35f937e4b22a85a46f35574834d120699d7ae50029"

func GetClient(t *testing.T) *Client {
	privateKey, err := hex.DecodeString("145e247e170ba3afd6ae97e88f00dbc976c2345d511b0f6713355d19d8b80b58")
	if err != nil {
		t.Fatalf("decode hex failed of %v", err)
	}
	config := &conf.Config{IsHTTP: true, ChainID: 1, IsSMCrypto: false, GroupID: 1,
		PrivateKey: privateKey, NodeURL: "http://localhost:8545"}
	c, err := Dial(config)
	if err != nil {
		t.Fatalf("Dial to %s failed of %v", config.NodeURL, err)
	}
	return c
}

// Get contractAddress、transactionHash、blockHash by this test
func TestBlockHashByNumber(t *testing.T) {
	deployedAddress, txHash := deployHelloWorld(t)
	c := GetClient(t)
	raw, err := c.GetTransactionReceipt(context.Background(), *txHash)
	if err != nil {
		t.Fatalf("GetTransactionReceipt failed: %v", err)
	}
	t.Logf("transaction receipt by transaction hash:\n%s", raw)
	address, err := c.GetContractAddress(context.Background(), *txHash)
	if err != nil {
		t.Fatalf("ContractAddress not found: %v", err)
	}
	if *deployedAddress != address {
		t.Fatalf("address mismatch: %s != %s", address.Hex(), deployedAddress.Hex())
	}

	blockHash, err := c.GetBlockHashByNumber(context.Background(), 1)
	if err != nil {
		t.Fatalf("block hash not found: %v", err)
	}
	includeTx := false
	block, err := c.GetBlockByHash(context.Background(), *blockHash, includeTx)
	if err != nil {
		t.Fatalf("block not found: %v", err)
	}
	peers, err := json.MarshalIndent(block, "", indent)
	t.Logf("block by hash:\n%+v", peers)
	_, err = c.GetTransactionByBlockHashAndIndex(context.Background(), *blockHash, 0)
	if err != nil {
		t.Fatalf("GetTransactionByBlockHashAndIndex failed: %v", err)
	}
	raw, err = c.GetTransactionReceipt(context.Background(), *txHash)
	if err != nil {
		t.Fatalf("transaction receipt not found: %v", err)
	}
	t.Logf("transaction receipt by transaction hash:\n%s", raw)
	transaction, err := c.GetTransactionByHash(context.Background(), *txHash)
	if err != nil {
		t.Fatalf("transaction not found: %v", err)
	}
	tx, err := json.MarshalIndent(transaction, "", indent)
	if err != nil {
		fmt.Printf("transaction marshalIndent error: %v\n", err)
		return
	}
	t.Logf("transaction by hash:\n%+v", tx)
}

func TestClientVersion(t *testing.T) {
	c := GetClient(t)

	clientVersion, err := c.GetClientVersion(context.Background())
	if err != nil {
		t.Fatalf("client version not found: %v", err)
	}
	cv, err := json.MarshalIndent(clientVersion, "", indent)
	if err != nil {
		t.Fatalf("client version marshalIndent error: %v", err)
	}
	t.Logf("client version:\n%s", cv)
}

func TestBlockNumber(t *testing.T) {
	c := GetClient(t)
	// cannot use big.NewInt to construct json request
	// TODO: analysis the ethereum's big.NewInt
	bn, err := c.GetBlockNumber(context.Background())
	if err != nil {
		t.Fatalf("block number not found: %v", err)
	}

	t.Logf("latest block number: \n%d", bn)
}

func TestPBFTView(t *testing.T) {
	c := GetClient(t)
	pv, err := c.GetPBFTView(context.Background())
	if err != nil {
		t.Fatalf("PBFT view not found: %v", err)
	}

	t.Logf("PBFT view: \n%s", pv)
}

func TestBlockLimit(t *testing.T) {
	c := GetClient(t)
	// cannot use big.NewInt to construct json request
	// TODO: analysis the ethereum's big.NewInt
	bl, err := c.GetBlockLimit(context.Background())
	if err != nil {
		t.Fatalf("blockLimit not found: %v", err)
	}

	t.Logf("latest blockLimit: \n%s", bl)
}

func TestGroupID(t *testing.T) {
	c := GetClient(t)
	// cannot use big.NewInt to construct json request
	// TODO: analysis the ethereum's big.NewInt
	groupid := c.GetGroupID()
	t.Logf("current groupID: \n%s", groupid)
}

func TestChainID(t *testing.T) {
	c := GetClient(t)
	// cannot use big.NewInt to construct json request
	// TODO: analysis the ethereum's big.NewInt
	chainid, err := c.GetChainID(context.Background())
	if err != nil {
		t.Fatalf("Chain ID not found: %v", err)
	}
	t.Logf("Chain ID: \n%s", chainid)
}

func TestSealerList(t *testing.T) {
	c := GetClient(t)
	sl, err := c.GetSealerList(context.Background())
	if err != nil {
		t.Fatalf("sealer list not found: %v", err)
	}

	t.Logf("sealer list:\n%s", sl)
}

func TestObserverList(t *testing.T) {
	c := GetClient(t)
	ol, err := c.GetObserverList(context.Background())
	if err != nil {
		t.Fatalf("observer list not found: %v", err)
	}

	t.Logf("observer list:\n%s", ol)
}

func TestConsensusStatus(t *testing.T) {
	c := GetClient(t)
	status, err := c.GetConsensusStatus(context.Background())
	if err != nil {
		t.Fatalf("consensus status not found: %v", err)
	}

	t.Logf("consensus status:\n%s", status)
}

func TestSyncStatus(t *testing.T) {
	c := GetClient(t)

	syncStatus, err := c.GetSyncStatus(context.Background())
	if err != nil {
		t.Fatalf("synchronization status not found: %v", err)
	}
	raw, err := json.MarshalIndent(syncStatus, "", indent)
	if err != nil {
		t.Fatalf("synchronization status marshalIndent error: %v", err)
	}
	t.Logf("synchronization Status:\n%s", raw)
}

func TestPeers(t *testing.T) {
	c := GetClient(t)

	nodes, err := c.GetPeers(context.Background())
	if err != nil {
		t.Fatalf("peers not found: %v", err)
	}
	raw, err := json.MarshalIndent(nodes, "", indent)
	if err != nil {
		t.Fatalf("peers marshalIndent error: %v", err)
	}
	t.Logf("peers:\n%s", raw)
}

func TestGroupPeers(t *testing.T) {
	c := GetClient(t)

	raw, err := c.GetGroupPeers(context.Background())
	if err != nil {
		t.Fatalf("group peers not found: %v", err)
	}

	t.Logf("group peers:\n%s", raw)
}

func TestNodeIDList(t *testing.T) {
	c := GetClient(t)

	raw, err := c.GetNodeIDList(context.Background())
	if err != nil {
		t.Fatalf("nodeID list not found: %v", err)
	}

	t.Logf("nodeID list:\n %s", raw)
}

func TestGroupList(t *testing.T) {
	c := GetClient(t)
	raw, err := c.GetGroupList(context.Background())
	if err != nil {
		t.Fatalf("group list not found: %v", err)
	}

	t.Logf("group list:\n%s", raw)
}

func TestBlockByNumber(t *testing.T) {
	c := GetClient(t)

	var blockNumber int64 = 1
	includeTx := true
	block, err := c.GetBlockByNumber(context.Background(), blockNumber, includeTx)
	if err != nil {
		t.Fatalf("block not found: %v", err)
	}
	raw, err := json.MarshalIndent(block, "", indent)
	if err != nil {
		t.Fatalf("peers marshalIndent error: %v", err)
	}
	t.Logf("block by number:\n%s", raw)
}

func TestTransactionByBlockNumberAndIndex(t *testing.T) {
	c := GetClient(t)

	var blockNumber int64 = 1
	txIndex := 0
	transcation, err := c.GetTransactionByBlockNumberAndIndex(context.Background(), blockNumber, txIndex)
	if err != nil {
		t.Fatalf("transaction not found: %v", err)
	}
	raw, err := json.MarshalIndent(transcation, "", indent)
	if err != nil {
		t.Fatalf("transaction marshalIndent error: %v", err)
	}
	t.Logf("transaction by block number and transaction index:\n%s", raw)
}

func TestPendingTransactions(t *testing.T) {
	c := GetClient(t)

	pendingTransactions, err := c.GetPendingTransactions(context.Background())
	if err != nil {
		t.Fatalf("pending transactions not found: %v", err)
	}
	raw, err := json.MarshalIndent(pendingTransactions, "", indent)
	if err != nil {
		t.Fatalf("pendingTransactions marshalIndent error: %v", err)
	}
	t.Logf("pending transactions:\n%s", raw)
}

func TestPendingTxSize(t *testing.T) {
	c := GetClient(t)

	raw, err := c.GetPendingTxSize(context.Background())
	if err != nil {
		t.Fatalf("pending transactions not found: %v", err)
	}

	t.Logf("the amount of the pending transactions:\n%s", raw)
}

func deployHelloWorld(t *testing.T) (*common.Address, *common.Hash) {
	c := GetClient(t)
	parsed, _ := abi.JSON(strings.NewReader(HelloWorldABI))
	address, tx, _, err := bind.DeployContract(c.GetTransactOpts(), parsed, common.FromHex(HelloWorldBin), c)
	if err != nil {
		t.Errorf("DeployHelloWorld failed: %v", err)
		return nil, nil
	}
	txHash := tx.Hash()
	return &address, &txHash
}

func TestGetCode(t *testing.T) {
	c := GetClient(t)
	address, _ := deployHelloWorld(t)
	raw, err := c.GetCode(context.Background(), *address)
	if err != nil {
		t.Fatalf("contract not found: %v", err)
	}

	t.Logf("the contract code:\n%s", raw)
}

func TestTotalTransactionCount(t *testing.T) {
	c := GetClient(t)

	totalTransactionCount, err := c.GetTotalTransactionCount(context.Background())
	if err != nil {
		t.Fatalf("transactions not found: %v", err)
	}
	raw, err := json.MarshalIndent(totalTransactionCount, "", indent)
	if err != nil {
		t.Fatalf("totalTransactionCount MarshalIndent error: %v", err)
	}
	t.Logf("the total transactions and present block height:\n%s", raw)
}

func TestSystemConfigByKey(t *testing.T) {
	c := GetClient(t)

	findkey := "tx_count_limit"
	raw, err := c.GetSystemConfigByKey(context.Background(), findkey)
	if err != nil {
		t.Fatalf("the value not found: %v", err)
	}

	t.Logf("the value got by the key:\n%s", raw)
}
