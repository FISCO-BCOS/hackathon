package com.media.bcos.client;

public class TRecord {

    int seller;
    int buyer;
    int price;
    String hash;
    String transactionId;

    public TRecord(int seller, int buyer, int price, String hash, String transactionId) {
        this.seller = seller;
        this.buyer = buyer;
        this.price = price;
        this.hash = hash;
        this.transactionId = transactionId;
    }

    public int getSeller() {
        return seller;
    }

    public void setSeller(int seller) {
        this.seller = seller;
    }

    public int getBuyer() {
        return buyer;
    }

    public void setBuyer(int buyer) {
        this.buyer = buyer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "TRecord{" +
                "seller=" + seller +
                ", buyer=" + buyer +
                ", price=" + price +
                ", hash='" + hash + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
