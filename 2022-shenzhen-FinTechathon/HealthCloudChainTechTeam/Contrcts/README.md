# 健康云链智能合约接口文档

## **Hebao.sol**

**保险核保**是保险公司对投保人的投保申请进行审查、核定和选择风险的过程。保险核保是保险承保的前提，是保险人处理业务的第一步，是保障保险公司稳定经营的条件。我们通过计算每一位投保人的投保分值，对是否接受投保人的投保申请进行评估，对高于一般的的危险，应提高费率或向投保人提出修改建议，使其改进后承保。

### Variables

  // 病种

```
enum MedicalHistory {
        JiXingBingDuXingGanYan, // 急性病毒性肝炎
        JiXingJiaXingGanYan, // 急性甲型肝炎
        JiXingYiXingGanYan, // 急性乙型肝炎
        JiXingBingXingGanYan, // 急性乙型肝炎
        JiXingDingXingGanYan, // 急性丁型肝炎
        JiXingWuXingGanYan, // 急性戊型肝炎
        ManXingBingDuXingGanYan, // 慢性病毒性肝炎
        ManXingYiXingGanYan, // 慢性乙型肝炎
        ManXingBingXingGanYan, // 慢性乙型肝炎
        ManXingDingXingGanYan, // 慢性丁型肝炎
        ManXingWuXingGanYan // 慢性戊型肝炎
    }
```

### Mapping

  // 病种分值表

```
constructor() public {
        mapScore[(uint256)(MedicalHistory.JiXingBingDuXingGanYan)] = 5; // 急性病毒性肝炎分值为5
        mapScore[(uint256)(MedicalHistory.JiXingJiaXingGanYan)] = 10;
        mapScore[(uint256)(MedicalHistory.JiXingYiXingGanYan)] = 7;
        mapScore[(uint256)(MedicalHistory.JiXingBingXingGanYan)] = 20;
        mapScore[(uint256)(MedicalHistory.JiXingDingXingGanYan)] = 15;
        mapScore[(uint256)(MedicalHistory.JiXingWuXingGanYan)] = 69;
        mapScore[(uint256)(MedicalHistory.ManXingBingDuXingGanYan)] = 3;
        mapScore[(uint256)(MedicalHistory.ManXingYiXingGanYan)] = 35;
        mapScore[(uint256)(MedicalHistory.ManXingBingXingGanYan)] = 25;
        mapScore[(uint256)(MedicalHistory.ManXingDingXingGanYan)] = 10;
        mapScore[(uint256)(MedicalHistory.ManXingWuXingGanYan)] = 5;
    }

    event eventResult(string data, int256 score);
```

### Functions

##### call

```
 function cal()
```

计算分值

```
 function cal(
        string name, // 姓名
        string phone, // 电话
        uint8 age, // 年龄
        string memory data // 既往病史。长度必须是MedicalHistory。索引中0表示出现，1表示不出现，比如"10000000001"表示出现JiXingBingDuXingGanYan,ManXingWuXingGanYan
    )
        public
        returns (
            int256 // 分值
        )
    {
        bytes memory databytes = bytes(data);
        require(
            databytes.length ==
                (uint256)(MedicalHistory.ManXingWuXingGanYan) + 1,
            "wrong data size"
        );

        // 计算年龄
        int256 score = 100; // 总分100
        if (50 < age) {
            score = score - 20;
        } else if (40 < age) {
            score = score - 10;
        } else if (30 < age) {
            score = score - 5;
        }

        // 计算既往病史
        bytes1 bt1 = "1";
        for (
            uint256 index = (uint256)(MedicalHistory.JiXingBingDuXingGanYan);
            index < (uint256)(MedicalHistory.ManXingWuXingGanYan) + 1;
            index++
        ) {
            if (databytes[index] == bt1) {
                score = score - (int256)(mapScore[index]);
                if (score <= 0) {
                    score = 0; // 最低扣分到0
                    break;
                }
            }
        }
        emit eventResult(data, score);
        return score;
    }

    // function lenOfChars(string memory src) internal pure returns (uint256) {
    //     uint256 i = 0;
    //     uint256 length = 0;
    //     bytes memory string_rep = bytes(src);
    //     //UTF-8 skip word
    //     while (i < string_rep.length) {
    //         i += utf8CharBytesLength(string_rep, i);
    //         length++;
    //     }
    //     return length;
    // }
```

## IERC721.sol

我们的保单存证合约方面的接口定义如下：

