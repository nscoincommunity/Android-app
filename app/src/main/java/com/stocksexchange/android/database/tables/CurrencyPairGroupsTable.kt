package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.android.database.daos.CurrencyPairGroupDao
import com.stocksexchange.android.database.model.DatabaseCurrencyPairGroup
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToCurrencyPairGroupList
import com.stocksexchange.android.mappings.mapToDatabaseCurrencyPairGroupList
import com.stocksexchange.core.model.Result

class CurrencyPairGroupsTable(
    private val currencyPairGroupDao: CurrencyPairGroupDao
) : BaseTable() {


    @Synchronized
    fun save(currencyPairGroups: List<CurrencyPairGroup>) {
        currencyPairGroupDao.insert(currencyPairGroups.mapToDatabaseCurrencyPairGroupList())
    }


    @Synchronized
    fun deleteAll() {
        currencyPairGroupDao.deleteAll()
    }


    @Synchronized
    fun getAll(): Result<List<CurrencyPairGroup>> {
        return currencyPairGroupDao.getAll().toResult(
            List<DatabaseCurrencyPairGroup>::mapToCurrencyPairGroupList
        )
    }


}