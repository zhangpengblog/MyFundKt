package com.example.myfundkt.http

import com.example.myfundkt.bean.selection.SelectionBean
import com.example.myfundkt.bean.top.TopBean
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface KtApi {
    @GET("get")
    suspend fun getfunds(@QueryMap map: HashMap<String, String>): Response<TopBean>

    @GET("FundMNewApi/FundMNFInfo")
    suspend fun getSellected(@QueryMap map: HashMap<String, String>): Response<SelectionBean>

}