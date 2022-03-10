pragma solidity >=0.4.22 <0.7.0;
pragma experimental ABIEncoderV2;

contract DeviceInfoItem
{
    uint[] _timestamp;     
    /*保存硬件设备上传数据时的时间戳*/
    uint8[] _temp;   
    /*保存茶叶种植或仓储中各个阶段的温度信息*/
    uint8[] _humi;    
    /*保存茶叶种植或仓储过程各个阶段湿度信息*/
    uint16[] _lux;  
    /*保存茶叶种植或仓储过程各个阶段光照度信息*/
    uint16[] _pa;   
    /*保存茶叶种植或仓储过程各个阶段大气压信息*/
    uint8[] _alt;   
    /*保存茶叶种植或仓储过程各个阶段海拔信息*/
    
    
    address _deviceAddress; 
    /*保存茶叶流转过程的硬件地址信息（和用户一一对应）*/
    
    uint _createTimestamp; 
    /*保存设备创建时间*/
    string _deviceName; 
    /*保存设备名称*/
    uint8 _status; 
    /*保存硬件设备所负责的阶段*/
    address _owner;

    constructor (string deviceName,uint8 status ,address deviceAddress) public 
    {
        _createTimestamp = now;
        _deviceName = deviceName;
        _deviceAddress = deviceAddress;
        _status = status;
        _owner = msg.sender;
    }

    function addDeviceInfo( uint8 temp, uint8 humi, uint16 lux, uint16 pa, uint8 alt,uint8 status) public returns(bool) 
    {
        require(_status == status , "status error");
        require(_owner == msg.sender, "only trace contract can invoke");
        _timestamp.push(now);
        _temp.push(temp);
        _humi.push(humi);
        _lux.push(lux);
        _pa.push(pa);
        _alt.push(alt);
        return true;
    }


    function getDeviceInfo() public constant returns(uint,string,uint8,address) 
    {
        return(_createTimestamp, _deviceName,_status,_deviceAddress);
    }
    
    function getDeviceTraceInfo() public constant returns(uint[] ,uint8[],uint8[],uint16[],uint16[], uint8[])
    {
        return(_timestamp, _temp,_humi,_lux,_pa,_alt);
    }

}