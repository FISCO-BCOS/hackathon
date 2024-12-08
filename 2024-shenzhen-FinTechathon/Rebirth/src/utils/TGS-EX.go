package utils

import (
	"DKSM/deploy"
	"DKSM/user"
	"context"
	"crypto/ecdsa"
	"crypto/rand"
	"crypto/sha256"
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

func Receipt_TGS_REP(Client user.User_True, KDC_Client_TGS_ForClient string, Server_ID string, DKSM common.Address,
	client_ETH *ethclient.Client, Client_ETH_PrivateKey *ecdsa.PrivateKey) (string, string) {
	fmt.Println("--------------------11. 客户端获取返回的 TGS_REP！---------------------------")
	ClientAuth, instance := deploy.Foundry(client_ETH, Client_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 调用合约的 C_Get_TGS_REP 方法获取加密的 TGS_REP 和时间戳
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: ClientAuth.From}, &result, "C_Get_TGS_REP", Client.ID, Server_ID)
	if err != nil {
		log.Fatalf("Failed to get TGS_REP from contract: %v", err)
	}
	TGS_REP := result[0].(string)
	fmt.Println("客户端接受到 KDC 回复的 TGS_REP：", TGS_REP)
	TS_tgsrep := result[1].(*big.Int).Int64()

	// 解密 TGS_REP，解析内容
	TGS_REP_Content := AES_Decrypt(KDC_Client_TGS_ForClient, TGS_REP)
	fmt.Println("客户端使用之前保存的 KDC_Client_TGS_ForClient 通行凭证解密 TGS_REP 信息：", TGS_REP_Content)

	// 解析 Kc_v 和 ST
	TGS_REP_Parts := strings.Split(TGS_REP_Content, ",")
	Client_Server_AESKey := TGS_REP_Parts[2]
	ST := TGS_REP_Parts[3]
	fmt.Println("KDC 回复信息 AS_REP 解析成功，时间戳：", TGS_REP_Parts[4])

	// 获取当前时间戳
	localtime := time.Now().Unix()
	fmt.Println("区块时间戳：", TS_tgsrep)
	fmt.Println("当前系统时间：", localtime)
	intValue, _ := strconv.ParseInt(TGS_REP_Parts[4], 10, 64)

	// 检查时间戳差异
	if localtime-intValue < 300 {
		fmt.Println("KDC 发送 TGS_REP 跟客户端处理时间相差 300 秒内，允许通过!")
		// 返回解密后的 AS_REP
		fmt.Println("客户端提取 TGS_REP 中 KDC 生成的客户端跟服务器间的通讯凭证：", Client_Server_AESKey)
		fmt.Println("客户端提取被 KDC 使用服务器 AES 密钥加密的 ST 密文：", ST)
		return Client_Server_AESKey, ST
	} else {
		fmt.Println("发送 ASREP 跟处理时间超过 300 秒，不允许通过!")
		return "", ""
	}
}

