package example

import (
	"github.com/flipped-aurora/gin-vue-admin/server/global"
)

type ExaFileUploadAndDownload struct {
	global.GVA_MODEL
	Name               string  `json:"name" gorm:"comment:文件名"`                // 文件名
	Url                string  `json:"url" gorm:"comment:文件地址"`                // 文件地址
	Tag                string  `json:"tag" gorm:"comment:文件标签"`                // 文件标签
	Key                string  `json:"key" gorm:"comment:编号"`                  // 编号
	Faker              string  `json:"faker" gorm:"comment:缩略图地址"`             //缩略图
	Owner              string  `json:"owner" gorm:"comment:所有者"`               //缩略图
	Certi_organization string  `json:"certi_organization" gorm:"comment:公证单位"` //缩略图
	Keywords           string  `json:"keywords" gorm:"comment:语义信息"`           //缩略图
	Biaoshi            string  `json:"biaoshi" gorm:"comment:文件标识"`            //缩略图
	Price              float64 `json:"price" gorm:"comment:价格"`                //缩略图
}

func (ExaFileUploadAndDownload) TableName() string {
	return "exa_file_upload_and_downloads"
}

type ExaKnowledge struct {
	Keyword string `json:"keywords" gorm:"comment:作品关键词"` // 文件名
}

func (ExaKnowledge) TableName() string {
	return "exa_knowledge"
}
