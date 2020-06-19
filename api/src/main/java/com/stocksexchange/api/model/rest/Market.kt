package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Market(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String
) : Parcelable