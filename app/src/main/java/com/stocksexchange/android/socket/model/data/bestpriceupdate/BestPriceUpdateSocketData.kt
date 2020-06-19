package com.stocksexchange.android.socket.model.data.bestpriceupdate

import com.google.gson.annotations.SerializedName

data class BestPriceUpdateSocketData(
    @SerializedName("best_price") val bestPrice: Double,
    @SerializedName("order_type_id") val orderTypeId: Int,
    @SerializedName("currency_pair_id") val currencyPairId: Int
) {


    companion object {

        private const val ORDER_TYPE_BID = 1
        private const val ORDER_TYPE_ASK = 2

    }




    val type: BestPriceUpdateType
        get() = when(orderTypeId) {
            ORDER_TYPE_BID -> BestPriceUpdateType.BID
            ORDER_TYPE_ASK -> BestPriceUpdateType.ASK

            else -> throw IllegalStateException("Unknown order type ID: $orderTypeId")
        }


}