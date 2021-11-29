package com.maskview.mainView.herAllFansInfo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.maskview.R;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.herAllFansInfo.adapter.HerAllFansInfoAdapter;
import com.maskview.mainView.herAllFansInfo.bean.HerAllFansInfoBean;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HerAllFansInfo extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvHerFansCount;
    private int mFansCount;
    private RecyclerView mRecyclerView;
    private TextView mTvNoData;
    private String mGetUserName;
    private boolean mIsMine;
    private Context mContext;
    private UserRequest mRequest;
    private HerAllFansInfoBean mHerAllFansInfoBeanClass;
    private List<HerAllFansInfoBean.DataBean> mBeanList;
    private HerAllFansInfoAdapter mAdapter;
    private static final int SUCCESS = 0;
    private static final int NO_DATA = 1;
    private static final int NO_RESPONSE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_her_all_fans_info);

        setDialogSize();
        // 广播接收刷新指定childPosition的UI
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshHerWindowUI");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    /**
     * 设置弹出框大小
     */
    private void setDialogSize() {
        // 获取对话框当前的参数值
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (UtilParameter.mScreenHeight * 0.9);
        p.width = (int) (UtilParameter.mScreenWidth * 0.9);
        // 设置本身透明度
        //p.alpha = 1.0f;
        // 设置黑暗度
        //p.dimAmount = 0.5f;
        getWindow().setAttributes(p);
        initView();
    }

    private void initView() {
        mTvHerFansCount = findViewById(R.id.tv_herAllFansInfo_fansCount);
        mRecyclerView = findViewById(R.id.recycler_herAllFansInfo);
        mTvNoData = findViewById(R.id.tv_herAllFansInfo_noData);
        ImageView mIvFinish = findViewById(R.id.iv_herAllFansInfo_finish);
        mIvFinish.setOnClickListener(this);
        mRequest = new UserRequest();
        mContext = this;
        getAllFansInfo();
    }

    private void getAllFansInfo() {
        mGetUserName = getIntent().getStringExtra("userName");
        mIsMine = getIntent().getBooleanExtra("isMine", false);
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = mRequest.getHerAllFansInfo(mGetUserName, UtilParameter.myToken);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
                            mHerAllFansInfoBeanClass = JSON.parseObject(result, HerAllFansInfoBean.class);
                            mBeanList = mHerAllFansInfoBeanClass.getData();
                            if (mBeanList.size() > 0) {
                                mFansCount = mBeanList.size();
                                mHandler.sendEmptyMessage(SUCCESS);
                            } else {
                                // 无数据
                                mHandler.sendEmptyMessage(NO_DATA);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mHandler.sendEmptyMessage(NO_RESPONSE);
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

    @SuppressLint("SetTextI18n")
    private void showRecyclerView() {
        mTvHerFansCount.setText("(" + mFansCount + ")");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (mIsMine) {
            mAdapter = new HerAllFansInfoAdapter(mContext, mBeanList, true);
        } else {
            mAdapter = new HerAllFansInfoAdapter(mContext, mBeanList, false);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == SUCCESS) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mTvNoData.setVisibility(View.INVISIBLE);
                showRecyclerView();
            } else if (msg.what == NO_DATA) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mTvNoData.setVisibility(View.VISIBLE);
            } else if (msg.what == NO_RESPONSE) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mTvNoData.setVisibility(View.INVISIBLE);
            }
            return false;
        }
    });

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshHerWindowUI")) {
                Bundle bundle = intent.getBundleExtra("msg");
                int selectedItemPosition = bundle.getInt("windowSelectedItemPosition");
                View view = mRecyclerView.getLayoutManager().findViewByPosition(selectedItemPosition);
                if (view != null) {
                    TextView tv_focus = view.findViewById(R.id.item_tv_herAllFansInfo_focus);
                    TextView tv_cancelFocus = view.findViewById(R.id.item_tv_herAllFansInfo_cancelFocus);
                    tv_focus.setVisibility(View.INVISIBLE);
                    tv_cancelFocus.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    /**
     * View结束后关闭广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_herAllFansInfo_finish:
                finish();
                break;
        }
    }

    /**
     * 本机自带返回键
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
