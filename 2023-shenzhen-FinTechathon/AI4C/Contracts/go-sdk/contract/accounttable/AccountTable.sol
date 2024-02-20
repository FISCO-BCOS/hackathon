pragma solidity >=0.5.0 <0.8.20;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract AccountTable {
    event CreateResult(int256 count);
    event InsertResult(int256 count);
    event UpdateResult(int256 count);
    event RemoveResult(int256 count);

    TableFactory tableFactory;
    string constant TABLE_NAME = "t_user";
    constructor() public {
        tableFactory = TableFactory(0x1001);
     
        tableFactory.createTable(TABLE_NAME, "user_id", "user_money,user_name,user_icon");
    }

    //select records
    function select(string memory user_id)
    public
    view
    returns (string[] memory, int256[] memory, string[] memory, string[] memory)
    {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();

        Entries entries = table.select(user_id, condition);
        string[] memory user_id_bytes_list = new string[](
            uint256(entries.size())
        );
        int256[] memory user_money_list = new int256[](uint256(entries.size()));
        string[] memory user_name_bytes_list = new string[](
            uint256(entries.size())
        );
        string[] memory user_icon_bytes_list = new string[](
            uint256(entries.size())
        );

        for (int256 i = 0; i < entries.size(); ++i) {
            Entry entry = entries.get(i);

            user_id_bytes_list[uint256(i)] = entry.getString("user_id");
            user_money_list[uint256(i)] = entry.getInt("user_money");
            user_name_bytes_list[uint256(i)] = entry.getString("user_name");
            user_icon_bytes_list[uint256(i)] = entry.getString("user_icon");
        }
        return (user_id_bytes_list, user_money_list, user_name_bytes_list, user_icon_bytes_list);
    }

    //insert records
    function insert(string memory user_id, int256 user_money, string memory user_name,string memory user_icon)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);

        Entry entry = table.newEntry();
        entry.set("user_id", user_id);
        entry.set("user_name", user_name);
        entry.set("user_money", user_money);
        entry.set("user_icon", user_icon);

        int256 count = table.insert(user_id, entry);
        emit InsertResult(count);

        return count;
    }
    //update records
    function update(string memory user_id, int256 user_money, string memory user_name, string memory user_icon)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);

        Entry entry = table.newEntry();
        entry.set("user_id", user_id);
        entry.set("user_money", user_money);
        entry.set("user_name", user_name);
        entry.set("user_icon", user_icon);

        Condition condition = table.newCondition();
        condition.EQ("user_id", user_id);
        int256 count = table.update(user_id, entry, condition);
        emit UpdateResult(count);

        return count;
    }
    //remove records
    function remove(string memory user_id) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();
        condition.EQ("user_id", user_id);
        int256 count = table.remove(user_id, condition);
        emit RemoveResult(count);

        return count;
    }
    function transfer(string memory user_id1,string memory user_id2,int256 money) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();

        Entries entries1 = table.select(user_id1, condition);
        Entry entry1=entries1.get(0);
        entry1.set("user_money",entry1.getInt("user_money")-money);

        Entries entries2 = table.select(user_id2, condition);
        Entry entry2=entries2.get(0);
        entry2.set("user_money",entry2.getInt("user_money")+money);
        
        Condition condition1 = table.newCondition();
        condition1.EQ("user_id", user_id1);
        int256 count = table.update(user_id1, entry1, condition1);
        
        Condition condition2 = table.newCondition();
        condition2.EQ("user_id", user_id2);
        count += table.update(user_id2, entry2, condition2);
        emit UpdateResult(count);

        return count;
    }
}
   