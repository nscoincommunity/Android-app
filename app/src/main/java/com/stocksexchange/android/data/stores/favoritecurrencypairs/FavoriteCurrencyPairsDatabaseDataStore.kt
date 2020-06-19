package com.stocksexchange.android.data.stores.favoritecurrencypairs

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.database.tables.FavoriteCurrencyPairsTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class FavoriteCurrencyPairsDatabaseDataStore(
    private val favoriteCurrencyPairsTable: FavoriteCurrencyPairsTable
) : FavoriteCurrencyPairsDataStore {


    override suspend fun favorite(currencyPair: CurrencyPair) {
        executeBackgroundOperation {
            favoriteCurrencyPairsTable.favorite(currencyPair)
        }
    }


    override suspend fun unfavorite(currencyPair: CurrencyPair) {
        executeBackgroundOperation {
            favoriteCurrencyPairsTable.unfavorite(currencyPair)
        }
    }


    override suspend fun isCurrencyPairFavorite(currencyPair: CurrencyPair): Boolean {
        throw UnsupportedOperationException()
    }


    override suspend fun getCount(): Result<Int> {
        return performBackgroundOperation { 
            favoriteCurrencyPairsTable.getCount()
        }
    }


    override suspend fun saveAll(ids: List<Int>) {
        throw UnsupportedOperationException()
    }


    override suspend fun getAll(): Result<List<Int>> {
        return performBackgroundOperation {
            favoriteCurrencyPairsTable.getAll()
        }
    }


}