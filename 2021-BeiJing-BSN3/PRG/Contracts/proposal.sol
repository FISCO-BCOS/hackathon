pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./lib/SafeMath.sol";
import "./utils/TimeUtil.sol";
import "./TableDefTools.sol";

/*
*
* proposal实现用户提议功能。针对用户提议内容会调用一次文本审核api接口，若存在违规词将会给用户扣分
* 首先查看用户是否注册，登陆。如果没有则不能提议
* 操作的表为t_proposal.
*user_id,proposal_content,proposal_time,approval_cnt,disapproval_cnt,check_result
*/
contract proposal is TableDefTools{
    /*******   引入库  *******/
     using TimeUtil for *;
     using SafeMath for *;
     event PROPOSAL(string proposalid,string Userid,string SuggestContent,string time);//提议事件.提议人ID，提议标题，内容。时间
     event APPROVAL(string proposalid,string Userid,string time);//提议事件，谁什么时间投了赞成票
     event DISAPPROVAL(string proposalid,string Userid,string time);//提议事件，谁什么时间投了赞成票
   /*
    * 构造函数，初始化使用到的表结构
    *
    * @param    无
    *
    * @return   无
    */
    constructor() public{
        //初始化需要用到的表。建言献策表
        initTableStruct(t_proposal_struct, TABLE_PROPOSAL_NAME, TABLE_PROPOSAL_PRIMARYKEY, TABLE_PROPOSAL_FIELDS);
    }
   /*
    * 1.用户提议
    *
    * @param _proposalid  提议ID号  
    * @param user_id  提议人ID
    * @param proposal_content  提议内容
    * @param proposal_time  提议时间（自动获取）
    * @param approval_cnt  支持票数（默认0）
    * @param disapproval_cnt  反对票数（默认0）
    * @param check_result 审核结果（默认审核中）
    * @return 执行状态码
    *
   * 测试举例  参数一："17846"  
    * 参数二："尊敬的领导，本人通过网络知道了了去年事务部和十大银行签了优抚协议，其它省份落实的很好，希望江西省也能对接下江西银行，江西农商银行等"
    *注册成功返回SUCCESS,以及提议的句子。否则返回错误码，错误码对应的问题请参考TableDefTools写的
    */
   function suggestUser(string memory _proposalid,string memory _userid,string memory _content) public  returns(int8){
        //获取时间
        string memory nowDate = TimeUtil.getNowDate();

         // string memory storeFields = StringUtil.strConcat5(_userid,',',suggestfields,',',nowDate);
        string memory lastThreeParams = "0,0,审核中";
        string memory storeFields = StringUtil.strConcat7(_userid,',',_content,',',nowDate,',',lastThreeParams);
        emit PROPOSAL(StringUtil.strConcat2("提议的ID号为:",_proposalid),StringUtil.strConcat2("提议人的ID为:",_userid),StringUtil.strConcat2("提议内容为:",_content),StringUtil.strConcat2("提议时间为:",nowDate));   
        return (insertOneRecord(t_proposal_struct,_proposalid,storeFields,false));
    }
    /*
    * 2.根据提议ID号查询提议人ID号和提议内容并以Json字符串方式输出
    *
    * @param _proposalid  提议id
    *
    * @return 执行状态码 
    * @return 该用户所有提议信息的Json字符串
    *
    * 测试举例  参数一："17846"
    */
    function getSuggestRecordJson(string _proposalid) public view returns(int8, string){
        return selectOneRecordToJson(t_proposal_struct, _proposalid);
    }
   /*
    * 3.据提议ID号查询提议人ID号和提议内容并以字符串数组方式输出
    *
    * @param _proposalid  提议id
    *
    * @return 执行状态码
    * @return 该提议人ID和提议内容信息的字符串数组
    *
    * 测试举例  参数一："17846"
    */
    function getSuggestRecordArray(string _proposalid) public view returns(int8, string[]){
        return selectOneRecordToArray(t_proposal_struct,_proposalid, ["proposal_id",_proposalid]);
    }
    /*
    * 4.根据提议ID号查看提议内容
    *
    * @param _proposalid  提议id
    *
    * @return 提议内容
    *
    * 测试举例  参数一："17846"
    */
    function getSuggestContent(string _proposalid) public view returns(string){
         int8 retCode;
         string[] memory retArray;
         (retCode, retArray) = getSuggestRecordArray(_proposalid);
         return retArray[1];
    }
     /*
    * 5.根据提议ID号查看提议人ID号，
    *
    * @param _proposalid  提议id
    *
    * @return 提议人Id
    *
    * 测试举例  参数一："17846"
    */
    function getSuggestId(string _proposalid) public view returns(string){
         int8 retCode;
         string[] memory retArray;
         (retCode, retArray) = getSuggestRecordArray(_proposalid);
         return retArray[0];
    }
    /*
    * 6.根据提议ID号查看当前提议的支持票数
    *
    * @param _proposalid  提议id
    *
    * @return 提议人Id
    *
    * 测试举例  参数一："17846"
    */
    function getApproval(string _proposalid) public view returns(uint){
         int8 retCode;
         uint proposalApproval;
         string[] memory retArray;
         (retCode, retArray) = getSuggestRecordArray(_proposalid);
         if(retCode == SUCCESS_RETURN){
            proposalApproval=TypeConvertUtil.stringToUint(retArray[3]);
            return proposalApproval;
         }else{
             return uint(-1);
         }
 
    }

    /*
    * 7根据提议ID号查看当前提议的反对票数
    *
    * @param _proposalid  提议id
    *
    * @return 提议人Id
    *
    * 测试举例  参数一："17846"
    */
    function getDisApproval(string _proposalid) public view returns(uint){
         int8 retCode;
         uint proposalApproval;
         string[] memory retArray;
         (retCode, retArray) = getSuggestRecordArray(_proposalid);
         if(retCode == SUCCESS_RETURN){
            proposalApproval=TypeConvertUtil.stringToUint(retArray[4]);
            return proposalApproval;
         }else{
             return uint(-1);
         }
    }
    /*
    * 8根据提议ID号查看当前提议的审核结果
    *
    * @param _proposalid  提议id
    *
    * @return 提议人Id
    *
    * 测试举例  参数一："17846"
    */
    function getResult(string _proposalid) public view returns(string){
         int8 retCode;
         string[] memory retArray;
         (retCode, retArray) = getSuggestRecordArray(_proposalid);
         return retArray[5];
    }
   /*
    *  9.向proposal表进行投赞成票操作
    *
    * @param _proposalid    提议id,唯一主键
    * @param _userid        投票人的ID
    * @return 执行状态码
    *测试举例  参数一："191867345212322",123,1
    */
     function Approval(string memory _proposalid,string memory _userid) public returns(int8) {
          // 该提案当前支持票票数
          uint proposalHasApproval;
          // 该提案更新后的支持票票数
          uint proposalNowApproval;
          // 查询信息返回状态
         int8 queryRetCode;
         // 更新信息返回状态
         int8 updateRetCode;
        // 数据表返回信息
         string[] memory retArray;
        // 获得当前的时间
         string memory updateTime= TimeUtil.getNowDate();
        // 查看该用户记录信息
        (queryRetCode, retArray) = selectOneRecordToArray(t_proposal_struct,_proposalid, ["proposal_id",_proposalid]);
        proposalHasApproval=getApproval(_proposalid);
        proposalNowApproval=SafeMath.add(proposalHasApproval,1);
       emit APPROVAL(StringUtil.strConcat2("提议的ID号为:",_proposalid),StringUtil.strConcat2("已投支持票,该投票人的ID为:",_userid),StringUtil.strConcat2("投票时间为:",updateTime));   
         string memory changedFieldsStr = getChangeFieldsString(retArray,3, TypeConvertUtil.uintToString(proposalNowApproval));//转换回字符串类型
          if(proposalNowApproval>9){
                  string memory changedFieldsStr4 = getChangeFieldsString(retArray,5,"支持");
                  return(updateOneRecord(t_proposal_struct,_proposalid,changedFieldsStr4));    
          }
           return(updateOneRecord(t_proposal_struct,_proposalid,changedFieldsStr)); 
     }
    /*
    *  10.向proposal表进行投赞成票操作
    *
    * @param _proposalid    提议id,唯一主键
    * @param _userid        投票人的ID

    * @return 执行状态码
    *测试举例  参数一："191867345212322",1
    */
    function DisApproval(string memory _proposalid,string memory _userid) public returns(int8) {

  
        // 该提案当前反对票票数
        uint proposalHasDisApproval;

        // 该提案更新后的反对票票数
        uint proposalNowDisApproval;

        // 查询信息返回状态
        int8 queryRetCode;
        // 更新信息返回状态
        int8 updateRetCode;
        // 数据表返回信息
        string[] memory retArray;
        // 获得当前的时间
        string memory updateTime= TimeUtil.getNowDate();
        // 查看该用户记录信息
        (queryRetCode, retArray) = selectOneRecordToArray(t_proposal_struct,_proposalid, ["proposal_id",_proposalid]);
         //该提案存在
        //将提案票数转换为整数进行操作selectOneRecordToArray
            proposalHasDisApproval=getDisApproval(_proposalid);

         // 更新投票结果
          proposalNowDisApproval=SafeMath.add(proposalHasDisApproval,1);//调用Safemath的减法操作，避免溢出问题
            //对信用分操作完毕后。更新用户表
             string memory changedFieldsStr = getChangeFieldsString(retArray,4, TypeConvertUtil.uintToString(proposalNowDisApproval));
             //过半通过/不通过
             if(proposalNowDisApproval>9){
                  string memory changedFieldsStr3 = getChangeFieldsString(retArray,5,"反对");
                  return(updateOneRecord(t_proposal_struct,_proposalid,changedFieldsStr3));  
             }
             emit DISAPPROVAL(StringUtil.strConcat2("提议的ID号为:",_proposalid),StringUtil.strConcat2("已投反对票,该投票人的ID为:",_userid),StringUtil.strConcat2("投票时间为:",updateTime));   
            return(updateOneRecord(t_proposal_struct,_proposalid,changedFieldsStr));    
    }
}


