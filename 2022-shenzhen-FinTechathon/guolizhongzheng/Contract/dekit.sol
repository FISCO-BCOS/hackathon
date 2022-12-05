pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
//代码库，就是说在这个库里面有很多对应的值进行操作;
//通过这个代码库能完成很多操作;
contract dekit {
    //int值改为string类型的变量;
    function Changestr(uint256 i) internal returns (string c) {
        if (i == 0) return "0";
        uint256 j = i;
        uint256 length;
        while (j != 0) {
            length++;
            j /= 10;
        }
        bytes memory bstr = new bytes(length);
        uint256 k = length - 1;
        while (i != 0) {
            bstr[k--] = bytes1(48 + (i % 10));
            i /= 10;
        }
        c = string(bstr);
    }
    //生成hash值的函数
    function GenerateHash(string str) internal pure returns (bytes32) {
        bytes32 hash = keccak256(abi.encodePacked(str));
        return hash;
    }
    //int类型转化为string类型;
    function intToStr(uint256 _i) internal pure returns (string memory) {
        if (_i == 0) return "0";
        uint256 j = _i;
        uint256 len;
        while (j > 0) {
            j /= 10;
            len++;
        }
        bytes memory bstr = new bytes(len);
        uint256 k = len - 1;
        while (_i > 0) {
            bstr[k] = bytes1(uint8(48 + (_i % 10)));
            _i /= 10;
            k--;
        }
        return string(bstr);
    }
    // Bytes转换成字符串 (bytes -> string)
    function bytesToStr(bytes memory data) internal pure returns (string) {
        bytes memory alphabet = "0123456789abcdef";
        bytes memory str = new bytes(2 + data.length * 2);
        str[0] = "0";
        str[1] = "x";
        for (uint256 i = 0; i < data.length; i++) {
            str[2 + i * 2] = alphabet[uint256(uint8(data[i] >> 4))];
            str[3 + i * 2] = alphabet[uint256(uint8(data[i] & 0x0f))];
        }
        return string(str);
    }
    // 地址转换成字符串 (address -> string)
    function addrToStr(address account) internal pure returns (string) {
        return bytesToStr(abi.encodePacked(account));
    }

    // Bytes32转换成字符串 (bytes32 -> string)
    function bytes32ToStr(bytes32 value) internal pure returns (string) {
        return bytesToStr(abi.encodePacked(value));
    }

    //两个字符串进行拼接;
    //字符串拼接;
    function strConcat(string _a, string _b) internal returns (string) {
        bytes memory _ba = bytes(_a);

        bytes memory _bb = bytes(_b);

        string memory ret = new string(_ba.length + _bb.length);

        bytes memory bret = bytes(ret);

        uint256 k = 0;

        for (uint256 i = 0; i < _ba.length; i++) bret[k++] = _ba[i];

        for (i = 0; i < _bb.length; i++) bret[k++] = _bb[i];
        return string(ret);
    }
    //根据索引删除值;
    //删除index对应的地址值;
    function deleteIndex(address[] primaryadd, uint256 index) internal {
        for (uint256 i = index; i < primaryadd.length - 1; i++) {
            primaryadd[i] = primaryadd[i + 1];
        }
        delete primaryadd[primaryadd.length - 1];
    }
    //删除地址
    function deleteAdd(address[] primaryadd, address deleteAdd) internal {
        uint256 index;
        for (uint256 i = 0; i < primaryadd.length; i++) {
            if (deleteAdd == primaryadd[i]) {
                index = i;
            }
            break;
        }
        //删除对应的索引;
        deleteIndex(primaryadd, index);
    }
}
