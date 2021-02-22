package com.example.myfundkt;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.example.myfundkt.utils.Conteaxt;

import org.jetbrains.annotations.NotNull;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate ();
        Conteaxt.init (this);
        LogConfiguration config = new LogConfiguration.Builder ()
                .logLevel (BuildConfig.DEBUG ? LogLevel.ALL             // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
                        : LogLevel.NONE)
                .tag ("BeeGame")                                         // 指定 TAG，默认为 "X-LOG"
                .build ();
        XLog.init (config);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull @NotNull Thread t, @NonNull @NotNull Throwable e) {
                Log.e("MyApplication", "uncaughtException: "+t.getName()+e.getMessage() );
                Log.e("MyApplication", "uncaughtException: ",e );
            }
        });
    }
}
