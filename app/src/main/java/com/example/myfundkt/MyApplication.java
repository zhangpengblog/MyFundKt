package com.example.myfundkt;

import android.app.Application;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.example.myfundkt.utils.Conteaxt;

public class MyApplication extends Application {
    private final static boolean isDebugARouter = BuildConfig.DEBUG;

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
    }
}
