package com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory.items.TradeHistoryItem

class TradeHistoryRecyclerViewAdapter(
    context: Context,
    items: MutableList<BaseItem<*, *, *>>
) : TrackableRecyclerViewAdapter<Long, BaseItem<*, *, *>, BaseItem.ViewHolder<*>>(context, items) {


    private lateinit var mResources: TradeHistoryResources

    private val viewBinderHelper = ViewBinderHelper()

    var onCancelBtnClickListener: ((View, TradeHistoryItem, Int) -> Unit)? = null




    override fun onBindViewHolder(holder: BaseItem.ViewHolder<*>, position: Int) {
        val item = (getItem(position) ?: return)

        when(item.getLayout()) {
            TradeHistoryItem.MAIN_LAYOUT_ID -> {
                val tradeHistoryItem = (item as TradeHistoryItem)
                val tradeHistoryItemViewHolder = (holder as TradeHistoryItem.ViewHolder)

                viewBinderHelper.bind(
                    tradeHistoryItemViewHolder.mSwipeRevealLayout,
                    tradeHistoryItem.trackKey.toString()
                )

                tradeHistoryItem.setOnCancelBtnClickListener(
                    tradeHistoryItemViewHolder,
                    position,
                    onCancelBtnClickListener
                )
            }
        }

        super.onBindViewHolder(holder, position)
    }


    fun setResources(resources: TradeHistoryResources) {
        mResources = resources
    }


    override fun getResources(): TradeHistoryResources? {
        return mResources
    }


}