package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.TickerItem
import com.stocksexchange.android.database.model.DatabaseTickerItem.Companion.TABLE_NAME

/**
 * A Room database model for the [TickerItem] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseTickerItem(
    @PrimaryKey @ColumnInfo(name = ID) var id: Int,
    @ColumnInfo(name = BASE_CURRENCY_SYMBOL) var baseCurrencySymbol: String,
    @ColumnInfo(name = BASE_CURRENCY_NAME) var baseCurrencyName: String,
    @ColumnInfo(name = QUOTE_CURRENCY_SYMBOL) var quoteCurrencySymbol: String,
    @ColumnInfo(name = QUOTE_CURRENCY_NAME) var quoteCurrencyName: String,
    @ColumnInfo(name = NAME) var name: String,
    @ColumnInfo(name = BEST_ASK_PRICE) var bestAskPrice: Double?,
    @ColumnInfo(name = BEST_BID_PRICE) var bestBidPrice: Double?,
    @ColumnInfo(name = LAST_PRICE) var lastPrice: Double?,
    @ColumnInfo(name = OPEN_PRICE) var openPrice: Double?,
    @ColumnInfo(name = LOW_PRICE) var lowPrice: Double?,
    @ColumnInfo(name = HIGH_PRICE) var highPrice: Double?,
    @ColumnInfo(name = DAILY_VOLUME_IN_BASE_CURRENCY) var dailyVolumeInBaseCurrency: Double?,
    @ColumnInfo(name = DAILY_VOLUME_IN_QUOTE_CURRENCY) var dailyVolumeInQuoteCurrency: Double?,
    @ColumnInfo(name = FIAT_CURRENCY_RATES) var fiatCurrencyRates: Map<String, Double>,
    @ColumnInfo(name = TIMESTAMP) var timestamp: Long
) {


    companion object {

        const val TABLE_NAME = "ticker_items"

        const val ID = "id"
        const val BASE_CURRENCY_SYMBOL = "base_currency_symbol"
        const val BASE_CURRENCY_NAME = "base_currency_name"
        const val QUOTE_CURRENCY_SYMBOL = "quote_currency_symbol"
        const val QUOTE_CURRENCY_NAME = "quote_currency_name"
        const val NAME = "name"
        const val BEST_ASK_PRICE = "best_ask_price"
        const val BEST_BID_PRICE = "best_bid_price"
        const val LAST_PRICE = "last_price"
        const val OPEN_PRICE = "open_price"
        const val LOW_PRICE = "low_price"
        const val HIGH_PRICE = "high_price"
        const val DAILY_VOLUME_IN_BASE_CURRENCY = "daily_volume_in_base_currency"
        const val DAILY_VOLUME_IN_QUOTE_CURRENCY = "daily_volume_in_quote_currency"
        const val FIAT_CURRENCY_RATES = "fiat_currency_rates"
        const val TIMESTAMP = "timestamp"

    }


    constructor(): this(
        id = -1,
        baseCurrencySymbol = "",
        baseCurrencyName = "",
        quoteCurrencySymbol = "",
        quoteCurrencyName = "",
        name = "",
        bestAskPrice = null,
        bestBidPrice = null,
        lastPrice = null,
        openPrice = null,
        lowPrice = null,
        highPrice = null,
        dailyVolumeInBaseCurrency = null,
        dailyVolumeInQuoteCurrency = null,
        fiatCurrencyRates = mapOf(),
        timestamp = -1L
    )

}