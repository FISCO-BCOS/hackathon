pragma solidity ^0.4.25;
import "./Module.sol";
import "./Roles.sol";
import "./dekit.sol";
import "./Share.sol";
import "./Stimulate.sol";
//患者合约;
pragma experimental ABIEncoderV2;
contract Patient is Module,dekit,Roles,Share,Stimulate{
    
    //患者注册;
    event PatientRegisterEvent(
        string Record
    );
    
    //患者注册返回账户地址:
    function PatientRegister(string  Cipher,string Record)public returns(address){
        // require(sha256(Record)!=sha256(""),"record can not be null!!!"); //患者信息不能为空;
        address PatientAddress=address(new Module());
        PatientMap[PatientAddress].cipher=Cipher;//密码进行赋值操作:
        PatientMap[PatientAddress].Pataddress=PatientAddress;//病人地址:
        PatientMap[PatientAddress].EvidenceBenAdd=address(new Module());//
        PatientMap[PatientAddress].ReportBenAdd=address(new Module());
        PatientMap[PatientAddress].PatRecord=Record;//(姓名，性别，身份，年龄，身份证，出生日期)
        PatList.push(PatientAddress);//推入;
        emit PatientRegisterEvent("注册成功!");//触发注册事件;
        return PatientAddress;
    }
    
    //患者登录;
    function PatientEnter(address PatAdd,string Cipher)public view returns (bool,address ){
        //是否有该患者用户:
        // require(GenerateHash(PatientMap[PatAdd].cipher)==GenerateHash(Cipher),"密码错误");
        return (
            true,
            PatAdd
            );
    }

    //查询患者的基本信息:
    function QueryPat(address PatAdd)public view returns(address ,uint ,address,address,string){
        PatientMap[PatAdd].Value=QueryAll(PatAdd);
        return (
            PatientMap[PatAdd].Pataddress,//患者账户地址;
            PatientMap[PatAdd].Value,//患者的贡献值:
            PatientMap[PatAdd].EvidenceBenAdd,//病历本的编号;
            PatientMap[PatAdd].ReportBenAdd,//体检报告本编号;
            PatientMap[PatAdd].PatRecord//患者基本信息;
        );
    }

    //查询患者的所有电子病历的地址:
    function QueryAllEvi(address PatAdd)public view returns(address[]){
        return  PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd];
    }

    //查询电子病历信息:
    function QuerEviMessage(address PatAdd,address EviAdd)public view returns(string,string,string,address,address[],address[],uint[]){    
        require(JudgeEviBen(PatAdd,EviAdd)||JudgeGather(PatAdd,EviAdd),"无访问权限");
       //如果用户有这个电子病历则直接返回该电子病历所有的数据:
        if(JudgeEviBen(PatAdd,EviAdd)){
            return (
            EvidenceMap[EviAdd].Record,//电子病历数据
            EvidenceMap[EviAdd].TestRecord,//测试数据
            EvidenceMap[EviAdd].RecordMessage,
            EvidenceMap[EviAdd].EviAdd,//电子病历本ID
            EvidenceMap[EviAdd].Signers,//签名
            EvidenceMap[EviAdd].Gather,//访问集合
            EvidenceMap[EviAdd].Value//价值与稀有度
            );
            //如果用户没有该电子病历就应该查看访问集合里面有没有该用户:
            //访问集合里面有没有该用户:
        }
        //是否脱敏处理脱敏处理:
        //脱敏就把隐私信息抹除，再进行传输:
            return(
            EvidenceMap[EviAdd].Record,
            EvidenceMap[EviAdd].TestRecord,
            EvidenceMap[EviAdd].RecordMessage,
            x,
            y,
            y,
            z
            );
    }

    //查询所有的体检报告的地址:
    function QueryAllReport(address PatAdd)public view returns(address[]){
        return  PatientMap[PatAdd].ReportBen[PatientMap[PatAdd].ReportBenAdd];
    }

    //查询体检信息:
    function QuerReportMessage(address PatAdd,address ReportAdd)public view returns(address,string,string,address[],address[],uint[]){
        require(JudgeReportBen(PatAdd,ReportAdd)||JudgeReport(PatAdd,ReportAdd),"无访问权限");
        //如果用户拥有该体检报告单则返回所有的信息:
        if(JudgeReportBen(PatAdd,ReportAdd)){
            return (
            ReportMap[ReportAdd].ReportAdd,//体检报告的地址:
            ReportMap[ReportAdd].Record,//基本信息:
            ReportMap[ReportAdd].TetsRecord,//测试的数据:
            ReportMap[ReportAdd].Gather,//体检报告访问集合:
            ReportMap[ReportAdd].Signers,//体检报告签名集合:
            ReportMap[ReportAdd].Value//价值Value:
            );
        }
        //是否进行脱敏处理:
        //如果进行脱敏处理该怎么样:
        return(
            x,
            a,
            ReportMap[ReportAdd].TetsRecord,
            y,
            y,
            z        
        );

    }

    //首先是查看所有的医院;
    function  QueryAllHospital()public view returns(string[]){
        return HospitalList;//返回所有的地址类型;
    }

    //再查询医院里面所有的医生:
    function QueryAllDoc(string HospitalName)public view returns(address[]){
        //返回该医院里面所有的医生地址;
        return Hospital[HospitalName];
    }

    //申请看病:
    //选择某个主治医生并且挂号:
    function Registration(address PatAdd,address DocterAddress)public{
        //新建一个电子病历地址;
        address EviAddress=address (new Module());
        //电子病历的基本信息持续不变:
        EvidenceMap[EviAddress].RecordMessage=PatientMap[PatAdd].PatRecord;
        //往用户的电子病历本中添加电子病历:
        PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd].push(EviAddress);
        //医生推入待就诊的病历:
        DocMap[DocterAddress].EviAddressList.push(EviAddress);
        //医生推入待就诊的患者:
        DocMap[DocterAddress].PatientAddress.push(PatAdd);
        EvidenceMap[EviAddress].EviAdd=EviAddress;
        //如果用户选择共享则前端再调用一个借口;
    }

    //患者进行二维码验证操作:
    function Verification(address PatAdd,address DocAdd ,address EviAddress)public view returns(string){
        require(JudgeRegistration(PatAdd,DocAdd)||JudgeTestEvi(DocAdd,EviAddress),"无访问权限");
        return PatientMap[PatAdd].PatRecord;
    }
    
    //患者去体检医生那里做医疗检测数据:
    function Phyexamation(address PatAdd,address DocAdd,address EviAdd)public {
        require(JudgeEviBen(PatAdd,EviAdd),"You have no qulatification");
        TestMap[DocAdd].Phyexam.push(EviAdd);
    }
    
    //申请体检:体检医生录入对应的体检报告本中:
    //在医生进行挂号:
    function Examination(address PatAdd,address DocAdd)public {
    //用户基本信息直接录入;
    address  ReportAddress=address (new Module()); 
    TestMap[DocAdd].ReportList.push(ReportAddress);
    ReportMap[PatAdd].ReportAdd=ReportAddress;
    ReportMap[PatAdd].Record=PatientMap[PatAdd].PatRecord;
    PatientMap[PatAdd].ReportBen[PatientMap[PatAdd].ReportBenAdd].push(ReportAddress);
    ReportMap[ReportAddress].Signers.push(PatAdd);

    }    
    
    //患者采用CP-ABE访问策略对自己的电子病历进行分权共享：
    //设置访问策略以及共享者的权限:
    function ShareData(address PatAdd,address EviAdd,address Doc,uint Level)public{
        //设置共享等级:
        if(Level==1){
        ShareOne(PatAdd,EviAdd,Doc);
        }else if(Level==2){
        ShareTwo(PatAdd,EviAdd,Doc);
        }else if(Level==3){
        ShareThree(PatAdd,EviAdd,Doc);
        }
    }

    //使用CP-ABE访问策略对自己的体检报告进行分权共享:
    //设置访问策略以及权限;
    function ShareReport(address PatAdd,address ReportAdd,address Doc,uint Level)public{
      //设置共享等级:
        if(Level==1){
        ShareReportOne(PatAdd,ReportAdd,Doc);
        }else if(Level==2){
        ShareReportTwo(PatAdd,ReportAdd,Doc);
        }else if(Level==3){
        ShareReportThree(PatAdd,ReportAdd,Doc);
        }
    }
    
}
