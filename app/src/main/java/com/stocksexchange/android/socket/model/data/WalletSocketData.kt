package com.stocksexchange.android.socket.model.data

import com.google.gson.annotations.SerializedName

data class WalletSocketData(
    @SerializedName("id") val id: Long,
    @SerializedName("currency_code") val currencyCode: String,
    @SerializedName("balance") val currentBalance: Double,
    @SerializedName("frozen_balance") val frozenBalance: Double,
    @SerializedName("bonus_balance") val bonusBalance: Double
) {


    val totalBalance: Double
        get() = (currentBalance + frozenBalance + bonusBalance)


}