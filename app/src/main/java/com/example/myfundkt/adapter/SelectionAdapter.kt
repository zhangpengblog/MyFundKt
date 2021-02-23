package com.example.myfundkt.adapter

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.myfundkt.R
import com.example.myfundkt.bean.CollectionBean
import com.example.myfundkt.utils.DKGREEN
import com.example.myfundkt.utils.MyLog

private const val TAG = "SelectionAdapter"

class SelectionAdapter(CollectionBean: MutableList<CollectionBean>?) :
    BaseQuickAdapter<CollectionBean, BaseViewHolder>(
        R.layout.item_my_fund,
        CollectionBean
    ) {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun convert(holder: BaseViewHolder, item: CollectionBean) {
        with(item) {

            MyLog.d(TAG, "gssy: $估算收益")
            holder.setText(R.id.name, 名称)
                .setText(R.id.cye, 持有额)
                .setText(R.id.cysy, 持有收益)
                .setText(R.id.cysyl, 持有收益率)
                .setText(R.id.gssy, 估算收益)
                .setText(R.id.fe, 持有份额)
                .setText(R.id.zdf, 涨跌幅)
                .setText(R.id.time, 时间)

            涨跌幅?.let {
                if (it.contains("-")) {
                    holder.setTextColor(R.id.zdf, DKGREEN)
                } else {
                    holder.setTextColor(R.id.zdf, Color.RED)
                }
            }


            持有收益?.let {
                if (it.contains("-")) {
                    holder.setTextColor(R.id.cysy, DKGREEN)
                } else {
                    holder.setTextColor(R.id.cysy, Color.RED)
                }
            }
            估算收益?.let {
                if (it.contains("-")) {
                    holder.setTextColor(R.id.gssy, DKGREEN)
                } else {
                    holder.setTextColor(R.id.gssy, Color.RED)
                }
            }
            持有收益率?.let {
                if (it.contains("-")) {
                    holder.setTextColor(R.id.cysyl, DKGREEN)
                } else {
                    holder.setTextColor(R.id.cysyl, Color.RED)
                }
            }

            updated?.let {
                if (it) {
                    holder.setGone(R.id.appCompatImageView, false)
                } else {
                    holder.setGone(R.id.appCompatImageView, true)
                }
            }
        }

    }
}

