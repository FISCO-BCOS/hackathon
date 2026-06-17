package controller

import (
	"fmt"
	"hufu/config"
	"hufu/errors"
	"hufu/model"
	"hufu/utils"
	"time"

	"gorm.io/gorm"
)

const (
	TransactionStatusFailed  = "failed"
	TransactionStatusSuccess = "completed"
)

const ProxyWalletCount = 3

// NormalTransfer 普通转账,不经过代理钱包
func NormalTransfer(from *model.Wallet, to *model.Wallet, amount float64) (*model.Transaction, error) {
	tx := model.DB.Begin()
	defer func() {
		if r := recover(); r != nil {
			tx.Rollback()
		}
	}()

	originalTx, err := createOriginalTransaction(tx, from, to, amount)
	if err != nil {
		tx.Rollback()
		return nil, err
	}

	if err := updateWalletBalances(tx, from, to, amount); err != nil {
		tx.Rollback()
		return nil, err
	}

	originalTx.Status = TransactionStatusSuccess
	if err := tx.Save(originalTx).Error; err != nil {
		tx.Rollback()
		return nil, err
	}

	return originalTx, tx.Commit().Error
}

// ProxyTransfer 代理转账, 经过代理钱包
func ProxyTransfer(from *model.Wallet, to *model.Wallet, amount float64) (*model.Transaction, error) {
	tx := model.DB.Begin()
	defer func() {
		if r := recover(); r != nil {
			tx.Rollback()
		}
	}()

	// 1. 创建并保存原始交易
	originalTx, err := createOriginalTransaction(tx, from, to, amount)
	if err != nil {
		tx.Rollback()
		return nil, err
	}

	// 2. 验证交易合规性
	if err := validateTransaction(tx, from, originalTx); err != nil {
		// 如果是不合规的交易，直接提交事务并返回
		// 因为validateTransaction已经更新了交易状态并创建了异常记录
		if err := tx.Commit().Error; err != nil {
			tx.Rollback()
			return nil, err
		}
		return originalTx, nil
	}

	// 3. 创建关联交易记录
	if err := createAssociatedTransactions(tx, originalTx); err != nil {
		tx.Rollback()
		return nil, err
	}

	// 4. 更新钱包余额
	if err := updateWalletBalances(tx, from, to, amount); err != nil {
		tx.Rollback()
		return nil, err
	}

	// 5. 完成交易
	if err := finalizeTransaction(tx, originalTx); err != nil {
		tx.Rollback()
		return nil, err
	}

	return originalTx, tx.Commit().Error
}

// GetTransferHistory 获取转账历史
func GetTransferHistory(walletID uint, page, pageSize int) (*model.PageResult, error) {
	var total int64

	// 定义包含用户名的查询结果结构
	type TransactionWithUsername struct {
		model.Transaction
		FromUsername string `json:"from_username"`
		ToUsername   string `json:"to_username"`
	}

	var transactions []TransactionWithUsername
	offset := (page - 1) * pageSize

	// 获取总数
	if err := model.DB.Model(&model.Transaction{}).
		Where("(from_wallet_id = ? OR to_wallet_id = ?) AND type = ?",
			walletID, walletID, model.DirectTransaction).
		Count(&total).Error; err != nil {
		return nil, err
	}

	// 使用JOIN查询获取用户名
	if err := model.DB.Table("transactions").
		Select("transactions.*, fw.username as from_username, tw.username as to_username").
		Joins("LEFT JOIN wallets fw ON transactions.from_wallet_id = fw.id").
		Joins("LEFT JOIN wallets tw ON transactions.to_wallet_id = tw.id").
		Where("(transactions.from_wallet_id = ? OR transactions.to_wallet_id = ?) AND transactions.type = ?",
			walletID, walletID, model.DirectTransaction).
		Limit(pageSize).
		Offset(offset).
		Order("transactions.created_at DESC").
		Find(&transactions).Error; err != nil {
		return nil, err
	}

	return &model.PageResult{
		List:     transactions,
		Total:    total,
		Page:     page,
		PageSize: pageSize,
	}, nil
}

// createOriginalTransaction 创建原始交易记录
func createOriginalTransaction(tx *gorm.DB, from *model.Wallet, to *model.Wallet, amount float64) (*model.Transaction, error) {
	originalTx := &model.Transaction{
		FromWalletID: from.ID,
		ToWalletID:   to.ID,
		Amount:       amount,
		Type:         model.DirectTransaction,
		Status:       "pending",
	}
	return originalTx, tx.Create(originalTx).Error
}

