package com.maskview.mainView.herAllFansInfo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maskview.R;
import com.maskview.dao.AvertTwoTouch;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.herAllFansInfo.bean.HerAllFansInfoBean;
import com.maskview.mainView.login.LoginByPhoneCode;
import com.maskview.mainView.mine.MineActivity;
import com.maskview.mainView.userInfoAndSellList.UserInfoAndSellList;
import com.maskview.util.CircleImageView;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HerAllFansInfoAdapter extends RecyclerView.Adapter<HerAllFansInfoAdapter.BaseViewHolder> {

    private Context mContext;
    private List<HerAllFansInfoBean.DataBean> mBeanList;
    private UserRequest mRequest;
    private static final int FOCUS_SUCCESS = 0;
    private static final int FOCUS_FAIL = 1;
    private static final int CANCEL_FOCUS_SUCCESS = 2;
    private static final int CANCEL_FOCUS_FAIL = 3;
    private static final int NO_RESPONSE = 4;
    private Handler mFocusHandler;
    private boolean mIsMine;
    private Handler mCancelFocusHandler;

    public HerAllFansInfoAdapter(Context context, List<HerAllFansInfoBean.DataBean> beanList, boolean isMine) {
        this.mContext = context;
        this.mBeanList = beanList;
        this.mIsMine = isMine;
        mRequest = new UserRequest();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_her_all_fans_info, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, final int position) {
        holder.setData(mBeanList.get(position));
        // 跳转选中用户信息界面
        holder.mItemLvJumpHerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    if (mBeanList.get(position).getUserName().equals(UtilParameter.myNickName)) {
                        Intent intent = new Intent(mContext, MineActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, UserInfoAndSellList.class);
                        intent.putExtra("userNickName", mBeanList.get(position).getUserName());
                        intent.putExtra("selectedItemPositionHerWindow", position);
                        mContext.startActivity(intent);
                    }
                }
            }
        });
        // 关注该用户
        holder.mItemTvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    if (!checkLogin()) {
                        jumpLoginByCode();
                    } else {
                        focusHer(holder, position);
                    }
                }
            }
        });
        // 取关该用户, 仅在我的界面有效
        holder.mItemTvCancelFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取关有效
                if (AvertTwoTouch.isFastClick()) {
                    if (!checkLogin()) {
                        jumpLoginByCode();
                    } else {
                        cancelFocusHer(holder, position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeanList != null ? mBeanList.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mItemIvHeadView;
        private TextView mItemTvUserName;
        private TextView mItemTvFocus;
        private TextView mItemTvCancelFocus;
        private LinearLayout mItemLvJumpHerInfo;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemIvHeadView = itemView.findViewById(R.id.item_iv_herAllFansInfo_headView);
            mItemTvUserName = itemView.findViewById(R.id.item_tv_herAllFansInfo_userName);
            mItemTvFocus = itemView.findViewById(R.id.item_tv_herAllFansInfo_focus);
            mItemTvCancelFocus = itemView.findViewById(R.id.item_tv_herAllFansInfo_cancelFocus);
            mItemLvJumpHerInfo = itemView.findViewById(R.id.lv_item_jumpHerInfo_fans);
        }

        @SuppressLint("SetTextI18n")
        public void setData(HerAllFansInfoBean.DataBean bean) {
            if (!bean.getHeadViewPath().equals("")) {
                String url = UtilParameter.IMAGES_IP + bean.getHeadViewPath();
                Glide.with(mContext).load(url).placeholder(R.mipmap.head).dontAnimate().into(mItemIvHeadView);
            }
            mItemTvUserName.setText(bean.getUserName() + "");
            if (bean.getUserName().equals(UtilParameter.myNickName)) {
                mItemTvFocus.setVisibility(View.INVISIBLE);
                mItemTvCancelFocus.setVisibility(View.INVISIBLE);
            } else if (bean.getRelation() == 0) {
                mItemTvFocus.setVisibility(View.VISIBLE);
                mItemTvCancelFocus.setVisibility(View.INVISIBLE);
            } else if (bean.getRelation() == 1) {
                mItemTvFocus.setVisibility(View.INVISIBLE);
                mItemTvCancelFocus.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 关注请求
     */
    private void focusHer(final BaseViewHolder holder, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = mRequest.focusUser(mBeanList.get(position).getUserName(), UtilParameter.myToken);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
                            // 关注成功
                            mFocusHandler.sendEmptyMessage(FOCUS_SUCCESS);
                        } else {
                            String note = jsonObject.get("note") + "";
                            if (note.equals("已经关注了!")) {
                                mFocusHandler.sendEmptyMessage(FOCUS_SUCCESS);
                            } else {
                                // 关注失败
                                mFocusHandler.sendEmptyMessage(FOCUS_FAIL);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 服务器未响应
                    mFocusHandler.sendEmptyMessage(NO_RESPONSE);
                }
            }
        }).start();
        mFocusHandler = new Handler(new Handler.Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == FOCUS_SUCCESS) {
                    holder.mItemTvFocus.setVisibility(View.INVISIBLE);
                    holder.mItemTvCancelFocus.setVisibility(View.VISIBLE);
                    if (mIsMine) {
                        // 发送广播, 更新我的界面关注数
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.setAction("action.focus.refreshMineFocusCount");
                                mContext.sendBroadcast(intent);
                            }
                        }).start();
                    } else {
                        // 发送广播, 更新目前item的粉丝数
                    }
                } else if (msg.what == FOCUS_FAIL) {
                    Toast toast = Toast.makeText(mContext, "关注失败,请稍后重试!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (msg.what == NO_RESPONSE) {
                    Toast toast = Toast.makeText(mContext, "似乎网络断了!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                return false;
            }
        });
    }

    /**
     * 取消关注请求
     */
    private void cancelFocusHer(final BaseViewHolder holder, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = mRequest.cancelFocus(mBeanList.get(position).getUserName(), UtilParameter.myToken);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
                            // 取关成功
                            mCancelFocusHandler.sendEmptyMessage(CANCEL_FOCUS_SUCCESS);
                        } else {
                            // 取关失败
                            mCancelFocusHandler.sendEmptyMessage(CANCEL_FOCUS_FAIL);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 服务器未响应
                    mCancelFocusHandler.sendEmptyMessage(NO_RESPONSE);
                }
            }
        }).start();
        mCancelFocusHandler = new Handler(new Handler.Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == CANCEL_FOCUS_SUCCESS) {
                    holder.mItemTvFocus.setVisibility(View.VISIBLE);
                    holder.mItemTvCancelFocus.setVisibility(View.INVISIBLE);
                    if (mIsMine) {
                        // 发送广播, 更新我的界面关注数
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.setAction("action.cancelFocus.refreshMineFocusCount");
                                mContext.sendBroadcast(intent);
                            }
                        }).start();
                    } else {
                        // 发送广播, 更新目前item的粉丝数
                    }
                } else if (msg.what == CANCEL_FOCUS_FAIL) {
                    Toast toast = Toast.makeText(mContext, "取关失败,请稍后重试!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (msg.what == NO_RESPONSE) {
                    Toast toast = Toast.makeText(mContext, "似乎网络断了!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                return false;
            }
        });
    }

    /**
     * 检查是否登录
     */
    private boolean checkLogin() {
        return UtilParameter.myPhoneNumber != null;
    }

    private void jumpLoginByCode() {
        Intent intent = new Intent(mContext, LoginByPhoneCode.class);
        mContext.startActivity(intent);
    }
}
