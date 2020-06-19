package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.CurrencyPair

interface FavoriteCurrencyPairsData<
    CountFetchingResult,
    CurrencyPairsFetchingResult
> {

    suspend fun favorite(currencyPair: CurrencyPair)

    suspend fun unfavorite(currencyPair: CurrencyPair)

    suspend fun isCurrencyPairFavorite(currencyPair: CurrencyPair): Boolean

    suspend fun getCount(): CountFetchingResult

    suspend fun getAll(): CurrencyPairsFetchingResult

}