// validateTransaction 验证交易合规性
func validateTransaction(tx *gorm.DB, from *model.Wallet, originalTx *model.Transaction) error {
	regulator := NewRegulator()
	if err := regulator.CheckTransaction(originalTx, from); err != nil {
		// 生成证据和签名
		evidence := fmt.Sprintf("Transaction ID: %d, From: %d, To: %d, Amount: %f, Time: %s, Error: %v",
			originalTx.ID, originalTx.FromWalletID, originalTx.ToWalletID,
			originalTx.Amount, time.Now().Format(time.RFC3339), err)

		// 使用监管者的私钥对证据进行签名
		signature, err := utils.SignData(config.GlobalConfig.Tee.PrivateKey, evidence)
		if err != nil {
			return err
		}

		// 创建异常交易记录
		abnormal := &model.AbnormalTransaction{
			WalletID:      from.ID,
			TransactionID: originalTx.ID,
			Evidence:      evidence,
			Signature:     signature,
		}

		if err := tx.Create(abnormal).Error; err != nil {
			return err
		}

		// 更新原始交易状态为失败
		originalTx.Status = TransactionStatusFailed
		if err := tx.Save(originalTx).Error; err != nil {
			return err
		}

		return errors.ErrTransactionAmountTooLarge
	}
	return nil
}

// createAssociatedTransactions 创建关联交易记录
func createAssociatedTransactions(tx *gorm.DB, originalTx *model.Transaction) error {
	// 创建加密交易记录
	encryptedTx, err := createEncryptedTransaction(originalTx)
	if err != nil {
		return err
	}
	if err := tx.Create(encryptedTx).Error; err != nil {
		return err
	}

	// 创建脱敏交易记录
	desensitizedTx := createDesensitizedTransaction(originalTx)
	if err := tx.Create(desensitizedTx).Error; err != nil {
		return err
	}

	// 创建代交易记录
	proxyTxs, err := createProxyTransactions(originalTx, originalTx.Amount)
	if err != nil {
		return err
	}
	for _, proxyTx := range proxyTxs {
		if err := tx.Create(proxyTx).Error; err != nil {
			return err
		}
	}
	return nil
}

// updateWalletBalances 更新钱包余额
func updateWalletBalances(tx *gorm.DB, from *model.Wallet, to *model.Wallet, amount float64) error {
	if from.Balance < amount {
		return errors.ErrInsufficientBalance
	}

	from.Balance -= amount
	to.Balance += amount

	if err := tx.Save(from).Error; err != nil {
		return err
	}
	return tx.Save(to).Error
}

// finalizeTransaction 完成交易
func finalizeTransaction(tx *gorm.DB, originalTx *model.Transaction) error {
	originalTx.Status = "completed"
	return tx.Save(originalTx).Error
}

// createProxyTransactions 创建代理交易记录
func createProxyTransactions(originalTx *model.Transaction, amount float64) ([]*model.Transaction, error) {
	// 获取代理钱包
	proxyWallets, err := GlobalWalletPool.GetRandomWallets(ProxyWalletCount)
	if err != nil {
		return nil, err
	}

	// 拆分金额
	amounts := splitAmount(amount)
	if len(amounts) != ProxyWalletCount {
		return nil, fmt.Errorf("金额拆分数量与代理钱包数量不匹配")
	}

	proxyTxs := make([]*model.Transaction, 0, ProxyWalletCount*2)

	// 1. 创建从原始钱包到代理钱包的交易
	for i, proxyWallet := range proxyWallets {
		toProxyTx := &model.Transaction{
			FromWalletID: originalTx.FromWalletID,
			ToWalletID:   proxyWallet.ID,
			Amount:       amounts[i],
			Type:         model.ToProxyTransaction,
			Status:       "completed",
		}
		proxyTxs = append(proxyTxs, toProxyTx)

		// 2. 创建代理钱包到目标钱包的交易
		fromProxyTx := &model.Transaction{
			FromWalletID: proxyWallet.ID,
			ToWalletID:   originalTx.ToWalletID,
			Amount:       amounts[i],
			Type:         model.FromProxyTransaction,
			Status:       "completed",
		}
		proxyTxs = append(proxyTxs, fromProxyTx)
	}

	return proxyTxs, nil
}

// splitAmount 将金额拆分成多个小额
func splitAmount(amount float64) []float64 {
	// 这里实现金额拆分的逻辑
	// 示例：简单地将金额平均拆分为3份
	part := amount / 3
	return []float64{part, part, amount - 2*part}
}

