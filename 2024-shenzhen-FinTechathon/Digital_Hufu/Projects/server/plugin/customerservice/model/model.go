package model

type PageInfo struct {
	Page    int    `json:"page" form:"page"`
	Limit   int    `json:"limit" form:"limit"`
	Keyword string `json:"keyword" form:"keyword"`
}

type MsgPageInfo struct {
	Page   int `json:"page" form:"page"`
	Limit  int `json:"limit" form:"limit"`
	FromId int `json:"from_id" form:"from_id"`
}

type AutoPageInfo struct {
	Page      int    `json:"page" form:"page"`
	Limit     int    `json:"limit" form:"limit"`
	Keyword   string `json:"keyword" form:"keyword"`
	ReplyType int    `json:"reply_type" form:"reply_type"`
}

type LoginInfo struct {
	Account  string `json:"account"  form:"account"`
	Password string `json:"password" form:"password"`
}
