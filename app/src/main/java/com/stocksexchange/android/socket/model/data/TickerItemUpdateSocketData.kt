package com.stocksexchange.android.socket.model.data

import com.google.gson.annotations.SerializedName

data class TickerItemUpdateSocketData(
    @SerializedName("id") val id: Int,
    @SerializedName("lastPrice") val lastPrice: Double,
    @SerializedName("lastPriceDayAgo") val openPrice: Double,
    @SerializedName("minSell") val lowPrice: Double,
    @SerializedName("maxBuy") val highPrice: Double,
    @SerializedName("volumeSum") val dailyVolumeInBaseCurrency: Double,
    @SerializedName("market_volume") val dailyVolumeInQuoteCurrency: Double
)