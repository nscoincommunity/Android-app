package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.core.utils.interfaces.HasUniqueKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderbookParameters(
    val currencyPairId: Int,
    val bidOrdersCount: Int,
    val askOrdersCount: Int
) : Parcelable, HasUniqueKey {


    companion object {


        fun getDefaultParameters(): OrderbookParameters {
            return OrderbookParameters(
                currencyPairId = -1,
                bidOrdersCount = Integer.MAX_VALUE,
                askOrdersCount = Integer.MAX_VALUE
            )
        }


        fun getDefaultParameters(currencyPairId: Int): OrderbookParameters {
            return getDefaultParameters().copy(currencyPairId = currencyPairId)
        }


    }


    override val uniqueKey: String
        get() = currencyPairId.toString()


}