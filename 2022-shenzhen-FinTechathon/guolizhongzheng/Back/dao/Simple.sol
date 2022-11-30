pragma solidity ^0.4.25;

contract Simple {
    uint number;
    address owner;

    constructor() public {
        owner = msg.sender;
    }

    function setNumber(uint _num)  public {
        number = _num;

    }

    function getNumber() public view  returns(uint) {
        return number;
    }

    function getOwnerAddress() public view  returns(address) {
        return owner;
    }
}