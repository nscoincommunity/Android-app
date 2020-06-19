package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlertPrice(
    @SerializedName("id") val id: Int,
    @SerializedName("currency_pair_id") val currencyPairId: Int,
    @SerializedName("currency_pair_name") val currencyPairName: String,
    @SerializedName("comparison_type") val comparisonType: String,
    @SerializedName("price") val price: Double,
    @SerializedName("active") val active: Boolean
) : Parcelable