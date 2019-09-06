pragma solidity ^0.4.25;

interface tokenRecipient{function receiveApproval(address _from, uint256 _value, address _token, bytes _extraData) public;}
contract TokenSewage {

    string public name;//token name
    string public symbol; //token symbol
    uint8 public decimals = 2; // the smallest token unit
    uint256 public totalSupply; // total supply totkens
    address admin; //admin address, can't be modified externally

    mapping(address => uint256) public balanceOf; // storage the balance of all accounts
    mapping(address => mapping(address => uint256)) public allowance; //

    event Transfer(address indexed from, address indexed to, uint256 value);
    event Burn(address indexed from, uint256 value);

    modifier onlyAdmin(){
        require(msg.sender == admin);
        _;
    }
    //construct function
    constructor(uint256 initialSupply, string tokenName, string tokenSymbol) public {
//        require(admin == msg.sender);
        totalSupply = initialSupply * 10 ** uint256(decimals);
        balanceOf[msg.sender] = totalSupply;
        name = tokenName;
        symbol = tokenSymbol;

    }
    // token transfer internal function
    function _transfer(address _from, address _to, uint256 _value) internal{
        require(_to != 0x0); // address can't be null
        require(balanceOf[_from] >= _value);
        require(balanceOf[_to] + _value > balanceOf[_to]);

        uint previousBalances = balanceOf[_from] + balanceOf[_to];
        balanceOf[_from] -= _value;
        balanceOf[_to] += _value;
        emit Transfer(_from, _to, _value);

        assert(balanceOf[_from] + balanceOf[_to] == previousBalances);

    }
    // token transfer function, call its internal function
    function transferTo(address _to, uint256 _value) public returns(bool success) {
        require(_to != 0x0);
        _transfer(msg.sender, _to, _value);
        return true;
    }

    function transferFrom(address _from, address _to, uint256 _value) public returns(bool success){
        require(_value <= allowance[_from][msg.sender]);
        allowance[_from][msg.sender] -= _value;
        _transfer(_from, _to, _value);
        return true;
    }

    function approve(address _spender, uint256 _value) public returns (bool success){
        allowance[msg.sender][_spender] = _value;
        return true;
    }

    function approveAndCall(address _spender, uint256 _value, bytes _extraData) public returns (bool success){
        tokenRecipient spender  = tokenRecipient(_spender);
        if(approve(_spender, _value)){
            // inform contract
            spender.receiveApproval(msg.sender, _value, this, _extraData);
            return true;
        }
    }


    function burn(uint256 _value) public returns (bool success) {
        require(balanceOf[msg.sender] >= _value);
        balanceOf[msg.sender] -= _value;
        totalSupply -= _value;
        emit Burn(msg.sender, _value);
        return true;

    }

    function burnFrom(address _from, uint256 _value) public returns (bool success){
        require(balanceOf[msg.sender] >= _value);
        require(_value <= allowance[_from][msg.sender]);
        balanceOf[_from] -= _value;
        allowance[_from][msg.sender] -= _value;
        totalSupply -= _value;
        emit Burn(_from, _value);
        return true;
    }
}
