package com.find.util;


import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.*;
import java.util.List;

public class DataPlot {

    public static void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listToFile(String filePath, List<Float> infolist) throws IOException {
        File file = new File(filePath);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (Float l:infolist){
            writer.write(l + "\r\n");
        }
        writer.close();

    }

    public static String getAccGraph(List<Float> accList , String cliId) throws PythonExecutionException, IOException {

//        System.out.println("accList to plot is : "+accList);

//      需修改精度列表缓存文件路径
        String listFilePath = "/home/su/fisco-project/dataset/listFile"+cliId+".txt";

        clearInfoForFile(listFilePath);

        listToFile(listFilePath, accList);

        String accGraphPath="";

        try {
            String[] params = new String[]
                    {"python", "/home/find-fabric/workspace/cli_train_plotting/py_script/plotting.py", listFilePath,cliId};

//            System.out.println("listFilePath is ："+listFilePath);
//            System.out.println("cliId for python is : "+cliId);
//          需修改python绘图脚本路径
            Process proc = Runtime.getRuntime().exec(params); //执行py文件
            InputStreamReader stdin = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(stdin);


            while ((accGraphPath = input.readLine()) != null) {

                System.out.println(accGraphPath);//得到输出

            }

            int re = proc.waitFor();
            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accGraphPath;

    }
}
