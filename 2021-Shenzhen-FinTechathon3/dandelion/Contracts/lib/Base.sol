pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./TMC.sol";
import "../lib/LibAddress.sol";

contract Base {
    string constant private _SPLIT_MARK = "^,^";
    // address _ADMIN_ADDRESS;
    
    TMC _table;
    
    constructor(string table_name, string key_field, string value_fields)
    public {
        // _ADMIN_ADDRESS = msg.sender;
        _table = new TMC(table_name, key_field, value_fields);
        _table.create();
    }
    //////////// PUBLIC FUNCTIONS ////////////
    // CRUD + Count
    function insert(string key_field, string[] memory record)
    public returns (int256)
    {
        string memory tip = "insert: 数据格式不正确";
        Entry entry = _table.newEntry();
        if(timestamp_included()) {
            require(record.length == fields_length() - 1, tip);
            entry.set("timestamp", int(block.timestamp));
        }
        else {
            require(record.length == fields_length(), tip);
        }
        
        for(uint256 i=0; i<record.length; i++){
            entry.set(_table.value_fields()[i], record[i]);
        }
        
        return _table.insert(key_field, entry);
    }
    
    function remove(string key_field, string[] memory filter)
    public returns (int256) {
        Condition condition;
        (condition,) = form_select_condition(key_field, filter);
        return _table.remove(key_field, condition);
    }
    
    function update(string key_field, string[] memory record, string[] memory filter)
    public returns (int256) {
        bool recordLengthMatch = false;
        if(timestamp_included()) {
            recordLengthMatch = record.length == fields_length() - 1;
        }
        else {
            recordLengthMatch = record.length == fields_length();
        }
        require(recordLengthMatch, "update: 新数据参数列表长度有误");
        Condition condition;
        (condition,) = form_select_condition(key_field, filter);
        
        Entry entry = _table.newEntry();
        for(uint i=0; i<record.length; i++) {
            if(bytes(record[i]).length==0) continue;
            entry.set(_table.value_fields()[i], record[i]);
        }
        return _table.update(key_field, entry, condition);
    }
    
    function select(string key_field, string[] memory filter) 
    public view returns (string[] memory result, int amount) {
        Condition condition;
        (condition, amount) = form_select_condition(key_field, filter);
        result = assemble(_table.select(key_field, condition));
    }
    function select_entries(string key_field, string[] memory filter) 
    public view returns (Entries result, int amount) {
        Condition condition;
        (condition, amount) = form_select_condition(key_field, filter);
        result = _table.select(key_field, condition);
    }
    
    /*
    function set_admin_address(string[] memory record, string field)
    public returns(string[] memory) {
        uint index = _table.index_of(field);
        if(index < fields_length()) {
            record[index] = LibAddress.toString(_ADMIN_ADDRESS);
        }
        return record;
    }
    
    function set_sender_address(string[] memory record, string field)
    public returns(string[] memory) {
        uint index = _table.index_of(field);
        if(index < fields_length()) {
            record[index] = LibAddress.toString(msg.sender);
        }
        return record;
    }*/
    
    // subs
    function filter_form()
    public view returns (string[] form) {
        uint offset = timestamp_included()? 3: 2;
        form = new string[](fields_length() + offset);
    }
    
    function fields_length()
    public view returns (uint) {
        return _table.value_fields().length;
    }
    
    //////////// PRIVATE FUNCTIONS //////////// 
    
    function timestamp_included()
    private view returns (bool){
        return LibString.equals(_table.value_fields()[_table.value_fields().length-1], "timestamp");
    }
    
    // function count(string key_field, Condition condition)
    // public view returns (int) {
    //     return _table.select(key_field, condition).size();
    // }
    
    // 生成匹配condition条件
    function form_select_condition(string key_field, string[] memory filter)
    private view returns (Condition condition, int amount) {
        condition = form_select_condition_count(filter);
        // amount = count(key_field, condition);
        amount = _table.select(key_field, condition).size();
        condition = form_select_condition_pagination(condition, amount, filter);
    }
    // 无分页条件，进行计数
    function form_select_condition_count(string[] memory filter)
    private view returns (Condition) {
        uint fieldsLength = fields_length();
        string memory tip = "form_select_condition: 查询条件格式不正确，参数列表长度有误";
        Condition condition = _table.newCondition();
        // 若最后一个字段为timestamp
        if(timestamp_included()) {
            // ..., timestamp => ..., start_timestmp, end_timestamp, page, num
            require(filter.length == fieldsLength + 3, tip);
            condition = _table.condition_during(condition, filter, fieldsLength-1, fieldsLength); // 时间筛选
        }
        else {
            // ... => ..., page, num
            require(filter.length == fieldsLength + 2, tip);
        }
        condition = _table.condition_eq(condition, filter, 0, fieldsLength-2); // 等值查询
        return condition;
    }
    // 加上分页条件
    function form_select_condition_pagination(Condition condition, int amount, string[] memory filter)
    private view returns (Condition) {
        condition = _table.condition_pagination(amount, condition, filter, filter.length-2, filter.length-1);// 分页
        return condition;
    }
    
    // copy from qbMain
    //将多条记录以一维数组的形式返回
    function assemble(Entries entries)
    private view returns (string[] memory) {
        string[] memory value_fields = _table.value_fields();
        string[] memory info_list = new string[](uint256(entries.size()));
        string memory info;
        // 倒序
        for (int256 j = 0; j < entries.size(); j++) {
            // Entry entry = entries.get(j);
            Entry entry = entries.get(entries.size()-1-j);
            info = "";
            for (uint i = 0; i < value_fields.length; i++){
                info = LibString.concat(info, entry.getString(value_fields[i]));
                if(i<value_fields.length-1){
                    info = LibString.concat(info, _SPLIT_MARK);
                }
            }
            info_list[uint256(j)] = info;
        }
        return info_list;
    }
    
}