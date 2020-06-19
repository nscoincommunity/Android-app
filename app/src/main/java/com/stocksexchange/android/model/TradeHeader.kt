package com.stocksexchange.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TradeHeader(
    val id: Long,
    val amountTitleText: String,
    val priceTitleText: String,
    val timeTitleText: String
) : Parcelable