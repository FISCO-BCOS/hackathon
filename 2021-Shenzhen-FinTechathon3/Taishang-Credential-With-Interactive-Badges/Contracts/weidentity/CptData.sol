pragma solidity ^0.4.4;
/*
 *       Copyright© (2018-2019) WeBank Co., Ltd.
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

import "./AuthorityIssuerData.sol";

contract CptData {
    // CPT ID has been categorized into 3 zones: 0 - 999 are reserved for system CPTs,
    //  1000-2000000 for Authority Issuer's CPTs, and the rest for common WeIdentiy DIDs.
    uint constant public AUTHORITY_ISSUER_START_ID = 1000;
    uint constant public NONE_AUTHORITY_ISSUER_START_ID = 2000000;
    uint private authority_issuer_current_id = 1000;
    uint private none_authority_issuer_current_id = 2000000;

    AuthorityIssuerData private authorityIssuerData;

    function CptData(
        address authorityIssuerDataAddress
    ) 
        public
    {
        authorityIssuerData = AuthorityIssuerData(authorityIssuerDataAddress);
    }

    struct Signature {
        uint8 v; 
        bytes32 r; 
        bytes32 s;
    }

    struct Cpt {
        //store the weid address of cpt publisher
        address publisher;
        // [0]: cpt version, [1]: created, [2]: updated, [3]: the CPT ID
        int[8] intArray;
        // [0]: desc
        bytes32[8] bytes32Array;
        //store json schema
        bytes32[128] jsonSchemaArray;
        //store signature
        Signature signature;
    }

    mapping (uint => Cpt) private cptMap;
    uint[] private cptIdList;

    function putCpt(
        uint cptId, 
        address cptPublisher, 
        int[8] cptIntArray, 
        bytes32[8] cptBytes32Array,
        bytes32[128] cptJsonSchemaArray, 
        uint8 cptV, 
        bytes32 cptR, 
        bytes32 cptS
    ) 
        public 
        returns (bool) 
    {
        Signature memory cptSignature = Signature({v: cptV, r: cptR, s: cptS});
        cptMap[cptId] = Cpt({publisher: cptPublisher, intArray: cptIntArray, bytes32Array: cptBytes32Array, jsonSchemaArray:cptJsonSchemaArray, signature: cptSignature});
        cptIdList.push(cptId);
        return true;
    }

    function getCptId(
        address publisher
    ) 
        public 
        constant
        returns 
        (uint cptId)
    {
        if (authorityIssuerData.isAuthorityIssuer(publisher)) {
            while (isCptExist(authority_issuer_current_id)) {
                authority_issuer_current_id++;
            }
            cptId = authority_issuer_current_id++;
            if (cptId >= NONE_AUTHORITY_ISSUER_START_ID) {
                cptId = 0;
            }
        } else {
            while (isCptExist(none_authority_issuer_current_id)) {
                none_authority_issuer_current_id++;
            }
            cptId = none_authority_issuer_current_id++;
        }
    }

    function getCpt(
        uint cptId
    ) 
        public 
        constant 
        returns (
        address publisher, 
        int[8] intArray, 
        bytes32[8] bytes32Array,
        bytes32[128] jsonSchemaArray, 
        uint8 v, 
        bytes32 r, 
        bytes32 s) 
    {
        Cpt memory cpt = cptMap[cptId];
        publisher = cpt.publisher;
        intArray = cpt.intArray;
        bytes32Array = cpt.bytes32Array;
        jsonSchemaArray = cpt.jsonSchemaArray;
        v = cpt.signature.v;
        r = cpt.signature.r;
        s = cpt.signature.s;
    } 

    function getCptPublisher(
        uint cptId
    ) 
        public 
        constant 
        returns (address publisher)
    {
        Cpt memory cpt = cptMap[cptId];
        publisher = cpt.publisher;
    }

    function getCptIntArray(
        uint cptId
    ) 
        public 
        constant 
        returns (int[8] intArray)
    {
        Cpt memory cpt = cptMap[cptId];
        intArray = cpt.intArray;
    }

    function getCptJsonSchemaArray(
        uint cptId
    ) 
        public 
        constant 
        returns (bytes32[128] jsonSchemaArray)
    {
        Cpt memory cpt = cptMap[cptId];
        jsonSchemaArray = cpt.jsonSchemaArray;
    }

    function getCptBytes32Array(
        uint cptId
    ) 
        public 
        constant 
        returns (bytes32[8] bytes32Array)
    {
        Cpt memory cpt = cptMap[cptId];
        bytes32Array = cpt.bytes32Array;
    }

    function getCptSignature(
        uint cptId
    ) 
        public 
        constant 
        returns (uint8 v, bytes32 r, bytes32 s) 
    {
        Cpt memory cpt = cptMap[cptId];
        v = cpt.signature.v;
        r = cpt.signature.r;
        s = cpt.signature.s;
    }

    function isCptExist(
        uint cptId
    ) 
        public 
        constant 
        returns (bool)
    {
        int[8] memory intArray = getCptIntArray(cptId);
        if (intArray[0] != 0) {
            return true;
        } else {
            return false;
        }
    }

    function getDatasetLength() public constant returns (uint) {
        return cptIdList.length;
    }

    function getCptIdFromIndex(uint index) public constant returns (uint) {
        return cptIdList[index];
    }
}