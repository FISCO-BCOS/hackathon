package com.maskview.mainView.putOnSale;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maskview.R;

import java.util.List;

/**
 * Created by Yzr on 2020/10/5
 */
public class PutOnSaleAdapter extends RecyclerView.Adapter<PutOnSaleAdapter.BaseViewHolder> {

    private List<Uri> mSaleImgUri;
    private Context mContext;

    public PutOnSaleAdapter(List<Uri> saleImgUri, Context context) {
        this.mSaleImgUri = saleImgUri;
        this.mContext = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_put_on_sale, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(mSaleImgUri.get(position));
    }

    @Override
    public int getItemCount() {
        return mSaleImgUri != null ? mSaleImgUri.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvImage;
        private ImageView mIvDelete;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.item_put_on_sale_img);
            mIvDelete = itemView.findViewById(R.id.item_put_on_sale_deleteThis);
        }

        private void setData(Uri uri) {
            mIvImage.setImageURI(uri);
        }
    }
}
