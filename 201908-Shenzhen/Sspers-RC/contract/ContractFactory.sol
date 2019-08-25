pragma solidity ^0.4.25;

import "./RaiseContract.sol";

contract ContractFactory{
    
    //event
    event successCreateRaiseContract(string pro_name,string pat_name,string i1,string i2,string det,string die_type,address addr);
    event errorCreateRaiseContract(string pro_name,string pat_name,string i1,string i2,string det,string die_type,address addr);
	
	address hospital;
	address government;
	address charity;
	
	address[] signers;

	address[] ctreated_contracts;
	address[] wfd_contracts;
        address[] donated_contracts;
	
	constructor(address hosp,address gov,address ch){
	    hospital = hosp;
	    government = gov;
	    charity = ch;
	    signers.push(hospital);
	    signers.push(government);
	    signers.push(charity);
	}
	
	function createNewRaiseContract(string pro_name,string pat_name,string i1,string i2,string det,string die_type) returns(address){
	    RaiseContract rc ;
	    if(msg.sender == hospital){
	        rc = new RaiseContract(pro_name,pat_name,i1,i2,det,die_type,this,msg.sender);
	        emit successCreateRaiseContract(pro_name,pat_name,i1,i2,det,die_type,msg.sender);
		ctreated_contracts.push(rc);
	        return rc;
	    }
	    
	    emit errorCreateRaiseContract(pro_name,pat_name,i1,i2,det,die_type,msg.sender);
	    return rc;
	}
	
	function vertifyHospital(address addr) returns(bool){
	    return (addr == hospital);
	}

	function getCreatedContracts() returns(address[]){
		return ctreated_contracts;
	}
	
	function getWFDContracts() returns(address[]){
		return wfd_contracts;
	}
	
	function getDonatedContracts() returns(address[]){
		return donated_contracts;
	}
	
	function removeFromcreated() returns(bool){
		bool flag = false;
		uint index ;
		for(uint i=0;i<ctreated_contracts.length;i++){
			if(ctreated_contracts[i] == msg.sender){
				//delete 
				flag = true;
				index = i;
				break;
			}
			
		}

		if(flag){
			uint len = ctreated_contracts.length-1;
			for(uint j=index;j<len;j++){
				ctreated_contracts[j] = ctreated_contracts[j+1];
			}
			delete ctreated_contracts[len-1];
		}

		wfd_contracts.push(msg.sender);
		return flag;
	}

	function removeFromWFD() returns(bool){
		bool flag = false;
		uint index;
		for(uint i=0;i<wfd_contracts.length;i++){
			if(wfd_contracts[i] == msg.sender){
				//delete 
				flag = true;
				index = i;
				break;
			}
			
		}

		if(flag){
			uint len = wfd_contracts.length-1;
			for(uint j=index;j<len;j++){
				wfd_contracts[j] = wfd_contracts[j+1];
			}
			delete wfd_contracts[len-1];
			
		}
		donated_contracts.push(msg.sender);
		return flag;
	}
	
}

