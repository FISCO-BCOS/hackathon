package com.brecycle.service;

import com.brecycle.entity.Battery;
import com.brecycle.entity.EntInfo;
import com.brecycle.entity.Trade;
import com.brecycle.entity.User;
import com.brecycle.entity.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface PointService {

    /**
     * 部署积分合约
     */
    String deployContract() throws Exception;

    /**
     * 添加DAO账户
     */
    void addDAO();

    /**
     * 积分总额初始化
     * DAO账户操作
     */
    void initPoint() throws Exception;

    /**
     * 初始化指定积分
     * @param value1
     * @param value2
     */
    void initPoint(BigDecimal value1, BigDecimal value2);

    /**
     * 积分账户注册
     * DAO账户操作
     * @param user
     */
    void registAccount(User user) throws Exception;

    /**
     * 企业注册积分派发
     * 由DAO分发给对应企业账户
     * @param user
     * @param entInfo
     */
    void registPointPublish(User user, EntInfo entInfo, Long role) throws Exception;

    /**
     * 年度结算，对回收商拆解行为进行年度积分派发
     * @param recycleEnt
     */
    void yearRecycleEntPublish(User recycleEnt, BigDecimal phaseEndBatteryKah, BigDecimal currentEndBatteryKah) throws Exception;

    /**
     * 积分缴纳
     * @param payEnt 电池生产商、车企
     * @param batteryList
     */
    void payPoint(User payEnt, List<Battery> batteryList, Integer role);

    /**
     * 消费者积分获取
     * DAO执行transfer
     *
     * @param param
     */
    void customerPoint(CustomerTransferParam param);

    /**
     * 拆解环节梯次利用企业积分获取
     */
    void secondUsedPoint(BatteryEndParam param);

    /**
     * 拆解环节回收商积分获取
     */
    void secondRecyclePoint(BatteryEndParam param);

    /**
     * 查询积分记录
     * @param userName
     * @return
     */
    List<PointLogDTO> getPointLogs(String userName) throws Exception;

    /**
     * 查询指定用户的积分
     * @param userName
     * @return
     */
    BigDecimal getPoint(String userName) throws Exception ;

    /**
     * 积分交易申请
     * @param param
     * @param currentUserName
     */
    void apply(PointTradeApplyParam param, String currentUserName) throws Exception;

    /**
     * 交易查询
     * @param param
     * @return
     */
    PageResult<TradeListDTO> list(TradeListParam param);

    /**
     * 竞价交易
     * @param param
     */
    void bid(TradeParam param);

    /**
     * 到期交易
     * @param trade
     */
    void deal(Trade trade);
}
