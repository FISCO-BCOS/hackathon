package com.maskview.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.maskview.R;

/**
 * Created by Yzr on 2020/10/9
 */
@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {

    // 旋转角度
    private int rotateDegree;
    // 是否需要旋转
    private boolean mNeedRotate;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 设置图标
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 三个参数分别为：旋转角度; 旋转的x坐标; 旋转的y坐标
        canvas.rotate(rotateDegree, (float) getWidth() / 2, (float) getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedRotate = true;
        // 绑定到window
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree += 30;
                rotateDegree = rotateDegree <= 360 ? rotateDegree : 0;
                invalidate();
                // 是否继续旋转
                if (mNeedRotate) {
                    postDelayed(this, 100);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 从window解绑
        mNeedRotate = false;
    }
}
