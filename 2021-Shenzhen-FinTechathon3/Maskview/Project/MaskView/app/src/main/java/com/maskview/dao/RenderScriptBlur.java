package com.maskview.dao;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class RenderScriptBlur {
    private static final float BITMAP_SCALE = 0.4f;
    private static final int BLUR_RADIUS = 7;

    public static Bitmap blur(Context context, Bitmap bitmap) {
        return blur(context, bitmap, BITMAP_SCALE, BLUR_RADIUS);
    }

    public static Bitmap blur(Context context, Bitmap bitmap, float bitmap_scale) {
        return blur(context, bitmap, bitmap_scale, BLUR_RADIUS);
    }

    public static Bitmap blur(Context context, Bitmap bitmap, int blur_radius) {
        return blur(context, bitmap, BITMAP_SCALE, blur_radius);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap bitmap, float bitmap_scale, int blur_radius) {
        //先对图片进行压缩然后再blur
        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(bitmap.getWidth() * bitmap_scale),
                Math.round(bitmap.getHeight() * bitmap_scale), false);
        //创建空的Bitmap用于输出
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        //1、初始化Renderscript
        RenderScript rs = RenderScript.create(context);
        //2、Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //3、native层分配内存空间
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        //4、设置blur的半径然后进行blur
        theIntrinsic.setRadius(blur_radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        //5、拷贝blur后的数据到java缓冲区中
        tmpOut.copyTo(outputBitmap);
        //6、销毁Renderscript
        rs.destroy();
        //bitmap.recycle();
        return outputBitmap;
    }
}
