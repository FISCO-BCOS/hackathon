// SPDX-License-Identifier: MIT
pragma solidity  >=0.4.25 <=0.6.10;

import "./ClaimInterface.sol";
import "./SafeMath.sol";

contract Claimitic is ClaimScheme{
    using SafeMath for uint;

    function firstLevel(uint _basicMoney) external pure returns(uint){
        return _basicMoney.mul(2);
    }
    function secondLevel(uint _basicMoney) external pure returns(uint){
        return _basicMoney.mul(3);
    }
    function thirdLevel(uint _basicMoney) external pure returns(uint){
        return _basicMoney.mul(4);
    }
    function forthLevel(uint _basicMoney) external pure returns(uint){
        return _basicMoney.mul(5);
    }
}