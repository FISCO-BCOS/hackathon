pragma solidity >=0.4.22 <0.6.0;
contract PileRepair{
    
    mapping(address => maintainer) public user;
    mapping(address => chargingPile) public pile;
    
    //维修人员
    struct maintainer{
        string name;//姓名
        int identifier;//职工编号
        int money;//奖励的钱数
        int creditPoint;//信用分
        bool isAuction;//是否正在参与维修，正在参与维修的不能维修其他桩
    }
    

    //桩
    struct chargingPile{
        string name;//设备名
        int id;//充电站id
        int locationIdentifier;//车位号
        string location;//经纬度
        int price;//价格/度
        int moneyNum;//桩本身带多少钱，交易用
        bool isOnline;//是否在线
        bool isComplete;//是否是完好的
        int maxCredit;//存放申请者的最大信用分
        address maintainerAddr;//申请者地址
        int count;//申请人数
    }
    
    //维修人员注册
    function registerUser(string name,int Identifier) public returns(string, int, int, int)
    {
        user[msg.sender].name = name;
        user[msg.sender].identifier = Identifier;
        user[msg.sender].money = 500;
        user[msg.sender].creditPoint = 500;
        user[msg.sender].isAuction = false;
        return(user[msg.sender].name, user[msg.sender].identifier, user[msg.sender].money, user[msg.sender].creditPoint);
    }
    
    //充电桩注册
    function registerPile(string Name,int Id,int locationIdentifier,string Location) public returns(string, int, int, string)
    {
        pile[msg.sender].name = Name;
        pile[msg.sender].id = Id;   //充电站id
        pile[msg.sender].locationIdentifier = locationIdentifier;   //车位号
        pile[msg.sender].location = Location;   //经纬度
        pile[msg.sender].moneyNum = 1000;
        pile[msg.sender].isOnline = true;
        pile[msg.sender].isComplete = true;
        pile[msg.sender].maxCredit = 0;
        pile[msg.sender].count = 0;
        return(pile[msg.sender].name, pile[msg.sender].id, pile[msg.sender].locationIdentifier,  pile[msg.sender].location);
    }
    
    //维修人员申请维修
    function auctionFix(address addr) public payable
    {
         require(pile[addr].count<3);
         require(!pile[addr].isComplete);
         user[msg.sender].isAuction = true;
         if(user[msg.sender].creditPoint > pile[addr].maxCredit)
         {
             pile[addr].maxCredit = user[msg.sender].creditPoint;
             pile[addr].maintainerAddr = msg.sender;
             pile[addr].count++;
         }
    }
    
    
    //根据维修结果进行奖励惩罚
    function isFix() public payable
    {
        address addr;
        addr = pile[msg.sender].maintainerAddr;
        if(!pile[msg.sender].isComplete)
        {
            user[addr].creditPoint-=2;
        }
        else
        {
            pile[msg.sender].moneyNum -= 100;
            user[addr].money += 100;
            user[addr].creditPoint+=1;
        }
        
        pile[msg.sender].maxCredit =  0;
        pile[msg.sender].count =  0;
    }
    
    //查询维修人员
    function queryUser() public returns(string, int, int, int)
    {
        return(user[msg.sender].name, user[msg.sender].identifier, user[msg.sender].money, user[msg.sender].creditPoint);
    }
    
    //查询充电桩
    function queryPile() public returns(string, int, int, string)
    {
        return(pile[msg.sender].name, pile[msg.sender].id, pile[msg.sender].locationIdentifier,  pile[msg.sender].location);
    }
    
    function getName(address addr) public view returns(string)
    {
            return(user[addr].name);
    }
    
    function getIdentifier(address addr) public view returns(int)
    {
            return(user[addr].identifier);
    }
    
    function getMoney(address addr) public view returns(int)
    {
            return(user[addr].money);
    }

    function getCreditPoint(address addr) public view returns(int)
    {
            return(user[addr].creditPoint);
    }
    
    function getIsAuction(address addr) public view returns(bool)
    {
            return(user[addr].isAuction);
    }

    function getChargingPilename(address addr) public view returns(string)
    {
            return(pile[addr].name);
    }
    
    function getChargingPileid(address addr) public view returns(int)
    {
            return(pile[addr].id);
    }
    
    function getLocationIdentifier(address addr) public view returns(int)
    {
            return(pile[addr].locationIdentifier);
    }
    
    function getLocation(address addr) public view returns(string)
    {
            return(pile[addr].location);
    }
    
    function getPrice(address addr) public view returns(int)
    {
            return(pile[addr].price);
    }
    
    function getMoneyNum(address addr) public view returns(int)
    {
            return(pile[addr].moneyNum);
    }
    
    function getIsOnline(address addr) public view returns(bool)
    {
            return(pile[addr].isOnline);
    }
    
    
    function getMaxCredit(address addr) public view returns(int)
    {
            return(pile[addr].maxCredit);
    }
    
    
    function getCount(address addr) public view returns(int)
    {
            return(pile[addr].count);
    }
    
    function getIsComplete(address addr) public view returns(bool)
    {
            return(pile[addr].isComplete);
    }
    
    function getMaintainerAddress(address addr) public payable returns(address)
    {
            return(pile[addr].maintainerAddr);
    }
}