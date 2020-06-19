package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.api.model.rest.SortOrder
import com.stocksexchange.core.utils.interfaces.HasUniqueKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TradeHistoryParameters(
    val currencyPairId: Int,
    val count: Int,
    val fromTimestamp: Long,    // in seconds
    val tillTimestamp: Long,    // in seconds
    val sortOrder: SortOrder
) : Parcelable, HasUniqueKey {


    companion object {

        fun getDefaultParameters(): TradeHistoryParameters {

            return TradeHistoryParameters(
                currencyPairId = -1,
                count = 100,
                fromTimestamp = -1L,
                tillTimestamp = -1L,
                sortOrder = SortOrder.DESC
            )
        }


        fun getDefaultParameters(currencyPairId: Int): TradeHistoryParameters {
            return getDefaultParameters().copy(currencyPairId = currencyPairId)
        }

    }


    override val uniqueKey: String
        get() = currencyPairId.toString()


}