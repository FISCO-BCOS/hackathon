// SPDX-License-Identifier: Apache-2.0
pragma solidity >=0.6.10 <=0.8.26;
pragma experimental ABIEncoderV2;

import {EntityTable} from "./EntityTable.sol";
import {ConsumeTable} from "./ConsumeTable.sol";
import {APISampleOracle} from "lib/Truora-Service/contracts/1.0/sol-0.6/oracle/APISampleOracle.sol";

contract AdminOps {
    EntityTable et;
    ConsumeTable ct;
    APISampleOracle private oracle;
    bytes32 private requestId;

    constructor(address oracleAddr) {
        et = new EntityTable();
        ct = new ConsumeTable();
        oracle = APISampleOracle(oracleAddr);
    }

    function addEntity(string memory id, string memory name, EntityTable.EntityType entity_type, string memory entity_address) public {
        et.insert(id, name, entity_type, entity_address);
    }

    function getEntity(string memory id) public view returns (string memory, string memory, string memory, string memory) {
        return et.select(id);
    }

    function addConsumeInfo() public returns(int32) {
        requestId = oracle.request();
        require(oracle.checkIdFulfilled(requestId) == false, " oracle query has not been fulfilled!");
        (
            string memory id,
            uint256 amount, 
            string memory consumer_id, 
            string memory platform_id
        ) = oracle.getById(requestId);

        result = ct.insert(id, amount, consumer_id, platform_id);
        return result;
    }

    function getConsumeInfo(string memory id) public view returns (string memory, string memory, string memory, string memory) {
        return ct.select(id);
    }
}