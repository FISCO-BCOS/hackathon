pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;
import "./ReportEvaluation.sol";
import "./EngineerList.sol";
import "./Credit.sol";

contract Agency {
    //安评师列表合约地址
    address engList;

    //机构公钥证书
    string agencyCert;

    //业务信息   
    struct businessInfo{ 
        uint time;
        address licenseAddr;
        string bussinessType;
    }

    //安评机构业务列表
    businessInfo[] businessList;


    //信用评价合约地址
    address creditAddr;

    //资质分
    int credit;
    

    //设置机构证书
    function setAgencyCert(string cert) public {
        agencyCert = cert;
    }

    //获取机构证书
    function getAgencyCert() public view returns(string) {
        return agencyCert;
    }

    //设置安评师列表合约的合约地址 ，以便addbusiness 
    function setEngListAddr(address addr) public {
        engList = addr;
    }

    //获取安评师列表合约地址
    function getEngListAddr() public view returns (address){
        return engList;
    }

    function confirm(address evaluationAddress, string[] id) public {
        ReportEvaluation evaluation = ReportEvaluation(evaluationAddress);
        address licenseAddr = evaluation.getLicenseAddr();
        addBusiness(id, now, licenseAddr, "审查");
        evaluation.confirm();
    }

    // 不通过审核
    function deny(address evaluationAddress, string[] id) public returns(address) {
        ReportEvaluation evaluation = ReportEvaluation(evaluationAddress);
        address licenseAddr = evaluation.getLicenseAddr();
        addBusiness(id, now, licenseAddr, "审查");
        return evaluation.deny();
    }
    
    // 为机构添加业务，同时为多个安评师添加业务，indexes记录安评师索引。
    function addBusiness(string[] id ,uint time,address licenseAddr,string bussinessType) public {
        businessInfo memory tempInfo ;
        tempInfo.time = time;
        tempInfo.licenseAddr = licenseAddr;
        tempInfo.bussinessType = bussinessType;
        businessList.push(tempInfo);
        uint i;
        for(i =0; i< id.length;i ++){
            EngineerList(engList).addBusiness(id[i],time,licenseAddr,bussinessType);
        }
        updateCredit();
    }
    
    //输出当前机构参与的所有业务
    function showBusiness() public view returns(businessInfo[]){
        return businessList;
    }

    //设置信用评价合约地址
    function setCreditAddr(address addr) public {
        creditAddr = addr;
    }

    //获取信用评价合约地址
    function getCreditAddr() public view returns(address){
        return creditAddr;
    }

    //更新信用分
    function updateCredit() public {
        require(creditAddr != address(0), "creditAddr must be set before updateCredit");
        credit = Credit(creditAddr).computeAgencyCredit(businessList);
    }

    //获取信用分
    function getCredit() public view returns(int) {
        return credit;
    }
}

