package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyPair(
    @SerializedName("id") val id: Int,
    @SerializedName("currency_id") val baseCurrencyId: Int,
    @SerializedName("currency_code") val baseCurrencySymbol: String,
    @SerializedName("currency_name") val baseCurrencyName: String,
    @SerializedName("market_currency_id") val quoteCurrencyId: Int,
    @SerializedName("market_code") val quoteCurrencySymbol: String,
    @SerializedName("market_name") val quoteCurrencyName: String,
    @SerializedName("symbol") val name: String,
    @SerializedName("group_name") val groupName: String,
    @SerializedName("group_id") val groupId: Int,
    @SerializedName("min_order_amount") val minOrderAmount: Double,
    @SerializedName("min_buy_price") val minBuyPrice: Double,
    @SerializedName("min_sell_price") val minSellPrice: Double,
    @SerializedName("buy_fee_percent") val buyFeeInPercentage: Double,
    @SerializedName("sell_fee_percent") val sellFeeInPercentage: Double,
    @SerializedName("active") val isActive: Boolean,
    @SerializedName("delisted") val isDelisted: Boolean,
    @SerializedName("pair_message") val message: String? = null,
    @SerializedName("currency_precision") val baseCurrencyPrecision: Int,
    @SerializedName("market_precision") val quoteCurrencyPrecision: Int
) : Parcelable {


    companion object {

        const val CURRENCY_PAIR_ALL_CODE = "ALL"

        val STUB_CURRENCY_PAIR = CurrencyPair(
            id = -1,
            baseCurrencyId = -1,
            baseCurrencySymbol = "",
            baseCurrencyName = "",
            quoteCurrencyId = -1,
            quoteCurrencySymbol = "",
            quoteCurrencyName = "",
            name = "",
            groupName = "",
            groupId = -1,
            minOrderAmount = 0.0,
            minBuyPrice = 0.0,
            minSellPrice = 0.0,
            buyFeeInPercentage = 0.0,
            sellFeeInPercentage = 0.0,
            isActive = false,
            isDelisted = false,
            message = null,
            baseCurrencyPrecision = 0,
            quoteCurrencyPrecision = 0
        )

    }


    val hasMessage: Boolean
        get() = ((message != null) && message.isNotBlank())


    val isStub: Boolean
        get() = (this == STUB_CURRENCY_PAIR)


    val buyFee: Double
        get() = (buyFeeInPercentage / 100.0)


    val sellFee: Double
        get() = (sellFeeInPercentage / 100.0)


}