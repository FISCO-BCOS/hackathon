# Sentichain需求说明及接口文档

Status: Done 🙌

<aside>
💡 高韬    伍嘉桓     2023.11.15
本报告是针对微众金融科技大赛项目‘二手物品流转平台’的业务逻辑拆解得出的需求说明及接口文档。需求分析将按照合约→前后端的顺序，来整理哪些信息需要上链，产品需要开发哪些功能，对应的是哪些页面和接口，后端数据库应该如何设计，前后端联调的出入参应该如何规定。本报告将对这些内容进行整理与分析。


</aside>

![Untitled](Sentichain%E9%9C%80%E6%B1%82%E8%AF%B4%E6%98%8E%E5%8F%8A%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A3%20ac2ddb172ca94a8785b59921870b6eb9/Untitled.jpeg)

![Untitled](Sentichain%E9%9C%80%E6%B1%82%E8%AF%B4%E6%98%8E%E5%8F%8A%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A3%20ac2ddb172ca94a8785b59921870b6eb9/Untitled.png)

![Untitled](Sentichain%E9%9C%80%E6%B1%82%E8%AF%B4%E6%98%8E%E5%8F%8A%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A3%20ac2ddb172ca94a8785b59921870b6eb9/Untitled%201.png)

# 流程分析

1. 卖家发布商品->平台审核->商品信息上链->推荐给潜在的买家(商品发布)
2. 买家确认购买商品->付款->卖家确认收款->平台确认收款->买家确认收货->成交信息上链->买家获取积分(商品交易)
3. 用户对商品发表评论->平台审核->情绪故事上链->用户获取积分(或者说当商品的故事引起足够的共鸣之后才能获得积分)->推荐给其他用户(故事分享)

# 合约设计

<aside>
💡 一共有两个合约，分别是积分合约以及商品合约。积分合约针对共创者经济模块，而商品合约则是针对交易与情绪模块。


每个用户在链上都会有个地址，在用户注册的时候就会分配一个地址，然后在合约、数据库中都以地址来代表用户。

</aside>

下面我将对积分合约以及商品合约中需要有哪些变量，结构体以及函数做统筹，并对每个函数要实现的功能进行阐述。

## 积分合约

类似erc20

variables：mapping add2bal 地址（账户）→ 积分余额

functions：

1. addAcc 添加新地址。往mapping中添加一个新的地址，并为它的积分余额赋值为0.
2. addBal 增加mapping中某个地址的积分余额。根据接收到的地址与增加的积分数量，增加mapping中对应地址的积分余额。修改成功后返回布尔值true，否则返回false。
3. reduceBal 减少mapping中某个地址的积分余额。根据接收到的地址与减少的积分数量，减少mapping中对应地址的积分余额。修改成功后返回布尔值true，否则返回false。
4. indexBalByAdd 查询某个地址的积分余额。根据接收到的地址，检索mapping中该地址的积分余额并返回。

## 商品合约

类似erc721

variables：mapping id2Goods （tokenId ↔ struct goodsInfo）

goodsInfo包括：

1. string story 记录了每个商品与主人的故事，将是一个200字-300字的长字符串
2. uint goodsAndStoryTIme 商品与故事发布的时间戳
3. uint goodsId 商品在数据库中的id
4. struct basicInfo 商品基本信息
   1. string imgUrl 图片链接
   2. address owner 该商品目前的主人
   3. string goodsName 商品名称
   4. string goodsDescrip 商品描述
5. struct dealInfo 商品成交信息
   1. uint dealPrice 成交价格 
   2. Date dealTime 成交时间
   3. address buyer 买方 
   4. address seller 卖方 
6. bool isSold 是否已卖出

functions：

