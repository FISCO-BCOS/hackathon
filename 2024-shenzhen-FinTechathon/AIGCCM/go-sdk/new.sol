// SPDX-License-Identifier: MIT
pragma solidity ^0.4.24;
pragma experimental ABIEncoderV2;

contract WhiteList {
    struct Item {
        string publicKey;
        bool authorized;
    }

    Item[] public items;

    function addItemToWhiteList(string memory publicKey) public {
        items.push(Item(publicKey, true));
    }

    function findItemInArray(
        string memory publicKey
    ) public view returns (Item memory) {
        for (uint256 i = 0; i < items.length; i++) {
            if (
                keccak256(abi.encodePacked(items[i].publicKey)) ==
                keccak256(abi.encodePacked(publicKey))
            ) {
                return items[i];
            }
        }
        revert("Item not found");
    }

    function modifyWhiteList(string memory publicKey) public {
        for (uint256 i = 0; i < items.length; i++) {
            if (
                keccak256(abi.encodePacked(items[i].publicKey)) ==
                keccak256(abi.encodePacked(publicKey))
            ) {
                items[i].authorized = !items[i].authorized;
                return;
            }
        }
        revert("Item not found");
    }

    function deleteItemFromWhiteList(string memory publicKey) public {
        for (uint256 i = 0; i < items.length; i++) {
            if (
                keccak256(abi.encodePacked(items[i].publicKey)) ==
                keccak256(abi.encodePacked(publicKey))
            ) {
                items[i] = items[items.length - 1];
                delete items[items.length - 1];
                return;
            }
        }
        revert("Item not found");
    }
}

contract JenkinsHash {
    function hash32(bytes memory msgData) public pure returns (uint32) {
        uint32 c;
        uint32 b;
        (c, b) = lookup3(msgData, 0x0, 0x0);
        return c;
    }

    function hash64(bytes memory msgData) public pure returns (bytes8) {
        uint32 c;
        uint32 b;
        (c, b) = lookup3(msgData, 0x0, 0x0);
        return bytes8((uint64(b) << 32) | uint64(c));
    }

    function lookup3(
        bytes memory k,
        uint32 pc,
        uint32 pb
    ) private pure returns (uint32 c, uint32 b) {
        uint32 a = 0xdeadbeef + uint32(k.length) + pc;
        b = a;
        c = a + pb;

        uint256 offset = 0;
        uint256 length = k.length;

        // Process 12-byte chunks
        while (length > 12) {
            a +=
                uint32(uint8(k[offset])) |
                (uint32(uint8(k[offset + 1])) << 8) |
                (uint32(uint8(k[offset + 2])) << 16) |
                (uint32(uint8(k[offset + 3])) << 24);
            b +=
                uint32(uint8(k[offset + 4])) |
                (uint32(uint8(k[offset + 5])) << 8) |
                (uint32(uint8(k[offset + 6])) << 16) |
                (uint32(uint8(k[offset + 7])) << 24);
            c +=
                uint32(uint8(k[offset + 8])) |
                (uint32(uint8(k[offset + 9])) << 8) |
                (uint32(uint8(k[offset + 10])) << 16) |
                (uint32(uint8(k[offset + 11])) << 24);

            (a, b, c) = mix(a, b, c);
            offset += 12;
            length -= 12;
        }

        // Handle remaining bytes
        if (length > 0) {
            if (length >= 1) a += uint32(uint8(k[offset]));
            if (length >= 2) a += uint32(uint8(k[offset + 1])) << 8;
            if (length >= 3) a += uint32(uint8(k[offset + 2])) << 16;
            if (length >= 4) a += uint32(uint8(k[offset + 3])) << 24;
            if (length >= 5) b += uint32(uint8(k[offset + 4]));
            if (length >= 6) b += uint32(uint8(k[offset + 5])) << 8;
            if (length >= 7) b += uint32(uint8(k[offset + 6])) << 16;
            if (length >= 8) b += uint32(uint8(k[offset + 7])) << 24;
            if (length >= 9) c += uint32(uint8(k[offset + 8]));
            if (length >= 10) c += uint32(uint8(k[offset + 9])) << 8;
            if (length >= 11) c += uint32(uint8(k[offset + 10])) << 16;
            if (length == 12) c += uint32(uint8(k[offset + 11])) << 24;
        }

        // Final mixing
        (a, b, c) = finalMix(a, b, c);
    }

    function mix(
        uint32 a,
        uint32 b,
        uint32 c
    ) private pure returns (uint32, uint32, uint32) {
        a -= c;
        a ^= rot(c, 4);
        c += b;
        b -= a;
        b ^= rot(a, 6);
        a += c;
        c -= b;
        c ^= rot(b, 8);
        b += a;
        a -= c;
        a ^= rot(c, 16);
        c += b;
        b -= a;
        b ^= rot(a, 19);
        a += c;
        c -= b;
        c ^= rot(b, 4);
        b += a;
        return (a, b, c);
    }

    /**
     * Final mixing of 3 32-bit values (a, b, c) into c.
     */
    function finalMix(
        uint32 a,
        uint32 b,
        uint32 c
    ) private pure returns (uint32, uint32, uint32) {
        c ^= b;
        c -= rot(b, 14);
        a ^= c;
        a -= rot(c, 11);
        b ^= a;
        b -= rot(a, 25);
        c ^= b;
        c -= rot(b, 16);
        a ^= c;
        a -= rot(c, 4);
        b ^= a;
        b -= rot(a, 14);
        c ^= b;
        c -= rot(b, 24);
        return (a, b, c);
    }

    /**
     * Rotate x by k distance.
     */
    function rot(uint32 x, uint32 k) private pure returns (uint32) {
        return (x << k) | (x >> (32 - k));
    }
}

