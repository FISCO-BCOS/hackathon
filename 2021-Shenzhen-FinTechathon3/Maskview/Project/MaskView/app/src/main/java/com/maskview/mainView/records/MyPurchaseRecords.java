package com.maskview.mainView.records;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.maskview.R;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.records.adapter.MyPurchaseRecordsAdapter;
import com.maskview.mainView.records.beans.PurchaseRecordsBean;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyPurchaseRecords extends AppCompatActivity implements View.OnClickListener {

    private UserRequest ur;
    private Context mContext;
    private PurchaseRecordsBean purchaseRecordsBean;

    private RefreshLayout refreshLayout;
    private RecyclerView myRecyclerView;
    private MyPurchaseRecordsAdapter adapter;
    private LinearLayout layout_noPurchaseRecords;
    private LinearLayout layout_noResponse;
    private int refreshTag;

    private final static int NO_RECORDS = 0;
    private final static int NO_RESPONSE = 1;
    private final static int SUCCESS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchase_records);

        initView();
        refreshTag = 0;
        mySellRecyclerRefresh();
        refreshLayout.autoRefresh();
    }


    private void initView() {
        mContext = this;
        ur = new UserRequest();

        ImageView iv_finishThisActivity = findViewById(R.id.purchaseRecords_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(this);
        refreshLayout = findViewById(R.id.smartRefresh_purchaseRecords);
        myRecyclerView = findViewById(R.id.recycler_purchaseRecords);
        layout_noPurchaseRecords = findViewById(R.id.layout_myPurchaseRecords_noRecords);
        layout_noPurchaseRecords.setVisibility(View.INVISIBLE);
        layout_noResponse = findViewById(R.id.layout_myPurchaseRecords_noResponse);
        layout_noResponse.setVisibility(View.INVISIBLE);
    }

    private void getData() {
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = ur.getPurchaseRecords(UtilParameter.myToken);
                Log.e("--------------", "result: " + result);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
                            purchaseRecordsBean = JSON.parseObject(result, PurchaseRecordsBean.class);
                            purchaseRecordsHandler.sendEmptyMessage(SUCCESS);
                        } else if (tag.equals("false")) {
                            purchaseRecordsBean = null;
                            purchaseRecordsHandler.sendEmptyMessage(NO_RECORDS);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    purchaseRecordsHandler.sendEmptyMessage(NO_RESPONSE);
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

    private void showRecyclerView() {
        myRecyclerView.setVisibility(View.VISIBLE);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setItemAnimator(null);

        //布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        //垂直方向或水平
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //是否反转
        linearLayoutManager.setReverseLayout(false);
        //布局管理器与RecyclerView绑定
        myRecyclerView.setLayoutManager(linearLayoutManager);
        //创建适配器
        adapter = new MyPurchaseRecordsAdapter(mContext, purchaseRecordsBean.getData());
        //RecyclerView绑定适配器
        myRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.purchaseRecords_finishThisActivity:
                MyPurchaseRecords.this.finish();
                overridePendingTransition(0, R.anim.out_to_right);
                break;
        }
    }

    //我的购买记录上拉刷新
    private void mySellRecyclerRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (refreshTag == 0) {
                            layout_noPurchaseRecords.setVisibility(View.INVISIBLE);
                            layout_noResponse.setVisibility(View.INVISIBLE);
                            getData();
                            refreshTag++;
                        } else {
                            getData();
                            if (purchaseRecordsBean != null) {
                                adapter.replaceAll(purchaseRecordsBean.getData());
                            }
                            refreshLayout.finishRefresh();
                        }
                    }
                }, 1000);
            }
        });
    }

    //我的购买记录提示
    private Handler purchaseRecordsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == SUCCESS) {
                layout_noPurchaseRecords.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.INVISIBLE);
                myRecyclerView.setVisibility(View.VISIBLE);
                showRecyclerView();
                refreshLayout.finishRefresh();
            } else if (msg.what == NO_RECORDS) {
                layout_noPurchaseRecords.setVisibility(View.VISIBLE);
                layout_noResponse.setVisibility(View.INVISIBLE);
                myRecyclerView.setVisibility(View.INVISIBLE);
                refreshLayout.finishRefresh();
            } else if (msg.what == NO_RESPONSE) {
                layout_noPurchaseRecords.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.VISIBLE);
                myRecyclerView.setVisibility(View.INVISIBLE);
                refreshLayout.finishRefresh();
            }
            return false;
        }
    });

    // 自带的返回键跳转页面并finish当前页面
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, R.anim.out_to_right);
        }
        return super.dispatchKeyEvent(event);
    }
}
