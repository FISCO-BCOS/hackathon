pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract Bank
{
    address bank_addr;
    string public bank_name;
    string public bank_id;
    
    TableFactory tf;
    string constant t_name = "TransferInfos";
 
    event insertTable(string tName, uint ProjectID, address Sender, address Receiver, int money, uint Timestamp, string TransferID, string remark);
    
    struct List {
        address Sender;
        address Receiver;
        int money;
        uint Timestamp;
        string TransferID;
        string remark;
    }
    
    modifier onlyOwner() 
    {
        require(msg.sender == bank_addr, "无权限!");
        _;
    }
    
    constructor(string name, string id) public
    {
        bank_addr = msg.sender;
        bank_id = id;
        bank_name = name;
        createTable();
    }
    
    //建立表格
    function createTable() private 
    {
        tf = TableFactory(0x1001);
        //| 项目编号(主键) | 汇款方 | 收款方 | 汇款金额 | 操作时间 | 流水号 | 备注 |
        //| uint | address | address | int | uint | string | address| string |
        tf.createTable(t_name, "ProjectID", "Sender,Receiver,money,Timestamp,TransferID,remark");
    }
    
    //打开表格
    function openTable() private returns(Table) 
    {
        Table table = tf.openTable(t_name);
        return table;
    }
    
    //表格添加数据
    function insert(uint ProjectID, address Sender, address Receiver, int money, uint Timestamp, string TransferID, string remark)
    {
        Table table = tf.openTable(t_name);
        Entry entry = table.newEntry();
        entry.set("ProjectID", ProjectID);
        entry.set("Sender", Sender);
        entry.set("Receiver", Receiver);
        entry.set("money", now);
        entry.set("TransferID", TransferID);
        entry.set("remark", remark);
        emit insertTable(t_name, ProjectID, Sender, Receiver, money, now, TransferID, remark);
    }
    
    //根据项目编号查询流水
    function select(uint ProjectID) public view returns (uint, List[] memory)
    {
        Table table = tf.openTable(t_name);
        Condition condition = table.newCondition();
        Entries entries = table.select(ProjectID, condition);
        List[] memory list = new List[](uint256(entries.size()));
        
        for (int256 i = 0; i < entries.size(); i++)
        {
            Entry entry = entries.get(i);
            
            list[uint256(i)].Sender = entry.getAddress("Sender");
            list[uint256(i)].Receiver = entry.getAddress("Receiver");
            list[uint256(i)].money = entry.getInt("money");
            list[uint256(i)].Timestamp = entry.getUInt("Timestamp");
            list[uint256(i)].TransferID = entry.getString("TransferID");
            list[uint256(i)].remark = entry.getString("remark");
        }
        return (ProjectID, list);
    }
}