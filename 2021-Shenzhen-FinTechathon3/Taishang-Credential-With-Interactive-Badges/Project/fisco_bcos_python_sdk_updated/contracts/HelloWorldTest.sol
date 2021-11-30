pragma solidity ^0.4.24;

contract HelloWorldTest{
    string name;

    constructor(string _name) public{
       name = _name;
    }

    function get() constant public returns(string){
        return name;
    }

    function set(string n) public{
    	name = n;
    }
}
