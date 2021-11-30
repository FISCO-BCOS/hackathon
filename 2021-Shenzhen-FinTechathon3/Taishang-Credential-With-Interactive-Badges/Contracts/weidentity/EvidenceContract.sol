pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

/*
 *       Copyright@ (2018-2020) WeBank Co., Ltd.
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

contract EvidenceContract {

    // block number map, hash as key
    mapping(bytes32 => uint256) changed;
    // hash map, extra id string as key, hash as value
    mapping(string => bytes32) extraKeyMapping;

    // Evidence attribute change event including signature and logs
    event EvidenceAttributeChanged(
        bytes32[] hash,
        address[] signer,
        string[] sigs,
        string[] logs,
        uint256[] updated,
        uint256[] previousBlock
    );
    
    // Additional Evidence attribute change event
    event EvidenceExtraAttributeChanged(
        bytes32[] hash,
        address[] signer,
        string[] keys,
        string[] values,
        uint256[] updated,
        uint256[] previousBlock
    );

    function getLatestRelatedBlock(
        bytes32 hash
    ) 
        public 
        constant 
        returns (uint256) 
    {
        return changed[hash];
    }

    /**
     * Create evidence. Here, hash value is the key; signature and log are values. 
     * This will only create a new evidence if its hash does not exist.
     */
    function createEvidence(
        bytes32[] hash,
        address[] signer,
        string[] sigs,
        string[] logs,
        uint256[] updated
    )
        public
    {
        uint256 sigSize = hash.length;
        bytes32[] memory hashs = new bytes32[](sigSize);
        string[] memory sigss = new string[](sigSize);
        string[] memory logss = new string[](sigSize);
        address[] memory signers = new address[](sigSize);
        uint256[] memory updateds = new uint256[](sigSize);
        uint256[] memory previousBlocks = new uint256[](sigSize);
        uint256 succeededCount = 0;
        for (uint256 i = 0; i < sigSize; i++) {
            bytes32 thisHash = hash[i];
            if (!isHashExist(thisHash)) {
                if (isEqualString(sigs[i], "")) {
                    // this record is a pure log event while hash does not exist, hence skipped
                    continue;
                }
                succeededCount++;
                hashs[i] = thisHash;
                sigss[i] = sigs[i];
                logss[i] = logs[i];
                signers[i] = signer[i];
                updateds[i] = updated[i];
                previousBlocks[i] = changed[thisHash];
                changed[thisHash] = block.number;
            }
        }
        if (succeededCount > 0) {
            emit EvidenceAttributeChanged(hashs, signers, sigss, logss, updateds, previousBlocks);
        }
    }

    /**
     * Add signature and logs to an existing evidence. Here, hash value is the key; signature and log are values. 
     */
    function addSignatureAndLogs(
        bytes32[] hash,
        address[] signer,
        string[] sigs,
        string[] logs,
        uint256[] updated
    )
        public
    {
        uint256 sigSize = hash.length;
        bytes32[] memory hashs = new bytes32[](sigSize);
        string[] memory sigss = new string[](sigSize);
        string[] memory logss = new string[](sigSize);
        address[] memory signers = new address[](sigSize);
        uint256[] memory updateds = new uint256[](sigSize);
        uint256[] memory previousBlocks = new uint256[](sigSize);
        uint256 succeededCount = 0;
        for (uint256 i = 0; i < sigSize; i++) {
            bytes32 thisHash = hash[i];
            if (isHashExist(thisHash)) {
                if (isEqualString(sigs[i], "")) {
                    if (isEqualString(logs[i], "")) {
                        // this record is a pure log event & hash does not exist, hence skipped
                        continue;
                    }
                }
                succeededCount++;
                hashs[i] = thisHash;
                sigss[i] = sigs[i];
                logss[i] = logs[i];
                signers[i] = signer[i];
                updateds[i] = updated[i];
                previousBlocks[i] = changed[thisHash];
                changed[thisHash] = block.number;
            }
        }
        if (succeededCount > 0) {
            emit EvidenceAttributeChanged(hashs, signers, sigss, logss, updateds, previousBlocks);
        }
    }

    /**
     * Create evidence by extra key. As in the normal createEvidence case, this further allocates
     * each evidence with an extra key in String format which caller can fetch as key,
     * to obtain the detailed info from within.
     * This will only create a new evidence if its hash does not exist.
     */
    function createEvidenceWithExtraKey(
        bytes32[] hash,
        address[] signer,
        string[] sigs,
        string[] logs,
        uint256[] updated,
        string[] extraKey
    )
        public
    {
        uint256 sigSize = hash.length;
        bytes32[] memory hashs = new bytes32[](sigSize);
        string[] memory sigss = new string[](sigSize);
        string[] memory logss = new string[](sigSize);
        address[] memory signers = new address[](sigSize);
        uint256[] memory updateds = new uint256[](sigSize);
        uint256[] memory previousBlocks = new uint256[](sigSize);
        uint256 succeededCount = 0;
        for (uint256 i = 0; i < sigSize; i++) {
            bytes32 thisHash = hash[i];
            if (!isHashExist(thisHash)) {
                if (isEqualString(sigs[i], "")) {
                    // this record is a pure log event while hash does not exist, hence skipped
                    continue;
                }
                succeededCount++;
                hashs[i] = thisHash;
                sigss[i] = sigs[i];
                logss[i] = logs[i];
                signers[i] = signer[i];
                updateds[i] = updated[i];
                previousBlocks[i] = changed[thisHash];
                changed[thisHash] = block.number;
                if (!isEqualString(extraKey[i], "")) {
                    extraKeyMapping[extraKey[i]] = thisHash;
                }
            }
        }
        if (succeededCount > 0) {
            emit EvidenceAttributeChanged(hashs, signers, sigss, logss, updateds, previousBlocks);
        }
    }

    /**
     * Create evidence by extra key. As in the normal createEvidence case, this further allocates
     * each evidence with an extra key in String format which caller can fetch as key,
     * to obtain the detailed info from within. This will only emit creation events when an evidence exists.
     */
    function addSignatureAndLogsWithExtraKey(
        bytes32[] hash,
        address[] signer,
        string[] sigs,
        string[] logs,
        uint256[] updated,
        string[] extraKey
    )
        public
    {
        uint256 sigSize = hash.length;
        bytes32[] memory hashs = new bytes32[](sigSize);
        string[] memory sigss = new string[](sigSize);
        string[] memory logss = new string[](sigSize);
        address[] memory signers = new address[](sigSize);
        uint256[] memory updateds = new uint256[](sigSize);
        uint256[] memory previousBlocks = new uint256[](sigSize);
        uint256 succeededCount = 0;
        for (uint256 i = 0; i < sigSize; i++) {
            bytes32 thisHash = hash[i];
            if (isHashExist(thisHash)) {
                if (isEqualString(sigs[i], "")) {
                    if (isEqualString(logs[i], "")) {
                        // this record is a pure log event & hash does not exist, hence skipped
                        continue;
                    }
                }
                succeededCount++;
                hashs[i] = thisHash;
                sigss[i] = sigs[i];
                logss[i] = logs[i];
                signers[i] = signer[i];
                updateds[i] = updated[i];
                previousBlocks[i] = changed[thisHash];
                changed[thisHash] = block.number;
                if (!isEqualString(extraKey[i], "")) {
                    extraKeyMapping[extraKey[i]] = thisHash;
                }
            }
        }
        if (succeededCount > 0) {
            emit EvidenceAttributeChanged(hashs, signers, sigss, logss, updateds, previousBlocks);
        }
    }

    /**
     * Set arbitrary extra attributes to any EXISTING evidence.
     */
    function setAttribute(
        bytes32[] hash,
        address[] signer,
        string[] key,
        string[] value,
        uint256[] updated
    )
        public
    {
        uint256 sigSize = hash.length;
        bytes32[] memory hashs = new bytes32[](sigSize);
        string[] memory keys = new string[](sigSize);
        string[] memory values = new string[](sigSize);
        address[] memory signers = new address[](sigSize);
        uint256[] memory updateds = new uint256[](sigSize);
        uint256[] memory previousBlocks = new uint256[](sigSize);
        for (uint256 i = 0; i < sigSize; i++) {
            bytes32 thisHash = hash[i];
            if (isHashExist(thisHash)) {
                hashs[i] = thisHash;
                keys[i] = key[i];
                values[i] = value[i];
                signers[i] = signer[i];
                updateds[i] = updated[i];
                previousBlocks[i] = changed[thisHash];
                changed[thisHash] = block.number;
            }
        }
        emit EvidenceExtraAttributeChanged(hashs, signers, keys, values, updateds, previousBlocks);
    }

    function isHashExist(bytes32 hash) public view returns (bool) {
        if (changed[hash] != 0) {
            return true;
        }
        return false;
    }

    function getHashByExtraKey(
        string extraKey
    )
        public
        view
        returns (bytes32)
    {
        return extraKeyMapping[extraKey];
    }

    function isEqualString(string a, string b) private pure returns (bool) {	
        if (bytes(a).length != bytes(b).length) {	
            return false;	
        } else {	
            return keccak256(a) == keccak256(b);	
        }	
    }
}