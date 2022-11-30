pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./LibConvert.sol";

contract CreatePolicy{
    
    LibConvert.Partical partical;
    
    // 创建披露政策:
    
/*  weid：披露，不可更改政策；
    数组内部： 均以string输入，bytes存储
    name：不披露；
    age：披露；
    gender：披露；
    pathogeny：披露；
    medicalid：披露；
    address：不披露；
    telephone：不披露

function createPolicy(bool[8] memory _bools) public returns(Policy){
        Policy memory p1 = policy;
        p1._weidp = _bools[0];
        p1._namep = _bools[1];
        p1._agep = _bools[2];
        p1._gendlep = _bools[3];
        p1._pathogenyp = _bools[4];
        p1._medicalidp = _bools[5];
        p1._addressp = _bools[6];
        p1._telephonep = _bools[7];
        return p1;
    }
*/  

    // 完成选择性披露处理
    function ParticalHash(bool[7] policy, address _weidf, string[7] _emr)public returns(LibConvert.Partical){
        
        bytes[7] memory _byte = LibConvert.str2bytes(_emr);
        
        LibConvert.Partical memory p1 = partical;
        
        p1.weid = _weidf;
        p1.policy = policy;
        for(uint i=0; i<7; i++){
            if(policy[i]==false){
                p1.hash[i] = sha256(_byte[i]);
            } else {
                p1.cpublic[i] = _byte[i];
            }
        }
        
        return p1;
    }
    
    
}