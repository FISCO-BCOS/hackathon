package com.maskview.mainView.mgGoods.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.maskview.R;

import java.util.ArrayList;

public class MyConfirmAdapterLowAPI extends RecyclerView.Adapter<MyConfirmAdapterLowAPI.BaseViewHolder> {

    private Context mContext;
    private ArrayList<String> mConfirmImgPath;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mShowCheckBox;

    public MyConfirmAdapterLowAPI(Context context, ArrayList<String> confirmImgPath, int screenWidth, int screenHeight) {
        this.mContext = context;
        this.mConfirmImgPath = confirmImgPath;
        this.mScreenWidth = screenWidth;
        this.mScreenHeight = screenHeight;
    }

    public void replaceAll(ArrayList<String> list) {
        mConfirmImgPath.clear();
        if (list != null && list.size() > 0) {
            mConfirmImgPath.addAll(list);
        }
        notifyDataSetChanged();
    }

    public boolean isShowCheckBox() {
        return mShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.mShowCheckBox = showCheckBox;
    }

    // 防止Checkbox错乱
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();

    // 实现点击和长按监听
    public interface onItemClickListener {
        void onClick(View view, String imgPath);

        boolean onLongClick(View view, int pos);
    }

    private onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MyConfirmAdapterLowAPI.onItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = View.inflate(mContext, R.layout.item_my_goods_confirm, null);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_goods_confirm, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, final int position) {
        holder.setData(mConfirmImgPath.get(position));
        // 防止复用导致的checkbox显示错乱
        holder.mChkConfirmCheckbox.setTag(position);
        // 判断当前checkbox的状态
        if (mShowCheckBox) {
            holder.mChkConfirmCheckbox.setVisibility(View.VISIBLE);
            // 防止显示错乱
            holder.mChkConfirmCheckbox.setChecked(mCheckStates.get(position, false));
        } else {
            holder.mChkConfirmCheckbox.setVisibility(View.GONE);
            // 取消掉Checkbox后不再保存当前选择的状态
            holder.mChkConfirmCheckbox.setChecked(false);
            mCheckStates.clear();
        }
        // 点击监听
        holder.mRlConfirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShowCheckBox) {
                    holder.mChkConfirmCheckbox.setChecked(!holder.mChkConfirmCheckbox.isChecked());
                }
                mOnItemClickListener.onClick(view, mConfirmImgPath.get(position));
            }
        });
        // 长按监听
        holder.mRlConfirmLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return mOnItemClickListener.onLongClick(view, position);
            }
        });
        // 对checkbox的监听 保存选择状态 防止checkbox显示错乱
        holder.mChkConfirmCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        return mConfirmImgPath != null ? mConfirmImgPath.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvConfirmImg;
        private CheckBox mChkConfirmCheckbox;
        private RelativeLayout mRlConfirmLayout;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvConfirmImg = itemView.findViewById(R.id.item_confirm_img);
            mChkConfirmCheckbox = itemView.findViewById(R.id.item_confirm_checkbox);
            mRlConfirmLayout = itemView.findViewById(R.id.item_confirm_layout);
        }

        public void setData(String path) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            //mIvConfirmImg.setImageBitmap(bitmap);
            /*Glide.with(mContext).load(bitmap).dontAnimate()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ViewGroup.LayoutParams params = mIvConfirmImg.getLayoutParams();
                            int width = (mScreenWidth / 2) - 10;
                            float scale = (float) width / (float) resource.getIntrinsicWidth();
                            int height = Math.round(resource.getIntrinsicHeight() * scale);
                            params.height = height;
                            params.width = width;
                            mIvConfirmImg.setAdjustViewBounds(true);
                            mIvConfirmImg.setLayoutParams(params);
                            return false;
                        }
                    }).into(mIvConfirmImg);*/
            Glide.with(mContext).asBitmap().load(bitmap)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            mIvConfirmImg.setAdjustViewBounds(true);
                            mIvConfirmImg.setImageBitmap(resource);
                        }
                    });
        }
    }
}
