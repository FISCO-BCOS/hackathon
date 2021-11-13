pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "../lib/Base.sol";
import "../lib/LibString.sol";

//交易记录表
//所有交易的记录表,包括但不限于积分结现，数字资产转移，积分平台分红
// 交易名称，起止地址（发生在资产转移时两者都有，积分结现只有fromAddress，出售积分只有toAddress），价值（有正负号）,时间戳
// 第一个address是用户的地址，当前插入数据地址为：0x65efc6c14414a313519c36d159910f8bd497e5a3
contract AllTradeHistory is Base("t_allTrade1", "address", "title,fromAddress,toAddress,value,timestamp"){
    
}