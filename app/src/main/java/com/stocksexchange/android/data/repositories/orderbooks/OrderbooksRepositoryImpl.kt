package com.stocksexchange.android.data.repositories.orderbooks

import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.android.data.stores.orderbooks.OrderbooksDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.OrderbookFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider

class OrderbooksRepositoryImpl(
    private val serverDataStore: OrderbooksDataStore,
    private val databaseDataStore: OrderbooksDataStore,
    private val freshDataHandler: OrderbookFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : OrderbooksRepository {


    @Synchronized
    override suspend fun refresh(params: OrderbookParameters) {
        freshDataHandler.refresh(params)
    }


    @Synchronized
    override suspend fun save(params: OrderbookParameters, orderbook: Orderbook) {
        databaseDataStore.save(params, orderbook)
    }


    @Synchronized
    override suspend fun get(params: OrderbookParameters): RepositoryResult<Orderbook> {
        val result = RepositoryResult<Orderbook>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider, params)) {
            result.serverResult = serverDataStore.get(params)

            if(result.isServerResultSuccessful()) {
                save(params, result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.get(params)
        }

        return result
    }


}