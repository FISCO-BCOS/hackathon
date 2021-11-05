pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./lib/SafeMath.sol";
import "./utils/TimeUtil.sol";
import "./TableDefTools.sol";

/*
*
* report实现用户反馈。用户可以对其他人进行举报或者表扬好人好事
* 操作的表为t_report.主键为report_id
* user_id,to_user_id,event_content,event_type 
*/
contract report is TableDefTools{
    /*******   引入库  *******/
     using TimeUtil for *;
     using SafeMath for *;
     //反馈人在XX时间反馈XXX做了XXX事情，该事情类别为X
    event FEEDBACK(string reportId,string  Userid,string ToUserid,string event_content,string time);
    /*
    * 构造函数，初始化使用到的表结构
    *
    * @param    无
    *
    * @return   无
    */
    constructor() public{
        //初始化需要用到的表。反馈表
        initTableStruct(t_report_struct, TABLE_REPORT_NAME, TABLE_REPORT_PRIMARYKEY, TABLE_REPORT_FIELDS);
    }
   /*
    * 1.用户进行反馈
    *
    * @param _reportid      反馈ID号唯一主键  
    * @param _userid    反馈ID
    * @param _toUserid  被反馈对象id
    * @param _content   反馈内容    
    * @param _type     反馈类别 0为好人好事 1为举报做了坏事
    * @return 执行状态码
    *
   * 测试举例  参数一："17846"  
    * 参数二："1","2","张三欠钱一直迟迟不还","1"
    *注册成功返回SUCCESS,以及留言的句子。否则返回错误码，错误码对应的问题请参考TableDefTools写的
    */
   function feedback(string memory _reportId,string memory _userid,string memory _toUserid,string memory _content,string memory _type) public  returns(int8){
        //获取时间
        string memory nowDate = TimeUtil.getNowDate();
       
        string memory storeFields = StringUtil.strConcat7(_userid,',',_toUserid,',',_content,',',_type);


    emit FEEDBACK(StringUtil.strConcat2("反馈的ID号为:",_reportId),StringUtil.strConcat2("反馈人的ID为:",_userid),StringUtil.strConcat2("反馈对象的ID为:",_toUserid),StringUtil.strConcat2("反馈内容为:",_content),StringUtil.strConcat2("反馈时间为:",nowDate));

   
        return (insertOneRecord(t_report_struct,_reportId,storeFields,false));
    }
    /*
    * 2.根据反馈ID号查询反馈内容并以Json字符串方式输出
    *
    * @param _reportid  反馈id
    *
    * @return 执行状态码 
    * @return 该用户所有留言信息的Json字符串
    *
    * 测试举例  参数一："17846"
    */
    function getReportJson(string _reportid) public view returns(int8, string){
        return selectOneRecordToJson(t_report_struct, _reportid);
    }
   /*
    * 3.据留言ID号查询留言人ID号和留言内容并以字符串数组方式输出
    *
    * @param _reportid  留言id
    *
    * @return 执行状态码

    *
    * 测试举例  参数一："17846"
    */
    function getReportArray(string _reportid) public view returns(int8, string[]){
        return selectOneRecordToArray(t_report_struct,_reportid, ["report_id",_reportid]);
    }
    /*
    * 4.根据留言ID号查看留言内容，最后通过这个内容去调用文本审核api
    *
    * @param _reportid  留言id
    *
    * @return 留言内容
    *
    * 测试举例  参数一："17846"
    */
    function getReportContent(string _reportid) public view returns(string){
         int8 retCode;
         string[] memory retArray;
         (retCode, retArray) = getReportArray(_reportid);
         return retArray[2];
    }
    

}


