pragma solidity ^0.4.25;


contract DAO {
    // DAO积分账户
    mapping (address => uint256) _daoAccount;
    // DAO列表，存在为true，否则为false
    mapping (address => bool) _daoList;

    event DAOAdded(address account);
    event DAORemoved(address account);

    modifier onlyDAO() {
        require(isDAO(msg.sender), "DAO: caller does not have the DAO role");
        _;
    }

    function isDAO(address account) public view returns (bool) {
        require(account != address(0), "DAO: account is the zero address");
        return _daoList[account];
    }

    function addDAO(address account) public {
        require(!isDAO(account), "DAO: account already has role");
        _daoList[account] = true;
        _daoAccount[account] = 0;
        emit DAOAdded(account);
    }

    function removeDAO(address account)public onlyDAO {
        require(isDAO(account), "DAO: account does not have role");
        _daoList[account] = false;
        emit DAORemoved(account);
    }
}





