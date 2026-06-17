package utils

import (
	"DKSM/deploy"
	"DKSM/user"
	"context"
	"crypto/ecdsa"
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"github.com/ethereum/go-ethereum/accounts/abi/bind"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/ethclient"
	"log"
	"math/big"
	"strconv"
	"strings"
	"time"
)

// Receipt_AS_REP 客户端接收 AS_REP 信息并处理
func Receipt_AS_REP(Client user.User_True, KDC_ID string, DKSM common.Address,
	client_ETH *ethclient.Client, Client_ETH_PrivateKey *ecdsa.PrivateKey) (string, string) {
	fmt.Println("--------------------7. 客户端接收处理 AS_REP！---------------------------")
	ClientAuth, instance := deploy.Foundry(client_ETH, Client_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 定义存储结果的切片
	var result_Client_INFO []interface{}

	// 调用合约的getInfo方法
	err := instance.Call(&bind.CallOpts{From: ClientAuth.From}, &result_Client_INFO, "getInfo", Client.ID)
	if err != nil {
		fmt.Println("获取", Client.ID, "信息失败：", err)
	}

	// 获取用户的身份属性
	Client_Attribute_ETH := result_Client_INFO[3].(string)
	fmt.Println("客户端从智能合约中获取自身的身份属性（这些属性在初始化系统时被 KDC 用客户端的 AES 密钥加密过）：", Client_Attribute_ETH)

	// 切割身份属性字符串，并用自身 AES 密钥解密
	Client_AttributeList := strings.Split(Client_Attribute_ETH, ",")
	Client_Key_AES := AES_Key_Generate(Client.Password)
	for index, attribute := range Client_AttributeList {
		Client_AttributeList[index] = AES_Decrypt(Client_Key_AES, attribute)
	}
	fmt.Println("客户端使用自己的密码生成 AES 密钥后解密获得自身身份属性：", Client_AttributeList[0]+","+Client_AttributeList[1]+","+Client_AttributeList[2])

	// 调用合约的 C_Get_AS_REP 方法，获取加密的 AS_REP 和时间戳
	var result []interface{}
	err = instance.Call(&bind.CallOpts{From: ClientAuth.From}, &result, "C_Get_AS_REP", Client.ID, KDC_ID)
	if err != nil {
		fmt.Println("客户端获取 KDC 发送的 AS_REP 失败：", err)
	}

	// 解析返回结果
	AS_REP := result[0].(string)
	fmt.Println("客户端接收到 KDC 发送的 AS_REP：")
	fmt.Println(AS_REP)
	TS_asrep := result[1].(*big.Int)

	// 解密 AS_REP
	Plain_AS_REP := ABE_Decrypt(AS_REP, Client_AttributeList)
	fmt.Println("客户端使用自身身份信息解密得到 AS_REP 明文信息：", Plain_AS_REP)

	Plain_AS_REP_List := strings.Split(Plain_AS_REP, ",")

	fmt.Println("KDC 回复信息 AS_REP 解析成功，客户端使用自身身份属性解密时间戳：", Plain_AS_REP_List[2])
	// 获取当前时间戳
	localtime := time.Now().Unix()
	fmt.Println("区块时间戳：", TS_asrep)
	fmt.Println("当前客户端处理 AS_REP 系统时间：", localtime)
	intValue, _ := strconv.ParseInt(Plain_AS_REP_List[2], 10, 64)

	// 检查时间戳差异
	if localtime-intValue < 300 {
		fmt.Println("KDC 发送 AS_REP 跟客户端处理时间相差 300 秒内，允许通过!")
		//// 返回解密后的 AS_REP
		//fmt.Printf("KDC 发送 AS_EX 回复的信息: %s\n", Plain_AS_REP)

		// 先切割出来 KDC_Client_TGS 凭证和 TGT
		AS_REP_List := strings.Split(Plain_AS_REP, ",")
		KDC_Client_TGS := AS_REP_List[0]
		fmt.Println("客户端已接收到 KDC_Client_TGS 通行凭证（注意保存，客户端在发送 TGS_REQ 时需要使用，本系统使用了一个变量 KDC_Client_TGS_ForClient 存储）：", KDC_Client_TGS)
		fmt.Println("客户端已经完成跟 KDC 之间的身份认证，并完成密钥交换，之后通讯使用 KDC_Client_TGS 通讯凭证（AES 密钥）")
		return Plain_AS_REP, KDC_Client_TGS
	} else {
		fmt.Println("发送 ASREP 跟处理时间超过 300 秒，不允许通过!")
		return "", ""
	}
}

// AS_Set_AS_REP KDC 返回客户端的注册结果
func AS_Set_AS_REP(KDC user.User_True, Client_ID string, address common.Address,
	client_ETH *ethclient.Client, KDC_ETH_PrivateKey *ecdsa.PrivateKey) string {
	fmt.Println("--------------------6. KDC 设置客户端注册结果信息 AS_REP！---------------------------")
	KdcAuth, instance := deploy.Foundry(client_ETH, KDC_ETH_PrivateKey, address, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 获取客户端的Nonce值
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: KdcAuth.From}, &result, "get_nonce", KDC.ID)
	if err != nil {
		fmt.Println("获取", KDC.ID, "的合约 nonce 失败：", err)
	}
	nonce := result[0].(*big.Int) // 将结果转换为*big.Int

	// 生成随机的客户端通行凭证密钥
	KDC_Client_TGS_Bytes := make([]byte, 16)
	_, err = rand.Read(KDC_Client_TGS_Bytes)
	if err != nil {
		fmt.Println("生成 KDC_Client_TGS 通行凭证失败：", err)
	}
	KDC_Client_TGS_Seed := hex.EncodeToString(KDC_Client_TGS_Bytes)
	KDC_Client_TGS := AES_Key_Generate(KDC_Client_TGS_Seed)
	fmt.Println("KDC 随机生成 KDC_Client_TGS 通行凭证（注意保存，KDC 在接收处理 TGS_REQ 时需要使用，本系统使用了一个变量 KDC_Client_TGS_ForKDC 存储）：", KDC_Client_TGS)

	// 获取当前时间戳
	TimeStamp := time.Now().Unix()
	fmt.Printf("KDC 截取当前系统的时间戳：%d\n", TimeStamp)

	// KDC 使用 AES 加密 TGT 信息
	TGTContent := fmt.Sprintf("%s,%s,%s", Client_ID, KDC.ID, KDC_Client_TGS)
	fmt.Println("KDC 打包 TGT 明文信息（Client_ID,KDC_ID,KDC_Client_TGS）：", TGTContent)
	TGT := AES_Encrypt(AES_Key_Generate(KDC.Password), TGTContent)
	fmt.Println("KDC 使用自己的密码生成 AES 对称加密密钥后加密 TGT 明文，密文信息：", TGT)

	// 使用 ABE 加密 AS_REP 信息，这样可以自动化验证客户端身份信息
	ASRepContent := fmt.Sprintf("%s,%s,%d,%s", KDC_Client_TGS, KDC.ID, TimeStamp, TGT)
	fmt.Println("KDC 打包 AS_REP 信息（KDC_Client_TGS,KDC_ID,TimeStamp,TGT）：", ASRepContent)
	AS_REP := ABE_Encrypt(ASRepContent, user.KDC_PL)
	fmt.Println("KDC 使用 ABE 属性基加密，访问策略为：", user.KDC_PL)
	fmt.Println("KDC 最终发送的 AS_REP 回复信息为：")
	fmt.Println(AS_REP)

	// 调用合约的 AS_Set_AS_REP 函数
	tx, err := instance.Transact(KdcAuth, "AS_Set_AS_REP", Client_ID, KDC.ID, AS_REP, nonce)
	if err != nil {
		fmt.Println("合约 AS_Set_AS_REP 无法调用：", err)
	}
	// 等待交易执行
	receipt, err := bind.WaitMined(context.Background(), client_ETH, tx)
	if err != nil {
		fmt.Println("等待交易执行失败：", err)
	}
	// 根据交易状态判断成功或失败
	if receipt.Status == 1 {
		fmt.Println("KDC 针对客户端的 AS_REP 回复信息已发送：", tx.Hash().Hex())
	} else {
		fmt.Println("KDC 的 AS_REP 设置失败")
	}

	//fmt.Println(KDC.ID, "已发送注册回复信息：", tx.Hash().Hex())
	return KDC_Client_TGS
}

