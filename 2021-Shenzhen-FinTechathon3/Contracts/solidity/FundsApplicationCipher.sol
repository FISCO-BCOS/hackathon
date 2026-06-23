// SPDX-License-Identifier: SimPL-2.0
pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./DataFormat.sol";

contract FundsApplicationCipher is DataFormat{
   event insertResult(int256 count);
   event updateResult(int256 count);
   event removeResult(int256 count);

   TableFactory tableFactory;

   string constant APP_TABLE_NAME = "fundsApplication";
   string constant FUNDS_TABLE_NAME = "fundsCompute";
   string constant WITHDRAWAL_TABLE_NAME = "fundsWithdrawal";
   string constant DETAIL_TABLE_NAME = "fundsDetail";
 
   string[] private allUserId;
   //记录当前用户申请数
   uint256 private flag = 0;
   //记录allUserId申请内存数
   uint256 private insertNum = 0;
  
   constructor() public{
      // Set tableFactory address 
      tableFactory = TableFactory(0x1001);
      //资助申请表,fundsState默认为1
      tableFactory.createTable(APP_TABLE_NAME, "userId", "certificateData,certificateTemplate,fundsState");
      //资助统计表
      tableFactory.createTable(FUNDS_TABLE_NAME, "userId", "fundsSum, fundingId, fundingNum, fundingTime");
      //资助提现表
      tableFactory.createTable(WITHDRAWAL_TABLE_NAME, "userId", "withdrawalAmount, balance, withdrawalSum");
      //提现明细表
      tableFactory.createTable(DETAIL_TABLE_NAME, "userId", "costSum, detail, costStr");
   }

   // 资助申请块插入记录
   // 参数数据格式（userId, 验证密文，验证临时数据）    返回值（插入记录条数）
   //"1" "asdfasdfdasfdas" "asdfasdhfasi"
   //"2" "zxcvuioios" zxcvupoiui"
   //"3" "qwerqwer" qweioruo"
   function insertFundsInfo(string memory userId, string memory certificateData, string memory certificateTemplate)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(APP_TABLE_NAME);
      
      Entry entry = table.newEntry();
      int256 count;
      // Set Value
      entry.set("userId", userId);
      entry.set("certificateData", certificateData);
      entry.set("certificateTemplate", certificateTemplate);
      //默认值 1 : 筹款未结束
      entry.set("fundsState", "1");


      for(uint i = 0; i < insertNum; i++)
      {
         if(strMatching(allUserId[i], "") == true)
         {
            allUserId[i] = userId;
            flag = flag + 1;
            count = table.insert(userId, entry);
            emit insertResult(count);
            return count;
         }
      }
      allUserId.push(userId);
      insertNum = insertNum + 1;
      flag = flag + 1;

      count = table.insert(userId, entry);
      emit insertResult(count);

      return count;
   }

   // 获得求助者的求助信息
   // 参数数据格式（userId）   返回值（用户信息）
   function getUserApplicationInfo(string memory userId)
   public
   view
   returns(string memory, string memory, string memory, string memory)
   {
      Table table = tableFactory.openTable(APP_TABLE_NAME);
      string memory certificateData;
      string memory certificateTemplate;
      string memory fundsState;

      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      Entries entries = table.select(userId, condition);
      if(0 == entries.size())
      {
         return("该ID不存在", "", "", "");
      }
      
      Entry entry = entries.get(0);

      certificateData= entry.getString("certificateData");
      certificateTemplate= entry.getString("certificateTemplate");
      fundsState = entry.getString("fundsState");
      
      return(userId, certificateData, certificateTemplate, fundsState);
   }

   // 更新用户个人信息
   // 参数数据格式（userId，验证密文，验证临时数据）    返回值（更新数据条数）
   function updateApplication(string memory userId, string memory certificateData, string memory certificateTemplate)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(APP_TABLE_NAME);
      Entry entry = table.newEntry();
      
      entry.set("certificateData", certificateData);
      entry.set("certificateTemplate", certificateTemplate);

      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      int256 count = table.update(userId, entry, condition);
      emit updateResult(count);

      return count;
   }

   // 更新用户筹款状态, 未完成 -> 已完成
   // 参数数据格式（userId）    返回值（更新数据条数）
   function changeFundsState(string memory userId)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(APP_TABLE_NAME);
      Entry entry = table.newEntry();
      
      //已完成状态 0
      entry.set("fundsState", "0");

      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      int256 count = table.update(userId, entry, condition);
      emit updateResult(count);
      return count;
   }


   /**************************************资助统计块**************************************/  
   // 初始化申请者统计表
   // 参数数据格式（userId）     返回值（插入数据条数）
   function initFundsCompute(string memory userId)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(FUNDS_TABLE_NAME);

      Entry entry = table.newEntry();
      // Set Value
      entry.set("userId", userId);
      entry.set("fundsSum", int256(0));
      entry.set("fundingId", "");
      entry.set("fundingNum", "");
      entry.set("fundingTime", "");
     
      int256 count = table.insert(userId, entry);
      emit insertResult(count);

      return count;
   }
  

   // 用户进行资助后，更新筹款金额
   // 参数数据格式（userId，资助金额int，资助者Id，资助金额string，资助时间）    返回值（更新数据条数）
   // 测试数据 1."1" "200" "伍羽放" "200" "2020-10-12"   2."1" "500" "赵双" "500" "2021-10-12"
   function updateFundsCompute(string memory userId, int256 fundsSum, string memory fundingId, string memory fundsNum, string memory fundingTime)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(FUNDS_TABLE_NAME);
      
      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      // Get current fundsSum
      Entry entry = table.select(userId, condition).get(0);

      // 每名资助者的金额
      string memory fundingNum = entry.getString("fundingNum");
      fundingNum = strCatWithSymbol(fundingNum, fundsNum);
     
      entry.set("fundingNum", fundingNum);
       
      fundsSum += entry.getInt("fundsSum");

      //资助者ID拼接
      string memory allFundingId = entry.getString("fundingId");
      allFundingId = strCatWithSymbol(allFundingId, fundingId);

      //资助者时间拼接
      string memory allFundingTime = entry.getString("fundingTime");
      allFundingTime = strCatWithSymbol(allFundingTime, fundingTime);

      entry.set("fundsSum", fundsSum);
      entry.set("fundingId", allFundingId);
      entry.set("fundingTime", allFundingTime);

      int256 count = table.update(userId, entry, condition);
      emit updateResult(count);

      return count;
   }

   // 获得求助者当前筹款数额
   // 参数数据格式（userId）   返回值（筹款金额，资助者ID，对应资助金额，资助时间）
   function getUserFundsInfo(string memory userId)
   public
   view
   returns(int256, string memory, string memory, string memory)
   {
      Table table = tableFactory.openTable(FUNDS_TABLE_NAME);

      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      Entries entries = table.select(userId, condition);
      if(0 == entries.size())
      {
         return(-1, "该用户不存在", "", "");
      }
      
      Entry entry = entries.get(0);

      int256 fundsSum = entry.getInt("fundsSum");
      string memory allFundingId = entry.getString("fundingId");
      string memory fundsNum = entry.getString("fundingNum");
      string memory fundingTime = entry.getString("fundingTime");
      
      return(fundsSum, allFundingId, fundsNum, fundingTime);
   }


