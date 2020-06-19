package com.stocksexchange.android.socket.model.data.pricechart

import com.google.gson.annotations.SerializedName
import com.stocksexchange.api.model.rest.CandleStick

data class CandleStickSocketData(
    @SerializedName("_id") val id: CandleStickId,
    @SerializedName("open") val openPrice: Double,
    @SerializedName("high") val highPrice: Double,
    @SerializedName("low") val lowPrice: Double,
    @SerializedName("close") val closePrice: Double,
    @SerializedName("volume") val volume: Double
) {


    fun toCandleStick(): CandleStick {
        return CandleStick(
            openPrice = openPrice,
            highPrice = highPrice,
            lowPrice = lowPrice,
            closePrice = closePrice,
            volume = volume,
            timestamp = id.timestampInMillis
        )
    }


}