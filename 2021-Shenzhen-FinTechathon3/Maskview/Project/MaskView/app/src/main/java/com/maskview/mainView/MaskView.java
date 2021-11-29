package com.maskview.mainView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.maskview.R;
import com.maskview.dao.ImageUriUtil;
import com.maskview.dao.SPUtils;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.confirmImg.ConfirmImage;
import com.maskview.mainView.displayHall.DisplayHall;
import com.maskview.mainView.mine.Mine;
import com.maskview.mainView.mgGoods.MyGoods;
import com.maskview.mainView.shoppingCart.ShoppingCart;
import com.maskview.mainView.login.LoginByPhoneCode;
import com.maskview.util.GlideImageEngine;
import com.maskview.util.UtilParameter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MaskView extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup bottomGroup;
    private ImageView iv_confirmImg;
    private DisplayHall frag_displayHall;
    private MyGoods frag_myGoods;
    private ShoppingCart frag_shoppingCar;
    private Mine frag_mine;
    private Fragment[] fragments;
    private int lastFragment;

    private static final int REQUEST_CODE_CHOOSE = 0x13;
    private Context mContext;
    private ArrayList<String> selectedImgName;
    private ArrayList<String> selectedImgPath;

    private SharedPreferences sprfMain;
    SharedPreferences.Editor editorMain;

    private RadioButton radioButton_myGoods;
    private RadioButton radioButton_mine;
    private RadioButton radioButton_displayHall;
    private RadioButton radioButton_shoppingCart;

    private UserRequest ur;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask_view);
        getScreenSize();

        sprfMain = PreferenceManager.getDefaultSharedPreferences(this);
        editorMain = sprfMain.edit();

        mContext = this;

        iv_confirmImg = findViewById(R.id.bt_ConfirmImg);
        iv_confirmImg.setOnClickListener(this);

        radioButton_displayHall = findViewById(R.id.radioBt_displayHall);
        radioButton_myGoods = findViewById(R.id.radioBt_mgGoods);
        radioButton_mine = findViewById(R.id.radioBt_mine);
        radioButton_shoppingCart = findViewById(R.id.radioBt_shoppingCart);

        initFragment();

        int fragmentID = getIntent().getIntExtra("fragmentID", 16);
        if (fragmentID == 3) {
            //底部导航栏选中我的
            radioButton_displayHall.setEnabled(true);
            radioButton_myGoods.setEnabled(true);
            radioButton_mine.setEnabled(true);
            radioButton_shoppingCart.setEnabled(true);
            iv_confirmImg.setEnabled(true);
            radioButton_mine.setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, frag_mine).show(frag_mine).commit();
            lastFragment = 3;
        } else if (fragmentID == 1) {
            //底部导航栏选中作品
            radioButton_displayHall.setEnabled(true);
            radioButton_myGoods.setEnabled(true);
            radioButton_mine.setEnabled(true);
            radioButton_shoppingCart.setEnabled(true);
            iv_confirmImg.setEnabled(true);
            radioButton_myGoods.setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, frag_myGoods).show(frag_myGoods).commit();
            lastFragment = 1;
        } else if (fragmentID == 0) {
            //底部导航栏选中展厅
            radioButton_displayHall.setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, frag_displayHall).show(frag_displayHall).commit();
            lastFragment = 0;
        }

        //检查并申请权限(SD卡)
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        checkLoginBefore();
    }


    /**
     * 检查是否登陆过
     */
    private void checkLoginBefore() {
        ur = new UserRequest();
        final String phone = (String) SPUtils.get(mContext, "phoneNumber", "");
        final String password = (String) SPUtils.get(mContext, "password", "");
        final String code = (String) SPUtils.get(mContext, "code", "");
        if (phone != null && !phone.equals("") && password != null && !password.equals("")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = ur.loginByPwd(phone, password, null);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (result.contains("true")) {
                            UtilParameter.myPhoneNumber = phone;
                            UtilParameter.myPoints = jsonObject.get("points") + "";
                            UtilParameter.myToken = jsonObject.get("token") + "";
                            UtilParameter.myNickName = jsonObject.get("userName") + "";
                        } else {
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else if (phone != null && !phone.equals("") && code != null && !code.equals("")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = ur.autoLoginByCode(phone, code, null);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (result.contains("true")) {
                            UtilParameter.myPhoneNumber = phone;
                            UtilParameter.myPoints = jsonObject.get("points") + "";
                            UtilParameter.myToken = jsonObject.get("token") + "";
                            UtilParameter.myNickName = jsonObject.get("userName") + "";
                        } else {
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            return;
        }
    }

    /**
     * 获取屏幕的宽高
     */
    private void getScreenSize() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        UtilParameter.mScreenWidth = point.x;
        UtilParameter.mScreenHeight = point.y;
    }

    private void initFragment() {
        frag_displayHall = new DisplayHall();
        frag_myGoods = new MyGoods();
        frag_shoppingCar = new ShoppingCart();
        frag_mine = new Mine();
        fragments = new Fragment[]{frag_displayHall, frag_myGoods, frag_shoppingCar, frag_mine};

        lastFragment = 0;

        RadioButton radioButton_displayHall = findViewById(R.id.radioBt_displayHall);
        radioButton_displayHall.setChecked(true);  //第一个fragment为选中状态
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, frag_displayHall).
                show(frag_displayHall).commit();  //初始化显示fragment1
        bottomGroup = findViewById(R.id.rg_main);
        bottomGroup.setOnCheckedChangeListener(this);
    }


    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastFragment]);//隐藏上个Fragment
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.fl_fragment, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }


    /**
     * 检查是否登录
     */
    private boolean checkLogin() {
        return UtilParameter.myPhoneNumber != null;
    }

    private void jumpLoginByCode() {
        Intent intent = new Intent(MaskView.this, LoginByPhoneCode.class);
        startActivity(intent);
        RadioButton radioButton_displayHall = findViewById(R.id.radioBt_displayHall);
        radioButton_displayHall.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioBt_displayHall:
                if (lastFragment != 0) {
                    switchFragment(lastFragment, 0);
                    lastFragment = 0;
                }
                break;
            case R.id.radioBt_mgGoods:
                if (!checkLogin()) {
                    jumpLoginByCode();
                } else {
                    if (lastFragment != 1) {
                        switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                }
                break;
            case R.id.radioBt_shoppingCart:
                if (!checkLogin()) {
                    jumpLoginByCode();
                } else {
                    if (lastFragment != 2) {
                        switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                }
                break;
            case R.id.radioBt_mine:
                if (!checkLogin()) {
                    jumpLoginByCode();
                } else {
                    if (lastFragment != 3) {
                        switchFragment(lastFragment, 3);
                        lastFragment = 3;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //跳转相册选择要确权的图片
            case R.id.bt_ConfirmImg:
                if (!checkLogin()) {
                    jumpLoginByCode();
                } else {
                    choseConfirmImg();
                }
                break;
        }
    }

    //跳转选取照片
    private void choseConfirmImg() {
        Matisse.from(MaskView.this)
                .choose(MimeType.ofImage(), false)
                .countable(true)  //true:选中后显示数字;false:选中后显示对号
                .maxSelectable(6) // 图片选择的最多数量
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideImageEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }


    //选择后,根据Uri获取图片名称
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE) {// 图库选择图片
                List<Uri> selectedImgUri = null;
                if (data != null) {
                    selectedImgUri = Matisse.obtainResult(data);
                    //获取图片路径 Uri--->Path
                    selectedImgPath = new ArrayList<>();
                    for (Uri uri : selectedImgUri) {
                        selectedImgPath.add(ImageUriUtil.getPhotoPathFromContentUri(mContext, uri));
                    }
                    //获取图片名称
                    selectedImgName = new ArrayList<>();
                    DocumentFile documentFile;
                    for (int i = 0; i < selectedImgUri.size(); i++) {
                        documentFile = DocumentFile.fromSingleUri(mContext, selectedImgUri.get(i));
                        if (documentFile != null) {
                            selectedImgName.add(documentFile.getName());
                            Log.e("-----", "图片名称 : " + documentFile.getName());
                        }
                    }
                }
                Intent intent = new Intent(MaskView.this, ConfirmImage.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedConfirmImgUri", (Serializable) selectedImgUri);
                bundle.putStringArrayList("selectedConfirmImgName", selectedImgName);
                bundle.putStringArrayList("selectedConfirmImgPath", selectedImgPath);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
