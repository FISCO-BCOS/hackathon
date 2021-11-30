package com.maskview.mainView.herAllFansInfo.bean;

import java.util.List;

public class HerAllFansInfoBean extends RequestResult {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private int uid;
        private String headViewPath;
        private String userName;
        private int relation;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getHeadViewPath() {
            return headViewPath;
        }

        public void setHeadViewPath(String headViewPath) {
            this.headViewPath = headViewPath;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }
    }
}
