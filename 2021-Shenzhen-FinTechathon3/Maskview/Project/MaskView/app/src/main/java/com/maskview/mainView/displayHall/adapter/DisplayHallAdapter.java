package com.maskview.mainView.displayHall.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.maskview.R;
import com.maskview.dao.AvertTwoTouch;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.displayHall.bean.DisplayHallBean;
import com.maskview.mainView.herAllFansInfo.HerAllFansInfo;
import com.maskview.mainView.herAllFocusInfo.HerAllFocusInfo;
import com.maskview.mainView.login.LoginByPhoneCode;
import com.maskview.mainView.purchaseView.PurchaseView;
import com.maskview.mainView.userInfoAndSellList.UserInfoAndSellList;
import com.maskview.util.CircleImageView;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DisplayHallAdapter extends RecyclerView.Adapter<DisplayHallAdapter.BaseViewHolder> {

    private List<DisplayHallBean.DataBean> mBeanList;
    private Context mContext;
    private int mScreenWidth;
    private UserRequest mRequest;
    private static final int FOCUS_SUCCESS = 0;
    private static final int FOCUS_FAIL = 1;
    private static final int CANCEL_FOCUS_SUCCESS = 2;
    private static final int CANCEL_FOCUS_FAIL = 3;
    private static final int NO_RESPONSE = 4;
    private Handler mFocusHandler;
    private Handler mCancelFocusHandler;

    public DisplayHallAdapter(Context context, List<DisplayHallBean.DataBean> beanList, int screenWidth) {
        this.mContext = context;
        this.mBeanList = beanList;
        this.mScreenWidth = screenWidth;
        mRequest = new UserRequest();
    }

    public void replaceAll(List<DisplayHallBean.DataBean> list) {
        mBeanList.clear();
        if (list != null && list.size() > 0) {
            mBeanList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addData(int position, List<DisplayHallBean.DataBean> list) {
        mBeanList.addAll(position, list);
        notifyItemInserted(position);
    }

    /**
     * 解决了下拉刷新图片显示混乱的问题
     */
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = View.inflate(mContext, R.layout.item_display_hall, null);
        return new BaseViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mBeanList != null ? mBeanList.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, final int position) {
        holder.setData(mBeanList.get(position));
        // 点击图片放大显示图片信息
        holder.mItemIvSellImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    Intent intent = new Intent(mContext, PurchaseView.class);
                    intent.putExtra("selectedImgUrl", UtilParameter.IMAGES_IP + mBeanList.get(position).getImgPath());
                    intent.putExtra("selectedHeadViewPath", UtilParameter.IMAGES_IP + mBeanList.get(position).getHeadViewPath());
                    intent.putExtra("childPositionDisplayHall", position);
                    mContext.startActivity(intent);
                }
            }
        });
        // 点击用户头像查看用户基本信息及上架图片集合
        holder.mItemIvHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    Intent intent = new Intent(mContext, UserInfoAndSellList.class);
                    intent.putExtra("userNickName", mBeanList.get(position).getUserName());
                    intent.putExtra("childPositionDisplayHall", position);
                    mContext.startActivity(intent);
                }
            }
        });
        // 关注某人
        holder.mItemBtFocus.setOnClickListener(new View.OnClickListener() {
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
        // 取消关注某人
        holder.mItemBtCancelFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    if (!checkLogin()) {
                        jumpLoginByCode();
                    } else {
                        cancelFocusHer(holder, position);
                    }
                }
            }
        });
        // 点击关注人数查看所有关注人的信息
        holder.mItemLvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    Intent intent = new Intent(mContext, HerAllFocusInfo.class);
                    intent.putExtra("userName", mBeanList.get(position).getUserName());
                    mContext.startActivity(intent);
                }
            }
        });
        // 点击粉丝人数查看所有粉丝的信息
        holder.mItemLvFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    Intent intent = new Intent(mContext, HerAllFansInfo.class);
                    intent.putExtra("userName", mBeanList.get(position).getUserName());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mItemIvHeadView;
        private TextView mItemTvUserName;
        private TextView mItemTvFansCount;
        private TextView mItemTvFocusCount;
        private ImageView mItemIvSellImg;
        private Button mItemBtFocus;
        private Button mItemBtCancelFocus;
        private ProgressBar mBar;
        private LinearLayout mItemLvFocus;
        private LinearLayout mItemLvFans;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemIvHeadView = itemView.findViewById(R.id.item_displayHall_user_headView);
            mItemTvUserName = itemView.findViewById(R.id.item_displayHall_userName);
            mItemTvFansCount = itemView.findViewById(R.id.item_displayHall_fans);
            mItemTvFocusCount = itemView.findViewById(R.id.item_displayHall_focus);
            mItemIvSellImg = itemView.findViewById(R.id.item_displayHall_sellImg);
            mItemBtFocus = itemView.findViewById(R.id.bt_item_displayHall_focus);
            mItemBtCancelFocus = itemView.findViewById(R.id.bt_item_displayHall_cancelFocus);
            mBar = itemView.findViewById(R.id.item_bar);
            mItemLvFocus = itemView.findViewById(R.id.item_lv_displayHall_focus);
            mItemLvFans = itemView.findViewById(R.id.item_lv_displayHall_fans);
        }

        @SuppressLint("SetTextI18n")
        public void setData(DisplayHallBean.DataBean bean) {
            int mRelation = bean.getRelation();
            mItemTvUserName.setText(bean.getUserName());
            mItemTvFansCount.setText(bean.getFansCount() + "");
            mItemTvFocusCount.setText(bean.getFocusCount() + "");
            String sellImgUrl = UtilParameter.IMAGES_IP + bean.getImgPath();
            ViewGroup.LayoutParams params = mItemIvSellImg.getLayoutParams();
            params.height = mScreenWidth / 2 + 140;
            params.width = mScreenWidth;
            mItemIvSellImg.setLayoutParams(params);
            Glide.with(mContext).load(sellImgUrl).dontAnimate().listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    ViewGroup.LayoutParams params = mItemIvSellImg.getLayoutParams();
                    // 将图片宽度设为屏幕宽度
                    int width = mScreenWidth - mItemIvSellImg.getPaddingLeft() - mItemIvSellImg.getPaddingRight();
                    // 屏幕宽与图片宽度比
                    float scale = (float) width / (float) resource.getIntrinsicWidth();
                    // 设置图片长度比例自适应
                    int height = Math.round(resource.getIntrinsicHeight() * scale);
                    params.height = height + mItemIvSellImg.getPaddingTop() + mItemIvSellImg.getPaddingBottom();
                    params.width = width;
                    mBar.setVisibility(View.INVISIBLE);
                    mItemIvSellImg.setLayoutParams(params);
                    return false;
                }
            }).into(mItemIvSellImg);
            if (!bean.getHeadViewPath().equals("")) {
                String headUrl = UtilParameter.IMAGES_IP + bean.getHeadViewPath();
                Glide.with(mContext).load(headUrl).placeholder(R.mipmap.head).dontAnimate().into(mItemIvHeadView);
            }
            // 关注关系显示
            if (mRelation == 0) {
                mItemBtCancelFocus.setVisibility(View.INVISIBLE);
                mItemBtFocus.setVisibility(View.VISIBLE);
            } else {
                mItemBtCancelFocus.setVisibility(View.VISIBLE);
                mItemBtFocus.setVisibility(View.INVISIBLE);
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
                    holder.mItemBtFocus.setVisibility(View.INVISIBLE);
                    holder.mItemBtCancelFocus.setVisibility(View.VISIBLE);
                    int fansCount = Integer.parseInt(holder.mItemTvFansCount.getText() + "");
                    int nowFans = fansCount + 1;
                    holder.mItemTvFansCount.setText(nowFans + "");
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
                    holder.mItemBtFocus.setVisibility(View.VISIBLE);
                    holder.mItemBtCancelFocus.setVisibility(View.INVISIBLE);
                    int fansCount = Integer.parseInt(holder.mItemTvFansCount.getText() + "");
                    int nowFans = fansCount - 1;
                    holder.mItemTvFansCount.setText(nowFans + "");
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
