package main

import (
    "fmt"
    "log"
	"math/big"

    "github.com/FISCO-BCOS/go-sdk/client"
    "github.com/FISCO-BCOS/go-sdk/conf"
    "github.com/FISCO-BCOS/go-sdk/auth"
    "github.com/ethereum/go-ethereum/common"
)

func main() {
    // 解析配置文件
	configs, err := conf.ParseConfigFile("config.toml")
	if err != nil {
		log.Fatal(err)
	}
	config := &configs[0]	
    contractAddress := common.HexToAddress("0x8eb4ade74b32802a2a23aD9e3fC887258c0DC53C")
    

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
	buyerinstance, err := auth.NewModelAuthorization(contractAddress, buyerClient)
    if err != nil {
        log.Fatal(err)
    }
	buyerSession := &auth.ModelAuthorizationSession{
		Contract: buyerinstance,
		CallOpts: *buyerClient.GetCallOpts(),
		TransactOpts: *buyerClient.GetTransactOpts(),
	}
	
	// 买方查询模型
	// 调用另一个合约，略

	// 买方创建授权请求
	// 以下为测试数据
	modelId := "testmodel1"
    seller := common.HexToAddress("0x83309d045a19c44dc3722d15a6abd472f95866ac") // 替换为卖家地址, 在查询模型时可以获取
    buyer := common.HexToAddress("0xc89979c91f21f1b01df59440e954533d6f7978aa")   // 替换为买家地址, 即当前用户
    tx, receipt, err := buyerSession.RequestAuthorization(modelId, seller, buyer)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	fmt.Printf("AuthRequest Created by buyer %s\n", buyerSession.CallOpts.From.Hex())

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
	sellerinstance, err := auth.NewModelAuthorization(contractAddress, sellerClient)
    if err != nil {
        log.Fatal(err)
    }
	sellerSession := &auth.ModelAuthorizationSession{
		Contract: sellerinstance,
		CallOpts: *sellerClient.GetCallOpts(),
		TransactOpts: *sellerClient.GetTransactOpts(),
	}
	
	// 卖方查询交易
	result, err := sellerSession.GetAuthorizationsByModel(modelId)
	if err != nil {
		log.Fatal(err)
	}
	sellers := result.Sellers
	buyers := result.Buyers
	timestamps := result.Timestamps
	auths := result.Auths
	usages := result.Usages
	fmt.Printf("【Auths for model ID - query by seller】: %s\n", modelId)
	for i := 0; i < len(sellers); i++ {
		fmt.Printf("AuthRequest %d:\n", i+1)
		fmt.Printf("Seller: %s\n", sellers[i].Hex())
		fmt.Printf("Buyer: %s\n", buyers[i].Hex())
		fmt.Printf("Timestamp: %s\n", timestamps[i].String())
		fmt.Printf("Auths: %v\n", auths[i])
		fmt.Printf("Usages: %v\n", usages[i])
		fmt.Printf("---------------------------------------\n")
	}


	// 卖方投票
	tx, receipt, err = sellerSession.VoteAuthorization(modelId, big.NewInt(0), true)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	fmt.Printf("Voted with seller %s\n", sellerSession.CallOpts.From.Hex())

	// 查询交易
	result, err = buyerSession.GetAuthorizationsByModel(modelId)
	if err != nil {
		log.Fatal(err)
	}
	sellers = result.Sellers
	buyers = result.Buyers
	timestamps = result.Timestamps
	auths = result.Auths
	usages = result.Usages
	fmt.Printf("【Auths for model ID - query by seller】: %s\n", modelId)
	for i := 0; i < len(sellers); i++ {
		fmt.Printf("AuthRequest %d:\n", i+1)
		fmt.Printf("Seller: %s\n", sellers[i].Hex())
		fmt.Printf("Buyer: %s\n", buyers[i].Hex())
		fmt.Printf("Timestamp: %s\n", timestamps[i].String())
		fmt.Printf("Auths: %v\n", auths[i])
		fmt.Printf("Usages: %v\n", usages[i])
		fmt.Printf("---------------------------------------\n")
	}

	// 买方投票
	tx, receipt, err = buyerSession.VoteAuthorization(modelId, big.NewInt(0), true)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("tx sent: %s\n", tx.Hash().Hex())
	fmt.Printf("transaction hash of receipt: %s\n", receipt.GetTransactionHash())
	fmt.Printf("Voted with buyer %s\n", sellerSession.CallOpts.From.Hex())

	// 查询交易
	result, err = buyerSession.GetAuthorizationsByModel(modelId)
	if err != nil {
		log.Fatal(err)
	}
	sellers = result.Sellers
	buyers = result.Buyers
	timestamps = result.Timestamps
	auths = result.Auths
	usages = result.Usages
	fmt.Printf("【Auths for model ID - query by seller】: %s\n", modelId)
	for i := 0; i < len(sellers); i++ {
		fmt.Printf("AuthRequest %d:\n", i+1)
		fmt.Printf("Seller: %s\n", sellers[i].Hex())
		fmt.Printf("Buyer: %s\n", buyers[i].Hex())
		fmt.Printf("Timestamp: %s\n", timestamps[i].String())
		fmt.Printf("Auths: %v\n", auths[i])
		fmt.Printf("Usages: %v\n", usages[i])
		fmt.Printf("---------------------------------------\n")
	}

	certainAuth, err := buyerSession.GetAuthorization(modelId, big.NewInt(0))
	fmt.Printf("Final test:\n")
	fmt.Printf("Seller: %s\n", certainAuth.Seller.Hex())
	fmt.Printf("Buyer: %s\n", certainAuth.Buyer.Hex())
	fmt.Printf("Timestamp: %s\n", certainAuth.Timestamp.String())
	fmt.Printf("Auths: %v\n", certainAuth.IsAuthorized)
	fmt.Printf("Usages: %v\n", certainAuth.IsUsed)

}