1. indexStoryById 查询目前记在链上的所有商品的故事。根据tokenid遍历所有商品的struct，将每个商品记录的故事收集在一起后统一返回。
2. createTrade 将达成的交易信息上链，修改某个已经在链上创建的商品的成交信息。接收后端传来的商品id，卖家的地址，买家的地址，成交的价格。根据商品id，在id2Goods找到对应的商品，首先把owner由原来的地址修改为买家的地址，然后把买家的地址，卖家的地址，成交价格都赋给相应的变量，同时按照此时的区块时间记录为成交时间。最后把isSold修改为true，代表已卖出。
3. createGoods 在合约中创建一个新的商品。接收后端传来的商品id，故事，还有各项商品基本信息，并赋值给相应的变量，其余暂未接收到的变量则初始化为空或0。isSold初始化为false。
4. indexBybuyer 查询某个用户买过的商品。接收后端传入的地址参数，然后索引id2Goods，将成交信息中buyer变量符合该地址的商品id收集起来并返回。
5. indexByseller 查询某个用户卖过的商品。接收后端传入的地址参数，然后索引id2Goods，将成交信息中seller变量符合该地址的商品id收集起来并返回。
6. indexStoryById 查询某个商品发布的时间，接收后端传入的商品id，返回该商品/故事发布的时间。

# 前后端设计

<aside>
💡 主要从产品需要哪些页面出发，来对前端的ui设计，以及后端的接口设计进行讨论


</aside>

整个web应用在页面顶部有一个tab标签栏，通过该tab栏来控制在四个页面间的跳转。下面是关于四个子页面的详细需求说明。

## 1 ‘好物列表’页面

首页。首页的设计原则是体现产品的最重要的功能，在这里也就是用户获取二手物品信息、购买二手物品。其次是整体的UI设计要流畅丝滑，重视用户体验，给用户留下深刻印象。此页面一共包括两个部分，分别是搜索栏和商品展示栏。搜索栏负责收集用户的需求并传给后端，商品展示栏负责展示后端在接受用户需求后，在数据库中查找后返回的商品列表。

### 搜索栏

用户在搜索栏输入需求、关键词，当点击搜索按钮后，搜索栏组件应该将用户需求发送给相应的后端接口页面，并获取后端返回的数据，在商品展示栏返回相关的产品推荐。

### 商品展示栏

主要功能是展示商品，默认展示目前所有的商品。当用户输入需求后，商品展示栏会根据后端返回的数据重新渲染商品展示栏。每个商品可以做成一个单独的商品组件，然后在商品展示栏内，每个商品都是单独成块的，会展示商品的信息，包括商品的图片，名称，以及价格。

另外，点击列表中的某个商品时，会弹进商品详情页。商品详情页会展示该商品的信息，包括故事、基本信息，期望价格等。点击列表时，会根据商品id，向商品详情接口发请求，获取商品的详情信息，然后对商品详情页进行渲染。

在商品详情页还需要两个额外功能，与商家交流，以及交易商品。在商品详情页的底部，有两个按钮，分别就是交流以及交易功能。当我们点击交流按钮，会弹出一个模态框，模仿微信聊天框的你一句我一句的消息流；当我们点击交易按钮，会向交易接口发请求，如果接收到后端返回的true信息，则在前端提示交易已完成。

## 2 ‘我要发布’

用户会通过这个页面来发售自己的物品。首先用户要先填写物品信息的表单，表单项包括：
pic:图片,

extent:磨损程度，

price:价格,

story:故事,

description:描述,

name:商品名称

填写完表单信息之后，页面底部有个确认发布的按钮，点击之后会弹出一个modal框，要求用户填写自己的私钥已确认是否要发布该商品的售卖信息。用户点击确认后，向商品发布接口发送信息，并在将信息入库、上链后返回发布成功的信息。前端在接到接口的返回信息后提示用户商品已发布，并返回buypage首页。

## 3 ‘情绪寄存’

用户可以通过这个页面来查看质量最高、评论、点赞数最多的故事。整个页面其实就是一个故事展示栏，然后每一行展示一个故事卡片，如果内容溢出就添加滑动条。故事卡片的顶部是商品名称，发布的用户，中间是用户与这件商品之间的故事，底部是商品的发布时间。另外，每张故事卡片都是可点击的，鼠标hover卡片要有显示特效。点击故事卡片，可以根据这个故事绑定的商品id，进而跳转到商品详情页面，查看该商品的详细信息。

## 4 ’个人信息‘

