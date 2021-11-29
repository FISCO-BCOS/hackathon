package com.maskview.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Yzr on 2020/10/6
 * 解决多次点击Toast出现多个弹框的问题
 */
public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String content, int xOffset, int yOffset) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        // Toast位置
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
