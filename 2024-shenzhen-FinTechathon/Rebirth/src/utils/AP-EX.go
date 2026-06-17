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

func Receipt_AP_REP(Client user.User_True, Server_ID string, Ksession string, DKSM common.Address,
	client_ETH *ethclient.Client, Client_ETH_PrivateKey *ecdsa.PrivateKey) {
	fmt.Println("--------------------15. 客户端接收回复信息 AP_REP！---------------------------")
	ClientAuth, instance := deploy.Foundry(client_ETH, Client_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 调用合约的 C_Get_AP_REP 方法获取 AP_REP 和时间戳
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: ClientAuth.From}, &result, "C_Get_AP_REP", Client.ID, Server_ID)
	if err != nil {
		log.Fatalf("Failed to get AP_REP: %v", err)
	}
	AP_REP := result[0].(string)
	fmt.Println("客户端获取服务器发送的 AP_REP 信息：", AP_REP)
	TS_aprep := result[1].(*big.Int).Int64()

	// 解密 AP_REP
	packet_APREP := AES_Decrypt(Ksession, AP_REP)
	fmt.Println("客户端使用自己之前生成的对称密钥解密 AP_REP 信息：", packet_APREP)

	packet_APREP_parts := strings.Split(packet_APREP, ",")
	fmt.Println("客户端服务请求 AP_REP 解析成功，时间戳解析结果：", packet_APREP_parts[2])
	// 获取当前时间戳
	localtime := time.Now().Unix()
	fmt.Println("区块时间戳：", TS_aprep)
	fmt.Println("当前系统时间：", localtime)
	// 将字符串转换为 int64
	intValue, _ := strconv.ParseInt(packet_APREP_parts[2], 10, 64)

	// 检查时间戳是否在300秒内
	if localtime-intValue < 300 {
		fmt.Println("服务器发送 AP_REP 跟客户端处理时间相差 300 秒内，允许通过!")
		fmt.Println("客户端与服务器成功建立连接！")
	} else {
		fmt.Println("发送 AP_REP 跟处理时间超过 300 秒，不允许通过!")
		return
	}

	// 检查 AP_REP 的内容
	if packet_APREP_parts[0] == Client.ID && packet_APREP_parts[1] == Server_ID {
		fmt.Println("AP_REP 检查正确，客户端与服务器已经完成密钥交换，可以安全进行通讯了！")
	} else {
		fmt.Println("APREP Check False")
	}
}

func S_Set_AP_REP(Server user.User_True, Client_ID string, Ksession string, DKSM common.Address,
	client_ETH *ethclient.Client, Server_ETH_PrivateKey *ecdsa.PrivateKey) {
	fmt.Println("--------------------14. 服务器设置回复信息 AP_REP！---------------------------")
	ServerAuth, instance := deploy.Foundry(client_ETH, Server_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 调用合约的 get_nonce 方法获取 Nonce 值
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: ServerAuth.From}, &result, "get_nonce", Server.ID)
	if err != nil {
		log.Fatalf("Failed to get nonce for server %s: %v", Server.ID, err)
	}
	nonce := result[0].(*big.Int) // 将结果转换为*big.Int

	// 获取当前时间戳
	TimeStamp := time.Now().Unix()
	fmt.Printf("服务器截取当前系统的时间戳：%d\n", TimeStamp)

	// 构建 AP_REP 数据包
	packet_APREP := fmt.Sprintf("%s,%s,%d", Client_ID, Server.ID, TimeStamp)
	fmt.Println("服务器打包 AP_REP 信息（Client_ID,Server_ID,TimeStamp）：", packet_APREP)
	AP_REP := AES_Encrypt(Ksession, packet_APREP)
	fmt.Println("服务器用获取到的对称密钥加密 AP_REP 后发送：", AP_REP)

	// 使用 Transact 方法调用合约的 S_Set_AP_REP 函数
	tx, err := instance.Transact(ServerAuth, "S_Set_AP_REP", Client_ID, Server.ID, AP_REP, nonce)
	if err != nil {
		log.Fatalf("Failed to set AP_REP: %v", err)
	}

	// 等待交易执行
	receipt, err := bind.WaitMined(context.Background(), client_ETH, tx)
	if err != nil {
		log.Fatalf("Failed to wait for transaction mining: %v", err)
	}

	// 根据交易状态判断成功或失败
	if receipt.Status == 1 {
		fmt.Println("AP-REP 发送成功")
	} else {
		fmt.Println("AP-REP 发送失败")
	}

	// 打印交易哈希
	fmt.Printf("AP-REP 已发送: %s\n", tx.Hash().Hex())
}

