# Advertise（广告位信息）
合约说明：CRUD合约，已继承了Base合约实现其基础的增删改查接口

**字段说明**

| id              | 广告位的id                        |
| --------------- | --------------------------------- |
| title           | 广告位的标题                      |
| adOwner         | 广告位拥有者的地址                |
| coverPic        | 广告位的封面地址（以URL方式存储） |
| integralNeed    | 需要的积分数量                    |
| integralGet     | 已经募集到的积分数量              |
| participantNeed | 需要参与方的数量                  |
| participants    | 参与方的地址信息                  |
| target          | 面向的人群                        |
| rule            | 投放规则                          |
| startTime       | 开始时间                          |
| endTime         | 结束时间                          |
| timestamp       | 发布时间                          |



## getRestIntegral

#### 功能说明

* 根据id返回该广告位需要的积分和已经募集到的积分

#### 接口说明

**入参**

| 参数      | 类型   | 说明       |
| --------- | ------ | ---------- |
| key_field | string | 表的主键   |
| id        | string | 广告位的id |

**出参**

| 参数         | 类型    | 说明                         |
| ------------ | ------- | ---------------------------- |
| integralNeed | uint256 | 该广告位需要的积分数量       |
| integralGet  | uint256 | 该广告位已经募集到的积分数量 |

**出参示例：**

```json
{
    "5000000000",
    "122656177"
}
```

###  



## updateIntegral

#### 功能说明

* 更新募集到的积分，当广告位获得积分时自动调用

#### 接口说明

**入参**

| 参数         | 类型    | 说明                 |
| ------------ | ------- | -------------------- |
| key_field    | string  | 主键                 |
| id           | string  | 广告位的id           |
| integralNeed | uint256 | 需要的积分数量       |
| integralGet  | uint256 | 已经募集到的积分数量 |

**出参**

null

###  

## application

#### 功能说明

* 广告商申请广告位

#### 接口说明

**入参**

| 参数      | 类型    | 说明         |
| --------- | ------- | ------------ |
| key_field | string  | 表的主键     |
| account   | address | 广告商的地址 |
| targetId  | string  | 广告位的id   |

**出参**

null

###  



###  