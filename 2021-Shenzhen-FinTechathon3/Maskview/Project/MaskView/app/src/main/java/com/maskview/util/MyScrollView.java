package com.maskview.util;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;

import androidx.annotation.NonNull;

public class MyScrollView extends ScrollView {
    private OnScrollListener onScrollListener;
    private int lastScrollY;

    private static final int LEN = 0xc8;
    private static final int DURATION = 500;
    private static final int MAX_DY = 200;
    private Scroller mScroller;
    TouchTool tool;
    int left, top;
    float startX, startY, currentX, currentY;
    int imageViewH;
    int rootW, rootH;
    ImageView imageView;
    boolean scrollerType;

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public MyScrollView(Context context) {
        super(context);
    }


    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {

        return 0;
    }

    private final int[] li = new int[2];
    private final int[] li2 = new int[2];
    private float lastLy;
    private boolean startIsTop = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        int action = event.getAction();
        if (!mScroller.isFinished()) {
            return super.onTouchEvent(event);
        }
        currentX = event.getX();
        currentY = event.getY();
        imageView.getLocationInWindow(li);
        getLocationOnScreen(li2);
        imageView.getTop();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (li[1] != li2[1]) {// 判断开始触摸时，imageview和窗口顶部对齐没
                    startIsTop = false;
                }
                left = imageView.getLeft();
                top = imageView.getBottom();
                rootW = getWidth();
                rootH = getHeight();
                imageViewH = imageView.getHeight();
                startX = currentX;
                startY = currentY;
                tool = new TouchTool(imageView.getLeft(), imageView.getBottom(), imageView.getLeft(),
                        imageView.getBottom() + LEN);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!startIsTop && li[1] == li2[1]) {
                    startY = currentY;
                    startIsTop = true;
                }
                if (imageView.isShown() && imageView.getTop() >= 0) {
                    if (tool != null) {
                        int t = tool.getScrollY(currentY - startY);
                        if (!scrollerType && currentY < lastLy && imageView.getHeight() > imageViewH) {
                            scrollTo(0, 0);
                            imageView.getLocationInWindow(li);
                            getLocationOnScreen(li2);
                            android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.height = t;
                            imageView.setLayoutParams(params);
                            if (imageView.getHeight() == imageViewH && li[1] == li2[1]) {
                                scrollerType = true;
                            }
                            if (startIsTop && li[1] != li2[1]) {
                                startIsTop = false;
                            }
                        }
                        if (t >= top && t <= imageView.getBottom() + LEN && li[1] == li2[1] && currentY > lastLy) {
                            android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.height = t;
                            imageView.setLayoutParams(params);
                        }
                    }
                    scrollerType = false;
                }

                lastLy = currentY;
                break;
            case MotionEvent.ACTION_UP:
                if (li[1] == li2[1]) {
                    scrollerType = true;
                    mScroller.startScroll(imageView.getLeft(), imageView.getBottom(), -imageView.getLeft(),
                            imageViewH - imageView.getBottom(), DURATION);
                    invalidate();
                }
                startIsTop = true;
                break;
        }

        return true;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            imageView.layout(0, 0, x + imageView.getWidth(), y);
            invalidate();
            if (!mScroller.isFinished() && scrollerType && y > MAX_DY) {
                android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = y;
                imageView.setLayoutParams(params);
            }
        }
    }

    public static class TouchTool {

        private final int startX;
        private final int startY;

        public TouchTool(int startX, int startY, int endX, int endY) {
            super();
            this.startX = startX;
            this.startY = startY;
        }

        public int getScrollX(float dx) {
            return (int) (startX + dx / 2.5F);
        }

        public int getScrollY(float dy) {
            return (int) (startY + dy / 2.5F);
        }
    }


    /**
     * 设置滚动接口
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int scrollY = MyScrollView.this.getScrollY();
            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 5);
            }
            if (onScrollListener != null) {
                onScrollListener.onScroll(scrollY);
            }
            return false;
        }
    });

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    /*private Handler handler = new Handler() {

        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {
            int scrollY = MyScrollView.this.getScrollY();

            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if(onScrollListener != null){
                onScrollListener.onScroll(scrollY);
            }

        }

    };*/
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //handler.sendMessageDelayed(handler.obtainMessage(), 20);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 20);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         */
        public void onScroll(int scrollY);
    }
}

