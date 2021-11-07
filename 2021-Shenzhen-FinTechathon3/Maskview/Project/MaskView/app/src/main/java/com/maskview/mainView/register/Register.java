package com.maskview.mainView.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maskview.R;
import com.maskview.dao.CheckString;
import com.maskview.dao.SPUtils;
import com.maskview.dao.UserRequest;
import com.maskview.mainView.MaskView;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Register extends AppCompatActivity {

    private EditText et_inputPwd;
    private EditText et_confirmPwd;
    private String inputPwd;
    private String confirmPwd;
    private String phoneNum;
    private Context mContext;
    private UserRequest ur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        initView();
    }

    private void initView() {
        //获取要注册的phoneNumber
        phoneNum = getIntent().getStringExtra("register_phoneNumber");

        et_inputPwd = findViewById(R.id.et_register_inputPwd);
        et_confirmPwd = findViewById(R.id.et_register_confirmPwd);
        Button bt_submitRegister = findViewById(R.id.bt_register_submit);
        bt_submitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        ur = new UserRequest();
    }

    private void register() {
        if (!checkPwd()) {
            Toast.makeText(mContext, "密码格式有误", Toast.LENGTH_SHORT).show();
        } else if (!inputPwd.equals(confirmPwd)) {
            Toast.makeText(mContext, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("TAG","开始注册....");
            //1.注册,数据入库 2.手机号密码自动登录
            ExecutorService es = Executors.newCachedThreadPool();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG","1");
                    final String result = ur.register(phoneNum, confirmPwd, null);
                    Log.e("TAG", "run: " + result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!result.equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String tag = jsonObject.get("result") + "";
                                    if (tag.equals("true")) {
                                        Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                                        //2.手机号密码自动登录
                                        autoLogin();
                                    } else {
                                        Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
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
            };
            es.submit(task);
            es.shutdown();
        }
    }


    private void autoLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String loginResult = ur.loginByPwd(phoneNum, confirmPwd, null);
                JSONObject loginJson = null;
                try {
                    loginJson = new JSONObject(loginResult);
                    String token = loginJson.get("token") + "";
                    String userPhone = loginJson.get("userPhone") + "";
                    String points = loginJson.get("points") + "";
                    String userName = loginJson.get("userName") + "";
                    //登录成功后,截取服务器返回的token值
                    UtilParameter.myToken = token;
                    UtilParameter.myPoints = points;
                    UtilParameter.myPhoneNumber = userPhone;
                    UtilParameter.myNickName = userName;
                    SPUtils.put(mContext, "phoneNumber", userPhone);
                    SPUtils.put(mContext, "password", confirmPwd);
                    Intent intent = new Intent(Register.this, MaskView.class);
                    intent.putExtra("fragmentID", 3);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private boolean checkPwd() {
        inputPwd = et_inputPwd.getText().toString().trim();
        confirmPwd = et_confirmPwd.getText().toString().trim();
        return !TextUtils.isEmpty(inputPwd) && !TextUtils.isEmpty(confirmPwd) &&
                CheckString.isPassword(inputPwd) && CheckString.isPassword(confirmPwd);
    }
}
