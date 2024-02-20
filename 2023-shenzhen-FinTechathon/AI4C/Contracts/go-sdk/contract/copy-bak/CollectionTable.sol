pragma solidity >=0.5.0 <0.8.20;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./AccountTable.sol";

contract collectionTable {
    event CreateResult(int256 count);
    event InsertResult(int256 count);
    event UpdateResult(int256 count);
    event RemoveResult(int256 count);

    TableFactory tableFactory;
    AccountTable accountTable;
    string constant TABLE_NAME = "t_collection";
    constructor() public {
        tableFactory = TableFactory(0x1001);
        accountTable = AccountTable(0x1001);

        tableFactory.createTable(TABLE_NAME, "collection_id","collection_name,owner_card,owner_id, owner_name,certificate_time,certificate_organization,collection_semantic,collection_trace,collection_tradable,aigc_gprice,aigc_cprice,aigc_income,collection_price");
    }

    //select records
    function select(string memory collection_id)
    public
    view
    returns (string memory, string memory,string memory,string memory,string memory,string memory,string memory,string memory,int256 ,int256 ,int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();

        Entries entries = table.select(collection_id, condition);

        string[] memory collection_id_list = new string[](uint256(entries.size()));
        string[] memory collection_name_list = new string[](uint256(entries.size()));
        string[] memory owner_id_list = new string[](uint256(entries.size())); 
        string[] memory certificate_time_list = new string[](uint256(entries.size()));

        string[] memory certificate_organization_list = new string[](uint256(entries.size()));
        string[] memory collection_semantic_list = new string[](uint256(entries.size()));
        string[] memory collection_trace_list = new string[](uint256(entries.size()));
        string[] memory collection_tradable_list = new string[](uint256(entries.size()));

        int256[] memory aigc_gprice_list = new int256[](uint256(entries.size()));

        int256[] memory aigc_income_list = new int256[](uint256(entries.size()));
        int256[] memory collection_price_list = new int256[](uint256(entries.size()));



        for (int256 i = 0; i < entries.size(); ++i) {
            Entry entry = entries.get(i);

            collection_id_list[uint256(i)]= entry.getString("collection_id");
            collection_name_list[uint256(i)]= entry.getString("collection_name");
            owner_id_list[uint256(i)]= entry.getString("owner_id");
            certificate_time_list[uint256(i)]= entry.getString("certificate_time");

            certificate_organization_list[uint256(i)]= entry.getString("certificate_organization");
            collection_semantic_list[uint256(i)]= entry.getString("certificate_semantic");
            collection_trace_list[uint256(i)]= entry.getString("collection_trace");
            collection_tradable_list[uint256(i)]= entry.getString("collection_url");

            aigc_gprice_list[uint256(i)]=entry.getInt("aigc_gprice");

            aigc_income_list[uint256(i)]=entry.getInt("aigc_income");
            collection_price_list[uint256(i)]=entry.getInt("collection_price");
        }
        return (collection_id_list[0],collection_name_list[0],owner_id_list[0],certificate_time_list[0],certificate_organization_list[0],collection_semantic_list[0],collection_trace_list[0],collection_tradable_list[0],aigc_gprice_list[0],aigc_income_list[0],collection_price_list[0]);
    }



    //insert records
    function insert(string memory collection_id,string memory collection_name,string memory owner_id, string memory certificate_time,
string memory certificate_organization,string memory collection_semantic,string memory collection_trace,string memory collection_url,string memory collection_tradable,
int256 aigc_gprice,int256 aigc_income,int256 collection_price)
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
        entry.set("collection_trace",collection_trace);
        entry.set("collection_url",collection_url);

        entry.set("collection_tradable",collection_tradable);
        entry.set("aigc_gprice",aigc_gprice);
      
        entry.set("aigc_income",aigc_income);
        entry.set("collection_price",collection_price);

        int256 count = table.insert(collection_id, entry);
        emit InsertResult(count);

        return count;
    }
    //update records
    function update(string memory collection_id,string memory collection_name,string memory owner_id, string memory certificate_time,
string memory certificate_organization,string memory collection_semantic,string memory collection_trace,string memory collection_url,string memory collection_tradable,
int256 aigc_gprice,int256 aigc_cprice,int256 aigc_income,int256 collection_price)
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
        entry.set("collection_trace",collection_trace);
        entry.set("collection_url",collection_url);
        entry.set("collection_tradable",collection_tradable);
        entry.set("aigc_gprice",aigc_gprice);
        entry.set("aigc_income",aigc_income);
        entry.set("collection_price",collection_price);

        Condition condition = table.newCondition();
        condition.EQ("collection_id", collection_id);
        int256 count = table.update(collection_id, entry, condition);
        emit UpdateResult(count);

        return count;
    }
    //remove records
    function remove(string memory collection_id) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();
        condition.EQ("collection_id", collection_id);
        int256 count = table.remove(collection_id, condition);
        emit RemoveResult(count);

        return count;
    }
    //id1 buy collection from id2
    function transfer(string memory user_id1,string memory user_id2,int256 money,string memory collection_id) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();

        Entries entries1 = table.select(collection_id, condition);
        Entry entry1=entries1.get(0);
        entry1.set("collection_price",money);
        entry1.set("owner_id",user_id1);
        string memory TempTrace = strConcat(entry1.getString("owner_id"),';');
        TempTrace = strConcat(TempTrace,user_id1);
        entry1.set("collection_trace",TempTrace);
        accountTable.transfer(user_id1,user_id2,money);

        return 0;
    }

    //id1 use collection from id2
    function gincome(string memory user_id1,string memory user_id2,int256 money,string memory collection_id) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();

        Entries entries1 = table.select(collection_id, condition);
        Entry entry1=entries1.get(0);
        entry1.set("aigc_income",entry1.getInt("aigc_income")+entry1.getInt("aigc_gprice"));

        accountTable.transfer(user_id1,user_id2,entry1.getInt("aigc_gprice"));

        return 0;
    }
        //id1 use collection from id2
    function cincome(string memory user_id1,string memory user_id2,int256 money,string memory collection_id) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();

        Entries entries1 = table.select(collection_id, condition);
        Entry entry1=entries1.get(0);
        entry1.set("aigc_income",entry1.getInt("aigc_income")+entry1.getInt("aigc_cprice"));

        accountTable.transfer(user_id1,user_id2,entry1.getInt("aigc_cprice"));

        return 0;
    }


    function strConcat(string memory a, string memory b) internal returns (string memory){
        bytes memory ba = bytes(a);
        bytes memory bb = bytes(b);
        string memory ret = new string(ba.length + bb.length);
        bytes memory bret = bytes(ret);
        uint k = 0;
        for (uint256 i = 0; i < ba.length; i++)bret[k++] = ba[i];
        for (uint256 i = 0; i < bb.length; i++) bret[k++] = bb[i];
        return string(ret);
   }  
}
   