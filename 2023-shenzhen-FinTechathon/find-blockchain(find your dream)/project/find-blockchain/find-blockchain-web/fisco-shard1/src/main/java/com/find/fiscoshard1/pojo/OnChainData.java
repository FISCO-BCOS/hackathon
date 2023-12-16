package com.find.fiscoshard1.pojo;

import java.io.Serializable;

/**
 * 联邦学习节点
 */
public class OnChainData implements Serializable {



    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getModelHash() {
        return modelHash;
    }

    public void setModelHash(String modelHash) {
        this.modelHash = modelHash;
    }

    private String times;
    private String modelHash;







}
