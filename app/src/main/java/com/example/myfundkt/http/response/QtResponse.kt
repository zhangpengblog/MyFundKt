package com.example.myfundkt.http.response

import android.util.Log
import com.example.myfundkt.bean.top.Diff
import com.example.myfundkt.bean.top.TopBean
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call

private const val TAG = "QtResponse"
abstract class QtResponse(dataCall: Call<ResponseBody>) : BaseResponse(dataCall) {
    override fun ONFAIL(t: Throwable) {
        super.ONFAIL(t)
    }

    override fun ONERROR(json: Any) {
        super.ONERROR(json)
    }

    override fun ONSUCCESS(json: String?) {
        super.ONSUCCESS(json)
        val jsonObject = JSONObject(json)
        val rc = jsonObject.getInt("rc")
        if (rc == 0) {
            try {
                val topBean: TopBean = Gson().fromJson(json, TopBean::class.java)
                topBean?.let {
                    val total: Int = it.data.total
                    val diffBeanList: List<Diff> = it.data.diff
                    Log.d(TAG, "ONSUCCESS: "+diffBeanList)
                    onSuccess(total, diffBeanList)
                }

            } catch (e: Exception) {
            }
        }
    }

    abstract fun onSuccess(total: Int, diffBeanList: List<Diff>)
}