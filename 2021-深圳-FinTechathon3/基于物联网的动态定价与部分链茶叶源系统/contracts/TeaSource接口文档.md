# **"TeaSource"接口文档**

# 1. 功能接口

## 设备

   设备信息存储管理

## 种植户

   种植信息存储管理、溯源信息查询管理

## 加工厂

   加工信息存储管理、动态价格设定、溯源信息查询管理

## 销售商

   销售信息存储管理、动态价格设定、溯源信息查询管理

## 消费者

   溯源信息查询管理

# 2. 主要合约

    主要合约包括：

    （1）设备信息合约

    （2）设备信息存储合约

    （3）种植户信息合约

    （4）加工场信息合约

    （5）销售商信息合约

    （6）角色权限认证的库合约： 提供角色权限地址增、删验证的基本功能，实现了角色的权限控制。

    （7）茶叶溯源信息存储合约： 对溯源信息存储，只对茶叶溯源信息创建者提供访问权限。

    （8）茶叶溯源信息管理合约： 维护链下溯源ID和具体茶叶溯源信息对应关系，管理茶叶溯源信息；同时提供各角色新建、添加、查询茶叶溯源信息等。

## Trace合约

### 功能：
    维护链下溯源ID和具体茶叶溯源信息对应关系，管理茶叶溯源信息；同时提供各角色新建、添加、查询茶叶溯源信息等。

### 构造函数：

```
constructor(address planter , address processor, address retailer,address device) 
        public Planter(planter) 
        Processor(processor) 
        Retailer(retailer)
        Device(device)
        {
        }
```

### 加工厂动态定价逻辑：
```
uint8 planterNum=0;
            uint8 processorNum=0;
            uint8 originPrice=TeaInfoItem(teas[traceNumber]).getTeaPrice(0);
            uint8 finallPrice;
            uint8 revenuePre;
            for(uint i=0;i<teaList.length;i++){
                if( keccak256(abi.encodePacked( TeaInfoItem(teas[teaList[i]]).getTeaKind()))== keccak256(abi.encodePacked(TeaInfoItem(teas[traceNumber]).getTeaKind()))){
                    if(TeaInfoItem(teas[teaList[i]]).getTeaStatus()==0){
                        planterNum++;
                    }
                    else if(TeaInfoItem(teas[teaList[i]]).getTeaStatus()==1){
                        processorNum++;
                    }
                }
            }
            /*当库存量大于10时开启动态定价*/
            if(processorNum>10){
                revenuePre=processorNum/planterNum;
                finallPrice = originPrice / revenuePre;
                return TeaInfoItem(teas[traceNumber]).addTraceInfoByProcessor(finallPrice, traceName,msg.sender,quality);
            }
            else{
                return TeaInfoItem(teas[traceNumber]).addTraceInfoByProcessor(originPrice, traceName,msg.sender,quality);
            }
```

### 零售商动态定价逻辑
```
 uint8 processorNum=0;
            uint8 retailerNum=0;
            uint8 originPrice=TeaInfoItem(teas[traceNumber]).getTeaPrice(1);
            uint8 finallPrice;
            uint8 revenuePre;
            for(uint i=0;i<teaList.length;i++){
                if( keccak256(abi.encodePacked( TeaInfoItem(teas[teaList[i]]).getTeaKind()))== keccak256(abi.encodePacked(TeaInfoItem(teas[traceNumber]).getTeaKind())))
                {
                    if(TeaInfoItem(teas[teaList[i]]).getTeaStatus()==1)
                    {
                        processorNum++;
                    }
                    else if(TeaInfoItem(teas[teaList[i]]).getTeaStatus()==2)
                    {
                        retailerNum++;
                    }
                }
            }

            /*当库存量大于10时开启动态定价*/
            if(processorNum>10)
            {
                revenuePre=retailerNum/processorNum;
                finallPrice = originPrice / revenuePre;
                return TeaInfoItem(teas[traceNumber]).addTraceInfoByRetailer(finallPrice, traceName,msg.sender,quality);
            }
            else
            {
                return TeaInfoItem(teas[traceNumber]).addTraceInfoByRetailer(originPrice, traceName,msg.sender,quality);
            }
```
## TeaInfoItem合约

