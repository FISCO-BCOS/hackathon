# 安产链智能合约接口文档

## Enterprise.sol
企业合约,为企业提供数据上传借口,并记录企业相关信息
### Variables
```sol
string name; //公司名称

string representative; // 法人

string addr; // 公司地址

string enterpriseType; // 公司类型

string enterpriseLimit;  // 生产领域

string encryptedDataIpfs; // 加密材料ipfs地址

string encryptedDataHash; // 加密材料哈希值

string reportIpfs; // 报告ipfs地址

string reportHash; // 报告哈希值

address licenseAddress; // 许可证合约地址

ReportEvaluation evaluation; // 报告审查合约
```
### Functions

#### setLicense
```sol
function setLicense() 
```
设置许可证合约地址

#### getReport
```sol
function getReport() constant returns(string)
```
获取合约ipfs地址

#### getLicenseAddress

```sol
function getLicenseAddress() constant returns(address)
```
获取许可证合约地址

#### setInformation
```sol
function setInformation(string _name,string _representative,string _addr,string _enterpriseType,string _enterpriseLimit) 
```

| 参数 | 含义 |
|  ---- | ---- |
|_name|公司名称|
|_representative|法人|
|_addr|公司地址|
| _enterpriseType | 公司类型 |
|_enterpriseLimit | 生产领域 |
设置公司基本信息

#### getInformation
```sol
function getInformation() returns(string,string,string,string,string)
```
获取公司基本信息

#### update
```sol
function update(string reporthash,string report,address agency,string [] engineer) returns(address)
```
|参数 | 含义|
|----|----|
|reporthash|报告哈希|
|report|安评报告|
|agency|相关安评机构|
|engineer|相关安评师|
安评机构上传安评相关信息
返回报告评定合约地址


#### updateData
```sol
function updateData(string datahash, string dataipfs)
```
生产企业上传加密数据hash以及ipfs地址

## Agency.sol
安评机构合约，提供安评机构管理功能
### Variables
```sol
address engList //安评师列表合约地址

string agencyCert   //安评机构证书信息 
  
struct businessInfo{    //业务信息
    uint time;  //业务时间
    address licenseAddr;    //业务对应证书地址
    string businessType;   //业务类型
}

businessInfo[] businessList;    //业务信息列表

address creditAddr; //信用合约地址

int credit; //信用分
```
### Functions
####  setAgencyCert
```sol
function setAgencyCert(string cert)
```
设置安评机构证书

#### getAgencyCert
```sol
function getAgencyCert() returns(string)
```
获取安评机构证书

### setEnglistAddr
```sol
function setEngListAddr(address addr)
```
设置安评师列表合约地址

#### getEngListAddr
```sol
function getEnglistAddr() returns(address)
```
获取安评师列表合约地址

#### confirm
```sol
function confirm(address evaluationAddress, string[] id)
```
evaluationAddress为审查合约地址， id为安评师编号列表。 向审查合约提交”审查通过“信息

#### deny
```sol
function deny(address evaluationAddress, string[] id) returns(address)
```
evaluationAddress为审查合约地址，id为安评师编号列表。向审查合约提交“审核拒绝”信息

#### addBusiness
```sol
function addBusiness(string[] id ,uint time,address licenseAddr,string businessType)
```
id为安评师编号列表，time为业务时间，licenseAddr为业务涉及证书地址，businessType为业务类型。 
函数功能：为安评机构和部分安评师添加业务记录

#### showBusiness
```sol
function showBusiness() returns(businessInfo[]) 
```
函数功能：返回安评机构业务列表

#### setCreditAddr
```sol
function setCreditAddr(address addr) 
```
函数功能： 设置信用合约地址

#### getCreditAddr
```sol
function getCreditAddr() returns(address)
```
函数功能： 获取信用合约地址

#### updateCredit
```sol
function updateCredit()
```
函数功能： 更新安评机构信用分

#### getCredit
```sol
function getCredit() returns(int)
```
函数功能： 获取安评机构信用分

