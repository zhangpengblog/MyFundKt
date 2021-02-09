package com.example.myfundkt.http.response

import com.example.myfundkt.bean.holdingStocks.FundStock
import com.example.myfundkt.bean.holdingStocks.HoldingStocksBean
import com.example.myfundkt.bean.selection.Data
import com.example.myfundkt.bean.selection.SelectionBean
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call

abstract class HoldingStocksResponse(dataCall: Call<ResponseBody>) : BaseResponse(dataCall) {
    override fun ONFAIL(t: Throwable) {
        super.ONFAIL(t)
    }

    override fun ONERROR(json: Any) {
        super.ONERROR(json)
    }

    override fun ONSUCCESS(json: String?) {
        super.ONSUCCESS(json)
        val holdingStocksBean: HoldingStocksBean = Gson().fromJson(json, HoldingStocksBean::class.java)
        if (holdingStocksBean.Success == true){
            holdingStocksBean.Datas?.fundStocks?.let {
                onSuccess(it)
            }
        }
    }

    abstract fun onSuccess(it: List<FundStock>)
}