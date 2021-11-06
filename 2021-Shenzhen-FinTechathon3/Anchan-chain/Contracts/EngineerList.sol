pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;
import "./Credit.sol";
contract EngineerList{
    //业务内容，包括时间、证书地址、业务类型
    struct businessInfo{ 
        uint time;
        address licenseAddr;
        string bussinessType;
    }

    //安评师信息，包括安评师姓名、安评师所属领域、安评师证书、安评师所属安评机构、安评师业务列表
    struct engineerInfo{
        string name;
        string field;
        string safetyEvaluationCertificate;
        string agency; 
        int credit;
        businessInfo[] businessList;
    }

    //以安评师证书id为索引创建Map
    mapping (string => engineerInfo) engineerMap;

    //信用合约地址
    address creditAddr;

    
    //添加安评师，
    function addEngineer(string _name, string id, string _field,string _cert,string _agency) public  {
        engineerMap[id].name = _name;
        engineerMap[id].field = _field;
        engineerMap[id].safetyEvaluationCertificate = _cert;
        engineerMap[id].agency = _agency;
    }

    //获取安评师信息，输入安评师证书id
    function getEngineer(string id) public view returns(engineerInfo){
        return engineerMap[id];
    }

    //删除安评师所属安评机构
    function deleteAgency(string id ) public{
        engineerMap[id].agency ="";
    }

    //安评师设置新机构
    function setAgency(string id,string agen) public {
        if ( bytes(engineerMap[id].agency).length == 0 && bytes(agen).length != 0)  {
            engineerMap[id].agency = agen;            
        }
    }

    //为安评师添加新业务信息
    function addBusiness(string id,uint time,address addr,string bType) public {
        businessInfo memory tempBusiness;
        tempBusiness.time = time;
        tempBusiness.licenseAddr = addr;
        tempBusiness.bussinessType = bType;
        engineerMap[id].businessList.push(tempBusiness);
        updateCredit(id);
    }

    function setCreditContractAddr(address addr) public  {
        creditAddr = addr;
    }
    function getCreditContractAddr() public view returns(address) {
        return creditAddr;
    } 

    function updateCredit(string id ) public  {
        require(creditAddr != address(0), "creditAddr must be set before updateCredit");
        engineerMap[id].credit = Credit(creditAddr).computeEngineerCredit(engineerMap[id].businessList);
    }
    
    function getCredit(string id) public view returns(int){
        return engineerMap[id].credit;
    }
}
