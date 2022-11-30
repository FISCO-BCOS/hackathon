## Insurance.sol
---
主要赔保合约，保险一方生成保单并将保单信息上链，购保方可进行查询，同时，拥有权限的监管企业可以凭借数据做出公平判决。

**event**
```
    event NewInsurance(uint256 indexed _insuranceId, address indexed _beneficiary);
    event CensorInsurance(uint256 indexed _insuranceId, bool result);
    event ExecuteInsurance(uint256 indexed _insuranceId, uint value);
    event InsuranceExpired(uint256 indexed _insuranceId);
    event InsuranceNotExpired(uint256 indexed _insuranceId);
    event Withdraw(address indexed _adminAddress, uint value);
```

**Variables**

- 合约变量
```
WeatherCheck weatherCheck;//天气判断
```

- 结构体变量
```
struct Insurance {
        uint256 insuranceId;//合于保单Id
        address beneficiaryAddress;//受保方
        uint compensation;//赔保金额
        bool insuranceStatus;//赔保状态
        bool censorStatus;//是否审查
        uint startTime;//开始事件
        uint endTime;//结束事件
        bool isExpired;//合约保单是否过期
        string insuranceUri;//合约保单更多详细信息地址
    }//保单结构体
```

- 地址变量
```
    address private adminAddress;//提供保单方权限者地址
```

- 常量
```
    uint constant private basicMoney = 1000;//固定基础金额
```

- 映射
```
    mapping(uint256 => Insurance) public idOfInsurance;//合约保单Id对应保单的映射
    mapping(uint256 => bool) public isExist;//是否存在此合约保单
    mapping(address => mapping(uint256 => uint256)) private ownerOfInsurance;//查找合约的拥有者
    mapping(address => uint256) private numberOfInsurance;//账户的合约数量    
```

**modifyer**

- isOnlyAdmin();  
```
//提供保单方权限设置
```

- isApprove(uint256 _insuranceId);  
```
//权限控制
```

**constructor**
```
constructor(address _supervision, WeatherCheck _weathercheck) Supervision(_supervision) public;
//初始化监管者地址、保单方地址、预言机合约地址
```
|     参数        |    含义       | 
|  ----           |   ----        | 
| _supervision    | 监管者地址     | 
| _weathercheck  | 天气判断合约地址 |

**Functions**

- createInsurance():生成单个合约保单，返回检索Id。
```
function createInsurance(address _beneficiary, uint _duration, string memory _insuranceUri) public isOnlyAdmin returns(uint256);
```
|     参数       |    含义     | 
|  ----          |   ----      | 
| _beneficiary   |  赔保人     | 
| _duration      |  有效期     |
| _insuranceUri  | 更多uri信息 |

- censorInsurance():审查保单并予以赔付等级。
```
function censorInsurance(uint256 _insuranceId) public isApprove(_insuranceId);
```
|     参数       |    含义     | 
|  ----          |   ----      | 
| _insuranceId   | 合约保单Id  | 
| _duration      | 有效期      |
| _insuranceUri  | 更多uri信息 |


- executeInsurance():改变合约保单状态。
```
function executeInsurance(uint256 _insuranceId) public isApprove(_insuranceId);
```

- findInsurance()：查找已生成的合约保单。
```
function findInsurance(uint256 _insuranceId) external view returns (uint256 insuranceId, address beneficiaryAddress, uint compensation, bool insuranceStatus, bool censorStatus,uint startTime, uint endTime, string memory insuranceUri);
``` 

- checkEndTime():检查是否过期。
```
function checkEndTime(uint256 _insuranceId) internal returns (bool);
```

- getListOfInsurance()：获取赔保方合约保单列表。
```
function getListOfInsurance(address _user) external view returns(uint256[] memory);
```

## ClaimInterface.sol
---
赔偿方案的合约接口，通过对方法的重写来实现对应的理赔方案。

**interface**
```
interface ClaimScheme {
    function firstLevel(uint _basicMoney) external pure returns(uint);
    function secondLevel(uint _basicMoney) external pure returns(uint);
    function thirdLevel(uint _basicMoney) external pure returns(uint);
    function forthLevel(uint _basicMoney) external pure returns(uint);
}
```

## Poverty.sol
---
用户合约，定义了用户可以执行的操作行为。  

**event**
```
 event Bind(uint256 indexed Id, address indexed poverty);
```

**Variables**

- 合约变量
```
Insurances private insurance;//保单合约实例
```

- 结构体变量
```
struct _insurance{
        uint256 insuranceId;//合约保单单号
        uint compensation;//赔款金额
        bool insuranceStatus;//保险是否理赔
        string insuranceUri;//保存的文本=>更多的保险合同的信息
    }
```

- 映射
```
mapping(uint256 => _insurance) public idToInsurance;//订单编号对应合约保单
```

**constructor**
```
constructor (Insurances _insurance) public;
//初始化保单方地址
```

**Functions**
- povertyBindInsurance():绑定赔保方的保单信息。
```
function povertyBindInsurance(uint256 _insuranceId) public returns(bool);
```  

## Supervison.sol
---
监管账户合约，拥有最高权限,能够介入赔保方和合约方的理赔过程。


**Variables**

- 地址变量
```
address private _supervision;//监管者地址
```

- 构造器
```
constructor (address supervision) public;//初始化监管者地址
```

**modifyer**
```
modifier onlySupervision();//权限控制
```

**function**
- getSupervision():获取监管者地址。
```    
function getSupervision() public view returns(address);
```

- isSupervision(): 调用者是否为监管者。
```
function isSupervision() public view returns(bool);
```

## WeatherCheck.sol
---
气象情况判定合约，完成对气象灾害的分类并进行赔偿金额的计算功能。

**Variables**

- 合约变量
```
Claimitic public claimitic;//气象组织合约实例
```

- 普通变量
```
uint count = 0;//计数器
```

- 映射
```
mapping(uint256 => uint256) private scheme;//计数器对应方案
mapping(uint256 => uint256) private time;//计数器对应时间戳
```

**constructor**
```
constructor(Claimitic _claimAddress) public;
//初始化气象组织合约实例地址
```

**function**

- setDisasterLevel():上传与灾害情况对应的事件。
``` 
function setDisasterLevel(uint16 _year, uint8 _month, uint8 _day, uint _level) public returns(bool); 
```
|  参数 |    含义       | 
|  ---- |  ----        | 
| _year | 时间单位      | 
| _month| 时间单位      |
|_level | 灾害评判等级  |


- getLevel():查询是否发生了气象灾害。 
```
function getLevel(uint _startTime, uint _endTime) internal view returns(uint);
``` 

- isLevel():判断是否为所定义的灾害的类型。
```
function isLevel(uint _startTime, uint _endTime) public view returns(bool); 
```
|     参数     |    含义      | 
|  ----        |   ----      | 
| _startTime   | 执行开始时间 | 
| _endTime     | 执行结束时间 |


- computeMoney():执行赔偿金额的计算。
```
function computeMoney(uint _basicMoney, uint _startTime, uint _endTime) public view returns(uint);
```  
|     参数    |    含义      | 
|  ----       |   ----      | 
| _basicMoney | 赔保人       | 
| _startTime  | 执行开始时间 |
| _endTime    | 执行结束时间 |

## SafeMath.sol
```
数学运算工具包
```

## DataTime.sol
```
时间戳工具包
```
