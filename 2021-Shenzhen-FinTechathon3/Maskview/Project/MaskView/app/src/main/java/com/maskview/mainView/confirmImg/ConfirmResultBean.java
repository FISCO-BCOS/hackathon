package com.maskview.mainView.confirmImg;

import java.io.Serializable;

/**
 * 确权结果显示
 */

public class ConfirmResultBean implements Serializable {

    private String uriOrPath;
    // 是否确权成功; 0:成功---1:分辨率不合要求---2:已确权过
    private int note;

    public String getUriOrPath() {
        return uriOrPath;
    }

    public void setUriOrPath(String uriOrPath) {
        this.uriOrPath = uriOrPath;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

}
