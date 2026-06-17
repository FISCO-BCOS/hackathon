package handler

import (
	"hufu/controller"
	"hufu/model"
	"hufu/utils"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
)

type TeeHandler struct {
	TeeController *controller.TeeController
}

func NewTeeHandler() *TeeHandler {
	return &TeeHandler{
		TeeController: controller.NewTeeController(),
	}
}

func (h *TeeHandler) TeeAdd(c *gin.Context) {
	var requestBody struct {
		Add string `json:"add"`
	}

	if err := c.ShouldBindJSON(&requestBody); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "解析 JSON 请求失败: " + err.Error(),
		})
		return
	}

	respBody, statusCode, err := h.TeeController.Add(requestBody.Add)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "请求失败: " + err.Error(),
		})
		return
	}

	c.String(statusCode, string(respBody))
}

func (h *TeeHandler) TeeGenerateKey(c *gin.Context) {
	var request struct {
		WalletID int `json:"wallet_id"`
	}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的请求格式: " + err.Error(),
		})
		return
	}

	if request.WalletID == 0 {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "wallet_id 不能为空",
		})
		return
	}

	respBody, statusCode, err := h.TeeController.GenerateKey(request.WalletID)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "请求失败: " + err.Error(),
		})
		return
	}

	c.String(statusCode, string(respBody))
}

func (h *TeeHandler) TeeShuffle(c *gin.Context) {
	var request struct {
		From   int     `json:"from"`
		To     int     `json:"to"`
		Amount float64 `json:"amount"`
	}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的请求格式: " + err.Error(),
		})
		return
	}

	respBody, statusCode, err := h.TeeController.Shuffle(request.From, request.To, request.Amount)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "请求失败: " + err.Error(),
		})
		return
	}

	c.String(statusCode, string(respBody))
}

func (h *TeeHandler) TeeWarning(c *gin.Context) {
	var request struct {
		From   int     `json:"from"`
		To     int     `json:"to"`
		Amount float64 `json:"amount"`
	}
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的请求格式: " + err.Error(),
		})
		return
	}

	respBody, statusCode, err := h.TeeController.Warning(request.From, request.To, request.Amount)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "请求失败: " + err.Error(),
		})
		return
	}

	c.String(statusCode, string(respBody))
}

func (h *TeeHandler) TeeDecrypt(c *gin.Context) {
	var request struct {
		From   string `json:"from"`
		To     string `json:"to"`
		Amount string `json:"amount"`
	}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的请求格式: " + err.Error(),
		})
		return
	}

	respBody, statusCode, err := h.TeeController.Decrypt(request.From, request.To, request.Amount)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "请求失败: " + err.Error(),
		})
		return
	}

	c.String(statusCode, string(respBody))
}

func (h *TeeHandler) TeeEncrypt(c *gin.Context) {
	var request struct {
		From   string `json:"from"`
		To     string `json:"to"`
		Amount string `json:"amount"`
	}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的请求格式: " + err.Error(),
		})
		return
	}

	encryptedData, err := h.TeeController.Encrypt(request.From, request.To, request.Amount)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": err.Error(),
		})
		return
	}

	c.JSON(http.StatusOK, encryptedData)
}

func (h *TeeHandler) GetEncryptedHistory(c *gin.Context) {
	var req struct {
		Id         string `json:"id"`
		PrivateKey string `json:"private_key"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的请求格式: " + err.Error(),
		})
		return
	}

	// 验证私钥是否正确
	wallet, err := controller.GetWalletByID(utils.StringToUint(req.Id))
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{
			"error": "钱包不存在: " + err.Error(),
		})
		return
	}
	walletKey, err := controller.GetWalletKeyByWalletID(wallet.ID)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{
			"error": "钱包密钥不存在: " + err.Error(),
		})
		return
	}

	// 验证私钥是否匹配
	if walletKey.PrivateKey != req.PrivateKey {
		c.JSON(http.StatusUnauthorized, gin.H{
			"error": "私钥验证失败",
		})
		return
	}
	var result []model.EncryptedTransaction
	if err := model.DB.Find(&result).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "请求失败: " + err.Error(),
		})
		return
	}

	c.JSON(http.StatusOK, result)
}

func (h *TeeHandler) GetDecryptedHistory(c *gin.Context) {
	var req struct {
		Id         string `json:"id"`
		PrivateKey string `json:"private_key"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的请求格式: " + err.Error(),
		})
		return
	}

	// 验证私钥是否正确
	wallet, err := controller.GetWalletByID(utils.StringToUint(req.Id))
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{
			"error": "钱包不存在: " + err.Error(),
		})
		return
	}
	walletKey, err := controller.GetWalletKeyByWalletID(wallet.ID)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{
			"error": "钱包密钥不存在: " + err.Error(),
		})
		return
	}

	// 验证私钥是否匹配
	if walletKey.PrivateKey != req.PrivateKey {
		c.JSON(http.StatusUnauthorized, gin.H{
			"error": "私钥验证失败",
		})
		return
	}

	type DecryptedTransaction struct {
		From     string `json:"from"`
		To       string `json:"to"`
		Amount   string `json:"amount"`
		CreateAt string `json:"create_at"`
	}
	var result []model.EncryptedTransaction
	if err := model.DB.Find(&result).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "请求失败: " + err.Error(),
		})
		return
	}

	var resp []DecryptedTransaction
	for _, item := range result {
		from, _ := utils.RSADecryptWithHexKey(item.EncryptedFromWalletID, walletKey.PrivateKey, walletKey.PublicKey)
		to, _ := utils.RSADecryptWithHexKey(item.EncryptedToWalletID, walletKey.PrivateKey, walletKey.PublicKey)
		amount, _ := utils.RSADecryptWithHexKey(item.EncryptedAmount, walletKey.PrivateKey, walletKey.PublicKey)
		createAt := item.CreatedAt.Format(time.DateTime)
		decrypted := DecryptedTransaction{
			From:     from,
			To:       to,
			Amount:   amount,
			CreateAt: createAt,
		}
		resp = append(resp, decrypted)
	}

	c.JSON(http.StatusOK, resp)
}
