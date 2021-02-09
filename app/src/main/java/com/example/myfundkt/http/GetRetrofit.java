package com.example.myfundkt.http;

import retrofit2.Retrofit;

public class GetRetrofit {

    public static Retrofit getPush2() {
        EnumSingle single = EnumSingle.INSTANCE;
        String APP = "https://push2.eastmoney.com/api/qt/ulist.np/";
        return single.getRetorfit (APP);
    }

    public static Retrofit getFundmobapi() {
        EnumSingle single = EnumSingle.INSTANCE;
        String member = "https://fundmobapi.eastmoney.com/";
        return single.getRetorfit (member);
    }

    public static Retrofit getDate(){
        EnumSingle single=EnumSingle.INSTANCE;
        String date="https://timor.tech/api/holiday/info/";
        return single.getRetorfit (date);
    }


}