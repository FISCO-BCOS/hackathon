pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Program.sol";
import "./DAG.sol";

/*
for the Organization users
Organizationworks
*/
contract Organization{         
    
    Program private _program;
    DAG private _DAG;
    
    constructor(
    address Programaddr,
    address DAGaddr){
        _program =Program(Programaddr);
        _DAG = DAG(DAGaddr);
        }
    
    
    modifier isexist (bool isexist) {
        require(isexist);
        
        _;   
    }
    
    modifier ownerOnly (address addr) {
        require(addr == msg.sender);
        
        _;   
    }
       function addprogram(
        uint totalneed,
        string title,
        string programType,
        string description)
        public returns(bool success){  
        /*
        publish  the charity program
        */
        require(totalneed >0 );
        _program.addProgram(msg.sender, totalneed, title, programType, description);
    }
   
    function changevalue( uint256 id, uint _value)ownerOnly(_program.getaddr(id)) public{
        /*
        change the program value, only can change the program you publish
        */
        _program.changevalue(id, _value);
    }
    
    function getprogram() public  returns(uint256[], string[]){
    function getprogram() public  returns(uint256[], string[]){
        /*
        get the program list you publish
        */
        return(_program.getprogram(msg.sender));
    }
    
    function getinfor(uint256 id) isexist(_program.getisexist(id))  public view returns(
    uint256, 
    address, 
    uint, 
    uint,
    uint){
        /*
        get the program information 
        no title, type and description
        */
        return (_program.getinfor(id));
    }
        
    function getstringinfor( uint256 id) isexist(_program.getisexist(id))  public view returns(
    string,
    string,
    string){
        /*
        get the program title, type and description
        */
        return (_program.getstringinfor(id));
    }
   
    function implementProgram(uint256 id, string _describe) 
    ownerOnly(_program.getaddr(id)) 
    isexist(_program.getisexist(id)) 
    public {
        /*
        updata the process of program
        */
        _program.implement(id, _describe);
    }
    
    function getDAG( uint256 id) public returns(uint256[] , uint256[]){
        //use DAG , get process
        uint blocknumber = _DAG.getblocknumber(id);
        bytes32 CommitHash = _DAG.getCommitHash(id, blocknumber);
        return(_DAG.getparents(CommitHash), _DAG.getchildren(CommitHash));
    }
    
        
    function getprocess( uint256 id) public view returns(string[], uint[]){
        /*
        get the process
        */
        return(_program.getprocess(id));
    }
    
    function getdevoter( uint256 id)ownerOnly(_program.getaddr(id))
    public returns(address[], uint[]){
        /*
        get the devoter lists in your program
        */
        return(_program.getdevoter(id));
    }
    
}