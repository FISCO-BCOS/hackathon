package com.media.text;

import com.media.bcos.client.PointControllerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Text {

    private static Logger logger = LoggerFactory.getLogger(Text.class);
    private static PointControllerClient pointControllerClient;

    public static void main(String[] args) {

        pointControllerClient = new PointControllerClient();
        try {
            pointControllerClient.initialize();
            //String result = getTxtHash("dlU8i926G5MvQdTWkxJTImUNK7Fd6o.txt");
            int uid = 44;
            // 注册
            boolean q = register(uid);
            // 充值积分
            int index = 87;
            boolean w = recharge(uid, index);
            // 查余额
            int result = getPoints(uid);
            logger.error("注册结果：" + q + " --- 充值结果：" + w + " --- 用户" + uid + "的余额为：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 用户注册,自动充值500积分
     * 查余额时, 可直接根据uid查
     *
     * @return : boolean
     */
    private static boolean register(int uid) {
        return pointControllerClient.registerId(uid);
    }

    /**
     * 充值积分
     *
     * @param uid   ：对应uid
     * @param value : 充值的金额
     * @return ：boolean
     */
    private static boolean recharge(int uid, int value) {
        return pointControllerClient.recharge(uid, value);
    }

    /**
     * 查余额
     *
     * @param uid ：用户uid
     * @return ：余额
     */
    private static int getPoints(int uid) {
        return pointControllerClient.queryPoint(uid);
    }

    /**
     * 根据txt名称获取txt的Hash
     *
     * @param key : txt名称
     * @return : 对应Hash
     */
    private static String getTxtHash(String key) {
        String result = null;
        try {
            //long start = System.currentTimeMillis();
            result = pointControllerClient.queryHash(key);
            //long end = System.currentTimeMillis();
            //System.out.println(" 时间是：" + (end - start));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
