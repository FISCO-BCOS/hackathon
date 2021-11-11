pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

contract DAG{
    
    /*
    store the parent programs and children programs and the consumed ids
    */
    mapping(bytes32 =>  uint256[]) internal Parents;  
    mapping(bytes32 =>  uint256[]) internal Children; 
    mapping (uint256 =>bool) internal _consumedIds;   

    //send the commit log
    event Commit(uint256 Id, bytes commitMessage);
    
    uint256[]  internal _parentTokenIds;
    uint256[]  internal _childrenTokenIds;
    uint256[]  internal commitBlockNumber;
    
    
    
    modifier isconsumed(bool isconsumed) {                           
        require(isconsumed != true);
        
        _;   
    }
    
    
    function _consume(uint256 id) internal returns (bool){
        /*
        sign the consumed id
        */
        _commit(id, "Consumed!"); 
        _consumedIds[id] = true;
    }
    
    function getCommitHash(uint256 id, uint number)  public returns (bytes32){
        return sha256(id,number);
    }
    
    
    function _commit(uint256 id, bytes memory commitMessage) isconsumed(_consumedIds[id]) internal {                     
        /*
        when the state change, send information
        */
        commitBlockNumber[id] = block.number;
        emit Commit(id, commitMessage);
    }

    function _merge(uint256 id, uint256[] memory parentTokenIds) internal returns (bool) {          
        /*
        merge the programs
        */
        bytes32 commitHash = getCommitHash(id, block.number);


        Parents[commitHash] = parentTokenIds;

        for(uint i=0; i<_parentTokenIds.length; i++) {
            _consume(_parentTokenIds[i]);
        }
        _commit(id, "Merge");
        return true;
    }


    function _split(uint256 id, uint256[] memory childTokenIds) internal returns (bool) {
        /*
        split the programs
        */
        bytes32 commitHash = getCommitHash(id, block.number);
        Children[commitHash] = childTokenIds;

        _consume(id);
        _commit(id, "Split");
        return true;
    }


    function _referTo(uint256 newTokenId, uint256 parentTokenId) internal returns (bool) {

        bytes32 commitHash = getCommitHash(newTokenId, block.number);
    
        Parents[commitHash].push(parentTokenId);

        return true;
    }


    function _fork(uint256 newTokenId,  uint256 parentTokenId) internal returns (bool) {
        /*
        fork the program
        */
        _referTo(newTokenId, parentTokenId);

        _commit(newTokenId, "fork");
        return true;
    }
    
    function getparents(bytes32 commitHash)public view returns(uint256[]){
        /*
        get the parent programs
        */
        return(Parents[commitHash]);
    }
    
    function getchildren(bytes32 commitHash)public view returns(uint256[]){
        /*
        get the children programs
        */
        return(Children[commitHash]);
    }
    
    function getblocknumber(uint256 id)public view returns(uint){
        return(commitBlockNumber[id]);
    } 
    
}   
