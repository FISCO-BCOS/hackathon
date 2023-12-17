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

