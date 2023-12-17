// 自动生成模板Img2img
package knowledge

import (
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	
	
)

// 图生图 结构体  Img2img
type Img2img struct {
      global.GVA_MODEL
      Desc  string `json:"desc" form:"desc" gorm:"column:desc;comment:;size:500;"`  //描述词 
      Rdesc  string `json:"rdesc" form:"rdesc" gorm:"column:rdesc;comment:;size:500;"`  //反向描述词 
      Number  string `json:"number" form:"number" gorm:"column:number;comment:;size:20;"`  //生成数量 
      Height  string `json:"height" form:"height" gorm:"column:height;comment:;size:50;"`  //高度 
      Width  string `json:"width" form:"width" gorm:"column:width;comment:;size:50;"`  //宽度 
      Seed  string `json:"seed" form:"seed" gorm:"column:seed;comment:;"`  //种子 
      Steps  string `json:"steps" form:"steps" gorm:"column:steps;comment:;size:50;"`  //步数 
      Cfg_scale  string `json:"cfg_scale" form:"cfg_scale" gorm:"column:cfg_scale;comment:;size:50;"`  //文本引导强度 
      Pic  string `json:"pic" form:"pic" gorm:"column:pic;comment:;size:500;"`  //参考图片 
      Output  string `json:"output" form:"output" gorm:"column:output;comment:;"`  //输出 
      Picurl  string `json:"picurl" form:"picurl" gorm:"column:picurl;comment:;size:500;"`  //参考图片地址 
      Outputurl  string `json:"outputurl" form:"outputurl" gorm:"column:outputurl;comment:;size:500;"`  //输出地址 
}


// TableName 图生图 Img2img自定义表名 img2img
func (Img2img) TableName() string {
  return "img2img"
}

