package com.maskview.mainView.myPurchase.adapter;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maskview.R;

import com.maskview.dao.AvertTwoTouch;
import com.maskview.mainView.myPurchase.OneImageView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

public class MyPurchaseAdapter extends RecyclerView.Adapter<MyPurchaseAdapter.BaseViewHolder> {

    private Context mContext;

    private List<Bitmap> myPurchaseImgBitmap;
    private int screenWidth;
    private int screenHeight;

    public MyPurchaseAdapter(Context mContext, List<Bitmap> myPurchaseImgBitmap, int screenWidth, int screenHeight) {
        this.mContext = mContext;
        this.myPurchaseImgBitmap = myPurchaseImgBitmap;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }


    public void replaceAll(List<Bitmap> list) {
        myPurchaseImgBitmap.clear();
        if (list != null && list.size() > 0) {
            myPurchaseImgBitmap.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_my_purchase, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {
        holder.setData(myPurchaseImgBitmap.get(position));
        // 我的购买点击图片放大处理
        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    Bitmap bitmap = myPurchaseImgBitmap.get(position);
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "IMG"+ Calendar.getInstance().getTime(),null));
                    Intent intent = new Intent(mContext, OneImageView.class);
                    intent.putExtra("myBitmap", uri.toString());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myPurchaseImgBitmap != null ? myPurchaseImgBitmap.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_image;

        private BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_myPurchase_img);
            RelativeLayout item_layout = itemView.findViewById(R.id.item_layout_myPurchase);
            //设置item宽高为屏幕宽度的1/2
            ViewGroup.LayoutParams ps = item_image.getLayoutParams();
            ps.width = (screenWidth - 8) / 2;
            ps.height = (screenWidth - 8) / 2;
        }

        public void setData(Bitmap bitmap) {
            item_image.setImageBitmap(bitmap);
        }
    }
}
