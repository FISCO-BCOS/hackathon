pragma solidity ^0.4.24;

contract HelloWorld{
    string name;
	uint id;
    event onset(string newname);
    constructor(string n,uint i) public{
       name = n;
	   id = i;
    }

    function get() constant public returns(string){
        return name;
    }
	
	function getid() constant public returns	 (uint){
		return id;
	}
	

    function set(string n) public returns (uint){
		emit onset(n);
    	name = n;
		return id;
    }
}
