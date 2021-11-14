pragma solidity ^0.4.25;

contract IDIGIT  {
    event Approval( address indexed owner, address approved, uint256 assetId);

    function name() external view returns (string name); //
    
    function symbol() external view returns (string symbol); //
    
    function quota(address account) external returns(uint256); //根据地址返回广告商拥有的积分额度，广告商根据此积分额度发布广告位
    
    function tokenURI (uint256 assetId) external view returns(string); //返回该代币的存储地址
    
    function totalSupply() external view returns(uint256); //返回当前链上所有的代币数
    
    function assetOfOnwer(address _owner) public view returns(uint256[]); //返回某地址所有资产编号
    
    function tokenOfOwnerByIndex(address _owner, uint256 _index) external view returns (uint256); //返回该地址的第index个代币
    
    function tokenOfOwnerLike(uint256 assetId) external view returns(uint256); //某视频的播放量
    
    function tokenOfOwnerLikeByIndex(uint256 assetId, address _platform) external view returns(uint256); // 返回某视频在某平台的播放量
    
    function tokenOfOwnerPlay(uint256 assetId) external view returns(uint256); //某视频的点赞量
    
    function tokenOfOwnerPlayByIndex(uint256 assetId, address _platform) external view returns(uint256); // 返回某视频在某平台的点赞量
    
    function balanceOf(address owner) external view returns (uint256 balance); //返回某地址（用户）的资产数量
    
    function ownerOf(uint256 assetId) external view returns (address owner);    //返回某资产的归属用户
     
    function approve(address to, uint assetId) public; //用户在某平台将发布视频约等于将视频授权给平台方
    
    function getApprove(uint256 assetId) public view returns(address); //获得资产授权的地址用户
    
    function _setAssetURI(uint256 assetId, string memory uri) internal; //设置某视频的存储位置
    
    function issueWithAssetURI(address to, string memory assetURI, bytes data)public returns(bool);//给地址to创建资产assetId，data是备注，assetURI是资产描述
    
    function destroy(uint256 assetId, bytes data) public; //销毁某个资产（可能用于某些视频下架），只有拥有者本人和监管方可以销毁
    
    
}
