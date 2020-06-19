package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.api.model.rest.ApiOrderCreationType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderCreationParameters(
    val currencyPairId: Int,
    val type: ApiOrderCreationType,
    val amount: String,
    val price: String,
    val stopPrice: String
) : Parcelable {


    companion object {

        val STUB_PARAMS = OrderCreationParameters(
            currencyPairId = -1,
            type = ApiOrderCreationType.UNKNOWN,
            amount = "",
            price = "",
            stopPrice = ""
        )

    }




    val isStub: Boolean
        get() = (this == STUB_PARAMS)


}