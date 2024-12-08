package controller

import (
	"fmt"
	"time"

	"hufu/model"
	"hufu/supervisor"
	"hufu/utils"
)

type Regulator struct {
	maxTransactionAmount float64
	maxDailyAmount       float64
	suspiciousFrequency  int // 每小时可疑交易频率阈值
}

func NewRegulator() *Regulator {
	return &Regulator{
		maxTransactionAmount: 10000,
		maxDailyAmount:       50000,
		suspiciousFrequency:  100,
	}
}

func (r *Regulator) SendAlert(tx *model.Transaction, evidence string) error {
	return nil
}

func (r *Regulator) CheckTransaction(tx *model.Transaction, w *model.Wallet) error {
	// 检查最大交易金额
	if tx.Amount > r.maxTransactionAmount {
		return fmt.Errorf("交易金额超过允许的最大值: %f", r.maxTransactionAmount)
	}

	// 检查日累计交易金额
	dailyAmount, _ := r.getDailyTransactionAmount(w.ID)
	if dailyAmount+tx.Amount > r.maxDailyAmount {
		return fmt.Errorf("日交易总额超过限制: %f", r.maxDailyAmount)
	}

	// 检查交易频率
	frequency, err := r.getHourlyTransactionFrequency(w.ID)
	if err != nil {
		return fmt.Errorf("获取交易频率失败: %v", err)
	}
	if frequency >= r.suspiciousFrequency {
		// 记录可疑交易
		return fmt.Errorf("交易频率过高")
	}

	return nil
}

// 获取钱包当日交易总额
func (r *Regulator) getDailyTransactionAmount(walletID uint) (float64, error) {
	var totalAmount float64
	today := time.Now().Format("2006-01-02")

	err := model.DB.Model(&model.Transaction{}).
		Where("wallet_id = ? AND DATE(created_at) = ?", walletID, today).
		Select("COALESCE(SUM(amount), 0)").
		Row().
		Scan(&totalAmount)

	return totalAmount, err
}

// 获取每小时交易频率
func (r *Regulator) getHourlyTransactionFrequency(walletID uint) (int, error) {
	var count int64
	oneHourAgo := time.Now().Add(-time.Hour)

	err := model.DB.Model(&model.Transaction{}).
		Where("from_wallet_id = ? AND created_at >= ?", walletID, oneHourAgo).
		Count(&count).Error

	return int(count), err
}

func ProcessPrivateKey(walletID uint, evidence string) ([]string, error) {
	// 获取私钥
	walletKey, err := GetWalletKeyByWalletID(walletID)
	if err != nil {
		return nil, fmt.Errorf("failed to get wallet key: %v", err)
	}

	parts, err := utils.SharePrivateKey(walletKey.PrivateKey)
	if err != nil {
		return nil, fmt.Errorf("failed to share private key: %v", err)
	}

	approved, err := supervisor.JuryInstance.HandleRegulatoryRequest(fmt.Sprintf("wallet-%d-%s", walletID, evidence))
	if err != nil {
		return nil, fmt.Errorf("failed to handle regulatory request: %v", err)
	}

	res := []string{}

	for i, part := range parts {
		if approved[supervisor.JuryInstance.Nodes[i].NodeID] {
			encryptedPart, err := utils.EncryptData(supervisor.JuryInstance.Nodes[i].PublicKey, part)
			if err != nil {
				return nil, fmt.Errorf("failed to encrypt part %d: %v", i, err)
			}
			// 存储加密后的分片
			id := fmt.Sprintf("%d-%d", walletID, i)
			name := fmt.Sprintf("key_share_%s", id)
			err = supervisor.JuryInstance.Nodes[i].StoreEncryptedKeyShare(id, name, encryptedPart)
			if err != nil {
				return nil, fmt.Errorf("failed to store encrypted part %d: %v", i, err)
			}

			res = append(res, part)
		}
	}

	return res, nil
}

// GetAbnormalTransactions 获取所有异常交易
func GetAbnormalTransactions() ([]model.AbnormalTransaction, error) {
	var transactions []model.AbnormalTransaction

	// 从数据库中查询标记为异常的交易
	result := model.DB.Find(&transactions)
	if result.Error != nil {
		return nil, result.Error
	}

	return transactions, nil
}
