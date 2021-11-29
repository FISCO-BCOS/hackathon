package com.maskview.mainView.securePurchase;

/**
 * Created by Yzr on 2020/10/7
 */
public class JsonCompareTxtHash {

    /**
     * result : true
     * note : 查询hash成功
     * data : edf368f038b45484414694ad4d928f2a556dcc31cee6448e638a34703f2c39f6
     */

    private boolean result;
    private String note;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
