package com.maskview.mainView.myPurchase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.maskview.R;

import com.maskview.mainView.MaskView;

import com.maskview.mainView.myPurchase.adapter.MyPurchaseAdapter;
import com.maskview.util.GetSDPath;
import com.maskview.util.UtilParameter;

import java.io.File;
import java.io.FileFilter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MyPurchase extends AppCompatActivity {

    private RefreshLayout refreshLayout_myPurchase;
    private RecyclerView recyclerView_myPurchase;
    private LinearLayout layout_noImg;
    private Context mContext;
    private MyPurchaseAdapter myPurchaseAdapter;
    private int purchaseRefreshTag = 0;
    private File[] mImgFiles;
    private List<Bitmap> mPurchaseImgBitmapList;
    private int screenHeight;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchase);
        mContext = this;
        initView();
        myPurchaseRecyclerRefresh();
        refreshLayout_myPurchase.autoRefresh();
    }

    private void initView() {
        refreshLayout_myPurchase = findViewById(R.id.smartRefresh_myPurchase);
        recyclerView_myPurchase = findViewById(R.id.recycler_myPurchase);
        layout_noImg = findViewById(R.id.layout_myPurchase_noImg);
        layout_noImg.setVisibility(View.INVISIBLE);
        ImageView iv_finishThisActivity = findViewById(R.id.myPurchase_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.out_to_right);
            }
        });
    }

    // 本机获取我购买的图片
    private boolean getMyPurchaseData() {
        mPurchaseImgBitmapList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 29) {
            // Android10以上(包括10)
            List<InputStream> imgInputStreamList = GetSDPath.getImageFile(mContext, "tra-" + UtilParameter.myPhoneNumber);
            for (int i = 0; i < imgInputStreamList.size(); i++) {
                mPurchaseImgBitmapList.add(BitmapFactory.decodeStream(imgInputStreamList.get(i)));
            }
            layout_noImg.setVisibility(View.INVISIBLE);
            getScreenSize();
            showMyPurchaseImg();
            refreshLayout_myPurchase.finishRefresh();
            return true;
        } else {
            // Android10以下
            String myPurchasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MaskView购买";
            File file = new File(myPurchasePath);
            if (file.exists()) {
                // 路径存在,获取该路径下所有图片
                mImgFiles = getMyPurchaseImageFiles(myPurchasePath);
                if (mImgFiles == null || mImgFiles.length == 0) {
                    // 没有图片提示
                    layout_noImg.setVisibility(View.VISIBLE);
                    refreshLayout_myPurchase.finishRefresh();
                    return false;
                } else {
                    // 文件夹内有照片,获取登陆者购买照片Bitmap
                    reversToBitmap();
                    if (mPurchaseImgBitmapList.size() > 0) {
                        // 文件夹内有登陆者的购买照片
                        layout_noImg.setVisibility(View.INVISIBLE);
                        getScreenSize();
                        showMyPurchaseImg();
                        refreshLayout_myPurchase.finishRefresh();
                        return true;
                    } else {
                        layout_noImg.setVisibility(View.VISIBLE);
                        refreshLayout_myPurchase.finishRefresh();
                        return false;
                    }
                }
            } else {
                // 没有图片提示
                layout_noImg.setVisibility(View.VISIBLE);
                refreshLayout_myPurchase.finishRefresh();
                return false;
            }
        }
    }

    // 显示图片,绑定适配器
    private void showMyPurchaseImg() {
        recyclerView_myPurchase.setVisibility(View.VISIBLE);
        recyclerView_myPurchase.setHasFixedSize(true);
        recyclerView_myPurchase.setItemAnimator(null);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        recyclerView_myPurchase.setLayoutManager(layoutManager);
        refreshMyPurchaseUI();
    }

    private void refreshMyPurchaseUI() {
        if (myPurchaseAdapter == null) {
            myPurchaseAdapter = new MyPurchaseAdapter(mContext, mPurchaseImgBitmapList, screenWidth, screenHeight);
            recyclerView_myPurchase.setAdapter(myPurchaseAdapter);
        } else {
            myPurchaseAdapter.notifyDataSetChanged();
        }
    }

    // 我的购买图片浏览上拉刷新
    private void myPurchaseRecyclerRefresh() {
        refreshLayout_myPurchase.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (purchaseRefreshTag == 0) {
                            recyclerView_myPurchase.setVisibility(View.INVISIBLE);
                            if (getMyPurchaseData()) {
                                purchaseRefreshTag++;
                            }
                        } else {
                            if (getMyPurchaseData()) {
                                myPurchaseAdapter.replaceAll(mPurchaseImgBitmapList);
                                refreshLayout.finishRefresh();
                            }
                        }
                    }
                }, 1000);
            }
        });
    }

    // 获取屏幕的宽高
    private void getScreenSize() {
        // 获取手机屏幕的长宽
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
    }

    // 获取我的购买所有图片file集合
    private File[] getMyPurchaseImageFiles(String folderPath) {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            //过滤器
            return folder.listFiles(imageFilter);
        }
        return null;
    }


    // 筛选图片的过滤器
    private FileFilter imageFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName();
            return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
        }
    };

    // 筛选登录者购买的照片,并转换为Bitmap
    private void reversToBitmap() {
        ArrayList<String> allImgPath = new ArrayList<>();
        ArrayList<String> mPurchaseImgPath = new ArrayList<>();
        for (File imgFile : mImgFiles) {
            allImgPath.add(imgFile.toString());
        }
        // 遍历筛选自己的购买图片
        String imgName;
        for (int i = 0; i < allImgPath.size(); i++) {
            imgName = allImgPath.get(i).substring(allImgPath.get(i).lastIndexOf("/") + 1);
            if (imgName.startsWith("tra-" + UtilParameter.myPhoneNumber)) {
                mPurchaseImgPath.add(allImgPath.get(i));
            }
        }
        // 将筛选的图片转为Bitmap
        for (int i = 0; i < mPurchaseImgPath.size(); i++) {
            mPurchaseImgBitmapList.add(BitmapFactory.decodeFile(mPurchaseImgPath.get(i)));
        }
    }

    // 设置本机返回键操作
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, R.anim.out_to_right);
        }
        return super.dispatchKeyEvent(event);
    }
}