func TGS_Set_TGS_REP(KDC user.User_True, KDC_Client_TGS string, Client_ID string, Server_ID string, address common.Address,
	client_ETH *ethclient.Client, KDC_ETH_PrivateKey *ecdsa.PrivateKey) {
	fmt.Println("--------------------10. KDC 设置 TGS_REP！---------------------------")
	KdcAuth, instance := deploy.Foundry(client_ETH, KDC_ETH_PrivateKey, address, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 获取客户端的Nonce值
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: KdcAuth.From}, &result, "get_nonce", KDC.ID)
	if err != nil {
		fmt.Println("获取", KDC.ID, "的合约 nonce 失败：", err)
	}
	nonce := result[0].(*big.Int) // 将结果转换为*big.Int

	// 从合约中获取 CKv
	var result1 []interface{}
	err = instance.Call(&bind.CallOpts{From: KdcAuth.From}, &result1, "TGS_Get_CKv", KDC.ID, Server_ID)
	if err != nil {
		log.Fatalf("Failed to get CKv for TGS %s and service %s: %v", KDC.ID, Server_ID, err)
	}
	Server_Key_AES_EncryptByKDC := result1[0].(string)
	fmt.Println("KDC 获取在智能合约中存放着被 KDC AES 对称加密后的服务器的 AES 密钥：")

	// 解密 CKv 获取服务密钥 Kv
	Server_Key_AES := AES_Decrypt(AES_Key_Generate(KDC.Password), Server_Key_AES_EncryptByKDC)
	fmt.Println("KDC 通过自身密码生成 KDC AES 对称加密密钥后，解密得到服务器的 AES 密钥：", Server_Key_AES)

	// 生成客户端和服务之间的会话密钥 Kc_v
	// 生成随机的 Kc_tgs 密钥
	Client_Server_AESKey_Bytes := make([]byte, 16)
	_, err = rand.Read(Client_Server_AESKey_Bytes)
	if err != nil {
		fmt.Println("生成 Client_Server 通行凭证失败：", err)
	}
	Client_Server_AESKey_Hex := hex.EncodeToString(Client_Server_AESKey_Bytes)
	Client_Server_AESKey := AES_Key_Generate(Client_Server_AESKey_Hex)
	fmt.Println("KDC 随机生成客户端跟服务器间的通讯凭证 Client_Server_TGS：", Client_Server_AESKey)

	// 获取当前时间戳
	TimeStamp := time.Now().Unix()
	fmt.Printf("KDC 截取当前系统的时间戳：%d\n", TimeStamp)

	// 生成加密的服务票据 ST
	//lifetime := "2024-09-01"
	ST := AES_Encrypt(Server_Key_AES, fmt.Sprintf("%s,%s,%s", Client_ID, Server_ID, Client_Server_AESKey))
	fmt.Println("KDC 打包 ST 明文信息（Client_ID,Server_ID,Client_Server_TGS）：", fmt.Sprintf("%s,%s,%s", Client_ID, Server_ID, Client_Server_AESKey))
	fmt.Println("KDC 用服务器的 AES 密钥加密 ST，得到 ST 密文：", ST)

	// 生成加密的 TGS 响应 TGS_REP
	TGS_REP := AES_Encrypt(KDC_Client_TGS, fmt.Sprintf("%s,%s,%s,%s,%d", Client_ID, Server_ID, Client_Server_AESKey, ST, TimeStamp))
	fmt.Println("KDC 打包 TGS_REP 信息（Client_ID,Server_ID,Client_Server_TGS,ST密文,TimeStamp）：", fmt.Sprintf("%s,%s,%s,%s,%d", Client_ID, Server_ID, Client_Server_AESKey, ST, TimeStamp))
	fmt.Println("KDC 使用之前保存的 KDC_Client_TGS_ForKDC 通行凭证加密 TGS_REP 信息，最终发送：", TGS_REP)

	// 使用 Transact 方法调用合约的 TGS_Set_TGS_REP 函数
	tx, err := instance.Transact(KdcAuth, "TGS_Set_TGS_REP", Client_ID, KDC.ID, Server_ID, TGS_REP, nonce)
	if err != nil {
		log.Fatalf("Failed to set TGS_REP: %v", err)
	}

	// 等待交易执行
	receipt, err := bind.WaitMined(context.Background(), client_ETH, tx)
	if err != nil {
		log.Fatalf("Failed to wait for transaction mining: %v", err)
	}
	// 根据交易状态判断成功或失败
	if receipt.Status == 1 {
		fmt.Println("KDC 已发送 TGS-REP 回复信息：", tx.Hash().Hex())
	} else {
		fmt.Println("KDC 发送 TGS-REP 失败")
	}

	// 打印交易哈希
	fmt.Printf("TGS-REP 已发送: %s\n", tx.Hash().Hex())
}

