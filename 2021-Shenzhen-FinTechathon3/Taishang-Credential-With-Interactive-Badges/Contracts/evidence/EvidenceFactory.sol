pragma solidity ^0.4.4;
import "Evidence.sol";

contract EvidenceFactory{
        address[] signers;
		event newEvidenceEvent(address addr);
        function newEvidence(string evi)public returns(address)
        {
            Evidence evidence = new Evidence(evi, this);
            newEvidenceEvent(evidence);
            return evidence;
        }
        
        function getEvidence(address addr) public constant returns(string,address[],address[]){
            return Evidence(addr).getEvidence();
        }

                
        function addSignatures(address addr) public returns(bool) {
            return Evidence(addr).addSignatures();
        }
        
        constructor(address[] evidenceSigners){
            for(uint i=0; i<evidenceSigners.length; ++i) {
            signers.push(evidenceSigners[i]);
			}
		}

        function verify(address addr)public constant returns(bool){
                for(uint i=0; i<signers.length; ++i) {
                if (addr == signers[i])
                {
                    return true;
                }
            }
            return false;
        }

        function getSigner(uint index)public constant returns(address){
            uint listSize = signers.length;
            if(index < listSize)
            {
                return signers[index];
            }
            else
            {
                return 0;
            }
    
        }

        function getSignersSize() public constant returns(uint){
            return signers.length;
        }
    
        function getSigners() public constant returns(address[]){
            return signers;
        }

}