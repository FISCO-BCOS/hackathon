// SPDX-License-Identifier: MIT
pragma solidity  <=0.6.10;

import "./SafeMath.sol";
import "./Supervison.sol";
import "./WeatherCheck.sol";


contract Insurances is Supervision{
    using SafeMath for uint256;

    WeatherOracle weatheroracle;

    event NewInsurance(uint256 indexed _insuranceId, address indexed _beneficiary);
    event CensorInsurance(uint256 indexed _insuranceId, bool result);
    event ExecuteInsurance(uint256 indexed _insuranceId, uint value);
    event InsuranceExpired(uint256 indexed _insuranceId);
    event InsuranceNotExpired(uint256 indexed _insuranceId);
    event Withdraw(address indexed _adminAddress, uint value);

    struct Insurance {
        uint256 insuranceId;
        address beneficiaryAddress;
        uint compensation;//0
        bool insuranceStatus;
        bool censorStatus;
        uint startTime;
        uint endTime;
        bool isExpired;
        string insuranceUri;
    }

    // Insurance[] private insurances;
    address public adminAddress;
    uint constant public basicMoney = 1000;
    // uint public totalCompensation;
    
    mapping(uint256 => Insurance) public idOfInsurance;
    mapping(uint256 => uint256) public index;
    mapping(uint256 => bool) public isExist;
    mapping(address => mapping(uint256 => uint256)) private ownerOfInsurance;
    mapping(address => uint256) private numberOfInsurance;

    modifier isOnlyAdmin {
        require(msg.sender == adminAddress, "not admin");
        _;
    }
    modifier isApprove(uint256 _insuranceId) {
        require(isExist[_insuranceId], "insurance not exist");
        Insurance memory insurance = idOfInsurance[_insuranceId];
        require(msg.sender == adminAddress || msg.sender == insurance.beneficiaryAddress || msg.sender == super.getSupervision(), "not Admin or beneficiary or supervision");
        _;
    }
    

    constructor(address _adminAddress, WeatherOracle _weatheroracle) Supervision(_adminAddress) public{
        require(address(_weatheroracle) != address(0), "oracle contract address must be non-null");
        adminAddress = msg.sender;
        weatheroracle = _weatheroracle; 
    }

    function createInsurance(address _beneficiary, uint _duration, string memory _insuranceUri) public isOnlyAdmin returns(uint256){
        require(_beneficiary != address(0), "beneficiary address can not be zero");
        uint _endTime = _duration.add(block.timestamp);
        uint256 _insuranceId = uint256(keccak256(abi.encode(_beneficiary, _endTime, _insuranceUri)));
        require(!isExist[_insuranceId], "insurance exist");
        Insurance memory _insurance = Insurance(_insuranceId, _beneficiary, 0, false, false, block.timestamp, _endTime, false, _insuranceUri);
        isExist[_insuranceId] = true;
        idOfInsurance[_insuranceId] = _insurance;
        uint _number = numberOfInsurance[_beneficiary];
        numberOfInsurance[_beneficiary] = _number.add(1);
        ownerOfInsurance[_beneficiary][_number] = _insuranceId;
        emit NewInsurance(_insuranceId, _beneficiary);
        return _insuranceId;
    }

    //审查合约是否能够理赔
    function censorInsurance(uint256 _insuranceId) public isApprove(_insuranceId) {
        if (!checkEndTime(_insuranceId)){
            return;
        }
        Insurance storage _insurance = idOfInsurance[_insuranceId];
        require(!_insurance.insuranceStatus, "insurance is executed");
        //预言机部分，result直接获取结果       
        bool result = weatheroracle.isLevel(_insurance.startTime, _insurance.endTime);
        if (result) {
            _insurance.censorStatus = true;
        }
        emit CensorInsurance(_insuranceId, result);
    }

    //执行理赔=>生成电子保单
    function executeInsurance(uint256 _insuranceId) public isApprove(_insuranceId) {
        if (!checkEndTime(_insuranceId)){
            return;
        }
        Insurance storage _insurance = idOfInsurance[_insuranceId];
        if (_insurance.insuranceStatus) {
            revert(string(abi.encodePacked(_insuranceId, " is excuted")));
        }
        if (!_insurance.censorStatus) {
            revert(string(abi.encodePacked(_insuranceId, " is not censored")));
        }
        _insurance.compensation = weatheroracle.computeMoney(basicMoney, _insurance.startTime, _insurance.endTime);
        _insurance.insuranceStatus = true;
        emit ExecuteInsurance(_insuranceId, _insurance.compensation);
    }

   function findInsurance(uint256 _insuranceId) external view returns (uint256 insuranceId, address beneficiaryAddress, uint compensation, bool insuranceStatus, bool censorStatus, uint endTime, string memory insuranceUri) {
        require(isExist[_insuranceId], "insurance do not exist");
        Insurance memory _insurance = idOfInsurance[_insuranceId];
        insuranceId = _insuranceId;
        beneficiaryAddress = _insurance.beneficiaryAddress;
        compensation = _insurance.compensation;
        insuranceStatus = _insurance.insuranceStatus;
        censorStatus = _insurance.censorStatus;
        endTime = _insurance.endTime;
        insuranceUri = _insurance.insuranceUri;
    }

    function checkEndTime(uint256 _insuranceId) internal returns (bool){
        Insurance memory _insurance = idOfInsurance[_insuranceId];
        if (_insurance.isExpired){
            emit InsuranceExpired(_insuranceId);
            return false;
        }
        if (_insurance.endTime < block.timestamp) {
            _insurance.isExpired = true;
            // totalCompensation = totalCompensation.sub(_insurance.compensation);
            emit InsuranceExpired(_insuranceId);
            return false;
        }
        emit InsuranceNotExpired(_insuranceId);
        return true;       
    }

    function getListOfInsurance(address _user) external view returns(uint256[] memory) {
        require(super.isSupervision(), "is not supervision");
        uint _number = numberOfInsurance[_user];
        uint[] memory _list;
        for(uint i = 0; i < _number; i++) {
            _list[i] = ownerOfInsurance[_user][i];
        }
        return _list;
    }

}
