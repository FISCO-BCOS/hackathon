package router

import (
	"github.com/flipped-aurora/gin-vue-admin/server/middleware"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/api"
	serMiddleware "github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/middleware"
	"github.com/gin-gonic/gin"
)

type CustomerServiceRouter struct {
}

func (s *CustomerServiceRouter) InitCustomerServiceRouter(Router *gin.RouterGroup) {
	wsRouter := Router.Group("")
	plugRouter := Router.Group("").Use(serMiddleware.JWTAuthMiddleware())
	privateRouter := Router.Group("").Use(middleware.JWTAuth()).Use(middleware.CasbinHandler())
	plugAdminApi := api.ApiGroupApp.AdminServiceApi
	{
		privateRouter.GET("/service/get_service_list", plugAdminApi.GetServiceList)
		privateRouter.POST("/service/save_service", plugAdminApi.SaveService)
		privateRouter.DELETE("/service/delete_service", plugAdminApi.DeleteService)
		privateRouter.GET("/service/find_service", plugAdminApi.FindService)
		privateRouter.GET("/service/admin_login", plugAdminApi.AdminServiceLogin)
		privateRouter.GET("/service/get_script_list", plugAdminApi.GetScriptList)
		privateRouter.POST("/service/save_script", plugAdminApi.SaveScript)
		privateRouter.DELETE("/service/delete_script", plugAdminApi.DeleteScript)
		privateRouter.GET("/service/find_script", plugAdminApi.FindScript)
		privateRouter.GET("/service/auto_reply_list", plugAdminApi.AutoReplyList)
		privateRouter.POST("/service/save_reply", plugAdminApi.SaveReply)
		privateRouter.DELETE("/service/delete_reply", plugAdminApi.DeleteReply)
		privateRouter.GET("/service/find_reply", plugAdminApi.FindReply)
	}
	plugApi := api.ApiGroupApp.CustomerServiceApi
	{
		plugRouter.POST("/service/send_msg", plugApi.SendMsg)
		plugRouter.GET("/service/get_msg_list", plugApi.GetMsgList)
		plugRouter.GET("/service/get_msg_user", plugApi.GetMsgUser)
		plugRouter.GET("/service/get_kf_info", plugApi.GetKefuInfo)
		plugRouter.GET("/service/set_msg_view", plugApi.SetMsgView)
		plugRouter.POST("/service/upload_file", plugApi.UploadFile)
		plugRouter.GET("/service/get_user_info", plugApi.GetUserInfo)
		plugRouter.GET("/service/get_service_script", plugApi.GetServiceScript)
	}
	wsRouter.GET("/service/serve_ws", plugApi.ServeWsForKefu)
	wsRouter.GET("/service/ws", plugApi.ServeWs)
	wsRouter.POST("/service/account_login", plugAdminApi.AccountServiceLogin)
}
