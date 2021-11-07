## Evidence 接口说明

Evidence 为存证合约，服务于证书中需要存证的内容。

EvidenceFactory 为主入口，主要用到的方法列表如下：

- constructor

  构造函数。

  参数：

  > - evidenceSigners: Address List 合法签名人列表。

- newEvidence

  新建存证，自动生成存证 key（Address ）。

  参数：

  > - evi: String 存证内容

- getEvidence

  获取存证内容。

  参数：

  > - addr: Address 存证Key

- addSignatures

  添加签名，用于多签存证场景下。

  参数：

  > - addr: Address 存证Key

## EvidencePlus 接口说明

升级版存证合约，适用于部分需要自己指定 EvidenceKey的场景。

- newEvidenceByKey 

  将上述 Evidence 中的 newEvidence 方法升级为 newEvidenceByKey 方法。

  参数：

  > - evi: String 存证内容
  >
  > - key: String 存证 key

- getEvidenceByKey 

将上述 Evidence 中的 getEvidence 方法升级为 getEvidenceByKey 方法。

参数：

> - key: String 存证 key



## ERC721Badges 接口说明

Erc721Badges 为数字徽章合约。

主要用到的方法列表如下：

- **mintNFT**

  修饰符：onlyOwner

  生成数字徽章。

  参数：

  > - receiver: Address 徽章接收人
  > - tokenURI: String 徽章样式资源
  > - isTransfer: Bool 是否可转移

  

- **transferTo**

  徽章转移。

  参数：

  > - to: Address 徽章接收人
  > - tokenId: uint256 徽章编号

- **tokenURI**

  获取徽章对应资源。

  参数：

  > - tokenId: uint256 徽章编号

- **ownerOf**

  获取徽章所有人地址。

  参数：

  > - tokenId: uint256 徽章编号

## WeIdentity 接口说明

见 WeIdentity 官方文档：

> weidentity.readthedocs.io

WeIdentity 合约主要被以下两个组件调用：

- [weid-build-tools](https://github.com/WeBankFinTech/WeIdentity-Build-Tools)
- [weid-rest-service](https://github.com/WeBankFinTech/WeIdentity-Rest-Service)