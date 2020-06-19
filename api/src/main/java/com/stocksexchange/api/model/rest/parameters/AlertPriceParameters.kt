package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.core.utils.interfaces.HasUniqueKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlertPriceParameters(
    val currencyPairId: Int,
    val comparison: String,
    val price: String
) : Parcelable, HasUniqueKey {


    companion object {

        fun getDefaultParameters(): AlertPriceParameters {
            return AlertPriceParameters(
                currencyPairId = -1,
                comparison = "",
                price = ""
            )
        }

    }


    override val uniqueKey: String
        get() = currencyPairId.toString()


}