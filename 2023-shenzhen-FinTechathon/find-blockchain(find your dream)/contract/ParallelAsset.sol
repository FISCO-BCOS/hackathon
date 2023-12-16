pragma solidity ^0.4.25;

pragma experimental ABIEncoderV2;

import "./ParallelContract.sol";// 引入ParallelContract.sol
import "./Table.sol";

contract ParallelAsset is ParallelContract{
    // event
    event RegisterEvent(int256 ret, string account_name, int256 asset_value);
    event TransferEvent(int256 ret, string from_account, string to_account, int256 amount);
    event UpdateResult(int256 count);
    event RemoveResult(int256 count);

    TableFactory tableFactory;
    string constant TABLE_NAME = "t_account";
    constructor() public {
        tableFactory = TableFactory(0x1001); //The fixed address is 0x1001 for TableFactory
        // the parameters of createTable are tableName,keyField,"vlaueFiled1,vlaueFiled2,vlaueFiled3,..."
        tableFactory.createTable(TABLE_NAME, "account", "account_name,account_asset");
    }

    //selectAll records
    function selectAll(string memory account)
    public
    view
    returns (string[] memory, int256[] memory, string[] memory)
    {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();

        Entries entries = table.select(account, condition);
        string[] memory account_bytes_list = new string[](
            uint256(entries.size())
        );
        int256[] memory account_asset_list = new int256[](uint256(entries.size()));
        string[] memory account_name_list = new string[](
            uint256(entries.size())
        );

        for (int256 i = 0; i < entries.size(); ++i) {
            Entry entry = entries.get(i);

            account_bytes_list[uint256(i)] = entry.getString("account");
            account_asset_list[uint256(i)] = entry.getInt("account_asset");
            account_name_list[uint256(i)] = entry.getString("account_name");
        }

        return (account_bytes_list, account_asset_list, account_name_list);
    }
    
    //select records
    function select(string memory account_name)
    public
    view
    returns (string memory, int256, string memory)
    {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();
        Entries entries = table.select("account", condition);
        string[] memory account_bytes_list = new string[](
            uint256(entries.size())
        );
        int256[] memory account_asset_list = new int256[](uint256(entries.size()));
        string[] memory account_name_list = new string[](
            uint256(entries.size())
        );

	int256 ccount = -1;

        (account_bytes_list, account_asset_list, account_name_list) = selectAll("account");


        for (int256 i = 0; i < entries.size(); ++i) {
            string memory targetname = account_name_list[uint256(i)];

            if(isEqual(targetname,account_name)){
            return (account_bytes_list[uint256(i)], account_asset_list[uint256(i)], account_name_list[uint256(i)]);
            }

        }
        return ("-1",ccount,"-1");
    }
    
    function isEqual(string memory a, string memory b) public pure returns (bool) {
        bytes memory aa = bytes(a);
        bytes memory bb = bytes(b);
        // 如果长度不等，直接返回
        if (aa.length != bb.length) return false;
        // 按位比较
        for(uint i = 0; i < aa.length; i ++) {
            if(aa[i] != bb[i]) return false;
        }

        return true;
}
    
        /*
    描述 : 资产注册
    参数 ：
            account : 资产账户
            amount  : 资产金额
    返回值：
            0  资产注册成功
            -1 资产账户已存在
            -2 其他错误
    */
    function register(string account_name, int256 asset_value) public returns(int256){
        string memory accountname = "";
        string memory name = ""; 
        int256 ret_code = 0;
        int256 temp_asset_value = 0;
        // 查询账户是否存在
        (accountname, temp_asset_value, name) = select(account_name);
        if(isEqual(accountname,"-1")) {
	    Table table = tableFactory.openTable(TABLE_NAME);

            Entry entry = table.newEntry();
            entry.set("account", "account"); 
            entry.set("account_name", account_name);
            entry.set("account_asset", int256(asset_value));
            // 插入
            int count = table.insert("account", entry);
            if (count == 1) {
                // 成功
                ret_code = 0;
            } else {
                // 失败? 无权限或者其他错误
                ret_code = -2;
            }
        } else {
            // 账户已存在
            ret_code = -1;
        }

        emit RegisterEvent(ret_code, account_name, asset_value);

        return ret_code;
    }

        /*
    描述 : 资产转移
    参数 ：
            from_account : 转移资产账户
            to_account ： 接收资产账户
            amount ： 转移金额
    返回值：
            0  资产转移成功
            -1 转移资产账户不存在
            -2 接收资产账户不存在
            -3 金额不足
            -4 金额溢出
            -5 其他错误
    */
    function transfer(string from_account, string to_account, int256 amount) public returns(int256) {
        // 查询转移资产账户信息
        string memory accountname = "";
        string memory name = ""; 
        int256 ret_code = 0;
        int256 from_asset_value = 0;
        int256 to_asset_value = 0;
        // 转移账户是否存在?
        (accountname, from_asset_value, name) = select(from_account);
        if(isEqual(accountname,"-1")) {
            ret_code = -1;
            // 转移账户不存在
            emit TransferEvent(ret_code, from_account, to_account, amount);
            return ret_code;

        }
        // 接受账户是否存在?
        (accountname, to_asset_value, name) = select(to_account);
        if(isEqual(accountname,"-1")) {
            ret_code = -2;
            // 接收资产的账户不存在
            emit TransferEvent(ret_code, from_account, to_account, amount);
            return ret_code;
        }

        if(from_asset_value < amount) {
            ret_code = -3;
            // 转移资产的账户金额不足
            emit TransferEvent(ret_code, from_account, to_account, amount);
            return ret_code;
        }
        if (to_asset_value + amount < to_asset_value) {
            ret_code = -4;
            // 接收账户金额溢出
            emit TransferEvent(ret_code, from_account, to_account, amount);
            return ret_code;
        }
        Table table = tableFactory.openTable(TABLE_NAME);
        Entry entry0 = table.newEntry();
        entry0.set("account", "account");
        entry0.set("account_name", from_account);
        entry0.set("account_asset", int256(from_asset_value - amount));
        // 更新转账账户
        int count = update(from_account, int256(from_asset_value - amount));
        if(count != 1) {
            ret_code = -5;
            // 失败? 无权限或者其他错误?
            emit TransferEvent(ret_code, from_account, to_account, amount);
            return ret_code;
        }
        Entry entry1 = table.newEntry();
        entry0.set("account", "account");
        entry1.set("account_name", to_account);
        entry1.set("account_asset", int256(to_asset_value + amount));
        // 更新接收账户
        update(to_account, int256(to_asset_value - amount));
        emit TransferEvent(ret_code, from_account, to_account, amount);
        return ret_code;
    }
    
    
    
    //insert records
    //function insert(string memory account, string memory account_name, int256 account_asset)
    //public
    //returns (int256)
    //{
        //Table table = tableFactory.openTable(TABLE_NAME);

        //Entry entry = table.newEntry();
        //entry.set("account", account);
        //entry.set("account_name", account_name);
        //entry.set("account_asset", account_asset);

        //int256 count = table.insert(account, entry);
        //emit InsertResult(count);

        //return count;
    //}
    
    //update records
    function update(string memory account_name, int256 account_asset)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);

        Entry entry = table.newEntry();
        entry.set("account_asset", account_asset);

        Condition condition = table.newCondition();
        condition.EQ("account", "account");
        condition.EQ("account_name", account_name);

        int256 count = table.update("account", entry, condition);
        emit UpdateResult(count);

        return count;
    }

    //Addupdate records
    function Addupdate(string memory account_name, int256 account_asset)
    public
    returns (int256)
    {
        string memory accountname = "";
        string memory name = ""; 
        int256 temp_asset_value = 0;
        // 查询账户获取余额
        (accountname, temp_asset_value, name) = select(account_name);
        Table table = tableFactory.openTable(TABLE_NAME);

        Entry entry = table.newEntry();
        entry.set("account_asset", account_asset+temp_asset_value);

        Condition condition = table.newCondition();
        condition.EQ("account", "account");
        condition.EQ("account_name", account_name);

        int256 count = table.update("account", entry, condition);
        emit UpdateResult(count);

        return count;
    }

    //Addupdate records
    function Minusupdate(string memory account_name, int256 account_asset)
    public
    returns (int256)
    {
        string memory accountname = "";
        string memory name = ""; 
        int256 temp_asset_value = 0;
        // 查询账户获取余额
        (accountname, temp_asset_value, name) = select(account_name);
        Table table = tableFactory.openTable(TABLE_NAME);

        Entry entry = table.newEntry();
        entry.set("account_asset", temp_asset_value-account_asset);

        Condition condition = table.newCondition();
        condition.EQ("account", "account");
        condition.EQ("account_name", account_name);

        int256 count = table.update("account", entry, condition);
        emit UpdateResult(count);

        return count;
    }

    //remove records
    function remove(string memory account_name) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_NAME);

        Condition condition = table.newCondition();
        condition.EQ("account", "account");
        condition.EQ("account_name", account_name);

        int256 count = table.remove("account", condition);
        emit RemoveResult(count);

        return count;
    }
    
        // 注册可以并行的合约接口
    function enableParallel() public
    {
        // 函数定义字符串（注意","后不能有空格）,参数的前几个是互斥参数（设计函数时互斥参数必须放在前面
        registerParallelFunction("transfer(string,string,int256)", 2); // critical: string string
	registerParallelFunction("register(string,int256)", 1); // set接口，前1个是互斥参数
	registerParallelFunction("update(string,int256)", 1); 
	registerParallelFunction("Addupdate(string,int256)", 1); 
	registerParallelFunction("Minusupdate(string,int256)", 1); 
	registerParallelFunction("remove(string)", 1); 
    }
   
    // 注销并行合约接口
    function disableParallel() public
    {
        unregisterParallelFunction("transfer(string,string,int256)");
        unregisterParallelFunction("register(string,int256)");
	unregisterParallelFunction("update(string,int256)"); 
	unregisterParallelFunction("Addupdate(string,int256)"); 
	unregisterParallelFunction("Minusupdate(string,int256)");         
	unregisterParallelFunction("remove(string)"); 
    } 
    
    
}
