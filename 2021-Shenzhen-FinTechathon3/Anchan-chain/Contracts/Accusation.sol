pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;
contract Accusation {
    
    struct accusationInfo{
        string exposer;
        string accusationAbstract;
        string attachment;
    }
    mapping (string => accusationInfo[]) accusationMap;

    
    function addAccusation(string expo, string abs, string attachFile,string auth) public {
        accusationInfo memory tempInfo;
        tempInfo.exposer = expo;
        tempInfo.accusationAbstract = abs;
        tempInfo.attachment =attachFile;
        accusationMap[auth].push(tempInfo);
    }
        
    function getAccusation(string auth) public view returns(accusationInfo[]){
        return accusationMap[auth];
        
    }
    
    
    
}