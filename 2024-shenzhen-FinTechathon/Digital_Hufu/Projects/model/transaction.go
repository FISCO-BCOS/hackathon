package model

import (
	"gorm.io/gorm"
)

// TransactionType 交易类型
type TransactionType string

const (
	DirectTransaction    TransactionType = "direct"     // 直接交易
	ToProxyTransaction   TransactionType = "to_proxy"   // 转入代理钱包
	FromProxyTransaction TransactionType = "from_proxy" // 代理钱包转出
)

// Transaction 交易记录
type Transaction struct {
	gorm.Model
	FromWalletID uint            `json:"from_wallet_id" gorm:"type:int;not null"`
	ToWalletID   uint            `json:"to_wallet_id" gorm:"type:int;not null"`
	Amount       float64         `json:"amount" gorm:"type:decimal(20,8);not null"`
	Status       string          `json:"status" gorm:"type:varchar(20);not null;default:'pending'"` // pending, completed, failed
	Type         TransactionType `json:"type" gorm:"type:varchar(20);not null"`                     // direct, proxy, mixed
}

// EncryptedTransaction 加密交易
type EncryptedTransaction struct {
	gorm.Model
	TransactionID         uint   `json:"transaction_id" gorm:"type:int;not null"`
	EncryptedFromWalletID string `json:"encrypted_from_wallet_id"`
	EncryptedToWalletID   string `json:"encrypted_to_wallet_id"`
	EncryptedAmount       string `json:"encrypted_amount"`
}

// DesensitizedTransaction 脱敏交易
type DesensitizedTransaction struct {
	gorm.Model
	TransactionID uint   `json:"transaction_id" gorm:"type:int;not null"`
	FromWalletID  uint   `json:"from_wallet_id"`
	ToWalletID    uint   `json:"to_wallet_id"`
	AmountRange   string `json:"amount_range"`
	TimeRange     string `json:"time_range"`
}

// AbnormalTransaction 异常交易
type AbnormalTransaction struct {
	gorm.Model
	WalletID      uint   `json:"wallet_id" gorm:"type:int;not null"`
	TransactionID uint   `json:"transaction_id" gorm:"type:int;not null"`
	Evidence      string `json:"evidence" gorm:"type:text;not null"`  // 证据内容
	Signature     string `json:"signature" gorm:"type:text;not null"` // 监管者签名
}
