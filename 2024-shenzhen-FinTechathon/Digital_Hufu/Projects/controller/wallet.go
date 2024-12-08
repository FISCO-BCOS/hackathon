package controller

import (
	"fmt"
	"hufu/model"
	"hufu/utils"
	"time"
)

type WalletStats struct {
	TodayTransactions int64 `json:"today_transactions"` // 今日交易次数
	TotalTransactions int64 `json:"total_transactions"` // 总交易次数
}

func NewWallet(walletName, username string, balance float64) (*model.Wallet, error) {
	w := &model.Wallet{
		WalletName: walletName,
		Username:   username,
		Balance:    balance,
	}

	// 统计id数量
	var count int64
	if err := model.DB.Model(&model.Wallet{}).Count(&count).Error; err != nil {
		return nil, err
	}

	privateKey, publicKey := utils.GenerateRSAKey(int(count + 1))
	if privateKey == "" || publicKey == "" {
		return nil, fmt.Errorf("failed to generate RSA key")
	}

	if err := model.DB.Create(w).Error; err != nil {
		return nil, err
	}

	walletKey := &model.WalletKey{
		WalletID:   w.ID,
		PublicKey:  publicKey,
		PrivateKey: privateKey,
	}
	if err := model.DB.Create(walletKey).Error; err != nil {
		return nil, err
	}

	return w, nil
}

func GetWalletByWalletName(walletName string) (*model.Wallet, error) {
	var w model.Wallet
	if err := model.DB.Where("wallet_name = ?", walletName).First(&w).Error; err != nil {
		return nil, err
	}
	return &w, nil
}

func GetWalletByUsername(username string) (*model.Wallet, error) {
	var w model.Wallet
	if err := model.DB.Where("username = ?", username).First(&w).Error; err != nil {
		return nil, err
	}
	return &w, nil
}

func GetWalletByID(ID uint) (*model.Wallet, error) {
	var w model.Wallet
	if err := model.DB.Where("id = ?", ID).First(&w).Error; err != nil {
		return nil, err
	}
	return &w, nil
}

func SetWalletBalance(ID uint, balance float64) error {
	return model.DB.Model(&model.Wallet{}).Where("id = ?", ID).Update("balance", balance).Error
}

func GetWalletKeyByWalletID(walletID uint) (*model.WalletKey, error) {
	var wk model.WalletKey
	if err := model.DB.Where("wallet_id = ?", walletID).First(&wk).Error; err != nil {
		return nil, err
	}
	return &wk, nil
}

// UpdateWallet 更新钱包信息
func UpdateWallet(walletID uint, name string, balance float64) error {
	// 首先检查钱包是否存在
	wallet, err := GetWalletByID(walletID)
	if err != nil {
		return fmt.Errorf("wallet not found: %v", err)
	}

	// 更新钱包信息
	wallet.WalletName = name
	wallet.Balance = balance

	// 保存更新
	if err := model.DB.Save(wallet).Error; err != nil {
		return fmt.Errorf("update wallet failed: %v", err)
	}

	return nil
}

func GetWalletStats(walletID uint) (*WalletStats, error) {
	// 获取今天的开始时间（零点）
	now := time.Now()
	todayStart := time.Date(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0, now.Location())

	var todayCount int64
	if err := model.DB.Model(&model.Transaction{}).
		Where("(from_wallet_id = ? OR to_wallet_id = ?) AND created_at >= ? AND type = ?",
			walletID, walletID, todayStart, model.DirectTransaction).
		Count(&todayCount).Error; err != nil {
		return nil, err
	}

	var totalCount int64
	if err := model.DB.Model(&model.Transaction{}).
		Where("(from_wallet_id = ? OR to_wallet_id = ?) AND type = ?",
			walletID, walletID, model.DirectTransaction).
		Count(&totalCount).Error; err != nil {
		return nil, err
	}

	return &WalletStats{
		TodayTransactions: todayCount,
		TotalTransactions: totalCount,
	}, nil
}
