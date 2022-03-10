package com.media.pojo;

import java.util.List;

/**
 * 水印结果
 */

public class MarkResult {

    private String robustSeq;
    private List<String> zeroSeq;

    public String getRobustSeq() {
        return robustSeq;
    }

    public void setRobustSeq(String robustSeq) {
        this.robustSeq = robustSeq;
    }

    public List<String> getZeroSeq() {
        return zeroSeq;
    }

    public void setZeroSeq(List<String> zeroSeq) {
        this.zeroSeq = zeroSeq;
    }
}
