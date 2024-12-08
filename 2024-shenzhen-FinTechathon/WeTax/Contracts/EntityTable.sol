// SPDX-License-Identifier: Apache-2.0
pragma solidity >=0.6.10 <=0.8.26;
pragma experimental ABIEncoderV2;

import {Table, TableManager, TableInfo, Entry, UpdateField} from "lib/console/src/main/resources/contract/solidity/Table.sol";
import {Strings} from "lib/openzeppelin-contracts/contracts/utils/Strings.sol";
import {Ownable} from "lib/openzeppelin-contracts/contracts/access/Ownable.sol";

contract EntityTable is Ownable{
    // 定义entity_type的枚举类型
    enum EntityType { Individual, Company, Platform }
    
    event TableCreated(int256 result, string tableName);
    event EntryInserted(int256 result, string indexed id, string name, EntityType indexed entity_type, string indexed entity_address);
    event EntryUpdated(int256 result, string indexed id, string name, EntityType indexed entity_type, string indexed entity_address);
    event EntryRemoved(int256 result, string indexed id);

    TableManager constant tm =  TableManager(address(0x1002));
    Table table;
    string constant TABLE_NAME = "entity_list";
    
    constructor() Ownable(msg.sender){
        // create table
        string[] memory columnNames = new string[](4);
        columnNames[0] = "name";
        columnNames[1] = "entity_type";
        columnNames[2] = "entity_address";
        columnNames[3] = "timestamp";
        TableInfo memory tf = TableInfo("id", columnNames);

        tm.createTable(TABLE_NAME, tf);
        address t_address = tm.openTable(TABLE_NAME);
        require(t_address!=address(0x0),"");
        table = Table(t_address);
    }

    // 返回实体信息
    function select(string memory id) public view returns (string memory, string memory, string memory, string memory) {
        Entry memory entry = table.select(id);
        require(entry.fields.length != 0, "Error: Entity does not exist!");

        string memory name;
        string memory entity_type;
        string memory entity_address;
        string memory timestamp;

        if(entry.fields.length == 4){
            name = entry.fields[0];
            entity_type = entry.fields[1];
            entity_address = entry.fields[2];
            timestamp = entry.fields[3];
        }

        return (name, entity_type, entity_address, timestamp);
    }

    // 插入新的实体
    function insert(
            string memory id,
            string memory name,
            EntityType entity_type,
            string memory entity_address
        ) public onlyOwner() returns (int32){
        string[] memory columns = new string[](4);
        columns[0] = name;
        columns[1] = Strings.toString(uint(entity_type));
        columns[2] = entity_address;
        columns[3] = Strings.toString(block.timestamp);
        Entry memory entry = Entry(id, columns);
        int32 result = table.insert(entry);
        emit EntryInserted(result, id, name, entity_type, entity_address);
        return result;
    }

    // 更新实体信息
    function update(
            string memory id,
            string memory name,
            EntityType entity_type,
            string memory entity_address
        ) public onlyOwner() returns (int32){
        Entry memory entry = table.select(id);
        require(entry.fields.length != 0, "Error: Entity does not exist!");

        UpdateField[] memory updateFields = new UpdateField[](4);
        updateFields[0] = UpdateField("name", name);
        updateFields[1] = UpdateField("entity_type", Strings.toString(uint(entity_type)));
        updateFields[2] = UpdateField("entity_address", entity_address);
        updateFields[3] = UpdateField("timestamp", Strings.toString(block.timestamp));        

        int32 result = table.update(id, updateFields);
        emit EntryUpdated(result, id, name, entity_type, entity_address);
        return result;
    }

    // 删除实体信息
    function remove(string memory id) public onlyOwner() returns(int32){
        Entry memory entry = table.select(id);
        require(entry.fields.length != 0, "Error: Entity does not exist!");

        int32 result = table.remove(id);
        emit EntryRemoved(result, id);
        return result;
    }

    function createTable(string memory tableName,string memory key,string[] memory fields) public onlyOwner() returns(int256){
        TableInfo memory tf = TableInfo(key, fields);
        int32 result = tm.createTable(tableName,tf);
        emit TableCreated(result, tableName);
        return result;
    }

    // 获取表信息
    function desc() public view returns(string memory, string[] memory){
        TableInfo memory ti = tm.desc(TABLE_NAME);
        return (ti.keyColumn,ti.valueColumns);
    }
}