pragma solidity ^0.4.4;

/**
控制层-消费券分配中心控制层
*/

import "./RtnCode.sol";

contract DedeApiContract is RtnCode{

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
    //总贡献
    uint public _totalContribution = 0;
    //待分配积分
    uint public _waitingDistributeIntegral = 0;

    //积分记录
    mapping(uint256 => uint256) public _frozenIntegrals;


    modifier validContribution(uint contribution) {
        require (contribution > 0);
        _;
    }


    function createUser(uint256 id) returns(int16 rtnCode) {
        if(_users[id].exists){
            return this.USER_EXIT();
        }else{
            return this.USER_NOT_EXIT();
        }

         if(_users[id].exists){
            return this.USER_EXIT();
        }else{
            _users[id] = User(id,0,0,0,true);
            _ids.push(id);
            return this.SUCCESS();
        }
    }

    // 添加贡献接口
    function addContribution(uint256 id,uint contribution) returns (int16 rtnCode){
       //当前总贡献添加
      _totalContribution += contribution;
      //累计待分配分红
      _waitingDistributeIntegral += contribution;

      _users[id].contribution += contribution;

      return this.SUCCESS();
    }


    //贡献消损接口
    function reduceContribution() returns (int16 rtnCode){
        uint _leaveContribution = 0;
        for(uint _i = 0; _i<_ids.length; _i++ ){
            uint256 _id = _ids[_i];
            User memory _user =  _users[_id];
            uint  _contribution = _user.contribution;
            uint  _frozenIntegral = _user.frozenIntegral;
            uint  _integral = _user.integral;

            if(_contribution <= 0) {
                 continue;
            }

            //用户剩余贡献
            _contribution = _contribution * 9 / 10;
            _users[_id].contribution = _contribution;
            _users[_id].frozenIntegral = _frozenIntegral;
            _users[_id].integral = _integral;

        }

        //更新当前总贡献
        _totalContribution = _leaveContribution;

        return this.SUCCESS();
    }


    //查询贡献接口
    function getAssets(uint256 id)  public view
        returns (uint contribution, uint frozenIntegral,uint integral){
        User memory _user =  _users[id];
        contribution = _user.contribution;
        frozenIntegral = _user.frozenIntegral;
        integral = _user.integral;
        return (contribution,frozenIntegral,integral);
    }

    //提现接口
    function withdraw(uint256 id, uint integral) returns (int16 rtnCode){
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
