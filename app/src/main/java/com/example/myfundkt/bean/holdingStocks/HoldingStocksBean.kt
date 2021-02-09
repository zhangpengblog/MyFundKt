package com.example.myfundkt.bean.holdingStocks

data class HoldingStocksBean(
    var Datas: Datas? = Datas(),
    var ErrCode: Int? = 0,
    var ErrMsg: Any? = Any(),
    var ErrorCode: String? = "",
    var ErrorMessage: Any? = Any(),
    var ErrorMsgLst: Any? = Any(),
    var Expansion: String? = "",
    var Message: Any? = Any(),
    var Success: Boolean? = false,
    var TotalCount: Int? = 0
)