package main

import (
	"./build/hello"

	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"

	"context"

	"math/big"
	"strings"
	"time"

	"github.com/chislab/go-fiscobcos/accounts/abi"
	"github.com/chislab/go-fiscobcos/accounts/abi/bind"
	"github.com/chislab/go-fiscobcos/common"
	"github.com/chislab/go-fiscobcos/common/hexutil"
	"github.com/chislab/go-fiscobcos/core/types"
	"github.com/chislab/go-fiscobcos/crypto"
	"github.com/chislab/go-fiscobcos/ethclient"
)

var (
	defaultNode   = "http://127.0.0.1:8917"
	genesisKey, _ = crypto.HexToECDSA("526ccb243b5e279a3ce30c08e4d091a0eb2c3bb5a700946d4da47b28df8fe6d5")
	priKey, _     = crypto.HexToECDSA("3058f2f8626a7445cecd3075eb8da9bb3284256d2737c4a185a96f79daca9441")
	blockKey1, _  = crypto.HexToECDSA("6baed1bb91fc7d37223aee677aff54d7f6b30b73f817b1df474c1c42b1fb2b9a")
	blockKey2, _  = crypto.HexToECDSA("b93f663a6f382c852c4f2e4464de0ef0203f3e62523837741e78fd19c88ff26c")
)

type JsonPRCReq struct {
	ID      uint64        `json:"id"`
	Jsonrpc string        `json:"jsonrpc"`
	Method  string        `json:"method"`
	Params  []interface{} `json:"params"`
}

type JsonPRCRsp struct {
	ID      uint64              `json:"id"`
	Jsonrpc string              `json:"jsonrpc"`
	Result  *types.FiscoReceipt `json:"result"`
}

func main() {
	gethCli, err := ethclient.Dial(defaultNode)
	if err != nil {
		return
	}
	genesisAuth := bind.NewKeyedTransactor(genesisKey)
	genesisAuth.GasLimit = 4700000
	contractAddress := rawDeploy(gethCli)
	fmt.Println("contractAddress", contractAddress.String())
	testGetAndSet(gethCli, genesisAuth, contractAddress)
}

func rawDeploy(gethCli *ethclient.Client) *common.Address {
	contractABI, _ := abi.JSON(strings.NewReader(hello.HelloABI))
	contractBin := common.FromHex(hello.HelloBin)
	input, _ := contractABI.Pack("")
	payLoad := append(contractBin, input...)
	nonce := time.Now().Unix()
	rawTx := types.ContractCreationForFISCOBCOS2(uint64(nonce), GetLatestBlockNumber(1)+100, big.NewInt(0), 4700000, big.NewInt(20000000000), payLoad, big.NewInt(1), big.NewInt(1), nil)
	var signer = types.HomesteadSigner{}
	signature, err := crypto.Sign(signer.Hash(rawTx).Bytes(), genesisKey)
	if err != nil {
		return nil
	}
	signed, err := rawTx.WithSignature(signer, signature)
	if err != nil {
		return nil
	}

	gethCli.SendTransaction(context.Background(), signed)
	fmt.Println("txHash: ", signed.Hash().String())

	receipt, err := CheckTxStatus(signed.Hash().String(), gethCli)
	contractAddr := common.HexToAddress(receipt.ContractAddress)
	return &contractAddr
}

func testGetAndSet(gethCli *ethclient.Client, opts *bind.TransactOpts, address *common.Address) {
	deployedHello, err := hello.NewHello(*address, gethCli)
	if err != nil {
		return
	}
	r, err := deployedHello.Get(&bind.CallOpts{GroupId: opts.GroupId, From: opts.From})
	fmt.Println(r)
	opts.RandomId = big.NewInt(time.Now().Unix())
	opts.BlockLimit = big.NewInt(int64(GetLatestBlockNumber(1) + 100))
	_, err = deployedHello.Set(opts, time.Now().String())
	if err != nil {

	}
	time.Sleep(1 * time.Second)
	r, err = deployedHello.Get(&bind.CallOpts{GroupId: opts.GroupId, From: opts.From})
	fmt.Println(r)
}

func CheckTxStatus(txHash string, b bind.DeployBackend) (*types.FiscoReceipt, error) {
	receipt, err := WaitMinedByHash(context.Background(), b, txHash)
	if err != nil {
		return nil, err
	}
	return receipt, nil
}

func WaitMinedByHash(ctx context.Context, b bind.DeployBackend, txHash string) (*types.FiscoReceipt, error) {
	queryTicker := time.NewTicker(time.Second)
	defer queryTicker.Stop()
	for {
		receipt := GetReceiptByHash(txHash)
		if receipt != nil && receipt.ContractAddress != "" {
			return receipt, nil
		}
		// Wait for the next round.
		select {
		case <-ctx.Done():
			return nil, ctx.Err()
		case <-queryTicker.C:
		}
	}
}

func UnlockPocket(keyJson, keyPwd string) (auth *bind.TransactOpts, err error) {
	myAuth, err := bind.NewTransactor(strings.NewReader(keyJson), keyPwd)
	if err != nil {
		return nil, err
	}
	if myAuth.GasPrice == nil {
		myAuth.GasPrice = big.NewInt(0)
	}
	return myAuth, nil
}

func GetLatestBlockNumber(groupId int) uint64 {
	rspBytes, err := ApplyRPCApi("getBlockNumber", []interface{}{groupId}, uint64(1))
	if err != nil {
		return 0
	}
	var retJson = make(map[string]interface{})
	json.Unmarshal(rspBytes, &retJson)
	var lastestBlockNumber uint64
	if retJson["result"] != nil {
		lastestBlockNumber, _ = hexutil.DecodeUint64(retJson["result"].(string))
	}
	//fmt.Println("lastestBlockNumber", lastestBlockNumber)
	return lastestBlockNumber
}

type ToolStruct struct {
	ID      int                 `json:"id"`
	Jsonrpc string              `json:"jsonrpc"`
	Result  *types.FiscoReceipt `json:"result"`
}

func GetReceiptByHash(txHash string) *types.FiscoReceipt {
	rspBytes, err := ApplyRPCApi("getTransactionReceipt", []interface{}{1, txHash}, uint64(1))
	if err != nil {
		return nil
	}
	var retJson = new(ToolStruct)
	err = json.Unmarshal(rspBytes, retJson)

	if err != nil {
		fmt.Println(err.Error())
		return nil
	}
	return retJson.Result
}

func ApplyRPCApi(rpcMethod string, hexParams []interface{}, nonce uint64) (respBytes []byte, err error) {
	jsonRPC := JsonPRCReq{
		ID:      nonce,
		Jsonrpc: "2.0",
		Method:  rpcMethod,
		Params:  hexParams,
	}
	bytesData, err := json.Marshal(jsonRPC)
	if err != nil {
		return
	}
	reader := bytes.NewReader(bytesData)

	request, err := http.NewRequest("POST", defaultNode, reader)
	if err != nil {
		return
	}
	request.Header.Set("Content-Type", "application/json;charset=UTF-8")
	client := http.Client{}
	resp, err := client.Do(request)
	if err != nil {
		return
	}
	respBytes, err = ioutil.ReadAll(resp.Body)
	if err != nil {
		return
	}
	return
}
