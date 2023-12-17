#### AI4C-面向AIGC创作的文化数字产品运营平台

#### 项目说明

本项目是AI4C-面向AIGC创作的文化数字产品运营平台演示demo，包含了多模态语义抽取与语义知识图谱构建、AIGC版权生成及交易等软件功能的实现，DPU硬件可信计算功能由于其特殊性无法在线上很好展示。

#### 环境依赖

##### 前端环境

1. 前往https://nodejs.org/zh-cn/下载当前版本node
2. 命令行运行 `node -v` 若控制台输出版本号则前端环境搭建成功
3. node 版本需大于 `16.4`
4. 开发工具推荐vscode https://code.visualstudio.com/



##### 后端环境

1. 下载golang安装 版本号需>=1.18
   - 国际: https://golang.org/dl/
   - 国内: https://golang.google.cn/dl/
2. 命令行运行 go 若控制台输出各类提示命令 则安装成功 输入 go version 确认版本大于 `1.18`
3. 开发工具推荐 [Goland](https://www.jetbrains.com/go/)



##### 使用说明

 需要本地具有 `git` `node` `golang` 环境

- node版本 >= 16.14.2
- golang版本 >= v1.18
- mysql 引擎需要是 innoDB
- IDE推荐：Goland
- 各位在clone项目以后，最好前往七牛云申请自己的空间地址。
- 替换掉项目中的七牛云公钥，私钥，仓名和默认url地址，以免发生测试文件数据错乱



##### 版本列表

- [main](https://github.com/flipped-aurora/gin-vue-admin/tree/main): 主版本 用于生产环境
- [组合式API多语言(i18n)版](https://github.com/flipped-aurora/gin-vue-admin/tree/i18n-dev-new)
- [声明式API版](https://github.com/flipped-aurora/gin-vue-admin/tree/v2.4.x)
- [声明式API多语言(i18n)版](https://github.com/flipped-aurora/gin-vue-admin/tree/i18n-dev)
- [develop](https://github.com/flipped-aurora/gin-vue-admin/tree/develop): 2.0, 用于测试环境
- [gin-vue-admin_v2_dev](https://github.com/flipped-aurora/gin-vue-admin/tree/gin-vue-admin_v2_dev) (v2.0 [GormV1版本](https://v1.gorm.io/)稳定分支)
- [gva_gormv2_dev](https://github.com/flipped-aurora/gin-vue-admin/tree/gva_gormv2_dev) (v2.0 [GormV2版本](https://v2.gorm.io/)开发分支)

#### 使用方法

在web目录下执行 npm run build 得到 dist文件夹 将dist文件夹上传到服务器 建议使用nginx进行代理 并且设置 proxy 把请求代理到后端



在 server下 go build . 得到一个可执行文件然后将可执行文件和config.yaml 以及 resource 文件夹上传至服务器 三者最好放在同一路径下 最终服务器目录结构如下

    ├── breakpointDir  // 后续断点续传自动生成
    ├── chunk   // 后续断点续传自动生成
    ├── fileDir   // 后续断点续传自动生成
    ├── finish   // 后续断点续传自动生成
    ├── resource
    │   └── 子目录文件					
    ├── dist
    │   └── 子目录文件
    ├── gin-vue-admin
    ├── config.yaml