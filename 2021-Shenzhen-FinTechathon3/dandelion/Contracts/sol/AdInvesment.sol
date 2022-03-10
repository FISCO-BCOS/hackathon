pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "../lib/Base.sol";
import "../lib/LibString.sol";
import "../lib/LibAddress.sol";
import "../lib/LibUint.sol";
import "../Advertise.sol";

//广告引资
//标题，广告的拥有者，封面图片地址，roe积分兑换比率，需要的积分量，已经募集到的积分量，目标广告位,开始时间，结束时间,交易记录在TradeHistory表中
// 分隔符 ^,^
// contract AdInvesment is Base("t_adIn", "k_adIn", "id,title,adOwner,integralNeed,integralGet,targetAd,startTime,endTime") {
    
    
    
// }
contract AdInvesment is Base("t_adIn2", "k_adIn", "id,title,adOwner,coverPic,roe,integralNeed,integralGet,targetAd,startTime,endTime,timestamp") {  
    
    using LibString for string;
    using LibAddress for address;
    using LibUint for uint;
    
    Advertise private advertise = new Advertise();
    
    /**
     * 重写了insert的接口，使其id可以自增
     */ 
    function _insert(string key_field, string[] memory record)public returns (int256){
        // require(_platOwner.exist_plat(record[2].toAddress()), "Advertise： insert 只有广告商可以增加广告位");
        string[] memory filter = filter_form();
        string[] memory temp;int count;
        (temp, count) = select("amount", filter);
        // require(count == 1, "AdInvesment: insert 数据库异常");
        if(count == 0){
            if(bytes(record[0]).length == 0){
                string memory start = "1";
                record[0] = start;
            }
            insert("amount",record);
            (temp, count) = select("amount", filter);
        }
        temp[0] = (uint(temp[0].toInt() + 1)).toString();
        record[0] = temp[0];
        super.insert(key_field, record);
        update("amount", record, filter);
        advertise.application("", msg.sender, record[7]);
    }
    
    /**
     * 根据地址获得某广告引资的id
     */ 
    function getId(address account) public returns(string memory id){
        string[] memory filter = filter_form();
        filter[2] = account.toString();
        Entries entries; int count;
        (entries, count) = select_entries("", filter);
        require(count > 0, "AdInvesment:getId not ad");
        Entry entry = entries.get(0);
        id = entry.getString("id");
    }
    
    /**
     * 根据广告引资的id获得积分需要量和已经募集到的积分量
     */ 
    function getRequest(string memory id) public returns(uint integralNeed, uint integralGet){
        string[] memory filter = filter_form();
        filter[0] = id;
        Entries entries; int count;
        (entries, count) = select_entries("", filter);
        require(count > 0, "AdInvesment:getRequest not ad");
        Entry entry = entries.get(0);
        if(entry.getBytes32("integralNeed") == 0){
            integralNeed = 0;
        }else {
            integralNeed = entry.getUInt("integralNeed");
        }
        if(entry.getBytes32("integralGet") == 0){
            integralGet = 0;
        }
        else {
            integralGet = entry.getUInt("integralGet");
        }
    }
    
    /**
     * 在获得到一定的积分之后，对已经募集到的积分进行更新
     */ 
    function updateRequest(string memory id, uint integralNeed, uint integralGet, uint _value) public{
        require(bytes(id).length != 0,"AdInvesment: updateRequest id could not be null");
        string[] memory filter = filter_form();
        filter[0] = id;
        string[] memory record = new string[](10);
        record[5] = integralNeed.toString();
        integralGet += _value;
        record[6] = integralGet.toString();
        update("", record, filter);
        Entries entries; int count;
        (entries, count) = select_entries("", filter);
        require(count > 0, "AdInvesment: not ad");
        Entry entry = entries.get(0);
        string memory targetAd = entry.getString("targetAd");
        (integralNeed, integralGet) = advertise.getRestIntegral("", targetAd);
        uint256 _integralRest = integralNeed - integralGet;
        require(_value <= _integralRest, "integral: _transfer 交易的积分必须小于广告商需要的积分");
        advertise.updateIntegral("", targetAd, integralNeed, integralGet + _value);
    }
    
}