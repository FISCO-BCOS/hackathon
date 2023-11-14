pragma solidity ^0.4.4;

/**业务层 真正实现业务计算*/
import "./UserData.sol";

contract Calculation is UserData{

    //用户信息
    UserData private _userData;
    //总贡献
    uint public _totalContribution = 0;
    //待分配积分
    uint public _waitingDistributeIntegral = 0;

    //积分记录
    mapping(uint256 => uint256) public _frozenIntegrals;


    //初始化总贡献
    function  initTotalContribution() private{
        //从数据库里面获取
    }

    //添加贡献
    function addContribution(uint256 id, uint contribution) public returns (int16 rtnCode){
       //用户添加贡献
       if(_userData.checkUserExit(id) == this.USER_NOT_EXIT()){
           return this.USER_NOT_EXIT();
       }

      //当前总贡献添加
      _totalContribution += contribution;
      //累计待分配分红
      _waitingDistributeIntegral += contribution;

      _userData.saveContribution(id,contribution);

    return this.SUCCESS();

    }


    //获取冻结积分记录
    function getFrozenIntegral(uint256 id) public returns (uint frozenIntegral){
       frozenIntegral =  _frozenIntegrals[id];
       _frozenIntegrals[id] = 0;
       return frozenIntegral;
    }


      //削减贡献
   function reduceContribution() public returns (int16 rtnCode) {
       uint _leaveContribution = 0;
        uint256[] memory indices = _userData.queryAll();
        for(uint _i = 0; _i<indices.length; _i++ ){
            uint256 _id = indices[_i];
            uint  _contribution;
            uint  _frozenIntegral;
            uint  _integral;
            (_contribution, _frozenIntegral, _integral,) = _userData.queryUser(_id);

            if(_contribution <= 0) {
                 continue;
            }

            //用户剩余贡献
            _contribution = _contribution * 9 / 10;
            _userData.updateAccount(_id,_contribution,_frozenIntegral,_integral);

        }

        //更新当前总贡献
        _totalContribution = _leaveContribution;

        return this.SUCCESS();
   }

      //获取当前贡献参数
   function queryContributionValue() public view returns(uint ){
       return (_totalContribution);
   }

         //获取当前贡献参数
   function queryWaitingDistributeIntegral() public view returns(uint ){
       return (_waitingDistributeIntegral);
   }

}
