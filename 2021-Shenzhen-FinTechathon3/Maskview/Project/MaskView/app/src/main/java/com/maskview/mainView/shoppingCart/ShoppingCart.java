package com.maskview.mainView.shoppingCart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.maskview.R;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.confirmOrders.ConfirmOrders;
import com.maskview.mainView.shoppingCart.adapter.ShoppingCartAdapter;
import com.maskview.mainView.shoppingCart.callBack.OnViewChildClickListener;
import com.maskview.mainView.shoppingCart.callBack.OnViewChildDeleteClickListener;
import com.maskview.mainView.shoppingCart.callBack.OnViewGroupClickListener;
import com.maskview.mainView.shoppingCart.entity.ShoppingCartData;
import com.maskview.util.ToastUtil;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingCart extends Fragment implements View.OnClickListener {

    private ExpandableListView mExpandableListView;
    private TextView mTvSelectedCount;
    private TextView mTvSelectedMoney;
    private CheckBox mCbAllChecked;
    private ShoppingCartAdapter mShoppingCartAdapter;
    private ShoppingCartData mShoppingCartData;
    private LinearLayout mLvEmptyCart;
    private LinearLayout mLvNoResponse;
    private LinearLayout mLvDeleteMany;
    private LinearLayout mLvSelectedGoodsPrice;
    private TextView mTvEdit;
    private TextView mTvCancelEdit;
    private List<ShoppingCartData.DataBean> mSelectedData;
    private ArrayList<String> mSelectedDeleteImgName;
    private int mAllSelectedPrice;
    private ProgressBar mDeleteManyLoading;
    private Context mContext;
    private UserRequest mRequest;
    private final static int SUCCESS = 0;
    private final static int NO_DATA = 1;
    private final static int NO_RESPONSE = 2;
    private boolean isGetData = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        initView();
    }

    //每次进入fragment都刷新数据
    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            mLvSelectedGoodsPrice.setVisibility(View.VISIBLE);
            mTvEdit.setVisibility(View.VISIBLE);
            mLvDeleteMany.setVisibility(View.INVISIBLE);
            mTvCancelEdit.setVisibility(View.INVISIBLE);
            initData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    private void initView() {
        mExpandableListView = Objects.requireNonNull(getActivity()).findViewById(R.id.cart_expandableListView);
        mTvSelectedCount = getActivity().findViewById(R.id.cart_selectedCount);  //选中的总数量
        mTvSelectedMoney = getActivity().findViewById(R.id.cart_selectedMoney);  //选中的总价钱
        mCbAllChecked = getActivity().findViewById(R.id.cart_cb_allSelected);  //全选与反选
        mCbAllChecked.setOnClickListener(this);
        Button bt_submit = getActivity().findViewById(R.id.cart_bt_countMoney);  //确认购买-->跳转确认订单界面
        bt_submit.setOnClickListener(this);
        Button bt_clearCart = getActivity().findViewById(R.id.clear_shoppingCart); //清空购物车
        Button bt_deleteMany = getActivity().findViewById(R.id.deleteMany_cart);  //删除多个购物车信息
        bt_clearCart.setOnClickListener(this);
        bt_deleteMany.setOnClickListener(this);
        mLvNoResponse = getActivity().findViewById(R.id.layout_shoppingCart_noResponse);
        mLvEmptyCart = getActivity().findViewById(R.id.layout_empty_shoppingCart);
        mExpandableListView.setVisibility(View.INVISIBLE);
        mLvEmptyCart.setVisibility(View.INVISIBLE);
        mLvNoResponse.setVisibility(View.INVISIBLE);
        mExpandableListView.setGroupIndicator(null);
        mDeleteManyLoading = getActivity().findViewById(R.id.mDeleteManyLoading);
        mDeleteManyLoading.setVisibility(View.INVISIBLE);
        mLvDeleteMany = getActivity().findViewById(R.id.mLvDeleteMany);
        mLvDeleteMany.setVisibility(View.INVISIBLE);
        mLvSelectedGoodsPrice = getActivity().findViewById(R.id.mLvSelectedGoodsPrice);
        mTvEdit = getActivity().findViewById(R.id.mTvEdit_shoppingCart);
        mTvCancelEdit = getActivity().findViewById(R.id.mTvCancelEdit);
        mTvEdit.setOnClickListener(this);
        mTvCancelEdit.setOnClickListener(this);
        mTvCancelEdit.setVisibility(View.INVISIBLE);
        mRequest = new UserRequest();
    }

    // 获取购物车数据
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = mRequest.getShoppingCartInfo(UtilParameter.myToken);
                String tag;
                if (!result.equals("")) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(result);
                        tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
                            mShoppingCartData = JSON.parseObject(result, ShoppingCartData.class);
                            shoppingCartHandler.sendEmptyMessage(SUCCESS);
                        } else {
                            mShoppingCartData = null;
                            shoppingCartHandler.sendEmptyMessage(NO_DATA);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    shoppingCartHandler.sendEmptyMessage(NO_RESPONSE);
                }
            }
        }).start();
    }

    private Handler shoppingCartHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == SUCCESS) {
                mTvSelectedCount.setText("共0件");
                mTvSelectedMoney.setText("¥ 0");
                mLvSelectedGoodsPrice.setVisibility(View.VISIBLE);
                mTvEdit.setVisibility(View.VISIBLE);
                mTvCancelEdit.setVisibility(View.INVISIBLE);
                mLvDeleteMany.setVisibility(View.INVISIBLE);
                mDeleteManyLoading.setVisibility(View.INVISIBLE);
                mLvEmptyCart.setVisibility(View.INVISIBLE);
                mExpandableListView.setVisibility(View.VISIBLE);
                mLvNoResponse.setVisibility(View.INVISIBLE);
                showData();
            } else if (msg.what == NO_DATA) {
                mTvSelectedCount.setText("共0件");
                mTvSelectedMoney.setText("¥ 0");
                mLvSelectedGoodsPrice.setVisibility(View.VISIBLE);
                mTvEdit.setVisibility(View.VISIBLE);
                mTvCancelEdit.setVisibility(View.INVISIBLE);
                mLvDeleteMany.setVisibility(View.INVISIBLE);
                mDeleteManyLoading.setVisibility(View.INVISIBLE);
                mExpandableListView.setVisibility(View.INVISIBLE);
                mLvEmptyCart.setVisibility(View.VISIBLE);
                mLvNoResponse.setVisibility(View.INVISIBLE);
            } else if (msg.what == NO_RESPONSE) {
                mTvSelectedCount.setText("共0件");
                mTvSelectedMoney.setText("¥ 0");
                mLvSelectedGoodsPrice.setVisibility(View.VISIBLE);
                mTvEdit.setVisibility(View.VISIBLE);
                mTvCancelEdit.setVisibility(View.INVISIBLE);
                mLvDeleteMany.setVisibility(View.INVISIBLE);
                mDeleteManyLoading.setVisibility(View.INVISIBLE);
                mExpandableListView.setVisibility(View.INVISIBLE);
                mLvEmptyCart.setVisibility(View.INVISIBLE);
                mLvNoResponse.setVisibility(View.VISIBLE);
            }
            return false;
        }
    });

    private void showData() {
        if (mShoppingCartData != null && mShoppingCartData.getData().size() > 0) {
            mShoppingCartAdapter = null;
            showExpandData();
        } else {
            mShoppingCartAdapter.notifyDataSetChanged();
        }
    }

    private void showExpandData() {
        mShoppingCartAdapter = new ShoppingCartAdapter(mContext, mExpandableListView, mShoppingCartData.getData());
        mExpandableListView.setAdapter(mShoppingCartAdapter);
        final int groupCount = mExpandableListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            // 展开所有组
            mExpandableListView.expandGroup(i);
        }
        // group的checkbox选择(单组全选)
        mShoppingCartAdapter.setOnGroupClickListener(new OnViewGroupClickListener() {
            @Override
            public void onGroupCheckBoxClick(boolean isChecked, View view, int groupPosition) {
                mShoppingCartData.getData().get(groupPosition).setCheck(isChecked);
                int childLength = mShoppingCartData.getData().get(groupPosition).getGoodsInfo().size();
                for (int i = 0; i < childLength; i++) {
                    mShoppingCartData.getData().get(groupPosition).getGoodsInfo().get(i).setCheck(isChecked);
                }
                // 判断整个购物车是否全选
                int index = 0;
                for (int i = 0; i < mShoppingCartData.getData().size(); i++) {
                    if (mShoppingCartData.getData().get(i).isCheck()) {
                        index++;
                    }
                }
                if (index == mShoppingCartData.getData().size()) {
                    mCbAllChecked.setChecked(true);
                } else {
                    mCbAllChecked.setChecked(false);
                }
                mShoppingCartAdapter.notifyDataSetChanged();
                showCommodityCalculation();
            }
        });

        // child的checkbox选择(单个child选择)
        mShoppingCartAdapter.setOnChildClickListener(new OnViewChildClickListener() {
            @Override
            public void onChildCheckBoxClick(boolean isChecked, View view, int groupPosition, int childPosition) {
                mShoppingCartData.getData().get(groupPosition).getGoodsInfo().get(childPosition).setCheck(isChecked);
                int childLength = mShoppingCartData.getData().get(groupPosition).getGoodsInfo().size();
                for (int i = 0; i < childLength; i++) {
                    if (!mShoppingCartData.getData().get(groupPosition).getGoodsInfo().get(i).isCheck()) {
                        if (!isChecked) {
                            mShoppingCartData.getData().get(groupPosition).setCheck(false);
                        }
                        mShoppingCartAdapter.notifyDataSetChanged();
                        showCommodityCalculation();
                        return;
                    } else {
                        if (i == (childLength - 1)) {
                            mShoppingCartData.getData().get(groupPosition).setCheck(isChecked);
                            mShoppingCartAdapter.notifyDataSetChanged();
                        }
                    }
                }
                // 全选监听
                int index = 0;
                for (int i = 0; i < mShoppingCartData.getData().size(); i++) {
                    if (mShoppingCartData.getData().get(i).isCheck()) {
                        index++;
                    }
                }
                if (index == mShoppingCartData.getData().size()) {
                    mCbAllChecked.setChecked(true);
                } else {
                    mCbAllChecked.setChecked(false);
                }
                mShoppingCartAdapter.notifyDataSetChanged();
                showCommodityCalculation();
            }
        });
        // 删除某个child
        mShoppingCartAdapter.setOnChildDeleteClickListener(new OnViewChildDeleteClickListener() {
            @Override
            public void onChildDeleteClick(View view, int groupPosition, int childPosition) {
                //请求服务器删除数据,然后刷新UI
                deleteOneToShoppingCart(groupPosition, childPosition);
            }
        });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void showCommodityCalculation() {
        mAllSelectedPrice = 0;
        int allSelectedNum = 0;
        for (int i = 0; i < mShoppingCartData.getData().size(); i++) {
            for (int j = 0; j < mShoppingCartData.getData().get(i).getGoodsInfo().size(); j++) {
                if (mShoppingCartData.getData().get(i).getGoodsInfo().get(j).isCheck()) {
                    mAllSelectedPrice += Double.parseDouble(mShoppingCartData.getData().get(i).getGoodsInfo().get(j).getImgPrice());
                    allSelectedNum++;
                }
            }
        }
        if (mAllSelectedPrice == 0) {
            mTvSelectedCount.setText("共0件");
            mTvSelectedMoney.setText("¥ 0");
            return;
        }
        mTvSelectedCount.setText("共" + allSelectedNum + "件");
        mTvSelectedMoney.setText("¥ " + mAllSelectedPrice);
    }

    /**
     * 获取选中的所有item,传给确认订单界面
     */
    private void getSelectedData() {
        mSelectedData = new ArrayList<>();
        mSelectedDeleteImgName = new ArrayList<>(); // 选中的删除购物车的图片名
        ShoppingCartData.DataBean dataBean;
        String selectedSellerName;
        List<ShoppingCartData.DataBean.GoodsInfoBean> goodsInfoBeans;
        for (int i = 0; i < mShoppingCartData.getData().size(); i++) {
            dataBean = new ShoppingCartData.DataBean();
            selectedSellerName = mShoppingCartData.getData().get(i).getSellerName();
            goodsInfoBeans = new ArrayList<>();
            for (int j = 0; j < mShoppingCartData.getData().get(i).getGoodsInfo().size(); j++) {
                if (mShoppingCartData.getData().get(i).getGoodsInfo().get(j).isCheck()) {
                    goodsInfoBeans.add(mShoppingCartData.getData().get(i).getGoodsInfo().get(j));
                    mSelectedDeleteImgName.add(mShoppingCartData.getData().get(i).getGoodsInfo().get(j).getImgPath());
                }
            }
            if (goodsInfoBeans.size() > 0) {
                dataBean.setSellerName(selectedSellerName);
                dataBean.setGoodsInfo(goodsInfoBeans);
                mSelectedData.add(dataBean);
            }
        }
    }

    // 全选与反选
    private void selectedAll() {
        if (mShoppingCartData != null) {
            int length = mShoppingCartData.getData().size();
            for (int i = 0; i < length; i++) {
                mShoppingCartData.getData().get(i).setCheck(mCbAllChecked.isChecked());
                List<ShoppingCartData.DataBean.GoodsInfoBean> groups = mShoppingCartData.getData().get(i).getGoodsInfo();
                for (int j = 0; j < groups.size(); j++) {
                    groups.get(j).setCheck(mCbAllChecked.isChecked());
                }
            }
            mShoppingCartAdapter.notifyDataSetChanged();
            showCommodityCalculation();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_cb_allSelected:
                selectedAll();
                break;
            case R.id.cart_bt_countMoney:
                jumpConfirmOrder();
                break;
            case R.id.mTvEdit_shoppingCart:
                //批量删除购物车或清空购物车控件显隐
                clearOrDeleteManyCart();
                break;
            case R.id.mTvCancelEdit:
                //取消批量删除控件显隐
                exitDeleteMany();
                break;
            case R.id.deleteMany_cart:
                deleteSelected();
                break;
            case R.id.clear_shoppingCart:
                clearShoppingCart();
                break;
        }
    }

    //批量删除购物车或清空购物车控件显隐
    private void clearOrDeleteManyCart() {
        mLvSelectedGoodsPrice.setVisibility(View.INVISIBLE);
        mLvDeleteMany.setVisibility(View.VISIBLE);
        mTvEdit.setVisibility(View.INVISIBLE);
        mTvCancelEdit.setVisibility(View.VISIBLE);
    }

    //取消批量删除控件显隐
    private void exitDeleteMany() {
        mLvSelectedGoodsPrice.setVisibility(View.VISIBLE);
        mLvDeleteMany.setVisibility(View.INVISIBLE);
        mTvEdit.setVisibility(View.VISIBLE);
        mTvCancelEdit.setVisibility(View.INVISIBLE);
    }

    //删除购物车中选中的
    private void deleteSelected() {
        if (mShoppingCartData != null && mShoppingCartData.getData().size() > 0) {
            getSelectedData();
            if (mSelectedData != null && mSelectedData.size() > 0) {
                mDeleteManyLoading.setVisibility(View.VISIBLE);
                ExecutorService es = Executors.newCachedThreadPool();
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mSelectedDeleteImgName.size(); i++) {
                            mRequest.deleteOneShoppingCartItem(mSelectedDeleteImgName.get(i), UtilParameter.myToken);
                        }
                    }
                };
                es.submit(task);
                es.shutdown();
                while (true) {
                    if (es.isTerminated()) {
                        initData();
                        break;
                    }
                }
            } else {
                ToastUtil.showToast(mContext, "还没有选择商品哦!", 0, 0);
                /*Toast toast = Toast.makeText(mContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();*/
            }
        } else {
            ToastUtil.showToast(mContext, "还没有选择商品哦!", 0, 0);
            /*Toast toast = Toast.makeText(mContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();*/
        }
    }

    //清空购物车
    private void clearShoppingCart() {
        if (mShoppingCartData != null && mShoppingCartData.getData().size() > 0) {
            AlertDialog finishAlertDialog = new AlertDialog.Builder(mContext)
                    .setMessage("确定要清空购物车吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ExecutorService es = Executors.newCachedThreadPool();
                            Runnable task = new Runnable() {
                                @Override
                                public void run() {
                                    mRequest.clearShoppingCart(UtilParameter.myToken);
                                }
                            };
                            es.submit(task);
                            es.shutdown();
                            while (true) {
                                if (es.isTerminated()) {
                                    initData();
                                    break;
                                }
                            }
                        }
                    }).setNegativeButton("再考虑考虑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
            finishAlertDialog.show();
            finishAlertDialog.setCanceledOnTouchOutside(false);
        } else {
            ToastUtil.showToast(mContext, "购物车是空的哦!", 0, 0);
            /*Toast toast = Toast.makeText(mContext, "购物车是空的哦!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();*/
        }
    }

    /**
     * 删除购物车某条数据
     */
    private void deleteOneToShoppingCart(final int groupPosition, final int childPosition) {
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String imgPath = mShoppingCartData.getData().get(groupPosition).getGoodsInfo().get(childPosition).getImgPath();
                String result = mRequest.deleteOneShoppingCartItem(imgPath, UtilParameter.myToken);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String tag = jsonObject.get("result") + "";
                    if (tag.equals("true")) {
                        Log.e("--------", "run: 删除成功");
                    } else if (tag.equals("false")) {
                        Log.e("--------", "run: 删除失败");
                    } else {
                        Log.e("--------", "run: 服务器未响应");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        es.submit(task);
        es.shutdown();

        while (true) {
            if (es.isTerminated()) {
                initData();
                break;
            }
        }
    }

    /**
     * 点击结算,跳转确认订单
     */
    private void jumpConfirmOrder() {
        if (mShoppingCartData != null && mShoppingCartData.getData().size() > 0) {
            getSelectedData();
            if (mSelectedData != null && mSelectedData.size() > 0) {
                Intent intent = new Intent(mContext, ConfirmOrders.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selected_shopping_goods", (Serializable) mSelectedData);
                bundle.putInt("mAllSelectedPrice", mAllSelectedPrice);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                ToastUtil.showToast(mContext, "还没有选择商品哦!", 0, 0);
                /*Toast toast = Toast.makeText(mContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();*/
            }
        } else {
            ToastUtil.showToast(mContext, "还没有选择商品哦!", 0, 0);
            /*Toast toast = Toast.makeText(mContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();*/
        }
    }
}
