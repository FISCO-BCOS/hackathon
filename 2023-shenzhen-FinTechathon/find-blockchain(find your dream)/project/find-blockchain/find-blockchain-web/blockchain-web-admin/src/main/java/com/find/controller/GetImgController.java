package com.find.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping(value="/api/v1")
public class GetImgController {


        @GetMapping(value = "/image",produces = MediaType.IMAGE_JPEG_VALUE)
        @ResponseBody
        public byte[] test( @RequestParam("plotId") String plotId) throws Exception {

//            System.out.println("plotId is : "+plotId);

            String plotName="/home/find-fabric/workspace/cli_train_plotting/train_result/"+plotId.substring(plotId.length()-1)+".png";

            File file = new File(plotName);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;

        }
    
}
