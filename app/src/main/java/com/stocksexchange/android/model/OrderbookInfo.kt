package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.api.model.rest.Orderbook
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderbookInfo(
    val lowestAsk: Double,
    val highestBid: Double,
    val buyVolume: Double,
    val sellVolume: Double
) : Parcelable {


    companion object {


        fun newInstance(orderbook: Orderbook): OrderbookInfo {
            return OrderbookInfo(
                lowestAsk = if(orderbook.areSellOrdersEmpty) 0.0 else orderbook.sellOrders.first().price,
                highestBid = if(orderbook.areBuyOrdersEmpty) 0.0 else orderbook.buyOrders.first().price,
                buyVolume = orderbook.buyOrdersVolume,
                sellVolume = orderbook.sellOrdersVolume
            )
        }


    }


    val hasLowestAsk: Boolean
        get() = (lowestAsk > 0.0)


    val hasHighestBid: Boolean
        get() = (highestBid > 0.0)


    val hasSellVolume: Boolean
        get() = (sellVolume > 0.0)


    val hasBuyVolume: Boolean
        get() = (buyVolume > 0.0)


}