package com.stocksexchange.android.data.stores.tickeritems

import com.stocksexchange.api.model.rest.TickerItem
import com.stocksexchange.android.database.tables.TickerItemsTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class TickerItemsDatabaseDataStore(
    private val tickerItemsTable: TickerItemsTable
) : TickerItemsDataStore {


    override suspend fun save(tickerItem: TickerItem) {
        executeBackgroundOperation {
            tickerItemsTable.save(tickerItem)
        }
    }


    override suspend fun save(tickerItems: List<TickerItem>) {
        executeBackgroundOperation {
            tickerItemsTable.save(tickerItems)
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            tickerItemsTable.deleteAll()
        }
    }


    override suspend fun get(currencyPairId: Int): Result<TickerItem> {
        return performBackgroundOperation {
            tickerItemsTable.get(currencyPairId)
        }
    }


    override suspend fun getAll(): Result<List<TickerItem>> {
        return performBackgroundOperation {
            tickerItemsTable.getAll()
        }
    }


}