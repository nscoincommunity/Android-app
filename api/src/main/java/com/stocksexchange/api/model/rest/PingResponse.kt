package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PingResponse(
    @SerializedName("server_datetime") val datetime: String,
    @SerializedName("server_timestamp") val timestamp: Long
) : Parcelable