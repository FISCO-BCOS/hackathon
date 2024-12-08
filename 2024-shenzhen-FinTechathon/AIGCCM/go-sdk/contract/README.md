#### 1.账户合约
---
##### 1. 创建链上账户
###### 为用户生成唯一账户
**URL**
`http://localhost:10100/account/insert`
###### 支持格式
`JSON`
###### HTTP请求方式
`post`
###### 请求参数

| 参数       | 必选  | 类型   | 说明         |
|------------|-------|--------|--------------|
| user_id    | true  | string | 用户id       |
| user_money | true  | int    | 用户账户余额 |
| user_name  | true  | string | 用户名称     |
| user_icon  | true  | string | 用户头像地址 |

###### 返回字段

| 返回字段 | 类型   | 说明     |
|----------|--------|----------|
| tx       | string | 链上地址 |

---
#### 2. 查询链上账户
###### 为用户生成链上唯一账户
**URL**
`http://localhost:10100/account/select`
###### 支持格式
`JSON`
###### HTTP请求方式
`post`
###### 请求参数

| 返回字段  | 类型    | 说明         |
|-----------|---------|--------------|
| tx        | string  | 链上地址     |
| user_money| int     | 用户账户余额 |
| user_name | true    | string       |
| user_icon | true    | string       |

---
#### 3. 链上账户交易
###### 为用户生成链上唯一账户
**URL**
`http://localhost:10100/account/transfer`
###### 支持格式
`JSON`
###### HTTP请求方式
`post`
###### 请求参数

| 参数       | 必选  | 类型    | 说明     |
|------------|-------|---------|----------|
| user_id1   | true  | string  | 购买者id |
| user_id2   | true  | string  | 售卖者id |
| user_money | true  | int| 交易金额 |

###### 返回字段

| 返回字段 | 类型   | 说明         |
|----------|--------|--------------|
| tx       | string | 链上交易地址 |

---
#### 2.语义标识版权合约

##### 1.版权生成
###### 为文化作品生成唯一标识，并保存其语义信息
URL
`http://localhost:10100/collection/insert`
###### 支持格式
`JSON`
###### HTTP请求方式
`post`
###### 请求参数




参数|	必选|	类型|	说明
-|-|-|-
collection_id|	ture|	string|	版权id
collection_name|	ture|	string|	文化作品名称
owner_id|	ture|	string|	作品持有者
certificate_time|	ture|	string|	版权认证时间
certificate_organization|	ture|	string|	版权认证机构
collection_semantic|	ture|	int|	作品描述

###### 返回字段

返回字段|	类型|	说明
-|-|-
tx|	string|	链上交易地址
---
##### 2.版权查询
###### 查询文化作品版权标识及语义信息
URL
`http://localhost:10100/collection/select`
###### 支持格式
`JSON`
###### HTTP请求方式
`post`
###### 请求参数
参数|	必选|	类型|	说明
-|-|-|-
collection_id|	ture|	string|	版权id

###### 返回字段
返回字段|	类型|	说明
-|-|-
collection_name|	ture|	string
owner_id|	ture|	string
certificate_time|	ture|	string
certificate_organization|	ture|	string
collection_semantic|	ture|	int

---
##### 3.版权更新
###### 更新文化作品版权标识信息
URL
`http://localhost:10100/collection/update`
###### 支持格式
`JSON`
###### HTTP请求方式
`post`
###### 请求参数
参数|	必选|	类型|	说明
-|-|-|-
collection_id|	ture|	string|	版权id
collection_name|	ture|	string|	新文化作品名称
owner_id|	ture|	string|	新作品持有者
certificate_time|	ture|	string|	新版权认证时间
certificate_organization|	ture|	string|	新版权认证机构
collection_semantic|	ture|	int|	新作品描述

###### 返回字段
返回字段|	类型|	说明
-|-|-
tx|	string|	链上交易地址

---
##### 4.版权流转
###### 查询文化作品版权标识及语义信息
URL 
`http://localhost:10100/collection/transfer`
###### 支持格式
`JSON`
###### HTTP请求方式
`post`
###### 请求参数
参数|	必选|	类型|	说明
-|-|-|-
collection_id|	ture|	string|	作品版权id
user_id1|	ture|	string|	购买者id
user_id2|	ture|	string	|售卖者id
goods|	ture|	string	|交易金额

###### 返回字段
返回字段|	类型|	说明
-|-|-
tx|	string|	链上交易地址

---
#### 3.启动方式
前端：Projects/web5.0; 执行npm install,npm run serve启动前端

后端：Projects/backend2; go run main.go

后端：Projects/server; go run main.go