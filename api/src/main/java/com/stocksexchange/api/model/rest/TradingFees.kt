package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TradingFees(
    @SerializedName("buy_fee") val buyFee: Double,
    @SerializedName("sell_fee") val sellFee: Double
) : Parcelable {


    val buyFeeInPercentage: Double
        get() = (buyFee * 100.0)


    val sellFeeInPercentage: Double
        get() = (sellFee * 100.0)


}