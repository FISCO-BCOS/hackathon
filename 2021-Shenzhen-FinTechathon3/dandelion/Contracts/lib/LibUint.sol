pragma solidity ^0.4.11;
// edited by wangdi
library LibUint {
    function toString(uint num) internal view returns(string memory count){
        uint temp = uint(num);
        if(temp == 0) return "0";
        uint length ;
        while(temp != 0){
            length++;
            temp /= 10;
        }
        bytes memory bstr = new bytes(length);
        uint k = length - 1;
        temp = uint(num);
        while(temp != 0){
            bstr[k--] = byte(48 + temp % 10);
            temp /= 10;
        }
        count = string(bstr);
    }
}