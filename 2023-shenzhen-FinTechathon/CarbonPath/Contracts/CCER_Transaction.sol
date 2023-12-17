// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.4.16 <0.9.0;
/** 
 * @title CCER Transaction 
 * @dev This contract allows users to record transcation information of CCER on the blockchain and then read from the blockchain.

 */

  contract CCER_Transaction{

    // CCER Transcation information
    uint constant txr_limit = 100;
    uint[txr_limit] txr_id; 
    string[txr_limit] txr_displayTime;
    uint[txr_limit] txr_price; //should be float; multiply 100 times
    string[txr_limit] txr_contactPerson;
    string[txr_limit] txr_contactWay; //email or ...
    string[txr_limit] txr_name;
    string[txr_limit] txr_status;
    string[txr_limit] txr_companyName;
    string[txr_limit] txr_date1;
    uint[txr_limit] txr_reductionExpected;
    string[txr_limit] txr_description;
    string[txr_limit] txr_file1;
    string[txr_limit] txr_file2;

    uint txr_size = 0; // total number of transaction information entry uploaded
    uint idPointer = 0;

   // First part of recording transaction
    function _txrRecord1(
        string memory displayTime,
        uint price,
        string memory contactPerson,
        string memory contactWay,
        string memory name,
        string memory status
    ) public returns (uint) {
        require(txr_size < txr_limit, "Transaction limit reached");
        uint recordId;
        recordId = txr_size + 1;
        txr_size++;

        txr_id[recordId] = recordId;
        txr_displayTime[recordId] = displayTime;
        txr_price[recordId] = price;
        txr_contactPerson[recordId] = contactPerson;
        txr_contactWay[recordId] = contactWay;
        txr_name[recordId] = name;
        txr_status[recordId] = status;
        
        idPointer = recordId; // idPointer active now

        return idPointer;

        
    }

    // Second part of recording transaction, must be called right after function _txrRecord1

    function _txrRecord2(

        string memory companyName,
        string memory date1,
        uint reductionExpected,
        string memory description,
        string memory file1,
        string memory file2
    ) public {
        require(idPointer !=  0, "Invalid record ID");
        uint recordId;
        recordId = idPointer;
        txr_companyName[recordId] = companyName;
        txr_date1[recordId] = date1;
        txr_reductionExpected[recordId] = reductionExpected;
        txr_description[recordId] = description;
        txr_file1[recordId] = file1;
        txr_file2[recordId] = file2;

        
        idPointer = 0; // idPointer invalid now
    }


function getTransaction1(uint id) public view returns (
        uint, string memory, uint, string memory, string memory
    ) {
        require(id <= txr_size, "Transaction ID does not exist");
        
        return (
            txr_id[id],
            txr_displayTime[id],
            txr_price[id],
            txr_contactPerson[id],
            txr_contactWay[id]
        );
    }

    function getTransaction2(uint id) public view returns (
        string memory, string memory, string memory, string memory, uint, string memory
    ) {
        require(id <= txr_size, "Transaction ID does not exist");
        
        return (
            txr_name[id],
            txr_status[id],
            txr_companyName[id],
            txr_date1[id],
            txr_reductionExpected[id],
            txr_description[id]
        );
    }
    
   function getTransaction3(uint id) public view returns (
        string memory, string memory
    ) {
        require(id <= txr_size, "Transaction ID does not exist");
        
        return (
            txr_file1[id],
            txr_file2[id]
        );
    }
    
    function _getSize() public view returns(uint size)
    {
        return txr_size;
    }

}