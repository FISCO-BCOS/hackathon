package model

type SysServiceRecord struct {
	Id          uint   `json:"id" form:"id" gorm:"primarykey"`
	ServiceId   int64  `json:"service_id" form:"service_id" gorm:"default:0;type:int;column:service_id;comment:客服id;"`
	Uid         int64  `json:"uid" form:"uid" gorm:"default:0;type:int;column:uid;comment:用户id;"`
	Avatar      string `json:"avatar" form:"avatar" gorm:"default:'';type:varchar(255);column:avatar;comment:用户头像;"`
	Nickname    string `json:"nickname" form:"nickname" gorm:"default:'';type:varchar(255);column:nickname;comment:用户昵称;"`
	Online      uint   `json:"online" form:"online" gorm:"default:0;type:tinyint;column:online;comment:是否在线;"`
	IsTourist   uint   `json:"is_tourist" form:"is_tourist" gorm:"default:0;type:tinyint;column:is_tourist;comment:是否游客0：否；1：是;"`
	Message     string `json:"message" form:"message" gorm:"default:'';type:text;column:message;comment:最新一条消息;"`
	AddTime     int64  `json:"add_time" form:"add_time" gorm:"default:0;type:int;column:add_time;comment:添加时间;"`
	UpdateTime  int64  `json:"update_time" form:"update_time" gorm:"default:0;type:int;column:update_time;comment:更新时间;"`
	MessageType int64  `json:"message_type" form:"message_type" gorm:"default:0;type:tinyint(1);column:message_type;comment:消息类型：1=文字 2=表情 3=图片 4=语音 5=视频 6=商品;"`
	NoRead      int64  `json:"no_read" gorm:"-"`
	AddTimeStr  string `json:"add_time_str" gorm:"-"`
}

func (SysServiceRecord) TableName() string {
	return "sys_service_record"
}
