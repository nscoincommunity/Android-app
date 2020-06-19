package com.stocksexchange.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderbookHeader(
    val title: String,
    val type: OrderbookOrderType
) : Parcelable