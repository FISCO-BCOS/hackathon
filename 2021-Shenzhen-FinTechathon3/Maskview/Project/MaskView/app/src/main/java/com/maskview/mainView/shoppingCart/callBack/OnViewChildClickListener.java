package com.maskview.mainView.shoppingCart.callBack;

import android.view.View;

/**
 * child的checkbox点击监听
 */

public interface OnViewChildClickListener {
    void onChildCheckBoxClick(boolean isChecked, View view, int groupPosition, int childPosition);
}
