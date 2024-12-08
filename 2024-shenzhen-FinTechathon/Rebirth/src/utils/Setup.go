package utils

import (
	"DKSM/deploy"
	"DKSM/user"
	"context"
	"crypto/ecdsa"
	"fmt"
	"github.com/ethereum/go-ethereum/accounts/abi/bind"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/ethclient"
	"strings"
)

// Setup 初始化设置
func Setup(KDC user.User_True, Client user.User_True, Server user.User_True,
	client_ETH *ethclient.Client, KDC_ETH_PrivateKey *ecdsa.PrivateKey) common.Address {
	fmt.Println("--------------------初始化开始！---------------------")
	KDC_Deploy := user.User{
		ID:        KDC.ID,
		Key:       "",
		Addr:      KDC.Addr,
		PL:        user.KDC_PL,
		Attribute: nil,
	}
	Client_Deploy := user.User{
		ID:        Client.ID,
		Key:       "",
		Addr:      Client.Addr,
		PL:        "",
		Attribute: user.Client_Attribute,
	}
	Server_Deploy := user.User{
		ID:        Server.ID,
		Key:       "",
		Addr:      Server.Addr,
		PL:        "",
		Attribute: nil,
	}

	KDC_Key_AES := AES_Key_Generate(KDC.Password)       // 生成 KDC 的 AES 密钥
	Client_Key_AES := AES_Key_Generate(Client.Password) // 生成客户端的 AES 密钥
	Server_Key_AES := AES_Key_Generate(Server.Password) // 生成服务器的 AES 密钥
	fmt.Println("参与者的对称加密密钥如下: ")
	fmt.Println("KDC_Key_AES：", KDC_Key_AES)
	fmt.Println("Client_Key_AES：", Client_Key_AES)
	fmt.Println("Server_Key_AES：", Server_Key_AES)
	fmt.Println("------------------------------------------------")

	fmt.Println("信息加密开始！")
	for index, value := range Client_Deploy.Attribute { // 客户端密钥加密自身身份
		Client_Deploy.Attribute[index] = AES_Encrypt(Client_Key_AES, value)
	}
	Client_Deploy.Key = AES_Encrypt(KDC_Key_AES, Client_Key_AES) // KDC 密钥加密客户端密钥
	Server_Deploy.Key = AES_Encrypt(KDC_Key_AES, Server_Key_AES) // KDC 密钥加密服务器密钥

	fmt.Println("信息加密完成！在这里客户端的身份策略被加密，客户端密钥被加密，服务器密钥被加密。")
	fmt.Println("------------------------------------------------")

	Users_Deploy := []user.User{KDC_Deploy, Client_Deploy, Server_Deploy}

	// 部署合约（Go版本的实现）
	fmt.Println("--------------------1. 部署合约！---------------------------")
	DKSM := deploy.DeployDKSM(client_ETH, KDC_ETH_PrivateKey) // 返回 DKSM 部署成功的合约地址
	fmt.Println("--------------------2. 设置合约用户参数！---------------------------")
	for _, Value := range Users_Deploy {
		KdcAuth, instance := deploy.Foundry(client_ETH, KDC_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例
		userAttribute := strings.Join(Value.Attribute, ",")
		tx, err := instance.Transact(KdcAuth, "setup", common.HexToAddress(Value.Addr), Value.ID, Value.Key, userAttribute, "")
		if err != nil {
			fmt.Println("上传", Value.ID, "信息失败")
		}

		// 等待交易执行
		receipt, err := bind.WaitMined(context.Background(), client_ETH, tx)
		if err != nil {
			fmt.Println("等待与合约交互失败：", err)
		}

		// 根据交易状态判断成功或失败
		if receipt.Status == 1 {
			fmt.Printf("用户 %s 设置成功\n", Value.ID)
		} else {
			fmt.Printf("用户 %s 设置失败\n", Value.ID)
		}
	}
	fmt.Println("用户参数设置完成！")

	return DKSM
}

func VerifySetup(KDC user.User_True, Client user.User_True, Server user.User_True, DKSM common.Address,
	client_ETH *ethclient.Client, KDC_ETH_PrivateKey *ecdsa.PrivateKey) {
	fmt.Println("--------------------3. 验证设置！---------------------------")
	KdcAuth, instance := deploy.Foundry(client_ETH, KDC_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 获取用户信息的内部函数
	getInfo := func(id string) {
		// 定义存储结果的切片
		var result []interface{}

		// 调用合约的getInfo方法
		err := instance.Call(&bind.CallOpts{From: KdcAuth.From}, &result, "getInfo", id)
		if err != nil {
			fmt.Println("获取", id, "信息失败：", err)
		}

		// 提取并打印结果
		userAddress := result[0].(common.Address)
		userID := result[1].(string)
		userKey := result[2].(string)
		userPL := result[3].(string)
		userLifetime := result[4].(string)

		fmt.Printf("User Address: %s\nID: %s\nKey: %s\nPL: %s\nLifetime: %s\n",
			userAddress.Hex(),
			userID,
			userKey,
			userPL,
			userLifetime)
	}

	// 验证KDC信息
	fmt.Println("----------------------------- KDC 的全部信息：-----------------------------")
	getInfo(KDC.ID)

	// 验证客户端信息
	fmt.Println("----------------------------- Client 的全部信息：-----------------------------")
	getInfo(Client.ID)

	// 验证服务器信息
	fmt.Println("----------------------------- Server 的全部信息：-----------------------------")
	getInfo(Server.ID)
}
