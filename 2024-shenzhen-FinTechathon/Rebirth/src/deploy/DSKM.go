package deploy

import (
	"bytes"
	"context"
	"crypto/ecdsa"
	"fmt"
	"github.com/ethereum/go-ethereum/accounts/abi"
	"github.com/ethereum/go-ethereum/accounts/abi/bind"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/ethereum/go-ethereum/ethclient"
	"io/ioutil"
	"math/big"
)

// DeployContract 部署DKSM合约
func DeployDKSM(client *ethclient.Client, privateKey *ecdsa.PrivateKey) common.Address {
	// 加载编译后的合约
	bytecode, err := ioutil.ReadFile("bin/DKSM.bin")
	if err != nil {
		fmt.Println("加载合约字节码失败：", err)
	}

	abiData, err := ioutil.ReadFile("abi/DKSM.abi")
	if err != nil {
		fmt.Println("加载合约 ABI 失败：", err)
	}

	parsedABI, err := abi.JSON(bytes.NewReader(abiData))
	if err != nil {
		fmt.Println("解析合约 ABI 失败：", err)
	}

	// 获取账户的nonce值
	publicKey := privateKey.Public()
	publicKeyECDSA, ok := publicKey.(*ecdsa.PublicKey)
	if !ok {
		fmt.Println("公钥转换为 ECDSA 失败")
	}

	fromAddress := crypto.PubkeyToAddress(*publicKeyECDSA)
	nonce, err := client.PendingNonceAt(context.Background(), fromAddress)
	if err != nil {
		fmt.Println("获取 nonce 失败：", err)
	}

	// 设置交易选项
	gasPrice, err := client.SuggestGasPrice(context.Background())
	if err != nil {
		fmt.Println("建议的 Gas 价格获取失败：", err)
	}

	chainID, err := client.NetworkID(context.Background())
	if err != nil {
		fmt.Println("获取链 ID 失败：", err)
	}

	auth, err := bind.NewKeyedTransactorWithChainID(privateKey, chainID)
	if err != nil {
		fmt.Println("创建交易签名器失败：", err)
	}

	auth.Nonce = big.NewInt(int64(nonce))
	auth.Value = big.NewInt(0)      // 不发送以太币
	auth.GasLimit = uint64(3000000) // 设置Gas限制
	auth.GasPrice = gasPrice

	// 部署合约
	address, tx, _, err := bind.DeployContract(auth, parsedABI, common.FromHex(string(bytecode)), client)
	if err != nil {
		fmt.Println("部署合约失败：", err)
	}

	// 等待交易执行
	receipt, err := bind.WaitMined(context.Background(), client, tx)
	if err != nil {
		fmt.Println("等待交易执行失败：", err)
	}

	// 根据交易状态判断成功或失败
	if receipt.Status == 1 {
		fmt.Println("DKSM 部署成功")
	} else {
		fmt.Println("DKSM 部署失败")
	}

	fmt.Printf("DKSM 合约已部署到: %s\n", address.Hex())
	fmt.Printf("交易哈希: %s\n", tx.Hash().Hex())

	// 返回合约地址
	return address
}
