package request

import (
	"github.com/flipped-aurora/gin-vue-admin/server/model/knowledge"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/request"
	"time"
)

type Img2imgSearch struct{
    knowledge.Img2img
    StartCreatedAt *time.Time `json:"startCreatedAt" form:"startCreatedAt"`
    EndCreatedAt   *time.Time `json:"endCreatedAt" form:"endCreatedAt"`
    request.PageInfo
}
