pragma solidity >=0.4.25 <0.6.11;
pragma experimental ABIEncoderV2;

import "./LibMerkleTree.sol";
import "./LibConvert.sol";

contract EMRtoTree{
    // 将电子病历的各字段转化为默克尔树
    using LibMerkleTree for LibMerkleTree.MerkleTree;
    
    LibMerkleTree.MerkleTree merk;
	
	event PrintRoot(bytes32 v);
	
    function MainToTree(address _weid, string[7] memory _emr) public returns(bytes32){
        bytes[7] memory _byte = LibConvert.str2bytes(_emr);
        bytes32[8] memory sha = convert(_weid,_byte);
        bytes32[] memory sha2 = LibConvert.dynamic2stab(sha);
        bytes32 root = createTree(sha2);
        return root;
    }
    
    // 接收字符串并生成sha
    function convert(address _weid, bytes[7] memory _emr) internal returns(bytes32[8]){
        bytes32[8] memory _sha;
        _sha[0] = sha256(_weid);
        for(uint i=1; i<8; i++){
            bytes memory para = bytes(_emr[i]);
            _sha[i] = sha256(para);
        }
        return _sha;
    }
    
    function createTree(bytes32[] memory _sha) internal returns(bytes32){
        LibMerkleTree.constructMerkleTree(_sha, merk);
        return merk.root.value;
    }
    
    
}
