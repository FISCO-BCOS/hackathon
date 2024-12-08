package knowledge

import (
	"github.com/flipped-aurora/gin-vue-admin/server/api/v1"
	"github.com/flipped-aurora/gin-vue-admin/server/middleware"
	"github.com/gin-gonic/gin"
)

type Img2imgRouter struct {
}

// InitImg2imgRouter 初始化 图生图 路由信息
func (s *Img2imgRouter) InitImg2imgRouter(Router *gin.RouterGroup) {
	img2imgRouter := Router.Group("img2img").Use(middleware.OperationRecord())
	img2imgRouterWithoutRecord := Router.Group("img2img")
	var img2imgApi = v1.ApiGroupApp.KnowledgeApiGroup.Img2imgApi
	{
		img2imgRouter.POST("createImg2img", img2imgApi.CreateImg2img)   // 新建图生图
		img2imgRouter.DELETE("deleteImg2img", img2imgApi.DeleteImg2img) // 删除图生图
		img2imgRouter.DELETE("deleteImg2imgByIds", img2imgApi.DeleteImg2imgByIds) // 批量删除图生图
		img2imgRouter.PUT("updateImg2img", img2imgApi.UpdateImg2img)    // 更新图生图
	}
	{
		img2imgRouterWithoutRecord.GET("findImg2img", img2imgApi.FindImg2img)        // 根据ID获取图生图
		img2imgRouterWithoutRecord.GET("getImg2imgList", img2imgApi.GetImg2imgList)  // 获取图生图列表
	}
}
