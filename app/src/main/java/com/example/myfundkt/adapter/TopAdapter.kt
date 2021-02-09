package com.example.myfundkt.adapter

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.myfundkt.R
import com.example.myfundkt.bean.top.Diff

class TopAdapter(diff: MutableList<Diff>?) :
    BaseQuickAdapter<Diff, BaseViewHolder>(R.layout.item_top, diff) {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun convert(holder: BaseViewHolder, item: Diff) {
        with(item){
            holder.setText(R.id.name, f14)
                .setText(R.id.socre, f2.toString() + "")
                .setText(R.id.change, f3.toString() + "%")
                .setText(R.id.change2, f4.toString() + "")
        }

        val change: String = item.f3.toString() + "%"
        if (change.contains("-")) {
            holder.setTextColor(R.id.socre, Color.GREEN)
                .setTextColor(R.id.change, Color.GREEN)
                .setTextColor(R.id.change2, Color.GREEN)
//            holder.itemView.outlineSpotShadowColor = Color.GREEN
        } else {
            holder.setTextColor(R.id.socre, Color.RED)
                .setTextColor(R.id.change, Color.RED)
                .setTextColor(R.id.change2, Color.RED)
//            holder.itemView.outlineSpotShadowColor = Color.RED
        }

    }
}