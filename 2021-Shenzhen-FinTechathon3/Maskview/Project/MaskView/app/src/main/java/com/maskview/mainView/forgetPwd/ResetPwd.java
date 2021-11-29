package com.maskview.mainView.forgetPwd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.maskview.R;
import com.maskview.dao.CheckString;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.login.LoginByPassword;

public class ResetPwd extends AppCompatActivity implements View.OnClickListener {

    private EditText et_newPwd;
    private EditText et_confirmNewPwd;
    private String newPwd;
    private String confirmNewPwd;
    private String phoneNum;
    private String phoneCode;

    private Context mContext;
    private UserRequest ur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        mContext = this;
        initView();


    }

    private void initView() {
        ImageView iv_finishThiActivity = findViewById(R.id.resetPwd_finishThisActivity);
        iv_finishThiActivity.setOnClickListener(this);
        et_newPwd = findViewById(R.id.et_newPwd);
        et_confirmNewPwd = findViewById(R.id.et_confirmNewPwd);
        Button bt_resetPwd = findViewById(R.id.bt_resetPwd);
        bt_resetPwd.setOnClickListener(this);

        ur = new UserRequest();
        phoneNum = getIntent().getStringExtra("phoneNum");
        phoneCode = getIntent().getStringExtra("phoneCode");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetPwd_finishThisActivity:
                ResetPwd.this.finish();
                break;
            case R.id.bt_resetPwd:
                resetPwd();
                break;
        }
    }

    private void resetPwd() {
        if (!checkPwd()) {
            Toast.makeText(mContext, "密码格式有误", Toast.LENGTH_SHORT).show();
        } else if (!newPwd.equals(confirmNewPwd)) {
            Toast.makeText(mContext, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result = ur.resetPwd(phoneNum, phoneCode, newPwd, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.contains("true")) {
                                Toast.makeText(mContext, "重置成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPwd.this, LoginByPassword.class);
                                startActivity(intent);
                            } else if (result.contains("false")) {
                                Toast.makeText(mContext, "重置失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "服务器未响应", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).start();
        }

    }

    private boolean checkPwd() {
        newPwd = et_newPwd.getText().toString().trim();
        confirmNewPwd = et_confirmNewPwd.getText().toString().trim();
        return !TextUtils.isEmpty(newPwd) && !TextUtils.isEmpty(confirmNewPwd) &&
                CheckString.isPassword(newPwd) && CheckString.isPassword(confirmNewPwd);
    }
}
