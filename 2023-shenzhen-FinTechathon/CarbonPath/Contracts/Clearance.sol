// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.4.16 <0.9.0;
/** 
 * @title CEA historical transaction records and CEA Quota Clearance
 * @dev This contract records CEA history transaction records and it allows users to record transcation information of CEA on the blockchain and then read from the blockchain.
 */

contract Clearance{
    uint constant htxa_limit = 100;
    // Historical Transaction Information arrays
    uint [htxa_limit] htxa_id;          // Array of transaction IDs
    string [htxa_limit] htxa_seller;    // Array of seller names
    string [htxa_limit] htxa_buyer;     // Array of buyer names
    uint[htxa_limit] htxa_amount;       // Array of transaction amounts (should be float; multiply 100 times)
    uint[htxa_limit] htxa_price;        // Array of transaction prices (should be float; multiply 100 times)
    string [htxa_limit] htxa_time;      // Array of transaction times in format: "YYYY-MM-DD"
    uint htxa_size = 0;                 // Total number of Historical Transaction Information entries uploaded

    // Clearance Information
    uint constant cl_Num = 13;          // 12 months in total, extra space as buffer
    string [cl_Num] cl_companyName;     // Array of company names for clearance
    string[cl_Num] cl_month;            // Array of months for clearance
    uint[cl_Num] cl_buyAmount;          // Array of buy amounts for each month (should be float; multiply 100 times)
    uint[cl_Num] cl_sellAmount;         // Array of sell amounts for each month (should be float; multiply 100 times)




    // Record Historical Transaction Information through variables
    // Return value is a string, "Success" for success, "Failure" for failure
    /**
     * @dev Record Historical Transaction Information through variables
     * @param _seller The seller's name
     * @param _buyer The buyer's name
     * @param _amount The transaction amount (should be multiplied by 100)
     * @param _price The transaction price (should be multiplied by 100)
     * @param _time The transaction time (format: "2023-12-16")
     * @return log A string indicating the success or failure of the operation
     */
    function htxaRecord(
        string memory _seller,
        string memory _buyer,
        uint _amount,
        uint _price,
        string memory _time
    ) public returns (string memory log) {
        require(htxa_size < htxa_limit, "Historical Transaction Information limit reached.");
        uint currentId = htxa_size + 1;
        htxa_id[currentId] = currentId;
        htxa_seller[currentId] = _seller;
        htxa_buyer[currentId] = _buyer;
        htxa_amount[currentId] = _amount;
        htxa_price[currentId] = _price;
        htxa_time[currentId] = _time;
        
        htxa_size = htxa_size + 1;
        
        return "Success";
    }

   /**
    * @dev Get Historical Transaction Information by htxa_id
    * @param _id The ID of the historical transaction to retrieve
    * @return seller The seller's name
    * @return buyer The buyer's name
    * @return amount The transaction amount (should be multiplied by 100)
    * @return price The transaction price (should be multiplied by 100)
    * @return time The transaction time (format: "2023-12-16")
    */
    // Get Historical Transaction Information by htxa_id
    function getHtxa(uint _id) public view returns (string memory seller, string memory buyer, uint amount, uint price, string memory time) {
        for (uint i = 1; i <= htxa_size; i++) {
            if (htxa_id[i] == _id) {
                return (htxa_seller[i], htxa_buyer[i], htxa_amount[i], htxa_price[i], htxa_time[i]);
            }
        }
        revert("Historical Transaction Information not found.");
    }
        function _getSize() public view returns(uint size)
    {
        return htxa_size;
    }

    
    /**
     * @dev Get the total sell amount for a specific company in each month
     * @param companyName The name of the company to retrieve sell amounts for
     * @return sellAmounts An array of 12 uint values representing the total sell amount for each month
    */
// _getcl function to calculate the total sell amount per month for a given company
    function _getclSold(string memory companyName) public view returns (uint[cl_Num] memory) 
    {
        uint[cl_Num] memory totalSellAmount;
    
        for (uint i = 1; i <= htxa_size; i++) 
        {
            if (keccak256(abi.encodePacked(htxa_seller[i])) == keccak256(abi.encodePacked(companyName))) 
            {
                string memory transactionMonth = _extractMonth(htxa_time[i]);
                uint monthIndex = _monthStringToInt(transactionMonth);
                if (monthIndex > 0 && monthIndex <= 12) {
                    totalSellAmount[monthIndex - 1] += htxa_amount[i];
                }
            }
        }

        return totalSellAmount;
    }

    /**
     * @dev Calculate and retrieve the total buy amount for a specific company in each month
     * @param companyName The name of the company
     * @return totalBuyAmount Array of total buy amounts for each month
     */
    function _getclBought(string memory companyName) public view returns (uint[cl_Num] memory) {
    uint[cl_Num] memory totalBuyAmount;

    for (uint i = 1; i <= htxa_size; i++) {
        if (keccak256(abi.encodePacked(htxa_buyer[i])) == keccak256(abi.encodePacked(companyName))) {
            // 提取交易时间的月份部分
            string memory transactionMonth = _extractMonth(htxa_time[i]);
            // 将月份字符串转换为整数
            uint monthIndex = _monthStringToInt(transactionMonth);
            if (monthIndex > 0 && monthIndex <= 12) {
                totalBuyAmount[monthIndex - 1] += htxa_amount[i];
            }
        }
    }
    return totalBuyAmount;
}


    // Helper function to extract the month from a date string
    function _extractMonth(string memory date) private pure returns (string memory) {
        bytes memory dateString = bytes(date);
        if (dateString.length >= 7) {
            return string(abi.encodePacked(dateString[5], dateString[6]));
        }
        return "";
    }

// Utility functions
// Helper function to convert month string to integer
    function _monthStringToInt(string memory monthString) private pure returns (uint) {
        bytes memory monthBytes = bytes(monthString);
        if (monthBytes.length != 2) {
            return 0;
        }

    uint8 tens = uint8(monthBytes[0]) - 48; // ASCII value of '0' is 48
    uint8 ones = uint8(monthBytes[1]) - 48;

    return tens * 10 + ones;
    }
}