package com.maskview.mainView.updatePwd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.maskview.R;
import com.maskview.dao.CheckString;
import commaskview.dao.SPUtils;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.MaskView;
import com.maskview.mainView.login.LoginByPassword;
import com.maskview.mainView.login.LoginByPhoneCode;
import com.maskview.util.UtilParameter;

public class UpdatePwd extends AppCompatActivity implements View.OnClickListener {

    private EditText et_inputPwd;
    private EditText et_confirmPwd;
    private String inputPwd;
    private String confirmPwd;

    private Context mContext;
    private UserRequest ur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        initView();
    }

    private void initView() {
        mContext = this;
        ImageView iv_finishThisActivity = findViewById(R.id.updatePwd_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(this);
        et_inputPwd = findViewById(R.id.et_updatePwd_inputPwd);
        et_confirmPwd = findViewById(R.id.et_updatePwd_confirmPwd);
        Button bt_submitUpdatePwd = findViewById(R.id.bt_updatePwd_submit);
        bt_submitUpdatePwd.setOnClickListener(this);
        ur = new UserRequest();
    }

    private void updatePwd() {
        if (!checkPwd()) {
            Toast.makeText(mContext, "密码格式有误", Toast.LENGTH_SHORT).show();
        } else if (!inputPwd.equals(confirmPwd)) {
            Toast.makeText(mContext, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = ur.updatePwd(inputPwd, UtilParameter.myToken);
                    if (result.contains("true")) {
                        //修改成功,自动注销,进入登录界面
                        Intent intent = new Intent(UpdatePwd.this, MaskView.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        SPUtils.clear(mContext);
                        UtilParameter.myNickName = null;
                        UtilParameter.myPhoneNumber = null;
                        UtilParameter.myPoints = null;
                        UtilParameter.myToken = null;
                        intent.putExtra("fragmentID", 0);
                        startActivity(intent);
                    }
                }
            }).start();
        }
    }

    private boolean checkPwd() {
        inputPwd = et_inputPwd.getText().toString().trim();
        confirmPwd = et_confirmPwd.getText().toString().trim();
        return !TextUtils.isEmpty(inputPwd) && !TextUtils.isEmpty(confirmPwd) &&
                CheckString.isPassword(inputPwd) && CheckString.isPassword(confirmPwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updatePwd_finishThisActivity:
                finish();
                overridePendingTransition(0, R.anim.out_to_right);
                break;
            case R.id.bt_updatePwd_submit:
                updatePwd();
                break;
        }
    }

    //手机自带返回键操作
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, R.anim.out_to_right);
        }
        return super.dispatchKeyEvent(event);
    }
}
