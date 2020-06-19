package com.stocksexchange.android.data.repositories.favoritecurrencypairs

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.data.stores.favoritecurrencypairs.FavoriteCurrencyPairsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.android.data.stores.favoritecurrencypairs.FavoriteCurrencyPairsCacheDataStore

class FavoriteCurrencyPairsRepositoryImpl(
    private val databaseDataStore: FavoriteCurrencyPairsDataStore,
    private val cacheDataStore: FavoriteCurrencyPairsCacheDataStore
) : FavoriteCurrencyPairsRepository {


    @Synchronized
    override suspend fun favorite(currencyPair: CurrencyPair) {
        databaseDataStore.favorite(currencyPair)
        cacheDataStore.favorite(currencyPair)
    }


    @Synchronized
    override suspend fun unfavorite(currencyPair: CurrencyPair) {
        databaseDataStore.unfavorite(currencyPair)
        cacheDataStore.unfavorite(currencyPair)
    }


    @Synchronized
    override suspend fun isCurrencyPairFavorite(currencyPair: CurrencyPair): Boolean {
        val favoriteCurrencyPairIdsResult = getAll()

        if(favoriteCurrencyPairIdsResult.isErroneous()) {
            return false
        }

        val favoriteCurrencyPairIds = favoriteCurrencyPairIdsResult.getSuccessfulResultValue()

        for(favoriteCurrencyPairId in favoriteCurrencyPairIds) {
            if(currencyPair.id == favoriteCurrencyPairId) {
                return true
            }
        }

        return false
    }


    @Synchronized
    override suspend fun getCount(): RepositoryResult<Int> {
        return RepositoryResult(databaseResult = databaseDataStore.getCount())
    }


    @Synchronized
    override suspend fun getAll(): RepositoryResult<List<Int>> {
        cacheDataStore.getAll().also {
            if(it is Result.Success) {
                return@getAll RepositoryResult(cacheResult = it)
            }
        }

        val favoriteCurrencyPairIdsDbResult = databaseDataStore.getAll()

        return RepositoryResult(databaseResult = when(favoriteCurrencyPairIdsDbResult) {
            is Result.Success -> favoriteCurrencyPairIdsDbResult.also {
                cacheDataStore.saveAll(it.value)
            }

            is Result.Failure -> favoriteCurrencyPairIdsDbResult
        })
    }


}