package com.maskview.dao;

import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yzr on 2020/10/7
 */
public class SecureDao {

    private final static String TAG = "SecureDao";

    /**
     * 文件hash(SHA256)
     */
    public static String fileHash(File file) {
        MessageDigest digest;
        String type = "SHA-256";
        try {
            digest = MessageDigest.getInstance(type);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param url      下载连接
     * @param saveDir  储存下载文件的目录
     * @param listener 下载监听
     */
    public static void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // 下载失败
                Log.e("", "onFailure: " + e);
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                long start = System.currentTimeMillis();
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                //String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(saveDir, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    long end = System.currentTimeMillis();
                    Log.e("TAG", "时间: " + (end - start));
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    Log.e("TAG", "onResponse: " + e);
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 从下载连接中解析出文件名
     */
    private static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

    /**
     * 将Base64位编码的图片进行解码，并保存到指定目录
     * @param base64Str
     * @param path
     * @return true
     */
    public static boolean base64ToFile(String base64Str,String path) {
        byte[] data = Base64.decode(base64Str,Base64.NO_WRAP);
        for (int i = 0; i < data.length; i++) {
            if(data[i] < 0){
                //调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
            os.flush();
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读txt文件内容, 采用字节数组缓冲流读, 速度最快
     * @param path
     */
    public static String readTxt(File path) {
        long start = System.currentTimeMillis();
        //StringBuilder sBuilder = new StringBuilder();
        StringBuffer sBuffer = new StringBuffer();
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(path));
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = bis.read(bytes)) != -1) {
                //System.out.println(new String(buf, 0, len));
                //sBuilder.append(new String(bytes, 0, len));
                sBuffer.append(new String(bytes, 0, len));
            }
            //System.out.println(sBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return sBuffer.toString();
    }
}
