// SPDX-License-Identifier: MIT
pragma solidity  >=0.4.25 <=0.6.10;

interface ClaimScheme {
    function firstLevel(uint _basicMoney) external pure returns(uint);
    function secondLevel(uint _basicMoney) external pure returns(uint);
    function thirdLevel(uint _basicMoney) external pure returns(uint);
    function forthLevel(uint _basicMoney) external pure returns(uint);
}