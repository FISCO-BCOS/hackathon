package szu.zhl.test1.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/*判断文件是否存在*/
@Component
public class IsFile {

    // 使用 @Value 注解从 application.properties 中读取文件路径

    private  String path = "C:/example/identity.json";

    /**
     * 检查给定路径是否指向一个存在的文件。
     *
     * @return 如果路径指向一个存在的文件，则返回true；否则返回false。
     */
    public  boolean isFileExists() {
        File file = new File(path);
        return file.exists() && file.isFile();
    }
}