func createEncryptedTransaction(t *model.Transaction) (*model.EncryptedTransaction, error) {
	// 从数据库读取公钥和私钥
	walletKey, err := GetWalletKeyByWalletID(t.FromWalletID)
	if err != nil {
		return nil, err
	}

	// 加密交易
	encryptedFromWalletID, err := utils.RSAEncryptWithHexKey(fmt.Sprintf("%d", t.FromWalletID), walletKey.PublicKey)
	if err != nil {
		return nil, err
	}

	encryptedToWalletID, err := utils.RSAEncryptWithHexKey(fmt.Sprintf("%d", t.ToWalletID), walletKey.PublicKey)
	if err != nil {
		return nil, err
	}

	encryptedAmount, err := utils.RSAEncryptWithHexKey(fmt.Sprintf("%f", t.Amount), walletKey.PublicKey)
	if err != nil {
		return nil, err
	}

	return &model.EncryptedTransaction{
		TransactionID:         t.ID,
		EncryptedFromWalletID: encryptedFromWalletID,
		EncryptedToWalletID:   encryptedToWalletID,
		EncryptedAmount:       encryptedAmount,
	}, nil
}

// TODO: 脱敏金额和时间范围需要根据实际情况进行调整
func createDesensitizedTransaction(t *model.Transaction) *model.DesensitizedTransaction {
	return &model.DesensitizedTransaction{
		FromWalletID: t.FromWalletID,
		ToWalletID:   t.ToWalletID,
		AmountRange:  fmt.Sprintf("%f-%f", t.Amount-10, t.Amount+10),
		TimeRange:    fmt.Sprintf("%d-%d", t.CreatedAt.Unix()-10, t.CreatedAt.Unix()+10),
	}
}

// GetEncryptedTransaction 获取加密交易信息
func GetEncryptedTransaction(walletID uint, privateKey string) ([]*model.Transaction, error) {
	walletKey, err := GetWalletKeyByWalletID(walletID)
	if err != nil {
		return nil, err
	}

	if walletKey.PrivateKey != privateKey {
		return nil, errors.ErrPrivateKeyInvalid
	}

	// 从数据库读取
	var txs []*model.Transaction
	if err := model.DB.Where("from_wallet_id = ? OR to_wallet_id = ?", walletID, walletID).Find(&txs).Error; err != nil {
		return nil, err
	}

	res := make([]*model.Transaction, 0)
	for _, tx := range txs {
		if tx.Type == model.DirectTransaction {
			res = append(res, tx)
		}
	}

	return res, nil
}

// GetDesensitizedTransaction 获取脱敏交易记录
func GetDesensitizedTransaction(walletID uint) ([]*model.DesensitizedTransaction, error) {
	var txs []*model.DesensitizedTransaction
	if err := model.DB.Where("from_wallet_id = ? OR to_wallet_id = ?", walletID, walletID).Find(&txs).Error; err != nil {
		return nil, err
	}
	return txs, nil
}

// GetReceivedTransactions 获取收款记录
func GetReceivedTransactions(walletID uint) ([]*model.Transaction, error) {
	var transactions []*model.Transaction

	// 直接查询所有已完成的收款记录
	if err := model.DB.Where("to_wallet_id = ? AND status = ?",
		walletID,
		"completed").
		Find(&transactions).Error; err != nil {
		return nil, err
	}

	return transactions, nil
}

// TransactionStats 交易统计信息
type TransactionStats struct {
	TotalAmount float64 `json:"total_amount"` // 总收入
	TodayAmount float64 `json:"today_amount"` // 今日收入
	WeekAmount  float64 `json:"week_amount"`  // 本周收入
	MonthAmount float64 `json:"month_amount"` // 本月收入
}

// GetTransactionStats 获取交易统计信息
func GetTransactionStats(walletID uint) (*TransactionStats, error) {
	var stats TransactionStats

	// 获取时间范围
	now := time.Now()

	// 今天开始时间（0点）
	todayStart := time.Date(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0, now.Location())

	// 本周开始时间
	offset := int(time.Monday - now.Weekday())
	if offset > 0 {
		offset = -6
	}
	weekStart := time.Date(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0, now.Location()).AddDate(0, 0, offset)

	// 本月开始时间（修正：使用当前月份的第一天）
	monthStart := time.Date(now.Year(), now.Month(), 1, 0, 0, 0, 0, now.Location())

	// 查询所有的交易
	var txs []*model.Transaction
	if err := model.DB.Where("to_wallet_id = ? AND type = ? AND status = ?",
		walletID, model.DirectTransaction, "completed").Find(&txs).Error; err != nil {
		return nil, err
	}

	// 计算总收入
	for _, tx := range txs {
		stats.TotalAmount += tx.Amount
	}

	// 计算今日收入
	for _, tx := range txs {
		if tx.CreatedAt.After(todayStart) {
			stats.TodayAmount += tx.Amount
		}
	}

	// 计算本周收入
	for _, tx := range txs {
		if tx.CreatedAt.After(weekStart) {
			stats.WeekAmount += tx.Amount
		}
	}

	// 计算本月收入
	nextMonth := time.Date(now.Year(), now.Month()+1, 1, 0, 0, 0, 0, now.Location())
	for _, tx := range txs {
		if tx.CreatedAt.After(monthStart) && tx.CreatedAt.Before(nextMonth) {
			stats.MonthAmount += tx.Amount
		}
	}

	return &stats, nil
}

