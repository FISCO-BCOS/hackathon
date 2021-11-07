pragma solidity >=0.4.25 <0.9.0;

library SafeRole {


    struct Role {

        mapping (address => bool) bearer;

        //TODO: data structure
        mapping (address => uint256) baseBeliefMarks;// only depend on regular evaluation
        mapping (address => int256) dynamicBeliefMarks;// change by user's action

        mapping (address => uint256) percent;// smaller marks to improve beliefMarks
        mapping (address => uint256) balance;
        mapping (address => uint256) time;// repay time
        mapping (address => uint256) checkBaseTime;
        mapping (address => uint256) deduct;
        mapping (address => uint256) quota;

        mapping (address => uint256) transferTime;
        mapping (address => uint256[]) timeLog;
        mapping (address => uint256[]) eventLog;
        mapping (address => int256[]) markLog;
    }

    function add(Role storage role, address account) internal returns(bool){
        if(has(role, account)){
            return false;
        }
        role.bearer[account] = true;
        return true;
    }

    function remove(Role storage role, address account) internal returns(bool) {
         if(!has(role, account)){
            return false;
        }
        role.bearer[account] = false;
        return true;
    }

    function has(Role storage role, address account) internal view returns (bool) {

        return role.bearer[account];

    }
}