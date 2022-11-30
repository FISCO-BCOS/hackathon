// SPDX-License-Identifier: MIT
pragma solidity <=0.6.10;

contract Supervision{
    
    address private _supervision;
    
    constructor (address supervision) public{
        _supervision = supervision;
    }

    modifier onlySupervision(){
        require(isSupervision(), "Caller is not the Supervision");
        _;
    }

    function getSupervision() public view returns(address){
        return _supervision;
    }

    function isSupervision() public view returns(bool){
        return msg.sender == _supervision;
    }

    //

}
