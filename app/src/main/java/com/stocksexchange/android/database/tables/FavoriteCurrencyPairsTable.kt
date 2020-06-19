package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.database.daos.FavoriteCurrencyPairDao
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToDatabaseFavoriteCurrencyPair
import com.stocksexchange.core.model.Result

class FavoriteCurrencyPairsTable(
    private val favoriteCurrencyPairDao: FavoriteCurrencyPairDao
) : BaseTable() {


    @Synchronized
    fun favorite(currencyPair: CurrencyPair) {
        favoriteCurrencyPairDao.insert(currencyPair.mapToDatabaseFavoriteCurrencyPair())
    }


    @Synchronized
    fun unfavorite(currencyPair: CurrencyPair) {
        favoriteCurrencyPairDao.delete(currencyPair.id)
    }


    @Synchronized
    fun getCount(): Result<Int> {
        return Result.Success(favoriteCurrencyPairDao.getCount())
    }


    @Synchronized
    fun getAll(): Result<List<Int>> {
        return favoriteCurrencyPairDao.getAll().toResult()
    }


}