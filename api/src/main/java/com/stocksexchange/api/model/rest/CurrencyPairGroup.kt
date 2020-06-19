package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyPairGroup(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("position") val position: Int
) : Parcelable {


    companion object {

        val STUB = CurrencyPairGroup(
            id = -1,
            name = "",
            position = -1
        )


        val DEFAULT_GROUPS = listOf(
            CurrencyPairGroup(id = 1, name = "BTC", position = 1),
            CurrencyPairGroup(id = 3, name = "ETH", position = 2),
            CurrencyPairGroup(id = 4, name = "LTC", position = 3),
            CurrencyPairGroup(id = 6, name = "USDT", position = 4),
            CurrencyPairGroup(id = 16, name = "xEUR", position = 5),
            CurrencyPairGroup(id = 14, name = "Fiat", position = 6),
            CurrencyPairGroup(id = 15, name = "Custom", position = 7)
        )

    }


    val isStub: Boolean
        get() = (this == STUB)


}