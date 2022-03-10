package com.maskview.mainView.mine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.maskview.R;
import com.maskview.dao.SPUtils;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.MaskView;
import com.maskview.mainView.herAllFansInfo.HerAllFansInfo;
import com.maskview.mainView.herAllFocusInfo.HerAllFocusInfo;
import com.maskview.mainView.myInfo.MyInfo;
import com.maskview.mainView.myPurchase.MyPurchase;
import com.maskview.mainView.records.MyPurchaseRecords;
import com.maskview.mainView.records.MySaleRecords;
import com.maskview.mainView.updatePwd.UpdatePwd;
import com.maskview.util.MyScrollView;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;

public class Mine extends Fragment implements View.OnClickListener {

    private TextView mTvPoints;
    private TextView mTvNickName;
    //private ImageView mIvHeadView;
    private RoundedImageView mIvHeadView;
    private ImageView mIvBackWall;
    private TextView mTvFocusCount;
    private TextView mTvFansCount;
    private String mUserName;
    private String mPoints;
    private String mHeadViewPath;
    private String mBackWallPath;
    private String mFansCount;
    private String mFocusCount;
    private boolean isGetData = false;
    private UserRequest mRequest;
    private Context mContext;
    private static final int SUCCESS = 0;
    private MyScrollView myScrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        // 广播接收刷新指定childPosition的UI
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.focus.refreshMineFocusCount");
        intentFilter.addAction("action.cancelFocus.refreshMineFocusCount");
        intentFilter.addAction("action.refreshMineFragment");
        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    //每次进入fragment都刷新数据
    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            getMyInfo();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isGetData) {
            getMyInfo();
        } else {
            isGetData = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        mContext = getActivity();
        LinearLayout layout_myPurchaseRecords = Objects.requireNonNull(getActivity()).findViewById(R.id.layout_mine_myPurchaseRecords);
        layout_myPurchaseRecords.setOnClickListener(this);
        ImageView iv_finish = getActivity().findViewById(R.id.iv_mine_finish);
        iv_finish.setVisibility(View.INVISIBLE);
        LinearLayout layout_mySaleRecords = getActivity().findViewById(R.id.layout_mine_mySaleRecords);
        layout_mySaleRecords.setOnClickListener(this);
        LinearLayout layout_myPurchase = getActivity().findViewById(R.id.layout_mine_myPurchase);
        layout_myPurchase.setOnClickListener(this);
        LinearLayout layout_myInfo = getActivity().findViewById(R.id.layout_mine_myInfo);
        layout_myInfo.setOnClickListener(this);
        LinearLayout layout_updatePwd = getActivity().findViewById(R.id.layout_mine_updatePwd);
        layout_updatePwd.setOnClickListener(this);
        mTvPoints = getActivity().findViewById(R.id.tv_mine_points);
        mTvNickName = getActivity().findViewById(R.id.tv_mine_myNickName);
        mIvHeadView = getActivity().findViewById(R.id.mine_myHeadView);
        mIvHeadView.setOnClickListener(this);
        mIvBackWall = getActivity().findViewById(R.id.iv_mine_myBackWall);
        mIvBackWall.setOnClickListener(this);
        mTvFocusCount = getActivity().findViewById(R.id.tv_mine_focusCount);
        mTvFansCount = getActivity().findViewById(R.id.tv_mine_fansCount);
        Button bt_logout = getActivity().findViewById(R.id.bt_mine_logout);
        bt_logout.setOnClickListener(this);
        LinearLayout mLvFansInfo = getActivity().findViewById(R.id.lv_mine_fansInfo);
        mLvFansInfo.setOnClickListener(this);
        LinearLayout mLvFocusInfo = getActivity().findViewById(R.id.lv_mine_focusInfo);
        mLvFocusInfo.setOnClickListener(this);
        mRequest = new UserRequest();
        // 设置下拉时背景墙放大,弹性效果
        myScrollView = getActivity().findViewById(R.id.sv_mineScroll);
        myScrollView.setImageView(mIvBackWall);
    }

    private void getMyInfo() {
        mHeadViewPath = "";
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = mRequest.getMyInfo(UtilParameter.myToken);
                if (result.contains("true")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        mPoints = jsonObject.get("points") + "";
                        mUserName = jsonObject.get("userName") + "";
                        mHeadViewPath = jsonObject.get("headViewPath") + "";
                        mFansCount = jsonObject.get("fansCount") + "";
                        mFocusCount = jsonObject.get("focusCount") + "";
                        mBackWallPath = jsonObject.get("backgroundWall") + "";
                        mHandler.sendEmptyMessage(SUCCESS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        es.submit(task);
        es.shutdown();
        while (true) {
            if (es.isTerminated()) {
                break;
            }
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == SUCCESS) {
                mTvPoints.setText(mPoints + "");
                mTvNickName.setText(mUserName + "");
                mTvFansCount.setText(mFansCount + "");
                mTvFocusCount.setText(mFocusCount + "");
                if (!mHeadViewPath.equals("")) {
                    String url = UtilParameter.IMAGES_IP + mHeadViewPath;
                    Glide.with(mContext).load(url).placeholder(R.mipmap.head).dontAnimate().into(mIvHeadView);
                } else {
                    mIvHeadView.setImageResource(R.mipmap.head);
                }
                if (!mBackWallPath.equals("")) {
                    String url = UtilParameter.IMAGES_IP + mBackWallPath;
                    Glide.with(mContext).load(url).dontAnimate().into(mIvBackWall);
                } else {
                    mIvBackWall.setImageResource(R.mipmap.mybackground);
                }
            }
            return false;
        }
    });

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.focus.refreshMineFocusCount")) {
                int focusCount = Integer.parseInt(mTvFocusCount.getText() + "");
                int nowFocus = focusCount + 1;
                mTvFocusCount.setText(nowFocus + "");
            } else if (action.equals("action.cancelFocus.refreshMineFocusCount")) {
                int focusCount = Integer.parseInt(mTvFocusCount.getText() + "");
                int nowFocus = focusCount - 1;
                mTvFocusCount.setText(nowFocus + "");
            } else if (action.equals("action.refreshMineFragment")) {
                getMyInfo();
            }
        }
    };

    /**
     * View结束后关闭广播
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mContext.unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_mine_myPurchaseRecords:
                Intent intent_myPurchaseRecords = new Intent(getActivity(), MyPurchaseRecords.class);
                startActivity(intent_myPurchaseRecords);
                break;
            case R.id.layout_mine_mySaleRecords:
                Intent intent_mySaleRecords = new Intent(getActivity(), MySaleRecords.class);
                startActivity(intent_mySaleRecords);
                break;
            case R.id.layout_mine_myPurchase:
                Intent intent_myPurchase = new Intent(getActivity(), MyPurchase.class);
                startActivity(intent_myPurchase);
                break;
            case R.id.layout_mine_myInfo:
                Intent intent_myInfo = new Intent(getActivity(), MyInfo.class);
                startActivity(intent_myInfo);
                break;
            case R.id.layout_mine_updatePwd:
                Intent intent_updatePwd = new Intent(getActivity(), UpdatePwd.class);
                startActivity(intent_updatePwd);
                break;
            case R.id.bt_mine_logout:
                logout();
                break;
            case R.id.lv_mine_fansInfo:
                Intent intent_fansInfo = new Intent(mContext, HerAllFansInfo.class);
                intent_fansInfo.putExtra("userName", UtilParameter.myNickName);
                intent_fansInfo.putExtra("isMine", true);
                mContext.startActivity(intent_fansInfo);
                break;
            case R.id.lv_mine_focusInfo:
                Intent intent_focusInfo = new Intent(mContext, HerAllFocusInfo.class);
                intent_focusInfo.putExtra("userName", UtilParameter.myNickName);
                intent_focusInfo.putExtra("isMine", true);
                mContext.startActivity(intent_focusInfo);
                break;
            case R.id.mine_myHeadView:
                // 放大头像
                Intent intent_myHeadView = new Intent(mContext, SetHeadView.class);
                if (!mHeadViewPath.equals("")) {
                    String url = UtilParameter.IMAGES_IP + mHeadViewPath;
                    intent_myHeadView.putExtra("headViewUrl", url);
                }
                startActivity(intent_myHeadView);
                break;
            case R.id.iv_mine_myBackWall:
                // 放大背景墙
                Intent intent_myBackWall = new Intent(mContext, SetBackgroundWall.class);
                if (!mBackWallPath.equals("")) {
                    String url = UtilParameter.IMAGES_IP + mBackWallPath;
                    intent_myBackWall.putExtra("backWallUrl", url);
                }
                startActivity(intent_myBackWall);
                break;
        }
    }

    /**
     * 注销
     */
    private void logout() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).setMessage("确定要注销吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.clear(mContext);
                        UtilParameter.myNickName = null;
                        UtilParameter.myPhoneNumber = null;
                        UtilParameter.myPoints = null;
                        UtilParameter.myToken = null;
                        Intent intent = new Intent(getActivity(), MaskView.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("fragmentID", 0);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
    }
}

