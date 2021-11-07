package com.maskview.mainView.mine;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class SetHeadView extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvNowHeadView;
    private UserRequest mRequest;
    private File privateFile;
    private Context mContext;
    private static final int RESULT_SUCCESS = 0;
    private static final int RESULT_FAIL = 1;
    private static final int NO_RESPONSE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_head_view);
        initView();
    }

    private void initView() {
        mContext = this;
        RelativeLayout mRvAllView = findViewById(R.id.rv_setHeadView);
        mRvAllView.setOnClickListener(this);
        mIvNowHeadView = findViewById(R.id.iv_nowHeadView);
        mIvNowHeadView.setOnClickListener(this);
        TextView mTvSetHeadView = findViewById(R.id.tv_setHeadView);
        mTvSetHeadView.setOnClickListener(this);
        mRequest = new UserRequest();
        initData();
    }

    private void initData() {
        String url = getIntent().getStringExtra("headViewUrl");
        if (url != null && !url.equals("")) {
            Glide.with(this).load(url).dontAnimate().listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    ViewGroup.LayoutParams params = mIvNowHeadView.getLayoutParams();
                    // 图片宽度为屏幕宽度
                    int width = UtilParameter.mScreenWidth - mIvNowHeadView.getPaddingLeft() - mIvNowHeadView.getPaddingRight();
                    // 宽度比
                    float scale = (float) width / (float) resource.getIntrinsicWidth();
                    // 显示高度
                    int height = Math.round(resource.getIntrinsicHeight() * scale);
                    params.width = width;
                    params.height = height + mIvNowHeadView.getPaddingTop() + mIvNowHeadView.getPaddingBottom();
                    mIvNowHeadView.setLayoutParams(params);
                    return false;
                }
            }).into(mIvNowHeadView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_setHeadView:
            case R.id.iv_nowHeadView:
                // 结束当前activity
                finish();
                break;
            case R.id.tv_setHeadView:
                updateMyHeadView();
                break;
        }
    }

    /**
     * 设置, 更新头像
     */
    private void updateMyHeadView() {
        UTakePhoto.with(SetHeadView.this)
                .openAlbum()
                .setCrop(new CropOptions.Builder()
                        .setAspectX(1)
                        .setAspectY(1)
                        .setOutputX(10)
                        .setOutputY(10)
                        .setWithOwnCrop(true)
                        .create())
                .setCompressConfig(new CompressConfig.Builder().create())
                .build(new ITakePhotoResult() {
                    @Override
                    public void takeSuccess(List<Uri> uriList) {
                        // 裁剪成功后, 上传私有目录图片, 上传成功后删除
                        final Uri uri = uriList.get(0);
                        // 此处的uri就是私有目录的uri
                        String privatePath = uri.getPath();
                        assert privatePath != null;
                        privateFile = new File(privatePath);
                        final String imgName = privateFile.getName();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String uploadResult = mRequest.uploadImage(privateFile, UtilParameter.uploadImgUrl, UtilParameter.myToken);
                                //String uploadResult = mRequest.uploadImage(privateFile, "https://10.0.2.2:718/restricted/uploadImg", UtilParameter.myToken);
                                if (!uploadResult.equals("")) {
                                    if (uploadResult.contains("true")) {
                                        // 图片上传成功,上传数据
                                        final String result = mRequest.updateMyHeadView(imgName, UtilParameter.myToken);
                                        if (result.contains("true")) {
                                            // 上传图片数据成功,删除私有目录下裁剪的图片
                                            if (privateFile.exists()) {
                                                privateFile.delete();
                                            }
                                            // 发送广播, 刷新我的界面数据
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
                                    myHandler.sendEmptyMessage(NO_RESPONSE);
                                }
                            }
                        }).start();
                        SetHeadView.this.finish();
                    }

                    @Override
                    public void takeFailure(TakeException ex) {

                    }

                    @Override
                    public void takeCancel() {
                        finish();
                    }
                });
    }

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == RESULT_SUCCESS) {
                // 发送广播刷新我的界面的头像
                //Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == RESULT_FAIL) {
                Toast.makeText(mContext, "失败,请稍后重试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == NO_RESPONSE) {
                Toast.makeText(mContext, "网络不佳", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

}