func Receipt_AP_REQ(Server user.User_True, Client_ID string, DKSM common.Address,
	client_ETH *ethclient.Client, Server_ETH_PrivateKey *ecdsa.PrivateKey) string {
	fmt.Println("--------------------13. 服务器接收客户端请求 AP_REQ！---------------------------")
	ServerAuth, instance := deploy.Foundry(client_ETH, Server_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例
	// 调用合约的 S_Get_AP_REQ 方法获取 AP_REQ 和时间戳
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: ServerAuth.From}, &result, "S_Get_AP_REQ", Client_ID, Server.ID)
	if err != nil {
		log.Fatalf("Failed to get AP_REQ: %v", err)
	}
	AP_REQ := result[0].(string)
	TS_apreq := result[1].(*big.Int).Int64()

	// 检查时间戳差异
	fmt.Println("服务器接收到客户端发送的 AP_REQ：", AP_REQ)
	AP_REQ_Parts := strings.Split(AP_REQ, ",")
	fmt.Println("客户端请求信息信息 AP_REQ 解析成功，时间戳解析结果：", AP_REQ_Parts[2])
	// 获取当前时间戳
	localtime := time.Now().Unix()
	fmt.Println("区块时间戳：", TS_apreq)
	fmt.Println("当前系统时间：", localtime)
	intValue, _ := strconv.ParseInt(AP_REQ_Parts[2], 10, 64)
	if localtime-intValue < 300 {
		fmt.Println("客户端发送 AP_REQ 跟服务器处理时间相差 300 秒内，允许通过!")

		// 解密服务票据 ST
		ST := AP_REQ_Parts[3] // IDc +','+ IDv +','+ Kc_v +','+ lifetime
		fmt.Println("服务器取 KDC 用服务器密钥加密的 ST 密文：", ST)
		packet_ST := AES_Decrypt(AES_Key_Generate(Server.Password), ST)
		fmt.Println("服务器用自己的密码生成 AES 密钥解密得到 ST 明文：", packet_ST)
		// 检查服务票据中的 Kc_v、IDc、IDv 是否匹配
		packet_ST_parts := strings.Split(packet_ST, ",")
		if packet_ST_parts[0] == Client_ID && packet_ST_parts[1] == Server.ID {
			fmt.Println("ST 内容检查通过，客户端请求服务的服务器对象正确")
		} else {
			fmt.Println("ST 检测失败")
			return ""
		}

		// 解密 Authenticator2 并验证
		KDC_Client_TGS_ForServer := packet_ST_parts[2]
		fmt.Println("服务器提取 KDC 放置在 ST 中的 KDC_Client_TGS 通讯凭证：", KDC_Client_TGS_ForServer)
		Authenticator2_Encrypt := AP_REQ_Parts[4]
		fmt.Println("服务器提取客户端用 KDC_Client_TGS 通讯凭证加密的身份认证信息：", Authenticator2_Encrypt)
		packet_Authenticator2 := AES_Decrypt(KDC_Client_TGS_ForServer, Authenticator2_Encrypt)
		fmt.Println("服务器用 KDC_Client_TGS 通讯凭证解密身份验证信息：", packet_Authenticator2)

		packet_Authenticator2_parts := strings.Split(packet_Authenticator2, ",")

		// 构建 CheckSum 并验证
		packet_APREQ := strings.Join(packet_ST_parts[:3], ",") // IDc +','+ IDv +','+ ST
		CheckSum_get := sha256.Sum256([]byte(packet_APREQ))

		if packet_Authenticator2_parts[0] == Client_ID && packet_Authenticator2_parts[1] == hex.EncodeToString(CheckSum_get[:]) {
			// 提取会话密钥 Ksession
			Ksession := packet_Authenticator2_parts[2]
			fmt.Println("客户端身份验证通过，服务器获取与客户端交互的对称密钥：", Ksession)

			return Ksession
		} else {
			fmt.Println("Authenticator2 Check False")
			fmt.Println(packet_Authenticator2_parts[0])
			fmt.Println(Client_ID)
			fmt.Println(packet_Authenticator2_parts[1])
			fmt.Println(hex.EncodeToString(CheckSum_get[:]))
			fmt.Println(packet_APREQ)
			return ""
		}
	} else {
		fmt.Println("发送 AP_REQ 跟处理时间超过 300 秒，不允许通过!")
		return ""
	}
}

