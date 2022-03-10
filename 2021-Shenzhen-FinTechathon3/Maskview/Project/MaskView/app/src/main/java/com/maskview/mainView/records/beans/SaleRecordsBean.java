package com.maskview.mainView.records.beans;

import java.util.List;

public class SaleRecordsBean extends RequestResultForRecords {

    private List<DataBeanSaleRecords> data;

    public List<DataBeanSaleRecords> getData() {
        return data;
    }

    public void setData(List<DataBeanSaleRecords> data) {
        this.data = data;
    }

    public static class DataBeanSaleRecords {
        private String imgPath;
        private String imgPrice;
        private String saleDate;

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

        public String getSaleDate() {
            return saleDate;
        }

        public void setSaleDate(String saleDate) {
            this.saleDate = saleDate;
        }
    }


}
