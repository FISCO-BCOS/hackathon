package com.maskview.mainView.securePurchase;

/**
 * Created by Yzr on 2020/10/7
 * 冻结/解冻积分Json
 */
public class JsonCommon {

    /**
     * result : true
     * note : 冻结成功
     * data : null
     */

    private boolean result;
    private String note;
    private Object data;

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
