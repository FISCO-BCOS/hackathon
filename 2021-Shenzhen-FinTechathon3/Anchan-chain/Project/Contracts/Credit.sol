pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;
import "./EngineerList.sol";
import "./Agency.sol";
contract Credit{

    function computeEngineerCredit(EngineerList.businessInfo[] businessList) payable public returns (int) {
        uint i;
        int credit = 0;
        for (i =0; i< businessList.length; i++){
            credit +=10;
        }
        return credit + 60;
    }
    
    function computeAgencyCredit(Agency.businessInfo[] businessList) payable public  returns (int ) {
        uint i;
        int credit = 0;
        for (i =0; i< businessList.length; i++){
            credit +=10;
        }
        return credit + 60;
    }
    
}