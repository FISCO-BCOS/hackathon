pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

/*
store the program information, detalis
program work 
*/
contract Program{
    
    uint256 private programID = 1 ;
    
    struct Details{            //store program details
        uint256 ID;            //[program ]id
        address addr;          //publisher address
        int16 status;          //program state
        uint totalneed;        //totalneed
        uint timestamp;        //publish time
        string title;          //
        string programtype;    //
        string description;    //
        bool isexist;          //the program is exist
    }
    
    struct Devoter{
        address[] addrs;
        uint[] amount;
    }
    
    
    struct processlist{            //store the program process       
        string[] describe;
        uint[] timestamp;
    }
    
    
    struct programlist{            //store the program list
        uint256[] Ids;
        string[] titles;
    }
    
    /*
    Judge whether it exists
    */
    modifier isexist (bool isexist) {
        require(isexist);
        
        _;   
    }
    
    
    mapping(uint256 => processlist) internal   _Processlist;    
    mapping(address => programlist) internal   _Programlist;    
    mapping(uint256 => Details)  internal _Programdata;
    mapping(uint256 => Devoter)  internal _Devotelist;
    mapping(uint256 => uint)   internal  _Amountlist;

    
    event Commit(uint id, string commitmessage);


    function addTolist(address addr, uint256 id, string title) public {
        /*
        add program to list 
        */
        _Programlist[addr].Ids.push(id);
        _Programlist[addr].titles.push(title);
        
    }
    
    function addProgram(         
    /*
    add the program 
    */
        address addr,        
        uint totalneed,       
        string title,
        string programtype,
        string description) isexist(!_Programdata[programID].isexist) public returns (bool){
        
        require(totalneed >0);
        Details  memory _detail = Details(programID, addr, 0, totalneed, now, title, programtype, description, true);
        _Programdata[programID]= _detail;
        addTolist(addr, programID, title);
        emit Commit(programID , "New Program!");
        programID++;
        }
    
    function getprogram(address addr) public view returns(uint256[], string[]){
        
        return(_Programlist[addr].Ids, _Programlist[addr].titles);
    }
    
    function getinfor(uint256 id) isexist(_Programdata[id].isexist)  public view returns(
    uint256, 
    address, 
    uint, 
    uint,
    uint
    ){
        /*
        get the program information 
        no title, type and description
        */
        uint  devoternumber = getnumber(id);
        uint  total = gettotal(id);
        return(_Programdata[id].ID,
        _Programdata[id].addr,
        _Programdata[id].totalneed, 
        devoternumber,
        total);
    }
    
    function getstringinfor(uint256 id)  isexist(_Programdata[id].isexist)  public view returns(
        string,
        string,
        string){
        /*
        get the program title, type and description
        */
        return(_Programdata[id].title,
        _Programdata[id].programtype,
        _Programdata[id].description);
    }
    
    function getstatus(uint256 id)  public view returns(int16){
        /*
        get the program state
        */
        return(_Programdata[id].status);
    }
    
    function getaddr(uint256 id) public view returns(address){
        /*
        get the publisher address
        */
        return(_Programdata[id].addr);
    }
     
    function getvalue(uint256 id)    public view returns(uint){
        /*
        get the latest program need 
        */
        return(_Programdata[id].totalneed);
    }
    
    function changevalue(uint256 id, uint _value) public returns(bool){
        /*
        change the program need
        */
        _Programdata[id].totalneed = _value;
        return true;
    }
    
    function changestatus(uint256 id)  public{
        _Programdata[id].status = 1;
    }
    
    function getisexist(uint256 id) public view returns(bool){
        return(_Programdata[id].isexist);
    }
    
    function adddevoter(uint256 id, address addr, uint _amount) public{
        /*
        add devoter to one program
        */
        _Devotelist[id].addrs.push(addr);
        _Devotelist[id].amount.push(_amount);
    }
    
    function getdevoter(uint256 id) public view returns(address[], uint[]){
        /*
        get devoter in one program
        */
        return(_Devotelist[id].addrs, _Devotelist[id].amount);
    }
    
    function getnumber(uint256 id) public view returns(uint){
        /*
        get devoter number in one program
        */
        require(_Devotelist[id].addrs.length == _Devotelist[id].amount.length);
        return(_Devotelist[id].addrs.length);
    }
    
    function gettotal(uint256 id) public view returns(uint){
        /*
        get total donated value  in one program
        */
        return(_Amountlist[id]);
    }
    
    function changetotal(uint256 id, uint _value) public {
        /*
        change total donated value  in one program
        */
        _Amountlist[id] = _value;
    }
    
    function implement(uint256 id, string _describe)  public returns(bool) {  
        /*
        updata the process for one process
        */
        uint po_length =_Processlist[id].describe.length;
        uint ti_length =_Processlist[id].timestamp.length;
        _Processlist[id].describe.push( _describe);
        _Processlist[id].timestamp.push(now);   
        require(po_length == ti_length);
        
        emit Commit(id, "Make progress!");
        return true;
    }
    
    
    function getprocess(uint256 id)  public view returns (string[], uint[] ){
        require(_Processlist[id].describe.length !=0);
        return(_Processlist[id].describe, _Processlist[id].timestamp);    
        /*
        get all process for one program
        */
    }
    
}