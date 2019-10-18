pragma solidity ^0.4.25;

contract CreditCert {

    //event

    event stuInfoInitEvent (
        uint64 stuID,
        string stuName,
        string usName,
        string major,
        string extInfo,
        uint64 registerTime
        );

    event stuInfoUpdateEvent (
        uint64 stuID,
        string stuName,
        string usName,
        string major,
        string extInfo,
        uint64 updateTime
        );

    event stuGradeRecordEvent (
        uint64 stuID,
        string stuName,
        uint32 grades,
        uint32 averageGrade,
        string extInfo,
        uint64 recordTime
        );

    event stuGradeUpdateEvent (
        uint64 stuID,
        string stuName,
        uint32 grades,
        uint32 averageGrade,
        string extInfo,
        uint64 updateTime
        );

    event activityRegisterEvent (
        uint32 actID,
        string actName,
        string sponsor,
        string status,
        string extInfo,
        uint64 registerTime
        );

    event activityInfoUpdateEvent (
        uint32 actID,
        string actName,
        string sponsor,
        string status,
        string extInfo,
        uint64 updateTime
        );

    event activityGradeRecodeEvent (
        uint32 actID,
        uint64 stuID,
        string actName,
        string stuName,
        string extInfo,
        uint64 recordTime,
        string actSignature
        );

    event certInfoInitEvent (
        uint64 certID,
        uint64 stuID,
        string stuName,
        string usName,
        string studyTime,
        uint64 initTime
        );

    event certInfoUpdateEvent (
        uint64 certID,
        uint64 stuID,
        string stuName,
        string usName,
        string studyTime,
        string certStatus,
        uint64 updateTime,
        string certSignature
        );


     //学生初始信息
    struct stuInfo{
    	uint64 stuID;  //学生唯一ID
     	string stuName;  //学生姓名
        string usName;  //所在学校名字
        uint32 usLevel; //学校星级 1-3
        string major;  //学生专业
        string extInfo;  //附加信息
        uint64 time;  //操作时间
        uint32 grades; //学生年级

        mapping (uint32 => gradeInfo) gradeInfoList;  //学期成绩列表

        mapping (uint32 => activityRecord) activityRecordList;  //参与活动列表
    }

    //学期成绩信息，,增加必修学分和选修学分两项，用于展示证书信息审核功能
    struct gradeInfo{
        uint64 stuID;  //学生唯一ID
        string stuName;  //学生姓名
        uint32 grades;  //学年，一般为1-4
        uint32 averageGrades;  //加权平均成绩
        uint32 obligatoryCredit;  //必修学分，新增
        uint32 optionalCredit; //选修学分，新增
        string extInfo;  //附加信息
        uint64 time;  //操作时间
    }

    //活动发布信息
    struct activityInfo{
        uint32 actID;  //活动ID
        string actName;  //活动名称
        string organizer;  //举办方
        string status;  //活动状态，一般分为未开始、进行中、已结束3种
        string extInfo;  //附加信息
        uint64 time;  //操作时间

    }

    //学生活动记录数据结构
    struct activityRecord{
    	uint32 actID;  //活动ID
        uint64 stuID;  //学生ID
        string actName;  //活动名称
        string stuName;  //学生姓名
        string extInfo;  //附加信息
        uint64 time;  //操作时间
        string actSignature;  //参与活动信息签名
    }
    //学历证书数据结构
    struct certInfo{
        uint64 certID;  //证书ID
        uint64 stuID;  //学生ID
        string stuName; //学生姓名
        string usName;  //证书颁发学校名称
        string major;  //学生专业
        string studyTime;  //在校学习时间
        string certStatus;  //证书状态
        string extInfo;  //附加信息
        uint64 time;  //操作时间
        string certSignature;  //证书发布签名
    }

    //学生信息列表：ID到学生信息表的映射
    mapping (uint64 => stuInfo) stuInfoList;
	//学历证书信息列表：ID到学历证书信息的映射
    mapping (uint64 => certInfo) certInfoList;
    //活动信息列表：ID到结构体的映射
	mapping (uint32 => activityInfo) activityInfoList;
}