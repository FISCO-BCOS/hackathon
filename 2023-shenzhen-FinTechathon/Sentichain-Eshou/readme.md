# SentiChain

2023年微众金融科技大赛区块链赛道项目。是一款基于区块链和情感寄托的二手潮玩交易平台。
项目可分为三个模块，分别是前端，后端以及合约。整个平台建立在Fisco-Bcos v3基础上。

## 前端说明

前端使用nextjs 14.0.3，tailwind css 4.0.0开发

1. 首先运行npm install 安装相关依赖。
2. 在命令行中运行npm run dev启动前端服务。
3. 如果需要打包前端项目，请运行npm run build，然后将打包得到的静态文件放在服务器或后端框架中。

## 后端说明

### 项目结构

- conf: 配置文件
- goods:商品合约文件(.abi,.go.bin,.sol)
- points:积分合约文件(.abi,.go.bin,.sol)
- log:日志文件
- service:后端接口服务代码
- main.go:服务接口代码主文件

### 项目依赖

- github.com/gin-gonic/gin v1.9.1
- github.com/ethereum/go-ethereum v1.13.5 
- github.com/tencentyun/cos-go-sdk-v5
- github.com/FISCO-BCOS/go-sdk v1.0.0

### 服务启动

- export LD_LIBRARY_PATH=/usr/local/lib(运行go run main.go报错则先使用这条命令)
- go run main.go

### 数据库

- 使用docker将mysql部署在服务器上

## 合约说明

本项目包含两个合约，分别是积分合约以及商品合约，使用solidity进行开发。合约通过go-sdk部署在fisco bcos上。

## 区块链环境说明

本项目使用开发部署工具build_chain.sh脚本在本地搭建一条Air版本的4节点的FISCO BCOS链，具体请参考：
https://fisco-bcos-doc.readthedocs.io/zh-cn/latest/docs/quick_start/air_installation.html
