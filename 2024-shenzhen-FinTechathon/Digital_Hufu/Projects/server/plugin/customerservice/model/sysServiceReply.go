package model

type SysServiceReply struct {
	Id         int64  `json:"id" form:"id" gorm:"primarykey"`
	ReplyType  int64  `json:"reply_type" form:"reply_type" gorm:"default:1;type:int;column:reply_type;comment:回复类型1文本，2图片;"`
	IsComplete int64  `json:"is_complete" form:"is_complete" gorm:"default:0;type:int;column:is_complete;comment:是否完全匹配0否1是;"`
	Keyword    string `json:"keyword" form:"keyword" gorm:"default:'';type:varchar(255);column:keyword;comment:关键字;"`
	Content    string `json:"content" form:"content" gorm:"default:'';type:text;column:content;comment:回复内容;"`
	AddTime    int64  `json:"add_time" form:"add_time" gorm:"default:0;type:int;column:add_time;comment:添加时间;"`
	AddTimeStr string `json:"add_time_str" form:"add_time_str" gorm:"-"`
	Status     int64  `json:"status" form:"sort" gorm:"default:0;type:tinyint(1);column:status;comment:是否显示;"`
}

func (SysServiceReply) TableName() string {
	return "sys_service_reply"
}
