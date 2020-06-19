package com.stocksexchange.android.data.stores.favoritecurrencypairs

import com.stocksexchange.android.data.stores.base.BaseCacheDataStore
import com.stocksexchange.android.utils.helpers.addOrUpdateItemToCache
import com.stocksexchange.android.utils.helpers.removeItemFromCache
import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.core.exceptions.NotFoundException
import com.stocksexchange.core.model.Result

class FavoriteCurrencyPairsCacheDataStore : BaseCacheDataStore(), FavoriteCurrencyPairsDataStore {


    companion object {

        private const val KEY_FAVORITE_CURRENCY_PAIR_IDS = "favorite_currency_pair_ids"

    }




    private fun saveFavoriteCurrencyPairIds(ids: List<Int>) {
        cache.put(KEY_FAVORITE_CURRENCY_PAIR_IDS, ids)
    }


    @Suppress("UNCHECKED_CAST")
    private fun getFavoriteCurrencyPairIds(): List<Int> {
        return (cache.get(KEY_FAVORITE_CURRENCY_PAIR_IDS) as List<Int>)
    }


    private fun hasFavoriteCurrencyPairIds(): Boolean {
        return cache.contains(KEY_FAVORITE_CURRENCY_PAIR_IDS)
    }


    private fun saveFavoriteCurrencyPairId(id: Int) {
        addOrUpdateItemToCache(
            cache = this,
            item = id,
            areItemsPresent = { hasFavoriteCurrencyPairIds() },
            fetchItems = { getFavoriteCurrencyPairIds() },
            areEqual = { cacheItem, newItem ->
                cacheItem == newItem
            },
            getIndexToInsertAt = { cacheItems, _ ->
                cacheItems.size
            },
            saveCache = { saveFavoriteCurrencyPairIds(it) }
        )
    }


    private fun removeFavoriteCurrencyPairId(id: Int) {
        removeItemFromCache(
            cache = this,
            item = id,
            areItemsPresent = { hasFavoriteCurrencyPairIds() },
            fetchItems = { getFavoriteCurrencyPairIds() },
            areEqual = { cacheItem, newItem ->
                cacheItem == newItem
            },
            saveCache = { saveFavoriteCurrencyPairIds(it) }
        )
    }


    override suspend fun favorite(currencyPair: CurrencyPair) {
        saveFavoriteCurrencyPairId(currencyPair.id)
    }


    override suspend fun unfavorite(currencyPair: CurrencyPair) {
        removeFavoriteCurrencyPairId(currencyPair.id)
    }


    override suspend fun isCurrencyPairFavorite(currencyPair: CurrencyPair): Boolean {
        throw UnsupportedOperationException()
    }


    override suspend fun getCount(): Result<Int> {
        throw UnsupportedOperationException()
    }


    override suspend fun saveAll(ids: List<Int>) {
        saveFavoriteCurrencyPairIds(ids)
    }


    override suspend fun getAll(): Result<List<Int>> {
        return if(hasFavoriteCurrencyPairIds()) {
            Result.Success(getFavoriteCurrencyPairIds())
        } else {
            Result.Failure(NotFoundException())
        }
    }


}