# DecisionStorage

合约名 `DecisionStorage`，用于在区块链上存储和管理决策记录

- **Decision**: 用于表示单个决策记录，包含以下字段：
    - `evidence`: 证据的字符串表示。
    - `nodeID`: 节点的唯一标识符。
    - `timestamp`: 决策记录的时间戳。
    - `approved`: 表示决策是否被批准的布尔值。

# KeyShare

`KeyShare`，用于在区块链上管理密钥共享信息。使用 `Table` 外部合约来实现数据存储和管理。以下是合约的主要功能和结构：

### 事件

- **CreateResult**: 创建表的结果。
- **InsertResult**: 插入记录的结果。
- **UpdateResult**: 更新记录的结果。
- **RemoveResult**: 移除记录的结果。

### 状态变量

- **tm**: `TableManager` 合约的地址，用于管理表。
- **table**: 当前操作的表。
- **TABLE_NAME**: 表的名称，固定为 `"t_key_share"`。