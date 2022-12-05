# B-Recycle合约接口说明


## 电池合约模块
### 1. BatteryContract.sol
电池溯源合约，记录电池信息和溯源操作记录。

#### Variables
```
_id：电池id
_batchNo：批次号
_status：池状态 1-待安全审查,2-正常,3-待回收,4-回收中,5-梯次利用,6-拆解
_info：电池信息，json格式存储
_traceData：流转记录
_owner：当前所属方
```

#### Constructor
```
constructor(string id, string batchNo, string info) public
```

#### Functions
#### 1. safeCheck
```
function safeCheck(string remark) public
```
函数说明：电池信息安全审查

#### 2. transfer
```
function transfer(address to, string remark, int16 status) public onlyOwner 
```
函数说明：电池归属流转

#### 3. endLife
```
function endLife(string remark) public onlyOwner
```
函数说明：电池拆解

#### 4. getStatus
```
function getStatus() public view returns(int16)
```
函数说明：查询电池状态

#### 5. getTraceInfo
```
function getTraceInfo() public view returns(TraceData[] memory _data) 
```
函数说明：查询溯源记录

---
## 企业准入模块
### 1. EntAccess.sol
企业准入合约，记录企业准入信息和监管机构审查结果。
#### Variables
```
_enterpriseAddr：企业账号地址
_name：企业名称
_idno：统一社会信用代码
_mobile：电话
_addr：联系地址
_remark：准入备注
_approvalAddr：准入审批方
_accessTime：准入时间
```

#### Constructor
```
constructor(address entAddr, string name, string idno, string mobile, string addr, string remark) public
```

#### Functions
#### 1. getAccessInfo
```
function getAccessInfo() public view returns(address, string, string, address, uint)
```
函数说明：获取准入审查结果

---
## 竞价交易模块
### 1. TradeContract.sol
竞价交易合约，记录梯次利用回收、拆解回收、积分交易的竞价交易信息。
#### Variables
```
_seller：卖方地址
_buyer：买方地址
_info：交易信息，json格式存储，根据不同业务存储不同内容
_status：交易状态：1-竞价中，2-交易成功，3-交易撤回或失败
_lowestAmt：最低金额
_expectAmt：期望金额
_tradeAmt：成交金额
_endTime：竞价结束时间
_tradeTime：交易时间
_highestBuyer：当前最高价买方地址
_highestAmt：当前最高价格
_bidLogs：记录参加过竞价的买方
```

#### Constructor
```
constructor(string info, uint256 lowestAmt, uint256 expectAmt, uint256 auctionDays) public
```

#### Functions
#### 1. bid
```
function bid(uint256 _price) public returns (bool) 
```
函数说明：竞价交易

#### 2. targetDeal
```
function targetDeal(address buyer) public
```
函数说明：指定交易，兼容非竞价交易的交易场景

#### 3. deal
```
function deal() public onlyOwner returns(address buyer, uint256 _tradeAmt)
```
函数说明：交易成交

#### 4. tradeEnd
```
function tradeEnd() public onlyOwner
```
函数说明：交易撤销

---
## 积分模块
#### 1. DAO
DAO合约，记录DAO组织信息。
#### Variables
```
_daoAccount：DAO积分账户
_daoList：DAO列表，存在为true，否则为false
```
#### Functions
#### 1. isDAO
```
function isDAO(address account) public view returns (bool)
```
函数说明：指定账号是否DAO组织

#### 2. addDAO
```
function addDAO(address account) public
```
函数说明：添加DAO组织

#### 3. removeDAO
```
function removeDAO(address account)public onlyDAO
```
函数说明：移除DAO组织

#### 2. PointData
积分定义合约，记录积分相关操作记录。
#### Variables
```
_point：账户的积分
_freezePoint：账户的冻结积分
_accounts：账户列表
_totalAmount1：积分总额池1
_totalAmount2：积分总额池2
```
#### Functions
#### 1. transfer
```
function transfer(address _from, address _to, uint256 _value) public returns(uint, uint, uint, uint) 
```
函数说明：积分转移

#### 2. freeze
```
function freeze(address _addr, uint256 _value) public returns(uint, uint)
```
函数说明：积分冻结

#### 3. unFreeze
```
function unFreeze(address _addr, uint256 _value) public returns(uint, uint)
```
函数说明：积分解冻

#### 4. unFreezeAndTransfer
```
function unFreezeAndTransfer(address _from, address _to, uint256 _value) public returns(uint, uint)
```
函数说明：从冻结积分中转移到指定账户


#### 2. PointController
积分操作合约，进行积分账户的相关操作。
#### Variables
```
_pointData：积分账户合约
_dao：DAO合约
_pointLog：积分操作记录
```
#### Functions
#### 1. register
```
function register() public accountNotExist 
```
函数说明：积分账户注册

#### 2. initPoint
```
function initPoint(uint256 value1, uint256 value2) public onlyDAO 
```
函数说明：每年的初始积分

#### 3. addDAO
```
function addDAO(address account) public
```
函数说明：添加DAO账户

#### 4. getPoint
```
function getPoint(address addr) public view returns (uint256) 
```
函数说明：查询指定账户积分

#### 5. pay
```
function pay(address DAOaddr, uint256 point) public
```
函数说明：企业缴纳积分

#### 6. daoTransfer
```
function daoTransfer(address to, uint256 point) onlyDAO public 
```
函数说明：DAO向目标账户转移积分

#### 7. transfer
```
function transfer(address to, uint256 point) public 
```
函数说明：普通账户之间转移积分

#### 8. freeze
```
function freeze(uint256 point) public
```
函数说明：积分冻结

#### 9. unFreezeAndTransfer
```
function unFreezeAndTransfer(address to, uint256 point) public 
```
函数说明：从冻结积分中转移积分

#### 10. unFreeze
```
function unFreeze(uint256 point) public 
```
函数说明：解冻积分

#### 11. getPointLog
```
function getPointLog(address account) public view returns(PointLog[] memory _data) 
```
函数说明：查询积分记录




