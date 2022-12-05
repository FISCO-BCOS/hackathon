package com.brecycle.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brecycle.config.FiscoBcos;
import com.brecycle.config.PointConfig;
import com.brecycle.contract.PointController;
import com.brecycle.contract.TradeContract;
import com.brecycle.controller.hanlder.BusinessException;
import com.brecycle.entity.*;
import com.brecycle.entity.dto.*;
import com.brecycle.enums.*;
import com.brecycle.mapper.BatteryMapper;
import com.brecycle.mapper.TradeMapper;
import com.brecycle.mapper.UserMapper;
import com.brecycle.service.PointService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 积分模块
 *
 * @author cmgun
 */
@Slf4j
@Service
public class PointServiceImpl implements PointService {

    /**
     * 车企注册派分需要的参数
     */
    public static final String CAR_REGIST_PARAM = "carProductRegist";
    /**
     * 车企注册派分需要的参数
     */
    public static final String PRODUCT_REGIST_PARAM = "batteryProductRegist";

    public static final String TRADE_INFO_KEY = "point";

    @Autowired
    FiscoBcos fiscoBcos;
    @Autowired
    PointConfig pointConfig;
    @Autowired
    UserMapper userMapper;
    @Autowired
    TradeMapper tradeMapper;
    @Autowired
    BatteryMapper batteryMapper;

    @Override
    public String deployContract() throws Exception {
        // 查找DAO账户
        User admin = userMapper.selectByUserName(pointConfig.dao);
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(admin.getPrivateKey());
        // 部署交易，标记为成功
        PointController pointController = PointController.deploy(client, currentKeyPair);
        log.info("pointController addr:{}", pointController.getContractAddress());
        return pointController.getContractAddress();
    }

