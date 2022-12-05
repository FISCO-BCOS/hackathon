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
    string internal primaykey = "cjr";
    TableFactory tf = TableFactory(0x1001);

    event CreateTable(int count);
    event ChangeBalance(int count);

    constructor() public {
        tableName = "prepaidCard3";
        int count = tf.createTable(
            tableName,
            "sort",
            "cardID,consumerID,shopID,balance,contractHash,createTime"
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
        entry.set("sort", primaykey);
        entry.set("cardID", _cardID);
        entry.set("consumerID", _consumerID);
        entry.set("shopID", _shopID);
        entry.set("balance", _balance);
        entry.set("contractHash", _contractHash);
        entry.set("createTime", _createTime);

        int count = table.insert(primaykey, entry);
        return count;
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
        condition.EQ("sort", primaykey);
        condition.EQ("cardID", _cardID);

        Entries entries = table.select(primaykey, condition);
        Entry entry = entries.get(0);

        cardID = entry.getString("cardID");
        consumerID = entry.getString("consumerID");
        shopID = entry.getString("shopID");
        balance = entry.getInt("balance");
        contractHash = entry.getInt("contractHash");
        createTime = entry.getUInt("createTime");

        return (cardID, consumerID, shopID, balance, contractHash, createTime);
    }
    
    function selectAll() 
        public 
        returns(
            string[],
            string[],
            string[],
            int[],
            int[],
            uint[]
        ) 
    {
        Table table = tf.openTable(tableName);
        Entries entries = table.select(primaykey, table.newCondition());
        
        string[] memory cardID1 = new string[](uint256(entries.size()));
        string[] memory consumerID1 = new string[](uint256(entries.size()));
        string[] memory shopID1 = new string[](uint256(entries.size()));
        int[] memory balance1 = new int[](uint256(entries.size()));
        int[] memory contractHash1 = new int[](uint256(entries.size()));
        uint[] memory createTime1 = new uint[](uint256(entries.size()));
        
        for (int i=0; i<entries.size(); i++) {
            Entry entry = entries.get(i);
            
            cardID1[uint256(i)] = entry.getString("cardID");
            consumerID1[uint256(i)] = entry.getString("consumerID");
            shopID1[uint256(i)] = entry.getString("shopID");
            balance1[uint256(i)] = entry.getInt("balance");
            contractHash1[uint256(i)] = entry.getInt("contractHash");
            createTime1[uint256(i)] = entry.getUInt("createTime");
        }
        
        return (cardID1,consumerID1,shopID1,balance1,contractHash1,createTime1);
    }
    
    
    function changeBalance(string _cardID, int _value) public returns (int) {
        Table table = tf.openTable(tableName);

        Condition condition = table.newCondition();
        condition.EQ("sort", primaykey);
        condition.EQ("cardID", _cardID);

        Entries entries = table.select(primaykey, condition);
        Entry entry = entries.get(0);
        balance = entry.getInt("balance");

        entry = table.newEntry();
        entry.set("balance", balance - _value);
        int count = table.update(primaykey, entry, condition);

        emit ChangeBalance(count);
        return count;
    }
    
    function deleteCard(string _cardID) public returns(int) {
        Table table = tf.openTable(tableName);
        
        Condition condition = table.newCondition();
        condition.EQ("sort", primaykey);
        condition.EQ("cardID", _cardID);
        
        int count = table.remove(primaykey, condition);
        return count;
    }
}
