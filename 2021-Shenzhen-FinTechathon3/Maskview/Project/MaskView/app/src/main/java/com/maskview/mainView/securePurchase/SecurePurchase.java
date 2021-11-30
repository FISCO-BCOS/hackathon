package com.maskview.mainView.securePurchase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.maskview.R;
import com.maskview.dao.SecureDao;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.myPurchase.MyPurchase;
import com.maskview.util.GetSDPath;
import com.maskview.util.UtilParameter;
import com.maskview.watermark.Robustwatermark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class SecurePurchase extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvImage;
    private TextView mTvSeller;
    private TextView mTvImgPrice;
    private TextView mTvMyPoints;
    private TextView mTvOverage;
    private Context mContext;
    private String mGetImgUrl;
    private String mOriginImgPath;
    private String mGetSellerName;
    private String mGetImgPrice;
    private UserRequest mRequest;
    private Robustwatermark mRobust;
    private String mPurchaseImgName; // 要购买的图片名称
    private String mThumbnailImgPath;
    private AlertDialog mLoadingAlert;
    private int mProgress;
    private static final int WAITING = 0;
    private static final int SUCCESS = 1;
    private static final int NETWORK_ERROR = 2;
    private static final int FREEZE_60_SECONDS = 3; // 账户冻结60s
    private static final int DECODE_FAIL = 4; // base64解码失败
    private static final int WATERMARK_FAIL = 5; // 水印失败
    private static final int ALREADY_FREEZE = 6; // 已经处于冻结状态
    private static final int FREEZE_FAIL = 7; // 冻结失败
    private static final int SERVER_ERROR = 8; // 服务器内部异常
    private static final int TXT_HASH_ERROR = 9; // 图片源错误
    private static final int INELIGIBLE = 10; // 不符合安全交易条件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_purchase);
        initView();
    }

    private void initView() {
        mContext = this;
        mRequest = new UserRequest();
        mRobust = new Robustwatermark();
        mTvSeller = findViewById(R.id.tv_secure_seller);
        mTvImgPrice = findViewById(R.id.tv_secure_imgPrice);
        mTvMyPoints = findViewById(R.id.tv_secure_myPoints);
        mTvOverage = findViewById(R.id.tv_secure_overage);
        mIvImage = findViewById(R.id.iv_secure_image);
        Button mBtConfirmPurchase = findViewById(R.id.bt_secure_purchase_confirm);
        mBtConfirmPurchase.setOnClickListener(this);
        getInitData();
    }

    private void getInitData() {
        Intent intent = getIntent();
        mThumbnailImgPath = intent.getStringExtra("imgUrl"); // thumbnailImg/xx/xxx.jpg
        mPurchaseImgName = mThumbnailImgPath.substring(mThumbnailImgPath.indexOf("/con-") + 17); // 购买的图片名称 xxx.jpg, 方便水印
        mGetImgUrl = UtilParameter.IMAGES_IP + mThumbnailImgPath;
        int index = mThumbnailImgPath.indexOf("thumbnailImg/") + 13;
        mOriginImgPath = "img/" + mThumbnailImgPath.substring(index); // 原图路径 img/1/xxx.jpg
        mGetSellerName = intent.getStringExtra("sellerName");
        mGetImgPrice = intent.getStringExtra("imgPrice");
        showView();
    }

    @SuppressLint("SetTextI18n")
    private void showView() {
        Glide.with(mContext).load(mGetImgUrl).override(Target.SIZE_ORIGINAL).dontAnimate().into(mIvImage);
        mTvSeller.setText(mGetSellerName);
        mTvImgPrice.setText("¥\t" + mGetImgPrice);
        String myPoints = UtilParameter.myPoints;
        mTvMyPoints.setText("¥\t" + myPoints);
        int myPointsInt = Integer.parseInt(myPoints);
        int imgPrice = Integer.parseInt(mGetImgPrice);
        int overage = myPointsInt - imgPrice;
        if (overage >= 0) {
            // 余额充足, 显示购买
            mTvOverage.setText("¥\t" + overage);
        } else {
            // TODO:余额不足, 充值界面

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_secure_purchase_confirm:
                confirmSecurePurchase();
                break;
        }
    }

    /**
     * 安全购买
     */
    private void confirmSecurePurchase() {
        loadingAlert();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 1. 请求go, 获取图片相关信息
                String imgInfoResult = mRequest.getImgInfoSecureTra(mOriginImgPath, UtilParameter.myToken);
                if (imgInfoResult.equals("Network Error")) {
                    // 网络错误提示
                    mHandler.sendEmptyMessage(NETWORK_ERROR);
                    return;
                }
                mProgress = 1;
                JsonImgInfo jsonImgInfo = JSON.parseObject(imgInfoResult, JsonImgInfo.class);
                final int purchaseUid = jsonImgInfo.getPurchaseUid();
                final int imgPrice = jsonImgInfo.getData().getImgPrice();
                final int sellerUid = jsonImgInfo.getData().getSellerUid();
                if (jsonImgInfo.isResult()) {
                    // 2. 下载对应txt
                    String txtPath = jsonImgInfo.getData().getTxtPath();
                    final String txtName = txtPath.substring(txtPath.lastIndexOf("/") + 1);  // txt文件名称
                    String url = UtilParameter.IMAGES_IP + txtPath; // 下载txt地址
                    final File purchaseImgFile;
                    if (Build.VERSION.SDK_INT >= 29) {
                        purchaseImgFile = new File(mContext.getExternalFilesDir(null).getAbsolutePath());
                    } else {
                        purchaseImgFile = new File(GetSDPath.getSDPath(mContext) + "/MaskView购买");
                    }
                    if (!purchaseImgFile.exists()) {
                        boolean make = purchaseImgFile.mkdirs();
                        if (!make) {
                            Toast.makeText(mContext, "文件夹创建失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    final String privatePath = getFilesDir() + ""; // 私有目录: 下载的txt存放位置
                    final File txtFile = new File(privatePath + "/" + txtName);
                    SecureDao.download(url, privatePath, new SecureDao.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess() {
                            // 3. 对txt取哈希
                            String txtHash = SecureDao.fileHash(txtFile);
                            Log.e("", "txtHash: " + txtHash);
                            // 4. 请求java, 对比哈希是否相同
                            String txtHashResult = mRequest.compareHash(txtName);
                            if (txtHashResult.equals("Network Error")) {
                                // 对比哈希,网络错误提示,删除txt
                                if (txtFile.exists()) {
                                    txtFile.delete();
                                }
                                mHandler.sendEmptyMessage(NETWORK_ERROR);
                                return;
                            }
                            JsonCompareTxtHash jsonCompareTxtHash = JSON.parseObject(txtHashResult, JsonCompareTxtHash.class);
                            if (jsonCompareTxtHash.isResult()) {
                                // 5. 相同, 冻结积分->解密txt->水印;  不同,return
                                String freezePointsResult = mRequest.freezePoints(purchaseUid, imgPrice);
                                if (freezePointsResult.equals("Network Error")) {
                                    // 冻结积分,网络错误提示,删除txt
                                    if (txtFile.exists()) {
                                        txtFile.delete();
                                    }
                                    mHandler.sendEmptyMessage(NETWORK_ERROR);
                                    return;
                                }
                                JsonCommon jsonFreezePoints = JSON.parseObject(freezePointsResult, JsonCommon.class);
                                if (jsonFreezePoints.isResult()) {
                                    mProgress = 2;
                                    // 解码base64 : 1.读txt 2.解码
                                    String txtBase64 = SecureDao.readTxt(txtFile);
                                    String originPath = privatePath + "/" + mPurchaseImgName;  // base64图片保存的本机路径
                                    File originFile = new File(originPath);
                                    boolean base64ToFileResult = SecureDao.base64ToFile(txtBase64, originPath);
                                    if (base64ToFileResult) {
                                        FileOutputStream fos;
                                        // 6. 水印成功, 交易完成, 删除下载的txt及解密的图片, 解冻;
                                        Bitmap originBitmap = BitmapFactory.decodeFile(originPath);
                                        Bitmap copyBitmap = originBitmap.copy(Bitmap.Config.ARGB_8888, true);
                                        try {
                                            Bitmap markBitmap = mRobust.robustWatermark(copyBitmap, UtilParameter.myPhoneNumber, UtilParameter.DEAL_FLAG);
                                            String key = mRobust.getKey();
                                            String afterMarkImgName = "tra-" + UtilParameter.myPhoneNumber + "-" + mPurchaseImgName;
                                            //File file = new File(privatePath + "/" + afterMarkImgName);
                                            File file = new File(purchaseImgFile.getPath() + "/" + afterMarkImgName);
                                            // 上传服务器
                                            String succeedResult = mRequest.securePurchaseSucceed(purchaseUid, sellerUid, mThumbnailImgPath, imgPrice, key);
                                            if (succeedResult.equals("Network Error")) {
                                                // 水印完成上链未响应,网络错误提示, 并删除txt及图片, 服务器倒计时解冻
                                                if (originFile.exists()) {
                                                    originFile.delete();
                                                }
                                                if (txtFile.exists()) {
                                                    txtFile.delete();
                                                }
                                                if (file.exists()) {
                                                    file.delete();
                                                }
                                                mHandler.sendEmptyMessage(FREEZE_60_SECONDS);
                                                return;
                                            }
                                            JsonCommon purchaseSucceed = JSON.parseObject(succeedResult, JsonCommon.class);
                                            if (purchaseSucceed.isResult()) {
                                                fos = new FileOutputStream(file);
                                                //markBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                                markBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                                fos.close();
                                                if (Build.VERSION.SDK_INT >= 29) {
                                                    GetSDPath.scanFile(file, mContext);
                                                    // 删除txt(txtFile) 及 base64图(originFile) 及 私有目录的水印图(file)
                                                    if (originFile.exists()) {
                                                        originFile.delete();
                                                    }
                                                    if (txtFile.exists()) {
                                                        txtFile.delete();
                                                    }
                                                    if (file.exists()) {
                                                        file.delete();
                                                    }
                                                } else {
                                                    // 发送广播,刷新图库
                                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                                    Uri uri = Uri.fromFile(file);
                                                    intent.setData(uri);
                                                    sendBroadcast(intent);
                                                }
                                                mHandler.sendEmptyMessage(SUCCESS);
                                            } else {
                                                // 上传成功信息, 返回false, 服务器内部异常
                                                mHandler.sendEmptyMessage(SERVER_ERROR);
                                            }
                                        } catch (IOException e) {
                                            // 水印失败
                                            mHandler.sendEmptyMessage(WATERMARK_FAIL);
                                            e.printStackTrace();
                                        }
                                    } else {
                                        // base64解码失败提示,通知解冻
                                        mRequest.returnPoints(purchaseUid);
                                        mHandler.sendEmptyMessage(DECODE_FAIL);
                                        Log.e("解码---", "result: 解码失败");
                                    }
                                } else if (jsonFreezePoints.getNote().equals("已经是冻结状态")) {
                                    // 已经是冻结状态
                                    mHandler.sendEmptyMessage(ALREADY_FREEZE);
                                } else {
                                    // 积分冻结失败
                                    mHandler.sendEmptyMessage(FREEZE_FAIL);
                                }
                            } else {
                                // hash不同, 不是同一张图提示
                                mHandler.sendEmptyMessage(TXT_HASH_ERROR);
                            }
                        }

                        @Override
                        public void onDownloading(int progress) {

                        }

                        @Override
                        public void onDownloadFailed() {
                            Log.e("", "onDownloadSuccess: 下载失败");
                        }
                    });
                } else if (jsonImgInfo.getNote().equals(UtilParameter.SECURE_TRADE_INELIGIBLE)) {
                    mHandler.sendEmptyMessage(INELIGIBLE);
                } else {
                    mHandler.sendEmptyMessage(SERVER_ERROR);
                }
            }
        }).start();


    }

    /**
     * 安全购买loading
     */
    private void loadingAlert() {
        mProgress = 0;
        View view = View.inflate(mContext, R.layout.alert_loading, null);
        mLoadingAlert = new AlertDialog.Builder(mContext).setView(view).create();
        mLoadingAlert.show();
        mHandler.sendEmptyMessage(WAITING);
    }

    /**
     * Handler刷新主线程界面
     */
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            View viewNote = View.inflate(mContext, R.layout.alert_note, null);
            final AlertDialog alertDialogNote = new AlertDialog.Builder(mContext).setView(viewNote).create();
            TextView textViewNote = viewNote.findViewById(R.id.alert_tv_note);
            Button buttonNote = viewNote.findViewById(R.id.alert_bt_secureTrade_note);
            buttonNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogNote.cancel();
                }
            });
            if (msg.what == WAITING) {
                TextView textView = mLoadingAlert.findViewById(R.id.alert_tv_loading);
                mHandler.sendEmptyMessageDelayed(0, 500);
                if (mProgress == 1) {
                    // 正在进行安全验证
                    Objects.requireNonNull(textView).setText(R.string.secureTrade_text1);
                } else if (mProgress == 2) {
                    // 正在进行加密操作
                    Objects.requireNonNull(textView).setText(R.string.secureTrade_text2);
                }
            } else if (msg.what == SUCCESS) {
                // 成功
                mLoadingAlert.cancel();
                // 购买完成后,刷新我的积分
                int myPoints = Integer.parseInt(UtilParameter.myPoints);
                myPoints = (myPoints - Integer.parseInt(mGetImgPrice));
                UtilParameter.myPoints = myPoints + "";
                View view = View.inflate(mContext, R.layout.alert_purchase_succeed, null);
                ImageView imageView = view.findViewById(R.id.alert_iv_purchase_succeed_showImg);
                Glide.with(view).load(mGetImgUrl).dontAnimate().into(imageView);
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).setView(view).create();
                alertDialog.show();
                Button button = view.findViewById(R.id.alert_bt_secureTrade_success);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MyPurchase.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (msg.what == NETWORK_ERROR) {
                // 网络错误
                mLoadingAlert.cancel();
                View view = View.inflate(mContext, R.layout.alert_network_error, null);
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).setView(view).create();
                alertDialog.show();
            } else if (msg.what == FREEZE_60_SECONDS) {
                // 网络错误, 提示账户冻结60s
                mLoadingAlert.cancel();
                alertDialogNote.show();
            } else if (msg.what == DECODE_FAIL) {
                // base64解码失败
                mLoadingAlert.cancel();
                textViewNote.setText(R.string.secureTra_base64Fail);
                alertDialogNote.show();
            } else if (msg.what == WATERMARK_FAIL) {
                // 水印失败
                mLoadingAlert.cancel();
                textViewNote.setText(R.string.secureTra_markFail);
                alertDialogNote.show();
            } else if (msg.what == ALREADY_FREEZE) {
                // 已经处于冻结状态
                mLoadingAlert.cancel();
                textViewNote.setText(R.string.secureTra_alreadyFreeze);
                alertDialogNote.show();
            } else if (msg.what == FREEZE_FAIL) {
                // 冻结失败
                mLoadingAlert.cancel();
                textViewNote.setText(R.string.secureTra_freezeFail);
                alertDialogNote.show();
            } else if (msg.what == SERVER_ERROR) {
                // 服务器内部异常
                mLoadingAlert.cancel();
                textViewNote.setText(R.string.server_error);
                alertDialogNote.show();
            } else if (msg.what == TXT_HASH_ERROR) {
                // 不是同一张图片
                mLoadingAlert.cancel();
                textViewNote.setText(R.string.secureTra_hashError);
                alertDialogNote.show();
            } else if (msg.what == INELIGIBLE) {
                // 不符合安全交易条件
                mLoadingAlert.cancel();
                textViewNote.setText(R.string.secureTra_ineligible);
                alertDialogNote.show();
            }
            return false;
        }
    });

    /**
     * 本机自带返回键
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
