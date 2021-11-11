pragma solidity ^0.4.4;
/*
 *       CopyrightÂ© (2018-2019) WeBank Co., Ltd.
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
 * @title Evidence
 * Evidence contract, created by Factory class.
 */

contract Evidence {
    bytes32[] private dataHash;
    address[] private goversigner;
    address[] private signer;
    bytes32[] private goverr;
    bytes32[] private govers;
    uint8[] private goverv;
    bytes32[] private r;
    bytes32[] private s;
    uint8[] private v;
    bytes32[] private extraContent;

    // Event and Constants.
    uint constant private RETURN_CODE_SUCCESS = 0;
    uint constant private RETURN_CODE_FAILURE_ILLEGAL_INPUT = 500401;
    event AddGovernmentSignatureLog(uint retCode, address goversigner, bytes32 goverr, bytes32 govers, uint8 goverv);
    event AddSignatureLog(uint retCode, address signer, bytes32 r, bytes32 s, uint8 v);
    event AddExtraContentLog(uint retCode, address sender, bytes32 extraContent);
    event AddHashLog(uint retCode, address signer);

    function EvidenceGovernment(
        bytes32[] hashValue,
        address[] GoversignerValue,
        bytes32 goverrValue,
        bytes32 goversValue,
        uint8 govervValue,
        bytes32[] extraValue
    )
        public
    {
        uint numOfHashParts = hashValue.length;
        uint index;
        for (index = 0; index < numOfHashParts; index++) {
            if (hashValue[index] != bytes32(0)) {
                dataHash.push(hashValue[index]);
            }
        }
        uint numOfSigners = GoversignerValue.length;
        for (index = 0; index < numOfSigners; index++) {
            goversigner.push(GoversignerValue[index]);
        }
        // Init signature fields - should always be of the same size as goversigner array
        for (index = 0; index < numOfSigners; index++) {
            if (tx.origin == goversigner[index]) {
                goverr.push(goverrValue);
                govers.push(goversValue);
                goverv.push(govervValue);
            } else {
                goverr.push(bytes32(0));
                govers.push(bytes32(0));
                goverv.push(uint8(0));
            }
        }
        uint numOfExtraValue = extraValue.length;
        for (index = 0; index < numOfExtraValue; index++) {
            extraContent.push(extraValue[index]);
        }
    }


    function Evidence(
        bytes32[] hashValue,
        address[] GoversignerValue,
        address[] signerValue,
        bytes32 goverrValue,
        bytes32 goversValue,
        uint8 govervValue,
        bytes32 rValue,
        bytes32 sValue,
        uint8 vValue,
        bytes32[] extraValue
    )
        public
    {
        uint numOfHashParts = hashValue.length;
        uint index;
        for (index = 0; index < numOfHashParts; index++) {
            if (hashValue[index] != bytes32(0)) {
                dataHash.push(hashValue[index]);
            }
        }
        uint numOfSigners = signerValue.length;
        for (index = 0; index < numOfSigners; index++) {
            signer.push(signerValue[index]);
        }
        uint numOfGovernmentSigners = GoversignerValue.length;
        for (index = 0; index < numOfGovernmentSigners; index++) {
            goversigner.push(GoversignerValue[index]);
        }
        // Init signature fields - should always be of the same size as signer array
        for (index = 0; index < numOfGovernmentSigners; index++) {
            if (tx.origin == signer[index]) {
                goverr.push(goverrValue);
                govers.push(goversValue);
                goverv.push(govervValue);
            } else {
                goverr.push(bytes32(0));
                govers.push(bytes32(0));
                goverv.push(uint8(0));
            }
        }
        
        for (index = 0; index < numOfSigners; index++) {
            if (tx.origin == signer[index]) {
                r.push(rValue);
                s.push(sValue);
                v.push(vValue);
            } else {
                r.push(bytes32(0));
                s.push(bytes32(0));
                v.push(uint8(0));
            }
        }
        uint numOfExtraValue = extraValue.length;
        for (index = 0; index < numOfExtraValue; index++) {
            extraContent.push(extraValue[index]);
        }
    }

    function getGoverInfo()public constant returns(
        bytes32[] hashValue,
        address[] GoversignerValue,
        bytes32[] goverrValue,
        bytes32[] goversValue,
        uint8[] govervValue,
        bytes32[] extraValue
    )
    {
        uint numOfHashParts = dataHash.length;
        uint index;
        hashValue = new bytes32[](numOfHashParts);
        for (index = 0; index < numOfHashParts; index++) {
            hashValue[index] = dataHash[index];
        }
        uint numOfSigners = signer.length;
        GoversignerValue = new address[](numOfSigners);
        for (index = 0; index < numOfSigners; index++) {
            GoversignerValue[index] = signer[index];
        }
        uint numOfSignatures = goverr.length;
        goverrValue = new bytes32[](numOfSignatures);
        goversValue = new bytes32[](numOfSignatures);
        govervValue = new uint8[](numOfSignatures);
        for (index = 0; index < numOfSignatures; index++) {
            goverrValue[index] = goverr[index];
            goversValue[index] = govers[index];
            govervValue[index] = goverv[index];
        }
        uint numOfExtraValue = extraContent.length;
        extraValue = new bytes32[](numOfExtraValue);
        for (index = 0; index < numOfExtraValue; index++) {
            extraValue[index] = extraContent[index];
        }
        return (hashValue, GoversignerValue, goverrValue, goversValue, govervValue, extraValue);
    }

    function getInfo() public constant returns (
        bytes32[] hashValue,
        address[] signerValue,
        bytes32[] rValue,
        bytes32[] sValue,
        uint8[] vValue,
        bytes32[] extraValue
    )
    {
        uint numOfHashParts = dataHash.length;
        uint index;
        hashValue = new bytes32[](numOfHashParts);
        for (index = 0; index < numOfHashParts; index++) {
            hashValue[index] = dataHash[index];
        }
        uint numOfSigners = signer.length;
        signerValue = new address[](numOfSigners);
        for (index = 0; index < numOfSigners; index++) {
            signerValue[index] = signer[index];
        }
        uint numOfSignatures = r.length;
        rValue = new bytes32[](numOfSignatures);
        sValue = new bytes32[](numOfSignatures);
        vValue = new uint8[](numOfSignatures);
        for (index = 0; index < numOfSignatures; index++) {
            rValue[index] = r[index];
            sValue[index] = s[index];
            vValue[index] = v[index];
        }
        uint numOfExtraValue = extraContent.length;
        extraValue = new bytes32[](numOfExtraValue);
        for (index = 0; index < numOfExtraValue; index++) {
            extraValue[index] = extraContent[index];
        }
        return (hashValue, signerValue, rValue, sValue, vValue, extraValue);
    }

    function addGovernmentSignature(
        bytes32 goverrValue,
        bytes32 goversValue,
        uint8 govervValue
    )
        public
        returns (bool)
    {
        uint numOfSigners = goversigner.length;
        for (uint index = 0; index < numOfSigners; index++) {
            if (tx.origin == goversigner[index] && goverv[index] == uint8(0)) {
                goverr[index] = goverrValue;
                govers[index] = goversValue;
                goverv[index] = govervValue;
                AddGovernmentSignatureLog(RETURN_CODE_SUCCESS, tx.origin,  goverrValue,  goversValue,  govervValue);
                return true;
            }
        }
        AddGovernmentSignatureLog(RETURN_CODE_FAILURE_ILLEGAL_INPUT, tx.origin,  goverrValue,  goversValue,  govervValue);
        return false;
    }

    function addSignature(
        bytes32 rValue,
        bytes32 sValue,
        uint8 vValue
    )
        public
        returns (bool)
    {
        uint numOfSigners = signer.length;
        for (uint index = 0; index < numOfSigners; index++) {
            if (tx.origin == signer[index] && v[index] == uint8(0)) {
                r[index] = rValue;
                s[index] = sValue;
                v[index] = vValue;
                AddSignatureLog(RETURN_CODE_SUCCESS, tx.origin, rValue, sValue, vValue);
                return true;
            }
        }
        AddSignatureLog(RETURN_CODE_FAILURE_ILLEGAL_INPUT, tx.origin, rValue, sValue, vValue);
        return false;
    }

    function setHash(bytes32[] hashArray) public {
        uint numOfSigners = signer.length;
        for (uint index = 0; index < numOfSigners; index++) {
            if (tx.origin == signer[index]) {
                dataHash = new bytes32[](hashArray.length);
                for (uint i = 0; i < hashArray.length; i++) {
                    dataHash[i] = hashArray[i];
                }
                AddHashLog(RETURN_CODE_SUCCESS, tx.origin);
                return;
            }
        }
        AddHashLog(RETURN_CODE_FAILURE_ILLEGAL_INPUT, tx.origin);
        return;
    }

    function addExtraValue(bytes32 extraValue) public returns (bool) {
        uint numOfSigners = signer.length;
        for (uint index = 0; index < numOfSigners; index++) {
            if (tx.origin == signer[index]) {
                extraContent.push(extraValue);
                AddExtraContentLog(RETURN_CODE_SUCCESS, tx.origin, extraValue);
                return true;
            }
        }
        AddExtraContentLog(RETURN_CODE_FAILURE_ILLEGAL_INPUT, tx.origin, extraValue);
        return false;
    }
}
