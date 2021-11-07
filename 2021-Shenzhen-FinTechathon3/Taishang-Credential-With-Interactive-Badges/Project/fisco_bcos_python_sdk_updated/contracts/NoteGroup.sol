pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;
import "./NoteItem.sol";


contract NoteGroup{

   event on_write(address itemaddr);
   event on_setarray(uint256 len,string memo);
   event on_total(uint256 total,string memo);
   event on_addr(uint index,address addr);
   event on_str(uint index,string str);
   event on_bool(uint index,bool b);
   
   constructor(address[] inlist,string memo) public{
       	emit on_setarray(inlist.length,memo);
        for(uint i = 0; i < inlist.length; i++) {
           emit on_addr(i,inlist[i]);
        }	
   }
 
   function  write(string memory timestamp,string memory title,string memory content) public returns (uint256) 
   {
         NoteItem  item = new NoteItem();
		 item.set_title(title);
		 item.set_content(content);
		 item.set_time(timestamp);
		 emit  on_write(item);
   }
   
   function set_addr(address[] inlist,string memo) public returns (uint256)
   {
		
		emit on_setarray(inlist.length,memo);
		uint num = 0;
        for(uint i = 0; i < inlist.length; i++) {
           emit on_addr(i,inlist[i]);
        }	
		return inlist.length;
   }
   
   function set_int(uint256[] inlist,string memo) public returns (uint256)
   {
   		
		uint256 num = 0;
        for(uint i = 0; i < inlist.length; i++) {
            num = num + inlist[i];
        }
		emit on_total(num,memo);
		emit on_setarray(inlist.length,memo);
		return inlist.length;
   }
   
   function set_string(string[] inlist,string memo) public returns (uint256)
   {
   		emit on_setarray(inlist.length,memo);
		for(uint i = 0; i < inlist.length; i++) {
           emit on_str(i,inlist[i]);
        }	
		return inlist.length;
   }
    
	function set_bool(bool[] inlist,string memo) public returns (uint256)
   {
        for(uint i = 0; i < inlist.length; i++) {
            emit on_bool(i,inlist[i]);
        }
   		emit on_setarray(inlist.length,memo);
		
		return inlist.length;
   }  
   
      function set_int_3(uint256[3] inlist,string memo) public returns (uint256)
   {
   		
		uint256 num = 0;
        for(uint i = 0; i < inlist.length; i++) {
            num = num + inlist[i];
        }
		emit on_total(num,memo);
		emit on_setarray(inlist.length,memo);
		return inlist.length;
   }
   
         function set_int_n(uint256[  ] inlist,string memo) public returns (uint256)
   {
   		
		uint256 num = 0;
        for(uint i = 0; i < inlist.length; i++) {
            num = num + inlist[i];
        }
		emit on_total(num,memo);
		emit on_setarray(inlist.length,memo);
		return inlist.length;
   }
}


