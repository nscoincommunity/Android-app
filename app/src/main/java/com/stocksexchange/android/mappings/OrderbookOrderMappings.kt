package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.OrderbookOrder
import com.stocksexchange.android.model.DataActionItem

fun List<DataActionItem<OrderbookOrder>>.mapToPriceOrderbookOrderActionItemsMap()
    : Map<Double, DataActionItem<OrderbookOrder>> {
    return mutableMapOf<Double, DataActionItem<OrderbookOrder>>().apply {
        for(item in this@mapToPriceOrderbookOrderActionItemsMap) {
            put(item.dataItem.price, item)
        }
    }
}