```
/**
 * @dev Required interface of an ERC721 compliant contract.
 */
contract IERC721 is IERC165 {

/**
  * @dev Emitted when `tokenId` token is transferred from `from` to `to`.
  *当保单的所有权更改时（不管哪种方式，最终都会调用该事件），就会触发Transfer。
  */
  event Transfer(
    address indexed from,
    address indexed to,
    uint256 indexed tokenId
  );

/**
  * @dev Emitted when `owner` enables `approved` to manage the `tokenId` token.
  *当更改或确认保单的授权地址时触发。approved参数表示被授权可以转移保单的地址。
  *approved为零地址表示取消授权的地址；发生 Transfer 事件时，该保单的授权地址（如果有）被重置为“无”（零地址）。
  */
  event Approval(
    address indexed owner,
    address indexed approved,
    uint256 indexed tokenId
  );

/**
  * @dev Emitted when `owner` enables or disables (`approved`) `operator` to manage all of its assets.
  *保单所有者启用或禁用操作员时触发。即指定或取消可管理整个保单的operator操作员。（操作员可管理保单所有者所持有的保单）
  */
  event ApprovalForAll(
    address indexed owner,
    address indexed operator,
    bool approved
  );

/**
  * @dev Returns the number of tokens in ``owner``'s account.
  *统计owner地址所持有的保单数量
  */
  function balanceOf(address owner) public view returns (uint256 balance);

/**
  * @dev Returns the owner of the `tokenId` token.
  *返回指定保单的所有者
  * Requirements:
  * - `tokenId` must exist.
  */
  function ownerOf(uint256 tokenId) public view returns (address owner);

/**
  * @dev Gives permission to `to` to transfer `tokenId` token to another account.
  * 更改或确认保单的授权地址
  * Requirements:
  *
  * - The caller must own the token or be an approved operator.
  * - `tokenId` must exist.
  *
  * Emits an {Approval} event.
  */
  function approve(address to, uint256 tokenId) public;

/**
  * @dev Returns the account approved for `tokenId` token.
  *获取单个保单的授权地址，也反映出一个tokenId只能同时授权给一个operator。
  * Requirements:
  * - `tokenId` must exist.
  */
  function getApproved(uint256 tokenId)
    public view returns (address operator);

/**
  * @dev Approve or remove `operator` as an operator for the caller.
  *启用或禁用第三方（操作员）管理 msg.sender 所有资产，触发 ApprovalForAll 事件。
  *Operators can call {transferFrom} or {safeTransferFrom} for any token owned by the caller.
  * Requirements:
  * - The `operator` cannot be the caller.
  * Emits an {ApprovalForAll} event.
  */
  function setApprovalForAll(address operator, bool _approved) public;

/**
  * @dev Returns if the `operator` is allowed to manage all of the assets of `owner`.
  * 查询一个地址是否是另一个地址的授权操作员
  *See {setApprovalForAll}
  */
  function isApprovedForAll(address owner, address operator)
    public view returns (bool);

/**
  * @dev Transfers `tokenId` token from `from` to `to`.
  *
  * WARNING: Usage of this method is discouraged, use {safeTransferFrom} whenever possible.
  *
  * Requirements:
  *转移所有权 – 调用者负责确认_to是否有能力接收保单，否则可能永久丢失。
  */
  function transferFrom(
      address from,
      address to,
      uint256 tokenId
  ) public;

/**
  * @dev Safely transfers `tokenId` token from `from` to `to`, checking first that contract recipients
  * are aware of the ERC721 protocol to prevent tokens from being forever locked.
  *
  * Requirements:
  *将保单的所有权从一个地址转移到另一个地址，功能同上，不带data参数。
  */
  function safeTransferFrom(
      address from,
      address to,
      uint256 tokenId
  ) public;
/**
  * @dev Safely transfers `tokenId` token from `from` to `to`.
  * Requirements:
  *将保单的所有权从一个地址转移到另一个地址，
  *data : 附加额外的参数（没有指定格式），传递给接收者。
  */
  function safeTransferFrom(
    address from,
    address to,
    uint256 tokenId,
    bytes data
  ) public;
}
```

## **IERC721Metadata.sol**

IERC721Metadata 接口用于提供合约的元数据：name , symbol 及 URI（保单所对应的资源）

其接口定义如下：

```
interface ERC721Metadata /* is ERC721 */ {
    function name() external pure returns (string _name);
    function symbol() external pure returns (string _symbol);
    function tokenURI(uint256 _tokenId) external view returns (string);
}
```

##### 接口说明：

- name(): 返回合约名字
- symbol(): 返回我们的保单符号
- tokenURI(): 返回_tokenId所对应的外部资源文件的URI

## **IERC721Enumerable.sol**

IERC721Enumerable的主要目的是提高合约中我们数字保单的可访问性，其接口定义如下：

```
contract IERC721Enumerable is IERC721 {
  function totalSupply() public view returns (uint256);
  function tokenOfOwnerByIndex(
    address owner,
    uint256 index
  )
  public view returns (uint256 tokenId);
  function tokenByIndex(uint256 index) public view returns (uint256);
}
```

##### 接口说明：

- totalSupply(): 返回保单总量
- tokenByIndex(): 通过索引返回对应的tokenId
- tokenOfOwnerByIndex(): 所有者可以一次拥有多个的保单，此函数返回_owner拥有的全保单列表中对应索引的tokenId
