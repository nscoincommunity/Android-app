package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.database.daos.CurrencyPairDao
import com.stocksexchange.android.database.model.DatabaseCurrencyPair
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToCurrencyPair
import com.stocksexchange.android.mappings.mapToCurrencyPairList
import com.stocksexchange.android.mappings.mapToDatabaseCurrencyPair
import com.stocksexchange.android.mappings.mapToDatabaseCurrencyPairList
import com.stocksexchange.core.model.Result

class CurrencyPairsTable(
    private val currencyPairsDao: CurrencyPairDao
) : BaseTable() {


    @Synchronized
    fun save(currencyPair: CurrencyPair) {
        currencyPairsDao.insert(currencyPair.mapToDatabaseCurrencyPair())
    }


    @Synchronized
    fun save(currencyPairs: List<CurrencyPair>) {
        currencyPairsDao.insert(currencyPairs.mapToDatabaseCurrencyPairList())
    }


    @Synchronized
    fun deleteAll() {
        currencyPairsDao.deleteAll()
    }


    @Synchronized
    fun search(query: String): Result<List<CurrencyPair>> {
        return currencyPairsDao.search(query).toResult(
            List<DatabaseCurrencyPair>::mapToCurrencyPairList
        )
    }


    @Synchronized
    fun get(currencyPairId: Int): Result<CurrencyPair> {
        return currencyPairsDao.get(currencyPairId).toResult(
            DatabaseCurrencyPair::mapToCurrencyPair
        )
    }


    @Synchronized
    fun getAll(): Result<List<CurrencyPair>> {
        return currencyPairsDao.getAll().toResult(
            List<DatabaseCurrencyPair>::mapToCurrencyPairList
        )
    }


}