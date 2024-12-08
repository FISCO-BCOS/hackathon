package model

import "gorm.io/gorm"

// PageInfo 分页请求参数
type PageInfo struct {
	Page     int `json:"page" form:"page"`           // 页码
	PageSize int `json:"page_size" form:"page_size"` // 每页数量
}

// PageResult 分页返回结果
type PageResult struct {
	List     interface{} `json:"list"`      // 数据列表
	Total    int64       `json:"total"`     // 总数
	Page     int         `json:"page"`      // 当前页码
	PageSize int         `json:"page_size"` // 每页数量
}

// Invoice 发票结构体
type Invoice struct {
	gorm.Model
	InvoiceID     string  `json:"invoice_id"`     // 发票编号
	IssueDate     string  `json:"issue_date"`     // 开票日期
	SellerName    string  `json:"seller_name"`    // 卖方名称
	SellerTaxID   string  `json:"seller_tax_id"`  // 卖方税号
	ItemName      string  `json:"item_name"`      // 项目名称
	UnitPrice     float64 `json:"unit_price"`     // 单价
	Quantity      int     `json:"quantity"`       // 数量
	Amount        float64 `json:"amount"`         // 金额
	TaxRate       float64 `json:"tax_rate"`       // 税率
	TaxAmount     float64 `json:"tax_amount"`     // 税额
	TotalAmount   float64 `json:"total_amount"`   // 合计金额
	InvoiceStatus string  `json:"invoice_status"` // 发票状态
	Remarks       string  `json:"remarks"`        // 备注
	InvoiceType   string  `json:"invoice_type"`   // 发票类型
}
