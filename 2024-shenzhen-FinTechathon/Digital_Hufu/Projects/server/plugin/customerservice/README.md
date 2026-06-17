[## GVA 图库功能

### 手动安装方法

    1.解压zip获得customerservice文件夹
    2.将 customerservice/web/plugin/customerservice 放置在web/plugin下
    3.将 customerservice/server/plugin/customerservice 放置在server/plugin下

#### 1. 前往GVA主程序下的initialize/router.go 在Routers 方法最末尾按照你需要的及安全模式添加本插件
    PluginInit(PublicGroup, customerservice.CreateCustomerServicePlug())
    到gva系统，角色管理，分配角色的api权限即可，插件会自动注册api，需要手动分配。
    注：会自动生成如下表：sys_service、sys_service_msg、sys_service_record、sys_service_reply、sys_service_script、sys_test_user
### 2. 配置说明

#### 2-1 后台主要功能

    客服管理、客服话术、客服自动回复配置等

#### 2-2 使用说明

    1、在前端vue部分路由需要手动配置：
    web/src/router/index.js下新增如下配置：
    {
        path: '/kefu/login',
        name: 'ServiceLogin',
        component: () => import('@/plugin/customerservice/view/login/index.vue'),
        meta:{
            client:true
        }
    },
    {
        path: '/kefu/main',
        name: 'ServiceMain',
        component: () => import('@/plugin/customerservice/view/chat/index.vue'),
        meta:{
            client:true
        }
    },
    {
        path: '/kefu/test',
        name: 'ServiceUserTest',
        component: () => import('@/plugin/customerservice/view/chat/test.vue'),
        meta:{
            client:true
        }
    },

    2、后台使用方法：
    启动gva项目，安装后在客服列表添加客服，然后可以从客服列表的进入工作台进入客服聊天页，或者打开客服登录页
    http://localhost:8080/#/kefu/login进行登录，在sys_test_user表手动新增测试聊天用户，然后打开链接
    http://localhost:8080/#/kefu/test/?uid=xx,uid的参数为你手动添加的test表自增id，就可以进行聊天测试了
    3、此插件涉及的图片上传使用了插件管理中《图库》插件，可根据自己喜好进行替换
    4、websocket连接的地方在插件view/chat/index.vue和test.vue下，连接地址改成自己项目地址，
      客服：websocket.value = new WebSocket(`ws://localhost:8888/service/serve_ws?token=${token.value}`)
      用户：websocket.value = new WebSocket(`ws://localhost:8888/service/ws?user_id=${uid.value}`)
    5、项目没进行过啥大的测试，仅供参考学习
    
#### 2-3 参数说明
    
### 3. 方法API
    无

]()