pragma solidity ^0.4.25;

contract DataFormat{
    // 功能：讲 address 类型转换为 string 类型
    function toString(address addr) internal pure returns(string){
        //Convert addr to bytes
        bytes20 value = bytes20(uint160(addr));
        bytes memory strBytes = new bytes(42);
        //Encode hex prefix
        strBytes[0] = '0';
        strBytes[1] = 'x';
        //Encode bytes usig hex encoding
        for(uint i=0;i<20;i++){
            uint8 byteValue = uint8(value[i]);
            strBytes[2 + (i<<1)] = encode(byteValue >> 4);
            strBytes[3 + (i<<1)] = encode(byteValue & 0x0f);
        }
        return string(strBytes);
    }

    // 功能：将 string 类型转为 address 类型
    function toAddress(string data) internal returns(address){
        bytes memory strBytes = bytes(data);
        require(strBytes.length >= 39 && strBytes.length <= 42, "Not hex string");
        //Skip prefix
        uint start = 0;
        uint bytesBegin = 0;
        if(strBytes[1] == 'x' || strBytes[1] == 'X'){
            start = 2;
        }
        //Special case: 0xabc. should be 0x0abc
        uint160 addrValue = 0;
        uint effectPayloadLen = strBytes.length - start;
        if(effectPayloadLen == 39){
            addrValue += decode(strBytes[start++]);
            bytesBegin++;
        }
        //Main loop
        for(uint i=bytesBegin;i < 20; i++){
            addrValue <<= 8;
            uint8 tmp1 = decode(strBytes[start]);
            uint8 tmp2 = decode(strBytes[start+1]);
            uint8 combined = (tmp1 << 4) + tmp2;
            addrValue += combined;
            start+=2;
        }
        
        return address(addrValue);
    }
    
    // 功能：拼接两个字符串，并返回
    function strConcat(string _a, string _b) internal returns (string){
        bytes memory _ba = bytes(_a);
        bytes memory _bb = bytes(_b);
        string memory ret = new string(_ba.length + _bb.length);
        bytes memory bret = bytes(ret);
        uint k = 0;
        for (uint i = 0; i < _ba.length; i++)bret[k++] = _ba[i];
        for (i = 0; i < _bb.length; i++) bret[k++] = _bb[i];
        return string(ret);
   }

    // 功能：比较两个字符串是否相等
    function compareTwoString(string memory a, string memory b) internal pure returns(bool){
        return keccak256(bytes(a)) == keccak256(bytes(b));
    }
    
    // 功能：内部辅助函数
    function encode(uint8 num) private pure returns(byte){
        //0-9 -> 0-9
        if(num >= 0 && num <= 9){
            return byte(num + 48);
        }
        //10-15 -> a-f
        return byte(num + 87);
    }

    // 功能：内部辅助函数   
    function decode(byte asc) private pure returns(uint8){
        uint8 val = uint8(asc);
        //0-9
        if(val >= 48 && val <= 57){
            return val - 48;
        }
        //A-F
        if(val >= 65 && val <= 70){
            return val - 55;
        }
        //a-f
        return val - 87;
    }
    
    uint private time;
    function getDate() internal returns(uint){
        time = now;
        return(time);
    }
    
    function callTime() public returns(uint){
        uint tim = getDate();
        return(tim);
    }
    
}
