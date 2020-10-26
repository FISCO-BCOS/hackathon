package org.fisco.bcos.model;

import java.math.BigInteger;

//p2ptime对象信息
public class P2PEntry {
    private String p2pAddress;
    private String ownerAddress;
    private String title;
    private BigInteger price;
    private String description;
    private BigInteger state;

    public String getP2pAddress() {
        return p2pAddress;
    }

    public void setP2pAddress(String p2pAddress) {
        this.p2pAddress = p2pAddress;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigInteger getState() {
        return state;
    }

    public void setState(BigInteger state) {
        this.state = state;
    }
}
