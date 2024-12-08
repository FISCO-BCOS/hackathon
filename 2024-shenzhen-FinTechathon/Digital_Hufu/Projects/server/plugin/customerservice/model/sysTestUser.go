package model

type SysTestUser struct {
	Id       int64  `json:"id" form:"id" gorm:"primarykey"`
	Avatar   string `json:"avatar" form:"avatar" gorm:"default:'';type:varchar(255);column:avatar;comment:头像;"`
	Nickname string `json:"nickname" form:"nickname" gorm:"default:'';type:varchar(255);column:nickname;comment:昵称;"`
}

func (SysTestUser) TableName() string {
	return "sys_test_user"
}