/*
Receipt_AS_REQ KDC 获取客户端的注册请求
验证是否通过后会获取到客户端发送来的 AES 密钥
*/
func Receipt_AS_REQ(KDC user.User_True, Client_ID string, address common.Address,
	client_ETH *ethclient.Client, KDC_ETH_PrivateKey *ecdsa.PrivateKey) {
	fmt.Println("--------------------5. KDC 获取 AS_REQ！---------------------------")
	KdcAuth, instance := deploy.Foundry(client_ETH, KDC_ETH_PrivateKey, address, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 调用合约的 AS_Get_AS_REQ 函数
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: KdcAuth.From}, &result, "AS_Get_AS_REQ", Client_ID, KDC.ID)
	if err != nil {
		log.Fatalf("Failed to get AS_REQ: %v", err)
	}

	// 提取返回的 AS_REQ, TS_asreq, CKc, CKas
	AS_REQ := result[0].(string) // 客户端ID、KDC ID、客户端加密时间戳
	fmt.Println("KDC 接收到客户端发送的 AS_REQ：", AS_REQ)
	TS_asreq := result[1].(*big.Int).Int64()          // 区块时间戳
	Client_Key_AES_EncryptByKDC := result[2].(string) // 客户端被 KDC AES 加密后的密钥
	fmt.Println("KDC 获取在智能合约中存放着被 KDC AES 对称加密后的客户端的 AES 密钥：", Client_Key_AES_EncryptByKDC)

	// 提取 Kkdc 并进行 AES 解密
	KDC_Key_AES := AES_Key_Generate(KDC.Password)
	Client_Key_AES := AES_Decrypt(KDC_Key_AES, Client_Key_AES_EncryptByKDC)
	fmt.Println("KDC 通过自身密码生成 KDC AES 对称加密密钥后，解密得到客户端的 AES 密钥：", Client_Key_AES)

	// 从 AS_REQ 提取时间戳并解密
	ET := strings.Split(AS_REQ, ",")[2]
	msg := AES_Decrypt(Client_Key_AES, ET)
	if msg != "" {
		fmt.Println("客户端注册请求 AS_REQ 解析成功，KDC 使用客户端的 AES 密钥解密时间戳：", msg)
		// 获取当前时间戳
		localtime := time.Now().Unix()
		fmt.Println("区块时间戳：", TS_asreq)
		fmt.Println("当前 KDC 处理 AS_REQ 系统时间：", localtime)
		// 将字符串转换为 int64
		intValue, _ := strconv.ParseInt(msg, 10, 64)

		// 检查时间戳是否在300秒内
		if localtime-intValue < 300 {
			fmt.Println("客户端发送 AS_REQ 跟 KDC 处理时间相差 300 秒内，允许通过!")
		} else {
			fmt.Println("发送 ASREQ 跟处理时间超过 300 秒，不允许通过!")
		}
	} else {
		fmt.Println("客户端注册请求 AS_REQ 解析失败！")
	}
}

