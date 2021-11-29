package com.maskview.mainView.shoppingCart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maskview.R;
import com.maskview.dao.ZQRoundOvalImageView;
import com.maskview.mainView.shoppingCart.callBack.OnViewChildClickListener;
import com.maskview.mainView.shoppingCart.callBack.OnViewChildDeleteClickListener;
import com.maskview.mainView.shoppingCart.callBack.OnViewGroupClickListener;
import com.maskview.mainView.shoppingCart.entity.FrontViewToMove;
import com.maskview.mainView.shoppingCart.entity.ShoppingCartData;
import com.maskview.util.UtilParameter;

import java.util.List;

public class ShoppingCartAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ListView listView;
    private List<ShoppingCartData.DataBean> list;

    private OnViewGroupClickListener myOnViewGroupClickListener;
    private OnViewChildClickListener myOnViewChildClickListener;
    private OnViewChildDeleteClickListener myOnViewChildDeleteClickListener;


    public ShoppingCartAdapter(Context context, ListView listView, List<ShoppingCartData.DataBean> list) {
        super();
        this.context = context;
        this.listView = listView;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (list != null && list.size() > 0) ? list.get(groupPosition).getGoodsInfo().size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getGoodsInfo().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_shopping_car, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        if (groupPosition == 0) {
            groupViewHolder.groupTopBar.setVisibility(View.GONE);
        }

        ShoppingCartData.DataBean dataBean = (ShoppingCartData.DataBean) getGroup(groupPosition);
        groupViewHolder.groupSellerName.setText(dataBean.getSellerName() + "的小店");
        groupViewHolder.groupCheckBox.setChecked(dataBean.isCheck());
        //点击group的checkbox
        groupViewHolder.groupCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnViewGroupClickListener.onGroupCheckBoxClick(groupViewHolder.groupCheckBox.isChecked(), v, groupPosition);
            }
        });
        //点击group的卖家昵称,跳转卖家所有的上架图片浏览
        groupViewHolder.groupSellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转卖家上架界面,后续完善
                Toast.makeText(context, "跳转卖家上架界面", Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildrenViewHolder childrenViewHolder;
        convertView = LayoutInflater.from(context).inflate(R.layout.item_child_shopping_car, null);
        childrenViewHolder = new ChildrenViewHolder(convertView, groupPosition, childPosition);
        //关键语句，使用自己写的类来对frontView的onTouch事件复写，实现视图滑动效果
        new FrontViewToMove(childrenViewHolder.frontView, listView, 260);
        childrenViewHolder.childCheckBox.setChecked(list.get(groupPosition).getGoodsInfo().get(childPosition).isCheck());
        childrenViewHolder.childImgTopic.setText(list.get(groupPosition).getGoodsInfo().get(childPosition).getImgTopic());
        childrenViewHolder.childImgPrice.setText("¥ " + list.get(groupPosition).getGoodsInfo().get(childPosition).getImgPrice());
        childrenViewHolder.childImage.setType(ZQRoundOvalImageView.TYPE_ROUND);
        childrenViewHolder.childImage.setRoundRadius(10);
        //网络加载图片
        int width = childrenViewHolder.childImage.getWidth();
        int height = childrenViewHolder.childImage.getHeight();
        String url = UtilParameter.IMAGES_IP + list.get(groupPosition).getGoodsInfo().get(childPosition).getImgPath();
        Glide.with(context).load(url).dontAnimate().error(R.mipmap.image_error)
                .override(width, height)
                .into(childrenViewHolder.childImage);
        //checkbox单击事件
        childrenViewHolder.childCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnViewChildClickListener.onChildCheckBoxClick(childrenViewHolder.childCheckBox.isChecked(), v, groupPosition, childPosition);
            }
        });
        //左滑删除按钮单击事件
        childrenViewHolder.bt_deleteChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnViewChildDeleteClickListener.onChildDeleteClick(v, groupPosition, childPosition);
                new FrontViewToMove(childrenViewHolder.frontView, listView, 200)
                        .generateRevealAnimate(childrenViewHolder.frontView, 0);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    //内部类,绑定group控件
    public class GroupViewHolder {
        TextView groupTopBar;
        TextView groupSellerName;
        CheckBox groupCheckBox;

        public GroupViewHolder(View view) {
            groupTopBar = view.findViewById(R.id.item_group_topBar);
            groupSellerName = view.findViewById(R.id.item_group_sellName_shoppingCart);
            groupCheckBox = view.findViewById(R.id.item_group_checkBox);
        }
    }

    //内部类,绑定children控件
    public class ChildrenViewHolder {
        private CheckBox childCheckBox;
        private ZQRoundOvalImageView childImage;
        //private ImageView childImage;
        private TextView childImgTopic;
        private TextView childImgPrice;
        private View frontView;
        private Button bt_deleteChild;
        private int groupPosition;
        private int childPosition;

        public ChildrenViewHolder(View view, int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
            childCheckBox = view.findViewById(R.id.item_child_checkBox);
            childImage = view.findViewById(R.id.item_child_image_shoppingCart);
            childImgTopic = view.findViewById(R.id.item_child_imgTopic_shoppingCart);
            childImgPrice = view.findViewById(R.id.item_child_price_shoppingCart);
            frontView = view.findViewById(R.id.item_child_layout_front);
            bt_deleteChild = view.findViewById(R.id.item_child_bt_deleteChild);
        }
    }


    //group的checkbox接口方法
    public void setOnGroupClickListener(OnViewGroupClickListener listener) {
        this.myOnViewGroupClickListener = listener;
    }

    //child的checkbox接口方法
    public void setOnChildClickListener(OnViewChildClickListener listener) {
        this.myOnViewChildClickListener = listener;
    }

    //child的左滑删除接口方法
    public void setOnChildDeleteClickListener(OnViewChildDeleteClickListener listener) {
        this.myOnViewChildDeleteClickListener = listener;
    }

}
