package com.find.pojo;
import java.io.Serializable;
import java.io.Serializable;

public class CarNode implements Serializable {

    /**
     * 联邦学习节点
     */
    private String id;
    private String nodeId;
    private double repValue;
    private String domain;
    private String security;
    private State state; //车联网场景
    private String hashcode; //车联网场景
    private String times; //全域场景
    private Float accuracy; //车联网场景
    private String point_x; //车联网场景
    private String point_y; //车联网场景
    private String speed;//车联网场景


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

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
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
