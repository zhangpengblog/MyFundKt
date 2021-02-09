package com.example.myfundkt.http;

import com.example.myfundkt.BuildConfig;
import com.example.myfundkt.utils.MyLog;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum EnumSingle {
    INSTANCE;
    private static final String TAG = "EnumSingle";

    public Retrofit getRetorfit(String baseurl) {
        MyLog.d (TAG, "getRetorfit: ");

        OkHttpClient client = new OkHttpClient ();
        OkHttpClient.Builder builder = new OkHttpClient.Builder ();
        if (BuildConfig.DEBUG) {
//            builder.addInterceptor (new MyInterceptor ());
            client = builder.build ();
        }


        return new Retrofit.Builder ()
                //设置网络请求BaseUrl地址
                .baseUrl (baseurl)
                //设置数据解析器
                .addConverterFactory (GsonConverterFactory.create ())
                .client (client)
                .build ();
    }


}