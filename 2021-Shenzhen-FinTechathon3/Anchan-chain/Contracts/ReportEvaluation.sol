pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./License.sol";
import "./Agency.sol";
import "./Enterprise.sol";
import "./EngineerList.sol";
import "./Management.sol";
import "./Arbitrate.sol";

interface randomNumber {
    function getRandomNumber(uint256 userProvidedSeed) external returns (bytes32 );
    function get() external returns (uint256);
} //获取随机数的接口

contract ReportEvaluation{
    // address[] allAgency; //所有机构列表
    
    address licenseAddress;
    mapping (uint256 => uint) public resultMap;//用于生成随机数
    Management systemManagement;

    uint256[] randomAgencyIndex;
    address[] randomAgencyAddress;
    string[] randomAgencyName;
    string[] pubKey;

    address[] allAgency;
    string[] allAgencyName;

    address enterprise;//被评定的企业
    address relatedAgency; // 发证机构
    string[] relatedEngineer; //相关安评师
    string bussiness;
    uint start;
    uint confirmTimes;
    
    address randomNumberAddress = 0x2ab1a8217e2e471784d0c5cbe01fba1d243c2e47;
    randomNumber randGen =  randomNumber(randomNumberAddress);
    
    function addAgency(address agency) public returns(address[]){
        allAgency.length = allAgency.push(agency);
        return allAgency;
    } //test


    //添加安评机构列表
    function addAgencyList(address[] agencyList) public returns(address[]) {
        uint i;
        for (i = 0 ; i < agencyList.length; i++){
            allAgency.push(agencyList[i]);
        }
        return allAgency;
    }

    function getAgencyList(address managementAddress) public {
        systemManagement = Management(managementAddress);
        (allAgencyName,allAgency) = systemManagement.getAgencyList();
    }

    function showAgencyList() constant public returns (address[], string[]) {
        return (allAgency, allAgencyName);
    }

    function getLicenseAddr() constant public returns (address) {
        return licenseAddress;
    }
    
    //合约初始化
    constructor(address agency,string[] engineer) {
        start = 0;
        confirmTimes = 0;
        relatedAgency = agency;
        relatedEngineer = engineer;
        enterprise = msg.sender;
    }

    function randFromOracle(uint256 seed,uint256 total) public returns(uint256) {
        randGen.getRandomNumber(seed);
        return randGen.get() % total;
    }


    
    function genNextRand(uint256 seed,uint256 total) public view returns (uint256) {
        uint256 randomNumber = uint256(keccak256(abi.encodePacked(block.number, seed)));
        return randomNumber%total;
    }


    function randomAgency(uint256 length,uint256 total) public returns(uint256[]){
        delete randomAgencyIndex;
        uint256 rand;
        uint256 len;
        uint256 nonce;
        len = length;
        nonce = randFromOracle(now,total);
        while (len>0){
            rand = genNextRand(nonce,total);
            if(resultMap[rand] != 1){
                resultMap[rand] = 1;
                randomAgencyIndex.push(rand);
                nonce++;
                len--;
            }
            else{
                nonce++;
            }
        }
        for(uint256 i = 0 ; i < randomAgencyIndex.length;i++){
            delete resultMap[randomAgencyIndex[i]];
        }
        return randomAgencyIndex;
    }
    
    function startEvaluation() public returns(address[]){
        if(start == 0){
            start = 1;
            randomAgency(3, allAgency.length);
            randomAgencyAddress.push(allAgency[randomAgencyIndex[0]]);
            randomAgencyAddress.push(allAgency[randomAgencyIndex[1]]);
            randomAgencyAddress.push(allAgency[randomAgencyIndex[2]]);
        }
        return randomAgencyAddress;
    }


    //安评机构审核通过
    function confirm(){
        uint i;
        for(i=0;i<randomAgencyIndex.length;i++){
            if(allAgency[randomAgencyIndex[i]] == msg.sender){
                delete randomAgencyIndex[i];
                confirmTimes++;
                if(confirmTimes>2){
                    License newLicense = License(licenseAddress);
                    newLicense.updateStatus("有效");
                }
                break;
            }
        }
    }

    // 安评机构审核不通过 返回生成的仲裁合约地址
    function deny() public returns(address){
        uint i;
        for(i=0;i<randomAgencyIndex.length;i++){
            if(allAgency[randomAgencyIndex[i]] == msg.sender){
                // 触发仲裁，修改licenseStatus
                License newLicense = License(licenseAddress);
                newLicense.updateStatus("待仲裁");
                Arbitrate newArbitrate = new Arbitrate(enterprise);
                return address(newArbitrate);
            }
        }
    }

    //开始审核后，生成证书，并更新业务列表
    function bussinessUpdate() public returns(address){
        License newLicense;
        newLicense = new License();
        newLicense.addLicense(enterprise,relatedAgency,relatedEngineer); //调用发证合约
        Enterprise _enterprise = Enterprise(enterprise);
        address licenseAddr = _enterprise.getLicenseAddress();
        bussiness = _enterprise.getReport();
        Agency agency = Agency(relatedAgency);//更新安评机构
        agency.addBusiness(relatedEngineer, now , licenseAddr,"评价");
        //更新业务
        licenseAddress = address(newLicense);
        return address(newLicense);
    } 

    function test() public view returns(uint,uint,address,address,string[]){
        return (start,confirmTimes,enterprise,relatedAgency,relatedEngineer);
    }
    function test2() public view returns(address[],uint256[],address[]){
        return (allAgency,randomAgencyIndex,randomAgencyAddress);
    }
    
}