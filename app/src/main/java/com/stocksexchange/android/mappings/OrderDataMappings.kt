package com.stocksexchange.android.mappings

import com.stocksexchange.android.model.OrderData
import com.stocksexchange.android.ui.orders.fragment.OrderItem

fun OrderData.mapToOrderItem(): OrderItem {
    return OrderItem(this)
}


fun List<OrderData>.mapToOrderItemList(): List<OrderItem> {
    return map { it.mapToOrderItem() }
}