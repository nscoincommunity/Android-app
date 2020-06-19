package com.stocksexchange.android.socket.model.data

import com.google.gson.annotations.SerializedName

data class InboxCountUpdateSocketData(
    @SerializedName("count") val count: Int
)