## EngineerList.sol
安评师列表合约，提供安评师管理功能
### Variables
```sol
    struct businessInfo{    //业务信息
        uint time;  //业务时间
        address licenseAddr;    //业务涉及证书地址
        string businessType;   //业务类型
    }
    
    struct engineerInfo{    //安评师信息
        string name;    //安评师姓名
        string field;   //安评师所属领域
        string safetyEvaluationCertificate; //安评师证书信息
        string agency;  //安评师所属安评机构
        int credit;     //安评师
        businessInfo[] businessList;    //安评师业务列表
    }

    mapping (string => engineerInfo) engineerMap;   //安评师信息存储Map，以安评师证书id为索引

    address creditAddr; //信用合约地址
```
### Functions
#### addEngineer
```sol
function addEngineer(string _name, string id, string _field,string _cert,string _agency)
```
|  参数   | 含义  |
|  ----  | ----  |
| -name  | 安评师姓名 |
| id | 安评师证书编号|
| _field | 安评师所属领域|
| _cert | 安评师证书信息|
| _agency | 安评师所属安评机构|
函数功能： 根据参数在列表中添加新安评师信息

#### getEngineer
```sol
function getEngineer(string id) returns(engineerInfo)
```
id为安评师证书id，函数功能：获取相应安评师信息

#### deleteAgency
```sol
function deleteAgency(string id )
```
id为安评师证书id，函数功能：将相应安评师“所属安评机构”属性删除

#### setAgency
```sol
function setAgency(string id,string agen)
```
id为安评师证书id，agen为安评机构 函数功能：为不属于任何安评机构的安评师设置"所属安评机构"属性

#### addBusiness
```sol
function addBusiness(string id,uint time,address addr,string bType)
```
|  参数   | 含义  |
|  ----  | ----  |
| id | 安评师证书id |
| time | 业务时间|
| addr | 业务涉及证书地址| 
| bType| 业务类型|
函数功能：为相应安评师添加业务信息

#### setCreditContractAddr
```sol
function setCreditContractAddr(address addr)
```
函数功能：设置信用合约地址

#### getCreditContractAddr
```sol
function getCreditContractAddr()  returns(address)
```
函数功能：返回信用合约地址

#### updateCredit
```sol
function updateCredit(string id)
```
函数功能：调用信用合约，为特定安评师更新信用分

#### getCredit
```sol
function getCredit(string id) returns(int)
```
函数功能：返回特定安评师信用分


## Management.sol
管理合约，存储区块链实体的相关信息
### Variables
```sol
struct entityInfo{  //实体信息
    address accountAddr;    //实体账户地址
    address contractAddr;   //实体合约地址
    string pubKey;  //实体公钥
    string field;   //实体所属领域
}

mapping (string => entityInfo) auditMap; //监管部门存储Map，以监管部门名为索引

mapping (string => entityInfo) agencyMap; //安评机构存储Map，以安评机构名为索引

mapping (string => entityInfo) enterpriseMap; //生产企业存储Map，以生产企业名为索引

string[] auditList; //监管部门名列表

string[] agencyList;    //安评机构名列表

string[] enterpriseList;    //生产企业名列表

address[] auditAddressList; //监管部门地址列表

address[] agencyAddressList;    //安评机构地址列表
```
### Functions

#### addAudit
```sol
function addAudit(string name, address accountAddr, string pubKey,string field) 
```
#### addAgency
```sol    
function addAgency(string name, address accountAddr, address contractAddr ,string pubKey, string field )
```
#### addEnterprise
```sol
function addEnterprise(string name, address accountAddr,address contractAddr, string pubKey, string field) 
```
#### getAuditAccountAddr
```sol
function getAuditAccnountAddr(string name) public view returns(address)
``` 
#### getAuditPubkey
```sol
function getAuditPubKey(string name) public view returns(string) 
```
#### getAuditField
```sol
function getAuditField(string name) public view returns(string)
```
#### getAgencyAccountAddr
```sol
function getAgencyAccountAddr(string name) public view returns(address) 
``` 
#### getAgencyContractAddr
```sol
function getAgencyContractAddr(string name) public view returns(address)
```
#### getAgencyPubKey
```sol
function getAgencyPubKey(string name) public view returns(string) 
```
#### getAgencyField
```sol
function getAgencyField(string name) public view returns(string)
```
#### getEnterpriseAccountAddr
```sol
function getEnterpriseAccountAddr(string name) public view returns(address) 
```
#### getEnterpriseContractAddr
```sol
function getEnterpriseContractAddr(string name) public view returns(address)
```
#### getEnterprisePubKey
```sol
function getEnterprisePubKey(string name) public view returns(string)
```
#### getEnterpriseFidld
```sol
function getEnterpriseField(string name) public view returns(string)
```
#### getAuditList
```sol
function getAuditList() public view returns(string[],address[])
```
#### getAgencyList
```sol
function getAgencyList() public view returns(string[],address[])
```
#### getEnterpriseList
```sol
function getEnterpriseList() public view returns(string[])
```

