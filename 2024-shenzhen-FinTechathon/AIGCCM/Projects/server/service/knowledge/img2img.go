package knowledge

import (
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/model/knowledge"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/request"
    knowledgeReq "github.com/flipped-aurora/gin-vue-admin/server/model/knowledge/request"
)

type Img2imgService struct {
}

// CreateImg2img 创建图生图记录
// Author [piexlmax](https://github.com/piexlmax)
func (img2imgService *Img2imgService) CreateImg2img(img2img *knowledge.Img2img) (err error) {
	err = global.GVA_DB.Create(img2img).Error
	return err
}

// DeleteImg2img 删除图生图记录
// Author [piexlmax](https://github.com/piexlmax)
func (img2imgService *Img2imgService)DeleteImg2img(img2img knowledge.Img2img) (err error) {
	err = global.GVA_DB.Delete(&img2img).Error
	return err
}

// DeleteImg2imgByIds 批量删除图生图记录
// Author [piexlmax](https://github.com/piexlmax)
func (img2imgService *Img2imgService)DeleteImg2imgByIds(ids request.IdsReq) (err error) {
	err = global.GVA_DB.Delete(&[]knowledge.Img2img{},"id in ?",ids.Ids).Error
	return err
}

// UpdateImg2img 更新图生图记录
// Author [piexlmax](https://github.com/piexlmax)
func (img2imgService *Img2imgService)UpdateImg2img(img2img knowledge.Img2img) (err error) {
	err = global.GVA_DB.Save(&img2img).Error
	return err
}

// GetImg2img 根据id获取图生图记录
// Author [piexlmax](https://github.com/piexlmax)
func (img2imgService *Img2imgService)GetImg2img(id uint) (img2img knowledge.Img2img, err error) {
	err = global.GVA_DB.Where("id = ?", id).First(&img2img).Error
	return
}

// GetImg2imgInfoList 分页获取图生图记录
// Author [piexlmax](https://github.com/piexlmax)
func (img2imgService *Img2imgService)GetImg2imgInfoList(info knowledgeReq.Img2imgSearch) (list []knowledge.Img2img, total int64, err error) {
	limit := info.PageSize
	offset := info.PageSize * (info.Page - 1)
    // 创建db
	db := global.GVA_DB.Model(&knowledge.Img2img{})
    var img2imgs []knowledge.Img2img
    // 如果有条件搜索 下方会自动创建搜索语句
    if info.StartCreatedAt !=nil && info.EndCreatedAt !=nil {
     db = db.Where("created_at BETWEEN ? AND ?", info.StartCreatedAt, info.EndCreatedAt)
    }
	err = db.Count(&total).Error
	if err!=nil {
    	return
    }

	if limit != 0 {
       db = db.Limit(limit).Offset(offset)
    }
	
	err = db.Find(&img2imgs).Error
	return  img2imgs, total, err
}
