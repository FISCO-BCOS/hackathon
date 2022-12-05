#合约说明
###主要合约：
+ Permission：权限管理合约，给账户进行身份注册，并赋予相应身份对应的权限。
+ Enterprise：企业功能合约，实现企业账户对应的功能（上传数据，查询数据，形成碳账户，发起碳交易）
+ Goverment：政府功能合约，实现政府账户对应的功能（公布标准，分配初始碳排放权，查询数据，对企业行业认证）
+ Audit：机构功能合约，实现机构功能合约（查询数据，上传报告）

###部署流程说明：
先部署permission合约，部署者成为owner（权限分配者），能赋予其他账户三种不同的权限，分别是企业，政府，机构。每个账户仅能获取一份权限。然后部署Enterprise、Goverment、Audit的三个合约分别实现企业、政府、机构的功能。部署时传入permission合约地址。部署者成为developer（开发人员），有权把两个合约的地址传入另一个合约，实现合约更新和合约互通。另外只有developer才能执行企业之间碳余额的交易。

###Permission合约：
####变量说明：
>  owner:部署者此合约的公钥地址，该账户拥有最高权限 。
>Role：映射即企业名字 -> 身份数字 ，用于账户身份注册。（111代表企业，222代表政府，333代表机构）

####函数说明:
> + 构造函数：将初始化owner为部署者地址
> +  ×××Register函数（三个）：
> >  参数：address：待注册账户
> > 功能：实现待注册账户身份注册，只能owner调用
> + getPermission函数：
> >  参数：address：已注册账户
> > 功能：返回身份数字

###Enterprise合约：
####变量说明：
> P , G , A :另三个合约地址
>developer：部署该合约者地址
> serialNumber：企业每次上传数据的序号
>Caccount：映射即企业名字 - > 数据struct
>ECaccount：数据struct，包含总碳排放权total，每日排放dailyEmissions，映射数据名字 -> 数量
>dataname：双重映射，数据序号 -> 企业名字 ->数据名字数组

####函数说明:
> + 构造函数：
> >  参数：P_：权限合约地址
> > 功能：初始化develoer为部署者地址（msg.sender）,初始化P（P__）
> +  contractAddress函数：
> >  参数：G_,  A_ ：政府合约地址与机构合约地址
> > 功能：传入政府合约地址和机构合约地址，只能developer调用
> + GoverWrite函数：
> >  参数：
> > 功能：给政府合约写入初始碳排权留的接口，只能政府账户调用
> + enterpriseWrite函数：
> > > 参数：
> > > name:企业名字
> > > dataName_：传入数据的名字数组 
> > > dataQuantity：传入数据的数量数组
> > > uint sNumber：该份数据序号
> > 功能：上传数据，只能企业账户调用
> + remainingEmissions函数：
> > > 参数：
> > > serialNumbe_:数据序号
> > > name:企业名字
> > 功能：根据上传数据完成碳账户的形成，只能enterpriseWrite函数调用
> + enterpriseView函数：
> > > 参数：
> > > name: 企业名字
> > >gasname:要查询数据名字
> > >time:数据序号
> > 功能：查询企业上传数据每个dataName对应数量
> + dataNameView函数：
> > > 参数：
> > > name: 企业名字
> > > sNumber:数据序号
> > 功能：查询企业在某份上传数据中的dataName名单
> + ViewRight函数：
> > > 参数：
> > > name: 企业名字
> > 功能：查看企业碳排放权以及剩余碳排放量
> + cTransaction函数：
> > > 参数：
> > > sender: 碳排放权卖出方
> > > receiver:碳排放权买入方
> > > amount:交易数量
> > 功能：企业之间进行碳交易，只有developer能够调用

###Goverment合约：
####变量说明：
> P , E, A :另三个合约地址
>developer:部署此合约者地址
>standard：映射行业代号 -> 对应标准
>industry ：映射企业名字 -> 行业代号
>enterpriseName: 企业名单
>JudgEnter: bool变量，验证企业名字是否已进入数组

####函数说明:
> + 构造函数：
> >  参数：P_：权限合约地址
> > 功能：初始化develoer为部署者地址（msg.sender）,初始化P（P__）
> +  contractAddress函数：
> >  参数：E_,  A_ ：企业合约地址与机构合约地址
> > 功能：传入企业合约地址和机构合约地址，只能developer调用
> + govermentWrite函数：
> > > 参数：
> > > Ename: 企业名字数组
> > > EMright:碳排放权数组
> > 功能：给每个企业分配初始碳排放权，只能政府账户调用
> + enterpriseNameView函数：
> >  参数：
> > 功能：返回企业名单
> + enterpriseView函数：
> > > 参数：
> > > name: 企业名字
> > > gasname:数据名字
> > > time:数据序号
> > 功能：查询企业数据
> + ViewRight函数：
> > > 参数：
> > > name: 企业名字
> > 功能：查看企业碳排放权及剩余碳排放量
> + publishStandard函数：
> > > 参数：
> > > Industry: 行业代号数组
> > > Standard:标准数组
> > 功能：公布不同行业碳排放标准，只能政府账户调用
> + industryWrite函数：
> > > 参数：
> > > name: 企业名字
> > > attribute：行业代号
> > 功能：公布不同行业碳排放标准，只能政府账户调用

###Audit合约：
####变量说明：
> P , E, G :另三个合约地址
>developer:部署此合约者地址
>eReport:双重映射,报告序号-> 企业名字 -> 报告struct
>report:报告struct,包含industry(行业代码) , field1(核算依据，核算日期范围) , filed2(生产过程碳排放：初始报告，核查，数据异常情况) , filed3(外购电力,其他)

####函数说明:
> + 构造函数：
> >  参数：P_：权限合约地址
> > 功能：初始化develoer为部署者地址（msg.sender）,初始化P（P__）
> +  contractAddress函数：
> >  参数：E_,  A_ ：企业合约地址与机构合约地址
> > 功能：传入企业合约地址和机构合约地址，只能developer调用
> +  enterpriseView函数：
> > > 参数:
> > > name: 企业名字
> > >gasname:要查询数据名字
> > >time:数据序号
> > 功能：查询企业上传数据每个dataName对应数量
> +  uploadReport函数：
> > > 参数:
> > > name: 企业名字
> > >indus:行业代号
> > >sNumber:数据序号
> > >field1:报告的一部分
> > >field2:报告的一部分
> > >field3:报告的一部分
> > 功能：上传企业报告
> + ViewRight函数：
> > > 参数:
> > > name: 企业名字
> > 功能：查看企业碳排放权和剩余碳排放量











