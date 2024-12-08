package szu.zhl.test1.utils;

import java.io.File;
import java.io.IOException;

public class OnlyRead {
    private  String filepath = "C:/example/identity.json";
    /**
     * 将指定路径的文件设置为只读
     * @param
     * @return 如果操作成功，返回 true；否则返回 false
     */
    public boolean setFileReadOnly() {
        // 创建文件对象
        File file = new File(filepath);

        // 检查文件是否存在
        if (!file.exists()) {
            System.out.println("文件不存在: " + filepath);
            return false;
        }

        // 将文件设置为只读
        boolean result = file.setReadOnly();

        // 检查操作是否成功
        if (result) {
            System.out.println("文件已设置为只读: " + filepath);
        } else {
            System.out.println("无法将文件设置为只读: " + filepath);
        }

        return result;
    }
}