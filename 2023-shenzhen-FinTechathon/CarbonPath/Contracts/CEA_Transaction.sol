// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.4.16 <0.9.0;

/** 
 * @title CEA Transaction 
 * @dev This contract allows users to record transcation information of CEA on the blockchain and then read from the blockchain.

 */

/*
The _txaRecord function now records all details of a transaction and increments the txa_size.
The getTransaction function allows retrieving the details of a transaction by its ID.
The contract limits the number of transactions to 100, as per your array size.
Make sure to test this contract thoroughly in a development environment before deploying it to a live blockchain.
*/
 contract CEA_Transaction{

    // CEA Transcation information
    uint constant txa_limit = 100;
    uint[txa_limit] txa_id;
    string[txa_limit] txa_companyName; 
    uint[txa_limit] txa_transAmount; 
    string[txa_limit] txa_time; // offer time display "yyyy-mm-dd xx:xx"
    uint[txa_limit] txa_price; //should be float; multiply 100 times
    string[txa_limit] txa_contactPerson;
    string[txa_limit] txa_contactWay; //email or ...

    uint txa_size = 0; // total number of transaction information entry uploaded


    /*
    function _txaRecord(string memory companyName, uint transAmount, string memory time, uint price, uint _type, string memory contactPerson, string memory contactWay) public returns (string memory log){
        uint txa_id = txa_size + 1;
        txa_[txa_id] = txa_id;
        txa_companyName[txa_id] = companyName;

        return log;
    }
    */

    function _txaRecord(
        string memory companyName, 
        uint transAmount, 
        string memory time, 
        uint price,  
        string memory contactPerson, 
        string memory contactWay
    ) public returns (string memory log) {
        require(txa_size < txa_limit, "Transaction limit reached");
        uint recordId = txa_size + 1;

        txa_id[recordId] = recordId;
        txa_companyName[recordId] = companyName;
        txa_transAmount[recordId] = transAmount;
        txa_time[recordId] = time;
        txa_price[recordId] = price;
        txa_contactPerson[recordId] = contactPerson;
        txa_contactWay[recordId] = contactWay;

        txa_size++;

        return "Success";
    }

    // Retrieve transaction information by ID
    function getTransaction1(uint id) public view returns ( uint, string memory, uint, string memory, uint) 
    {
        require(id <= txa_size, "Transaction ID does not exist");
        
        return ( 
            txa_id[id], 
            txa_companyName[id], 
            txa_transAmount[id], 
            txa_time[id], 
            txa_price[id]
            
        );
    }
    function getTransaction2(uint id) public view returns ( 
        string memory, string memory) 
    {
        require(id <= txa_size, "Transaction ID does not exist");
        
        return (     
            txa_contactPerson[id], 
            txa_contactWay[id]
        );
    }
            
    function _getSize() public view returns(uint size)
    {
        return txa_size;
    }
    
}