package com.brecycle.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author cmgun
 */
@Slf4j
@Api(tags = "模板下载")
@RestController
@RequestMapping("download")
public class FileController {

    @ApiOperation("电池批量导入模板下载")
    @PostMapping("/batchBatteryNo")
    public void downloadTemp(HttpServletRequest request, HttpServletResponse response){
        //定义变量
        String downPath = "classpath:data/userTemplate.xls";
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = null;
        byte[] buffer = new byte[1024];
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        OutputStream os = null; //输出流
        try {
            //获取resource中的文件，并生成流信息
            resource = resourceLoader.getResource("classpath:template/batchBatteryNo.xlsx");
            inputStream = resource.getInputStream();
            //设置返回文件信息
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode("batchBatteryNo.xlsx","UTF-8"));
            //将内容使用字节流写入输出流中
            os = response.getOutputStream();
            bis = new BufferedInputStream(inputStream);
            while(bis.read(buffer) != -1){
                os.write(buffer);
            }
        } catch (IOException e) {
            log.error("文件模板下载异常", e);
        } finally {
            //关闭流信息
            try {
                if(inputStream !=null ) {
                    inputStream.close();
                }
                if(bis != null) {
                    bis.close();
                }
                if(os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
