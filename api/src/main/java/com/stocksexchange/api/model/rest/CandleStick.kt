package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CandleStick(
    @SerializedName("open") val openPrice: Double,
    @SerializedName("high") val highPrice: Double,
    @SerializedName("low") val lowPrice: Double,
    @SerializedName("close") val closePrice: Double,
    @SerializedName("volume") val volume: Double,
    @SerializedName("time") val timestamp: Long
) : Parcelable