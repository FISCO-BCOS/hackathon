package com.maskview.mainView.confirmImg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maskview.R;
import com.maskview.mainView.MaskView;
import com.maskview.util.UtilParameter;

import java.util.ArrayList;

public class ConfirmResult extends AppCompatActivity {

    private TextView mTvConfirmResult;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private int mGetSuccessCount;
    private ArrayList<ConfirmResultBean> mGetConfirmResultBeanList;
    private ConfirmResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_confirm_result);
        setDialogSize();
    }

    private void setDialogSize() {
        // 获取对话框当前的参数值
        WindowManager.LayoutParams p = getWindow().getAttributes();
        getWindow().setGravity(Gravity.BOTTOM);
        p.height = (int) (UtilParameter.mScreenHeight * 0.6);
        p.width = (int) (UtilParameter.mScreenWidth * 0.9);
        getWindow().setAttributes(p);
        initView();
    }

    private void initView() {
        mContext = this;
        mTvConfirmResult = findViewById(R.id.alert_tv_confirmResult);
        TextView mTvDone = findViewById(R.id.alert_tv_done);
        mTvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转我的确权界面
                Intent intent = new Intent(ConfirmResult.this, MaskView.class);
                intent.putExtra("fragmentID", 1);
                startActivity(intent);
            }
        });
        mRecyclerView = findViewById(R.id.alert_recycler_confirmResult);
        getData();
    }

    @SuppressLint("SetTextI18n")
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mGetSuccessCount = bundle.getInt("confirmSuccessCount");
            mGetConfirmResultBeanList = (ArrayList<ConfirmResultBean>) bundle.getSerializable("confirmResultList");
        }
        mTvConfirmResult.setText("确权结果：成功确权 " + mGetSuccessCount + " 张");
        showRecyclerView();
    }

    private void showRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ConfirmResultAdapter(mContext, mGetConfirmResultBeanList);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 本机自带返回键
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //跳转我的确权界面
            Intent intent = new Intent(ConfirmResult.this, MaskView.class);
            intent.putExtra("fragmentID", 1);
            startActivity(intent);
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

}
