package com.find.fiscoshard1.pojo;

import java.io.Serializable;

/**
 * 联邦学习节点
 */
public class FlNode implements Serializable {

    private String id;
    private State state; // 状态
    private String hashcode;
    private String times;
    private String accuracy;

    public enum State {
        TRAINING("训练中..."),
        WAITING("等待中..."),
        GROUPING("聚合中..."),
        DONE("已完成");

        private String description;

        State(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getState() {
        return state.getDescription();
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }


}
