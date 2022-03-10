package com.maskview.mainView.records.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maskview.R;
import com.maskview.mainView.records.beans.PurchaseRecordsBean;
import com.maskview.util.UtilParameter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyPurchaseRecordsAdapter extends RecyclerView.Adapter<MyPurchaseRecordsAdapter.BaseViewHolder> {

    private Context mContext;
    private List<PurchaseRecordsBean.DataBeanPurchaserRecords> datas;

    public MyPurchaseRecordsAdapter(Context mContext, List<PurchaseRecordsBean.DataBeanPurchaserRecords> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    public void replaceAll(List<PurchaseRecordsBean.DataBeanPurchaserRecords> list) {
        datas.clear();
        if (list != null && list.size() > 0) {
            datas.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyPurchaseRecordsAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_purchase_records, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPurchaseRecordsAdapter.BaseViewHolder holder, int position) {
        holder.setData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_image;
        private TextView tv_price;
        private TextView tv_date;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.item_purchaseRecords_image);
            tv_price = itemView.findViewById(R.id.item_purchaseRecords_money);
            tv_date = itemView.findViewById(R.id.item_purchaseRecords_date);
        }

        @SuppressLint("SetTextI18n")
        public void setData(PurchaseRecordsBean.DataBeanPurchaserRecords data) {
            //网络加载图片
            String url = UtilParameter.IMAGES_IP + data.getImgPath();
            Glide.with(mContext).load(url).into(iv_image);
            tv_price.setText(data.getImgPrice() + "");
            //转换时期格式
            String sDate = "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = formatter.parse(data.getPurchaseDate());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                if (date != null) {
                    sDate = sdf.format(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tv_date.setText(sDate);
        }
    }
}
