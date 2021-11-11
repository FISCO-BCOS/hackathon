pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./RingSigPrecompiled.sol";

contract CallRingSig{
    RingSigPrecompiled ringsig;
    string _ring_param_info;//环标识
    string _rand_G;//随机信息
    string _target_addrG;//目标地址
    string _ring_sig;//环签名
    string _message;//秘密信息
    bool _verified=false;

    modifier isVerified(){
        require(_verified==true,"INVALID RINGSIGNATURE!");
        _;
    }
    function CallRingSig(){
        //实例化RingSigPrecompiled预编译合约
        ringsig=RingSigPrecompiled(0x5005);
    }
    function ringSigVerify(string signature, string message, string paramInfo) public constant returns(bool){
        _verified=ringsig.ringSigVerify(signature,message,paramInfo);
        return _verified;
    }
    //生成环
    function setup_ring( string ring_param_info )public {
        _ring_param_info=ring_param_info;
    }
    //生成环签名
    function ring_sig(string ring_sign)public {
        _ring_sig=ring_sign;
    }
    //生成秘密信息
    function set_message(string message)public {
        _message=message;
    }
    //生成目标地址
    function set_target(string rand_G,string target_addrG)public{
        _rand_G=rand_G;
        _target_addrG=target_addrG;
    }
    //获取目标地址
    function get_target() isVerified()public returns(string rand_G,string target_addrG){
        return(_rand_G,_target_addrG);
    }
    //获取秘密信息
    function get_message() isVerified()public returns (string) {
        return _message;
    }
}

