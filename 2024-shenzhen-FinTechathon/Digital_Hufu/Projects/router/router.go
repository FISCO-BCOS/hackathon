package router

import (
	"hufu/handler"

	"github.com/gin-gonic/gin"
)

func InitHufuRouter(r *gin.Engine) {
	hufu := r.Group("/api/v1/hufu")
	{
		// 钱包相关路由
		wallet := hufu.Group("/wallet")
		{
			wallet.POST("/create", handler.CreateWallet)  // 创建钱包
			wallet.POST("/", handler.GetWallet)           // 获取单个钱包
			wallet.POST("/update", handler.UpdateWallet)  // 更新钱包
			wallet.POST("/stats", handler.GetWalletStats) // 获取钱包统计
			wallet.POST("/trend", handler.GetTrend)       // 获取收支趋势
		}

		// 转账相关路由
		tx := hufu.Group("/tx")
		{
			tx.POST("/normal-transfer", handler.NormalTransfer) // 普通转账
			tx.POST("/proxy-transfer", handler.ProxyTransfer)   // 代理转账
			// tx.POST("/encrypt", handler.EncryptData)            // 加密数据
			// tx.POST("/encrypted-transfer", handler.EncryptedTransfer)    // 加密转账
			tx.POST("/history", handler.GetTransferHistory) // 获取转账历史
			// tx.POST("/desensitized", handler.GetDesensitizedTransaction) // 获取脱敏交易记录
			tx.POST("/received", handler.GetReceivedTransactions) // 添加获取收款记录路由
			tx.POST("/stats", handler.GetTransactionStats)        // 添加获取收入统计路由
		}

		// 发票相关路由
		invoice := hufu.Group("/invoice")
		{
			invoice.POST("/create", handler.CreateInvoice)     // 创建发票
			invoice.POST("/get", handler.GetInvoice)           // 获取单张发票
			invoice.POST("/list", handler.ListInvoices)        // 获取发票列表
			invoice.POST("/update", handler.UpdateInvoice)     // 更新发票信息
			invoice.POST("/cancel", handler.CancelInvoice)     // 作废发票
			invoice.POST("/stats", handler.GetInvoiceStats)    // 获取发票统计
			invoice.POST("/verify", handler.VerifyInvoice)     // 验证发票
			invoice.POST("/download", handler.DownloadInvoice) // 下载发票
			invoice.POST("/email", handler.EmailInvoice)       // 发送发票到邮箱
		}

		// 密钥相关路由
		keys := hufu.Group("/keys")
		{
			keys.POST("/", handler.GetEncryptionKeys) // 获取加密密钥
		}

		teeHandler := handler.NewTeeHandler()
		tee := hufu.Group("/tee")
		{
			tee.POST("/add", teeHandler.TeeAdd)
			tee.POST("/generate-key", teeHandler.TeeGenerateKey)
			tee.POST("/encrypt-transaction", teeHandler.TeeEncrypt)
			tee.POST("/decrypt-transaction", teeHandler.TeeDecrypt)
			tee.POST("/shuffle", teeHandler.TeeShuffle)
			tee.POST("/warning", teeHandler.TeeWarning)
			tee.POST("/encrypted-history", teeHandler.GetEncryptedHistory) // 获取加密交易记录
			tee.POST("/decrypt-history", teeHandler.GetDecryptedHistory)   // 获取解密交易记录
		}
	}
}

func InitRegulatorRouter(r *gin.Engine) {
	regulator := r.Group("/api/v1/regulator")
	{
		regulator.POST("/alert", handler.CheckTransaction)          // 检查交易
		regulator.POST("/private-key", handler.GetPrivateKey)       // 获取私钥
		regulator.POST("/application", handler.GetApplication)      // 获取申请记录
		regulator.POST("/abnormal", handler.GetAbnormalTransaction) // 获取异常交易
		regulator.POST("/decision", handler.GetDecision)            // 获取决策
		regulator.POST("/event", handler.GetEvent)                  // 获取事件
	}
}
