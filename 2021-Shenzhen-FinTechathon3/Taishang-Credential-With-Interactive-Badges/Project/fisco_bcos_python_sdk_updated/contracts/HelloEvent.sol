pragma solidity ^0.4.24;

contract HelloEvent{
    string name ;
	bool b;
    event on_set(string newname);
	event on_number(string name,int indexed age);
	event on_two_indexed(string name ,int indexed age, string indexed key);
	event on_address(address addr);
    constructor() public{
       name = "Hello, Event!";
    }

    function get() constant public returns(string){
        return name;
    }

	function setbool(string n ,bool i) public
	{
		
		name = n;
		b = i;
		
	}
	
	function settwo(string n ,int i,string key) public
	{
		emit on_two_indexed(n,i,key);
		name = n;
		
	}
	
	function setnum(string n ,int i) public
	{
		emit on_number(n,i);
		name = n;
	}

    function set(string n) public{
		emit on_set(n);
		emit on_number(n,5);
		emit on_address(tx.origin);
		name = n;		
    }
}
