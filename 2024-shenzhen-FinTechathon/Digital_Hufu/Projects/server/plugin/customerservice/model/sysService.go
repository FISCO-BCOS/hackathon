package model

type SysService struct {
	Id         int64  `json:"id" form:"id" gorm:"primarykey"`
	MerchantId uint   `json:"merchant_id" form:"merchant_id" gorm:"default:0;type:int;column:merchant_id;comment:商户id;"`
	Uid        uint   `json:"uid" form:"uid" gorm:"default:0;type:int;column:uid;comment:用户id;"`
	Online     uint   `json:"online" form:"online" gorm:"default:0;type:tinyint;column:online;comment:客服是否在线;"`
	Account    string `json:"account" form:"account" gorm:"default:'';type:varchar(255);column:account;comment:账户;"`
	Password   string `json:"password" form:"password" gorm:"default:'';type:varchar(255);column:password;comment:密码;"`
	Avatar     string `json:"avatar" form:"avatar" gorm:"default:'';type:varchar(255);column:avatar;comment:头像;"`
	Nickname   string `json:"nickname" form:"nickname" gorm:"default:'';type:varchar(255);column:nickname;comment:客服名称;"`
	AddTime    int64  `json:"add_time" form:"add_time" gorm:"default:0;type:int;column:add_time;comment:添加时间;"`
	Status     *uint  `json:"status" form:"status" gorm:"default:0;type:tinyint(1);column:status;comment:是否显示;"`
}

func (SysService) TableName() string {
	return "sys_service"
}