contract HexToBinary {
    // Lookup table for hexadecimal to binary conversion
    function hexCharToBinary(bytes1 hexChar) private pure returns (string) {
        if (hexChar == "0") return "0000";
        if (hexChar == "1") return "0001";
        if (hexChar == "2") return "0010";
        if (hexChar == "3") return "0011";
        if (hexChar == "4") return "0100";
        if (hexChar == "5") return "0101";
        if (hexChar == "6") return "0110";
        if (hexChar == "7") return "0111";
        if (hexChar == "8") return "1000";
        if (hexChar == "9") return "1001";
        if (hexChar == "a" || hexChar == "A") return "1010";
        if (hexChar == "b" || hexChar == "B") return "1011";
        if (hexChar == "c" || hexChar == "C") return "1100";
        if (hexChar == "d" || hexChar == "D") return "1101";
        if (hexChar == "e" || hexChar == "E") return "1110";
        if (hexChar == "f" || hexChar == "F") return "1111";
        revert("Invalid hex character");
    }

    /**
     * Converts a hexadecimal string to its binary representation.
     * Input string should not include "0x".
     */
    function toBinary(string hexString) public pure returns (string) {
        bytes memory hexBytes = bytes(hexString);
        string memory binaryResult = "";

        for (uint256 i = 0; i < hexBytes.length; i++) {
            binaryResult = string(
                abi.encodePacked(binaryResult, hexCharToBinary(hexBytes[i]))
            );
        }

        return binaryResult;
    }
}

