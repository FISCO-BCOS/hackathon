## DKSM 合约接口文档

### 概述

DKSM 是一个智能合约，旨在通过去中心化的方式管理 Kerberos 服务。该合约定义了用户注册、身份验证及权限管理相关的功能，包括 AS-EX、TGS-EX 和 AP-EX 的请求与响应操作。

**合约版本**：Solidity 版本：^0.8.16

**许可证**：SPDX-License-Identifier: GPL-3.0

**合约所有者**

合约所有者在部署时自动设定为合约部署者，拥有以下权限：

* 初始化用户 (setup)
* 维护用户信息

**数据结构**

**User (用户结构体)**

| 字段      | 类型    | 描述                     |
| --------- | ------- | ------------------------ |
| user      | address | 用户公钥地址信息         |
| ID        | string  | 用户唯一标识符           |
| Key       | string  | 用户加密密钥             |
| PL        | string  | 权限列表                 |
| lifetime  | string  | 用户生命周期             |
| login     | bool    | 用户登录状态             |
| nonce     | uint256 | 随机数，用于防止重放攻击 |
| Timestamp | uint256 | 用户注册或者操作时间戳   |

**Packet (数据包结构体)**

| 字段      | 类型    | 描述       |
| --------- | ------- | ---------- |
| content   | string  | 消息内容   |
| timestamp | uint256 | 消息时间戳 |

### 主要接口

#### 用户管理

**1. setup**

初始化用户信息。

* **权限**: 仅合约所有者可调用
* **输入参数**:
  * iUser (address): 用户以太坊地址
  * iID (string): 用户唯一标识符
  * iKey (string): 用户加密密钥
  * iPL (string): 权限列表
  * ilifetime (string): 用户生命周期

* **返回值**: 无

**2. get_nonce**

获取指定用户的随机数 (nonce)。

* **权限**: 仅用户本人可调用
* **输入参数**:
  * iID (string): 用户唯一标识符

* **返回值：uint256: 用户的 nonce

**3. getInfo**

获取用户的关键信息。

* **权限**: 无限制
* **输入参数**:
  * iID (string): 用户唯一标识符
* **返回值**:
  * address: 用户地址
  * string: 用户 ID
  * string: 用户密钥
  * string: 权限列表
  * string: 生命周期

#### AS-EX (认证服务器交互)

**1. C_Set_AS_REQ**

设置客户端的 AS-EX 请求。

* **权限**: 仅用户本人可调用
* **输入参数**:
  * IDc (string): 客户端 ID
  * IDas (string): 认证服务器 ID
  * AS_REQ (string): 请求内容
  * _nonce (uint256): 客户端当前随机数
* **返回值**:
  * bool: 操作是否成功

**2. AS_Get_AS_REQ**

认证服务器获取指定客户端的 AS-EX 请求。

* **权限**: 仅认证服务器用户可调用
* **输入参数**:
  * IDc (string): 客户端 ID
  * IDas (string): 认证服务器 ID
* **返回值**:
  * string: 请求内容
  * uint256: 请求时间戳
  * string: 客户端密钥
  * string: 认证服务器密钥

**3. AS_Set_AS_REP**

设置认证服务器的 AS-EX 响应。

* **权限**: 仅认证服务器用户可调用
* **输入参数**:
  * IDc (string): 客户端 ID
  * IDas (string): 认证服务器 ID
  * AS_REP (string): 响应内容
  * _nonce (uint256): 认证服务器当前随机数
* **返回值**:
  * bool: 操作是否成功

**4. C_Get_AS_REP**

客户端获取 AS-EX 响应。

* **权限**: 仅客户端用户可调用
* **输入参数**:
  * IDc (string): 客户端 ID
  * IDas (string): 认证服务器 ID
* **返回值**:
  * string: 响应内容
  * uint256: 响应时间戳

#### TGS-EX (票据授权服务器交互)

**1. C_Set_TGS_REQ**

设置客户端的 TGS-EX 请求。

* **权限**: 仅已登录用户可调用
* **输入参数**:
  * IDc (string): 客户端 ID
  * IDtgs (string): TGS 服务器 ID
  * TGS_REQ (string): 请求内容
  * _nonce (uint256): 客户端当前随机数
* **返回值**:
  * bool: 操作是否成功

**2. TGS_Get_TGS_REQ**

TGS 服务器获取指定客户端的 TGS-EX 请求。

* **权限**: 仅 TGS 服务器用户可调用
* **输入参数**:
  * IDc (string): 客户端 ID
  * IDtgs (string): TGS 服务器 ID
* **返回值**:
  * string: 请求内容
  * uint256: 请求时间戳
  * string: TGS 服务器密钥

**3. TGS_Set_TGS_REP**

设置 TGS-EX 响应。

* **权限**: 仅 TGS 服务器用户可调用
* **输入参数**:
  * IDc (string): 客户端 ID
  * IDtgs (string): TGS 服务器 ID
  * IDv (string): 服务端 ID
  * TGS_REP (string): 响应内容
  * _nonce (uint256): TGS 服务器当前随机数
* **返回值**:
  * bool: 操作是否成功

#### AP-EX (服务端交互)

**1. C_Set_AP_REQ**

设置客户端的 AP-EX 请求。

* **权限**: 仅客户端用户可调用
* **输入参数**:
  * IDc (string): 客户端 ID
  * IDv (string): 服务端 ID
  * AP_REQ (string): 请求内容
  * _nonce (uint256): 客户端当前随机数
* **返回值**:
  * bool: 操作是否成功

**2. S_Get_AP_REQ**

服务端获取 AP-EX 请求。

* **权限**: 仅服务端用户可调用
* **输入参数**:
  * IDc (string): 客户端 ID
  * IDv (string): 服务端 ID
* **返回值**:
  * string: 请求内容
  * uint256: 请求时间戳