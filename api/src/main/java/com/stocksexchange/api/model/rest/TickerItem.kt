package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TickerItem(
    @SerializedName("id") val id: Int,
    @SerializedName("currency_code") val baseCurrencySymbol: String,
    @SerializedName("currency_name") val baseCurrencyName: String,
    @SerializedName("market_code") val quoteCurrencySymbol: String,
    @SerializedName("market_name") val quoteCurrencyName: String,
    @SerializedName("symbol") val name: String,
    @SerializedName("ask") val _bestAskPrice: Double?,
    @SerializedName("bid") val _bestBidPrice: Double?,
    @SerializedName("last") val _lastPrice: Double?,
    @SerializedName("open") val _openPrice: Double?,
    @SerializedName("low") val _lowPrice: Double?,
    @SerializedName("high") val _highPrice: Double?,
    @SerializedName("volumeQuote") val _dailyVolumeInBaseCurrency: Double?,
    @SerializedName("volume") val _dailyVolumeInQuoteCurrency: Double?,
    @SerializedName("fiatsRate") val fiatCurrencyRates: Map<String, Double>,
    @SerializedName("timestamp") val timestamp: Long
) : Parcelable {


    companion object {

        val STUB_TICKER_ITEM = TickerItem(
            id = -1,
            baseCurrencySymbol = "",
            baseCurrencyName = "",
            quoteCurrencySymbol = "",
            quoteCurrencyName = "",
            name = "",
            _bestAskPrice = null,
            _bestBidPrice = null,
            _lastPrice = null,
            _openPrice = null,
            _lowPrice = null,
            _highPrice = null,
            _dailyVolumeInBaseCurrency = null,
            _dailyVolumeInQuoteCurrency = null,
            fiatCurrencyRates = mapOf(),
            timestamp = -1L
        )

    }


    val dailyPriceChange: Double
        get() = if(hasLastPrice && hasOpenPrice) {
            (lastPrice - openPrice)
        } else {
            0.0
        }


    val dailyPriceChangeInPercentage: Double
        get() = if(hasLastPrice && hasOpenPrice && (openPrice != 0.0)) {
            ((dailyPriceChange / openPrice) * 100.0)
        } else {
            0.0
        }


    val hasLastPrice: Boolean
        get() = (_lastPrice != null)


    val hasOpenPrice: Boolean
        get() = (_openPrice != null)


    val isStub: Boolean
        get() = (this == STUB_TICKER_ITEM)


    val bestAskPrice: Double
        get() = (_bestAskPrice ?: 0.0)


    val bestBidPrice: Double
        get() = (_bestBidPrice ?: 0.0)


    val lastPrice: Double
        get() = (_lastPrice ?: 0.0)


    val openPrice: Double
        get() = (_openPrice ?: 0.0)


    val lowPrice: Double
        get() = (_lowPrice ?: 0.0)


    val highPrice: Double
        get() = (_highPrice ?: 0.0)


    val dailyVolumeInBaseCurrency: Double
        get() = (_dailyVolumeInBaseCurrency ?: 0.0)


    val dailyVolumeInQuoteCurrency: Double
        get() = (_dailyVolumeInQuoteCurrency ?: 0.0)


}