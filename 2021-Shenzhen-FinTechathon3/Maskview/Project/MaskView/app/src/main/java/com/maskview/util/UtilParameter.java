package com.maskview.util;


public class UtilParameter {

    // 我的积分,登录后赋值
    public static String myPoints;
    // 我的手机号,登录后赋值
    public static String myPhoneNumber;
    // token值
    public static String myToken;
    // 我的用户名
    public static String myNickName;
    // 屏幕宽度
    public static int mScreenWidth;
    // 屏幕高度
    public static int mScreenHeight;

    // 显示照片url的ip
    //public static final String IMAGES_IP = "http://10.0.2.2:8080/";  // 本机
    public static final String IMAGES_IP = "http://121.41.83.124/";  // 云服务器

    // 上传图片url
    public static final String uploadImgUrl = GetIP.myIP() + "/restricted/uploadImg";

    // 安全交易---上架图片,加密,哈希。。。
    public static final String secureSaleUrl = GetIP.myIP() + "/restricted/sellForSecureTra";

    // 确权标志位
    public static final int CONFIRM_FLAG = 1;
    // 购买标志位
    public static final int DEAL_FLAG = 2;

    // 安全交易
    public static final String SECURE_TRADE_INELIGIBLE = "该图不符合安全交易条件";


}
