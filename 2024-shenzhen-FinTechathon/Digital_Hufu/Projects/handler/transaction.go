package handler

import (
	"encoding/json"
	"fmt"
	"hufu/config"
	"hufu/controller"
	"hufu/model"
	"hufu/utils"
	"log"
	"net/http"
	"strconv"
	"time"

	"github.com/gin-gonic/gin"
)

type EncryptFTA struct {
	From   string `json:"from"`
	To     string `json:"to"`
	Amount string `json:"amount"`
}

type DecryptFTA struct {
	From   int     `json:"from"`
	To     int     `json:"to"`
	Amount float64 `json:"amount"`
}

type ResDecrypt struct {
	Message string     `json:"message"`
	Data    DecryptFTA `json:"data"`
}

type ResWarning struct {
	TransactionStatus string `json:"transaction_status"`
	WarningMessage    string `json:"warning_msg"`
}

type ResShuffle struct {
	Data map[string]float64 `json:"data"`
}

// NormalTransfer 处理转账请求
func NormalTransfer(c *gin.Context) {
	var req model.Transaction
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	from, err := controller.GetWalletByID(req.FromWalletID)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": err.Error()})
		return
	}
	to, err := controller.GetWalletByID(req.ToWalletID)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": err.Error()})
		return
	}

	tx, err := controller.NormalTransfer(from, to, req.Amount)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": tx})
}

// ProxyTransfer 处理转账请求
func ProxyTransfer(c *gin.Context) {
	var req EncryptFTA
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	tc := controller.NewTeeController()

	// 1. 解密交易数据
	decryptedData, err := handleDecryption(tc, req)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	// 2. 保存EncryptFTA
	if err := createEncryptedTransaction(decryptedData); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	// 3. 验证交易
	if err := handleWarningCheck(tc, decryptedData); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	// 4. 混洗交易
	shuffleResult, err := handleShuffle(tc, decryptedData)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	// 5. 执行源钱包到代理钱包的转账
	if err := handleSourceToProxy(decryptedData); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	// 6. 执行代理到目标钱包的转账
	if err := handleProxyToTarget(decryptedData, shuffleResult); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "success"})
}

// createEncryptedTransaction 创建加密交易记录
func createEncryptedTransaction(decryptedData *DecryptFTA) error {
	walletKey, err := controller.GetWalletKeyByWalletID(uint(decryptedData.From))
	if err != nil {
		return err
	}

	encryptedFromWalletID, err := utils.RSAEncryptWithHexKey(strconv.Itoa(decryptedData.From), walletKey.PublicKey)
	if err != nil {
		return err
	}

	encryptedToWalletID, err := utils.RSAEncryptWithHexKey(strconv.Itoa(decryptedData.To), walletKey.PublicKey)
	if err != nil {
		return err
	}

	encryptedAmount, err := utils.RSAEncryptWithHexKey(strconv.FormatFloat(decryptedData.Amount, 'f', -1, 64), walletKey.PublicKey)
	if err != nil {
		return err
	}

	var count int64
	if err := model.DB.Model(&model.Transaction{}).Count(&count).Error; err != nil {
		return err
	}

	encryptedTx := &model.EncryptedTransaction{
		TransactionID:         uint(count + 1),
		EncryptedFromWalletID: encryptedFromWalletID,
		EncryptedToWalletID:   encryptedToWalletID,
		EncryptedAmount:       encryptedAmount,
	}

	return model.DB.Create(encryptedTx).Error
}

// handleDecryption 处理解密过程
func handleDecryption(tc *controller.TeeController, req EncryptFTA) (*DecryptFTA, error) {
	respBody, _, _ := tc.Decrypt(req.From, req.To, req.Amount)
	log.Println(string(respBody))

	var resDecrypt ResDecrypt
	if err := json.Unmarshal(respBody, &resDecrypt); err != nil {
		return nil, err
	}

	if resDecrypt.Message != "Transaction decrypted successfully." {
		return nil, fmt.Errorf(resDecrypt.Message)
	}

	return &resDecrypt.Data, nil
}

// handleWarningCheck 处理交易验证
func handleWarningCheck(tc *controller.TeeController, decryptedData *DecryptFTA) error {
	respBody, _, _ := tc.Warning(decryptedData.From, decryptedData.To, decryptedData.Amount)
	log.Println("warning:", string(respBody))

	var resWarning ResWarning
	if err := json.Unmarshal(respBody, &resWarning); err != nil {
		return err
	}

	if resWarning.TransactionStatus != "Success" {
		log.Println("warning:================================================")
		log.Println(resWarning.WarningMessage)
		if err := createAbnormalTransaction(decryptedData, resWarning.WarningMessage); err != nil {
			return err
		}
		return fmt.Errorf(resWarning.WarningMessage)
	}

	return nil
}

