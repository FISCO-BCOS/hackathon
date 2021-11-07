pragma solidity ^0.4.24;

contract HelloWorld{
    string name;
    event onset(string newname);
    constructor() public{
       name = "Hello, World!";
    }

    function get() constant public returns(string){
        return name;
    }

    function set(string n) public{
	emit onset(n);
    	name = n;
    }
}
