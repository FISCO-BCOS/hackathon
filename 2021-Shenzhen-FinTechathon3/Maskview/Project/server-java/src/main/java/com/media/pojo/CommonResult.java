package com.media.pojo;

import java.io.Serializable;

public class CommonResult implements Serializable {

    private boolean result;
    private String note;
    private Object data;

    public CommonResult(boolean result, String note) {
        this.result = result;
        this.note = note;
    }

    public CommonResult(boolean result, String note, Object data) {
        this.result = result;
        this.note = note;
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
