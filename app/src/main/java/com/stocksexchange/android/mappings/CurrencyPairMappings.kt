package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.database.model.DatabaseCurrencyPair

fun CurrencyPair.mapToDatabaseCurrencyPair(): DatabaseCurrencyPair {
    return DatabaseCurrencyPair(
        id = id,
        baseCurrencyId = baseCurrencyId,
        baseCurrencySymbol = baseCurrencySymbol,
        baseCurrencyName = baseCurrencyName,
        quoteCurrencyId = quoteCurrencyId,
        quoteCurrencySymbol = quoteCurrencySymbol,
        quoteCurrencyName = quoteCurrencyName,
        name = name,
        groupName = groupName,
        groupId = groupId,
        minOrderAmount = minOrderAmount,
        minBuyPrice = minBuyPrice,
        minSellPrice = minSellPrice,
        buyFeeInPercentage = buyFeeInPercentage,
        sellFeeInPercentage = sellFeeInPercentage,
        isActive = isActive,
        isDelisted = isDelisted,
        message = message,
        baseCurrencyPrecision = baseCurrencyPrecision,
        quoteCurrencyPrecision = quoteCurrencyPrecision
    )
}


fun List<CurrencyPair>.mapToDatabaseCurrencyPairList(): List<DatabaseCurrencyPair> {
    return map { it.mapToDatabaseCurrencyPair() }
}


fun List<CurrencyPair>.mapToIdCurrencyPairMap(): Map<Int, CurrencyPair> {
    return mutableMapOf<Int, CurrencyPair>().apply {
        for(item in this@mapToIdCurrencyPairMap) {
            put(item.id, item)
        }
    }
}


fun DatabaseCurrencyPair.mapToCurrencyPair(): CurrencyPair {
    return CurrencyPair(
        id = id,
        baseCurrencyId = baseCurrencyId,
        baseCurrencySymbol = baseCurrencySymbol,
        baseCurrencyName = baseCurrencyName,
        quoteCurrencyId = quoteCurrencyId,
        quoteCurrencySymbol = quoteCurrencySymbol,
        quoteCurrencyName = quoteCurrencyName,
        name = name,
        groupName = groupName,
        groupId = groupId,
        minOrderAmount = minOrderAmount,
        minBuyPrice = minBuyPrice,
        minSellPrice = minSellPrice,
        buyFeeInPercentage = buyFeeInPercentage,
        sellFeeInPercentage = sellFeeInPercentage,
        isActive = isActive,
        isDelisted = isDelisted,
        message = message,
        baseCurrencyPrecision = baseCurrencyPrecision,
        quoteCurrencyPrecision = quoteCurrencyPrecision
    )
}


fun List<DatabaseCurrencyPair>.mapToCurrencyPairList(): List<CurrencyPair> {
    return map { it.mapToCurrencyPair() }
}