package com.maskview.mainView.shoppingCart.callBack;

import android.view.View;

/**
 * group的checkbox点击监听
 */

public interface OnViewGroupClickListener {
    void onGroupCheckBoxClick(boolean isChecked, View view, int groupPosition);
}
