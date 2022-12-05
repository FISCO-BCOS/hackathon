pragma solidity ^0.4.25;

import "./Table.sol";

contract HashUpload {
    // Events.
    event UploadHash(int256 ret, string hash_id, string hash_value);
    event ModifyHash(int256 ret, string hash_id, string hash_value);

    constructor() public {
        // In the construtor, we construct the hash table.
        createTable();
    }

    function createTable() private {
        TableFactory tf = TableFactory(0x1001);
        /*
            The hash tabele, key: hash_id, field: hash_value.
         */
        tf.createTable("hash_table", "hash_id", "hash_value");
    }

    function openTable() private returns (Table) {
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable("hash_table");
        return table;
    }

    /*
    Description: Query the hash value according to the hash id.
    Param:
        hash_id: The id for the specific hash.
    Returns:
        Param 1: 0 represents success, and -1 represents failure.
        Param 2: The hash value, it is valid when the first parameter is 0.
    */

    function selectHash(string memory hash_id)
        public
        constant
        returns (int256, string memory)
    {
        // Open the table
        Table table = openTable();
        // Query the hash value.
        Entries entries = table.select(hash_id, table.newCondition());
        string memory hash_value = "";
        if (0 == uint256(entries.size())) {
            return (-1, hash_value);
        } else {
            Entry entry = entries.get(0);
            return (0, entry.getString("hash_value"));
        }
    }

    /*
    Description: Upload the hash_id and the corresponding value to the table.
    Param:
        hash_id: The id for the specific hash.
        hash_value: The corresponding value.
    Returns:
        Param 1: The status code.
     */

    function uploadHash(string memory hash_id, string memory hash_value)
        public
        returns (int256)
    {
        int256 ret_code = 0;
        int256 ret = 0;
        string memory temp_hash_value = "";

        // Check whether the hash_id is existed.
        (ret, temp_hash_value) = selectHash(hash_id);
        if (ret != 0) {
            // The hash_id doesn't exists.
            Table table = openTable();

            Entry entry = table.newEntry();
            entry.set("hash_id", hash_id);
            entry.set("hash_value", hash_value);
            // Insert the data.
            int256 count = table.insert(hash_id, entry);
            if (count == 1) {
                // Success
                ret_code = 0;
            } else {
                // Failure, without permission or other failures.
                ret_code = -2;
            }
        } else {
            // The hash_id is already existed.
            ret_code = -1;
        }

        emit UploadHash(ret_code, hash_id, hash_value);
        return ret_code;
    }

    /*
    Description: Modify the hash value according to the hash_id.
    Param:
        hash_id: The id for the specific hash.
        hash_value: The corresponding value.
    Returns:
        Param 1: The status code.
     */

    function modifyHash(string memory hash_id, string memory hash_value)
        public
        returns (int256)
    {
        int256 ret_code = 0;
        int256 ret = 0;
        string memory temp_hash_value = "";

        // Check whether the hash_id is existed.
        (ret, temp_hash_value) = selectHash(hash_id);
        if (ret != 0) {
            // The hash_id doesn't exist.
            ret_code = -1;
            emit ModifyHash(ret, hash_id, hash_value);
            return ret_code;
        }

        Table table = openTable();

        Entry entry = table.newEntry();
        entry.set("hash_id", hash_id);
        entry.set("hash_value", hash_value);

        // Update the hash table.
        int256 count = table.update(hash_id, entry, table.newCondition());
        if (count != 1) {
            // Failure, without permission or other failures.
            ret_code = -2;
            emit ModifyHash(ret, hash_id, hash_value);

            return ret_code;
        }
        
        emit ModifyHash(ret, hash_id, hash_value);

        return ret_code;
    }
}
