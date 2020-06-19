package com.stocksexchange.android.socket.model.data.pricechart

import com.google.gson.annotations.SerializedName

data class CandleStickId(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("currency_pair_id") val currencyPairId: Int
) {


    val timestampInMillis: Long
        get() = (timestamp * 1000L)


}