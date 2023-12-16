# Wetender合约接口说明文档
## 合约说明
### 部门及管理合约
- ``Bank`` 银行合约
- ``Enterprise`` 企业合约
- ``Town_Gov`` 乡政府合约
- ``County_Gov`` 县政府合约
### 辅助功能合约
- ``Project`` 项目进度合约
- ``Table`` 表合约
- ``Government`` 政府合约

## 项目进度合约(Project)
### 变量
- ``Struct Tracedata``追踪表结构体
    - ``uint public ProjectId``项目编号
    - ``string public ProjectName``项目名称
    - ``uint public _status``项目状态 状态代号：0.已立项; 1.已招标; 2.正在施工; 3.已竣工; 4.已验收; 5.已投入使用; 6.售后修缮;
- ``uint public bal``项目预算
- ``uint used_money``当前使用总预算  
- ``TraceData[] public traceData``追踪表   
- ``address public owner``项目归属方
- ``address public builder``项目承包方

### 初始化
```
constructor(uint id, string name, uint balance) public
```
### 功能函数
1.项目开标函数
```
function Deal(address target, string remark) external onlyOwner()
```
2.设置施工状态函数
```
function Build(uint money, string remark) external onlyBuilder()
```
3.设置竣工状态函数
```
function Finish(string remark) external onlyBuilder()
```
4.项目验收函数 备注：money用于第三方检测验收的预算
```
function Pass(uint money, string remark) external onlyOwner()
```
5.项目投入使用函数
```
function Work(string remark) external onlyOwner()
```
6.售后修缮函数
```
function AfterMarket(uint money, string remark) external onlyOwner()
```
7.查询项目状态
```
function getStatus() public view returns(uint)
```
8.查询项目使用资金
```
function getPrice() public view returns(uint)
```
9.查询溯源记录
```
function getTraceInfo() public view returns(TraceData[] memory _data)
 ```


## 政府合约(Government)
### 变量
- ``address gov_addr``政府单位地址
- ``string public gov_name``政府单位名称
- ``int public bal``总预算
- ``int public rev``剩余预算
- ``int public num``项目数量
- ``mapping(int => Project) public proj``项目列表

### 初始化
```
constructor(string name) public
```

### 功能函数
1.设置预算函数
```
function setBal(int _bal) public onlyOwner()
```
2.立项函数
```
function CreateProj(int ProjectID, string ProjectName, int money) public onlyOwner()
```
3.开标函数
```
function Deal(int ProjectID, address target, string remark) public onlyOwner()
```
4.验收函数
```
function Pass(int ProjectID, int money, string remark) public onlyOwner()
```
5.投入使用函数
```
function Work(int ProjectID, string remark) public onlyOwner()
```
6.售后修缮函数
```
function AfterMarket(int ProjectID, int money, string remark) public onlyOwner()
```
7.查询项目状态
```
function getStatus(int ProjectID) public view returns(int)
```
8.查询项目使用资金
```
function getPrice(int ProjectID) public view returns(int)
```
9.查询溯源记录
```
function getTraceInfo(int ProjectID) public view returns(Project.TraceData[] memory _data)
```
## 企业合约(Enterprise)
### 变量
- ``address etp_addr``企业地址
- ``string public etp_id``企业编号
- ``string public etp_name``企业名称
- ``int public num``企业项目数量
- ``mapping(int => address) public proj``企业项目列表
### 初始化
```
constructor(string id, string name) public
```
### 功能函数
1.同步开标函数
```
function AddProj(int ProjectID, address ProjectAddr, string remark) external
```
2.设置施工函数
```
function Build(int ProjectID, int money, string remark) public onlyOwner()
```
3.设置竣工函数
```
function Finish(int ProjectID, string remark) public onlyOwner()
```


## 银行合约(Bank)
### 变量
- ``address bank_addr``银行地址
- ``string public bank_name``银行名称
- ``string public bank_id``银行统一信用社会代码
- ``TableFactory tf``TF表格
- ``string constant t_name = "Transfer Records"``表名
- ``struct List``流水结构体
    - ``address Sender``转账人
    - ``address Receiver``收款人
    - ``int money``金额
    - ``uint Timestamp``转账时间
    - ``string TransferID``转账流水号
    - ``string remark``备注
### 初始化
```
constructor(string name, string id) public
```
### 功能函数
1.建表函数
```
function createTable() private
```
2.开启表格函数
```
function openTable() private returns(Table)
```
3.表格添加数据函数
```
function insert(uint ProjectID, address Sender, address Receiver, int money, uint Timestamp, string TransferID, string remark)
```
4.查询函数
```
function select(uint ProjectID) public view returns (uint, List[] memory)
```

## 县级政府合约(County_Gov)
### 变量
- ``struct Towns``乡级政府列表结构体
    - ``address town``乡级政府地址
    - ``bool valid``用于检验乡镇是否存在
### 初始化
```
constructor(string name) public Government(name){}
```
### 功能函数
1.添加乡级政府函数
```
function addTown(string name, address town) public onlyOwner() 
```
2.删除乡级政府函数
```
function removeTown(string name) public onlyOwner()  
```
3.查询乡级政府是否存在函数
```
function checkTown(string name) public view returns(string, address)
```

## 乡级政府合约(Town_Gov)
乡级政府合约从政府合约(Government)派生而成，请参考Government合约。



