package com.stocksexchange.android.ui.currencymarketpreview.views.orderbook

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.stocksexchange.android.ui.currencymarketpreview.views.orderbook.items.OrderbookHeaderItem
import com.stocksexchange.android.ui.currencymarketpreview.views.orderbook.items.OrderbookOrderItem

class OrderbookRecyclerViewAdapter(
    context: Context,
    items: MutableList<BaseItem<*, *, *>>
) : TrackableRecyclerViewAdapter<String, BaseItem<*, *, *>, BaseItem.ViewHolder<*>>(context, items) {


    private lateinit var mResources: OrderbookResources

    var onHeaderMoreButtonClickListener: ((View, OrderbookHeaderItem, Int) -> Unit)? = null
    var onItemClickListener: ((View, OrderbookOrderItem, Int) -> Unit)? = null




    override fun assignListeners(holder: BaseItem.ViewHolder<*>, position: Int, item: BaseItem<*, *, *>) {
        super.assignListeners(holder, position, item)

        when(item.getLayout()) {

            OrderbookHeaderItem.BID_ITEM_LAYOUT_ID,
            OrderbookHeaderItem.ASK_ITEM_LAYOUT_ID -> {
                (item as OrderbookHeaderItem).setOnMoreButtonClickListener(
                    (holder as OrderbookHeaderItem.ViewHolder),
                    position,
                    onHeaderMoreButtonClickListener
                )
            }

            OrderbookOrderItem.BUY_ITEM_LAYOUT_ID,
            OrderbookOrderItem.SELL_ITEM_LAYOUT_ID -> {
                (item as OrderbookOrderItem).setOnItemClickListener(
                    (holder as OrderbookOrderItem.ViewHolder),
                    position,
                    onItemClickListener
                )
            }

        }
    }


    fun setResources(resources: OrderbookResources) {
        mResources = resources
    }


    override fun getResources(): OrderbookResources? {
        return mResources
    }


}