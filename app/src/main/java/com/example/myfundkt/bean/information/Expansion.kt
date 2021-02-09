package com.example.myfundkt.bean.information


import com.google.gson.annotations.SerializedName

data class Expansion(
    @SerializedName("BUY")
    var bUY: Boolean?,
    @SerializedName("DTZT")
    var dTZT: String?,
    @SerializedName("DWJZ")
    var dWJZ: String?,
    @SerializedName("FCODE")
    var fCODE: String?,
    @SerializedName("GSZZL")
    var gSZZL: String?,
    @SerializedName("GZ")
    var gZ: String?,
    @SerializedName("GZTIME")
    var gZTIME: String?,
    @SerializedName("GZZF")
    var gZZF: String?,
    @SerializedName("ISBUY")
    var iSBUY: String?,
    @SerializedName("JZRQ")
    var jZRQ: String?,
    @SerializedName("Market")
    var market: String?,
    @SerializedName("rate")
    var rate: String?,
    @SerializedName("SGZT")
    var sGZT: String?,
    @SerializedName("SHORTNAME")
    var sHORTNAME: String?,
    @SerializedName("SHZT")
    var sHZT: String?,
    @SerializedName("SOURCERATE")
    var sOURCERATE: String?
)