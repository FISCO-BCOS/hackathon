package com.maskview.mainView.mgGoods.bigImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.maskview.R;
import com.maskview.dao.RenderScriptBlur;

import java.io.IOException;

import uk.co.senab.photoview.PhotoView;

public class ShowBigConfirmImg extends AppCompatActivity {

    private PhotoView mIvBigConfirmImg;
    private LinearLayout mLvAllView;
    private String mGetUri;
    private Bitmap mBitmap;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_confirm_img);

        initView();
    }

    private void initView() {
        mIvBigConfirmImg = findViewById(R.id.iv_bigConfirmImg);
        mLvAllView = findViewById(R.id.lv_allView);
        mContext = this;
        initData();
    }

    private void initData() {
        mGetUri = getIntent().getStringExtra("imgUri");
        Uri uri = Uri.parse(mGetUri);
        // 高斯模糊
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            mBitmap = RenderScriptBlur.blur(mContext, bitmap, 0.06f, 10);
            mLvAllView.setBackground(new BitmapDrawable(getResources(), mBitmap));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mIvBigConfirmImg.setImageURI(uri);
    }
}