/**************************************资助提现块**************************************/  
   // 初始化申请者提现表
   // 参数数据格式（userId）     返回值（插入数据条数）
   function initFundsWithdrawal(string memory userId)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(WITHDRAWAL_TABLE_NAME);

      Entry entry = table.newEntry();
      // Set Value
      entry.set("userId", userId);
      entry.set("withdrawalAmount", int256(0));
      entry.set("balance", int256(0));
      entry.set("withdrawalSum", int256(0));
     
      int256 count = table.insert(userId, entry);
      emit insertResult(count);

      return count;
   }

   // 用户进行提现后，更新提现表
   // 参数数据格式（userId，提现金额）    返回值（更新数据条数）
   // 测试数据 1."1" "50"     2."1" "20"
   function updateFundsWithdrawal(string memory userId, int256 withdrawalAmount)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(WITHDRAWAL_TABLE_NAME);
      
      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      // Get current fundsSum
      Entry entry = table.select(userId, condition).get(0);

      entry.set("withdrawalAmount", withdrawalAmount);
      int256 withdrawalSum = entry.getInt("withdrawalSum") + withdrawalAmount;
      
      int256 fundsSum;
      (fundsSum,,,) = getUserFundsInfo(userId);
      
      int256 balance = fundsSum - withdrawalSum;

      entry.set("balance", balance);
      entry.set("withdrawalSum", withdrawalSum);

      int256 count = table.update(userId, entry, condition);
      emit updateResult(count);

      return count;
   }

   // 获得求助者当前提现数据
   // 参数数据格式（userId）   返回值（上次提现金额，剩余金额，提现金额总数）
   function getUserFundsWithdrawal(string memory userId)
   public
   view
   returns(int256, int256, int256)
   {
      Table table = tableFactory.openTable(WITHDRAWAL_TABLE_NAME);

      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      Entries entries = table.select(userId, condition);
      if(0 == entries.size())
      {
         return(-1, -1, -1);
      }
      
      Entry entry = entries.get(0);
      if(0 == entry.getInt("withdrawalAmount"))
      {
         int256 initBalance;
         (initBalance,,,) = getUserFundsInfo(userId);
         return(0, initBalance, 0);
      }


      int256 withdrawalAmount = entry.getInt("withdrawalAmount");
      int256 balance = entry.getInt("balance");
      int256 withdrawalSum = entry.getInt("withdrawalSum");
      
      return(withdrawalAmount, balance, withdrawalSum);
   }

