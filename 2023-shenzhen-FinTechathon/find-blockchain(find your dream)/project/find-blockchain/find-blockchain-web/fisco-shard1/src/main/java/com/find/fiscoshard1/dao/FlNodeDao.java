package com.find.fiscoshard1.dao;

import com.find.fiscoshard1.pojo.FlNode;
import com.find.fiscoshard1.pojo.OnlineNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FlNodeDao {

    //查询所有账户数据
    List<FlNode> getAccount();
    //根据账户id查询账户数据

    //根据id删除账户
    public void deleteAccount(String nodeId);

    //根据nodeId修改节点信誉值
    @Update("UPDATE validation_6g.federalNode v SET v.repValue = #{repValue} WHERE nodeId = #{nodeId}")
    void modifyRepValueById(String nodeId,String repValue);


    //根据id列表查询信誉值
    @Select("<script>" +
            "SELECT v.repValue FROM validation_6g.federalNode v WHERE nodeId IN " +
            "<foreach item='item' collection='nodeIdList' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<Float> getRepValue(@Param("nodeIdList") List<String> nodeIdList);

    //根据id查看账户信誉值
    @Select("SELECT v.repValue as repValue FROM validation_6g.federalNode v WHERE nodeId = #{nodeId}")
    Float getRepValueById(@Param("nodeId")String nodeId);

    //新增账户数据
    void insert(OnlineNode onlineNode);

    //根据nodeId查onlineNode
    @Select("SELECT * FROM validation_6g.federalNode WHERE nodeId = #{nodeId}")
    OnlineNode getOnlineNodeById(@Param("nodeId")String nodeId);
}
