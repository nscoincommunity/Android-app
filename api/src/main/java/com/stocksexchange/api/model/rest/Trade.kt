package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trade(
    @SerializedName("id") val id: Long,
    @SerializedName("price") val price: Double,
    @SerializedName("amount") val amount: Double,
    @SerializedName("type") val typeStr: String,
    @SerializedName("timestamp") val timestamp: Long // in seconds
) : Parcelable, Comparable<Trade> {


    companion object {

        private const val STUB_TRADE_ID = -1L


        val STUB_BUY_TRADE = Trade(
            id = STUB_TRADE_ID,
            timestamp = 0L,
            price = -1.0,
            amount = -1.0,
            typeStr = TradeType.BUY.name
        )

        val STUB_SELL_TRADE = Trade(
            id = STUB_TRADE_ID,
            timestamp = 0L,
            price = -1.0,
            amount = -1.0,
            typeStr = TradeType.SELL.name
        )

    }


    val isStub: Boolean
        get() = (id == STUB_TRADE_ID)


    val timestampInMillis: Long
        get() = (timestamp * 1000L)


    val type: TradeType
        get() = TradeType.newInstance(typeStr)




    override fun compareTo(other: Trade): Int {
        return when {
            (timestamp > other.timestamp) -> 1
            (timestamp < other.timestamp) -> -1
            else -> 0
        }
    }


}