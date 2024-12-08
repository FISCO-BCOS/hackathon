package handler

import (
	"net/http"

	"hufu/model"
	"hufu/utils"

	"github.com/gin-gonic/gin"
)

// CreateInvoice 创建发票
func CreateInvoice(c *gin.Context) {
	var invoice model.Invoice
	if err := c.ShouldBindJSON(&invoice); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的请求数据"})
		return
	}

	// 创建发票记录
	invoice.InvoiceID = utils.GenerateRandomNumber(20)
	if err := model.DB.Create(&invoice).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "创建发票失败"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": invoice})
}

// GetInvoice 获取单张发票
func GetInvoice(c *gin.Context) {
	invoiceID := c.Param("id")
	var invoice model.Invoice

	if err := model.DB.First(&invoice, "invoice_id = ?", invoiceID).Error; err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "发票不存在"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": invoice})
}

// ListInvoices 获取发票列表
func ListInvoices(c *gin.Context) {
	var pageInfo model.PageInfo
	if err := c.ShouldBindQuery(&pageInfo); err != nil {
		pageInfo.Page = 1
		pageInfo.PageSize = 10
	}

	var invoices []model.Invoice
	var total int64

	// 获取总数
	model.DB.Model(&model.Invoice{}).Count(&total)

	// 分页查询
	offset := (pageInfo.Page - 1) * pageInfo.PageSize
	result := model.DB.Offset(offset).Limit(pageInfo.PageSize).Find(&invoices)
	if result.Error != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "获取发票列表失败"})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"data": model.PageResult{
			List:     invoices,
			Total:    total,
			Page:     pageInfo.Page,
			PageSize: pageInfo.PageSize,
		},
	})
}

// UpdateInvoice 更新发票信息
func UpdateInvoice(c *gin.Context) {
	invoiceID := c.Param("id")
	var invoice model.Invoice

	// 检查发票是否存在
	if err := model.DB.First(&invoice, "invoice_id = ?", invoiceID).Error; err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "发票不存在"})
		return
	}

	// 绑定更新数据
	var updateData model.Invoice
	if err := c.ShouldBindJSON(&updateData); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的请求数据"})
		return
	}

	// 更新发票信息
	if err := model.DB.Model(&invoice).Updates(updateData).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "更新发票失败"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": invoice})
}

// CancelInvoice 作废发票
func CancelInvoice(c *gin.Context) {
	invoiceID := c.Param("id")
	var invoice model.Invoice

	if err := model.DB.First(&invoice, "invoice_id = ?", invoiceID).Error; err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "发票不存在"})
		return
	}

	// 更新发票状态为作废
	if err := model.DB.Model(&invoice).Update("invoice_status", "已作废").Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "作废发票失败"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "发票已作废"})
}

// GetInvoiceStats 获取发票统计
func GetInvoiceStats(c *gin.Context) {
	var stats struct {
		TotalCount     int64   `json:"total_count"`
		TotalAmount    float64 `json:"total_amount"`
		CancelledCount int64   `json:"cancelled_count"`
	}

	// 统计总发票数和总金额
	model.DB.Model(&model.Invoice{}).Count(&stats.TotalCount)
	model.DB.Model(&model.Invoice{}).Select("COALESCE(SUM(total_amount), 0)").Scan(&stats.TotalAmount)
	model.DB.Model(&model.Invoice{}).Where("invoice_status = ?", "已作废").Count(&stats.CancelledCount)

	c.JSON(http.StatusOK, gin.H{"data": stats})
}

// VerifyInvoice 验证发票
func VerifyInvoice(c *gin.Context) {
	invoiceID := c.Param("id")
	var invoice model.Invoice

	if err := model.DB.First(&invoice, "invoice_id = ?", invoiceID).Error; err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "发票不存在"})
		return
	}

	// 验证发票金额计算是否正确
	calculatedAmount := float64(invoice.Quantity) * invoice.UnitPrice
	calculatedTaxAmount := calculatedAmount * invoice.TaxRate
	calculatedTotalAmount := calculatedAmount + calculatedTaxAmount

	isValid := invoice.Amount == calculatedAmount &&
		invoice.TaxAmount == calculatedTaxAmount &&
		invoice.TotalAmount == calculatedTotalAmount

	c.JSON(http.StatusOK, gin.H{
		"is_valid": isValid,
		"details": gin.H{
			"calculated_amount":       calculatedAmount,
			"calculated_tax_amount":   calculatedTaxAmount,
			"calculated_total_amount": calculatedTotalAmount,
		},
	})
}

// DownloadInvoice 下载发票
func DownloadInvoice(c *gin.Context) {
	invoiceID := c.Param("id")
	var invoice model.Invoice

	if err := model.DB.First(&invoice, "invoice_id = ?", invoiceID).Error; err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "发票不存在"})
		return
	}

	// TODO: 实现生成PDF文件的逻辑
	// 这里需要集成PDF生成库，如go-pdf等

	c.Header("Content-Disposition", "attachment; filename=invoice-"+invoiceID+".pdf")
	c.Header("Content-Type", "application/pdf")
	// c.File(pdfPath) // 发送生成的PDF文件
}

// EmailInvoice 发送发票到邮箱
func EmailInvoice(c *gin.Context) {
	var req struct {
		InvoiceID string `json:"invoice_id"`
		Email     string `json:"email"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的请求数据"})
		return
	}

	var invoice model.Invoice
	if err := model.DB.First(&invoice, "invoice_id = ?", req.InvoiceID).Error; err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "发票不存在"})
		return
	}

	// TODO: 实现邮件发送逻辑
	// 1. 生成PDF
	// 2. 配置邮件服务
	// 3. 发送邮件

	c.JSON(http.StatusOK, gin.H{"message": "发票已发送至邮箱"})
}
