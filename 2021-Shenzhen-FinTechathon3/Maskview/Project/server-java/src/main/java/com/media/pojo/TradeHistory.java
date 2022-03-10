package com.media.pojo;

import java.util.Date;

public class TradeHistory {

    private Integer id;
    private Integer purchaseUid;
    private Integer sellerUid;
    private String imgPath;
    private Integer tradePrice;
    private String dataHash;
    private Date createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPurchaseUid() {
        return purchaseUid;
    }

    public void setPurchaseUid(Integer purchaseUid) {
        this.purchaseUid = purchaseUid;
    }

    public Integer getSellerUid() {
        return sellerUid;
    }

    public void setSellerUid(Integer sellerUid) {
        this.sellerUid = sellerUid;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Integer getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Integer tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
