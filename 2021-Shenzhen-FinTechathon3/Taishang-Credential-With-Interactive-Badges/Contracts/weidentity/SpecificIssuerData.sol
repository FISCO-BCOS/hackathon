pragma solidity ^0.4.4;
/*
 *       Copyright© (2019) WeBank Co., Ltd.
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

/**
 * @title SpecificIssuerData
 * Stores data about issuers with specific types.
 */

contract SpecificIssuerData {

    // Error codes
    uint constant private RETURN_CODE_SUCCESS = 0;
    uint constant private RETURN_CODE_FAILURE_ALREADY_EXISTS = 500501;
    uint constant private RETURN_CODE_FAILURE_NOT_EXIST = 500502;
    uint constant private RETURN_CODE_FAILURE_EXCEED_MAX = 500503;

    struct IssuerType {
        // typeName as index, dynamic array as getAt function and mapping as search
        bytes32 typeName;
        address[] fellow;
        mapping (address => bool) isFellow;
        bytes32[8] extra;
    }

    mapping (bytes32 => IssuerType) private issuerTypeMap;

    function registerIssuerType(bytes32 typeName) public returns (uint) {
        if (isIssuerTypeExist(typeName)) {
            return RETURN_CODE_FAILURE_ALREADY_EXISTS;
        }
        address[] memory fellow;
        bytes32[8] memory extra;
        IssuerType memory issuerType = IssuerType(typeName, fellow, extra);
        issuerTypeMap[typeName] = issuerType;
        return RETURN_CODE_SUCCESS;
    }

    function addExtraValue(bytes32 typeName, bytes32 extraValue) public returns (uint) {
        if (!isIssuerTypeExist(typeName)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        IssuerType issuerType = issuerTypeMap[typeName];
        for (uint index = 0; index < 8; index++) {
            if (issuerType.extra[index] == bytes32(0)) {
                issuerType.extra[index] = extraValue;
                break;
            }
        }
        if (index == 8) {
            return RETURN_CODE_FAILURE_EXCEED_MAX;
        }
        return RETURN_CODE_SUCCESS;
    }

    function getExtraValue(bytes32 typeName) public constant returns (bytes32[8]) {
        bytes32[8] memory extraValues;
        if (!isIssuerTypeExist(typeName)) {
            return extraValues;
        }
        IssuerType issuerType = issuerTypeMap[typeName];
        for (uint index = 0; index < 8; index++) {
            extraValues[index] = issuerType.extra[index];
        }
        return extraValues;
    }

    function isIssuerTypeExist(bytes32 name) public constant returns (bool) {
        if (issuerTypeMap[name].typeName == bytes32(0)) {
            return false;
        }
        return true;
    }

    function addIssuer(bytes32 typeName, address addr) public returns (uint) {
        if (isSpecificTypeIssuer(typeName, addr)) {
            return RETURN_CODE_FAILURE_ALREADY_EXISTS;
        }
        if (!isIssuerTypeExist(typeName)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        issuerTypeMap[typeName].fellow.push(addr);
        issuerTypeMap[typeName].isFellow[addr] = true;
        return RETURN_CODE_SUCCESS;
    }

    function removeIssuer(bytes32 typeName, address addr) public returns (uint) {
        if (!isSpecificTypeIssuer(typeName, addr) || !isIssuerTypeExist(typeName)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        address[] memory fellow = issuerTypeMap[typeName].fellow;
        uint dataLength = fellow.length;
        for (uint index = 0; index < dataLength; index++) {
            if (addr == fellow[index]) {
                break;
            }
        }
        if (index != dataLength-1) {
            issuerTypeMap[typeName].fellow[index] = issuerTypeMap[typeName].fellow[dataLength-1];
        }
        delete issuerTypeMap[typeName].fellow[dataLength-1];
        issuerTypeMap[typeName].fellow.length--;
        issuerTypeMap[typeName].isFellow[addr] = false;
        return RETURN_CODE_SUCCESS;
    }

    function isSpecificTypeIssuer(bytes32 typeName, address addr) public constant returns (bool) {
        if (issuerTypeMap[typeName].isFellow[addr] == false) {
            return false;
        }
        return true;
    }

    function getSpecificTypeIssuers(bytes32 typeName, uint startPos) public constant returns (address[50]) {
        address[50] memory fellow;
        if (!isIssuerTypeExist(typeName)) {
            return fellow;
        }

        // Calculate actual dataLength via batch return for better perf
        uint totalLength = getSpecificTypeIssuerLength(typeName);
        uint dataLength;
        if (totalLength < startPos) {
            return fellow;
        } else if (totalLength <= startPos + 50) {
            dataLength = totalLength - startPos;
        } else {
            dataLength = 50;
        }

        // dynamic -> static array data copy
        for (uint index = 0; index < dataLength; index++) {
            fellow[index] = issuerTypeMap[typeName].fellow[index + startPos];
        }
        return fellow;
    }

    function getSpecificTypeIssuerLength(bytes32 typeName) public constant returns (uint) {
        if (!isIssuerTypeExist(typeName)) {
            return 0;
        }
        return issuerTypeMap[typeName].fellow.length;
    }
}