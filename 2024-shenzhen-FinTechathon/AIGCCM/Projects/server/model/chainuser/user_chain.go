// 自动生成模板Userchain
package chainuser

import (
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	
	
)

// 区块链用户 结构体  Userchain
type Userchain struct {
      global.GVA_MODEL
      Name  string `json:"name" form:"name" gorm:"column:name;comment:;size:255;"`  //用户名 
      Biaoshi  string `json:"biaoshi" form:"biaoshi" gorm:"column:biaoshi;comment:;size:255;"`  //用户标识 
      Money  *float64 `json:"money" form:"money" gorm:"column:money;comment:;"`  //余额 
      Workpath  string `json:"workpath" form:"workpath" gorm:"column:workpath;comment:;size:255;"`  //作品路径 
      Password  string `json:"password" form:"password" gorm:"column:password;comment:;size:255;"`  //密码 
      Email  string `json:"email" form:"email" gorm:"column:email;comment:;size:255;"`  //邮箱 
      Organization  string `json:"organization" form:"organization" gorm:"column:organization;comment:;size:255;"`  //所属机构 
      CreatedBy  uint   `gorm:"column:created_by;comment:创建者"`
      UpdatedBy  uint   `gorm:"column:updated_by;comment:更新者"`
      DeletedBy  uint   `gorm:"column:deleted_by;comment:删除者"`
}


// TableName 区块链用户 Userchain自定义表名 user_chain
func (Userchain) TableName() string {
  return "user_chain"
}

