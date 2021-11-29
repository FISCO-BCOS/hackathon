package com.maskview.mainView.mine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MineActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvPoints;
    private TextView mTvNickName;
    private ImageView mIvHeadView;
    private TextView mTvFocusCount;
    private TextView mTvFansCount;
    private String mUserName;
    private String mPoints;
    private String mHeadViewPath;
    private String mFansCount;
    private String mFocusCount;
    private UserRequest mRequest;
    private Context mContext;
    private static final int SUCCESS = 0;
    private boolean isGetData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mine);
        initView();
        // 广播接收刷新指定childPosition的UI
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.focus.refreshMineFocusCount");
        intentFilter.addAction("action.cancelFocus.refreshMineFocusCount");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isGetData) {
            getMyInfo();
        } else {
            isGetData = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isGetData = false;
    }

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
            }
        }
    };

    /**
     * View结束后关闭广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mRefreshBroadcastReceiver);
    }

    private void initView() {
        mContext = this;
        LinearLayout layout_myPurchaseRecords = findViewById(R.id.layout_mine_myPurchaseRecords);
        layout_myPurchaseRecords.setOnClickListener(this);
        ImageView iv_finish = findViewById(R.id.iv_mine_finish);
        iv_finish.setOnClickListener(this);
        LinearLayout layout_mySaleRecords = findViewById(R.id.layout_mine_mySaleRecords);
        layout_mySaleRecords.setOnClickListener(this);
        LinearLayout layout_myPurchase = findViewById(R.id.layout_mine_myPurchase);
        layout_myPurchase.setOnClickListener(this);
        LinearLayout layout_myInfo = findViewById(R.id.layout_mine_myInfo);
        layout_myInfo.setOnClickListener(this);
        LinearLayout layout_updatePwd = findViewById(R.id.layout_mine_updatePwd);
        layout_updatePwd.setOnClickListener(this);
        mTvPoints = findViewById(R.id.tv_mine_points);
        mTvNickName = findViewById(R.id.tv_mine_myNickName);
        mIvHeadView = findViewById(R.id.mine_myHeadView);
        mTvFocusCount = findViewById(R.id.tv_mine_focusCount);
        mTvFansCount = findViewById(R.id.tv_mine_fansCount);
        Button bt_logout = findViewById(R.id.bt_mine_logout);
        bt_logout.setOnClickListener(this);
        LinearLayout mLvFansInfo = findViewById(R.id.lv_mine_fansInfo);
        mLvFansInfo.setOnClickListener(this);
        LinearLayout mLvFocusInfo = findViewById(R.id.lv_mine_focusInfo);
        mLvFocusInfo.setOnClickListener(this);
        mRequest = new UserRequest();
        getMyInfo();
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
                }
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_mine_myPurchaseRecords:
                Intent intent_myPurchaseRecords = new Intent(mContext, MyPurchaseRecords.class);
                startActivity(intent_myPurchaseRecords);
                break;
            case R.id.layout_mine_mySaleRecords:
                Intent intent_mySaleRecords = new Intent(mContext, MySaleRecords.class);
                startActivity(intent_mySaleRecords);
                break;
            case R.id.layout_mine_myPurchase:
                Intent intent_myPurchase = new Intent(mContext, MyPurchase.class);
                startActivity(intent_myPurchase);
                break;
            case R.id.layout_mine_myInfo:
                Intent intent_myInfo = new Intent(mContext, MyInfo.class);
                startActivity(intent_myInfo);
                break;
            case R.id.layout_mine_updatePwd:
                Intent intent_updatePwd = new Intent(mContext, UpdatePwd.class);
                startActivity(intent_updatePwd);
                break;
            case R.id.bt_mine_logout:
                logout();
                break;
            case R.id.lv_mine_fansInfo:
                Intent intent_fansInfo = new Intent(mContext, HerAllFansInfo.class);
                intent_fansInfo.putExtra("userName", UtilParameter.myNickName);
                startActivity(intent_fansInfo);
                break;
            case R.id.lv_mine_focusInfo:
                Intent intent_focusInfo = new Intent(mContext, HerAllFocusInfo.class);
                intent_focusInfo.putExtra("userName", UtilParameter.myNickName);
                startActivity(intent_focusInfo);
                break;
            case R.id.iv_mine_finish:
                finish();
                overridePendingTransition(0, R.anim.out_to_right);
                break;
        }
    }

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
                        Intent intent = new Intent(mContext, MaskView.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
