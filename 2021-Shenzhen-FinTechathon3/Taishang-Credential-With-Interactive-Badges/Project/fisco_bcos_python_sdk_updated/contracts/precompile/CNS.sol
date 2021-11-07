pragma solidity ^0.4.24;
contract CNS
{
    function selectByName(string name) public constant returns(string);
    function selectByNameAndVersion(string name, string version) public constant returns(string);
    function insert(string name, string version, string addr, string cnsAbi) public returns(int);
}
