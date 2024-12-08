package szu.zhl.test1.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZKProof {
    public boolean verify() throws Exception{
        AtomicBoolean result = new AtomicBoolean(false);

        ProcessBuilder processBuilder = new ProcessBuilder(
                "D:\\Anaconda\\envs\\did\\python.exe", // Python 解释器路径
                "E:\\java\\本地服务器以及本地前端\\test1\\src\\main\\java\\szu\\zhl\\test1\\utils\\ZKP.py" // Python 脚本路径
        );

        // 设置工作目录
        processBuilder.directory(new File("E:\\java\\本地服务器以及本地前端\\test1\\src\\main\\java\\szu\\zhl\\test1\\utils"));

        processBuilder.redirectErrorStream(true); // 合并标准错误流和标准输出流

        try {
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Output: " + line);
                    if (line.contains("false")) {
                        result.set(false);
                    }
                    if (line.contains("true")) {
                        result.set(true);
                    }
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("调用脚本成功");
            } else {
                System.out.println("调用失败");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("result: " + result.get());
        return result.get();
    }
    public static void main(String[] args) throws Exception {
        ZKProof zkVerify = new ZKProof();
        zkVerify.verify();
    }
}
