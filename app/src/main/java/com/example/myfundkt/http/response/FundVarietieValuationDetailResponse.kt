package com.example.myfundkt.http.response


import com.example.myfundkt.bean.information.Expansion
import com.example.myfundkt.bean.information.InformationData
import com.example.myfundkt.bean.selection.Data
import com.example.myfundkt.bean.selection.SelectionBean
import com.example.myfundkt.utils.MyLog
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call

private const val TAG = "FundVarietieValuationDe"
abstract class FundVarietieValuationDetailResponse(dataCall: Call<ResponseBody>) : BaseResponse(dataCall) {
    override fun ONFAIL(t: Throwable) {
        super.ONFAIL(t)
    }

    override fun ONERROR(json: Any) {
        super.ONERROR(json)
    }

    override fun ONSUCCESS(json: String?) {
        super.ONSUCCESS(json)
        val jsonObject = JSONObject(json)
        val ErrCode = jsonObject.getInt("ErrCode")
        if (ErrCode == 0) {
            val informationData: InformationData = Gson().fromJson(json, InformationData::class.java)
            informationData.let { onSuccess(it) }

        }
    }

    abstract  fun onSuccess(informationData: InformationData)
}