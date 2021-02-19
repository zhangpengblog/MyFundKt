package com.example.myfundkt.http

import com.example.myfundkt.bean.information.InformationData
import com.example.myfundkt.bean.selection.SelectionBean
import com.example.myfundkt.bean.top.TopBean
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface KtApi {
    @GET("get")
    suspend fun getfunds(@QueryMap map: HashMap<String, String>): Response<TopBean>

    @GET("FundMNewApi/FundMNFInfo")
    suspend fun getSellected(@QueryMap map: HashMap<String, String>): Response<SelectionBean>

    //https://fundmobapi.eastmoney.com/FundMApi/FundVarietieValuationDetail.ashx?FCODE=000961&deviceid=Wap&plat=Wap&product=EFund&version=2.0.0&_=1612339403432
    //图数据
    @GET("FundMApi/FundVarietieValuationDetail.ashx")
    suspend fun getLineData(
        @Query("FCODE") FCODE: String?, @Query("deviceid") deviceid: String?,
        @Query("plat") plat: String?,
        @Query("product") product: String?,
        @Query("version") version: String?,
        @Query("_") _ex: String?
    ): Response<InformationData>
}