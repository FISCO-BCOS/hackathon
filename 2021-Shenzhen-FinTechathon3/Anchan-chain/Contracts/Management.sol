pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;
contract Management{
    // enum entityType{ AGENCY,AUDIT,ENTERPRISE }
    
    struct entityInfo{
        address accountAddr;
        address contractAddr;
        string pubKey;
        string field;
        // entityType accountType;
    }
    mapping (string => entityInfo) auditMap;
    mapping (string => entityInfo) agencyMap;
    mapping (string => entityInfo) enterpriseMap;
    string[] auditList;
    string[] agencyList;
    string[] enterpriseList;
    address[] auditAddressList;
    address[] agencyAddressList;

    function addAudit(string name, address accountAddr, string pubKey,string field) public {
        auditMap[name] = entityInfo(accountAddr, 0x0,pubKey,field);
        auditList.push(name);
        auditAddressList.push(accountAddr);
    }
    
    function addAgency(string name, address accountAddr, address contractAddr ,string pubKey, string field ) public {
        agencyMap[name] = entityInfo(accountAddr,contractAddr,pubKey,field);
        agencyList.push(name);
        agencyAddressList.push(contractAddr);
    }

    function addEnterprise(string name, address accountAddr,address contractAddr, string pubKey, string field) public {
        enterpriseMap[name] = entityInfo(accountAddr,contractAddr,pubKey,field);
        enterpriseList.push(name);
    }
    
    function getAuditAccnountAddr(string name) public view returns(address){
        return auditMap[name].accountAddr;
    }
    function getAuditPubKey(string name) public view returns(string) {
        return auditMap[name].pubKey;
    }
    function getAuditField(string name) public view returns(string){
        return auditMap[name].field;
    }
    
    function getAgencyAccountAddr(string name) public view returns(address) {
        return agencyMap[name].accountAddr;
    }
    function getAgencyContractAddr(string name) public view returns(address){
        return agencyMap[name].contractAddr;
    }
    function getAgencyPubKey(string name) public view returns(string) {
        return agencyMap[name].pubKey;
    }
    function getAgencyField(string name) public view returns(string){
        return agencyMap[name].field;
    }
    
    function getEnterpriseAccountAddr(string name) public view returns(address) {
        return enterpriseMap[name].accountAddr;
    }
    function getEnterpriseContractAddr(string name) public view returns(address){
        return enterpriseMap[name].contractAddr;
    }
    function getEnterprisePubKey(string name) public view returns(string) {
        return enterpriseMap[name].pubKey;
    }
    function getEnterpriseField(string name) public view returns(string){
        return enterpriseMap[name].field;
    }
    

    function getAuditList() public view returns(string[],address[]){
        return (auditList,auditAddressList);
    }
    function getAgencyList() public view returns(string[],address[]){
        return (agencyList,agencyAddressList);
    }
    function getEnterpriseList() public view returns(string[]){
        return enterpriseList;
    }
}