## ReportEvaluation.sol
报告审查合约,随机选取安评价机构进行安评报告审查
### Variables
```sol
address licenseAddress; // 记录许可证合约地址

mapping (uint256 => uint) public resultMap; // 用于生成随机数的map

Management systemManagement; // 系统管理合约实例

uint256[] randomAgencyIndex; // 随机选取的安评机构索引

address[] randomAgencyAddress; // 随机选取的安评机构地址

string[] randomAgencyName; // 随机选取的安评机构的名称

string[] pubKey; //安评机构公钥列表

address[] allAgency; // 安评机构合约地址列表

string[] allAgencyName; // 安评机构名称列表

address enterprise; // 被评定的企业的合约地址

address relatedAgency; // 生成报告的安评机构的合约地址

string[] relatedEngineer; // 与生成报告相关的安评师

string business; // 业务名称

uint start; // 记录是否开始审查

uint confirmTimes; // 记录审查机构通过的数量
    
address randomNumberAddress; // 随机数生成合约地址

randomNumber randGen; // 随机数生成合约实例
```
### Functions

#### addAgency
```sol
function addAgency(address agency) returns(address[])
```
输入安评机构合约地址，添加到安评机构合约地址列表

#### addAgencyList
```sol
function addAgencyList(address[] agencyList) returns(address[]) 
```
输入安评机构列表,添加安评机构到合约地址列表

#### getAgencyList
```sol
function getAgencyList(address managementAddress)
```
从management合约获取安评机构合约地址列表并记录


#### showAgencyList
```sol
function showAgencyList() constant returns (address[], string[])
```
查看所有安评机构合约地址

#### getLicenseAddr
```sol
function getLicenseAddr() constant returns (address) 
```
获取待审查实体的许可证合约地址

#### randFromOracle
```sol
function randFromOracle(uint256 seed,uint256 total) return(uint256) 
```
seed:随机数种子 total:随机数范围

从TruOra获取随机数

#### genNextRand
```sol
function genNextRand(uint256 seed,uint256 total) view returns(uint256) 
```
seed:随机数种子 total:随机数范围
生产随机序列所用随机数生成函数

#### randomAgency
```sol
function randomAgency(uint256 length,uint256 total) return(uint256[])
```
输入选取数量和安评机构总数 返回随机选取的安评机构索引

#### startEvaluation
```sol
function startEvaluation() returns(address[])
```
开始审查函数,其中调用了randomAgency随机选取安评机构 返回随机选取的安评机构合约地址列表


#### confirm
```sol
function confirm()
```
由安评机构调用,通过审查


#### deny
```sol
function deny() public returns(address)
```
安评机构不通过审查,并生成仲裁合约,并返回仲裁合约的地址
    
#### businessUpdate
```sol
function businessUpdate() returns(address)
```
为相关安评机构与安评师更新业务列表


## License.sol
许可证合约,记录企业的相关信息以及许可证的信息
### Variables
```sol
string name; // 公司名称

string representative; // 法人

string addr; // 公司地址

string enterpriseType; // 公司类型

string enterpriseLimit;  // 生产范围

string license; // 证书内容

string dataIpfs; // 加密数据ipfs地址

string reportIpfs; //安评报告ipfs地址

address agency; // 生成安评报告的安评机构合约地址

string[] engineer; //相关安评师id

uint expiration; // 许可证过期时间

string licenseStatus = "待审查"; // 许可证状态
```
### Functions

#### showInfo
```sol
function showInfo() constant returns(string,string,string,address,string[])
```
获取安全评估过程相关信息

#### getInfo
```sol
function getInfo() constant returns(string, string, string, string, string, uint,string)
```
获取企业相关信息


#### addLicense
```sol
function addLicense(address enterprise,address _agency,string[] _engineer)
```
|参数|含义|
|----|----|
|enterprise|企业合约地址|
|_agency|安评机构地址|
|_engineer|相关安评师地址|
添加许可证相关信息,并填写对应企业的license合约地址


#### revokeLicense
```sol
function revokeLicense()
```
撤销许可证,修改许可证状态为撤销

#### updateStatus
```sol
function updateStatus(string status)
```
修改证书状态

## Accusation.sol
举报合约，接收公众举报，查询举报信息
### Variables
```sol
 struct accusationInfo{ //举报信息
        string exposer; //举报人
        string accusationAbstract; //举报概要
        string attachment;  //附件
    }
    mapping (string => accusationInfo[]) accusationMap;// 举报信息存储Map，以监管机构名为索引
```

