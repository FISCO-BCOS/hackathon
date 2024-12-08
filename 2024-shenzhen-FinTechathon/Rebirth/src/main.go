package main

import (
	"DKSM/user"
	"DKSM/utils"
	"fmt"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/ethereum/go-ethereum/ethclient"
	"log"
)

const (
	node      = "http://127.0.0.1:8545/"
	KDC_id    = "KDC - 3D printer"
	Client_id = "Teacher-01"
	Server_id = "3D printer"

	SetUP  = 0 // 当您选择为 0 时，说明您准备初始化 DKSM
	KDC    = 1 // 当您选择为 1 时， 您的身份就是 KDC
	Client = 2 // 当您选择为 2 时，您的身份就是客户端
	Server = 3 // 当您选择为 3 时，您的身份就是服务器

	C_Set_AS_REQ     = 1 // 第一步为客户端向 KDC 发送 AS_REQ
	KDC_Set_AS_REP   = 2 // 第二步为 KDC 接收 AS_REQ 后处理请求，向客户端发送 AS_REP
	C_Set_TGS_REQ    = 3 // 第三步为客户端接收 AS_REP 后处理回复信息，向 KDC 发送 TGS_REQ
	KDC_Set_TGS_REP  = 4 // 第四步为 KDC 接收 TGS_REQ 后处理回复信息，向客户端发送 TGS_REP
	C_Set_AP_REQ     = 5 // 第五步为客户端接收 TGS_REP 后处理回复信息，向服务器发送 AP_REQ
	S_Set_AP_REP     = 6 // 第六步为服务器接收 AP_REQ 后处理回复信息，向客户端发送 AP_REP
	C_Receipt_AP_REP = 7 // 第七步为客户端接收 AP_REP 后处理回复信息，验证成功后，即可安全通讯

	Client_Send_Message = 8
	Server_Send_Message = 9
)

