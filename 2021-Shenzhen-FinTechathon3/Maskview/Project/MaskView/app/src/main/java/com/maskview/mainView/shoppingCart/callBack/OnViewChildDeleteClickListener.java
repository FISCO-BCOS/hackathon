package com.maskview.mainView.shoppingCart.callBack;

import android.view.View;

/**
 * child的删除事件监听
 */

public interface OnViewChildDeleteClickListener {
    void onChildDeleteClick(View view, int groupPosition, int childPosition);
}