### Functions
#### addAccusation
```sol
function addAccusation(string expo, string abs, string attachFile,string auth)
```
| 参数 | 含义 |
| ---- | ---- |
| expo | 举报人信息|
| abs | 举报概要|
| attachFile | 举报附件信息|
| auth | 要提交的监管部门 |
函数功能： 添加举报信息

#### getAccusation
```sol
function getAccusation(string auth) returns(accusationInfo[])
```
函数功能：以监管部门为索引，查询向该监管部门提交的举报信息




## Credit.sol
信用合约，根据输入为安评机构和安评师计算信用分
### Variables
empty
### Functions
#### computeEngineerCredit
```sol
function computeEngineerCredit(EngineerList.businessInfo[] businessList) payable public returns (int) 
```
函数功能：计算安评师信用分
#### computeAgencyCredit
```sol
function computeAgencyCredit(Agency.businessInfo[] businessList) payable public  returns (int ) 
```
函数功能：计算安评机构信用分

## PublicQuery.sol
公众查询合约，为公众查询提供接口
### Variablse
```sol
address accusationAddr; //举报合约地址

address managementAddr; //管理合约地址

address creditAddr; //信用合约地址
```

### Functions
#### getLicense
```sol
function getLicense(string _name) returns(address) 
```
_name为企业名称
函数功能：调用管理合约，获取指定企业证书地址


#### getPubkey
```sol
function getPubkey(string _type , string _name) returns (string) 
```
_type为实体类型，_name为实体名
函数功能：调用管理合约，获取特定实体公钥

#### getEngCredit
```sol
function getEngCredit(string _name) returns (int)
```
_name为安评师姓名
函数功能：调用管理合约，根据姓名获取安评师列表合约地址后调用安评师列表合约，获取特定安评师信用分


#### getAgencyCredit
```sol
function getAgencyCredit(string _name) returns (int)
```
_name为安评机构名
函数功能：调用管理合约，获取安评机构地址后调用安评机构合约，获取信用分

## Arbitrate
仲裁合约,当企业安评报告未通过或者收到举报时,对企业进行仲裁
### Variables
```sol
mapping (uint256 => uint) public resultMap; //用于生成随机数的map

address licenseAddress; // 许可证合约地址

address enterpriseAddress; // 相关生产企业合约地址

address[] allAudit; // 所有监管部门的账户地址

uint256[] randomAuditIndex; // 随机选择的监管部门索引

address[] randomAuditAddress;  // 随机选择的监管部门的账户地址列表

string[] allAuditName; //所有监管部门名称

string[] randomAuditName; // 随机选择的监管部门名称列表

Enterprise newEnterprise; // 首仲裁的企业实例

Management systemManagement; // 系统管理合约实例

uint confirmTimes; // 监管机构通过数

uint start; // 记录是否开始仲裁

address randomNumberAddress; // 随机数生成合约地址

randomNumber randGen; // 随机数生成合约实例
```
### Functions

#### addAudit
```sol
function addAudit(address audit) public returns(address[])
```
添加监管部门账户地址


#### addAuditList
```sol
function addAuditList(address[] auditList) returns(address[])
```
获取监管部门账户地址列表

#### getAuditList
```sol
function getAuditList(address managementAddress)
```
从系统的管理合约获取监管部门账户地址列表

#### randFromOracle
```sol
function randFromOracle(uint256 seed,uint256 total) return(uint256) 
```
seed:随机数种子 total:随机数范围

从TruOra获取随机数

#### genNextRand
```sol
function genNextRand(uint256 seed,uint256 total) view returns(uint256) 
```
seed:随机数种子 total:随机数范围
生产随机序列所用随机数生成函数


#### randomAudit
```sol
function randomAudit(uint256 length,uint256 total) returns(uint256[])
```
输入选取数量和监管部门总数 返回随机选取的监管部门索引

#### startAudition
```sol
function startAudition() returns(string[],string[], address[])
```
开始仲裁函数,调用`randomAudit()`随机选取监管部门，并返回所选监管部门的名称,公钥,账户地址

#### confirm
```sol
function confirm() returns (bool)
```
监管部门调用`confirm()`表示仲裁通过

#### deny
```sol
function deny() returns (bool) 
```
监管部门调用`deny()`表示仲裁不通过,修改相应许可证状态为"仲裁不通过"

