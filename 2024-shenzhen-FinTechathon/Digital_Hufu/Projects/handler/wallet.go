package handler

import (
	"hufu/controller"
	"hufu/model"
	"net/http"

	"github.com/gin-gonic/gin"
)

// CreateWallet 创建钱包
func CreateWallet(c *gin.Context) {
	var req model.Wallet
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	wallet, err := controller.NewWallet(req.WalletName, req.Username, req.Balance)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": wallet})
}

// GetWallet 获取单个钱包详情
func GetWallet(c *gin.Context) {
	var req model.Wallet
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	if req.ID != 0 {
		wallet, err := controller.GetWalletByID(req.ID)
		if err != nil {
			c.JSON(http.StatusNotFound, gin.H{"error": err.Error()})
			return
		}
		c.JSON(http.StatusOK, gin.H{"data": wallet})
		return
	}
	if req.WalletName != "" {
		wallet, err := controller.GetWalletByWalletName(req.WalletName)
		if err != nil {
			c.JSON(http.StatusNotFound, gin.H{"error": err.Error()})
			return
		}
		c.JSON(http.StatusOK, gin.H{"data": wallet})

	}
	if req.Username != "" {
		wallet, err := controller.GetWalletByUsername(req.Username)
		if err != nil {
			c.JSON(http.StatusNotFound, gin.H{"error": err.Error()})
			return
		}
		c.JSON(http.StatusOK, gin.H{"data": wallet})
	}

}

// UpdateWallet 更新钱包信息
func UpdateWallet(c *gin.Context) {
	var req model.Wallet

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"code": -1,
			"msg":  "参数错误: " + err.Error(),
		})
		return
	}

	// 调用 controller 层更新钱包
	err := controller.UpdateWallet(req.ID, req.WalletName, req.Balance)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"code": -1,
			"msg":  "更新钱包失败: " + err.Error(),
		})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"code": 0,
		"msg":  "更新钱包成功",
	})
}

// GetWalletStats 获取钱包统计信息
func GetWalletStats(c *gin.Context) {
	var req struct {
		WalletID uint `json:"wallet_id" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	stats, err := controller.GetWalletStats(req.WalletID)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": stats})
}

// GetTrend 获取收支趋势
func GetTrend(c *gin.Context) {
	var req struct {
		WalletID uint `json:"wallet_id" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"code":    -1,
			"message": "参数错误",
			"error":   err.Error(),
		})
		return
	}

	// 从数据库获取支出趋势数据
	trends, err := controller.GetTrend(req.WalletID)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"code":    -1,
			"message": "获取支出趋势失败",
			"error":   err.Error(),
		})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"code":    0,
		"message": "success",
		"data":    trends,
	})
}