contract SimHash {
    JenkinsHash private jenkinsHash;
    HexToBinary private hexToBinary;

    constructor() public {
        jenkinsHash = new JenkinsHash();
        hexToBinary = new HexToBinary();
    }

    function simhash(
        string[] memory tokens,
        uint256[] memory weights
    ) public view returns (string memory) {
        require(
            tokens.length == weights.length,
            "Tokens and weights must have the same length"
        );

        if (tokens.length == 0) {
            return "0";
        }

        int256[] memory vector = new int256[](64);

        for (uint256 i = 0; i < tokens.length; i++) {
            bytes memory tokenBytes = bytes(tokens[i]);
            bytes8 hash = jenkinsHash.hash64(tokenBytes);
            string memory binaryHash = hexToBinary.toBinary(
                string(abi.encodePacked(hash))
            );

            for (uint256 j = 0; j < 64; j++) {
                if (bytes(binaryHash)[j] == "1") {
                    vector[j] += int256(weights[i]);
                } else {
                    vector[j] -= int256(weights[i]);
                }
            }
        }

        string memory result = "";
        for (uint256 k = 0; k < 64; k++) {
            if (vector[k] > 0) {
                result = string(abi.encodePacked(result, "1"));
            } else {
                result = string(abi.encodePacked(result, "0"));
            }
        }

        return result;
    }

    function similarity(
        string memory a,
        string memory b
    ) public pure returns (uint256) {
        require(
            bytes(a).length == bytes(b).length,
            "Strings must have the same length"
        );

        uint256 count = 0;
        for (uint256 i = 0; i < bytes(a).length; i++) {
            if (bytes(a)[i] == bytes(b)[i]) {
                count++;
            }
        }

        return (count * 100) / bytes(a).length;
    }

    function getIntervalNumber(uint256 value) public pure returns (uint256) {
        require(value <= 100, "Value must be between 0 and 100");

        if (value > 90) return 1;
        if (value > 80) return 2;
        if (value > 70) return 3;
        if (value > 60) return 4;
        if (value > 50) return 5;
        if (value > 40) return 6;
        if (value > 30) return 7;
        if (value > 20) return 8;
        if (value > 10) return 9;
        return 10;
    }

    function matrixToString(
        uint256[][] memory matrix
    ) public pure returns (string memory) {
        bytes memory result;

        for (uint256 i = 0; i < matrix.length; i++) {
            for (uint256 j = 0; j < matrix[i].length; j++) {
                result = abi.encodePacked(result, uint2str(matrix[i][j]));
                if (j < matrix[i].length - 1) {
                    result = abi.encodePacked(result, ",");
                }
            }
            if (i < matrix.length - 1) {
                result = abi.encodePacked(result, "\n");
            }
        }

        return string(result);
    }

    function uint2str(uint256 _i) internal pure returns (string memory) {
        if (_i == 0) {
            return "0";
        }
        uint256 j = _i;
        uint256 length;
        while (j != 0) {
            length++;
            j /= 10;
        }
        bytes memory bstr = new bytes(length);
        uint256 k = length - 1;
        while (_i != 0) {
            bstr[k--] = bytes1(uint8(48 + (_i % 10)));
            _i /= 10;
        }
        return string(bstr);
    }

    function getSimhash(
        string memory style,
        string memory make,
        string memory record,
        uint256[][] memory matrix
    ) public view returns (string memory) {
        string[] memory tokens = new string[](4);
        uint256[] memory weights = new uint256[](4);

        tokens[0] = style;
        weights[0] = 1;

        tokens[1] = make;
        weights[1] = 1;

        tokens[2] = record;
        weights[2] = 5;

        tokens[3] = matrixToString(matrix);
        weights[3] = 5;

        return simhash(tokens, weights);
    }
}

