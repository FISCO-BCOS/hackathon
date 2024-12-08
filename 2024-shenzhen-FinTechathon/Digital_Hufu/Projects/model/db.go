package model

import (
	"fmt"
	"hufu/config"

	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

var DB *gorm.DB

func SetupDB() *gorm.DB {
	cfg := config.GlobalConfig.Database
	dsn := fmt.Sprintf("%s:%s@tcp(%s:%d)/%s?charset=%s&parseTime=True&loc=Local",
		cfg.Username,
		cfg.Password,
		cfg.Host,
		cfg.Port,
		cfg.DBName,
		cfg.Charset,
	)

	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}

	// 按照依赖关系顺序进行迁移
	err = db.AutoMigrate(
		&Wallet{},
		&WalletKey{},
		&Transaction{},
		&EncryptedTransaction{},
		&DesensitizedTransaction{},
		&AbnormalTransaction{},
		&Invoice{},
	)
	if err != nil {
		panic("failed to auto migrate: " + err.Error())
	}

	DB = db
	return db
}
