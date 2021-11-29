package com.maskview.mainView.mgGoods.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ThumbnailImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.maskview.R;

import java.util.ArrayList;

public class MySellAdapter extends RecyclerView.Adapter<MySellAdapter.BaseViewHolder> {

    private Context mContext;
    private ArrayList<String> mySellImgList;
    private boolean showCheckBox;
    private int screenWidth;
    private int screenHeight;

    public MySellAdapter(Context mContext, ArrayList<String> mySellImgList, int screenWidth, int screenHeight) {
        this.mContext = mContext;
        this.mySellImgList = mySellImgList;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public void replaceAll(ArrayList<String> list) {
        mySellImgList.clear();
        if (list != null && list.size() > 0) {
            mySellImgList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MySellAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = View.inflate(mContext, R.layout.item_my_goods_sell, null);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_goods_sell, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MySellAdapter.BaseViewHolder holder, final int position) {
        holder.setData(mySellImgList.get(position));
        //防止复用导致的checkbox显示错乱
        holder.item_mySellCheckbox.setTag(position);
        //判断当前checkbox的状态
        if (showCheckBox) {
            holder.item_mySellCheckbox.setVisibility(View.VISIBLE);
            //防止显示错乱
            holder.item_mySellCheckbox.setChecked(mCheckStates.get(position, false));
        } else {
            holder.item_mySellCheckbox.setVisibility(View.GONE);
            //取消掉Checkbox后不再保存当前选择的状态
            holder.item_mySellCheckbox.setChecked(false);
            mCheckStates.clear();
        }
        //点击监听
        holder.item_mySell_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showCheckBox) {
                    holder.item_mySellCheckbox.setChecked(!holder.item_mySellCheckbox.isChecked());
                }
                onItemClickListener.onClick(view, mySellImgList.get(position));
            }
        });
        //长按监听
        holder.item_mySell_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return onItemClickListener.onLongClick(view, position);
            }
        });
        //对checkbox的监听 保存选择状态 防止checkbox显示错乱
        holder.item_mySellCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int pos = (int) compoundButton.getTag();
                if (b) {
                    mCheckStates.put(pos, true);
                } else {
                    mCheckStates.delete(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mySellImgList != null ? mySellImgList.size() : 0;
    }

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    //防止Checkbox错乱 做setTag  getTag操作
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();


    //自己写接口，实现点击和长按监听
    public interface onItemClickListener {
        void onClick(View view, String imgUrl);

        boolean onLongClick(View view, int pos);
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(MySellAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_mySellImg;
        private CheckBox item_mySellCheckbox;
        private RelativeLayout item_mySell_layout;

        private BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            item_mySellImg = itemView.findViewById(R.id.item_mySell_img);
            item_mySellCheckbox = itemView.findViewById(R.id.item_mySell_checkbox);
            item_mySell_layout = itemView.findViewById(R.id.item_mySell_layout);
            //mItemChildProBar = itemView.findViewById(R.id.item_child_shoppingCar_proBar);
            //设置item宽高为屏幕宽度的1/2
        }

        public void setData(String imgPath) {
            /*Glide.with(mContext).load(imgPath).dontAnimate()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ViewGroup.LayoutParams params = item_mySellImg.getLayoutParams();
                            //int width = (screenWidth / 3) - 4;
                            int width = (screenWidth / 2) - 10;
                            float scale = (float) width / (float) resource.getIntrinsicWidth();
                            int height = Math.round(resource.getIntrinsicHeight() * scale);
                            params.height = height;
                            params.width = width;
                            item_mySellImg.setAdjustViewBounds(true);
                            item_mySellImg.setLayoutParams(params);
                            return false;
                        }
                    }).into(item_mySellImg);*/
            Glide.with(mContext).asBitmap().load(imgPath)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            item_mySellImg.setAdjustViewBounds(true);
                            item_mySellImg.setImageBitmap(resource);
                        }
                    });
            //Glide.with(mContext).load(imgPath).dontAnimate()
            //        .override((screenWidth - 8) / 2, (screenWidth - 8) / 2).into(item_mySellImg);
        }
    }
}
