package szu.blockchain.check.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZKVerify {
    public boolean verify() throws Exception{
        AtomicBoolean result = new AtomicBoolean(false);


         Process process = null;

        try {
            // 使用 Python 解释器来运行脚本
            String[] cmd = {
                "C:\\Users\\zzzzz\\.conda\\envs\\did\\python.exe", // Python 解释器路径
                "D:\\java\\code\\check\\pythonverify\\verify.py" // Python 脚本路径
            };
            process = Runtime.getRuntime().exec(cmd);

            // 获取进程的标准输入流和错误流
            final InputStream is1 = process.getInputStream();
            final InputStream is2 = process.getErrorStream();

            // 启动线程读取标准输出流
            Thread outputThread = new Thread(() -> {
                try (BufferedReader br1 = new BufferedReader(new InputStreamReader(is1, "GBK"))) {
                    String line;
                    while ((line = br1.readLine()) != null) {
                        System.out.println("Output Stream: " + line);
                        if(line.contains("false")){
                            result.set(false);

                        }
                        if (line.contains("true")){
                            result.set(true);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // 启动线程读取标准错误流
            Thread errorThread = new Thread(() -> {
                try (BufferedReader br2 = new BufferedReader(new InputStreamReader(is2, "GBK"))) {
                    String line;
                    while ((line = br2.readLine()) != null) {
                        System.err.println("Error Stream: " + line);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            outputThread.start();
            errorThread.start();

            // 等待进程完成
            int re = process.waitFor();
            outputThread.join(); // 确保线程在主线程结束前完成
            errorThread.join();

            if (re == 0) {
                System.out.println("调用脚本成功");
            } else {
                System.out.println("调用失败");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                try {
                    process.getErrorStream().close();
                    process.getInputStream().close();
                    process.getOutputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("result"+result.get());
        return result.get();

    }
    public static void main(String[] args) throws Exception {
        ZKVerify zkVerify = new ZKVerify();
        zkVerify.verify();
    }
}
