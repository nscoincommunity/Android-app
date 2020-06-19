package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Orderbook(
    @SerializedName("bid") val buyOrders: List<OrderbookOrder>,
    @SerializedName("ask") val sellOrders: List<OrderbookOrder>
) : Parcelable {


    val areBuyOrdersEmpty: Boolean
        get() = buyOrders.isEmpty()


    val areSellOrdersEmpty: Boolean
        get() = sellOrders.isEmpty()


    val isEmpty: Boolean
        get() = (areBuyOrdersEmpty && areSellOrdersEmpty)


    val largestOrdersCount: Int
        get() {
            return if(buyOrders.size > sellOrders.size) {
                buyOrders.size
            } else {
                sellOrders.size
            }
        }


    @IgnoredOnParcel
    var buyOrdersVolume: Double = getOrdersVolume(buyOrders)
        private set
        get() {
            if((field == 0.0) && !areBuyOrdersEmpty) {
                field = getOrdersVolume(buyOrders)
            }

            return field
        }


    @IgnoredOnParcel
    var sellOrdersVolume: Double = getOrdersVolume(sellOrders)
        private set
        get() {
            if((field == 0.0) && !areSellOrdersEmpty) {
                field = getOrdersVolume(sellOrders)
            }

            return field
        }


    /**
     * Truncates the orderbook's orders (buy and sell) to the
     * specified size.
     *
     * @param bidOrdersSize The size of bid orders to truncate to
     * @param askOrdersSize The size of ask orders to truncate to
     *
     * @return The truncated orderbook
     */
    fun truncate(bidOrdersSize: Int, askOrdersSize: Int): Orderbook {
        val shouldTruncateBuyOrders = (buyOrders.size > bidOrdersSize)
        val shouldTruncateSellOrders = (sellOrders.size > askOrdersSize)

        if(!shouldTruncateBuyOrders && !shouldTruncateSellOrders) {
            return this
        }

        val buyOrders = if(shouldTruncateBuyOrders) {
            buyOrders.take(bidOrdersSize)
        } else {
            buyOrders
        }

        val sellOrders = if(shouldTruncateSellOrders) {
            sellOrders.take(askOrdersSize)
        } else {
            sellOrders
        }

        return Orderbook(buyOrders, sellOrders)
    }


    private fun getOrdersVolume(orders: List<OrderbookOrder>): Double {
        var volume = 0.0

        for(order in orders) {
            volume += order.baseCurrencyAmount
        }

        return volume
    }


}