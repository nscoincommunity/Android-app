package com.stocksexchange.android.data.stores.favoritecurrencypairs

import com.stocksexchange.android.data.base.FavoriteCurrencyPairsData
import com.stocksexchange.core.model.Result

interface FavoriteCurrencyPairsDataStore : FavoriteCurrencyPairsData<
    Result<Int>,
    Result<List<Int>>
> {

    suspend fun saveAll(ids: List<Int>)

}