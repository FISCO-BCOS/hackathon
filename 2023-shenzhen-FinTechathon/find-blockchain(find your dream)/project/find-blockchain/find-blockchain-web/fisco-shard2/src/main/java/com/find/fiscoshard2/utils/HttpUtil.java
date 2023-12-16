package com.find.fiscoshard2.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public final class HttpUtil {

    public static String postMsg(String url, String data) throws IOException {
        // 根据url地址发起post请求
        HttpPost httppost = new HttpPost(url);

        StringEntity stEntity;

        // 获取到httpclient客户端
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 设置请求的一些头部信息
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("procode", "test");
            stEntity = new StringEntity(data, "UTF-8");
            httppost.setEntity(stEntity);
            // 设置请求的一些配置设置，主要设置请求超时，连接超时等参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000).setConnectionRequestTimeout(1000).setSocketTimeout(5000)
                    .build();
            httppost.setConfig(requestConfig);
            // 执行请求
            CloseableHttpResponse response = httpclient.execute(httppost);
            // 请求结果
            String resultString = "";
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                log.info("请求状态：{}", response.getStatusLine().getStatusCode());
                // 获取请求响应结果
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 将响应内容转换为指定编码的字符串
                    resultString = EntityUtils.toString(entity, "UTF-8");
//                    log.info("Response content:{}", resultString);
                    return resultString;
                }
            } else {
//                log.info("请求失败！");
                return resultString;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            httpclient.close();
        }
        return null;
    }

    public static String get(String url) {
        String result = "";
        try {
            // 根据地址获取请求
            HttpGet request = new HttpGet(url);
            request.setHeader("procode", "test");

            // 获取当前客户端对
            CloseableHttpClient httpclient = HttpClients.createDefault();
            // 通过请求对象获取响应对象
            HttpResponse response = httpclient.execute(request);
            // 判断请求结果状态码
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    public static void main(String[] args) throws Exception {
//
//        String shardCount = "2";
//        String errorCount= "1";
//
//        String url = "http://localhost:8083/test/tpsCount?shardCount="+shardCount+"&errorCount="+errorCount;
//        System.out.println("url is :"+url);
//        // 发起post请求
////        String resultPost = postMsg(url, "1");
////        System.out.println("get返回结果：{}"+resultPost);
//
//        // 发起get请求
//        String resultGet = get(url);
//        System.out.println("get返回结果：{}"+resultGet);
//
//    }



}
