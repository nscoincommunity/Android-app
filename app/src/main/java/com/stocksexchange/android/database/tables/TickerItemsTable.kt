package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.TickerItem
import com.stocksexchange.android.database.daos.TickerItemDao
import com.stocksexchange.android.database.model.DatabaseTickerItem
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToDatabaseTickerItem
import com.stocksexchange.android.mappings.mapToDatabaseTickerItemList
import com.stocksexchange.android.mappings.mapToTickerItem
import com.stocksexchange.android.mappings.mapToTickerItemList
import com.stocksexchange.core.model.Result

class TickerItemsTable(
    private val tickerItemDao: TickerItemDao
) : BaseTable() {


    @Synchronized
    fun save(tickerItem: TickerItem) {
        tickerItemDao.insert(tickerItem.mapToDatabaseTickerItem())
    }


    @Synchronized
    fun save(tickerItems: List<TickerItem>) {
        tickerItemDao.insert(tickerItems.mapToDatabaseTickerItemList())
    }


    @Synchronized
    fun deleteAll() {
        tickerItemDao.deleteAll()
    }


    @Synchronized
    fun get(currencyPairId: Int): Result<TickerItem> {
        return tickerItemDao.get(currencyPairId).toResult(
            DatabaseTickerItem::mapToTickerItem
        )
    }


    @Synchronized
    fun getAll(): Result<List<TickerItem>> {
        return tickerItemDao.getAll().toResult(
            List<DatabaseTickerItem>::mapToTickerItemList
        )
    }


}