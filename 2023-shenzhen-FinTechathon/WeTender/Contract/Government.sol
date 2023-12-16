pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Project.sol";

//政府的智能合约
contract Government
{
    address gov_addr;           //政府单位地址
    string public gov_name;     //政府单位名称
    uint public bal;            //总预算    -   方便上级政府查询监督
    uint public rev;            //剩余预算  -   方便上级政府查询监督
    uint public num;            //项目数量  -   方便上级政府查询监督
    
    mapping(uint => Project) public proj;  //项目列表
    
    event newBalanceSheet(string name, uint _bal);
    
    modifier onlyOwner() 
    {
        require(msg.sender == gov_addr, "无权限!");
        _;
    }
    
    //初始化
    constructor(string name) public
    {
        gov_addr = msg.sender;
        gov_name = name;
        num = 0;
    }
    
    //设置预算
    function setBal(uint _bal) public onlyOwner()
    {
        bal = _bal;
        rev = bal;
        emit newBalanceSheet(gov_name, bal);
    }
    
    //立项
    function CreateProj(uint ProjectID, string ProjectName, uint money) public onlyOwner()
    {
        require(money <= rev, "预算不足!!!");
        proj[ProjectID] = new Project(ProjectID, ProjectName, money);
        rev -= money;
        num++;
    }
    
    //开标
    function Deal(uint ProjectID, address target, string remark) public onlyOwner() 
    {
        require(proj[ProjectID].getStatus() == 0, "该项目不存在!");
        proj[ProjectID].Deal(target, remark);
    }
    
    //通过验收
    function Pass(uint ProjectID, uint money, string remark) public onlyOwner()
    {
        require(proj[ProjectID].getStatus() == 3, "项目未完成!");
        proj[ProjectID].Pass(money, remark);
    }
    
    //投入使用
    function Work(uint ProjectID, string remark) public onlyOwner()
    {
        require(proj[ProjectID].getStatus() == 4, "项目未验收!");
        proj[ProjectID].Work(remark);
        num--;
    }
    
    //项目结算
    function Salary(uint ProjectID, uint money, string remark) public onlyOwner()
    {
        require(proj[ProjectID].getStatus() == 5, "项目未投入使用!");
        proj[ProjectID].Salary(money, remark);
    }
    
    //售后修缮
    function AfterMarket(uint ProjectID, uint money, string remark) public onlyOwner()
    {
        require(proj[ProjectID].getStatus() == 5, "没有权限!");
        proj[ProjectID].AfterMarket(money, remark);
        num++;
    }
    
    //查询项目状态
    function getStatus(uint ProjectID) public view returns(uint)
    {
        return proj[ProjectID].getStatus();
    }
    
    //查询项目使用资金
    function getPrice(uint ProjectID) public view returns(uint)
    {
        return proj[ProjectID].getPrice();
    }
    
    //查询项目剩余资金
    function getRev(uint ProjectID) public view returns(int)
    {
        return proj[ProjectID].getRev();
    }
    
    // 查询溯源记录
    function getTraceInfo(uint ProjectID) public view returns(Project.TraceData[] memory _data) 
    {
        return proj[ProjectID].getTraceInfo();
    }
}