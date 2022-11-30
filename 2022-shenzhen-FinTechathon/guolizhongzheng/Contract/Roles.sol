pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./Module.sol";
contract Roles is Module{

    //判断是否存在该患者;
    function JudgeHas(address PatAdd)public  view returns(bool){
    for(uint index=0;index<PatList.length;index++){
        if(PatAdd==PatList[index]){
            return true;
        }
    }    
    return false;
    }

    //判断是否有这个医生;
    function JudgeHasSch(address DocAdd)public view returns(bool){
        for(uint index=0;index<DocList.length;index++){
            if(DocList[index]==DocAdd){
                return true;
            }
        }
        return false;
    }

    //判断该用户是否在该电子病历访问集合里面:
    function JudgeGather(address Add,address EviAdd)public view returns(bool){
        //进行遍历操作：
        for(uint index=0;index<EvidenceMap[EviAdd].Gather.length;index++){
            if(Add==EvidenceMap[EviAdd].Gather[index]){
                return true;
            }
        }
        return false;
    }

    //判断患者是否有该电子病历:
    function JudgeEviBen(address Add,address EviAdd)public view returns(bool){
        for(uint index=0;index<PatientMap[Add].EvidenceBen[PatientMap[Add].EvidenceBenAdd].length;index++){
            if(EviAdd==PatientMap[Add].EvidenceBen[PatientMap[Add].EvidenceBenAdd][index]){
                return true;
            }
        }
        return false;
    }

    //判断用户是否有该体检报告:
    function JudgeReportBen(address Add,address ReportAdd)public view returns(bool){
       for(uint index=0;index<PatientMap[Add].ReportBen[PatientMap[Add].ReportBenAdd].length;index++){
            if(ReportAdd==PatientMap[Add].ReportBen[PatientMap[Add].ReportBenAdd][index]){
                return true;
            }
        }
        return false;
    }

    //判断该用户是否在体检报告授权集合之内:
    function JudgeReport(address Add,address ReportAdd)public view returns(bool){
        for(uint index=0;index<ReportMap[ReportAdd].Gather.length;index++){
            if(ReportMap[ReportAdd].Gather[index]==Add){
                return true;
            }
        }
        return false;
    } 

    //判断是否用户是否在该医生里面挂号:
    function JudgeRegistration(address PatAdd,address DocAdd)public view returns(bool){
        for(uint index=0;index<DocMap[DocAdd].PatientAddress.length;index++){
            if(PatAdd==DocMap[DocAdd].PatientAddress[index]){
                return true;
            }
        }
        return false;
    }

    //判断医生是否有着待就诊的电子病历:
    function JudgeTestEvi(address DocAdd,address EviAdd)public view returns(bool){
        for(uint index=0;index<DocMap[DocAdd].EviAddressList.length;index++){
            if(EviAdd==DocMap[DocAdd].EviAddressList[index]){
                return true;
            }
        }
        return false;
    }
    
}
