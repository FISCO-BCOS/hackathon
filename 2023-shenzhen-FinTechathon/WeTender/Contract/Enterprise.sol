pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Project.sol";

contract Enterprise
{
    address etp_addr;           //企业地址
    string public etp_id;       //企业编号
    string public etp_name;     //企业名称
    uint public num;            //企业项目数量
    uint public profit;         //企业利润
    
    
    mapping(uint => address) public proj;       //企业项目列表
    
    modifier onlyOwner() 
    {
        require(msg.sender == etp_addr, "无权限!");
        _;
    }
    
    constructor(string id, string name) public
    {
        etp_addr = msg.sender;
        etp_id = id;
        etp_name = name;
        num = 0;
    }
    
    //开标同步
    function AddProj(uint ProjectID, address ProjectAddr, string remark) external
    {
        proj[ProjectID] = ProjectAddr;
        num++;
    }
    
    //施工
    function Build(uint ProjectID, uint money, string remark) public onlyOwner()
    {
        Project(proj[ProjectID]).Build(money, remark);
    }
    
    //竣工
    function Finish(uint ProjectID, string remark) public onlyOwner()
    {
        Project(proj[ProjectID]).Finish(remark);
        num--;
    }
    
    //项目成交同步
    function AddProfit(uint money) external
    {
        require(money > 0, "内容非法!");
        profit += money;
    }
}