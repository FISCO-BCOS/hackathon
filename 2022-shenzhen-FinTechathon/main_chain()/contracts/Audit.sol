// SPDX-License-Identifier: MIT
   pragma solidity ^0.6.10;
   pragma experimental ABIEncoderV2;
   import "Permission.sol";
   import "Goverment.sol";
   import "Enterprise.sol";
   
   contract Audit{
       Permission  P;
       Goverment G;
       Enterprise E;
       address developer;
       
       modifier onlyDeveloper(){
                  require( msg.sender == developer,"Only developer can call this.");
                  _;
          }
       modifier onlyAudit(Permission P_){
                  require(P_.getPermission(tx.origin) == 333,"Only Audit can call this.");
                  _;
          }
                
       constructor(Permission  P_) public{
           P = P_;
           developer = msg.sender;
       }
       
       mapping(string => uint) serialNumber;
       mapping(uint =>mapping (string => report)) eReport;
       //报告
       struct report{
           uint industry;
           string field1;    //核算依据，核算日期范围
           string field2;    //生产过程碳排放：初始报告，核查，数据异常情况
           string field3;    //外购电力,其他;
           
       }
        function contractAddress3(Enterprise  E_, Goverment G_) public onlyDeveloper{
            E = E_;
            G = G_;
        }   
        //查询企业数据,返回映射不好解决，返回一个，让调用者循环下去。
        function enterpriseView(string memory name, string memory gasname,uint time) public view returns(uint ){
            return E.enterpriseView(name,gasname,time);
        }
    
        //查看企业碳排放权
        function ViewRight(string memory name) public view returns(uint , uint){
            return E.ViewRight(name);
        }
        //上传企业报告
        function uploadReport(string memory name, uint indus, uint sNumber, string memory field1, string memory field2, string memory field3)public onlyAudit(P){
            require(sNumber == serialNumber[name] + 1, "Please correct the serial number");
            eReport[sNumber][name].industry = indus;
            eReport[sNumber][name].field1 = field1;
            eReport[sNumber][name].field2 = field2;
            eReport[sNumber][name].field3 = field3;
            
        }
   }