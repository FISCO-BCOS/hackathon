package main

import (
    "fmt"
    "log"
	"math/big"

    "github.com/FISCO-BCOS/go-sdk/client"
    "github.com/FISCO-BCOS/go-sdk/conf"
    "github.com/FISCO-BCOS/go-sdk/trade"
    "github.com/ethereum/go-ethereum/common"
)

func main() {
    // 解析配置文件
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		log.Fatal(err)
	}
	config := &configs[0]	
    contractAddress := common.HexToAddress("0x493Cb81dAeE9d739a33235cC1F9bd37C2280550A")
    

	// 假设买方账户创建交易
	// 初始化买方client
	buyerpath := ".ci/0xc89979c91f21f1b01df59440e954533d6f7978aa.pem"
	buyerKey, _, err := conf.LoadECPrivateKeyFromPEM(buyerpath)
	if err != nil {
		log.Fatal(err)
	}
	config.PrivateKey = buyerKey
	buyerClient, err := client.Dial(config)
	if err != nil {
		log.Fatal(err)
	}
	buyerinstance, err := trade.NewModelTransaction(contractAddress, buyerClient)
    if err != nil {
        log.Fatal(err)
    }
	buyerSession := &trade.ModelTransactionSession{
		Contract: buyerinstance,
		CallOpts: *buyerClient.GetCallOpts(),
		TransactOpts: *buyerClient.GetTransactOpts(),
	}
	
	// 买方查询模型
	// 调用另一个合约，略

	// 买方创建交易
	// 以下为测试数据
	modelId := "testmodel1"
    // seller := common.HexToAddress("0x83309d045a19c44dc3722d15a6abd472f95866ac") // 替换为卖家地址, 在查询模型时可以获取
    // buyer := common.HexToAddress("0xc89979c91f21f1b01df59440e954533d6f7978aa")   // 替换为买家地址, 即当前用户
    // amount := big.NewInt(1000)                           						 // 交易金额
    // tx, receipt, err := buyerSession.CreateTransaction(modelId, seller, buyer, amount)
	// if err != nil {
	// 	log.Fatal(err)
	// }
	// fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	// fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	// fmt.Printf("Trade Created by buyer %s\n", buyerSession.CallOpts.From.Hex())

	// 初始化卖方client
	sellerpath := ".ci/0x83309d045a19c44dc3722d15a6abd472f95866ac.pem"
	sellerKey, _, err := conf.LoadECPrivateKeyFromPEM(sellerpath)
	if err != nil {
		log.Fatal(err)
	}
	config.PrivateKey = sellerKey
	sellerClient, err := client.Dial(config)
	if err != nil {
		log.Fatal(err)
	}
	sellerinstance, err := trade.NewModelTransaction(contractAddress, sellerClient)
    if err != nil {
        log.Fatal(err)
    }
	sellerSession := &trade.ModelTransactionSession{
		Contract: sellerinstance,
		CallOpts: *sellerClient.GetCallOpts(),
		TransactOpts: *sellerClient.GetTransactOpts(),
	}
	
	// 卖方查询交易
	result, err := sellerSession.GetTransactionsByModel(modelId)
	if err != nil {
		log.Fatal(err)
	}
	sellers := result.Sellers
	buyers := result.Buyers
	amounts := result.Amounts
	timestamps := result.Timestamps
	completions := result.Completions
	fmt.Printf("【Transactions for model ID - query by seller】: %s\n", modelId)
	for i := 0; i < len(sellers); i++ {
		fmt.Printf("Transaction %d:\n", i+1)
		fmt.Printf("Seller: %s\n", sellers[i].Hex())
		fmt.Printf("Buyer: %s\n", buyers[i].Hex())
		fmt.Printf("Amount: %s\n", amounts[i].String())
		fmt.Printf("Timestamp: %s\n", timestamps[i].String())
		fmt.Printf("Completion: %v\n", completions[i])
		fmt.Printf("---------------------------------------\n")
	}


	// 卖方投票
	// tx, receipt, err = sellerSession.VoteForTransaction(modelId, big.NewInt(0))
	// if err != nil {
	// 	log.Fatal(err)
	// }
	// fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	// fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	// fmt.Printf("Voted with seller %s\n", sellerSession.CallOpts.From.Hex())

	// 查询交易
	result, err = buyerSession.GetTransactionsByModel(modelId)
	if err != nil {
		log.Fatal(err)
	}
	sellers = result.Sellers
	buyers = result.Buyers
	amounts = result.Amounts
	timestamps = result.Timestamps
	completions = result.Completions
	fmt.Printf("【Transactions for model ID - query by buyer】: %s\n", modelId)
	for i := 0; i < len(sellers); i++ {
		fmt.Printf("Transaction %d:\n", i+1)
		fmt.Printf("Seller: %s\n", sellers[i].Hex())
		fmt.Printf("Buyer: %s\n", buyers[i].Hex())
		fmt.Printf("Amount: %s\n", amounts[i].String())
		fmt.Printf("Timestamp: %s\n", timestamps[i].String())
		fmt.Printf("Completion: %v\n", completions[i])
		fmt.Printf("---------------------------------------\n")
	}

	// 买方投票
	// tx, receipt, err = buyerSession.VoteForTransaction(modelId, big.NewInt(0))
	// if err != nil {
	// 	log.Fatal(err)
	// }
	// fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	// fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	// fmt.Printf("Voted with buyer %s\n", sellerSession.CallOpts.From.Hex())

	// 查询交易
	result, err = buyerSession.GetTransactionsByModel(modelId)
	if err != nil {
		log.Fatal(err)
	}
	sellers = result.Sellers
	buyers = result.Buyers
	amounts = result.Amounts
	timestamps = result.Timestamps
	completions = result.Completions
	fmt.Printf("【Transactions for model ID - query by buyer】: %s\n", modelId)
	for i := 0; i < len(sellers); i++ {
		fmt.Printf("Transaction %d:\n", i+1)
		fmt.Printf("Seller: %s\n", sellers[i].Hex())
		fmt.Printf("Buyer: %s\n", buyers[i].Hex())
		fmt.Printf("Amount: %s\n", amounts[i].String())
		fmt.Printf("Timestamp: %s\n", timestamps[i].String())
		fmt.Printf("Completion: %v\n", completions[i])
		fmt.Printf("---------------------------------------\n")
	}

	certainTx, err := buyerSession.GetTransaction(modelId, big.NewInt(0))
	fmt.Printf("Final test:\n")
	fmt.Printf("Seller: %s\n", certainTx.Seller.Hex())
	fmt.Printf("Buyer: %s\n", certainTx.Buyer.Hex())
	fmt.Printf("Amount: %d\n", certainTx.Amount.Int64())
	fmt.Printf("Timestamp: %s\n", certainTx.Timestamp.String())
	fmt.Printf("Completion: %v\n", certainTx.IsCompleted)
}