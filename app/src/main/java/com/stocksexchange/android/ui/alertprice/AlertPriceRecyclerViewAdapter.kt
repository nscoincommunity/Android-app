package com.stocksexchange.android.ui.alertprice

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.stocksexchange.api.model.rest.AlertPrice

class AlertPriceRecyclerViewAdapter(
    context: Context,
    items: MutableList<AlertPriceItem>
) : TrackableRecyclerViewAdapter<Long, AlertPriceItem, AlertPriceItem.ViewHolder>(context, items) {


    private lateinit var mResources: AlertPriceResources

    var onItemMoreDeleteListener: ((View, AlertPriceItem, Int) -> Unit)? = null
    var onItemLessDeleteListener: ((View, AlertPriceItem, Int) -> Unit)? = null
    var onItemClickListener: ((View, AlertPriceItem, Int) -> Unit)? = null



    override fun assignListeners(holder: AlertPriceItem.ViewHolder, position: Int, item: AlertPriceItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnMoreDeleteListener(holder, position, onItemMoreDeleteListener)
            setOnLessDeleteListener(holder, position, onItemLessDeleteListener)
            setOnItemClickListener(holder, position, onItemClickListener)
        }
    }


    fun getItemPosition(item: AlertPrice): Int? {
        return items.indices.firstOrNull {
            items[it].itemModel.currencyPairId == item.id
        }
    }


    fun setResources(resources: AlertPriceResources) {
        mResources = resources
    }


    override fun getResources(): AlertPriceResources? {
        return mResources
    }


}