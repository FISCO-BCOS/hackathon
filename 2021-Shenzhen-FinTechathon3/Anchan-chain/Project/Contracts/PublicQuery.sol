pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;
import "./Accusation.sol";
import "./Management.sol";
import "./Enterprise.sol";
import "./Credit.sol";
import "./Agency.sol";

contract PublicQuery {
    address accusationAddr;
    address managementAddr;
    address creditAddr;
    
    constructor(address _accusationAddr,address _managementAddr,address _creditAddr){
        accusationAddr = _accusationAddr;
        managementAddr = _managementAddr;
        creditAddr = _creditAddr;
    }
    
    function getLicense(string _name) public returns(address) {
        address _enterpriseAddr;
        Management _management = Management(managementAddr);
        _enterpriseAddr = _management.getEnterpriseAccountAddr(_name);
        Enterprise _enterprise = Enterprise(_enterpriseAddr);
        return _enterprise.getLicenseAddress();
    }
    
    
    function getPubkey(string _type , string _name) public constant returns (string) {
        Management _management = Management(managementAddr);
        if(_type == "生产企业"){
            return _management.getEnterprisePubKey(_name);
        }
        if(_type == "安评机构"){
            return _management.getAgencyPubKey(_name);
        }
        if(_type == "监管部门"){
            return _management.getAuditPubKey(_name);
        }
    }
    
    function getEngCredit(EngineerList.businessInfo[] businessList) payable public returns (int){
        Credit _credit = Credit(_creditAddr);
        return _credit.computeEngineerCredit(businessList);
    }
    
    function getAgencyCredit(string _name) payable public returns (int){
        Management _management = Management(managementAddr);
        _agencyAddr = _management.getAgencyContractAddr(_name);
        Agency _agency = Agency(_agencyAddr);
        return _agency.getCredit();
    }
    
}

