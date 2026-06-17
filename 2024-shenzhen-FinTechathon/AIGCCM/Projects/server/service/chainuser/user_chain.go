package chainuser

import (
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/model/chainuser"
	chainuserReq "github.com/flipped-aurora/gin-vue-admin/server/model/chainuser/request"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/request"
	"gorm.io/gorm"
)

type UserchainService struct {
}

// CreateUserchain 创建区块链用户记录
// Author [piexlmax](https://github.com/piexlmax)
func (chainuseService *UserchainService) CreateUserchain(chainuse *chainuser.Userchain) (err error) {
	err = global.GVA_DB.Create(chainuse).Error
	return err
}

// DeleteUserchain 删除区块链用户记录
// Author [piexlmax](https://github.com/piexlmax)
func (chainuseService *UserchainService) DeleteUserchain(chainuse chainuser.Userchain) (err error) {
	err = global.GVA_DB.Transaction(func(tx *gorm.DB) error {
		if err := tx.Model(&chainuser.Userchain{}).Where("id = ?", chainuse.ID).Update("deleted_by", chainuse.DeletedBy).Error; err != nil {
			return err
		}
		if err = tx.Delete(&chainuse).Error; err != nil {
			return err
		}
		return nil
	})
	return err
}

// DeleteUserchainByIds 批量删除区块链用户记录
// Author [piexlmax](https://github.com/piexlmax)
func (chainuseService *UserchainService) DeleteUserchainByIds(ids request.IdsReq, deleted_by uint) (err error) {
	err = global.GVA_DB.Transaction(func(tx *gorm.DB) error {
		if err := tx.Model(&chainuser.Userchain{}).Where("id in ?", ids.Ids).Update("deleted_by", deleted_by).Error; err != nil {
			return err
		}
		if err := tx.Where("id in ?", ids.Ids).Delete(&chainuser.Userchain{}).Error; err != nil {
			return err
		}
		return nil
	})
	return err
}

// UpdateUserchain 更新区块链用户记录
// Author [piexlmax](https://github.com/piexlmax)
func (chainuseService *UserchainService) UpdateUserchain(chainuse chainuser.Userchain) (err error) {
	err = global.GVA_DB.Save(&chainuse).Error
	return err
}

// GetUserchain 根据id获取区块链用户记录
// Author [piexlmax](https://github.com/piexlmax)
func (chainuseService *UserchainService) GetUserchain(Name string) (chainuse chainuser.Userchain, err error) {
	err = global.GVA_DB.Where("name = ?", Name).First(&chainuse).Error
	return
}

// GetUserchainInfoList 分页获取区块链用户记录
// Author [piexlmax](https://github.com/piexlmax)
func (chainuseService *UserchainService) GetUserchainInfoList(info chainuserReq.UserchainSearch) (list []chainuser.Userchain, total int64, err error) {
	limit := info.PageSize
	offset := info.PageSize * (info.Page - 1)
	// 创建db
	db := global.GVA_DB.Model(&chainuser.Userchain{})
	var chainuses []chainuser.Userchain
	// 如果有条件搜索 下方会自动创建搜索语句
	if info.StartCreatedAt != nil && info.EndCreatedAt != nil {
		db = db.Where("created_at BETWEEN ? AND ?", info.StartCreatedAt, info.EndCreatedAt)
	}
	err = db.Count(&total).Error
	if err != nil {
		return
	}

	if limit != 0 {
		db = db.Limit(limit).Offset(offset)
	}

	err = db.Find(&chainuses).Error
	return chainuses, total, err
}
