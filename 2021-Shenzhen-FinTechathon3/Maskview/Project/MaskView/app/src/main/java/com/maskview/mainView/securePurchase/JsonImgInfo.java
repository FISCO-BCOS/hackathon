package com.maskview.mainView.securePurchase;

/**
 * Created by Yzr on 2020/10/7
 */
public class JsonImgInfo {

    /**
     * result : true
     * note : 获取信息成功
     * purchaseUid : 1
     * data : {"sellerUid":4,"imgPrice":0,"txtPath":"base64File/4/oDvx19x4xKBGNMC0Z0Ke6d62PD2R78.txt"}
     */

    private boolean result;
    private String note;
    private int purchaseUid;
    private DataBean data;

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

    public int getPurchaseUid() {
        return purchaseUid;
    }

    public void setPurchaseUid(int purchaseUid) {
        this.purchaseUid = purchaseUid;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sellerUid : 4
         * imgPrice : 0
         * txtPath : base64File/4/oDvx19x4xKBGNMC0Z0Ke6d62PD2R78.txt
         */

        private int sellerUid;
        private int imgPrice;
        private String txtPath;

        public int getSellerUid() {
            return sellerUid;
        }

        public void setSellerUid(int sellerUid) {
            this.sellerUid = sellerUid;
        }

        public int getImgPrice() {
            return imgPrice;
        }

        public void setImgPrice(int imgPrice) {
            this.imgPrice = imgPrice;
        }

        public String getTxtPath() {
            return txtPath;
        }

        public void setTxtPath(String txtPath) {
            this.txtPath = txtPath;
        }
    }
}
