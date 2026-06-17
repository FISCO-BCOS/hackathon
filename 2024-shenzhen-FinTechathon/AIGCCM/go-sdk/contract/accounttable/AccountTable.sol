pragma solidity >=0.5.0 <0.8.20;
pragma experimental ABIEncoderV2;

//维护信息：
//usr_id usr_money usr_name usr_icon（图标)
//选：根据id选4个值
//增：插入4个值
//删：根据id删
//转账：根据两个id和转账金额
import "./Table.sol";

contract AccountTable {
    event CreateResult(int256 count);
    event SelectNotNull(string user_id);
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
        string[] memory user_ids = new string[](uint256(entries.size()));
        int256[] memory user_moneys = new int256[](uint256(entries.size()));
        string[] memory user_names = new string[](uint256(entries.size()));
        string[] memory user_icons = new string[](uint256(entries.size()));

        for (int256 i = 0; i < entries.size(); ++i) {
            Entry entry = entries.get(i);
            user_ids[uint256(i)] = entry.getString("user_id");
            user_moneys[uint256(i)] = entry.getInt("user_money");
            user_names[uint256(i)] = entry.getString("user_name");
            user_icons[uint256(i)] = entry.getString("user_icon");
        }
        emit SelectNotNull(user_id);
        return (user_ids, user_moneys, user_names, user_icons);
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

        int256 insert_result = table.insert(user_id, entry);
        emit InsertResult(insert_result);

        return insert_result;
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
        int256 update_result = table.update(user_id, entry, condition);
        emit UpdateResult(update_result);

        return update_result;
    }
    //remove records
    function remove(string memory user_id) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();
        condition.EQ("user_id", user_id);
        int256 remove_result = table.remove(user_id, condition);
        emit RemoveResult(remove_result);

        return remove_result;
    }
    function transfer(string memory user_id1,string memory user_id2,int256 money) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();

        Entries entries1 = table.select(user_id1, condition);
        Entry entry1 = entries1.get(0);
        entry1.set("user_money", entry1.getInt("user_money") - money);

        Entries entries2 = table.select(user_id2, condition);
        Entry entry2 = entries2.get(0);
        entry2.set("user_money", entry2.getInt("user_money") + money);
        
        Condition condition1 = table.newCondition();
        condition1.EQ("user_id", user_id1);
        int256 update_result = table.update(user_id1, entry1, condition1);
        
        Condition condition2 = table.newCondition();
        condition2.EQ("user_id", user_id2);
        update_result += table.update(user_id2, entry2, condition2);
        emit UpdateResult(update_result);

        return update_result;
    }
}
   