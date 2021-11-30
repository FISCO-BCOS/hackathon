package com.maskview.mainView.mine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sl.utakephoto.compress.CompressConfig;
import com.sl.utakephoto.crop.CropOptions;
import com.sl.utakephoto.exception.TakeException;
import com.sl.utakephoto.manager.ITakePhotoResult;
import com.sl.utakephoto.manager.UTakePhoto;
import com.maskview.R;
import com.maskview.dao.UserRequest;
import com.maskview.util.UtilParameter;

import java.io.File;
import java.util.List;

public class SetBackgroundWall extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvNowBackgroundWall;
    private UserRequest mRequest;
    private File privateFile;
    private Context mContext;
    private static final int RESULT_SUCCESS = 0;
    private static final int RESULT_FAIL = 1;
    private static final int NO_RESPONSE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_background_wall);
        initView();
    }

    private void initView() {
        mContext = this;
        RelativeLayout mRvAllView = findViewById(R.id.rv_setBackgroundWall);
        mRvAllView.setOnClickListener(this);
        mIvNowBackgroundWall = findViewById(R.id.iv_nowBackgroundWall);
        mIvNowBackgroundWall.setOnClickListener(this);
        TextView mTvSetBackgroundWall = findViewById(R.id.tv_setBackgroundWall);
        mTvSetBackgroundWall.setOnClickListener(this);
        mRequest = new UserRequest();
        initData();
    }

    private void initData() {
        String url = getIntent().getStringExtra("backWallUrl");
        if (url != null && !url.equals("")) {
            Glide.with(mContext).load(url).dontAnimate().listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    ViewGroup.LayoutParams params = mIvNowBackgroundWall.getLayoutParams();
                    int width = UtilParameter.mScreenWidth - mIvNowBackgroundWall.getPaddingLeft() - mIvNowBackgroundWall.getPaddingRight();
                    // 宽度比
                    float scale = (float) width / (float) resource.getIntrinsicWidth();
                    // 显示高度
                    int height = Math.round(resource.getIntrinsicHeight() * scale);
                    params.width = width;
                    params.height = height + mIvNowBackgroundWall.getPaddingTop() + mIvNowBackgroundWall.getPaddingBottom();
                    mIvNowBackgroundWall.setLayoutParams(params);
                    return false;
                }
            }).into(mIvNowBackgroundWall);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_setBackgroundWall:
            case R.id.iv_nowBackgroundWall:
                finish();
                break;
            case R.id.tv_setBackgroundWall:
                setBackgroundWall();
                break;
        }
    }

    /**
     * 设置背景墙
     */
    private void setBackgroundWall() {
        UTakePhoto.with(SetBackgroundWall.this)
                .openAlbum()
                .setCrop(new CropOptions.Builder()
                        .setAspectX(6)
                        .setAspectY(5)
                        .setOutputX(1)
                        .setOutputY(20)
                        .setWithOwnCrop(true)
                        .create())
                .setCompressConfig(new CompressConfig.Builder().create())
                .build(new ITakePhotoResult() {
                    @Override
                    public void takeSuccess(List<Uri> uriList) {
                        Uri uri = uriList.get(0);
                        final String privatePath = uri.getPath();
                        assert privatePath != null;
                        privateFile = new File(privatePath);
                        final String imgName = privateFile.getName();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String uploadResult = mRequest.uploadImage(privateFile, UtilParameter.uploadImgUrl, UtilParameter.myToken);
                                Log.e("TAG", "uploadResult: " + uploadResult);
                                if (!uploadResult.equals("")) {
                                    if (uploadResult.contains("true")) {
                                        String result = mRequest.updateBackgroundWall(imgName, UtilParameter.myToken);
                                        Log.e("TAG", "result: " + result);
                                        if (result.contains("true")) {
                                            if (privateFile.exists()) {
                                                privateFile.delete();
                                            }
                                            Intent intent = new Intent();
                                            intent.setAction("action.refreshMineFragment");
                                            sendBroadcast(intent);
                                            myHandler.sendEmptyMessage(RESULT_SUCCESS);
                                        } else {
                                            myHandler.sendEmptyMessage(RESULT_FAIL);
                                        }
                                    } else {
                                        myHandler.sendEmptyMessage(RESULT_FAIL);
                                    }
                                } else {
                                    // 未响应
                                    myHandler.sendEmptyMessage(NO_RESPONSE);
                                }
                            }
                        }).start();
                        SetBackgroundWall.this.finish();
                    }

                    @Override
                    public void takeFailure(TakeException ex) {

                    }

                    @Override
                    public void takeCancel() {
                        SetBackgroundWall.this.finish();
                    }
                });
    }

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == RESULT_SUCCESS) {

            } else if (msg.what == RESULT_FAIL) {
                Toast.makeText(mContext, "失败,请稍后重试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == NO_RESPONSE) {
                Toast.makeText(mContext, "网络不佳", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
}
