package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderbookOrder(
    @SerializedName("price") val price: Double,
    @SerializedName("amount") val baseCurrencyAmount: Double,
    @SerializedName("amount2") val quoteCurrencyAmount: Double
) : Parcelable {


    companion object {

        private const val STUB_ORDERBOOK_FIELD_VALUE = -1.0


        val STUB_ORDERBOOK_ORDER = OrderbookOrder(
            price = STUB_ORDERBOOK_FIELD_VALUE,
            baseCurrencyAmount = STUB_ORDERBOOK_FIELD_VALUE,
            quoteCurrencyAmount = STUB_ORDERBOOK_FIELD_VALUE
        )

    }


    val isStub: Boolean
        get() = (
            (price == STUB_ORDERBOOK_FIELD_VALUE) &&
            (baseCurrencyAmount == STUB_ORDERBOOK_FIELD_VALUE) &&
            (quoteCurrencyAmount == STUB_ORDERBOOK_FIELD_VALUE)
        )


    /**
     * A field that returns amount of the order.
     * Defaults to base currency amount.
     */
    val amount: Double
        get() = baseCurrencyAmount


}