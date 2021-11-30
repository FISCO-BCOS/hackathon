package com.maskview.mainView.userInfoAndSellList.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.maskview.R;
import com.maskview.dao.AvertTwoTouch;
import com.maskview.mainView.purchaseView.PurchaseView;
import com.maskview.util.UtilParameter;

import java.util.List;

public class UserSellListAdapter extends RecyclerView.Adapter<UserSellListAdapter.BaseViewHolder> {

    private Context mContext;
    private List<String> mSellImgList;
    private String mHeadViewPath;
    private int mScreenWidth;

    public UserSellListAdapter(Context context, List<String> sellImgList, String headViewPath, int screenWidth) {
        this.mContext = context;
        this.mSellImgList = sellImgList;
        this.mHeadViewPath = headViewPath;
        this.mScreenWidth = screenWidth;
    }

    public void replaceAll(List<String> list) {
        mSellImgList.clear();
        if (list != null && list.size() > 0) {
            mSellImgList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_user_sell_list, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {
        holder.setData(mSellImgList.get(position));
        // 点击图片跳转购买加入购物车界面
        holder.mIvItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    Intent intent = new Intent(mContext, PurchaseView.class);
                    intent.putExtra("selectedImgUrl", UtilParameter.IMAGES_IP + mSellImgList.get(position));
                    intent.putExtra("selectedHeadViewPath", UtilParameter.IMAGES_IP + mHeadViewPath);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSellImgList != null ? mSellImgList.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvItemImage;
        private int width;
        private int height;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvItemImage = itemView.findViewById(R.id.item_iv_userSellList_image);
        }

        public void setData(String imgPath) {
            final String url = UtilParameter.IMAGES_IP + imgPath;
            //Glide.with(mContext).load(url).dontAnimate().placeholder(R.drawable.loading_anim).override(1000, 800).into(mIvItemImage);
            Glide.with(mContext).asBitmap().load(url).dontAnimate().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    width = bitmap.getWidth();
                    height = bitmap.getHeight();
                }
            });
            // 等比例缩放图片
            ViewGroup.LayoutParams item = mIvItemImage.getLayoutParams();
            item.width = mScreenWidth / 2 - 10;
            int finalWidth = item.width - mIvItemImage.getPaddingLeft() - mIvItemImage.getPaddingRight();
            // item宽与图片宽度比
            float scale = (float) finalWidth / (float) width;
            // 设置图片长度比例自适应
            int finalHeight = Math.round(height * scale);
            Glide.with(mContext).load(url).dontAnimate()
                    .override(finalWidth, finalHeight)
                    .into(mIvItemImage);
        }
    }
}
