pragma solidity >=0.4.22 <0.6.0;
contract EnergyTrading{

    int opTradeWithCharge = 8;
    int userTradeWithCharge = 10;

    // constructor (int opPrice, int pilePrice) public
    // {
    //     opTradeWithCharge = opPrice;
    //     userTradeWithCharge = pilePrice;
    // }

    mapping(address => User) public user;
    mapping(address => ChargingPile) public sell;
    mapping(address => Operator) public op;

    struct User//电动汽车用户
    {
        string name;
        string carID;//用户对应车牌号
        int money;//用户剩余金额
        int totalEle;//车辆电池容量
        int remainEle;//车辆剩余电量
        int allCostEle;//总共冲过的电
    }

    struct ChargingPile//充电桩
    {
        int identifier;
        int money;//充电桩账户金额
        int costEle;//充电桩总共充过的电
    }

    struct Operator
    {
        int eleCost;//运营商总共发出的电
        int money;//运营商账户金额
    }
    
    //根据博弈对电能定价
    function setPrice(int opPrice, int pilePrice) public
    {
        opTradeWithCharge = opPrice;
        userTradeWithCharge = pilePrice;
    }

    //注册用户
    function registerUser(string name, string carID, int totalEle, int remainEle, int money) public returns(string, string, int, int, int)
    {
        user[msg.sender].name = name;
        user[msg.sender].carID = carID;
        user[msg.sender].money = money;
        user[msg.sender].totalEle = totalEle;  //电动车的电池电量 
        user[msg.sender].remainEle = remainEle;     //电动车剩余电量
        user[msg.sender].allCostEle = 0;   //已经冲过多少电
        return (user[msg.sender].name, user[msg.sender].carID, user[msg.sender].totalEle, user[msg.sender].remainEle, user[msg.sender].money);
    }

    //注册充电桩
    function registerCharger(int num) public
    {
        sell[msg.sender].identifier = num;
        sell[msg.sender].money = 100000;
        sell[msg.sender].costEle = 0;   //总共充过的电
    }

    //注册运营商
    function registerOperator() public
    {
        op[msg.sender].eleCost = 0;     //总共充过的电 
        op[msg.sender].money = 10000000;
    }

    //充电桩和电动汽车用户电能交易
    function tradeForUser(address addr, int ele) public payable
    {
        require(ele<=user[msg.sender].totalEle - user[msg.sender].remainEle, "exceed the max compacity");
        require(ele*userTradeWithCharge<=user[msg.sender].money, "exceed the max user money");
          user[msg.sender].allCostEle += ele;
          user[msg.sender].remainEle -= ele;
          user[msg.sender].money -= ele * userTradeWithCharge;
          sell[addr].money += ele * userTradeWithCharge;
          sell[addr].costEle += ele;
    }

    //电网公司和充电桩电能交易结算
    function tradeWithCharger(address addr) public payable
    {
        op[msg.sender].eleCost += sell[addr].costEle;
        op[msg.sender].money += sell[addr].costEle * opTradeWithCharge;
        sell[addr].money -= sell[addr].costEle * opTradeWithCharge;
        sell[addr].costEle = 0;
    }

    function getUserRemainEle(address addr) public view returns(int)
    {
            return(user[addr].remainEle);
    }

    function getUserMoney(address addr) public view returns(int)
    {
            return(user[addr].money);
    }

    function getSellerMoney(address addr) public view returns(int)
    {
            return(sell[addr].money);
    }

    function getSellerEleCost(address addr) public view returns(int)
    {
            return(sell[addr].costEle);
    }

    function totalEleCost(address addr) public view returns(int)
    {
            return(op[addr].money);
    }
}