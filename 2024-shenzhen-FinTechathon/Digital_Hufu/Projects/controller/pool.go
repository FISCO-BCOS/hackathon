package controller

import (
	"errors"
	"fmt"
	"hufu/model"
	"log"
	"math/rand"
	"sync"
)

// WalletPool 是一个管理钱包的全局池
type WalletPool struct {
	wallets []*model.Wallet
	mutex   sync.RWMutex
}

// 全局 WalletPool 实例
var GlobalWalletPool *WalletPool

const (
	initCount   = 10
	initBalance = 1000000
	initPrefix  = "ProxyWallet"
)

// InitWalletPool 初始化全局 WalletPool
func InitWalletPool() {
	GlobalWalletPool = &WalletPool{
		wallets: make([]*model.Wallet, 0),
	}

	// 获取所有以 ProxyWallet 开头的钱包
	var existingWallets []*model.Wallet
	if err := model.DB.Where("wallet_name LIKE ?", fmt.Sprintf("%s%%", initPrefix)).Find(&existingWallets).Error; err != nil {
		log.Fatal(err)
	}

	if len(existingWallets) == 0 {
		// 如果池中没有钱包，则创建新的钱包
		for i := 0; i < initCount; i++ {
			wallet, err := NewWallet(fmt.Sprintf("%s%d", initPrefix, i), fmt.Sprintf("proxy-user%d", i), initBalance)
			if err != nil {
				log.Fatal(err)
			}
			GlobalWalletPool.AddWallet(wallet)
		}
	} else {
		GlobalWalletPool.wallets = existingWallets
	}
}

// AddWallet 向池中添加钱包
func (wp *WalletPool) AddWallet(wallet *model.Wallet) {
	wp.mutex.Lock()
	defer wp.mutex.Unlock()
	wp.wallets = append(wp.wallets, wallet)
}

// GetRandomWallet 从池中随机获取一个钱包
func (wp *WalletPool) GetRandomWallet() *model.Wallet {
	wp.mutex.RLock()
	defer wp.mutex.RUnlock()
	if len(wp.wallets) == 0 {
		return nil
	}
	return wp.wallets[rand.Intn(len(wp.wallets))]
}

// GetAllWallets 获取池中所有钱包
func (wp *WalletPool) GetAllWallets() []*model.Wallet {
	wp.mutex.RLock()
	defer wp.mutex.RUnlock()
	return wp.wallets
}

// GetRandomWallets 从池中随机获取指定数量的钱包
func (wp *WalletPool) GetRandomWallets(count int) ([]*model.Wallet, error) {
	wp.mutex.RLock()
	defer wp.mutex.RUnlock()

	if len(wp.wallets) == 0 {
		return nil, errors.New("no wallets in the pool")
	}

	if count >= len(wp.wallets) {
		return nil, errors.New("not enough wallets in the pool")
	}

	selectedWallets := make([]*model.Wallet, count)
	indexes := rand.Perm(len(wp.wallets))
	for i := 0; i < count; i++ {
		selectedWallets[i] = wp.wallets[indexes[i]]
	}

	return selectedWallets, nil
}
