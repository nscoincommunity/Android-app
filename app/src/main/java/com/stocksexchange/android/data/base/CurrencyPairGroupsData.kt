package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.CurrencyPairGroup

interface CurrencyPairGroupsData<
    CurrencyPairGroupsFetchingResult
> {

    suspend fun save(currencyPairGroups: List<CurrencyPairGroup>)

    suspend fun deleteAll()

    suspend fun getAll(): CurrencyPairGroupsFetchingResult

}