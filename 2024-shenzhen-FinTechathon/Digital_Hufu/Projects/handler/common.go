package handler

import (
	"encoding/json"
	"hufu/config"
	"hufu/utils"

	"github.com/gin-gonic/gin"
)

// GetEncryptionKeys 获取加密密钥的处理函数
func GetEncryptionKeys(c *gin.Context) {
	c.JSON(200, gin.H{
		"public_key": config.GlobalConfig.Tee.PublicKey,
	})
}

func EncryptData(c *gin.Context) {
	type Request struct {
		FromWalletID uint `json:"from_wallet_id"`
		ToWalletID   uint `json:"to_wallet_id"`
		Amount       int  `json:"amount"`
	}

	var request Request
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(400, gin.H{"error": err.Error()})
		return
	}

	data, err := json.Marshal(request)
	if err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
		return
	}

	encryptedData, err := utils.RSAEncryptWithHexKey(string(data), config.GlobalConfig.Tee.PublicKey)
	if err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
		return
	}

	c.JSON(200, gin.H{"data": encryptedData})
}
