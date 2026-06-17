pragma solidity >=0.5.0 <0.8.20;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract CollectionTable {
    event CreateResult(int256 count);
    event InsertResult(int256 count);
    event UpdateResult(int256 count);
    event RemoveResult(int256 count);
    event TransferResult(int256 count);

    TableFactory tableFactory;
    string constant TABLE_NAME = "t_collection";
    constructor() public {
        tableFactory = TableFactory(0x1001);

        tableFactory.createTable(TABLE_NAME, "collection_id", "collection_name,owner_id,certificate_time,certificate_organization,collection_semantic");
    }

    //select records
    function select(string memory collection_id)
    public
    view
    returns (string memory, string memory,string memory,string memory,string memory,string memory)
    {
        Table table = tableFactory.openTable(TABLE_NAME);
        Condition condition = table.newCondition();
        Entries entries = table.select(collection_id, condition);
        string[] memory collection_ids = new string[](uint256(entries.size()));
        string[] memory collection_names = new string[](uint256(entries.size()));
        string[] memory owner_ids = new string[](uint256(entries.size())); 
        string[] memory certificate_times = new string[](uint256(entries.size()));
        string[] memory certificate_organizations = new string[](uint256(entries.size()));
        string[] memory collection_semantics = new string[](uint256(entries.size()));



        for (int256 i = 0; i < entries.size(); ++i) {
            Entry entry = entries.get(i);

            collection_ids[uint256(i)]= entry.getString("collection_id");
            collection_names[uint256(i)]= entry.getString("collection_name");
            owner_ids[uint256(i)]= entry.getString("owner_id");
            certificate_times[uint256(i)]= entry.getString("certificate_time");
            certificate_organizations[uint256(i)]= entry.getString("certificate_organization");
            collection_semantics[uint256(i)]= entry.getString("collection_semantic");

        }
        return (collection_ids[0],collection_names[0],owner_ids[0],certificate_times[0],certificate_organizations[0],collection_semantics[0]);
    }


 
    //insert records
    function insert(string memory collection_id,string memory collection_name,string memory owner_id, string memory certificate_time,string memory certificate_organization,string memory collection_semantic)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);

        Entry entry = table.newEntry();

        entry.set("collection_id",collection_id);
        
        entry.set("collection_name",collection_name);
        entry.set("owner_id",owner_id);
        entry.set("certificate_time",certificate_time);

        entry.set("certificate_organization",certificate_organization);
        entry.set("collection_semantic",collection_semantic);
        int256 insert_result = table.insert(collection_id, entry);
        emit InsertResult(insert_result);

        return insert_result;
    }
    //update records
    function update(string memory collection_id,string memory collection_name,string memory owner_id, string memory certificate_time,
string memory certificate_organization,string memory collection_semantic)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);

        Entry entry = table.newEntry();

        entry.set("collection_id",collection_id);
        entry.set("collection_name",collection_name);
        entry.set("owner_id",owner_id);
        entry.set("certificate_time",certificate_time);
        entry.set("certificate_organization",certificate_organization);
        entry.set("collection_semantic",collection_semantic);

        Condition condition = table.newCondition();
        condition.EQ("collection_id", collection_id);
        int256 update_result = table.update(collection_id, entry, condition);
        emit UpdateResult(update_result);
        return update_result;
    }
    //remove records
    function remove(string memory collection_id) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();
        condition.EQ("collection_id", collection_id);
        int256 remove_result = table.remove(collection_id, condition);
        emit RemoveResult(remove_result);

        return remove_result;
    }
    //id1 buy collection from id2
    function transfer(string memory user_id1,string memory user_id2,int256 money,string memory collection_id) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);
        Condition condition = table.newCondition();

        Entries entries1 = table.select(collection_id, condition);
        Entry entry1=entries1.get(0);
        entry1.set("owner_id",user_id2);

        condition.EQ("collection_id", collection_id);
        int256 transfer_result = table.update(collection_id, entry1, condition);
        emit TransferResult(transfer_result);
        account_transfer(user_id1, user_id2, money);
        return transfer_result;
    }

    //id1 use collection from id2
    function gincome(string memory user_id1,string memory user_id2,int256 money) public returns (int256) {
        account_transfer(user_id1,user_id2,money);
        return 0;
    }

    function account_transfer(string memory user_id1,string memory user_id2,int256 money) public returns (int256) {
        Table table = tableFactory.openTable("t_user");

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
   