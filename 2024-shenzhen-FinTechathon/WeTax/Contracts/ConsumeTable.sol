// SPDX-License-Identifier: Apache-2.0
pragma solidity >=0.6.10 <=0.8.26;
pragma experimental ABIEncoderV2;

import {Table, TableInfo, TableManager, Entry, UpdateField} from "lib/console/src/main/resources/contract/solidity/Table.sol";
import {APISampleOracle} from "lib/Truora-Service/contracts/1.0/sol-0.6/oracle/APISampleOracle.sol";
import {Strings} from "lib/openzeppelin-contracts/contracts/utils/Strings.sol";
import {Ownable} from "lib/openzeppelin-contracts/contracts/access/Ownable.sol";

contract ConsumeTable is Ownable {
    event TableCreated(int256 result, string tableName);
    event EntryInserted(int256 result, string indexed id, uint256 amount, string indexed consumer_id, string indexed platform_id);
    event EntryUpdated(int256 result, string indexed id, uint256 amount, string indexed consumer_id, string indexed platform_id);
    event EntryRemoved(int256 result, string indexed id);

    TableManager constant tm = TableManager(address(0x1002));
    Table table;
    string constant TABLE_NAME = "consume_list";

    constructor() Ownable(msg.sender) {
        // create table
        string[] memory columnNames = new string[](4);
        columnNames[0] = "amount";
        columnNames[1] = "consumer_id";
        columnNames[2] = "platform_id";
        columnNames[3] = "timestamp";
        TableInfo memory tf = TableInfo("id", columnNames);

        tm.createTable(TABLE_NAME, tf);
        address t_address = tm.openTable(TABLE_NAME);
        require(t_address != address(0x0), "");
        table = Table(t_address);
    }

    // 返回消费信息
    function select(string memory id) public view returns (string memory, string memory, string memory, string memory) {
        Entry memory entry = table.select(id);
        require(entry.fields.length != 0, "Error: Entry does not exist!");

        string memory amount;
        string memory consumer_id;
        string memory platform_id;
        string memory timestamp;

        if (entry.fields.length == 4) {
            amount = entry.fields[0];
            consumer_id = entry.fields[1];
            platform_id = entry.fields[2];
            timestamp = entry.fields[3];
        }

        return (amount, consumer_id, platform_id, timestamp);
    }

    // 插入新的消费记录
    function insert(
        string memory id,
        uint256 amount,
        string memory consumer_id,
        string memory platform_id
    ) public onlyOwner() returns (int32) {
        string[] memory columns = new string[](4);
        columns[0] = Strings.toString(amount);
        columns[1] = consumer_id;
        columns[2] = platform_id;
        columns[3] = Strings.toString(block.timestamp);
        Entry memory entry = Entry(id, columns);
        int32 result = table.insert(entry);
        emit EntryInserted(result, id, amount, consumer_id, platform_id);
        return result;
    }

    // 更新消费记录
    function update(
        string memory id,
        uint256 amount,
        string memory consumer_id,
        string memory platform_id
    ) public onlyOwner() returns (int32) {
        Entry memory entry = table.select(id);
        require(entry.fields.length != 0, "Error: Entry does not exist!");

        UpdateField[] memory updateFields = new UpdateField[](4);
        updateFields[0] = UpdateField("amount", Strings.toString(amount));
        updateFields[1] = UpdateField("consumer_id", consumer_id);
        updateFields[2] = UpdateField("platform_id", platform_id);
        updateFields[3] = UpdateField("timestamp", Strings.toString(block.timestamp));

        int32 result = table.update(id, updateFields);
        emit EntryUpdated(result, id, amount, consumer_id, platform_id);
        return result;
    }

    // 删除消费记录
    function remove(string memory id) public onlyOwner() returns (int32) {
        Entry memory entry = table.select(id);
        require(entry.fields.length != 0, "Error: Entry does not exist!");

        int32 result = table.remove(id);
        emit EntryRemoved(result, id);
        return result;
    }

    function createTable(string memory tableName, string memory key, string[] memory fields) public onlyOwner() returns (int256) {
        TableInfo memory tf = TableInfo(key, fields);
        int32 result = tm.createTable(tableName, tf);
        emit TableCreated(result, tableName);
        return result;
    }

    // 获取表信息
    function desc() public view returns (string memory, string[] memory) {
        TableInfo memory ti = tm.desc(TABLE_NAME);
        return (ti.keyColumn, ti.valueColumns);
    }
}
