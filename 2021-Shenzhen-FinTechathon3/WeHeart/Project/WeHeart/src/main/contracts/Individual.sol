pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Program.sol";
import "./Integral.sol";

/*
for the individual users
individual works
*/
contract Individual{
    
   event devotelog(uint256 _id, address _from, uint _value);
   event implementlog(uint256 _id, string _describe);
    
    Program  private _program;
    Integral private _integral;
    
    constructor(
        address Programaddr,
        address Integraladdr){
            _program =Program(Programaddr);
            _integral = Integral(Integraladdr);
    }
    
    struct donate{     
        /*
        store the donation information
        */
        uint256 programID;
        string programType;
        uint amount;
        string message;
    }
    
        /*
        store the donation IDs and information
        */ 
    mapping(address => bytes32[]) _donationids;       
    mapping(bytes32 => donate)  _donationinf;         
    
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
        seek help by individual users
        */
        require(totalneed >0 );
        _program.addProgram( msg.sender, totalneed, title, programType, description);
    }
    
    
    function devote(
        uint256 id, 
        uint _value,
        string _programType,
        string _message)
        isexist(_program.getisexist(id)) 
        {
        /*
        to devote, can send message to the program
        devote can add integral
        */
        require(_program.getstatus(id) ==0);      
        require(_value >0);
        uint _totalneed = _program.getvalue(id);
        require(_value <= _totalneed);
        _totalneed -= _value;
        _program.changevalue(id, _totalneed);
        _program.adddevoter(id, msg.sender, _value);

        bytes32 _txid =_integral.addPoints(msg.sender, _value );

        _donationids[msg.sender].push(_txid);
         donate memory _donations = donate(id, _programType, _value, _message);
        _donationinf[_txid] = _donations;
        
        uint _programAmount = _program.gettotal(id);
        _programAmount += _value;
        _program.changetotal(id, _programAmount);
        
        emit devotelog(id, msg.sender, _value);
        
        if (_program.getvalue(id) == 0){
            _program.changestatus(id);
        }
        
        
    }
       
    function changevalue(uint256 id, uint _value) ownerOnly(_program.getaddr(id))
        ownerOnly(_program.getaddr(id))  public{
        /*
        change the program value, only can change the program you publish
        */
        _program.changevalue(id, _value);
    }
    
    function getprogram() public  returns(uint256[], string[]){
        /*
        get the program list you publish
        */
        return(_program.getprogram(msg.sender));
    }
    
    function getdonationids()public returns(bytes32[]){
        /*
        get all  your donation IDs 
        */
        return(_donationids[msg.sender]);
    }
    
    function getdonation(bytes32 _txid) public returns(bytes32, uint256, string, uint, string){
        /*
        get the donation information
        */
        return(_txid,
        _donationinf[_txid].programID,
        _donationinf[_txid].programType,
        _donationinf[_txid].amount,
        _donationinf[_txid].message);
    }
    
    function getinfor( uint256 id) isexist(_program.getisexist(id))  public view returns(
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
        
    function implementProgram( uint256 id, string _describe) 
    ownerOnly(_program.getaddr(id)) 
    isexist(_program.getisexist(id)) 
    public {
        /*
        updata the process of program
        */
        emit implementlog(id, _describe);
        _program.implement(id, _describe);
    }
    
    
    function getprocess( uint256 id) public view returns(string[], uint[]){
        /*
        get the process of program
        */
        return(_program.getprocess(id));
    }
   
   
        /*
        some integral work
        */
    function dealPoints( address _to, uint _value) public {
        /*
        send integral
        */
       uint balance = _integral.getBalance(msg.sender);
       require(balance >= _value);
       _integral.dealPoints(msg.sender, _to, _value);
       
   }
   
   function getBalance()public  returns(uint){
       /*
       get your balance
       */
       return(_integral.getBalance(msg.sender));
   }
   
   function getTxs()public  returns(bytes32[]){
       /*
       get  all your txids
       */
       return(_integral.getTxs(msg.sender));
   }
   
   function getTxInfor( bytes32 id) public view returns(bytes32,
      address,
      address,
      uint256,
      uint){
        /*
        get txiformation from txid
        */
       return(_integral.getTxInfor(id));
   }
   
}