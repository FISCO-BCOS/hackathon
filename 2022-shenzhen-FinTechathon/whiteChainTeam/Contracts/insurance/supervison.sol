// SPDX-License-Identifier: MIT
pragma solidity <=0.6.10;

contract Supervision{
    
    address private _supervision;
    
    //设置传入还是直接初始化?
    constructor (address supervision) public{
        _supervision = supervision;
    }

    modifier onlySupervision(){
        require(isSupervision(), "Caller is not the Supervision");
        _;
    }

    //查看supervision
    function getSupervision() public view returns(address){
        return _supervision;
    }

    //查看当前合约调用者是否为supervision
    function isSupervision() public view returns(bool){
        return msg.sender == _supervision;
    }

    //

}