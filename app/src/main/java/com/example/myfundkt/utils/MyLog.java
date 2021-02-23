package com.example.myfundkt.utils;

import com.elvishew.xlog.XLog;

public class MyLog {
    private final static String head = "MyLog: ";

    /*d*/
    public static void d(String TAG, String msg) {
        XLog.tag(head + TAG).d(msg);
    }

    public static void d(String TAG, Object object) {
        XLog.tag(head + TAG).d(object);
    }

    public static void d(String TAG, Object[] array) {
        XLog.tag(head + TAG).d(array);
    }

    public static void d(String TAG, String msg, Throwable tr) {
        XLog.tag(head + TAG).d(msg, tr);
    }

    public static void d(String TAG, String format, Object... args) {
        XLog.tag(head + TAG).d(format, args);
    }

    /*i*/
    public static void i(String TAG, String msg) {
        XLog.tag(head + TAG).i(msg);
    }

    public static void i(String TAG, Object object) {
        XLog.tag(head + TAG).i(object);
    }

    public static void i(String TAG, Object[] array) {
        XLog.tag(head + TAG).i(array);
    }

    public static void i(String TAG, String msg, Throwable tr) {
        XLog.tag(head + TAG).i(msg, tr);
    }

    public static void i(String TAG, String format, Object... args) {
        XLog.tag(head + TAG).i(format, args);
    }

    /*e*/
    public static void e(String TAG, String msg) {
        XLog.tag(head + TAG).e(msg);
    }

    public static void e(String TAG, Object object) {
        XLog.tag(head + TAG).e(object);
    }

    public static void e(String TAG, Object[] array) {
        XLog.tag(head + TAG).e(array);
    }

    public static void e(String TAG, String msg, Throwable tr) {
        XLog.tag(head + TAG).e(msg, tr);
    }

    public static void e(String TAG, String format, Object... args) {
        XLog.tag(head + TAG).e(format, args);
    }

    /*json*/
    public static void json(String TAG, String json) {
        XLog.tag(head + TAG + "\n").b().json(json);
    }

    public static void json(String TAG, String msg, String json) {
        XLog.tag(head + TAG + ": " + msg + ": \n").b().json(json);
    }
}