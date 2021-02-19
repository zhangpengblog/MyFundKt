package com.example.myfundkt.http;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface Api {
    @GET("get")
    Call<ResponseBody> getQT(@QueryMap HashMap<String,String> map);



    @GET("FundMNewApi/FundMNFInfo")
    Call<ResponseBody> getBottoms(@QueryMap HashMap<String,String> map);

    @GET
    Call<ResponseBody> getHoliday(@Url String url);

    //https://fundmobapi.eastmoney.com/FundMApi/FundVarietieValuationDetail.ashx?FCODE=000961&deviceid=Wap&plat=Wap&product=EFund&version=2.0.0&_=1612339403432
    //图数据
    @GET("FundMApi/FundVarietieValuationDetail.ashx")
    Call<ResponseBody> getLineData(@Query("FCODE") String FCODE,@Query("deviceid") String deviceid,
                                   @Query("plat") String plat,
                                   @Query("product") String product,
                                   @Query("version") String version,
                                   @Query("_") String _ex);

    //持仓
    @GET("FundMNewApi/FundMNInverstPosition")
    Call<ResponseBody> getHoldingStocks(@Query("FCODE") String FCODE,
                                        @Query("deviceid") String deviceid,
                                        @Query("plat") String plat,
                                        @Query("product") String product,
                                        @Query("version") String version,
                                        @Query("Uid") String Uid,
                                        @Query("_") String ex_ );

    //持仓股的涨跌

}
