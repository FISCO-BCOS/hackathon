// RingSigPrecompiled.sol
pragma solidity ^0.4.24;
contract RingSigPrecompiled{
    function ringSigVerify(string signature,string message,string paramInfo) public constant returns(bool);
}