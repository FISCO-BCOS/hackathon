pragma solidity ^0.4.25;
import "./SafeMath.sol";
import "./Counters.sol";
import "./Roles.sol";
import "./Integral.sol";
import "./lib/LibAddress.sol";
import "./lib/LibString.sol";
import "./lib/LibUint.sol";
pragma experimental ABIEncoderV2;

/**
 * 标识在紧急情况下可以暂停合约的角色（管理员）部署该合约的管理员为第一个管理员，只有管理员可以增加管理员
 */ 
contract SuspenderRole {
    using Roles for Roles.Role;

    event SuspenderAdded(address indexed account);
    event SuspenderRemoved(address indexed account);

    Roles.Role private _suspenders;

    constructor () internal {
        _addSuspender(msg.sender);
    }

    modifier onlySuspender() {
        require(isSuspender(msg.sender), "SuspenderRole: caller does not have the Suspender role");
        _;
    }

    function isSuspender(address account) public view returns (bool) {
        return _suspenders.has(account);
    }

    function addSuspender(address account) public onlySuspender {
        _addSuspender(account);
    }

    function renounceSuspender() public {
        _removeSuspender(msg.sender);
    }

    function _addSuspender(address account) internal {
        _suspenders.add(account);
        emit SuspenderAdded(account);
    }

    function _removeSuspender(address account) internal {
        _suspenders.remove(account);
        emit SuspenderRemoved(account);
    }
}

/**
 * 基础了上面的管理员接口，增加暂停合约的接口，判断当前合约是否被暂停的修饰函数
 */ 
contract Suspendable is SuspenderRole {

    event Suspended(address account);
    event UnSuspended(address account);
    event Approval(address owner, address to, uint256 assetId);

    bool private _suspended;

    constructor () internal {
        _suspended = false;
    }

    /**
     * @return True if the contract is suspended, false otherwise.
     */
    function suspended() public view returns (bool) {
        return _suspended;
    }

    /**
     * @dev Modifier to make a function callable only when the contract is not suspended.
     * 函数的修饰器，用来改变函数的行为，
     * 在函数执行前检查该条件
     */
    modifier whenNotSuspended() {
        require(!_suspended, "Suspendable: suspended");
        _;
    }

    /**
     * @dev Modifier to make a function callable only when the contract is suspended.
     */
    modifier whenSuspended() {
        require(_suspended, "Suspendable: not suspended");
        _;
    }

    /**
     * @dev Called by a Suspender to suspend, triggers stopped state.
     */
    function suspend() public onlySuspender whenNotSuspended {
        _suspended = true;
        emit Suspended(msg.sender);
    }

    /**
     * @dev Called by a Suspender to unSuspend, returns to normal state.
     */
    function unSuspend() public onlySuspender whenSuspended {
        _suspended = false;
        emit UnSuspended(msg.sender);
    }
}

/**
 * 一个非同质化2资产，一个资产代表一个视频
 */ 
