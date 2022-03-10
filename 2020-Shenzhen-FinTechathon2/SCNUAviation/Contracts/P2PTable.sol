pragma solidity ^0.4.20;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract P2PTable {
    event CreateResult(int count);
    event InsertResult(int count);
    event UpdateResult(int count);
    event RemoveResult(int count);

    struct P2P {
        string[] p2p_address_list;
        string[] owner_address_list;
        string[] title_list;
        int[] price_list;
        string[] description_list;
        int[] state_list;
    }
    string public table_name;
    string internal primaryKey = "p2p";

    // 创建表
    function create(string _table_name) public returns(int) {
        table_name = _table_name;
        TableFactory tf = TableFactory(0x1001);  // TableFactory的地址固定为0x1001
        // 创建t_test表，表的key_field为name，value_field为item_id,item_name
        // key_field表示AMDB主key value_field表示表中的列，可以有多列，以逗号分隔
        int count = tf.createTable(table_name,
                                   "sort",
                                   "p2p_address,owner_address,volunteer_address,title,price,description,state");
        emit CreateResult(count);

        return count;
    }

    function setTableName(string _tableName) public {
        table_name = _tableName;
    }

    // 查询数据
    function select(string key, string value) public constant
//    function select(string key, string value) public
        returns(string[],
                string[],
                string[],
                int[],
                string[],
                int[]) {
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable(table_name);

        // 条件为空表示不筛选 也可以根据需要使用条件筛选
        Condition condition = table.newCondition();
        if(keccak256(abi.encodePacked(key)) == keccak256(abi.encodePacked("owner_address")))
            condition.EQ("owner_address", value);
        if(keccak256(abi.encodePacked(key)) == keccak256(abi.encodePacked("volunteer_address")))
            condition.EQ("volunteer_address", value);

        Entries entries = table.select(primaryKey, condition);
        P2P memory p2p;
        p2p.p2p_address_list = new string[](uint256(entries.size()));
        p2p.owner_address_list = new string[](uint256(entries.size()));
        p2p.title_list = new string[](uint256(entries.size()));
        p2p.price_list = new int[](uint256(entries.size()));
        p2p.description_list = new string[](uint256(entries.size()));
        p2p.state_list = new int[](uint256(entries.size()));

        for(int i=0; i<entries.size(); ++i) {
            Entry entry = entries.get(i);

            p2p.p2p_address_list[uint256(i)] = entry.getString("p2p_address");
            p2p.owner_address_list[uint256(i)] = entry.getString("owner_address");
            p2p.title_list[uint256(i)] = entry.getString("title");
            p2p.price_list[uint256(i)] = entry.getInt("price");
            p2p.description_list[uint256(i)] = entry.getString("description");
            p2p.state_list[uint256(i)] = entry.getInt("state");
        }

        return (p2p.p2p_address_list,
                p2p.owner_address_list,
                p2p.title_list,
                p2p.price_list,
                p2p.description_list,
                p2p.state_list);
    }

    // 插入数据
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
        entry.set("p2p_address", p2p_address);
        entry.set("owner_address", owner_address);
        entry.set("title", title);
        entry.set("price", price);
        entry.set("description", description);
        entry.set("state", state);

        int count = table.insert(primaryKey, entry);
        emit InsertResult(count);

        return count;
    }

    // 更新数据
    function update(string p2p_address,
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
        condition.EQ("p2p_address", p2p_address);

        int count = table.update(primaryKey, entry, condition);
        emit UpdateResult(count);

        return count;
    }

    // 删除数据
//    function remove(string name, int item_id) public returns(int){
//        TableFactory tf = TableFactory(0x1001);
//        Table table = tf.openTable("t_test");
//
//        Condition condition = table.newCondition();
//        condition.EQ("name", name);
//        condition.EQ("item_id", item_id);
//
//        int count = table.remove(name, condition);
//        emit RemoveResult(count);
//
//        return count;
//    }


}
