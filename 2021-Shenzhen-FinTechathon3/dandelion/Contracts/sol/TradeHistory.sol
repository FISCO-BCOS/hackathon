pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "../lib/Base.sol";
import "../lib/LibString.sol";
import "./AdInvesment.sol";
import "../lib/LibUint.sol";
import "../lib/LibAddress.sol";

//交易记录表
//转账方，数额，时间 用户购买广告积分的记录
// toAddress可删掉，肯定是给广告商我的
//此处id标识广告的id
contract TradeHistory is Base("t_tradeH", "id", "fromAddress,value,timestamp"){
    
    AdInvesment private _ad = new AdInvesment();
    
    using LibUint for uint;
    using LibAddress for address;
    
    //插入一条交易的记录
    function insert(address _from, address _to, uint _value){
        string[] memory record = new string[](2);
        record[0] = _from.toString();
        record[1] = _value.toString();
        super.insert(_ad.getId(_to), record);
    }
}