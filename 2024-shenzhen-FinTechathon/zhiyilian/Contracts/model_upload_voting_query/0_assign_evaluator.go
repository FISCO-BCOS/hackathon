package main

import (
	"log"
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/conf"
	"github.com/FISCO-BCOS/go-sdk/model"
	"github.com/ethereum/go-ethereum/common"
)

func main() {
	// 解析配置文件
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		log.Fatal(err)
	}
	config := &configs[0]
    client, err := client.Dial(config)
    if err != nil {
        log.Fatal(err)
    }

	// 获取合约实例
	contractAddress := common.HexToAddress("0xC27e774a6D5a0C48E2f611CABd0DA4D812aA62f7")
	instance, err := model.NewModelRegistry(contractAddress, client)
    if err != nil {
        log.Fatal(err)
    }

	modelRegistrySession := &model.ModelRegistrySession{Contract: instance, CallOpts: *client.GetCallOpts(), TransactOpts: *client.GetTransactOpts()}

	// 将三个账户设置为评估机构
	evaluators := []common.Address{
		common.HexToAddress("0xdcc91e225ccf2bffec2d82f9828b53f426556ef2"),
		common.HexToAddress("0x1fe1129f1cef820beb94d40101360df0da6a3879"),
		common.HexToAddress("0xaf816e9bc6f805e10405c0eecc260179d631d3c7"),
		common.HexToAddress("0xbec5fea6a971423e3e27cda3417307b765bf7aab"),
	}

	for _, evaluator := range evaluators {
		tx, receipt, err := modelRegistrySession.AddEvaluator(evaluator)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
		fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	}
}
