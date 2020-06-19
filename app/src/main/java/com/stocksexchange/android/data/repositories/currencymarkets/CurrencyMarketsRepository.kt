package com.stocksexchange.android.data.repositories.currencymarkets

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.data.repositories.base.Repository

interface CurrencyMarketsRepository : Repository {

    suspend fun refresh()

    suspend fun save(currencyMarket: CurrencyMarket)

    suspend fun favorite(currencyMarket: CurrencyMarket)

    suspend fun unfavorite(currencyMarket: CurrencyMarket)

    suspend fun deleteAll()

    suspend fun search(query: String): RepositoryResult<List<CurrencyMarket>>

    suspend fun isCurrencyMarketFavorite(currencyMarket: CurrencyMarket): Boolean

    suspend fun getCurrencyMarket(pairId: Int): RepositoryResult<CurrencyMarket>

    suspend fun getCurrencyMarket(baseCurrencySymbol: String, quoteCurrencySymbol: String): RepositoryResult<CurrencyMarket>

    suspend fun getCurrencyMarkets(groupId: Int): RepositoryResult<List<CurrencyMarket>>

    suspend fun getFavoriteMarkets(): RepositoryResult<List<CurrencyMarket>>

    suspend fun getAll(): RepositoryResult<List<CurrencyMarket>>

}