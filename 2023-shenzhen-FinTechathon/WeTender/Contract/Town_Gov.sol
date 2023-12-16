pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Government.sol";

contract Town_Gov is Government
{
    constructor(string name) public Government(name) {}
}