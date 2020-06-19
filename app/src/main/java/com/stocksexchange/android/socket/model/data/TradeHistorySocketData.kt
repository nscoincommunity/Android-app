package com.stocksexchange.android.socket.model.data

import com.google.gson.annotations.SerializedName
import com.stocksexchange.api.model.rest.Trade

data class TradeHistorySocketData(
    @SerializedName("id") val tradeId: Long,
    @SerializedName("currency_pair_id") val currencyPairId: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("amount") val amount: Double,
    @SerializedName("order_type") val type: String,
    @SerializedName("timestamp") val timestamp: Long
) {


    fun toTrade(): Trade {
        return Trade(
            id = tradeId,
            price = price,
            amount = amount,
            typeStr = type,
            timestamp = timestamp
        )
    }


}