func Receipt_TGS_REQ(KDC user.User_True, KDC_Client_TGS string, Client_ID string, address common.Address,
	client_ETH *ethclient.Client, KDC_ETH_PrivateKey *ecdsa.PrivateKey) {
	fmt.Println("--------------------9. KDC 获取 TGS_REQ！---------------------------")
	KdcAuth, instance := deploy.Foundry(client_ETH, KDC_ETH_PrivateKey, address, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 调用合约的 TGS_Get_TGS_REQ 方法
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: KdcAuth.From}, &result, "TGS_Get_TGS_REQ", Client_ID, KDC.ID)
	if err != nil {
		log.Fatalf("Failed to get TGS_REQ from contract: %v", err)
	}

	// 解析返回结果
	TGS_REQ := result[0].(string)             // 数据包
	TS_tgsreq := result[1].(*big.Int).Int64() // 区块时间戳
	fmt.Println("KDC 接收到客户端发送的 TGS_REQ：", TGS_REQ)

	// 解析 TGS_REQ 内容
	TGS_REQ_Parts := strings.Split(TGS_REQ, ",")
	//Client_ID = parts[0]
	Server_ID := TGS_REQ_Parts[1]
	fmt.Println("KDC 提取到客户端想要请求服务的服务器为：", Server_ID)

	Authenticator1_EncryptBy_KDC_Client_TGS := TGS_REQ_Parts[3]
	fmt.Println("KDC 获取客户端使用 KDC_Client_TGS 通行凭证加密身份认证信息：", Authenticator1_EncryptBy_KDC_Client_TGS)
	Authenticator1 := AES_Decrypt(KDC_Client_TGS, Authenticator1_EncryptBy_KDC_Client_TGS)
	fmt.Println("KDC 使用先前保存的 KDC_Client_TGS_ForKDC 解密身份验证信息：", Authenticator1)
	Authenticator1_Parts := strings.Split(Authenticator1, ",")
	fmt.Println("客户端服务请求 TGS_REQ 解析成功，KDC 用 KDC_Client_TGS 通行凭证解密时间戳：", Authenticator1_Parts[2])
	// 获取当前时间戳
	localtime := time.Now().Unix()
	fmt.Println("区块时间戳：", TS_tgsreq)
	fmt.Println("当前系统时间：", localtime)
	// 将字符串转换为 int64
	intValue, _ := strconv.ParseInt(Authenticator1_Parts[2], 10, 64)

	// 检查时间戳是否在300秒内
	if localtime-intValue < 300 {
		fmt.Println("客户端发送 TGS_REQ 跟 KDC 处理时间相差 300 秒内，允许通过!")
		// 验证 TGT_content 是否与期望值匹配
		// 解密 TGT
		TGT_EncryptBy_KDC_Client_TGS := TGS_REQ_Parts[2]
		fmt.Println("KDC 获取客户端使用 KDC_Client_TGS 通行凭证加密 TGT 密文：", TGT_EncryptBy_KDC_Client_TGS)
		TGT := AES_Decrypt(KDC_Client_TGS, TGT_EncryptBy_KDC_Client_TGS)
		fmt.Println("KDC 使用先前保存的 KDC_Client_TGS_ForKDC 解密 TGT 密文的第一层加密：", TGT)
		TGT_content := AES_Decrypt(AES_Key_Generate(KDC.Password), TGT)
		fmt.Println("KDC 用自己的密码生成 AES 密钥解密 TGT 密文的第二层加密：", TGT_content)
		expectedValues := []string{Client_ID, KDC.ID, KDC_Client_TGS}
		fmt.Println("KDC 创建验证信息（Client_ID,KDC_ID,KDC_Client_TGS）：", fmt.Sprintf("%s,%s,%s", expectedValues[0], expectedValues[1], expectedValues[2]))
		TGT_parts := strings.Split(TGT_content, ",")
		if TGT_parts[0] == expectedValues[0] && TGT_parts[1] == expectedValues[1] && TGT_parts[2] == expectedValues[2] {
			fmt.Println("KDC 验证 TGS_REQ 数据正确")
		} else {
			fmt.Println("TGS_REQ 解包失败")
		}
	} else {
		fmt.Println("发送 TGS_REQ 跟处理时间超过 300 秒，不允许通过!")
	}
}

