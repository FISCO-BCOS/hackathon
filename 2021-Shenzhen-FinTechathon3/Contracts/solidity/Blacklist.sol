// SPDX-License-Identifier: SimPL-2.0
pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./DataFormat.sol";

contract Blacklist is DataFormat{
   event insertResult(int256 count);
   event updateResult(int256 count);
   event removeResult(int256 count);


   TableFactory tableFactory;

   string constant BLACK_LIST_TABLE_NAME = "blacklist";

   string[] allUserId;

      constructor() public {
      // Set tableFactory address 
      tableFactory = TableFactory(0x1001);

      //
      tableFactory.createTable(BLACK_LIST_TABLE_NAME, "userId", "userState");
   }

    
    // 初始化用户账户状态表;    用户状态默认值 1 ： 正常账户
    // 参数数据格式（userId）    返回值（插入数据条数）
   function initUserState(string memory userId)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(BLACK_LIST_TABLE_NAME);
      Entry entry = table.newEntry();

      // Set Value
      entry.set("userId", userId);
      entry.set("userState", "1");
     
      int256 count = table.insert(userId, entry);
      emit insertResult(count);

      return count;
   }

   // 更新用户账户状态, 正常 -> 拉黑
   // 参数数据格式（userId）    返回值（更新数据条数）
   function changeUserState(string memory userId)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(BLACK_LIST_TABLE_NAME);
      Entry entry = table.newEntry();

      // 记录黑名单账户
      allUserId.push(userId);
      
      //已完成状态 0
      entry.set("userState", "0");

      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      int256 count = table.update(userId, entry, condition);
      emit updateResult(count);
      return count;
   }


   // 获得用户账户状态
   // 参数数据格式（userId）   返回值（"1" -> 正常；"0" -> 拉黑）
   function getUserState(string memory userId)
   public
   view
   returns(string memory)
   {
      Table table = tableFactory.openTable(BLACK_LIST_TABLE_NAME);
      string memory userState;

      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      Entries entries = table.select(userId, condition);
      if(0 == entries.size())
      {
         return("该ID不存在");
      }
      
      Entry entry = entries.get(0);

      userState= entry.getString("userState");
      
      return(userState);
   }

    // 获取所有黑名单账户
   function getAllBlacklistUser()
   public
   view
   returns(string[] memory, uint256)
   {
        uint256 blacklistNum = allUserId.length;
        return(allUserId, blacklistNum);
   }
}