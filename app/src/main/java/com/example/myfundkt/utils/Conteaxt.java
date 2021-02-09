package com.example.myfundkt.utils;

import android.annotation.SuppressLint;
import android.content.Context;

public class Conteaxt {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private Conteaxt() {
        throw new UnsupportedOperationException("you can't instantiate me...");
    }

    //初始化配置
    public static void init(Context context) {
        Conteaxt.mContext = context.getApplicationContext();
    }

    //获取全局上下文对象
    public static Context getContext() {
        if (mContext != null)
            return mContext;
        throw new NullPointerException("you should init first!");
    }
}