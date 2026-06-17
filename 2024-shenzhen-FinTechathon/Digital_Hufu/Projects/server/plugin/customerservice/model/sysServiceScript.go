package model

type SysServiceScript struct {
	Id         int64  `json:"id" form:"id" gorm:"primarykey"`
	ServiceId  int64  `json:"service_id" form:"service_id" gorm:"default:0;type:int;column:service_id;comment:客服id为0说明是公共话术;"`
	Title      string `json:"title" form:"title" gorm:"default:'';type:varchar(255);column:title;comment:话术标题;"`
	Content    string `json:"content" form:"content" gorm:"default:'';type:text;column:content;comment:话术内容;"`
	AddTime    int64  `json:"add_time" form:"add_time" gorm:"default:0;type:int;column:add_time;comment:添加时间;"`
	AddTimeStr string `json:"add_time_str" form:"add_time_str" gorm:"-"`
	Sort       int64  `json:"sort" form:"sort" gorm:"default:0;type:int;column:sort;comment:排序;"`
}

func (SysServiceScript) TableName() string {
	return "sys_service_script"
}
