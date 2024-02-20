package com.find.blockchainwebadmin;

import com.alibaba.fastjson.JSON;
import com.find.component.JavaWebSocketClient;
import com.find.component.WebSocketServer;
import com.find.dao.AccountDao;
import com.find.pojo.OnlineNode;
import com.find.util.Constant;
import org.apache.juli.OneLineFormatter;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

import static com.find.util.Constant.LOCK_WORKER_NUM;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)



class BlockchainWebAdminApplicationTests {

    @Autowired
    AccountDao accountDao;

    @Autowired
    @Qualifier("restHighLevelClient")
    public RestHighLevelClient client;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() throws IOException {
//        System.out.println(redisTemplate.opsForValue().get(Constant.SECURITY_LIST));
        GetRequest getRequest = new GetRequest("model", "server0_0");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            String sourceAsString = getResponse.getSourceAsString();
            System.out.println(sourceAsString);
        }else{
            System.err.println("error");
        }
    }

    @Test
    void test1(){

        accountDao.insertTrainData("client1","1","user1","2023.11.1",9.6,10.65f);

//        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
//        String password="123456";
//        //密文
//        String encode = passwordEncoder.encode(password);
//        System.out.println(encode);
//
//        accountDao.UpdateByUsername("aaaa",encode);
//
//        //匹配，同样发明文通过计算是不一样的，但是matches方法传的值是第一次或第二次都是true
//        //这里数据库中就可以更改密码为密文
//        boolean matches = passwordEncoder.matches(password, encode);
//        System.out.println(matches);



        //OnlineNode client1 = accountDao.getOnlineNodeById("client1");
//        accountDao.modifyRepValueById("client1",6.5);

//        WebSocketServer server = new WebSocketServer();
////        if (server.)
//        server.sendInfo("1","client1_1");

        //使用Redisson分布式锁
//        RLock lock = redissonClient.getLock("lock_task");
//
//        System.out.println(lock.tryLock());
//
//        String nums = stringRedisTemplate.opsForValue().get(LOCK_WORKER_NUM);
//
//        System.out.println("当前剩余的工作节点数" + nums);
//
//        if(nums != null && Integer.parseInt(nums) > 0){
//            //更新redis中的值
//            stringRedisTemplate.opsForValue().set(LOCK_WORKER_NUM,String.valueOf(Integer.parseInt(nums)-1));
//        }
    }

}
