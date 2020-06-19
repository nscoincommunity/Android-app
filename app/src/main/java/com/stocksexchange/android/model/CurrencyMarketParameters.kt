package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.api.model.rest.CurrencyPairGroup
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyMarketParameters(
    val searchQuery: String,
    val currencyMarketType: CurrencyMarketType,
    val currencyPairGroup: CurrencyPairGroup
) : Parcelable {


    companion object {

        fun getDefaultParameters(): CurrencyMarketParameters {
            return CurrencyMarketParameters(
                searchQuery = "",
                currencyMarketType = CurrencyMarketType.NORMAL,
                currencyPairGroup = CurrencyPairGroup.STUB
            )
        }

    }


    val lowercasedSearchQuery: String
        get() = searchQuery.toLowerCase()


}