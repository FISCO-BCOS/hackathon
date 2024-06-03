// SPDX-License-Identifier: SimPL-2.0
pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./DataFormat.sol";

contract Medical is DataFormat{
    event insertResult(int256 count);
    event updateResult(int256 count);

    TableFactory tableFactory;

    string constant MEIDICAL_RECORD = "medicalRecord";

    constructor() public {
        tableFactory = TableFactory(0x1001);

        tableFactory.createTable(MEIDICAL_RECORD, "idNumber", "userName, userMedicalRecord");

        Table table = tableFactory.openTable(MEIDICAL_RECORD);
        Entry entry_1 = table.newEntry();
        Entry entry_2 = table.newEntry();
        Entry entry_3 = table.newEntry();
        Entry entry_4 = table.newEntry();
        Entry entry_5 = table.newEntry();

        entry_1.set("idNumber", "110108199912121212");
        entry_1.set("userName", "Tom");
        entry_1.set("userMedicalRecord", "心脑血管-心肌梗死");

        entry_2.set("idNumber", "410527199901010101");
        entry_2.set("userName", "Jerry");
        entry_2.set("userMedicalRecord", "感染病-病毒性肝炎");

        entry_3.set("idNumber", "130528199902020202");
        entry_3.set("userName", "Bob");
        entry_3.set("userMedicalRecord", "血液恶性病-白血病");

        entry_4.set("idNumber", "130224199903030303");
        entry_4.set("userName", "Kitty");
        entry_4.set("userMedicalRecord", "肿瘤-恶性肿瘤");

        entry_5.set("idNumber", "452427199904040404");
        entry_5.set("userName", "Sammy");
        entry_5.set("userMedicalRecord", "心脑血管-冠心病");
        
        table.insert("110108199912121212", entry_1);
        table.insert("410527199901010101", entry_2);
        table.insert("130528199902020202", entry_3);
        table.insert("130224199903030303", entry_4);
        table.insert("452427199904040404", entry_5);
    }

    // 插入病历信息
    // 参数(身份证号，姓名，病历信息)
    function insertMeidicalRecord(string memory idNumber, string memory userName, string memory userMedicalRecord) 
    public 
    returns(int256)
    {
        Table table = tableFactory.openTable(MEIDICAL_RECORD);
        Entry entry = table.newEntry();
        int256 count;

        // Set Value
        entry.set("idNumber", idNumber);
        entry.set("userName", userName);
        entry.set("userMedicalRecord", userMedicalRecord);

        count = table.insert(idNumber, entry);
        emit insertResult(count);

        return count;
    }

    // 查询用户病历信息
    // 参数(身份证号, 姓名, 病历信息)   返回值(bool, 错误信息)
    function getUserMedicalRecord(string memory idNumber, string memory userName, string memory userMedicalRecord)
    public
    view
    returns(bool, string memory)
    {
        Table table = tableFactory.openTable(MEIDICAL_RECORD);

        Condition condition = table.newCondition();
        condition.EQ("idNumber", idNumber);

        string memory userNameTemp;
        string memory userMedicalRecordTemp;

        Entries entries = table.select(idNumber, condition);
        if(0 == entries.size())
        {
            return(false, "用户ID出错");
        }
      
        Entry entry = entries.get(0);

        userNameTemp = entry.getString("userName");
        userMedicalRecordTemp = entry.getString("userMedicalRecord");
        if(strMatching(userNameTemp, userName) != true)
        {
            return(false, "姓名错误");
        }
        if(strMatching(userMedicalRecordTemp, userMedicalRecord) != true)
        {
            return(false, "病历信息错误");
        }
      
        return(true, "");
    }
}