// SPDX-License-Identifier: MIT
pragma solidity  >=0.4.25 <=0.6.10;
import "./Claimitic.sol";
import "./DateTime.sol";

contract WeatherOracle {
    Claimitic public claimitic;


    mapping(uint256 => uint256) private scheme;
    mapping(uint256 => uint256) private time;
    uint count = 0;

    constructor(Claimitic _claimAddress) public {
        require(address(_claimAddress) != address(0), "claimAddress can not be zero");
        claimitic = _claimAddress;
    }

    function setDisasterLevel(uint16 _year, uint8 _month, uint8 _day, uint _level) public returns(bool){
        uint _timeStamp = DateTime.toTimestamp(_year, _month, _day);
        count++;
        scheme[count] = _level;
        time[count] = _timeStamp;
    } 

    function getLevel(uint _startTime, uint _endTime) internal view returns(uint) {
        uint _level = 0;
        for(uint i; i >= 0; i--) {
            if(time[i] > _endTime) {
                break;
            }
            if(time[i] <= _endTime && time[i] >= _startTime && scheme[i] > _level) {
                _level = scheme[i];
            }
            if(_level == 4) {
                break;
            }
        }      
        return _level;
    }

    function isLevel(uint _startTime, uint _endTime) public view returns(bool) {
        uint _level = getLevel(_startTime, _endTime);
        if(_level == 1 || _level == 2 || _level == 3 || _level == 4) {
            return true;
        }
        return false;
    }

    function computeMoney(uint _basicMoney, uint _startTime, uint _endTime) public view returns(uint){
        uint _latestLevel = getLevel(_startTime, _endTime);
        if(_latestLevel == 4){
            return claimitic.forthLevel(_basicMoney);
        }else if(_latestLevel == 3){
            return claimitic.thirdLevel(_basicMoney);
        }else if(_latestLevel == 2){
            return claimitic.secondLevel(_basicMoney);
        }else if(_latestLevel == 1){
            return claimitic.firstLevel(_basicMoney);
        }else{
            return 0;
        }
    }

}
