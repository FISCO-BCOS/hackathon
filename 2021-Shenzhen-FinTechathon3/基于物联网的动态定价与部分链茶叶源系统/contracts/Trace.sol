pragma solidity >=0.4.22 <0.7.0;
pragma experimental ABIEncoderV2;

import "./TeaInfoItem.sol";
import "./Planter.sol";
import "./Processor.sol";
import "./Retailer.sol";
import "./Device.sol";
import "./DeviceInfoItem.sol";

contract Trace is Planter, Processor, Retailer,Device
{
        /*关联茶叶溯源码*/
        mapping (uint256 => address)  teas;
        uint[]  teaList;
        
        /*关联硬件溯源码*/
        mapping (uint256 => address)  devices;
        uint[]  deviceList;

        /*构造函数*/
        constructor(address planter , address processor, address retailer,address device) 
        public Planter(planter) 
        Processor(processor) 
        Retailer(retailer)
        Device(device)
        {
        }
    
        /*新建设备信息*/
        function newDevice(string deviceName, uint8 status, uint256 deviceTraceNumber)
        public onlyDevice returns(address)
        {
            require(devices[deviceTraceNumber] == address(0), "traceNumber already exist");
            DeviceInfoItem device = new DeviceInfoItem(deviceName,status,msg.sender);
            devices[deviceTraceNumber] = device;
            deviceList.push(deviceTraceNumber);
            return device;
        }

        /*新建茶叶信息*/
        function newTea(string teaKind, uint8 tracePrice, uint256 traceNumber, string traceName, uint8 quality,uint256 deviceTraceNumber ) 
        public onlyPlanter returns(address)
        {
            require(teas[traceNumber] == address(0), "traceNumber already exist");
            TeaInfoItem tea = new TeaInfoItem(teaKind,tracePrice,traceName,quality,deviceTraceNumber,msg.sender);
            teas[traceNumber] = tea;
            teaList.push(traceNumber);
            return tea;
        }
        
        
        function addDeviceinfo(uint8 temp, uint8 humi, uint16 lux, uint16 pa, uint8 alt,uint256 deviceTraceNumber,uint8 status) 
        public onlyDevice returns(bool) {
            require(devices[deviceTraceNumber] != address(0), "deviceNumber does not exist");
            return DeviceInfoItem(devices[deviceTraceNumber]).addDeviceInfo(  temp,  humi,  lux,  pa,  alt,status);
        }
        
        
        
        function addTraceInfoByProcessor(uint256 traceNumber, string traceName, uint8 quality) 
        public onlyProcessor returns(bool) {
            require(teas[traceNumber] != address(0), "traceNumber does not exist");
            
            /*加工厂环节动态定价逻辑*/ 
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
            
            
            
            // return TeaInfoItem(teas[traceNumber]).addTraceInfoByProcessor(tracePrice, traceName,msg.sender,quality);
        }
        
        function addTraceInfoByRetailer(uint256 traceNumber, string traceName, uint8 quality) 
        public onlyRetailer returns(bool) 
        {
            require(teas[traceNumber] != address(0), "traceNumber does not exist");
            
            /*零售商环节动态定价逻辑*/ 
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
            
            
        }

		function getTraceInfo(uint256 traceNumber) public constant returns(uint[], uint8[], string[], address[], uint8[]) 
        {
            require(teas[traceNumber] !=  address(0), "traceNumber does not exist");
            return   TeaInfoItem(teas[traceNumber]).getTraceInfo();
        }

        function traceOver(uint256 traceNumber) public
        {
            require(teas[traceNumber] !=  address(0), "traceNumber does not exist");
            return   TeaInfoItem(teas[traceNumber]).setStatus(3);
        }

        function getTea(uint256 traceNumber) public constant returns(uint, uint8, string, string, string, address, uint8,uint8) 
        {
            require(teas[traceNumber] != address(0), "traceNumber does not exist");
            return  TeaInfoItem(teas[traceNumber]).getTea();
        }

        function getAllTea()  public constant returns (uint[])
         {
            return  teaList;
        }

         function getTeaDeviceInfo(uint256 traceNumber)  public constant returns (uint[] ,uint8[],uint8[],uint16[],uint16[], uint8[]) 
        {
            
            require(teas[traceNumber] !=  address(0), "traceNumber does not exist");
            uint256 num = TeaInfoItem(teas[traceNumber]).getTeaDeviceTrace();
            return DeviceInfoItem(devices[num]).getDeviceTraceInfo();
        }
        
        

}