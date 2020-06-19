package com.stocksexchange.android.utils.diffcallbacks

import com.stocksexchange.android.model.OrderData
import com.stocksexchange.android.ui.orders.fragment.OrderItem
import com.stocksexchange.android.utils.diffcallbacks.base.BaseDiffCallback

class OrdersDiffCallback(
    oldList: List<OrderItem>,
    newList: List<OrderItem>
) : BaseDiffCallback<OrderData, OrderItem>(oldList, newList) {


    override fun areItemsTheSame(oldItem: OrderData, newItem: OrderData): Boolean {
        return (oldItem.order.id == newItem.order.id)
    }


}