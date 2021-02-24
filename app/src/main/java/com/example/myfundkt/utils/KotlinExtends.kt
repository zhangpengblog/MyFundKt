package com.example.myfundkt.utils

import android.view.View
import com.example.myfundkt.bean.CollectionBean
import com.example.myfundkt.db.KtDatabase
import com.example.myfundkt.db.dao.KtDao
import com.example.myfundkt.http.GetRetrofit
import com.example.myfundkt.http.KtApi
import com.google.android.material.snackbar.Snackbar


val StringBuilder.deleteLastChar: String
    get() = StringBuilder(this.substring(0, this.length - 1)).toString()

val List<String>.splitWithComma: StringBuilder
    get() {
        val fo = StringBuilder()
        for (s in this) {
            fo.append(s).append(",")
        }
        return fo
    }


val DKGREEN: Int
    get() = 0xFF00880E.toInt()

val Float.decimalFomart
    get() = String.format("%.2f", this)

val Double.decimalFomart
    get() = String.format("%.2f", this)

fun percentFomart (a:Float,b:Float):String{
   return try {
        ((a / b) * 100).decimalFomart+"%"
    }catch (e:Exception){
        ""
    }
}

fun percentFomart (a:Double,b:Double):String{
    return try {
        ((a / b) * 100).decimalFomart+"%"
    }catch (e:Exception){
        ""
    }
}



val Fundmobapi: KtApi
    get() = GetRetrofit.getFundmobapi().create(KtApi::class.java)

val Push2api: KtApi
    get() = GetRetrofit.getPush2().create(KtApi::class.java)


fun String.longSnack(view: View) {
    try {
        Snackbar.make(view, this, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    } catch (e: Exception) {
        println(e.message)
    }
}

fun String.shortSnack(view: View) {
    try {
        Snackbar.make(view, this, Snackbar.LENGTH_SHORT)
            .setAction("Action", null).show()
    } catch (e: Exception) {
        println(e.message)
    }
}

val List<CollectionBean>.sortByMoneyDes: List<CollectionBean>
    get() {
        return try {
            this.sortedByDescending { collectionBean -> collectionBean.持有额?.toFloat() }
        } catch (e: Exception) {
            this
        }

    }

val ktDao: KtDao
    get() = KtDatabase.dataBase.getDao()