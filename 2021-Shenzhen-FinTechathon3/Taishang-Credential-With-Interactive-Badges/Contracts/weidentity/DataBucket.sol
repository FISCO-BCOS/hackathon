pragma solidity ^0.4.4;
pragma experimental ABIEncoderV2;

/*
 *       Copyright© (2018-2020) WeBank Co., Ltd.
 *
 *       This file is part of weidentity-contract.
 *
 *       weidentity-contract is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weidentity-contract is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weidentity-contract.  If not, see <https://www.gnu.org/licenses/>.
 */
contract DataBucket {
    
    string[] bucketIdList;      // all bucketId
    
    struct DataStruct {
        string bucketId;         // the bucketId
        address owner;        // owner for bucket
        address[] useAddress; // the user list for use this bucket
        bool isUsed;           // the bucketId is be useed
        uint256 index;        // the bucketId index in bucketIdList
        uint256 timestamp;    // the first time for create bucketId
        mapping(bytes32 => string) extra; //the mapping for store the key--value
    }
    
    mapping(string => DataStruct) bucketData; // bucketId-->DataStruct
    
    address owner;
    
    uint8 constant private SUCCESS = 100;
    uint8 constant private NO_PERMISSION = 101;
    uint8 constant private THE_BUCKET_DOES_NOT_EXIST = 102;
    uint8 constant private THE_BUCKET_IS_USED = 103;
    uint8 constant private THE_BUCKET_IS_NOT_USED = 104;
    
    function DataBucket() public {
        owner = msg.sender;
    }
    
    /**
     * put the key-value into hashData.
     * 
     * @param bucketId the bucketId
     * @param key the store key
     * @param value the value of the key
     * @return code the code for result
     */ 
    function put(
        string bucketId, 
        bytes32 key, 
        string value
    ) 
        public 
        returns (uint8 code) 
    {
        DataStruct storage data = bucketData[bucketId];
        //the first put bucketId
        if (data.owner == address(0x0)) {
            data.bucketId = bucketId;
            data.owner = msg.sender;
            data.timestamp = now;
            pushBucketId(data);
            data.extra[key] = value;
            return SUCCESS;
        } else {
            // no permission
            if (data.owner != msg.sender) {
                 return NO_PERMISSION;
            }
            data.extra[key] = value;
            return SUCCESS;
        }
    }
    
    /**
     * push bucketId into hashList.
     * 
     * @param data the data for bucket
     * 
     */ 
    function pushBucketId(
        DataStruct storage data
    ) 
        internal 
    {
        // find the first empty index.
        int8 emptyIndex = -1;
        for (uint8 i = 0; i < bucketIdList.length; i++) {
            if (isEqualString(bucketIdList[i], "")) {
                emptyIndex = int8(i);
                break;
            }
        }
        // can not find the empty index, push data to last
        if (emptyIndex == -1) {
            bucketIdList.push(data.bucketId);
            data.index = bucketIdList.length - 1;
        } else {
            // push data by index
            uint8 index = uint8(emptyIndex);
            bucketIdList[index] = data.bucketId;
            data.index = index;
        }
    }
    
    /**
     * get value by key in the bucket data.
     * 
     * @param bucketId the bucketId
     * @param key get the value by this key
     * @return value the value
     */ 
    function get(
        string bucketId, 
        bytes32 key
    ) 
        public view
        returns (uint8 code, string value) 
    {
        DataStruct storage data = bucketData[bucketId];
        if (data.owner == address(0x0)) {
            return (THE_BUCKET_DOES_NOT_EXIST, "");
        }
        return (SUCCESS, data.extra[key]);
    }
    
    /**
     * remove bucket when the key is null, others remove the key
     * 
     * @param bucketId the bucketId
     * @param key the key
     * @return the code for result
     */ 
    function removeExtraItem(
        string bucketId, 
        bytes32 key
    ) 
        public 
        returns (uint8 code) 
    {
        DataStruct memory data = bucketData[bucketId];
        if (data.owner == address(0x0)) {
            return THE_BUCKET_DOES_NOT_EXIST;
        } else if (msg.sender != data.owner) {
            return NO_PERMISSION;
        } else if (data.isUsed) {
            return THE_BUCKET_IS_USED;
        } else {
           delete bucketData[bucketId].extra[key];
           return SUCCESS;
        }
    }
    
    /**
     * remove bucket when the key is null, others remove the key
     * 
     * @param bucketId the bucketId
     * @param force force delete
     * @return the code for result
     */ 
    function removeDataBucketItem(
        string bucketId,
        bool force
    ) 
        public 
        returns (uint8 code) 
    {
        DataStruct memory data = bucketData[bucketId];
        if (data.owner == address(0x0)) {
            return THE_BUCKET_DOES_NOT_EXIST;
        } else if (msg.sender == owner && force) {
            delete bucketIdList[data.index];
            delete bucketData[bucketId];
            return SUCCESS;
        } else if (msg.sender != data.owner) {
            return NO_PERMISSION;
        } else if (data.isUsed) {
            return THE_BUCKET_IS_USED;
        } else {
            delete bucketIdList[data.index];
            delete bucketData[bucketId];
            return SUCCESS;
        }
    }
    
    /**
     * enable the bucket.
     * @param bucketId the bucketId
     */
    function enable(
        string bucketId
    ) 
        public 
        returns (uint8) 
    {
        DataStruct storage data = bucketData[bucketId];
        if (data.owner == address(0x0)) {
            return THE_BUCKET_DOES_NOT_EXIST;
        }
        
        if (!data.isUsed) {
            data.isUsed = true;
        }
        pushUseAddress(data);
        return SUCCESS;
    }
    
    /**
     * push the user into useAddress.
     */ 
    function pushUseAddress(
        DataStruct storage data
    ) 
        internal 
    {
        int8 emptyIndex = -1;
        for (uint8 i = 0; i < data.useAddress.length; i++) {
            if (data.useAddress[i] == msg.sender) {
                return;
            } 
            if (emptyIndex == -1 && data.useAddress[i] == address(0x0)) {
                emptyIndex = int8(i);
            }
        }
        if (emptyIndex == -1) {
            data.useAddress.push(msg.sender);
        } else {
            data.useAddress[uint8(emptyIndex)] = msg.sender;
        }
    }
    
    /**
     * remove the use Address from DataStruct.
     */ 
    function removeUseAddress(
        DataStruct storage data
    ) 
        internal 
    {
        uint8 index = 0;
        for (uint8 i = 0; i < data.useAddress.length; i++) {
            if (data.useAddress[i] == msg.sender) {
                index = i;
                break;
            }
        }
        delete data.useAddress[index];
    }
    
    /**
     * true is THE_BUCKET_IS_USED, false THE_BUCKET_IS_NOT_USED.
     */
    function hasUse(
        DataStruct storage data
    ) 
        internal 
        view 
        returns (bool)
    {
        for (uint8 i = 0; i < data.useAddress.length; i++) {
            if (data.useAddress[i] != address(0x0)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * disable the bucket
     * @param bucketId the bucketId
     */
    function disable(
        string bucketId
    ) 
        public 
        returns (uint8) 
    {
        DataStruct storage data = bucketData[bucketId];
        if (data.owner == address(0x0)) {
            return THE_BUCKET_DOES_NOT_EXIST;
        }
        if (!data.isUsed) {
            return THE_BUCKET_IS_NOT_USED;
        }
        removeUseAddress(data);
        data.isUsed = hasUse(data);
        return SUCCESS;
    }
    
    /**
     * get all bucket by page.
     */ 
    function getAllBucket(
        uint8 index, 
        uint8 num
    ) 
        public 
        view
        returns (string[] bucketIds, address[] owners, uint256[] timestamps, uint8 nextIndex) 
    {
        bucketIds = new string[](num);
        owners = new address[](num);
        timestamps = new uint256[](num);
        uint8 currentIndex = 0;
        uint8 next = 0;
        for (uint8 i = index; i < bucketIdList.length; i++) {
            string storage bucketId = bucketIdList[i];
            if (!isEqualString(bucketId, "")) {
                DataStruct memory data = bucketData[bucketId];
                bucketIds[currentIndex] = bucketId;
                owners[currentIndex] = data.owner;
                timestamps[currentIndex] = data.timestamp;
                currentIndex++;
                if (currentIndex == num && i != bucketIdList.length - 1) {
                    next = i + 1;
                    break;
                }
            }
        }
        return (bucketIds, owners, timestamps, next);
    }
    
    function isEqualString(
        string a, 
        string b
    ) 
        private 
        constant 
        returns (bool) 
    {
        if (bytes(a).length != bytes(b).length) {
            return false;
        } else {
            return keccak256(a) == keccak256(b);
        }
    }
    
    /**
     * update the owner of bucket
     */
    function updateBucketOwner(
        string bucketId,
        address newOwner
    ) 
        public 
        returns (uint8) 
    {
        // check the bucketId is exist
        DataStruct storage data = bucketData[bucketId];
        if (data.owner == address(0x0)) {
            return THE_BUCKET_DOES_NOT_EXIST;
        }
        
        // check the owner
        if (msg.sender != owner) {
            return NO_PERMISSION;
        }
        
        if (newOwner != address(0x0)) {
            data.owner = newOwner;
        }
        return SUCCESS;
    }

    /**
     * get use address by bucketId.
     * @param bucketId the bucketId
     * @param index query start index
     * @param num query count
     */ 
    function getActivatedUserList(
        string bucketId,
        uint8 index, 
        uint8 num
    ) 
        public 
        view
        returns (address[] users, uint8 nextIndex) 
    {
        users = new address[](num);
        uint8 userIndex = 0;
        uint8 next = 0;
        DataStruct memory data = bucketData[bucketId];
        for (uint8 i = index; i < data.useAddress.length; i++) {
            address user = data.useAddress[i];
            if (user != address(0x0)) {
                users[userIndex] = user;
                userIndex++;
                if (userIndex == num && i != data.useAddress.length - 1) {
                    next = i + 1;
                    break;
                }
            }
        }
        return (users, next);
    }
}