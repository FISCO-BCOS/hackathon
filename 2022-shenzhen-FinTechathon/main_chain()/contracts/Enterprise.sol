// SPDX-License-Identifier: MIT
   pragma solidity ^0.6.10;
   pragma experimental ABIEncoderV2;
   import "Permission.sol";
   import "Goverment.sol";
   import "Audit.sol";
   
   contract Enterprise{
       Permission  P;
       Goverment G;
       Audit A;
       
       address developer;
       mapping (string => uint)   serialNumber;               //每次企业上传数据序号（相当于时间戳）
       
       mapping (string => ECaccount) public Caccount;          //通过企业名字绑定碳账户 
       
       mapping(uint =>mapping (string => string[]) ) dataName;              //每个企业的数据名称组
       
       struct ECaccount {                               //企业的排污数据及碳账户余额             
         mapping(uint => mapping (string => uint)) Data;
         uint total;
         uint dailyEmissions;
         
       }
       
       modifier onlyEnterprise(Permission P_){
                  require(P_.getPermission(tx.origin) == 111,"Only enterprise can call this.");
                  _;
          }
          
       modifier onlyDeveloper(){
                  require( msg.sender == developer,"Only developer can call this.");
                  _;
          }
          
       constructor (Permission  P_) public {
           P = P_;
           developer = msg.sender;
       }
       
    function contractAddress(Goverment G_, Audit A_) public onlyDeveloper{
        G = G_;
        A = A_;
    }
    
    //政府写入数据
    function GoverWrite(string memory name, uint EMright) public{
        require(P.getPermission(tx.origin) == 222, "Only goverment can call this");
            Caccount[name].total =  EMright;
    }
    
    //企业写入数据
    function enterpriseWrite(string memory name,string [] memory dataName_, uint [] memory dataQuantity,uint sNumber) public onlyEnterprise(P){
        
            require(sNumber == serialNumber[name] + 1, "Please correct the serial number");
            for(uint i = 0;i<dataName_.length; i++){
                Caccount[name].Data[sNumber][dataName_[i]] = dataQuantity[i];
                dataName[sNumber][name].push(dataName_[i]);
            }
            serialNumber[name] = sNumber;
            remainingEmissions(sNumber, name);
    }
    
    //根据上传数据完成剩余碳排放量计算
    function remainingEmissions(uint serialNumbe_, string memory name) private{
        
        uint total_ = Caccount[name].total;
        uint dailyEmissions_ = Caccount[name].dailyEmissions;
        for(uint i = 0; i < dataName[serialNumber[name]][name].length; i++){
            total_ -= Caccount[name].Data[serialNumbe_][dataName[serialNumbe_][name][i]];
            dailyEmissions_ +=Caccount[name].Data[serialNumbe_][dataName[serialNumbe_][name][i]];
        }
        Caccount[name].total = total_;
        Caccount[name].dailyEmissions = dailyEmissions_;
    }
    
    //查询企业上传数据每个dataName对应数量
    function enterpriseView(string memory name, string memory gasname, uint time) public view returns(uint ){
        return Caccount[name].Data[time][gasname];
    }
    
    //查询企业在某份上传数据中的dataName名单
    function dataNameView(string memory name,uint sNumber)public view returns(string [] memory){
        return dataName[sNumber][name];
    }
    
    //查看企业碳排放权以及剩余碳排放量
    function ViewRight(string memory name) public view returns(uint , uint ){
        return (Caccount[name].total, Caccount[name].dailyEmissions);
    }
    
    //企业之间碳交易
    function cTransaction(string memory sender ,string memory receiver , uint amount) public onlyDeveloper{
        require(Caccount[sender].total >= amount, "Sorry, your credit is running low");
        Caccount[sender].total -= amount;
        Caccount[receiver].total += amount;
    }
    
   }