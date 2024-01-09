// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

// 商品合约
contract GoodsContract {
    //商品的基本信息
    struct BasicInfo {
            string imgUrl; // 图片链接
            address owner; // 商品目前的主人
            string goodsName; // 商品名称
            string goodsDescrip; // 商品描述
    }
    //商品的成交信息
    struct DealInfo {
            uint256 dealPrice; // 成交价格
            uint256 dealTime; // 成交时间（以区块时间为准）
            address buyer; // 买方
            address seller; // 卖方
    }
    struct GoodsInfo {
        string story; // 商品与主人的故事
        uint256 goodsId; // 商品在数据库中的id
        uint256 goodsAndStoryTime; // 商品的发布时间戳
        BasicInfo basicInfo;
        DealInfo dealInfo;
        bool isSold; // 是否已卖出
    }
    uint256 tokenId;//目前账本里的商品数量
    mapping(uint256 => GoodsInfo) public id2Goods; // tokenId ↔ 商品信息

    constructor(){
        tokenId=0;
    }
    // 查询商品发布时间戳
    function indexBlockTimeById(uint256 gId) public view returns (uint256) {
        uint256 id = indexBygid(gId);
        return id2Goods[id].goodsAndStoryTime;
    }
    // 查询目前记在链上的所有商品的故事
    function indexStoryById() public view returns (string[] memory) {
        string[] memory stories = new string[](tokenId);
        for (uint256 i = 0; i < tokenId; i++) {
            stories[i] = id2Goods[i].story;
        }
        return stories;
    }
    //查询账本中是否存在某个商品
    function isGoodExist(uint256 gId) public view returns (bool) {
        for (uint256 i = 0; i <tokenId; i++) {
            if (id2Goods[i].goodsId==gId) {
                return true;
            }
        }
        return false;
    }
    //根据商品id检索tokenId
    function indexBygid(uint256 gId) public view returns (uint256) {
        for (uint256 i = 0; i <tokenId; i++) {
            if (id2Goods[i].goodsId==gId) {
                return i;
            }
        }

        return 0;
    }
    // 将达成的交易信息上链，修改某个已经在链上创建的商品的成交信息
    function createTrade(
        uint256 gId,
        address buyer,
        uint256 dealPrice
    ) public {
        require(isGoodExist(gId), "Invalid goodsId"); // 确保商品存在
        uint256 id=indexBygid(gId);
        GoodsInfo storage goods = id2Goods[id];
        goods.dealInfo.buyer = buyer;
        goods.dealInfo.seller = goods.basicInfo.owner;
        goods.dealInfo.dealPrice = dealPrice;
        goods.dealInfo.dealTime = block.timestamp;
        goods.basicInfo.owner = buyer;
        goods.isSold = true;
    }

    // 在合约中创建一个新的商品
    function createGoods(
        uint256 gId,
        string memory story,
        string memory imgUrl,
        string memory goodsName,
        string memory goodsDescrip,
        address owner
    ) public {
        require(!isGoodExist(gId), "TokenId already exists"); // 确保商品不存在
        GoodsInfo storage goods = id2Goods[tokenId];
        tokenId++;
        goods.story = story;
        goods.goodsId = gId;
	goods.goodsAndStoryTime = block.timestamp; // 使用区块时间作为发布时间戳
        goods.basicInfo.imgUrl = imgUrl;
        goods.basicInfo.owner = owner;
        goods.basicInfo.goodsName = goodsName;
        goods.basicInfo.goodsDescrip = goodsDescrip;
        goods.isSold = false;
    }

    // 查询某个用户买过的商品
    function indexByBuyer(address buyer) public view returns (uint256[] memory) {
        uint256[] memory buyerGoods=new uint256[](tokenId);
        uint256 count = 0;
        for (uint256 i = 0; i < tokenId; i++) {
            if (id2Goods[i].dealInfo.buyer == buyer) {
                buyerGoods[count] = id2Goods[i].goodsId;
                count++;
            }
        }
        return buyerGoods;
    }

    // 查询某个用户卖过的商品
    function indexBySeller(address seller) public view returns (uint256[] memory) {
        uint256[] memory sellerGoods=new uint256[](tokenId);
        uint256 count = 0;
        for (uint256 i = 0; i < tokenId; i++) {
            if (id2Goods[i].dealInfo.seller == seller) {
                sellerGoods[count] = id2Goods[i].goodsId;
                count++;
            }
        }
        return sellerGoods;
    }
    //查询目前未被卖出的商品
    function indexGoodsonList() public view returns(uint256[] memory){
        uint256[] memory goodsOnList=new uint256[](tokenId);
        uint256 count=0;
        for(uint i=0;i<tokenId;i++){
            if(id2Goods[i].isSold == false){
                goodsOnList[count]=id2Goods[i].goodsId;
                count++;
            }
        }
        return goodsOnList;
    }
}