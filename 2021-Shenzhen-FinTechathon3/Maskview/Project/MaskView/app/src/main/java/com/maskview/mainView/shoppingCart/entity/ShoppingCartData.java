package com.maskview.mainView.shoppingCart.entity;

import java.io.Serializable;
import java.util.List;

public class ShoppingCartData extends RequestResultForShoppingCar {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        private String sellerName;  //卖家姓名
        private List<GoodsInfoBean> goodsInfo;  //该卖家下用户购物车的每个商品信息
        private boolean isCheck = false;

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public List<GoodsInfoBean> getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public static class GoodsInfoBean implements Serializable {

            private String imgTopic;
            private String imgPath;
            private String imgPrice;
            private boolean isCheck = false;
            private int num = 1;

            public String getImgTopic() {
                return imgTopic;
            }

            public void setImgTopic(String imgTopic) {
                this.imgTopic = imgTopic;
            }

            public String getImgPath() {
                return imgPath;
            }

            public void setImgPath(String imgPath) {
                this.imgPath = imgPath;
            }

            public String getImgPrice() {
                return imgPrice;
            }

            public void setImgPrice(String imgPrice) {
                this.imgPrice = imgPrice;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }
    }
}
