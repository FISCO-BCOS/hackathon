pragma solidity ^0.4.20;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract ShopAccounts {

    string shopID;
    int balance;
    int leverage;

    string public tableName;
    TableFactory tf = TableFactory(0x1001);

    event CreateTable(int count);
    event ChangeBalance(int count);

    constructor() public {
        tableName = "shopAccounts";
        int count = tf.createTable(
            tableName,
            "shopID",
            "balance,leverage"
        );
        emit CreateTable(count);
    }

    function createAccount(string _shopID) public returns(int){
        Table table = tf.openTable(tableName);
        
        int zero = 0;
        Entry entry = table.newEntry();
        entry.set("shopID", _shopID);
        entry.set("balance", zero);
        entry.set("leverage", zero);

        int count = table.insert(_shopID, entry);
        return(count);
    }

    function selectByShopID(string _shopID) public returns(int, int) {
        Table table = tf.openTable(tableName);

        Condition condition = table.newCondition();
        condition.EQ("shopID", _shopID);

        Entries entries = table.select(_shopID, condition);
        Entry entry = entries.get(0);

        balance = entry.getInt("balance");
        leverage = entry.getInt("leverage");

        return (balance, leverage);
    }

    function changeBalance(string _shopID, int _value) public returns (int) {
        Table table = tf.openTable(tableName);

        Condition condition = table.newCondition();
        condition.EQ("shopID", _shopID);

        Entries entries = table.select(_shopID, condition);
        Entry entry = entries.get(0);
        balance = entry.getInt("balance");

        entry = table.newEntry();
        entry.set("balance", balance + _value);
        int count = table.update(_shopID, entry, condition);

        emit ChangeBalance(count);
        return count;
    }

    function changeLeverage(string _shopID, int _value) public returns (int) {
        Table table = tf.openTable(tableName);

        Entry entry = table.newEntry();
        entry.set("leverage", _value);

        Condition condition = table.newCondition();
        condition.EQ("shopID", _shopID);

        int count = table.update(_shopID, entry, condition);

        emit ChangeBalance(count);
        return count;
    }
}