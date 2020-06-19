package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.Currency

interface CurrenciesData<
    CurrencyFetchingResult,
    CurrenciesFetchingResult
> {

    suspend fun save(currency: Currency)

    suspend fun save(currencies: List<Currency>)

    suspend fun deleteAll()

    suspend fun get(currencyId: Int): CurrencyFetchingResult

    suspend fun getAll(): CurrenciesFetchingResult

}