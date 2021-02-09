package com.example.myfundkt.http.response

import com.example.myfundkt.utils.MyLog
import com.example.myfundkt.utils.ToastUtil
import okhttp3.ResponseBody
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private const val TAG = "BaseResponse"
private lateinit var json:String
open class BaseResponse(dataCall :Call<ResponseBody> ) {

   init {
       dataCall.enqueue(object :Callback<ResponseBody>{
           override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
               if (Objects.requireNonNull(t.message)
                       ?.contains("10000ms")!!
               ) {
                   ToastUtil.show("网络超时")
               }
               ONFAIL(t)
           }

           override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

               try {
                   MyLog.d(TAG,response.toString())
                   json = response.body()?.string() ?: "";
                   ONSUCCESS(json)
               }catch (e :JSONException){
                   json?.let { ONERROR(it) }
                   e.printStackTrace()
               }


           }
       })
   }

    open fun ONFAIL(t: Throwable) {
    }

    open fun ONERROR(json: Any) {
    }

    open fun ONSUCCESS(json: String?) {

    }

}