package com.example.myfundkt.http.response

import com.example.myfundkt.bean.selection.Data
import com.example.myfundkt.bean.selection.SelectionBean
import com.example.myfundkt.utils.MyLog
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call

private const val TAG = "BottomsResponse"
abstract class BottomsResponse(dataCall: Call<ResponseBody>) : BaseResponse(dataCall) {
    override fun ONFAIL(t: Throwable) {
        super.ONFAIL(t)
    }

    override fun ONERROR(json: Any) {
        super.ONERROR(json)
        onError()
    }

    override fun ONSUCCESS(json: String?) {
        super.ONSUCCESS(json)
        val jsonObject = JSONObject(json!!)
        val ErrCode = jsonObject.getInt("ErrCode")
        if (ErrCode == 0) {
            val sellectionBean:SelectionBean = Gson().fromJson(json, SelectionBean::class.java)
            val datasBeanList: List<Data>? =
                sellectionBean.Datas
            datasBeanList?.let { onSuccess(it) }

        }
    }

    abstract fun onError()

    abstract fun onSuccess(it: List<Data>)


}