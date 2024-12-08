package szu.zhl.test1.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
@Component
public class FilePermissionUtils {

    private  String filePath = "C:/example/identity.json";

    /**
     * 设置文件的可读可写权限。
     *
     * @param
     * @return 如果成功设置权限，返回true；否则返回false。
     */



        public boolean setFileReadableWritable() {


            File file = new File(filePath);

            boolean readable = file.setReadable(true);
            boolean writable = file.setWritable(true);
            System.out.println("文件是否可读：" + readable);
            System.out.println("文件是否可写：" + writable);

            if (readable && writable) {
                System.out.println("文件权限设置成功：" + filePath);
                return true;
            } else {
                System.out.println("文件权限设置失败：" + filePath);
                return false;
            }
        }
    }
