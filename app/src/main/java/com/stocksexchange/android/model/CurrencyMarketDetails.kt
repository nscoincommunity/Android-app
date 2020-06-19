package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.TradingFees
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyMarketDetails(
    val currencyMarket: CurrencyMarket,
    val tradingFees: TradingFees
) : Parcelable {


    val isActive: Boolean
        get() = currencyMarket.isActive


    val lowPrice: Double
        get() = currencyMarket.lowPrice


    val highPrice: Double
        get() = currencyMarket.highPrice


    val dailyVolumeInBaseCurrency: Double
        get() = currencyMarket.dailyVolumeInBaseCurrency


    val buyFeeInPercentage: Double
        get() = tradingFees.buyFeeInPercentage


    val sellFeeInPercentage: Double
        get() = tradingFees.sellFeeInPercentage


    val baseCurrencySymbol: String
        get() = currencyMarket.baseCurrencySymbol


    val baseCurrencyName: String
        get() = currencyMarket.baseCurrencyName


    val quoteCurrencySymbol: String
        get() = currencyMarket.quoteCurrencySymbol


    val quoteCurrencyName: String
        get() = currencyMarket.quoteCurrencyName


}