// TransactionData 交易数据结构
type TransactionData struct {
	Date    string  `json:"date"`
	Income  float64 `json:"income"`  // 收入
	Expense float64 `json:"expense"` // 支出
}

// TransactionTrend 交易趋势数据
type TransactionTrend struct {
	WeeklyData  []TransactionData `json:"weeklyData"`
	MonthlyData []TransactionData `json:"monthlyData"`
}

// GetTrend 获取收支趋势
func GetTrend(walletID uint) (*TransactionTrend, error) {
	now := time.Now()

	monthlyData, err := getMonthlyData(walletID, now)
	if err != nil {
		return nil, err
	}

	weeklyData, err := getWeeklyData(walletID, now)
	if err != nil {
		return nil, err
	}

	return &TransactionTrend{
		WeeklyData:  weeklyData,
		MonthlyData: monthlyData,
	}, nil
}

// getMonthlyData 获取月度收支数据（最近30天）
func getMonthlyData(walletID uint, now time.Time) ([]TransactionData, error) {
	var result []TransactionData

	// 获取最近30天的数据
	for i := 29; i >= 0; i-- {
		currentDay := now.AddDate(0, 0, -i)
		dayStart := time.Date(currentDay.Year(), currentDay.Month(), currentDay.Day(), 0, 0, 0, 0, now.Location())
		dayEnd := dayStart.AddDate(0, 0, 1)

		// 查询收入
		var income float64
		if err := model.DB.Model(&model.Transaction{}).
			Select("COALESCE(SUM(amount), 0) as amount").
			Where("to_wallet_id = ? AND status = ? AND created_at >= ? AND created_at < ?",
				walletID, "completed", dayStart, dayEnd).
			Scan(&income).Error; err != nil {
			return nil, err
		}

		// 查询支出
		var expense float64
		if err := model.DB.Model(&model.Transaction{}).
			Select("COALESCE(SUM(amount), 0) as amount").
			Where("from_wallet_id = ? AND status = ? AND created_at >= ? AND created_at < ?",
				walletID, "completed", dayStart, dayEnd).
			Scan(&expense).Error; err != nil {
			return nil, err
		}

		result = append(result, TransactionData{
			Date:    dayStart.Format("01-02"),
			Income:  income,
			Expense: expense,
		})
	}

	return result, nil
}

// getWeeklyData 获取周收支数据（最近7天）
func getWeeklyData(walletID uint, now time.Time) ([]TransactionData, error) {
	var result []TransactionData

	// 获取最近7天的数据
	for i := 6; i >= 0; i-- {
		currentDay := now.AddDate(0, 0, -i)
		dayStart := time.Date(currentDay.Year(), currentDay.Month(), currentDay.Day(), 0, 0, 0, 0, now.Location())
		dayEnd := dayStart.AddDate(0, 0, 1)

		// 查询收入
		var income float64
		if err := model.DB.Model(&model.Transaction{}).
			Select("COALESCE(SUM(amount), 0) as amount").
			Where("to_wallet_id = ? AND status = ? AND created_at >= ? AND created_at < ?",
				walletID, "completed", dayStart, dayEnd).
			Scan(&income).Error; err != nil {
			return nil, err
		}

		// 查询支出
		var expense float64
		if err := model.DB.Model(&model.Transaction{}).
			Select("COALESCE(SUM(amount), 0) as amount").
			Where("from_wallet_id = ? AND status = ? AND created_at >= ? AND created_at < ?",
				walletID, "completed", dayStart, dayEnd).
			Scan(&expense).Error; err != nil {
			return nil, err
		}

		result = append(result, TransactionData{
			Date:    dayStart.Format("01-02"),
			Income:  income,
			Expense: expense,
		})
	}

	return result, nil
}
