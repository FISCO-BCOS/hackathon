# 代码说明

本内容为部署教程说明，包含Java后端以及WeIdentity部署

#### Java后端部署

1.  从Gitee上克隆项目到本地：git clone https://gitee.com/Leivon1Z/blockchain-esg.git
2.  修改项目目录下./java-backend/src/main/java/resources/application-prod.yml：

```yml
server:
  port: 2333

bcos:
  key-store-path: {生成用户密钥的路径，如：/Users/admin/account/key}
  config-file: {项目目录/java-backend/config.toml}
  contract-name: ESGPlatform
  contract-address: {部署的合约地址}
  contract-abi: {合约abi的路径，如：/Users/admin/abi/}
```

3. 进入java-backend，执行命令：mvn clean package（需要事先安装maven）
4. 执行命令：nohup java -jar target/java-backend-0.0.1-SNAPSHOT.jar &


#### WeIdentity部署步骤

1.  根据WeIdentity官方文档安装部署工具

https://weidentity.readthedocs.io/zh-cn/latest/docs/weidentity-installation-by-web.html

2. 使用可视化部署的方式部署WeIdentity

​	a)    选择“WeID原始模式”

​	b)    区块链管理员选择“联盟链委员会管理员”，非管理员选择“非联盟链委员会管理员”

​	c)    配置区块链节点，具体参数如下：

​		i.     机构名称：AskChain

​		ii.    通讯ID：1

​		iii.    非国密/国密SSL：非国密

​		iv.    链版本：2.0

​		v.    区块链节点IP和Channel端口：127.0.0.1:20200

​		vi.    ca.crt、sdk.crt、sdk.key等：在部署区块链节点的文件夹下nodes/{服务器IP}/sdk中

​	d)    主群组为1

​	e)    不配置数据库

​	f)     选择“系统自动创建公私钥”

​	g)    如果是管理员，则部署 WeIdentity 智能合约

官方文档网址：https://weidentity.readthedocs.io/zh-cn/latest/docs/deploy-via-web.html

3. 打开网页http://localhost:6021，创建当前用户的WeID
4. 如果是管理员，需要发布CPT

CPT模板如下：

```
{
	"cptType" : "original",
	"$schema" : "http://json-schema.org/draft-04/schema#",
	"description" : "assessment org qualification",
	"title" : "AssessmentQualificationV2.0",
	"type" : "object",
	"properties" : {
		"address" : {
		"description" : "org address",
		"type" : "string"
		},
		"field" : {
		"description" : "E/S/G",
		"type" : "string"
		},
		"level" : {
		"description" : "org level",
		"type" : "integer"
		},
		"name" : {
		"description" : "org name",
		"type" : "string"
		}
	}
}

```

#### WeIdentity-Sample部署步骤

1. 下载WeIdentity-Sample代码：

`git clone https://github.com/WeBankBlockchain/WeIdentity-Sample`

2. 切换到3.1.0分支：

`git checkout release/3.1.0`

3. 修改application.properties配置文件：

   ```properties
   encrypt.type=0
   
   bcos.version=2
   
   deploy.style=blockchain
   ```

4. 将私钥、证书、配置文件等拷贝到当前项目中：

(1)   将weid-build-tools/output/admin中的private_key拷贝至weid-sample/keys/priv/目录下。

(2)   在weid-build-tools的resources目录下，将weidentity.properties 和 fisco.properties 复制到weid-sample的resources目录下。

(3)   在weid-build-tools的resources/conf目录下，将所有FISCO BCOS节点证书文件复制到weid-sample的resources/conf目录下（为了方便，可以直接将整个conf目录拷贝过来）。。

5. 如果您是第一次运行 WeIdentity-Sample，您需要先进行编译：

​	`chmod +x build.sh`

​	`		./build.sh`

6. 启用服务

​	`chmod +x start.sh stop.sh`

​	`./start.sh`
