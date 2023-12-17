package request

import (
	"github.com/flipped-aurora/gin-vue-admin/server/model/chainuser"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/request"
	"time"
)

type UserchainSearch struct{
    chainuser.Userchain
    StartCreatedAt *time.Time `json:"startCreatedAt" form:"startCreatedAt"`
    EndCreatedAt   *time.Time `json:"endCreatedAt" form:"endCreatedAt"`
    request.PageInfo
}
