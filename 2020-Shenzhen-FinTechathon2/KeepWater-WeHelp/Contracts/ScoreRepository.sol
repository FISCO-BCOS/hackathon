pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./DataFormat.sol";


contract ScoreRepository is DataFormat{
    string constant TABLE_NAME = "wehelp_score";
    TableFactory tf;
    string private id_str;
    
    function create() public returns(int){
        tf = TableFactory(0x1001);  
        int count = tf.createTable(TABLE_NAME, "id", "weid, balance, used_balance");
        return count;
    }
    
    // 功能：ScoreRepository合约 增加新用户
    function addUser(address id, string weid, int balance, int used_balance) public returns(int){
        Table table = tf.openTable(TABLE_NAME);
        id_str = toString(id);
        
        Entry entry = table.newEntry();
        entry.set("id", id_str);
        entry.set("weid", weid);
        entry.set("balance", balance);
        entry.set("used_balance", used_balance);

        int count = table.insert(id_str, entry);
        return count;
    }
    
    // 功能：ScoreRepository合约 根据善行凭证中的积分，为用户增加积分余额
    function addBlanceByWeid(address id , string weid, int increase ) public returns(int){
        Table table = tf.openTable(TABLE_NAME);
        id_str = toString(id);
        int balance;
        int new_balance;
        (,balance) = selectBalanceById(id);
        new_balance = balance + increase;
        
        Entry entry = table.newEntry();
        entry.set("balance", new_balance);
        
        Condition condition = table.newCondition();
        condition.EQ("id", id_str);
        condition.EQ("weid", weid);
        
        int count = table.update(id_str, entry, condition);
        return count;
    }
    
    // 功能：ScoreRepository合约 根据用户的兑换请求，经过检验方检验后，更新用户的余额
    function reduceBlanceByWeid(address id , string weid, int reduce ) public returns(int){
        Table table = tf.openTable(TABLE_NAME);
        id_str = toString(id);
        int balance;
        int used_balance;
        int new_balance;
        int new_used_balance;
        (,balance) = selectBalanceById(id);
        (,used_balance) = selectUsedBalanceById(id);
        
        if(balance>=reduce){
            new_balance = balance - reduce;
            new_used_balance = used_balance + reduce;
        
            Entry entry = table.newEntry();
            entry.set("balance", new_balance);
            entry.set("used_balance", new_used_balance);
             
            Condition condition = table.newCondition();
            condition.EQ("id", id_str);
            condition.EQ("weid", weid);
            
            int count = table.update(id_str, entry, condition);
            return count;
        }else{
            return (0);
        }
    }
    
    // 功能：ScoreRepository合约 根据请求，查看用户账户积分余额
    function selectBalanceById(address id ) public view returns(int ok, int balance){
        Table table = tf.openTable(TABLE_NAME);
        id_str = toString(id);
        
        Condition condition = table.newCondition();
        condition.EQ("id", id_str);

        Entries entries = table.select(id_str, condition);
        if(entries.size()==1){
            Entry entry = entries.get(0);
            balance = entry.getInt("balance");
            return (1, balance);
        }else{
            return (0, 0);
        }
    }
    // 功能：ScoreRepository合约 根据请求，查看用户账户积分已使用额
    function selectUsedBalanceById(address id ) public view returns(int ok, int used_balance){
        Table table = tf.openTable(TABLE_NAME);
        id_str = toString(id);
        
        Condition condition = table.newCondition();
        condition.EQ("id", id_str);

        Entries entries = table.select(id_str, condition);
        if(entries.size()==1){
            Entry entry = entries.get(0);
            used_balance = entry.getInt("used_balance");
            return (1, used_balance);
        }else{
            return (0, 0);
        }
    }
    
}