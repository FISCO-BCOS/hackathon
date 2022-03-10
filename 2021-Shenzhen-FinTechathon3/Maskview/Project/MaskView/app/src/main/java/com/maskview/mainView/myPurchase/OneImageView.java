package com.maskview.mainView.myPurchase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.maskview.R;
import com.maskview.dao.ZoomImageView;

import java.io.FileNotFoundException;

public class OneImageView extends AppCompatActivity {

    //private ImageView mImage;
    private ZoomImageView mImage;
    private Bitmap mColorBitmap;
    private Bitmap mImgBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private int mScreenHeight;
    private int mScreenWidth;
    private RelativeLayout mRvAllView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_image_view);

        initView();
    }

    private void initView() {
        mContext = this;
        mRvAllView = findViewById(R.id.layout_myPurchase_oneImg);
        mImage = findViewById(R.id.oneImage_myPurchase);
        getScreenSize();
        getBitmap();
    }

    private void getBitmap() {
        Uri uri = Uri.parse(getIntent().getStringExtra("myBitmap"));
        if (uri != null) {
            try {
                mImage.setImageURI(uri);
                mImgBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                showSoftColor();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showSoftColor() {
        if (mImgBitmap != null) {
            Palette.from(mImgBitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    if (palette == null) return;
                    if (palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                        createLinearGradientBitmap(palette.getDarkVibrantColor(Color.TRANSPARENT),
                                palette.getDarkVibrantColor(Color.TRANSPARENT));
                    } else if (palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                        createLinearGradientBitmap(palette.getDarkMutedColor(Color.TRANSPARENT),
                                palette.getDarkMutedColor(Color.TRANSPARENT));
                    } else {
                        createLinearGradientBitmap(palette.getDarkMutedColor(Color.TRANSPARENT),
                                palette.getDarkMutedColor(Color.TRANSPARENT));
                    }
                }
            });
        }
    }

    private void createLinearGradientBitmap(int darkColor, int color) {
        int[] bgColors = new int[2];
        bgColors[0] = darkColor;
        bgColors[1] = color;
        if (mColorBitmap == null) {
            mColorBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_4444);
        }
        if (mCanvas == null) {
            mCanvas = new Canvas();
        }
        if (mPaint == null) {
            mPaint = new Paint();
        }
        mCanvas.setBitmap(mColorBitmap);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        LinearGradient gradient = new LinearGradient(0, 0, 0, mColorBitmap.getHeight(), bgColors[0], bgColors[1], Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);
        RectF rectF = new RectF(0, 0, mColorBitmap.getWidth(), mColorBitmap.getHeight());
        mCanvas.drawRect(rectF, mPaint);
        mRvAllView.setBackground(new BitmapDrawable(getResources(), mColorBitmap));
    }

    // 获取屏幕的宽高
    private void getScreenSize() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;
    }

    /*// 设置本机返回键操作
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.dispatchKeyEvent(event);
    }*/
}