contract DIGIT is Suspendable{
    
    event Create(address indexed _owner, uint256 _assetId);  //创建数字资产 
    event Destroy(address indexed _owner, uint256 _assetId); //销毁数字资产
    event Play(address indexed _account, uint256 _assetId, uint256 timestamp); //数字资产被播放
    
    Integral integral = Integral(0xebf7c1a6139ae44cbf3e5b559f160b326ecd52e1);
    
    string private _description;
    
    string private _shortName;
    
    string constant private splitString = "^,^";
    
    // using Address for address;
    using SafeMath for uint256;
    using Counters for Counters.Counter;
    using LibAddress for address;
    using LibString for string;
    using LibUint for uint;
    
    uint256[] private _allAssets; //所有资产的编号
    mapping(uint256 => uint256) private _allAssetsIndex; //对应上面资产地址的下标
    mapping(uint256 => mapping(address => uint256)) private _assetLike; //视频的点赞量
    mapping(uint256 => mapping(address => uint256)) private _assetPlay; //视频的播放量 
    mapping(uint256 => uint256) private _assetTotalLike; //视频总点赞量
    mapping(uint256 => uint256) private _assetTotalPlay; //视频总播放量
    mapping(uint256 => string) private _assetURI; //标识该视频在平台商处的存储链接
    mapping(uint256 => address) private _assetOwners; //存储某个asset对应的用户 
    mapping(address => uint256) private _balances;  // 某个地址下asset的数量
    mapping(address => uint256[]) private _ownedAssets; //某个地址下拥有的资产列表
    mapping(uint256 => address) private _assetApprovals; //某个资产被授权给某个地址（一般用作授权平台方）
    mapping(address => mapping(address => bool)) private _operatorApprovals; //授权操作
    mapping(uint256 => uint256) private _ownedAssetsIndex; //某个资产在用户资产列表的位置
    mapping(address => uint256) private _quota; //某平台拥有发布广告积分的额度
    mapping(uint256 => string) private _assetTopic; //视频的标题
    mapping(uint256 => uint256) private _allIntegral; //某个资产获取的积分数量（为用户带来的收益）
    
    constructor() public
    {
        _description = "视频的非同质化资产";
        _shortName = "DIGIT";
    }
    
    /**
     * 返回某地址下拥有的资产数量
     */ 
    function balanceOfAsset(address _owner) public view returns(uint256){
        require(_owner != address(0), "DIGIT: balance query for the zero address");
        return _balances[_owner];
    }
    
    /**
     * 返回某平台方拥有的积分额度
     */ 
    function quota(address account) external returns(uint256){
        return _quota[account];
    }

    /**
     * 根据资产的id返回该资产的拥有者的地址
     */ 
    function ownerOf(uint256 assetId) public view returns (address) {
        require(_assetOwners[assetId] != address(0), "DIGIT: owner query for nonexistent asset");
        return _assetOwners[assetId];
    }
    
    /**
     * 返回某视频给用户带来的积分收益
     */ 
    function allIntegral(uint256 assetId) public view returns(uint256){
        return _allIntegral[assetId];
    }
    
    /**
     * 返回某资产的总播放量
     */ 
    function assetTotalPlay(uint256 assetId) public view returns(uint256){
        require(_exists(assetId),  "DIGIT: totalPlay for nonexistent asset");
        return _assetTotalPlay[assetId];
    }
    
    function assetTotalLike(uint256 assetId) public view returns(uint256){
        require(_exists(assetId),  "DIGIT: totalLike for nonexistent asset");
        return _assetTotalLike[assetId];
    }
    
    /**
     * 视频在某平台的播放量
     */ 
    function playOfAttribution(uint256 assetId, address operator) public view returns(uint256){
        require(_exists(assetId), "DIGIT: playOfAttribution for nonexistent asset");
        require(operator != address(0), "DIGIT: playOfAttribution for nonexistent address");
        return _assetPlay[assetId][operator];
    }
    
    function likeOfAttribution(uint256 assetId, address operator) public view returns(uint256){ //视频在某平台的点赞量
        require(_exists(assetId), "DIGIT: likeOfAttribution for nonexistent asset");
        require(operator != address(0), "DIGIT: likeOfAttribution for nonexistent address");
        return _assetLike[assetId][operator];
    }
    
    /**
     * 视频被播放
     */ 
     function assetPlay(uint256 assetId, address operator, uint amount) public{ //视频被播放
        require(_exists(assetId), "DIGIT: assetPlay for nonexistent asset");
        require(operator != address(0), "DIGIT: assetPlay for nonexistent address");
        require(amount > 0, "DIGIT:assetPlay amount must more than zero");
        _assetPlay[assetId][operator] += amount;
        _assetTotalPlay[assetId] += amount;
        _quota[operator].add(10 * amount);
        integral.mint(ownerOf(assetId),_assetApprovals[assetId], operator, amount);
        _allIntegral[assetId] += integral.authorProp(_assetApprovals[assetId]) * amount;
        emit Play(operator, assetId, block.timestamp);
    }
    
    // function assetLike(uint256 assetId, address operator) public onlyIssuer{ //视频被点赞
    function assetLike(uint256 assetId, address operator) public{ //视频被点赞
        require(_exists(assetId), "DIGIT: assetLike for nonexistent asset");
        require(operator != address(0), "DIGIT: assetLike for nonexistent address");
        _assetLike[assetId][operator] += 1;
        _assetTotalLike[assetId] += 1;
    }
    
    //资产的总数量
    function totalSupply() external view returns(uint256){
        return _allAssets.length;
    }
    
    //该非同质化资产的描述
    function description() external view returns (string memory) {
        return _description;
    }

    //简称
    function shortName() external view returns (string memory) {
        return _shortName;
    }
    
    //根据用户地址访问他的第几个资产
    function assetOfOwnerByIndex (address owner, uint256 index) public view returns (uint256) {
        require(index < balanceOfAsset(owner), "DIGITMetadata: owner index out of bounds");
        return _ownedAssets[owner][index];
    }
    
    //访问总资产列表的第index个资产
    function assetByIndex(uint256 index) public returns (uint256) {
        uint256 totalAmount = _allAssets.length.sub(1);
        require(index < totalAmount, "DIGIT: global index out of bounds");
        require(index >= 0, "DIGIT: global index 不能小于0");
        return _allAssets[index];
    }
    //组装数据
    function getDataFromIndex(uint256[] assetids) internal returns(string[] memory){
        uint256 totalAmount = assetids.length;
        string[] memory result = new string[](totalAmount);
        for(uint i = 0; i < totalAmount; i++){
            result[i] = assetids[i].toString();
            result[i] = LibString.concat(result[i], splitString);
            result[i] = LibString.concat(result[i], _assetOwners[assetids[i]].toString());
            result[i] = LibString.concat(result[i], splitString);
            result[i] = LibString.concat(result[i], _assetTotalPlay[assetids[i]].toString());
            result[i] = LibString.concat(result[i], splitString);
            result[i] = LibString.concat(result[i], _assetURI[assetids[i]]);
            result[i] = LibString.concat(result[i], splitString);
            result[i] = LibString.concat(result[i], _assetApprovals[assetids[i]].toString());
            result[i] = LibString.concat(result[i], splitString);
            result[i] = LibString.concat(result[i], _assetTopic[assetids[i]]);
            result[i] = LibString.concat(result[i], splitString);
            result[i] = LibString.concat(result[i], allIntegral(assetids[i]).toString());
        }
        return result;
    }
    
    //访问某地址下的所有资产
    function assetOfOwner(address _owner) public view returns(string[] memory){
        require(_owner != address(0), "DIGIT: assetOfOwner query for the zero address");
        return getDataFromIndex(_ownedAssets[_owner]);
    }
    
    //访问所有的资产
    function allAsset() public view returns(string[] memory){
        return getDataFromIndex(_allAssets);
    }
    
    //委托给平台
    function approve(address to, uint256 assetId) public whenNotSuspended { //用户授权于平台方
        address owner = ownerOf(assetId);
        require(to != owner, "DIGIT: approval to current owner");
        require(msg.sender == owner, "DIGIT: approve caller is not owner nor approved for all");
        _assetApprovals[assetId] = to;
        emit Approval( owner, to, assetId);
    }
    
    function getApproved(uint256 assetId) public view returns (address) {
        require(_exists(assetId), "DIGIT: approved query for nonexistent asset");
        return _assetApprovals[assetId];
    }
    
    function setApprovalForAll(address to, bool approved) public whenNotSuspended { //用户想将其所有的视频转移到另一个平台
        require(to != msg.sender, "DIGIT: approve to caller");
        _operatorApprovals[msg.sender][to] = approved;
        // emit ApprovalForAll( msg.sender, to, approved);
    }
    
    function isApprovedForAll(address owner, address operator) public view returns (bool) {
        return _operatorApprovals[owner][operator];
    }

    //根据资产的id返回资产的物理存储地址
    function assetURI(uint256 assetId) external view returns (string memory) {
        require(_exists(assetId), "DIGITMetadata: URI query for nonexistent asset");
        return _assetURI[assetId];
    }
    
    // function issueWithAssetURI(address to, string memory assetURI, bytes data)public onlyIssuer returns(bool){ //增发资产
    function issueWithAssetURI(address to, string memory assetURI, string assetTopic, bytes data)public returns(bool){ //增发资产
        uint256 lastAssetIndex = _allAssets.length;
        uint256 assetId;
        if(lastAssetIndex <= 0){
            assetId = 1;
        }else{
            assetId = _allAssets[lastAssetIndex - 1] + 1; //资产的id依次递增
        }
        _issue( msg.sender, assetId, data);
        _setAssetURI(assetId, assetURI);
        _assetApprovals[assetId] = to;
        _assetTopic[assetId] = assetTopic;
        emit Approval( msg.sender, to, assetId);
        Create(msg.sender, assetId);
        return true;
        
    }
    /* ly edition
    function issueWithAssetURI(address to, string memory assetURI, string assetTopic, bytes data)public returns(bool){ //增发资产
        uint256 lastAssetIndex = _allAssets.length;
        uint256 assetId;
        if(lastAssetIndex <= 0){
            assetId = 1;
        }else{
            assetId = _allAssets[lastAssetIndex - 1] + 1; //资产的id依次递增
        }
        _issue( to, assetId, data);
        _setAssetURI(assetId, assetURI);
        _assetApprovals[assetId] = msg.sender;
        _assetTopic[assetId] = assetTopic;
        emit Approval( to, msg.sender, assetId);
        Create(to, assetId);
        return true;
        
    }*/
    
    function _issue( address to, uint256 assetId, bytes data) internal {
        require(to != address(0), "DIGIT: mint to the zero address");
        require(!_exists(assetId), "DIGIT: asset already minted");

        _assetOwners[assetId] = to;
        // _ownedAssetsCount[to].increment();
        _balances[to] += 1;
        
        _addAssetToOwnerEnumeration(to, assetId);

        _addAssetToAllAssetsEnumeration(assetId);
    }
    
    function _setAssetURI(uint256 assetId, string memory uri) internal {
        require(_exists(assetId), "DIGITMetadata: URI set of nonexistent asset");
        _assetURI[assetId] = uri;
    }
    
    //销毁资产，只有用户和监管方可执行
    function destroy(uint256 assetId, bytes data) public{
        address _owner = ownerOf(assetId);
        require(_owner != address(0), "DIGIT: could destroy nonexist asset");
        _destroy(_owner, msg.sender, assetId, data);
        emit Destroy(_owner, assetId);
    }
    
    function _destroy(address owner, address operator, uint256 assetId, bytes data) internal{
        require(_assetOwners[assetId] == owner || isSuspender(operator), "DIGIT: destroy of asset that is not own");
        require(_balances[owner] > 0, "DIGIT: user do not have encough asset!");
        _clearApproval(assetId);
        
        _clearHistory(assetId);
        
        _balances[owner] -= 1;
        // _assertAttribution[assetId] = address(0);
        _assetOwners[assetId] = address(0);
        
        if(bytes(_assetURI[assetId]).length != 0){
            delete _assetURI[assetId];
        }

        _removeAssetFromOwnerEnumeration(owner, assetId);
        
        _ownedAssetsIndex[assetId] = 0;

        _removeAssetFromAllAssetsEnumeration(assetId);
    }
    
    function _clearHistory(uint256 assetId) private{ //清除播放量和点赞量
        if(_exists(assetId)){
            // delete _assetLike[assetId];
            // delete _assetPlay[assetId];
        }
    }
    
    function _clearApproval(uint256 assetId) private { //清除授权记录
        if (_assetApprovals[assetId] != address(0)) {
            _assetApprovals[assetId] = address(0);
        }
    }
    
    function _addAssetToAllAssetsEnumeration(uint256 assetId) private { //在资产列表增加资产
        _allAssetsIndex[assetId] = _allAssets.length;
        _allAssets.push(assetId);
    }
    
    function _addAssetToOwnerEnumeration(address to, uint256 assetId) private { //在用户资产列表增加资产
        _ownedAssetsIndex[assetId] = _ownedAssets[to].length;
        _ownedAssets[to].push(assetId);
    }
    
    function _removeAssetFromOwnerEnumeration(address from, uint256 assetId) private { //从用户的资产列表中移除

        uint256 lastAssetIndex = _ownedAssets[from].length - 1;
        uint256 assetIndex = _ownedAssetsIndex[assetId];

        if (assetIndex != lastAssetIndex) {
            uint256 lastAssetId = _ownedAssets[from][lastAssetIndex];

            _ownedAssets[from][assetIndex] = lastAssetId;
            _ownedAssetsIndex[lastAssetId] = assetIndex;
        }

        _ownedAssets[from].length--;

    }
    
    function _removeAssetFromAllAssetsEnumeration(uint256 assetId) private { //从所有的资产列表移除

        uint256 lastAssetIndex = _allAssets.length - 1;
        uint256 assetIndex = _allAssetsIndex[assetId];

        uint256 lastAssetId = _allAssets[lastAssetIndex];

        _allAssets[assetIndex] = lastAssetId;
        
        _allAssetsIndex[lastAssetId] = assetIndex;
        
        _allAssets.length--;
        _allAssetsIndex[assetId] = 0;
    }
    
    function _exists(uint256 assetId) internal returns (bool){ //某资产是否存在
        address owner = _assetOwners[assetId];
        return owner != address(0);
    }
}
