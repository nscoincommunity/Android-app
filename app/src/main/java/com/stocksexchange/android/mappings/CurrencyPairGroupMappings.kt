package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.android.database.model.DatabaseCurrencyPairGroup

fun CurrencyPairGroup.mapToDatabaseCurrencyPairGroup(): DatabaseCurrencyPairGroup {
    return DatabaseCurrencyPairGroup(
        id = id,
        name = name,
        position = position
    )
}


fun List<CurrencyPairGroup>.mapToDatabaseCurrencyPairGroupList(): List<DatabaseCurrencyPairGroup> {
    return map { it.mapToDatabaseCurrencyPairGroup() }
}


fun DatabaseCurrencyPairGroup.mapToCurrencyPairGroup(): CurrencyPairGroup {
    return CurrencyPairGroup(
        id = id,
        name = name,
        position = position
    )
}


fun List<DatabaseCurrencyPairGroup>.mapToCurrencyPairGroupList(): List<CurrencyPairGroup> {
    return map { it.mapToCurrencyPairGroup() }
}