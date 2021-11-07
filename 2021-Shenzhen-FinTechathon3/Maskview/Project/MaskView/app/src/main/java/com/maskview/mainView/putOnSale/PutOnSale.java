package com.maskview.mainView.putOnSale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maskview.R;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.MaskView;
import com.maskview.util.API29ImgBean;
import com.maskview.util.GetSDPath;
import com.maskview.util.ToastUtil;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PutOnSale extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private EditText mEtTopic;
    private EditText mEtPrice;
    private TextView mTvAllCount;
    private Context mContext;
    private PutOnSaleAdapter mAdapter;
    private UserRequest mRequest;
    private List<API29ImgBean> mGetBeanList;
    private List<String> mGetImgPathList;  // < API29
    private List<Uri> mSaleImgUriList;  // >= API29
    private AlertDialog waitingAlertDialog;
    private final static int SALE_WAITING = 0;
    private final static int SALE_NO_RESPONSE = 1;
    private final static int SALE_ERROR = 2;
    private int waitingTag;
    private String mPostTopic;
    private String mPostPrice;
    private String note;  // 出错信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_on_sale);
        initView();
    }

    private void initView() {
        mContext = this;
        mRequest = new UserRequest();
        mRecyclerView = findViewById(R.id.recycler_putOnSale);
        mEtTopic = findViewById(R.id.et_put_on_sale_topic);
        mEtPrice = findViewById(R.id.et_put_on_sale_price);
        mTvAllCount = findViewById(R.id.tv_put_on_sale_allCount);
        Button btConfirmSale = findViewById(R.id.bt_put_on_sale_confirmSale);
        btConfirmSale.setOnClickListener(this);
        getData();
    }

    /**
     * 接收Intent传递的数据
     */
    private void getData() {
        // API>=29, 接收传过来的API29ImgBean集合, 拿到里边的Uri, 根据Uri显示, 并复制到私有目录, 进行上传
        // API<29, 接收传过来的imgPath集合, 将path转换为Uri, 根据Uri显示, 根据path转换相应file, 进行上传
        mSaleImgUriList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 29) {
            mGetBeanList = getIntent().getParcelableArrayListExtra("selectedImgBeanList");
            if (mGetBeanList != null) {
                for (int i = 0; i < mGetBeanList.size(); i++) {
                    mSaleImgUriList.add(mGetBeanList.get(i).getImgUri());
                }
            }
        } else {
            mGetImgPathList = getIntent().getStringArrayListExtra("selectedSellImgPathList");
            if (mGetImgPathList != null) {
                for (int i = 0; i < mGetImgPathList.size(); i++) {
                    mSaleImgUriList.add(Uri.fromFile(new File(mGetImgPathList.get(i))));
                }
            }
        }
        // 设置EditText换行键enter无效
        mEtTopic.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        mEtPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        setAdapter();
    }

    /**
     * 设置相应数据及Recycler适配
     */
    @SuppressLint("SetTextI18n")
    private void setAdapter() {
        // 设置显示照片张数
        int count = Build.VERSION.SDK_INT >= 29 ? mGetBeanList.size() : mGetImgPathList.size();
        mTvAllCount.setText("共 " + count + " 张");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        // 设置item上下间距
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 25;
                outRect.bottom = 25;
            }
        });
        mAdapter = new PutOnSaleAdapter(mSaleImgUriList, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_put_on_sale_confirmSale:
                if (checkEmpty()) {
                    waitingTag = 1;
                    loadingAlert();
                    if (Build.VERSION.SDK_INT >= 29) {
                        confirmSaleHighAPI();
                    } else {
                        confirmSaleLowAPI();
                    }
                }
                break;
        }
    }

    /**
     * 判空处理
     */
    private boolean checkEmpty() {
        // 判空处理
        mPostPrice = String.valueOf(Integer.parseInt(mEtPrice.getText().toString().trim()));
        mPostTopic = mEtTopic.getText().toString().trim();
        if (mPostTopic.equals("")) {
            ToastUtil.showToast(mContext, "标题不能为空", 0, 30);
            return false;
        } else if (mPostPrice.equals("")) {
            ToastUtil.showToast(mContext, "价钱不能为空", 0, 30);
            return false;
        } else if (Integer.parseInt(mPostPrice) == 0) {
            ToastUtil.showToast(mContext, "价钱不能为0", 0, 30);
            return false;
        }
        return true;
    }

    /**
     * 确认上架, API>=29
     */
    private void confirmSaleHighAPI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mGetBeanList.size(); i++) {
                    API29ImgBean bean = mGetBeanList.get(i);
                    DocumentFile documentFile = DocumentFile.fromSingleUri(mContext, bean.getImgUri());
                    // 所选的共享目录下图片名称---名称带类型:xxx.jpg
                    final String imgName = documentFile.getName();
                    // 将选中的共享目录的照片复制到私有目录, 并获取的复制后的私有路径
                    String privatePath = GetSDPath.getPrivatePath(mContext, bean.getImgUri(), imgName);
                    // 将私有目录下的照片Path->File,上传
                    final File file = new File(privatePath);
                    Map<String, String> params = new HashMap<>();
                    params.put("imgPrice", mPostPrice);
                    params.put("imgTopic", mPostTopic);
                    // 上传图片,主题及价格
                    String jsonResult = mRequest.uploadFileAndParams(UtilParameter.secureSaleUrl,
                            file, "file", params, imgName, UtilParameter.myToken);
                    if (!jsonResult.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResult);
                            String result = jsonObject.get("result") + "";
                            note = jsonObject.getString("note");
                            if (result.equals("true")) {
                                waitingTag++;
                            } else {
                                waitingOrNoResponseHandler.sendEmptyMessage(SALE_ERROR);
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 未响应
                        waitingOrNoResponseHandler.sendEmptyMessage(SALE_NO_RESPONSE);
                        return;
                    }
                }
            }
        }).start();
    }

    /**
     * 确认上架, API<29
     */
    private void confirmSaleLowAPI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mGetImgPathList.size(); i++) {
                    File file = new File(mGetImgPathList.get(i));
                    String imgName = file.getName();
                    Map<String, String> params = new HashMap<>();
                    params.put("imgPrice", mPostPrice);
                    params.put("imgTopic", mPostTopic);
                    // 上传图片,主题及价格
                    String jsonResult = mRequest.uploadFileAndParams(UtilParameter.secureSaleUrl,
                            file, "file", params, imgName, UtilParameter.myToken);
                    if (!jsonResult.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResult);
                            String result = jsonObject.get("result") + "";
                            note = jsonObject.getString("note");
                            if (result.equals("true")) {
                                waitingTag++;
                            } else {
                                waitingOrNoResponseHandler.sendEmptyMessage(SALE_ERROR);
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 未响应
                        waitingOrNoResponseHandler.sendEmptyMessage(SALE_NO_RESPONSE);
                        return;
                    }
                }
            }
        }).start();
    }

    /**
     * 上架等待提示
     */
    private void loadingAlert() {
        View view = View.inflate(getApplicationContext(), R.layout.alert_sell_waiting, null);
        waitingAlertDialog = new AlertDialog.Builder(this).setView(view).create();
        waitingAlertDialog.setTitle("正在上传,请耐心等待......");
        waitingAlertDialog.show();
        waitingOrNoResponseHandler.sendEmptyMessage(SALE_WAITING);
    }

    private Handler waitingOrNoResponseHandler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == SALE_WAITING) {
                TextView tv_now = waitingAlertDialog.findViewById(R.id.loading_nowCount);
                TextView tv_all = waitingAlertDialog.findViewById(R.id.loading_allCount);
                if (tv_now != null && tv_all != null) {
                    tv_now.setText(waitingTag + "");
                    tv_all.setText("/" + mSaleImgUriList.size());
                }
                if (waitingAlertDialog.isShowing()) {
                    waitingOrNoResponseHandler.sendEmptyMessageDelayed(0, 1000);
                    if (waitingTag == mSaleImgUriList.size() + 1) {
                        waitingAlertDialog.cancel();
                        saleFinishAlert();
                    }
                }
            } else if (msg.what == SALE_NO_RESPONSE) {
                // 未响应
                waitingAlertDialog.cancel();
                AlertDialog noResponseAlertDialog = new AlertDialog.Builder(mContext).setMessage("服务器未响应,请稍后重试")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
                noResponseAlertDialog.show();
            } else if (msg.what == SALE_ERROR) {
                // 某步出错, 打印note
                waitingAlertDialog.cancel();
                ToastUtil.showToast(mContext, note, 0, 0);
            }
            return false;
        }
    });

    /**
     * 上架完成
     */
    private void saleFinishAlert() {
        AlertDialog finishAlertDialog = new AlertDialog.Builder(this).setMessage("上架成功")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PutOnSale.this, MaskView.class);
                        intent.putExtra("fragmentID", 1);
                        startActivity(intent);
                        finish();
                    }
                }).create();
        finishAlertDialog.show();
    }
}
