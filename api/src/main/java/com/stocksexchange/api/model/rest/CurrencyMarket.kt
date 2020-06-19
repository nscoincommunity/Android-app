package com.stocksexchange.api.model.rest

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A model class that acts a wrapper class combining
 * a currency pair and its ticker data.
 */
@Parcelize
data class CurrencyMarket(
    val currencyPair: CurrencyPair,
    val tickerItem: TickerItem
) : Parcelable {


    companion object {

        val STUB_CURRENCY_MARKET = CurrencyMarket(
            currencyPair = CurrencyPair.STUB_CURRENCY_PAIR,
            tickerItem = TickerItem.STUB_TICKER_ITEM
        )

    }


    val isActive: Boolean
        get() = currencyPair.isActive


    val isStub: Boolean
        get() = (this == STUB_CURRENCY_MARKET)


    val pairId: Int
        get() = currencyPair.id


    val baseCurrencyId: Int
        get() = currencyPair.baseCurrencyId


    val quoteCurrencyId: Int
        get() = currencyPair.quoteCurrencyId


    val groupId: Int
        get() = currencyPair.groupId


    val buyFee: Double
        get() = currencyPair.buyFee


    val sellFee: Double
        get() = currencyPair.sellFee


    val buyFeeInPercentage: Double
        get() = currencyPair.buyFeeInPercentage


    val sellFeeInPercentage: Double
        get() = currencyPair.sellFeeInPercentage


    val lowPrice: Double
        get() = tickerItem.lowPrice


    val highPrice: Double
        get() = tickerItem.highPrice


    val lastPrice: Double
        get() = tickerItem.lastPrice


    val bestBidPrice: Double
        get() = tickerItem.bestBidPrice


    val bestAskPrice: Double
        get() = tickerItem.bestAskPrice


    val dailyPriceChange: Double
        get() = tickerItem.dailyPriceChange


    val dailyPriceChangeInPercentage: Double
        get() = tickerItem.dailyPriceChangeInPercentage


    val dailyVolumeInBaseCurrency: Double
        get() = tickerItem.dailyVolumeInBaseCurrency


    val dailyVolumeInQuoteCurrency: Double
        get() = tickerItem.dailyVolumeInQuoteCurrency


    val minOrderAmount: Double
        get() = currencyPair.minOrderAmount


    val pairName: String
        get() = currencyPair.name


    val baseCurrencySymbol: String
        get() = currencyPair.baseCurrencySymbol


    val baseCurrencyName: String
        get() = currencyPair.baseCurrencyName


    val quoteCurrencySymbol: String
        get() = currencyPair.quoteCurrencySymbol


    val quoteCurrencyName: String
        get() = currencyPair.quoteCurrencyName


    val fiatCurrencyRates: Map<String, Double>
        get() = tickerItem.fiatCurrencyRates


}