package szu.zhl.test1.utils;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

public class NewFile {
    // 设定文件路径
    private  String filePath = "C:/example/identity.json";

    public  boolean createFile() {


        // 创建File对象
        File file = new File(filePath);

        try {
            // 检查文件是否已经存在
            if (file.exists()) {
                System.out.println("文件已经存在，无需创建。");
                return true;
            }

            // 创建目录（如果目录不存在）
            File parentDir = file.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                System.out.println("无法创建目录：" + parentDir.getAbsolutePath());
                return false;
            }

            // 创建文件
            boolean created = file.createNewFile();
            if (created) {
                System.out.println("文件创建成功：" + filePath);
                return true;
            } else {
                System.out.println("文件创建失败：" + filePath);
                return false;
            }
        } catch (IOException e) {
            System.out.println("创建文件时发生错误：" + e.getMessage());
            return false;
        } catch (SecurityException e) {
            System.out.println("安全管理器拒绝访问：" + e.getMessage());
            return false;
        }
    }
}