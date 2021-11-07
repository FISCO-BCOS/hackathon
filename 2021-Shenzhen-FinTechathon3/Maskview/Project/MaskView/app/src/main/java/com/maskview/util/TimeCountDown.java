package com.maskview.util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

import com.maskview.R;

public class TimeCountDown extends CountDownTimer {

    private Button button;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCountDown(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        button.setBackgroundResource(R.drawable.send_code_already);
        button.setTextColor(Color.WHITE);
        button.setClickable(false);
        button.setText(millisUntilFinished / 1000 + "");
    }

    @Override
    public void onFinish() {
        button.setText("重新获取");
        button.setTextColor(Color.BLUE);
        button.setClickable(true);
        button.setBackgroundResource(R.drawable.send_code);
    }
}
