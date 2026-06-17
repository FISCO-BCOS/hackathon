# 数据库设计文档

## 1. 钱包相关表

### Wallet (钱包表)
| 字段名 | 类型 | 必填 | 默认值 | 描述 |
|-------|------|------|--------|------|
| id | uint | 是 | 自增 | 主键ID |
| created_at | datetime | 是 | - | 创建时间 |
| updated_at | datetime | 是 | - | 更新时间 |
| deleted_at | datetime | 否 | null | 删除时间 |
| username | varchar(100) | 是 | - | 用户名 |
| wallet_name | varchar(100) | 是 | - | 钱包名称 |
| balance | decimal(20,8) | 是 | 0 | 钱包余额 |

### WalletKey (钱包密钥表)
| 字段名 | 类型 | 必填 | 默认值 | 描述 |
|-------|------|------|--------|------|
| id | uint | 是 | 自增 | 主键ID |
| created_at | datetime | 是 | - | 创建时间 |
| updated_at | datetime | 是 | - | 更新时间 |
| deleted_at | datetime | 否 | null | 删除时间 |
| wallet_id | int | 是 | - | 关联的钱包ID |
| public_key | varchar(1024) | 是 | - | 公钥 |
| private_key | varchar(1024) | 是 | - | 私钥 |

## 2. 交易相关表

### Transaction (交易记录表)
| 字段名 | 类型 | 必填 | 默认值 | 描述 |
|-------|------|------|--------|------|
| id | uint | 是 | 自增 | 主键ID |
| created_at | datetime | 是 | - | 创建时间 |
| updated_at | datetime | 是 | - | 更新时间 |
| deleted_at | datetime | 否 | null | 删除时间 |
| from_wallet_id | int | 是 | - | 转出钱包ID |
| to_wallet_id | int | 是 | - | 转入钱包ID |
| amount | decimal(20,8) | 是 | - | 交易金额 |
| status | varchar(20) | 是 | pending | 交易状态(pending/completed/failed) |
| type | varchar(20) | 是 | - | 交易类型(direct/to_proxy/from_proxy) |

### EncryptedTransaction (加密交易表)
| 字段名 | 类型 | 必填 | 默认值 | 描述 |
|-------|------|------|--------|------|
| id | uint | 是 | 自增 | 主键ID |
| created_at | datetime | 是 | - | 创建时间 |
| updated_at | datetime | 是 | - | 更新时间 |
| deleted_at | datetime | 否 | null | 删除时间 |
| transaction_id | int | 是 | - | 关联的交易ID |
| encrypted_from_wallet_id | varchar(255) | 是 | - | 加密后的转出钱包ID |
| encrypted_to_wallet_id | varchar(255) | 是 | - | 加密后的转入钱包ID |
| encrypted_amount | varchar(255) | 是 | - | 加密后的交易金额 |

### DesensitizedTransaction (脱敏交易表)
| 字段名 | 类型 | 必填 | 默认值 | 描述 |
|-------|------|------|--------|------|
| id | uint | 是 | 自增 | 主键ID |
| created_at | datetime | 是 | - | 创建时间 |
| updated_at | datetime | 是 | - | 更新时间 |
| deleted_at | datetime | 否 | null | 删除时间 |
| transaction_id | int | 是 | - | 关联的交易ID |
| from_wallet_id | int | 是 | - | 转出钱包ID |
| to_wallet_id | int | 是 | - | 转入钱包ID |
| amount_range | varchar(255) | 是 | - | 交易金额范围 |
| time_range | varchar(255) | 是 | - | 交易时间范围 |

### AbnormalTransaction (异常交易表)
| 字段名 | 类型 | 必填 | 默认值 | 描述 |
|-------|------|------|--------|------|
| id | uint | 是 | 自增 | 主键ID |
| created_at | datetime | 是 | - | 创建时间 |
| updated_at | datetime | 是 | - | 更新时间 |
| deleted_at | datetime | 否 | null | 删除时间 |
| wallet_id | int | 是 | - | 相关钱包ID |
| transaction_id | int | 是 | - | 异常交易ID |
| evidence | text | 是 | - | 异常证据内容 |
| signature | text | 是 | - | 监管者签名 |

## 3. 发票相关表

### Invoice (发票表)
| 字段名 | 类型 | 必填 | 默认值 | 描述 |
|-------|------|------|--------|------|
| id | uint | 是 | 自增 | 主键ID |
| created_at | datetime | 是 | - | 创建时间 |
| updated_at | datetime | 是 | - | 更新时间 |
| deleted_at | datetime | 否 | null | 删除时间 |
| invoice_id | varchar(255) | 是 | - | 发票编号 |
| issue_date | varchar(255) | 是 | - | 开票日期 |
| seller_name | varchar(255) | 是 | - | 卖方名称 |
| seller_tax_id | varchar(255) | 是 | - | 卖方税号 |
| item_name | varchar(255) | 是 | - | 项目名称 |
| unit_price | decimal | 是 | - | 单价 |
| quantity | int | 是 | - | 数量 |
| amount | decimal | 是 | - | 金额 |
| tax_rate | decimal | 是 | - | 税率 |
| tax_amount | decimal | 是 | - | 税额 |
| total_amount | decimal | 是 | - | 合计金额 |
| invoice_status | varchar(255) | 是 | - | 发票状态 |
| remarks | varchar(255) | 否 | - | 备注 |
| invoice_type | varchar(255) | 是 | - | 发票类型 |

## 4. 分页相关结构

### PageInfo (分页请求参数)
| 字段名 | 类型 | 描述 |
|-------|------|------|
| page | int | 页码 |
| page_size | int | 每页数量 |

### PageResult (分页返回结果)
| 字段名 | 类型 | 描述 |
|-------|------|------|
| list | interface{} | 数据列表 |
| total | int64 | 总数 |
| page | int | 当前页码 |
| page_size | int | 每页数量 |

## 5. 表关系说明

1. Wallet 与 WalletKey 是一对一关系，通过 wallet_id 关联
2. Transaction 与 EncryptedTransaction 是一对一关系，通过 transaction_id 关联
3. Transaction 与 DesensitizedTransaction 是一对一关系，通过 transaction_id 关联
4. Transaction 与 AbnormalTransaction 是一对一关系，通过 transaction_id 关联
5. Transaction 表中的 from_wallet_id 和 to_wallet_id 都关联到 Wallet 表的 id

## 6. 字段说明补充

### 交易状态(status)说明
- pending: 交易待处理
- completed: 交易已完成
- failed: 交易失败

### 交易类型(type)说明
- direct: 直接交易
- to_proxy: 转入代理钱包
- from_proxy: 代理钱包转出

### 安全性说明
1. WalletKey 表存储了敏感的密钥信息，需要采取额外的加密措施
2. EncryptedTransaction 表用于存储加密后的交易信息，保护交易隐私
3. DesensitizedTransaction 表用于存储脱敏后的交易信息，用于数据分析
4. AbnormalTransaction 表用于记录异常交易，包含证据和监管者签名 