func C_Set_AP_REQ(Client user.User_True, Server_ID string, Client_Server_AESKey string, ST string, DKSM common.Address,
	client_ETH *ethclient.Client, Client_ETH_PrivateKey *ecdsa.PrivateKey) string {
	fmt.Println("--------------------12. 客户端设置对服务器的请求 AP_REQ！---------------------------")
	ClientAuth, instance := deploy.Foundry(client_ETH, Client_ETH_PrivateKey, DKSM, "abi/DKSM.abi") // 创建签名实例和合约实例

	// 获取客户端的Nonce值
	var result []interface{}
	err := instance.Call(&bind.CallOpts{From: ClientAuth.From}, &result, "get_nonce", Client.ID)
	if err != nil {
		fmt.Println("获取", Client.ID, "的合约 nonce 失败：", err)
	}
	nonce := result[0].(*big.Int) // 将结果转换为*big.Int

	// 生成会话密钥 Ksession
	KsessionBytes := make([]byte, 16)
	_, err = rand.Read(KsessionBytes)
	if err != nil {
		log.Fatalf("Failed to generate Ksession: %v", err)
	}
	Ksession := AES_Key_Generate(hex.EncodeToString(KsessionBytes))
	fmt.Println("客户端随机生成与服务器交互的 AES 密钥 Ksession：", Ksession)

	// 获取当前时间戳
	TimeStamp := time.Now().Unix()
	fmt.Printf("客户端截取当前系统的时间戳：%d\n", TimeStamp)
	//lifetime := "2024-09-01"

	// 构建 AP_REQ 数据包
	packet_APREQ := fmt.Sprintf("%s,%s,%s", Client.ID, Server_ID, Client_Server_AESKey)
	fmt.Println("客户端打包 AP_REQ 明文信息（Client_ID,Server_ID,Client_Server_TGS）：", packet_APREQ)
	CheckSum := sha256.Sum256([]byte(packet_APREQ))
	fmt.Println("客户端对 AP_REQ 明文信息做哈希计算 CheckSum：", hex.EncodeToString(CheckSum[:]))

	Authenticator2 := AES_Encrypt(Client_Server_AESKey, fmt.Sprintf("%s,%x,%s,%d", Client.ID, CheckSum, Ksession, TimeStamp))
	fmt.Println("客户端打包身份认证信息（Client.ID,CheckSum,Ksession,TimeStamp）：", fmt.Sprintf("%s,%x,%s,%d", Client.ID, CheckSum, Ksession, TimeStamp))
	fmt.Println("客户端用获取到的 Client_Server_TGS 通讯凭证加密身份认证信息：", Authenticator2)

	AP_REQ := fmt.Sprintf("%s,%s,%d,%s,%s", Client.ID, Server_ID, TimeStamp, ST, Authenticator2)
	fmt.Println("客户端最终发送的 AP_REQ 请求信息为：", AP_REQ)

	// 使用 Transact 方法调用合约的 C_Set_AP_REQ 函数
	tx, err := instance.Transact(ClientAuth, "C_Set_AP_REQ", Client.ID, Server_ID, AP_REQ, nonce)
	if err != nil {
		log.Fatalf("Failed to set AP_REQ: %v", err)
	}

	// 等待交易执行
	receipt, err := bind.WaitMined(context.Background(), client_ETH, tx)
	if err != nil {
		log.Fatalf("Failed to wait for transaction mining: %v", err)
	}

	// 根据交易状态判断成功或失败
	if receipt.Status == 1 {
		fmt.Println("客户端已发送 AP-REQ：", tx.Hash().Hex())
	} else {
		fmt.Println("AP-REQ 发送失败")
	}

	return Ksession
}