/*
	C_Set_AS_REQ

客户端向 AS 发送注册信息，包含三段内容：
1. 客户端的 ID
2. KDC 的ID
3. ET 被客户端加密的时间戳
*/
func C_Set_AS_REQ(Client user.User_True, KDC_ID string, DKSM common.Address,
	client_ETH *ethclient.Client, Client_ETH_PrivateKey *ecdsa.PrivateKey) {
	fmt.Println("--------------------4. 客户端发送 AS_REQ！---------------------------")
	ClientAuth, instance := deploy.Foundry(client_ETH, Client_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 获取客户端的Nonce值
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: ClientAuth.From}, &result, "get_nonce", Client.ID)
	if err != nil {
		fmt.Println("获取", Client.ID, "的合约 nonce 失败：", err)
	}
	nonce := result[0].(*big.Int) // 将结果转换为*big.Int

	// 获取当前时间戳
	timeStamp := time.Now().Unix()
	fmt.Printf("客户端截取当前系统的时间戳：%d\n", timeStamp)

	// 生成客户端的AES密钥
	Client_key_AES := AES_Key_Generate(Client.Password)

	// 客户端 AES 加密时间戳
	ET := AES_Encrypt(Client_key_AES, fmt.Sprintf("%d", timeStamp))
	fmt.Println("客户端使用自己的密码生成 AES 对称加密密钥后加密时间戳 ET：", ET)

	// 生成 AS_REQ 消息，客户端ID、KDC ID、加密时间戳
	AS_REQ := Client.ID + "," + KDC_ID + "," + ET
	fmt.Println("客户端打包 AS_REQ 发送（Client_ID,KDC_ID,ET）：", AS_REQ)

	// 调用合约设置 AS_REQ
	tx, err := instance.Transact(ClientAuth, "C_Set_AS_REQ", Client.ID, KDC_ID, AS_REQ, nonce)
	if err != nil {
		fmt.Println("合约 C_Set_AS_REQ 无法调用：", err)
	}

	// 等待交易执行
	receipt, err := bind.WaitMined(context.Background(), client_ETH, tx)
	if err != nil {
		fmt.Println("等待交易执行失败：", err)
	}

	// 根据交易状态判断成功或失败
	if receipt.Status == 1 {
		//fmt.Println("Client0 的 AS_REQ 设置成功")
		fmt.Println(Client.ID, "的注册信息已发送：", tx.Hash().Hex())
	} else {
		fmt.Println("Client0 的 AS_REQ 设置失败")
	}
}