func main() {
	var choice int
	var KDC_Client_TGS_ForKDC string
	var KDC_Client_TGS_ForClient string
	var Ksession_ForClient string
	var Ksession_ForServer string

	var ctxt string
	flag := true

	client_ETH, err := ethclient.Dial(node)
	if err != nil {
		log.Fatalf("连接到以太坊客户端失败: %v", err)
	}

	for flag {
		// 打印提示信息
		fmt.Println("欢迎使用 DKSM 服务协议：")
		fmt.Println("0. 初始化 DSKM 服务协议并部署智能合约")
		fmt.Println("1. 选择您的身份为 KDC")
		fmt.Println("2. 选择您的身份为 客户端")
		fmt.Println("3. 选择您的身份为 服务器")
		fmt.Printf("请您输入您的选择：")

		// 等待用户输入，并将输入存储到 choice 变量中
		_, err := fmt.Scanf("%d", &choice)
		if err != nil {
			log.Fatalf("输入无效，请输入一个整数: %v", err)
		}

		switch choice {
		case SetUP:
			fmt.Println("-------------------------------------")
			fmt.Println("当前正在为您初始化 DKSM")

			KDC_ETH_PrivateKey, err := crypto.HexToECDSA(user.KDC_True.BlockChain_PrivateKey) // 因为做的是分布式的 KDC，所以部署合约应该是 KDC 身份才对
			if err != nil {
				log.Fatalf("加载私钥失败: %v", err)
			}

			DKSM := utils.Setup(user.KDC_True, user.Client_True, user.Server_True, client_ETH, KDC_ETH_PrivateKey) // 初始化并获取公钥、秘密密钥和合约地址
			utils.VerifySetup(user.KDC_True, user.Client_True, user.Server_True, DKSM, client_ETH, KDC_ETH_PrivateKey)

			fmt.Println("合约部署完成，用户信息初始化成功")
			fmt.Println("-------------------------------------")

		case KDC:
			fmt.Println("-------------------------------------")
			fmt.Println("您当前的身份为 KDC")
			KDC_ETH_PrivateKey, err := crypto.HexToECDSA(user.KDC_True.BlockChain_PrivateKey)
			if err != nil {
				log.Fatalf("加载私钥失败: %v", err)
			}

			// 打印提示信息
			fmt.Println("KDC 工作选项：")
			fmt.Println("2. KDC 对客户端发送 AS_REP")
			fmt.Println("4. KDC 对客户端发送 TGS_REP")
			fmt.Printf("请您输入您的选择：")

			// 等待用户输入，并将输入存储到 choice 变量中
			_, err = fmt.Scanf("%d", &choice)
			if err != nil {
				log.Fatalf("输入无效，请输入一个整数: %v", err)
			}

			fmt.Printf("请您输入 DKSM 的合约地址:")
			var address string
			fmt.Scanln(&address)
			// 将输入字符串转换为 common.Address 类型
			DKSM := common.HexToAddress(address)

			switch choice {
			case KDC_Set_AS_REP:
				utils.Receipt_AS_REQ(user.KDC_True, Client_id, DKSM, client_ETH, KDC_ETH_PrivateKey)
				KDC_Client_TGS_ForKDC = utils.AS_Set_AS_REP(user.KDC_True, Client_id, DKSM, client_ETH, KDC_ETH_PrivateKey)
				fmt.Println("系统已自动为 KDC 存储 KDC_Client_TGS 通行凭证到 KDC_Client_TGS_ForKDC")

			case KDC_Set_TGS_REP:
				utils.Receipt_TGS_REQ(user.KDC_True, KDC_Client_TGS_ForKDC, Client_id, DKSM, client_ETH, KDC_ETH_PrivateKey)
				utils.TGS_Set_TGS_REP(user.KDC_True, KDC_Client_TGS_ForKDC, Client_id, Server_id, DKSM, client_ETH, KDC_ETH_PrivateKey)

			default:
				continue
			}
			fmt.Println("-------------------------------------")

		case Client:
			fmt.Println("-------------------------------------")
			fmt.Println("您当前的身份为客户端")
			Client_ETH_PrivateKey, err := crypto.HexToECDSA(user.Client_True.BlockChain_PrivateKey)
			if err != nil {
				log.Fatalf("加载私钥失败: %v", err)
			}

			// 打印提示信息
			fmt.Println("客户端工作选项：")
			fmt.Println("1. 客户端对 KDC 发送 AS_REQ")
			fmt.Println("3. 客户端对 KDC 发送 TGS_REQ")
			fmt.Println("5. 客户端对服务器发送 AP_REQ")
			fmt.Println("7. 客户端确认服务器的回复信息")
			fmt.Println("8. 客户端查看或给服务器留言")
			fmt.Printf("请您输入您的选择：")

			// 等待用户输入，并将输入存储到 choice 变量中
			_, err = fmt.Scanf("%d", &choice)
			if err != nil {
				log.Fatalf("输入无效，请输入一个整数: %v", err)
			}

			switch choice {
			case C_Set_AS_REQ:
				fmt.Printf("请您输入 DKSM 的合约地址:")
				var address string
				fmt.Scanln(&address)
				// 将输入字符串转换为 common.Address 类型
				DKSM := common.HexToAddress(address)
				// 客户端向 KDC 发送注册信息
				utils.C_Set_AS_REQ(user.Client_True, KDC_id, DKSM, client_ETH, Client_ETH_PrivateKey)

			case C_Set_TGS_REQ:
				fmt.Printf("请您输入 DKSM 的合约地址:")
				var address string
				fmt.Scanln(&address)
				// 将输入字符串转换为 common.Address 类型
				DKSM := common.HexToAddress(address)
				// 客户端接收 AS_REP信息：KDC_Client_TGS, KDC.ID, TimeStamp, TGT
				ASRepContent, KDC_Client_TGS_Receieve := utils.Receipt_AS_REP(user.Client_True, KDC_id, DKSM, client_ETH, Client_ETH_PrivateKey)
				KDC_Client_TGS_ForClient = KDC_Client_TGS_Receieve
				fmt.Println("系统已自动为客户端存储 KDC_Client_TGS 通行凭证到 KDC_Client_TGS_ForClient")
				utils.C_Set_TGS_REQ(user.Client_True, ASRepContent, KDC_id, Server_id, DKSM, client_ETH, Client_ETH_PrivateKey)

			case C_Set_AP_REQ:
				fmt.Printf("请您输入 DKSM 的合约地址:")
				var address string
				fmt.Scanln(&address)
				// 将输入字符串转换为 common.Address 类型
				DKSM := common.HexToAddress(address)
				Client_Server_AESKey, ST := utils.Receipt_TGS_REP(user.Client_True, KDC_Client_TGS_ForClient, Server_id, DKSM, client_ETH, Client_ETH_PrivateKey)
				Ksession := utils.C_Set_AP_REQ(user.Client_True, Server_id, Client_Server_AESKey, ST, DKSM, client_ETH, Client_ETH_PrivateKey)
				Ksession_ForClient = Ksession
				fmt.Println("客户端持有与服务器交互的对称密钥：", Ksession_ForClient)

			case C_Receipt_AP_REP:
				fmt.Printf("请您输入 DKSM 的合约地址:")
				var address string
				fmt.Scanln(&address)
				// 将输入字符串转换为 common.Address 类型
				DKSM := common.HexToAddress(address)
				utils.Receipt_AP_REP(user.Client_True, Server_id, Ksession_ForClient, DKSM, client_ETH, Client_ETH_PrivateKey)

			case Client_Send_Message:
				if ctxt == "" {
					fmt.Println("当前没有信息，请您发送")
					fmt.Printf("请您输入您想要发送的信息：")
					_, err = fmt.Scanf("%s", &ctxt)
					ctxt = utils.AES_Encrypt(Ksession_ForClient, ctxt)
					fmt.Println("此时留言的信息为：", ctxt)
				} else {
					fmt.Println("此时留言的信息为：", ctxt)
					ctxt = utils.AES_Decrypt(Ksession_ForClient, ctxt)
					fmt.Println("解密后查看：", ctxt)
					ctxt = ""
				}

			default:
				continue
			}

			fmt.Println("-------------------------------------")

		case Server:
			fmt.Println("-------------------------------------")
			fmt.Println("您当前的身份为服务器")
			Server_ETH_PrivateKey, err := crypto.HexToECDSA(user.Server_True.BlockChain_PrivateKey)
			if err != nil {
				log.Fatalf("加载私钥失败: %v", err)
			}

			// 打印提示信息
			fmt.Println("服务器工作选项：")
			fmt.Println("6. 服务器对客户端发送 AP_REP")
			fmt.Println("9. 服务器查看或给客户端留言")
			fmt.Printf("请您输入您的选择：")

			// 等待用户输入，并将输入存储到 choice 变量中
			_, err = fmt.Scanf("%d", &choice)
			if err != nil {
				log.Fatalf("输入无效，请输入一个整数: %v", err)
			}

			switch choice {
			case S_Set_AP_REP:
				fmt.Printf("请您输入 DKSM 的合约地址:")
				var address string
				fmt.Scanln(&address)
				// 将输入字符串转换为 common.Address 类型
				DKSM := common.HexToAddress(address)
				Ksession_ForServer = utils.Receipt_AP_REQ(user.Server_True, Client_id, DKSM, client_ETH, Server_ETH_PrivateKey)
				fmt.Println("服务器持有与客户端交互的对称密钥：", Ksession_ForServer)
				utils.S_Set_AP_REP(user.Server_True, Client_id, Ksession_ForServer, DKSM, client_ETH, Server_ETH_PrivateKey)

			case Server_Send_Message:
				if ctxt == "" {
					fmt.Println("当前没有信息，请您发送")
					fmt.Printf("请您输入您想要发送的信息：")
					_, err = fmt.Scanf("%s", &ctxt)
					ctxt = utils.AES_Encrypt(Ksession_ForClient, ctxt)
					fmt.Println("此时留言的信息为：", ctxt)
				} else {
					fmt.Println("此时留言的信息为：", ctxt)
					ctxt = utils.AES_Decrypt(Ksession_ForClient, ctxt)
					fmt.Println("解密后查看：", ctxt)
					ctxt = ""
				}

			default:
				continue
			}

			fmt.Println("-------------------------------------")

		default:
			fmt.Println("欢迎您再次使用 DKCM 服务协议！")
			flag = false
		}
	}
}
