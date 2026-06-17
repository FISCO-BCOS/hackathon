package deploy

import (
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
	"strings"
)

// Foundry 函数返回智能合约实例和 auth 对象
func Foundry(client *ethclient.Client, privateKey *ecdsa.PrivateKey, contractAddress common.Address, abiPath string) (*bind.TransactOpts, *bind.BoundContract) {
	// 加载ABI文件
	parsedABI, err := LoadABI(abiPath)
	if err != nil {
		fmt.Println("加载合约 ABI 失败：", err)
		return nil, nil
	}

	// 获取链ID
	chainID, err := client.NetworkID(context.Background())
	if err != nil {
		fmt.Println("获取当前链的 ID 失败：", err)
		return nil, nil
	}

	// 创建交易签名器
	auth, err := bind.NewKeyedTransactorWithChainID(privateKey, chainID)
	if err != nil {
		fmt.Println("创建交易签名器失败：", err)
		return nil, nil
	}

	// 获取Nonce值
	fromAddress := crypto.PubkeyToAddress(privateKey.PublicKey)
	nonce, err := client.PendingNonceAt(context.Background(), fromAddress)
	if err != nil {
		fmt.Println("获取当前账户的交易 nonce 失败：", err)
		return nil, nil
	}

	// 设置交易选项
	auth.Nonce = big.NewInt(int64(nonce))
	auth.Value = big.NewInt(0)       // 发送0 ETH
	auth.GasLimit = uint64(10000000) // 设置Gas限制
	gasPrice, err := client.SuggestGasPrice(context.Background())
	if err != nil {
		fmt.Println("获取当前交易的 gas 费用失败：", err)
		return nil, nil
	}
	auth.GasPrice = gasPrice

	// 创建智能合约实例
	instance := bind.NewBoundContract(contractAddress, parsedABI, client, client, client)

	return auth, instance
}

// LoadABI 加载ABI文件的辅助函数
func LoadABI(abiPath string) (abi.ABI, error) {
	abiData, err := ioutil.ReadFile(abiPath)
	if err != nil {
		return abi.ABI{}, err
	}

	parsedABI, err := abi.JSON(strings.NewReader(string(abiData)))
	if err != nil {
		return abi.ABI{}, err
	}

	return parsedABI, nil
}
