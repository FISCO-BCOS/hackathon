package com.maskview.mainView.records.beans;

import java.util.List;

public class PurchaseRecordsBean extends RequestResultForRecords {

    private List<DataBeanPurchaserRecords> data;

    public List<DataBeanPurchaserRecords> getData() {
        return data;
    }

    public void setData(List<DataBeanPurchaserRecords> data) {
        this.data = data;
    }

    public static class DataBeanPurchaserRecords {
        private String imgPath;
        private String imgPrice;
        private String purchaseDate;

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getImgPrice() {
            return imgPrice;
        }

        public void setImgPrice(String imgPrice) {
            this.imgPrice = imgPrice;
        }

        public String getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(String purchaseDate) {
            this.purchaseDate = purchaseDate;
        }
    }


}
