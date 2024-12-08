// 自动生成模板UserBlock
package user_block

import (
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	
	
)

// 区块链用户 结构体  UserBlock
type UserBlock struct {
      global.GVA_MODEL
      Name  string `json:"name" form:"name" gorm:"column:name;comment:;size:255;"`  //用户名 
      Biaoshi  string `json:"biaoshi" form:"biaoshi" gorm:"column:biaoshi;comment:;size:255;"`  //用户标识 
      Money  *float64 `json:"money" form:"money" gorm:"column:money;comment:;"`  //余额 
      Workid  *int `json:"workid" form:"workid" gorm:"column:workid;comment:;"`  //作品序号 
      Password  string `json:"password" form:"password" gorm:"column:password;comment:;size:255;"`  //密码 
      Email  string `json:"email" form:"email" gorm:"column:email;comment:;size:255;"`  //邮箱 
      CreatedBy  uint   `gorm:"column:created_by;comment:创建者"`
      UpdatedBy  uint   `gorm:"column:updated_by;comment:更新者"`
      DeletedBy  uint   `gorm:"column:deleted_by;comment:删除者"`
}


// TableName 区块链用户 UserBlock自定义表名 user_block
func (UserBlock) TableName() string {
  return "user_block"
}

