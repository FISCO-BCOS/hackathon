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
    event addProfit(uint ProjectID, uint money, string remark);
    
    
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
    function AddProfit(uint ProjectID, uint money) external
    {
        require(money > 0, "内容非法!");
        profit += money;
        emit addProfit(ProjectID, money, "Finish");
    }
    
    
    //--------------中标企业与外包企业的合约-------------------
    
    mapping(uint => Project) public proj_etp;       //企业的外包项目列表
    
    //物料立项
    function CreateProj_etp(uint ProjectID, uint ProjectID_etp, string ProjectName_etp, uint money, string remark) public onlyOwner()
    {
        Build(ProjectID, money, remark);
        proj_etp[ProjectID_etp] = new Project(ProjectID_etp, ProjectName_etp, money);
    }
    
    //指定供应方
    function Deal_etp(uint ProjectID_etp, address target, string remark) public onlyOwner() 
    {
        require(proj_etp[ProjectID_etp].getStatus() == 0, "该项目不存在!");
        proj_etp[ProjectID_etp].Deal(target, remark);
    }
    
    //物料验收
    function Pass_etp(uint ProjectID_etp, string remark) public onlyOwner()
    {
        require(proj_etp[ProjectID_etp].getStatus() == 3, "项目未完成!");
        proj_etp[ProjectID_etp].Pass(0, remark);
    }
    
    //物料投入使用
    function Work_etp(uint ProjectID_etp, string remark) public onlyOwner()
    {
        require(proj_etp[ProjectID_etp].getStatus() == 4, "项目未验收!");
        proj_etp[ProjectID_etp].Work(remark);
    }
    
    //物料结算
    function Salary_etp(uint ProjectID_etp, uint money, string remark) public onlyOwner()
    {
        require(proj_etp[ProjectID_etp].getStatus() == 5, "项目未投入使用!");
        proj_etp[ProjectID_etp].Salary(money, remark);
    }
    
    //物料售后
    function AfterMarket_etp(uint ProjectID_etp, uint money, string remark) public onlyOwner()
    {
        require(proj_etp[ProjectID_etp].getStatus() == 5, "没有权限!");
        proj_etp[ProjectID_etp].AfterMarket(money, remark);
    }
    
    //查询物料状态
    function getStatus_etp(uint ProjectID_etp) public view returns(uint)
    {
        return proj_etp[ProjectID_etp].getStatus();
    }
    
    //查询物料使用资金
    function getPrice_etp(uint ProjectID_etp) public view returns(uint)
    {
        return proj_etp[ProjectID_etp].getPrice();
    }
    
    //查询项目剩余资金
    function getRev_etp(uint ProjectID_etp) public view returns(int)
    {
        return proj_etp[ProjectID_etp].getRev();
    }
    
    // 查询溯源记录
    function getTraceInfo_etp(uint ProjectID_etp) public view returns(Project.TraceData[] memory _data) 
    {
        return proj_etp[ProjectID_etp].getTraceInfo();
    }
}