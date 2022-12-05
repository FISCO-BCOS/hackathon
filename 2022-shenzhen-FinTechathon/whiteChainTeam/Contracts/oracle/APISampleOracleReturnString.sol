pragma solidity ^0.4.25;

import "./FiscoOracleClient.sol";

contract APISampleOracleReturnString is FiscoOracleClient {

    struct WeatherInfo {
        string province;
        string city;
        string weather;
        string temperature;
        string winddirection;
        string windpower;
        string humidity;
    }
    
    WeatherInfo[] public info;
    uint index = 0;

    //指定处理的oracle
    address private oracleCoreAddress;

    // Multiply the result by 1000000000000000000 to remove decimals
  //  uint256 private timesAmount  = 10**18;

    mapping(bytes32=>string) private resultMap;

    mapping(bytes32=>bool) private validIds;

    string public result;
    string private url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";


    constructor(address oracleAddress) public {
        oracleCoreAddress = oracleAddress;
    }


    function request() public returns (bytes32)
    {
        returnType = ReturnType.STRING;
          // Set your URL
          // url = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
        // url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
         bytes32  requestId = oracleQuery(oracleCoreAddress, url, 0, returnType);
         validIds[requestId] = true;
         return requestId;
          
    }

    /**
     * Receive the response in the form of string
     */
    function __callback(bytes32 _requestId, bytes  _result) internal
    {
        require(validIds[_requestId], "id must be not used!") ;
        resultMap[_requestId]= string(_result);
        delete validIds[_requestId];
        result = string(_result) ;
    }

      function get()  public view  returns(string ){
         return result;
      }

    function getById(bytes32  id)  public view  returns(string ){
        return resultMap[id];
    }

    function checkIdFulfilled(bytes32 id)  public view  returns(bool){
        return validIds[id];
    }


     function setUrl(string  _url) public {
         url = _url;
     }

    function getUrl() public view  returns(string ){
        return url;
    }

    function push(string memory province, string memory city, string memory weather, string memory temperature, string memory winddirection, string memory windpower, string memory humidity) public {
        WeatherInfo memory _weatherInfo = WeatherInfo(province,city,weather,temperature,winddirection,windpower,humidity);
        info[index] = _weatherInfo;
        index++;
    }
}
