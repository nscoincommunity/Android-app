package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.CurrencyPair

interface CurrencyPairsData<
    CurrencyPairFetchingResult,
    CurrencyPairsFetchingResult
> {

    suspend fun save(currencyPair: CurrencyPair)

    suspend fun save(currencyPairs: List<CurrencyPair>)

    suspend fun deleteAll()

    suspend fun search(query: String): CurrencyPairsFetchingResult

    suspend fun get(currencyPairId: Int): CurrencyPairFetchingResult

    suspend fun getAll(): CurrencyPairsFetchingResult

}