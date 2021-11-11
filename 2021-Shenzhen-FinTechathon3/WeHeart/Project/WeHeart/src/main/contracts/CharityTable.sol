pragma solidity ^0.4.20;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract CharityTable {
    event CreateResult(int count);
    event InsertResult(int count);
    event UpdateResult(int count);
    event RemoveResult(int count);

    struct Charity {
        string[] owner_address_list;
        string[] title_list;
        int[] price_list;
        string[] description_list;
        int[] state_list;
    }
    string public table_name;
    string internal primaryKey = "Charity";

    /*
    create table
    */
    function create(string _table_name) public returns(int) {
        table_name = _table_name;
        TableFactory tf = TableFactory(0x1001);  // TableFactory address is always 0x1001
        // key_field:name，value_field:item_id,item_name
        int count = tf.createTable(table_name,
                                   "sort",
                                   "owner_address,volunteer_address,title,price,description,state");
        emit CreateResult(count);

        return count;
    }

    /*
    search data
    */
    function select(string key, string value) public constant
        returns(
                string[],
                string[],
                int[],
                string[],
                int[]) {
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable(table_name);

        Condition condition = table.newCondition();
        if(keccak256(abi.encodePacked(key)) == keccak256(abi.encodePacked("owner_address")))
            condition.EQ("owner_address", value);
        if(keccak256(abi.encodePacked(key)) == keccak256(abi.encodePacked("volunteer_address")))
            condition.EQ("volunteer_address", value);

        Entries entries = table.select(primaryKey, condition);
        Charity charity;
        charity.owner_address_list = new string[](uint256(entries.size()));
        charity.title_list = new string[](uint256(entries.size()));
        charity.price_list = new int[](uint256(entries.size()));
        charity.description_list = new string[](uint256(entries.size()));
        charity.state_list = new int[](uint256(entries.size()));

        for(int i=0; i<entries.size(); ++i) {
            Entry entry = entries.get(i);

            charity.owner_address_list[uint256(i)] = entry.getString("owner_address");
            charity.title_list[uint256(i)] = entry.getString("title");
            charity.price_list[uint256(i)] = entry.getInt("price");
            charity.description_list[uint256(i)] = entry.getString("description");
            charity.state_list[uint256(i)] = entry.getInt("state");
        }

        return (charity.owner_address_list,
                charity.title_list,
                charity.price_list,
                charity.description_list,
                charity.state_list);
    }

    /*
    insert data
    */
    function insert(string p2p_address,
                    string owner_address,
                    string title,
                    uint256 price,
                    string description,
                    uint256 state) public returns(int) {

        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable(table_name);

        Entry entry = table.newEntry();
        entry.set("sort", primaryKey);
        entry.set("owner_address", owner_address);
        entry.set("title", title);
        entry.set("price", price);
        entry.set("description", description);
        entry.set("state", state);

        int count = table.insert(primaryKey, entry);
        emit InsertResult(count);

        return count;
    }

    /*
    update data
    */
    function update(
                    string volunteer_address,
                    int state) public returns(int) {
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable(table_name);

        Entry entry = table.newEntry();
        entry.set("state", state);
        if(keccak256(abi.encodePacked(volunteer_address)) != keccak256(abi.encodePacked(""))) {
            entry.set("volunteer_address", volunteer_address);
        }

        Condition condition = table.newCondition();
        condition.EQ("sort", primaryKey);

        int count = table.update(primaryKey, entry, condition);
        emit UpdateResult(count);

        return count;
    }


}