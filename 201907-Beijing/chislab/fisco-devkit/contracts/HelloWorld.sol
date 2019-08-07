pragma solidity ^0.4.25;

contract HelloWorld {
    string name;
    constructor() public {
       name = "Hi, fisco devkit!";
    }
    function get() public constant returns(string) {
        return name;
    }

    function set(string n) public {
    	name = n;
    }
}
