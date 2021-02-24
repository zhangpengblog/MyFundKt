package com.example.myfundkt.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.myfundkt.bean.CollectionBean
import com.example.myfundkt.bean.selection.Data
import com.example.myfundkt.bean.top.Diff
import com.example.myfundkt.db.entity.FoudInfoEntity
import com.example.myfundkt.http.Api
import com.example.myfundkt.http.GetRetrofit
import com.example.myfundkt.http.response.HolidayResponse
import com.example.myfundkt.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.set

private const val TAG = "MyViewModel"

class MyViewModel : ViewModel() {


    private val _topLiveData = MutableLiveData(listOf<Diff>())
    val topLiveData: LiveData<List<Diff>> = _topLiveData
    private var codes: List<String> = mutableListOf()

    private val _selectionData = MutableLiveData(listOf<Data>())
    val selectionLiveData: LiveData<List<Data>> = _selectionData

    private val _collection = MutableLiveData(listOf<CollectionBean>())
    val collection: LiveData<List<CollectionBean>> = _collection

    val infomationCode = MutableLiveData<String?>()

    //loading显示
    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility: LiveData<Int> = _progressBarVisibility


    init {
        initCode()
    }


    fun initCode() {
        _progressBarVisibility.value = View.VISIBLE
        initFundCoro()
        getHoliday()
        codes = emptyList()
        viewModelScope.launch {

            ktDao.getCodes()?.let {
                Log.d(TAG, "initCode: $it")
                codes = it
                initSelectedFundCoro()
            }
        }
    }


