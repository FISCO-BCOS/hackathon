package com.find.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedNode{

    private int id;
    private String name;
    private String nodeId;
    private double repValue;
    private String domain;
    private String security;
    private String onlineState;
    private State state; //车联网场景
    private String hashcode; //车联网场景
    private String times; //全域场景
    private String docId; //文档id
    private float accuracy; //车联网场景
    private String point_x; //车联网场景
    private String point_y; //车联网场景
    private String speed;//车联网场景
    private String scene;//车联网场景
    private String line;//车联网场景
    private String CommQuality;//车联网场景

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
}
