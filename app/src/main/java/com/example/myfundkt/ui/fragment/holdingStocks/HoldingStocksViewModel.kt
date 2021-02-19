package com.example.myfundkt.ui.fragment.holdingStocks

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfundkt.bean.holdingStocks.FundStock
import com.example.myfundkt.bean.holdingStocks.HoldingData
import com.example.myfundkt.bean.top.Diff
import com.example.myfundkt.http.Api
import com.example.myfundkt.http.GetRetrofit
import com.example.myfundkt.http.KtApi
import com.example.myfundkt.http.response.HoldingStocksResponse
import com.example.myfundkt.http.response.QtResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private const val TAG = "HoldingStocksViewModel"
class HoldingStocksViewModel : ViewModel() {
    private val _stocksData = MutableLiveData(listOf<FundStock>())
    val stocksData : LiveData<List<FundStock>> = _stocksData
    private val _GPDM = mutableListOf<String>()
    private val _qtLiveData = MutableLiveData(listOf<Diff>())
    val qtLiveData: LiveData<List<Diff>> = _qtLiveData

    private val _holdingData = MutableLiveData(listOf<HoldingData>())
    val holdingData: LiveData<List<HoldingData>> = _holdingData

    //loading显示
    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility: LiveData<Int> = _progressBarVisibility

//   fun getStocks(code: String){
//       _progressBarVisibility.value = View.VISIBLE
//       object : HoldingStocksResponse(
//           GetRetrofit.getFundmobapi().create(Api::class.java).getHoldingStocks(code,"Wap","Wap","EFund","2.0.0",
//           "",System.currentTimeMillis().toString())
//       ) {
//
//           override fun onSuccess(it: List<FundStock>) {
//               it?.let {
//
//                   _stocksData.value = it
//                   it.forEach{
//                       it.GPDM?.let { it1 -> _GPDM.add(it.NEWTEXCH+"."+it1)}
//
//                   }
//                   initQt(_GPDM)
//
//               }
//
//
//           }
//       }
//   }

    fun getStocksCoro(code: String?){
        _progressBarVisibility.value = View.VISIBLE
        _stocksData.value = null
        viewModelScope.launch(Dispatchers.IO){
            try {
                val api =GetRetrofit.getFundmobapi().create(KtApi::class.java)
                val response = api.getHoldingStocks(code,"Wap","Wap","EFund","2.0.0",
                    "",System.currentTimeMillis().toString())
                if (response.isSuccessful){
                    val holdingStocksBean = response.body()
                    holdingStocksBean?.let {
                        val list = it.Datas?.fundStocks
                        _stocksData.postValue(list)
                        if (list != null) {
                            list.forEach{
                                it.GPDM?.let { it1 -> _GPDM.add(it.NEWTEXCH+"."+it1)}

                            }
                            initQtCoro(_GPDM)
                        }


                    }
                }
            }catch (e:Exception){
                Log.e(TAG, "getStocksCoro: ", e)
                _progressBarVisibility.postValue(View.GONE)
                _stocksData.postValue(listOf(FundStock()))
                this.cancel()

            }
        }

    }


//    fun initQt(mutableList: MutableList<String>) {
//        var string = ""
//        mutableList.forEach{
//           string += "$it,"
//        }
//
//        Log.d(TAG, "initQt: "+ string)
//        val map = HashMap<String, String>()
//        map["fltt"] = "2"
//        map["fields"] = "f1,f2,f3,f4,f12,f13,f14,f292"
//        map["_"] = System.currentTimeMillis().toString()
//        map["deviceid"] = "Wap"
//        map["plat"]= "Wap"
//        map["product"] ="EFund"
//        map["version"]="2.0.0"
//        map["secids"] = string
//
//        object : QtResponse(
//            GetRetrofit.getPush2().create(Api::class.java).getQT(map)
//        ) {
//            override fun onSuccess(total: Int, diffBeanList: List<Diff>) {
//                _progressBarVisibility.value = View.GONE
//                Log.d(TAG, "onSuccess: "+diffBeanList)
//                _holdingData.value = listOf()
//                _qtLiveData.value = diffBeanList
//                val holdingList = mutableListOf<HoldingData>()
//                diffBeanList.forEach{
//                    val holding:HoldingData= HoldingData()
//
//                    holding.price=it.f2
//                    holding.zdf=it.f3
//                    _stocksData.value?.forEach { it1->
//                        if (it.f14.equals(it1.GPJC)){
//                            holding.nameAndCode =it1.GPJC+"("+it1.GPDM+")"
//                            holding.JZBL=it1.JZBL
//                            holding.PCTNVCHG=it1.PCTNVCHG
//                            holdingList.add(holding)
//                        }
//                    }
//                }
//                _holdingData.value=holdingList
//            }
//        }
//    }

    fun initQtCoro(mutableList: MutableList<String>){
        var string = ""
        mutableList.forEach{
            string += "$it,"
        }

        Log.d(TAG, "initQt: "+ string)
        val map = HashMap<String, String>()
        map["fltt"] = "2"
        map["fields"] = "f1,f2,f3,f4,f12,f13,f14,f292"
        map["_"] = System.currentTimeMillis().toString()
        map["deviceid"] = "Wap"
        map["plat"]= "Wap"
        map["product"] ="EFund"
        map["version"]="2.0.0"
        map["secids"] = string

        val api = GetRetrofit.getPush2().create(KtApi::class.java)
        viewModelScope.launch(Dispatchers.IO) {
            _holdingData.postValue(null)
            _qtLiveData.postValue(listOf())
            try {
                _progressBarVisibility.postValue(View.GONE)
                val response = api.getfunds(map)
                if (response.isSuccessful){
                    val topBean = response.body()
                    topBean?.let {
                        val diffBeanList: List<Diff> = it.data.diff
                        _qtLiveData.postValue(diffBeanList)
                        val holdingList = mutableListOf<HoldingData>()
                        diffBeanList.forEach{
                            val holding:HoldingData= HoldingData()
                            holding.price=it.f2
                            holding.zdf=it.f3
                            _stocksData.value?.forEach { it1->
                                if (it.f14.equals(it1.GPJC)){
                                    holding.nameAndCode =it1.GPJC+"("+it1.GPDM+")"
                                    holding.JZBL=it1.JZBL
                                    holding.PCTNVCHG=it1.PCTNVCHG
                                    holdingList.add(holding)
                                }
                            }
                        }
                        _holdingData.postValue(holdingList)
                    }
                }else{

                }
            }catch (e:Exception){
                Log.e(TAG, "initQtCoro: ${e.message}",e )
                _holdingData.postValue(null)

            }
        }
    }


}