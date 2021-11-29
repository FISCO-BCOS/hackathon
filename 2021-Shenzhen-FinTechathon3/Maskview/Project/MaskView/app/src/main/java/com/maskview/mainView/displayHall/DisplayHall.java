package com.maskview.mainView.displayHall;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.maskview.R;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.displayHall.adapter.DisplayHallAdapter;
import com.maskview.mainView.displayHall.bean.DisplayHallBean;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisplayHall extends Fragment {

    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout mLvLoadingView;
    private LinearLayout mLvNoResponse;
    private int mScreenWidth;
    private UserRequest mRequest;
    private DisplayHallBean mDisplayHallBeanClass;
    private List<DisplayHallBean.DataBean> mBeanList;
    private List<DisplayHallBean.DataBean> mFirstBeanList;
    private DisplayHallAdapter mDisplayHallAdapter;
    private static final int RESPONSE_SUCCESS = 0;
    private static final int NO_RESPONSE = 1;
    private int mAllCount;
    private int mCurrentCount;
    private Context mContext;
    private int tagRefresh = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_hall, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        // 设置刷新与加载更多监听
        onRefresh();
        // 打开界面自动刷新
        mRefreshLayout.autoRefresh();
        // 广播接收刷新指定childPosition的UI
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshDisplayHallFansCount");
        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    /**
     * 绑定控件,初始化视图
     */
    private void initView() {
        mContext = getActivity();
        getScreenSize();
        mRefreshLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.smartRefreshLayout_displayHall);
        mRecyclerView = getActivity().findViewById(R.id.recyclerView_displayHall);
        mLvLoadingView = getActivity().findViewById(R.id.layout_displayHall_loading);
        mLvNoResponse = getActivity().findViewById(R.id.layout_displayHall_noResponse);
        mLvNoResponse.setVisibility(View.INVISIBLE);
        mRequest = new UserRequest();
    }

    /**
     * 广播刷新该Activity对应childPosition的UI
     */
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshDisplayHallFansCount")) {
                // 在用户信息界面点击关注后, 刷新展厅界面的关注状态
                Bundle bundle = intent.getBundleExtra("msg");
                int childPosition = bundle.getInt("recyclerChildPosition");
                mRecyclerView = getActivity().findViewById(R.id.recyclerView_displayHall);
                View view = mRecyclerView.getLayoutManager().findViewByPosition(childPosition);
                if (view != null) {
                    Button bt_focus = view.findViewById(R.id.bt_item_displayHall_focus);
                    Button bt_cancelFocus = view.findViewById(R.id.bt_item_displayHall_cancelFocus);
                    TextView tv_fansCount = view.findViewById(R.id.item_displayHall_fans);
                    bt_focus.setVisibility(View.INVISIBLE);
                    bt_cancelFocus.setVisibility(View.VISIBLE);
                    int fansCount = Integer.parseInt(tv_fansCount.getText() + "");
                    int nowFans = fansCount + 1;
                    tv_fansCount.setText(nowFans + "");
                }
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

    /**
     * 获取的展厅数据,根据是否登录获取到的数据不同
     */
    private void getData() {
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (UtilParameter.myToken == null) {
                    // 未登录状态
                    String result = mRequest.getDisplayInfo(null);
                    if (!result.equals("")) {
                        // 服务器响应成功
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String tag = jsonObject.get("result") + "";
                            if (tag.equals("true")) {
                                mDisplayHallBeanClass = JSON.parseObject(result, DisplayHallBean.class);
                                mBeanList = mDisplayHallBeanClass.getData();
                                mAllCount = mBeanList.size();
                                mHandler.sendEmptyMessage(RESPONSE_SUCCESS);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 服务器未响应
                        mDisplayHallBeanClass = null;
                        mBeanList = null;
                        mAllCount = 0;
                        mHandler.sendEmptyMessage(NO_RESPONSE);
                    }
                } else {
                    // 登录状态
                    String result = mRequest.getDisplayInfo(UtilParameter.myToken);
                    if (!result.equals("")) {
                        // 服务器响应成功
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String tag = jsonObject.get("result") + "";
                            if (tag.equals("true")) {
                                mDisplayHallBeanClass = JSON.parseObject(result, DisplayHallBean.class);
                                mBeanList = mDisplayHallBeanClass.getData();
                                mAllCount = mBeanList.size();
                                mHandler.sendEmptyMessage(RESPONSE_SUCCESS);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 服务器未响应
                        mDisplayHallBeanClass = null;
                        mBeanList = null;
                        mAllCount = 0;
                        mHandler.sendEmptyMessage(NO_RESPONSE);
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

    /**
     * 获取到请求的数据,显示数据
     */
    private void showRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(null);
        // 打乱顺序
        Collections.shuffle(mBeanList);
        mCurrentCount = 0;
        mFirstBeanList = new ArrayList<>();
        if (mBeanList.size() > 0) {
            // 初始显示3条数据
            if (mAllCount < 3) {
                for (int i = 0; i < mAllCount; i++) {
                    mFirstBeanList.add(mBeanList.get(i));
                    mCurrentCount++;
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    mFirstBeanList.add(mBeanList.get(i));
                    mCurrentCount++;
                }
            }
        }

        // 布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        // 垂直方向或水平
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        // 是否反转
        linearLayoutManager.setReverseLayout(false);
        // 布局管理器与RecyclerView绑定
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // 创建适配器
        mDisplayHallAdapter = new DisplayHallAdapter(mContext, mFirstBeanList, mScreenWidth);
        // RecyclerView绑定适配器
        mRecyclerView.setAdapter(mDisplayHallAdapter);
    }

    /**
     * 上拉,下拉监听
     */
    private void onRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (tagRefresh == 0) {
                            mLvLoadingView.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.INVISIBLE);
                            getData();
                            //tagRefresh++;
                        } else {
                            getData();
                            if (mBeanList != null && mBeanList.size() > 0) {
                                mDisplayHallAdapter.replaceAll(getInitialData());
                            }
                            refreshLayout.finishRefresh();
                        }
                    }
                }, 2000);
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentCount < mAllCount) {
                            mDisplayHallAdapter.addData(mDisplayHallAdapter.getItemCount(), getMoreData());
                            refreshLayout.finishLoadMore();
                        } else {
                            Toast toast = Toast.makeText(getContext(), "已全部加载!!!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 2000);
            }
        });
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()));
    }

    /**
     * 下拉刷新
     */
    private List<DisplayHallBean.DataBean> getInitialData() {
        // 刷新时首先将当前数据量清零
        mCurrentCount = 0;
        // 打乱顺序
        Collections.shuffle(mBeanList);
        List<DisplayHallBean.DataBean> list = new ArrayList<>();
        // 初始只显示3条数据, 每加载更多一次, 显示1条
        if (mAllCount < 3) {
            for (int i = 0; i < mAllCount; i++) {
                list.add(mBeanList.get(i));
                mCurrentCount++;
            }
        } else {
            for (int i = 0; i < 3; i++) {
                list.add(mBeanList.get(i));
                mCurrentCount++;
            }
        }
        return list;
    }

    /**
     * 上拉加载更多
     */
    private List<DisplayHallBean.DataBean> getMoreData() {
        List<DisplayHallBean.DataBean> list = new ArrayList<>();
        int laveCount = mAllCount - mCurrentCount;
        //每加载更多一次, 显示5条
        if (laveCount >= 6) {
            for (int i = 0; i < 6; i++) {
                list.add(mBeanList.get(mCurrentCount));
                mCurrentCount++;
            }
        } else {
            for (int i = 0; i < laveCount; i++) {
                list.add(mBeanList.get(mCurrentCount));
                mCurrentCount++;
            }
        }
        return list;
    }

    /**
     * 刷新UI操作
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == RESPONSE_SUCCESS) {
                tagRefresh++;
                mLvLoadingView.setVisibility(View.INVISIBLE);
                mLvNoResponse.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                showRecyclerView();
                mRefreshLayout.finishRefresh();
            } else if (msg.what == NO_RESPONSE) {
                mLvLoadingView.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                mLvNoResponse.setVisibility(View.VISIBLE);
                mRefreshLayout.finishRefresh();
            }
            return false;
        }
    });

    /**
     * 获取屏幕的宽高
     */
    private void getScreenSize() {
        Display defaultDisplay = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        mScreenWidth = point.x;
    }
}
