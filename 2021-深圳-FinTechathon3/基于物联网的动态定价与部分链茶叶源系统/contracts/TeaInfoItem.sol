pragma solidity >=0.4.22 <0.7.0;
pragma experimental ABIEncoderV2;

contract TeaInfoItem
{
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

  constructor (string kind, uint8 tracePrice, string traceName, uint8 quality, uint256 deviceTraceNumber,address planter) public 
  
  {
        _timestamp.push(now);
        _tracePrice.push(tracePrice);
        _traceName.push(traceName);
        _traceAddress.push(planter);
        _traceQuality.push(quality);
        _kind = kind;
        _currentTraceName = traceName;
        _deviceTraceNumber = deviceTraceNumber;
        _quality = quality;
        _status = 0;
        _owner = msg.sender;
    }

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
    
    function setStatus(uint8 status) public 
    {
        _status=status;
        
    }
    
    function getTraceInfo() public constant returns(uint[], uint8[], string[], address[], uint8[]) 
    {
        return(_timestamp, _tracePrice, _traceName, _traceAddress, _traceQuality);
    }

    function getTea() public constant returns(uint, uint8, string, string, string, address, uint8,uint8) 
    {
        return(_timestamp[0], _tracePrice[0], _traceName[0], _kind, _currentTraceName, _traceAddress[0], _quality,_status);
    }
    
    function getTeaKind() public returns(string) 
    {
        return(_kind);
    }

    function getTeaStatus() public constant returns(uint8) 
    {
        return(_status);
    }

    function getTeaPrice(uint8 indexs) public constant returns(uint8) 
    {
        return(_tracePrice[indexs]);
    }

    function getTeaDeviceTrace() public returns(uint256) 
    {
        return(_deviceTraceNumber);
    }
    
}