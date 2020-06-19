package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.Order
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderData(
    val order: Order,
    val currencyMarket: CurrencyMarket
) : Parcelable