package com.example.myfundkt.bean.importData

/*
{
	"BadgeContent": 2,
	"BadgeType": 2,
	"RealtimeFundcode": null,
	"darkMode": true,
	"fundListM": [{
		"code": "005827",
		"cost": "3.1798",
		"num": "628.97"
	}, {
		"code": "110011",
		"cost": "9.3821",
		"num": "241.74"
	}, {
		"code": "005969",
		"cost": "3.2274",
		"num": "61.97"
	}, {
		"code": "009068",
		"cost": "2.0566",
		"num": "194.50"
	}, {
		"code": "519674",
		"cost": "5.9476",
		"num": "90.66"
	}, {
		"code": "007882",
		"cost": "1.1541",
		"num": "303.31"
	}, {
		"code": "968061",
		"cost": "20.0723",
		"num": "24.91"
	}, {
		"code": "110023",
		"num": 0
	}, {
		"code": "007465",
		"num": 0
	}, {
		"code": "161725",
		"num": 0
	}, {
		"code": "512000",
		"num": 0
	}, {
		"code": "001595",
		"num": 0
	}],
	"grayscaleValue": 0,
	"isLiveUpdate": true,
	"normalFontSize": false,
	"opacityValue": 10,
	"seciList": ["1.000001", "1.000300", "100.HSI", "0.399006"],
	"showAmount": true,
	"showBadge": 1,
	"showCost": true,
	"showCostRate": true,
	"showGSZ": true,
	"showGains": true,
	"sortTypeObj": {
		"name": "amount",
		"type": "desc"
	},
	"userId": "ee11bd80-fa55-417d-b155-9eb08fc131ed",
	"version": "2.5.1"
} */
data class ImportDataBean(
    var BadgeContent: Int? = 0,
    var BadgeType: Int? = 0,
    var RealtimeFundcode: Any? = Any(),
    var darkMode: Boolean? = false,
    var fundListM: List<FundM>? = listOf(),
    var grayscaleValue: Int? = 0,
    var isLiveUpdate: Boolean? = false,
    var normalFontSize: Boolean? = false,
    var opacityValue: Int? = 0,
    var seciList: List<String>? = listOf(),
    var showAmount: Boolean? = false,
    var showBadge: Int? = 0,
    var showCost: Boolean? = false,
    var showCostRate: Boolean? = false,
    var showGSZ: Boolean? = false,
    var showGains: Boolean? = false,
    var sortTypeObj: SortTypeObj? = SortTypeObj(),
    var userId: String? = "",
    var version: String? = ""
)