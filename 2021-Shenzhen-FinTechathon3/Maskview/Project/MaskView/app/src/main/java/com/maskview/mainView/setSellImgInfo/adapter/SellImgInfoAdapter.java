package com.maskview.mainView.setSellImgInfo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maskview.R;

import java.util.List;

public class SellImgInfoAdapter extends RecyclerView.Adapter<SellImgInfoAdapter.BaseViewHolder> {

    private List<Uri> mSelectedImgUri;
    private Context mContext;

    public SellImgInfoAdapter(Context Context, List<Uri> selectedImgUri) {
        this.mContext = Context;
        this.mSelectedImgUri = selectedImgUri;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_set_sell_img_info, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(mSelectedImgUri.get(position));
    }

    @Override
    public int getItemCount() {
        return mSelectedImgUri != null ? mSelectedImgUri.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvImage;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.item_setSellImgInfo_image);
            EditText mEtTopic = itemView.findViewById(R.id.item_setSellImgInfo_topic);
            EditText mEtPrice = itemView.findViewById(R.id.item_setSellImgInfo_price);
        }

        public void setData(Uri imgUri) {
            mIvImage.setImageURI(imgUri);
        }
    }
}