/**************************************提现明细块**************************************/ 
   // 初始化申请者明细表
   // 参数数据格式（userId）     返回值（插入数据条数）
   function initCostDetail(string memory userId)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(DETAIL_TABLE_NAME);

      Entry entry = table.newEntry();
      // Set Value
      entry.set("userId", userId);
      entry.set("costSum", int256(0));
      entry.set("detail", "");
      entry.set("costStr", "");
     
      int256 count = table.insert(userId, entry);
      emit insertResult(count);

      return count;
   }

   // 用户提交明细后，更新明细表
   // 参数数据格式（userId，使用金额Int，使用明细, 使用金额string）    返回值（更新数据条数）
   // 测试数据 1."1" "20" "花钱吃麦当劳" "20"     2."1" "40" "花钱吃第二顿麦当劳" "40"
   function updateCostDetail(string memory userId, int256 cost, string memory detail, string memory costStr)
   public
   returns(int256)
   {
      Table table = tableFactory.openTable(DETAIL_TABLE_NAME);
      
      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      Entry entry = table.select(userId, condition).get(0);

      cost += entry.getInt("costSum");
      
      string memory allDetail = entry.getString("detail");
      allDetail = strCatWithSymbol(allDetail, detail);

      string memory allCostStr = entry.getString("costStr");
      allCostStr = strCatWithSymbol(allCostStr, costStr);
      
      entry.set("costSum", cost);
      entry.set("detail", allDetail);
      entry.set("costStr", allCostStr);

      int256 count = table.update(userId, entry, condition);
      emit updateResult(count);

      return count;
   }

   // 获得用户当前明细
   // 参数数据格式（userId）   返回值（使用总金额，使用明细，对应使用金额）
   function getUserCostDetail(string memory userId)
   public
   view
   returns(int256, string memory, string memory)
   {
      Table table = tableFactory.openTable(DETAIL_TABLE_NAME);

      Condition condition = table.newCondition();
      condition.EQ("userId", userId);

      Entries entries = table.select(userId, condition);
      if(0 == entries.size())
      {
         return(-1, "该ID不存在", "该ID不存在");
      }
      
      Entry entry = entries.get(0);

      int256 costSum = entry.getInt("costSum");
      string memory detail = entry.getString("detail");
      string memory costStr = entry.getString("costStr");

      return(costSum, detail, costStr);
   }

/**************************************其余接口**************************************/ 
   // 删除用户求助信息
   // 参数数据格式（userId）  返回值（删除数据条数）
   function removeAll(string memory userId) 
   public 
   returns (int256)
   {
        Table table1 = tableFactory.openTable(APP_TABLE_NAME);
        Condition condition = table1.newCondition();
        condition.EQ("userId", userId);

        int256 count = table1.remove(userId, condition);

        Table table2 = tableFactory.openTable(FUNDS_TABLE_NAME);
        count += table2.remove(userId, condition);

        Table table3 = tableFactory.openTable(WITHDRAWAL_TABLE_NAME);
        count += table3.remove(userId, condition);

        Table table4 = tableFactory.openTable(DETAIL_TABLE_NAME);
        count += table4.remove(userId, condition);

        for(uint256 i = 0; i < flag; i++)
        {
           if(strMatching(allUserId[i], userId) == true)
           {
              for(uint256 j = i; j < flag - 1 ; j++)
              {
                 allUserId[j] = allUserId[j + 1];
              }
              
              allUserId[flag - 1] = "";
              flag = flag - 1;
              //removeNum = removeNum + 1;

              break;
           }
          
        }
       

        emit removeResult(count);

        return count;
    }
   
   // 获取全部求助信息
   // 返回值（string[]） (userId, certificateData, certificateTemplate, fundsState)：用`分隔
   function getAllUserApplication() 
   public 
   view
   returns(string[] memory)
   {
      string[] memory allUserInfo = new string[](flag);
      Table table = tableFactory.openTable(APP_TABLE_NAME);
      Condition condition = table.newCondition();
      
      for(uint256 i = 0; i < flag; i++)
      {
         condition.EQ("userId", allUserId[i]);
         Entries entries = table.select(allUserId[i], condition);
         if(0 == entries.size())
         {
            continue;   
         }
         
         Entry entry = entries.get(0);

         string memory userId = entry.getString("userId");
         string memory certificateData = entry.getString("certificateData");
         string memory certificateTemplate = entry.getString("certificateTemplate");
         string memory fundsState = entry.getString("fundsState");
         allUserInfo[i] = strCat(strCat(strCat(strCat(strCat(strCat(userId, "`"), certificateData), "`"),certificateTemplate), "`"), fundsState);
      }
      return(allUserInfo);
   }

   // 查看当前用户数申请数 和 全部用户Id
   function get() public view returns(uint, string[] memory)
   {
      return(flag, allUserId);
   }

}