contract DAG {
    struct Node {
        string hash;
        string prehash;
        uint256[][] matrix;
        string style;
        string make;
        string record;
        address owner;
        string HSnMinus1;
        string HMn;
        string SIGn;
        mapping(uint256 => Node[]) children;
    }
    Node public root;
    SimHash private simHash;

    constructor(Node memory rootNode) public {
        rootNode
            .prehash = "0000000000000000000000000000000000000000000000000000000000000000";
        root = rootNode;
        updateAffectedNodes(root);
    }
    function updateAffectedNodes(Node memory rootNode)public
    {

    }

    function findBestMatch(
        string memory newSimhash
    ) public view returns (Node memory) {
        uint256 similarityValue = simHash.similarity(root.hash, newSimhash);
        return _findBestMatchRecurisively(root, newSimhash, similarityValue);
    }

    function _findBestMatchRecurisively(
        Node storage currentNode,
        string memory newSimhash,
        uint256 currentSimilarity
    ) internal view returns (Node memory) {
        Node memory bestMatch = currentNode;
        uint256 bestSimilarity = currentSimilarity;
        uint256 level = simHash.getIntervalNumber(currentSimilarity);

        if (currentNode.children[level].length > 0) {
            Node[] storage children = currentNode.children[level];
            for (uint256 i = 0; i < children.length; i++) {
                uint256 childSimilarity = simHash.similarity(
                    children[i].hash,
                    newSimhash
                );
                if (childSimilarity > bestSimilarity) {
                    Node memory deeperMatch = _findBestMatchRecurisively(
                        children[i],
                        newSimhash,
                        childSimilarity
                    );
                    uint256 deeperSimilarity = simHash.similarity(
                        deeperMatch.hash,
                        newSimhash
                    );

                    if (deeperSimilarity > bestSimilarity) {
                        bestMatch = deeperMatch;
                        bestSimilarity = deeperSimilarity;
                    }
                }
            }
        }
    }

    function insertNode(Node memory newNode) public {
        _insertRecursively(root, newNode);
    }

    function _insertRecursively(
        Node storage currentNode,
        Node memory newNode
    ) internal {
        uint256 similarityValue = simHash.similarity(
            currentNode.hash,
            newNode.hash
        );
        uint256 level = simHash.getIntervalNumber(similarityValue);
        bool found = false;

        if (currentNode.children[level].length == 0) {
            currentNode.children[level].push(newNode);
            newNode.prehash = currentNode.hash;
            updateAffectedNodes(newNode);
        } else {
            Node storage bestChild;
            uint256 bestChildSimilarity = 0;

            for (uint256 i = 0; i < currentNode.children[level].length; i++) {
                uint256 childSimilarity = simHash.similarity(
                    currentNode.children[level][i].hash,
                    newNode.hash
                );
                if (childSimilarity > bestChildSimilarity) {
                    bestChildSimilarity = childSimilarity;
                    bestChild = currentNode.children[level][i];
                }
            }

            if (similarityValue > bestChildSimilarity) {
                newNode.prehash = currentNode.hash;
                currentNode.children[level].push(newNode);
                updateAffectedNodes(newNode);
            } else {
                _insertRecursively(bestChild, newNode);
            }
        }
    }

    function getPath(
        Node memory queryNode
    ) public view returns (Node[] memory) {
        Node memory currentNode = root;
        Node[] memory path = new Node[](1);
        path[0] = currentNode;
        uint256 pathLength = 1;

        while (
            keccak256(abi.encodePacked(currentNode.hash)) !=
            keccak256(abi.encodePacked(queryNode.hash))
        ) {
            Node memory bestMatch;
            uint256 bestSimilarity = 0;

            uint256 similarityValue = simHash.similarity(
                currentNode.hash,
                queryNode.hash
            );
            uint256 level = simHash.getIntervalNumber(similarityValue);

            if (currentNode.children[level].length > 0) {
                Node[] storage children = currentNode.children[level];
                for (uint256 i = 0; i < children.length; i++) {
                    uint256 childSimilarity = simHash.similarity(
                        children[i].hash,
                        queryNode.hash
                    );
                    if (childSimilarity > bestSimilarity) {
                        bestSimilarity = childSimilarity;
                        bestMatch = children[i];
                    }
                }
            }

            if (
                keccak256(abi.encodePacked(bestMatch.hash)) ==
                keccak256(abi.encodePacked(""))
            ) {
                break;
            }

            Node[] memory newPath = new Node[](pathLength + 1);
            for (uint256 j = 0; j < pathLength; j++) {
                newPath[j] = path[j];
            }
            newPath[pathLength] = bestMatch;
            path = newPath;
            pathLength++;

            currentNode = bestMatch;
        }

        if (
            keccak256(abi.encodePacked(currentNode.hash)) ==
            keccak256(abi.encodePacked(queryNode.hash))
        ) {
            return path;
        } else {
            return new Node[](0);
        }
    }

    function findNodeByHash(string memory searchHash) public view returns (Node memory) {
        Node memory currentNode = root;

        if (keccak256(abi.encodePacked(currentNode.hash)) == keccak256(abi.encodePacked(searchHash))) {
            return currentNode;
        }

        Node[] memory queue = new Node[](1);
        queue[0] = currentNode;
        uint256 queueLength = 1;

        while (queueLength > 0) {
            currentNode = queue[queueLength - 1];
            queueLength--;

            if (keccak256(abi.encodePacked(currentNode.hash)) == keccak256(abi.encodePacked(searchHash))) {
                return currentNode;
            }

            for (uint256 level = 1; level <= 10; level++) {
                if (currentNode.children[level].length > 0) {
                    Node[] storage children = currentNode.children[level];
                    for (uint256 i = 0; i < children.length; i++) {
                        Node[] memory newQueue = new Node[](queueLength + 1);
                        for (uint256 j = 0; j < queueLength; j++) {
                            newQueue[j] = queue[j];
                        }
                        newQueue[queueLength] = children[i];
                        queue = newQueue;
                        queueLength++;
                    }
                }
            }
        }

        revert("Node not found");
    }







}
