// SPDX-License-Identifier: MIT
   pragma solidity ^0.6.10;
   pragma experimental ABIEncoderV2;
   import "Permission.sol";
   import "Enterprise.sol";
   import "Audit.sol";
   
   contract Goverment{
       Permission  P;
       Enterprise  E;
       Audit A;
       address developer;
       mapping(uint => uint) standard;       //行业标准
       mapping (string => uint) industry;    //企业所属行业类别
       modifier onlyGoverment(Permission P_){
                  require(P_.getPermission(tx.origin) == 222,"Only goverment can call this.");
                  _;
          }
          
       modifier onlyDeveloper(){
                  require( msg.sender == developer,"Only developer can call this.");
                  _;
          }
  
       constructor(Permission  P_) public{
           P = P_;
           developer = msg.sender;
       }
       
    
    string [] public enterpriseName;                 //企业名单
    
    mapping (string => bool)  JudgEnter;
    
    function contractAddress(Enterprise  E_, Audit A_) public onlyDeveloper{
        E = E_;
        A = A_;
    }
    function govermentWrite(string [] memory Ename,uint [] memory EMright) public onlyGoverment(P){    //政府写入数据
       
        for(uint i = 0;i < Ename.length; i++){
               if(JudgEnter[Ename[i]] != true){
                    enterpriseName.push(Ename[i]);
                    JudgEnter[Ename[i]] = true;
               }
               E.GoverWrite(Ename[i], EMright[i]);
        }
    }
    
    function enterpriseNameView() public view returns(string [] memory){                              //返回企业名单
       return enterpriseName;
    }
    
     //查询企业数据,返回映射不好解决，返回一个，让调用者循环下去。
    function enterpriseView(string memory name, string memory gasname,uint time) public view returns(uint ){
        return E.enterpriseView(name,gasname,time);
    }
    
    //查看企业碳排放权及剩余碳排放量
    function ViewRight(string memory name) public view returns(uint ,uint){
        return  E.ViewRight(name);
    }
    //公布不同行业碳排放标准
    function publishStandard(uint [] memory Industry, uint [] memory Standard) public onlyGoverment(P){
        for(uint i = 0; i<Industry.length; i++){
            standard[Industry[i]] = Standard[i];
        }
    }
    //企业所属行业认证
    function industryWrite(string memory name, uint attribute) public onlyGoverment(P){
        industry[name] = attribute;
    }
    
    
   }