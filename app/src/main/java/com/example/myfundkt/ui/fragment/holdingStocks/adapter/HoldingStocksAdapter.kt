package com.example.myfundkt.ui.fragment.holdingStocks.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.myfundkt.R
import com.example.myfundkt.bean.holdingStocks.HoldingData
import com.example.myfundkt.utils.DKGREEN

var format = "%.2f"

class HoldingStocksAdapter(holdingDatas: MutableList<HoldingData>?) :
    BaseQuickAdapter<HoldingData, BaseViewHolder>(
        R.layout.item_holding_stocks, holdingDatas
    ) {


    override fun convert(holder: BaseViewHolder, item: HoldingData) {
        with(item) {
            var proportion = ""
            var change = ""
            var ZD = ""
            PCTNVCHG?.let { change = "$it%" }
            JZBL?.let { proportion = "$it%" }
            zdf?.let { ZD = "$it%" }
            holder.setText(R.id.nameAndcode, nameAndCode)
                .setText(R.id.change, change)
                .setText(R.id.proportion, proportion)
                .setText(R.id.price, price.toString())
                .setText(R.id.zdf, ZD)
            if (change.contains("-")) {
                holder.setTextColor(R.id.change, DKGREEN)
            } else {
                holder.setTextColor(R.id.change, Color.RED)
            }
            if (ZD.contains("-")) {
                holder.setTextColor(R.id.zdf, DKGREEN)
            } else {
                holder.setTextColor(R.id.zdf, Color.RED)
            }
        }
    }
}