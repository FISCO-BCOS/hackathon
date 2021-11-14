pragma solidity ^0.4.25;
import "./Roles.sol";
import "./PlatOwner.sol";
import "./SafeMath.sol";
import "./AdIssuer.sol";
import "./Advertise.sol";
import "./TradeHistory.sol";
import "./lib/LibString.sol";
import "./lib/LibUint.sol";
import "./AdInvesment.sol";
pragma experimental ABIEncoderV2;

/**
 *  蒲公英积分 
 */
contract Integral {
    
    AdIssuer private _adIssuer = new AdIssuer();
    
    PlatOwner private _platOwner = new PlatOwner();
    
    Advertise private _advertise = new Advertise();
    
    TradeHistory private _trade = new TradeHistory();
    
    AdInvesment private _adInvesment = new AdInvesment();
    
    string private _shortName; //代币名字(蒲公英)
    
    string private _symbol; //代符号
    
    uint256 private _totalAmount; //当前系统积分总量，不设上限
    
    uint8 private _oncePlay = 10; //一次播放产生的积分数量
    
    using SafeMath for uint256;
    using LibString for string;
    using LibUint for uint256;
    /**
     * 每播放一个视频，生成10积分，默认作者和平台各获得5
     * 平台可以改变比例以吸引作者在其上投稿
     */
    mapping(address => uint8) private authorProportion; //作者拥有积分的比例
    
    mapping(address => uint8) private platPlayProportion;   // 播放平台方拥有积分的比例
    
    mapping(address => uint8) private platOwnerProportion; //提供视频资源平台方获取积分量(别人看我平台的视频，要给我多少积分)
    
    mapping(address => uint256) private _balanceOf; //账户下拥有的积分量
    //存放所有的平台信息
    // mapping(address => string) public platform;
    mapping(address => uint256) private _balanceOfHistory;
    
    
    constructor() public{
        _shortName = "Dandelion";
        _symbol = "DEN";
    }
    //增加积分，测试时可用
    // function addIntegral(address _owner, uint256 _value)public view{
    function addIntegral(address _owner, uint256 _value)public{
        require(_owner != address(0), "Integral: could not add internal to none address");
        require(_value > 0, "Integral: could not add zero value");
        _balanceOf[_owner] += _value;
    }
    
    //判断条件，只有广告商
    modifier onlyAdvertise() {
        require(_isAdvertise(msg.sender), "AdvertiseRole: caller does not have the Advertise role");
        _;
    }
    
    modifier onlyIssuer() {  //判断条件，判断该地址是否平台方
        require(_isIssuer(msg.sender), "IssuerRole: caller does not have the Issuer role");
        _;
    }
    
    function authorProp(address account) public returns(uint256){
        return uint256(authorProportion[account]);
    }

    function _isIssuer(address account) internal returns (bool) {
        require(account != address(0), "internal: _isIssuer account not be null");
        return _platOwner.exist_plat(account);
    }
    
    //判断某地址是否是广告商
    function _isAdvertise(address account) internal returns(bool){
        require(account != address(0), "internal: _isAdvertise account not be null");
        return _adIssuer.exist_bill(account);
    }
    
    //根据地址返回某平台的分成比例
    function getProportion(address account) public view returns(uint8 result){
        require(account != address(0), "integral: could not get none address Proportion");
        if(platPlayProportion[account] == 0){
            platPlayProportion[account] = 5;
        }
        return platPlayProportion[account];
    }
    //获得所有平台积分的比例（仅返回平台的分成，10-该分成为用户的分成）
    function getAllProportion() public view returns(string[]){
        string[] memory plats = _platOwner.getAllAddress();
        string[] memory result = new string[](plats.length);
        for(uint i = 0; i < plats.length; i++){
            if(platPlayProportion[plats[i].toAddress()] == 0){
                platPlayProportion[plats[i].toAddress()] = 5;
            }
            result[i] = plats[i];
            result[i] = LibString.concat(result[i], "^,^");
            result[i] = LibString.concat(result[i], (uint(platPlayProportion[plats[i].toAddress()])).toString());
        }
        return result;
    }
    
    //平台可更改获取积分的比例
    function ProportionChange(uint8 _platform) public{
        require(_platform > 0 && _platform < 10, "integral: could not less than 0 or more than 10");
        _ProportionChange(msg.sender, _oncePlay - _platform, _platform);
    }
    
    function _ProportionChange(address _owner, uint8 _author, uint8 _platform) internal{
        require(_author + _platform == 10, "integral: _ProportionChange _author add _platform must equal ten");
        require(_author >= 0, "integral: _ProportionChange _author must more than zero");
        require(_platform >= 0, "integral: _ProportionChange _platform must more than zero");
        // require(_platOwner >= 0);
        // require(_platOwner < 10);
        require(_owner != address(0));
        authorProportion[_owner] = _author;
        platPlayProportion[_owner] = _platform;
        // platOwnerProportion[_owner] = _platform;
    }
    
    function mint(address authorReceiver, address platformReceiver, address operator) public{
        mint(authorReceiver,platformReceiver,operator,1);
    }
    
    // mapping(address => mapping(address => uint256)) public allowance;
    //由播放时间触发，每播放一次平台和用户获得对应比例的积分
    // function mint(address authorReceiver, address operator) public onlyIssuer{
    function mint(address authorReceiver,address platformReceiver, address operator, uint256 amount) public{
        _mint(authorReceiver, platformReceiver, operator,amount);
    }
    
    function _mint(address authorReceiver, address platformReceiver, address operator,uint256 amount) internal{
        require(authorReceiver != address(0), "integral: _mint author has not be null");
        require(platformReceiver != address(0), "integral: _mint platform has not be null");
        require(operator != address(0), "integral: _mint operator could not be non address");
        require(amount > 0, "integral: could not play less than 1");
        // TOFIX(不重要)：platform = 10-author即可, proportionChange同
        if(platPlayProportion[platformReceiver] == 0){
            authorProportion[platformReceiver] = 5;
            platPlayProportion[platformReceiver] = 5;
        }
        require(authorProportion[platformReceiver] + platPlayProportion[platformReceiver] == 10, "integral: _mint number has question");
        
        if(platformReceiver == operator){
            _balanceOf[authorReceiver] += uint256(authorProportion[platformReceiver]) * amount;
            _balanceOf[platformReceiver] += uint256(platPlayProportion[platformReceiver]) * amount;
            _balanceOfHistory[authorReceiver] += uint256(authorProportion[platformReceiver]) * amount;
            _balanceOfHistory[platformReceiver] += uint256(platPlayProportion[platformReceiver]) * amount;
        }else{
            _balanceOf[authorReceiver] += (uint256(authorProportion[platformReceiver]) - 1) * amount;
            _balanceOf[platformReceiver] += (uint256(platPlayProportion[platformReceiver]) - 1) * amount;
            _balanceOf[operator] += amount; //给第三方转播方1积分
            _balanceOfHistory[authorReceiver] += (uint256(authorProportion[platformReceiver]) - 1) * amount;
            _balanceOfHistory[platformReceiver] += (uint256(platPlayProportion[platformReceiver]) - 1) * amount;
            _balanceOfHistory[operator] += amount; //给第三方转播方1积分
        }
    }
    
    //返回某个地址上的积分数
    function balance(address _owner) public view returns(uint256 balance){
        // address owner = _owner.toAddress();
        require(_owner != address(0), "integral: balance _owner has not be null");
        balance = _balanceOf[_owner];
    }
    
    //该账户的历史总积分
    function balanceHistory() public view returns(uint256 balance){
        balance = _balanceOfHistory[msg.sender];
    }
    
    //交易,用于购买广告需要的积分
    // TODO: 如何保证用户之间无法转账
    function _transfer(address _from, address _to, uint256 _value, string memory _advertiseId) internal {
        require(_to != address(0), "integral: _transfer can not Transfer to zeroAddress");
        require(_isAdvertise(_to), "integral: _transfer only can Transfer to advertisement");
        require(_balanceOf[_from] >= _value, "integral: _transfer the rest integral is not enough");
        require(_balanceOf[_to] + _value > _balanceOf[_to], "integral: _transfer _value must more than zero");
        require(bytes(_advertiseId).length != 0,"integral: _transfer _advertiseId _value must not be null");
        uint256 IntegralNeed;
        uint256 IntegralGet;
        (IntegralNeed, IntegralGet) = _adInvesment.getRequest(_advertiseId);
        // require(_value <= IntegralNeed - IntegralGet,"Integral,_transfer the Integral more than need");
        if(_value > IntegralNeed - IntegralGet){
            _value = IntegralNeed - IntegralGet;
        }
        _adInvesment.updateRequest(_advertiseId, IntegralNeed, IntegralGet, _value);
        uint256 previousBalance = _balanceOf[_from] + _balanceOf[_to];
        _balanceOf[_from] -= _value;
        _balanceOf[_to] += _value;
        _balanceOfHistory[_to] += _value;
        emit Transfer(_from,_to,_value);
        assert(_balanceOf[_from] + _balanceOf[_to] == previousBalance);
    }
    
    // _to 广告商的地址 _value 转的积分 _advertiseId 广告位的id
    // function transfer(address _to, uint256 _value, string memory _advertiseId) public whenNotSuspended returns (bool success){
    function transfer(address _to, uint256 _value, string memory _advertiseId) public returns (bool success){
        _transfer(msg.sender, _to, _value, _advertiseId);
        
        _trade.insert(msg.sender, _to, _value);
        success = true;
    }
    
    // function destroy(uint256 _value) public whenNotSuspended returns(bool){
    function destroy(uint256 _value) public returns(bool){
        require(_value > 0, "Integral: value must more than zero");
        _destroy(msg.sender, _value);
        return true;
    }
    
    //广告商购买该数量的积分发布广告
    function _destroy(address _owner, uint256 _value) internal {
        require(_balanceOf[_owner] >= _value, "Integral: destroy Insufficient balance");
        _balanceOf[_owner] -= _value;
        emit Destroy(_owner, _value);
    }
    
    event Transfer(address indexed _from, address indexed _to, uint256 _value); 
    event Destroy(address indexed _owner, uint256 _value);
    event Proportion(address indexed account, uint256 _platform);
    
}
