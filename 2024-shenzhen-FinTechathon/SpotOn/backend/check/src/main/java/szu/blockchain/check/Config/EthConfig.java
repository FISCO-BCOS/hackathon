//package szu.blockchain.check.Config;
//
//
//import lombok.Value;
//import okhttp3.OkHttpClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.admin.Admin;
//import org.web3j.protocol.geth.Geth;
//import org.web3j.protocol.http.HttpService;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @author deray.wang
// * @date 2024/04/20 17:18
// */
//@Configuration
//public class EthConfig {
//    @Value("${web3j.client-address}")
//    private String rpc;
//
//    @Bean
//    public Web3j web3j() {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(30*1000, TimeUnit.MILLISECONDS);
//        builder.writeTimeout(30*1000, TimeUnit.MILLISECONDS);
//        builder.readTimeout(30*1000, TimeUnit.MILLISECONDS);
//        OkHttpClient httpClient = builder.build();
//        Web3j web3j = Web3j.build(new HttpService(rpc,httpClient,false));
//        return web3j;
//    }
//
//    /**
//     * 初始化admin级别操作的对象
//     * @return Admin
//     */
//    @Bean
//    public Admin admin() {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(30*1000, TimeUnit.MILLISECONDS);
//        builder.writeTimeout(30*1000, TimeUnit.MILLISECONDS);
//        builder.readTimeout(30*1000, TimeUnit.MILLISECONDS);
//        OkHttpClient httpClient = builder.build();
//        Admin admin = Admin.build(new HttpService(rpc,httpClient,false));
//        return admin;
//    }
//
//    /**
//     * 初始化personal级别操作的对象
//     * @return Geth
//     */
//    @Bean
//    public Geth geth() {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(30*1000, TimeUnit.MILLISECONDS);
//        builder.writeTimeout(30*1000, TimeUnit.MILLISECONDS);
//        builder.readTimeout(30*1000, TimeUnit.MILLISECONDS);
//        OkHttpClient httpClient = builder.build();
//        Geth geth = Geth.build(new HttpService(rpc,httpClient,false));
//        return geth;
//    }
//
//}