用户可以通过这个页面查看自己的个人信息，包括用户的名称，用户的公钥地址，用户的积分，用户发布过的商品，以及用户购买的商品。其中还包括用户的名称，积分，发布过的商品，买过的东西都是前端页面在加载的时候，通过用户id向后端发请求，得到响应数据后渲染的。然后用户发布过的商品以及购买的商品都是可以点击的模块，点击后可查看发布商品以及购买商品的详情列表，在详情列表里，可以查看到购买的商品的名称，简要叙述以及价格。点击商品卡片可以再进入商品详情页面。

# 接口出入参

(GET)返回商品列表(81.71.5.116:9090/goodsitem)

入参：无

返回结果：所有商品的名称，期望价格，图片？

```json
{
    "commoditieslist": [
        {
            "CommodityID": 1,
            "Extent": 0.3,
            "ExpectPrice": 1005.6,
            "Pic": "https://www.haozekeji.com/d/file/2022/07/20220707140753_6c3d0005528dffecbf91.png.png",
            "CommodityName": "原神手办",
            "Time": "2023-11-24T14:35:02+08:00",
            "CommodityDescription": "我去,原深",
            "StoryDetail": "这是一个搞笑的故事",
            "PublicAddress": "b20b7e23ee51e804b4de698d60127abc329a1f53ac701338f40365ec39918c86ef4c63ce2f7ebc7b73db969891318107ceb452ad736ebdb800f94f8b3f8071dc"
        },
        {
            "CommodityID": 2,
            "Extent": 0.75,
            "ExpectPrice": 15000,
            "Pic": "http://pic-1304105328.cos.ap-guangzhou.myqcloud.com/commodity1.jpg",
            "CommodityName": "可爱的小玩具",
            "Time": "2023-11-25T03:38:48+08:00",
            "CommodityDescription": "女生自用，九九新",
            "StoryDetail": "不敢说的秘密",
            "PublicAddress": "b20b7e23ee51e804b4de698d60127abc329a1f53ac701338f40365ec39918c86ef4c63ce2f7ebc7b73db969891318107ceb452ad736ebdb800f94f8b3f8071dc"
        },
        {
            "CommodityID": 3,
            "Extent": 0.99,
            "ExpectPrice": 600000,
            "Pic": "http://pic-1304105328.cos.ap-guangzhou.myqcloud.com/commodity2.jpg",
            "CommodityName": "神仙熊",
            "Time": "2023-11-25T04:00:40+08:00",
            "CommodityDescription": "女生自用，九九新",
            "StoryDetail": "不敢说的秘密",
            "PublicAddress": "b20b7e23ee51e804b4de698d60127abc329a1f53ac701338f40365ec39918c86ef4c63ce2f7ebc7b73db969891318107ceb452ad736ebdb800f94f8b3f8071dc"
        },
        {
            "CommodityID": 4,
            "Extent": 0.99,
            "ExpectPrice": 300000,
            "Pic": "http://pic-1304105328.cos.ap-guangzhou.myqcloud.com/commodity3.jpg",
            "CommodityName": "一对金银神仙熊",
            "Time": "2023-11-25T05:40:55+08:00",
            "CommodityDescription": "好看",
            "StoryDetail": "你懂得",
            "PublicAddress": "b20b7e23ee51e804b4de698d60127abc329a1f53ac701338f40365ec39918c86ef4c63ce2f7ebc7b73db969891318107ceb452ad736ebdb800f94f8b3f8071dc"
        }
    ]
}
```

(GET)返回商品详情(81.71.5.116:9090/goodsdetail?commodityid=)

入参：商品id(int类型(1,2,3))

出参：所有商品信息

```json
{
    "CommodityID": 1,
    "Extent": 0.3,
    "ExpectPrice": 1005.6,
    "Pic": "https://www.haozekeji.com/d/file/2022/07/20220707140753_6c3d0005528dffecbf91.png.png",
    "CommodityName": "原神手办",
    "Time": "2023-11-24T14:35:02+08:00",
    "CommodityDescription": "我去,原深",
    "StoryDetail": "这是一个搞笑的故事",
    "PublicAddress": "b20b7e23ee51e804b4de698d60127abc329a1f53ac701338f40365ec39918c86ef4c63ce2f7ebc7b73db969891318107ceb452ad736ebdb800f94f8b3f8071dc"
}
```

(POST)达成交易(81.71.5.116:9090/confirmorder)

