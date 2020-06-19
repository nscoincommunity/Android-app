package com.stocksexchange.android.data.repositories.tickeritems

import com.stocksexchange.api.model.rest.TickerItem
import com.stocksexchange.android.data.stores.tickeritems.TickerItemsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider

class TickerItemsRepositoryImpl(
    private val serverDataStore: TickerItemsDataStore,
    private val databaseDataStore: TickerItemsDataStore,
    private val freshDataHandler: SimpleFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : TickerItemsRepository {


    @Synchronized
    override suspend fun refresh() {
        freshDataHandler.refresh()
    }


    @Synchronized
    override suspend fun save(tickerItem: TickerItem) {
        databaseDataStore.save(tickerItem)
    }


    @Synchronized
    override suspend fun save(tickerItems: List<TickerItem>) {
        databaseDataStore.save(tickerItems)
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun get(currencyPairId: Int): RepositoryResult<TickerItem> {
        return RepositoryResult<TickerItem>().apply {
            databaseResult = databaseDataStore.get(currencyPairId)

            if(isDatabaseResultErroneous() && connectionProvider.isNetworkAvailable()) {
                serverResult = serverDataStore.get(currencyPairId)
            }
        }
    }


    @Synchronized
    override suspend fun getAll(): RepositoryResult<List<TickerItem>> {
        val result = RepositoryResult<List<TickerItem>>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider)) {
            result.serverResult = serverDataStore.getAll()

            if(result.isServerResultSuccessful()) {
                deleteAll()
                save(result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.getAll()
        }

        return result
    }


}