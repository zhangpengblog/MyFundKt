package com.example.myfundkt.bean.information


import com.google.gson.annotations.SerializedName

data class InformationData(
    @SerializedName("Datas")
    val datas: List<String>?,
    @SerializedName("ErrCode")
    val errCode: Int?,
    @SerializedName("ErrMsg")
    val errMsg: Any?,
    @SerializedName("Expansion")
    val expansion: Expansion?,
    @SerializedName("TotalCount")
    val totalCount: Int?
)