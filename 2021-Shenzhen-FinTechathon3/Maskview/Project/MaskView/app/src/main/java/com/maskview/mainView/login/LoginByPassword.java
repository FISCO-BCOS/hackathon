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
import com.maskview.mainView.forgetPwd.ForgetPwdCode;
import com.maskview.util.GetIP;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginByPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText et_PhoneNum;
    private EditText et_Pwd;
    private String phoneNumber;
    private String password;
    private Context mContext;

    private UserRequest ur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_password);
        mContext = this;
        initView();
    }

    private void initView() {
        String userPhone = getIntent().getStringExtra("inputPhoneByCode");
        ImageView iv_finishThisActivity = findViewById(R.id.loginByPwd_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(this);
        TextView tv_forgetPwd = findViewById(R.id.jump_forgetPwd);
        tv_forgetPwd.setOnClickListener(this);
        et_PhoneNum = findViewById(R.id.et_loginByPwd_PhoneNum);
        et_PhoneNum.setText(userPhone);
        et_Pwd = findViewById(R.id.et_loginByPwd_Pwd);
        Button bt_loginByPwd = findViewById(R.id.bt_loginByPwd);
        bt_loginByPwd.setOnClickListener(this);
        ur = new UserRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginByPwd_finishThisActivity:
                LoginByPassword.this.finish();
                break;
            case R.id.jump_forgetPwd:
                jumpForgetPwd();
                break;
            case R.id.bt_loginByPwd:
                login();
                break;
        }
    }

    private void jumpForgetPwd() {
        Intent intent = new Intent(LoginByPassword.this, ForgetPwdCode.class);
        startActivity(intent);
    }

    private void login() {
        phoneNumber = et_PhoneNum.getText().toString().trim();
        password = et_Pwd.getText().toString().trim();
        if (!checkPhoneNum()) {
            Toast.makeText(mContext, "手机号格式有误", Toast.LENGTH_LONG).show();
        } else if (!checkPwd()) {
            Toast.makeText(mContext, "密码格式有误", Toast.LENGTH_LONG).show();
        } else {
            //访问网络放在独立线程中(不是主线程)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result = ur.loginByPwd(phoneNumber, password, null);
                    //线程问题,Toast
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!result.equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String tag = jsonObject.get("result") + "";
                                    String token = jsonObject.get("token") + "";
                                    if (tag.equals("true")) {
                                        String points = jsonObject.get("points") + "";
                                        String userName = jsonObject.get("userName") + "";
                                        //登录成功后,截取服务器返回的token值
                                        UtilParameter.myToken = token;
                                        UtilParameter.myPoints = points;
                                        UtilParameter.myPhoneNumber = phoneNumber;
                                        UtilParameter.myNickName = userName;
                                        SPUtils.put(mContext, "phoneNumber", phoneNumber);
                                        SPUtils.put(mContext, "password", password);
                                        Intent intent = new Intent(LoginByPassword.this, MaskView.class);
                                        intent.putExtra("fragmentID", 3);
                                        LoginByPassword.this.finish();
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();
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

    /**
     * 校验输入的手机号格式
     */
    private boolean checkPhoneNum() {
        phoneNumber = et_PhoneNum.getText().toString().trim();
        return !TextUtils.isEmpty(phoneNumber) && CheckString.isMobile(phoneNumber);
    }

    /**
     * 校验输入的密码格式
     */
    private boolean checkPwd() {
        password = et_Pwd.getText().toString().trim();
        return !et_Pwd.getText().toString().contains(" ") && CheckString.isPassword(password) && !TextUtils.isEmpty(password);
    }
}