    fun initFundCoro() {
        val api = Push2api
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getfunds(getExponentMap())
                if (response.isSuccessful) {
                    val topBean = response.body()
                    topBean?.let {
                        val diffBeanList: List<Diff> = it.data.diff
                        Log.d(TAG, "initFundCoro: $diffBeanList")
                        if (codes.isEmpty()) {
                            _progressBarVisibility.postValue(View.GONE)
                        }
                        _topLiveData.postValue(diffBeanList)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "initFundCoro: ${e.message}", e)
            }
        }
    }

    //转换模型
    private suspend fun setSelectionData(data: List<Data>) {
        _selectionData.postValue(data)
        if (data.isNotEmpty()) {
            val list: MutableList<CollectionBean> = ArrayList()
            for (b in data) {
                val code: String = b.FCODE
                MyLog.d(TAG, "getFCODE: $code")
                var bean: CollectionBean? = null
                for (code1 in codes) {
                    MyLog.d(TAG, "codes: $code1")
                    if (code1 == code) {
                        MyLog.d(TAG, "codes: true")
                        bean = setLiveCollection(b, code)
                    }
                }
                if (bean != null) {
                    list.add(bean)
                }
            }
            Log.d(TAG, "setSelectionData: $list")
//            _collection.postValue(null)
            _collection.postValue(list.sortByMoneyDes)


        }
    }

    private suspend fun setLiveCollection(b: Data, code: String): CollectionBean? {

        MyLog.d(TAG, "setLiveCollection: $code")
        if (code.isNotEmpty()) {
            var entity: FoudInfoEntity?
            val collectionBean = viewModelScope.async {

                entity = ktDao.findByCode(code)
                Log.d(TAG, "setLiveCollection:FindByCode " + (entity?.code ?: 0))
                entity?.let {
                    val 持有份额: Double = entity!!.quantity //持有份额
                    val 成本价: Double = entity!!.cost //成本价
                    val 昨日价: String = b.NAV //昨日价
                    val 估算涨跌: String = b.GSZZL//估算涨跌
                    val 代码 = b.FCODE
                    val 名称 = b.SHORTNAME
                    val 时间 = b.GZTIME.substring(IntRange(10, 15))
                    val 涨跌幅: String = b.NAVCHGRT //涨跌幅
                    val 持有额 = cye(昨日价, 持有份额) //持有额
                    val 持有收益 = cysy(昨日价, 持有份额, 成本价) //持有收益
                    val 持有收益率 = cysyl(昨日价, 成本价) //持有收益率
                    var 估算收益: String //估算收益
                    Log.d(TAG, "setLiveCollection: " + b.PDATE)
                    Log.d(TAG, "setLiveCollection:时间 $时间")

                    var 涨跌: String
                    var updated = false
                    if (b.PDATE == b.GZTIME.substring(IntRange(0, 9))) {//已结算
                        Log.d(TAG, "setLiveCollection: 已结算")
                        估算收益 = sy(涨跌幅, 持有份额, 昨日价)
                        涨跌 = 涨跌幅
                        updated = true
                    } else {
                        Log.d(TAG, "setLiveCollection: 未结算")
                        估算收益 = gssy(估算涨跌, 持有份额, 昨日价)
                        涨跌 = 估算涨跌
                    }

                    return@async CollectionBean(
                        代码, 名称, 持有份额.decimalFomart, 持有额, 持有收益, "$持有收益率%",
                        "$涨跌%", 估算收益, 时间, updated
                    )
                }
            }
            return collectionBean.await()


        }
        return null

    }

    private fun getExponentMap(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["fltt"] = "2"
        map["fields"] = "f2,f3,f4,f12,f13,f14"
        map["_"] = System.currentTimeMillis().toString()
        map["secids"] = "1.000001,1.000300,100.HSI,0.399001,0.399006"
        return map

    }

    private fun getSellectionMap(): HashMap<String, String> {
        Log.d(TAG, "getSellectionMap: $codes")
        val map =
            HashMap<String, String>()
        map["pageIndex"] = "1"
        map["pageSize"] = "50"
        map["plat"] = "Android"
        map["product"] = "EFund"
        map["appType"] = "ttjj"
        map["Version"] = "1"
        map["deviceid"] = "ee11bd80-fa55-417d-b155-9eb08fc131ed"
        map["Fcodes"] = formatCode(codes)
        Log.d(TAG, "formatCode: ${map["Fcodes"]}")
        return map

    }


    private fun formatCode(list: List<String>): String {
//        var fo = StringBuilder()
//        for (s in list) {
//            fo.append(s).append(",")
//        }
//        if (fo.isNotEmpty()) {
//            fo.deleteLastChar
//            fo = StringBuilder(fo.substring(0, fo.length - 1))
//        }
        return try {
            val fo: StringBuilder = list.splitWithComma
            fo.deleteLastChar
        } catch (e: Exception) {
            String()
        }

    }


    /**
     * 持有额=last价*持有份额
     *
     * @param nav      昨日价
     * @param quantity 持有份额
     * @return 持有额
     */
    private val cye = { nav: String, quantity: Double ->
        try {
            val last = nav.toDouble()
            val res = last * quantity
            MyLog.d(TAG, "getCye: $res")
            res.decimalFomart
        } catch (e: Exception) {
            ""
        }
    }


    /**
     * 持有收益=（last价-成本价）*份额
     *
     * @param nav      昨日价
     * @param quantity 持有份额
     * @param cost     成本
     * @return 持有收益
     */
    private val cysy = { nav: String, quantity: Double, cost: Double ->
        try {
            val last = nav.toDouble()
            val res = (last - cost) * quantity
            MyLog.d(TAG, "getCysy: $res")
            res.decimalFomart
        } catch (e: Exception) {
            ""
        }
    }


    /**
     * 持有收益率=（last价-成本价）/成本价
     *
     * @param nav  昨日价
     * @param cost 成本
     * @return 持有收益率
     */
    private val cysyl = { nav: String, cost: Double ->
        try {
            val last = nav.toDouble()
            var res = (last - cost) / cost
            res *= 100
            MyLog.d(TAG, "getCysyl: $res")
            res.decimalFomart
        } catch (e: Exception) {
            ""
        }
    }


    /**
     * 估算收益=涨跌幅*持有额
     *
     * @param gszzl    涨跌幅
     * @param quantity 持有额
     * @return 估算收益
     */
    private val gssy = { gszzl: String, quantity: Double, nav: String ->
        try {
            if (gszzl == "--") {
                "0.0"
            }
            val last = nav.toDouble()
            var gszdf = gszzl.toDouble()
            MyLog.d(TAG, "zdf: $gszdf")
            gszdf *= 0.01
            val res = gszdf * last * quantity
            MyLog.d(TAG, "getGssy: $res")
            res.decimalFomart
        } catch (e: Exception) {
            MyLog.e(TAG, "getGssy" + e.message)
            ""
        }
    }


    /**
     * 实际收益=【(今日价/(1+涨跌幅))-今日价】*持有额
     *    =[-(涨跌幅)/(1+涨跌幅)]*今日价*持有额
     * @param gszzl    涨跌幅
     * @param quantity 持有额
     * @param nav 今日价
     * @return 估算收益
     */

    private val sy = { gszzl: String, quantity: Double, nav: String ->
        try {
            if (gszzl == "--") {
                "0.0"
            }
            val today = nav.toDouble()
            var gszdf = gszzl.toDouble()
            MyLog.d(TAG, "zdf: $gszdf")
            gszdf *= 0.01
            val res = quantity * (today - (today / (1 + gszdf)))
            MyLog.d(TAG, "getGssy: $res")
            res.decimalFomart
        } catch (e: Exception) {
            MyLog.e(TAG, "getGssy" + e.message)
            ""
        }
    }

    fun initSelectedFundCoro() {
        val api = Fundmobapi
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _progressBarVisibility.postValue(View.GONE)
                val response = api.getSellected(getSellectionMap())
                if (response.isSuccessful) {
                    val selectionBean = response.body()
                    selectionBean?.let {
                        it.Datas?.let { it1 ->
                            setSelectionData(it1)
                            Log.d(TAG, "initSelectedFundCoro: " + it1.size)
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "initSelectedFundCoro: ", e)

            }
        }
    }

    private fun getHoliday() {
        @SuppressLint("SimpleDateFormat") val today =
            SimpleDateFormat("yyyy-MM-dd").format(Date().time)
        MyLog.d(TAG, "today: $today")
        object : HolidayResponse(
            GetRetrofit.getDate().create(Api::class.java)
                .getHoliday("https://timor.tech/api/holiday/info/$today")
        ) {
            override fun holiday() {
                MyLog.d(
                    TAG,
                    "HolidayNoTask: " + Date().time
                )
            }

            override fun workDay() {
                viewModelScope.launch(Dispatchers.IO) {
                    MyLog.d(TAG, "Redo: " + LocalTime.now())
                    val timer = Timer()
                    if (LocalTime.now().isAfter(LocalTime.of(9, 30)) && LocalTime.now().isBefore(
                            LocalTime.of(15, 0)
                        )
                    ) {
                        MyLog.d(TAG, "Redo:2")
                        val timerTask: TimerTask = object : TimerTask() {
                            override fun run() {
                                MyLog.d(TAG, "Redo: ")
                                initFundCoro()
                                initSelectedFundCoro()
                                MyLog.d(
                                    TAG,
                                    "TimerTask: " + Date().time
                                )
                            }
                        }
                        timer.schedule(
                            timerTask,
                            60000,  //延迟60秒执行
                            60000
                        )
                    } else {
                        timer.cancel()
                        this.cancel()
                    }
                }


            }

        }
    }


}