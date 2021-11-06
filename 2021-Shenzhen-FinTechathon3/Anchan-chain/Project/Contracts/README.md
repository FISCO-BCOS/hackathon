## 合约概述

**Enterprise.sol**

```solidity
    string encrypted_data_ipfs; //加密材料ipfs地址
    string report_ipfs; //报告ipfs地址
    address license_add; //许可证合约地址
    // 用于发证合约发证
    function set_license() public 
    // 企业上传相关信息(加密材料ipfs，报告ipfs，安评机构地址，相关安评师地址)
    function update(string data,string report,address agency,address [] engineer) public returns(address)
```

**ReportEvaluation.sol**

```solidity
    address[] All_agency; //所有安评机构列表
    uint256[] random_Agency; //随机选择安评机构序号
    address[] random_Agency_address; //随机选择的安评机构合约地址
    address enterprise;//被评定的企业
    address related_agency; // 发证机构
    address[] related_engineer; //发证相关安评师
    string bussiness; //被评定企业的报告ipfs地址
    uint start; //标记是否开始评定
    uint flag; //判断是否通过评定
    //添加机构(用于测试)
    function add_Agency(address agency) public returns(address[]) 
    //用于随机选择评定机构
    function rand(uint256 total) public view returns(uint256) 
    function rand2(uint256 seed,uint256 total) public view returns (uint256) 
    function randomAgency(uint256 length,uint256 total) public view returns(uint256[])
    //开始评定，并随机选择安评机构
    function start_evaluation() public returns(address[])
    //用于安评机构合约调用，通过评定
    function confirm()
    //通过评定后发证并更新业务
    function bussiness_update() public returns(bool)
```

**License.sol**

```solidity
    string data_ipfs;   //材料ipfs
    string report_ipfs; //报告ipfs
    address agency;     //安评机构合约地址
    address[] engineer; //相关安评师合约地址
    //发证，并填写相关信息
    function add(address enterprise,address _agency,address[] _engineer)
    //查看许可证合约信息
    function show() view returns(string,string,string,address,address[])
```

**Agency.sol**

```solidity
    //安评师列表合约地址
    address engList;
    //机构公钥证书
    string agencyCert;
    //业务内容 包括时间、证书地址、业务类型
    struct businessInfo{ 
        string time;
        string licenseAddr;
        string bussinessType;
    }
    //业务列表  
    businessInfo[] BussinessList;
    //资质分
    int credit;
    //设置证书
    function setAgencyCert(string cert)
    //获取证书
    function getAgencyCert()
    //设置安评师列表合约的合约地址 ，以便addbusiness 
    function setEngListAddr(address addr)
    //获取安评师列表合约地址
    function getEngListAddr() public view returns (address)
    //确认业务
    function confirm(address evaluation_address)
    // 为机构添加业务，同时为多个安评师添加业务，indexes记录安评师索引。
    function addBusiness(uint[] indexes,string business) 
    //展示所有业务
    function showBusiness() returns(string[])
```

**Engineer.sol**

```solidity
    //业务内容，包括时间、证书地址、业务类型
    struct bussinessInfo{ 
        string time;
        string licenseAddr;
        string bussinessType;
    }
    //安评师信息，包括安评师姓名、安评师所属领域、安评师证书、安评师所属安评机构、安评师业务列表
    struct engineerInfo{
        string name;
        string field;
        string safetyEvaluationCertificate;
        string agency; 
        BussinessInfo[] businessList;
    }
    //以安评师证书id为索引创建Map
    mapping (string => EngineerInfo) engineerMap;
    //添加安评师，
    function addEngineer(string n, string id, string f,string cert,string agen)
    //获取安评师信息，输入安评师证书id
    function getEngineer(string id) public returns(EngineerInfo)
    //删除安评师所属安评机构
    function deleteAgency(string id ) public
    //安评师设置新机构
    function setAgency(string id,string agen) public
    //为安评师添加新业务信息
    function addBusiness(string id,string time,string addr,string bType) public
```
