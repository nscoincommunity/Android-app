package com.stocksexchange.android.socket.model.data.activeorders

import com.google.gson.annotations.SerializedName

data class ActiveOrderStatusUpdateSocketData(
    @SerializedName("id") val orderId: Long,
    @SerializedName("currency_pair_id") val currencyPairId: Int,
    @SerializedName("status") val newStatusStr: String
)