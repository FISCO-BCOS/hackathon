pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Enterprise.sol";

contract Project
{
    struct TraceData {
        address addr;       //操作者地址
        int status;         //项目状态
        uint money;         //操作预算
        uint timestamp;     //操作时间
        string remark;      //备注
    }
    
    uint public ProjectId;         //项目编号
    string public ProjectName;     //项目名称
    uint public _status;             //项目状态
    // 0.已立项; 1.已招标; 2.正在施工; 3.已竣工; 4.已验收; 5.已投入使用; 6.售后修缮;
    
    uint public bal;                //项目预算
    uint used_money;                //当前使用总预算
    
    TraceData[] public traceData;   //追踪表
    
    address public owner;              //项目归属方
    address public builder;            //项目承包方
    
    modifier onlyOwner() 
    {
        require(msg.sender == owner, "无权限!");
        _;
    }
    modifier onlyBuilder() 
    {
        require(msg.sender == builder, "无权限!");
        _;
    }
    
    event newStatus(address addr, int status, uint money, uint timestamp, string remark);
    
    //初始化
    constructor(uint id, string name, uint balance) public
    {
        ProjectId = id;
        ProjectName = name;
        bal = balance;
        used_money = 0;
        _status = 0;
        owner = msg.sender;
        traceData.push(TraceData({addr:msg.sender, status:0, money:0, timestamp:now, remark:"项目立项"}));
        emit newStatus(msg.sender, 0, 0, now, "create");
    }
    
    //项目开标（只有所有者可以操作）
    function Deal(address target, string remark) external onlyOwner() 
    {
        builder = target;
        Enterprise(builder).AddProj(ProjectId, this, remark);       //项目信息自动同步承包方
        _status = 1;
        traceData.push(TraceData({addr:msg.sender, status:1, money:0, timestamp:now, remark:remark}));
        emit newStatus(msg.sender, 1, 0, now, remark);
    }
    
    //正在施工（只有中标者可以操作）
    function Build(uint money, string remark) external onlyBuilder() 
    {
        traceData.push(TraceData({addr:msg.sender, status:2, money:money, timestamp:now, remark:remark}));
        used_money += money;
        _status = 2;
        emit newStatus(msg.sender, 2, money, now, remark);
    }

    //已竣工（只有中标者可以操作）
    function Finish(string remark) external onlyBuilder()
    {
        traceData.push(TraceData({addr:msg.sender, status:3, money:0, timestamp:now, remark:remark}));
        _status = 3;
        emit newStatus(msg.sender, 3, 0, now, remark);
    }
    
    //已验收（只有所有者可以操作）
    function Pass(uint money, string remark) external onlyOwner()      //这里的money是用来请第三方检测验收的预算
    {
        traceData.push(TraceData({addr:msg.sender, status:4, money:money, timestamp:now, remark:remark}));
        used_money += money;
        _status = 4;
        emit newStatus(msg.sender, 4, 0, now, remark);
    }
    
    //投入使用（只有所有者可以操作）
    function Work(string remark) external onlyOwner()
    {
        traceData.push(TraceData({addr:msg.sender, status:5, money:0, timestamp:now, remark:remark}));
        _status = 5;
        emit newStatus(msg.sender, 5, 0, now, remark);
    }
    
    //项目结算（只有所有者可以操作）
    function Salary(uint money, string remark) external onlyOwner()
    {
        traceData.push(TraceData({addr:msg.sender, status:5, money:money, timestamp:now, remark:remark}));
        Enterprise(builder).AddProfit(ProjectId, money);
        used_money += money;
        emit newStatus(msg.sender, 5, money, now, remark);
    }
    
    //售后修缮（只有所有者可以操作）
    function AfterMarket(uint money, string remark) external onlyOwner()
    {
        traceData.push(TraceData({addr:msg.sender, status:6, money:money, timestamp:now, remark: remark}));
        used_money += money;
        _status = 6;
        Enterprise(builder).AddProj(ProjectId, this, remark);       //项目信息自动同步承包方
        emit newStatus(msg.sender, 6, money, now, remark);
    }
    
    //查询项目状态
    function getStatus() public view returns(uint)
    {
        return _status;
    }
    
    //查询项目使用资金
    function getPrice() public view returns(uint)
    {
        return used_money;
    }
    
    //查询项目剩余预算（只有所有者可以查看）
    function getRev() public onlyOwner() view returns(int)
    {
        return int(bal) - int(used_money);
    }
    
    //查询溯源记录
    function getTraceInfo() public view returns(TraceData[] memory _data) 
    {
        return traceData;
    }
}