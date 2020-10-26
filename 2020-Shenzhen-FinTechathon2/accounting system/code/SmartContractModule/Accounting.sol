pragma solidity ^0.4.24;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./PaillierPrecompiled.sol";

contract Accounting {


    address private _owner;
 
    modifier onlyOwner{
        require(_owner == msg.sender, "Auth: only owner is authorized");
        _;
    }
	
	// 同态加密
	PaillierPrecompiled paillier;
	
    constructor () public {
        _owner = msg.sender;
		// 实例化PaillierPrecompiled合约
        paillier = PaillierPrecompiled(0x5003);
    }
	
	function add(string cipher1, string cipher2) public constant
    returns(string)
    {
        return paillier.paillierAdd(cipher1, cipher2);
    }
	
	struct Amount {
		string debit_all;
		string credit_all;
		int count;
	}


    event createEvent(address owner, string table_name);

    event insertCertificateEvent(address company_address, string[] certificate);

    event insertAccountingEntryStep1Event(address company_address, string id, string certificate_id, string accounting_title_serial_number, string accounting_title_name, string accounting_entry_type, string debit_amount, string credit_amount);
    event insertAccountingEntryStep2Event(address company_address, string id,  string actual_certificate_id, string abstraction, string spent_accounting_entry_id, string original_accounting_entry_id, string original_document_hash);
    event insertAccountingEntryStep3Event(address company_address, string id, string company_id, string is_unspent, string spent_timestamp, string spent_for_certificate_id, string create_timestamp);

    event spendUtxoEvent(address company_address, string company_id, string id, string is_unspent, string spent_timestamp, string spent_for_certificate_id);


    // 创建表
    function create() public onlyOwner returns(int){

        TableFactory tf = TableFactory(0x1001);  // TableFactory的地址固定为0x1001
        // 创建t_test表，表的key_field为name，value_field为item_id,item_name
        // key_field表示AMDB主key value_field表示表中的列，可以有多列，以逗号分隔
        int count = 0;

        count += tf.createTable("certificate", "id", "company_address, certificate_number, year, month, day, total_debit_amount, total_credit_amount, tabulator, company_id, create_timestamp");
        emit createEvent(msg.sender, "certificate");

        count += tf.createTable("accounting_entry", "id", "company_address, certificate_id,actual_certificate_id,abstraction,accounting_title_serial_number,accounting_title_name,accounting_entry_type,spent_accounting_entry_id,original_accounting_entry_id,debit_amount,credit_amount,original_document_hash,company_id,is_unspent,spent_timestamp,spent_for_certificate_id,create_timestamp");
        emit createEvent(msg.sender, "accounting_entry");

        return count;

    }


    



    // 插入凭证数据
    // 
    // // 测试数据
    // company_address: address
    // 0x386cc3ec844f46c434c6ba3575abaf46dfc9ce96
    // 
    // certificate: string id, string certificate_number, string year, string month, string day, string total_debit_amount, string total_credit_amount, string tabulator, string company_id, string create_timestamp
    // ["1", "1", "2020", "9", "28", "1000", "1000", "施建锋", "99", "1601280472"]
    function insert_certificate(address company_address, string[] certificate) public returns(int) {
        
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable("certificate");


        string memory company_address_str = addressToString(company_address);


        Entry entry = table.newEntry();

        entry.set("company_address", company_address_str);
        entry.set("id", certificate[0]);
        entry.set("certificate_number", certificate[1]);
        entry.set("year", certificate[2]);
        entry.set("month", certificate[3]);
        entry.set("day", certificate[4]);
        entry.set("total_debit_amount", certificate[5]);
        entry.set("total_credit_amount", certificate[6]);
        entry.set("tabulator", certificate[7]);
        entry.set("company_id", certificate[8]);
        entry.set("create_timestamp", certificate[9]);


        int count = table.insert(certificate[0], entry);
        emit insertCertificateEvent(company_address, certificate);

        return count;

    }


    // 插入会计分录数组-step1
    // 
    // // 测试数据
    // company_address: address
    // 0x386cc3ec844f46c434c6ba3575abaf46dfc9ce96
    // 
    // ids: ["66", "67", "68"]
    // certificate_id: ["0", "47", "47"]
    
    // accounting_title_serial_number: ["1001", "1001", "1002"]
    // accounting_title_name: ["库存现金", "库存现金", "银行存款"]
    // accounting_entry_type: ["1", "2", "1"]
    // debit_amount: ["-1000", "0", "1000"]
    // credit_amount: ["0", "1000", "0"]
    function insert_accounting_entry_step_1(address company_address, string[] ids, string[] certificate_id, string[] accounting_title_serial_number, string[] accounting_title_name, string[] accounting_entry_type, string[] debit_amount, string[] credit_amount) public returns(int) {
        
 
        // TODO
        // 统计certificate_id不为0的，accounting_entry_type=1的debit_amount总和 是否等于 accounting_entry_type=2的credit_amount总和
		
		Amount memory a;
		a.debit_all = "";
		a.credit_all = "";
		
		
		for(int i=0; i < int(ids.length); ++i) {
			if(hashCompareInternal(accounting_entry_type[uint256(i)],"1")){
				if(bytes(a.debit_all).length==0){
					a.debit_all = debit_amount[uint256(i)];
				}else{
					a.debit_all = add(a.debit_all,debit_amount[uint256(i)]);
				}
			}
			if(hashCompareInternal(accounting_entry_type[uint256(i)],"2")){
				if(bytes(a.credit_all).length==0){
					a.credit_all = credit_amount[uint256(i)];
				}else{
					a.credit_all = add(a.credit_all,credit_amount[uint256(i)]);
				}
			}
		}
		
		if(!hashCompareInternal(a.debit_all,a.credit_all)){
			return -1;
		}		

        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable("accounting_entry");

        string memory company_address_str = addressToString(company_address);


        a.count = 0;
        
        for(i=0; i < int(ids.length); ++i) {


            Entry entry = table.newEntry();

            entry.set("company_address", company_address_str);
            entry.set("id", ids[uint256(i)]);
            entry.set("certificate_id", certificate_id[uint256(i)]);
            
            entry.set("accounting_title_serial_number", accounting_title_serial_number[uint256(i)]);
            entry.set("accounting_title_name", accounting_title_name[uint256(i)]);
            entry.set("accounting_entry_type", accounting_entry_type[uint256(i)]);
            entry.set("debit_amount", debit_amount[uint256(i)]);
            entry.set("credit_amount", credit_amount[uint256(i)]);
            

            a.count += table.insert(ids[uint256(i)], entry);
            emit insertAccountingEntryStep1Event(company_address, ids[uint256(i)], certificate_id[uint256(i)],  accounting_title_serial_number[uint256(i)], accounting_title_name[uint256(i)], accounting_entry_type[uint256(i)], debit_amount[uint256(i)], credit_amount[uint256(i)]);

        }

        return a.count;
    }


    // 插入会计分录数组-step2
    // 
    // // 测试数据
    // company_address: address
    // 0x386cc3ec844f46c434c6ba3575abaf46dfc9ce96
    // 
    // ids: ["66", "67", "68"]
    // 
    // actual_certificate_id: ["47", "47", "47"]
    // abstraction: ["找零生成", "", "A"]
    // 
    // spent_accounting_entry_id: ["0", "0", "0"]
    // original_accounting_entry_id: ["0", "0", "0"]
    // 
    // original_document_hash: ["", "0064bb47b3c325b7f6c5add74f907934", "0064bb47b3c325b7f6c5add74f907934"]
    function insert_accounting_entry_step_2(address company_address, string[] ids, string[] actual_certificate_id, string[] abstraction, string[] spent_accounting_entry_id, string[] original_accounting_entry_id, string[] original_document_hash) public returns(int) {
        
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable("accounting_entry");

        string memory company_address_str = addressToString(company_address);


        int count = 0;
        
        for(int i=0; i < int(ids.length); ++i) {


            Entry entry = table.newEntry();
            
            entry.set("actual_certificate_id", actual_certificate_id[uint256(i)]);
            entry.set("abstraction", abstraction[uint256(i)]);
            entry.set("spent_accounting_entry_id", spent_accounting_entry_id[uint256(i)]);
            entry.set("original_accounting_entry_id", original_accounting_entry_id[uint256(i)]);
            
            entry.set("original_document_hash", original_document_hash[uint256(i)]);
            


            Condition condition = table.newCondition();
            condition.EQ("company_address", company_address_str);
            condition.EQ("id", ids[uint256(i)]);


            count += table.update(ids[uint256(i)], entry, condition);
            emit insertAccountingEntryStep2Event(company_address, ids[uint256(i)], actual_certificate_id[uint256(i)], abstraction[uint256(i)], spent_accounting_entry_id[uint256(i)], original_accounting_entry_id[uint256(i)], original_document_hash[uint256(i)]);

        }

        return count;
    }


    // 插入会计分录数组-step3
    // 
    // 测试数据
    // company_address: address
    // 0x386cc3ec844f46c434c6ba3575abaf46dfc9ce96
    // 
    // ids: ["66", "67", "68"]
    // company_id: ["1", "1", "1"]
    // is_unspent: ["0", "1", "0"]
    // spent_timestamp: ["1600653106", "0", "1599980117"]
    // spent_for_certificate_id: ["51", "", "48"]
    // create_timestamp: ["1599980080", "1599980080", "1599980080"]
    function insert_accounting_entry_step_3(address company_address, string[] ids, string[] company_id, string[] is_unspent, string[] spent_timestamp, string[] spent_for_certificate_id, string[] create_timestamp) public returns(int) {
        
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable("accounting_entry");

        string memory company_address_str = addressToString(company_address);


        int count = 0;
        
        for(int i=0; i < int(ids.length); ++i) {


            Entry entry = table.newEntry();
            
            entry.set("company_id", company_id[uint256(i)]);
            entry.set("is_unspent", is_unspent[uint256(i)]);
            entry.set("spent_timestamp", spent_timestamp[uint256(i)]);
            entry.set("spent_for_certificate_id", spent_for_certificate_id[uint256(i)]);
            entry.set("create_timestamp", create_timestamp[uint256(i)]);


            Condition condition = table.newCondition();
            condition.EQ("company_address", company_address_str);
            condition.EQ("id", ids[uint256(i)]);


            count += table.update(ids[uint256(i)], entry, condition);
            emit insertAccountingEntryStep3Event(company_address, ids[uint256(i)], company_id[uint256(i)], is_unspent[uint256(i)], spent_timestamp[uint256(i)], spent_for_certificate_id[uint256(i)], create_timestamp[uint256(i)]);

        }

        return count;
    }





    // 更新会计分录数据
    // 
    // 测试数据
    // company_address: address
    // 0x386cc3ec844f46c434c6ba3575abaf46dfc9ce96
    // 
    // company_id: 1
    // ids: ["67"]
    // is_unspent: 0
    // spent_timestamp: 1599980117
    // spent_for_certificate_id: 100
    function spend_utxo(address company_address, string company_id, string[] ids, string is_unspent, string spent_timestamp, string spent_for_certificate_id) public returns(int) {

        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable("accounting_entry");


        string memory company_address_str = addressToString(company_address);
 

        int count = 0;
        
        for(int i=0; i < int(ids.length); ++i) {

            Condition condition = table.newCondition();
            condition.EQ("company_address", company_address_str);
            condition.EQ("company_id", company_id);
            condition.EQ("id", ids[uint256(i)]);


            Entry entry = table.newEntry();
            entry.set("is_unspent", is_unspent);
            entry.set("spent_timestamp", spent_timestamp);
            entry.set("spent_for_certificate_id", spent_for_certificate_id);

     
            count += table.update(ids[uint256(i)], entry, condition);
            emit spendUtxoEvent(company_address, company_id, ids[uint256(i)], is_unspent, spent_timestamp, spent_for_certificate_id);
            
            
        }
        
        return count;



    }
	
	
	function hashCompareInternal(string a, string b) internal returns (bool) {
	   return keccak256(a) == keccak256(b);
	}
    
    
    function addressToString(address addr) private pure returns(string) {
        // Convert addr to bytes
        bytes20 value = bytes20(uint160(addr));
        bytes memory strBytes = new bytes(42);
        // Encode hex prefix
        strBytes[0] = '0';
        strBytes[1] = 'x';
 
        // Encode bytes usig hex encoding
        for(uint i = 0; i < 20; i++){
            uint8 byteValue = uint8(value[i]);
            strBytes[2 + (i<<1)] = encode(byteValue >> 4);
            strBytes[3 + (i<<1)] = encode(byteValue & 0x0f);
        }
 
        return string(strBytes);
 
 
 
    }
 
 
    function encode(uint8 num) private pure returns(byte){
        // 0-9 -> 0-9
        if(num >= 0 && num <= 9){
            return byte(num + 48);
        }
        // 10-15 -> a-f
        return byte(num + 87);
    }
    
    
    
}

