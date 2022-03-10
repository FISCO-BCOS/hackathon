pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;


contract Blacklist {
    address admin;
    struct info{
        string enterpriseId;
        string enterpriseName;
        string illegalNum;
        string illegalRatio;
        string totalBalance;
        // uint length;
        string isDeleted;
        // detail[] details;

    }

    // struct detail{
    //     address orderAddress;
    //     int128 value;
    //     string time;
    //     string reason;
    // }

    mapping(address=>info) public illegalEnterprises;


    function Blacklist(){
        admin = msg.sender;
    }

    function add(address _enterprise,
        string _enterpriseName,
        string _enterpriseId,
        string _illegalNum,
        string _illegalRatio,
        string _totalBalance) public returns(bool success){
        // require(msg.sender == admin);
        illegalEnterprises[_enterprise].enterpriseId = _enterpriseId;
        illegalEnterprises[_enterprise].enterpriseName = _enterpriseName;
        illegalEnterprises[_enterprise].illegalNum = _illegalNum;
        illegalEnterprises[_enterprise].illegalRatio = _illegalRatio;
        illegalEnterprises[_enterprise].totalBalance = _totalBalance;
        // illegalEnterprises[_enterprise].length = _length;
        illegalEnterprises[_enterprise].isDeleted = "0";

        // detail info;
        // info.orderAddress = _orderAddress;
        // info.value = _value;
        // info.time = _time;
        // info.reason = _reason;
        // uint length = illegalEnterprises[_enterprise].length;
        // illegalEnterprises[_enterprise].details[length] = info;
        // illegalEnterprises[_enterprise].length += 1;
        return true;
    }



    function query(address _enterprise) public view returns(string _info){
        // _info = illegalEnterprises[_enterprise];
        string memory  _enterpriseId = illegalEnterprises[_enterprise].enterpriseId;
        string memory _enterpriseName = illegalEnterprises[_enterprise].enterpriseName;
        string memory _illegalNum = illegalEnterprises[_enterprise].illegalNum;
        string memory _illegalRatio = illegalEnterprises[_enterprise].illegalRatio;
        string memory _totalBalance = illegalEnterprises[_enterprise].totalBalance;
        string isDeleted = illegalEnterprises[_enterprise].isDeleted;
        // string aa = "test" + "ee"
        // string memory infoConcat = strConcat("123", "456");
        string memory temp = strConcat(_enterpriseId, "#", _enterpriseName, "#");
        string memory temp1 = strConcat(temp, _illegalNum, "#","");
        string memory temp2 = strConcat(temp1, _illegalRatio, "#","");
        string memory temp3 = strConcat(temp2, _totalBalance, "#","");
        string memory temp4 = strConcat(temp3, "0","","");

        //
        return temp4;
        // uint _length = illegalEnterprises[_enterprise].length;
        // return info(_enterpriseId, , _illegalNum, _illegalRatio, _totalBalance, false);
    }
     function strConcat(string _a, string _b, string _c, string _d) internal returns (string){
        bytes memory _ba = bytes(_a);
        bytes memory _bb = bytes(_b);
        bytes memory _bc = bytes(_c);
        bytes memory _bd = bytes(_d);
        string memory ret = new string(_ba.length + _bb.length + _bc.length + _bd.length);
        bytes memory bret = bytes(ret);
        uint k = 0;
        for (uint i = 0; i < _ba.length; i++)bret[k++] = _ba[i];
        for (i = 0; i < _bb.length; i++) bret[k++] = _bb[i];
        for (i = 0; i < _bc.length; i++) bret[k++] = _bc[i];
        for (i = 0; i < _bd.length; i++) bret[k++] = _bd[i];
        return string(ret);
     }



    // function update(address _enterprise,
    //     string _enterpriseName,
    //     uint256 _enterpriseId,
    //     uint128 _value,
    //     string _time,
    //     string _reason) public returns(bool success){
    //     require(msg.sender == admin);
    //     illegalEnterprises[_enterprise].enterpriseName = _enterpriseName;
    //     illegalEnterprises[_enterprise].enterpriseId = _enterpriseId;
    //     illegalEnterprises[_enterprise].enterpriseName = _enterpriseName;
    //     illegalEnterprises[_enterprise].enterpriseName = _enterpriseName;
    //     illegalEnterprises[_enterprise].enterpriseName = _enterpriseName;
    // }

    function remove(address _entAddress) public returns(bool success){
        require(msg.sender == admin);
        illegalEnterprises[_entAddress].isDeleted = "1";
        return true;
    }
}