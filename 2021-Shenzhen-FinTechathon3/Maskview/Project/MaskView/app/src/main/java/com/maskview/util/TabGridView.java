package com.maskview.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class TabGridView extends GridView {
    public TabGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public TabGridView(Context context) {
        super(context);
    }


    public TabGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

