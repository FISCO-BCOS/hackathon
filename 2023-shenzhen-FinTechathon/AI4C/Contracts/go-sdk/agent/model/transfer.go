package model

import "math/big"

type Transfer struct {
	UserId1 string   `json:"user_id1"`
	UserId2 string   `json:"user_id2"`
	CollectionId string   `json:"collection_id"`
	Goods   *big.Int `json:"goods"`
}
