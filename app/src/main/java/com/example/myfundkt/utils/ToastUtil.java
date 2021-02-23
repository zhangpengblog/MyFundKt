package com.example.myfundkt.utils;

import android.widget.Toast;

public class ToastUtil {
    public static void show(String s) {
        Toast.makeText(Conteaxt.getContext(), s, Toast.LENGTH_SHORT).show();
    }
}