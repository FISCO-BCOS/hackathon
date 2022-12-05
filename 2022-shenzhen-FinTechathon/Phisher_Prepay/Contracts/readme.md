# 合约接口说明文档

**PrePay项目的合约主要分为两个部分，数据存储合约以及交易功能合约**

### 数据存储合约（CRUD 合约）：

（1）预付卡合约（PrepaidCard）：实现预付卡的办理，将预付卡信息与合同哈希上链存储；查询与更新预付卡信息

（2）商家账户合约（ShopAccounts）：实现商家账户的注册，记录商家的账户余额与杠杆率；查询与更新商家账户信息

（3）服务记录合约（ServiceHistory）：记录消费历史，将每一笔消费的服务记录上链存储，使消费历史可追溯；查询与更新投诉内容

### 交易功能合约：

（4）杠杆合约（Leverage）：根据商家的杠杆率，计算每一笔消费中商家能够获得的实际金额

（5）支付合约（Transfer）：实现消费过程中预付卡账户与商家账户余额的更新，并记录消费历史

（6）退卡合约（Refund）：判断用户是否在七天冷静期内，并计算退款金额、清空预付卡账户余额

（7）申诉合约（Complaint）：将投诉内容上链存储

<br />

## 预付卡合约 PrepaidCard
### 1、createCard
功能：办理预付卡并将相关信息与合同哈希上链存储

```
function createCard(
        string _cardID,
        string _consumerID,
        string _shopID,
        int _balance,
        string _contractHash,
        uint _createTime
        )
```
参数列表：
- `_cardID`：预付卡ID
- `_consumerID`：消费者ID
- `_shopID`：商家ID
- `_balance`：预付卡余额
- `_contractHash`：合同哈希值
- `_createTime`：办卡时间

### 2、selectByCardID
功能：根据预付卡ID查询预付卡信息

```
function selectByCardID(string _cardID)
        public
        returns (
            string cardID,
            string consumerID,
            string shopID,
            string balance,
            int contractHash,
            uint createTime
        )
```
参数列表：
- `_cardID`：预付卡ID

返回值列表：
- `cardID`：预付卡ID
- `consumerID`：消费者ID
- `shopID`：商家ID
- `balance`：预付卡余额
- `contractHash`：合同哈希值
- `createTime`：办卡时间

### 3、changeBalance
功能：更新预付卡的余额

```
function changeBalance(string _cardID, int _value)
```
参数列表：
- `_cardID`：预付卡ID
- `_value`：消费金额
<br />

## 商家账户合约 ShopAccounts
### 1、createAccount
功能：注册商家账户并将相关信息上链存储

```
function createAccount(string _shopID)
```
参数列表：
- `_shopID`：商家ID

### 2、selectByCardID
功能：根据商家ID查询商家账户信息

```
function selectByShopID(string _shopID) public returns(int balance, int leverage)
```
参数列表：
- `_shopID`：商家ID

返回值列表：
- `balance`：商家账户余额
- `leverage`：杠杆率

### 3、changeBalance
功能：更新商家账户的余额

```
function changeBalance(string _shopID, int _value)
```
参数列表：
- `_shopID`：商家ID
- `_value`：消费金额

### 4、changeLeverage
功能：更新商家账户的杠杆率

```
function changeLeverage(string _shopID, int _value)
```
参数列表：
- `_shopID`：商家ID
- `_value`：新的杠杆率
<br />

## 杠杆合约 Leverage
### calculate
功能：根据商家的杠杆率计算交易获得的实际金额

```
function Calculate(string _shopID, int _value) public returns (int amount)
```
参数列表：
- `_shopID`：商家ID
- `_value`：消费金额

返回值列表：
- `amount`：商家实际获得的金额
<br />

## 支付合约 Transfer
### transfer
功能：更新预付卡与商家账户余额，并将消费记录上链

```
function transfer(
        string _cardID,
        string _shopID,
        string _serviceID,
        string _record,
        int _value
    )
```
参数列表：
- `_cardID`：预付卡ID
- `_shopID`：商家ID
- `_serviceID`：服务记录ID
- `_record`：消费记录
- `_value`：消费金额
<br />

## 退卡合约 Refund
### refund
功能：判断是否在七天冷静期内，并返回相应退款金额
```
function refund(string _cardID, uint _now) public returns (bool inSeven, int balance)
```
参数列表：
- `_cardID`：预付卡ID
- `_now`：现在的时间（时间戳）

返回值列表：
- `inSeven`：布尔值，是否在七天冷静期内
- `balance`：退款金额
<br />

## 申诉合约 Complaint
### complaint
功能：将用户对历史消费记录的投诉上链存储
```
function complaint(
        string _serviceID,
        string _content,
        string _cardID,
        uint _now
    )
```
参数列表：
- `_serviceID`：服务记录ID
- `_content`：投诉内容
- `_cardID`：预付卡ID
- `_now`：当前时间（时间戳）
