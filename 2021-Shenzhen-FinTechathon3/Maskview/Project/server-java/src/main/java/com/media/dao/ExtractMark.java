package com.media.dao;

import com.media.pojo.MarkResult;
import com.media.watermark.Watermarkextract1;

import java.io.IOException;

/**
 * 提取鲁棒水印和零水印
 */

public class ExtractMark {

    private String imgPath;

    public ExtractMark(String imgPath) {
        this.imgPath = imgPath;
    }

    public MarkResult extract() {
        MarkResult markResult = new MarkResult();
        try {
            Watermarkextract1 extract = new Watermarkextract1(imgPath, "rg0.txt");
            markResult.setRobustSeq(extract.getRobustWaterMark());
            markResult.setZeroSeq(extract.getZeroWaterMark());
            return markResult;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
