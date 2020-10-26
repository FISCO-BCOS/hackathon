# 合约接口说明文档

主要合约包括：

（1） TokenTime合约：定义区块链中流通的志愿时（本质是代币），以及志愿时的基本属性（币名，发行量，单位等）与方法（转账，授权等）

（2） P2P任务合约 

（3） 公益项目DAO合约

（4） 公益商品购买合约

（5） 账户类合约：用于存储时间银行区块链上的个体（志愿者/被服务对象）名单，用于管理区块链上的个体信息。

（6） 信用评分合约：用于保存志愿者/服务对象在志愿服务过程中产生信用评分的相关信息，同时对这些信息进行增删查改

## TokenTime合约

### 1、构造函数

功能：用于初始化志愿时代币以及将总代币的数量发给合约创建者

java调用方法：

```java
TokenTime.deploy(
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                        new BigInteger("100"),
                        "testName",
                        "testSymbol"
                ).send();
```

参数列表：

* ``new BigInteger("100")``：发行的志愿时总量
* ``"testName"``：志愿时代币名称
* `"testSymbol"`：志愿时代币符号



### 2、initial_balance

功能：初始化用户的志愿时余额

java调用方法：

```java
tokenTime.initial_balance(address).send();
```

参数列表：

* ``address``：需要初始化志愿时余额的账户地址



### 3、transferTo

功能：转账函数，转移志愿时

java调用方法：

```java
tokenTime.transferTo(to, value).send();
```

参数列表：

* ``to``：转账接受方账户地址
* ``value``：被转移的志愿时额度



### 4、approve

功能：授权函数，表示A给B授权自己账户中的多少志愿时可以给B用

java调用方法：

```java
tokenTime.approve(spender, value).send();
```

参数列表：

* ``spender``：被授权转账的账户地址
* ``value``：授权转账额度



## P2PTime合约

### 1、构造函数

功能：初始化P2P任务的相关信息

java调用方法：

```java
P2PTime.deploy(
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                        _tokenTime,
                        title,
                        price,
                        sort,
                        description
                ).send();
```

参数列表：

* ``_tokenTime``：志愿时代币合约地址
* ``title``：P2P任务标题
* ``price``：任务价格
* ``sort``：任务种类
* ``description``：任务内容描述



### 2、publish

功能：发布志愿服务

java调用方法：

```java
p2pTime.publish().send();
```



### 3、apply

功能：志愿者申请承接该P2P服务任务

java调用方法：

```java
p2pTime.apply().send();
```



### 4、accept

功能：被服务对象指定志愿者为其服务

java调用方法：

```java
p2pTime.accept(volunteerAddress).send();
```

