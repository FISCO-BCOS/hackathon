pragma solidity ^0.4.25;

contract ContractFactoryABI{
	function verifyHospital(address addr) returns(bool);
}

contract RaiseContract{
	string[3] Statuss = ["created","WFD","donated"];
	address creator;
	address contractfactory;

	string project_name;
	string patient_name;
    string id1;
	string id2;
	string detail;
	string diease_type;
	string status;

	address[] signatures;

	constructor(string pro_name,string pat_name,string i1,string i2,string det,string die_type,address cf,address cre){
            contractfactory = cf;
            creator = cre;
            project_name = pro_name;
            patient_name = pat_name;
            id1 = i1;
            id2 = i2;
            detail = det;
            diease_type = die_type;
            status = Statuss[0];
            signatures.push(cre);

	}
	
	function CallVerify(address addr) returns(bool){
	    return ContractFactoryABI(contractfactory).verifyHospital(addr);
	}
	
	function getInformation() returns(string pro_name,string pat_name,string i1,string i2,string det,string die_type,address cre){
		return (project_name,patient_name,id1,id2,detail,diease_type,creator);
	}

	function getSignatures() returns(address[]){
		return signatures;
	}
	
	function addSignature() returns(bool,string){
	    if(keccak256(status) != keccak256("donated")){
	        //完成后不能在进行操作，只能查询信息
	        string memory message = "signed successfully!";
    	    bool flag = false;
    	    for(uint i=0;i<signatures.length;i++){
    	        if(signatures[i] == msg.sender){
    	            flag = true;
    	            message = "Can't not resign!";
    	            break;
    	        }
    	    }
    	    
    	    if(!flag){
    	        signatures.push(msg.sender);
    	    }
    	    return (!flag,message);
	    }else{
	        return (false,"Contract is completed!");
	    }
	    
	}
	
	function setStatus(uint index) returns(bool,string){
	   if(keccak256(status) != keccak256("donated")){
	        //完成后不能在进行操作，只能查询信息
            status = Statuss[index];
            return(true,"successfully change status");
	    }else{
	        return (false,"Contract is completed!");
	    }
	}
	

	function transformToWWFD() returns(bool){
		status = Statuss[1];
	} 
	
	function transformToDonated() returns(bool){
		status = Statuss[2];
	}
	
}

