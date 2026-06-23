// SPDX-License-Identifier: SimPL-2.0
pragma solidity>=0.4.24 <0.6.11;

contract DataFormat{
    // string matching
   function strMatching(string memory v1, string memory v2)
   internal
   pure
   returns(bool)
   {
      return keccak256(bytes(v1)) == keccak256(bytes(v2));
   }

   // string catenate
    function strCat(string memory v1, string memory v2) 
    internal 
    pure
    returns (string memory){
        bytes memory v1Bytes = bytes(v1);
        bytes memory v2Bytes = bytes(v2);

        string memory result = new string(v1Bytes.length + v2Bytes.length);
        bytes memory resultBytes = bytes(result);
      
        uint k = 0;
        uint i = 0;
        for (i = 0; i < v1Bytes.length; i++){
           resultBytes[k++] = v1Bytes[i];
        }
        for (i = 0; i < v2Bytes.length; i++) {
           resultBytes[k++] = v2Bytes[i];
        }
        
        return string(resultBytes);
        
   }


    function strCatWithSymbol(string memory v1, string memory v2) 
    internal
    pure
    returns (string memory) 
    {
      if(true == strMatching("", v1)){
         v1 = v2;
      }
      else{
         v1 = strCat(v1, ",");
         v1 = strCat(v1, v2);
      }
      return v1;
   }
}