package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.database.model.DatabaseFavoriteCurrencyPair

fun CurrencyPair.mapToDatabaseFavoriteCurrencyPair(): DatabaseFavoriteCurrencyPair {
    return DatabaseFavoriteCurrencyPair(
        id = id
    )
}