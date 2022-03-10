package com.maskview.dao;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.maskview.util.GetIP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * 请求服务器模块方法
 */

public class UserRequest {

    private static String CHARSET = "UTF-8"; //编码格式
    private String ip = GetIP.myIP();

    /**
     * POST方法请求服务器 并 得到返回结果
     *
     * @param url    : go服务器地址
     * @param params : 传递的字段key值
     * @return : 返回请求服务器的结果
     */
    private String requestResult(String url, Map<String, String> params, String token) {

        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //响应时间超时设置
            conn.setConnectTimeout(2000);

            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.addRequestProperty("Authorization", "Bearer " + token);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), CHARSET);
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (param.length() > 0) {
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                    // System.out.println(entry.getKey()+":"+entry.getValue());
                }
                System.out.println("param:" + param.toString());
                out.write(param.toString());
            }

            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, CHARSET);
            in = new BufferedReader(inputStreamReader);
            //in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Network Error";
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 以json形式传
     */
    private static String sendJsonPost(String url, String Json, String token) {
        String result = "";
        BufferedReader reader = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept", "application/json");
            // 往服务器里面发送数据
            if (Json != null && !TextUtils.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();
                Log.d("hlhupload", "doJsonPost: conn" + conn.getResponseCode());
            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Network Error";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 上传图片给服务器
     *
     * @param file       : 图片以文件形式上传
     * @param RequestURL : 水印服务器地址
     * @return : 返回请求服务器的结果
     */
    public String uploadImage(File file, String RequestURL, String token) {
        String result = "error";
        String BOUNDARY = UUID.randomUUID().toString();//边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";//内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);  //超时时间
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);//允许输入流
            conn.setDoOutput(true);//允许输出流
            conn.setUseCaches(false);//不允许使用缓存
            conn.setRequestMethod("POST");//请求方式
            conn.setRequestProperty("Charset", CHARSET);//设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.addRequestProperty("Authorization", "Bearer " + token);
            conn.connect();

            if (file != null) {
                //当文件不为空，把文件包装并且上传
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                /*
                 * 这里重点注意：
                 * name里面的值为服务器端需要key,只有这个key才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的。比如:abc.png
                 */
                dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
                dos.writeBytes("Content-Disposition: form-data; " + "name=\"file\";filename=\"" + file.getName() + "\"" + LINE_END);
                dos.writeBytes(LINE_END);

                FileInputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());

                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /*
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流  
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuilder sbs = new StringBuilder();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sbs.append((char) ss);
                    }
                    result = sbs.toString();
                    Log.i("---", "result------------------>>" + result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String uploadImageSetName(File file, String imgName, String RequestURL, String token) {
        String result = "error";
        String BOUNDARY = UUID.randomUUID().toString();//边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";//内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);  //超时时间
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);//允许输入流
            conn.setDoOutput(true);//允许输出流
            conn.setUseCaches(false);//不允许使用缓存
            conn.setRequestMethod("POST");//请求方式
            conn.setRequestProperty("Charset", CHARSET);//设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.addRequestProperty("Authorization", "Bearer " + token);
            conn.connect();

            if (file != null) {
                //当文件不为空，把文件包装并且上传
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                /*
                 * 这里重点注意：
                 * name里面的值为服务器端需要key,只有这个key才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的。比如:abc.png
                 */
                dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
                dos.writeBytes("Content-Disposition: form-data; " + "name=\"file\";filename=\"" + imgName + "\"" + LINE_END);
                dos.writeBytes(LINE_END);

                FileInputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());

                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /*
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流  
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuilder sbs = new StringBuilder();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sbs.append((char) ss);
                    }
                    result = sbs.toString();
                    Log.i("---", "result------------------>>" + result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 同时上传file和字段
     *
     * @param urlAddress  ：url
     * @param file        : 文件file
     * @param fileKey     : 服务器接收的字段名
     * @param paramsMap   ：字段
     * @param newFileName ：file名称, null表示默认file文件名
     * @return
     */
    public String uploadFileAndParams(String urlAddress, File file, String fileKey,
                                      Map<String, String> paramsMap, String newFileName, String token) {
        // 屏蔽证书
        FakeX509TrustManager.allowAllSSL();
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        String response = "";//服务器端返回值

        try {
            URL url = new URL(urlAddress);
            HttpURLConnection uRLConnection = (HttpURLConnection) url.openConnection();//打开连接
            uRLConnection.setDoInput(true);//以后就可以使用conn.getOutputStream().read()
            uRLConnection.setDoOutput(true);//设置以后就可以使用conn.getOutputStream().write()
            uRLConnection.setRequestMethod("POST");//使用post提交
            uRLConnection.setUseCaches(false);//不使用缓存
            uRLConnection.setInstanceFollowRedirects(false);//不自动重定向
            uRLConnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);//设置请求头
            uRLConnection.setRequestProperty("connection", "keep-alive");//保持连接
            uRLConnection.setRequestProperty("Charset", CHARSET);
            uRLConnection.addRequestProperty("Authorization", "Bearer " + token);
            uRLConnection.setConnectTimeout(10000);//请求超时时间
            //开始请求连接
            uRLConnection.connect();
            //获取输出流
            DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());
            /*
             * 以下先是用于上传参数
             */
            if (paramsMap != null && paramsMap.size() > 0) {
                Iterator<String> it = paramsMap.keySet().iterator();
                while (it.hasNext()) {
                    StringBuffer sb = new StringBuffer();
                    String key = it.next();
                    String value = paramsMap.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"")
                            .append(key).append("\"").append(LINE_END)
                            .append(LINE_END);
                    sb.append(value).append(LINE_END);
                    String params = sb.toString();
                    out.write(params.getBytes());
                    // dos.flush();
                }
            }

            //参数上传完开始上传文件
            if (file != null) {
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /*
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名
                 */
                if (null != newFileName && !"".equals(newFileName)) {
                    sb.append("Content-Disposition: form-data; name=\"" + fileKey
                            + "\"; filename=\"" + newFileName + "\"" + LINE_END);
                } else {
                    sb.append("Content-Disposition: form-data; name=\"" + fileKey
                            + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
                }
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                out.write(sb.toString().getBytes());
                InputStream isinput = new FileInputStream(file);

                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = isinput.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
                out.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                out.write(end_data);
                isinput.close();
                out.flush();
            }
            int i = uRLConnection.getResponseCode();
            if (i == 200) {
                InputStream is = uRLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String readLine = null;
                while ((readLine = br.readLine()) != null) {
                    // response = br.readLine();
                    response = readLine;
                }

                is.close();
                br.close();
            } else {
                response = uRLConnection.getResponseCode() + "";
            }
            //关闭连接
            uRLConnection.disconnect();
        } catch (Exception e) {
            return e.getMessage();
        }
        return response;
    }


    /**
     * 登录请求---密码登录
     *
     * @param phoneNumber : 手机号
     * @param password    : 密码
     * @param token       : null
     * @return : 请求服务器的结果
     */
    public String loginByPwd(String phoneNumber, String password, String token) {
        //对所有证书添加信任
        FakeX509TrustManager.allowAllSSL();
        //获取登录url
        String url = ip + "/loginByPwd";
        //传递的字段uid和password
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("password", password);
        //获取服务器的返回结果
        return requestResult(url, params, token);
    }

    /**
     * 发送验证码
     *
     * @param phoneNumber : 手机号
     * @param token       : null
     * @return 服务器返回值
     */
    public String sendPhoneCode(String phoneNumber, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/getPhoneCode";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        return requestResult(url, params, token);
    }


    /**
     * 注册
     *
     * @param phoneNumber : 手机号
     * @param password    : 密码
     * @param token       : null
     * @return 服务器返回值
     */
    public String register(String phoneNumber, String password, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/register";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("password", password);
        return requestResult(url, params, token);
    }


    /**
     * 登录请求---手机验证码登录
     *
     * @param phoneNumber : 手机号
     * @param phoneCode   : 手机验证码
     * @param token       : null
     * @return 服务器返回值
     */
    public String loginByCode(String phoneNumber, String phoneCode, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/loginByVCode";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("vcode", phoneCode);
        return requestResult(url, params, token);
    }


    /**
     * 自动登录---验证码
     *
     * @param phoneNumber : 手机号
     * @param code        : 最后一次验证码
     * @param token       : token
     * @return 服务器返回结果
     */
    public String autoLoginByCode(String phoneNumber, String code, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/autoLoginByCode";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("code", code);
        return requestResult(url, params, token);
    }


    /**
     * 忘记密码---检查验证码是否正确
     *
     * @param phoneNumber : 手机号
     * @param phoneCode   : 手机验证码
     * @param token       : null
     * @return 服务器结果
     */
    public String forgetPwdCheckCode(String phoneNumber, String phoneCode, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/forgetPwdCheckCode";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("vcode", phoneCode);
        return requestResult(url, params, token);
    }


    /**
     * 忘记密码---重置密码
     *
     * @param phoneNumber : 手机号码
     * @param phoneCode   : 手机验证码
     * @param newPwd      : 新密码
     * @param token       : null
     * @return 服务器返回值
     */
    public String resetPwd(String phoneNumber, String phoneCode, String newPwd, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/forgetResetPwd";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("password", newPwd);
        params.put("vcode", phoneCode);
        return requestResult(url, params, token);
    }

    /**
     * 获取我的购物车信息(需要登录)
     *
     * @param token : token
     * @return 服务器返回值
     */
    public String getShoppingCartInfo(String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/getMyShoppingCarInfo";
        return requestResult(url, null, token);
    }


    /**
     * 删除购物车中的某条信息(需要登录)
     *
     * @param imgPath : 要删除的照片路径---img/3/xxxx.jpg
     * @param token   : token
     * @return 服务器返回值
     */
    public String deleteOneShoppingCartItem(String imgPath, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/deleteOneToShoppingCar";
        Map<String, String> params = new HashMap<>();
        params.put("imgPath", imgPath);
        return requestResult(url, params, token);
    }


    /**
     * 清空购物车
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String clearShoppingCart(String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/clearMyShoppingCart";
        return requestResult(url, null, token);
    }


    /**
     * 获取展厅信息, 登录与未登录请求的url不同
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String getDisplayInfo(String token) {
        FakeX509TrustManager.allowAllSSL();
        String url;
        if (token == null) {
            url = ip + "/getDisplayInfoNotLogin";
        } else {
            url = ip + "/restricted/getDisplayInfoAfterLogin";
        }
        return requestResult(url, null, token);
    }


    /**
     * 展厅中获取选中图片的信息
     *
     * @param imgName : 图片名称 : img/1/xxxxxxx.jpg为例
     * @param token   : token
     * @return 服务器返回结果
     */
    public String getOneSellImgInfo(String imgName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/getOneSellImgInfoByImgName";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);
        return requestResult(url, params, token);
    }


    /**
     * 添加某个图片到购物车(需要登录)
     *
     * @param sellerName : 卖家用户名
     * @param imgPath    : 图片路径 : img/1/xxxxxxx.jpg为例
     * @param token      : token
     * @return 服务器返回结果
     */
    public String addOneToMyShoppingCart(String sellerName, String imgPath, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/addOneToShoppingCar";
        Map<String, String> params = new HashMap<>();
        params.put("sellerName", sellerName);
        params.put("imgPath", imgPath);
        return requestResult(url, params, token);
    }


    /**
     * 获取我的上架图片集合
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String getMySellImgList(String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/getSellImgList";
        return requestResult(url, null, token);
    }


    /**
     * 上架图片,需要登录
     *
     * @param imgName:  图片名称
     * @param imgPrice: 图片价格
     * @param token     : token
     * @return 服务器返回的结果
     */
    public String sellImage(String imgName, String imgPrice, String imgTopic, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/sellImg";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);
        params.put("imgPrice", imgPrice);
        params.put("imgTopic", imgTopic);
        return requestResult(url, params, token);
    }


    /**
     * 修改上架图片的价格,需要登录
     *
     * @param imgName     : 图片名称 (con-xxxx.jpg)
     * @param newImgPrice : 新的价格
     * @param token       : token
     * @return 服务器返回结果
     */
    public String updateSellImgPrice(String imgName, String newImgPrice, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/updateSellImgPrice";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);
        params.put("newImgPrice", newImgPrice);
        return requestResult(url, params, token);
    }


    /**
     * 下架某张图片,需要登录
     *
     * @param imgName : 图片名称 (con-xxx.jpg)
     * @param token   : token
     * @return 服务器返回结果
     */
    public String underSellImg(String imgName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/deleteSellImg";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);
        return requestResult(url, params, token);
    }

    /**
     * 上传确权信息,需要登录
     *
     * @param imgName: 图片名称
     * @param imgKey:  零水印序列
     * @param token    : token
     * @return : 服务器返回结果
     */
    public String sendConfirmInfo(String imgName, String imgKey, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/confirmImg";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);
        params.put("imgKey", imgKey);
        return requestResult(url, params, token);
    }


    /**
     * 检验照片是否是自己的,是否可以购买,需要登录
     *
     * @param sellerName : 卖家昵称
     * @param token      : token
     * @return 服务器返回结果
     */
    public String isMyImg(String sellerName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/isMyImage";
        Map<String, String> params = new HashMap<>();
        params.put("sellerName", sellerName);
        return requestResult(url, params, token);
    }


    /**
     * 购买图片,需要登录
     *
     * @param sellerName : 卖家用户名
     * @param imgName    : 本次购买的图片名称(卖家的图片名称), 添加购买水印的图片在本机,服务器不存储
     * @param imgPrice   : 购买价格
     * @param dataHash   : 购买水印序列
     * @return 服务器返回结果
     */
    public String purchaseImg(String sellerName, String imgName, String imgPrice, String dataHash, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/purchaseImg";
        Map<String, String> params = new HashMap<>();
        params.put("sellerName", sellerName);
        params.put("imgName", imgName);
        params.put("imgPrice", imgPrice);
        params.put("dataHash", dataHash);
        return requestResult(url, params, token);
    }


    /**
     * 获取购买记录
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String getPurchaseRecords(String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/getMyPurchaseRecords";
        return requestResult(url, null, token);
    }


    /**
     * 获取出售记录
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String getSaleRecords(String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/getMySaleRecords";
        return requestResult(url, null, token);
    }


    /**
     * 获取用户信息,需要登录
     *
     * @param token : token
     */
    public String getMyInfo(String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/getMyInfo";
        return requestResult(url, null, token);
    }


    /**
     * 修改昵称,需要登录
     *
     * @param newNickName : 新昵称
     * @param token       : token
     * @return 服务器返回结果
     */
    public String updateMyNickName(String newNickName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/updateMyNickName";
        Map<String, String> params = new HashMap<>();
        params.put("newNickName", newNickName);
        return requestResult(url, params, token);
    }

    /**
     * 修改性别,需要登录
     *
     * @param sex   : 性别
     * @param token : token
     * @return 服务器返回结果
     */
    public String updateMySex(String sex, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/updateMySex";
        Map<String, String> params = new HashMap<>();
        params.put("sex", sex);
        return requestResult(url, params, token);
    }


    /**
     * 修改出生日期,需要登录
     *
     * @param birth : 出生日期
     * @param token : token
     * @return 服务器返回结果
     */
    public String updateMyBirth(String birth, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/updateMyBirth";
        Map<String, String> params = new HashMap<>();
        params.put("birth", birth);
        return requestResult(url, params, token);
    }


    /**
     * 设置头像,需要登录
     *
     * @param headViewName : 头像图片名称
     * @param token        : token
     * @return 服务器返回结果
     */
    public String updateMyHeadView(String headViewName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/updateMyHeadView";
        Map<String, String> params = new HashMap<>();
        params.put("headViewName", headViewName);
        return requestResult(url, params, token);
    }

    /**
     * 设置背景墙,需要登录
     *
     * @param imgName ： 背景墙图片名称
     * @param token   ： token
     * @return 服务器返回结果
     */
    public String updateBackgroundWall(String imgName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/updateMyBackgroundWall";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);
        return requestResult(url, params, token);
    }

    /**
     * 修改密码,需要登录
     *
     * @param newPassword : 新密码
     * @param token       : token
     * @return 服务器返回结果
     */
    public String updatePwd(String newPassword, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/updatePwd";
        Map<String, String> params = new HashMap<>();
        params.put("newPassword", newPassword);
        return requestResult(url, params, token);
    }

    /**
     * 查看某个用户的基本信息与所有上架图片集合, 登录与未登录请求的结果不同
     *
     * @param userName : 昵称
     * @param token    : token
     * @return 服务器返回结果
     */
    public String getUserInfoAndSellList(String userName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url;
        if (token == null) {
            url = ip + "/getUserInfoAndSellListNotLogin";
        } else {
            url = ip + "/restricted/getUserInfoAndSellListAfterLogin";
        }
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        return requestResult(url, params, token);
    }

    /**
     * 关注某人
     *
     * @param userName : 昵称
     * @param token    : token
     * @return 服务器返回结果
     */
    public String focusUser(String userName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/focusUser";
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        return requestResult(url, params, token);
    }

    /**
     * 取消关注某人
     *
     * @param userName : 昵称
     * @param token    : token
     * @return 服务器返回结果
     */
    public String cancelFocus(String userName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/cancelFocus";
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        return requestResult(url, params, token);
    }

    /**
     * 选中用户所有关注的人的信息
     *
     * @param userName : 昵称
     * @param token    : token
     * @return 服务器返回结果
     */
    public String getHerAllFocusInfo(String userName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url;
        if (token != null && !token.equals("")) {
            url = ip + "/restricted/getUserAllFocusInfo";
        } else {
            url = ip + "/getUserAllFocusInfoNotLogin";
        }
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        return requestResult(url, params, token);
    }

    /**
     * 选中用户所有粉丝的信息
     *
     * @param userName : 昵称
     * @param token    : token
     * @return
     */
    public String getHerAllFansInfo(String userName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url;
        if (token != null && !token.equals("")) {
            url = ip + "/restricted/getUserAllFansInfo";
        } else {
            url = ip + "/getUserAllFansInfoNotLogin";
        }
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        return requestResult(url, params, token);
    }

    /*-----------------------------------安全交易 start ---------------------------------------*/

    /**
     * 根据图片名称获取图片信息,txt,及买卖双方uid
     *
     * @param imgPath ：图片名称(img/xxx/xxx.jpg)
     * @param token   : token
     * @return
     */
    public String getImgInfoSecureTra(String imgPath, String token) {
        FakeX509TrustManager.allowAllSSL();
        String url = ip + "/restricted/purchaseForSecureTra";
        Map<String, String> params = new HashMap<>();
        params.put("imgPath", imgPath);
        return requestResult(url, params, token);
    }

    /**
     * 对比哈希, 确认是同一张图片
     *
     * @param txtName ：txt名称
     * @return
     */
    public String compareHash(String txtName) {
        FakeX509TrustManager.allowAllSSL();
        String url = GetIP.javaIp() + "/fisco-bcos/getTxtHash";
        Map<String, String> params = new HashMap<>();
        params.put("txtName", txtName);
        return requestResult(url, params, null);
    }

    /**
     * 冻结积分
     *
     * @param purchaserUid : 自己的uid(go服务器获取)
     * @param imgPrice     : 图片价格(go服务器获取)
     * @return
     */
    public String freezePoints(int purchaserUid, int imgPrice) {
        FakeX509TrustManager.allowAllSSL();
        String url = GetIP.javaIp() + "/fisco-bcos/freezePoints";
        Map<String, String> params = new HashMap<>();
        params.put("purchaserUid", purchaserUid + "");
        params.put("imgPrice", imgPrice + "");
        return requestResult(url, params, null);
    }

    /**
     * 安全交易成功
     *
     * @param purchaseUid ：自己uid
     * @param sellerUid   : 卖家uid
     * @param imgPath     : 图片路径
     * @param tradePrice  : 交易价格
     * @param dataHash    : 水印哈希
     * @return
     */
    public String securePurchaseSucceed(int purchaseUid, int sellerUid, String imgPath, int tradePrice, String dataHash) {
        FakeX509TrustManager.allowAllSSL();
        String url = GetIP.javaIp() + "/fisco-bcos/succeedTrade";
        Map<String, String> params = new HashMap<>();
        params.put("purchaseUid", purchaseUid + "");
        params.put("sellerUid", sellerUid + "");
        params.put("imgPath", imgPath);
        params.put("tradePrice", tradePrice + "");
        params.put("dataHash", dataHash);
        String json = JSON.toJSONString(params);
        return sendJsonPost(url, json, null);
    }

    /**
     * 解冻积分, 安全交易失败
     *
     * @param purchaserUid : 自己uid
     * @return
     */
    public String returnPoints(int purchaserUid) {
        FakeX509TrustManager.allowAllSSL();
        String url = GetIP.javaIp() + "/fisco-bcos/returnPoints";
        Map<String, String> params = new HashMap<>();
        params.put("purchaserUid", purchaserUid + "");
        return requestResult(url, params, null);
    }

    /*-----------------------------------安全交易 end ---------------------------------------*/
}
