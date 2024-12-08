package model

import "math/big"

type Account struct {
	UserId    string   `json:"user_id"`
	UserMoney *big.Int `json:"user_money"`
	UserName  string   `json:"user_name"`
	UserIcon  string   `json:"user_icon"`
}
