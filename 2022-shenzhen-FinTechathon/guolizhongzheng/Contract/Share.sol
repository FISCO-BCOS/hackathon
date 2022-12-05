pragma solidity ^0.4.25;
import "./Roles.sol";
import "./Module.sol";
import "./dekit.sol";
pragma experimental ABIEncoderV2;
//对应的合约变量;
contract Share is Module,dekit,Roles{

    //一级共享:对医疗数据进行同态加密再共享:
    function ShareOne(address PatAdd,address EviAdd,address Doc)internal{
        require(JudgeEviBen(PatAdd,EviAdd),"You are not the owner");
        EvidenceMap[EviAdd].Gather.push(Doc);
        EvidenceMap[EviAdd].Level[Doc]=1;
        DocMap[Doc].Evicollection.push(EviAdd);
    }
    
    //二级共享:对医疗数据原文进行共享，选择对隐私身份信息进行脱敏处理:
    function ShareTwo(address PatAdd,address EviAdd,address Doc)internal{
     require(JudgeEviBen(PatAdd,EviAdd),"You are not the owner");
     EvidenceMap[EviAdd].Gather.push(Doc);
     EvidenceMap[EviAdd].Level[Doc]=2;
     DocMap[Doc].Evicollection.push(EviAdd);
    }

    //三级共享:不仅能对医疗数据进行共享，而且能让医院或者医生对该医疗数据进行共享并做出医学研究:
    function ShareThree(address PatAdd,address EviAdd,address Doc)internal{
       require(JudgeEviBen(PatAdd,EviAdd),"You are not the owner");
       EvidenceMap[EviAdd].Gather.push(Doc);
       EvidenceMap[EviAdd].Level[Doc]=3;
       DocMap[Doc].Evicollection.push(EviAdd);
    }

    //体检报告共享操作:
    //共享给医生:
    //共享给医院:
    //共享给患者;
    //一级共享:对体检医疗数据进行同态加密再共享:
    function ShareReportOne(address PatAdd,address ReportAdd,address Doc)internal{
        require(JudgeReportBen(PatAdd,ReportAdd),"You are not the owner");
        ReportMap[ReportAdd].Gather.push(Doc);
        ReportMap[ReportAdd].Level[Doc]=1;
        DocMap[Doc].Reportcollection.push(ReportAdd);
    }

    //二级共享:对体检数据原文进行共享，选择对隐私身份信息进行脱敏处理:
    function ShareReportTwo(address PatAdd,address ReportAdd,address Doc)internal{
     require(JudgeReportBen(PatAdd,ReportAdd),"You are not the owner");
     ReportMap[ReportAdd].Gather.push(Doc);
     ReportMap[ReportAdd].Level[Doc]=2;
      DocMap[Doc].Reportcollection.push(ReportAdd);
    }

    //三级共享:不仅能对体检数据进行共享，而且能让医院或者医生对该医疗数据进行共享并做出医学研究:
    function ShareReportThree(address PatAdd,address ReportAdd,address Doc)internal{
       require(JudgeReportBen(PatAdd,ReportAdd),"You are not the owner");
       ReportMap[ReportAdd].Gather.push(Doc);
       ReportMap[ReportAdd].Level[Doc]=3;
       DocMap[Doc].Reportcollection.push(ReportAdd);
    }

    //将医生拉入电子病历全局的访问集合:
    function ShareEvidenceBen(address PatAdd,address Doc)public{
        for(uint index=0;index<PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd].length;index++){
          EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Gather.push(Doc);
        }
        DocMap[Doc].EvidenceBen.push(PatientMap[PatAdd].EvidenceBenAdd);
    }

    //将医生拉入体检报告的全局访问集合:
    function ShareReportBen(address PatAdd,address Doc)public{
        for(uint index=0;index<PatientMap[PatAdd].ReportBen[PatientMap[PatAdd].ReportBenAdd].length;index++){
            ReportMap[PatientMap[PatAdd].ReportBen[PatientMap[PatAdd].ReportBenAdd][index]].Gather.push(Doc);
        }
        DocMap[Doc].ReportBen.push(PatientMap[PatAdd].ReportBenAdd);
    }

    //出院之后可以选择删除医生所有的访问权限:
    function DeleteAllAuthority(address PatAdd,address Doc)public {        
          address[] x1;
          if(DocMap[Doc].EvidenceBen.length>0){
          //1.删除用户给医生提供的病历本ID和体检报告ID:
          deleteAdd(DocMap[Doc].EvidenceBen,PatientMap[PatAdd].EvidenceBenAdd);
          //2.删除医生访问电子病历的权限:
          for(uint index=0;index<PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd].length;index++){
              deleteAdd(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Gather,Doc);
              
          }
              
          }
          
          if(DocMap[Doc].ReportBen.length>0){
          
          deleteAdd(DocMap[Doc].ReportBen,PatientMap[PatAdd].ReportBenAdd);
          //3.删除医生访问体检本的权限:
            for(uint i=0;i<PatientMap[PatAdd].ReportBen[PatientMap[PatAdd].ReportBenAdd].length;i++){
              deleteAdd(ReportMap[PatientMap[PatAdd].ReportBen[PatientMap[PatAdd].ReportBenAdd][i]].Gather,Doc);
          }
          
         }
    }
    
    //修改病历中某个集合元素的权限:
    function ModifyEviAuthority(address EviAdd,address Add,uint level)public{
        EvidenceMap[EviAdd].Level[Add]=level;
    }
    
    //将病历中某个元素踢出访问集合:
    function DeleteDocdd(address Doc, address EviAdd)public{
        deleteAdd(EvidenceMap[EviAdd].Gather,Doc);
        deleteAdd(DocMap[Doc].Evicollection,EviAdd);
    }
    
    //修改体检报告中访问集合的权限:
    function ModifyRepAuthority(address EviAdd,address Add,uint level)public{
        ReportMap[EviAdd].Level[Add]=level;
    }

    //将体检报告中某个元素提出访问集合:
    function DeleteRepdd(address Doc,address EviAdd)public{
        deleteAdd(ReportMap[EviAdd].Gather,Doc);
        deleteAdd(DocMap[Doc].Reportcollection,EviAdd);
    }

}
