# 合约接口说明文档
主要合约包括：

1.  FundsApplicationCipher合约：存储用户的求助信息，用户筹款信息以及多种方法。
2.  FundingUser合约：存储用户资助信息。
3.  Blacklist合约：存储黑名单用户。
4.  Medical合约：存储用户病历信息。
5.  UserAssetInfo合约：存储用户房车产信息。
6.  DataFormat合约：格式化合约。

## FundsApplicationCipher合约
### insertFundsInfo
插入用户求助信息  
参数：  
userId: String 用户id  
certificateData： String 密文  
certificateTemplate: String 密文模板  
返回值:   
 无
### getUserApplicationInfo
查询用户求助信息  
参数：  
userId: String 用户id  
返回值:  
userId: String 用户id  
certificateData： String 密文  
certificateTemplate: String 密文模板   
fundState: String 筹款状态 
### updateApplication
更新用户个人信息  
参数：  
userId: String 用户id  
certificateData： String 密文  
certificateTemplate: String 密文模板  
返回值:   
 无  
### changeFundsState
更改用户筹款状态  
参数：  
userId: String 用户id  
返回值:   
 无  
### initFundsCompute
初始化用户筹款统计表  
参数：  
userId: String 用户id  
返回值:   
 无  
### updateFundsCompute
更新用户筹款信息  
参数：  
userId: String 用户id  
fundSum: int 资助金额  
fundingId: String 资助人Id  
fundsNum: String 资助金额  
fundingTime: String 资助时间  
返回值:   
 无  
### getUserFundsInfo
获取求助者当前筹款金额  
参数：  
userId: String 用户id  
返回值:   
fundSum: int 资助金额  
fundingId: String 资助人Id  
fundsNum: String 资助金额  
fundingTime: String 资助时间  
### initFundsWithdrawl
初始化申请者提现表  
参数：  
userId: String 用户id  
返回值:   
 无  
### updateFundsWithdrawal
更新用户提现信息  
参数：  
userId: String 用户id  
withdrawalAmount: int 提款金额
返回值:   
 无  
### getUserFundsWithdrawal  
获取用户提现数据  
参数：  
userId: String 用户id  
返回值：  
withdrawalAmount: int 上次取款金额  
balance: int 余额  
withdrawalSum: int 取款总额   
### initCostDetail  
初始化用户花费明细表  
参数：  
userId: String 用户id  
withdrawalAmount: int 提款金额  
返回值:   
 无  
### updateCostDetail
更新用户花费明细  
参数：  
userId: String 用户id  
cost: int 花费金额  
detail: String 花费明细  
costStr: String 花费金额  
返回值:   
 无  
### getUserCostDetail
获取用户当前花费明细  
参数：  
userId: String 用户id  
返回值：  
costSum: int 花费总金额  
detai: String 花费明细  
costStr: String 花费金额  
### removeAll 
移除用户求助信息  
参数：  
无  
返回值：  
无  
### getAllUserApplication
获取全部用户求助信息  
参数：  
无  
返回值：  
无  

## FundingUser合约

## initFundingInfo
初始化用户资助表  
参数：  
userId: String 用户id  
返回值:   
 无  
## updateFundingInfo
更新用户资助信息  
参数：  
userId: String 用户id  
fundsNumInt： int 资助金额  
fundingId: String 捐款id  
fundsNumStr: String  资助金额  
fundingTime: String 捐款时间  
返回值:   
 无  
## getUserFundingInfo
获取资助者当前资助数据  
参数：  
userId: String 用户id  
返回值:   
allFundingUser：String 资助id  
allFundingNumStr：String 资助金额  
fundingTime：String 资助时间  
score：爱心值    
## Blacklist合约

### initUserState
初始化用户状态表  
参数：  
userId: String 用户id  
返回值:   
 无  
### changeUserState
更改用户账户状态  
参数：  
userId: String 用户id  
返回值:   
 无  
### getUserState
获取用户账户状态  
参数：  
userId: String 用户id  
返回值:   
userState: String 用户状态  
## Medical合约
### insertMedicalRecord
插入病历信息  
参数：  
userId: String 用户id  
userName:String 用户名字   
userMedicalRecord:String 用户病例  
返回值:   
 无  
### getUserMedicalRecord
验证用户病历信息  
参数：  
userId: String 用户id  
返回值:   
result: boolean 验证结果  
## UserAssetInfo合约

### insertUserAssetInfo
插入用户资产信息  
参数：  
userId: String 用户id  
userName:String 用户名字   
propertyValue:String 房产信息    
carValue：String 车产信息  
返回值:   
 无  
### getUserAssetInfo
验证用户资产信息  
参数：  
userId: String 用户id  
userName:String 用户名字   
propertyValue:String 房产信息    
carValue：String 车产信息  
返回值:   
result: boolean 验证结果 


