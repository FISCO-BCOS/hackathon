package com.maskview.mainView.mgGoods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.maskview.R;
import com.maskview.dao.AvertTwoTouch;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.mgGoods.adapter.MyConfirmAdapterHighAPI;
import com.maskview.mainView.mgGoods.adapter.MyConfirmAdapterLowAPI;
import com.maskview.mainView.mgGoods.adapter.MySellAdapter;
import com.maskview.mainView.mgGoods.bigImg.ShowBigConfirmImg;
import com.maskview.mainView.putOnSale.PutOnSale;
import com.maskview.mainView.resetSellPriceTopic.ResetSellImgPrice;
import com.maskview.mainView.setSellImgInfo.SetSellImgInfo;
import com.maskview.util.API29ImgBean;
import com.maskview.util.GetSDPath;
import com.maskview.util.UtilParameter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyGoods extends Fragment implements View.OnClickListener {

    private RelativeLayout layout_myConfirm;
    private RelativeLayout layout_mySell;
    private LinearLayout layout_noConfirmImg;
    private LinearLayout layout_noSellImg;
    private LinearLayout layout_noResponse;
    private RefreshLayout refreshLayout_myConfirm;
    private RefreshLayout refreshLayout_mySell;
    private LinearLayout mLvMyConfirmLongClick;
    private LinearLayout mLvMySellLongClick;
    private int confirmRefreshTag = 0;
    private int sellRefreshTag = 0;
    private int mSellClickCount = 0;
    private int mConfirmClickCount = 0;
    private RecyclerView recyclerView_myConfirm;
    private RecyclerView recyclerView_mySell;
    private TextView text1;
    private TextView text2;
    private View view1;
    private View view2;
    private Context mContext;
    private AlertDialog underImgWaitingDialog;  //下架图片等待提示框
    private final static int UNDER_WAITING = 0;  //下架等待标志
    private final static int UNDER_NO_RESPONSE = 1;  //下架时服务器未响应标志
    private int underCount = 0;
    private final static int NO_SELL_IMAGE = 2; //我的上架没有图片标志
    private final static int SHOW_SELL_NO_RESPONSE = 3; //显示上架图片时,服务器未响应标志
    private File[] imgFiles;
    private ArrayList<String> myConfirmImgPath;
    private MyConfirmAdapterHighAPI myConfirmAdapterHighAPI;
    private MyConfirmAdapterLowAPI myConfirmAdapterLowAPI;
    private MySellAdapter mySellAdapter;
    private boolean isShowChecked;
    private UserRequest ur;
    private ArrayList<String> mySellImgList;
    private ArrayList<String> selectedUnderImg;
    private List<API29ImgBean> mConfirmImgInfoList;
    private List<API29ImgBean> mSelectedConfirmImgInfoList;
    private ArrayList<String> mSelectedConfirmImgPath;
    private int screenWidth;
    private int screenHeight;
    // activity的RadioButton
    private RadioButton radioButton_displayHall;
    private RadioButton radioButton_myGoods;
    private RadioButton radioButton_shoppingCart;
    private RadioButton radioButton_mine;
    private ImageView bt_confirmImg;
    private int mAlreadyConfirmCount;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_goods, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        confirmRecyclerRefresh();
        refreshLayout_myConfirm.autoRefresh();
    }

    private void initView() {
        mContext = getActivity();
        layout_myConfirm = Objects.requireNonNull(getActivity()).findViewById(R.id.layout_myGoods_myConfirm);
        layout_mySell = getActivity().findViewById(R.id.layout_myGoods_mySell);
        radioButton_displayHall = getActivity().findViewById(R.id.radioBt_displayHall);
        radioButton_myGoods = getActivity().findViewById(R.id.radioBt_mgGoods);
        radioButton_shoppingCart = getActivity().findViewById(R.id.radioBt_shoppingCart);
        radioButton_mine = getActivity().findViewById(R.id.radioBt_mine);
        bt_confirmImg = getActivity().findViewById(R.id.bt_ConfirmImg);
        Button bt_closeSellLongClick = getActivity().findViewById(R.id.bt_myGoods_closeSellLongClick);
        Button bt_closeUnderLongClick = getActivity().findViewById(R.id.bt_myGoods_closeUnderLongClick);
        layout_noConfirmImg = getActivity().findViewById(R.id.layout_myGoods_noConfirmImg);
        layout_noSellImg = getActivity().findViewById(R.id.layout_myGoods_noSellImg);
        layout_noResponse = getActivity().findViewById(R.id.layout_myGoods_showSellImg_noResponse);
        layout_noConfirmImg.setVisibility(View.INVISIBLE);
        layout_noSellImg.setVisibility(View.INVISIBLE);
        layout_noResponse.setVisibility(View.INVISIBLE);
        mLvMyConfirmLongClick = getActivity().findViewById(R.id.layout_myGoods_myConfirm_longClick);
        mLvMySellLongClick = getActivity().findViewById(R.id.layout_myGoods_mySell_longClick);
        mLvMyConfirmLongClick.setVisibility(View.INVISIBLE);
        mLvMySellLongClick.setVisibility(View.INVISIBLE);
        refreshLayout_myConfirm = getActivity().findViewById(R.id.smartRefresh_myGoods_myConfirm);
        refreshLayout_mySell = getActivity().findViewById(R.id.smartRefresh_myGoods_mySell);
        recyclerView_myConfirm = getActivity().findViewById(R.id.recycler_myGoods_myConfirm);
        recyclerView_mySell = getActivity().findViewById(R.id.recycler_myGoods_mySell);
        Button bt_setImgInfo = getActivity().findViewById(R.id.bt_myGoods_setInfo);
        Button bt_underImg = getActivity().findViewById(R.id.bt_myGoods_under);
        bt_setImgInfo.setOnClickListener(this);
        bt_underImg.setOnClickListener(this);
        bt_setImgInfo.getBackground().setAlpha(180);
        bt_underImg.getBackground().setAlpha(180);
        bt_closeSellLongClick.setOnClickListener(this);
        bt_closeUnderLongClick.setOnClickListener(this);
        bt_closeSellLongClick.getBackground().setAlpha(180);
        bt_closeUnderLongClick.getBackground().setAlpha(180);
        text1 = getActivity().findViewById(R.id.text_1);
        text2 = getActivity().findViewById(R.id.text_2);
        view1 = getActivity().findViewById(R.id.view_1);
        view2 = getActivity().findViewById(R.id.view_2);
        LinearLayout layout_myGoods_text_myConfirm = getActivity().findViewById(R.id.layout_myGoods_text_myConfirm);
        LinearLayout layout_myGoods_text_mySell = getActivity().findViewById(R.id.layout_myGoods_text_mySell);
        layout_mySell.setVisibility(View.INVISIBLE);
        ur = new UserRequest();
        mSelectedConfirmImgInfoList = new ArrayList<>();
        mSelectedConfirmImgPath = new ArrayList<>();
        layout_myGoods_text_myConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setTextColor(Color.parseColor("#2196f3"));
                view1.setBackgroundColor(Color.parseColor("#2196f3"));
                layout_noConfirmImg.setVisibility(View.INVISIBLE);
                layout_noSellImg.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.VISIBLE);
                text2.setTextColor(Color.BLACK);
                layout_myConfirm.setVisibility(View.VISIBLE);
                layout_mySell.setVisibility(View.INVISIBLE);
                mLvMyConfirmLongClick.setVisibility(View.INVISIBLE);
                mLvMySellLongClick.setVisibility(View.INVISIBLE);
                refreshLayout_myConfirm.setEnableRefresh(true);
                isCheckedRadioButton(true);
                isShowChecked = false;
                /*if (mConfirmImgInfoList == null || mConfirmImgInfoList.size() == 0) {
                    layout_noConfirmImg.setVisibility(View.VISIBLE);
                } */
                Log.e("TAG", "mAlreadyConfirmCount: " + mAlreadyConfirmCount);
                if (mAlreadyConfirmCount == 0) {
                    layout_noConfirmImg.setVisibility(View.VISIBLE);
                } else {
                    layout_noConfirmImg.setVisibility(View.INVISIBLE);
                }
                if (Build.VERSION.SDK_INT >= 29) {
                    if (myConfirmAdapterHighAPI != null) {
                        myConfirmAdapterHighAPI.setShowCheckBox(false);
                    }
                } else {
                    if (myConfirmAdapterLowAPI != null) {
                        myConfirmAdapterLowAPI.setShowCheckBox(false);
                    }
                }
                refreshConfirmUI();
            }
        });

        layout_myGoods_text_mySell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setTextColor(Color.parseColor("#2196f3"));
                view2.setBackgroundColor(Color.parseColor("#2196f3"));
                layout_noConfirmImg.setVisibility(View.INVISIBLE);
                layout_noSellImg.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                text1.setTextColor(Color.BLACK);
                layout_myConfirm.setVisibility(View.INVISIBLE);
                layout_mySell.setVisibility(View.VISIBLE);
                mLvMyConfirmLongClick.setVisibility(View.INVISIBLE);
                mLvMySellLongClick.setVisibility(View.INVISIBLE);

                refreshLayout_mySell.setEnableRefresh(true);
                isCheckedRadioButton(true);
                if (mSellClickCount == 0) {
                    mySellRecyclerRefresh();
                    refreshLayout_mySell.autoRefresh();
                    mSellClickCount++;
                } else {
                    isShowChecked = false;
                    mySellAdapter.setShowCheckBox(false);
                    refreshMySellUI();
                }
            }
        });
    }

    // 长按时其他控件不可点击
    private void isCheckedRadioButton(boolean isOrNo) {
        radioButton_displayHall.setEnabled(isOrNo);
        radioButton_myGoods.setEnabled(isOrNo);
        radioButton_shoppingCart.setEnabled(isOrNo);
        radioButton_mine.setEnabled(isOrNo);
        bt_confirmImg.setEnabled(isOrNo);
    }

    // 本机获取我的确权照片
    private boolean getMyConfirmData() {
        mConfirmImgInfoList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 29) {
            // 本机我的确权图片模糊查询字段
            String confirmKey = "con-" + UtilParameter.myPhoneNumber + "-";
            // Android10以上(包括10)
            mConfirmImgInfoList = GetSDPath.getAPI29ImgInfo(mContext, confirmKey);
            if (mConfirmImgInfoList.size() > 0) {
                mAlreadyConfirmCount = mConfirmImgInfoList.size();
                Log.e("TAG", "mAlreadyConfirmCount: " + mAlreadyConfirmCount);
                layout_noConfirmImg.setVisibility(View.INVISIBLE);
                getScreenSize();
                showConfirmImg();
                refreshLayout_myConfirm.finishRefresh();
                return true;
            } else {
                mAlreadyConfirmCount = 0;
                layout_noConfirmImg.setVisibility(View.VISIBLE);
                refreshLayout_myConfirm.finishRefresh();
                return false;
            }
        } else {
            // Android10以下
            String myConfirmPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MaskView确权";
            File file = new File(myConfirmPath);
            if (file.exists()) {
                // 路径存在,获取该路径下所有图片
                imgFiles = getConfirmImages(myConfirmPath);
                if (imgFiles == null || imgFiles.length == 0) {
                    // 没有图片提示
                    mAlreadyConfirmCount = 0;
                    layout_noConfirmImg.setVisibility(View.VISIBLE);
                    refreshLayout_myConfirm.finishRefresh();
                    return false;
                } else {
                    // 文件夹内有照片,获取登陆者确权照片的Bitmap
                    reversToPath();
                    if (myConfirmImgPath.size() > 0) {
                        // 文件夹内有登陆者确权的图片
                        mAlreadyConfirmCount = myConfirmImgPath.size();
                        layout_noConfirmImg.setVisibility(View.INVISIBLE);
                        getScreenSize();
                        showConfirmImg();
                        refreshLayout_myConfirm.finishRefresh();
                        return true;
                    } else {
                        // 没有照片提示
                        mAlreadyConfirmCount = 0;
                        layout_noConfirmImg.setVisibility(View.VISIBLE);
                        refreshLayout_myConfirm.finishRefresh();
                        return false;
                    }
                }
            } else {
                // 没有照片提示
                mAlreadyConfirmCount = 0;
                layout_noConfirmImg.setVisibility(View.VISIBLE);
                refreshLayout_myConfirm.finishRefresh();
                return false;
            }
        }
    }

    // 服务器获取我的上架图片集合
    private void getMySellData() {
        mySellImgList = new ArrayList<>();
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = ur.getMySellImgList(UtilParameter.myToken);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
                            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String imgPath = UtilParameter.IMAGES_IP + jsonArray.get(i);
                                mySellImgList.add(imgPath);
                            }
                            Log.e("--------", jsonArray.length() + "张图片");
                        } else {
                            //刷新UI,没有上架的图片,请在我的确权中选择图片上架
                            noSellImgHandler.sendEmptyMessage(NO_SELL_IMAGE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    noSellImgHandler.sendEmptyMessage(SHOW_SELL_NO_RESPONSE);
                }
            }
        };
        es.submit(task);
        es.shutdown();
        while (true) {
            if (es.isTerminated()) {
                getScreenSize();
                showMySellImg();
                refreshLayout_mySell.finishRefresh();
                break;
            }
        }
    }

    private void showConfirmImg() {
        recyclerView_myConfirm.setVisibility(View.VISIBLE);
        recyclerView_myConfirm.setHasFixedSize(true);
        recyclerView_myConfirm.setItemAnimator(null);
        //GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        final int spanCount = 2;
        recyclerView_myConfirm.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int[] first = new int[spanCount];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });
        recyclerView_myConfirm.setLayoutManager(layoutManager);
        refreshConfirmUI();
    }

    private void showMySellImg() {
        recyclerView_mySell.setVisibility(View.VISIBLE);
        recyclerView_mySell.setHasFixedSize(true);
        //recyclerView_mySell.setItemAnimator(null);
        //GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 防止item切换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView_mySell.setLayoutManager(layoutManager);
        final int spanCount = 2;
        recyclerView_mySell.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int[] first = new int[spanCount];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });
        selectedUnderImg = new ArrayList<>();
        refreshMySellUI();
    }

    private void refreshConfirmUI() {
        if (Build.VERSION.SDK_INT >= 29) {
            if (myConfirmAdapterHighAPI == null) {
                myConfirmAdapterHighAPI = new MyConfirmAdapterHighAPI(mContext, mConfirmImgInfoList, screenWidth, screenHeight);
                recyclerView_myConfirm.setAdapter(myConfirmAdapterHighAPI);
            } else {
                myConfirmAdapterHighAPI.notifyDataSetChanged();
            }
        } else {
            if (myConfirmAdapterLowAPI == null) {
                myConfirmAdapterLowAPI = new MyConfirmAdapterLowAPI(mContext, myConfirmImgPath, screenWidth, screenHeight);
                recyclerView_myConfirm.setAdapter(myConfirmAdapterLowAPI);
            } else {
                myConfirmAdapterLowAPI.notifyDataSetChanged();
            }
        }
    }

    private void refreshMySellUI() {
        if (mySellAdapter == null) {
            mySellAdapter = new MySellAdapter(mContext, mySellImgList, screenWidth, screenHeight);
            recyclerView_mySell.setAdapter(mySellAdapter);
        } else {
            mySellAdapter.notifyDataSetChanged();
        }
    }

    // 确权浏览点击监听
    private void initConfirmListener() {
        // adapter中定义的监听事件　可以根据isShowCheck判断当前状态
        if (Build.VERSION.SDK_INT >= 29) {
            // Android10及以上
            myConfirmAdapterHighAPI.setOnItemClickListener(new MyConfirmAdapterHighAPI.onItemClickListener() {
                @Override
                public void onClick(View view, API29ImgBean bean) {
                    if (isShowChecked) {
                        if (mSelectedConfirmImgInfoList.contains(bean)) {
                            mSelectedConfirmImgInfoList.remove(bean);
                        } else {
                            mSelectedConfirmImgInfoList.add(bean);
                        }
                    } else {
                        // 点击我的确权图片--放大图片展示
                        Intent intent = new Intent(mContext, ShowBigConfirmImg.class);
                        intent.putExtra("imgUri", bean.getImgUri() + "");
                        startActivity(intent);
                        /*Date date = new Date(bean.getImgDate() * 1000L);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        Log.e("------------", "onClick: 放大图片操作:" + format.format(date));*/
                    }
                }

                @Override
                public boolean onLongClick(View view, API29ImgBean bean) {
                    if (!isShowChecked) {
                        mLvMyConfirmLongClick.setVisibility(View.VISIBLE);
                        refreshLayout_myConfirm.setEnableRefresh(false);
                        isCheckedRadioButton(false);
                        myConfirmAdapterHighAPI.setShowCheckBox(true);
                        refreshConfirmUI();
                    }
                    isShowChecked = !isShowChecked;
                    return false;
                }
            });
        } else {
            // Android10以下
            myConfirmAdapterLowAPI.setOnItemClickListener(new MyConfirmAdapterLowAPI.onItemClickListener() {
                @Override
                public void onClick(View view, String imgPath) {
                    if (isShowChecked) {
                        if (mSelectedConfirmImgPath.contains(imgPath)) {
                            mSelectedConfirmImgPath.remove(imgPath);
                        } else {
                            mSelectedConfirmImgPath.add(imgPath);
                        }
                    } else {
                        // 点击我的确权图片--放大图片展示
                        Log.e("------------", "onClick: 放大图片操作");
                    }
                }

                @Override
                public boolean onLongClick(View view, int pos) {
                    if (!isShowChecked) {
                        mLvMyConfirmLongClick.setVisibility(View.VISIBLE);
                        refreshLayout_myConfirm.setEnableRefresh(false);
                        isCheckedRadioButton(false);
                        myConfirmAdapterLowAPI.setShowCheckBox(true);
                        refreshConfirmUI();
                    }
                    isShowChecked = !isShowChecked;
                    return false;
                }
            });
        }
    }

    //我的上架浏览点击监听
    private void initMySellListener() {
        //adapter中定义的监听事件　可以根据isShowCheck判断当前状态
        mySellAdapter.setOnItemClickListener(new MySellAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, String imgUrl) {
                if (isShowChecked) {
                    if (selectedUnderImg.contains(imgUrl)) {
                        selectedUnderImg.remove(imgUrl);
                    } else {
                        selectedUnderImg.add(imgUrl);
                    }
                } else {
                    // 我的上架点击某张图片,进入编辑页面,可修改价格和主题
                    if (AvertTwoTouch.isFastClick()) {
                        Intent intent = new Intent(getActivity(), ResetSellImgPrice.class);
                        intent.putExtra("selectedResetPriceImgUrl", imgUrl);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public boolean onLongClick(View view, int pos) {
                if (!isShowChecked) {
                    isCheckedRadioButton(false);
                    mLvMySellLongClick.setVisibility(View.VISIBLE);
                    refreshLayout_mySell.setEnableRefresh(false);
                    mySellAdapter.setShowCheckBox(true);
                    refreshMySellUI();
                }
                isShowChecked = !isShowChecked;
                return false;
            }
        });
    }

    //确权浏览上拉刷新
    private void confirmRecyclerRefresh() {
        refreshLayout_myConfirm.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= 29) {
                            if (confirmRefreshTag == 0) {
                                recyclerView_myConfirm.setVisibility(View.INVISIBLE);
                                if (getMyConfirmData()) {
                                    confirmRefreshTag++;
                                    initConfirmListener();
                                }
                            } else {
                                if (getMyConfirmData()) {
                                    myConfirmAdapterHighAPI.replaceAll(mConfirmImgInfoList);
                                    refreshLayout.finishRefresh();
                                    initConfirmListener();
                                }
                            }
                        } else {
                            if (confirmRefreshTag == 0) {
                                recyclerView_myConfirm.setVisibility(View.INVISIBLE);
                                if (getMyConfirmData()) {
                                    confirmRefreshTag++;
                                    initConfirmListener();
                                }
                            } else {
                                if (getMyConfirmData()) {
                                    myConfirmAdapterLowAPI.replaceAll(myConfirmImgPath);
                                    refreshLayout.finishRefresh();
                                    initConfirmListener();
                                }
                            }
                        }
                    }
                }, 500);
            }
        });
    }

    //我的上架浏览上拉刷新
    private void mySellRecyclerRefresh() {
        refreshLayout_mySell.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (sellRefreshTag == 0) {
                            recyclerView_mySell.setVisibility(View.INVISIBLE);
                            getMySellData();
                            sellRefreshTag++;
                            initMySellListener();
                        } else {
                            getMySellData();
                            mySellAdapter.replaceAll(mySellImgList);
                            refreshLayout.finishRefresh();
                            initMySellListener();
                        }
                    }
                }, 500);
            }
        });
    }

    // 获取指定路径下的所有图片
    private File[] getConfirmImages(String folderPath) {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            // 过滤器
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

    // 筛选登录者确权的照片,并转换为Path
    private void reversToPath() {
        ArrayList<String> allImgPath = new ArrayList<>();
        myConfirmImgPath = new ArrayList<>();
        for (File imgFile : imgFiles) {
            allImgPath.add(imgFile.toString());
        }
        // 遍历筛选自己的确权图片
        String imgName;
        for (int i = 0; i < allImgPath.size(); i++) {
            imgName = allImgPath.get(i).substring(allImgPath.get(i).lastIndexOf("/") + 1);
            if (imgName.startsWith("con-" + UtilParameter.myPhoneNumber)) {
                myConfirmImgPath.add(allImgPath.get(i));
            }
        }
    }

    // 获取屏幕的宽高
    private void getScreenSize() {
        // 获取手机屏幕的长宽
        Display defaultDisplay = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_myGoods_setInfo:
                setSelectedImgPrice();
                break;
            case R.id.bt_myGoods_under:
                isOrNoUnderAlert();
                break;
            case R.id.bt_myGoods_closeSellLongClick:
                // 取消上架长按效果
                cancelSell();
                break;
            case R.id.bt_myGoods_closeUnderLongClick:
                // 取消下架长按效果
                cancelUnder();
                break;
        }
    }

    // 取消上架长按效果
    private void cancelSell() {
        isCheckedRadioButton(true);
        if (Build.VERSION.SDK_INT >= 29) {
            myConfirmAdapterHighAPI.setShowCheckBox(false);
            refreshConfirmUI();
            mSelectedConfirmImgInfoList.clear();
        } else {
            myConfirmAdapterLowAPI.setShowCheckBox(false);
            refreshConfirmUI();
            mSelectedConfirmImgPath.clear();
        }
        isShowChecked = !isShowChecked;
        refreshLayout_myConfirm.setEnableRefresh(true);
        mLvMyConfirmLongClick.setVisibility(View.INVISIBLE);
    }

    // 取消下架长按效果
    private void cancelUnder() {
        isCheckedRadioButton(true);
        mySellAdapter.setShowCheckBox(false);
        refreshMySellUI();
        selectedUnderImg.clear();
        isShowChecked = !isShowChecked;
        refreshLayout_mySell.setEnableRefresh(true);
        mLvMySellLongClick.setVisibility(View.INVISIBLE);
    }

    // 设置选中图片的价格和主题
    private void setSelectedImgPrice() {
        if (mSelectedConfirmImgInfoList.size() == 0 && mSelectedConfirmImgPath.size() == 0) {
            Toast.makeText(mContext, "请选择要上架的图片", Toast.LENGTH_SHORT).show();
        } else if (mSelectedConfirmImgInfoList.size() > 6 || mSelectedConfirmImgPath.size() > 6) {
            Toast.makeText(mContext, "每次最多可上架6张图片", Toast.LENGTH_SHORT).show();
        } else {
            //Intent intent = new Intent(getActivity(), SetSellImgInfo.class);
            Intent intent = new Intent(getActivity(), PutOnSale.class);
            if (Build.VERSION.SDK_INT >= 29) {
                intent.putParcelableArrayListExtra("selectedImgBeanList", (ArrayList<? extends Parcelable>) mSelectedConfirmImgInfoList);
                myConfirmAdapterHighAPI.setShowCheckBox(false);
                isShowChecked = false;
                isCheckedRadioButton(true);
                mLvMyConfirmLongClick.setVisibility(View.INVISIBLE);
                refreshLayout_myConfirm.setEnableRefresh(true);
                refreshConfirmUI();
                startActivity(intent);
                mSelectedConfirmImgInfoList.clear();
            } else {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("selectedSellImgPathList", mSelectedConfirmImgPath);
                intent.putExtras(bundle);
                myConfirmAdapterLowAPI.setShowCheckBox(false);
                isShowChecked = false;
                isCheckedRadioButton(true);
                mLvMyConfirmLongClick.setVisibility(View.INVISIBLE);
                refreshLayout_myConfirm.setEnableRefresh(true);
                refreshConfirmUI();
                startActivity(intent);
                mSelectedConfirmImgPath.clear();
            }
        }
    }

    //下架选中的图片
    private void underSelectedImg() {
        loadingAlert();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                String imgName;
                for (int i = 0; i < selectedUnderImg.size(); i++) {
                    imgName = selectedUnderImg.get(i).substring(selectedUnderImg.get(i).indexOf("con-"));
                    result = ur.underSellImg(imgName, UtilParameter.myToken);
                    if (!result.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String tag = jsonObject.get("result") + "";
                            if (tag.equals("true")) {
                                underCount++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        waitingOrNoResponseHandler.sendEmptyMessage(UNDER_NO_RESPONSE);
                        return;
                    }
                }
            }
        }).start();
    }

    // 确认下架提示
    private void isOrNoUnderAlert() {
        if (selectedUnderImg == null || selectedUnderImg.size() == 0) {
            Toast.makeText(mContext, "请选择要下架的图片", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog isUnderDialog = new AlertDialog.Builder(mContext).
                setMessage("确认下架" + selectedUnderImg.size() + "张图?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        underCount = 0;
                        mySellAdapter.setShowCheckBox(false);
                        isShowChecked = false;
                        mLvMySellLongClick.setVisibility(View.INVISIBLE);
                        refreshMySellUI();
                        //请求服务器,删除选中的图片
                        underSelectedImg();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        isUnderDialog.show();
        isUnderDialog.setCanceledOnTouchOutside(false);
    }

    // 显示下架进度提示
    private void loadingAlert() {
        View view = View.inflate(mContext, R.layout.alert_sell_waiting, null);
        underImgWaitingDialog = new AlertDialog.Builder(mContext).setView(view).create();
        underImgWaitingDialog.setTitle("正在下架处理,请耐心等待......");
        underImgWaitingDialog.show();
        underImgWaitingDialog.setCanceledOnTouchOutside(false);
        waitingOrNoResponseHandler.sendEmptyMessage(UNDER_WAITING);
    }

    // 显示下架进度或未响应提示
    private Handler waitingOrNoResponseHandler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == UNDER_WAITING) {
                TextView tv_now = underImgWaitingDialog.findViewById(R.id.loading_nowCount);
                TextView tv_all = underImgWaitingDialog.findViewById(R.id.loading_allCount);
                if (tv_now != null && tv_all != null) {
                    tv_now.setText(underCount + "");
                    tv_all.setText("/" + selectedUnderImg.size());
                }
                if (underImgWaitingDialog.isShowing()) {
                    waitingOrNoResponseHandler.sendEmptyMessageDelayed(UNDER_WAITING, 1000);
                    if (underCount == selectedUnderImg.size()) {
                        underImgWaitingDialog.cancel();
                        finishAlert();
                    }
                }
            } else if (msg.what == UNDER_NO_RESPONSE) {
                underImgWaitingDialog.cancel();
                AlertDialog noResponseAlertDialog = new AlertDialog.Builder(mContext).setMessage("服务器未响应,请稍后重试")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
                noResponseAlertDialog.show();
            }
            return false;
        }
    });

    // 下架完成后提示
    private void finishAlert() {
        AlertDialog finishAlertDialog = new AlertDialog.Builder(mContext).setMessage("下架完成")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        isCheckedRadioButton(true);
                        selectedUnderImg.clear();
                        refreshLayout_mySell.setEnableRefresh(true);
                        refreshLayout_mySell.autoRefresh();
                    }
                }).create();
        finishAlertDialog.show();
        finishAlertDialog.setCanceledOnTouchOutside(false);
    }

    //我的上架图片为空,服务器未响应提示
    private Handler noSellImgHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == NO_SELL_IMAGE) {
                layout_noSellImg.setVisibility(View.VISIBLE);
                layout_noConfirmImg.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.INVISIBLE);
            } else if (msg.what == SHOW_SELL_NO_RESPONSE) {
                layout_noResponse.setVisibility(View.VISIBLE);
                layout_noConfirmImg.setVisibility(View.INVISIBLE);
                layout_noSellImg.setVisibility(View.INVISIBLE);
            }
            return false;
        }
    });
}
