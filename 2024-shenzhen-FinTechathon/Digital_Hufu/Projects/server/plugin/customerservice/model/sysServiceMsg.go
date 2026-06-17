package model

type SysServiceMsg struct {
	Id         uint   `gorm:"primarykey" json:"id"` // 主键ID
	MerchantId uint   `json:"merchant_id" form:"merchant_id" gorm:"default:0;type:int;column:merchant_id;comment:商户id;"`
	Content    string `json:"content" form:"content" gorm:"default:'';type:text;column:content;comment:消息内容;"`
	ServiceId  int64  `json:"service_id" form:"service_id" gorm:"default:0;type:int;column:service_id;comment:客服id;"`
	Uid        int64  `json:"uid" form:"uid" gorm:"default:0;type:int;column:uid;comment:用户id;"`
	IsTourist  uint   `json:"is_tourist" form:"is_tourist" gorm:"default:0;type:tinyint;column:is_tourist;comment:是否游客;"`
	IsView     uint   `json:"is_view" form:"is_view" gorm:"default:0;type:tinyint;column:is_view;comment:是否已读;"`
	AddTime    int    `json:"add_time" form:"add_time" gorm:"default:0;type:int;column:add_time;comment:添加时间;"`
	MsgType    int64  `json:"msg_type" form:"msg_type" gorm:"default:1;type:tinyint;column:msg_type;comment:消息类型 1=文字 2=表情 3=图片 4=语音 5=视频 6=商品;"`
	IsKf       int64  `json:"is_kf" form:"is_kf" gorm:"default:0;type:tinyint;column:is_kf;comment:是否客服消息;"`
}

func (SysServiceMsg) TableName() string {
	return "sys_service_msg"
}
