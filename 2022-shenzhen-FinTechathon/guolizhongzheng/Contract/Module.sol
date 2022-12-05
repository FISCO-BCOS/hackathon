pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
//所有的参数变量均放在这里;
contract Module{
    
    string[] A;
    address x;
    address[] y;
    uint[] z;
    string a="********";
    //一个医院名称对应了多个地址;
    string[] HospitalList;
    mapping(string=>address[])Hospital;
    address[] PatList;//所有的患者;
    mapping(address=>Pat)PatientMap;//患者对应的PatientMap类型;

    //患者结构体;
    struct Pat{ 
        address Pataddress;//学生的账户地址:
        string PatRecord;//患者基本信息:(姓名，性别，身份，年龄，身份证，出生日期)
        string cipher;//密码;
        address EvidenceBenAdd;//电子病历本地址;
        address ReportBenAdd;//体检报告本地址:
        uint Value;//共享因子;(共享值根据共享医疗数据的等级与人数以及稀缺程度和人数价值程度综合评判!!!)
        mapping(address=>address[])EvidenceBen;//电子病历本;
        mapping(address=>address[])ReportBen;//体检报告本;
    }
    
    //单个电子病历;
    mapping(address=>SignalEvidence)EvidenceMap;
    struct SignalEvidence{
        address[] Gather;//该电子病历访问集合:
        string RecordMessage;//用户身份信息:
        mapping(address=>uint)Level;//对应的层次等级:
        address[] Signers;//电子病历签名:
        address EviAdd;//电子病历地址:
        string Record;//电子病历数据:
        string TestRecord;//检测报告数据:
        uint[] Value;//医疗数据价值与稀缺度评判:
    }

    //单个体检报告;
    mapping(address=>SignalReport)ReportMap;
    struct SignalReport{
        //进行map映射判断:
        mapping(address=>uint)Level;
        address ReportAdd;//体检报告地址:
        string Record;//据:
        string TetsRecord;//检测数据:
        address[] Gather;//体检报告访问集合:
        address[] Signers;//体检报告签名:
        uint[] Value;//医疗数据价值与稀缺度评判:
    }

    address[] DocList;//主治医生;
    mapping(address=>Doc)DocMap;
    //主治医生结构体;
    struct Doc{
    address Docaddress;//医生的账户地址;
    string Cipher;//密码;
    string DocRecord;//医生的基本信息;
    string HospitalName;//医生所处医院的地址:
    uint Number;//医生的就诊人数;
    address[] EviAddressList;//待就诊的病历;
    address[] PatientAddress;//待就诊的患者;
    address []EvidenceBen;//患者给予医生可查看的电子病历本;
    address []ReportBen;//可查看的体检本地址访问集合;
    address [] Evicollection;//电子病历权限集合:
    address [] Reportcollection;//体检报告权限集合:
    }
    
    //检测医生结构体:
    address[] TestList;
    mapping(address=>TestDoc)TestMap;
    struct TestDoc{
    address Docaddress;//医生的账户地址:
    string Cipher;//密码:
    string DocRecord;//检测医生的基本信息:
    uint Number;//医生的检测的人数:
    address [] ReportList;//待检测的体检报告:
    address[] Phyexam;//待检测的检测报告:
    }

}
