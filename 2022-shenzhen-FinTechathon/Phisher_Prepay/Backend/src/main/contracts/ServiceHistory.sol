pragma solidity ^0.4.20;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract ServiceHistory {

    string serviceID;
    string cardID;
    string record;
    string complaint;

    string public tableName;
    TableFactory tf = TableFactory(0x1001);
    
    event CreateTable(int count);
    event ChangeComplaint(int count);

    constructor() public {
        tableName = "serviceHistory";
        int count = tf.createTable(
            tableName,
            "serviceID",
            "cardID,record,complaint"
        );
        emit CreateTable(count);
    }

    function createHistory(string _serviceID, string _cardID, string _record) public returns(int){
        Table table = tf.openTable(tableName);

        Entry entry = table.newEntry();
        entry.set("serviceID", _serviceID);
        entry.set("cardID", _cardID);
        entry.set("record", _record);
        entry.set("complaint", "Null");

        int count = table.insert(_serviceID, entry);
        return(count);
    }

    function selectByServiceID(string _serviceID) public returns(string, string) {
        Table table = tf.openTable(tableName);

        Condition condition = table.newCondition();
        condition.EQ("serviceID", _serviceID);

        Entries entries = table.select(_serviceID, condition);
        Entry entry = entries.get(0);

        record = entry.getString("record");
        complaint = entry.getString("complaint");

        return(record, complaint);
    }

    function changeComplaint(string _serviceID, string _complaint) public returns (int) {
        Table table = tf.openTable(tableName);

        Entry entry = table.newEntry();
        entry.set("complaint", _complaint);

        Condition condition = table.newCondition();
        condition.EQ("serviceID", _serviceID);

        int count = table.update(_serviceID, entry, condition);

        emit ChangeComplaint(count);
        return count;
    }
}
