package com.example.myfundkt.http;


import android.text.TextUtils;

import com.example.myfundkt.utils.MyLog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 自定义 OkHttp3 日志拦截器
 */
public class MyInterceptor implements Interceptor {
    private static final String TAG = "网络请求日志";

    public MyInterceptor() {
    }

    @NotNull
    @Override
    public Response intercept(@NotNull okhttp3.Interceptor.Chain chain) throws IOException {
        //添加到责任链中
        Request request = chain.request ();
        logForRequest (request);
        Response response = chain.proceed (request);


        return logForResponse (response);
    }

    /**
     * 打印响应日志
     *
     * @param response
     * @return
     */
    private Response logForResponse(Response response) {
        MyLog.e (TAG, "************************************************响应日志开始************************************************");
        Response.Builder builder = response.newBuilder ();
        Response clone = builder.build ();
        MyLog.d (TAG, "url:" + clone.request ().url ());
        MyLog.d (TAG, "code:" + clone.code ());
        if (!TextUtils.isEmpty (clone.message ())) {
            MyLog.d (TAG, "message:" + clone.message ());
        }
        ResponseBody body = clone.body ();
        if (body != null) {
            MediaType mediaType = body.contentType ();
            if (mediaType != null) {
                if (isText (mediaType)) {
                    String resp = null;
                    try {
                        resp = body.string ();
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                    MyLog.json (TAG, "响应:", resp);
                    MyLog.e (TAG, "************************************************响应日志结束************************************************");
                    body = ResponseBody.create (mediaType, resp);
                    return response.newBuilder ().body (body).build ();
                } else {
                    MyLog.e (TAG, "响应内容 : " + "发生错误-非文本类型");
                }
            }

        }

        MyLog.e (TAG, "************************************************响应日志结束************************************************");
        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type () != null && mediaType.type ().equals ("text")) {
            return true;
        }
        if (mediaType.subtype () != null) {
            if (mediaType.subtype ().equals ("json")
                    || mediaType.subtype ().equals ("xml")
                    || mediaType.subtype ().equals ("html")
                    || mediaType.subtype ().equals ("webviewhtml")
                    || mediaType.subtype ().equals ("x-www-form-urlencoded")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打印请求日志
     *
     * @param request
     */
    private void logForRequest(Request request) {
        String url = request.url ().toString ();
        MyLog.e (TAG, "================================================请求日志开始================================================");
        MyLog.d (TAG, "请求方式 : " + request.method ());
        HttpUrl httpUrl = request.url ();
        MyLog.d (TAG, "请求参数: " + getQuerys (httpUrl));
        MyLog.d (TAG, "url : " + url);
        RequestBody requestBody = request.body ();
        if (requestBody != null) {
            MediaType mediaType = requestBody.contentType ();
            if (mediaType != null) {
                MyLog.d (TAG, "请求内容类别 : " + mediaType.toString ());
                if (isText (mediaType)) {
                    MyLog.d (TAG, "请求内容 : " + bodyToString (request));
                } else {
                    MyLog.d (TAG, "请求内容 : " + " 无法识别。");
                }
            }
        }


        MyLog.e (TAG, "================================================请求日志结束================================================");
    }

    private String bodyToString(Request request) {
        Request req = request.newBuilder ().build ();
        String urlSub = null;
        Buffer buffer = new Buffer();
        try {
            req.body ().writeTo (buffer);
            String message = buffer.readUtf8 ();
            urlSub = URLDecoder.decode (message, "utf-8");
        } catch (IOException e) {
            e.printStackTrace ();
            return "在解析请求内容时候发生了异常-非字符串";
        }
        return urlSub;
    }

    /**
     * 读取参数
     *
     * @param requestBody
     * @return
     */
    private String getParam(RequestBody requestBody) {
        Buffer buffer = new Buffer ();
        String logparm;
        try {
            requestBody.writeTo (buffer);
            logparm = buffer.readUtf8 ();
            logparm = URLDecoder.decode (logparm, "utf-8");
        } catch (IOException e) {
            e.printStackTrace ();
            return "";
        }
        return logparm;
    }

    private Map<String, String> getQuerys(HttpUrl url) {
        Map<String, String> map = new HashMap<>();
        Set<String> names = url.queryParameterNames ();
        int i = 0;

        for (String name : names) {
            map.put (name, url.queryParameterValue (i));
            i++;
        }

        return map;

    }


}