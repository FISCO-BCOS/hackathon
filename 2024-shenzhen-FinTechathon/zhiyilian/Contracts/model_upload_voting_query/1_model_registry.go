package main

import (
	"fmt"
	"log"

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

	// 连接到FISCO BCOS节点
	defaultclient, err := client.Dial(config)
	if err != nil {
		log.Fatal(err)
	}

	// 加载合约
	contractAddress := common.HexToAddress("0xC27e774a6D5a0C48E2f611CABd0DA4D812aA62f7")
	instance, err := model.NewModelRegistry(contractAddress, defaultclient)
	if err != nil {
		log.Fatal(err)
	}

	modelRegistrySession := &model.ModelRegistrySession{Contract: instance, CallOpts: *defaultclient.GetCallOpts(), TransactOpts: *defaultclient.GetTransactOpts()}

	// 发起模型上链提案
	// 以下为测试数据
	proposalModelId := "testmodel3"
	proposalModelHash := "testmodelHash3"
	proposalReportHash := "testreportHash3"
	proposalOwner := "0x83309d045a19c44dc3722d15a6abd472f95866ac"
	// 以上为测试数据
	tx, receipt, err := modelRegistrySession.CreateProposal(proposalModelId, proposalModelHash, proposalReportHash, common.HexToAddress(proposalOwner))
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	fmt.Printf("Proposal created with ID: %s\n", proposalModelId)

	// 定义评估机构的私钥路径和地址
	evaluatorPrivateKeyPaths := []string{
		".ci/0x1fe1129f1cef820beb94d40101360df0da6a3879.pem",
		".ci/0xaf816e9bc6f805e10405c0eecc260179d631d3c7.pem",
		".ci/0xbec5fea6a971423e3e27cda3417307b765bf7aab.pem",
	}

	// 切换账户，查询提案，进行投票
	for _, path := range evaluatorPrivateKeyPaths {
		evaluatorKey, _, err := conf.LoadECPrivateKeyFromPEM(path)
		if err != nil {
			log.Fatal(err)
		}
		config.PrivateKey = evaluatorKey

		evaluatorClient, err := client.Dial(config)
		if err != nil {
			log.Fatal(err)
		}
		evaluatorSession := &model.ModelRegistrySession{
			Contract: instance,
			CallOpts: *evaluatorClient.GetCallOpts(),
			TransactOpts: *evaluatorClient.GetTransactOpts(),
		}

		// 查询提案
		proposal, err := evaluatorSession.GetProposal(proposalModelId)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Printf("Current proposal votes: %d\n", proposal.Votes.Int64())

		// 进行投票
		tx, receipt, err = evaluatorSession.VoteForProposal(proposalModelId)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
		fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
		fmt.Printf("Voted with evaluator %s\n", evaluatorSession.CallOpts.From.Hex())

		// 尝试获取模型上链信息
		model, err := evaluatorSession.GetModel(proposalModelId)
		if err != nil {
			fmt.Printf("Model not passing vote yet.\n")
			continue
		}
		fmt.Printf("Model %s is now on chain with hash: %s, report hash: %s, owner: %s\n", proposalModelId, model.ModelHash, model.ReportHash, model.Owner.Hex())
	}
}