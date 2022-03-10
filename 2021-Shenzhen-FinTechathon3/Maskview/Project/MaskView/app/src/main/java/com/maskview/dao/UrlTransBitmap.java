package com.maskview.dao;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UrlTransBitmap {

    public Bitmap returnBitMap(final String url, final Context mContext) {

        final Bitmap[] bm = new Bitmap[1];
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    bm[0] = Glide.with(mContext).asBitmap().load(url).dontAnimate().submit().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        es.submit(task);
        es.shutdown();

        while (true) {
            if (es.isTerminated()) {
                return bm[0];
            }
        }
    }
}
