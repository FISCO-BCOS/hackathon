// SPDX-License-Identifier: SimPL-2.0
pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./DataFormat.sol";

contract UserAssetInfo is DataFormat{
    event insertResult(int256 count);
    event updateResult(int256 count);

    TableFactory tableFactory;

    string constant ASSET_INFO = "assetInfo";

    constructor() public {
        tableFactory = TableFactory(0x1001);

        tableFactory.createTable(ASSET_INFO, "idNumber", "userName, propertyValue, carValue");

        Table table = tableFactory.openTable(ASSET_INFO);
        Entry entry_1 = table.newEntry();
        Entry entry_2 = table.newEntry();
        Entry entry_3 = table.newEntry();
        Entry entry_4 = table.newEntry();
        Entry entry_5 = table.newEntry();

        entry_1.set("idNumber", "110108199912121212");
        entry_1.set("userName", "Tom");
        entry_1.set("propertyValue", int256(8000));
        entry_1.set("carValue", int256(2000));

        entry_2.set("idNumber", "410527199901010101");
        entry_2.set("userName", "Jerry");
        entry_2.set("propertyValue", int256(10000));
        entry_2.set("carValue", int256(6000));

        entry_3.set("idNumber", "130528199902020202");
        entry_3.set("userName", "Bob");
        entry_3.set("propertyValue", int256(50000));
        entry_3.set("carValue", int256(20000));

        entry_4.set("idNumber", "130224199903030303");
        entry_4.set("userName", "Kitty");
        entry_4.set("propertyValue", int256(40000));
        entry_4.set("carValue", int256(30000));

        entry_5.set("idNumber", "452427199904040404");
        entry_5.set("userName", "Sammy");
        entry_5.set("propertyValue", int256(30000));
        entry_5.set("carValue", int256(10000));

        table.insert("110108199912121212", entry_1);
        table.insert("410527199901010101", entry_2);
        table.insert("130528199902020202", entry_3);
        table.insert("130224199903030303", entry_4);
        table.insert("452427199904040404", entry_5);
    }

    // 插入资产信息
    // 参数(身份证号，姓名，房产，车产)
    function insertUserAssetInfo(string memory idNumber, string memory userName, int256 propertyValue, int256 carValue) 
    public 
    returns(int256)
    {
        Table table = tableFactory.openTable(ASSET_INFO);
        Entry entry = table.newEntry();
        int256 count;

        // Set Value
        entry.set("idNumber", idNumber);
        entry.set("userName", userName);
        entry.set("propertyValue", propertyValue);
        entry.set("carValue", carValue);

        count = table.insert(idNumber, entry);
        emit insertResult(count);

        return count;
    }

    // 查询用户资产信息
    // 参数(身份证号，姓名，房产，车产)   返回值(bool)
    function getUserAssetInfo(string memory idNumber, string memory userName, int256 propertyValue, int256 carValue)
    public
    view
    returns(bool, string memory)
    {
        Table table = tableFactory.openTable(ASSET_INFO);

        string memory userNameTemp;
        int256 propertyValueTemp;
        int256 carValueTemp;

        Condition condition = table.newCondition();
        condition.EQ("idNumber", idNumber);

        Entries entries = table.select(idNumber, condition);
        if(0 == entries.size())
        {
            return(false, "用户ID出错");
        }
      
        Entry entry = entries.get(0);

        userNameTemp = entry.getString("userName");
        propertyValueTemp = entry.getInt("propertyValue");
        carValueTemp = entry.getInt("carValue");

        if(strMatching(userNameTemp, userName) != true)
        {
            return(false, "姓名错误");
        }

        if(propertyValue > (propertyValueTemp * 105 / 100) || propertyValue < (propertyValueTemp * 95 / 100))
        {
            return(false, "房产金额错误");
        }
        if(carValue > (carValueTemp * 105 / 100) || carValue < (carValueTemp * 95 / 100))
        {
            return(false, "车产金额错误");
        }

        return(true, "");
    }
}