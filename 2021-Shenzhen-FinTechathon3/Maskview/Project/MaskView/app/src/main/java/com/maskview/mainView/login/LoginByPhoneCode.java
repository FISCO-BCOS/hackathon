package com.maskview.mainView.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maskview.R;
import com.maskview.dao.CheckString;
import com.maskview.dao.SPUtils;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.MaskView;
import com.maskview.mainView.register.Register;
import com.maskview.util.TimeCountDown;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginByPhoneCode extends AppCompatActivity implements View.OnClickListener {

    private EditText et_PhoneNum;
    private EditText et_PhoneCode;
    private String phoneNum;
    private String phoneCode;
    private TimeCountDown timeCountDown;
    private Context mContext;
    private UserRequest ur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_phone_code);
        mContext = this;
        initView();
    }

    private void initView() {
        ImageView iv_finishThisActivity = findViewById(R.id.loginByCode_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(this);
        TextView tv_jump_loginByPwd = findViewById(R.id.jump_loginByPwd);
        tv_jump_loginByPwd.setOnClickListener(this);
        et_PhoneNum = findViewById(R.id.et_loginByCode_PhoneNum);
        et_PhoneCode = findViewById(R.id.et_loginByCode_Code);
        Button bt_sendCode = findViewById(R.id.bt_loginByCode_sendCode);
        bt_sendCode.setOnClickListener(this);
        Button bt_loginByCode = findViewById(R.id.bt_loginByCode);
        bt_loginByCode.setOnClickListener(this);
        timeCountDown = new TimeCountDown(60000, 1000, bt_sendCode);
        ur = new UserRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginByCode_finishThisActivity:
                LoginByPhoneCode.this.finish();
                break;
            case R.id.jump_loginByPwd:
                Intent intent = new Intent(LoginByPhoneCode.this, LoginByPassword.class);
                intent.putExtra("inputPhoneByCode", et_PhoneNum.getText().toString());
                LoginByPhoneCode.this.finish();
                startActivity(intent);
                break;
            case R.id.bt_loginByCode_sendCode:
                sendCode();
                break;
            case R.id.bt_loginByCode:
                login();
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
                            if (!result.equals("")) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(result);
                                    String tag = jsonObject.get("result") + "";
                                    if (tag.equals("true")) {
                                        timeCountDown.start();
                                        Toast.makeText(mContext, "验证码已发送", Toast.LENGTH_LONG).show();
                                        Log.e("--------------", "请求验证码 : " + result);
                                    } else {
                                        Toast.makeText(mContext, "验证码发送失败", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(mContext, "服务器未响应", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    private void login() {
        phoneCode = et_PhoneCode.getText().toString().trim();
        if (!checkPhoneNum()) {
            Toast.makeText(mContext, "手机号格式有误", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result = ur.loginByCode(phoneNum, phoneCode, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!result.equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String tag = jsonObject.get("result") + "";
                                    Log.e("--------------", "tag: " + tag);
                                    String note = jsonObject.get("note") + "";
                                    if (tag.equals("true")) {
                                        if (note.equals("请注册")) {
                                            Intent intent = new Intent(LoginByPhoneCode.this, Register.class);
                                            intent.putExtra("register_phoneNumber", phoneNum);
                                            startActivity(intent);
                                        } else {
                                            //登录成功后,截取服务器返回的token值
                                            SPUtils.put(mContext, "phoneNumber", phoneNum);
                                            SPUtils.put(mContext, "code", phoneCode);
                                            UtilParameter.myToken = jsonObject.get("token") + "";
                                            UtilParameter.myPoints = jsonObject.get("points") + "";
                                            UtilParameter.myPhoneNumber = phoneNum;
                                            UtilParameter.myNickName = jsonObject.get("userName") + "";
                                            Intent intent = new Intent(LoginByPhoneCode.this, MaskView.class);
                                            intent.putExtra("fragmentID", 3);
                                            LoginByPhoneCode.this.finish();
                                            startActivity(intent);
                                        }
                                    } else {
                                        Toast.makeText(mContext, note, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(mContext, "服务器未响应,请稍后再试!", Toast.LENGTH_SHORT).show();
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
