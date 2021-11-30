package com.media.dao;

import com.media.DemoApplication;
import com.media.bcos.client.PointControllerClient;

import java.util.TimerTask;

public class AutoReturnPoints extends TimerTask {

    private PointControllerClient pointControllerClient = DemoApplication.pointControllerClient;
    private Integer uid;

    public AutoReturnPoints(Integer uid) {
        this.uid = uid;
    }

    @Override
    public void run() {
        pointControllerClient.returnPoint(uid);
    }
}
