package com.stocksexchange.android.socket.model.data.activeorders

import com.google.gson.annotations.SerializedName

data class ActiveOrderFillUpdateSocketData(
    @SerializedName("id") val orderId: Long,
    @SerializedName("currency_pair_id") val currencyPairId: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("amount") val baseCurrencyAmount: Double,
    @SerializedName("amount2") val quoteCurrencyAmount: Double
)