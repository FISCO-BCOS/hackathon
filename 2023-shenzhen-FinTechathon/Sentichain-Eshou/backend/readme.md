# 区块链项目说明文档 

## 项目结构

- goods:商品合约文件(.abi,.go.bin,.sol)
- points:积分合约文件(.abi,.go.bin,.sol)
- service:后端接口服务代码
- main.go:服务接口代码主文件

## 项目依赖

- github.com/gin-gonic/gin v1.9.1
- github.com/ethereum/go-ethereum v1.13.5 
- github.com/tencentyun/cos-go-sdk-v5
- github.com/FISCO-BCOS/go-sdk v1.0.0

## 服务启动

- export LD_LIBRARY_PATH=/usr/local/lib(运行go run main.go报错则先使用这条命令)
- go run main.go

## 数据库

- 使用docker将mysql部署在服务器上
