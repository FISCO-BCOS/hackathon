package com.maskview.mainView.confirmImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maskview.R;

import java.util.List;

public class ConfirmResultAdapter extends RecyclerView.Adapter<ConfirmResultAdapter.BaseViewHolder> {

    private List<ConfirmResultBean> mConfirmResultBeans;
    private Context mContext;

    public ConfirmResultAdapter(Context context, List<ConfirmResultBean> confirmResultBeans) {
        this.mContext = context;
        this.mConfirmResultBeans = confirmResultBeans;
    }

    @NonNull
    @Override
    public ConfirmResultAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_alert_confirm_result, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmResultAdapter.BaseViewHolder holder, int position) {
        holder.setData(mConfirmResultBeans.get(position));
    }

    @Override
    public int getItemCount() {
        return mConfirmResultBeans != null ? mConfirmResultBeans.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvShowImage;
        private TextView mTvConfirmResultNote;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvShowImage = itemView.findViewById(R.id.item_iv_confirmResult_image);
            mTvConfirmResultNote = itemView.findViewById(R.id.item_iv_confirmResult_note);
        }

        public void setData(ConfirmResultBean data) {
            // 显示图片
            if (Build.VERSION.SDK_INT >= 29) {
                mIvShowImage.setImageURI(Uri.parse(data.getUriOrPath()));
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(data.getUriOrPath());
                mIvShowImage.setImageBitmap(bitmap);
            }
            // 显示水印结果note
            if (data.getNote() == 0) {
                mTvConfirmResultNote.setText("确权成功");
            } else if (data.getNote() == 1) {
                mTvConfirmResultNote.setText("分辨率低");
                mTvConfirmResultNote.setTextColor(Color.parseColor("#E3170D"));
            } else if (data.getNote() == 2) {
                mTvConfirmResultNote.setText("已确权过");
                mTvConfirmResultNote.setTextColor(Color.parseColor("#E3170D"));
            }
        }
    }
}
