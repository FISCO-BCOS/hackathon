package service

import (
	"github.com/flipped-aurora/gin-vue-admin/server/service/chainuser"
	"github.com/flipped-aurora/gin-vue-admin/server/service/example"
	"github.com/flipped-aurora/gin-vue-admin/server/service/knowledge"
	"github.com/flipped-aurora/gin-vue-admin/server/service/system"
)

type ServiceGroup struct {
	SystemServiceGroup    system.ServiceGroup
	ExampleServiceGroup   example.ServiceGroup
	KnowledgeServiceGroup knowledge.ServiceGroup
	ChainuserServiceGroup chainuser.ServiceGroup
}

var ServiceGroupApp = new(ServiceGroup)
