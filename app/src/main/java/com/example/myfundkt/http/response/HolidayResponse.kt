package com.example.myfundkt.http.response

import com.example.myfundkt.bean.holiday.HolidayBean

import com.example.myfundkt.utils.MyLog
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call

abstract class HolidayResponse(dataCall: Call<ResponseBody>) : BaseResponse(dataCall) {
    override fun ONFAIL(t: Throwable) {
        super.ONFAIL(t)
    }

    override fun ONERROR(json: Any) {
        super.ONERROR(json)
    }

    override fun ONSUCCESS(json: String?) {
        super.ONSUCCESS(json)
        MyLog.d("Holiday", json)
        val holidayBean: HolidayBean = Gson().fromJson(json, HolidayBean::class.java)
        if (holidayBean.code == 0) {
            if (holidayBean.type.type == 0) {
                workDay()
            } else {
                holiday()
            }
        }
    }

    abstract fun holiday()

    abstract fun workDay()
}