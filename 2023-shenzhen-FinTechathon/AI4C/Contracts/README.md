 
目录
 
1\. 账户合约
 
---
 
**1\. 创建链上账户**
###### 为用户生成链上唯一账户

###### URL
> [http://localhost:10100/account/insert](www.api.com/index.php)
 
###### 支持格式
> JSON
 
###### HTTP请求方式
> post
 
###### 请求参数

| 参数       | 必选 | 类型     | 说明         |
| ---------- | ---- | -------- | ------------ |
| user_id    | ture | string   | 用户id       |
| user_money | ture | *big.Int | 用户账户余额 |
| user_name  | ture | string   | 用户名称     |
| user_icon  | ture | string   | 用户头像地址 |
|            |      |          |              |


###### 返回字段
|  返回字段    | 类型     | 说明         |
| ---------- | ---- | -------- |
|tx   |string    |链上地址   |

---
 
**2\. 查询链上账户**
###### 为用户生成链上唯一账户

###### URL
> [http://localhost:10100/account/select](www.api.com/index.php)
 
###### 支持格式
> JSON
 
###### HTTP请求方式
> post
 
###### 请求参数
|  返回字段    | 类型     | 说明         |
| ---------- | ---- | -------- |                             |
|user_id    |ture    |string|用户id             |

###### 返回字段
|  返回字段    | 类型     | 说明         |
| ---------- | ---- | -------- |
|tx   |string    |链上地址   |
|user_money    |true    |*big.Int   |用户账户余额|
|user_name    |ture    |string|用户名称  |
|user_icon    |ture    |string|用户头像地址 |

---
**3\. 链上账户交易**
###### 为用户生成链上唯一账户

###### URL
> [http://localhost:10100/account/transfer](www.api.com/index.php)
 
###### 支持格式
> JSON
 
###### HTTP请求方式
> post
 
###### 请求参数

| 参数       | 必选 | 类型     | 说明         |
| ---------- | ---- | -------- | ------------ |
| user_id1    | ture | string   | 购买者id       |
| user_id2    | ture | string   | 售卖者id       |
| user_money | ture | *big.Int | 交易金额 |
|            |      |          |              |


###### 返回字段
|  返回字段    | 类型     | 说明         |
| ---------- | ---- | -------- |
|tx   |string    |链上交易地址   |

---
2\. 语义标识版权合约
---
**1\. 版权生成**
###### 为文化作品生成唯一标识，并保存其语义信息

###### URL
> [http://localhost:10100/collection/insert](www.api.com/index.php)
 
###### 支持格式
> JSON
 
###### HTTP请求方式
> post
 
###### 请求参数

| 参数       | 必选 | 类型     | 说明         |
| ---------- | ---- | -------- | ------------ |
| collection_id  | ture | string   | 版权id|
| collection_name  | ture | string   | 文化作品名称       |
| owner_id | ture | string| 作品持有者 |
| certificate_time  | ture | string   |     版权认证时间   |
| certificate_organization  | ture | string   | 版权认证机构       |
| collection_semantic | ture | *big.Int | 作品语义 |
|            |      |          |              |


###### 返回字段
|  返回字段    | 类型     | 说明         |
| ---------- | ---- | -------- |
|tx   |string    |链上交易地址   |

---


**2\. 版权查询**
###### 查询文化作品版权标识及语义信息

###### URL
> [http://localhost:10100/collection/select](www.api.com/index.php)
 
###### 支持格式
> JSON
 
###### HTTP请求方式
> post
 
###### 请求参数

| 参数       | 必选 | 类型     | 说明         |
| ---------- | ---- | -------- | ------------ |
| collection_id  | ture | string   | 版权id|

|            |      |          |              |


###### 返回字段
|  返回字段    | 类型     | 说明         |
| ---------- | ---- | -------- |
| collection_name  | ture | string   | 文化作品名称       |
| owner_id | ture | string| 作品持有者 |
| certificate_time  | ture | string   |     版权认证时间   |
| certificate_organization  | ture | string   | 版权认证机构       |
| collection_semantic | ture | *big.Int | 作品语义 |

---
**3\. 版权更新**
###### 更新文化作品版权标识信息

###### URL
> [http://localhost:10100/collection/update](www.api.com/index.php)
 
###### 支持格式
> JSON
 
###### HTTP请求方式
> post
 
###### 请求参数

| 参数       | 必选 | 类型     | 说明         |
| ---------- | ---- | -------- | ------------ |
| collection_id  | ture | string   | 版权id|
| collection_name  | ture | string   | 文化作品名称       |
| owner_id | ture | string| 作品持有者 |
| certificate_time  | ture | string   |     版权认证时间   |
| certificate_organization  | ture | string   | 版权认证机构       |
| collection_semantic | ture | *big.Int | 作品语义 |
|            |      |          |              |


###### 返回字段
|  返回字段    | 类型     | 说明         |
| ---------- | ---- | -------- |
|tx   |string    |链上交易地址   |

---

**4\. 作品交易，版权更换**
###### 查询文化作品版权标识及语义信息
 
###### URL
> [http://localhost:10100/collection/transfer](www.api.com/index.php)
 
###### 支持格式
> JSON
 
###### HTTP请求方式
> post
 
###### 请求参数

| 参数       | 必选 | 类型     | 说明         |
| ---------- | ---- | -------- | ------------ |
| user_id1  | ture | string   | 购买者id|
| user_id2  | ture | string   | 售卖者id|
| collection_id  | ture | string   | 作品版权id|
| goods  | ture | string   | 交易金额 |
|            |      |          |              |


###### 返回字段
|  返回字段    | 类型     | 说明         |
| ---------- | ---- | -------- |                        |
|:-----   |:------|:-----------------------------   |
|tx   |string    |链上交易地址   |
---

**5\. AIGC版权生成作品分账**
###### AIGC作品生成过程中，与大模型参考的作品所有者进行收益分账
 
###### URL
> [http://localhost:10100/collection/gincome](www.api.com/index.php)
 
###### 支持格式
> JSON
 
###### HTTP请求方式
> post
 
###### 请求参数

| 参数       | 必选 | 类型     | 说明         |
| ---------- | ---- | -------- | ------------ |
| user_id1  | ture | string   | AIGC作品购买者id|
| user_id2  | ture | string   | 参与收益分账的账户id|
| goods  | ture | string   | 分账金额 |
|            |      |          |              |


###### 返回字段
|  返回字段    | 类型     | 说明         |
| ---------- | ---- | -------- |
|tx   |string    |链上交易地址   |
|            |      |          | 
---
