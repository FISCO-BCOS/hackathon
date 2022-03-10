package com.maskview.mainView.forgetPwd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.maskview.R;
import com.maskview.dao.CheckString;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.login.LoginByPassword;
import com.maskview.util.TimeCountDown;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPwdCode extends AppCompatActivity implements View.OnClickListener {

    private EditText et_PhoneNum;
    private EditText et_PhoneCode;
    private String phoneNum;
    private String phoneCode;

    private Context mContext;
    private UserRequest ur;
    private TimeCountDown timeCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd_code);
        mContext = this;
        initView();
    }

    private void initView() {
        ImageView iv_finishThisActivity = findViewById(R.id.forgetPwd_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(this);
        et_PhoneNum = findViewById(R.id.et_forgetPwd_phoneNum);
        et_PhoneCode = findViewById(R.id.et_forgetPwd_code);
        Button bt_sendCode = findViewById(R.id.bt_forgetPwd_sendCode);
        bt_sendCode.setOnClickListener(this);
        Button bt_jump_resetPwd = findViewById(R.id.bt_jump_resetPwd);
        bt_jump_resetPwd.setOnClickListener(this);

        ur = new UserRequest();
        timeCountDown = new TimeCountDown(60000, 1000, bt_sendCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_forgetPwd_sendCode:
                sendCode();
                break;
            case R.id.bt_jump_resetPwd:
                jumpResetPwd();
                break;
            case R.id.forgetPwd_finishThisActivity:
                ForgetPwdCode.this.finish();
                break;
        }
    }


    private void sendCode() {
        if (!checkPhoneNum()) {
            Toast.makeText(mContext, "手机号格式有误", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result = ur.sendPhoneCode(phoneNum, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(result);
                                String note = (String) jsonObject.get("note");
                                if (result.contains("true")) {
                                    timeCountDown.start();
                                    Toast.makeText(mContext, "验证码已发送", Toast.LENGTH_LONG).show();
                                    Log.e("--------------", "请求验证码 : " + result);
                                } else if (note.equals("该手机号未注册")) {
                                    Toast.makeText(mContext, "跳转注册界面", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mContext, note, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    private void jumpResetPwd() {
        phoneCode = et_PhoneCode.getText().toString().trim();
        if (!checkPhoneNum()) {
            Toast.makeText(mContext, "手机号格式有误", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result = ur.forgetPwdCheckCode(phoneNum, phoneCode, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.contains("true")) {
                                Intent intent = new Intent(ForgetPwdCode.this, ResetPwd.class);
                                intent.putExtra("phoneNum", phoneNum);
                                intent.putExtra("phoneCode", phoneCode);
                                ForgetPwdCode.this.finish();
                                startActivity(intent);
                            } else if (result.contains("false")) {
                                Toast.makeText(mContext, "验证码错误", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "服务器未响应", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).start();
        }

    }

    private boolean checkPhoneNum() {
        phoneNum = et_PhoneNum.getText().toString().trim();
        return !TextUtils.isEmpty(phoneNum) && CheckString.isMobile(phoneNum);
    }
}
