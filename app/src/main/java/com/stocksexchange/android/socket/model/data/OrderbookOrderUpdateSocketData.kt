package com.stocksexchange.android.socket.model.data

import com.google.gson.annotations.SerializedName
import com.stocksexchange.api.model.rest.OrderbookOrder

data class OrderbookOrderUpdateSocketData(
    @SerializedName("currency_pair_id") val currencyPairId: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("amount") val baseCurrencyAmount: Double,
    @SerializedName("amount2") val quoteCurrencyAmount: Double
) {


    fun toOrderbookOrder(): OrderbookOrder {
        return OrderbookOrder(
            price = price,
            baseCurrencyAmount = baseCurrencyAmount,
            quoteCurrencyAmount = quoteCurrencyAmount
        )
    }


}