package com.stocksexchange.android.data.stores.tickeritems

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.TickerItem
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class TickerItemsServerDataStore(
    private val stexRestApi: StexRestApi
) : TickerItemsDataStore {


    override suspend fun save(tickerItem: TickerItem) {
        throw UnsupportedOperationException()
    }


    override suspend fun save(tickerItems: List<TickerItem>) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun get(currencyPairId: Int): Result<TickerItem> {
        return performBackgroundOperation {
            stexRestApi.getTickerItem(currencyPairId)
        }
    }


    override suspend fun getAll(): Result<List<TickerItem>> {
        return performBackgroundOperation {
            stexRestApi.getTickerItems()
        }
    }


}