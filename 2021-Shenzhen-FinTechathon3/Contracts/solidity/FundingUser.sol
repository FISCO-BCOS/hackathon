// SPDX-License-Identifier: SimPL-2.0
pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./DataFormat.sol";

contract FundingUser is DataFormat{
   event insertResult(int256 count);
   event updateResult(int256 count);
   event removeResult(int256 count);

   TableFactory tableFactory;

   string constant FUNDING_USER_TABLE_NAME = "fundingUser";

   constructor() public {
      // Set tableFactory address 
      tableFactory = TableFactory(0x1001);

      //资助用户(allFundingNumStr, 对应allFundingUser的金额)
      tableFactory.createTable(FUNDING_USER_TABLE_NAME, "userId", "allFundingUser, allFundingNumStr, fundingTime, score");
   }

    // 初始化用户资助表
    // 参数数据格式（userId）    返回值（插入数据条数）
   function initFundingInfo(string memory userId)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(FUNDING_USER_TABLE_NAME);
      Entry entry = table.newEntry();

      // Set Value
      entry.set("userId", userId);
      entry.set("allFundingUser", "");
      entry.set("allFundingNumStr", "");
      entry.set("fundingTime", "");
      entry.set("score", int256(0));
     
      int256 count = table.insert(userId, entry);
      emit insertResult(count);

      return count;
   }

   // 用户进行资助后，更新资助信息
   // 参数数据格式（userId，资助金额int，资助对象Id，资助金额string，资助时间）    返回值（更新数据条数）
   // 测试数据 1."2" "200" "伍羽放" "200" "2020-10-12"   2."2" "500" "赵双" "500" "2021-10-12"
   function updateFundingInfo(string memory userId, int256 fundsNumInt, string memory fundingId, string memory fundsNumStr, string memory fundingTime)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(FUNDING_USER_TABLE_NAME);
      
      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      // Get current fundsSum
      Entry entry = table.select(userId, condition).get(0);

      //资助者对象Id拼接
      string memory allFundingUser = entry.getString("allFundingUser");
      allFundingUser = strCatWithSymbol(allFundingUser, fundingId);

      // 每名资助者的金额
      // 资助金额拼接
      string memory allFundingNumStr = entry.getString("allFundingNumStr");
      allFundingNumStr = strCatWithSymbol(allFundingNumStr, fundsNumStr);

      //资助者时间拼接
      string memory allFundingTime = entry.getString("fundingTime");
      allFundingTime = strCatWithSymbol(allFundingTime, fundingTime);

      int256 score = entry.getInt("score");
      score = score + (fundsNumInt * 10);

      entry.set("allFundingUser", allFundingUser);
      entry.set("allFundingNumStr", allFundingNumStr);
      entry.set("fundingTime", allFundingTime);
      entry.set("score", score);

      int256 count = table.update(userId, entry, condition);
      emit updateResult(count);

      return count;
   }

   // 获得资助者当前资助数据
   // 参数数据格式（userId）   返回值（所有资助对象Id，所有资助对象金额，对应资助时间，总积分）
   function getUserFundingInfo(string memory userId)
   public
   view
   returns(string memory, string memory, string memory, int256)
   {
      Table table = tableFactory.openTable(FUNDING_USER_TABLE_NAME);

      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      Entries entries = table.select(userId, condition);
      if(0 == entries.size())
      {
         return("","","",-1);
      }
      
      Entry entry = entries.get(0);
      //allFundingUser, allFundingNumStr, fundingTime, score
      string memory allFundingUser = entry.getString("allFundingUser");
      string memory allFundingNumStr = entry.getString("allFundingNumStr");
      string memory fundingTime = entry.getString("fundingTime");
      int256 score = entry.getInt("score");
      
      return(allFundingUser, allFundingNumStr, fundingTime, score);
   }

}