package com.example.myfundkt.db

import com.example.myfundkt.db.dao.FoudInfoDao
import com.example.myfundkt.db.dao.SortDao
import com.example.myfundkt.db.entity.FoudInfoEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class KtRepositiry(
    var dbMaster: DbMaster? = DbMaster.getDbMaster(),
    var foudInfoDao: FoudInfoDao? = dbMaster?.foudInfoDao,
    var  sortDao: SortDao? = dbMaster?.sortDao
) {
//    private suspend fun insert(foudInfoEntity: FoudInfoEntity) {
//        var
//    }



    fun insert(foudInfoEntity: FoudInfoEntity) {
        GlobalScope.launch {
            foudInfoDao?.insertFoudInfo(foudInfoEntity)
        }
    }





}