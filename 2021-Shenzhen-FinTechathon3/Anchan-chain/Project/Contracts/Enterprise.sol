pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;
import "./ReportEvaluation.sol";
//以string的形式存储IPFS地址
contract Enterprise{
    string name; 
    string representative;
    string addr;
    string enterpriseType;
    string enterpriseLimit; //公司基本信息

    string encryptedDataIpfs;
    string encryptedDataHash;

    string reportIpfs;
    string reportHash;

    address licenseAddress;
    ReportEvaluation evaluation;
    
    constructor() {
        encryptedDataIpfs = "";
        reportIpfs = "";
        licenseAddress = 0;
    }

    // function set_data(string data) public{
    //     encryptedDataIpfs = data;
    // }

    // function set_report(string report) public{
    //     reportIpfs = report; 
    // }
    
    function setLicense() public {
            licenseAddress = msg.sender;
    }
    
    function set(string data,string report) private{
        encryptedDataIpfs = data;
        reportIpfs = report; 
    }

    function get()constant returns(string,string,address){
        return (encryptedDataIpfs, reportIpfs, licenseAddress );
    }
    
    function getReport() constant returns(string){
        return reportIpfs;
    }

    function getLicenseAddress() constant returns(address){
        return licenseAddress;
    }
    
    function setInformation(string _name,string _representative,string _addr,string _enterpriseType,string _enterpriseLimit) public {
        name = _name; 
        representative = _representative;
        addr = _addr;
        enterpriseType = _enterpriseType;
        enterpriseLimit = _enterpriseLimit;
    }

    function getInformation() public returns(string,string,string,string,string){
        return (name,representative,addr,enterpriseType,enterpriseLimit);
    }

    function update(string reporthash,string report,address agency,string [] engineer) public returns(address){
        reportIpfs = report;
        reportHash = reporthash;
        evaluation = new ReportEvaluation(agency,engineer);
        return address(evaluation);
    }

    function updateData(string datahash, string dataipfs) public {
        encryptedDataIpfs = dataipfs;
        encryptedDataHash = datahash;
    }
}