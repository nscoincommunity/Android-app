package com.stocksexchange.api.model.rest

import com.google.gson.annotations.SerializedName

data class InboxDeleteItemResponse(
    @SerializedName("message") val message: String
)