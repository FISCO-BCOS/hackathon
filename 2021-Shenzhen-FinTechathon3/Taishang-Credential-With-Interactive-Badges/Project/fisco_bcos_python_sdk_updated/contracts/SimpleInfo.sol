pragma solidity ^0.4.24;

contract SimpleInfo{
    string  name = "";
    uint256 balance = 0;
	address addr =0x0;
	uint256 counter=0;
	event on_set(int retcode,string name,uint256 balance,address addr,string memo);
	event on_change(int retcode,string indexed name,uint256 balance,address indexed addr,string memo);
	event on_sender(int retcode,string name,uint256 balance,address  addr,string memo);
    event on_reset(int retcode,string indexed name) anonymous;
	event on_set_empty(string msg);
	

    constructor() public
    {
    }

    function getname() constant public returns(string){
        return name;
    }
    function getbalance() constant public returns(uint256){
        return balance;
    }
    function getbalance1(uint256 plus) constant public returns(uint256){
        return (balance+plus);
    }
	function getaddress() constant public returns(address){
        return addr;
    }
	function getall() public constant returns(string ,uint256,address)
    {
         return (name,balance,addr);
    }
	
	function getcounter() constant public returns(uint256){
        return counter;
    }
    function setbalance(uint256 b) public {
        balance = b;
        emit on_set(0,name,balance,addr,"balance set");
    }
	
	function setempty() public {
        emit on_set_empty("empty set");
    }

    function set(string n,uint256 b,address a) public returns(int){
    	name = n;
        balance = b;
		addr = a;
		emit on_set(0,n,b,a,"info set");
		emit on_change(0,name,balance,addr,"on_set_change");
		emit on_sender(0,n,b,msg.sender,"change by sender");
		return 100;
    }
	function add(uint256 b) public returns(uint256){
		balance = balance +b;
		emit on_change(0,name,balance,addr,"balance add");
		return balance;
	}
	function reset() public returns (int){
		name="";
		balance = 0;
		addr = 0x0;
		emit on_reset(-100,name);
	}
	 function() public { counter = counter+1; }
}
