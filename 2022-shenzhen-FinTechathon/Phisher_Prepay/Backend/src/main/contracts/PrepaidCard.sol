pragma solidity ^0.4.20;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract PrepaidCard {
    
    string cardID;
    string consumerID;
    string shopID;
    int balance;
    int contractHash;
    uint createTime;
    
    string public tableName;
    TableFactory tf = TableFactory(0x1001);

    event CreateTable(int count);
    event ChangeBalance(int count);

    constructor() public {
        tableName = "prepaidCard";
        int count = tf.createTable(
            tableName,
            "cardID",
            "consumerID,shopID,balance,contractHash,createTime"
        );
        emit CreateTable(count);
    }

    function createCard(
        string _cardID,
        string _consumerID,
        string _shopID,
        int _balance,
        int _contractHash,
        uint _createTime
    ) public returns(int){
        Table table = tf.openTable(tableName);

        Entry entry = table.newEntry();
        entry.set("cardID", _cardID);
        entry.set("consumerID", _consumerID);
        entry.set("shopID", _shopID);
        entry.set("balance", _balance);
        entry.set("contractHash", _contractHash);
        entry.set("createTime", _createTime);

        int count = table.insert(_cardID, entry);
        return(count);
    }

    function selectByCardID(string _cardID)
        public
        returns (
            string,
            string,
            string,
            int,
            int,
            uint
        )
    {
        Table table = tf.openTable(tableName);

        Condition condition = table.newCondition();
        condition.EQ("cardID", _cardID);

        Entries entries = table.select(_cardID, condition);
        Entry entry = entries.get(0);

        cardID = entry.getString("cardID");
        consumerID = entry.getString("consumerID");
        shopID = entry.getString("shopID");
        balance = entry.getInt("balance");
        contractHash = entry.getInt("contractHash");
        createTime = entry.getUInt("createTime");

        return (cardID, consumerID, shopID, balance, contractHash, createTime);
    }

    function changeBalance(string _cardID, int _value) public returns (int) {
        Table table = tf.openTable(tableName);

        Condition condition = table.newCondition();
        condition.EQ("cardID", _cardID);

        Entries entries = table.select(_cardID, condition);
        Entry entry = entries.get(0);
        balance = entry.getInt("balance");

        entry = table.newEntry();
        entry.set("balance", balance - _value);
        int count = table.update(_cardID, entry, condition);

        emit ChangeBalance(count);
        return count;
    }
}
