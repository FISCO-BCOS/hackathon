pragma solidity>=0.4.24 <0.6.11;

contract NoteItem{
        string public timestamp;
        string public title;
        string public content;
		
		constructor() public
		{
		}
		
		function set_time(string memory t) public 
		{
			timestamp  =t;
		}
		
		function set_title(string memory t) public 
		{
			title = t;
		}
		
		function set_content(string memory c) public 
		{
			content = c;
		}
		
}