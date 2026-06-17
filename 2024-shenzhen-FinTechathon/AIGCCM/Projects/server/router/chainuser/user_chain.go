package chainuser

import (
	"github.com/flipped-aurora/gin-vue-admin/server/api/v1"
	"github.com/flipped-aurora/gin-vue-admin/server/middleware"
	"github.com/gin-gonic/gin"
)

type UserchainRouter struct {
}

// InitUserchainRouter 初始化 区块链用户 路由信息
func (s *UserchainRouter) InitUserchainRouter(Router *gin.RouterGroup) {
	chainuseRouter := Router.Group("chainuse").Use(middleware.OperationRecord())
	chainuseRouterWithoutRecord := Router.Group("chainuse")
	var chainuseApi = v1.ApiGroupApp.ChainuserApiGroup.UserchainApi
	{
		chainuseRouter.POST("createUserchain", chainuseApi.CreateUserchain)   // 新建区块链用户
		chainuseRouter.DELETE("deleteUserchain", chainuseApi.DeleteUserchain) // 删除区块链用户
		chainuseRouter.DELETE("deleteUserchainByIds", chainuseApi.DeleteUserchainByIds) // 批量删除区块链用户
		chainuseRouter.PUT("updateUserchain", chainuseApi.UpdateUserchain)    // 更新区块链用户
	}
	{
		chainuseRouterWithoutRecord.GET("findUserchain", chainuseApi.FindUserchain)        // 根据ID获取区块链用户
		chainuseRouterWithoutRecord.GET("getUserchainList", chainuseApi.GetUserchainList)  // 获取区块链用户列表
	}
}
