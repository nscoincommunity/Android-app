package com.stocksexchange.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PerformedOrderActions(
    val statusChangedOrdersMap: MutableMap<Long, OrderData> = mutableMapOf(),
    val filledAmountChangedOrdersMap: MutableMap<Long, OrderData> = mutableMapOf()
) : Parcelable {


    fun addStatusChangedOrder(orderData: OrderData): PerformedOrderActions {
        statusChangedOrdersMap[orderData.order.id] = orderData
        return this
    }


    fun removeStatusChangedOrder(orderData: OrderData): PerformedOrderActions {
        statusChangedOrdersMap.remove(orderData.order.id)
        return this
    }


    fun removeAllStatusChangedOrders() {
        statusChangedOrdersMap.clear()
    }


    fun hasStatusChangedOrders(): Boolean {
        return statusChangedOrdersMap.isNotEmpty()
    }


    fun addFilledAmountChangedOrder(orderData: OrderData): PerformedOrderActions {
        filledAmountChangedOrdersMap[orderData.order.id] = orderData
        return this
    }


    fun removeFilledAmountChangedOrder(orderData: OrderData): PerformedOrderActions {
        filledAmountChangedOrdersMap.remove(orderData.order.id)
        return this
    }


    fun removeAllFilledAmountChangedOrders() {
        filledAmountChangedOrdersMap.clear()
    }


    fun hasFilledAmountChangedOrders(): Boolean {
        return filledAmountChangedOrdersMap.isNotEmpty()
    }


    fun merge(actions: PerformedOrderActions): PerformedOrderActions {
        statusChangedOrdersMap.putAll(actions.statusChangedOrdersMap)
        filledAmountChangedOrdersMap.putAll(actions.filledAmountChangedOrdersMap)

        return this
    }


    fun clear() {
        if(isEmpty()) {
            return
        }

        if(hasStatusChangedOrders()) {
            removeAllStatusChangedOrders()
        }

        if(hasFilledAmountChangedOrders()) {
            removeAllFilledAmountChangedOrders()
        }
    }


    fun isEmpty(): Boolean {
        return (!hasStatusChangedOrders() &&
                !hasFilledAmountChangedOrders())
    }


}