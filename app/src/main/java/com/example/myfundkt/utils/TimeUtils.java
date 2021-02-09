package com.example.myfundkt.utils;

import android.annotation.SuppressLint;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TimeUtils {
    public static String TimeStempToDate(Object object) {
        if (object == null) {
            return "error";
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss");
        return simpleDateFormat.format (new Date(Long.parseLong (object.toString ()) * 1000));
    }

    public static String TimeStempToDay(Object object) {
        if (object == null) {
            return "error";
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd");
        return simpleDateFormat.format (new Date (Long.parseLong (object.toString ()) * 1000));
    }

    public static long DateToTimeStemp(String s){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return Objects.requireNonNull (simpleDateFormat.parse (s, new ParsePosition(0))).getTime() / 1000;
    }

    public static long DayToTimeStemp(String s){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return Objects.requireNonNull (simpleDateFormat.parse (s, new ParsePosition(0))).getTime() / 1000;
    }

    public static String FormatTime(long lo){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("MM-dd HH:mm");
        return simpleDateFormat.format (new Date (Long.parseLong (Long.toString (lo)) * 1000));
    }


    /**
     * 获取时间戳
     *
     */
    public static String getSecondTimestamp() {
        return String.valueOf (System.currentTimeMillis () / 1000);
    }
}