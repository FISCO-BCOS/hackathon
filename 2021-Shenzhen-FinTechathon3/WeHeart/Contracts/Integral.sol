pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

/**
 * @title SafeMath
 * @dev Math operations with safety checks that throw on error
 */
 
library SafeMath {
  function mul(uint256 a, uint256 b) public constant returns (uint256) {
    uint256 c = a * b;
    assert(a == 0 || c / a == b);
    return c;
  }

  function div(uint256 a, uint256 b) public constant returns (uint256) {
    // assert(b > 0); // Solidity automatically throws when dividing by 0
    uint256 c = a / b;
    // assert(a == b * c + a % b); // There is no case in which this doesn‘t hold
    return c;
  }

  function sub(uint256 a, uint256 b) public constant returns (uint256) {
    assert(b <= a);
    return a - b;
  }

  function add(uint256 a, uint256 b) public constant returns (uint256) {
    uint256 c = a + b;
    assert(c >= a);
    return c;
  }
  
   function toBytesNickJohnson(uint256 x) constant returns (bytes b) {
        b = new bytes(32);
        assembly { mstore(add(b, 32), x) }
    }
}


contract Integral  {


   using SafeMath for uint256;

   
   //事件机制测试
   event Instructor(bytes32 txid,address memberOID);

   struct TxDetail{
      bytes32 txid;        //交易编号
      address _from;       //交易者编号
      address _to;         //交易者编号
      uint256 createTime;  //交易时间
      uint integral;       //积分数
   }
   
  

    //查看对应账号的积分余额。 任何人都可以查到任何地址的余额，正如所有数据在区块链上都是公开的。
	mapping(address => uint) internal balanceOf;
	//用户交易明细
	mapping(address => bytes32[]) internal user_txs;
	//交易细节
	mapping(bytes32 => TxDetail) internal tx_details;
	//交易留言
	    
    function gettxid(uint256 time, uint number)  public returns (bytes32){
        return sha256(time,number);
    }
	
        /*
        add integral
        */
	function addPoints(address _user, uint _value)public returns(bytes32) {
	    uint balance = balanceOf[_user] + _value;
	    balanceOf[_user] = balance;
	    bytes32 _txid = gettxid(now, block.number);
	    
	    user_txs[_user].push(_txid);
	    tx_details[_txid] = TxDetail({txid: _txid, _from: _user, _to : _user , createTime: now, integral: _value});
	    emit Instructor(_txid, _user);
	    
	    return(_txid);
	}
	
        /*
        send integral
        */
	function dealPoints(address _from, address _to, uint _value)public {
	    require(balanceOf[_from] >= _value);
	    balanceOf[_from] = balanceOf[_from] - _value;
	    balanceOf[_to] = balanceOf[_to]+ _value;
	    
	    bytes32 _txid = gettxid(now, block.number);
	    
	    user_txs[_from].push(_txid);
	    user_txs[_to].push(_txid);
	    tx_details[_txid] = TxDetail({txid: _txid, _from: _from, _to : _to , createTime: now, integral: _value});
	    emit Instructor(_txid, _from);
	}
	
	
       /*
       get your balance
       */
	function getBalance(address _user) public returns (uint) {
	    return balanceOf[_user];
	}

	
       /*
       get  all your txids
       */
	function getTxs(address addr)public view returns(bytes32[]){
	    return(user_txs[addr]);
	}
	
        /*
        get txiformation from txid
        */
	function getTxInfor(bytes32 txs_id)public view returns(bytes32,
      address,
      address,
      uint256,
      uint){
	    return(tx_details[txs_id].txid,
	    tx_details[txs_id]._from,
	    tx_details[txs_id]._to,
	    tx_details[txs_id].createTime,
	    tx_details[txs_id].integral);
	}
	
	
}
