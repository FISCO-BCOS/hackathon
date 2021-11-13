pragma solidity ^0.4.11;
import "./LibString.sol";
library LibAddress {
    
    using LibAddress for *;
    
    function toString(address x)
    internal view returns (string) {
        bytes memory s = new bytes(40);
        for(uint i=0; i<20; i++) {
            bytes1 b = bytes1(uint8(uint(uint160(x))/(2**(8*(19-i)))));
            bytes1 hi = bytes1(uint8(b)/16);
            bytes1 lo = bytes1(uint8(b) - 16 * uint8(hi));
            s[2*i] = char(hi);
            s[2*i+1] = char(lo);
        }
        return LibString.concat("0x", string(s));
    }
    function char(bytes1 b)
    internal view returns(bytes1 c) {
        if(uint8(b) < 10) return bytes1(uint8(b) + 0x30);
        else return bytes1(uint8(b) + 0x57);
    }
}