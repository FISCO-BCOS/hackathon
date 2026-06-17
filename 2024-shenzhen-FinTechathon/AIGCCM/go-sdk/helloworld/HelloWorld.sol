pragma solidity >=0.4.10 <0.8.20;

contract HelloWorld {
    string value;

    constructor() public {
        value = "Hello, World!";
    }

    function get() public view returns (string memory) {
        return value;
    }

    function set(string v) public {
        value = v;
    }
}
