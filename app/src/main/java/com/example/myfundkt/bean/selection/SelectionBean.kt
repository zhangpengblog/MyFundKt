package com.example.myfundkt.bean.selection

data class SelectionBean(
    val Datas: List<Data>? = null,
    val ErrCode: Int?=0,
    val ErrMsg: Any,
    val ErrorCode: String,
    val ErrorMessage: Any,
    val ErrorMsgLst: Any,
    val Expansion: Expansion,
    val Message: Any,
    val Success: Boolean,
    val TotalCount: Int
)