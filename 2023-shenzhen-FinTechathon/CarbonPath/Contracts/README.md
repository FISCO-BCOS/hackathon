# Carbon Path合约接口说明


## 交易信息上链存证 
### 1. CEA_Transaction.sol
This contract allows users to record transcation information of CEA on the blockchain and then read from the blockchain.
#### Variables
```
    txa_limit: maximum number of transactions that can be recorded
    txa_id: transaction IDs
    txa_companyName: names of companies involved in each transaction
    txa_transAmount: records the amount of each transaction
    txa_time: storing the time when each transaction was offered
    txa_price: transaction prices
    txa_contactPerson: the names of contact person
    txa_contactWay: the contact details (such as email addresses or phone numbers) 

```

#### Functions
#### 1. _txaRecord
```
function _txaRecord(
        string memory companyName, 
        uint transAmount, 
        string memory time, 
        uint price,  
        string memory contactPerson, 
        string memory contactWay
    ) public eturns (string memory log)
```
```
Note: record transaction information to the blockchain
```
#### 2. getTransaction1 & getTransaction2
```
function getTransaction1(uint id) public view returns ( uint, string memory, uint, string memory, uint) 
function getTransaction2(uint id) public view returns ( string memory, string memory) 

```
Note: retrieve CEA transaction information from the blockchain



### 2. CCER_Transaction.sol
This contract allows users to record transcation information of CCER on the blockchain and then read from the blockchain.
#### Variables
```
txr_limit: Maximum number of transactions that can be recorded. 
txr_id: Transaction IDs. 
txr_displayTime: Display time for transactions. 
txr_price: Transaction prices, stored as integers. 
txr_contactPerson: Contact persons' names for each transaction.
txr_contactWay: Contact details for transactions, such as email addresses or phone numbers, enabling communication regarding the transactions.
txr_name: Names associated with each transaction.
txr_status: Status of each transaction. 
txr_companyName: Company names involved in each transaction. 
txr_date1: Specific dates related to each transaction. 
txr_reductionExpected: Expected reduction or change figures for each transaction.
txr_description: Descriptions of each transaction. 
txr_file1: References or links to a primary document or file associated with each transaction.
txr_file2: Similar to txr_file1, this array holds references to a secondary relevant document or file for each transaction.
txr_size: Total number of transaction information entries uploaded. 
```

#### Functions
#### 1. _txrRecord1 
```
function _txrRecord1(
        string memory displayTime,
        uint price,
        string memory contactPerson,
        string memory contactWay,
        string memory name,
        string memory status
    ) public returns (uint)
function _txrRecord2(

        string memory companyName,
        string memory date1,
        uint reductionExpected,
        string memory description,
        string memory file1,
        string memory file2
    ) public

```
Note: record CCER transactions information to the blockchain. It's divided into part 1 and 2.

#### 2. getTransaction1 & getTransaction2 & getTransaction3 
```
function getTransaction1(uint id) public view returns (
        uint, string memory, uint, string memory, string memory
    ) 
function getTransaction2(uint id) public view returns (
        string memory, string memory, string memory, string memory, uint, string memory
    )
function getTransaction3(uint id) public view returns (
        string memory, string memory
    )
```
```
Note: retrieve CCER transaction information from the blockchain
```







## 碳排放核算、清缴

### 3. EmissionAccounting.sol
This contract allows users carbon emitting enterprises to account for their own carbon emissions
#### Variables
```
    uint constant et1 = 1981; // 原煤碳排放因子
    uint constant et2 = 2405; // 洗精煤碳排放因子
    uint constant et3 = 2860; // 焦炭排放因子
    uint constant et4 = 21622; // 天然气排放因子
    uint constant et5 = 2925; // 汽油排放因子
    uint constant et6 = 3096; // 柴油碳排放因子
    uint constant et7 = 318; // K2CO3排放因子
    uint constant et8 = 223; // BaCO3排放因子
    uint constant et9 = 522; // MgCO3排放因子
    uint constant et10 = 440; // CaCO3碳排放因子
```

#### Functions
#### 1. _AccountEmission1 
```
function _AccountEmission1(
        uint amount1,
        uint amount2,
        uint amount3,
        uint amount4,
        uint amount5
    ) public returns (string memory log)

function _AccountEmission2(
        uint amount6,
        uint amount7,
        uint amount8,
        uint amount9,
        uint amount10
    ) public returns (string memory log)

```
```
Note: Calculate emissions based on emission factors and amounts
```

#### 2. _sumEmission
```
function _sumEmission() public view returns (uint totalEmissions) 
```
```
Note: Returns the sum of total emissions from both _AccountEmission1 and _AccountEmission2. 
```


### 4. Clearance.sol
This contract allows users carbon emitting enterprises to account for their own carbon emissions
#### Variables
```
    // Historical Transaction Information arrays
    htxa_id: Array of transaction IDs
    htxa_seller: Array of seller names
    htxa_buyer: Array of buyer names
    htxa_amount: Array of transaction amounts (should be float; multiply 100 times)
    htxa_price: Array of transaction prices (should be float; multiply 100 times)
    htxa_time: Array of transaction times in format: "YYYY-MM-DD"
    htxa_size: Total number of Historical Transaction Information entries uploaded

    // Clearance Information
    cl_Num = 13: 12 months in total, extra space as buffer
    cl_companyName: Array of company names for clearance
    cl_month: Array of months for clearance
    cl_buyAmount: Array of buy amounts for each month (should be float; multiply 100 times)
    cl_sellAmount: Array of sell amounts for each month (should be float; multiply 100 times)
```

#### Functions
#### 1. htxaRecord
```
ffunction htxaRecord(
        string memory _seller,
        string memory _buyer,
        uint _amount,
        uint _price,
        string memory _time
    ) public returns (string memory log)

```
```
Note: Record Historical Transaction Information through variables
```
#### 2. getHtxa 
```
function getHtxa(uint _id) public view returns (string memory seller, string memory buyer, uint amount, uint price, string memory time)
```
```
Note: Get Historical Transaction Information by htxa_id
```

#### 3. _getclSold 
```
function _getclSold(string memory companyName) public view returns (uint[cl_Num] memory) 
```
```
Note: Get the total sell amount for a specific company in each month
```

#### 4. _getclBought 
```
function _getclBought(string memory companyName) public view returns (uint[cl_Num] memory)
```
```
Note: Calculate and retrieve the total buy amount for a specific company in each month
```


