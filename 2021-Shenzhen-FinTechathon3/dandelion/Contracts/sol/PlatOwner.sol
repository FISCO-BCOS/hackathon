pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;
import "../lib/Base.sol";
import "../lib/LibAddress.sol";

//平台方的具体信息
contract PlatOwner is Base("t_plat", "k_plat", "address,name,avatar") {
    
    using LibAddress for address;
    
    //根据地址判断该平台方是否存在
    function exist_plat(address account) public returns (bool){
        string[] memory filter = filter_form();
        filter[0] = account.toString();
        Entries entries; int count;
        (entries, count) = select_entries("", filter);
        if(count == 0) return false;
        return true;
    }
    
    //获取所有的平台方地址信息
    function getAllAddress() public returns(string[] memory){
        string[] memory filter = filter_form();
        Entries entries; int count; Entry entry;
        (entries, count) = select_entries("", filter);
        string[] memory result = new string[](uint(count));
        for(uint i = 0; i < result.length; i++){
            entry = entries.get(int(i));
            result[i] = entry.getString("address");
        }
        return result;
    }
    
}