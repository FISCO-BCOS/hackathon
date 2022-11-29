# B-Recycle电池回收管理平台
[English](README-en.md)

## 项目介绍
**B-Recycle**是一个基于`FISCO BCOS`的区块链电池回收管理平台，使用`Vue+SpringBoot`进行前后端搭建，集成了`FISCO BCOS`的`Java-SDK`
、区块链消息中间件`WeEvent`。
该平台致力于打通动力电池产业链上下游的信息壁垒，以区块链技术助力政府部门监管，推动产业可持续健康发展。


## 项目背景
近年来，由于国家节能减排、实现“双碳”目标的需求，政策持续支持新能源汽车产业发展，新能源汽车销量逐年增长，并且有望在未来保持强势涨幅。相对应地，
动力电池的生产和回收市场也快速增长扩张。这也意味着，未来退役的电池量自然也会跟着上涨。GGII 预计 2025 年我国退役动力电池累计 137.4GWh，
梯次利用与再生利用产值预计超千亿规模。

![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/background1.png?raw=true)

![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/background2.png?raw=true)

规范电池回收市场是新能源车市场可持续发展的重要前提，工信部先后三次发布了《新能源汽车废旧动力蓄电池综合利用行业规范条件》企业名单公告，将格林美等45家动力电池专业回收企业纳入白名单，
进入白名单十分困难，至少需要符合50多项评审条件，这意味着，正规的回收企业需要较高的成本。这就使得电池回收市场出现了严重的“劣币驱逐良币”现象，
即废旧动力电池大部分流入了低成本、不规范的回收小作坊手中。这一方面给正规回收企业带来恶性竞争的生存压力，另一方面也造成了市场秩序的混乱，政府监管困难。
不仅如此，小作坊的不合规处理还将带来资源浪费和环境不友好。因此，如何通过一个更加健全的监管体系来促进动力电池回收的规范化和标准化，以及如何降低电池产业链的成本，
将成为促进新能源电动车推广的关键环节。

![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/background3.jpg?raw=true)


## 项目目标
**B-Recycle电池回收管理平台**项目针对正规动力电池回收企业资质获取难、回收成本高和政府监管困难等动力电池回收市场问题，基于区块链进行技术研发，
并创新设计了**机构准入、激励机制、竞价交易**等平台服务，切实解决以上产业痛点，提供了一个**透明化、可信任、易监管**的链上动力电池回收管理平台，
满足产业目前发展改进的需求，具有实际应用价值和落地可行性，能够为规范动力电池回收市场提供帮助。

## 解决方案
### 1. 电池回收区块链
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/solution1.jpg?raw=true)

### 2. 积分计算模型
- 积分初始化
  ![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/pointmodel1.png?raw=true)
  ![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/pointmodel2.png?raw=true)

- 积分消耗
  ![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/pointmodel3.png?raw=true)

- 积分派发
  ![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/pointmodel4.png?raw=true)

### 3. 系统分层架构
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/solution2.jpg?raw=true)

## 项目结构说明
`Contracts`为本项目涉及到的智能合约，智能合约接口文档见：[READMD](Contracts/README.md)

`Projects`为本项目的系统实现，包含前端、后端、以及相关文档，详细说明：[READMD](Projects/README.md)

## 开源协议
本项目使用了`Apache-2.0 license`协议。