入参：**价格，成交双方的地址**

返回结果:json数据

(POST)发布商品的信息(81.71.5.116:9090/releasegoods)

入参：用户的用户公钥地址，磨损程度，价格，**故事，图片**

![微信截图_20231126001233.png](Sentichain%E9%9C%80%E6%B1%82%E8%AF%B4%E6%98%8E%E5%8F%8A%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A3%20ac2ddb172ca94a8785b59921870b6eb9/%25E5%25BE%25AE%25E4%25BF%25A1%25E6%2588%25AA%25E5%259B%25BE_20231126001233.png)

出参：是否发布成功(json格式)

{

“msg”:”发布成功”

}

(GET)返回故事列表(81.71.5.116:9090/storiesitem)

入参：无

出参：所有故事，以及每个故事它发布的时间和发布用户地址

```
{
    "storieslist": [
        {
            "StoryID": 1,
            "StoryDetail": "这是一个搞笑的故事",
            "CID": 1,
            "Time": "2023-11-24T15:01:48+08:00",
            "PublicAddress": "b20b7e23ee51e804b4de698d60127abc329a1f53ac701338f40365ec39918c86ef4c63ce2f7ebc7b73db969891318107ceb452ad736ebdb800f94f8b3f8071dc"
        },
        {
            "StoryID": 2,
            "StoryDetail": "你懂得",
            "CID": 4,
            "Time": "2023-11-25T05:40:55+08:00",
            "PublicAddress": "b20b7e23ee51e804b4de698d60127abc329a1f53ac701338f40365ec39918c86ef4c63ce2f7ebc7b73db969891318107ceb452ad736ebdb800f94f8b3f8071dc"
        }
    ]
}
```

(GET)根据故事id跳转商品详情(81.71.5.116:9090/togoodsdetail?storyid=)

入参：故事id(int类型)

出参：根据商品id查出来的商品详情返回给前端

```json
{
    "CommodityID": 1,
    "Extent": 0.3,
    "ExpectPrice": 1005.6,
    "Pic": "https://www.haozekeji.com/d/file/2022/07/20220707140753_6c3d0005528dffecbf91.png.png",
    "CommodityName": "原神手办",
    "Time": "2023-11-24T14:35:02+08:00",
    "CommodityDescription": "我去,原深",
    "StoryDetail": "这是一个搞笑的故事",
    "PublicAddress": "b20b7e23ee51e804b4de698d60127abc329a1f53ac701338f40365ec39918c86ef4c63ce2f7ebc7b73db969891318107ceb452ad736ebdb800f94f8b3f8071dc"
}
```

(GET)返回我卖的商品列表(81.71.5.116:9090/goodsitemsell?address=)

入参：用户公钥地址

出参：buyer为该地址的商品及其信息

(GET)返回我买的商品列表(81.71.5.116:9090/goodsitembuy?address=)

入参：用户公钥地址

出参：seller为该地址的商品及其信息

(GET)查询积分(81.71.5.116:9090/searchscore?address=)

入参：用户公钥地址

出参：用户在链上积分的数量

{
"balance": 10
}

(GET)返回故事id的评论内容内容(81.71.5.116:9090/searchcomment?storyid=)

入参: 故事id

出参：评论内容，评论时间，用户地址

(POST)发布评论内容(81.71.5.116:9090/releasecomment?storyid=&useraddress=&comment=)

入参：故事id,故事内容，用户地址

出参: 状态码

# 表设计

## 商品表

**commodityId**，公钥地址（用fisco的getaccount函数为每个用户初始化一个地址），磨损程度（float），卖家期望价格（expected price， float），story？，图片？（是放在合约里像nft一样放链接，还是干脆就存在数据库里，就是把图片转换成三维数组存储在链上，检索出来后重新渲染）

## 故事表

故事Id，公钥地址，发布的时间，商品id

图片可以直接放在服务器上，别人通过公网IP直接访问服务器图片资源（需要查看教程）

可以免费使用腾讯的COS服务将图片上传至COS服务中供外网访问([https://cloud.tencent.com/act/pro/free1month](https://cloud.tencent.com/act/pro/free1month))

## 评论表

记录每个故事的评论
