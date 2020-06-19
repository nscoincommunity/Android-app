package com.stocksexchange.android.ui.orders.fragment

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.stocksexchange.api.model.rest.Order
import com.stocksexchange.api.model.rest.OrderLifecycleType
import com.stocksexchange.api.model.rest.SortOrder
import com.stocksexchange.api.model.rest.OrderSelectivityType

class OrdersRecyclerViewAdapter(
    context: Context,
    items: MutableList<OrderItem>
) : TrackableRecyclerViewAdapter<Long, OrderItem, OrderItem.ViewHolder>(context, items) {


    private lateinit var mResources: OrderResources

    var onMarketNameClickListener: ((View, OrderItem, Int) -> Unit)? = null
    var onCancelBtnClickListener: ((View, OrderItem, Int) -> Unit)? = null




    override fun assignListeners(holder: OrderItem.ViewHolder, position: Int, item: OrderItem) {
        super.assignListeners(holder, position, item)

        item.setOnMarketNameClickListener(holder, resources, position, onMarketNameClickListener)
        item.setOnCancelBtnClickListener(holder, position, onCancelBtnClickListener)
    }


    fun setOrderLifecycleType(orderLifecycleType: OrderLifecycleType) {
        mResources.orderLifecycleType = orderLifecycleType
    }


    fun setOrderSelectivityType(orderSelectivityType: OrderSelectivityType) {
        mResources.orderSelectivityType = orderSelectivityType
    }


    fun getOrderPosition(order: Order): Int? {
        return items.indices.firstOrNull {
            items[it].itemModel.order.id == order.id
        }
    }


    fun getChronologicalPositionForOrder(order: Order, sortOrder: SortOrder): Int {
        return items.indices.firstOrNull {
            items[it].itemModel.order.compareTo(order) == (if(sortOrder == SortOrder.ASC) 1 else -1)
        } ?: itemCount
    }


    fun setResources(resources: OrderResources) {
        mResources = resources
    }


    override fun getResources(): OrderResources? {
        return mResources
    }


}