    @Override
    public void addDAO() {
        User dao = userMapper.selectByUserName(pointConfig.dao);
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(dao.getPrivateKey());
        // 部署交易，标记为成功
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt result = pointController.addDAO(dao.getAddr());

        log.info("addDAO执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
    }

    @Override
    public void initPoint() throws Exception {
        // 查找DAO账户
        User admin = userMapper.selectByUserName(pointConfig.dao);
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(admin.getPrivateKey());
        // 计算初始积分1：电池生产商、车企
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(Calendar.YEAR);
        Map<String, Integer> marketPredict = pointConfig.getMarketPredict();
        Map<String, Integer> systemPredict = pointConfig.getSystemPredict();
        BigDecimal bt_1 = new BigDecimal(systemPredict.get(String.valueOf(currentYear - 1)));
        BigDecimal at = new BigDecimal(marketPredict.get(String.valueOf(currentYear)));
        BigDecimal at_1 = new BigDecimal(marketPredict.get(String.valueOf(currentYear - 1)));
        BigDecimal carAvgKmh = BigDecimal.valueOf(pointConfig.getCarAvgKAh());
        BigDecimal alpha = BigDecimal.valueOf(pointConfig.getAlpha());
        BigDecimal point1 = bt_1.multiply(alpha).multiply(at).multiply(carAvgKmh).divide(at_1, 0, RoundingMode.DOWN);
        log.info("initPoint, point1={}", point1);
        // 初始积分2：回收商
        BigDecimal point2;
        BigDecimal bt1 = new BigDecimal(systemPredict.get(String.valueOf(currentYear + 1)));
        if (pointConfig.getVarT() <= 5) {
            point2 = bt1.multiply(alpha).multiply(carAvgKmh).setScale(0, RoundingMode.DOWN);
        } else {
            Float partRatio = pointConfig.getEndRecyclePartRatio();
            BigDecimal endRecyclePartRatio = new BigDecimal(1 - partRatio / 10);
            BigDecimal bt_4 = new BigDecimal(systemPredict.get(String.valueOf(currentYear - 1)));
            point2 = bt1.multiply(alpha).add(bt_4.multiply(endRecyclePartRatio)).multiply(carAvgKmh).setScale(0, RoundingMode.DOWN);
        }
        log.info("initPoint, point2={}", point2);
        // 获取积分合同
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt result = pointController.initPoint(point1.toBigInteger(), point2.toBigInteger());
        log.info("initPoint执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
        BigInteger totalPoint = pointController.getPoint(admin.getAddr());
        log.info("initPoint, current dao point:{}", totalPoint);
    }

    @Override
    public void initPoint(BigDecimal value1, BigDecimal value2) {
        // 查找DAO账户
        User admin = userMapper.selectByUserName(pointConfig.dao);
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(admin.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt result = pointController.initPoint(value1.toBigInteger(), value2.toBigInteger());
        log.info("initPoint执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
    }

    @Override
    public void registAccount(User user) throws Exception {
        // 积分账户注册
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(user.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt result = pointController.register();
        log.info("registAccount执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
        BigInteger point = pointController.getPoint(user.getAddr());
        log.info("addr:{}, point:{}", user.getAddr(), point);
    }

    @Override
    public void registPointPublish(User user, EntInfo entInfo, Long role) throws Exception {
        if (!role.equals(Long.valueOf(RoleEnums.PRODUCTOR.getKey())) && !role.equals(Long.valueOf(RoleEnums.CAR.getKey()))) {
            log.info("userId:{}, 非生产商或车企，不进行注册积分派发", user.getId());
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(Calendar.YEAR);
        Map<String, Integer> marketPredict = pointConfig.getMarketPredict();
        Map<String, Integer> systemPredict = pointConfig.getSystemPredict();
        BigDecimal batteryKah = BigDecimal.valueOf(pointConfig.getBatteryAvgKAh());
        BigDecimal carKah = BigDecimal.valueOf(pointConfig.getCarAvgKAh());
        JSONObject info = JSONObject.parseObject(entInfo.getInfo());
        BigDecimal payRatio = BigDecimal.valueOf(pointConfig.getProducePayRatio());
        BigDecimal carRatio = new BigDecimal(1).subtract(payRatio);
        BigDecimal alpha = BigDecimal.valueOf(pointConfig.getAlpha());
        BigDecimal point;
        if (pointConfig.isFirstYear) {
            // 首年计算规则
            BigDecimal bt = new BigDecimal(systemPredict.get(String.valueOf(currentYear)));
            BigDecimal bt_1 = new BigDecimal(systemPredict.get(String.valueOf(currentYear - 1)));
            if (role.equals(Long.valueOf(RoleEnums.PRODUCTOR.getKey()))) {
                BigDecimal batteryProductRegist = info.getBigDecimal(PRODUCT_REGIST_PARAM);
                point = bt.multiply(alpha).multiply(batteryProductRegist).multiply(payRatio).multiply(batteryKah).divide(bt_1, 0, RoundingMode.DOWN);
            } else {
                BigDecimal carProductRegist = info.getBigDecimal(CAR_REGIST_PARAM);
                point = bt.multiply(alpha).multiply(carProductRegist).multiply(carRatio).multiply(carKah).divide(bt_1, 0, RoundingMode.DOWN);
            }
        } else {
            // 次年计算规则
            BigDecimal at = new BigDecimal(marketPredict.get(String.valueOf(currentYear)));
            BigDecimal at_1 = new BigDecimal(marketPredict.get(String.valueOf(currentYear - 1)));
            if (role.equals(Long.valueOf(RoleEnums.PRODUCTOR.getKey()))) {
                BigDecimal batteryProductRegist = info.getBigDecimal(PRODUCT_REGIST_PARAM);
                point = batteryProductRegist.multiply(alpha).multiply(at).multiply(payRatio).multiply(batteryKah).divide(at_1, 0, RoundingMode.DOWN);
            } else {
                BigDecimal carProductRegist = info.getBigDecimal(CAR_REGIST_PARAM);
                point = at.multiply(alpha).multiply(carProductRegist).multiply(carRatio).multiply(carKah).divide(at_1, 0, RoundingMode.DOWN);
            }
        }
        if (point.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("当前积分不大于0，不进行派发，积分:{}", point);
            return;
        }
        // 查找DAO账户
        User admin = userMapper.selectByUserName(pointConfig.dao);
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(admin.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);

        // 查一下DAO的积分
        BigInteger totalPoint = pointController.getPoint(admin.getAddr());
        log.info("initPoint, current dao point:{}", totalPoint);

        TransactionReceipt result = pointController.daoTransfer(user.getAddr(), point.toBigInteger());
        log.info("daoTransfer执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
    }

    @Override
    public void yearRecycleEntPublish(User recycleEnt, BigDecimal phaseEndBatteryKah, BigDecimal currentEndBatteryKah) throws Exception {
        // 年度回收商积分派发
        User admin = userMapper.selectByUserName(pointConfig.dao);
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(admin.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        BigInteger totalAmount2 = pointController.getTotalAmount2();
        BigDecimal point = currentEndBatteryKah.multiply(new BigDecimal(totalAmount2)).divide(phaseEndBatteryKah, 0, RoundingMode.DOWN);
        TransactionReceipt result = pointController.daoTransfer(recycleEnt.getAddr(), point.toBigInteger());
        log.info("daoTransfer执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
    }

    @Override
    public void payPoint(User payEnt, List<Battery> batteryList, Integer role) {
        // 缴纳积分
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(payEnt.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        // 计算需要缴纳的积分
        BigDecimal alpha = BigDecimal.valueOf(pointConfig.getAlpha());
        BigDecimal producePayRatio = BigDecimal.valueOf(pointConfig.getProducePayRatio());
        BigDecimal payRatio = role.equals(RoleEnums.PRODUCTOR.getKey()) ? producePayRatio : BigDecimal.ONE.subtract(producePayRatio);
        BigDecimal totalKAh = BigDecimal.ZERO;
        for (Battery battery : batteryList) {
            totalKAh = totalKAh.add(new BigDecimal(battery.getKah()));
        }
        BigDecimal payPoint = totalKAh.multiply(alpha).multiply(payRatio).setScale(0, RoundingMode.DOWN);
        User dao = userMapper.selectByUserName(pointConfig.dao);
        TransactionReceipt result = pointController.pay(dao.getAddr(), payPoint.toBigInteger());
        log.info("point.pay执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
    }

    @Override
    public void customerPoint(CustomerTransferParam param) {
        // 消费者获取积分
        User customer = userMapper.selectByUserName(param.getOriginUserName());
        Battery battery = batteryMapper.selectById(param.getId());
        BigDecimal actualChargeTimes = new BigDecimal(param.getChargeTimes());
        BigDecimal designChargeTimes = new BigDecimal(battery.getChargeTimes());
        BigDecimal kah = new BigDecimal(battery.getKah());
        BigDecimal index = BigDecimal.ONE.add(actualChargeTimes.subtract(designChargeTimes).divide(designChargeTimes, 0, RoundingMode.DOWN));
        BigDecimal alpha = new BigDecimal(pointConfig.alpha);
        BigDecimal partRatio = new BigDecimal(pointConfig.firstRecyclePartRatio);
        BigDecimal point = index.multiply(alpha).multiply(partRatio).multiply(kah).setScale(0, RoundingMode.DOWN);
        // SDK配置
        User admin = userMapper.selectByUserName(pointConfig.dao);
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(admin.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt result = pointController.daoTransfer(customer.getAddr(), point.toBigInteger());
        log.info("customerPoint.daoTransfer执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
    }

    @Override
    public void secondUsedPoint(BatteryEndParam param) {
        // 梯次利用企业积分派发
        User storedEnt = userMapper.selectByUserName(param.getOriginUserName());
        Battery battery = batteryMapper.selectById(param.getId());
        BigDecimal actualChargeTimes = new BigDecimal(param.getChargeTimes());
        BigDecimal designChargeTimes = new BigDecimal(battery.getChargeTimes());
        BigDecimal kah = new BigDecimal(battery.getKah());
        BigDecimal index = BigDecimal.ONE.add(actualChargeTimes.subtract(designChargeTimes).divide(designChargeTimes, 0, RoundingMode.DOWN));
        BigDecimal alpha = new BigDecimal(pointConfig.alpha);
        BigDecimal partRatio = new BigDecimal(pointConfig.secondRecyclePartRatio);
        BigDecimal point = index.multiply(alpha).multiply(partRatio).multiply(kah).setScale(0, RoundingMode.DOWN);
        // SDK配置
        User admin = userMapper.selectByUserName(pointConfig.dao);
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(admin.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt result = pointController.daoTransfer(storedEnt.getAddr(), point.toBigInteger());
        log.info("secondUsedPoint.daoTransfer执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
    }

    @Override
    public void secondRecyclePoint(BatteryEndParam param) {
        // 拆解回收积分派发
        User recycleEnt = userMapper.selectByUserName(param.getToUserName());
        Battery battery = batteryMapper.selectById(param.getId());
        // 不同电池的index计算不同
        BigDecimal index;
        if (StringUtils.equals(BatteryType.Li.getValue(), battery.getType())) {
            BigDecimal liBRatio = new BigDecimal("0.85");
            BigDecimal bRatio = new BigDecimal("0.98");
            BigDecimal liRatio = param.getLiRatio().subtract(liBRatio).divide(liBRatio, 0, RoundingMode.DOWN);
            BigDecimal mnRatio = param.getMnRatio().subtract(bRatio).divide(bRatio, 0, RoundingMode.DOWN);
            BigDecimal coRatio = param.getCoRatio().subtract(bRatio).divide(bRatio, 0, RoundingMode.DOWN);
            BigDecimal niRatio = param.getNiRatio().subtract(bRatio).divide(bRatio, 0, RoundingMode.DOWN);
            BigDecimal recycleRatio = liRatio.add(mnRatio).add(coRatio).add(niRatio);
            index = BigDecimal.ONE.add(recycleRatio);
        } else {
            BigDecimal bRatio = new BigDecimal("0.95");
            BigDecimal liRatio = param.getLiRatio().subtract(bRatio).divide(bRatio, 0, RoundingMode.DOWN);
            BigDecimal mnRatio = param.getMnRatio().subtract(bRatio).divide(bRatio, 0, RoundingMode.DOWN);
            BigDecimal coRatio = param.getCoRatio().subtract(bRatio).divide(bRatio, 0, RoundingMode.DOWN);
            BigDecimal niRatio = param.getNiRatio().subtract(bRatio).divide(bRatio, 0, RoundingMode.DOWN);
            BigDecimal otherRatio = param.getOtherRatio().subtract(bRatio).divide(bRatio, 0, RoundingMode.DOWN);
            BigDecimal recycleRatio = liRatio.add(mnRatio).add(coRatio).add(niRatio).add(otherRatio);
            index = BigDecimal.ONE.add(recycleRatio);
        }
        BigDecimal alpha = new BigDecimal(pointConfig.alpha);
        BigDecimal partRatio = new BigDecimal(pointConfig.secondRecyclePartRatio);
        BigDecimal kah = new BigDecimal(battery.getKah());
        BigDecimal point = index.multiply(partRatio).multiply(alpha).multiply(kah).setScale(0, RoundingMode.DOWN);

        // SDK配置
        User admin = userMapper.selectByUserName(pointConfig.dao);
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(admin.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt result = pointController.daoTransfer(recycleEnt.getAddr(), point.toBigInteger());
        log.info("secondRecyclePoint.daoTransfer执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
    }

    @Override
    public List<PointLogDTO> getPointLogs(String userName) throws Exception {
        User user = userMapper.selectByUserName(userName);
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(user.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        DynamicArray<PointController.Struct0> logs = pointController.getPointLog(user.getAddr());
        List<PointLogDTO> result = Lists.newArrayList();
        for (PointController.Struct0 item : logs.getValue()) {
            PointLogDTO dto = new PointLogDTO();
            dto.setAddr(item.addr);
            Date optTime = new Date(Long.parseLong(item.timestamp.toString()));
            String formatOptTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(optTime);
            dto.setOptTime(formatOptTime);
            dto.setRemark(item.remark);
            dto.setBeforePoint(new BigDecimal(item.beforePoint));
            dto.setAfterPoint(new BigDecimal(item.afterPoint));
            result.add(dto);
        }
        return result;
    }

    @Override
    public BigDecimal getPoint(String userName) throws Exception {
        User user = userMapper.selectByUserName(userName);
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(user.getPrivateKey());
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        BigInteger result = pointController.getPoint(user.getAddr());
        return new BigDecimal(result);
    }

    @Override
    public void apply(PointTradeApplyParam param, String currentUserName) throws Exception {
        // 获取当前登录用户的账户信息
        User seller = userMapper.selectByUserName(currentUserName);
        // 指定买方
        if (param.isHasTarget()) {
            saveSuccessTrade(param, seller);
            return;
        }
        // 委托交易
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(seller.getPrivateKey());
        // 冻结积分
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt result = pointController.freeze(param.getPoint().toBigInteger());
        log.info("point.freeze执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
        // 记录交易的内容，即积分数量
        JSONObject info = new JSONObject();
        info.put(TRADE_INFO_KEY, param.getPoint());
        // 部署交易，标记为成功
        TradeContract tradeContract = TradeContract.deploy(client, currentKeyPair, info.toJSONString()
                , BigInteger.valueOf(param.getLowestAmt().longValue()), BigInteger.valueOf(param.getExpectAmt().longValue())
                , BigInteger.valueOf(param.getBidDays().longValue()));
        // 保存交易数据
        // 保存数据
        Trade trade = new Trade();
        trade.setSellerId(seller.getId());
        trade.setLowestAmt(param.getLowestAmt());
        trade.setExpectAmt(param.getExpectAmt());
        trade.setBidDays(param.getBidDays());
        trade.setCreateTime(new Date());
        trade.setTradeType(TradeType.POINT.getValue());
        trade.setStatus(TradeStatus.BIDING.getValue());
        trade.setAddr(tradeContract.getContractAddress());
        trade.setInfo(info.toJSONString());
        tradeMapper.insert(trade);
    }

    @Override
    public PageResult<TradeListDTO> list(TradeListParam param) {
        IPage page = new Page<>(param.getPageNo(), param.getPageSize());
        if (param.getMyId() == null) {
            param.setStatus(TradeStatus.BIDING.getValue());
        }
        param.setTradeType(TradeType.POINT.getValue());
        IPage<Trade> data = tradeMapper.selectTradeListByPage(page, param);
        PageResult<TradeListDTO> result = new PageResult<>();
        result.setTotal(data.getTotal());
        result.setPageNo(data.getCurrent());
        result.setPageCount(data.getPages());
        result.setPageSize(data.getSize());
        if (CollectionUtils.isNotEmpty(data.getRecords())) {
            result.setData(data.getRecords().stream().map(item -> {
                TradeListDTO tradeListDTO = new TradeListDTO();
                tradeListDTO.setId(item.getId());
                User seller = userMapper.selectById(item.getSellerId());
                tradeListDTO.setSellerName(seller.getName());
                if (item.getBuyerId() != null) {
                    User buyer = userMapper.selectById(item.getBuyerId());
                    tradeListDTO.setBuyerName(buyer.getName());
                    tradeListDTO.setTradeAmt(item.getTradeAmt());
                }
                tradeListDTO.setStatus(item.getStatus());
                tradeListDTO.setLowestAmt(item.getLowestAmt());
                // 交易积分
                JSONObject info = JSON.parseObject(item.getInfo());
                tradeListDTO.setInfo(String.valueOf(info.get(TRADE_INFO_KEY)));
                return tradeListDTO;
            }).collect(Collectors.toList()));
        } else {
            result.setData(Lists.newArrayList());
        }
        return result;
    }

    @Override
    public void bid(TradeParam param) {
        // 竞价交易
        User buyer = userMapper.selectByUserName(param.getBuyerUserName());
        Trade trade = tradeMapper.selectById(param.getTradeId());
        if (trade == null) {
            throw new BusinessException("交易不存在");
        }
        if (buyer.getId().equals(trade.getSellerId())) {
            throw new BusinessException("不能对自己发起的交易进行竞价");
        }
        Date now = new Date();
        Date endTime = DateUtils.addDays(trade.getCreateTime(), trade.getBidDays());
        if (now.compareTo(endTime) > 0) {
            throw new BusinessException("超出竞价时间");
        }
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(buyer.getPrivateKey());
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        TradeContract tradeContract = TradeContract.load(trade.getAddr(), client, currentKeyPair);
        TransactionReceipt receipt = tradeContract.bid(BigInteger.valueOf(param.getBidAmt().longValue()));
        log.info("point.bid执行结果：{}", receipt);
        Tuple1<Boolean> result = tradeContract.getBidOutput(receipt);
        if (!StringUtils.equals(receipt.getStatus(), "0x0")) {
            throw new BusinessException("提交失败，已存在更高竞价");
        }
        if (result.getValue1()) {
            // 达到期望交易
            trade.setStatus(TradeStatus.SUCCESS.getValue());
            trade.setBuyerId(buyer.getId());
            trade.setTradeAmt(param.getBidAmt());
            tradeMapper.updateById(trade);
            JSONObject info = JSON.parseObject(trade.getInfo());
            // 积分转移
            PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
            TransactionReceipt transferResult = pointController.transfer(buyer.getAddr(), info.getBigInteger(TRADE_INFO_KEY));
            log.info("point.transfer执行结果：{}", transferResult);
            if (!StringUtils.equals(transferResult.getStatus(), "0x0")) {
                throw new BusinessException("执行交易失败");
            }
        }
    }

    @Override
    public void deal(Trade trade) {
        User seller = userMapper.selectById(trade.getSellerId());
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(seller.getPrivateKey());
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        TradeContract tradeContract = TradeContract.load(trade.getAddr(), client, currentKeyPair);
        TransactionReceipt receipt = tradeContract.deal();
        log.info("point.deal执行结果：{}", receipt);
        if (!StringUtils.equals(receipt.getStatus(), "0x0")) {
            log.error("积分交易到期执行失败");
        }
        Tuple2<String, BigInteger> result = tradeContract.getDealOutput(receipt);
        // 没有买方，说明交易需要撤回
        if (StringUtils.isBlank(result.getValue1()) || result.getValue1().startsWith("0x00000")) {
            // 撤回交易
            trade.setStatus(TradeStatus.REJECT.getValue());
            tradeMapper.updateById(trade);
            // 解冻积分
            PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
            JSONObject info = JSON.parseObject(trade.getInfo());
            TransactionReceipt transferResult = pointController.unFreeze(info.getBigInteger(TRADE_INFO_KEY));
            log.info("point.unFreeze执行结果：{}", transferResult);
            if (!StringUtils.equals(transferResult.getStatus(), "0x0")) {
                throw new BusinessException("执行交易失败");
            }
            return;
        }
        User buyer = userMapper.selectByAddr(result.getValue1());
        trade.setBuyerId(buyer.getId());
        trade.setTradeAmt(BigDecimal.valueOf(result.getValue2().longValue()));
        trade.setStatus(TradeStatus.SUCCESS.getValue());
        tradeMapper.updateById(trade);
        JSONObject info = JSON.parseObject(trade.getInfo());
        // 积分转移
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt transferResult = pointController.transfer(buyer.getAddr(), info.getBigInteger(TRADE_INFO_KEY));
        log.info("point.transfer执行结果：{}", transferResult);
        if (!StringUtils.equals(transferResult.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }
    }

    /**
     * 记录一笔积分交易明细
     */
    private void saveSuccessTrade(PointTradeApplyParam param, User seller) throws Exception {
        User buyer = userMapper.selectByName(param.getName());
        if (buyer == null) {
            throw new BusinessException("指定买方企业名称不存在");
        }
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(seller.getPrivateKey());

        // 记录交易的内容，即积分数量
        JSONObject info = new JSONObject();
        info.put(TRADE_INFO_KEY, param.getPoint());
        // 部署交易，标记为成功
        TradeContract tradeContract = TradeContract.deploy(client, currentKeyPair, info.toJSONString()
                , BigInteger.valueOf(param.getLowestAmt().longValue()), BigInteger.valueOf(param.getExpectAmt().longValue())
                , BigInteger.valueOf(param.getBidDays().longValue()));
        TransactionReceipt result = tradeContract.targetDeal(buyer.getAddr());
        log.info("pointTrade.apply执行结果：{}", result);
        if (!StringUtils.equals(result.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }

        // 积分转移
        PointController pointController = PointController.load(pointConfig.getPointController(), client, currentKeyPair);
        TransactionReceipt transferResult = pointController.transfer(buyer.getAddr(), param.getPoint().toBigInteger());
        log.info("point.transfer执行结果：{}", transferResult);
        if (!StringUtils.equals(transferResult.getStatus(), "0x0")) {
            throw new BusinessException("执行交易失败");
        }

        // 保存数据
        Trade trade = new Trade();
        trade.setSellerId(seller.getId());
        trade.setBuyerId(buyer.getId());
        trade.setLowestAmt(param.getLowestAmt());
        trade.setExpectAmt(param.getExpectAmt());
        trade.setTradeAmt(param.getExpectAmt());
        trade.setCreateTime(new Date());
        trade.setTradeType(TradeType.POINT.getValue());
        trade.setStatus(TradeStatus.SUCCESS.getValue());
        trade.setAddr(tradeContract.getContractAddress());
        trade.setInfo(info.toJSONString());
        tradeMapper.insert(trade);
    }
}
