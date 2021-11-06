pragma solidity ^0.4.4;

import "./RtnCode.sol";

contract UserData is RtnCode{

    struct User {
        uint256 id;
        // 用户贡献 （以“分”的形式上链）
        uint contribution;
        // 用户冻结积分
        uint frozenIntegral;
        // 用户积分 （以“分”的形式上链）
        uint integral;
        // 是否存在
        bool exists;

    }

    //用户集合
    mapping(uint256 => User) public _users;
    uint256[] private _ids;


    /**创建新用户*/
    function createUser(uint256 id) returns (int16 rtnCode) {
        if(_users[id].exists){
            return this.USER_EXIT();
        }else{
            _users[id] = User(id,0,0,0,true);
            _ids.push(id);
            return this.SUCCESS();
        }
    }

    /**查询用户信息*/
    function queryUser(uint256 id) public view returns (uint contribution,uint frozenIntegral,uint integral) {
        User memory _user =  _users[id];
        contribution = _user.contribution;
        frozenIntegral = _user.frozenIntegral;
        integral = _user.integral;
        return (contribution,frozenIntegral,integral);
    }


    /**检测用户是否存在*/
    function checkUserExit(uint256 id) public view returns (int16 rtnCode){
        if(_users[id].exists){
            return this.USER_EXIT();
        }else{
            return this.USER_NOT_EXIT();
        }
    }

    /**查询所有用户*/
    function queryAll() public view returns (uint256[]){
        return _ids;
    }

    /**更新贡献*/
    function saveContribution(uint256 id,uint contribution) {
        _users[id].contribution += contribution;
    }

    function frozenIntegral(uint256 id, uint integral){
          _users[id].integral += integral;
          _users[id].frozenIntegral -= integral;
    }


        /**更新分配中心账户数据*/
    function updateAccount(uint256 id,uint contribution,uint frozenIntegral,uint integral){
        _users[id].contribution = contribution;
        _users[id].frozenIntegral = frozenIntegral;
        _users[id].integral = integral;
    }


    //提现
    function withdraw(uint256 id, uint integral)  returns (int16 rtnCode){
      _users[id].integral -= integral;
      return this.SUCCESS();
   }

   //分红领取
   function unFrozenIntegral(uint256 id, uint integral) returns (int16 rtnCode){
        _users[id].integral += integral;
        _users[id].frozenIntegral -= integral;
        return this.SUCCESS();
   }

}
