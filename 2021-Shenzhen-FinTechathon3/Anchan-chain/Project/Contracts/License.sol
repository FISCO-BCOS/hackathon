pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;
import "./Enterprise.sol";
contract License{
    Enterprise ent;
    // 证书内容；
    // 安评材料地址；
    // 安评师ID；
    // 安评机构ID；
    // 有效期/撤销；
    string name; 
    string representative;
    string addr;
    string enterpriseType;
    string enterpriseLimit; //公司基本信息

    string license;
    string dataIpfs;
    string reportIpfs;
    address agency;
    string[] engineer;
    uint expiration;
    string licenseStatus = "待审查";

    function showInfo() constant returns(string,string,string,address,string[]){
        return (license,dataIpfs,reportIpfs,agency,engineer);
    }

    function getInfo() constant returns(string, string, string, string, string, uint,string){
        return (name, representative, addr, enterpriseType, enterpriseLimit, expiration,licenseStatus);
    }


    function addLicense(address enterprise,address _agency,string[] _engineer){
        expiration = now + 3*365*24*60*60 * 1000;
        ent = Enterprise(enterprise);
        ent.setLicense();
        (dataIpfs,reportIpfs,) = ent.get();
        (name,representative,addr,enterpriseType,enterpriseLimit) = ent.getInformation();
        (agency,engineer) = (_agency,_engineer);
        ent.setLicense();
    }

    function revokeLicense(){
        licenseStatus = "已撤销";
    }

    function updateStatus(string status){
        licenseStatus = status;
    }
}

