pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "../lib/Table.sol";
import "../lib/LibString.sol";

contract TMC {
    using LibString for *;
    
    TableFactory tf;
    string _NAME;
    string _KEY_FIELD;
    string _VALUE_FIELDS_STR;
    string[] _VALUE_FIELDS;
    
    constructor(string name, string key_field, string value_fields_str) 
    public {
        tf = TableFactory(0x1001); //The fixed address is 0x1001 for TableFactory
        
        _NAME = name;
        _KEY_FIELD = key_field;
        _VALUE_FIELDS_STR = value_fields_str;
        LibString.split(value_fields_str, ",", _VALUE_FIELDS);
    }
    
    function create()
    public {
        tf.createTable(_NAME, _KEY_FIELD, _VALUE_FIELDS_STR);
    }
    
    function newCondition()
    public returns(Condition) {
        Table table = tf.openTable(_NAME);
        return table.newCondition();
    }
    
    function newEntry()
    public returns(Entry) {
        Table table = tf.openTable(_NAME);
        return table.newEntry();
    }
    
    function value_fields() 
    public view returns(string[]) {
        return _VALUE_FIELDS;
    }
    
    function index_of(string field_str)
    public view returns(uint) {
        for(uint i=0; i<_VALUE_FIELDS.length; i++) {
            if(LibString.equals(_VALUE_FIELDS[i], field_str)) return i;
        }
        return i;
    }
    
    // CRUD
    function insert(string key_field, Entry entry) 
    public returns (int256) {
        Table table = tf.openTable(_NAME);
        
        if(bytes(key_field).length == 0) {
            return table.insert(_KEY_FIELD, entry);
        }
        return table.insert(key_field, entry); // count
    }
    
    function select(string key_field, Condition condition)
    public view returns (Entries) {
        Table table = tf.openTable(_NAME);
        if(bytes(key_field).length == 0) {
            return table.select(_KEY_FIELD, condition);
        }
        return table.select(key_field, condition);
    }
    
    function update(string key_field, Entry entry, Condition condition)
    public returns (int256) {
        Table table = tf.openTable(_NAME);

        if(bytes(key_field).length == 0) {
            return table.update(_KEY_FIELD, entry, condition);
        }
        return table.update(key_field, entry, condition); // count
    }
    
    function remove(string key_field, Condition condition) 
    public returns (int256) {
        Table table = tf.openTable(_NAME);

        if(bytes(key_field).length == 0) {
            return table.remove(_KEY_FIELD, condition);
        }
        return table.remove(key_field, condition); //count
    }
    
    // REQUIREMENTS
    function require_not_empty(string key_field, string[] record, string field_str)
    public {
        require_not_empty(key_field, record, field_str, field_str);
    }
    function require_not_empty(string key_field, string[] record, string start_str, string end_str)
    public {
        uint start_i = index_of(start_str);
        uint end_i = index_of(end_str);
        for(uint i = start_i; i<=end_i; i++) {
            require(bytes(record[i]).length>0, LibString.concat("字段不能为空：", _VALUE_FIELDS[i]));
        }
    }
    function require_exist_or_empty(string key_field, string[] record, string foreign_key_str, string key_str)
    public {
        uint key_i = index_of(key_str);
        uint foreign_key_i = index_of(foreign_key_str);
        if(bytes(record[foreign_key_i]).length==0) return;
        
        Table table = tf.openTable(_NAME);
        Condition condition = table.newCondition();
        condition.EQ(_VALUE_FIELDS[key_i], record[foreign_key_i]);
        
        Entries entries = select(key_field, condition);
        require(entries.size()>0, LibString.concat("字段对应数据不存在：", _VALUE_FIELDS[foreign_key_i]));
    }
    
    function require_not_exist(string key_field, string[] record, string field_str)
    public {
        uint i = index_of(field_str);
        Table table = tf.openTable(_NAME);
        Condition condition = table.newCondition();
        condition.EQ(_VALUE_FIELDS[i], record[i]);
        
        Entries entries = select(key_field, condition);
        require(entries.size()==0, LibString.concat("字段已存在数据：", _VALUE_FIELDS[i]));
    }
    
    // SELECT CONDITIONS
    function condition_eq(Condition condition, string[] filter, uint start_i, uint end_i)
    public returns (Condition) {
        for(uint i=start_i; i<=end_i; i++) {
            if(bytes(filter[i]).length == 0) continue;
            condition.EQ(_VALUE_FIELDS[i], filter[i]);
        }
        return condition;
    }
    function condition_during(Condition condition, string[] filter, uint start_i, uint end_i)
    public returns (Condition) {
        if(bytes(filter[start_i]).length>0) {
            condition.GE("timestamp", LibString.toInt(filter[start_i]));
        }
        if(bytes(filter[end_i]).length>0) {
            condition.LE("timestamp", LibString.toInt(filter[end_i]));
        }
        return condition;
    }
    function condition_pagination(int count, Condition condition, string[] filter, uint start_i, uint end_i)
    public returns (Condition) {
        // 若其中某参数为空直接返回
        if(bytes(filter[start_i]).length==0 || bytes(filter[start_i]).length==0) {
            return condition;
        }
        int page = LibString.toInt(filter[start_i]);
        int num = LibString.toInt(filter[end_i]);
        int startAt = count-page*num >= 0? count-page*num: 0;
        condition.limit(startAt, num);
        return condition;
    }
}
