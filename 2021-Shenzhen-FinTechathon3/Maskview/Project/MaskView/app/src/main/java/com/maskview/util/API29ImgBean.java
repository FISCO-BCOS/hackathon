package com.maskview.util;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


public class API29ImgBean implements Parcelable {
    private Uri imgUri;
    private String imgPath;
    private String imgName;
    private int imgID;
    private long imgDate;

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public long getImgDate() {
        return imgDate;
    }

    public void setImgDate(long imgDate) {
        this.imgDate = imgDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUri.toString());
        dest.writeString(imgPath);
        dest.writeString(imgName);
        dest.writeInt(imgID);
        dest.writeLong(imgDate);
    }

    public static final Parcelable.Creator<API29ImgBean> CREATOR = new Creator<API29ImgBean>() {
        @Override
        public API29ImgBean createFromParcel(Parcel source) {
            API29ImgBean bean = new API29ImgBean();
            bean.imgUri = Uri.parse(source.readString());
            bean.imgPath = source.readString();
            bean.imgName = source.readString();
            bean.imgID = source.readInt();
            bean.imgDate = source.readLong();
            return bean;
        }

        @Override
        public API29ImgBean[] newArray(int size) {
            return new API29ImgBean[0];
        }
    };
}