// C_Set_TGS_REQ 客户端设置 TGS-EX 请求
func C_Set_TGS_REQ(Client user.User_True, AS_REP string, KDC_ID string, Server_ID string, DKSM common.Address,
	client_ETH *ethclient.Client, Client_ETH_PrivateKey *ecdsa.PrivateKey) {
	fmt.Println("--------------------8. 客户端设置 TGS_REQ！---------------------------")
	ClientAuth, instance := deploy.Foundry(client_ETH, Client_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 先切割出来 KDC_Client_TGS 凭证和 TGT
	AS_REP_List := strings.Split(AS_REP, ",")
	KDC_Client_TGS := AS_REP_List[0]
	TGT := AS_REP_List[3]

	// 获取客户端的Nonce值
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: ClientAuth.From}, &result, "get_nonce", Client.ID)
	if err != nil {
		fmt.Println("获取", Client.ID, "的合约 nonce 失败：", err)
	}
	nonce := result[0].(*big.Int) // 将结果转换为*big.Int

	// 获取当前时间戳
	TimeStamp := time.Now().Unix()
	fmt.Printf("客户端截取当前系统的时间戳：%d\n", TimeStamp)

	// 生成 packet_TGSREQ
	fmt.Println("客户端提取 AS_REP 的第三段信息 TGT 密文（被 KDC 使用 KDC 的 AES 密钥加密过的）：", TGT)
	TGT_EncryptBy_KDC_Client_TGS := AES_Encrypt(KDC_Client_TGS, TGT)
	fmt.Println("客户端使用 KDC_Client_TGS 通行凭证加密 TGT 密文：", TGT_EncryptBy_KDC_Client_TGS)
	packet_TGSREQ := fmt.Sprintf("%s,%s,%s", Client.ID, Server_ID, TGT_EncryptBy_KDC_Client_TGS)
	fmt.Println("客户端打包 TGS_REQ 信息（Client_ID,Server_ID,TGT_EncryptBy_KDC_Client_TGS）：", packet_TGSREQ)

	// 计算 CheckSum
	hash := sha256.New()
	hash.Write([]byte(packet_TGSREQ))
	CheckSum := fmt.Sprintf("%x", hash.Sum(nil))
	fmt.Println("客户端对 TGS_REQ 做哈希计算 CheckSum：", CheckSum)

	// 生成 Authenticator1
	Authenticator1 := AES_Encrypt(KDC_Client_TGS, fmt.Sprintf("%s,%s,%d", Client.ID, CheckSum, TimeStamp))
	fmt.Println("客户端打包身份认证信息（Client_ID,CheckSum,TimeStamp）：", fmt.Sprintf("%s,%s,%d", Client.ID, CheckSum, TimeStamp))
	fmt.Println("客户端使用 KDC_Client_TGS 通行凭证加密身份认证信息：", Authenticator1)

	// 生成 TGS_REQ
	TGS_REQ := fmt.Sprintf("%s,%s", packet_TGSREQ, Authenticator1)
	fmt.Println("客户端最终发送的 TGS_REQ 请求信息为：", TGS_REQ)

	// 使用 Transact 方法调用合约的 C_Set_TGS_REQ 函数
	tx, err := instance.Transact(ClientAuth, "C_Set_TGS_REQ", Client.ID, KDC_ID, TGS_REQ, nonce)
	if err != nil {
		fmt.Println("合约 C_Set_TGS_REQ 无法调用：", err)
	}

	// 等待交易执行
	receipt, err := bind.WaitMined(context.Background(), client_ETH, tx)
	if err != nil {
		fmt.Println("等待交易执行失败：", err)
	}
	// 根据交易状态判断成功或失败
	if receipt.Status == 1 {
		fmt.Println("客户端发送 TGS-REQ：", tx.Hash().Hex())
	} else {
		fmt.Println("客户端发送 TGS-REQ 失败")
	}
}
