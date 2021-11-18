pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "../lib/Base.sol";
import "../lib/LibString.sol";
import "../lib/LibUint.sol";
import "../PlatOwner.sol";
import "../DIGIT.sol";
import "../lib/LibAddress.sol";

//广告位的材料
//标题，广告的拥有者,封面图片，需要的积分量，已经募集到的积分量，需要参与方的数量，参与方的信息详情，面向人群，投放规则，开始时间，结束时间,
// 分隔符 ^,^
contract Advertise is Base("t_ad2", "k_ad1", "id,title,adOwner,coverPic,integralNeed,integralGet,participantNeed,participants,target,rule,startTime,endTime,timestamp") {
    
    using LibString for string;
    using LibUint for uint;
    using LibAddress for address;
    
    string splitString = ",";
    
    PlatOwner private _platOwner = new PlatOwner();

    /**
     * 返回该广告位需要的积分数和已经募集到的积分数
     */ 
    function getRestIntegral(string key_field, string memory id) public view returns(uint256 integralNeed, uint256 integralGet){
        require(bytes(id).length != 0, "Advertise: getRestIntegral id could not be null");
        string[] memory filter = filter_form();
        filter[0] = id;
        Entries entries; int count;
        (entries, count) = select_entries("", filter);
        require(count == 1,"Advertise: the ad was not exist");
        Entry entry = entries.get(0);
        integralNeed = uint256(entry.getInt("integralNeed"));
        integralGet = uint256(entry.getInt("integralGet"));
    }
    
    /**
     * 更新募集到一定数量的积分
     */
    function updateIntegral(string key_field, string memory id, uint integralNeed, uint integralGet) public {
        require(bytes(id).length != 0, "Advertise updateIntegral id could not be null");
        string[] memory filter = filter_form();
        filter[0] = id;
        string[] memory newRecord = new string[](12);
        newRecord[4] = integralNeed.toString(); newRecord[5] = integralGet.toString();
        update(key_field, newRecord, filter);
    }
    /**
     * 重写了insert接口，使其id可以自增，限定只有平台方可以增加
     */ 
    function insert(string key_field, string[] memory record)public returns (int256){
        // require(_platOwner.exist_plat(record[2].toAddress()), "Advertise： insert 只有平台方可以增加广告位");
        // uint256 integralNeed = uint256(record[4].toInt());
        // require(digit.balance((msg.sender).toString()) >= integralNeed, "Advertise： insert 广告方的积分额度不足");
        string[] memory filter = filter_form();
        Entries entries; int count; uint amount = 1;
        (entries, count) = select_entries("", filter);
        require(count >= 0);
        if(count > 0){
            Entry entry = entries.get(count - 1);
            amount = uint(entry.getInt("id")) + 1;
        }
        record[0] = amount.toString();
        super.insert(key_field, record);
    }

    /**
     * 广告商申请广告位
     */ 
    function application(string key_field, address account, string memory targetId) public{
        require(account != address(0), "Advertise: application account could not be null");
        require(bytes(targetId).length != 0, "Advertise: application targetId could not be null");
        string[] memory filter = filter_form();
        filter[0] = targetId;
        string[] memory newRecord = new string[](12); 
        int count = 0;
        Entries entries;
        (entries, count) = select_entries("",filter);
        require(count == 1, "Advertise: application");
        Entry entry = entries.get(0);
        string memory participants = entry.getString("participants");
        require(participants.indexOf(account.toString()) == -1, "Advertise, application:this participant is exist");
        if(bytes(participants).length > 0){
            participants = LibString.concat(participants, splitString);
        }
        participants = LibString.concat(participants, account.toString());
        newRecord[7] = participants;
        update(key_field, newRecord, filter);
    }
    
    function _exit(address account, string memory targetId) public {
        require(account != address(0), "Advertise _exit account could not be null");
        require(bytes(targetId).length != 0, "Advertise: _exit targetId could not be null");
        string[] memory filter = filter_form();
        string[] memory newRecord = new string[](12);
        filter[0] = targetId;
        int count; Entries entries;
        (entries, count) = select_entries("",filter);
        require(count > 0, "Advertise: _exit targetId point to null advertise");
        Entry entry = entries.get(0);
        
    }
}