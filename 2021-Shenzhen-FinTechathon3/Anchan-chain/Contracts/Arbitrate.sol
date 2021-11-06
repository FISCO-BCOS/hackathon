pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;
import "./License.sol";
import "./Agency.sol";
import "./Enterprise.sol";
import "./EngineerList.sol";
import "./Management.sol";

contract Arbitrate{
    mapping (uint256 => uint) public resultMap;//用于生成随机数
    address licenseAddress;
    address enterpriseAddress;
    address[] allAudit;
    uint256[] randomAuditIndex;
    address[] randomAuditAddress;    
    string[] allAuditName;
    string[] randomAuditName;
    Enterprise newEnterprise;
    Management systemManagement;
    uint confirmTimes;
    uint start;


    constructor(address _enterpriseAddress){
        enterpriseAddress = _enterpriseAddress;
        newEnterprise = Enterprise(_enterpriseAddress);
        (,,licenseAddress) = newEnterprise.get();

        License newLicense = License(licenseAddress);
        newLicense.updateStatus("待仲裁");
    }

    //添加监管部门
    function addAudit(address audit) public returns(address[]){
        allAudit.length = allAudit.push(audit);
        return allAudit;
    } 


    //获取监管部门列表
    function addAuditList(address[] auditList) public returns(address[]) {
        uint i;
        for (i = 0 ; i < auditList.length; i++){
            allAudit.push(auditList[i]);
        }
        return allAudit;
    }

    //输入管理合约地址  初始化监管部门列表
    function getAuditList(address managementAddress) public {
        systemManagement = Management(managementAddress);
        (allAuditName,allAudit) = systemManagement.getAuditList();
    }


    function rand(uint256 total) public view returns(uint256) {
        uint256 random = uint256(keccak256(abi.encodePacked(block.number, now)));
        return random%total;
    }


    
    function rand2(uint256 seed,uint256 total) public view returns (uint256) {
        uint256 randomNumber = uint256(keccak256(abi.encodePacked(block.number, seed)));
        return randomNumber%total;
    }


    //随机选择监管部门
    function randomAudit(uint256 length,uint256 total) public returns(uint256[]){
        uint256 r;
        uint256 len;
        uint256 nonce;
        len = length;
        nonce = rand(total);
        while (len>0){
            r = rand2(nonce,total);
            if(resultMap[r] != 1){
                resultMap[r] = 1;
                randomAuditIndex.push(r);
                nonce++;
                len--;
            }
            else{
                nonce++;
            }
        }
        return randomAuditIndex;
    }


    //开始仲裁 返回选出的监管部门的名称和公钥
    function startAudition() public returns(string[],string[], address[]){
        string[] pubKey;
        
        if(start == 0){
            start = 1;
            randomAudit(3, allAuditName.length);
            // randomAuditIndex = [0];
            randomAuditName.push(allAuditName[randomAuditIndex[0]]);
            randomAuditName.push(allAuditName[randomAuditIndex[1]]);
            randomAuditName.push(allAuditName[randomAuditIndex[2]]);
            randomAuditAddress.push(allAudit[randomAuditIndex[0]]);
            randomAuditAddress.push(allAudit[randomAuditIndex[1]]);
            randomAuditAddress.push(allAudit[randomAuditIndex[2]]);
            pubKey.push(systemManagement.getAuditPubKey(randomAuditName[0]));
            pubKey.push(systemManagement.getAuditPubKey(randomAuditName[1]));
            pubKey.push(systemManagement.getAuditPubKey(randomAuditName[2]));
        }

        return (randomAuditName,pubKey, randomAuditAddress);
    }

    //监管部门通过
    function confirm() returns (bool) {
        uint i;
        for(i=0;i<randomAuditIndex.length;i++){
            if(allAudit[randomAuditIndex[i]] == msg.sender){
                // delete randomAuditIndex[i];
                confirmTimes++;
                if(confirmTimes>2){
                    License newLicense = License(licenseAddress);
                    newLicense.updateStatus("有效");
                }
                return true;
            }
        }
        return false;
    }

    // 监管部门审核不通过
    function deny() returns (bool) {
        uint i;
        for(i=0;i<randomAuditIndex.length;i++){
            if(allAudit[randomAuditIndex[i]] == msg.sender){
                // 触发仲裁，修改licenseStatus
                License newLicense = License(licenseAddress);
                newLicense.updateStatus("仲裁不通过");
                return true;
            }
        }

        return false;
    }


}




