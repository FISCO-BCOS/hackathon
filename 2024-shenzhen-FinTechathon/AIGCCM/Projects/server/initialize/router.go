package initialize

import (
	"net/http"

	swaggerFiles "github.com/swaggo/files"

	"github.com/flipped-aurora/gin-vue-admin/server/docs"
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/middleware"
	"github.com/flipped-aurora/gin-vue-admin/server/router"
	"github.com/gin-gonic/gin"
	ginSwagger "github.com/swaggo/gin-swagger"
)

//

func Routers() *gin.Engine {

	// 设置为发布模
	if global.GVA_CONFIG.System.Env == "public" {
		gin.SetMode(gin.ReleaseMode)	//DebugMode ReleaseMode TestMode
	}

	Router := gin.New()

	if global.GVA_CONFIG.System.Env != "public" {
		Router.Use(gin.Logger(), gin.Recovery())
	}

	InstallPlugin(Router)	//
	systemRouter := router.RouterGroupApp.System
	exampleRouter := router.RouterGroupApp.Example
	//
	// VUE_APP_BASE_API = /
	// VUE_APP_BASE_PATH = http://localhost
	//
	// Router.Static("/favicon.ico", "./dist/favicon.ico")
	// Router.Static("/assets", "./dist/assets")   //
	// Router.StaticFile("/", "./dist/index.html") //

	Router.StaticFS(global.GVA_CONFIG.Local.StorePath, http.Dir(global.GVA_CONFIG.Local.StorePath))	// 为用户头像和文件提供静态地址
	// Router.Use(middleware.LoadTls())  // 如果需要使用https 请打开此中间件 然后前往 core/server.go 将启动模
	// 跨域
	// Router.Use(middleware.Cors()) // 直接
	// Router.Use(middleware.CorsByRules()) // 按照配置
	//global.GVA_LOG.Info("use middleware cors")
	docs.SwaggerInfo.BasePath = global.GVA_CONFIG.System.RouterPrefix
	Router.GET(global.GVA_CONFIG.System.RouterPrefix+"/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))
	global.GVA_LOG.Info("register swagger handler")
	// 方便统一添加由组前缀 多服务器上线使用

	PublicGroup := Router.Group(global.GVA_CONFIG.System.RouterPrefix)
	{
		// 健康监测
		PublicGroup.GET("/health", func(c *gin.Context) {
			c.JSON(http.StatusOK, "ok")
		})
	}
	{
		systemRouter.InitBaseRouter(PublicGroup)			// 注册基功能不做鉴
		systemRouter.InitInitRouter(PublicGroup)			// 动初始化相关
		exampleRouter.InitFileUploadAndDownloadRouter(PublicGroup)	// 文件上传下载功能
	}
	PrivateGroup := Router.Group(global.GVA_CONFIG.System.RouterPrefix)
	PrivateGroup.Use(middleware.JWTAuth()).Use(middleware.CasbinHandler())
	{
		systemRouter.InitApiRouter(PrivateGroup, PublicGroup)		// 注册功能api
		systemRouter.InitJwtRouter(PrivateGroup)			// jwt相关
		systemRouter.InitUserRouter(PrivateGroup)			// 注册用户
		systemRouter.InitMenuRouter(PrivateGroup)			// 注册menu
		systemRouter.InitSystemRouter(PrivateGroup)			// system相关
		systemRouter.InitCasbinRouter(PrivateGroup)			// 权限相关
		systemRouter.InitAutoCodeRouter(PrivateGroup)			// 创建动化代码
		systemRouter.InitAuthorityRouter(PrivateGroup)			// 注册角色
		systemRouter.InitSysDictionaryRouter(PrivateGroup)		// 字典管理
		systemRouter.InitAutoCodeHistoryRouter(PrivateGroup)		// 动化代码历史
		systemRouter.InitSysOperationRecordRouter(PrivateGroup)		// 操作记录
		systemRouter.InitSysDictionaryDetailRouter(PrivateGroup)	// 字典详情管理
		systemRouter.InitAuthorityBtnRouterRouter(PrivateGroup)		// 字典详情管理
		systemRouter.InitChatGptRouter(PrivateGroup)			// chatGpt接口

		exampleRouter.InitCustomerRouter(PrivateGroup)	// 客户

	}
	{
		chainuserRouter := router.RouterGroupApp.Chainuser
		chainuserRouter.InitUserchainRouter(PublicGroup)
	}
	{
		knowledgeRouter := router.RouterGroupApp.Knowledge
		knowledgeRouter.InitImg2imgRouter(PrivateGroup)
	}

	global.GVA_LOG.Info("router register success")
	return Router
}