// createAbnormalTransaction 创建异常交易记录
func createAbnormalTransaction(decryptedData *DecryptFTA, warningMessage string) error {
	var count int64
	if err := model.DB.Model(&model.Transaction{}).Count(&count).Error; err != nil {
		return err
	}

	evidence := fmt.Sprintf("Transaction ID: %d, From: %d, To: %d, Amount: %f, Time: %s, Error: %v",
		count+1, decryptedData.From, decryptedData.To, decryptedData.Amount,
		time.Now().Format(time.RFC3339), warningMessage)

	// 使用监管者的私钥对证据进行签名
	signature, err := utils.SignData(config.GlobalConfig.Tee.PrivateKey, evidence)
	if err != nil {
		return err
	}

	abnormal := &model.AbnormalTransaction{
		WalletID:      uint(decryptedData.From),
		TransactionID: uint(count + 1),
		Evidence:      evidence,
		Signature:     signature,
	}

	return model.DB.Create(abnormal).Error
}

// handleShuffle 处理混洗过程
func handleShuffle(tc *controller.TeeController, decryptedData *DecryptFTA) (*ResShuffle, error) {
	respBody, _, _ := tc.Shuffle(decryptedData.From, decryptedData.To, decryptedData.Amount)
	log.Println("shuffle:", string(respBody))

	var resShuffle ResShuffle
	if err := json.Unmarshal(respBody, &resShuffle); err != nil {
		return nil, err
	}

	return &resShuffle, nil
}

// handleSourceToProxy 处理源钱包到代理钱包的转账
func handleSourceToProxy(decryptedData *DecryptFTA) error {
	from, err := controller.GetWalletByID(uint(decryptedData.From))
	if err != nil {
		return err
	}

	proxy, err := controller.GetWalletByID(2)
	if err != nil {
		return err
	}

	_, err = controller.NormalTransfer(from, proxy, decryptedData.Amount)
	return err
}

// handleProxyToTarget 处理代理到目标钱包的转账
func handleProxyToTarget(decryptedData *DecryptFTA, shuffleResult *ResShuffle) error {
	to, err := controller.GetWalletByID(uint(decryptedData.To))
	if err != nil {
		return err
	}

	for from, amount := range shuffleResult.Data {
		id, _ := strconv.Atoi(from)
		fromWallet, err := controller.GetWalletByID(uint(id))
		if err != nil {
			return err
		}

		if _, err := controller.NormalTransfer(fromWallet, to, amount); err != nil {
			return err
		}
	}

	return nil
}

// GetTransferHistory 获取转账历史
func GetTransferHistory(c *gin.Context) {
	var req struct {
		WalletID uint `json:"wallet_id" binding:"required"`
		Page     int  `json:"page"`
		PageSize int  `json:"page_size"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	// 设置默认值
	if req.Page <= 0 {
		req.Page = 1
	}
	if req.PageSize <= 0 {
		req.PageSize = 10
	}

	result, err := controller.GetTransferHistory(req.WalletID, req.Page, req.PageSize)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"code": 0,
		"data": result,
	})
}

// GetEncryptedTransaction 获取加密交易信息
// func GetEncryptedTransaction(c *gin.Context) {
// 	var req Req
// 	if err := c.ShouldBindJSON(&req); err != nil {
// 		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
// 		return
// 	}

// 	encryptedTx, err := controller.GetEncryptedTransaction(req.WalletID, req.PrivateKey)
// 	if err != nil {
// 		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
// 		return
// 	}

// 	c.JSON(http.StatusOK, gin.H{
// 		"code": 0,
// 		"data": encryptedTx,
// 	})
// }

// GetDesensitizedTransaction 获取脱敏交易记录
// func GetDesensitizedTransaction(c *gin.Context) {
// 	var req Req
// 	if err := c.ShouldBindJSON(&req); err != nil {
// 		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
// 		return
// 	}

// 	transactions, err := controller.GetDesensitizedTransaction(req.WalletID)
// 	if err != nil {
// 		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
// 		return
// 	}

// 	c.JSON(http.StatusOK, gin.H{
// 		"data": transactions,
// 	})
// }

// GetReceivedTransactions 获取收款记录
func GetReceivedTransactions(c *gin.Context) {
	var req struct {
		WalletID uint `json:"wallet_id" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	result, err := controller.GetReceivedTransactions(req.WalletID)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"code": 0,
		"data": result,
	})
}

// GetTransactionStats 获取交易统计信息
func GetTransactionStats(c *gin.Context) {
	var req struct {
		WalletID uint `json:"wallet_id" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"code": -1,
			"msg":  "参数错误: " + err.Error(),
		})
		return
	}

	stats, err := controller.GetTransactionStats(req.WalletID)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"code": -1,
			"msg":  "获取统计信息失败: " + err.Error(),
		})
		return
	}

	// 在返回结果之前对数据进行格式化处理
	formattedStats := gin.H{
		"total_amount": utils.FormatFloat(stats.TotalAmount, 2),
		"today_amount": utils.FormatFloat(stats.TodayAmount, 2),
		"week_amount":  utils.FormatFloat(stats.WeekAmount, 2),
		"month_amount": utils.FormatFloat(stats.MonthAmount, 2),
	}

	c.JSON(http.StatusOK, gin.H{
		"code": 0,
		"data": formattedStats,
	})
}
