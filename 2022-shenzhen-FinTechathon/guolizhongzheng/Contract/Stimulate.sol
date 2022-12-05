pragma solidity ^0.4.25;
import "./Module.sol";
//激励机制合约:
//统计用户所获得的积分再运算:
contract Stimulate is Module{

    //一条电子病历记录随着共享人数的增加分数会增加:
    //上限是10分:
    function StimulateNumber(address PatAdd)internal view returns(uint){
        uint a=0;
        //a是关于共享人数的积分:
        for(uint index=0;index<PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd].length;index++){
            if(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Gather.length>10){
                a=a+10;
            }else{
                a=a+EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Gather.length;
            }
        }
        return a;
    }

    //该医疗数据如果珍贵程度分为 1，2，3:
    //统计医疗数据珍贵程度积分值:
    function StimulateValue(address PatAdd)internal view returns(uint){
        //b是关于价值的积分:
        uint b=0;
        //价值为一级:
        for(uint index=0;index<PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd].length;index++){

           if(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Gather.length>0){

            if(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Value[0]==1){
                b+=1;
            }else if(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Value[0]==2){
                b+=5;
            }else if(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Value[0]==3){
                b+=10;
            }
           
           }
        
        }
        return b;
    }

    //统计医疗数据稀缺程度积分值 1，2，3:
    function StimulateScarity(address PatAdd)internal view returns(uint){

        uint c=0;
        //c是关于稀缺程度的积分:
        for(uint index=0;index<PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd].length;index++){

        if(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Gather.length>0){

        if(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Value[1]==1){
                c+=1;//添加价值的分数;
        }else if(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Value[1]==2){
                 c+=5;//添加价值的分数;
        }else if(EvidenceMap[PatientMap[PatAdd].EvidenceBen[PatientMap[PatAdd].EvidenceBenAdd][index]].Value[1]==3){
                c+=10;//添加价值的分数;
        }

        }
        
        }
        return c;
    }

    //统计所有的共享因子:
    function QueryAll(address PatAdd)internal view returns(uint){
        uint sum=0;
        sum=StimulateNumber(PatAdd)*2+ StimulateValue(PatAdd)*4+StimulateScarity(PatAdd)*4;
        return sum;

    }


    

}
