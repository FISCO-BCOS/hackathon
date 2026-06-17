package router

import (
	"github.com/flipped-aurora/gin-vue-admin/server/router/chainuser"
	"github.com/flipped-aurora/gin-vue-admin/server/router/example"
	"github.com/flipped-aurora/gin-vue-admin/server/router/knowledge"
	"github.com/flipped-aurora/gin-vue-admin/server/router/system"
)

type RouterGroup struct {
	System    system.RouterGroup
	Example   example.RouterGroup
	Knowledge knowledge.RouterGroup
	Chainuser chainuser.RouterGroup
}

var RouterGroupApp = new(RouterGroup)
