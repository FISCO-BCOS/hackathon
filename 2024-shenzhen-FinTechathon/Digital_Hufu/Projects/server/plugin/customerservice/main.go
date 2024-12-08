package customerservice

import (
	gvaGlobal "github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/model/system"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/model"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/router"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/customerservice/service/ws"
	"github.com/flipped-aurora/gin-vue-admin/server/plugin/plugin-tool/utils"
	"github.com/gin-gonic/gin"
)

type CustomerServicePlugin struct {
}

func CreateCustomerServicePlug() *CustomerServicePlugin {
	go gvaGlobal.GVA_DB.AutoMigrate(model.SysService{},
		model.SysServiceMsg{},
		model.SysServiceRecord{},
		model.SysTestUser{},
		model.SysServiceReply{},
		model.SysServiceScript{}) // 此处可以把插件依赖的数据库结构体自动创建表 需要填写对应的结构体
	// 下方会自动注册菜单 第一个参数为菜单一级路由信息一般为定义好的组名 第二个参数为真实使用的web页面路由信息
	// 具体值请根据实际情况修改
	utils.RegisterMenus(
		system.SysBaseMenu{
			Path:      "service",
			Name:      "客服管理",
			Hidden:    false,
			Component: "view/routerHolder.vue",
			Sort:      4,
			Meta: system.Meta{
				Title: "客服管理",
				Icon:  "service",
			},
		},
		system.SysBaseMenu{
			Path:      "index",
			Name:      "客服列表",
			Hidden:    false,
			Component: "plugin/customerservice/view/service/index.vue",
			Sort:      1,
			Meta: system.Meta{
				Title: "客服列表",
				Icon:  "service",
			},
		},
		system.SysBaseMenu{
			Path:      "script/list",
			Name:      "客服话术",
			Hidden:    false,
			Component: "plugin/customerservice/view/script/index.vue",
			Sort:      2,
			Meta: system.Meta{
				Title: "客服话术",
				Icon:  "document",
			},
		},
		system.SysBaseMenu{
			Path:      "reply/list",
			Name:      "自动回复",
			Hidden:    false,
			Component: "plugin/customerservice/view/reply/index.vue",
			Sort:      3,
			Meta: system.Meta{
				Title: "自动回复",
				Icon:  "bell-filled",
			},
		},
	)

	// 下方会自动注册api 以下格式为示例格式，请按照实际情况修改
	utils.RegisterApis(
		//system.SysApi{
		//	Path:        "/service/ws",
		//	Description: "用户连接接口",
		//	ApiGroup:    "客服管理",
		//	Method:      "GET",
		//},
		//system.SysApi{
		//	Path:        "/service/serve_ws",
		//	Description: "客服连接接口",
		//	ApiGroup:    "客服管理",
		//	Method:      "GET",
		//},
		//system.SysApi{
		//	Path:        "/service/send_msg",
		//	Description: "发送消息接口",
		//	ApiGroup:    "客服管理",
		//	Method:      "POST",
		//},
		//system.SysApi{
		//	Path:        "/service/get_msg_list",
		//	Description: "消息列表",
		//	ApiGroup:    "客服管理",
		//	Method:      "GET",
		//},
		//system.SysApi{
		//	Path:        "/service/get_msg_user",
		//	Description: "客服聊天用户列表",
		//	ApiGroup:    "客服管理",
		//	Method:      "GET",
		//},
		//system.SysApi{
		//	Path:        "/service/get_kf_info",
		//	Description: "当前客服详情",
		//	ApiGroup:    "客服管理",
		//	Method:      "GET",
		//},
		//system.SysApi{
		//	Path:        "/service/set_msg_view",
		//	Description: "设置已读",
		//	ApiGroup:    "客服管理",
		//	Method:      "GET",
		//},
		system.SysApi{
			Path:        "/service/get_service_list",
			Description: "后台客服列表",
			ApiGroup:    "客服管理",
			Method:      "GET",
		},
		system.SysApi{
			Path:        "/service/save_service",
			Description: "后台客服新增/更新",
			ApiGroup:    "客服管理",
			Method:      "POST",
		},
		system.SysApi{
			Path:        "/service/delete_service",
			Description: "删除客服",
			ApiGroup:    "客服管理",
			Method:      "DELETE",
		},
		system.SysApi{
			Path:        "/service/find_service",
			Description: "客服详情",
			ApiGroup:    "客服管理",
			Method:      "GET",
		},
		system.SysApi{
			Path:        "/service/get_script_list",
			Description: "客服话术列表",
			ApiGroup:    "客服管理",
			Method:      "GET",
		},
		system.SysApi{
			Path:        "/service/save_script",
			Description: "客服话术新增/更新",
			ApiGroup:    "客服管理",
			Method:      "POST",
		},
		system.SysApi{
			Path:        "/service/delete_script",
			Description: "删除客服话术",
			ApiGroup:    "客服管理",
			Method:      "DELETE",
		},
		system.SysApi{
			Path:        "/service/find_script",
			Description: "客服话术详情",
			ApiGroup:    "客服管理",
			Method:      "GET",
		},
		system.SysApi{
			Path:        "/service/auto_reply_list",
			Description: "自动回复列表",
			ApiGroup:    "客服管理",
			Method:      "GET",
		},
		system.SysApi{
			Path:        "/service/save_reply",
			Description: "自动回复新增/更新",
			ApiGroup:    "客服管理",
			Method:      "POST",
		},
		system.SysApi{
			Path:        "/service/delete_reply",
			Description: "删除自动回复",
			ApiGroup:    "客服管理",
			Method:      "DELETE",
		},
		system.SysApi{
			Path:        "/service/find_reply",
			Description: "自动回复详情",
			ApiGroup:    "客服管理",
			Method:      "GET",
		},
		system.SysApi{
			Path:        "/service/admin_login",
			Description: "进入客服工作台",
			ApiGroup:    "客服管理",
			Method:      "GET",
		},
	)
	go ws.Manager.Start()
	go ws.Manager.CheckClientActivity()
	return &CustomerServicePlugin{}
}

func (*CustomerServicePlugin) Register(group *gin.RouterGroup) {
	router.RouterGroupApp.InitCustomerServiceRouter(group)
}

func (*CustomerServicePlugin) RouterPath() string {
	return ""
}
