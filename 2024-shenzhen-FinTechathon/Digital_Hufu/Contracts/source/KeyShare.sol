// SPDX-License-Identifier: Apache-2.0
pragma solidity >=0.6.10 <0.8.20;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract KeyShare {
    event CreateResult(int256 count);
    event InsertResult(int256 count);
    event UpdateResult(int256 count);
    event RemoveResult(int256 count);

    TableManager constant tm =  TableManager(address(0x1002));
    Table table;
    string constant TABLE_NAME = "t_key_share";
    constructor () public{
        // create table
        string[] memory columnNames = new string[](2);
        columnNames[0] = "name";
        columnNames[1] = "secret";
        TableInfo memory tf = TableInfo("id", columnNames);

        tm.createTable(TABLE_NAME, tf);
        address t_address = tm.openTable(TABLE_NAME);
        require(t_address!=address(0x0),"");
        table = Table(t_address);
    }

    function select(string memory id) public view returns (string memory,string memory)
    {
        Entry memory entry = table.select(id);

        string memory name;
        string memory secret;
        if(entry.fields.length==2){
            name = entry.fields[0];
            secret = entry.fields[1];
        }
        return (name,secret);
    }

    function insert(string memory id,string memory name,string memory secret) public returns (int32){
        string[] memory columns = new string[](2);
        columns[0] = name;
        columns[1] = secret;
        Entry memory entry = Entry(id, columns);
        int32 result = table.insert(entry);
        emit InsertResult(result);
        return result;
    }

    function update(string memory id, string memory name, string memory secret) public returns (int32){
        UpdateField[] memory updateFields = new UpdateField[](2);
        updateFields[0] = UpdateField("name", name);
        updateFields[1] = UpdateField("secret", secret);

        int32 result = table.update(id, updateFields);
        emit UpdateResult(result);
        return result;
    }

    function remove(string memory id) public returns(int32){
        int32 result = table.remove(id);
        emit RemoveResult(result);
        return result;
    }

    function createTable(string memory tableName,string memory key,string[] memory fields) public returns(int256){
        TableInfo memory tf = TableInfo(key, fields);
        int32 result = tm.createTable(tableName,tf);
        emit CreateResult(result);
        return result;
    }

    function desc() public view returns(string memory, string[] memory){
        TableInfo memory ti = tm.desc(TABLE_NAME);
        return (ti.keyColumn,ti.valueColumns);
    }
}