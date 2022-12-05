pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./LibDecode.sol";
import "./LibMerkleTree.sol";
import "./LibConvert.sol";

contract Verify{
    
    using LibMerkleTree for LibMerkleTree.MerkleTree;
    
    LibMerkleTree.MerkleTree merk;
	
	event PrintRoot(bytes32 v);
    
    // using LibDecode for LibDecode.Decode;
    
    LibConvert.Partical partical;
    
    function CreatePartical(bool[7] _policy, bytes32[7] _hash, string[7] _public, address _weid) public returns(LibConvert.Partical){
        LibConvert.Partical memory p1 = partical;
        bytes[7] memory _byte = LibConvert.str2bytes(_public);
        p1.cpublic = _byte;
        p1.policy = _policy;
        p1.hash = _hash;
        p1.weid = _weid;
        return p1;
    }
    
    function partical2Tree(LibConvert.Partical _part)internal returns(bytes32){
        bytes32[8] tree;
        tree[0] = sha256(_part.weid);
        for(uint i=1; i<8; i++){
            uint j=i-1;
            if(_part.policy[j]==true){
                tree[i] = sha256(_part.cpublic[j]);
            } else {
                tree[i] = _part.hash[j];
            }
        }
    }
    
    function Decode(bytes signHash, bytes signedString) internal returns (address){
        return LibDecode.decode(signHash, signedString);
    }
    
    function MainVerify(bytes _in, bytes _hash, address _pk) public view returns(bool){
        address calculated_pk = Decode(_in,_hash);
        return(calculated_pk == _pk);
    }
}