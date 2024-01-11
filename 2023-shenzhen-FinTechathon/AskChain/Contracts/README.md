# 合约

## 简介

此部分为我们项目所使用的智能合约、编译后对应的Java合约类与使用展示图例

## 部署教程

1. 根据FISCO BCOS官方文档，下载build_chain.sh

网址：https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html

2. 监管机构建立节点：bash build_chain.sh -l {政府服务器ip}:1
3. 部署智能合约：使用控制台，部署ESGPlatform智能合约
4. 监管机构公布ca.crt和ca.key
5. 企业或评估机构获取ca.crt和ca.key，将他们置于同一目录下
6. 企业或评估机构、项目方建立节点：bash build_chain.sh -l {自身服务器ip}:1 -k {ca.crt/ca.key所在目录}
