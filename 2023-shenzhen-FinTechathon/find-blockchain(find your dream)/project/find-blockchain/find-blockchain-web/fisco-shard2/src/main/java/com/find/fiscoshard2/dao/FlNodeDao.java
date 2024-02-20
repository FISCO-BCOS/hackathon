package com.find.fiscoshard2.dao;

import com.find.fiscoshard2.pojo.Account;
import com.find.fiscoshard2.pojo.FlNode;
import com.find.fiscoshard2.pojo.OnlineNode;
import org.apache.ibatis.annotations.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Update("UPDATE terminal.terminal_data v SET v.repValue = #{repValue} WHERE nodeId = #{nodeId}")
    void modifyRepValueById(String nodeId,String repValue);

    //根据nodeId修改节点pem文件地址
    @Update("UPDATE terminal.terminal_data v SET v.address = #{address} WHERE nodeId = #{nodeId}")
    void modifyAddressById(String nodeId,String address);

    //根据id列表查询信誉值
    @Select("<script>" +
            "SELECT v.repValue FROM terminal.terminal_data v WHERE nodeId IN " +
            "<foreach item='item' collection='nodeIdList' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<Float> getRepValue(@Param("nodeIdList") List<String> nodeIdList);

    //根据id查看账户信誉值
    @Select("SELECT v.repValue as repValue FROM terminal.terminal_data v WHERE nodeId = #{nodeId}")
    Float getRepValueById(@Param("nodeId")String nodeId);

    //新增账户数据
    @Insert("insert into terminal.terminal_data(nodeId,address,repValue) values (#{nodeId},#{address},#{repValue})")
    void insert(Account account);

    //根据nodeId查onlineNode
    @Select("SELECT * FROM terminal.terminal_data WHERE nodeId = #{nodeId}")
    OnlineNode getOnlineNodeById(@Param("nodeId")String nodeId);

    //查询区块链节点数量
    @Select("select count(*) from node0._sys_consensus_")
    int getBlkNode();
}
