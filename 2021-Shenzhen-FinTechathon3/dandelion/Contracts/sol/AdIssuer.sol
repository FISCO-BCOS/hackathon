pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "../lib/Base.sol";
import "../lib/LibAddress.sol";

//广告方的具体信息
// contract billOwner is Base("t_adOwner", "k_adOwner", "id,address,name,avatar") {
    
// }

//广告方的具体信息
contract AdIssuer is Base("t_ad_issuer","k_ad_issuer", "address,name,avatar") {
    
    using LibAddress for address;
    
    /**
     * 根据地址判断此广告方是否存在
     */ 
    function exist_bill(address account) public returns (bool){
        string[] memory filter = filter_form();
        string memory str = account.toString();
        filter[0] = str;
        Entries entries; int count;
        (entries, count) = select_entries("", filter);
        if(count == 0) return false;
        return true;
    }
}