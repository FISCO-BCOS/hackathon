package com.find.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constant {

    public static final String LOCK_SECURITY_KEY = "LOCK_SECURITY_KEY";

    public static List<String> MessageList = new ArrayList<>();

    public static List<String> TrainList = new ArrayList<>();

    public static volatile List<String> CLIENT_DATA_LIST = new ArrayList<>();

    public static String SECURITY_LIST = "SECURITY_LIST";

    public static String LOCK_WORKER_NUM = "WORKER_NUM";

    public static double REWARD = 0;

    //缓存逻辑过期时间
    public static final Long CACHE_LOGICAL_TTL = 20L;

    public static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 权限: 普通用户
     */
    String AUTHORITY_USER = "user";

    /**
     * 权限: 管理员
     */
    String AUTHORITY_ADMIN = "admin";

    /**
     * 权限: 版主
     */
    String AUTHORITY_MODERATOR = "moderator";


}
