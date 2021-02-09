package com.example.myfundkt.bean.holdingStocks

data class Datas(
    var ETFCODE: Any? = Any(),
    var ETFSHORTNAME: Any? = Any(),
    var fundStocks: List<FundStock>? = listOf(),
    var fundboods: List<Fundbood>? = listOf(),
    var fundfofs: List<Any>? = listOf()
)