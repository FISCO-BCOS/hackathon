package com.maskview.mainView.purchaseView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.maskview.R;
import com.maskview.dao.RenderScriptBlur;
import com.maskview.dao.UrlTransBitmap;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.confirmOrders.ConfirmOrders;
import com.maskview.mainView.login.LoginByPhoneCode;
import com.maskview.mainView.securePurchase.SecurePurchase;
import com.maskview.mainView.shoppingCart.entity.ShoppingCartData;
import com.maskview.mainView.userInfoAndSellList.UserInfoAndSellList;
import com.maskview.util.CircleImageView;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PurchaseView extends AppCompatActivity implements View.OnClickListener {

    private String selectedImgUrl;
    private View allView, headerView, footerView;
    private PhotoView Iv_showPurchaseImg;
    private CircleImageView mIvHeadView;
    private TextView tv_imgPrice;
    private TextView tv_imgSellerName;
    private TextView tv_imgTopic;
    private TextView tv_sellDate;
    private boolean isHidden = false;
    private Bitmap bgBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private int screenWidth;
    private int screenHeight;
    private Context mContext;
    private UserRequest ur;
    private String imgOwner;   //照片持有者昵称
    private String sellDate;  //照片上架日期
    private String imgPrice;  //照片价格
    private String imgTopic;  //照片主题
    private String mGetHeadViewPath;
    private AlertDialog chooseDialog;
    private int mGetChildPosition;
    private int mHeadViewWidth;
    private int mHeadViewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_view);
        mContext = this;
        // 先拿到选中的图片URL
        Iv_showPurchaseImg = findViewById(R.id.iv_showPurchaseImg);
        Bundle bundle = getIntent().getExtras();
        selectedImgUrl = bundle.getString("selectedImgUrl");
        mGetHeadViewPath = bundle.getString("selectedHeadViewPath");
        mGetChildPosition = bundle.getInt("childPositionDisplayHall");
        // 获取数据
        initData();
        // 点击图片显隐图片信息
        Iv_showPurchaseImg.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                updateHeaderAndFooter();
            }
        });
    }

    //请求服务器获取选中照片的信息
    private void initData() {
        //将图片地址截取为图片名称
        final String imgName = selectedImgUrl.substring(selectedImgUrl.indexOf("thumbnailImg/"));
        ur = new UserRequest();
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = ur.getOneSellImgInfo(imgName, null);
                if (result.contains("true")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        imgOwner = jsonObject.get("userName") + "";
                        imgPrice = jsonObject.get("imgPrice") + "";
                        imgTopic = jsonObject.get("imgTopic") + "";
                        sellDate = jsonObject.get("sellDate") + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        es.submit(task);
        es.shutdown();
        while (true) {
            if (es.isTerminated()) {
                initView();
                break;
            }
        }
    }

    private void initView() {
        // 获取屏幕宽高
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        mIvHeadView = findViewById(R.id.iv_purchaseView_headView);
        mIvHeadView.setOnClickListener(this);
        tv_imgPrice = findViewById(R.id.tv_purchase_imgPrice);
        tv_imgSellerName = findViewById(R.id.iv_purchase_sellerName);
        tv_imgTopic = findViewById(R.id.tv_purchase_imgTopic);
        tv_sellDate = findViewById(R.id.iv_purchase_sellDate);
        ImageView iv_back = findViewById(R.id.iv_purchaseBack);
        allView = findViewById(R.id.allView_purchase);
        headerView = findViewById(R.id.frameLayout_header_view);
        footerView = findViewById(R.id.frameLayout_footer_view);
        footerView.getBackground().setAlpha(10); //设置半透明
        headerView.getBackground().setAlpha(10); //设置半透明
        iv_back.setOnClickListener(this);
        Button bt_jumpOrders = findViewById(R.id.bt_purchase_now);
        bt_jumpOrders.setOnClickListener(this);
        Button bt_addOneToShoppingCart = findViewById(R.id.bt_purchase_addShoppingCart);
        bt_addOneToShoppingCart.setOnClickListener(this);
        text();
        tv_imgSellerName.setText(imgOwner);
        tv_imgPrice.setText(imgPrice);
        tv_imgTopic.setText(imgTopic);
        tv_sellDate.setText(sellDate);
        //网络加载图片
        Glide.with(this).load(selectedImgUrl).dontAnimate().into(Iv_showPurchaseImg);
        if (!mGetHeadViewPath.equals(UtilParameter.IMAGES_IP)) {
            mIvHeadView.post(new Runnable() {
                @Override
                public void run() {
                    mHeadViewWidth = mIvHeadView.getWidth();
                    mHeadViewHeight = mIvHeadView.getHeight();
                }
            });
            mIvHeadView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(this).load(mGetHeadViewPath).placeholder(R.mipmap.head)
                    .override(mHeadViewWidth, mHeadViewHeight).dontAnimate().into(mIvHeadView);
        }
    }

    private boolean checkLogin() {
        return UtilParameter.myPhoneNumber != null;
    }

    private void jumpLoginByCode() {
        Intent intent = new Intent(PurchaseView.this, LoginByPhoneCode.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_purchaseBack:
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
                finish();
                break;
            case R.id.bt_purchase_now:
                if (!checkLogin()) {
                    jumpLoginByCode();
                } else {
                    choosePurchaseType();
                }
                break;
            case R.id.bt_purchase_addShoppingCart:
                if (!checkLogin()) {
                    jumpLoginByCode();
                } else {
                    addToMyShoppingCart();
                }
                break;
            case R.id.iv_purchaseView_headView:
                Intent intent = new Intent(mContext, UserInfoAndSellList.class);
                intent.putExtra("userNickName", imgOwner);
                intent.putExtra("childPositionDisplayHall", mGetChildPosition);
                startActivity(intent);
                break;
        }
    }

    // 选择交易类型
    private void choosePurchaseType() {
        // 弹框选择是否为安全交易
        View view = View.inflate(mContext, R.layout.alert_choose_purchase_type, null);
        chooseDialog = new AlertDialog.Builder(mContext).setView(view).create();
        chooseDialog.show();
        // 设置dialog弹窗圆角
        chooseDialog.getWindow().setBackgroundDrawableResource(R.drawable.lv_radius);
        Button bt_normalTrade = view.findViewById(R.id.alert_normal_trade);
        Button bt_secureTrade = view.findViewById(R.id.alert_secure_trade);
        // 正常购买
        bt_normalTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpConfirmOrders();
                chooseDialog.cancel();
            }
        });
        // 安全购买
        bt_secureTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                securePurchase();
                chooseDialog.cancel();
            }
        });
    }

    // 添加到购物车
    private void addToMyShoppingCart() {
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String imgPath = selectedImgUrl.substring(selectedImgUrl.indexOf("thumbnailImg"));
                final String result = ur.addOneToMyShoppingCart(imgOwner, imgPath, UtilParameter.myToken);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!result.equals("")) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String tag = jsonObject.get("result") + "";
                                if (tag.equals("true")) {
                                    Toast.makeText(mContext, "加入成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    String note = jsonObject.get("data") + "";
                                    Toast.makeText(mContext, note, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "服务器未响应!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        es.submit(task);
        es.shutdown();
    }

    // 正常购买,跳转确认订单界面
    private void jumpConfirmOrders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 判断是否为自己的照片
                final String result = ur.isMyImg(imgOwner, UtilParameter.myToken);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!result.equals("")) {
                            if (result.contains("true")) {
                                Intent intent = new Intent(PurchaseView.this, ConfirmOrders.class);
                                List<ShoppingCartData.DataBean> data = new ArrayList<>();
                                ShoppingCartData.DataBean dataBean = new ShoppingCartData.DataBean();
                                dataBean.setSellerName(tv_imgSellerName.getText() + "");
                                List<ShoppingCartData.DataBean.GoodsInfoBean> goodsData = new ArrayList<>();
                                ShoppingCartData.DataBean.GoodsInfoBean goodsInfoBean = new ShoppingCartData.DataBean.GoodsInfoBean();
                                goodsInfoBean.setImgPath(selectedImgUrl.substring(selectedImgUrl.indexOf("/thumbnailImg")));
                                goodsInfoBean.setImgPrice(tv_imgPrice.getText().toString());
                                goodsInfoBean.setImgTopic(imgTopic);
                                goodsData.add(goodsInfoBean);
                                dataBean.setGoodsInfo(goodsData);
                                data.add(dataBean);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("selected_shopping_goods", (Serializable) data);
                                bundle.putInt("mAllSelectedPrice", Integer.parseInt(imgPrice));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                Toast.makeText(mContext, "不能购买自己的图片", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "服务器未响应,请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    // 安全购买, 跳转安全交易界面
    private void securePurchase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result = ur.isMyImg(imgOwner, UtilParameter.myToken);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!result.equals("")) {
                            if (result.contains("true")) {
                                Intent intent = new Intent(PurchaseView.this, SecurePurchase.class);
                                intent.putExtra("imgUrl", selectedImgUrl.substring(selectedImgUrl.indexOf("/thumbnailImg")));
                                intent.putExtra("sellerName", imgOwner);
                                intent.putExtra("imgPrice", imgPrice);
                                startActivity(intent);
                            } else {
                                Toast.makeText(mContext, "这张照片就是你的", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "服务器未响应,请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private void updateHeaderAndFooter() {
        if (isHidden) {
            headerView.animate().translationY(0);
            footerView.animate().translationY(0);
        } else {
            headerView.animate().translationY(-headerView.getMeasuredHeight());
            footerView.animate().translationY(footerView.getMeasuredHeight());
        }
        isHidden = !isHidden;
    }

    /**
     * 高斯模糊
     */
    private void text() {
        long startTime = System.currentTimeMillis();
        UrlTransBitmap trans = new UrlTransBitmap();
        Bitmap bitmap = trans.returnBitMap(selectedImgUrl, mContext);
        long endTime1 = System.currentTimeMillis();
        Log.e("---", "转bitmap时间: " + (endTime1 - startTime));
        Bitmap mAll = RenderScriptBlur.blur(mContext, bitmap, 0.06f, 10);
        allView.setBackground(new BitmapDrawable(getResources(), mAll));
        long endTime2 = System.currentTimeMillis();
        Log.e("---", "高斯模糊时间: " + (endTime2 - endTime1));
    }

    /*// 设置本机返回键操作
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
            finish();
        }
        return super.dispatchKeyEvent(event);
    }*/
}
