package com.example.myfundkt.ui.fragment.importData

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfundkt.bean.importData.ImportDataBean
import com.example.myfundkt.bean.information.InformationData
import com.example.myfundkt.db.DbRepository
import com.example.myfundkt.db.entity.FoudInfoEntity
import com.example.myfundkt.utils.ToastUtil
import com.google.gson.Gson

class ImportDataViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private var repository: DbRepository = DbRepository()
     var _count : Int = 0
    private val _nowIndex = MutableLiveData<Int>(0)
    val nowIndex: LiveData<Int> = _nowIndex


    fun fromImport(string: String){
        try {
            val importDataBean: ImportDataBean = Gson().fromJson(string, ImportDataBean::class.java)
            with(importDataBean){
                _count= fundListM?.size ?:0

                fundListM?.forEach {
                    _nowIndex.value=_nowIndex.value?.plus(1)
                    val num: Double = it.num.toString().toDouble()
                    val fundEntity = it.cost?.let { it1 ->
                        FoudInfoEntity(it.code,num,
                            it1.toDouble())
                    }
                    repository.InsertInfo(fundEntity)
                }
            }
        }catch (e: Exception){
            Log.e("ImportDataViewModel", "fromImport: ",e )
            ToastUtil.show("格式错误")
        }

    }
}