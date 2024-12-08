package router

import (
	"github.com/FISCO-BCOS/go-sdk/agent/controller"
	"github.com/gin-gonic/gin"
)

func SetupRouter() *gin.Engine {
	controller.PriKeysinit()
	r := gin.Default()
	utilsGroup := r.Group("utils")
	{
		utilsGroup.GET("/ping", controller.Ping)
		utilsGroup.GET("/kvset", controller.Kvset)
		utilsGroup.GET("/kvget", controller.Kvget)
	}
	accountGroup := r.Group("account")
	{
		accountGroup.POST("/insert", controller.AccountInsert)
		accountGroup.POST("/select", controller.AccountSelect)
		accountGroup.POST("/update", controller.AccountUpdate)
		accountGroup.POST("/delete", controller.AccountDelete)
		accountGroup.POST("/transfer", controller.AccountTransfer)
	}
	collectionGroup := r.Group("collection")
	{
		collectionGroup.POST("/insert", controller.CollectionInsert)
		collectionGroup.POST("/select", controller.CollectionSelect)
		collectionGroup.POST("/update", controller.CollectionUpdate)
		collectionGroup.POST("/delete", controller.CollectionDelete)
		collectionGroup.POST("/transfer", controller.CollectionTransfer)
	}
	return r
}