### 功能：
    对溯源信息存储，只对茶叶溯源信息创建者提供访问权限。
    
### 参数：

```
    uint[] _timestamp;     
    /*保存茶叶流转过程中各个阶段的时间戳*/
    uint8[] _tracePrice;   
    /*保存茶叶销售过程中各个阶段的价格*/
    string[] _traceName;    
    /*保存茶叶流转过程各个阶段的用户名*/
    address[] _traceAddress; 
    /*保存茶叶流转过程各个阶段的用户地址信息（和用户一一对应）*/
    uint8[] _traceQuality;  
    /*保存茶叶流转过程中各个阶段的质量*/
    uint256 _deviceTraceNumber;    
    /*关联硬件设备ID溯源码*/
    string _kind;  
    /*茶叶种类*/
    string _currentTraceName;  
    /*当前用户名称*/
    uint8 _quality; 
    /*质量（0=优质 1=合格 2=不合格）*/
    uint8 _status; 
    /*状态（0:种植 1:加工 2:仓储 3：销售）*/
    address  _owner;
```

### 函数：

```
function addTraceInfoByProcessor( uint8 tracePrice, string traceName, address processor, uint8 quality) public returns(bool) 
    {
        require(_status == uint8(0) , "status must be planting");
        require(_owner == msg.sender, "only trace contract can invoke");
        _timestamp.push(now);
        _tracePrice.push(tracePrice);
        _traceName.push(traceName);
        _currentTraceName = traceName;
        _traceAddress.push(processor);
        _quality = quality;
        _traceQuality.push(_quality);
        _status = 1;
        return true;
    }
```

```
function addTraceInfoByRetailer( uint8 tracePrice, string traceName, address retailer, uint8 quality) public returns(bool) 
    
    {
        require(_status == uint8(1), "status must be processing");
        require(_owner == msg.sender, "only trace contract can invoke");
        _timestamp.push(now);
        _tracePrice.push(tracePrice);
        _traceName.push(traceName);
        _currentTraceName = traceName;
        _traceAddress.push(retailer);
        _quality = quality;
        _traceQuality.push(_quality);
        _status = 2;
        return true;
    }
```

```
function setStatus(uint8 status) public 
    {
        _status=status;
        
    }
```

```
function getTraceInfo() public constant returns(uint[], uint8[], string[], address[], uint8[]) 
    {
        return(_timestamp, _tracePrice, _traceName, _traceAddress, _traceQuality);
    }
```

```
function getTea() public constant returns(uint, uint8, string, string, string, address, uint8,uint8) 
    {
        return(_timestamp[0], _tracePrice[0], _traceName[0], _kind, _currentTraceName, _traceAddress[0], _quality,_status);
    }
```

```
function getTeaKind() public returns(string) 
    {
        return(_kind);
    }
```

```
function getTeaStatus() public constant returns(uint8) 
    {
        return(_status);
    }
```

```
function getTeaPrice(uint8 indexs) public constant returns(uint8) 
    {
        return(_tracePrice[indexs]);
    }
```

```
function getTeaDeviceTrace() public returns(uint256) 
    {
        return(_deviceTraceNumber);
    }
```
## Roles权限认证合约

### 功能：

    提供角色权限地址增、删验证的基本功能，实现了角色的权限控制。

### 函数：
```
function add(Role storage role, address account) internal 
    {
        require(!has(role, account), "Roles: account already has role");
        role.bearer[account] = true;
    }
```

```
function remove(Role storage role, address account) internal 
    {
        require(has(role, account), "Roles: account does not have role");
        role.bearer[account] = false;
    }
```

```
function has(Role storage role, address account) internal view returns (bool) 
    {
        require(account != address(0), "Roles: account is the zero address");
        return role.bearer[account];
    }
```
