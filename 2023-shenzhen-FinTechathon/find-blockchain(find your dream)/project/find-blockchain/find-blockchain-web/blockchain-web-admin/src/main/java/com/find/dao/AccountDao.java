package com.find.dao;

import com.find.pojo.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface AccountDao {

    //查询所有节点数据
    List<FlNode> getAccount();
    //根据账户id查询账户数据

    //查询所有Node节点数据
    @Select("SELECT * FROM terminal.terminal_data")
    List<OnlineNode> getAllNode();

    //查询所有OnlineNode节点数据
    @Select("SELECT * FROM terminal.terminal_data")
    List<OnlineNode> getAllOnlineNode();

    //根据用户名查看用户
    @Select("SELECT * FROM terminal.login_user WHERE name = #{name}")
    User selectUser(String name);

    //根据nodeId修改节点信誉值
    @Update("UPDATE terminal.terminal_data v SET v.repValue = #{repValue} WHERE nodeId = #{nodeId}")
    void modifyRepValueById(String nodeId,Double repValue);

    //根据nodeId修改节点状态
    @Update("UPDATE terminal.terminal_data v SET v.onlineState = #{onlineState}, v.ip = #{ip}, v.lon = #{lon}, v.lat = #{lat} WHERE nodeId = #{nodeId}")
    void modifyonlineStateById(String nodeId,String onlineState, String ip, String lon, String lat);

    //根据nodeId修改节点当前任务状态
    @Update("UPDATE terminal.terminal_data v SET v.scene = #{scene} WHERE nodeId = #{nodeId}")
    void modifySceneById(String nodeId,String scene);

    //根据nodeId修改节点pem文件地址
    @Update("UPDATEterminal.terminal_data v SET v.address = #{address} WHERE nodeId = #{nodeId}")
    void modifyAddressById(String nodeId,String address);

    //根据id列表查询信誉值
    @Select("<script>" +
            "SELECT v.repValue FROM terminal.terminal_data v WHERE nodeId IN " +
            "<foreach item='item' collection='nodeIdList' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<Float> getRepValue(@Param("nodeIdList") List<Integer> nodeIdList);

    //根据id列表查询可信
    @Select( "SELECT nodeId " +
            "FROM (SELECT * FROM terminal.terminal_data WHERE nodeId IN (${nodeIds})) AS filtered_data " +
            "WHERE security = '可信'")
    List<String> getSecurityNode(@Param("nodeIds") String nodeIds);

    //todo:获取在线安全节点列表

    //根据id查看节点信誉值
    @Select("SELECT v.repValue as repValue FROM terminal.terminal_data v WHERE nodeId = #{nodeId}")
    Double getRepValueById(@Param("nodeId")String nodeId);

    //根据id查看节点所属的域
    @Select("SELECT v.domain as domain FROM terminal.terminal_data v WHERE nodeId = #{nodeId}")
    String getDomainById(@Param("nodeId")String nodeId);

    //新增账户数据
    void insert(OnlineNode onlineNode);

    //根据nodeId查onlineNode
    @Select("SELECT * FROM terminal.terminal_data WHERE nodeId = #{nodeId}")
    OnlineNode getOnlineNodeById(@Param("nodeId")String nodeId);

    //根据id修改可信的值
    @Update("UPDATE terminal.terminal_data v SET v.security = #{security} WHERE nodeId = #{nodeId}")
    void UpdateSecurityById(@Param("nodeId")String nodeId,String security);

    @Select("SELECT * FROM terminal.terminal_data WHERE onlineState = '在线' AND scene = 'NONE' ORDER BY repValue DESC LIMIT 1")
    SharedNode getNewNode();

    @Select("SELECT * FROM terminal.User WHERE username = #{username}")
    User findByUsername(String username);

    @Select("UPDATE terminal.User u SET u.password  = #{password} WHERE username = #{username}")
    void UpdateByUsername(String username,String password);

    @Select("SELECT permission FROM terminal.User WHERE id = #{id}")
    String getPermissionById(int id);

    //插入训练数据
    @Insert("INSERT into terminal.train_data (nodeId,times,username,timestamp,reward,accuracy) VALUES (#{nodeId},#{times},#{username},#{timestamp},#{reward},#{accuracy})")
    void insertTrainData(String nodeId, String times, String username, String timestamp,double reward,float accuracy);
//    //根据id修改可信的值
//    @Select("SELECT * FROM terminal.terminal_data")
//    List<SharedNode> getCarNode();

    //插入用户发起的任务信息（第n次发起，用户名，时间，报酬/人，训练精度）
    @Insert("INSERT into terminal.user_task (username,times,timestamp,reward,accuracy,work_num) VALUES (#{username},#{times},#{timestamp},#{reward},#{accuracy},#{work_num})")
    void insertUserTaskInfo(String username,String times,String timestamp,double reward,float accuracy,int work_num);

    //测试查询某个用户全部任务的发起与结果信息（username，timestamp）
    @Select("SELECT * FROM terminal.user_task WHERE username = #{username}")
    List<TrainInfo> selectUserTaskInfo(String username);

    //查询用户在某一轮任务下的各个客户端训练情况——联合索引（username，timestamp）
    @Select("SELECT * FROM terminal.train_data WHERE username = #{username} AND timestamp <= #{timestamp} ORDER BY timestamp DESC limit #{dataNums}")
    List<TrainInfo> selectTrainInfoByUser(String username,String timestamp,int dataNums);

}
