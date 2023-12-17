// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.4.16 <0.9.0;

/** 
 * @title CCER historical transaction records
 * @dev This contract records CCER history transaction records.
 */

 contract CCER_History{
     uint constant htxr_limit = 100;
    // Historical Transaction Information arrays
    uint [htxr_limit] htxr_id;          // Array of transaction IDs
    string [htxr_limit] htxr_seller;    // Array of seller names
    string [htxr_limit] htxr_buyer;     // Array of buyer names
    uint[htxr_limit] htxr_amount;       // Array of transaction amounts (should be float; multiply 100 times)
    uint[htxr_limit] htxr_price;        // Array of transaction prices (should be float; multiply 100 times)
    string [htxr_limit] htxr_time;      // Array of transaction times in format: "YYYY-MM-DD"
    uint htxr_size = 0;                 // Total number of Historical Transaction Information entries uploaded

function htxrRecord(
        string memory _seller,
        string memory _buyer,
        uint _amount,
        uint _price,
        string memory _time
    ) public returns (string memory log) {
        require(htxr_size < htxr_limit, "Historical Transaction Information limit reached.");
        uint currentId = htxr_size + 1;
        htxr_id[currentId] = currentId;
        htxr_seller[currentId] = _seller;
        htxr_buyer[currentId] = _buyer;
        htxr_amount[currentId] = _amount;
        htxr_price[currentId] = _price;
        htxr_time[currentId] = _time;
        
        htxr_size = htxr_size + 1;
        
        return "Success";
    }
    function getHtxr(uint _id) public view returns (string memory seller, string memory buyer, uint amount, uint price, string memory time) {
        for (uint i = 1; i <= htxr_size; i++) {
            if (htxr_id[i] == _id) {
                return (htxr_seller[i], htxr_buyer[i], htxr_amount[i], htxr_price[i], htxr_time[i]);
            }
        }
        revert("Historical Transaction Information not found.");
    }
    
    function _getSize() public view returns(uint size)
    {
        return htxr_size;
    }



 }