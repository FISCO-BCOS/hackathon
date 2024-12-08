package model

import "gorm.io/gorm"

// Wallet 钱包
type Wallet struct {
	gorm.Model
	Username   string  `json:"user_name" gorm:"type:varchar(100);not null"`   // 用户名
	WalletName string  `json:"wallet_name" gorm:"type:varchar(100);not null"` // 钱包名称
	Balance    float64 `json:"balance" gorm:"type:decimal(20,8);default:0"`   // 余额
}

// 钱包公私钥
type WalletKey struct {
	gorm.Model
	WalletID   uint   `json:"wallet_id" gorm:"type:int;not null"`
	PublicKey  string `json:"public_key" gorm:"type:varchar(1024);not null"`
	PrivateKey string `json:"private_key" gorm:"type:varchar(1024);not null"`
}
