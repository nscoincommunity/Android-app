package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.Currency
import com.stocksexchange.android.database.daos.CurrencyDao
import com.stocksexchange.android.database.model.DatabaseCurrency
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToCurrency
import com.stocksexchange.android.mappings.mapToCurrencyList
import com.stocksexchange.android.mappings.mapToDatabaseCurrency
import com.stocksexchange.android.mappings.mapToDatabaseCurrencyList
import com.stocksexchange.core.model.Result

class CurrenciesTable(
    private val currencyDao: CurrencyDao
) : BaseTable() {


    @Synchronized
    fun save(currency: Currency) {
        currencyDao.insert(currency.mapToDatabaseCurrency())
    }


    @Synchronized
    fun save(currencies: List<Currency>) {
        currencyDao.insert(currencies.mapToDatabaseCurrencyList())
    }


    @Synchronized
    fun deleteAll() {
        currencyDao.deleteAll()
    }


    @Synchronized
    fun get(currencyId: Int): Result<Currency> {
        return currencyDao.get(currencyId).toResult(DatabaseCurrency::mapToCurrency)
    }


    @Synchronized
    fun getAll(): Result<List<Currency>> {
        return currencyDao.getAll().toResult(List<DatabaseCurrency>::mapToCurrencyList)
    }


}