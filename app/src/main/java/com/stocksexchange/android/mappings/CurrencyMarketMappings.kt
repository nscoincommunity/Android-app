package com.stocksexchange.android.mappings

import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketItem
import com.stocksexchange.api.model.rest.CurrencyMarket

fun CurrencyMarket.mapToCurrencyMarketItem(): CurrencyMarketItem {
    return CurrencyMarketItem(this)
}


fun List<CurrencyMarket>.mapToCurrencyMarketItemList(): List<CurrencyMarketItem> {
    return map { it.mapToCurrencyMarketItem() }
}


fun List<CurrencyMarket>.mapToPairIdMarketMap(): Map<Int, CurrencyMarket> {
    return mutableMapOf<Int, CurrencyMarket>().apply {
        for(item in this@mapToPairIdMarketMap) {
            put(item.pairId, item)
        }
    }
}