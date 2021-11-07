package com.maskview.mainView.confirmOrders.bean;

// 购买时向服务器传的字段

public class JsonData {

    // 图片所有者昵称,即卖方
    public String imgOwnerName;
    // 上传,上传的是卖家确权的图片名称
    public String imgPath;
    // 图片名称, 便于tra-手机号的拼接
    public String imgName;
    // 图片价格
    public String imgPrice;
}
