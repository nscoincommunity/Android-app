package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.TickerItem
import com.stocksexchange.android.database.model.DatabaseTickerItem

fun TickerItem.mapToDatabaseTickerItem(): DatabaseTickerItem {
    return DatabaseTickerItem(
        id = id,
        baseCurrencySymbol = baseCurrencySymbol,
        baseCurrencyName = baseCurrencyName,
        quoteCurrencySymbol = quoteCurrencySymbol,
        quoteCurrencyName = quoteCurrencyName,
        name = name,
        bestAskPrice = _bestAskPrice,  // Needs to be saved in the raw state
        bestBidPrice = _bestBidPrice,  // Needs to be saved in the raw state
        lastPrice = _lastPrice, // Needs to be saved in the raw state
        openPrice = _openPrice, // Needs to be saved in the raw state
        lowPrice = _lowPrice,   // Needs to be saved in the raw state
        highPrice = _highPrice, // Needs to be saved in the raw state
        dailyVolumeInBaseCurrency = _dailyVolumeInBaseCurrency, // Needs to be saved in the raw state
        dailyVolumeInQuoteCurrency = _dailyVolumeInQuoteCurrency,   // Needs to be saved in the raw state
        fiatCurrencyRates = fiatCurrencyRates,
        timestamp = timestamp
    )
}


fun List<TickerItem>.mapToDatabaseTickerItemList(): List<DatabaseTickerItem> {
    return map { it.mapToDatabaseTickerItem() }
}


fun List<TickerItem>.mapToIdTickerItemMap(): Map<Int, TickerItem> {
    return mutableMapOf<Int, TickerItem>().apply {
        for(item in this@mapToIdTickerItemMap) {
            put(item.id, item)
        }
    }
}


fun DatabaseTickerItem.mapToTickerItem(): TickerItem {
    return TickerItem(
        id = id,
        baseCurrencySymbol = baseCurrencySymbol,
        baseCurrencyName = baseCurrencyName,
        quoteCurrencySymbol = quoteCurrencySymbol,
        quoteCurrencyName = quoteCurrencyName,
        name = name,
        _bestAskPrice = bestAskPrice,
        _bestBidPrice = bestBidPrice,
        _lastPrice = lastPrice,
        _openPrice = openPrice,
        _lowPrice = lowPrice,
        _highPrice = highPrice,
        _dailyVolumeInBaseCurrency = dailyVolumeInBaseCurrency,
        _dailyVolumeInQuoteCurrency = dailyVolumeInQuoteCurrency,
        fiatCurrencyRates = fiatCurrencyRates,
        timestamp = timestamp
    )
}


fun List<DatabaseTickerItem>.mapToTickerItemList(): List<TickerItem> {
    return map { it.mapToTickerItem() }
}