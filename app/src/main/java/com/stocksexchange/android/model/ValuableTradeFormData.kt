package com.stocksexchange.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValuableTradeFormData(
    val price: String,
    val amount: String,
    val total: String,
    val seekBarProgress: Int
) : Parcelable {


    companion object {

        val STUB = ValuableTradeFormData(
            price = "",
            amount = "",
            total = "",
            seekBarProgress = 0
        )

    }


    val isStub: Boolean
        get() = (this == STUB)


}