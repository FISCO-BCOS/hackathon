pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

library LibConvert{
    
    // 选择性披露后的结构体
    struct Partical{
        bool[7] policy;
        bytes32[7] hash;
        bytes[7] cpublic;
        address weid;
    }
    
    function str2bytes(string[7] _str) public returns(bytes[7] _byte){
        for(uint i=0; i<7; i++){
            _byte[i] = bytes(_str[i]);
        }
    }
    
    function dynamic2stab(bytes32[8] memory _dyna) public returns(bytes32[]){
        bytes32[] memory _stab = new bytes32[](8);
        for(uint i=0; i<_dyna.length; i++){
            _stab[i] = _dyna[i];
        }
    }
    
}