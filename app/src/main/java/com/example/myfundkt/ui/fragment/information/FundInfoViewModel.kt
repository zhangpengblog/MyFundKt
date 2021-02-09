package com.example.myfundkt.ui.fragment.information

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfundkt.bean.information.Expansion
import com.example.myfundkt.bean.information.InformationData
import com.example.myfundkt.http.Api
import com.example.myfundkt.http.GetRetrofit
import com.example.myfundkt.http.response.FundVarietieValuationDetailResponse
import com.example.myfundkt.ui.lineChart.LineChart

private const val TAG = "FundInfoViewModel"
class FundInfoViewModel : ViewModel() {
    // TODO: Implement the ViewModel


    //line数组
    private val _data = MutableLiveData(listOf<LineChart.Data<Float>>())
     val data : LiveData<List<LineChart.Data<Float>>> = _data

    //净值起点
    val  start= MutableLiveData<Float>()
    private val _start : MutableLiveData<Float> = start

    //详细信息
    val expansion = MutableLiveData<Expansion>()
    private val _expansion : MutableLiveData<Expansion> = expansion

    //loading显示
    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility: LiveData<Int> = _progressBarVisibility




    fun initSellectionFund(FCODE:String) {
        _progressBarVisibility.value = View.VISIBLE
        Log.d(TAG, "initSellectionFund: "+FCODE)
        object : FundVarietieValuationDetailResponse(
            GetRetrofit.getFundmobapi().create(Api::class.java).getLineData(
                FCODE,
                "Wap",
                "Wap",
                "EFund",
                "2.0.0",
                "1612339403432"
            )
        ){
            override fun onSuccess(informationData: InformationData) {
                _progressBarVisibility.value = View.GONE
                _expansion.value = informationData.expansion
                val lineData : List<String>? = informationData.datas
                if (lineData != null) {
                    print("lineData.size"+lineData.size)
                }
                //昨日收价
                val mid: Float = informationData.expansion?.dWJZ?.toFloat()!!
                _start.value = mid
                var temp: Float = mid
                var dataTemp = mutableListOf<LineChart.Data<Float>>()
                lineData?.forEach { s: String ->
                   val strArray = s.split(",");
                    val zd:Float = strArray[2].toFloat()
                    temp *= (1 + zd)
                    val value = mid*(1+(zd/100))
                    //序号，时间，涨跌
                    dataTemp.add(LineChart.Data<Float>(zd,value, strArray[1]))

                }

                _data.value = dataTemp

            }
        }
    }
}