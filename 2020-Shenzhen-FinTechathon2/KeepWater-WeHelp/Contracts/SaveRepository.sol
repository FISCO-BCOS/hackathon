pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./DataFormat.sol";


contract SaveRepository is DataFormat{
    string constant TABLE_NAME = "wehelp_t";
    TableFactory tf;
    string private id_str;
    
    function create() public returns(int){
        tf = TableFactory(0x1001);  
        int count = tf.createTable(TABLE_NAME, "id", "time,location,other");
        return count;
    }
    
    // 功能：更新Evidence中的 location/other
    function addEvidence(address id, string time, string location, string other) public returns(int){
        Table table = tf.openTable(TABLE_NAME);
        id_str = toString(id);
        
        Entry entry = table.newEntry();
        entry.set("id", id_str);
        entry.set("time", time);
        entry.set("location", location);
        entry.set("other", other);

        int count = table.insert(id_str, entry);
        return count;
    }

    // 功能：根据address id 和 毫秒时间戳time 进行删除
    function removeByIdAndTime(address id, string time) public returns(int){
        Table table = tf.openTable("TABLE_NAME");

        id_str = toString(id);
        
        Condition condition = table.newCondition();
        condition.EQ("id", id_str);
        condition.EQ("time", time);

        int count = table.remove(id_str, condition);

        return count;
    }
    
    // 功能：根据address id和毫秒时间戳time进行 更改
    function updateEvidence(address id , string time, string location, string other) public returns(int){
        Table table = tf.openTable(TABLE_NAME);
        id_str = toString(id);
        
        Entry entry = table.newEntry();
        entry.set("location", location);
        entry.set("other", other);
        
        Condition condition = table.newCondition();
        condition.EQ("id", id_str);
        condition.EQ("time",time);

        int count = table.update(id_str, entry, condition);
        return count;
    }
    
    // 功能：根据address id和毫秒时间戳time进行查询
    function selectByIdAndTime(address id, string time) public view returns(int ok, string location, string other){
        Table table = tf.openTable(TABLE_NAME);
        
        id_str = toString(id);
        
        Condition condition = table.newCondition();
        condition.EQ("id", id_str);
        condition.EQ("time", time);
        

        Entries entries = table.select(id_str, condition);
        if(entries.size()==1){
            Entry entry = entries.get(0);
            location = entry.getString("location");
            other = entry.getString("other");
            return (1, location, other);
        }else{
            return (0,"","");
        }
    }

}