package com.maskview.mainView.resetSellPriceTopic;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.maskview.R;
import com.maskview.dao.RenderScriptBlur;
import com.maskview.dao.UrlTransBitmap;
import com.maskview.dao.UserRequest;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ResetSellImgPrice extends AppCompatActivity implements View.OnClickListener {

    private String mResetPriceImgUrl;
    private PhotoView mIvSellImg;
    private EditText mEtNewPrice;
    private TextView mTvImgTopic;
    private TextView mTvSellerName;
    private TextView mTvSellDate;
    private View mHeaderView, mFooterView;
    private RelativeLayout mRvView;
    private Bitmap mBitmap;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mIsHidden = false;
    private final static int RESET_PRICE_SUCCESS = 0;
    private final static int RESET_PRICE_FAIL = 1;
    private final static int RESET_NO_RESPONSE = 2;
    private AlertDialog mAlertDialog;
    private Context mContext;
    private UserRequest mRequest;
    private String mImgOwner;
    private String mSellDate;
    private String mImgPrice;
    private String mImgTopic;
    private int mNewPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_sell_img_price);
        initView();
        getImgUrl();
    }

    private void initView() {
        mContext = this;
        mRequest = new UserRequest();
        mRvView = findViewById(R.id.layout_resetSellImgPrice);
        ImageView iv_finishThiActivity = findViewById(R.id.iv_finishResetSellPrice);
        iv_finishThiActivity.setOnClickListener(this);
        mIvSellImg = findViewById(R.id.iv_mySellOneImg);
        mEtNewPrice = findViewById(R.id.et_resetSellPrice_newPrice);
        mTvImgTopic = findViewById(R.id.tv_resetSellPrice_imgTopic);
        mTvSellerName = findViewById(R.id.tv_resetSellPrice_sellerName);
        mTvSellDate = findViewById(R.id.tv_resetSellPrice_sellDate);
        mHeaderView = findViewById(R.id.resetSellPrice_header_view);
        mFooterView = findViewById(R.id.resetSellPrice_footer_view);
        Button bt_resetSellPrice_quit = findViewById(R.id.bt_resetSellPrice_quit);
        bt_resetSellPrice_quit.setOnClickListener(this);
        Button bt_resetSellPrice_ok = findViewById(R.id.bt_resetSellPrice_ok);
        bt_resetSellPrice_ok.setOnClickListener(this);
        // 获取屏幕宽高
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;
        // 点击图片显隐图片信息
        mIvSellImg.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                updateHeaderAndFooter();
            }
        });
    }

    private void showView() {
        mHeaderView.getBackground().setAlpha(10);
        mFooterView.getBackground().setAlpha(10);
        mEtNewPrice.setHint(mImgPrice);
        mTvImgTopic.setText(mImgTopic);
        mTvSellerName.setText(mImgOwner);
        mTvSellDate.setText(mSellDate);
        // 网络加载图片
        Glide.with(this).load(mResetPriceImgUrl).dontAnimate()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ViewGroup.LayoutParams params = mIvSellImg.getLayoutParams();
                        // 图片宽度为屏幕宽度
                        int width = UtilParameter.mScreenWidth - mIvSellImg.getPaddingLeft() - mIvSellImg.getPaddingRight();
                        // 宽度比
                        float scale = (float) width / (float) resource.getIntrinsicWidth();
                        // 显示高度
                        int height = Math.round(resource.getIntrinsicHeight() * scale);
                        params.width = width;
                        params.height = height + mIvSellImg.getPaddingTop() + mIvSellImg.getPaddingBottom();
                        mIvSellImg.setLayoutParams(params);
                        return false;
                    }
                }).into(mIvSellImg);
    }

    // 接收传回过来的图片地址
    private void getImgUrl() {
        mResetPriceImgUrl = getIntent().getStringExtra("selectedResetPriceImgUrl");
        if (mResetPriceImgUrl != null && !mResetPriceImgUrl.equals("")) {
            // 高斯背景
            UrlTransBitmap trans = new UrlTransBitmap();
            Bitmap bitmap = trans.returnBitMap(mResetPriceImgUrl, mContext);
            mBitmap = RenderScriptBlur.blur(mContext, bitmap, 0.06f, 10);
            mRvView.setBackground(new BitmapDrawable(getResources(), mBitmap));
            getImgInfo();
        }
    }

    // 请求服务器获取图片信息
    private void getImgInfo() {
        // 将图片地址截取为图片名称
        final String imgName = mResetPriceImgUrl.substring(mResetPriceImgUrl.indexOf("thumbnailImg"));
        mRequest = new UserRequest();
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = mRequest.getOneSellImgInfo(imgName, null);
                if (result.contains("true")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        mImgOwner = jsonObject.get("userName") + "";
                        mImgPrice = jsonObject.get("imgPrice") + "";
                        mImgTopic = jsonObject.get("imgTopic") + "";
                        mSellDate = jsonObject.get("sellDate") + "";
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
                showView();
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finishResetSellPrice:
                ResetSellImgPrice.this.finish();
                break;
            case R.id.bt_resetSellPrice_ok:
                resetImgPrice();
                break;
            case R.id.bt_resetSellPrice_quit:
                // 点击取消改价,恢复原价
                mEtNewPrice.setText("");
                break;
        }
    }

    // 修改价格,传给服务器
    private void resetImgPrice() {
        //final int newPrice;
        if (mEtNewPrice.getText().toString().trim().equals("")) {
            Toast.makeText(mContext, "请输入修改的价格", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mNewPrice = Integer.parseInt(mEtNewPrice.getText().toString());
            if (mNewPrice == 0) {
                Toast.makeText(mContext, "价格不能为0", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String imgName = mResetPriceImgUrl.substring(mResetPriceImgUrl.indexOf("con-" + UtilParameter.myPhoneNumber));
                String result = mRequest.updateSellImgPrice(imgName, mNewPrice + "", UtilParameter.myToken);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
                            // 修改成功提示框,返回我的上架
                            resetWindowHandler.sendEmptyMessage(RESET_PRICE_SUCCESS);
                        } else {
                            // 修改失败,请稍后重试
                            resetWindowHandler.sendEmptyMessage(RESET_PRICE_FAIL);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 服务器未响应
                    resetWindowHandler.sendEmptyMessage(RESET_NO_RESPONSE);
                }
            }
        };
        es.submit(task);
        es.shutdown();
    }

    // 改价成功失败提示
    private Handler resetWindowHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == RESET_PRICE_SUCCESS) {
                mAlertDialog = new AlertDialog.Builder(mContext).setMessage("修改成功")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                mEtNewPrice.setText("");
                                mEtNewPrice.setHint(mNewPrice + "");
                            }
                        }).create();
            } else if (msg.what == RESET_PRICE_FAIL) {
                mAlertDialog = new AlertDialog.Builder(mContext).setMessage("改价失败,求稍后重试")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
            } else if (msg.what == RESET_NO_RESPONSE) {
                mAlertDialog = new AlertDialog.Builder(mContext).setMessage("服务器未响应,请稍后重试")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
            }
            mAlertDialog.show();
            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    dialog.cancel();
                    mEtNewPrice.setText("");
                    mEtNewPrice.setHint(mNewPrice + "");
                    return false;
                }
            });
            return false;
        }
    });


    /*private void showSoftColor() {
        UrlTransBitmap trans = new UrlTransBitmap();
        Bitmap bitmap = trans.returnBitMap(mResetPriceImgUrl, mContext);
        if (bitmap != null) {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    if (palette == null) return;
                    if (palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                        createLinearGradientBitmap(palette.getDarkVibrantColor(Color.TRANSPARENT),
                                palette.getDarkVibrantColor(Color.TRANSPARENT));
                    } else if (palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                        createLinearGradientBitmap(palette.getDarkMutedColor(Color.TRANSPARENT),
                                palette.getDarkMutedColor(Color.TRANSPARENT));
                    } else {
                        createLinearGradientBitmap(palette.getDarkMutedColor(Color.TRANSPARENT),
                                palette.getDarkMutedColor(Color.TRANSPARENT));
                    }
                }
            });
        }
    }

    // 创建线性渐变背景色
    private void createLinearGradientBitmap(int darkColor, int color) {
        int bgColors[] = new int[2];
        bgColors[0] = darkColor;
        bgColors[1] = color;
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_4444);
        }
        if (mCanvas == null) {
            mCanvas = new Canvas();
        }
        if (mPaint == null) {
            mPaint = new Paint();
        }
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        LinearGradient gradient = new LinearGradient(0, 0, 0, mBitmap.getHeight(), bgColors[0], bgColors[1], Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);
        RectF rectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mCanvas.drawRect(rectF, mPaint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRvView.setBackground(new BitmapDrawable(getResources(), mBitmap));
        }
    }*/


    // 控制头部尾部的显隐
    private void updateHeaderAndFooter() {
        if (mIsHidden) {
            mHeaderView.animate().translationY(0);
            mFooterView.animate().translationY(0);
        } else {
            mHeaderView.animate().translationY(-mHeaderView.getMeasuredHeight());
            mFooterView.animate().translationY(mFooterView.getMeasuredHeight());
            // 隐藏输入法
            hiddenInputMethod(mRvView, this);
        }
        mIsHidden = !mIsHidden;
    }

    // 隐藏输入法
    public static void hiddenInputMethod(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
